package com.ttlock.bl.sdk.scanner

/**
 * Created by TTLock on 2016/5/12.
 */
class ScannerImplJB : ScannerCompat(), BluetoothAdapter.LeScanCallback {
    private val mBluetoothAdapter: BluetoothAdapter

    init {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    fun onLeScan(device: BluetoothDevice?, rssi: Int, scanRecord: ByteArray?) {
        // TODO:HEX
//        LogUtil.i("device=" + device + " " + device.getName() + " rssi=" + rssi + " scanRecord = " + Arrays.toString(scanRecord), DBG);
        mIScanCallback!!.onScan(ExtendedBluetoothDevice(device, rssi, scanRecord))
    }

    //    @Deprecated

    override fun startScanInternal(serviceUuids: Array<UUID?>?) {
        // TODO:check
//        ThreadPool.getThreadPool().execute(new Runnable() {
//            @Override
//
//            public void run() {
        LogUtil.d(this@ScannerImplJB.toString(), DBG)
        mBluetoothAdapter.startLeScan(serviceUuids, this@ScannerImplJB)
        //        });
    }

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
