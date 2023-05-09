package com.ttlock.bl.sdk.constant;

/**
 * Created by Smartlock on 2016/5/31.
 */
@Deprecated
public class LockInternalErrorCode {
    /**
     * CRC校验出错
	 * CRC error
     */
    public static final byte LOCK_CRC_CHECK_ERROR = 0x01;

    /**
     * 非管理员没有操作权限
	 * Not administrator, has no permission.
     */
    public static final byte LOCK_NO_PERMISSION = 0x02;

    /**
     * 校验出错
	 * Wrong administrator password.
     */
    public static final byte LOCK_ADMIN_CHECK_ERROR = 0x03;

    /**
     * 锁中不存在管理员
	 * no administrator
     */
    public static final byte LOCK_NOT_EXIST_ADMIN = 0x06;

    /**
     * 添加管理处于非设置模式
	 * Non-setting mode 
     */
    public static final byte LOCK_IS_IN_NO_SETTING_MODE = 0x07;

    /**
     * 动态码错误
	 * invalid dynamic code
     */
    public static final byte LOCK_DYNAMIC_PWD_ERROR = 0x08;

    /**
     * 电池快没电了
	 * Running out of battery
     */
    public static final byte LOCK_NO_POWER = 0x0a;

    /**
     * 过期
	 * expired
     */
    public static final byte LOCK_USER_TIME_EXPIRED = 0x0e;

    /**
     * 未生效
	 * Haven't become effective
     */
    public static final byte LOCK_USER_TIME_INEFFECTIVE = 0x11;

    /**
     * 操作失败 未定义的错误
	 * Failed. Undefined error.
     */
    public static final byte LOCK_OPERATE_FAILED = 0x13;

    /**
     * 添加的密码已经存在
	 * password already exists.
     */
    public static final byte LOCK_PASSWORD_EXIST = 0x14;

    /**
     * 删除或者修改的密码不存在
	 * password not exists.
     */
    public static final byte LOCK_PASSWORD_NOT_EXIST = 0x15;

    /**
     * 存储空间不足(比如添加密码时，超过存储容量)
	 * out of memory
     */
    public static final byte LOCK_NO_FREE_MEMORY = 0x16;
}
