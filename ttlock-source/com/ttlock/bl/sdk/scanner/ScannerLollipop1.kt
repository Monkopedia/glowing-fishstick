package com.ttlock.bl.sdk.scanner

import com.ttlock.bl.sdk.service.ThreadPool
import java.lang.Exception
import java.util.ArrayList

/**
 * Created by Sciener on 2016/5/9.
 */

class ScannerLollipop : ScannerCompat() {
    private val mBluetoothAdapter: BluetoothAdapter?
    private val scanCallback: ScanCallbackImpl? = ScanCallbackImpl()
    private var scanner: BluetoothLeScanner? = null

    init {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    override fun startScanInternal(serviceUuids: Array<UUID?>?) {
        scanner = mBluetoothAdapter.getBluetoothLeScanner()
        if (scanner == null) {
            LogUtil.w("BT le scanner not available", DBG)
        } else {
            val settings: ScanSettings = Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()
            var filters: MutableList<ScanFilter?>? = ArrayList<ScanFilter?>()
            filters!!.add(
                Builder().setServiceUuid(ParcelUuid.fromString(ScannerCompat.Companion.UUID_SERVICE))
                    .build()
            )
            LogUtil.d("Thread:" + Thread.currentThread())
            LogUtil.d("scanCallback:$scanCallback", DBG)
            if (scanAll) {
                filters = null
            }
            /**
             * java.lang.NullPointerException: Attempt to invoke virtual method 'void com.android.bluetooth.gatt.ScanManager.registerScanner(java.util.UUID)' on a null object reference
             * at android.os.Parcel.readException(Parcel.java:2014)
             * at android.os.Parcel.readException(Parcel.java:1954)
             * at android.bluetooth.IBluetoothGatt$Stub$Proxy.registerScanner(IBluetoothGatt.java:957)
             * at android.bluetooth.le.BluetoothLeScanner$BleScanCallbackWrapper.startRegistration(BluetoothLeScanner.java:428)
             * at android.bluetooth.le.BluetoothLeScanner.startScan(BluetoothLeScanner.java:241)
             * at android.bluetooth.le.BluetoothLeScanner.startScan(BluetoothLeScanner.java:133)
             * at com.ttlock.bl.sdk.scanner.ScannerLollipop.startScanInternal(ScannerLollipop.java:57)
             * at com.ttlock.bl.sdk.scanner.ScannerCompat$1.run(ScannerCompat.java:61)
             * at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1162)
             * at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:636)
             * at java.lang.Thread.run(Thread.java:784)
             */
            try {
                scanner.startScan(filters, settings, scanCallback)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun stopScan() {
        if (scanner != null && scanCallback != null && mBluetoothAdapter.isEnabled()) {
            scanner.stopScan(scanCallback)
            LogUtil.d("scanCallback:$scanCallback", DBG)
        }
    }

    private inner class ScanCallbackImpl : ScanCallback() {

        fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            //            LogUtil.d("callbackType=" + callbackType + " scanResult=" + result, DBG);
            mIScanCallback!!.onScan(ExtendedBluetoothDevice(result))
        }

        /**
         * Callback when batch results are delivered.
         *
         * @param results List of scan results that are previously scanned.
         */
        fun onBatchScanResults(results: List<ScanResult?>?) {
            super.onBatchScanResults(results)
        }

        fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            if (onScanFailedListener != null) {
                onScanFailedListener.onScanFailed(errorCode)
            }
            //            restartBle();
            LogUtil.w("errorCode=$errorCode", DBG)
        }
    }

    private fun restartBle() {
        if (mBluetoothAdapter != null) {
            // 一旦发生错误，除了重启蓝牙再没有其它解决办法
            if (!mBluetoothAdapter.disable()) {
                return
            }
            ThreadPool.getThreadPool().execute {
                while (true) {
                    try {
                        Thread.sleep(500)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    // 要等待蓝牙彻底关闭，然后再打开，才能实现重启效果
                    if (mBluetoothAdapter.getState() === BluetoothAdapter.STATE_OFF) {
                        mBluetoothAdapter.enable()
                        break
                    }
                }
            }
        }
    }

    companion object {
        private const val DBG = true
    }
}
