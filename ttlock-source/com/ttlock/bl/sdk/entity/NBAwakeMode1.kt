package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2020/9/14.
 */
enum class NBAwakeMode( // 特殊密码激活0x08 "9#"
    private var value: Byte
) {
    //    UNKNOWN((byte)0x00),
    KEYPAD(0x01.toByte()), CARD(0x02.toByte()), FINGERPRINT(0x04.toByte());

    fun getValue(): Byte {
        return value
    }

    private fun setValue(value: Byte) {
        this.value = value
    }

    companion object {
        fun getInstance(value: Int): NBAwakeMode? {
            when (value) {
                0x01 -> return NBAwakeMode.KEYPAD
                0x02 -> return NBAwakeMode.CARD
                0x04 -> return NBAwakeMode.FINGERPRINT
            }
            //        UNKNOWN.setValue((byte) value);
            return null
        }
    }
}
