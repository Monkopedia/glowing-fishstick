package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2020/12/17.
 */
enum class AccessoryType(value: Int) {
    WIRELESS_KEYPAD(1), REMOTE(2);

    var value: Byte

    init {
        this.value = value.toByte()
    }

    fun getValue(): Byte {
        return value
    }

    fun setValue(value: Byte) {
        this.value = value
    }

    companion object {
        fun getInstance(value: Int): AccessoryType? {
            when (value) {
                1 -> return AccessoryType.WIRELESS_KEYPAD
                2 -> return AccessoryType.REMOTE
            }
            return null
        }
    }
}
