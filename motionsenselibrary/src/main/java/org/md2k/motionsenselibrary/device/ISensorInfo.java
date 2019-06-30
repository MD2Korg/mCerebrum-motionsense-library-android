package org.md2k.motionsenselibrary.device;

public interface ISensorInfo {
    public interface ISensorType {
        ITitle setSensorType(SensorType sensorType);
    }

    public interface ITitle {
        IDescription setTitle(String title);
    }

    public interface IDescription {
        IFields setDescription(String description);
    }

    public interface IFields {
        IUnit setFields(String[] fields);
    }

    public interface IUnit {
        IRange setUnit(String unit);
    }

    public interface IRange {
        IBuild setRange(int minValue, int maxValue);
    }

    public interface IBuild {
        SensorInfo build();
    }

}
