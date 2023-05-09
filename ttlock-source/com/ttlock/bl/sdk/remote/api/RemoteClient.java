package com.ttlock.bl.sdk.remote.api;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import androidx.annotation.RequiresPermission;


import com.ttlock.bl.sdk.api.EncryptionUtil;
import com.ttlock.bl.sdk.device.Remote;
import com.ttlock.bl.sdk.entity.LockData;
import com.ttlock.bl.sdk.remote.callback.GetRemoteSystemInfoCallback;
import com.ttlock.bl.sdk.remote.callback.InitRemoteCallback;
import com.ttlock.bl.sdk.remote.callback.ScanRemoteCallback;
import com.ttlock.bl.sdk.remote.model.ConnectParam;
import com.ttlock.bl.sdk.remote.model.RemoteError;
import com.ttlock.bl.sdk.remote.model.OperationType;

/**
 * Created by TTLock on 2021/1/25.
 */
public class RemoteClient {
    public static final int REQUEST_ENABLE_BT = 1;

    private WirelessKeyFobSDKApi mApi;

    private RemoteClient() {
        mApi = new WirelessKeyFobSDKApi();
    }

    public static RemoteClient getDefault() {
        return InstanceHolder.mInstance;
    }

    private static class InstanceHolder{
        private final static RemoteClient mInstance = new RemoteClient();
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public boolean isBLEEnabled(Context context) {
        return mApi.isBLEEnabled(context);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public void requestBleEnable(Activity activity) {
        mApi.requestBleEnable(activity);
    }

    public void prepareBTService(Context context){
        mApi.prepareBTService(context);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void startScan(ScanRemoteCallback callback) {
        RemoteCallbackManager.getInstance().setScanCallback(callback);
        mApi.startScan(callback);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void stopScan() {
        mApi.stopScanGateway();
        RemoteCallbackManager.getInstance().clearScanCallback();
    }

    public void initialize(Remote device, String lockData, InitRemoteCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if (device == null || lockParam == null) {
            callback.onFail(RemoteError.DATA_FORMAT_ERROR);
            return;
        }

        if(!RemoteCallbackManager.getInstance().isBusy(OperationType.INIT, callback)){
//            String address = device.getAddress();
//            if(ConnectManager.getInstance().isDeviceConnected(address)){
//                mApi.init(device);
//            } else {
                ConnectParam connectParam = new ConnectParam();
                connectParam.setLockmac(lockParam.lockMac);
                connectParam.setLockKey(lockParam.lockKey);
                connectParam.setAesKey(lockParam.aesKeyStr);
                ConnectManager.getInstance().storeConnectParamForCallback(connectParam);
                ConnectManager.getInstance().connect2Device(device);
//            }
        }
    }

    public void getRemoteSystemInfo(String remoteMac, GetRemoteSystemInfoCallback callback) {
        if (!RemoteCallbackManager.getInstance().isBusy(OperationType.GET_DEVICE_INFO, callback)) {
            BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(remoteMac);
            Remote remote = new Remote(bluetoothDevice);
            if (ConnectManager.getInstance().isDeviceConnected(remoteMac)) {
                callback.onGetRemoteSystemInfoSuccess(GattCallbackHelper.getInstance().getDeviceInfo());
            } else {
                ConnectParam connectParam = new ConnectParam();
                ConnectManager.getInstance().storeConnectParamForCallback(connectParam);
                ConnectManager.getInstance().connect2Device(remote);
            }
        }
    }

}
