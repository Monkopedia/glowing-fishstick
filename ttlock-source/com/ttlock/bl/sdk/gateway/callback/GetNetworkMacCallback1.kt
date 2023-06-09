package com.ttlock.bl.sdk.gateway.callback

import com.ttlock.bl.sdk.gateway.model.GatewayError

/**
 * Created on  2019/4/10 0010 10:25
 *
 * @author theodre
 */
interface GetNetworkMacCallback : GatewayCallback {
    fun onGetNetworkMacSuccess(networkMac: String?)
    override fun onFail(error: GatewayError)
}
