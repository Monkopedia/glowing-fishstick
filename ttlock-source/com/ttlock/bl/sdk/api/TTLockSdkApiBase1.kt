package com.ttlock.bl.sdk.api

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.util.Context
import android.util.Intent

/**
 * Created on  2019/6/14 0014 14:28
 *
 * @author theodore_hu
 */
internal abstract class TTLockSdkApiBase {

    fun isBLEEnabled(context: Context): Boolean {
        val manager: BluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter: BluetoothAdapter? = manager.getAdapter()
        return adapter != null && adapter.isEnabled()
    }

    abstract fun prepareBTService(context: Context?)
    abstract fun stopBTService()
    abstract fun startScan()
    abstract fun stopScan()

    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }
}
