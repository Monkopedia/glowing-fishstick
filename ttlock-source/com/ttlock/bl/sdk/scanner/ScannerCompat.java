package com.ttlock.bl.sdk.scanner;

import android.os.Build;

import com.ttlock.bl.sdk.callback.OnScanFailedListener;
import com.ttlock.bl.sdk.service.ThreadPool;
import com.ttlock.bl.sdk.util.LogUtil;

import java.util.UUID;

/**
 *
 *
 */
public abstract class ScannerCompat {
    private static boolean DBG = true;
    private static ScannerCompat mInstance;

    protected static String UUID_SERVICE = "00001910-0000-1000-8000-00805f9b34fb";


    protected static UUID[] serviceUuids = {UUID.fromString(UUID_SERVICE)};

    protected IScanCallback mIScanCallback;
    protected OnScanFailedListener onScanFailedListener;

    protected boolean scanBongOnly;

    protected boolean scanAll;

    public static ScannerCompat getScanner() {
        if(mInstance != null)
            return mInstance;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return mInstance = new ScannerLollipop();
        return mInstance = new ScannerImplJB();
    }

//    public OnScanFailedListener getOnScanFailedListener() {
//        return onScanFailedListener;
//    }

    public void setOnScanFailedListener(OnScanFailedListener onScanFailedListener) {
        this.onScanFailedListener = onScanFailedListener;
    }

//    public boolean isScanAll() {
//        return scanAll;
//    }

    public void setScanAll(boolean scanAll) {
        this.scanAll = scanAll;
    }

    public void startScan(final IScanCallback scanCallback) {
        LogUtil.d("scanCallback:" + scanCallback, DBG);
        LogUtil.d(Thread.currentThread().toString(), DBG);
        mIScanCallback = scanCallback;
        startScanInternal(serviceUuids);
//        ThreadPool.getThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                mIScanCallback = scanCallback;
//                startScanInternal(serviceUuids);
//            }
//        });
    }

    public abstract void startScanInternal(final UUID[] serviceUuids);

    public abstract void stopScan();

    public void setScanBongOnly(boolean scanBongOnly) {
        this.scanBongOnly = scanBongOnly;
    }

}
