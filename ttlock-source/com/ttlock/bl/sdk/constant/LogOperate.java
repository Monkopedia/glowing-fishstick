package com.ttlock.bl.sdk.constant;

/**
 * Created by Administrator on 2016/6/22 0022.
 */
public class LogOperate {
    //手机开锁
    /**
     * 蓝牙开锁
     */
    public static final byte OPERATE_TYPE_MOBILE_UNLOCK = 1;

//    //服务器开锁
//    public static final byte OPERATE_TYPE_SERVER_UNLOCK = 3;

    //密码开锁
    public static final byte OPERATE_TYPE_KEYBOARD_PASSWORD_UNLOCK = 4;

    //键盘上修改密码
    public static final byte OPERATE_TYPE_KEYBOARD_MODIFY_PASSWORD = 5;

    //键盘上删除单个密码
    public static final byte OPERATE_TYPE_KEYBOARD_REMOVE_SINGLE_PASSWORD = 6;

    //错误密码开锁
    public static final byte OPERATE_TYPE_ERROR_PASSWORD_UNLOCK = 7;

    //键盘上删除所有密码
    public static final byte OPERATE_TYPE_KEYBOARD_REMOVE_ALL_PASSWORDS = 8;

    //密码被挤掉
    public static final byte OPERATE_TYPE_KEYBOARD_PASSWORD_KICKED = 9;

    /**
     * 带删除功能密码第一次开锁，之前密码被清空
     */
    public static final byte OPERATE_TYPE_USE_DELETE_CODE = 10;

    /**
     * 密码过期
     */
    public static final byte OPERATE_TYPE_PASSCODE_EXPIRED = 11;

    /**
     * 密码开锁失败，存储容量不足
     */
    public static final byte OPERATE_TYPE_SPACE_INSUFFICIENT = 12;

    /**
     * 密码开锁失败—密码在黑名单
     */
    public static final byte OPERATE_TYPE_PASSCODE_IN_BLACK_LIST = 13;

    /**
     * 门锁重新上电启动了（也就是重新上电池了）
     */
    public static final byte OPERATE_TYPE_DOOR_REBOOT = 14;

    /**
     * 添加IC卡成功
     */
    public static final byte OPERATE_TYPE_ADD_IC = 15;

    /**
     * 清空IC卡成功
     */
    public static final byte OPERATE_TYPE_CLEAR_IC_SUCCEED = 16;

    /**
     * IC卡开门成功
     */
    public static final byte OPERATE_TYPE_IC_UNLOCK_SUCCEED = 17;

    /**
     * 删除单个IC卡成功
     */
    public static final byte OPERATE_TYPE_DELETE_IC_SUCCEED = 18;

    /**
     * Bong手环开门成功
     */
    public static final byte OPERATE_TYPE_BONG_UNLOCK_SUCCEED = 19;

    /**
     * 指纹开门成功
     */
    public static final byte OPERATE_TYPE_FR_UNLOCK_SUCCEED = 20;

    /**
     * 指纹添加成功
     */
    public static final byte OPERATE_TYPE_ADD_FR = 21;

    /**
     * 指纹开门失败
     */
    public static final byte OPERATE_TYPE_FR_UNLOCK_FAILED = 22;

    /**
     * 删除单个指纹成功
     */
    public static final byte OPERATE_TYPE_DELETE_FR_SUCCEED = 23;

    /**
     * 清空指纹成功
     */
    public static final byte OPERATE_TYPE_CLEAR_FR_SUCCEED = 24;

    /**
     * IC卡开门失败-已过期或未生效
     */
    public static final byte OPERATE_TYPE_IC_UNLOCK_FAILED = 25;

    /**
     * 蓝牙或网关闭锁
     */
    public static final byte OPERATE_BLE_LOCK = 26;

    /**
     * 机械钥匙开锁
     */
    public static final byte OPERATE_KEY_UNLOCK = 27;

    /**
     * 网关开锁
     */
    public static final byte GATEWAY_UNLOCK = 28;

    /**
     * 非法开锁(比如脚踏)
     */
    public static final byte ILLAGEL_UNLOCK = 29;

    /**
     * 门磁合上
     */
    public static final byte DOOR_SENSOR_LOCK = 30;

    /**
     * 门磁打开
     */
    public static final byte DOOR_SENSOR_UNLOCK = 31;

    /**
     * 出门记录
     */
    public static final byte  DOOR_GO_OUT = 32;

    /**
     * 指纹关锁
     */
    public static final byte FR_LOCK = 33;

    /**
     * 密码关锁
     */
    public static final byte PASSCODE_LOCK = 34;

    public static final byte IC_LOCK = 35;

    /**
     * 机械钥匙关锁
     */
    public static final byte OPERATE_KEY_LOCK = 36;

    /**
     * 遥控按键
     */
    public static final byte REMOTE_CONTROL_KEY = 37;

    /**
     * 密码开锁失败，门反锁
     */
    public static final byte PASSCODE_UNLOCK_FAILED_LOCK_REVERSE = 38;

    /**
     * IC卡开锁失败，门反锁
     */
    public static final byte IC_UNLOCK_FAILED_LOCK_REVERSE = 39;

    /**
     * 指纹开锁失败，门反锁
     */
    public static final byte FR_UNLOCK_FAILED_LOCK_REVERSE = 40;

    /**
     * app开锁失败,门反锁
     */
    public static final byte APP_UNLOCK_FAILED_LOCK_REVERSE = 41;

    //42 ~ 48 无参数

    /**
     *  IC卡开锁失败—黑名单卡
     */
    public static final byte IC_UNLOCK_FAILED_BLANKLIST = 51;

    /**
     * 无线钥匙
     */
    public static final byte WIRELESS_KEY_FOB = 55;

    /**
     * 无线键盘电量
     */
    public static final byte WIRELESS_KEY_PAD = 56;

    /**
     * IC卡开锁失败—CPU安全信息错误
     */
    public static final byte CPU_CARD_UNLOCK_FAILED = 74;

    /**
     * 记录类型
     */
    private int recordType;

    /**
     * 锁中记录的唯一标识
     */
    private int recordId;

    /**
     * 用户id
     */
    private int uid;

    /**
     * 密码
     */
    private String password;

    /**
     * 修改后的密码
     */
    private String newPassword;

    /**
     * 操作时间
     */
    private long operateDate;

    /**
     * 删除密码的日期
     */
    private long deleteDate;

    /**
     * 锁电量
     */
    private int electricQuantity;

    /**
     * 配件电量
     */
    private int accessoryElectricQuantity;

    /**
     * 遥控按键ID或按键值(1 – 开锁  2  - 闭锁)
     *
     */
    private int keyId;

    public int getRecordType() {
        return recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public long getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(long operateDate) {
        this.operateDate = operateDate;
    }

    public long getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(long deleteDate) {
        this.deleteDate = deleteDate;
    }

    public int getElectricQuantity() {
        return electricQuantity;
    }

    public void setElectricQuantity(int electricQuantity) {
        this.electricQuantity = electricQuantity;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public int getAccessoryElectricQuantity() {
        return accessoryElectricQuantity;
    }

    public void setAccessoryElectricQuantity(int accessoryElectricQuantity) {
        this.accessoryElectricQuantity = accessoryElectricQuantity;
    }

    @Override
    public String toString() {
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
                '}';
    }
}
