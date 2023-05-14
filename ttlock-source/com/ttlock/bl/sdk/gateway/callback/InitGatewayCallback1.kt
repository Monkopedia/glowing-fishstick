package com.ttlock.bl.sdk.gateway.callback

import com.ttlock.bl.sdk.gateway.model.DeviceInfo
import com.ttlock.bl.sdk.gateway.model.GatewayError

/**
 * Created on  2019/4/10 0010 10:25
 *
 * @author theodre
 */
interface InitGatewayCallback : GatewayCallback {
    fun onInitGatewaySuccess(deviceInfo: DeviceInfo?)
    override fun onFail(error: GatewayError)
}
