package com.ttlock.bl.sdk.api;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import androidx.annotation.RequiresPermission;

import com.ttlock.bl.sdk.callback.InitKeypadCallback;
import com.ttlock.bl.sdk.callback.OperationType;
import com.ttlock.bl.sdk.callback.ScanKeypadCallback;
import com.ttlock.bl.sdk.device.WirelessKeypad;
import com.ttlock.bl.sdk.entity.ConnectParam;


/**
 * Created by TTLock on 2019/4/24.
 */

public class WirelessKeypadClient {

    public static final int REQUEST_ENABLE_BT = 1;

    private WirelessKeypadSDKApi mApi;

    private WirelessKeypadClient() {
        mApi = new WirelessKeypadSDKApi();
    }

    public static WirelessKeypadClient getDefault() {
        return InstanceHolder.mInstance;
    }

    private static class InstanceHolder{
        private final static WirelessKeypadClient mInstance = new WirelessKeypadClient();
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
    public void startScanKeyboard(ScanKeypadCallback callback) {
        mApi.startScan(callback);
    }

    public void stopBTService(){
        mApi.stopBTService();
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void stopScanKeyboard() {
        mApi.stopScan();
    }

    public void initializeKeypad(WirelessKeypad device, String lockmac, InitKeypadCallback callback) {
        if(!LockCallbackManager.getInstance().isDeviceBusy(OperationType.INIT_KEYPAD, callback)){
            ConnectParam connectParam = new ConnectParam();
            connectParam.setLockmac(lockmac);
//            connectParam.setFactoryDate(factoryDate);
//            if (TextUtils.isEmpty(connectParam.getFactoryDate())) {
//                connectParam.setFactoryDate("19700101");
//            }
            ConnectManager.getInstance().storeConnectParamForCallback(connectParam);
            ConnectManager.getInstance().connect2Device(device);
        }
    }

}
