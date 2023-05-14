package com.ttlock.bl.sdk.gateway.model

class ConnectParam {
    var mac: String? = null
    @JvmName("getMac1")
    fun getMac(): String? {
        return mac
    }

    @JvmName("setMac1")
    fun setMac(mac: String?) {
        this.mac = mac
    }
}
