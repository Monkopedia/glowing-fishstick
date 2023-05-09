package com.ttlock.bl.sdk.constant;

/**
 * Created by TTLock on 2017/1/5 0005.
 */
public class DeviceInfoType {
    /**
     * 产品型号
     */
    public static final byte MODEL_NUMBER = 1;

    /**
     * 硬件版本号
     */
    public static final byte HARDWARE_REVISION = 2;

    /**
     * 固件版本号
     */
    public static final byte FIRMWARE_REVISION = 3;

    /**
     * 生产日期
     */
    public static final byte MANUFACTURE_DATE = 4;

    /**
     * 蓝牙地址
     */
    public static final byte MAC_ADDRESS = 5;

    /**
     * 时钟
     */
    public static final byte LOCK_CLOCK = 6;

    /**
     * 运营商信息
     */
    public static final byte NB_OPERATOR = 7;

    /**
     * NB模块号(IMEI)
     */
    public static final byte NB_IMEI = 8;

    /**
     * NB卡信息
     */
    public static final byte NB_CARD_INFO = 9;

    /**
     * NB信号值
     */
    public static final byte NB_RSSI = 10;

    /**
     * 数字按键数量
     */
    public static final byte PASSCODE_KEY_NUMBER = 12;

}
