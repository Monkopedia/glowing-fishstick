package com.ttlock.bl.sdk.gateway.api

import android.util.SparseArray
import com.ttlock.bl.sdk.gateway.callback.ConnectCallback

/**
 * Created by TTLock on 2019/4/24.
 */
internal class GatewayCallbackManager private constructor() {
    private var mScanCallback: ScanGatewayCallback? = null
    private var mConnectCallback: ConnectCallback? = null
    private val mCallbackArray: SparseArray<GatewayCallback> = SparseArray(1)

    init {
        mCallbackArray.clear()
    }

    private object InstanceHolder {
        private val mInstance = GatewayCallbackManager()
    }

    fun setGatewayScanCallback(callback: ScanGatewayCallback?) {
        mScanCallback = callback
    }

    fun getGatewayScanCallback(): ScanGatewayCallback? {
        return mScanCallback
    }

    fun clearScanCallback() {
        mScanCallback = null
    }

    fun setConnectCallback(connectCallback: ConnectCallback?) {
        mConnectCallback = connectCallback
    }

    fun getConnectCallback(): ConnectCallback? {
        return mConnectCallback
    }

    fun isGatewayBusy(type: Int, callback: GatewayCallback?): Boolean {
        val isGatewayBusy = false
        //        if(mCallbackArray.size() > 0){
//            multiConnectFastFail(callback);
//            isGatewayBusy = true;
//        }else {
//            mCallbackArray.put(type,callback);
//        }
        // todo:
        if (mCallbackArray.size() > 0) {
            mCallbackArray.clear()
        }
        mCallbackArray.put(type, callback)
        return isGatewayBusy
    }

    fun getOperationType(): Int {
        return if (mCallbackArray.size() === 0) {
            0
        } else mCallbackArray.keyAt(0)
    }

    //    public GatewayCallback getCallbackWithoutRemove(){
    //        int operationType = mCallbackArray.keyAt(0);
    //        return mCallbackArray.get(operationType);
    //    }
    fun getCallback(): GatewayCallback? {
        if (mCallbackArray.size() === 0) {
            return null
        }
        val operationType: Int = mCallbackArray.keyAt(0)
        return mCallbackArray.get(operationType)
    }

    fun clearAllCallback() {
        mCallbackArray.clear()
    } //    private void multiConnectFastFail(GatewayCallback callback){

    //        callback.onFail(GatewayError.GATEWAY_IS_BUSY);
    //    }
    companion object {
        fun getInstance(): GatewayCallbackManager {
            return InstanceHolder.mInstance
        }
    }
}
