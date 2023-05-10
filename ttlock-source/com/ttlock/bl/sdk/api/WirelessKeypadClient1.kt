package com.ttlock.bl.sdk.api

import android.Manifest
import com.ttlock.bl.sdk.callback.OperationType
import com.ttlock.bl.sdk.entity.ConnectParam

/**
 * Created by TTLock on 2019/4/24.
 */
class WirelessKeypadClient private constructor() {
    private val mApi: WirelessKeypadSDKApi

    init {
        mApi = WirelessKeypadSDKApi()
    }

    private object InstanceHolder {
        private val mInstance = WirelessKeypadClient()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun isBLEEnabled(context: Context): Boolean {
        return mApi.isBLEEnabled(context)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun requestBleEnable(activity: Activity) {
        mApi.requestBleEnable(activity)
    }

    fun prepareBTService(context: Context?) {
        mApi.prepareBTService(context)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    fun startScanKeyboard(callback: ScanKeypadCallback?) {
        mApi.startScan(callback)
    }

    fun stopBTService() {
        mApi.stopBTService()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
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
            ConnectManager.Companion.getInstance().connect2Device(device)
        }
    }

    companion object {
        const val REQUEST_ENABLE_BT = 1
        fun getDefault(): WirelessKeypadClient {
            return InstanceHolder.mInstance
        }
    }
}