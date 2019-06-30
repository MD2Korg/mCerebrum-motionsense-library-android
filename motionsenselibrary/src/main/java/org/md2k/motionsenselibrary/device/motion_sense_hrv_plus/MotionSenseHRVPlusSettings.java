package org.md2k.motionsenselibrary.device.motion_sense_hrv_plus;

import org.md2k.motionsenselibrary.device.DeviceSettings;

public class MotionSenseHRVPlusSettings implements DeviceSettings {
    private boolean debugEnable;
    private boolean accelerometerEnable;
    private double accelerometerFrequency;
    private boolean dataQualityAccelerometerEnable;
    private double dataQualityAccelerometerFrequency;
    private boolean quaternionEnable;
    private double quaternionFrequency;
    private boolean sequenceNumberMotionEnable;
    private double sequenceNumberMotionFrequency;
    private boolean rawMotionEnable;
    private double rawMotionFrequency;
    private boolean batteryEnable;
    private boolean ppgEnable;
    private double ppgFrequency;
    private boolean dataQualityPPGEnable;
    private double dataQualityPPGFrequency;
    private boolean magnetometerEnable;
    private double magnetometerFrequency;
    private boolean magnetometerSensitivityEnable;
    private double magnetometerSensitivityFrequency;
    private boolean rawMagnetometerEnable;
    private double rawMagnetometerFrequency;
    private boolean sequenceNumberMagnetometerEnable;
    private double sequenceNumberMagnetometerFrequency;

    private MotionSenseHRVPlusSettings(){
        accelerometerFrequency = 25;
        quaternionFrequency = 25;
        sequenceNumberMotionFrequency = 25;
        rawMotionFrequency = 25;
        ppgFrequency = 25;
        dataQualityAccelerometerFrequency = 0.33;
        dataQualityPPGFrequency = 0.33;
        magnetometerFrequency = 25;
        magnetometerSensitivityFrequency = 12.5;
        rawMagnetometerFrequency = 12.5;
        sequenceNumberMagnetometerFrequency = 12.5;
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

    public boolean isQuaternionEnable() {
        return quaternionEnable;
    }

    public double getQuaternionFrequency() {
        return quaternionFrequency;
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

    public boolean isMagnetometerEnable() {
        return magnetometerEnable;
    }

    public double getMagnetometerFrequency() {
        return magnetometerFrequency;
    }

    public boolean isMagnetometerSensitivityEnable() {
        return magnetometerSensitivityEnable;
    }

    public double getMagnetometerSensitivityFrequency() {
        return magnetometerSensitivityFrequency;
    }

    public boolean isRawMagnetometerEnable() {
        return rawMagnetometerEnable;
    }

    public double getRawMagnetometerFrequency() {
        return rawMagnetometerFrequency;
    }

    public boolean isSequenceNumberMagnetometerEnable() {
        return sequenceNumberMagnetometerEnable;
    }

    public double getSequenceNumberMagnetometerFrequency() {
        return sequenceNumberMagnetometerFrequency;
    }
    public static class Builder {
        private MotionSenseHRVPlusSettings settings;
        public Builder(){
            settings = new MotionSenseHRVPlusSettings();
        }
        public Builder setDefault(){
            settings.debugEnable = true;
            settings.accelerometerEnable = true;
            settings.dataQualityAccelerometerEnable = true;
            settings.quaternionEnable=true;
            settings.sequenceNumberMotionEnable = true;
            settings.rawMotionEnable = true;
            settings.batteryEnable= true;
            settings.ppgEnable = true;
            settings.dataQualityPPGEnable = true;
            settings.magnetometerEnable = true;
            settings.magnetometerSensitivityEnable = true;
            settings.rawMagnetometerEnable = true;
            settings.sequenceNumberMagnetometerEnable = true;
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
        public Builder quaternionEnable(boolean enable){
            settings.quaternionEnable = enable;
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
        public Builder magnetometerEnable(boolean enable){
            settings.magnetometerEnable = enable;
            return this;
        }
        public Builder magnetometerSensitivityEnable(boolean enable){
            settings.magnetometerSensitivityEnable = enable;
            return this;
        }
        public Builder rawMagnetometerEnable(boolean enable){
            settings.rawMagnetometerEnable = enable;
            return this;
        }
        public Builder sequenceNumberMagnetometerEnable(boolean enable){
            settings.sequenceNumberMagnetometerEnable = enable;
            return this;
        }
        public DeviceSettings build() {
            return settings;
        }
    }
}

