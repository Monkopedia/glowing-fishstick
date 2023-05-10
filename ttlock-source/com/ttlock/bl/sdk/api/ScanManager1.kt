package com.ttlock.bl.sdk.api

import android.Manifest
import java.util.ArrayList

/**
 * Created by TTLock on 2019/4/28.
 */
internal class ScanManager {
    private var scanCallback: ScanKeypadCallback? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var scanner: BluetoothLeScanner? = null
    private var leScanCallback: ScanCallback? = null
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun prepare() {
        if (mBluetoothAdapter == null) mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (scanner == null && mBluetoothAdapter != null) {
            scanner = mBluetoothAdapter.getBluetoothLeScanner()
        }
        if (leScanCallback == null) { //TODO:更换对象
            leScanCallback = object : ScanCallback() {
                @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH])
                fun onScanResult(callbackType: Int, result: ScanResult?) {
                    super.onScanResult(callbackType, result)
                    if (scanCallback != null) {
                        scanCallback.onScanKeyboardSuccess(WirelessKeypad(result))
                    }
                }

                fun onScanFailed(errorCode: Int) {
                    super.onScanFailed(errorCode)
                    if (scanCallback == null) {
                        return
                    }
                    var error: LockError = LockError.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES
                    when (errorCode) {
                        ScanCallback.SCAN_FAILED_ALREADY_STARTED -> error =
                            LockError.SCAN_FAILED_ALREADY_START
                        ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> error =
                            LockError.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED
                        ScanCallback.SCAN_FAILED_INTERNAL_ERROR -> error =
                            LockError.SCAN_FAILED_INTERNAL_ERROR
                        ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED -> error =
                            LockError.SCAN_FAILED_FEATURE_UNSUPPORTED
                        else -> {}
                    }
                    scanCallback.onFail(error)
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH])
    fun startScan(scanCallback: ScanKeypadCallback?) {
        prepare()
        this.scanCallback = scanCallback
        if (scanner == null) {
            LogUtil.w("BT le scanner not available")
        } else {
            val settings: ScanSettings = Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()
            val filters: MutableList<ScanFilter> = ArrayList<ScanFilter>()
            filters.add(Builder().setServiceUuid(ParcelUuid.fromString(UUID_SERVICE)).build())
            scanner.startScan(filters, settings, leScanCallback)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH])
    fun stopScan() { //TODO:
        if (leScanCallback != null && scanner != null) {
            scanner.stopScan(leScanCallback)
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