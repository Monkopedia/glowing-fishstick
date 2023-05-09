package com.ttlock.bl.sdk.entity;

/**
 * Created by TTLock on 2019/11/15.
 */
public enum PowerSaverWorkMode {
    ALL_CARDS(0x01),
    /**
     * 身份证取电
     */
    ID_CARD(0x02),
    /**
     * 酒店卡取电
     */
    HOTEL_CARD(0x04),

    /**
     * 房间卡取电
     */
    ROOM_CARD(0x08);


    int value;
    private PowerSaverWorkMode(int value) {
        this.value = value;
    }

    public static PowerSaverWorkMode getInstance(int value) {
        switch (value) {
            case 0x01:
                return ALL_CARDS;
            case 0x02:
                return ID_CARD;
            case 0x04:
                return HOTEL_CARD;
            case 0x08:
                return ROOM_CARD;
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
