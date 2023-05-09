package com.ttlock.bl.sdk.entity;

/**
 * Created by TTLock on 2020/12/17.
 */
public enum AccessoryType {
    WIRELESS_KEYPAD(1),
    REMOTE(2);
    byte value;

    private AccessoryType(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public static AccessoryType getInstance(int value) {
        switch (value) {
            case 1:
                return WIRELESS_KEYPAD;
            case 2:
                return REMOTE;
        }
        return null;
    }

}
