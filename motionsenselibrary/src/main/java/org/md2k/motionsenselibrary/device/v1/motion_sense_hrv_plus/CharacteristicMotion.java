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

import android.util.Log;

import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselibrary.device.Characteristics;
import org.md2k.motionsenselibrary.device.Data;
import org.md2k.motionsenselibrary.device.SensorType;

import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

class CharacteristicMotion extends Characteristics {
    private static final UUID CHARACTERISTICS = UUID.fromString("da39c921-1d81-48e2-9c68-d0ae4bbd351f");
    private static final int MAX_SEQUENCE_NUMBER = 1024;
    private double frequency;

    CharacteristicMotion(double frequency) {
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
                    long correctedTimestamp = correctTimeStamp(sequenceNumber, curTime, lastSequenceNumber[0], lastCorrectedTimestamp[0], frequency, MAX_SEQUENCE_NUMBER);
                    Data[] data = new Data[5];
                    data[0] = new Data(SensorType.ACCELEROMETER, correctedTimestamp, getAccelerometer(bytes));
                    data[1] = new Data(SensorType.QUATERNION, correctedTimestamp, getQuaternion(bytes));
                    data[2] = new Data(SensorType.PPG, correctedTimestamp, getPPG(bytes));
                    data[3] = new Data(SensorType.MOTION_RAW, curTime, getRaw(bytes));
                    data[4] = new Data(SensorType.MOTION_SEQUENCE_NUMBER, correctedTimestamp, new double[]{sequenceNumber});
                    lastCorrectedTimestamp[0] = correctedTimestamp;
                    lastSequenceNumber[0] = sequenceNumber;
                    return Observable.fromArray(data);
                });
    }

    private double[] getAccelerometer(byte[] bytes) {
        double[] sample = new double[3];
        sample[0] = convertAccelADCtoSI((short)((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff));
        sample[1] = convertAccelADCtoSI((short)((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff));
        sample[2] = convertAccelADCtoSI((short)((bytes[4] & 0xff) << 8) | (bytes[5] & 0xff));
        return sample;
    }
    private double convertQuaternionToSI(double x){
        Log.d("mag","x="+x);
        return (2.0*x)/(65535.0) - 1;
    }
    private double[] getQuaternion(byte[] bytes) {
        double[] sample = new double[3];
        sample[0] = convertQuaternionToSI((bytes[6] & 0xff) << 8 | (bytes[7] & 0xff));
        sample[1] = convertQuaternionToSI((bytes[8] & 0xff) << 8 | (bytes[9] & 0xff));
        sample[2] = convertQuaternionToSI((bytes[10] &0xff) << 8 | (bytes[11] & 0xff));
        return sample;
    }

    private int getSequenceNumber(byte[] data) {
        int y = (data[18] & 0x03);
        int x = (data[19] & 0xff);
        return (y << 8) | x;
    }

    private double[] getPPG(byte[] bytes) {
        double[] sample = new double[3];
        sample[0] = ((bytes[12] & 0xff)<<10) | ((bytes[13] & 0xff) <<2) | ((bytes[14] & 0xc0)>>6);
        sample[1] = ((bytes[14] & 0x3f)<<12) | ((bytes[15] & 0xff) <<4) | ((bytes[16] & 0xf0)>>4);
        sample[2] = ((bytes[16] & 0x0f)<<14) | ((bytes[17] & 0xff) <<6) | ((bytes[18] & 0xfc)>>2);
        return sample;
    }

    private  double convertAccelADCtoSI(double x) {
        return 2.0 * x / 16384;
    }

    private double[] getRaw(byte[] bytes) {
        double[] sample = new double[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            sample[i] = bytes[i];
        return sample;
    }
}
