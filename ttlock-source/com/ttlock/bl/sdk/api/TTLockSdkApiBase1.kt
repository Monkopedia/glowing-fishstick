package com.ttlock.bl.sdk.api

import android.Manifest

/**
 * Created on  2019/6/14 0014 14:28
 *
 * @author theodore_hu
 */
internal abstract class TTLockSdkApiBase {
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
            activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
        }
    }

    abstract fun prepareBTService(context: Context?)
    abstract fun stopBTService()
    abstract fun startScan()
    abstract fun stopScan()

    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }
}