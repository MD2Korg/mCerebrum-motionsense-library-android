package org.md2k.motionsenselibrary.device.v2.motion_sense_hrv_plus;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselibrary.device.Characteristics;
import org.md2k.motionsenselibrary.device.DeviceInfo;
import org.md2k.motionsenselibrary.device.SensorInfo;
import org.md2k.motionsenselibrary.device.SensorType;
import org.md2k.motionsenselibrary.device.DeviceSettings;
import org.md2k.motionsenselibrary.device.v2.CharacteristicMagnetometer;
import org.md2k.motionsenselibrary.device.v2.motion_sense_hrv.MotionSenseHrvV2;

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
public class MotionSenseHrvPlusV2 extends MotionSenseHrvV2 {
    private CharacteristicConfigMotionSenseHrvPlus characteristicConfig;

    public MotionSenseHrvPlusV2(RxBleClient rxBleClient, DeviceInfo deviceInfo, DeviceSettings deviceSettings) {
        super(rxBleClient, deviceInfo, deviceSettings);
        characteristicConfig = new CharacteristicConfigMotionSenseHrvPlus();
    }

    @Override
    protected HashMap<SensorType, SensorInfo> createSensorInfo() {
        MotionSenseHRVPlusSettingsV2 settings = (MotionSenseHRVPlusSettingsV2) deviceSettings;
        HashMap<SensorType, SensorInfo> sensorInfoArrayList = super.createSensorInfo();
        if (settings.magnetometerEnable)
            sensorInfoArrayList.put(SensorType.MAGNETOMETER, createMagnetometerInfo());
        if (settings.magnetometerRawEnable)
            sensorInfoArrayList.put(SensorType.MAGNETOMETER_RAW, createMagnetometerRawInfo(14));
        if (settings.magnetometerSequenceNumberEnable)
            sensorInfoArrayList.put(SensorType.MAGNETOMETER_SEQUENCE_NUMBER, createMagnetometerSequenceNumberInfo(65535));
        return sensorInfoArrayList;
    }

    @Override
    protected ArrayList<Characteristics> createCharacteristics() {
        MotionSenseHRVPlusSettingsV2 s = (MotionSenseHRVPlusSettingsV2) deviceSettings;
        ArrayList<Characteristics> characteristics = super.createCharacteristics();
        if(s.magnetometerEnable|| s.magnetometerRawEnable|| s.magnetometerSequenceNumberEnable){
            characteristics.add(new CharacteristicMagnetometer(s.magnetometerRawFrequency));
        }

        return characteristics;
    }

    @Override
    protected Observable<RxBleConnection> setConfiguration(RxBleConnection rxBleConnection) {
        return characteristicConfig.setConfiguration(rxBleConnection, deviceSettings);
    }

}
