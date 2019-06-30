package org.md2k.motionsenselibrary.device.motion_sense_hrv_plus_gen2;


import org.md2k.motionsenselibrary.device.DeviceSettings;

public class MotionSenseHRVPlusGen2Settings implements DeviceSettings {
    private boolean debugEnable;
    private int minimumConnectionInterval;
    //Motion: Accelerometer
    private boolean accelerometerEnable;
    private double accelerometerFrequency;
    private int accelerometerSensitivity;
    private boolean dataQualityAccelerometerEnable;

    //Motion: Gyroscope
    private boolean gyroscopeEnable;
    private double gyroscopeFrequency;
    private int gyroscopeSensitivity;

    //Motion: sequence number
    private boolean sequenceNumberMotionEnable;
    private double sequenceNumberMotionFrequency;
    //Motion: raw
    private boolean rawMotionEnable;
    private double rawMotionFrequency;

    //battery
    private boolean batteryEnable;

    //led
    private boolean ppgEnable;
    private double ppgFrequency;
    private boolean ppgFilteredEnable;
    private int ppgRed;
    private int ppgGreen;
    private int ppgInfrared;
    private boolean dataQualityPPGEnable;
    private boolean sequenceNumberPPGEnable;
    private double sequenceNumberPPGFrequency;
    private boolean rawPPGEnable;
    private double rawPPGFrequency;

    // magnetometer
    private boolean magnetometerEnable;
    private double magnetometerFrequency;
    private boolean magnetometerSensitivityEnable;
    private boolean rawMagnetometerEnable;
    private double rawMagnetometerFrequency;
    private boolean sequenceNumberMagnetometerEnable;
    private double sequenceNumberMagnetometerFrequency;

    public boolean isDebugEnable() {
        return debugEnable;
    }

    public int getMinimumConnectionInterval() {
        return minimumConnectionInterval;
    }

    public boolean isAccelerometerEnable() {
        return accelerometerEnable;
    }

    public double getAccelerometerFrequency() {
        return accelerometerFrequency;
    }

    public int getAccelerometerSensitivity() {
        return accelerometerSensitivity;
    }

    public boolean isDataQualityAccelerometerEnable() {
        return dataQualityAccelerometerEnable;
    }

    public boolean isGyroscopeEnable() {
        return gyroscopeEnable;
    }

    public double getGyroscopeFrequency() {
        return gyroscopeFrequency;
    }

    public int getGyroscopeSensitivity() {
        return gyroscopeSensitivity;
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

    public boolean isPpgFilteredEnable() {
        return ppgFilteredEnable;
    }

    public int getPpgRed() {
        return ppgRed;
    }

    public int getPpgGreen() {
        return ppgGreen;
    }

    public int getPpgInfrared() {
        return ppgInfrared;
    }

    public boolean isDataQualityPPGEnable() {
        return dataQualityPPGEnable;
    }

    public boolean isSequenceNumberPPGEnable() {
        return sequenceNumberPPGEnable;
    }

    public double getSequenceNumberPPGFrequency() {
        return sequenceNumberPPGFrequency;
    }

    public boolean isRawPPGEnable() {
        return rawPPGEnable;
    }

    public double getRawPPGFrequency() {
        return rawPPGFrequency;
    }

    public boolean isMagnetometerEnable() {
        return magnetometerEnable;
    }

    public double getMagnetometerFrequency() {
        return magnetometerFrequency;
    }

    public boolean getMagnetometerSensitivityEnable() {
        return magnetometerSensitivityEnable;
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
        private MotionSenseHRVPlusGen2Settings settings;
        public Builder(){
            settings = new MotionSenseHRVPlusGen2Settings();
        }
        public Builder setDefault(){
            settings.debugEnable = true;
            settings.minimumConnectionInterval = 10;
            settings.accelerometerEnable = true;
            settings.accelerometerFrequency = 25;
            settings.accelerometerSensitivity = 4;
            settings.dataQualityAccelerometerEnable = true;
            settings.gyroscopeEnable=true;
            settings.gyroscopeFrequency = 25;
            settings.gyroscopeSensitivity = 500;
            settings.sequenceNumberMotionEnable = true;
            settings.sequenceNumberMotionFrequency = 25;
            settings.rawMotionEnable = true;
            settings.rawMotionFrequency = 25;
            settings.batteryEnable= true;

            settings.ppgEnable = true;
            settings.ppgFrequency = 25;
            settings.ppgFilteredEnable = false;
            settings.ppgRed = 62;
            settings.ppgGreen = 104;
            settings.ppgInfrared = 20;
            settings.dataQualityPPGEnable = true;
            settings.sequenceNumberPPGEnable = true;
            settings.sequenceNumberPPGFrequency = 25;
            settings.rawPPGEnable =true;
            settings.rawPPGFrequency=25;

            settings.magnetometerEnable = true;
            settings.magnetometerFrequency = 25;
            settings.magnetometerSensitivityEnable = true;
            settings.rawMagnetometerEnable = true;
            settings.rawMagnetometerFrequency = 12.5;
            settings.sequenceNumberMagnetometerEnable = true;
            settings.sequenceNumberMagnetometerFrequency = 12.5;
            return this;
        }
        public Builder debugEnable(boolean enable){
            settings.debugEnable = enable;
            return this;
        }
        public Builder minimumConnectionInterval(int minimumConnectionInterval){
            settings.minimumConnectionInterval = minimumConnectionInterval;
            return this;
        }
        public Builder accelerometerEnable(boolean enable){
            settings.accelerometerEnable = enable;
            return this;
        }
        public Builder accelerometerFrequency(double frequency){
            settings.accelerometerFrequency = frequency;
            settings.gyroscopeFrequency = frequency;
            settings.sequenceNumberMotionFrequency = frequency;
            settings.rawMotionFrequency = frequency;
            return this;
        }
        public Builder accelerometerSensitivity(int sensitivity){
            settings.accelerometerSensitivity = sensitivity;
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
        public Builder gyroscopeFrequency(double frequency){
            settings.accelerometerFrequency = frequency;
            settings.gyroscopeFrequency = frequency;
            settings.sequenceNumberMotionFrequency = frequency;
            settings.rawMotionFrequency = frequency;
            return this;
        }
        public Builder gyroscopeSensitivity(int sensitivity){
            settings.gyroscopeSensitivity = sensitivity;
            return this;
        }
        public Builder sequenceNumberMotionEnable(boolean enable){
            settings.sequenceNumberMotionEnable = enable;
            return this;
        }
        public Builder sequenceNumberMotionFrequency(double frequency){
            settings.accelerometerFrequency = frequency;
            settings.gyroscopeFrequency = frequency;
            settings.sequenceNumberMotionFrequency = frequency;
            settings.rawMotionFrequency = frequency;
            return this;
        }
        public Builder rawMotionEnable(boolean enable){
            settings.rawMotionEnable = enable;
            return this;
        }
        public Builder rawMotionFrequency(double frequency){
            settings.accelerometerFrequency = frequency;
            settings.gyroscopeFrequency = frequency;
            settings.sequenceNumberMotionFrequency = frequency;
            settings.rawMotionFrequency = frequency;
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
        public Builder ppgFrequency(double frequency){
            settings.ppgFrequency=frequency;
            settings.rawPPGFrequency=frequency;
            settings.sequenceNumberPPGFrequency = frequency;
            return this;
        }
        public Builder ppgFilteredEnable(boolean enable){
            settings.ppgFilteredEnable = enable;
            return this;
        }
        public Builder ppgRed(int value){
            settings.ppgRed = value;
            return this;
        }
        public Builder ppgGreen(int value){
            settings.ppgGreen = value;
            return this;
        }
        public Builder ppgInfrared(int value){
            settings.ppgInfrared = value;
            return this;
        }
        public Builder dataQualityPPGEnable(boolean enable){
            settings.dataQualityPPGEnable = enable;
            return this;
        }
        public Builder sequenceNumberPPGEnable(boolean enable){
            settings.sequenceNumberPPGEnable = enable;
            return this;
        }
        public Builder sequenceNumberPPGFrequency(double frequency){
            settings.ppgFrequency=frequency;
            settings.rawPPGFrequency=frequency;
            settings.sequenceNumberPPGFrequency = frequency;
            return this;
        }
        public Builder rawPPGEnable(boolean enable){
            settings.rawPPGEnable = enable;
            return this;
        }
        public Builder rawPPGFrequency(double frequency){
            settings.ppgFrequency=frequency;
            settings.rawPPGFrequency=frequency;
            settings.sequenceNumberPPGFrequency = frequency;
            return this;
        }


        public Builder magnetometerEnable(boolean enable){
            settings.magnetometerEnable = enable;
            return this;
        }
        public Builder magnetometerFrequency(double frequency){
            settings.magnetometerFrequency=frequency;
            settings.rawMagnetometerFrequency=frequency;
            settings.sequenceNumberMagnetometerFrequency = frequency;
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
        public Builder rawMagnetometerFrequency(double frequency){
            settings.magnetometerFrequency=frequency;
            settings.rawMagnetometerFrequency=frequency;
            settings.sequenceNumberMagnetometerFrequency = frequency;
            return this;
        }
        public Builder sequenceNumberMagnetometerEnable(boolean enable){
            settings.sequenceNumberMagnetometerEnable = enable;
            return this;
        }
        public Builder sequenceNumberMagnetometerFrequency(double frequency){
            settings.magnetometerFrequency=frequency;
            settings.rawMagnetometerFrequency=frequency;
            settings.sequenceNumberMagnetometerFrequency = frequency;
            return this;
        }
        public DeviceSettings build() {
            return settings;
        }
    }

}
