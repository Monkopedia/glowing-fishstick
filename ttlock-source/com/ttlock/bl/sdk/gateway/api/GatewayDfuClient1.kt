package com.ttlock.bl.sdk.gateway.api

import android.content.Context
import com.ttlock.bl.sdk.gateway.callback.DfuCallback

/**
 * Created by TTLock on 2017/8/16.
 */
class GatewayDfuClient {
    private val mApi: DfuSDKApi

    init {
        mApi = DfuSDKApi()
    }

    private object InstanceHolder {
        private val mInstance = GatewayDfuClient()
    }

    fun startDfu(
        context: Context?,
        clientId: String?,
        accessToken: String?,
        gatewayId: Int,
        gatewayMac: String?,
        dfuCallback: DfuCallback?
    ) {
        //TODO:校验值是否为null
        mApi.startDfu(context, clientId, accessToken, gatewayId, gatewayMac, dfuCallback)
    }

    fun retryEnterDfuModeByNet() {
        mApi.retryEnterDfuModeByNet()
    }

    fun retryEnterDfuModeByBle() {
        mApi.retryEnterDfuModeByBle()
    }

    fun abortDfu() {
        mApi.abortUpgradeProcess()
    }

    companion object {
        fun getDefault(): GatewayDfuClient {
            return InstanceHolder.mInstance
        }
    }
}