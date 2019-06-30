package org.md2k.motionsenselibrary.device.motion_sense_hrv_plus_gen2;
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

class CharacteristicPPGFilteredDc extends Characteristics {
    private static final UUID CHARACTERISTICS = UUID.fromString("DA39C926-1D81-48E2-9C68-D0AE4BBD351F");
    private static final int MAX_SEQUENCE_NUMBER = 65536;

    private double frequency;

    CharacteristicPPGFilteredDc(double frequency) {
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
                    data[0] = new Data(SensorType.PPG_DC, correctedTimestamp, getFilteredPPG(bytes));
                    data[1] = new Data(SensorType.PPG_DC_RAW, curTime, getRaw(bytes));
                    data[2] = new Data(SensorType.PPG_DC_SEQUENCE_NUMBER, correctedTimestamp, new double[]{sequenceNumber});
                    lastCorrectedTimestamp[0] = correctedTimestamp;
                    lastSequenceNumber[0] = sequenceNumber;
                    return Observable.fromArray(data);
                });
    }

    /**
     * Infra-red 1: bytes 17-14
     * Infra-red 2: bytes 13-10
     * Green/Red 1: bytes 9-6
     * Green/Red 2: bytes 5-2
     * Counter: bytes 1-0
     */
    private double[] getFilteredPPG(byte[] bytes) {
        double[] sample = new double[4];
        sample[0] = convertFilteredPPGValues(bytes[0], bytes[1], bytes[2], bytes[3]);
        sample[1] = convertFilteredPPGValues(bytes[4], bytes[5], bytes[6], bytes[7]);
        sample[2] = convertFilteredPPGValues(bytes[8], bytes[9], bytes[10], bytes[11]);
        sample[3] = convertFilteredPPGValues(bytes[12], bytes[13], bytes[14], bytes[15]);
        return sample;
    }


    /**
     * each ppg dc value is of type 32-bit single-precision float sent over the channels in an
     * unsigned uInt8 array, floatCast of size 4. The format is little endian. So, for example, in
     * Channel Infra-red floatCast[0] corresponds to the MSB and floatCast[3] is the LSB. The
     * counter is also in little-endian form
     */
    private static double convertFilteredPPGValues(byte floatCast3, byte floatCast2, byte floatCast1, byte floatCast0) {

        return (double) (floatCast0 << 24 | floatCast1 << 16 | floatCast2 << 8 | floatCast3); //TODO: This needs testing.
    }


    private int getSequenceNumber(byte[] data) {
        return ((data[data.length - 2] & 0xff) << 8) | (data[data.length - 1] & 0xff);
    }

    private double[] getRaw(byte[] bytes) {
        double[] sample = new double[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            sample[i] = bytes[i];
        return sample;
    }
}
