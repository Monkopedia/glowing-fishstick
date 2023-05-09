package com.ttlock.bl.sdk.constant;

/**
 * Created by TTLock on 2016/10/18 0018.
 */
public class FeatureValue {
    /**
     * 密码
     */
    public static final byte PASSCODE = 0;

    /**
     * CARD
     */
    public static final byte IC = 1;

    /**
     * 指纹
     */
    public static final byte FINGER_PRINT = 2;

    /**
     * 手环
     */
    public static final byte WRIST_BAND = 3;

    /**
     * 自动闭锁功能
     */
    public static final byte AUTO_LOCK = 4;

    /**
     * 密码带删除功能
     */
    public static final byte PASSCODE_WITH_DELETE_FUNCTION = 5;

    /**
     * 支持固件升级设置指令
     */
    public static final byte FIRMWARE_SETTTING = 6;

    /**
     * 修改密码(自定义)功能
     */
    public static final int MODIFY_PASSCODE_FUNCTION = 7;

    /**
     * 闭锁指令
     */
    public static final int MANUAL_LOCK = 8;

    /**
     * 支持密码显示或者隐藏
     */
    public static final int PASSWORD_DISPLAY_OR_HIDE = 9;

    /**
     * 支持网关开锁指令
     */
    public static final int GATEWAY_UNLOCK = 10;

    /**
     * 支持网关冻结解冻指令
     */
    public static final int FREEZE_LOCK = 11;

    /**
     * 支持循环密码
     */
    public static final int CYCLIC_PASSWORD = 12;

    /**
     * 支持门磁
     */
    public static final int MAGNETOMETER = 13;

     /**
     * 支持配置远程开锁
     */
    public static final int CONFIG_GATEWAY_UNLOCK = 14;

    /**
     * Audio management
     */
    public static final int AUDIO_MANAGEMENT = 15;

    /**
     * 支持NB
     */
    public static final int NB_LOCK = 16;

//    /**
//     * 支持酒店锁卡系统
//     */
//    @Deprecated
//    public static int HOTEL_LOCK = 0x20000;

    /**
     * 支持读取管理员密码
     */
    public static final int GET_ADMIN_CODE = 18;

    /**
     * 支持酒店锁卡系统
     */
    public static final int HOTEL_LOCK = 19;

    /**
     * 锁没有时钟芯片
     */
    public static final int LOCK_NO_CLOCK_CHIP = 20;

    /**
     * 蓝牙不广播，不能实现App点击开锁
     */
    public static final int CAN_NOT_CLICK_UNLOCK = 21;

    /**
     * 支持某一天几点到几点常开的模式
     */
    public static final int PASSAGE_MODE = 22;

    /**
     * 支持常开模式及设置自动闭锁的情况下，是否支持关闭自动闭锁
     */
    public static final int PASSAGE_MODE_AND_AUTO_LOCK_AND_CAN_CLOSE = 23;

    public static final int WIRELESS_KEYBOARD = 24;

    /**
     * 照明灯
     */
    public static final int LAMP = 25;

    /**
     * 防撬开关配置
     */
    public static final int TAMPER_ALERT = 28;

    /**
     * 重置键配置
     */
    public static final int RESET_BUTTON = 29;

    /**
     * 反锁
     */
    public static final int PRIVACY_LOCK = 30;

    /**
     * 死锁(原先的31不使用了)
     */
    public static final int DEAD_LOCK = 32;

    /**
     * 支持常开模式例外
     */
//    public static int PASSAGE_MODE_ = 33;

    public static final int CYCLIC_IC_OR_FINGER_PRINT = 34;

    /**
     * 支持左右开门设置
     */
    public static final int UNLOCK_DIRECTION = 36;
    /**
     * 指静脉
     */
    public static final int FINGER_VEIN = 37;

    /**
     * 泰凌蓝牙芯片
     */
    public static final int TELINK_CHIP = 38;

    /**
     * 支持NB激活配置
     */
    public static final int NB_ACTIVITE_CONFIGURATION = 39;

    /**
     * 支持循环密码恢复功能
     */
    public static final int CYCLIC_PASSCODE_CAN_RECOVERY = 40;

    /**
     * 支持无线钥匙
     */
    public static final int WIRELESS_KEY_FOB = 41;

    /**
     * 支持读取配件电量信息
     */
    public static final int ACCESSORY_BATTERY = 42;

    /**
     * 支持音量及语言设置
     */
    public static final int SOUND_VOLUME_AND_LANGUAGE_SETTING = 43;

    /**
     * 支持二维码
     */
    public static final int QR_CODE = 44;

    /**
     * 支持门磁状态（以前也支持，但没有门磁未知状态，增加未知状态，使得门磁状态更加准确）
     *
     */
    public static final int DOOR_SENSOR = 45;

    /**
     * 支持常开模式自动开锁设置
     */
    public static final int PASSAGE_MODE_AUTO_UNLOCK_SETTING = 46;

    /**
     * 支持指纹下发功能（为了Web页面简化显示，支持指纹下发的，不管是哪种指纹，这一位都必须要设置为1）
     */
    public static final int FINGERPRINT_DISTRIBUTION = 47;

    /**
     * 支持中正指纹下发功能
     */
    public static final int ZHONG_ZHENG = 48;

    /**
     * 支持晟元指纹下发功能
     */
    public static final int SYNO = 49;

    /**
     * 支持无线门磁
     */
    public static final int WIRELESS_DOOR_SENSOR = 50;

    /**
     * 支持门未关报警
     */
    public static final int DOOR_OPEN_ALARM = 51;

    /**
     * 支持接近感应  检测人靠近 即支持灵敏度
     */
    public static final int SENSITIVITY = 52;

    /**
     * 支持3D人脸
     */
    public static final int FACE_3D = 53;

    /**
     * 支持CPU卡
     */
    public static final int CPU_CARD = 55;

    /**
     * 支持WIFI锁
     */
    public static final int WIFI_LOCK = 56;

    /**
     * wifi锁支持静态IP
     */
    public static final int WIFI_LOCK_SUPPORT_STATIC_IP = 58;

    /**
     * 不完全密码
     */
    public static final int INCOMPLETE_PASSCODE = 60;

}
