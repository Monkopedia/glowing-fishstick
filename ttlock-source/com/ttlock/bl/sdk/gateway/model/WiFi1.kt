package com.ttlock.bl.sdk.gateway.model

/**
 * Created by TTLock on 2019/3/12.
 */
class WiFi {
    var ssid: String? = null
    var rssi = 0
    fun getSsid(): String? {
        return ssid
    }

    fun setSsid(ssid: String?) {
        this.ssid = ssid
    }

    fun getRssi(): Int {
        return rssi
    }

    fun setRssi(rssi: Int) {
        this.rssi = rssi
    }

    override fun toString(): String {
        return "WiFi{" +
            "ssid='" + ssid + '\'' +
            ", rssi=" + rssi +
            '}'
    }
}
