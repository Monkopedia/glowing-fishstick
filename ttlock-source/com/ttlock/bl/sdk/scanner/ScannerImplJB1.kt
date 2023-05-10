package com.ttlock.bl.sdk.scanner

import android.Manifest

/**
 * Created by TTLock on 2016/5/12.
 */
class ScannerImplJB : ScannerCompat(), BluetoothAdapter.LeScanCallback {
    private val mBluetoothAdapter: BluetoothAdapter

    init {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH])
    fun onLeScan(device: BluetoothDevice?, rssi: Int, scanRecord: ByteArray?) {
        //TODO:HEX
//        LogUtil.i("device=" + device + " " + device.getName() + " rssi=" + rssi + " scanRecord = " + Arrays.toString(scanRecord), DBG);
        mIScanCallback!!.onScan(ExtendedBluetoothDevice(device, rssi, scanRecord))
    }

    //    @Deprecated
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH])
    override fun startScanInternal(serviceUuids: Array<UUID?>?) {
        //TODO:check
//        ThreadPool.getThreadPool().execute(new Runnable() {
//            @Override
//            @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
//            public void run() {
        LogUtil.d(this@ScannerImplJB.toString(), DBG)
        mBluetoothAdapter.startLeScan(serviceUuids, this@ScannerImplJB)
        //        });
    }

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH])
    override fun stopScan() {
        try {
            if (mBluetoothAdapter.isEnabled()) {
                LogUtil.d(this.toString(), DBG)
                mBluetoothAdapter.stopLeScan(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val DBG = true
    }
}