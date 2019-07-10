package org.md2k.motionsenselibrary.device.v2.motion_sense_hrv;


import org.md2k.motionsenselibrary.device.v2.motion_sense.MotionSenseSettingsV2;

public class MotionSenseHRVSettingsV2 extends MotionSenseSettingsV2 {
    public boolean ppgEnable;
    public boolean ppgFilteredEnable;
    public int ppgRed=62;
    public int ppgGreen=104;
    public int ppgInfrared=20;
    public boolean ppgDataQualityEnable;
    public boolean ppgSequenceNumberEnable;
    public boolean ppgRawEnable;
    public double ppgRawFrequency=25;

    public void setDefault(){
        super.setDefault();
        ppgRawFrequency = 25;
        ppgRed = 62;
        ppgGreen = 104;
        ppgInfrared = 20;
        ppgEnable = true;
        ppgFilteredEnable = false;
        ppgDataQualityEnable = true;
        ppgSequenceNumberEnable = true;
        ppgRawEnable = true;
    }
}
