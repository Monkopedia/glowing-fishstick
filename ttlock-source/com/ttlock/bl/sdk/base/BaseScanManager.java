package com.ttlock.bl.sdk.base;

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

import com.ttlock.bl.sdk.constant.BleConstant;
import com.ttlock.bl.sdk.device.TTDevice;
import com.ttlock.bl.sdk.device.WirelessDoorSensor;
import com.ttlock.bl.sdk.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TTLock on 2019/4/28.
 */

public class BaseScanManager {

    private static BaseScanManager instance = new BaseScanManager();

    private String UUID_SERVICE;

    /**
     * 扫描回调
     */
    private BaseScanCallback scanCallback;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner scanner;
    private ScanCallback leScanCallback;

    public static BaseScanManager getInstance() {
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
            leScanCallback = new ScanCallback() {

                @Override
                @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    scanCallback(result);
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                    LogUtil.w("errorCode:" + errorCode);
                }
            };
        }
    }

    private void scanCallback(ScanResult result) {
        TTDevice ttDevice = null;
        switch (UUID_SERVICE) {
            case BleConstant.UUID_SERVICE_Door_Sensor:
                ttDevice = new WirelessDoorSensor(result);
                break;
        }
        scanCallback.onScan(ttDevice);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void startScan(String UUID_SERVICE, BaseScanCallback scanCallback) {
        this.UUID_SERVICE = UUID_SERVICE;
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
    public void stopScan() {
        if (leScanCallback != null && scanner != null) {
            scanner.stopScan(leScanCallback);
        }
    }
}
