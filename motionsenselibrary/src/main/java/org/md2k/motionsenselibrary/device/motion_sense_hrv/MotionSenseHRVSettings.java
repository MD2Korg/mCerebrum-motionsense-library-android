package org.md2k.motionsenselibrary.device.motion_sense_hrv;

import org.md2k.motionsenselibrary.device.DeviceSettings;

public class MotionSenseHRVSettings implements DeviceSettings {

    private boolean debugEnable;
    private boolean accelerometerEnable;
    private double accelerometerFrequency;
    private boolean dataQualityAccelerometerEnable;
    private double dataQualityAccelerometerFrequency;
    private boolean gyroscopeEnable;
    private double gyroscopeFrequency;
    private boolean sequenceNumberMotionEnable;
    private double sequenceNumberMotionFrequency;
    private boolean rawMotionEnable;
    private double rawMotionFrequency;
    private boolean batteryEnable;
    private boolean ppgEnable;
    private double ppgFrequency;
    private boolean dataQualityPPGEnable;
    private double dataQualityPPGFrequency;
    private MotionSenseHRVSettings(){
        accelerometerFrequency = 16;
        gyroscopeFrequency = 16;
        sequenceNumberMotionFrequency = 16;
        rawMotionFrequency = 16;
        ppgFrequency = 16;
        dataQualityAccelerometerFrequency = 0.33;
        dataQualityPPGFrequency = 0.33;
    }

    public boolean isDebugEnable() {
        return debugEnable;
    }

    public boolean isAccelerometerEnable() {
        return accelerometerEnable;
    }

    public double getAccelerometerFrequency() {
        return accelerometerFrequency;
    }

    public boolean isDataQualityAccelerometerEnable() {
        return dataQualityAccelerometerEnable;
    }

    public double getDataQualityAccelerometerFrequency() {
        return dataQualityAccelerometerFrequency;
    }

    public boolean isGyroscopeEnable() {
        return gyroscopeEnable;
    }

    public double getGyroscopeFrequency() {
        return gyroscopeFrequency;
    }

    public boolean isSequenceNumberMotionEnable() {
        return sequenceNumberMotionEnable;
    }

    public double getSequenceNumberMotionFrequency() {
        return sequenceNumberMotionFrequency;
    }

    public boolean isRawMotionEnable() {
        return rawMotionEnable;
    }

    public double getRawMotionFrequency() {
        return rawMotionFrequency;
    }

    public boolean isBatteryEnable() {
        return batteryEnable;
    }

    public boolean isPpgEnable() {
        return ppgEnable;
    }

    public double getPpgFrequency() {
        return ppgFrequency;
    }

    public boolean isDataQualityPPGEnable() {
        return dataQualityPPGEnable;
    }

    public double getDataQualityPPGFrequency() {
        return dataQualityPPGFrequency;
    }
    public static class Builder {
        private MotionSenseHRVSettings settings;
        public Builder(){
            settings = new MotionSenseHRVSettings();
        }
        public Builder setDefault(){
            settings.debugEnable = true;
            settings.accelerometerEnable = true;
            settings.dataQualityAccelerometerEnable = true;
            settings.gyroscopeEnable=true;
            settings.sequenceNumberMotionEnable = true;
            settings.rawMotionEnable = true;
            settings.batteryEnable= true;
            settings.ppgEnable = true;
            settings.dataQualityPPGEnable = true;
            return this;
        }
        public Builder debugEnable(boolean enable){
            settings.debugEnable = enable;
            return this;
        }
        public Builder accelerometerEnable(boolean enable){
            settings.accelerometerEnable = enable;
            return this;
        }
        public Builder dataQualityAccelerometerEnable(boolean enable){
            settings.dataQualityAccelerometerEnable = enable;
            return this;
        }
        public Builder gyroscopeEnable(boolean enable){
            settings.gyroscopeEnable = enable;
            return this;
        }
        public Builder sequenceNumberMotionEnable(boolean enable){
            settings.sequenceNumberMotionEnable = enable;
            return this;
        }
        public Builder rawMotionEnable(boolean enable){
            settings.rawMotionEnable = enable;
            return this;
        }
        public Builder batteryEnable(boolean enable){
            settings.batteryEnable = enable;
            return this;
        }
        public Builder ppgEnable(boolean enable){
            settings.ppgEnable = enable;
            return this;
        }
        public Builder dataQualityPPGEnable(boolean enable){
            settings.dataQualityPPGEnable = enable;
            return this;
        }
        public DeviceSettings build() {
            return settings;
        }
    }

}

