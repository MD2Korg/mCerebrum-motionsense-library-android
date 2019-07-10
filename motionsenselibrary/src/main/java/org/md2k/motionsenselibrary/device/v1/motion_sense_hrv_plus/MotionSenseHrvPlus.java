package org.md2k.motionsenselibrary.device.v1.motion_sense_hrv_plus;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselibrary.device.CharacteristicBattery;
import org.md2k.motionsenselibrary.device.DataQualityAccelerometer;
import org.md2k.motionsenselibrary.device.DataQualityPPG;
import org.md2k.motionsenselibrary.device.DeviceSettings;
import org.md2k.motionsenselibrary.device.Characteristics;
import org.md2k.motionsenselibrary.device.DataQuality;
import org.md2k.motionsenselibrary.device.Device;
import org.md2k.motionsenselibrary.device.DeviceInfo;
import org.md2k.motionsenselibrary.device.SensorInfo;
import org.md2k.motionsenselibrary.device.SensorType;

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
public class MotionSenseHrvPlus extends Device {

    public MotionSenseHrvPlus(RxBleClient rxBleClient, DeviceInfo deviceInfo, DeviceSettings deviceSettings) {
        super(rxBleClient, deviceInfo, deviceSettings);
    }
    @Override
    protected Observable<RxBleConnection> setConfiguration(RxBleConnection rxBleConnection) {
        return Observable.just(rxBleConnection);
    }

    @Override
    protected HashMap<SensorType, SensorInfo> createSensorInfo() {
        MotionSenseHRVPlusSettingsV1 settings = (MotionSenseHRVPlusSettingsV1) deviceSettings;
        HashMap<SensorType, SensorInfo> sensorInfoArrayList = new HashMap<>();
        if(settings.accelerometerEnable) sensorInfoArrayList.put(SensorType.ACCELEROMETER, createAccelerometerInfo());
        if(settings.quaternionEnable) sensorInfoArrayList.put(SensorType.QUATERNION, createQuaternionInfo());
        if(settings.motionSequenceNumberEnable) sensorInfoArrayList.put(SensorType.MOTION_SEQUENCE_NUMBER, createMotionSequenceNumberInfo(1023));
        if(settings.motionRawEnable) sensorInfoArrayList.put(SensorType.MOTION_RAW, createMotionRawInfo(20));
        if(settings.accelerometerDataQualityEnable) sensorInfoArrayList.put(SensorType.ACCELEROMETER_DATA_QUALITY, createAccelerometerDataQualityInfo());
        if(settings.batteryEnable) sensorInfoArrayList.put(SensorType.BATTERY, createBatteryInfo());
        if(settings.ppgEnable) sensorInfoArrayList.put(SensorType.PPG, createPPGInfo("measure the value of ppg (red, infrared, green)", new String[]{"red", "infrared", "green"}));
        if(settings.ppgDataQualityEnable) sensorInfoArrayList.put(SensorType.PPG_DATA_QUALITY, createPPGDataQualityInfo());
        if(settings.magnetometerEnable) sensorInfoArrayList.put(SensorType.MAGNETOMETER, createMagnetometerInfo());
        if(settings.magnetometerSensitivityEnable) sensorInfoArrayList.put(SensorType.MAGNETOMETER_SENSITIVITY, createMagnetometerSensitivityInfo());
        if(settings.magnetometerSequenceNumberEnable) sensorInfoArrayList.put(SensorType.MAGNETOMETER_SEQUENCE_NUMBER, createMagnetometerSequenceNumberInfo(1023));
        if(settings.magnetometerRawEnable) sensorInfoArrayList.put(SensorType.MAGNETOMETER_RAW, createMagnetometerRawInfo(17));
        return sensorInfoArrayList;
    }

    @Override
    protected ArrayList<Characteristics> createCharacteristics() {
        MotionSenseHRVPlusSettingsV1 s = (MotionSenseHRVPlusSettingsV1) deviceSettings;
        ArrayList<Characteristics> characteristics = new ArrayList<>();
        if (s.accelerometerEnable || s.quaternionEnable || s.motionRawEnable || s.motionSequenceNumberEnable || s.accelerometerDataQualityEnable || s.ppgEnable || s.ppgDataQualityEnable)
            characteristics.add(new CharacteristicMotion(s.motionRawFrequency));
        if (s.batteryEnable)
            characteristics.add(new CharacteristicBattery());
        if(s.magnetometerEnable || s.magnetometerSensitivityEnable || s.magnetometerRawEnable||s.magnetometerSequenceNumberEnable)
            characteristics.add(new CharacteristicMagnetometer(s.magnetometerRawFrequency));
        return characteristics;
    }

    @Override
    protected ArrayList<DataQuality> createDataQualities() {
        MotionSenseHRVPlusSettingsV1 s = (MotionSenseHRVPlusSettingsV1) deviceSettings;
        ArrayList<DataQuality> dataQualities = new ArrayList<>();
        if (s.accelerometerDataQualityEnable)
            dataQualities.add(new DataQualityAccelerometer());
        if(s.ppgDataQualityEnable)
            dataQualities.add(new DataQualityPPG());
        return dataQualities;
    }
}
