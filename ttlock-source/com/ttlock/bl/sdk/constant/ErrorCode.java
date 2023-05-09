package com.ttlock.bl.sdk.constant;

/**
 * Created by Smartlock on 2016/5/27.
 * TODO:更换锁内部错误码
 */
public class ErrorCode {

    /**
     * 成功
     */
    public static final byte SUCCESS = 0;

    /**
     * 过期
     */
    public static final byte LOCK_USER_TIME_EXPIRED = -4;

    /**
     * 管理员密码不正确 校验不通过
     */
    public static final int LOCK_ADMIN_CHECK_ERROR = -5;
    /**
     * 未生效 未到开锁时间
     */
    public static final byte LOCK_USER_TIME_INEFFECTIVE = -6;

    /**
     * 开锁失败
     */
    public static final int LOCK_UNLOCK_FAIL = -9;

    /**
     * CRC校验出错
     */
    public static final int LOCK_CRC_CHECK_FAIL = -17;
    /**
     * 添加管理处于非设置模式
     */
    public static final int LOCK_IS_IN_NO_SETTING_MODE = -25;

    /**
     * 获取AESKey失败
     */
    public static final int LOCK_GET_AESKEY_FAILED = -26;

    /**
     * AES解析出错
     */
    public static final int LOCK_AES_PARSE_ERROR = -27;

    /**
     * 解密失败
     */
    public static final int KEY_INVALID = -32;

}
