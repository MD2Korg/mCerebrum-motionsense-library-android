package org.md2k.motionsenselibrary.device.v2;
/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselibrary.device.Characteristics;
import org.md2k.motionsenselibrary.device.Data;
import org.md2k.motionsenselibrary.device.SensorType;

import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class CharacteristicPpgNew extends Characteristics {
    private static final UUID CHARACTERISTICS = UUID.fromString("DA39C925-1D81-48E2-9C68-D0AE4BBD351F");
    private static final int MAX_SEQUENCE_NUMBER = 65535;
    private double frequency;
    public CharacteristicPpgNew(double frequency) {
        this.frequency = frequency;
    }

    @Override
    public Observable<Data> listen(RxBleConnection rxBleConnection) {
        final int[] lastSequenceNumber = {-1};
        final long[] lastCorrectedTimestamp = {-1};
        return getCharacteristicListener(rxBleConnection, CHARACTERISTICS)
                .flatMap((Function<byte[], Observable<Data>>) bytes -> {
                    long curTime = System.currentTimeMillis();
                    Data[] data = new Data[3];
                    int sequenceNumber = getSequenceNumber(bytes);
                    long correctedTimestamp = correctTimeStamp(sequenceNumber, curTime, lastSequenceNumber[0], lastCorrectedTimestamp[0], frequency, MAX_SEQUENCE_NUMBER);
                    data[0] = new Data(SensorType.PPG, correctedTimestamp, getPPG(bytes));
                    data[1] = new Data(SensorType.PPG_RAW, curTime, getRaw(bytes));
                    data[2] = new Data(SensorType.PPG_SEQUENCE_NUMBER, correctedTimestamp, new double[]{sequenceNumber});
                    lastCorrectedTimestamp[0] = correctedTimestamp;
                    lastSequenceNumber[0] = sequenceNumber;
                    return Observable.fromArray(data);
                });
    }

    /**
     * Infra-red 1: bytes 13-11
     * Infra-red 2: bytes 10-8
     * Green/Red 1: bytes 7-5
     * Green/Red 2: bytes 4-2
     * Counter: bytes 1-0
     */
    private double[] getPPG(byte[] bytes) {
        double[] sample = new double[4];
        sample[0] = convertPPGValues(bytes[0], bytes[1], bytes[2]);
        sample[1] = convertPPGValues(bytes[3], bytes[4], bytes[5]);
        sample[2] = convertPPGValues(bytes[6], bytes[7], bytes[8]);
        sample[3] = convertPPGValues(bytes[9], bytes[10], bytes[11]);
        return sample;
    }

    /**
     * each ppg dc value is of type 32-bit single-precision float sent over the channels in an
     * unsigned uInt8 array, floatCast of size 4. The format is little endian. So, for example, in
     * Channel Infra-red floatCast[0] corresponds to the MSB and floatCast[3] is the LSB. The
     * counter is also in little-endian form
     */
    private static int convertPPGValues(byte intCast2, byte intCast1, byte intCast0) {
        return (intCast2 << 16 | intCast1 << 8 | intCast0);
        //TODO: This needs testing.
    }


    private int getSequenceNumber(byte[] data) {
        return ((data[12] & 0xff) << 8) | (data[13] & 0xff);
    }

    private double[] getRaw(byte[] bytes) {
        double[] sample = new double[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            sample[i] = bytes[i];
        return sample;
    }
}
