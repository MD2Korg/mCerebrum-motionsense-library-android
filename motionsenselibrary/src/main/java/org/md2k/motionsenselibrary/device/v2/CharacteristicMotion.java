package org.md2k.motionsenselibrary.device.v2;

import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselibrary.device.Characteristics;
import org.md2k.motionsenselibrary.device.Data;
import org.md2k.motionsenselibrary.device.SensorType;

import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

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
public class CharacteristicMotion extends Characteristics {
    private static final UUID CHARACTERISTICS = UUID.fromString("DA39C921-1D81-48E2-9C68-D0AE4BBD351F");
    private static final int MAX_SEQUENCE_NUMBER = 65536;

    private int accelerometerSensitivity;
    private int gyroscopeSensitivity;
    private double frequency;

    public CharacteristicMotion(double frequency, int accelerometerSensitivity, int gyroscopeSensitivity) {
        this.frequency = frequency;
        this.accelerometerSensitivity = accelerometerSensitivity;
        this.gyroscopeSensitivity = gyroscopeSensitivity;
    }


    @Override
    public Observable<Data> listen(RxBleConnection rxBleConnection) {
        final int[] lastSequenceNumber = {-1};
        final long[] lastCorrectedTimestamp = {-1};
        final double scaleFactorAcl = getScalingFactorAcl(accelerometerSensitivity);
        final double scaleFactorGyro = getScalingFactorGyro(gyroscopeSensitivity);
        return getCharacteristicListener(rxBleConnection, CHARACTERISTICS)
                .flatMap((Function<byte[], Observable<Data>>) bytes -> {
                    long curTime = System.currentTimeMillis();
                    Data[] data = new Data[4];
                    int sequenceNumber = getSequenceNumber(bytes);
                    long correctTimeStamp = correctTimeStamp(sequenceNumber, curTime, lastSequenceNumber[0], lastCorrectedTimestamp[0], frequency, MAX_SEQUENCE_NUMBER);
                    data[0] = new Data(SensorType.ACCELEROMETER, correctTimeStamp, getAccelerometer(bytes, scaleFactorAcl));
                    data[1] = new Data(SensorType.GYROSCOPE, correctTimeStamp, getGyroscope(bytes, scaleFactorGyro));
                    data[2] = new Data(SensorType.MOTION_RAW, curTime, getRaw(bytes));
                    data[3] = new Data(SensorType.MOTION_SEQUENCE_NUMBER, correctTimeStamp, new double[]{sequenceNumber});
                    lastCorrectedTimestamp[0] = correctTimeStamp;
                    lastSequenceNumber[0] = sequenceNumber;
                    return Observable.fromArray(data);
                });
    }

    private double getScalingFactorAcl(int sensitivityAcl) {
        switch (sensitivityAcl) {
            case 2:
                return 16384;
            case 4:
                return 8192;
            case 8:
                return 4096;
            case 16:
                return 2048;
            default:
                return 16384;
        }
    }

    private double getScalingFactorGyro(int sensitivityGyro) {
        switch (sensitivityGyro) {
            case 250:
                return 131;
            case 500:
                return 65.5;
            case 1000:
                return 32.8;
            case 2000:
                return 16.4;
            default:
                return 131;
        }
    }

    private double[] getAccelerometer(byte[] bytes, double scalingFactor) {
        double[] sample = new double[3];
        sample[0] = convertADCtoSI((short) ((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff), scalingFactor);
        sample[1] = convertADCtoSI((short) ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff), scalingFactor);
        sample[2] = convertADCtoSI((short) ((bytes[4] & 0xff) << 8) | (bytes[5] & 0xff), scalingFactor);
        return sample;
    }


    private double[] getGyroscope(byte[] bytes, double scalingFactor) {
        double[] sample = new double[3];
        sample[0] = convertADCtoSI((short) ((bytes[6] & 0xff) << 8) | (bytes[7] & 0xff), scalingFactor);
        sample[1] = convertADCtoSI((short) ((bytes[8] & 0xff) << 8) | (bytes[9] & 0xff), scalingFactor);
        sample[2] = convertADCtoSI((short) ((bytes[10] & 0xff) << 8) | (bytes[11] & 0xff), scalingFactor);
        return sample;
    }

    private static double convertADCtoSI(double x, double scalingFactor) {
        return x / scalingFactor;
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
