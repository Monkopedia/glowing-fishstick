package com.ttlock.bl.sdk.api

/**
 * Created by TTLock on 2019/1/24.
 */
class PassageModeData {
    var type = 0
    var weekOrDay = 0
    var month = 0
    var startDate = 0
    var endDate = 0
    fun getType(): Int {
        return type
    }

    fun setType(type: Int) {
        this.type = type
    }

    fun getWeekOrDay(): Int {
        return weekOrDay
    }

    fun setWeekOrDay(weekOrDay: Int) {
        this.weekOrDay = weekOrDay
    }

    fun getMonth(): Int {
        return month
    }

    fun setMonth(month: Int) {
        this.month = month
    }

    fun getStartDate(): Long {
        return startDate.toLong()
    }

    fun setStartDate(startDate: Int) {
        this.startDate = startDate
    }

    fun getEndDate(): Long {
        return endDate.toLong()
    }

    fun setEndDate(endDate: Int) {
        this.endDate = endDate
    }
}
