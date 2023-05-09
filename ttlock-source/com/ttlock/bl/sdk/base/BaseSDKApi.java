package com.ttlock.bl.sdk.base;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.RequiresPermission;

import com.ttlock.bl.sdk.constant.BleConstant;
import com.ttlock.bl.sdk.wirelessdoorsensor.GattCallbackHelper;

public class BaseSDKApi {

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
            activity.startActivityForResult(enableIntent, BleConstant.REQUEST_ENABLE_BT);
        }
    }

    public void prepareBTService(Context context){//todo:
        GattCallbackHelper.getInstance().prepare(context);
    }


}
