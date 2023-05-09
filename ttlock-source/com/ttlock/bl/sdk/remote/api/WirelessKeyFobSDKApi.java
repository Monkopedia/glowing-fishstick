package com.ttlock.bl.sdk.remote.api;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.RequiresPermission;


import com.ttlock.bl.sdk.device.Remote;
import com.ttlock.bl.sdk.remote.callback.ScanRemoteCallback;
import com.ttlock.bl.sdk.remote.command.CommandUtil;


/**
 * Created by TTLock on 2019/4/24.
 */

class WirelessKeyFobSDKApi {

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

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void startScan(ScanRemoteCallback callback){
        ScanManager.getInstance().startScan(callback);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void stopScanGateway(){
        ScanManager.getInstance().stopScan();
    }

    public void init(Remote keyFob) {
        CommandUtil.setLock(ConnectManager.getInstance().getConnectParam(), keyFob);
    }
}
