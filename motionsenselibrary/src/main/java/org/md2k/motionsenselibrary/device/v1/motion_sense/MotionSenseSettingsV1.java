package org.md2k.motionsenselibrary.device.v1.motion_sense;

import org.md2k.motionsenselibrary.device.DeviceSettings;

public class MotionSenseSettingsV1 implements DeviceSettings {
    public boolean debugEnable;

    public boolean accelerometerEnable;
    public boolean accelerometerDataQualityEnable;
    public boolean gyroscopeEnable;
    public boolean motionSequenceNumberEnable;
    public boolean motionRawEnable;
    public double motionRawFrequency=16;
    public boolean batteryEnable;

    public void setDefault() {
        motionRawFrequency = 16;
        debugEnable = true;
        motionRawEnable = true;
        accelerometerEnable = true;
        accelerometerDataQualityEnable = true;
        gyroscopeEnable = true;
        motionSequenceNumberEnable = true;
        batteryEnable = true;
    }
}

