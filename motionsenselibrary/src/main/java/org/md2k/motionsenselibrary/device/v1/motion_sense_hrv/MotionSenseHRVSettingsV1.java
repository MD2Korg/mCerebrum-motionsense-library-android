package org.md2k.motionsenselibrary.device.v1.motion_sense_hrv;

import org.md2k.motionsenselibrary.device.DeviceSettings;

public class MotionSenseHRVSettingsV1 implements DeviceSettings {

    public boolean debugEnable;
    public boolean accelerometerEnable;
    public boolean accelerometerDataQualityEnable;
    public boolean gyroscopeEnable;
    public boolean motionSequenceNumberEnable;
    public boolean motionRawEnable;
    public double motionRawFrequency;
    public boolean batteryEnable;
    public boolean ppgEnable;
    public boolean ppgDataQualityEnable;
    public double ppgRawFrequency;
    public boolean ppgRawEnable;
    public boolean ppgSequenceNumberEnable;

    public MotionSenseHRVSettingsV1(){
        motionRawFrequency = 16;
        ppgRawFrequency =25;
    }
    public void setDefault() {
        motionRawFrequency = 16;
        ppgRawFrequency =25;
        debugEnable = true;
        motionRawEnable = true;
        accelerometerEnable = true;
        accelerometerDataQualityEnable = true;
        gyroscopeEnable = true;
        motionSequenceNumberEnable = true;
        batteryEnable = true;
        ppgEnable = true;
        ppgDataQualityEnable = true;
        ppgRawEnable = true;
        ppgSequenceNumberEnable = true;
    }
}

