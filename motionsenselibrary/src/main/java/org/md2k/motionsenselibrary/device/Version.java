package org.md2k.motionsenselibrary.device;

import androidx.annotation.NonNull;

/*
 * Copyright (c) 2018, The University of Memphis, MD2K Center
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
public class Version {
    private int major;
    private int minor;
    private DeviceType type;
    private int patch;
    public Version(String versionStr){
        String[] split = versionStr.split("\\.");
        this.major = Integer.parseInt(split[0]);
        this.minor = Integer.parseInt(split[1]);
        this.type = DeviceType.getDeviceType(Integer.parseInt(split[2]));
        this.patch = Integer.parseInt(split[3]);
    }
    public Version(DeviceType deviceType, int major, int minor, int patch){
        this.type = deviceType;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }
    public boolean isVersion1(){
        return major==1;
    }
    public boolean isVersion2(){
        return major!=1;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public DeviceType getType() {
        return type;
    }

    Version(byte[] data) {
        this.major = (int) data[0];
        this.minor = (int) data[1];
        this.patch = (int) data[3];
        this.type = DeviceType.getDeviceType(data[2]);
    }

    @NonNull
    @Override
    public String toString() {
        String result = "";
        result += this.major + ".";
        result += this.minor + ".";
        result += this.type.getId()+ ".";
        result += Integer.toString(this.patch);
        return result;
    }

    /*
     * motionSenseHRV (acc + Gyro + PPG): 0x01
     * motionSenseHRV+ (acc + Gyro + PPG + magnetometer): 0x02
     * motionBand (acc + Gyro): 0x03
     * motionSenseHRV+_Gen_2 (acc + Gyro + PPG new hardware + magnetometer): 0x04
     */
/*
    public enum EETECH_HARDWARE_VERSIONS {
        HRV((byte) 0x01),
        HRV_PLUS((byte) 0x02),
        MOTIONSENSE((byte) 0x03),
        HRV_GEN_2((byte) 0x04),
        AUTOSENSE((byte) 0x05);

        private byte value;

        EETECH_HARDWARE_VERSIONS(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return this.value;
        }
    }
*/
}
