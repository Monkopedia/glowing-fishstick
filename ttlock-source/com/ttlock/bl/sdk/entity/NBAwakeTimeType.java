package com.ttlock.bl.sdk.entity;

/**
 * Created by TTLock on 2020/9/14.
 */
public enum NBAwakeTimeType {
    UNKNOWN((byte)0x00),
    TIME_POINT((byte)0x01),
    TIME_INTERVAL((byte)0x02);

    NBAwakeTimeType(byte value) {
        this.value = value;
    }

    private byte value;

    public byte getValue() {
        return value;
    }

    private void setValue(byte value) {
        this.value = value;
    }

    public static NBAwakeTimeType getInstance(int value) {
        switch (value) {
            case 0x01:
                return TIME_POINT;
            case 0x02:
                return TIME_INTERVAL;
        }
        UNKNOWN.setValue((byte) value);
        return UNKNOWN;
    }
}
