package com.ttlock.bl.sdk.entity

/**
 * 300个密码
 */
class KeyboardPwdAuto private constructor() {
    /** 当前使用密码的索引，范围：[0,299]，-1表示未使用  */
    private var currentIndex = 0

    /** 四位数密码列表，形式：[1111,2222,3333...]  */
    private var fourKeyboardPwdList: String? = null

    /** 时间对照表  */
    private var timeControlTb: String? = null

    /** 位置，形式：[1,2,3]  */
    private var position: String? = null

    /** 校验数字，长度为10的字符串  */
    private var checkDigit: String? = null
    fun getCurrentIndex(): Int {
        return currentIndex
    }

    fun setCurrentIndex(currentIndex: Int) {
        this.currentIndex = currentIndex
    }

    fun getFourKeyboardPwdList(): String? {
        return fourKeyboardPwdList
    }

    fun setFourKeyboardPwdList(fourKeyboardPwdList: String?) {
        this.fourKeyboardPwdList = fourKeyboardPwdList
    }

    fun getTimeControlTb(): String? {
        return timeControlTb
    }

    fun setTimeControlTb(timeControlTb: String?) {
        this.timeControlTb = timeControlTb
    }

    fun getPosition(): String? {
        return position
    }

    fun setPosition(position: String?) {
        this.position = position
    }

    fun getCheckDigit(): String? {
        return checkDigit
    }

    fun setCheckDigit(checkDigit: String?) {
        this.checkDigit = checkDigit
    }
}
