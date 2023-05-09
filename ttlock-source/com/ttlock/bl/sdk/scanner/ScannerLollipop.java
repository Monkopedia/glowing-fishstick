package com.ttlock.bl.sdk.scanner;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.ParcelUuid;
import androidx.annotation.RequiresPermission;

import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.service.ThreadPool;
import com.ttlock.bl.sdk.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sciener on 2016/5/9.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)

public class ScannerLollipop extends ScannerCompat {

    private static final boolean DBG = true;

    private final BluetoothAdapter mBluetoothAdapter;
    private ScanCallbackImpl scanCallback = new ScanCallbackImpl();
    private BluetoothLeScanner scanner;

    public ScannerLollipop() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void startScanInternal(UUID[] serviceUuids) {
        scanner = mBluetoothAdapter.getBluetoothLeScanner();
        if (scanner == null) {
            LogUtil.w("BT le scanner not available", DBG);
        } else {
            final ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
            List<ScanFilter> filters = new ArrayList<>();
            filters.add(new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(UUID_SERVICE)).build());

            LogUtil.d("Thread:" + Thread.currentThread());
            LogUtil.d("scanCallback:" + scanCallback, DBG);
            if (scanAll){
                filters = null;
            }
            /**
             * java.lang.NullPointerException: Attempt to invoke virtual method 'void com.android.bluetooth.gatt.ScanManager.registerScanner(java.util.UUID)' on a null object reference
             at android.os.Parcel.readException(Parcel.java:2014)
             at android.os.Parcel.readException(Parcel.java:1954)
             at android.bluetooth.IBluetoothGatt$Stub$Proxy.registerScanner(IBluetoothGatt.java:957)
             at android.bluetooth.le.BluetoothLeScanner$BleScanCallbackWrapper.startRegistration(BluetoothLeScanner.java:428)
             at android.bluetooth.le.BluetoothLeScanner.startScan(BluetoothLeScanner.java:241)
             at android.bluetooth.le.BluetoothLeScanner.startScan(BluetoothLeScanner.java:133)
             at com.ttlock.bl.sdk.scanner.ScannerLollipop.startScanInternal(ScannerLollipop.java:57)
             at com.ttlock.bl.sdk.scanner.ScannerCompat$1.run(ScannerCompat.java:61)
             at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1162)
             at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:636)
             at java.lang.Thread.run(Thread.java:784)
             */
            try {
                scanner.startScan(filters, settings, scanCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void stopScan() {
        if(scanner != null && scanCallback != null && mBluetoothAdapter.isEnabled()) {
            scanner.stopScan(scanCallback);
            LogUtil.d("scanCallback:" + scanCallback, DBG);
        }
    }

    private class ScanCallbackImpl extends ScanCallback {
        @Override
        @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
//            LogUtil.d("callbackType=" + callbackType + " scanResult=" + result, DBG);
            mIScanCallback.onScan(new ExtendedBluetoothDevice(result));
        }

        /**
         * Callback when batch results are delivered.
         *
         * @param results List of scan results that are previously scanned.
         */
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            if (onScanFailedListener != null) {
                onScanFailedListener.onScanFailed(errorCode);
            }
//            restartBle();
            LogUtil.w("errorCode=" + errorCode, DBG);
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    private void restartBle() {
        if (mBluetoothAdapter != null) {
            // 一旦发生错误，除了重启蓝牙再没有其它解决办法

            if (!mBluetoothAdapter.disable()){
                return;
            }

            ThreadPool.getThreadPool().execute(new Runnable(){

                @Override
                @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
                public void run() {
                    while(true) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //要等待蓝牙彻底关闭，然后再打开，才能实现重启效果
                        if(mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                            mBluetoothAdapter.enable();
                            break;
                        }
                    }
                }

            });
        }
    }
}
