package com.ttlock.bl.sdk.entity

class WifiLockInfo {
    /**
     * WiFi MAC地址
     * （6 bytes）
     * 高在前，低在后
     */
    private var wifiMac: String? = null

    /**
     * 有符号数，一般都是负数
     *
     * 未连接时，返回-127
     */
    private var wifiRssi = 0

    constructor() {}
    constructor(wifiMac: String?, wifiRssi: Int) {
        this.wifiMac = wifiMac
        this.wifiRssi = wifiRssi
    }

    fun getWifiMac(): String? {
        return wifiMac
    }

    fun setWifiMac(wifiMac: String?) {
        this.wifiMac = wifiMac
    }

    fun getWifiRssi(): Int {
        return wifiRssi
    }

    fun setWifiRssi(wifiRssi: Int) {
        this.wifiRssi = wifiRssi
    }
}
