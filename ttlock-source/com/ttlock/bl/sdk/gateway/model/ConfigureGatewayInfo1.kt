package com.ttlock.bl.sdk.gateway.model

import com.ttlock.bl.sdk.util.DigitUtil
import com.ttlock.bl.sdk.util.LogUtil
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.util.*

/**
 * Created by TTLock on 2019/3/12.
 */
class ConfigureGatewayInfo {
    /**
     * 4g 设置一个默认值
     */
    var ssid = "ssid"
    var wifiPwd = "wifipwd"
    var server = "plug.sciener.cn"
    var port = 2999
    var uid = 0
    var userPwd: String? = null
    var companyId = 0
    var branchId = 0
    var plugName: String? = null

    /**
     * 默认G2网关
     */
    var plugVersion = 2
    fun getMd5UserPwd(): String? {
        if (userPwd != null && userPwd!!.length != 32) userPwd = DigitUtil.getMD5(userPwd)
        return userPwd
    }

    fun getCorrentNameBytes(): ByteArray? {
        var nameBytes: ByteArray? = null
        val linkNameBuilder = StringBuilder(plugName)
        try {
            nameBytes = linkNameBuilder.toString().toByteArray(charset("UTF-8"))
            val needLenth: Int = ConfigureGatewayInfo.Companion.LINK_NAME_LENGTH - nameBytes.size
            // 补充G2的特殊字符2
            if (needLenth > 1) {
                linkNameBuilder.append("\n")
                linkNameBuilder.append(plugVersion.toString())
                for (i in 2 until needLenth) {
                    linkNameBuilder.append("\n")
                }
                nameBytes = linkNameBuilder.toString().toByteArray(charset("UTF-8"))
            } else { // 长度超了截取
                LogUtil.w("name is to long")
                nameBytes =
                    Arrays.copyOf(nameBytes, ConfigureGatewayInfo.Companion.LINK_NAME_LENGTH)
                nameBytes[ConfigureGatewayInfo.Companion.LINK_NAME_LENGTH - 2] = '\n'.code.toByte()
                nameBytes[ConfigureGatewayInfo.Companion.LINK_NAME_LENGTH - 1] = '2'.code.toByte()
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return nameBytes
    }

    fun getSsid(): String {
        return ssid
    }

    fun setSsid(ssid: String) {
        this.ssid = ssid
    }

    fun getWifiPwd(): String {
        return wifiPwd
    }

    fun setWifiPwd(wifiPwd: String) {
        this.wifiPwd = wifiPwd
    }

    fun getServer(): String {
        return server
    }

    fun setServer(server: String) {
        this.server = server
    }

    fun getPort(): Int {
        return port
    }

    fun setPort(port: Int) {
        this.port = port
    }

    fun getUid(): Int {
        return uid
    }

    fun setUid(uid: Int) {
        this.uid = uid
    }

    fun getUserPwd(): String? {
        return userPwd
    }

    fun setUserPwd(userPwd: String?) {
        this.userPwd = userPwd
    }

    fun getCompanyId(): Int {
        return companyId
    }

    fun setCompanyId(companyId: Int) {
        this.companyId = companyId
    }

    fun getBranchId(): Int {
        return branchId
    }

    fun setBranchId(branchId: Int) {
        this.branchId = branchId
    }

    fun getPlugName(): String? {
        return plugName
    }

    fun setPlugName(plugName: String?) {
        this.plugName = plugName
    }

    fun getPlugVersion(): Int {
        return plugVersion
    }

    fun setPlugVersion(plugVersion: Int) {
        this.plugVersion = plugVersion
    }

    companion object {
        private const val LINK_NAME_LENGTH = 51
    }
}
