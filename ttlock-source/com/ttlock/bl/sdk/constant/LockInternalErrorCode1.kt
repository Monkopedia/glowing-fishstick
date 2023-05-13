package com.ttlock.bl.sdk.constant

/**
 * Created by Smartlock on 2016/5/31.
 */
@Deprecated("")
object LockInternalErrorCode {
    /**
     * CRC校验出错
     * CRC error
     */
    const val LOCK_CRC_CHECK_ERROR: Byte = 0x01

    /**
     * 非管理员没有操作权限
     * Not administrator, has no permission.
     */
    const val LOCK_NO_PERMISSION: Byte = 0x02

    /**
     * 校验出错
     * Wrong administrator password.
     */
    const val LOCK_ADMIN_CHECK_ERROR: Byte = 0x03

    /**
     * 锁中不存在管理员
     * no administrator
     */
    const val LOCK_NOT_EXIST_ADMIN: Byte = 0x06

    /**
     * 添加管理处于非设置模式
     * Non-setting mode
     */
    const val LOCK_IS_IN_NO_SETTING_MODE: Byte = 0x07

    /**
     * 动态码错误
     * invalid dynamic code
     */
    const val LOCK_DYNAMIC_PWD_ERROR: Byte = 0x08

    /**
     * 电池快没电了
     * Running out of battery
     */
    const val LOCK_NO_POWER: Byte = 0x0a

    /**
     * 过期
     * expired
     */
    const val LOCK_USER_TIME_EXPIRED: Byte = 0x0e

    /**
     * 未生效
     * Haven't become effective
     */
    const val LOCK_USER_TIME_INEFFECTIVE: Byte = 0x11

    /**
     * 操作失败 未定义的错误
     * Failed. Undefined error.
     */
    const val LOCK_OPERATE_FAILED: Byte = 0x13

    /**
     * 添加的密码已经存在
     * password already exists.
     */
    const val LOCK_PASSWORD_EXIST: Byte = 0x14

    /**
     * 删除或者修改的密码不存在
     * password not exists.
     */
    const val LOCK_PASSWORD_NOT_EXIST: Byte = 0x15

    /**
     * 存储空间不足(比如添加密码时，超过存储容量)
     * out of memory
     */
    const val LOCK_NO_FREE_MEMORY: Byte = 0x16
}
