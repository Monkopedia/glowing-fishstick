package com.ttlock.bl.sdk.constant;

import com.ttlock.bl.sdk.entity.CyclicConfig;

import java.util.List;

/**
 * Created by TTLock on 2017/7/4.
 */

public class RecoveryData {//todo: check valid

    /**
     * 卡类型：1、普通卡、4-循环
     */
    public int cardType;

    /**
     * 类型：1、普通、4-循环
     */
    public int fingerprintType;

    /**
     * 循环操作时间设置（循环类型的才需要传）
     */
//    public String cyclicConfig;
    public List<CyclicConfig> cyclicConfig;

    /**
     * 键盘密码
     */
    public String keyboardPwd;

    /**
     * 键盘密码类型
     */
    public int keyboardPwdType;

    /**
     * 循环密码类型
     */
    public int cycleType;

    /**
     * 卡号
     */
    public String cardNumber;

    /**
     * 指纹号
     */
    public String fingerprintNumber;

    /**
     * 有效期开始时间
     */
    public long startDate;

    /**
     * 有效期结束时间
     */
    public long endDate;

}
