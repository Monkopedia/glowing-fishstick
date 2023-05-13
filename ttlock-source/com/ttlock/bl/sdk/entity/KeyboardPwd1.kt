package com.ttlock.bl.sdk.entity

/**
 * 900个密码
 */
class KeyboardPwd private constructor() {
    private var oneDay: String? = null
    private var oneDaySequence = 0
    private var twoDays: String? = null
    private var twoDaysSequence = 0
    private var threeDays: String? = null
    private var threeDaysSequence = 0
    private var fourDays: String? = null
    private var fourDaysSequence = 0
    private var fiveDays: String? = null
    private var fiveDaysSequence = 0
    private var sixDays: String? = null
    private var sixDaysSequence = 0
    private var sevenDays: String? = null
    private var sevenDaysSequence = 0
    private var tenMinutes: String? = null
    private var tenMinutesSequence = 0
    fun getOneDaySequence(): Int {
        return oneDaySequence
    }

    fun setOneDaySequence(oneDaySequence: Int) {
        this.oneDaySequence = oneDaySequence
    }

    fun getTwoDaysSequence(): Int {
        return twoDaysSequence
    }

    fun setTwoDaysSequence(twoDaysSequence: Int) {
        this.twoDaysSequence = twoDaysSequence
    }

    fun getThreeDaysSequence(): Int {
        return threeDaysSequence
    }

    fun setThreeDaysSequence(threeDaysSequence: Int) {
        this.threeDaysSequence = threeDaysSequence
    }

    fun getFourDaysSequence(): Int {
        return fourDaysSequence
    }

    fun setFourDaysSequence(fourDaysSequence: Int) {
        this.fourDaysSequence = fourDaysSequence
    }

    fun getFiveDaysSequence(): Int {
        return fiveDaysSequence
    }

    fun setFiveDaysSequence(fiveDaysSequence: Int) {
        this.fiveDaysSequence = fiveDaysSequence
    }

    fun getSixDaysSequence(): Int {
        return sixDaysSequence
    }

    fun setSixDaysSequence(sixDaysSequence: Int) {
        this.sixDaysSequence = sixDaysSequence
    }

    fun getSevenDaysSequence(): Int {
        return sevenDaysSequence
    }

    fun setSevenDaysSequence(sevenDaysSequence: Int) {
        this.sevenDaysSequence = sevenDaysSequence
    }

    fun getTenMinutesSequence(): Int {
        return tenMinutesSequence
    }

    fun setTenMinutesSequence(tenMinutesSequence: Int) {
        this.tenMinutesSequence = tenMinutesSequence
    }

    fun getOneDay(): String? {
        return oneDay
    }

    fun setOneDay(oneDay: String?) {
        this.oneDay = oneDay
    }

    fun getTwoDays(): String? {
        return twoDays
    }

    fun setTwoDays(twoDays: String?) {
        this.twoDays = twoDays
    }

    fun getThreeDays(): String? {
        return threeDays
    }

    fun setThreeDays(threeDays: String?) {
        this.threeDays = threeDays
    }

    fun getFourDays(): String? {
        return fourDays
    }

    fun setFourDays(fourDays: String?) {
        this.fourDays = fourDays
    }

    fun getFiveDays(): String? {
        return fiveDays
    }

    fun setFiveDays(fiveDays: String?) {
        this.fiveDays = fiveDays
    }

    fun getSixDays(): String? {
        return sixDays
    }

    fun setSixDays(sixDays: String?) {
        this.sixDays = sixDays
    }

    fun getSevenDays(): String? {
        return sevenDays
    }

    fun setSevenDays(sevenDays: String?) {
        this.sevenDays = sevenDays
    }

    fun getTenMinutes(): String? {
        return tenMinutes
    }

    fun setTenMinutes(tenMinutes: String?) {
        this.tenMinutes = tenMinutes
    }

    companion object {
        var ONE_DAY_PWD = 1
        var TWO_DAYS_PWD = 2
        var THREE_DAYS_PWD = 3
        var FOUR_DAYS_PWD = 4
        var FIVE_DAYS_PWD = 5
        var SIX_DAYS_PWD = 6
        var SEVEN_DAYS_PWD = 7
        var TEN_MINUTES_PWD = 8
    }
}
