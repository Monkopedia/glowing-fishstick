package com.ttlock.bl.sdk.api;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.RequiresPermission;

import com.ttlock.bl.sdk.callback.ScanKeypadCallback;
import com.ttlock.bl.sdk.device.WirelessKeypad;
import com.ttlock.bl.sdk.entity.ConnectParam;


/**
 * Created by TTLock on 2019/4/24.
 */

class WirelessKeypadSDKApi {

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

    public void prepareBTService(Context context){
        //TODO:
        GattCallbackHelper.getInstance().prepare(context);
    }

    public void stopBTService(){
        GattCallbackHelper.getInstance().clear();
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void startScan(ScanKeypadCallback callback){
        ScanManager.getInstance().startScan(callback);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void stopScan(){
        ScanManager.getInstance().stopScan();
    }

    public static void initKeyapd(WirelessKeypad keypad, ConnectParam param){
        CommandUtil.setLock(param, keypad);
    }
}
