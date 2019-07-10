package org.md2k.motionsenselibrary.device.v1.motion_sense;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselibrary.device.CharacteristicBattery;
import org.md2k.motionsenselibrary.device.Characteristics;
import org.md2k.motionsenselibrary.device.DataQuality;
import org.md2k.motionsenselibrary.device.DataQualityAccelerometer;
import org.md2k.motionsenselibrary.device.Device;
import org.md2k.motionsenselibrary.device.DeviceInfo;
import org.md2k.motionsenselibrary.device.SensorInfo;
import org.md2k.motionsenselibrary.device.SensorType;
import org.md2k.motionsenselibrary.device.DeviceSettings;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;


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
public class MotionSense extends Device {

    public MotionSense(RxBleClient rxBleClient, DeviceInfo deviceInfo, DeviceSettings deviceSettings) {
        super(rxBleClient, deviceInfo, deviceSettings);
    }

    @Override
    protected Observable<RxBleConnection> setConfiguration(RxBleConnection rxBleConnection) {
        return Observable.just(rxBleConnection);
    }

    @Override
    protected HashMap<SensorType, SensorInfo> createSensorInfo() {
        HashMap<SensorType, SensorInfo> sensorInfoArrayList = new HashMap<>();
        MotionSenseSettingsV1 s = (MotionSenseSettingsV1) deviceSettings;
        if(s.accelerometerEnable) sensorInfoArrayList.put(SensorType.ACCELEROMETER, createAccelerometerInfo());
        if(s.gyroscopeEnable) sensorInfoArrayList.put(SensorType.GYROSCOPE, createGyroscopeInfo());
        if(s.motionSequenceNumberEnable) sensorInfoArrayList.put(SensorType.MOTION_SEQUENCE_NUMBER, createMotionSequenceNumberInfo(65535));
        if(s.motionRawEnable) sensorInfoArrayList.put(SensorType.MOTION_RAW, createMotionRawInfo(20));
        if(s.batteryEnable) sensorInfoArrayList.put(SensorType.BATTERY, createBatteryInfo());
        if(s.accelerometerDataQualityEnable) sensorInfoArrayList.put(SensorType.ACCELEROMETER_DATA_QUALITY, createAccelerometerDataQualityInfo());
        return sensorInfoArrayList;
    }

    @Override
    protected ArrayList<Characteristics> createCharacteristics() {
        MotionSenseSettingsV1 s = (MotionSenseSettingsV1) deviceSettings;
        ArrayList<Characteristics> characteristics = new ArrayList<>();
        if (s.accelerometerEnable || s.gyroscopeEnable || s.motionRawEnable || s.motionSequenceNumberEnable || s.accelerometerDataQualityEnable)
            characteristics.add(new CharacteristicMotionSense(s.motionRawFrequency));
        if (s.batteryEnable)
            characteristics.add(new CharacteristicBattery());
        return characteristics;
    }

    @Override
    protected ArrayList<DataQuality> createDataQualities() {
        MotionSenseSettingsV1 s = (MotionSenseSettingsV1) deviceSettings;
        ArrayList<DataQuality> dataQualities = new ArrayList<>();
        if (s.accelerometerDataQualityEnable)
            dataQualities.add(new DataQualityAccelerometer());
        return dataQualities;
    }
}
