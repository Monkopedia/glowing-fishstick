package com.ttlock.bl.sdk.entity

/**
 * Created on  2019/4/17 0017 10:13
 *
 * @author theodre
 */
class PassageModeConfig {
    private var modeType: PassageModeType? = null

    /**
     * 1~7 周一到周日
     * json数组
     * 0: means ervery day
     */
    private var repeatWeekOrDays: String? = null
    private var month = 0

    /**
     * the minutes of cycle start time
     * 0:0 means all day
     */
    private var startDate = 0

    /**
     * the minutes of cycle end time
     */
    private var endDate = 0
    fun getModeType(): PassageModeType? {
        return modeType
    }

    fun setModeType(modeType: PassageModeType?) {
        this.modeType = modeType
    }

    fun getRepeatWeekOrDays(): String? {
        return repeatWeekOrDays
    }

    fun setRepeatWeekOrDays(repeatWeekOrDays: String?) {
        this.repeatWeekOrDays = repeatWeekOrDays
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
