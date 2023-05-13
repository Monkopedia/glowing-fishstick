package com.ttlock.bl.sdk.remote.api

import com.ttlock.bl.sdk.device.Remote
import com.ttlock.bl.sdk.remote.model.ConnectParam
import com.ttlock.bl.sdk.remote.model.OperationType

/**
 * Created by TTLock on 2021/1/25.
 */
class RemoteClient private constructor() {
    private val mApi: WirelessKeyFobSDKApi

    init {
        mApi = WirelessKeyFobSDKApi()
    }

    private object InstanceHolder {
        private val mInstance = RemoteClient()
    }

    fun isBLEEnabled(context: Context): Boolean {
        return mApi.isBLEEnabled(context)
    }

    fun requestBleEnable(activity: Activity) {
        mApi.requestBleEnable(activity)
    }

    fun prepareBTService(context: Context?) {
        mApi.prepareBTService(context)
    }

    fun startScan(callback: ScanRemoteCallback?) {
        RemoteCallbackManager.Companion.getInstance().setScanCallback(callback)
        mApi.startScan(callback)
    }

    fun stopScan() {
        mApi.stopScanGateway()
        RemoteCallbackManager.Companion.getInstance().clearScanCallback()
    }

    fun initialize(device: Remote?, lockData: String?, callback: InitRemoteCallback) {
        val lockParam: LockData = EncryptionUtil.parseLockData(lockData)
        if (device == null || lockParam == null) {
            callback.onFail(RemoteError.DATA_FORMAT_ERROR)
            return
        }
        if (!RemoteCallbackManager.Companion.getInstance().isBusy(OperationType.INIT, callback)) {
//            String address = device.getAddress();
//            if(ConnectManager.getInstance().isDeviceConnected(address)){
//                mApi.init(device);
//            } else {
            val connectParam = ConnectParam()
            connectParam.lockmac = lockParam.lockMac
            connectParam.lockKey = lockParam.lockKey
            connectParam.aesKey = lockParam.aesKeyStr
            ConnectManager.Companion.getInstance().storeConnectParamForCallback(connectParam)
            ConnectManager.Companion.getInstance().connect2Device(device)
            //            }
        }
    }

    fun getRemoteSystemInfo(remoteMac: String, callback: GetRemoteSystemInfoCallback) {
        if (!RemoteCallbackManager.Companion.getInstance()
            .isBusy(OperationType.GET_DEVICE_INFO, callback)
        ) {
            val bluetoothDevice: BluetoothDevice =
                BluetoothAdapter.getDefaultAdapter().getRemoteDevice(remoteMac)
            val remote: Remote = Remote(bluetoothDevice)
            if (ConnectManager.Companion.getInstance().isDeviceConnected(remoteMac)) {
                callback.onGetRemoteSystemInfoSuccess(
                    GattCallbackHelper.Companion.getInstance().getDeviceInfo()
                )
            } else {
                val connectParam = ConnectParam()
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(connectParam)
                ConnectManager.Companion.getInstance().connect2Device(remote)
            }
        }
    }

    companion object {
        const val REQUEST_ENABLE_BT = 1
        fun getDefault(): RemoteClient {
            return InstanceHolder.mInstance
        }
    }
}
