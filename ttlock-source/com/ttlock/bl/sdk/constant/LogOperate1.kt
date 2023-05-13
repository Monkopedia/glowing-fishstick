package com.ttlock.bl.sdk.constant

/**
 * Created by Administrator on 2016/6/22 0022.
 */
class LogOperate {
    /**
     * 记录类型
     */
    private var recordType = 0

    /**
     * 锁中记录的唯一标识
     */
    private var recordId = 0

    /**
     * 用户id
     */
    private var uid = 0

    /**
     * 密码
     */
    private var password: String? = null

    /**
     * 修改后的密码
     */
    private var newPassword: String? = null

    /**
     * 操作时间
     */
    private var operateDate: Long = 0

    /**
     * 删除密码的日期
     */
    private var deleteDate: Long = 0

    /**
     * 锁电量
     */
    private var electricQuantity = 0

    /**
     * 配件电量
     */
    private var accessoryElectricQuantity = 0

    /**
     * 遥控按键ID或按键值(1 – 开锁  2  - 闭锁)
     *
     */
    private var keyId = 0
    fun getRecordType(): Int {
        return recordType
    }

    fun setRecordType(recordType: Int) {
        this.recordType = recordType
    }

    fun getRecordId(): Int {
        return recordId
    }

    fun setRecordId(recordId: Int) {
        this.recordId = recordId
    }

    fun getUid(): Int {
        return uid
    }

    fun setUid(uid: Int) {
        this.uid = uid
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    fun getNewPassword(): String? {
        return newPassword
    }

    fun setNewPassword(newPassword: String?) {
        this.newPassword = newPassword
    }

    fun getOperateDate(): Long {
        return operateDate
    }

    fun setOperateDate(operateDate: Long) {
        this.operateDate = operateDate
    }

    fun getDeleteDate(): Long {
        return deleteDate
    }

    fun setDeleteDate(deleteDate: Long) {
        this.deleteDate = deleteDate
    }

    fun getElectricQuantity(): Int {
        return electricQuantity
    }

    fun setElectricQuantity(electricQuantity: Int) {
        this.electricQuantity = electricQuantity
    }

    fun getKeyId(): Int {
        return keyId
    }

    fun setKeyId(keyId: Int) {
        this.keyId = keyId
    }

    fun getAccessoryElectricQuantity(): Int {
        return accessoryElectricQuantity
    }

    fun setAccessoryElectricQuantity(accessoryElectricQuantity: Int) {
        this.accessoryElectricQuantity = accessoryElectricQuantity
    }

    override fun toString(): String {
        return "LogOperate{" +
            "recordType=" + recordType +
            ", recordId=" + recordId +
            ", uid=" + uid +
            ", password='" + password + '\'' +
            ", newPassword='" + newPassword + '\'' +
            ", operateDate=" + operateDate +
            ", deleteDate=" + deleteDate +
            ", electricQuantity=" + electricQuantity +
            ", keyId=" + keyId +
            '}'
    }

    companion object {
        // 手机开锁
        /**
         * 蓝牙开锁
         */
        const val OPERATE_TYPE_MOBILE_UNLOCK: Byte = 1

        //    //服务器开锁
        //    public static final byte OPERATE_TYPE_SERVER_UNLOCK = 3;
        // 密码开锁
        const val OPERATE_TYPE_KEYBOARD_PASSWORD_UNLOCK: Byte = 4

        // 键盘上修改密码
        const val OPERATE_TYPE_KEYBOARD_MODIFY_PASSWORD: Byte = 5

        // 键盘上删除单个密码
        const val OPERATE_TYPE_KEYBOARD_REMOVE_SINGLE_PASSWORD: Byte = 6

        // 错误密码开锁
        const val OPERATE_TYPE_ERROR_PASSWORD_UNLOCK: Byte = 7

        // 键盘上删除所有密码
        const val OPERATE_TYPE_KEYBOARD_REMOVE_ALL_PASSWORDS: Byte = 8

        // 密码被挤掉
        const val OPERATE_TYPE_KEYBOARD_PASSWORD_KICKED: Byte = 9

        /**
         * 带删除功能密码第一次开锁，之前密码被清空
         */
        const val OPERATE_TYPE_USE_DELETE_CODE: Byte = 10

        /**
         * 密码过期
         */
        const val OPERATE_TYPE_PASSCODE_EXPIRED: Byte = 11

        /**
         * 密码开锁失败，存储容量不足
         */
        const val OPERATE_TYPE_SPACE_INSUFFICIENT: Byte = 12

        /**
         * 密码开锁失败—密码在黑名单
         */
        const val OPERATE_TYPE_PASSCODE_IN_BLACK_LIST: Byte = 13

        /**
         * 门锁重新上电启动了（也就是重新上电池了）
         */
        const val OPERATE_TYPE_DOOR_REBOOT: Byte = 14

        /**
         * 添加IC卡成功
         */
        const val OPERATE_TYPE_ADD_IC: Byte = 15

        /**
         * 清空IC卡成功
         */
        const val OPERATE_TYPE_CLEAR_IC_SUCCEED: Byte = 16

        /**
         * IC卡开门成功
         */
        const val OPERATE_TYPE_IC_UNLOCK_SUCCEED: Byte = 17

        /**
         * 删除单个IC卡成功
         */
        const val OPERATE_TYPE_DELETE_IC_SUCCEED: Byte = 18

        /**
         * Bong手环开门成功
         */
        const val OPERATE_TYPE_BONG_UNLOCK_SUCCEED: Byte = 19

        /**
         * 指纹开门成功
         */
        const val OPERATE_TYPE_FR_UNLOCK_SUCCEED: Byte = 20

        /**
         * 指纹添加成功
         */
        const val OPERATE_TYPE_ADD_FR: Byte = 21

        /**
         * 指纹开门失败
         */
        const val OPERATE_TYPE_FR_UNLOCK_FAILED: Byte = 22

        /**
         * 删除单个指纹成功
         */
        const val OPERATE_TYPE_DELETE_FR_SUCCEED: Byte = 23

        /**
         * 清空指纹成功
         */
        const val OPERATE_TYPE_CLEAR_FR_SUCCEED: Byte = 24

        /**
         * IC卡开门失败-已过期或未生效
         */
        const val OPERATE_TYPE_IC_UNLOCK_FAILED: Byte = 25

        /**
         * 蓝牙或网关闭锁
         */
        const val OPERATE_BLE_LOCK: Byte = 26

        /**
         * 机械钥匙开锁
         */
        const val OPERATE_KEY_UNLOCK: Byte = 27

        /**
         * 网关开锁
         */
        const val GATEWAY_UNLOCK: Byte = 28

        /**
         * 非法开锁(比如脚踏)
         */
        const val ILLAGEL_UNLOCK: Byte = 29

        /**
         * 门磁合上
         */
        const val DOOR_SENSOR_LOCK: Byte = 30

        /**
         * 门磁打开
         */
        const val DOOR_SENSOR_UNLOCK: Byte = 31

        /**
         * 出门记录
         */
        const val DOOR_GO_OUT: Byte = 32

        /**
         * 指纹关锁
         */
        const val FR_LOCK: Byte = 33

        /**
         * 密码关锁
         */
        const val PASSCODE_LOCK: Byte = 34
        const val IC_LOCK: Byte = 35

        /**
         * 机械钥匙关锁
         */
        const val OPERATE_KEY_LOCK: Byte = 36

        /**
         * 遥控按键
         */
        const val REMOTE_CONTROL_KEY: Byte = 37

        /**
         * 密码开锁失败，门反锁
         */
        const val PASSCODE_UNLOCK_FAILED_LOCK_REVERSE: Byte = 38

        /**
         * IC卡开锁失败，门反锁
         */
        const val IC_UNLOCK_FAILED_LOCK_REVERSE: Byte = 39

        /**
         * 指纹开锁失败，门反锁
         */
        const val FR_UNLOCK_FAILED_LOCK_REVERSE: Byte = 40

        /**
         * app开锁失败,门反锁
         */
        const val APP_UNLOCK_FAILED_LOCK_REVERSE: Byte = 41
        // 42 ~ 48 无参数
        /**
         * IC卡开锁失败—黑名单卡
         */
        const val IC_UNLOCK_FAILED_BLANKLIST: Byte = 51

        /**
         * 无线钥匙
         */
        const val WIRELESS_KEY_FOB: Byte = 55

        /**
         * 无线键盘电量
         */
        const val WIRELESS_KEY_PAD: Byte = 56

        /**
         * IC卡开锁失败—CPU安全信息错误
         */
        const val CPU_CARD_UNLOCK_FAILED: Byte = 74
    }
}
