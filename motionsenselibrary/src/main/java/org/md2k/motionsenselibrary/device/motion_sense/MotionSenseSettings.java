package org.md2k.motionsenselibrary.device.motion_sense;

import org.md2k.motionsenselibrary.device.DeviceSettings;

public class MotionSenseSettings implements DeviceSettings {
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

    private MotionSenseSettings() {
        accelerometerFrequency = 16;
        gyroscopeFrequency = 32;
        sequenceNumberMotionFrequency = 16;
        dataQualityAccelerometerFrequency = 0.33;
        rawMotionFrequency = 16;
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

    public double getDataQualityAccelerometerFrequency() {
        return dataQualityAccelerometerFrequency;
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

    public static class Builder {
        private MotionSenseSettings settings;

        public Builder() {
            settings = new MotionSenseSettings();
        }

        public Builder setDefault() {
            settings.debugEnable = true;
            rawMotionEnable(true);
            dataQualityAccelerometerEnable(true);
            accelerometerEnable(true);
            gyroscopeEnable(true);
            sequenceNumberMotionEnable(true);
            batteryEnable(true);
            return this;
        }

        public Builder debugEnable(boolean enable) {
            settings.debugEnable = enable;
            return this;
        }

        public Builder dataQualityAccelerometerEnable(boolean enable) {
            settings.dataQualityAccelerometerEnable = enable;
            return this;
        }

        public Builder accelerometerEnable(boolean enable) {
            settings.accelerometerEnable = enable;
            return this;
        }

        public Builder gyroscopeEnable(boolean enable) {
            settings.gyroscopeEnable = enable;
            return this;
        }

        public Builder sequenceNumberMotionEnable(boolean enable) {
            settings.sequenceNumberMotionEnable = enable;
            return this;
        }

        public Builder rawMotionEnable(boolean enable) {
            settings.rawMotionEnable = enable;
            return this;
        }

        public Builder batteryEnable(boolean enable) {
            settings.batteryEnable = enable;
            return this;
        }

        public DeviceSettings build() {
            return settings;
        }
    }
}

