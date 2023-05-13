package com.ttlock.bl.sdk.remote.api

import android.util.SparseArray
import com.ttlock.bl.sdk.remote.callback.ConnectCallback
import com.ttlock.bl.sdk.remote.model.OperationType

/**
 * Created on  2019/4/2 0002 15:12
 *
 */
internal class RemoteCallbackManager private constructor() {
    private var mScanCallback: ScanRemoteCallback? = null
    private var mConnectCallback: ConnectCallback? = null
    private val mCallbackArray: SparseArray<RemoteCallback> = SparseArray(1)

    init {
        mCallbackArray.clear()
    }

    private object InstanceHolder {
        private val mInstance = RemoteCallbackManager()
    }

    fun setScanCallback(callback: ScanRemoteCallback?) {
        mScanCallback = callback
    }

    fun getScanCallback(): ScanRemoteCallback? {
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

    fun isBusy(type: Int, callback: RemoteCallback): Boolean {
        var isLockBusy = false
        if (mCallbackArray.size() > 0) {
            multiConnectFastFail(callback)
            isLockBusy = true
        } else {
            mCallbackArray.put(type, callback)
        }
        return isLockBusy
    }

    fun getOperationType(): Int {
        return if (mCallbackArray.size() === 0) {
            OperationType.INIT
        } else mCallbackArray.keyAt(0)
    }

    fun getCallbackWithoutRemove(): RemoteCallback? {
        if (mCallbackArray.size() === 0) {
            return null
        }
        val operationType: Int = mCallbackArray.keyAt(0)
        return mCallbackArray.get(operationType)
    }

    fun getCallback(): RemoteCallback? {
        if (mCallbackArray.size() === 0) {
            return null
        }
        val operationType: Int = mCallbackArray.keyAt(0)
        val currentCallback: RemoteCallback = mCallbackArray.get(operationType)
        if (currentCallback != null) {
            mCallbackArray.clear()
        }
        return currentCallback
    }

    fun clearAllCallback() {
        mCallbackArray.clear()
    }

    private fun multiConnectFastFail(callback: RemoteCallback) {
        callback.onFail(RemoteError.Device_IS_BUSY)
    }

    companion object {
        fun getInstance(): RemoteCallbackManager {
            return InstanceHolder.mInstance
        }
    }
}
