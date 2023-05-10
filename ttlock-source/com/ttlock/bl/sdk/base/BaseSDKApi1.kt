package com.ttlock.bl.sdk.base

import android.Manifest
import com.ttlock.bl.sdk.wirelessdoorsensor.GattCallbackHelper

open class BaseSDKApi {
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun isBLEEnabled(context: Context): Boolean {
        val manager: BluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter: BluetoothAdapter = manager.getAdapter()
        return adapter != null && adapter.isEnabled()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun requestBleEnable(activity: Activity) {
        val mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableIntent, BleConstant.REQUEST_ENABLE_BT)
        }
    }

    open fun prepareBTService(context: Context?) { //todo:
        GattCallbackHelper.Companion.getInstance().prepare(context)
    }
}