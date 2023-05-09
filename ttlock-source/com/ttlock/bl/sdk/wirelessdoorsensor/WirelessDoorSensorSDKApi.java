package com.ttlock.bl.sdk.wirelessdoorsensor;

import android.Manifest;
import android.content.Context;

import androidx.annotation.RequiresPermission;

import com.ttlock.bl.sdk.base.BaseSDKApi;
import com.ttlock.bl.sdk.base.BaseScanManager;
import com.ttlock.bl.sdk.constant.BleConstant;
import com.ttlock.bl.sdk.device.WirelessDoorSensor;
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.ScanWirelessDoorSensorCallback;
import com.ttlock.bl.sdk.wirelessdoorsensor.command.CommandUtil;


/**
 * Created by TTLock on 2019/4/24.
 */

class WirelessDoorSensorSDKApi extends BaseSDKApi {


    @Override
    public void prepareBTService(Context context){
        //TODO:
//        GattCallbackHelper.getInstance().prepare(context);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void startScan(ScanWirelessDoorSensorCallback callback){
        BaseScanManager.getInstance().startScan(BleConstant.UUID_SERVICE_Door_Sensor, callback);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void stopScan(){
        BaseScanManager.getInstance().stopScan();
    }

    public void stopBTService(){
        GattCallbackHelper.getInstance().clear();
    }

    public void init(WirelessDoorSensor doorSensor) {
        CommandUtil.setLock(ConnectManager.getInstance().getConnectParam(), doorSensor);
    }

    public void checkAdmin(WirelessDoorSensor doorSensor) {
        CommandUtil.checkAdmin(doorSensor);
    }

//    public void setDoorNotClosedWarningTime(String doorSensorMac, int time) {
//        CommandUtil.doorNotClosedWarning(doorSensorMac, time);
//    }

}
