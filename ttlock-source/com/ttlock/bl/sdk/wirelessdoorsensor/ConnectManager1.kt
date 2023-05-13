package com.ttlock.bl.sdk.wirelessdoorsensor

import android.os.Handler
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.ConnectCallback
import com.ttlock.bl.sdk.wirelessdoorsensor.model.ConnectParam
import com.ttlock.bl.sdk.wirelessdoorsensor.model.OperationType

/**
 * Created on  2019/4/12 SetLockTimeCallback 11:30
 * @author theodore
 */
internal class ConnectManager private constructor() : ConnectCallback {
    private val mApi: WirelessDoorSensorSDKApi
    private val mDataCheckHandler: Handler = Handler()
    private var mCurrentMac: String? = null
    private var mConnectParam: ConnectParam?

    init {
        mApi = WirelessDoorSensorSDKApi()
        mConnectParam = null
    }

    fun getConnectParam(): ConnectParam? {
        return mConnectParam
    }

    override fun onConnectSuccess(device: WirelessDoorSensor?) {
        val callbackType: Int = DoorSensorCallbackManager.Companion.getInstance().getOperationType()
        // TODO:
        if (mConnectParam == null) return
        when (callbackType) {
            OperationType.INIT -> mApi.init(device)
            OperationType.ENTER_DFU -> mApi.checkAdmin(device)
            else -> {}
        }
    }

    fun storeConnectParamForCallback(param: ConnectParam?) {
        mConnectParam = param
    }

    override fun onFail(error: DoorSensorError?) {
        val callbackType: Int = DoorSensorCallbackManager.Companion.getInstance().getOperationType()
        val mCallback: DoorSensorCallback =
            DoorSensorCallbackManager.Companion.getInstance().getCallback()
        if (mCallback != null) {
            if (!TextUtils.isEmpty(mCurrentMac)) {
                retryConnect(callbackType, mCallback)
            } else {
                mCallback.onFail(error)
            }
        }
    }

    private object InstanceHolder {
        private val mInstance = ConnectManager()
    }

    //    public void removeDataCheckTimer(){
    //        mDataCheckHandler.removeCallbacks(mDataCheckErrorRunnable);
    //    }
    fun isDeviceConnected(address: String?): Boolean {
        // TODO:
//        return BluetoothImpl.getInstance().isConnected(address);
        return false
    }

    fun connect2Device(address: String?) {
        mCurrentMac = address
        DoorSensorCallbackManager.Companion.getInstance().setConnectCallback(this)
    }

    fun connect2Device(device: WirelessDoorSensor?) {
        DoorSensorCallbackManager.Companion.getInstance().setConnectCallback(this)
        GattCallbackHelper.Companion.getInstance().connect(device)
    }

    //    private void startDataCheckTimer(){
    //        mDataCheckHandler.postDelayed(mDataCheckErrorRunnable,500);
    //    }
    private fun retryConnect(callbackType: Int, callback: DoorSensorCallback) {
        connect2Device(mCurrentMac)
        DoorSensorCallbackManager.Companion.getInstance().isBusy(callbackType, callback)
        mCurrentMac = ""
    }

    companion object {
        //    private Runnable mDataCheckErrorRunnable = new Runnable() {
        //        @Override
        //        public void run() {
        //            KeyFobCallback mCallback = KeyFobCallbackManager.getInstance().getCallback();
        //            if(mCallback != null){
        //                mCallback.onFail(KeyFobError.DATA_FORMAT_ERROR);
        //            }
        //        }
        //    };
        fun getInstance(): ConnectManager {
            return InstanceHolder.mInstance
        }
    }
}
