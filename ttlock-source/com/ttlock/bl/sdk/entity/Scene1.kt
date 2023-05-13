package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2018/7/5.
 */
object Scene {
    /**
     * 门禁
     */
    const val GLASS_LOCK = 4.toByte()

    /**
     * 保险箱锁
     */
    const val SAFE_LOCK = 5.toByte()
    const val BICYCLE_LOCK = 6.toByte()
    const val PARKING_LOCK = 7.toByte()

    /**
     * 挂锁
     */
    const val PAD_LOCK = 8.toByte()

    /**
     * 锁芯
     */
    const val CYLINDER = 9.toByte()

    /**
     * 遥控设备
     */
    const val REMOTE_CONTROL_DEVICE = 10.toByte()

    /**
     * 同保险箱锁只有单次密码
     */
    const val SAFE_LOCK_SINGLE_PASSCODE = 11.toByte()

    /**
     * 梯控
     */
    const val LIFT = 12.toByte()

    /**
     * 取电开关
     */
    const val POWER_SAVER = 13.toByte()
}
