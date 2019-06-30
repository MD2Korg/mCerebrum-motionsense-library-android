package org.md2k.motionsenselibrary.device;


import org.md2k.motionsenselibrary.device.motion_sense.MotionSenseSettings;
import org.md2k.motionsenselibrary.device.motion_sense_hrv.MotionSenseHRVSettings;
import org.md2k.motionsenselibrary.device.motion_sense_hrv_plus.MotionSenseHRVPlusSettings;
import org.md2k.motionsenselibrary.device.motion_sense_hrv_plus_gen2.MotionSenseHRVPlusGen2Settings;

public interface DeviceSettings {

    static MotionSenseSettings.Builder MotionSenseBuilder() {
        return new MotionSenseSettings.Builder();
    }
    static MotionSenseHRVSettings.Builder MotionSenseHRVBuilder() {
        return new MotionSenseHRVSettings.Builder();
    }
    static MotionSenseHRVPlusSettings.Builder MotionSenseHRVPlusBuilder() {
        return new MotionSenseHRVPlusSettings.Builder();
    }
    static MotionSenseHRVPlusGen2Settings.Builder MotionSenseHRVPlusGen2Builder() {
        return new MotionSenseHRVPlusGen2Settings.Builder();
    }

}
