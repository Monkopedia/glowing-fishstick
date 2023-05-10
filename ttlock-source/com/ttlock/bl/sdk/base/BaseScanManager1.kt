package com.ttlock.bl.sdk.base

import android.Manifest
import java.util.ArrayList

/**
 * Created by TTLock on 2019/4/28.
 */
class BaseScanManager {
    private var UUID_SERVICE: String? = null

    /**
     * 扫描回调
     */
    private var scanCallback: BaseScanCallback<*>? = null
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
                fun onScanResult(callbackType: Int, result: ScanResult) {
                    super.onScanResult(callbackType, result)
                    scanCallback(result)
                }

                fun onScanFailed(errorCode: Int) {
                    super.onScanFailed(errorCode)
                    LogUtil.w("errorCode:$errorCode")
                }
            }
        }
    }

    private fun scanCallback(result: ScanResult) {
        var ttDevice: TTDevice? = null
        when (UUID_SERVICE) {
            BleConstant.UUID_SERVICE_Door_Sensor -> ttDevice = WirelessDoorSensor(result)
        }
        scanCallback!!.onScan(ttDevice)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH])
    fun startScan(UUID_SERVICE: String?, scanCallback: BaseScanCallback<*>?) {
        this.UUID_SERVICE = UUID_SERVICE
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
    fun stopScan() {
        if (leScanCallback != null && scanner != null) {
            scanner.stopScan(leScanCallback)
        }
    }

    companion object {
        private val instance = BaseScanManager()
        fun getInstance(): BaseScanManager {
            return instance
        }
    }
}