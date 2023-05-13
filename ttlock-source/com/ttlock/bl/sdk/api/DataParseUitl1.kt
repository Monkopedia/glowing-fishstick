package com.ttlock.bl.sdk.api

import com.ttlock.bl.sdk.entity.NBAwakeMode
import com.ttlock.bl.sdk.entity.NBAwakeTime
import com.ttlock.bl.sdk.entity.NBAwakeTimeType
import com.ttlock.bl.sdk.entity.PowerSaverWorkMode
import java.util.ArrayList

/**
 * Created by TTLock on 2020/9/14.
 */
internal object DataParseUitl {
    fun parseNBActivateConfig(data: ByteArray?): List<NBAwakeTime> {
        val nbAwakeTimes: MutableList<NBAwakeTime> = ArrayList()
        if (data == null || data.size == 0) {
            return nbAwakeTimes
        }
        var index = 0
        val cnt = data[index++].toInt()
        while (index < data.size) {
            val nbAwakeTime = NBAwakeTime()
            nbAwakeTime.setNbAwakeTimeType(
                NBAwakeTimeType.Companion.getInstance(data[index++].toInt()))
            nbAwakeTime.setMinutes( data[index++] * 60 + data[index++])
            nbAwakeTimes.add(nbAwakeTime)
        }
        return nbAwakeTimes
    }

    fun parseNBActivateMode(activateMode: Byte): List<NBAwakeMode> {
        val nbAwakeModes: MutableList<NBAwakeMode> = ArrayList()
        for (nbAwakeMode in NBAwakeMode.values()) {
            if ((activateMode.toInt() and nbAwakeMode.getValue().toInt()) != 0) {
                nbAwakeModes.add(nbAwakeMode)
            }
        }
        return nbAwakeModes
    }

    fun parsePowerWorkModes(data: ByteArray?): List<PowerSaverWorkMode> {
        val powerSaverWorkModes: MutableList<PowerSaverWorkMode> = ArrayList()
        if (data == null || data.size == 0) {
            return powerSaverWorkModes
        }
        var index = 0
        val len = data[index++].toInt()
        val powerWorkModeValue = data[index++].toInt()
        for (powerSaverWorkMode in PowerSaverWorkMode.values()) {
            if (powerWorkModeValue and powerSaverWorkMode.value != 0) {
                powerSaverWorkModes.add(powerSaverWorkMode)
            }
        }
        return powerSaverWorkModes
    }
}
