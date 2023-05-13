package com.ttlock.bl.sdk.entity

import com.ttlock.bl.sdk.entity.Error.FR_NOT_EXIST
import com.ttlock.bl.sdk.entity.Error.IC_CARD_NOT_EXIST
import com.ttlock.bl.sdk.entity.Error.INVALID_COMMAND
import com.ttlock.bl.sdk.entity.Error.INVALID_VENDOR
import com.ttlock.bl.sdk.entity.Error.LOCK_ADMIN_CHECK_ERROR
import com.ttlock.bl.sdk.entity.Error.LOCK_CRC_CHECK_ERROR
import com.ttlock.bl.sdk.entity.Error.LOCK_DYNAMIC_PWD_ERROR
import com.ttlock.bl.sdk.entity.Error.LOCK_INIT_KEYBOARD_FAILED
import com.ttlock.bl.sdk.entity.Error.LOCK_IS_IN_NO_SETTING_MODE
import com.ttlock.bl.sdk.entity.Error.LOCK_IS_IN_SETTING_MODE
import com.ttlock.bl.sdk.entity.Error.LOCK_KEY_FLAG_INVALID
import com.ttlock.bl.sdk.entity.Error.LOCK_NOT_EXIST_ADMIN
import com.ttlock.bl.sdk.entity.Error.LOCK_NO_FREE_MEMORY
import com.ttlock.bl.sdk.entity.Error.LOCK_NO_PERMISSION
import com.ttlock.bl.sdk.entity.Error.LOCK_NO_POWER
import com.ttlock.bl.sdk.entity.Error.LOCK_OPERATE_FAILED
import com.ttlock.bl.sdk.entity.Error.LOCK_PASSWORD_EXIST
import com.ttlock.bl.sdk.entity.Error.LOCK_PASSWORD_LENGTH_INVALID
import com.ttlock.bl.sdk.entity.Error.LOCK_PASSWORD_NOT_EXIST
import com.ttlock.bl.sdk.entity.Error.LOCK_REVERSE
import com.ttlock.bl.sdk.entity.Error.LOCK_SUPER_PASSWORD_IS_SAME_WITH_DELETE_PASSWORD
import com.ttlock.bl.sdk.entity.Error.LOCK_USER_NOT_LOGIN
import com.ttlock.bl.sdk.entity.Error.LOCK_USER_TIME_EXPIRED
import com.ttlock.bl.sdk.entity.Error.LOCK_USER_TIME_INEFFECTIVE
import com.ttlock.bl.sdk.entity.Error.RECORD_NOT_EXIST
import com.ttlock.bl.sdk.entity.Error.SUCCESS

/**
 * Created by Administrator on 2016/7/21 0021.
 */
@Deprecated("")
enum class Error(
    private val errorCode: Int,
    private val description: String,
    private val errorMsg: String
) {
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
    LOCK_NO_POWER(0x0a, "run out of battery", "run out of battery"), LOCK_INIT_KEYBOARD_FAILED(
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
    ),
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
    LOCK_OPERATE_FAILED(
        0x13,
        "Failed. Undefined error.",
        "Failed. Undefined error."
    ),
    LOCK_PASSWORD_EXIST(0x14, "password already exists.", "password already exists."), // 添加的密码已经存在
    LOCK_PASSWORD_NOT_EXIST(0x15, "password not exists.", "password not exists."), // 删除或者修改的密码不存在
    LOCK_NO_FREE_MEMORY(0x16, "out of memory", "out of memory"), // 存储空间不足(比如添加密码时，超过存储容量)
    IC_CARD_NOT_EXIST(0x18, "Card number not exist.", "Card number not exist."), FR_NOT_EXIST(
        0x1a,
        "Finger print not exist.",
        "Finger print not exist."
    ),
    INVALID_COMMAND(0x1b, "Invalid command", "Invalid command"), INVALID_VENDOR(
        0x1d,
        "invalid vendor string",
        "invalid vendor string"
    ), // 无效特殊字符串,客户使用专业字符串
    LOCK_REVERSE(0x1e, "", ""), // 门反锁了，普通用户不允许开锁
    RECORD_NOT_EXIST(0x1f, "record not exist", "record not exist"),

    /**
     * customized error code
     */
    AES_PARSE_ERROR(0x30, "aes parse error", "aes parse error"), KEY_INVALID(
        0x31,
        "key invalid, may be reset",
        "key invalid, may be reset"
    ), // 钥匙无效(锁可能被重置),解密失败
    LOCK_NOT_SUPPORT_CHANGE_PASSCODE(
        0x60,
        "the lock doesn't support to modify password.",
        "the lock doesn't support to modify password."
    );

    private var lockname: String? = null
    private var lockmac: String? = null
    private var command: Byte = 0

    /**
     * error time
     */
    private var date: Long = 0
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

    //    public void setErrorMsg(String errorMsg) {
    //        this.errorMsg = errorMsg;
    //    }
    fun getDescription(): String {
        return description
    }

    //    public void setDescription(String description) {
    //        this.description = description;
    //    }
    fun getErrorCode(): String {
        return String.format("%#x", errorCode)
    } //    public void setErrorCode(int errorCode) {

    //        this.errorCode = errorCode;
    //    }
    companion object {
        fun getInstance(errorCode: Int): com.ttlock.bl.sdk.entity.Error? {
            when (errorCode) {
                0 -> return com.ttlock.bl.sdk.entity.Error.SUCCESS
                0x01 -> return com.ttlock.bl.sdk.entity.Error.LOCK_CRC_CHECK_ERROR
                0x02 -> return com.ttlock.bl.sdk.entity.Error.LOCK_NO_PERMISSION
                0x03 -> return com.ttlock.bl.sdk.entity.Error.LOCK_ADMIN_CHECK_ERROR
                0x05 -> return com.ttlock.bl.sdk.entity.Error.LOCK_IS_IN_SETTING_MODE
                0x06 -> return com.ttlock.bl.sdk.entity.Error.LOCK_NOT_EXIST_ADMIN
                0x07 -> return com.ttlock.bl.sdk.entity.Error.LOCK_IS_IN_NO_SETTING_MODE
                0x08 -> return com.ttlock.bl.sdk.entity.Error.LOCK_DYNAMIC_PWD_ERROR
                0x0a -> return com.ttlock.bl.sdk.entity.Error.LOCK_NO_POWER
                0x0b -> return com.ttlock.bl.sdk.entity.Error.LOCK_INIT_KEYBOARD_FAILED
                0x0d -> return com.ttlock.bl.sdk.entity.Error.LOCK_KEY_FLAG_INVALID
                0x0e -> return com.ttlock.bl.sdk.entity.Error.LOCK_USER_TIME_EXPIRED
                0x0f -> return com.ttlock.bl.sdk.entity.Error.LOCK_PASSWORD_LENGTH_INVALID
                0x10 -> return com.ttlock.bl.sdk.entity.Error.LOCK_SUPER_PASSWORD_IS_SAME_WITH_DELETE_PASSWORD
                0x11 -> return com.ttlock.bl.sdk.entity.Error.LOCK_USER_TIME_INEFFECTIVE
                0x12 -> return com.ttlock.bl.sdk.entity.Error.LOCK_USER_NOT_LOGIN
                0x13 -> return com.ttlock.bl.sdk.entity.Error.LOCK_OPERATE_FAILED
                0x14 -> return com.ttlock.bl.sdk.entity.Error.LOCK_PASSWORD_EXIST
                0x15 -> return com.ttlock.bl.sdk.entity.Error.LOCK_PASSWORD_NOT_EXIST
                0x16 -> return com.ttlock.bl.sdk.entity.Error.LOCK_NO_FREE_MEMORY
                0x18 -> return com.ttlock.bl.sdk.entity.Error.IC_CARD_NOT_EXIST
                0x1a -> return com.ttlock.bl.sdk.entity.Error.FR_NOT_EXIST
                0x1b -> return com.ttlock.bl.sdk.entity.Error.INVALID_COMMAND
                0x1d -> return com.ttlock.bl.sdk.entity.Error.INVALID_VENDOR
                0x1e -> return com.ttlock.bl.sdk.entity.Error.LOCK_REVERSE
                0x1f -> return com.ttlock.bl.sdk.entity.Error.RECORD_NOT_EXIST
            }
            return null
        }
    }
}
