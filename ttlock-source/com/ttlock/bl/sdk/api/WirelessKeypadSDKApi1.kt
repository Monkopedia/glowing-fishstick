package com.ttlock.bl.sdk.api

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import com.ttlock.bl.sdk.entity.ConnectParam
import android.util.Context
import com.ttlock.bl.sdk.callback.ScanKeypadCallback
import com.ttlock.bl.sdk.device.WirelessKeypad

/**
 * Created by TTLock on 2019/4/24.
 */
internal class WirelessKeypadSDKApi {

    fun isBLEEnabled(context: Context): Boolean {
        val manager: BluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter: BluetoothAdapter? = manager.getAdapter()
        return adapter != null && adapter.isEnabled()
    }

    fun prepareBTService(context: Context?) {
        // TODO:
        GattCallbackHelper.Companion.getInstance().prepare(context)
    }

    fun stopBTService() {
        GattCallbackHelper.Companion.getInstance().clear()
    }

    fun startScan(callback: ScanKeypadCallback?) {
        ScanManager.Companion.getInstance().startScan(callback)
    }

    fun stopScan() {
        ScanManager.Companion.getInstance().stopScan()
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1
        fun initKeyapd(keypad: WirelessKeypad, param: ConnectParam) {
            CommandUtil.setLock(param, keypad)
        }
    }
}
