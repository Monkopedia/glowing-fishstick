package com.ttlock.bl.sdk.entity

import com.ttlock.bl.sdk.entity.NBAwakeTimeType.TIME_INTERVAL
import com.ttlock.bl.sdk.entity.NBAwakeTimeType.TIME_POINT
import com.ttlock.bl.sdk.entity.NBAwakeTimeType.UNKNOWN

/**
 * Created by TTLock on 2020/9/14.
 */
enum class NBAwakeTimeType(private var value: Byte) {
    UNKNOWN(0x00.toByte()), TIME_POINT(0x01.toByte()), TIME_INTERVAL(0x02.toByte());

    fun getValue(): Byte {
        return value
    }

    private fun setValue(value: Byte) {
        this.value = value
    }

    companion object {
        fun getInstance(value: Int): NBAwakeTimeType {
            when (value) {
                0x01 -> return NBAwakeTimeType.TIME_POINT
                0x02 -> return NBAwakeTimeType.TIME_INTERVAL
            }
            NBAwakeTimeType.UNKNOWN.setValue(value.toByte())
            return NBAwakeTimeType.UNKNOWN
        }
    }
}
