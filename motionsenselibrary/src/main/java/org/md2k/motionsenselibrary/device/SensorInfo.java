package org.md2k.motionsenselibrary.device;

/*
 * Copyright (c) 2016, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class SensorInfo {
    private SensorType sensorType;
    private String title;
    private String description;
    private String[] fields;
    private String unit;
    private long startSampleTime;
    private long lastSampleTime;
    private double[] lastSample;
    private long count;
    private int minValue;
    private int maxValue;


    private SensorInfo() {

    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public long getStartSampleTime() {
        return startSampleTime;
    }

    public void setStartSampleTime(long startSampleTime) {
        this.startSampleTime = startSampleTime;
    }

    public long getLastSampleTime() {
        return lastSampleTime;
    }

    public void setLastSampleTime(long lastSampleTime) {
        this.lastSampleTime = lastSampleTime;
    }

    public double[] getLastSample() {
        return lastSample;
    }

    public void setLastSample(double[] lastSample) {
        this.lastSample = lastSample;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String[] getFields() {
        return fields;
    }

    public String getUnit() {
        return unit;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }
    public static ISensorInfo.ISensorType builder(){
        return new Builder();
    }

    public static class Builder implements ISensorInfo.ISensorType, ISensorInfo.ITitle, ISensorInfo.IDescription, ISensorInfo.IFields, ISensorInfo.IUnit, ISensorInfo.IRange, ISensorInfo.IBuild {
        private SensorInfo sensorInfo;

        public Builder() {
            sensorInfo = new SensorInfo();
        }

        public ISensorInfo.ITitle setSensorType(SensorType sensorType) {
            sensorInfo.sensorType = sensorType;
            return this;
        }

        public ISensorInfo.IDescription setTitle(String title) {
            sensorInfo.title = title;
            return this;
        }

        public ISensorInfo.IFields setDescription(String description) {
            sensorInfo.description = description;
            return this;
        }

        public ISensorInfo.IUnit setFields(String[] fields) {
            sensorInfo.fields = fields;
            return this;
        }

        public ISensorInfo.IRange setUnit(String unit) {
            sensorInfo.unit = unit;
            return this;
        }

        public ISensorInfo.IBuild setRange(int minValue, int maxValue) {
            sensorInfo.minValue = minValue;
            sensorInfo.maxValue = maxValue;
            return this;
        }

        public SensorInfo build() {
            return sensorInfo;
        }
    }

}
