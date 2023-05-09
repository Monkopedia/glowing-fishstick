package com.ttlock.bl.sdk.wirelessdoorsensor;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.RequiresPermission;

import com.ttlock.bl.sdk.api.EncryptionUtil;
import com.ttlock.bl.sdk.base.BaseClient;
import com.ttlock.bl.sdk.device.WirelessDoorSensor;
import com.ttlock.bl.sdk.entity.LockData;
import com.ttlock.bl.sdk.util.LogUtil;
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.EnterDfuCallback;
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.InitDoorSensorCallback;
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.ScanWirelessDoorSensorCallback;
import com.ttlock.bl.sdk.wirelessdoorsensor.model.ConnectParam;
import com.ttlock.bl.sdk.wirelessdoorsensor.model.DoorSensorError;
import com.ttlock.bl.sdk.wirelessdoorsensor.model.OperationType;


/**
 * Created by TTLock on 2021/1/25.
 */
public class WirelessDoorSensorClient extends BaseClient<WirelessDoorSensorSDKApi> {

//    private WirelessDoorSensorSDKApi mApi;

    private WirelessDoorSensorClient() {
        mApi = new WirelessDoorSensorSDKApi();
    }

    public static WirelessDoorSensorClient getDefault() {
        return InstanceHolder.mInstance;
    }

    private static class InstanceHolder{
        private final static WirelessDoorSensorClient mInstance = new WirelessDoorSensorClient();
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void startScan(ScanWirelessDoorSensorCallback callback) {
//        Wirelessdoor.getInstance().setScanCallback(callback);
        mApi.startScan(callback);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void stopScan() {
        mApi.stopScan();
//        KeyFobCallbackManager.getInstance().clearScanCallback();
    }

    @Override //todo:临时测试用
    public void prepareBTService(Context context){
        LogUtil.d("prepare service");
//        mApi.prepareBTService(context);
        GattCallbackHelper.getInstance().prepare(context);
    }

    public void stopBTService(){
        mApi.stopBTService();
    }

    /**
     * 门磁初始化(将锁信息写入门磁内)
     * @param device
     * @param lockData
     * @param callback
     */
    public void initialize(WirelessDoorSensor device, String lockData, InitDoorSensorCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if (lockParam == null) {
            callback.onFail(DoorSensorError.DATA_FORMAT_ERROR);
            return;
        }
        if (!DoorSensorCallbackManager.getInstance().isBusy(OperationType.INIT, callback)) {
            ConnectParam connectParam = new ConnectParam();
            connectParam.setLockmac(lockParam.lockMac);
            connectParam.setLockKey(lockParam.lockKey);
            connectParam.setAesKey(lockParam.aesKeyStr);
            ConnectManager.getInstance().storeConnectParamForCallback(connectParam);
            ConnectManager.getInstance().connect2Device(device);
        }
    }

    public void enterDfu(String doorSensorMac, String lockKey, String aeskey, EnterDfuCallback callback) {
        if (TextUtils.isEmpty(doorSensorMac)) {
            callback.onFail(DoorSensorError.DATA_FORMAT_ERROR);
            return;
        }
        BluetoothDevice bluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(doorSensorMac);
        WirelessDoorSensor device = new WirelessDoorSensor(bluetoothDevice);
        enterDfu(device, lockKey, aeskey, callback);
    }

    public void enterDfu(WirelessDoorSensor device, String lockKey, String aeskey, EnterDfuCallback callback) {
        if (TextUtils.isEmpty(lockKey) || TextUtils.isEmpty(aeskey)) {
            callback.onFail(DoorSensorError.DATA_FORMAT_ERROR);
            return;
        }

        if(!DoorSensorCallbackManager.getInstance().isBusy(OperationType.ENTER_DFU, callback)){
            LogUtil.d("进行连接");
            ConnectParam connectParam = new ConnectParam();
            connectParam.setLockKey(lockKey);
            connectParam.setAesKey(aeskey);
            ConnectManager.getInstance().storeConnectParamForCallback(connectParam);
            ConnectManager.getInstance().connect2Device(device);
        } else {
            LogUtil.d("lock is busy");
        }
    }

    /**
     * 设置门未关报警
     * @param doorSensorMac
     * @param time
     * @param callback
     */
//    public void setDoorNotClosedWarnningTime(String doorSensorMac, int time, SetDoorNotClosedWarningTimeCallback callback) {
//        if (TextUtils.isEmpty(doorSensorMac) || time < 0) {
//            callback.onFail(DoorSensorError.DATA_FORMAT_ERROR);
//            return;
//        }
//        if(!DoorSensorCallbackManager.getInstance().isBusy(OperationType.SET_DOOR_NOT_CLOSED_WARNING_TIME, callback)){
//            LogUtil.d("进行连接");
//            ConnectParam connectParam = new ConnectParam();
//            connectParam.setDoorSensorMac(doorSensorMac);
//            connectParam.setTime(time);
//            ConnectManager.getInstance().storeConnectParamForCallback(connectParam);
//            ConnectManager.getInstance().connect2Device(doorSensorMac);
//        } else {
//            LogUtil.d("lock is busy");
//        }
//
//    }

}
