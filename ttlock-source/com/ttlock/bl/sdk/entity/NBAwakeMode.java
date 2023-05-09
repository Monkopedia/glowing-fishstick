package com.ttlock.bl.sdk.entity;

/**
 * Created by TTLock on 2020/9/14.
 */
public enum NBAwakeMode {
//    UNKNOWN((byte)0x00),
    KEYPAD((byte)0x01),
    CARD((byte)0x02),
    FINGERPRINT((byte)0x04);
    //特殊密码激活0x08 "9#"
    private byte value;

    NBAwakeMode(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    private void setValue(byte value) {
        this.value = value;
    }

    public static NBAwakeMode getInstance(int value) {
        switch (value) {
            case 0x01:
                return KEYPAD;
            case 0x02:
                return CARD;
            case 0x04:
                return FINGERPRINT;
        }
//        UNKNOWN.setValue((byte) value);
        return null;
    }
}
