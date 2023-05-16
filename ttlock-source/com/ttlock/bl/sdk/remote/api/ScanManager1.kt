package com.ttlock.bl.sdk.remote.api

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothLeScanner
import android.bluetooth.ScanCallback
import android.bluetooth.ScanResult
import com.ttlock.bl.sdk.device.Remote
import com.ttlock.bl.sdk.remote.callback.ScanRemoteCallback
import com.ttlock.bl.sdk.util.LogUtil
import java.util.ArrayList

/**
 * Created by TTLock on 2019/4/28.
 */
internal class ScanManager {
    private var scanCallback: ScanRemoteCallback? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var scanner: BluetoothLeScanner? = null
    private var leScanCallback: ScanCallback? = null

    private fun prepare() {
        if (mBluetoothAdapter == null) mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (scanner == null && mBluetoothAdapter != null) {
            scanner = mBluetoothAdapter!!.getBluetoothLeScanner()
        }
        if (leScanCallback == null) { // TODO:更换对象
            leScanCallback = object : ScanCallback() {

                override fun onScanResult(callbackType: Int, result: ScanResult) {
                    super.onScanResult(callbackType, result)
                    scanCallback!!.onScanRemote(Remote(result))
                }

                override fun onScanFailed(errorCode: Int) {
                    super.onScanFailed(errorCode)
                    LogUtil.w("errorCode:$errorCode")
                }
            }
        }
    }

    fun startScan(scanCallback: ScanRemoteCallback?) {
        prepare()
        this.scanCallback = scanCallback
        if (scanner == null) {
            LogUtil.w("BT le scanner not available")
        } else {
//            val settings: ScanSettings = Builder()
//                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
//                .build()
//            val filters: MutableList<ScanFilter> = ArrayList<ScanFilter>()
//            filters.add(Builder().setServiceUuid(ParcelUuid.fromString(UUID_SERVICE)).build())
            scanner!!.startScan(UUID_SERVICE, leScanCallback)
        }
    }

    fun stopScan() { // TODO:
        if (leScanCallback != null && scanner != null) scanner!!.stopScan(leScanCallback!!)
    }

    companion object {
        protected var UUID_SERVICE = "00001710-0000-1000-8000-00805f9b34fb"
        private val instance = ScanManager()
        fun getInstance(): ScanManager {
            return instance
        }
    }
}
