package com.ttlock.bl.sdk.entity;

/**
 * Created by TTLock on 2017/10/20.
 */

public class Passcode {
    /**
     * 原始密码
     */
    public String keyboardPwd;
    public String newKeyboardPwd;
    public long startDate;
    public long endDate;

    /**
     * 密码类型
     */
    public int keyboardPwdType;

    /**
     * 循环类型
     */
    public int cycleType;
}
