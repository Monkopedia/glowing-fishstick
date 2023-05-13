package com.ttlock.bl.sdk.constant

/**
 * Created by Smartlock on 2016/5/27.
 */
object LockType {
    const val LOCK_TYPE_V1 = 1

    /** 3.0  */
    const val LOCK_TYPE_V2 = 2

    /** 5.1  */
    const val LOCK_TYPE_V2S = 3

    /** 5.4  */
    const val LOCK_TYPE_V2S_PLUS = 4

    /** 三代锁 5.3  */
    const val LOCK_TYPE_V3 = 5

    /** 车位锁 a.1  */
    const val LOCK_TYPE_CAR = 6

    /**
     * 三代车位锁   5.3.7
     */
    const val LOCK_TYPE_V3_CAR = 8

    /** 电动车锁 b.1  */
    const val LOCK_TYPE_MOBI = 7 //    /**
    //     * 遥控设备 5.3.10
    //     */
    //    public static final int LOCK_TYPE_REMOTE_CONTROL_DEVICE = 9;
    //    /** 保险箱锁 */
    //    public static final int LOCK_TYPE_SAFE_LOCK = 8;
    //
    //    /** 自行车锁 */
    //    public static final int LOCK_TYPE_BICYCLE = 9;
}
