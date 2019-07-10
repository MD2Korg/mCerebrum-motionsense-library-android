package org.md2k.motionsenselibrary.device.v2.motion_sense;


import org.md2k.motionsenselibrary.device.DeviceSettings;

public class MotionSenseSettingsV2 implements DeviceSettings {
    public boolean debugEnable;
    public int minimumConnectionInterval=10;
    //Motion: Accelerometer
    public boolean accelerometerEnable;
    public int accelerometerSensitivity=4;
    public boolean accelerometerDataQualityEnable;

    //Motion: Gyroscope
    public boolean gyroscopeEnable;
    public int gyroscopeSensitivity=500;

    //Motion: sequence number
    public boolean motionSequenceNumberEnable;

    //Motion: raw
    public boolean motionRawEnable;
    public double motionRawFrequency=25;

    //battery
    public boolean batteryEnable;

    public void setDefault(){
        minimumConnectionInterval = 10;
        accelerometerSensitivity = 4;
        gyroscopeSensitivity = 500;
        motionRawFrequency = 25;
        debugEnable = true;
        accelerometerEnable = true;
        accelerometerDataQualityEnable = true;
        gyroscopeEnable = true;
        motionSequenceNumberEnable = true;
        motionRawEnable = true;
        batteryEnable = true;
    }
}
