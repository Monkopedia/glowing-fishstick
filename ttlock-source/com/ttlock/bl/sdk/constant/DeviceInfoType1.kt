package com.ttlock.bl.sdk.constant

/**
 * Created by TTLock on 2017/1/5 0005.
 */
object DeviceInfoType {
    /**
     * 产品型号
     */
    const val MODEL_NUMBER: Byte = 1

    /**
     * 硬件版本号
     */
    const val HARDWARE_REVISION: Byte = 2

    /**
     * 固件版本号
     */
    const val FIRMWARE_REVISION: Byte = 3

    /**
     * 生产日期
     */
    const val MANUFACTURE_DATE: Byte = 4

    /**
     * 蓝牙地址
     */
    const val MAC_ADDRESS: Byte = 5

    /**
     * 时钟
     */
    const val LOCK_CLOCK: Byte = 6

    /**
     * 运营商信息
     */
    const val NB_OPERATOR: Byte = 7

    /**
     * NB模块号(IMEI)
     */
    const val NB_IMEI: Byte = 8

    /**
     * NB卡信息
     */
    const val NB_CARD_INFO: Byte = 9

    /**
     * NB信号值
     */
    const val NB_RSSI: Byte = 10

    /**
     * 数字按键数量
     */
    const val PASSCODE_KEY_NUMBER: Byte = 12
}
