package com.ttlock.bl.sdk.entity

import com.ttlock.bl.sdk.util.LogUtil
import java.io.Serializable

/**
 * Created by TTLock on 2020/3/30.
 */
class CyclicConfig : Serializable {
    /**
     * 1-7 means monday to sunday
     */
    var weekDay = 0

    /**
     * the minutes of cycle start time
     */
    var startTime = 0

    /**
     * the minutes of cycle end time
     */
    var endTime = 0

    constructor() {}
    constructor(weekDay: Int, startTime: Int, endTime: Int) {
        this.weekDay = weekDay
        this.startTime = startTime
        this.endTime = endTime
    }

    fun getWeekDay(): Int {
        return weekDay
    }

    fun setWeekDay(weekDay: Int) {
        this.weekDay = weekDay
    }

    fun getStartTime(): Int {
        return startTime
    }

    fun setStartTime(startTime: Int) {
        this.startTime = startTime
    }

    fun getEndTime(): Int {
        return endTime
    }

    fun setEndTime(endTime: Int) {
        this.endTime = endTime
    }

    fun isValidData(): Boolean {
        if (weekDay >= 1 && weekDay <= 7 && startTime >= 0 && startTime <= CyclicConfig.Companion.MAX_DAY_MINUTES && endTime >= 0 && endTime <= CyclicConfig.Companion.MAX_DAY_MINUTES && startTime <= endTime) {
            return true
        }
        LogUtil.d(this.toString())
        return false
    }

    override fun toString(): String {
        return "CyclicConfig{" +
            "weekDay=" + weekDay +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            '}'
    }

    companion object {
        const val MAX_DAY_MINUTES = 24 * 60
    }
}
