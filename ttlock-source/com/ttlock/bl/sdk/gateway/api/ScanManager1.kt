package com.ttlock.bl.sdk.gateway.api

import android.Manifest
import java.util.ArrayList

/**
 * Created by TTLock on 2019/4/28.
 */
internal class ScanManager {
    private var scanGatewayCallback: ScanGatewayCallback? = null
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
                    scanGatewayCallback.onScanGatewaySuccess(ExtendedBluetoothDevice(result))
                }

                fun onScanFailed(errorCode: Int) {
                    super.onScanFailed(errorCode)
                    scanGatewayCallback.onScanFailed(errorCode)
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH])
    fun startScan(scanGatewayCallback: ScanGatewayCallback?) {
        prepare()
        this.scanGatewayCallback = scanGatewayCallback
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
        if (leScanCallback != null && scanner != null && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) scanner.stopScan(
            leScanCallback
        )
    }

    companion object {
        protected var UUID_SERVICE = "00001911-0000-1000-8000-00805f9b34fb"
        private val instance = ScanManager()
        fun getInstance(): ScanManager {
            return instance
        }
    }
}