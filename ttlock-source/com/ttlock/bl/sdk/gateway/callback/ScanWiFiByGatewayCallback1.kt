package com.ttlock.bl.sdk.gateway.callback

import com.ttlock.bl.sdk.gateway.model.GatewayError
import com.ttlock.bl.sdk.gateway.model.WiFi

/**
 * Created on  2019/4/10 0010 10:25
 *
 * @author theodre
 */
interface ScanWiFiByGatewayCallback : GatewayCallback {
    fun onScanWiFiByGateway(wiFis: List<WiFi?>?)
    fun onScanWiFiByGatewaySuccess()
    override fun onFail(error: GatewayError?)
}
