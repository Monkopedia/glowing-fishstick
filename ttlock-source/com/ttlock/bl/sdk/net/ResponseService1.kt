package com.ttlock.bl.sdk.net

import java.util.HashMap

/**
 * Created by TTLock on 2016/9/8 0008.
 */
object ResponseService {
    private const val TAG = "ResponseService"
    private const val actionUrl = "https://api.ttlock.com.cn"

    //    private static String actionUrl = "http://120.26.119.23:8085";
    private val actionUrlV3 = ResponseService.actionUrl + "/v3"

    /**
     * 解绑管理员
     * @param lockId
     * @return
     */
    fun getRecoverData(clientId: String?, accessToken: String?, lockId: Int): String {
        val url = ResponseService.actionUrlV3 + "/lock/getRecoverData"
        val params: HashMap<*, *> = HashMap<Any?, Any?>()
        params["clientId"] = clientId
        params["accessToken"] = accessToken
        params["lockId"] = lockId.toString()
        params["date"] = System.currentTimeMillis().toString()
        return OkHttpRequest.Companion.sendPost(url, params)
    }

    /**
     * 解绑管理员
     * @param lockId
     * @return
     */
    fun getUpgradePackage(clientId: String?, accessToken: String?, lockId: Int): String {
        val url = ResponseService.actionUrlV3 + "/lock/getUpgradePackage"
        val params: HashMap<*, *> = HashMap<Any?, Any?>()
        params["clientId"] = clientId
        params["accessToken"] = accessToken
        params["lockId"] = lockId.toString()
        params["date"] = System.currentTimeMillis().toString()
        return OkHttpRequest.Companion.sendPost(url, params)
    }

    fun lockUpgradeSuccess(
        clientId: String?,
        accessToken: String?,
        lockId: Int,
        lockData: String?
    ): String {
        val url = ResponseService.actionUrlV3 + "/lock/upgradeSuccess"
        val params: HashMap<*, *> = HashMap<Any?, Any?>()
        params["clientId"] = clientId
        params["accessToken"] = accessToken
        params["lockId"] = lockId.toString()
        params["lockData"] = lockData
        //        params.put("specialValue", String.valueOf(specialValue));
        params["date"] = System.currentTimeMillis().toString()
        return OkHttpRequest.Companion.sendPost(url, params)
    }

    /**
     * 上传操作日志
     * @param clientId
     * @param accessToken
     * @param lockId
     * @param records
     * @return
     */
    fun uploadOperateLog(
        clientId: String?,
        accessToken: String?,
        lockId: Int,
        records: String?
    ): String {
        val url = ResponseService.actionUrlV3 + "/lockRecord/upload"
        val params: HashMap<*, *> = HashMap<Any?, Any?>()
        params["clientId"] = clientId
        params["accessToken"] = accessToken
        params["lockId"] = lockId.toString()
        params["records"] = records
        params["date"] = System.currentTimeMillis().toString()
        return OkHttpRequest.Companion.sendPost(url, params)
    }

    fun enterDfuMode(clientId: String?, accessToken: String?, gatewayId: Int): String {
        val url = ResponseService.actionUrlV3 + "/gateway/setUpgradeMode"
        val params: HashMap<*, *> = HashMap<Any?, Any?>()
        params["clientId"] = clientId
        params["accessToken"] = accessToken
        params["gatewayId"] = gatewayId.toString()
        params["date"] = System.currentTimeMillis().toString()
        return OkHttpRequest.Companion.sendPost(url, params)
    }

    fun getPlugUpgradePackage(clientId: String?, accessToken: String?, gatewayId: Int): String {
        val url = ResponseService.actionUrlV3 + "/gateway/getUpgradePackage"
        val params: HashMap<*, *> = HashMap<Any?, Any?>()
        params["clientId"] = clientId
        params["accessToken"] = accessToken
        params["gatewayId"] = gatewayId.toString()
        params["date"] = System.currentTimeMillis().toString()
        return OkHttpRequest.Companion.sendPost(url, params)
    }

    fun plugUpgradeSuccess(clientId: String?, accessToken: String?, gatewayId: Int): String {
        val url = ResponseService.actionUrlV3 + "/gateway/upgradeSuccess"
        val params: HashMap<*, *> = HashMap<Any?, Any?>()
        params["clientId"] = clientId
        params["accessToken"] = accessToken
        params["gatewayId"] = gatewayId.toString()
        params["date"] = System.currentTimeMillis().toString()
        return OkHttpRequest.Companion.sendPost(url, params)
    }
}
