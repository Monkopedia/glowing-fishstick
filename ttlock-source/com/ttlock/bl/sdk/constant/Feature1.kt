package com.ttlock.bl.sdk.constant

/**
 * Created by TTLock on 2016/10/18 0018.
 */
@Deprecated("")
object Feature {
    /**
     * PASSCODE
     */
    var PASSCODE: Byte = 0x01

    /**
     * CARD
     */
    var IC: Byte = 0x02

    /**
     * FINGERPRINT
     */
    var FINGER_PRINT: Byte = 0x04

    /**
     * WRIST_BAND
     */
    var WRIST_BAND: Byte = 0x08

    /**
     * AUTO_LOCK
     */
    var AUTO_LOCK: Byte = 0x10

    /**
     * 密码带删除功能
     */
    var PASSCODE_WITH_DELETE_FUNCTION: Byte = 0x20

    /**
     * 支持固件升级设置指令
     */
    var FIRMWARE_SETTTING: Byte = 0x40

    /**
     * MODIFY_PASSCODE_FUNCTION(customized)
     */
    var MODIFY_PASSCODE_FUNCTION = 0x80

    /**
     * 闭锁指令
     */
    var MANUAL_LOCK = 0x100

    /**
     * PASSWORD_DISPLAY_OR_HIDE
     */
    var PASSWORD_DISPLAY_OR_HIDE = 0x200

    /**
     * GATEWAY_UNLOCK
     */
    var GATEWAY_UNLOCK = 0x400

    /**
     * 支持网关冻结解冻指令
     */
    var FREEZE_LOCK = 0x800

    /**
     * 支持循环密码
     */
    var CYCLIC_PASSWORD = 0x1000

    /**
     * 支持门磁
     */
    var MAGNETOMETER = 0x2000

    /**
     * 支持配置远程开锁
     */
    var CONFIG_GATEWAY_UNLOCK = 0x4000

    /**
     * Audio management
     */
    var AUDIO_MANAGEMENT = 0x8000

    /**
     * 支持NB
     */
    var NB_LOCK = 0x10000
    //    /**
    //     * 支持酒店锁卡系统
    //     */
    //    @Deprecated
    //    public static int HOTEL_LOCK = 0x20000;
    /**
     * 支持读取管理员密码
     */
    var GET_ADMIN_CODE = 0x40000

    /**
     * 支持酒店锁卡系统
     */
    var HOTEL_LOCK = 0x80000

    /**
     * 锁没有时钟芯片
     */
    var LOCK_NO_CLOCK_CHIP = 0x100000

    /**
     * 蓝牙不广播，不能实现App点击开锁
     */
    var CAN_NOT_CLICK_UNLOCK = 0x200000

    /**
     * 支持某一天几点到几点常开的模式
     */
    var PASSAGE_MODE = 0x400000

    /**
     * 支持常开模式及设置自动闭锁的情况下，是否支持关闭自动闭锁
     */
    var PASSAGE_MODE_AND_AUTO_LOCK_AND_CAN_CLOSE = 0x800000

    /**
     * 无线键盘
     */
    const val WIRELESS_KEYBOARD = 0x1000000

    /**
     * 照明灯
     */
    var LAMP = 0x2000000
}
