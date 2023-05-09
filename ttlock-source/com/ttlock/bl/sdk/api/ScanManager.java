package com.ttlock.bl.sdk.api;

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

import com.ttlock.bl.sdk.callback.ScanKeypadCallback;
import com.ttlock.bl.sdk.device.WirelessKeypad;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.util.LogUtil;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by TTLock on 2019/4/28.
 */

class ScanManager {

    protected static String UUID_SERVICE = "00001810-0000-1000-8000-00805f9b34fb";
    public static final int SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES = 5;
    private static ScanManager instance = new ScanManager();
    private ScanKeypadCallback scanCallback;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner scanner;
    private android.bluetooth.le.ScanCallback leScanCallback;

    public static ScanManager getInstance() {
        return instance;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void prepare() {
        if (mBluetoothAdapter == null)
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (scanner == null && mBluetoothAdapter != null) {
            scanner = mBluetoothAdapter.getBluetoothLeScanner();
        }
        if (leScanCallback == null) {//TODO:更换对象
            leScanCallback = new android.bluetooth.le.ScanCallback() {

                @Override
                @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    if(scanCallback != null){
                        scanCallback.onScanKeyboardSuccess(new WirelessKeypad(result));
                    }
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);

                    if(scanCallback == null){
                        return;
                    }

                    LockError error = LockError.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES;
                    switch (errorCode){
                        case ScanCallback.SCAN_FAILED_ALREADY_STARTED:
                            error = LockError.SCAN_FAILED_ALREADY_START;
                            break;
                        case ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                            error = LockError.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED;
                            break;
                        case ScanCallback.SCAN_FAILED_INTERNAL_ERROR:
                            error = LockError.SCAN_FAILED_INTERNAL_ERROR;
                            break;
                        case ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED:
                            error = LockError.SCAN_FAILED_FEATURE_UNSUPPORTED;
                            break;
                            default:
                                break;
                    }
                    scanCallback.onFail(error);
                }
            };
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void startScan(ScanKeypadCallback scanCallback) {
        prepare();
        this.scanCallback = scanCallback;
        if (scanner == null) {
            LogUtil.w("BT le scanner not available");
        } else {
            final ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
            List<ScanFilter> filters = new ArrayList<>();
            filters.add(new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(UUID_SERVICE)).build());
            scanner.startScan(filters, settings, leScanCallback);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void stopScan() {//TODO:
        if (leScanCallback != null && scanner != null){
            scanner.stopScan(leScanCallback);
            scanCallback = null;
        }

    }
}
