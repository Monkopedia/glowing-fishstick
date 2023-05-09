package com.ttlock.bl.sdk.gateway.api;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.ParcelUuid;
import androidx.annotation.RequiresPermission;

import com.ttlock.bl.sdk.gateway.callback.ScanGatewayCallback;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TTLock on 2019/4/28.
 */

class ScanManager {

    protected static String UUID_SERVICE = "00001911-0000-1000-8000-00805f9b34fb";
    private static ScanManager instance = new ScanManager();
    private ScanGatewayCallback scanGatewayCallback;
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
                    scanGatewayCallback.onScanGatewaySuccess(new ExtendedBluetoothDevice(result));
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                    scanGatewayCallback.onScanFailed(errorCode);
                }
            };
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH})
    public void startScan(ScanGatewayCallback scanGatewayCallback) {
        prepare();
        this.scanGatewayCallback = scanGatewayCallback;
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
        if (leScanCallback != null && scanner != null && mBluetoothAdapter != null && mBluetoothAdapter.isEnabled())
            scanner.stopScan(leScanCallback);
    }
}