package org.md2k.motionsenselibrary.device.motion_sense_hrv_plus_gen2;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;

import org.md2k.motionsenselibrary.device.CharacteristicBattery;
import org.md2k.motionsenselibrary.device.Characteristics;
import org.md2k.motionsenselibrary.device.DataQuality;
import org.md2k.motionsenselibrary.device.DataQualityAccelerometer;
import org.md2k.motionsenselibrary.device.Device;
import org.md2k.motionsenselibrary.device.DeviceInfo;
import org.md2k.motionsenselibrary.device.DeviceSettings;
import org.md2k.motionsenselibrary.device.SensorInfo;
import org.md2k.motionsenselibrary.device.SensorType;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
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
public class MotionSenseHrvPlusGen2 extends Device {
    private CharacteristicConfig characteristicConfig;

    public MotionSenseHrvPlusGen2(RxBleClient rxBleClient, DeviceInfo deviceInfo, DeviceSettings deviceSettings) {
        super(rxBleClient, deviceInfo, deviceSettings);
        characteristicConfig = new CharacteristicConfig();
    }
     HashMap<SensorType, SensorInfo> createPPGSensorInfoList(){
         MotionSenseHRVPlusGen2Settings settings = (MotionSenseHRVPlusGen2Settings) deviceSettings;
         HashMap<SensorType, SensorInfo> sensorInfoArrayList = new HashMap<>();
         if (settings.isPpgFilteredEnable()) {
             if (settings.isPpgEnable()) {
                 sensorInfoArrayList.put(SensorType.PPG_FILTERED, createPPGFilteredInfo("measure the filtered value of ppg (infrared, red)", new String[]{"infrared1", "infrared2", "red1", "red2"}));
                 sensorInfoArrayList.put(SensorType.PPG_DC, createPPGDcInfo("measure the dc value of ppg (infrared, red)", new String[]{"infrared1", "infrared2", "red1", "red2"}));
             }
             if (settings.isRawPPGEnable()) {
                 sensorInfoArrayList.put(SensorType.PPG_RAW, createPPGRawInfo(18));
                 sensorInfoArrayList.put(SensorType.PPG_DC_RAW, createPPGDcRawInfo(18));
             }
             if (settings.isSequenceNumberPPGEnable()) {
                 sensorInfoArrayList.put(SensorType.PPG_SEQUENCE_NUMBER, createPPGSequenceNumberInfo(65535));
                 sensorInfoArrayList.put(SensorType.PPG_DC_SEQUENCE_NUMBER, createPPGDcSequenceNumberInfo(65535));
             }
         } else {
             if (settings.isPpgEnable()) {
                 sensorInfoArrayList.put(SensorType.PPG, createPPGInfo("measure the value of ppg (infrared, red)", new String[]{"infrared1", "infrared2", "red1", "red2"}));
             }
             if (settings.isRawPPGEnable()) {
                 sensorInfoArrayList.put(SensorType.PPG_RAW, createPPGRawInfo(14));
             }
             if (settings.isSequenceNumberPPGEnable()) {
                 sensorInfoArrayList.put(SensorType.PPG_SEQUENCE_NUMBER, createPPGSequenceNumberInfo(65535));
             }
         }
         return sensorInfoArrayList;

     }

    @Override
    protected HashMap<SensorType, SensorInfo> createSensorInfo() {
        MotionSenseHRVPlusGen2Settings settings = (MotionSenseHRVPlusGen2Settings) deviceSettings;
        HashMap<SensorType, SensorInfo> sensorInfoArrayList = new HashMap<>();
        if (settings.isAccelerometerEnable())
            sensorInfoArrayList.put(SensorType.ACCELEROMETER, createAccelerometerInfo());
        if (settings.isDataQualityAccelerometerEnable())
            sensorInfoArrayList.put(SensorType.ACCELEROMETER_DATA_QUALITY, createAccelerometerDataQualityInfo());
        if (settings.isGyroscopeEnable())
            sensorInfoArrayList.put(SensorType.GYROSCOPE, createGyroscopeInfo());
        if (settings.isSequenceNumberMotionEnable())
            sensorInfoArrayList.put(SensorType.MOTION_SEQUENCE_NUMBER, createMotionSequenceNumberInfo(65535));
        if (settings.isRawMotionEnable())
            sensorInfoArrayList.put(SensorType.MOTION_RAW, createMotionRawInfo(14));
        if (settings.isBatteryEnable())
            sensorInfoArrayList.put(SensorType.BATTERY, createBatteryInfo());
        if (settings.isMagnetometerEnable())
            sensorInfoArrayList.put(SensorType.MAGNETOMETER, createMagnetometerInfo());
        if (settings.isRawMagnetometerEnable())
            sensorInfoArrayList.put(SensorType.MAGNETOMETER_RAW, createMagnetometerRawInfo(14));
        if (settings.isSequenceNumberMagnetometerEnable())
            sensorInfoArrayList.put(SensorType.MAGNETOMETER_SEQUENCE_NUMBER, createMagnetometerSensitivityInfo());
        sensorInfoArrayList.putAll(createPPGSensorInfoList());
        return sensorInfoArrayList;
    }

    @Override
    protected ArrayList<Characteristics> createCharacteristics() {
        MotionSenseHRVPlusGen2Settings s = (MotionSenseHRVPlusGen2Settings) deviceSettings;
        ArrayList<Characteristics> characteristics = new ArrayList<>();
        if (s.isAccelerometerEnable() || s.isGyroscopeEnable() || s.isRawMotionEnable() || s.isSequenceNumberMotionEnable() || s.isDataQualityAccelerometerEnable())
            characteristics.add(new CharacteristicMotion(s.getAccelerometerFrequency(), s.getAccelerometerSensitivity(), s.getGyroscopeSensitivity()));
        if (s.isBatteryEnable())
            characteristics.add(new CharacteristicBattery());
        if (s.isPpgFilteredEnable() && (s.isPpgEnable() || s.isRawPPGEnable() || s.isDataQualityPPGEnable() || s.isSequenceNumberPPGEnable())) {
            characteristics.add(new CharacteristicPPGFiltered(s.getPpgFrequency()));
            characteristics.add(new CharacteristicPPGFilteredDc(s.getPpgFrequency()));
        }
        if (!s.isPpgFilteredEnable() && (s.isPpgEnable() || s.isRawPPGEnable() || s.isDataQualityPPGEnable() || s.isSequenceNumberPPGEnable())) {
            characteristics.add(new CharacteristicPpg(s.getPpgFrequency()));
        }
        if(s.isMagnetometerEnable()|| s.isRawMagnetometerEnable()|| s.isSequenceNumberMagnetometerEnable()){
            characteristics.add(new CharacteristicMagnetometer(s.getMagnetometerFrequency()));
        }

        return characteristics;
    }

    @Override
    protected ArrayList<DataQuality> createDataQualities() {
        MotionSenseHRVPlusGen2Settings s = (MotionSenseHRVPlusGen2Settings) deviceSettings;
        ArrayList<DataQuality> dataQualities = new ArrayList<>();
        if (s.isDataQualityAccelerometerEnable())
            dataQualities.add(new DataQualityAccelerometer());
/*
        if(s.isDataQualityPPGEnable())
            dataQualities.add(new DataQualityPPG());
*/
        return dataQualities;
    }

    @Override
    protected Observable<RxBleConnection> setConfiguration(RxBleConnection rxBleConnection) {
        return characteristicConfig.setConfiguration(rxBleConnection, (MotionSenseHRVPlusGen2Settings) deviceSettings);
    }

}
