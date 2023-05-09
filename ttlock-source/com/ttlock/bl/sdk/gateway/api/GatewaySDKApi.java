package com.ttlock.bl.sdk.gateway.api;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import android.text.TextUtils;

import com.ttlock.bl.sdk.entity.IpSetting;
import com.ttlock.bl.sdk.gateway.callback.ConfigIpCallback;
import com.ttlock.bl.sdk.gateway.callback.InitGatewayCallback;
import com.ttlock.bl.sdk.gateway.callback.ScanGatewayCallback;
import com.ttlock.bl.sdk.gateway.command.Command;
import com.ttlock.bl.sdk.gateway.command.CommandUtil;
import com.ttlock.bl.sdk.gateway.model.ConfigureGatewayInfo;
import com.ttlock.bl.sdk.gateway.model.GatewayError;
import com.ttlock.bl.sdk.gateway.model.GatewayType;
import com.ttlock.bl.sdk.gateway.util.GatewayUtil;
import com.ttlock.bl.sdk.util.LogUtil;

import java.io.UnsupportedEncodingException;


/**
 * Created by TTLock on 2019/4/24.
 */

class GatewaySDKApi {

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
        GattCallbackHelper.getInstance().prepare(context);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void startScanGateway(ScanGatewayCallback callback){
        ScanManager.getInstance().startScan(callback);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void stopScanGateway(){
        ScanManager.getInstance().stopScan();
    }

    public void initializeGateway(@NonNull ConfigureGatewayInfo configureInfo, InitGatewayCallback initGatewayCallback) {
        GattCallbackHelper.getInstance().setConfigureInfo(configureInfo);
        if (configureInfo == null || TextUtils.isEmpty(configureInfo.ssid)) {
            LogUtil.d("configureInfo is null");
            if (initGatewayCallback != null) {
                initGatewayCallback.onFail(GatewayError.DATA_FORMAT_ERROR);
            }
        } else {
            LogUtil.d("plugversion:" + configureInfo.plugVersion);
            try {
                if (configureInfo.plugVersion == GatewayType.G2) {
                    CommandUtil.configureWifi(configureInfo);
                } else {//G3 G4网关不需要走配置WIFI
                    CommandUtil.configureServer(configureInfo);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                if (initGatewayCallback != null) {
                    initGatewayCallback.onFail(GatewayError.DATA_FORMAT_ERROR);
                }
            }
        }
    }

    public void enterDfu(String mac) {
        Command command = new Command(Command.COMM_ENTER_DFU);
        command.setMac(mac);
        command.setData("SCIENER".getBytes());
        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    }

    public void scanWiFiByGateway(String mac) {
        Command command = new Command(Command.COMM_GET_NEARBY_SSID);
        command.setMac(mac);
        command.setData("SCIENER".getBytes());
        GattCallbackHelper.getInstance().clearWifi();
        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    }

    public void configIp(String mac, IpSetting ipSetting, ConfigIpCallback configIpCallback) {
        if (configIpCallback == null) {
            return;
        }
        if (ipSetting == null) {
            LogUtil.d("ipSetting is null");
            if (configIpCallback != null) {
                configIpCallback.onFail(GatewayError.DATA_FORMAT_ERROR);
            }
        } else {
            Command command = new Command(Command.COMM_CONFIG_IP);
            command.setMac(mac);

            byte[] data = new byte[1 + 4 + 4 + 4 + 4 + 4];//类型、ip、子网掩码、默认网关、首选dns服务器、备选dns服务器
            data[0] = (byte) ipSetting.getType();
            byte[] bytes = GatewayUtil.convertIp2Bytes(ipSetting.getIpAddress());
            if (bytes == null) {
                configIpCallback.onFail(GatewayError.DATA_FORMAT_ERROR);
                return;
            } else {
                System.arraycopy(bytes, 0, data, 1, 4);
            }

            bytes = GatewayUtil.convertIp2Bytes(ipSetting.getSubnetMask());
            if (bytes == null) {
                configIpCallback.onFail(GatewayError.DATA_FORMAT_ERROR);
                return;
            } else {
                System.arraycopy(bytes, 0, data, 5, 4);
            }

            bytes = GatewayUtil.convertIp2Bytes(ipSetting.getRouter());
            if (bytes == null) {
                configIpCallback.onFail(GatewayError.DATA_FORMAT_ERROR);
                return;
            } else {
                System.arraycopy(bytes, 0, data, 9, 4);
            }

            bytes = GatewayUtil.convertIp2Bytes(ipSetting.getPreferredDns());
            if (bytes == null) {
                configIpCallback.onFail(GatewayError.DATA_FORMAT_ERROR);
                return;
            } else {
                System.arraycopy(bytes, 0, data, 13, 4);
            }

            bytes = GatewayUtil.convertIp2Bytes(ipSetting.getAlternateDns());
            if (bytes == null) {
                configIpCallback.onFail(GatewayError.DATA_FORMAT_ERROR);
                return;
            } else {
                System.arraycopy(bytes, 0, data, 17, 4);
            }

            command.setData(data);
            GattCallbackHelper.getInstance().sendCommand(command.buildCommand());

        }
    }

}
