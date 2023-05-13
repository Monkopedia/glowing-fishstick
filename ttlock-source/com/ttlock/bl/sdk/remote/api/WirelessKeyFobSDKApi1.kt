package com.ttlock.bl.sdk.remote.api

import com.ttlock.bl.sdk.device.Remote
import com.ttlock.bl.sdk.remote.command.CommandUtil

/**
 * Created by TTLock on 2019/4/24.
 */
internal class WirelessKeyFobSDKApi {

    fun isBLEEnabled(context: Context): Boolean {
        val manager: BluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter: BluetoothAdapter = manager.getAdapter()
        return adapter != null && adapter.isEnabled()
    }

    fun requestBleEnable(activity: Activity) {
        val mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
        }
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

    fun init(keyFob: Remote?) {
        CommandUtil.setLock(ConnectManager.Companion.getInstance().getConnectParam(), keyFob)
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }
}
