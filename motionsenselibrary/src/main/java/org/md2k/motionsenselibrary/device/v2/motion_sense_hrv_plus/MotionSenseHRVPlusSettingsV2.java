package org.md2k.motionsenselibrary.device.v2.motion_sense_hrv_plus;


import org.md2k.motionsenselibrary.device.v2.motion_sense_hrv.MotionSenseHRVSettingsV2;

public class MotionSenseHRVPlusSettingsV2 extends MotionSenseHRVSettingsV2 {
    public boolean magnetometerEnable;
    public boolean magnetometerSensitivityEnable;
    public boolean magnetometerRawEnable;
    public double magnetometerRawFrequency=25;
    public boolean magnetometerSequenceNumberEnable;

    public void setDefault(){
        super.setDefault();
        magnetometerRawFrequency = 25;
        magnetometerEnable = true;
        magnetometerSensitivityEnable = true;
        magnetometerRawEnable = true;
        magnetometerSequenceNumberEnable = true;
    }
}
