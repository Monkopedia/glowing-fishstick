package com.ttlock.bl.sdk.constant;

/**
 * Created by TTLock on 2016/10/18 0018.
 */
@Deprecated
public class Feature {
    /**
     * PASSCODE
     */
    public static byte PASSCODE = 0x01;

    /**
     * CARD
     */
    public static byte IC = 0x02;

    /**
     * FINGERPRINT
     */
    public static byte FINGER_PRINT = 0x04;

    /**
     * WRIST_BAND
     */
    public static byte WRIST_BAND = 0x08;

    /**
     * AUTO_LOCK
     */
    public static byte AUTO_LOCK = 0x10;

    /**
     * 密码带删除功能
     */
    public static byte PASSCODE_WITH_DELETE_FUNCTION = 0x20;

    /**
     * 支持固件升级设置指令
     */
    public static byte FIRMWARE_SETTTING = 0x40;

    /**
     * MODIFY_PASSCODE_FUNCTION(customized)
     */
    public static int MODIFY_PASSCODE_FUNCTION = 0x80;

    /**
     * 闭锁指令
     */
    public static int MANUAL_LOCK = 0x100;

    /**
     * PASSWORD_DISPLAY_OR_HIDE
     */
    public static int PASSWORD_DISPLAY_OR_HIDE = 0x200;

    /**
     * GATEWAY_UNLOCK
     */
    public static int GATEWAY_UNLOCK = 0x400;

    /**
     * 支持网关冻结解冻指令
     */
    public static int FREEZE_LOCK = 0x800;

    /**
     * 支持循环密码
     */
    public static int CYCLIC_PASSWORD = 0x1000;

    /**
     * 支持门磁
     */
    public static int MAGNETOMETER = 0x2000;

     /**
     * 支持配置远程开锁
     */
    public static int CONFIG_GATEWAY_UNLOCK = 0x4000;

    /**
     * Audio management
     */
    public static int AUDIO_MANAGEMENT = 0x8000;

    /**
     * 支持NB
     */
    public static int NB_LOCK = 0x10000;

//    /**
//     * 支持酒店锁卡系统
//     */
//    @Deprecated
//    public static int HOTEL_LOCK = 0x20000;

    /**
     * 支持读取管理员密码
     */
    public static int GET_ADMIN_CODE = 0x40000;

    /**
     * 支持酒店锁卡系统
     */
    public static int HOTEL_LOCK = 0x80000;

    /**
     * 锁没有时钟芯片
     */
    public static int LOCK_NO_CLOCK_CHIP = 0x100000;

    /**
     * 蓝牙不广播，不能实现App点击开锁
     */
    public static int CAN_NOT_CLICK_UNLOCK = 0x200000;

    /**
     * 支持某一天几点到几点常开的模式
     */
    public static int PASSAGE_MODE = 0x400000;

    /**
     * 支持常开模式及设置自动闭锁的情况下，是否支持关闭自动闭锁
     */
    public static int PASSAGE_MODE_AND_AUTO_LOCK_AND_CAN_CLOSE = 0x800000;

    /**
     * 无线键盘
     */
    public static final int WIRELESS_KEYBOARD = 0x1000000;

    /**
     * 照明灯
     */
    public static int LAMP = 0x2000000;
}
