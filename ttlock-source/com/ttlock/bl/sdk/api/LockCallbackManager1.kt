package com.ttlock.bl.sdk.api

import android.util.SparseArray
import com.ttlock.bl.sdk.callback.ConnectCallback
import com.ttlock.bl.sdk.callback.LockCallback
import com.ttlock.bl.sdk.callback.OperationType
import com.ttlock.bl.sdk.callback.ScanLockCallback
import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/2 0002 15:12
 *
 * @author theodre
 */
internal class LockCallbackManager private constructor() {
    private var mScanCallback: ScanLockCallback? = null
    private var mConnectCallback: ConnectCallback? = null
    private val mCallbackArray: SparseArray<LockCallback> = SparseArray(1)

    init {
        mCallbackArray.clear()
    }

    private object InstanceHolder {
        val mInstance = LockCallbackManager()
    }

    fun setLockScanCallback(callback: ScanLockCallback?) {
        mScanCallback = callback
    }

    fun getLockScanCallback(): ScanLockCallback? {
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

    fun isDeviceBusy(type: Int, callback: LockCallback, isRollingGate: Boolean): Boolean {
        var isDeviceBusy = false
        if (mCallbackArray.size() > 0) {
            if (!isRollingGate || getOperationType() != type) {
                multiConnectFastFail(callback)
                isDeviceBusy = true
            }
        } else {
            mCallbackArray.put(type, callback)
        }
        return isDeviceBusy
    }

    fun isDeviceBusy(type: Int, callback: LockCallback): Boolean {
        var isDeviceBusy = false
        if (mCallbackArray.size() > 0) {
            multiConnectFastFail(callback)
            isDeviceBusy = true
        } else {
            mCallbackArray.put(type, callback)
        }
        return isDeviceBusy
    }

    fun getOperationType(): Int {
        return if (mCallbackArray.size() > 0) mCallbackArray.keyAt(0) else OperationType.UNKNOWN_TYPE
    }

    fun getCallbackWithoutRemove(): LockCallback? {
        if (mCallbackArray.size() > 0) {
            val operationType: Int = mCallbackArray.keyAt(0)
            return mCallbackArray.get(operationType)
        }
        return null
    }

    fun getCallback(): LockCallback? {
        if (mCallbackArray.size() > 0) {
            val operationType: Int = mCallbackArray.keyAt(0)
            val currentCallback: LockCallback? = mCallbackArray.get(operationType)
            if (currentCallback != null) {
                mCallbackArray.clear()
            }
            return currentCallback
        }
        return null
    }

    fun clearAllCallback() {
        mCallbackArray.clear()
    }

    private fun multiConnectFastFail(callback: LockCallback) {
        callback.onFail(LockError.LOCK_IS_BUSY)
    }

    fun callbackArrayIsEmpty(): Boolean {
        return mCallbackArray.size() === 0
    }

    companion object {
        fun getInstance(): LockCallbackManager {
            return InstanceHolder.mInstance
        }
    }
}
