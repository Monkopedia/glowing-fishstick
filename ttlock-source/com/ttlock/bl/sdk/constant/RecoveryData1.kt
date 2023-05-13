package com.ttlock.bl.sdk.constant

import com.ttlock.bl.sdk.entity.CyclicConfig

/**
 * Created by TTLock on 2017/7/4.
 */
class RecoveryData {
    // todo: check valid
    /**
     * 卡类型：1、普通卡、4-循环
     */
    var cardType = 0

    /**
     * 类型：1、普通、4-循环
     */
    var fingerprintType = 0

    /**
     * 循环操作时间设置（循环类型的才需要传）
     */
    //    public String cyclicConfig;
    var cyclicConfig: List<CyclicConfig>? = null

    /**
     * 键盘密码
     */
    var keyboardPwd: String? = null

    /**
     * 键盘密码类型
     */
    var keyboardPwdType = 0

    /**
     * 循环密码类型
     */
    var cycleType = 0

    /**
     * 卡号
     */
    var cardNumber: String? = null

    /**
     * 指纹号
     */
    var fingerprintNumber: String? = null

    /**
     * 有效期开始时间
     */
    var startDate: Long = 0

    /**
     * 有效期结束时间
     */
    var endDate: Long = 0
}
