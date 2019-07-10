package org.md2k.motionsenselibrary.device.v1.motion_sense_hrv_plus;

import org.md2k.motionsenselibrary.device.DeviceSettings;

public class MotionSenseHRVPlusSettingsV1 implements DeviceSettings {

    public boolean debugEnable;
    public boolean accelerometerEnable;
    public boolean accelerometerDataQualityEnable;
    public boolean quaternionEnable;
    public boolean motionSequenceNumberEnable;
    public boolean motionRawEnable;
    public double motionRawFrequency;
    public boolean batteryEnable;
    public boolean ppgEnable;
    public boolean ppgDataQualityEnable;
    public double ppgRawFrequency;
    public boolean ppgRawEnable;
    public boolean magnetometerEnable;
    public double magnetometerRawFrequency;
    public boolean magnetometerRawEnable;
    public boolean magnetometerSensitivityEnable;
    public boolean magnetometerSequenceNumberEnable;

    public MotionSenseHRVPlusSettingsV1(){
        motionRawFrequency = 25;
        ppgRawFrequency =25;
    }
    public void setDefault() {
        motionRawFrequency = 25;
        ppgRawFrequency =25;
        magnetometerRawFrequency = 12.5;
        debugEnable = true;
        motionRawEnable = true;
        accelerometerEnable = true;
        accelerometerDataQualityEnable = true;
        quaternionEnable = true;
        motionSequenceNumberEnable = true;
        batteryEnable = true;
        ppgEnable = true;
        ppgDataQualityEnable = true;
        ppgRawEnable = true;
        magnetometerEnable = true;
        magnetometerRawEnable = true;
        magnetometerSequenceNumberEnable = true;
        magnetometerSensitivityEnable = true;
    }
}

