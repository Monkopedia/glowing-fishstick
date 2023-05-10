package com.ttlock.bl.sdk.gateway.util

import android.text.TextUtils

object GatewayUtil {
    fun convertIp2Bytes(ip: String): ByteArray? {
        val bytes = ByteArray(4)
        if (TextUtils.isEmpty(ip)) {
            return bytes
        }
        try {
            val dividerList = ip.split("\\.").toTypedArray()
            if (dividerList.size != 4) {
                return null
            }
            for (i in 0..3) {
                val temp = Integer.valueOf(dividerList[i])
                if (temp < 0 || temp > 255) {
                    return null
                }
                bytes[i] = temp.toByte()
            }
        } catch (e: Exception) {
            return null
        }
        return bytes
    }
}