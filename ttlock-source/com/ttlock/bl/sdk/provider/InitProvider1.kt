package com.ttlock.bl.sdk.provider

import android.util.getContext
import com.ttlock.bl.sdk.api.TTLockClient
import com.ttlock.bl.sdk.api.WirelessKeypadClient
import com.ttlock.bl.sdk.gateway.api.GatewayClient


/**
 * Created on  2019/5/30 0030 14:36
 * this provider just use for doing sdk init
 * @author theodore_hu
 */
class InitProvider {
    fun onCreate(): Boolean {
        TTLockClient.Companion.getDefault().prepareBTService(getContext())
        GatewayClient.Companion.getDefault().prepareBTService(getContext())
        WirelessKeypadClient.Companion.getDefault().prepareBTService(getContext())
        return true
    }

}
