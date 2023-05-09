package com.ttlock.bl.sdk.api;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.RequiresPermission;

/**
 * Created on  2019/6/14 0014 14:28
 *
 * @author theodore_hu
 */
 abstract class TTLockSdkApiBase {
    private static final int REQUEST_ENABLE_BT = 1;
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public boolean isBLEEnabled(Context context) {
        final BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter adapter = manager.getAdapter();
        return adapter != null && adapter.isEnabled();
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public void requestBleEnable(Activity activity) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    abstract void prepareBTService(Context context);
    abstract void stopBTService();
    abstract void startScan();
    abstract void stopScan();
}
