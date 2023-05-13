package com.ttlock.bl.sdk.api

import com.ttlock.bl.sdk.callback.DfuCallback

import android.util.Context
import com.ttlock.bl.sdk.callback.GetLockSystemInfoCallback

/**
 * Created by TTLock on 2017/8/16.
 */
class LockDfuClient {
    private val mApi: DfuSDKApi

    init {
        mApi = DfuSDKApi()
    }

    private object InstanceHolder {
        val mInstance = LockDfuClient()
    }

    fun getLockSystemInfo(
        lockData: String?,
        lockMac: String?,
        callback: GetLockSystemInfoCallback
    ) {
        mApi.getLockSystemInfo(lockData, lockMac, callback)
    }

    fun startDfu(
        context: Context?,
        clientId: String,
        accessToken: String,
        lockid: Int,
        lockData: String?,
        lockMac: String?,
        dfuCallback: DfuCallback?
    ) {
        // TODO:校验值是否为null
        mApi.startDfu(context, clientId, accessToken, lockid, lockData, lockMac, dfuCallback)
    }

    fun startDfu(
        context: Context?,
        lockData: String?,
        lockMac: String?,
        firmwarePackage: String?,
        dfuCallback: DfuCallback?
    ) {
        // TODO:校验值是否为null
        mApi.startDfu(context, lockData, lockMac, firmwarePackage, dfuCallback)
    }

    fun retry() {
        mApi.retry()
    }

    fun abortDfu() {
        mApi.abortUpgradeProcess()
    }

    companion object {
        fun getDefault(): LockDfuClient {
            return InstanceHolder.mInstance
        }
    }
}
