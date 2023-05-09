package com.ttlock.bl.sdk.entity;

public enum SoundVolume {
//    NO_MODIFY(0),
    FIRST_LEVEL(1),
    SECOND_LEVEL(2),
    THIRD_LEVEL(3),
    FOUTH_LEVEL(4),
    FIFTH_LEVEL(5),

    /**
     * 关闭
     */
    OFF(-1),
    /**
     * 开启
     */
    ON(-2);
    private int value;

    private SoundVolume(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SoundVolume getInstance(int value) {
        switch (value) {
            case -1:
                return OFF;
            case -2:
                return ON;
//            case 0:
//                return NO_MODIFY;
            case 1:
                return FIRST_LEVEL;
            case 2:
                return SECOND_LEVEL;
            case 3:
                return THIRD_LEVEL;
            case 4:
                return FOUTH_LEVEL;
            case 5:
                return FIFTH_LEVEL;
            default:
                return OFF;
        }
    }
}
