package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2019/11/15.
 */
enum class PowerSaverWorkMode(var value: Int) {
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

    fun getValue(): Int {
        return value
    }

    fun setValue(value: Int) {
        this.value = value
    }

    companion object {
        fun getInstance(value: Int): PowerSaverWorkMode? {
            when (value) {
                0x01 -> return PowerSaverWorkMode.ALL_CARDS
                0x02 -> return PowerSaverWorkMode.ID_CARD
                0x04 -> return PowerSaverWorkMode.HOTEL_CARD
                0x08 -> return PowerSaverWorkMode.ROOM_CARD
            }
            return null
        }
    }
}
