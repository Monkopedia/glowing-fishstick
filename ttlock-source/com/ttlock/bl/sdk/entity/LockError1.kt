package com.ttlock.bl.sdk.entity

import com.ttlock.bl.sdk.entity.LockError.BAD_WIFI_NAME
import com.ttlock.bl.sdk.entity.LockError.BAD_WIFI_PASSWORD
import com.ttlock.bl.sdk.entity.LockError.COMMAND_RECEIVED
import com.ttlock.bl.sdk.entity.LockError.FINGER_PRINT_NOT_EXIST
import com.ttlock.bl.sdk.entity.LockError.Failed
import com.ttlock.bl.sdk.entity.LockError.IC_CARD_NOT_EXIST
import com.ttlock.bl.sdk.entity.LockError.INVALID_COMMAND
import com.ttlock.bl.sdk.entity.LockError.INVALID_PARAM
import com.ttlock.bl.sdk.entity.LockError.INVALID_VENDOR
import com.ttlock.bl.sdk.entity.LockError.LOCK_ADMIN_CHECK_ERROR
import com.ttlock.bl.sdk.entity.LockError.LOCK_CRC_CHECK_ERROR
import com.ttlock.bl.sdk.entity.LockError.LOCK_DYNAMIC_PWD_ERROR
import com.ttlock.bl.sdk.entity.LockError.LOCK_FROZEN
import com.ttlock.bl.sdk.entity.LockError.LOCK_INIT_KEYBOARD_FAILED
import com.ttlock.bl.sdk.entity.LockError.LOCK_IS_IN_NO_SETTING_MODE
import com.ttlock.bl.sdk.entity.LockError.LOCK_IS_IN_SETTING_MODE
import com.ttlock.bl.sdk.entity.LockError.LOCK_KEY_FLAG_INVALID
import com.ttlock.bl.sdk.entity.LockError.LOCK_NOT_EXIST_ADMIN
import com.ttlock.bl.sdk.entity.LockError.LOCK_NO_FREE_MEMORY
import com.ttlock.bl.sdk.entity.LockError.LOCK_NO_PERMISSION
import com.ttlock.bl.sdk.entity.LockError.LOCK_NO_POWER
import com.ttlock.bl.sdk.entity.LockError.LOCK_OPERATE_FAILED
import com.ttlock.bl.sdk.entity.LockError.LOCK_PASSWORD_EXIST
import com.ttlock.bl.sdk.entity.LockError.LOCK_PASSWORD_LENGTH_INVALID
import com.ttlock.bl.sdk.entity.LockError.LOCK_PASSWORD_NOT_EXIST
import com.ttlock.bl.sdk.entity.LockError.LOCK_REVERSE
import com.ttlock.bl.sdk.entity.LockError.LOCK_SUPER_PASSWORD_IS_SAME_WITH_DELETE_PASSWORD
import com.ttlock.bl.sdk.entity.LockError.LOCK_USER_NOT_LOGIN
import com.ttlock.bl.sdk.entity.LockError.LOCK_USER_TIME_EXPIRED
import com.ttlock.bl.sdk.entity.LockError.LOCK_USER_TIME_INEFFECTIVE
import com.ttlock.bl.sdk.entity.LockError.NO_DEFINED_ERROR
import com.ttlock.bl.sdk.entity.LockError.PARKING_LOCK_LOCKED_FAILED
import com.ttlock.bl.sdk.entity.LockError.RECORD_NOT_EXIST
import com.ttlock.bl.sdk.entity.LockError.SUCCESS

/**
 * Created on  2019/4/11 0011 16:01
 *
 * @author theodre
 */
enum class LockError // 设备连接超时
(private val errorCode: Int, private val description: String, private val errorMsg: String) {
    SUCCESS(0, "success", "success"),

    /**
     * lock error code
     */
    LOCK_CRC_CHECK_ERROR(0x01, "CRC error", "CRC error"), // CRC校验出错
    LOCK_NO_PERMISSION(
        0x02,
        "Not administrator, has no permission.",
        "Not administrator, has no permission."
    ), // 非管理员没有操作权限
    LOCK_ADMIN_CHECK_ERROR(
        0x03,
        "Wrong administrator password.",
        "Wrong administrator password."
    ), // 管理员校验出错
    LOCK_IS_IN_SETTING_MODE(0x05, "lock is in setting mode", "lock is in setting mode"), // 当前处于设置状态
    LOCK_NOT_EXIST_ADMIN(
        0x06,
        "lock has no administrator",
        "lock has no administrator"
    ), // 锁中不存在管理员
    LOCK_IS_IN_NO_SETTING_MODE(0x07, "Non-setting mode", "Non-setting mode"), // 添加管理员处于非设置模式
    LOCK_DYNAMIC_PWD_ERROR(0x08, "invalid dynamic code", "invalid dynamic code"), // 动态码错误
    LOCK_NO_POWER(0x0a, "run out of battery", "run out of battery"), // 低电量提示
    LOCK_INIT_KEYBOARD_FAILED(
        0x0b,
        "initialize keyboard password falied",
        "initialize keyboard password falied"
    ), // 初始化(重置)键盘密码出错
    LOCK_KEY_FLAG_INVALID(
        0x0d,
        "invalid ekey, lock flag position is low",
        "invalid ekey, lock flag position is low"
    ), // 电子钥匙失效flag过低
    LOCK_USER_TIME_EXPIRED(0x0e, "ekey expired", "ekey expired"), // 电子钥匙过期
    LOCK_PASSWORD_LENGTH_INVALID(
        0x0f,
        "invalid password length",
        "invalid password length"
    ), // 无效的密码长度
    LOCK_SUPER_PASSWORD_IS_SAME_WITH_DELETE_PASSWORD(
        0x10,
        "admin super password is same with delete password",
        "admin super password is same with delete password"
    ), // 管理员密码与删除密码相同
    LOCK_USER_TIME_INEFFECTIVE(
        0x11,
        "ekey hasn't become effective",
        "ekey hasn't become effective"
    ), // 电子钥匙尚未生效
    LOCK_USER_NOT_LOGIN(0x12, "user not login", "user not login"), // 未登录,无操作权限
    LOCK_OPERATE_FAILED(0x13, "Failed. Undefined error.", "Failed. Undefined error."), // 失败，
    LOCK_PASSWORD_EXIST(0x14, "password already exists.", "password already exists."), // 添加的密码已经存在
    LOCK_PASSWORD_NOT_EXIST(
        0x15,
        "password not exist or never be used",
        "password not exist or never be used"
    ), // 删除或者修改的密码不存在
    LOCK_NO_FREE_MEMORY(0x16, "out of memory", "out of memory"), // 存储空间不足(比如添加密码时，超过存储容量)
    NO_DEFINED_ERROR(0x17, "undefined error", "undefined error"), // 参数长度错误
    IC_CARD_NOT_EXIST(0x18, "Card number not exist.", "Card number not exist."), // 卡号不存在
    FINGER_PRINT_NOT_EXIST(0x1a, "Finger print not exist.", "Finger print not exist."), // 指纹不存在
    INVALID_COMMAND(0x1b, "Invalid command", "Invalid command"), // 无效的命令
    LOCK_FROZEN(0x1c, "lock frozen", "lock frozen"), // 锁被冻结
    INVALID_VENDOR(0x1d, "invalid vendor string", "invalid vendor string"), // 无效特殊字符串, 不同客户使用特定的字符串
    LOCK_REVERSE(0x1e, "double locked", "double locked"), // 门反锁了，普通用户不允许开锁
    RECORD_NOT_EXIST(0x1f, "record not exist", "record not exist"), // 记录不存在
    INVALID_PARAM(0x20, "invalid param", "invalid param"), // 参数错误，一般是写入锁的数据有问题
    PARKING_LOCK_LOCKED_FAILED(
        0x21,
        "Maybe there are obstacles or cars above the parking lock",
        "Maybe there are obstacles or car above the parking lock"
    ), // 车位锁上有障碍物或者有车停在上面，不允许进行闭锁操作(即车位锁升起)

    /**
     * Custom failed code
     */
    Failed(-1, "failed", "failed"), // 失败

    /**
     * WIFI部分
     */
    // 命令接收成功，正在处理
    COMMAND_RECEIVED(0x24, "command received", "command received"), BAD_WIFI_NAME(
        0x25,
        "bad wifi name",
        "bad wifi name"
    ),
    BAD_WIFI_PASSWORD(0x26, "bad wifi password", "bad wifi password"),

    /**
     * customized error code
     */
    AES_PARSE_ERROR(0x30, "aes parse error", "aes parse error"), // 解密失败
    KEY_INVALID(
        0x31,
        "key invalid, may be reset",
        "key invalid, may be reset"
    ), // 钥匙无效(锁可能被重置),解密失败

    //    LOCK_NOT_SUPPORT_CHANGE_PASSCODE(0x60, "the lock doesn't support to modify password.", "the lock doesn't support to modify password."),
    LOCK_CONNECT_FAIL(0x400, "lock connect time out", "lock connect time out"), // 连接锁超时
    LOCK_IS_BUSY(
        0x401,
        "only one command can be proceed at a time",
        "lock is busy"
    ), // 接口是单任务的，每次只能执行一条指令

    //    LOCK_IS_DISCONNECTED(0x402,"lock is disconnected","lock is disconnected"),
    DATA_FORMAT_ERROR(
        0x403,
        "parameter format or content is incorrect",
        "parameter error"
    ), // 数据错误，一般是传入的数据格式不满足要求
    LOCK_IS_NOT_SUPPORT(
        0x404,
        "lock doesn't support this operation",
        "lock doesn't support this operation"
    ), // 锁不支持当前操作
    BLE_SERVER_NOT_INIT(
        0x405,
        "bluetooth is disable",
        "not init or bluetooth is disable"
    ), // 蓝牙不可用，可能没进行初始化操作
    SCAN_FAILED_ALREADY_START(
        0x406,
        "fails to start scan as BLE scan with the same settings is already started by the app",
        "BLE scan already started by the app"
    ),
    SCAN_FAILED_APPLICATION_REGISTRATION_FAILED(
        0x407,
        "fails to start scan as app cannot be registered",
        "fails to start scan as app cannot be registered"
    ),
    SCAN_FAILED_INTERNAL_ERROR(
        0x408,
        "fails to start scan due to an internal error",
        "fails to start scan due an internal error"
    ),
    SCAN_FAILED_FEATURE_UNSUPPORTED(
        0x409,
        "fails to start power optimized scan as this feature is not supported",
        "fails to start power optimized scan as this feature is not supported"
    ),
    SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES(
        0x410,
        "fails to start scan as it is out of hardware resources",
        "fails to start scan as it is out of hardware resources"
    ),
    INIT_WIRELESS_KEYBOARD_FAILED(
        0x411,
        "add keyboard failed",
        "failed to init wireless keyboard"
    ), // 无线键盘初始化失败
    WIRELESS_KEYBOARD_NO_RESPONSE(
        0x412,
        "wireless keyboard no response",
        "time out"
    ), // 接口调用，无线键盘没有任何响应
    DEVICE_CONNECT_FAILED(0x413, "device connect time out", "device connect time out");

    private var lockname: String? = null
    private var lockmac: String? = null
    private var command: Byte = 0

    /**
     * error time
     */
    private var date: Long = 0
    private var sdkLog: String? = null
    fun getLockname(): String? {
        return lockname
    }

    fun setLockname(lockname: String?) {
        this.lockname = lockname
    }

    fun getLockmac(): String? {
        return lockmac
    }

    fun setLockmac(lockmac: String?) {
        this.lockmac = lockmac
    }

    fun getCommand(): String {
        return if (command >= 'A'.code.toByte() && command <= 'Z'.code.toByte()) Char(command.toUShort()).toString() else String.format(
            "%#x",
            command
        )
    }

    fun setCommand(command: Byte) {
        this.command = command
    }

    fun getDate(): Long {
        return date
    }

    fun setDate(date: Long) {
        this.date = date
    }

    fun getErrorMsg(): String {
        return errorMsg
    }

    fun getDescription(): String {
        return description
    }

    fun getErrorCode(): String {
        return String.format("%#x", errorCode)
    }

    fun getIntErrorCode(): Int {
        return errorCode
    }

    fun getSdkLog(): String? {
        return sdkLog
    }

    fun setSdkLog(sdkLog: String?) {
        this.sdkLog = sdkLog
    }

    companion object {
        fun getInstance(errorCode: Int): LockError {
            when (errorCode) {
                0 -> return LockError.SUCCESS
                0x01 -> return LockError.LOCK_CRC_CHECK_ERROR
                0x02 -> return LockError.LOCK_NO_PERMISSION
                0x03 -> return LockError.LOCK_ADMIN_CHECK_ERROR
                0x05 -> return LockError.LOCK_IS_IN_SETTING_MODE
                0x06 -> return LockError.LOCK_NOT_EXIST_ADMIN
                0x07 -> return LockError.LOCK_IS_IN_NO_SETTING_MODE
                0x08 -> return LockError.LOCK_DYNAMIC_PWD_ERROR
                0x0a -> return LockError.LOCK_NO_POWER
                0x0b -> return LockError.LOCK_INIT_KEYBOARD_FAILED
                0x0d -> return LockError.LOCK_KEY_FLAG_INVALID
                0x0e -> return LockError.LOCK_USER_TIME_EXPIRED
                0x0f -> return LockError.LOCK_PASSWORD_LENGTH_INVALID
                0x10 -> return LockError.LOCK_SUPER_PASSWORD_IS_SAME_WITH_DELETE_PASSWORD
                0x11 -> return LockError.LOCK_USER_TIME_INEFFECTIVE
                0x12 -> return LockError.LOCK_USER_NOT_LOGIN
                0x13 -> return LockError.LOCK_OPERATE_FAILED
                0x14 -> return LockError.LOCK_PASSWORD_EXIST
                0x15 -> return LockError.LOCK_PASSWORD_NOT_EXIST
                0x16 -> return LockError.LOCK_NO_FREE_MEMORY
                0x17 -> return LockError.NO_DEFINED_ERROR
                0x18 -> return LockError.IC_CARD_NOT_EXIST
                0x1a -> return LockError.FINGER_PRINT_NOT_EXIST
                0x1b -> return LockError.INVALID_COMMAND
                0x1c -> return LockError.LOCK_FROZEN
                0x1d -> return LockError.INVALID_VENDOR
                0x1e -> return LockError.LOCK_REVERSE
                0x1f -> return LockError.RECORD_NOT_EXIST
                0x20 -> return LockError.INVALID_PARAM
                0x21 -> return LockError.PARKING_LOCK_LOCKED_FAILED
                0x24 -> return LockError.COMMAND_RECEIVED
                0x25 -> return LockError.BAD_WIFI_NAME
                0x26 -> return LockError.BAD_WIFI_PASSWORD
            }
            return LockError.Failed
        }
    }
}
