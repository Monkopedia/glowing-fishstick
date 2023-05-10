package com.ttlock.bl.sdk.api

import android.Manifest
import com.ttlock.bl.sdk.entity.ConnectParam

/**
 * Created by TTLock on 2019/4/24.
 */
internal class WirelessKeypadSDKApi {
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

    fun prepareBTService(context: Context?) {
        //TODO:
        GattCallbackHelper.Companion.getInstance().prepare(context)
    }

    fun stopBTService() {
        GattCallbackHelper.Companion.getInstance().clear()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    fun startScan(callback: ScanKeypadCallback?) {
        ScanManager.Companion.getInstance().startScan(callback)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    fun stopScan() {
        ScanManager.Companion.getInstance().stopScan()
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1
        fun initKeyapd(keypad: WirelessKeypad, param: ConnectParam?) {
            CommandUtil.setLock(param, keypad)
        }
    }
}