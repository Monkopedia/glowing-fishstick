package com.ttlock.bl.sdk.constant

/**
 * Created by TTLock on 2016/10/18 0018.
 */
object FeatureValue {
    /**
     * 密码
     */
    const val PASSCODE: Byte = 0

    /**
     * CARD
     */
    const val IC: Byte = 1

    /**
     * 指纹
     */
    const val FINGER_PRINT: Byte = 2

    /**
     * 手环
     */
    const val WRIST_BAND: Byte = 3

    /**
     * 自动闭锁功能
     */
    const val AUTO_LOCK: Byte = 4

    /**
     * 密码带删除功能
     */
    const val PASSCODE_WITH_DELETE_FUNCTION: Byte = 5

    /**
     * 支持固件升级设置指令
     */
    const val FIRMWARE_SETTTING: Byte = 6

    /**
     * 修改密码(自定义)功能
     */
    const val MODIFY_PASSCODE_FUNCTION = 7

    /**
     * 闭锁指令
     */
    const val MANUAL_LOCK = 8

    /**
     * 支持密码显示或者隐藏
     */
    const val PASSWORD_DISPLAY_OR_HIDE = 9

    /**
     * 支持网关开锁指令
     */
    const val GATEWAY_UNLOCK = 10

    /**
     * 支持网关冻结解冻指令
     */
    const val FREEZE_LOCK = 11

    /**
     * 支持循环密码
     */
    const val CYCLIC_PASSWORD = 12

    /**
     * 支持门磁
     */
    const val MAGNETOMETER = 13

    /**
     * 支持配置远程开锁
     */
    const val CONFIG_GATEWAY_UNLOCK = 14

    /**
     * Audio management
     */
    const val AUDIO_MANAGEMENT = 15

    /**
     * 支持NB
     */
    const val NB_LOCK = 16
    //    /**
    //     * 支持酒店锁卡系统
    //     */
    //    @Deprecated
    //    public static int HOTEL_LOCK = 0x20000;
    /**
     * 支持读取管理员密码
     */
    const val GET_ADMIN_CODE = 18

    /**
     * 支持酒店锁卡系统
     */
    const val HOTEL_LOCK = 19

    /**
     * 锁没有时钟芯片
     */
    const val LOCK_NO_CLOCK_CHIP = 20

    /**
     * 蓝牙不广播，不能实现App点击开锁
     */
    const val CAN_NOT_CLICK_UNLOCK = 21

    /**
     * 支持某一天几点到几点常开的模式
     */
    const val PASSAGE_MODE = 22

    /**
     * 支持常开模式及设置自动闭锁的情况下，是否支持关闭自动闭锁
     */
    const val PASSAGE_MODE_AND_AUTO_LOCK_AND_CAN_CLOSE = 23
    const val WIRELESS_KEYBOARD = 24

    /**
     * 照明灯
     */
    const val LAMP = 25

    /**
     * 防撬开关配置
     */
    const val TAMPER_ALERT = 28

    /**
     * 重置键配置
     */
    const val RESET_BUTTON = 29

    /**
     * 反锁
     */
    const val PRIVACY_LOCK = 30

    /**
     * 死锁(原先的31不使用了)
     */
    const val DEAD_LOCK = 32

    /**
     * 支持常开模式例外
     */
    //    public static int PASSAGE_MODE_ = 33;
    const val CYCLIC_IC_OR_FINGER_PRINT = 34

    /**
     * 支持左右开门设置
     */
    const val UNLOCK_DIRECTION = 36

    /**
     * 指静脉
     */
    const val FINGER_VEIN = 37

    /**
     * 泰凌蓝牙芯片
     */
    const val TELINK_CHIP = 38

    /**
     * 支持NB激活配置
     */
    const val NB_ACTIVITE_CONFIGURATION = 39

    /**
     * 支持循环密码恢复功能
     */
    const val CYCLIC_PASSCODE_CAN_RECOVERY = 40

    /**
     * 支持无线钥匙
     */
    const val WIRELESS_KEY_FOB = 41

    /**
     * 支持读取配件电量信息
     */
    const val ACCESSORY_BATTERY = 42

    /**
     * 支持音量及语言设置
     */
    const val SOUND_VOLUME_AND_LANGUAGE_SETTING = 43

    /**
     * 支持二维码
     */
    const val QR_CODE = 44

    /**
     * 支持门磁状态（以前也支持，但没有门磁未知状态，增加未知状态，使得门磁状态更加准确）
     *
     */
    const val DOOR_SENSOR = 45

    /**
     * 支持常开模式自动开锁设置
     */
    const val PASSAGE_MODE_AUTO_UNLOCK_SETTING = 46

    /**
     * 支持指纹下发功能（为了Web页面简化显示，支持指纹下发的，不管是哪种指纹，这一位都必须要设置为1）
     */
    const val FINGERPRINT_DISTRIBUTION = 47

    /**
     * 支持中正指纹下发功能
     */
    const val ZHONG_ZHENG = 48

    /**
     * 支持晟元指纹下发功能
     */
    const val SYNO = 49

    /**
     * 支持无线门磁
     */
    const val WIRELESS_DOOR_SENSOR = 50

    /**
     * 支持门未关报警
     */
    const val DOOR_OPEN_ALARM = 51

    /**
     * 支持接近感应  检测人靠近 即支持灵敏度
     */
    const val SENSITIVITY = 52

    /**
     * 支持3D人脸
     */
    const val FACE_3D = 53

    /**
     * 支持CPU卡
     */
    const val CPU_CARD = 55

    /**
     * 支持WIFI锁
     */
    const val WIFI_LOCK = 56

    /**
     * wifi锁支持静态IP
     */
    const val WIFI_LOCK_SUPPORT_STATIC_IP = 58

    /**
     * 不完全密码
     */
    const val INCOMPLETE_PASSCODE = 60
}
