package org.md2k.motionsenselibrary.device;

public enum DeviceType {
    MOTION_SENSE(3),
    MOTION_SENSE_HRV(1),
    MOTION_SENSE_HRV_PLUS(2),
    MOTION_SENSE_HRV_PLUS_GEN2(4),
    MOTION_SENSE_HRV_PLUS_GEN2_IG(5),
    MOTION_SENSE_HRV_PLUS_GEN2_IR(6);
    private int id;
    DeviceType(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }
    static DeviceType getDeviceType(int id){
        for(DeviceType d:DeviceType.values()){
            if(d.id==id) return d;
        }
        return null;
    }
}
