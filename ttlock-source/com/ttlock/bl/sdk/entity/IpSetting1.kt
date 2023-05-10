package com.ttlock.bl.sdk.entity

import android.text.TextUtils
import java.util.*

class IpSetting {
    /**
     * 0 -固定ip
     * 1 -DHCP 自动获取ip地址
     */
    private var type = 0

    /**
     * 静态ip
     */
    private var ipAddress: String? = null

    /**
     * 子网掩码
     */
    private var subnetMask: String? = null

    /**
     * 默认网关
     */
    private var router: String? = null

    /**
     * 首选dns
     */
    private var preferredDns: String? = null

    /**
     * 备用dns
     */
    private var alternateDns: String? = null
    fun getType(): Int {
        return type
    }

    fun setType(type: Int) {
        this.type = type
    }

    fun getIpAddress(): String? {
        return ipAddress
    }

    fun setIpAddress(ipAddress: String?) {
        this.ipAddress = ipAddress
    }

    fun getSubnetMask(): String? {
        return subnetMask
    }

    fun setSubnetMask(subnetMask: String?) {
        this.subnetMask = subnetMask
    }

    fun getRouter(): String? {
        return router
    }

    fun setRouter(router: String?) {
        this.router = router
    }

    fun getPreferredDns(): String? {
        return preferredDns
    }

    fun setPreferredDns(preferredDns: String?) {
        this.preferredDns = preferredDns
    }

    fun getAlternateDns(): String? {
        return alternateDns
    }

    fun setAlternateDns(alternateDns: String?) {
        this.alternateDns = alternateDns
    }

    fun isValidData(): Boolean {
        if (type == STATIC_IP) {
            if (!checkIpFormat(ipAddress)) {
                return false
            }
            if (!checkIpFormat(subnetMask)) {
                return false
            }
            if (!checkIpFormat(router)) {
                return false
            }
            if (!TextUtils.isEmpty(preferredDns) && !checkIpFormat(preferredDns)) { //dns可以为空 不设置
                return false
            }
            if (!TextUtils.isEmpty(alternateDns) && !checkIpFormat(alternateDns)) { //dns可以为空 不设置
                return false
            }
        }
        return true
    }

    fun checkIpFormat(ip: String?): Boolean {
        if (TextUtils.isEmpty(ip)) {
            return false
        }
        try {
            LogUtil.d("ip:$ip")
            val dividerList = ip!!.split("\\.").toTypedArray()
            LogUtil.d("dividerList:" + Arrays.toString(dividerList))
            if (dividerList.size != 4) {
                return false
            }
            for (str in dividerList) {
                val temp = Integer.valueOf(str)
                if (temp < 0 || temp > 255) {
                    return false
                }
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }

    companion object {
        const val STATIC_IP = 0
        const val DHCP = 1
    }
}