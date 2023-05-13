package com.ttlock.bl.sdk.constant

/**
 * Created by Smartlock on 2016/5/27.
 * TODO:更换锁内部错误码
 */
object ErrorCode {
    /**
     * 成功
     */
    const val SUCCESS: Byte = 0

    /**
     * 过期
     */
    const val LOCK_USER_TIME_EXPIRED: Byte = -4

    /**
     * 管理员密码不正确 校验不通过
     */
    const val LOCK_ADMIN_CHECK_ERROR = -5

    /**
     * 未生效 未到开锁时间
     */
    const val LOCK_USER_TIME_INEFFECTIVE: Byte = -6

    /**
     * 开锁失败
     */
    const val LOCK_UNLOCK_FAIL = -9

    /**
     * CRC校验出错
     */
    const val LOCK_CRC_CHECK_FAIL = -17

    /**
     * 添加管理处于非设置模式
     */
    const val LOCK_IS_IN_NO_SETTING_MODE = -25

    /**
     * 获取AESKey失败
     */
    const val LOCK_GET_AESKEY_FAILED = -26

    /**
     * AES解析出错
     */
    const val LOCK_AES_PARSE_ERROR = -27

    /**
     * 解密失败
     */
    const val KEY_INVALID = -32
}
