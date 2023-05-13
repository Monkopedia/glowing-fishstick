package com.ttlock.bl.sdk.api

import com.ttlock.bl.sdk.callback.OperationType
import com.ttlock.bl.sdk.entity.ConnectParam

import android.util.Context
import com.ttlock.bl.sdk.callback.InitKeypadCallback
import com.ttlock.bl.sdk.callback.ScanKeypadCallback
import com.ttlock.bl.sdk.device.WirelessKeypad

/**
 * Created by TTLock on 2019/4/24.
 */
class WirelessKeypadClient private constructor() {
    private val mApi: WirelessKeypadSDKApi

    init {
        mApi = WirelessKeypadSDKApi()
    }

    private object InstanceHolder {
        val mInstance = WirelessKeypadClient()
    }

    fun isBLEEnabled(context: Context): Boolean {
        return mApi.isBLEEnabled(context)
    }

    fun prepareBTService(context: Context?) {
        mApi.prepareBTService(context)
    }

    fun startScanKeyboard(callback: ScanKeypadCallback?) {
        mApi.startScan(callback)
    }

    fun stopBTService() {
        mApi.stopBTService()
    }

    fun stopScanKeyboard() {
        mApi.stopScan()
    }

    fun initializeKeypad(device: WirelessKeypad?, lockmac: String?, callback: InitKeypadCallback) {
        if (!LockCallbackManager.Companion.getInstance()
            .isDeviceBusy(OperationType.INIT_KEYPAD, callback)
        ) {
            val connectParam = ConnectParam()
            connectParam.lockmac = lockmac
            //            connectParam.setFactoryDate(factoryDate);
//            if (TextUtils.isEmpty(connectParam.getFactoryDate())) {
//                connectParam.setFactoryDate("19700101");
//            }
            ConnectManager.Companion.getInstance().storeConnectParamForCallback(connectParam)
            ConnectManager.Companion.getInstance().connect2Device(device!!)
        }
    }

    companion object {
        const val REQUEST_ENABLE_BT = 1
        fun getDefault(): WirelessKeypadClient {
            return InstanceHolder.mInstance
        }
    }
}
