package com.ttlock.bl.sdk.entity;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
@Deprecated
public enum Error {

    SUCCESS(0, "success", "success"),
    /**
     * lock error code
     */
    LOCK_CRC_CHECK_ERROR(0x01, "CRC error", "CRC error"),//CRC校验出错
    LOCK_NO_PERMISSION(0x02, "Not administrator, has no permission.", "Not administrator, has no permission."),//非管理员没有操作权限
    LOCK_ADMIN_CHECK_ERROR(0x03, "Wrong administrator password.", "Wrong administrator password."),//管理员校验出错
    LOCK_IS_IN_SETTING_MODE(0x05, "lock is in setting mode", "lock is in setting mode"),//当前处于设置状态
    LOCK_NOT_EXIST_ADMIN(0x06, "lock has no administrator", "lock has no administrator"),//锁中不存在管理员
    LOCK_IS_IN_NO_SETTING_MODE(0x07, "Non-setting mode", "Non-setting mode"),//添加管理员处于非设置模式
    LOCK_DYNAMIC_PWD_ERROR(0x08, "invalid dynamic code", "invalid dynamic code"),//动态码错误
    LOCK_NO_POWER(0x0a, "run out of battery", "run out of battery"),
    LOCK_INIT_KEYBOARD_FAILED(0x0b, "initialize keyboard password falied", "initialize keyboard password falied"),//初始化(重置)键盘密码出错
    LOCK_KEY_FLAG_INVALID(0x0d, "invalid ekey, lock flag position is low", "invalid ekey, lock flag position is low"),//电子钥匙失效flag过低
    LOCK_USER_TIME_EXPIRED(0x0e, "ekey expired", "ekey expired"),//电子钥匙过期
    LOCK_PASSWORD_LENGTH_INVALID(0x0f, "invalid password length", "invalid password length"),
    LOCK_SUPER_PASSWORD_IS_SAME_WITH_DELETE_PASSWORD(0x10, "admin super password is same with delete password", "admin super password is same with delete password"),//管理员密码与删除密码相同
    LOCK_USER_TIME_INEFFECTIVE(0x11, "ekey hasn't become effective", "ekey hasn't become effective"),//电子钥匙尚未生效
    LOCK_USER_NOT_LOGIN(0x12, "user not login", "user not login"),//未登录,无操作权限
    LOCK_OPERATE_FAILED(0x13, "Failed. Undefined error.", "Failed. Undefined error."),
    LOCK_PASSWORD_EXIST(0x14, "password already exists.", "password already exists."),//添加的密码已经存在
    LOCK_PASSWORD_NOT_EXIST(0x15, "password not exists.", "password not exists."),//删除或者修改的密码不存在
    LOCK_NO_FREE_MEMORY(0x16, "out of memory", "out of memory"),//存储空间不足(比如添加密码时，超过存储容量)
    IC_CARD_NOT_EXIST(0x18, "Card number not exist.", "Card number not exist."),
    FR_NOT_EXIST(0x1a, "Finger print not exist.", "Finger print not exist."),
    INVALID_COMMAND(0x1b, "Invalid command", "Invalid command"),
    INVALID_VENDOR(0x1d, "invalid vendor string", "invalid vendor string"),//无效特殊字符串,客户使用专业字符串
    LOCK_REVERSE(0x1e, "", ""),//门反锁了，普通用户不允许开锁
    RECORD_NOT_EXIST(0x1f, "record not exist", "record not exist"),
    /**
     * customized error code
     */
    AES_PARSE_ERROR(0x30, "aes parse error", "aes parse error"),
    KEY_INVALID(0x31, "key invalid, may be reset", "key invalid, may be reset"),//钥匙无效(锁可能被重置),解密失败
    LOCK_NOT_SUPPORT_CHANGE_PASSCODE(0x60, "the lock doesn't support to modify password.", "the lock doesn't support to modify password.");


    private Error(int errorCode, String description, String errorMsg) {
        this.errorCode = errorCode;
        this.description = description;
        this.errorMsg = errorMsg;
    }

    public static Error getInstance(int errorCode) {
        switch (errorCode) {
            case 0:
                return SUCCESS;
            case 0x01:
                return LOCK_CRC_CHECK_ERROR;
            case 0x02:
                return LOCK_NO_PERMISSION;
            case 0x03:
                return LOCK_ADMIN_CHECK_ERROR;
            case 0x05:
                return LOCK_IS_IN_SETTING_MODE;
            case 0x06:
                return LOCK_NOT_EXIST_ADMIN;
            case 0x07:
                return LOCK_IS_IN_NO_SETTING_MODE;
            case 0x08:
                return LOCK_DYNAMIC_PWD_ERROR;
            case 0x0a:
                return LOCK_NO_POWER;
            case 0x0b:
                return LOCK_INIT_KEYBOARD_FAILED;
            case 0x0d:
                return LOCK_KEY_FLAG_INVALID;
            case 0x0e:
                return LOCK_USER_TIME_EXPIRED;
            case 0x0f:
                return LOCK_PASSWORD_LENGTH_INVALID;
            case 0x10:
                return LOCK_SUPER_PASSWORD_IS_SAME_WITH_DELETE_PASSWORD;
            case 0x11:
                return LOCK_USER_TIME_INEFFECTIVE;
            case 0x12:
                return LOCK_USER_NOT_LOGIN;
            case 0x13:
                return LOCK_OPERATE_FAILED;
            case 0x14:
                return LOCK_PASSWORD_EXIST;
            case 0x15:
                return LOCK_PASSWORD_NOT_EXIST;
            case 0x16:
                return LOCK_NO_FREE_MEMORY;
            case 0x18:
                return IC_CARD_NOT_EXIST;
            case 0x1a:
                return FR_NOT_EXIST;
            case 0x1b:
                return INVALID_COMMAND;
            case 0x1d:
                return INVALID_VENDOR;
            case 0x1e:
                return LOCK_REVERSE;
            case 0x1f:
                return RECORD_NOT_EXIST;
        }
        return null;
    }


    private String lockname;


    private String lockmac;


    private int errorCode;


    private String errorMsg;


    private String description;


    private byte command;

    /**
     * error time
     */
    private long date;

    public String getLockname() {
        return lockname;
    }

    public void setLockname(String lockname) {
        this.lockname = lockname;
    }

    public String getLockmac() {
        return lockmac;
    }

    public void setLockmac(String lockmac) {
        this.lockmac = lockmac;
    }

    public String getCommand() {
        if(command >= 'A' && command <= 'Z')
            return String.valueOf((char) command);
        return String.format("%#x", command);
    }

    public void setCommand(byte command) {
        this.command = command;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

//    public void setErrorMsg(String errorMsg) {
//        this.errorMsg = errorMsg;
//    }

    public String getDescription() {
        return description;
    }

//    public void setDescription(String description) {
//        this.description = description;
//    }

    public String getErrorCode() {
        return String.format("%#x", errorCode);
    }

//    public void setErrorCode(int errorCode) {
//        this.errorCode = errorCode;
//    }
}
