package org.md2k.motionsenselibrary.device.v2.motion_sense_hrv;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselibrary.device.Characteristics;
import org.md2k.motionsenselibrary.device.DataQuality;
import org.md2k.motionsenselibrary.device.DeviceInfo;
import org.md2k.motionsenselibrary.device.SensorInfo;
import org.md2k.motionsenselibrary.device.SensorType;
import org.md2k.motionsenselibrary.device.DeviceSettings;
import org.md2k.motionsenselibrary.device.v2.CharacteristicPPGFilteredDcOld;
import org.md2k.motionsenselibrary.device.v2.CharacteristicPPGFilteredOld;
import org.md2k.motionsenselibrary.device.v2.CharacteristicPpgOld;
import org.md2k.motionsenselibrary.device.v2.motion_sense.MotionSenseV2;

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
public class MotionSenseHrvV2 extends MotionSenseV2 {
    private CharacteristicConfigMotionSenseHRV characteristicConfig;

    public MotionSenseHrvV2(RxBleClient rxBleClient, DeviceInfo deviceInfo, DeviceSettings deviceSettings) {
        super(rxBleClient, deviceInfo, deviceSettings);
        characteristicConfig = new CharacteristicConfigMotionSenseHRV();
    }

    @Override
    protected HashMap<SensorType, SensorInfo> createSensorInfo() {
        MotionSenseHRVSettingsV2 settings = (MotionSenseHRVSettingsV2) deviceSettings;
        HashMap<SensorType, SensorInfo> sensorInfoArrayList = super.createSensorInfo();
        if (settings.ppgFilteredEnable) {
            if (settings.ppgEnable) {
                sensorInfoArrayList.put(SensorType.PPG_FILTERED, createPPGFilteredInfo("measure the filtered value of ppg (infrared, red)", new String[]{"infrared1", "infrared2", "red1", "red2"}));
                sensorInfoArrayList.put(SensorType.PPG_DC, createPPGDcInfo("measure the dc value of ppg (infrared, red)", new String[]{"infrared1", "infrared2", "red1", "red2"}));
            }
            if (settings.ppgRawEnable) {
                sensorInfoArrayList.put(SensorType.PPG_RAW, createPPGRawInfo(18));
                sensorInfoArrayList.put(SensorType.PPG_DC_RAW, createPPGDcRawInfo(18));
            }
            if (settings.ppgSequenceNumberEnable) {
                sensorInfoArrayList.put(SensorType.PPG_SEQUENCE_NUMBER, createPPGSequenceNumberInfo(65535));
                sensorInfoArrayList.put(SensorType.PPG_DC_SEQUENCE_NUMBER, createPPGDcSequenceNumberInfo(65535));
            }
        } else {
            if (settings.ppgEnable) {
                sensorInfoArrayList.put(SensorType.PPG, createPPGInfo("measure the value of ppg (infrared, red)", new String[]{"infrared1", "infrared2", "red1", "red2"}));
            }
            if (settings.ppgRawEnable) {
                sensorInfoArrayList.put(SensorType.PPG_RAW, createPPGRawInfo(14));
            }
            if (settings.ppgSequenceNumberEnable) {
                sensorInfoArrayList.put(SensorType.PPG_SEQUENCE_NUMBER, createPPGSequenceNumberInfo(65535));
            }
        }
        return sensorInfoArrayList;
    }

    @Override
    protected ArrayList<Characteristics> createCharacteristics() {
        MotionSenseHRVSettingsV2 s = (MotionSenseHRVSettingsV2) deviceSettings;
        ArrayList<Characteristics> characteristics = super.createCharacteristics();
        if (s.ppgFilteredEnable && (s.ppgEnable || s.ppgRawEnable || s.ppgDataQualityEnable || s.ppgSequenceNumberEnable)) {
            characteristics.add(new CharacteristicPPGFilteredOld(s.ppgRawFrequency));
            characteristics.add(new CharacteristicPPGFilteredDcOld(s.ppgRawFrequency));
        }
        if (!s.ppgFilteredEnable && (s.ppgEnable || s.ppgRawEnable || s.ppgDataQualityEnable || s.ppgSequenceNumberEnable)) {
            characteristics.add(new CharacteristicPpgOld(s.ppgRawFrequency));
        }
        return characteristics;
    }

    @Override
    protected ArrayList<DataQuality> createDataQualities() {
        MotionSenseHRVSettingsV2 s = (MotionSenseHRVSettingsV2) deviceSettings;
        ArrayList<DataQuality> dataQualities = super.createDataQualities();
/*
        if(s.ppgDataQualityEnable)
            dataQualities.add(new DataQualityPPG());
*/
        return dataQualities;
    }

    @Override
    protected Observable<RxBleConnection> setConfiguration(RxBleConnection rxBleConnection) {
        return characteristicConfig.setConfiguration(rxBleConnection, deviceSettings);
    }

}
