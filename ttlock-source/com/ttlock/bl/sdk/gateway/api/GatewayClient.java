package com.ttlock.bl.sdk.gateway.api;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.entity.IpSetting;
import com.ttlock.bl.sdk.gateway.callback.ConfigIpCallback;
import com.ttlock.bl.sdk.gateway.callback.ConnectCallback;
import com.ttlock.bl.sdk.gateway.callback.EnterDfuCallback;
import com.ttlock.bl.sdk.gateway.callback.GetNetworkMacCallback;
import com.ttlock.bl.sdk.gateway.callback.InitGatewayCallback;
import com.ttlock.bl.sdk.gateway.callback.ScanGatewayCallback;
import com.ttlock.bl.sdk.gateway.callback.ScanWiFiByGatewayCallback;
import com.ttlock.bl.sdk.gateway.model.ConfigureGatewayInfo;
import com.ttlock.bl.sdk.gateway.model.ConnectParam;
import com.ttlock.bl.sdk.gateway.model.GatewayError;
import com.ttlock.bl.sdk.gateway.model.OperationType;


/**
 * Created by TTLock on 2019/4/24.
 */

public class GatewayClient {

    public static final int REQUEST_ENABLE_BT = 1;

    private GatewaySDKApi mApi;

    private GatewayClient() {
        mApi = new GatewaySDKApi();
    }

    public static GatewayClient getDefault() {
        return InstanceHolder.mInstance;
    }

    private static class InstanceHolder{
        private final static GatewayClient mInstance = new GatewayClient();
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
    public void startScanGateway(ScanGatewayCallback callback) {//TODO:
//        GatewayCallbackManager.getInstance().setGatewayScanCallback(callback);
        mApi.startScanGateway(callback);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void stopScanGateway() {
        mApi.stopScanGateway();
//        GatewayCallbackManager.getInstance().clearScanCallback();
    }

    public void connectGateway(String mac, ConnectCallback connectCallback) {
        ConnectManager.getInstance().connect2Device(mac, connectCallback);
    }

    public void connectGateway(ExtendedBluetoothDevice extendedBluetoothDevice, ConnectCallback connectCallback) {
        ConnectManager.getInstance().connect2Device(extendedBluetoothDevice, connectCallback);
    }

    public void disconnectGateway() {
        ConnectManager.getInstance().disconnect();
    }

    public void initGateway(@NonNull ConfigureGatewayInfo configureInfo, InitGatewayCallback callback) {
        if (ConnectManager.getInstance().isDeviceConnected()) {
            GatewayCallbackManager.getInstance().isGatewayBusy(OperationType.INIT_GATEWAY, callback);
            mApi.initializeGateway(configureInfo, callback);
        } else {
            callback.onFail(GatewayError.UNCONNECTED);
        }
    }

    void enterDfu(String mac, EnterDfuCallback callback) {
        GatewayCallbackManager.getInstance().isGatewayBusy(OperationType.ENTER_DFU_MODE, callback);
        if (ConnectManager.getInstance().isDeviceConnected()) {
            mApi.enterDfu(mac);
        } else {
            ConnectParam connectParam = new ConnectParam();
            connectParam.setMac(mac);
            ConnectManager.getInstance().storeConnectParamForCallback(connectParam);
            ConnectManager.getInstance().connect2Device(mac);
        }
    }

    public void scanWiFiByGateway(String mac, ScanWiFiByGatewayCallback callback) {
        if (ConnectManager.getInstance().isDeviceConnected()) {
            GatewayCallbackManager.getInstance().isGatewayBusy(OperationType.SCAN_WIFI, callback);
            mApi.scanWiFiByGateway(mac);
        } else {
            callback.onFail(GatewayError.UNCONNECTED);
        }
    }

    public void configIp(String mac, IpSetting ipSetting, ConfigIpCallback callback) {
        if (ConnectManager.getInstance().isDeviceConnected()) {
            GatewayCallbackManager.getInstance().isGatewayBusy(OperationType.CONFIG_IP, callback);
            mApi.configIp(mac, ipSetting, callback);
        } else {
            callback.onFail(GatewayError.UNCONNECTED);
        }
    }

    /**
     * 获取网关的网络mac(只有锁处于连接状态下才可获取)
     * @param callback
     */
    public void getNetworkMac(GetNetworkMacCallback callback) {
        if (ConnectManager.getInstance().isDeviceConnected()) {
            callback.onGetNetworkMacSuccess(GattCallbackHelper.getInstance().getNetworkMac());
        } else {
            callback.onFail(GatewayError.UNCONNECTED);
        }
    }

}
