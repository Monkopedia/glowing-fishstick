package com.ttlock.bl.sdk.scanner;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.RequiresPermission;

import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.util.LogUtil;

import java.util.UUID;


/**
 * Created by TTLock on 2016/5/12.
 */
public class ScannerImplJB extends ScannerCompat implements BluetoothAdapter.LeScanCallback {

    private static boolean DBG = true;

    private final BluetoothAdapter mBluetoothAdapter;


    public ScannerImplJB() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        //TODO:HEX
//        LogUtil.i("device=" + device + " " + device.getName() + " rssi=" + rssi + " scanRecord = " + Arrays.toString(scanRecord), DBG);
        mIScanCallback.onScan(new ExtendedBluetoothDevice(device, rssi, scanRecord));
    }

    @Override
//    @Deprecated
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void startScanInternal(final UUID[] serviceUuids) {
        //TODO:check
//        ThreadPool.getThreadPool().execute(new Runnable() {
//            @Override
//            @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
//            public void run() {
                LogUtil.d(ScannerImplJB.this.toString(), DBG);
               mBluetoothAdapter.startLeScan(serviceUuids, ScannerImplJB.this);
//        });
    }

    @Override
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void stopScan() {
        try {
            if(mBluetoothAdapter.isEnabled()) {
                LogUtil.d(this.toString(), DBG);
                mBluetoothAdapter.stopLeScan(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
