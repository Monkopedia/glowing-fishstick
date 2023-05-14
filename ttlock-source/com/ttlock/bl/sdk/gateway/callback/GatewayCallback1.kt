package com.ttlock.bl.sdk.gateway.callback

import com.ttlock.bl.sdk.gateway.model.GatewayError

/**
 * Created on  2019/4/8 0008 15:31
 *
 * @author theodore
 */
interface GatewayCallback {
    fun onFail(error: GatewayError)
}
