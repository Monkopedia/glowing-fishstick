package com.ttlock.bl.sdk.wirelessdoorsensor

import android.util.SparseArray
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.ConnectCallback
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.DoorSensorCallback
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.ScanWirelessDoorSensorCallback
import com.ttlock.bl.sdk.wirelessdoorsensor.model.DoorSensorError
import com.ttlock.bl.sdk.wirelessdoorsensor.model.OperationType

/**
 * Created on  2019/4/2 0002 15:12
 *
 */
internal class DoorSensorCallbackManager private constructor() {
    private var mScanCallback: ScanWirelessDoorSensorCallback? = null
    private var mConnectCallback: ConnectCallback? = null
    private val mCallbackArray: SparseArray<DoorSensorCallback> = SparseArray(1)

    init {
        mCallbackArray.clear()
    }

    private object InstanceHolder {
        val mInstance = DoorSensorCallbackManager()
    }

    fun setScanCallback(callback: ScanWirelessDoorSensorCallback?) {
        mScanCallback = callback
    }

    fun getScanCallback(): ScanWirelessDoorSensorCallback? {
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

    fun isBusy(type: Int, callback: DoorSensorCallback): Boolean {
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

    fun getCallbackWithoutRemove(): DoorSensorCallback? {
        if (mCallbackArray.size() === 0) {
            return null
        }
        val operationType: Int = mCallbackArray.keyAt(0)
        return mCallbackArray.get(operationType)
    }

    fun getCallback(): DoorSensorCallback? {
        if (mCallbackArray.size() === 0) {
            return null
        }
        val operationType: Int = mCallbackArray.keyAt(0)
        val currentCallback: DoorSensorCallback? = mCallbackArray.get(operationType)
        if (currentCallback != null) {
            mCallbackArray.clear()
        }
        return currentCallback
    }

    fun clearAllCallback() {
        mCallbackArray.clear()
    }

    private fun multiConnectFastFail(callback: DoorSensorCallback) {
        callback.onFail(DoorSensorError.Device_IS_BUSY)
    }

    companion object {
        fun getInstance(): DoorSensorCallbackManager {
            return InstanceHolder.mInstance
        }
    }
}
