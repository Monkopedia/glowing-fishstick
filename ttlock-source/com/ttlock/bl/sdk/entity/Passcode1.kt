package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2017/10/20.
 */
class Passcode {
    /**
     * 原始密码
     */
    var keyboardPwd: String? = null
    var newKeyboardPwd: String? = null
    var startDate: Long = 0
    var endDate: Long = 0

    /**
     * 密码类型
     */
    var keyboardPwdType = 0

    /**
     * 循环类型
     */
    var cycleType = 0
}
