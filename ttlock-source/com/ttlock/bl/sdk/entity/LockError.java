package com.ttlock.bl.sdk.entity;

/**
 * Created on  2019/4/11 0011 16:01
 *
 * @author theodre
 */
public enum LockError {

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
    LOCK_NO_POWER(0x0a, "run out of battery", "run out of battery"),//低电量提示
    LOCK_INIT_KEYBOARD_FAILED(0x0b, "initialize keyboard password falied", "initialize keyboard password falied"),//初始化(重置)键盘密码出错
    LOCK_KEY_FLAG_INVALID(0x0d, "invalid ekey, lock flag position is low", "invalid ekey, lock flag position is low"),//电子钥匙失效flag过低
    LOCK_USER_TIME_EXPIRED(0x0e, "ekey expired", "ekey expired"),//电子钥匙过期
    LOCK_PASSWORD_LENGTH_INVALID(0x0f, "invalid password length", "invalid password length"),//无效的密码长度
    LOCK_SUPER_PASSWORD_IS_SAME_WITH_DELETE_PASSWORD(0x10, "admin super password is same with delete password", "admin super password is same with delete password"),//管理员密码与删除密码相同
    LOCK_USER_TIME_INEFFECTIVE(0x11, "ekey hasn't become effective", "ekey hasn't become effective"),//电子钥匙尚未生效
    LOCK_USER_NOT_LOGIN(0x12, "user not login", "user not login"),//未登录,无操作权限
    LOCK_OPERATE_FAILED(0x13, "Failed. Undefined error.", "Failed. Undefined error."),//失败，
    LOCK_PASSWORD_EXIST(0x14, "password already exists.", "password already exists."),//添加的密码已经存在
    LOCK_PASSWORD_NOT_EXIST(0x15, "password not exist or never be used", "password not exist or never be used"),//删除或者修改的密码不存在
    LOCK_NO_FREE_MEMORY(0x16, "out of memory", "out of memory"),//存储空间不足(比如添加密码时，超过存储容量)
    NO_DEFINED_ERROR(0x17,"undefined error","undefined error"), //参数长度错误
    IC_CARD_NOT_EXIST(0x18, "Card number not exist.", "Card number not exist."),//卡号不存在

    FINGER_PRINT_NOT_EXIST(0x1a, "Finger print not exist.", "Finger print not exist."),//指纹不存在
    INVALID_COMMAND(0x1b, "Invalid command", "Invalid command"),    //无效的命令
    LOCK_FROZEN(0x1c, "lock frozen", "lock frozen"),  //锁被冻结
    INVALID_VENDOR(0x1d, "invalid vendor string", "invalid vendor string"),//无效特殊字符串, 不同客户使用特定的字符串
    LOCK_REVERSE(0x1e, "double locked", "double locked"),//门反锁了，普通用户不允许开锁
    RECORD_NOT_EXIST(0x1f, "record not exist", "record not exist"),//记录不存在


    INVALID_PARAM(0x20, "invalid param", "invalid param"),  //参数错误，一般是写入锁的数据有问题
    PARKING_LOCK_LOCKED_FAILED(0x21, "Maybe there are obstacles or cars above the parking lock", "Maybe there are obstacles or car above the parking lock"), //车位锁上有障碍物或者有车停在上面，不允许进行闭锁操作(即车位锁升起)
    /**
     * Custom failed code
     */
    Failed(-1, "failed", "failed"), //失败

    /**
     * WIFI部分
     */
    //命令接收成功，正在处理
    COMMAND_RECEIVED(0x24, "command received", "command received"),
    BAD_WIFI_NAME(0x25, "bad wifi name", "bad wifi name"),
    BAD_WIFI_PASSWORD(0x26, "bad wifi password", "bad wifi password"),

    /**
     * customized error code
     */
    AES_PARSE_ERROR(0x30, "aes parse error", "aes parse error"),  //解密失败
    KEY_INVALID(0x31, "key invalid, may be reset", "key invalid, may be reset"),//钥匙无效(锁可能被重置),解密失败
//    LOCK_NOT_SUPPORT_CHANGE_PASSCODE(0x60, "the lock doesn't support to modify password.", "the lock doesn't support to modify password."),

    LOCK_CONNECT_FAIL(0x400,"lock connect time out","lock connect time out"), //连接锁超时
    LOCK_IS_BUSY(0x401,"only one command can be proceed at a time","lock is busy"), //接口是单任务的，每次只能执行一条指令
//    LOCK_IS_DISCONNECTED(0x402,"lock is disconnected","lock is disconnected"),
    DATA_FORMAT_ERROR(0x403,"parameter format or content is incorrect","parameter error"), //数据错误，一般是传入的数据格式不满足要求
    LOCK_IS_NOT_SUPPORT(0x404,"lock doesn't support this operation","lock doesn't support this operation"), //锁不支持当前操作
    BLE_SERVER_NOT_INIT(0x405,"bluetooth is disable","not init or bluetooth is disable"), //蓝牙不可用，可能没进行初始化操作

    SCAN_FAILED_ALREADY_START(0x406,"fails to start scan as BLE scan with the same settings is already started by the app","BLE scan already started by the app"),
    SCAN_FAILED_APPLICATION_REGISTRATION_FAILED(0x407,"fails to start scan as app cannot be registered","fails to start scan as app cannot be registered"),
    SCAN_FAILED_INTERNAL_ERROR(0x408,"fails to start scan due to an internal error","fails to start scan due an internal error"),
    SCAN_FAILED_FEATURE_UNSUPPORTED(0x409,"fails to start power optimized scan as this feature is not supported","fails to start power optimized scan as this feature is not supported"),
    SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES(0x410,"fails to start scan as it is out of hardware resources","fails to start scan as it is out of hardware resources"),
    INIT_WIRELESS_KEYBOARD_FAILED(0x411,"add keyboard failed","failed to init wireless keyboard"), //无线键盘初始化失败
    WIRELESS_KEYBOARD_NO_RESPONSE(0x412,"wireless keyboard no response","time out"),//接口调用，无线键盘没有任何响应
    DEVICE_CONNECT_FAILED(0x413,"device connect time out","device connect time out");//设备连接超时


    private LockError(int errorCode, String description, String errorMsg) {
        this.errorCode = errorCode;
        this.description = description;
        this.errorMsg = errorMsg;
    }

    public static LockError getInstance(int errorCode) {
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
            case 0x17:
                return NO_DEFINED_ERROR;
            case 0x18:
                return IC_CARD_NOT_EXIST;
            case 0x1a:
                return FINGER_PRINT_NOT_EXIST;
            case 0x1b:
                return INVALID_COMMAND;
            case 0x1c:
                return LOCK_FROZEN;
            case 0x1d:
                return INVALID_VENDOR;
            case 0x1e:
                return LOCK_REVERSE;
            case 0x1f:
                return RECORD_NOT_EXIST;
            case 0x20:
                return INVALID_PARAM;
            case 0x21:
                return PARKING_LOCK_LOCKED_FAILED;
            case 0x24:
                return COMMAND_RECEIVED;
            case 0x25:
                return BAD_WIFI_NAME;
            case 0x26:
                return BAD_WIFI_PASSWORD;
        }
        return Failed;
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

    private String sdkLog;

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


    public String getDescription() {
        return description;
    }


    public String getErrorCode() {
        return String.format("%#x", errorCode);
    }

    public int getIntErrorCode() {
        return errorCode;
    }

    public String getSdkLog() {
        return sdkLog;
    }

    public void setSdkLog(String sdkLog) {
        this.sdkLog = sdkLog;
    }
}
