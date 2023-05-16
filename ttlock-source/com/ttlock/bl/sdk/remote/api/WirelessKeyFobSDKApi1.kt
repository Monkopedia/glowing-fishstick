package com.ttlock.bl.sdk.remote.api

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.util.Context
import com.ttlock.bl.sdk.device.Remote
import com.ttlock.bl.sdk.remote.callback.ScanRemoteCallback
import com.ttlock.bl.sdk.remote.command.CommandUtil

/**
 * Created by TTLock on 2019/4/24.
 */
internal class WirelessKeyFobSDKApi {

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

    fun startScan(callback: ScanRemoteCallback?) {
        ScanManager.Companion.getInstance().startScan(callback)
    }

    fun stopScanGateway() {
        ScanManager.Companion.getInstance().stopScan()
    }

    fun init(keyFob: Remote) {
        CommandUtil.setLock(ConnectManager.Companion.getInstance().getConnectParam()!!, keyFob)
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }
}
