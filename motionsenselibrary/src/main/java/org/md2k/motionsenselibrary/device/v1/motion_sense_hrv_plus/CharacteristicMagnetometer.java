package org.md2k.motionsenselibrary.device.v1.motion_sense_hrv_plus;
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

class CharacteristicMagnetometer extends Characteristics {
    private static final UUID CHARACTERISTICS = UUID.fromString("da39c924-1d81-48e2-9c68-d0ae4bbd351f");
    private static final int MAX_SEQUENCE_NUMBER = 1024;
    private double frequency;

    CharacteristicMagnetometer(double frequency) {
        this.frequency = frequency;
    }

    @Override
    public Observable<Data> listen(RxBleConnection rxBleConnection) {
        final int[] lastSequenceNumber = {-1};
        final long[] lastCorrectedTimestamp = {-1};
        return getCharacteristicListener(rxBleConnection, CHARACTERISTICS)
                .flatMap((Function<byte[], Observable<Data>>) bytes -> {
                    long curTime = System.currentTimeMillis();
                    int sequenceNumber = getSequenceNumber(bytes);
                    long correctedTimestamp = correctTimeStamp(sequenceNumber/2, curTime, lastSequenceNumber[0], lastCorrectedTimestamp[0], frequency, MAX_SEQUENCE_NUMBER/2);
                    Data[] data = new Data[5];
                    data[0] = new Data(SensorType.MAGNETOMETER, correctedTimestamp-(long)(1000.0/(frequency*2)), getMagnetometer1(bytes));
                    data[1] = new Data(SensorType.MAGNETOMETER, correctedTimestamp, getMagnetometer2(bytes));
                    data[2] = new Data(SensorType.MAGNETOMETER_SENSITIVITY, correctedTimestamp, getSensitivity(bytes));
                    data[3] = new Data(SensorType.MAGNETOMETER_RAW, curTime, getRaw(bytes));
                    data[4] = new Data(SensorType.MAGNETOMETER_SEQUENCE_NUMBER, correctedTimestamp, new double[]{sequenceNumber});
                    lastCorrectedTimestamp[0] = correctedTimestamp;
                    lastSequenceNumber[0] = sequenceNumber;
                    return Observable.fromArray(data);
                });
    }

    private double[] getMagnetometer1(byte[] bytes) {
        double[] sample = new double[3];
        double[] sensitivity = getSensitivity(bytes);
        sample[0] = convertADCtoSI((short)((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff), sensitivity[0]);
        sample[1] = convertADCtoSI((short)((bytes[4] & 0xff) << 8) | (bytes[5] & 0xff), sensitivity[1]);
        sample[2] = convertADCtoSI((short)((bytes[8] & 0xff) << 8) | (bytes[9] & 0xff), sensitivity[2]);
        return sample;
    }

    private double[] getMagnetometer2(byte[] bytes) {
        double[] sample = new double[3];
        double[] sensitivity = getSensitivity(bytes);
        sample[0] = convertADCtoSI((short)((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff), sensitivity[0]);
        sample[1] = convertADCtoSI((short)((bytes[6] & 0xff) << 8) | (bytes[7] & 0xff), sensitivity[1]);
        sample[2] = convertADCtoSI((short)((bytes[10] & 0xff) << 8) | (bytes[11] & 0xff), sensitivity[2]);
        return sample;
    }
    private double convertADCtoSI(double mag, double sensitivity){
        return mag*((sensitivity-128)*.5/128.0+1);
    }

    private double[] getSensitivity(byte[] bytes) {
        double[] sample = new double[3];
        sample[0] = bytes[12] & 0xff;
        sample[1] = bytes[13] & 0xff;
        sample[2] = bytes[14] & 0xff;
        return sample;
    }

    private int getSequenceNumber(byte[] data) {
        int y=(data[15] & 0x03);
        int x=(data[16] & 0xff);
        return (y<<8)| x;
    }
    private double[] getRaw(byte[] bytes) {
        double[] sample = new double[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            sample[i] = bytes[i];
        return sample;
    }

}
