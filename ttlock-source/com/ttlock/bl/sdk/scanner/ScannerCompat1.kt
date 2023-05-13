package com.ttlock.bl.sdk.scanner

import android.os.Build

/**
 *
 *
 */
abstract class ScannerCompat {
    protected var mIScanCallback: IScanCallback? = null
    protected var onScanFailedListener: OnScanFailedListener? = null
    protected var scanBongOnly = false
    protected var scanAll = false

    //    public OnScanFailedListener getOnScanFailedListener() {
    //        return onScanFailedListener;
    //    }
    fun setOnScanFailedListener(onScanFailedListener: OnScanFailedListener?) {
        this.onScanFailedListener = onScanFailedListener
    }

    //    public boolean isScanAll() {
    //        return scanAll;
    //    }
    fun setScanAll(scanAll: Boolean) {
        this.scanAll = scanAll
    }

    fun startScan(scanCallback: IScanCallback) {
        LogUtil.d("scanCallback:$scanCallback", DBG)
        LogUtil.d(Thread.currentThread().toString(), DBG)
        mIScanCallback = scanCallback
        startScanInternal(serviceUuids)
        //        ThreadPool.getThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                mIScanCallback = scanCallback;
//                startScanInternal(serviceUuids);
//            }
//        });
    }

    abstract fun startScanInternal(serviceUuids: Array<UUID?>?)
    abstract fun stopScan()
    fun setScanBongOnly(scanBongOnly: Boolean) {
        this.scanBongOnly = scanBongOnly
    }

    companion object {
        private const val DBG = true
        private var mInstance: ScannerCompat? = null
        protected var UUID_SERVICE = "00001910-0000-1000-8000-00805f9b34fb"
        protected var serviceUuids: Array<UUID> = arrayOf<UUID>(UUID.fromString(UUID_SERVICE))
        fun getScanner(): ScannerCompat? {
            if (mInstance != null) return mInstance
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ScannerLollipop().also {
                mInstance = it
            } else ScannerImplJB().also { mInstance = it }
        }
    }
}
