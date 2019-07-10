package org.md2k.motionsenselibrary.device;


import org.md2k.motionsenselibrary.device.v1.motion_sense.MotionSenseSettingsV1;
import org.md2k.motionsenselibrary.device.v1.motion_sense_hrv.MotionSenseHRVSettingsV1;
import org.md2k.motionsenselibrary.device.v1.motion_sense_hrv_plus.MotionSenseHRVPlusSettingsV1;
import org.md2k.motionsenselibrary.device.v2.motion_sense.MotionSenseSettingsV2;
import org.md2k.motionsenselibrary.device.v2.motion_sense_hrv.MotionSenseHRVSettingsV2;
import org.md2k.motionsenselibrary.device.v2.motion_sense_hrv_plus.MotionSenseHRVPlusSettingsV2;
import org.md2k.motionsenselibrary.device.v2.motion_sense_hrv_plus_gen2.MotionSenseHRVPlusGen2SettingsV2;

public interface DeviceSettings {
    void setDefault();
    public static Builder builder(DeviceInfo deviceInfo){
        return new Builder(deviceInfo);
    }

    public class Builder{
        DeviceSettings deviceSettings;
        public Builder(DeviceInfo deviceInfo) {
            if (deviceInfo.getVersion().isVersion1()) {
                switch (deviceInfo.getVersion().getType()) {
                    case MOTION_SENSE:
                        deviceSettings = new MotionSenseSettingsV1();
                        break;
                    case MOTION_SENSE_HRV:
                        deviceSettings = new MotionSenseHRVSettingsV1();
                        break;
                    case MOTION_SENSE_HRV_PLUS:
                        deviceSettings = new MotionSenseHRVPlusSettingsV1();
                        break;
                    default:
                        deviceSettings = new MotionSenseSettingsV1();
                }
            } else {
                switch (deviceInfo.getVersion().getType()) {
                    case MOTION_SENSE:
                        deviceSettings = new MotionSenseSettingsV2();
                        break;
                    case MOTION_SENSE_HRV:
                        deviceSettings = new MotionSenseHRVSettingsV2();
                        break;
                    case MOTION_SENSE_HRV_PLUS:
                        deviceSettings = new MotionSenseHRVPlusSettingsV2();
                        break;
                    case MOTION_SENSE_HRV_PLUS_GEN2:
                        deviceSettings = new MotionSenseHRVPlusGen2SettingsV2();
                        break;
                    case MOTION_SENSE_HRV_PLUS_GEN2_IG:
                        deviceSettings = new MotionSenseHRVPlusGen2SettingsV2();
                        break;
                    case MOTION_SENSE_HRV_PLUS_GEN2_IR:
                        deviceSettings = new MotionSenseHRVPlusGen2SettingsV2();
                        break;
                    default:
                        deviceSettings = new MotionSenseSettingsV1();
                }
            }
        }
        public Builder setDefault(){
            deviceSettings.setDefault();
            return this;
        }
        public DeviceSettings build(){
            return deviceSettings;
        }
    }

}
