package com.ttlock.bl.sdk.remote.api

import android.os.Handler
import com.ttlock.bl.sdk.device.Remote
import com.ttlock.bl.sdk.remote.callback.ConnectCallback
import com.ttlock.bl.sdk.remote.model.ConnectParam
import com.ttlock.bl.sdk.remote.model.OperationType

/**
 * Created on  2019/4/12 SetLockTimeCallback 11:30
 * @author theodore
 */
internal class ConnectManager private constructor() : ConnectCallback {
    private val mApi: WirelessKeyFobSDKApi
    private val mDataCheckHandler: Handler = Handler()
    private var mCurrentMac: String? = null
    private var mConnectParam: ConnectParam?
    private val mDataCheckErrorRunnable = Runnable {
        val mCallback: RemoteCallback = RemoteCallbackManager.Companion.getInstance().getCallback()
        if (mCallback != null) {
            mCallback.onFail(RemoteError.DATA_FORMAT_ERROR)
        }
    }

    init {
        mApi = WirelessKeyFobSDKApi()
        mConnectParam = null
    }

    fun getConnectParam(): ConnectParam? {
        return mConnectParam
    }

    override fun onConnectSuccess(device: Remote?) {
        val callbackType: Int = RemoteCallbackManager.Companion.getInstance().getOperationType()
        //TODO:
//        startDataCheckTimer();
        if (mConnectParam == null) return
        when (callbackType) {
            OperationType.INIT -> mApi.init(device)
            else -> {}
        }
    }

    fun storeConnectParamForCallback(param: ConnectParam?) {
        mConnectParam = param
    }

    override fun onFail(error: RemoteError?) {
        val callbackType: Int = RemoteCallbackManager.Companion.getInstance().getOperationType()
        val mCallback: RemoteCallback = RemoteCallbackManager.Companion.getInstance().getCallback()
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

    fun removeDataCheckTimer() {
        mDataCheckHandler.removeCallbacks(mDataCheckErrorRunnable)
    }

    fun isDeviceConnected(address: String): Boolean {
        //TODO:
        return GattCallbackHelper.Companion.getInstance().isConnected(address)
        //        return false;
    }

    fun connect2Device(address: String?) {
        mCurrentMac = address
        RemoteCallbackManager.Companion.getInstance().setConnectCallback(this)
        //TODO:
//        BluetoothImpl.getInstance().connect(address);
    }

    fun connect2Device(device: Remote?) {
        RemoteCallbackManager.Companion.getInstance().setConnectCallback(this)
        GattCallbackHelper.Companion.getInstance().connect(device)
    }

    private fun startDataCheckTimer() {
        mDataCheckHandler.postDelayed(mDataCheckErrorRunnable, 500)
    }

    private fun retryConnect(callbackType: Int, callback: RemoteCallback) {
        connect2Device(mCurrentMac)
        RemoteCallbackManager.Companion.getInstance().isBusy(callbackType, callback)
        mCurrentMac = ""
    }

    companion object {
        fun getInstance(): ConnectManager {
            return InstanceHolder.mInstance
        }
    }
}