package com.ttlock.bl.sdk.api

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothLeScanner
import android.bluetooth.ScanCallback
import android.bluetooth.ScanResult
import com.ttlock.bl.sdk.callback.ScanKeypadCallback
import com.ttlock.bl.sdk.device.WirelessKeypad
import com.ttlock.bl.sdk.entity.LockError
import com.ttlock.bl.sdk.util.LogUtil

/**
 * Created by TTLock on 2019/4/28.
 */
internal class ScanManager {
    private var scanCallback: ScanKeypadCallback? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var scanner: BluetoothLeScanner? = null
    private var leScanCallback: ScanCallback? = null

    private fun prepare() {
        if (mBluetoothAdapter == null) mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (scanner == null && mBluetoothAdapter != null) {
            scanner = mBluetoothAdapter!!.getBluetoothLeScanner()
        }
        if (leScanCallback == null) { // TODO:更换对象
            leScanCallback = object : android.bluetooth.ScanCallback() {

                override fun onScanResult(callbackType: Int, result: ScanResult) {
                    super.onScanResult(callbackType, result)
                    scanCallback?.onScanKeyboardSuccess(WirelessKeypad(result))
                }

                override fun onScanFailed(errorCode: Int) {
                    super.onScanFailed(errorCode)
                    if (scanCallback == null) {
                        return
                    }
                    var error: LockError = LockError.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES
                    when (errorCode) {
                        ScanCallback.SCAN_FAILED_ALREADY_STARTED ->
                            error =
                                LockError.SCAN_FAILED_ALREADY_START
                        ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED ->
                            error =
                                LockError.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED
                        ScanCallback.SCAN_FAILED_INTERNAL_ERROR ->
                            error =
                                LockError.SCAN_FAILED_INTERNAL_ERROR
                        ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED ->
                            error =
                                LockError.SCAN_FAILED_FEATURE_UNSUPPORTED
                        else -> {}
                    }
                    scanCallback!!.onFail(error)
                }
            }
        }
    }

    fun startScan(scanCallback: ScanKeypadCallback?) {
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
        if (leScanCallback != null && scanner != null) {
            scanner!!.stopScan(leScanCallback!!)
            scanCallback = null
        }
    }

    companion object {
        protected var UUID_SERVICE = "00001810-0000-1000-8000-00805f9b34fb"
        const val SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES = 5
        private val instance = ScanManager()
        fun getInstance(): ScanManager {
            return instance
        }
    }
}
