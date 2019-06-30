package org.md2k.motionsenselibrary.device;

public enum DataQualityType {
    GOOD(0),
    NO_DATA(1),
    NOT_WORN(2),
    LOOSE_ATTACHMENT(3);
    private int value;
    DataQualityType(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
