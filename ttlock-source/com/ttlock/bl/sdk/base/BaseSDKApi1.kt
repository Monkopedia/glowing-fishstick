package com.ttlock.bl.sdk.base

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.util.Context
import com.ttlock.bl.sdk.wirelessdoorsensor.GattCallbackHelper

open class BaseSDKApi {

    fun isBLEEnabled(context: Context): Boolean {
        val manager: BluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter: BluetoothAdapter? = manager.getAdapter()
        return adapter != null && adapter.isEnabled()
    }

    open fun prepareBTService(context: Context) { // todo:
        GattCallbackHelper.Companion.getInstance().prepare(context)
    }
}
