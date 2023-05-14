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
        if (userPwd != null && userPwd!!.length != 32) userPwd = DigitUtil.getMD5(userPwd!!)
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

    @JvmName("getSsid1")
    fun getSsid(): String {
        return ssid
    }

    @JvmName("setSsid1")
    fun setSsid(ssid: String) {
        this.ssid = ssid
    }

    @JvmName("getWifiPwd1")
    fun getWifiPwd(): String {
        return wifiPwd
    }

    @JvmName("setWifiPwd1")
    fun setWifiPwd(wifiPwd: String) {
        this.wifiPwd = wifiPwd
    }

    @JvmName("getServer1")
    fun getServer(): String {
        return server
    }

    @JvmName("setServer1")
    fun setServer(server: String) {
        this.server = server
    }

    @JvmName("getPort1")
    fun getPort(): Int {
        return port
    }

    @JvmName("setPort1")
    fun setPort(port: Int) {
        this.port = port
    }

    @JvmName("getUid1")
    fun getUid(): Int {
        return uid
    }

    @JvmName("setUid1")
    fun setUid(uid: Int) {
        this.uid = uid
    }

    @JvmName("getUserPwd1")
    fun getUserPwd(): String? {
        return userPwd
    }

    @JvmName("setUserPwd1")
    fun setUserPwd(userPwd: String?) {
        this.userPwd = userPwd
    }

    @JvmName("getCompanyId1")
    fun getCompanyId(): Int {
        return companyId
    }

    @JvmName("setCompanyId1")
    fun setCompanyId(companyId: Int) {
        this.companyId = companyId
    }

    @JvmName("getBranchId1")
    fun getBranchId(): Int {
        return branchId
    }

    @JvmName("setBranchId1")
    fun setBranchId(branchId: Int) {
        this.branchId = branchId
    }

    @JvmName("getPlugName1")
    fun getPlugName(): String? {
        return plugName
    }

    @JvmName("setPlugName1")
    fun setPlugName(plugName: String?) {
        this.plugName = plugName
    }

    @JvmName("getPlugVersion1")
    fun getPlugVersion(): Int {
        return plugVersion
    }

    @JvmName("setPlugVersion1")
    fun setPlugVersion(plugVersion: Int) {
        this.plugVersion = plugVersion
    }

    companion object {
        private const val LINK_NAME_LENGTH = 51
    }
}
