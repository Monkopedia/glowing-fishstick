package com.ttlock.bl.sdk.gateway.api

import android.os.Handler
import com.ttlock.bl.sdk.gateway.callback.ConnectCallback
import com.ttlock.bl.sdk.gateway.model.ConnectParam
import com.ttlock.bl.sdk.gateway.model.OperationType

/**
 * Created on  2019/4/12 SetLockTimeCallback 11:30
 * @author theodore
 */
internal class ConnectManager private constructor() : ConnectCallback {
    private val mApi: GatewaySDKApi
    private var mConnectParam: ConnectParam?
    private val mDataCheckHandler: Handler = Handler()
    private var mCurrentMac: String? = null
    private var connectCallback: ConnectCallback? = null
    private var connect_status = CONNECT_STATUS_UNKNOWN
    private val mDataCheckErrorRunnable = Runnable {
        val mCallback: GatewayCallback =
            GatewayCallbackManager.Companion.getInstance().getCallback()
        if (mCallback != null) {
            mCallback.onFail(GatewayError.DATA_FORMAT_ERROR)
        }
    }

    init {
        mApi = GatewaySDKApi()
        mConnectParam = null
    }

    private object InstanceHolder {
        private val mInstance = ConnectManager()
    }

    fun removeDataCheckTimer() {
        mDataCheckHandler.removeCallbacks(mDataCheckErrorRunnable)
    }

    fun storeConnectParamForCallback(param: ConnectParam?) {
        mConnectParam = param
    }

    fun isDeviceConnected(): Boolean {
        connectStatusLog()
        return connect_status == CONNECT_STATUS_CONNECTED
        //        if (connect_status != CONNECT_STATUS_CONNECTED) {
//            return false;
//        }
//        if (TextUtils.isEmpty(mCurrentMac) || TextUtils.isEmpty(address)) {
//            return false;
//        }
//        return mCurrentMac.equals(address);
    }

    @JvmOverloads
    fun connect2Device(address: String?, connectCallback: ConnectCallback? = this) {
        Log.d("OMG", "==connect2Device=")
        this.connectCallback = connectCallback
        mCurrentMac = address
        connect_status = CONNECT_STATUS_CONNECTING
        connectStatusLog()
        GatewayCallbackManager.Companion.getInstance().setConnectCallback(connectCallback)
        GattCallbackHelper.Companion.getInstance().connect(address)
    }

    fun disconnect() {
        connect_status = CONNECT_STATUS_DISCONNECTED
        connectStatusLog()
        GattCallbackHelper.Companion.getInstance().disconnect()
    }

    /**
     *
     * @param device
     * @param connectCallback    对外专门提供的连接回调
     */
    fun connect2Device(device: ExtendedBluetoothDevice, connectCallback: ConnectCallback?) {
        connect_status = CONNECT_STATUS_CONNECTING
        connectStatusLog()
        mCurrentMac = device.getAddress()
        GatewayCallbackManager.Companion.getInstance().setConnectCallback(connectCallback)
        this.connectCallback = connectCallback
        GattCallbackHelper.Companion.getInstance().connect(device)
    }

    private fun startDataCheckTimer() {
        mDataCheckHandler.postDelayed(mDataCheckErrorRunnable, 500)
    }

    override fun onConnectSuccess(device: ExtendedBluetoothDevice?) {
        connect_status = CONNECT_STATUS_CONNECTED
        connectStatusLog()
        val callbackType: Int = GatewayCallbackManager.Companion.getInstance().getOperationType()
        when (callbackType) {
            OperationType.ENTER_DFU_MODE -> mApi.enterDfu(
                mConnectParam!!.mac
            )
            else -> {
                // 对外 单独提供的  连接回调
                val mConnectCallback: ConnectCallback =
                    GatewayCallbackManager.Companion.getInstance().getConnectCallback()
                if (mConnectCallback != null) {
                    Log.d("OMG", "====disconnect==1==")
                    mConnectCallback.onConnectSuccess(device)
                }
            }
        }
    }

    //    /**
    //     * do second connect if first time connect fail.
    //     * @param error
    //     */
    //    @Override
    //    public void onFail(GatewayError error) {
    //        int callbackType = GatewayCallbackManager.getInstance().getOperationType();
    //        GatewayCallback mCallback = GatewayCallbackManager.getInstance().getCallback();
    //        if(mCallback != null){
    //            if(!TextUtils.isEmpty(mCurrentMac)){
    //                retryConnect(callbackType,mCallback);
    //            }else {
    //                mCallback.onFail(error);
    //            }
    //        }
    //    }
    override fun onDisconnected() {
        setDisconnected()
        val mCallback: ConnectCallback =
            GatewayCallbackManager.Companion.getInstance().getConnectCallback()
        if (mCallback != null && mCallback !is ConnectManager) { // 如果是外部 单独的连接回调 才进行断开回调(自己回调自己 死循环)
            mCallback.onDisconnected()
        }
    }

    fun setDisconnected() {
        connect_status = CONNECT_STATUS_DISCONNECTED
        connectStatusLog()
    }

    private fun retryConnect(callbackType: Int, callback: GatewayCallback) {
        Log.d("OMG", "==retryConnect=$callback")
        connect2Device(mCurrentMac, connectCallback)
        GatewayCallbackManager.Companion.getInstance().isGatewayBusy(callbackType, callback)
        mCurrentMac = ""
    }

    private fun connectStatusLog() {
        LogUtil.d("connect_status:$connect_status")
    }

    companion object {
        const val CONNECT_STATUS_UNKNOWN = 0
        const val CONNECT_STATUS_CONNECTING = 1
        const val CONNECT_STATUS_CONNECTED = 2
        const val CONNECT_STATUS_DISCONNECTED = 3
        fun getInstance(): ConnectManager {
            return InstanceHolder.mInstance
        }
    }
}
