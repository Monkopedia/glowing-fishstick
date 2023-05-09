package com.ttlock.bl.sdk.api;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import androidx.annotation.RequiresPermission;
import android.text.TextUtils;

import com.ttlock.bl.sdk.callback.ActivateLiftFloorsCallback;
import com.ttlock.bl.sdk.callback.AddDoorSensorCallback;
import com.ttlock.bl.sdk.callback.AddFingerprintCallback;
import com.ttlock.bl.sdk.callback.AddICCardCallback;
import com.ttlock.bl.sdk.callback.AddRemoteCallback;
import com.ttlock.bl.sdk.callback.ClearAllFingerprintCallback;
import com.ttlock.bl.sdk.callback.ClearAllICCardCallback;
import com.ttlock.bl.sdk.callback.ClearRemoteCallback;
import com.ttlock.bl.sdk.callback.ClearPassageModeCallback;
import com.ttlock.bl.sdk.callback.ConfigServerCallback;
import com.ttlock.bl.sdk.callback.ConfigIpCallback;
import com.ttlock.bl.sdk.callback.ConfigWifiCallback;
import com.ttlock.bl.sdk.callback.ConnectLockCallback;
import com.ttlock.bl.sdk.callback.ControlLockCallback;
import com.ttlock.bl.sdk.callback.CreateCustomPasscodeCallback;
import com.ttlock.bl.sdk.callback.DeleteDoorSensorCallback;
import com.ttlock.bl.sdk.callback.DeleteFingerprintCallback;
import com.ttlock.bl.sdk.callback.DeleteICCardCallback;
import com.ttlock.bl.sdk.callback.DeleteRemoteCallback;
import com.ttlock.bl.sdk.callback.DeletePassageModeCallback;
import com.ttlock.bl.sdk.callback.DeletePasscodeCallback;
import com.ttlock.bl.sdk.callback.EnterDfuModeCallback;
import com.ttlock.bl.sdk.callback.GetAccessoryBatteryLevelCallback;
import com.ttlock.bl.sdk.callback.GetAdminPasscodeCallback;
import com.ttlock.bl.sdk.callback.GetAllValidFingerprintCallback;
import com.ttlock.bl.sdk.callback.GetAllValidICCardCallback;
import com.ttlock.bl.sdk.callback.GetAllValidPasscodeCallback;
import com.ttlock.bl.sdk.callback.GetAutoLockingPeriodCallback;
import com.ttlock.bl.sdk.callback.GetBatteryLevelCallback;
import com.ttlock.bl.sdk.callback.GetLockConfigCallback;
import com.ttlock.bl.sdk.callback.GetLockFreezeStateCallback;
import com.ttlock.bl.sdk.callback.GetLockMuteModeStateCallback;
import com.ttlock.bl.sdk.callback.GetLockSoundWithSoundVolumeCallback;
import com.ttlock.bl.sdk.callback.GetLockStatusCallback;
import com.ttlock.bl.sdk.callback.GetLockSystemInfoCallback;
import com.ttlock.bl.sdk.callback.GetLockTimeCallback;
import com.ttlock.bl.sdk.callback.GetLockVersionCallback;
import com.ttlock.bl.sdk.callback.GetNBAwakeModesCallback;
import com.ttlock.bl.sdk.callback.GetNBAwakeTimesCallback;
import com.ttlock.bl.sdk.callback.GetOperationLogCallback;
import com.ttlock.bl.sdk.callback.GetPassageModeCallback;
import com.ttlock.bl.sdk.callback.GetPasscodeVerificationInfoCallback;
import com.ttlock.bl.sdk.callback.GetPasscodeVisibleStateCallback;
import com.ttlock.bl.sdk.callback.GetPowerSaverWorkModesCallback;
import com.ttlock.bl.sdk.callback.GetRemoteUnlockStateCallback;
import com.ttlock.bl.sdk.callback.GetSpecialValueCallback;
import com.ttlock.bl.sdk.callback.GetUnlockDirectionCallback;
import com.ttlock.bl.sdk.callback.GetWifiInfoCallback;
import com.ttlock.bl.sdk.callback.InitLockCallback;
import com.ttlock.bl.sdk.callback.LockCallback;
import com.ttlock.bl.sdk.callback.ModifyAdminPasscodeCallback;
import com.ttlock.bl.sdk.callback.ModifyFingerprintPeriodCallback;
import com.ttlock.bl.sdk.callback.ModifyICCardPeriodCallback;
import com.ttlock.bl.sdk.callback.ModifyRemoteValidityPeriodCallback;
import com.ttlock.bl.sdk.callback.ModifyPasscodeCallback;
import com.ttlock.bl.sdk.callback.OperationType;
import com.ttlock.bl.sdk.callback.RecoverLockDataCallback;
import com.ttlock.bl.sdk.callback.ReportLossCardCallback;
import com.ttlock.bl.sdk.callback.ResetKeyCallback;
import com.ttlock.bl.sdk.callback.ResetLockCallback;
import com.ttlock.bl.sdk.callback.ResetPasscodeCallback;
import com.ttlock.bl.sdk.callback.ScanLockCallback;
import com.ttlock.bl.sdk.callback.ScanWifiCallback;
import com.ttlock.bl.sdk.callback.SetAutoLockingPeriodCallback;
import com.ttlock.bl.sdk.callback.SetHotelCardSectorCallback;
import com.ttlock.bl.sdk.callback.SetHotelDataCallback;
import com.ttlock.bl.sdk.callback.SetLiftControlableFloorsCallback;
import com.ttlock.bl.sdk.callback.SetLiftWorkModeCallback;
import com.ttlock.bl.sdk.callback.SetLightTimeCallback;
import com.ttlock.bl.sdk.callback.SetLockConfigCallback;
import com.ttlock.bl.sdk.callback.SetLockFreezeStateCallback;
import com.ttlock.bl.sdk.callback.SetLockMuteModeCallback;
import com.ttlock.bl.sdk.callback.SetLockSoundWithSoundVolumeCallback;
import com.ttlock.bl.sdk.callback.SetLockTimeCallback;
import com.ttlock.bl.sdk.callback.SetNBAwakeModesCallback;
import com.ttlock.bl.sdk.callback.SetNBAwakeTimesCallback;
import com.ttlock.bl.sdk.callback.SetNBServerCallback;
import com.ttlock.bl.sdk.callback.SetPassageModeCallback;
import com.ttlock.bl.sdk.callback.SetPasscodeVisibleCallback;
import com.ttlock.bl.sdk.callback.SetPowerSaverControlableLockCallback;
import com.ttlock.bl.sdk.callback.SetPowerSaverWorkModeCallback;
import com.ttlock.bl.sdk.callback.SetRemoteUnlockSwitchCallback;
import com.ttlock.bl.sdk.callback.SetUnlockDirectionCallback;
import com.ttlock.bl.sdk.callback.WriteFingerprintDataCallback;
import com.ttlock.bl.sdk.constant.ControlAction;
import com.ttlock.bl.sdk.constant.FeatureValue;
import com.ttlock.bl.sdk.entity.AccessoryInfo;
import com.ttlock.bl.sdk.entity.ConnectParam;
import com.ttlock.bl.sdk.entity.HotelData;
import com.ttlock.bl.sdk.entity.HotelInfo;
import com.ttlock.bl.sdk.entity.IpSetting;
import com.ttlock.bl.sdk.entity.LockData;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.entity.NBAwakeConfig;
import com.ttlock.bl.sdk.entity.NBAwakeMode;
import com.ttlock.bl.sdk.entity.NBAwakeTime;
import com.ttlock.bl.sdk.entity.PassageModeConfig;
import com.ttlock.bl.sdk.entity.PowerSaverWorkMode;
import com.ttlock.bl.sdk.entity.SoundVolume;
import com.ttlock.bl.sdk.entity.TTLiftWorkMode;
import com.ttlock.bl.sdk.entity.TTLockConfigType;
import com.ttlock.bl.sdk.entity.UnlockDirection;
import com.ttlock.bl.sdk.entity.ValidityInfo;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.FeatureValueUtil;
import com.ttlock.bl.sdk.util.GsonUtil;
import com.ttlock.bl.sdk.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on  2019/4/11 0011 15:34
 *
 * @author theodre
 */
public final class TTLockClient {
    private TTLockSDKApi mApi;
    private int uid;

    private TTLockClient(){
        mApi = new TTLockSDKApi();
    }

    public static TTLockClient getDefault(){
        return InstanceHolder.mInstance;
    }


    /**
     *
     * Judging whether Bluetooth is available
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public boolean isBLEEnabled(Context context) {
        return mApi.isBLEEnabled(context);
    }

    /**
     *  Request to turn on Bluetooth
     * @param activity
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public void requestBleEnable(Activity activity) {
        mApi.requestBleEnable(activity);
    }

    private static class InstanceHolder{
        private final static TTLockClient mInstance = new TTLockClient();
    }

    /**
     * init the Bluetooth configuration
     * @param context
     */
    public void prepareBTService(Context context){
        mApi.prepareBTService(context);
    }

    /**
     * stop the service to release Bluetooth resource
     */
    public void stopBTService(){
        mApi.stopBTService();
    }

    /**
     * start scan BT lock
     * @param callback
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public void startScanLock(ScanLockCallback callback){
        LockCallbackManager.getInstance().setLockScanCallback(callback);
        mApi.startScan();
    }

    /**
     * stop the scan
     */
    public void stopScanLock(){
        mApi.stopScan();
        LockCallbackManager.getInstance().clearScanCallback();
    }

    /**
     * init the lock
     * @param device
     * @param callback
     */
    public void initLock(ExtendedBluetoothDevice device, InitLockCallback callback){
        if(!LockCallbackManager.getInstance().isDeviceBusy(OperationType.INIT_LOCK,callback)){
            String address = device.getAddress();
            if(ConnectManager.getInstance().isDeviceConnected(address)){
                mApi.initLock(device);
            } else {
                ConnectManager.getInstance().storeConnectParamForCallback(new ConnectParam());
                ConnectManager.getInstance().connect2Device(device);
            }
        }
    }

    public void connectLock(String lockData, ConnectLockCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.CONNECT_LOCK, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                callback.onConnectSuccess();
            } else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * reset the ekey, and the lockFlagPos will change
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void resetEkey(String lockData,String lockMac, ResetKeyCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        lockParam.lockFlagPos++;
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.RESET_KEY, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.resetEkey(lockParam);
            } else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     *  the lock will be reset to factory mode and if you want to use it,you should do initLock
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void resetLock(String lockData,String lockMac, ResetLockCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.RESET_LOCK, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.resetLock(lockParam);
            } else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * lock or unlock
     * @param controlAction (ControlAction.LOCK and ControlAction.UNLOCK)
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void controlLock(int controlAction, String lockData, String lockMac,ControlLockCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        boolean isRollingGate = false;
        if (controlAction == ControlAction.ROLLING_GATE_UP || controlAction == ControlAction.ROLLING_GATE_DOWN
                || controlAction == ControlAction.ROLLING_GATE_PAUSE || controlAction == ControlAction.ROLLING_GATE_LOCK) {
            isRollingGate = true;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.CONTROL_LOCK, callback, isRollingGate)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (lockParam.getUid() == 0 && uid != 0) {
                lockParam.setUid(uid);
                uid = 0;
            }
//            lockParam.lockMac = lockMac;
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setControlAction(controlAction);
                mApi.controlLock(controlAction,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setControlAction(controlAction);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * Get Audio Switch State
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void getMuteModeState(String lockData, String lockMac,GetLockMuteModeStateCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_MUTE_MODE_STATE, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);

            if(!isSupportThisOperation(lockParam)){
                return;
            }

            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getMuteModeState(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * Set Audio Switch State
     * @param enable    true - audio off, false audio on
     * @param lockData
     * @param lockMac
     * @param callback
     */
    @Deprecated
    public void setMuteMode(boolean enable, String lockData, String lockMac,SetLockMuteModeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_MUTE_MODE_STATE, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);

            if(!isSupportThisOperation(lockParam)){
                return;
            }

            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setLockModeEnable(enable);
                mApi.setMuteMode(enable,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setLockModeEnable(enable);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * Set remote unlock switch
     * @param enable    (true – on, false – off)
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void setRemoteUnlockSwitchState(boolean enable, String lockData,String lockMac, SetRemoteUnlockSwitchCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_REMOTE_UNLOCK_STATE, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);

            if(!isSupportThisOperation(lockParam)){
                return;
            }

            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setLockModeEnable(enable);
                mApi.setRemoteUnlockSwitchState(enable,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setLockModeEnable(enable);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * Get remote unlock switch state(true – on, false – off)
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void getRemoteUnlockSwitchState(String lockData, String lockMac,GetRemoteUnlockStateCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_REMOTE_UNLOCK_STATE, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);

            if(!isSupportThisOperation(lockParam)){
                return;
            }

            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getRemoteUnlockSwitchState(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     *  Set the lock time
     * @param timestamp
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void setLockTime(long timestamp, String lockData, String lockMac,SetLockTimeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_LOCK_TIME, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setTimestamp(timestamp);
                mApi.setLockTime(timestamp,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setTimestamp(timestamp);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * get the lock time
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void getLockTime(String lockData,String lockMac, GetLockTimeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_LOCK_TIME, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getLockTime(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     *  get the lock log
     * @param logType ALL - all the operation record from lock is initialized. NEW - only the new added operation record from last time you get log.
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void getOperationLog(int logType,String lockData, String lockMac,GetOperationLogCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_OPERATION_LOG, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setLogType(logType);
                mApi.getOperationLog(logType,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setLogType(logType);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     *  get lock battery
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void getBatteryLevel(String lockData,String lockMac, GetBatteryLevelCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_ELECTRIC_QUALITY, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getBatteryLevel(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getLockVersion(String lockMac,GetLockVersionCallback callback){
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_LOCK_VERSION, callback)){
            if (ConnectManager.getInstance().isDeviceConnected(lockMac)) {
                mApi.getLockVersion();
            }else {
                ConnectManager.getInstance().storeConnectParamForCallback(new ConnectParam());
                ConnectManager.getInstance().connect2Device(lockMac);
            }
        }
    }

    @Deprecated
    public void getSpecialValue(String lockData, String lockMac, GetSpecialValueCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_SPECIAL_VALUE, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getSpecialValue(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getLockSystemInfo(String lockData,String lockMac, GetLockSystemInfoCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_SYSTEM_INFO, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getLockSystemInfo(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getLockStatus(String lockData,String lockMac, GetLockStatusCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_LOCK_STATUS, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getLockStatus(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * set Automatic locking period
     * @param seconds
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void setAutomaticLockingPeriod(int seconds, String lockData,String lockMac,SetAutoLockingPeriodCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_AUTO_LOCK_PERIOD, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);

            if(!isSupportThisOperation(lockParam)){
                return;
            }

            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setAutoLockingPeriod(seconds);
                mApi.setAutomaticLockingPeriod(seconds,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setAutoLockingPeriod(seconds);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getAutomaticLockingPeriod(String lockData, GetAutoLockingPeriodCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_AUTO_LOCK_PERIOD, callback)) {
//            没有特征值也会有自动闭锁时间
//            if(!isSupportThisOperation(lockParam)){
//                return;
//            }

            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getAutomaticLockingPeriod(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * create custom passcode(Passcode range : 4 - 9 Digits in length)
     * @param passcode
     * @param startDate
     * @param endDate
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void createCustomPasscode(String passcode, long startDate, long endDate, String lockData,String lockMac, CreateCustomPasscodeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.CREATE_CUSTOM_PASSCODE, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);

            //TODO:需要增加无线键盘的判断
//            if(!isSupportThisOperation(lockParam)){
//                return;
//            }

            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectParam param = ConnectManager.getInstance().getConnectParamForCallback();
                param.setLockData(lockParam);
                param.setStartDate(startDate);
                param.setEndDate(endDate);
                param.setOriginalPasscode(passcode);
                mApi.createCustomPasscode(passcode,startDate,endDate,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setStartDate(startDate);
                param.setEndDate(endDate);
                param.setOriginalPasscode(passcode);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * modify passcode（if newPasscode is null or "" means you do not want to change original passcode. startDate and endDate values are both 0 means you do not want to change expired date,or both values should not be 0.）
     * @param originalCode
     * @param newCode
     * @param startDate
     * @param endDate
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void modifyPasscode(String originalCode, String newCode, long startDate, long endDate, String lockData, String lockMac, ModifyPasscodeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.MODIFY_PASSCODE, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);

            if(!isSupportThisOperation(lockParam)){
                return;
            }

            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
//                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectParam param = ConnectManager.getInstance().getConnectParamForCallback();
                param.setLockData(lockParam);
                param.setStartDate(startDate);
                param.setEndDate(endDate);
                param.setOriginalPasscode(originalCode);
                param.setNewPasscode(newCode);
                mApi.modifyPasscode(originalCode,newCode,startDate,endDate,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setStartDate(startDate);
                param.setEndDate(endDate);
                param.setOriginalPasscode(originalCode);
                param.setNewPasscode(newCode);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * delete passcode
     * @param passcode
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void deletePasscode(String passcode, String lockData, String lockMac, DeletePasscodeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.DELETE_PASSCODE, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
//                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectParam param = ConnectManager.getInstance().getConnectParamForCallback();
                param.setLockData(lockParam);
                param.setOriginalPasscode(passcode);
                mApi.deletePasscode(passcode,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setOriginalPasscode(passcode);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * reset passcode(all passcodes will be invalid)
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void resetPasscode(String lockData, String lockMac, ResetPasscodeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.RESET_PASSCODE, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.resetPasscode(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * get all valid passcodes in lock
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void getAllValidPasscodes(String lockData, String lockMac, GetAllValidPasscodeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_ALL_VALID_PASSCODES, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getAllValidPasscodes(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getPasscodeVerificationParams(String lockData, GetPasscodeVerificationInfoCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_PASSCODE_INFO, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getPasscodeVerificationParams(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getAdminPasscode(String lockData, String lockMac, GetAdminPasscodeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_ADMIN_PASSCODE, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);

            if(!isSupportThisOperation(lockParam)){
                return;
            }

            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getAdminPasscode(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * modify admin passcode
     * @param newPasscode
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void modifyAdminPasscode(String newPasscode, String lockData, String lockMac, ModifyAdminPasscodeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.MODIFY_ADMIN_PASSCODE, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
//                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectParam param = ConnectManager.getInstance().getConnectParamForCallback();
                param.setLockData(lockParam);
                param.setNewPasscode(newPasscode);
                mApi.modifyAdminPasscode(newPasscode,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setNewPasscode(newPasscode);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * (startDate and endDate set 0 means the card will be valid for ever. onEnterAddMode:the lock is ready to add a card.)
     * @param startDate
     * @param endDate
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void addICCard(long startDate,long endDate,String lockData,String lockMac,AddICCardCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.ADD_IC_CARD, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);

            if(!isSupportThisOperation(lockParam)){
                return;
            }

            ValidityInfo validityInfo = new ValidityInfo();
            validityInfo.setModeType(ValidityInfo.TIMED);
            validityInfo.setStartDate(startDate);
            validityInfo.setEndDate(endDate);

            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
//                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectParam param = ConnectManager.getInstance().getConnectParamForCallback();
                param.setLockData(lockParam);

                param.setValidityInfo(validityInfo);
                mApi.addICCard(validityInfo, lockParam);
//                mApi.addICCard(startDate,endDate,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);

                param.setValidityInfo(validityInfo);
//                param.setStartDate(startDate);
//                param.setEndDate(endDate);

                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void addICCard(ValidityInfo validityInfo, String lockData, AddICCardCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || validityInfo == null || !validityInfo.isValidData()){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(validityInfo.getModeType() == ValidityInfo.CYCLIC ? OperationType.ADD_CYCLIC_IC_CARD : OperationType.ADD_IC_CARD, callback)) {

            if(!isSupportThisOperation(lockParam)){
                return;
            }

            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setValidityInfo(validityInfo);
                mApi.addICCard(validityInfo, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setValidityInfo(validityInfo);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * modify CARD Card Period
     * @param startDate
     * @param endDate
     * @param cardNum
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void modifyICCardValidityPeriod(long startDate, long endDate, String cardNum, String lockData, String lockMac, ModifyICCardPeriodCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || TextUtils.isEmpty(cardNum)){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        ValidityInfo validityInfo = new ValidityInfo();
        validityInfo.setModeType(ValidityInfo.TIMED);
        validityInfo.setStartDate(startDate);
        validityInfo.setEndDate(endDate);

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.MODIFY_IC_CARD_PERIOD, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
//                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectParam param = ConnectManager.getInstance().getConnectParamForCallback();
                param.setLockData(lockParam);
                param.setValidityInfo(validityInfo);
                param.setAttachmentNum(Long.valueOf(cardNum));
                mApi.modifyICCardValidityPeriod(validityInfo, Long.valueOf(cardNum),lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setValidityInfo(validityInfo);
                param.setAttachmentNum(Long.valueOf(cardNum));
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void modifyICCardValidityPeriod(ValidityInfo validityInfo, String cardNum, String lockData, ModifyICCardPeriodCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || TextUtils.isEmpty(cardNum)){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.MODIFY_IC_CARD_PERIOD, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
//                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectParam param = ConnectManager.getInstance().getConnectParamForCallback();
                param.setLockData(lockParam);
                param.setValidityInfo(validityInfo);
                param.setAttachmentNum(Long.valueOf(cardNum));
                mApi.modifyICCardValidityPeriod(validityInfo, Long.valueOf(cardNum), lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setValidityInfo(validityInfo);
                param.setAttachmentNum(Long.valueOf(cardNum));
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * get all valid CARD Cards
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void getAllValidICCards(String lockData, String lockMac, GetAllValidICCardCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_ALL_IC_CARDS, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getAllValidICCards(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * delete CARD Card
     * @param cardNum
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void deleteICCard(String cardNum, String lockData, String lockMac, DeleteICCardCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || TextUtils.isEmpty(cardNum)){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.DELETE_IC_CARD, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setAttachmentNum(Long.valueOf(cardNum));
                mApi.deleteICCard(Long.valueOf(cardNum),lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setAttachmentNum(Long.valueOf(cardNum));
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void reportLossCard(String cardNum, String lockData, ReportLossCardCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || TextUtils.isEmpty(cardNum)){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.LOSS_CARD, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setAttachmentNum(Long.valueOf(cardNum));
                mApi.lossICCard(Long.valueOf(cardNum),lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setAttachmentNum(Long.valueOf(cardNum));
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * clear ic cards
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void clearAllICCard(String lockData, String lockMac, ClearAllICCardCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.CLEAR_ALL_IC_CARD, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.clearAllICCard(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * add Fingerprint
     * @param startDate
     * @param endDate
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void addFingerprint(long startDate,long endDate,String lockData,String lockMac,AddFingerprintCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        ValidityInfo validityInfo = new ValidityInfo();
        validityInfo.setModeType(ValidityInfo.TIMED);
        validityInfo.setStartDate(startDate);
        validityInfo.setEndDate(endDate);

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.ADD_FINGERPRINT, callback)) {

            if(!isSupportThisOperation(lockParam)){
                return;
            }

            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setValidityInfo(validityInfo);
                mApi.addFingerprint(validityInfo, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setValidityInfo(validityInfo);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void addFingerprint(ValidityInfo validityInfo,String lockData, AddFingerprintCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || validityInfo == null || !validityInfo.isValidData()){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(validityInfo.getModeType() == ValidityInfo.CYCLIC ? OperationType.ADD_CYCLIC_FINGERPRINT : OperationType.ADD_FINGERPRINT, callback)) {

            if(!isSupportThisOperation(lockParam)){
                return;
            }

            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setValidityInfo(validityInfo);
                mApi.addFingerprint(validityInfo, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setValidityInfo(validityInfo);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * get all valid fingerprints
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void getAllValidFingerprints(String lockData, String lockMac, GetAllValidFingerprintCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_ALL_FINGERPRINTS, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getAllValidFingerprints(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * delete fingerprint
     * @param fingerprintNum
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void deleteFingerprint(String fingerprintNum, String lockData, String lockMac, DeleteFingerprintCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || TextUtils.isEmpty(fingerprintNum)){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.DELETE_FINGERPRINT, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setAttachmentNum(Long.valueOf(fingerprintNum));
                mApi.deleteFingerprint(Long.valueOf(fingerprintNum),lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setAttachmentNum(Long.valueOf(fingerprintNum));
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * clear all fingerprints
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void clearAllFingerprints(String lockData, String lockMac, ClearAllFingerprintCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.CLEAR_ALL_FINGERPRINTS, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.clearAllFingerprints(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * modify fingerprint period
     * @param startDate
     * @param endDate
     * @param fingerprintNum
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void modifyFingerprintValidityPeriod(long startDate, long endDate, String fingerprintNum, String lockData, String lockMac, ModifyFingerprintPeriodCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || TextUtils.isEmpty(fingerprintNum)){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        ValidityInfo validityInfo = new ValidityInfo();
        validityInfo.setModeType(ValidityInfo.TIMED);
        validityInfo.setStartDate(startDate);
        validityInfo.setEndDate(endDate);

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.MODIFY_FINGEPRINT_PERIOD, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setValidityInfo(validityInfo);
                ConnectManager.getInstance().getConnectParamForCallback().setAttachmentNum(Long.valueOf(fingerprintNum));
                mApi.modifyFingerprintValidityPeriod(validityInfo,Long.valueOf(fingerprintNum),lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setValidityInfo(validityInfo);
                param.setAttachmentNum(Long.valueOf(fingerprintNum));
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void modifyFingerprintValidityPeriod(ValidityInfo validityInfo, String fingerprintNum, String lockData, ModifyFingerprintPeriodCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || TextUtils.isEmpty(fingerprintNum)){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.MODIFY_FINGEPRINT_PERIOD, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setValidityInfo(validityInfo);
                ConnectManager.getInstance().getConnectParamForCallback().setAttachmentNum(Long.valueOf(fingerprintNum));
                mApi.modifyFingerprintValidityPeriod(validityInfo,Long.valueOf(fingerprintNum),lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setValidityInfo(validityInfo);
                param.setAttachmentNum(Long.valueOf(fingerprintNum));
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void enterDfuMode(String lockData, String lockMac, EnterDfuModeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.ENTER_DFU_MODE, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.enterDfuMode(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }


    public void setLockConfig(TTLockConfigType ttLockConfigType, boolean switchOn, String lockData, SetLockConfigCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_LOCK_CONFIG, callback)) {
            //todo:支持判断
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setTtLockConfigType(ttLockConfigType);
                ConnectManager.getInstance().getConnectParamForCallback().setLockModeEnable(switchOn);
                switch (ttLockConfigType) {
                    case LOCK_SOUND:
                        mApi.setMuteMode(!switchOn,lockParam);
                        break;
                    case PASSCODE_VISIBLE:
                        mApi.setPasscodeVisibleSwitchState(switchOn,lockParam);
                        break;
                    case LOCK_FREEZE:
                        mApi.setLockFreezeState(switchOn, lockParam);
                        break;
                    case PRIVACY_LOCK:
                    case TAMPER_ALERT:
                    case RESET_BUTTON:
                    case PASSAGE_MODE_AUTO_UNLOCK_SETTING:
                    case WIFI_LOCK_POWER_SAVING_MODE:
                        mApi.setLockConfig(ttLockConfigType, switchOn, lockParam);
                        break;
                }
            } else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setTtLockConfigType(ttLockConfigType);
                param.setLockModeEnable(switchOn);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getLockConfig(TTLockConfigType ttLockConfigType, String lockData, GetLockConfigCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || ttLockConfigType == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_LOCK_CONFIG, callback)) {
            //todo:支持判断
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setTtLockConfigType(ttLockConfigType);
                switch (ttLockConfigType) {
                    case LOCK_SOUND:
                        mApi.getMuteModeState(lockParam);
                        break;
                    case PASSCODE_VISIBLE:
                        mApi.getPasscodeVisibleSwitchState(lockParam);
                        break;
                    case LOCK_FREEZE:
                        mApi.getLockFreezeState(lockParam);
                        break;
                    case PRIVACY_LOCK:
                    case TAMPER_ALERT:
                    case RESET_BUTTON:
                    case WIFI_LOCK_POWER_SAVING_MODE:
                        mApi.getLockConfig(ttLockConfigType, lockParam);
                        break;
                }
            } else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setTtLockConfigType(ttLockConfigType);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

        /**
         * **********************未测试***开始*********************
         */

    public void writeFingerprintData(String fingerprintData, int tempFingerprintNumber, long startDate, long endDate, String lockData, String lockMac,WriteFingerprintDataCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.WRITE_FINGERPRINT_DATA, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
//                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectParam param = ConnectManager.getInstance().getConnectParamForCallback();
                param.setLockData(lockParam);
                param.setStartDate(startDate);
                param.setEndDate(endDate);
                param.setDataJsonStr(fingerprintData);
                param.setAttachmentNum(tempFingerprintNumber);
                mApi.writeFingerprintData(fingerprintData,tempFingerprintNumber,startDate,endDate,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setStartDate(startDate);
                param.setEndDate(endDate);
                param.setDataJsonStr(fingerprintData);
                param.setAttachmentNum(tempFingerprintNumber);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void recoverLockData(String dataJson, int dataType, String lockData,String lockMac, RecoverLockDataCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || TextUtils.isEmpty(dataJson)){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.RECOVERY_DATA, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setRecoveryDataStr(dataJson);
                ConnectManager.getInstance().getConnectParamForCallback().setRecoveryDataType(dataType);
                mApi.recoverLockData(dataJson,dataType,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setRecoveryDataStr(dataJson);
                param.setRecoveryDataType(dataType);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getPasscodeVisibleSwitchState(String lockData, String lockMac,GetPasscodeVisibleStateCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_PASSCODE_VISIBLE_STATE, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getRemoteUnlockSwitchState(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    @Deprecated
    public void setPasscodeVisibleSwitchState(boolean enable, String lockData, String lockMac,SetPasscodeVisibleCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_PASSCODE_VISIBLE_STATE, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setLockModeEnable(enable);
                mApi.setPasscodeVisibleSwitchState(enable,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setLockModeEnable(enable);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    private final static String SPECIAL_VALUE_KEY = "specialValue";
    public void setNBServerInfo(short portNumber, String serverAddress, String lockData, SetNBServerCallback callback){
        LockData lockParam = null;
        try {
            if(!TextUtils.isEmpty(lockData) && lockData.contains(SPECIAL_VALUE_KEY)){
                lockParam  = GsonUtil.toObject(lockData, LockData.class);
            }else {
                lockParam = EncryptionUtil.parseLockData(lockData);
                if(lockParam == null){
                    callback.onFail(LockError.DATA_FORMAT_ERROR);
                    return;
                }
//                lockParam = GsonUtil.toObject(lockData, LockData.class);
            }
        }catch (IllegalStateException e){

        }

        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_NB_SERVER, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setServerAddress(serverAddress);
                ConnectManager.getInstance().getConnectParamForCallback().setServerPort(portNumber);
                mApi.setNBServerInfo(portNumber,serverAddress,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setServerAddress(serverAddress);
                param.setServerPort(portNumber);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }
    /****************未测试 结束****************************************************************************************/

    /**
     * get passage mode
     * @param lockData
     * @param lockMac
     * @param callback
     */
    public void getPassageMode(String lockData,String lockMac, GetPassageModeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_PASSAGE_MODE, callback)) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getPassageMode(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void setPassageMode(PassageModeConfig modeData, String lockData,String lockMac, SetPassageModeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || modeData == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_PASSAGE_MODE, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setPassageModeConfig(modeData);
                mApi.setPassageMode(modeData,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setPassageModeConfig(modeData);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void deletePassageMode(PassageModeConfig modeData, String lockData, String lockMac,DeletePassageModeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || modeData == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.DELETE_PASSAGE_MODE, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setPassageModeConfig(modeData);
                mApi.deletePassageMode(modeData,lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setPassageModeConfig(modeData);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void clearPassageMode(String lockData, String lockMac,ClearPassageModeCallback callback){
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.CLEAR_PASSAGE_MODE, callback)) {
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.clearPassageMode(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * @param isOn                          true - freeze，false - unfreeze
     */
    @Deprecated
    public void setLockFreezeState(boolean isOn, String lockData, SetLockFreezeStateCallback callback) {

        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_LOCK_FREEZE_STATE, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setLockModeEnable(isOn);
                mApi.setLockFreezeState(isOn, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setLockModeEnable(isOn);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getLockFreezeState(String lockData, GetLockFreezeStateCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_LOCK_FREEZE_STATE, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getLockFreezeState(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void setLightTime(int seconds, String lockData, SetLightTimeCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_LIGHT_TIME, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setSeconds(seconds);
                mApi.setLightTime(seconds, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setSeconds(seconds);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getLightTime(String lockData, GetLockTimeCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_LIGHT_TIME, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getLightTime(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * 设置写入扇区(跟锁内保持一致)
     * @param sectorStr  从1开始计数  数据格式 "1,2,3" 表示写入扇区1、2、3
     * @param lockData
     * @param callback
     */
    public void setHotelCardSector(String sectorStr, String lockData, SetHotelCardSectorCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_HOTEL_CARD_SECTION, callback)) {
            HotelData hotelData = new HotelData();
            hotelData.sector = DigitUtil.calSectorValue(sectorStr);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setHotelData(hotelData);
                mApi.setHotelCardSector(hotelData, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setHotelData(hotelData);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * 设置的保留扇区 跟锁内是相反的 扇区从0开始计算
     * @param sectors
     * @param lockData
     * @param callback
     */
    @Deprecated
    public void setHotelCardSector(ArrayList<Integer> sectors, String lockData, SetHotelCardSectorCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_HOTEL_CARD_SECTION, callback)) {
            HotelData hotelData = new HotelData();
            hotelData.sector = DigitUtil.calSectorValue(sectors);
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setHotelData(hotelData);
                mApi.setHotelCardSector(hotelData, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setHotelData(hotelData);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }


    /**
     * 目前只用来修改icKey, aesKey, hotelNumber, buildingNumber, floorNumber
     * @param hotelData  hotelData.setHotelInfo(hotelInfo);hotelInfo从服务端获取
     *                   hotelData.setBuildingNumber(buildingNumber);
     *                   hotelData.setFloorNumber(floorNumber);
     * @param lockData
     * @param callback
     */
    public void setHotelData(HotelData hotelData, String lockData, SetHotelDataCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || hotelData == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_HOTEL_DATA, callback)) {
            //TODO:
//            if(!isSupportThisOperation(lockParam)){
//                return;
//            }
            try {
                HotelInfo hotelInfo = DigitUtil.decodeHotelInfo(hotelData.hotelInfo);
                hotelData.icKey = hotelInfo.icKey;
                hotelData.aesKey = hotelInfo.aesKey;
                hotelData.hotelNumber = hotelInfo.hotelNumber;
            } catch (Exception exception) {
                LogUtil.w("exception:" + exception);
                callback.onFail(LockError.DATA_FORMAT_ERROR);
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setHotelData(hotelData);
                mApi.setHotelData(hotelData, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setHotelData(hotelData);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * 设置梯控的关联楼层
     * @param controlableFloors 数据格式 "1,2,3" 表示关联楼层1、2、3
     * @param lockData
     * @param callback
     */
    public void setLiftControlableFloors(String controlableFloors, String lockData, SetLiftControlableFloorsCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || TextUtils.isEmpty(controlableFloors)){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        HotelData hotelData = new HotelData();//todo:抛出异常
        hotelData.controlableFloors = DigitUtil.getControlableFloors(controlableFloors);
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_ELEVATOR_CONTROLABLE_FLOORS, callback)) {
            //TODO:
//            if(!isSupportThisOperation(lockParam)){
//                return;
//            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setHotelData(hotelData);
                mApi.setLiftControlableFloors(hotelData, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setHotelData(hotelData);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * 设置梯控的工作模式
     * @param ttLiftWorkMode
     * @param lockData
     * @param callback
     */
    public void setLiftWorkMode(TTLiftWorkMode ttLiftWorkMode, String lockData, SetLiftWorkModeCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || ttLiftWorkMode == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        HotelData hotelData = new HotelData();
        hotelData.ttLiftWorkMode = ttLiftWorkMode;

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_ELEVATOR_WORK_MODE, callback)) {
            //TODO:
//            if(!isSupportThisOperation(lockParam)){
//                return;
//            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setHotelData(hotelData);
                mApi.setLiftWorkMode(hotelData, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setHotelData(hotelData);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * 梯控可达楼层
     * @param activateFloors 可达楼层列表
     * @param currentDate  当前系统时间。传入0，默认使用当前手机时间
     * @param lockData
     * @param callback
     */
    public void activateLiftFloors(List<Integer> activateFloors, long currentDate, String lockData, ActivateLiftFloorsCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || activateFloors == null || activateFloors.size() == 0){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.ACTIVATE_LIFT_FLOORS, callback)) {
//            if(!isSupportThisOperation(lockParam)){
//                return;
//            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setActivateFloors(activateFloors);
                ConnectManager.getInstance().getConnectParamForCallback().setTimestamp(currentDate);
                mApi.activateLiftFloors(activateFloors, currentDate, lockParam);
            } else {
                ConnectParam param = new ConnectParam();
                param.setActivateFloors(activateFloors);
                param.setTimestamp(currentDate);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void setNBAwakeModes(List<NBAwakeMode> nbAwakeModeList, String lockData, SetNBAwakeModesCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || nbAwakeModeList == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        NBAwakeConfig nbAwakeConfig = new NBAwakeConfig();
        nbAwakeConfig.setNbAwakeModeList(nbAwakeModeList);

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_NB_ACTIVATE_MODE, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setNbAwakeConfig(nbAwakeConfig);
                mApi.setNBAwakeModes(nbAwakeConfig, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setNbAwakeConfig(nbAwakeConfig);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getNBAwakeModes(String lockData, GetNBAwakeModesCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_NB_ACITATE_MODE, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getNBAwakeModes(lockParam);
            } else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void setNBAwakeTimes(List<NBAwakeTime> nbAwakeTimeList, String lockData, SetNBAwakeTimesCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || nbAwakeTimeList == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        NBAwakeConfig nbAwakeConfig = new NBAwakeConfig();
        nbAwakeConfig.setNbAwakeTimeList(nbAwakeTimeList);
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_NB_ACTIVATE_CONFIG, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setNbAwakeConfig(nbAwakeConfig);
                mApi.setNBAwakeTimes(nbAwakeConfig, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setNbAwakeConfig(nbAwakeConfig);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getNBAwakeTimes(String lockData, GetNBAwakeTimesCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_NB_ACTIVATE_CONFIG, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getNBAwakeTimes(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void setPowerSaverWorkMode(PowerSaverWorkMode powerSaverWorkMode, String lockData, SetPowerSaverWorkModeCallback callback) {
        List<PowerSaverWorkMode> powerWorkModeList = new ArrayList<>();
        if (powerSaverWorkMode != null) {
            powerWorkModeList.add(powerSaverWorkMode);
        }
        setPowerSaverWorkModes(powerWorkModeList, lockData, callback);
    }

    /**
     *
     * @param powerWorkModeList null or size 0
     * @param lockData
     * @param callback
     */
    private void setPowerSaverWorkModes(List<PowerSaverWorkMode> powerWorkModeList, String lockData, SetPowerSaverWorkModeCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        HotelData hotelData = new HotelData();
        hotelData.paraType = HotelData.TYPE_POWER_SAVER_WORK_MODE;
        hotelData.powerWorkModeValue = DigitUtil.getPowerWorkModeValue(powerWorkModeList);
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_HOTEL_DATA, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setHotelData(hotelData);
                mApi.setHotelData(hotelData, lockParam);
            } else {
                ConnectParam param = new ConnectParam();
                param.setHotelData(hotelData);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    private void getPowerSaverWorkModes(String lockData, GetPowerSaverWorkModesCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        HotelData hotelData = new HotelData();
        hotelData.paraType = HotelData.TYPE_POWER_SAVER_WORK_MODE;

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_HOTEL_DATA, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getHotelData(hotelData, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setHotelData(hotelData);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     *
     * @param controlableLockMac null or "" means discontrolable
     * @param lockData
     * @param callback
     */
    public void setPowerSaverControlableLock(String controlableLockMac, String lockData, SetPowerSaverControlableLockCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){//todo:判断mac格式
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (TextUtils.isEmpty(controlableLockMac)) {//取电取消关联锁的时候，可以把关联的mac设成取电自己本身
            controlableLockMac = lockParam.getLockMac();
        }

        HotelData hotelData = new HotelData();
        hotelData.setControlableLockMac(controlableLockMac);
        hotelData.paraType = HotelData.TYPE_POWER_SAVER_CONTROLABLE_LOCK;

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_HOTEL_DATA, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setHotelData(hotelData);
                mApi.setHotelData(hotelData, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setHotelData(hotelData);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getUnlockDirection(String lockData, GetUnlockDirectionCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_UNLOCK_DIRECTION, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getUnlockDirection(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void setUnlockDirection(UnlockDirection unlockDirection, String lockData, SetUnlockDirectionCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_UNLOCK_DIRECTION, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setUnlockDirection(unlockDirection);
                mApi.setUnlockDirection(unlockDirection, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setUnlockDirection(unlockDirection);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getAccessoryBatteryLevel(AccessoryInfo accessoryInfo, String lockData, GetAccessoryBatteryLevelCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || accessoryInfo == null){//todo:判断mac格式
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_ACCESSORY_BATTERY, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setAccessoryInfo(accessoryInfo);
                mApi.getAccessoryBatteryLevel(accessoryInfo, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setAccessoryInfo(accessoryInfo);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void addRemote(String keyfobMac, ValidityInfo validityInfo, String lockData, AddRemoteCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || TextUtils.isEmpty(keyfobMac)){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.ADD_KEY_FOB, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setKeyfobMac(keyfobMac);
                ConnectManager.getInstance().getConnectParamForCallback().setValidityInfo(validityInfo);
                mApi.addRemote(keyfobMac, validityInfo, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setKeyfobMac(keyfobMac);
                param.setValidityInfo(validityInfo);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void modifyRemoteValidityPeriod(String keyfobMac, ValidityInfo validityInfo, String lockData, ModifyRemoteValidityPeriodCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || TextUtils.isEmpty(keyfobMac)){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.UPDATE_KEY_FOB_VALIDITY, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setKeyfobMac(keyfobMac);
                ConnectManager.getInstance().getConnectParamForCallback().setValidityInfo(validityInfo);
                mApi.modifyRemoteValidity(keyfobMac, validityInfo, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setKeyfobMac(keyfobMac);
                param.setValidityInfo(validityInfo);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void deleteRemote(String keyfobMac, String lockData, DeleteRemoteCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || TextUtils.isEmpty(keyfobMac)){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.DELETE_KEY_FOB, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setKeyfobMac(keyfobMac);
                mApi.deleteRemote(keyfobMac, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setKeyfobMac(keyfobMac);
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void clearRemote(String lockData, ClearRemoteCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.CLEAR_KEY_FOB, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.clearRemote(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * 扫描wifi锁附近的wifi
     * @param lockData
     * @param callback
     */
    public void scanWifi(String lockData, ScanWifiCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SCAN_WIFI, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.scanWifi(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * 配置wifi锁连接的wifi
     * @param wifiName
     * @param wifiPassword
     * @param lockData
     * @param callback
     */
    public void configWifi(String wifiName, String wifiPassword, String lockData, ConfigWifiCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || TextUtils.isEmpty(wifiName)){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.CONFIGURE_WIFI_AP, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setWifiName(wifiName);
                ConnectManager.getInstance().getConnectParamForCallback().setWifiPassword(wifiPassword);
                mApi.configureWifiAp(wifiName, wifiPassword, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setWifiName(wifiName);
                param.setWifiPassword(wifiPassword);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * 配置wifi锁 服务器
     * 服务器地址为空 或者 端口号为0 则默认使用ttlock的配置
     * @param serverAddress
     * @param portNumber
     * @param lockData
     * @param callback
     */
    public void configServer(String serverAddress, int portNumber, String lockData, ConfigServerCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if(TextUtils.isEmpty(serverAddress) || portNumber == 0) {
            serverAddress = "wifilock.ttlock.com";
            portNumber = 4999;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.CONFIGURE_SERVER, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setServerAddress(serverAddress);
                ConnectManager.getInstance().getConnectParamForCallback().setServerPort((short) portNumber);
                mApi.configureServer(serverAddress, portNumber, lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setServerAddress(serverAddress);
                param.setServerPort((short) portNumber);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * 获取wifi锁的 wifi mac
     * @param lockData
     * @param callback
     */
    public void getWifiInfo(String lockData, GetWifiInfoCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_WIFI_INFO, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getWifiInfo(lockParam);
            }else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * wifi锁配置静态ip
     * @param ipSetting
     * @param lockData
     * @param callback
     */
    public void configIp(IpSetting ipSetting, String lockData, ConfigIpCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || ipSetting == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }

        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.CONFIGURE_STATIC_IP, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setIpSetting(ipSetting);
                mApi.configureStaticIp(ipSetting, lockParam);
            } else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setIpSetting(ipSetting);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void setLockSoundWithSoundVolume(SoundVolume soundVolume, String lockData, SetLockSoundWithSoundVolumeCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null || soundVolume == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.SET_LOCK_SOUND_VOLUME, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setSoundVolume(soundVolume);
                mApi.setLockSound(soundVolume, lockParam);
            } else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setSoundVolume(soundVolume);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void getLockSoundWithSoundVolume(String lockData, GetLockSoundWithSoundVolumeCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.GET_LOCK_SOUND_VOLUME, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.getLockSound(lockParam);
            } else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * 添加门磁(将门磁mac写入锁内)
     * @param doorSensorMac  门磁mac地址
     * @param lockData
     * @param callback
     */
    public void addDoorSensor(String doorSensorMac, String lockData, AddDoorSensorCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.ADD_DOOR_SENSOR, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                ConnectManager.getInstance().getConnectParamForCallback().setDoorSensorMac(doorSensorMac);
                mApi.addDoorSensor(doorSensorMac, lockParam);
            } else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                param.setDoorSensorMac(doorSensorMac);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    /**
     * 删除锁关联的门磁
     * @param lockData
     * @param callback
     */
    public void deleteDoorSensor(String lockData, DeleteDoorSensorCallback callback) {
        LockData lockParam = EncryptionUtil.parseLockData(lockData);
        if(lockParam == null){
            callback.onFail(LockError.DATA_FORMAT_ERROR);
            return;
        }
        if (!LockCallbackManager.getInstance().isDeviceBusy(OperationType.DELETE_DOOR_SENSOR, callback)) {
            if(!isSupportThisOperation(lockParam)){
                return;
            }
            if (ConnectManager.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                mApi.deleteDoorSensor(lockParam);
            } else {
                ConnectParam param = new ConnectParam();
                param.setLockData(lockParam);
                ConnectManager.getInstance().storeConnectParamForCallback(param);
                ConnectManager.getInstance().connect2Device(lockParam.lockMac);
            }
        }
    }

    public void disconnect() {
        ConnectManager.getInstance().disconnect();
    }

    public void clearAllCallback() {
        LockCallbackManager.getInstance().clearAllCallback();
    }

    public String getSdkLog() {
        return BluetoothImpl.getInstance().getSdkLog();
    }

    private boolean isSupportThisOperation(LockData lockData){
        boolean isSupport = isSupportOperation(LockCallbackManager.getInstance().getOperationType(),lockData);
        if(!isSupport){
            LockCallback failCallback = LockCallbackManager.getInstance().getCallback();
            if(failCallback != null){
                failCallback.onFail(LockError.LOCK_IS_NOT_SUPPORT);
            }
        }
        return isSupport;
    }

    private static boolean isSupportOperation(int callbackType, LockData lockData){
        int feature = 1;
        switch (callbackType){
            case OperationType.GET_ADMIN_PASSCODE:
                feature = FeatureValue.GET_ADMIN_CODE;
                break;
            case OperationType.GET_MUTE_MODE_STATE:
            case OperationType.SET_MUTE_MODE_STATE:
                feature = FeatureValue.AUDIO_MANAGEMENT;
                break;
            case OperationType.SET_AUTO_LOCK_PERIOD:
                feature = FeatureValue.AUTO_LOCK;
                break;
            case OperationType.GET_REMOTE_UNLOCK_STATE:
            case OperationType.SET_REMOTE_UNLOCK_STATE:
                feature = FeatureValue.CONFIG_GATEWAY_UNLOCK;
                break;
            case OperationType.MODIFY_PASSCODE:
                feature = FeatureValue.MODIFY_PASSCODE_FUNCTION;
                break;
            case OperationType.ADD_FINGERPRINT:
                feature = FeatureValue.FINGER_PRINT;
                break;
            case OperationType.ADD_IC_CARD:
                feature = FeatureValue.IC;
                break;
            case OperationType.CREATE_CUSTOM_PASSCODE:
                feature = FeatureValue.PASSCODE;
                break;
            case OperationType.GET_PASSCODE_VISIBLE_STATE:
            case OperationType.SET_PASSCODE_VISIBLE_STATE:
                feature = FeatureValue.PASSWORD_DISPLAY_OR_HIDE;
                break;
            case OperationType.SET_NB_SERVER:
                feature = FeatureValue.NB_LOCK;
                break;
            case OperationType.GET_PASSAGE_MODE:
            case OperationType.SET_PASSAGE_MODE:
                feature = FeatureValue.PASSAGE_MODE;
                break;
            case OperationType.GET_LOCK_FREEZE_STATE:
            case OperationType.SET_LOCK_FREEZE_STATE:
                feature = FeatureValue.FREEZE_LOCK;
                break;
            case OperationType.GET_LIGHT_TIME:
            case OperationType.SET_LIGHT_TIME:
                feature = FeatureValue.LAMP;
                break;
            case OperationType.ADD_CYCLIC_IC_CARD:
            case OperationType.ADD_CYCLIC_FINGERPRINT:
                feature = FeatureValue.CYCLIC_IC_OR_FINGER_PRINT;
                break;
            case OperationType.SET_NB_ACTIVATE_CONFIG:
            case OperationType.GET_NB_ACTIVATE_CONFIG:
            case OperationType.SET_NB_ACTIVATE_MODE:
            case OperationType.GET_NB_ACITATE_MODE:
                feature = FeatureValue.NB_ACTIVITE_CONFIGURATION;
                break;
            case OperationType.SET_UNLOCK_DIRECTION:
            case OperationType.GET_UNLOCK_DIRECTION:
                feature = FeatureValue.UNLOCK_DIRECTION;
                break;
            case OperationType.GET_ACCESSORY_BATTERY:
                feature = FeatureValue.ACCESSORY_BATTERY;
                break;
            case OperationType.ADD_KEY_FOB:
            case OperationType.UPDATE_KEY_FOB_VALIDITY:
            case OperationType.DELETE_KEY_FOB:
            case OperationType.CLEAR_KEY_FOB:
                feature = FeatureValue.WIRELESS_KEY_FOB;
                break;
            case OperationType.SCAN_WIFI:
            case OperationType.CONFIGURE_WIFI_AP:
            case OperationType.CONFIGURE_SERVER:
            case OperationType.GET_WIFI_INFO:
                feature = FeatureValue.WIFI_LOCK;
                break;
            case OperationType.CONFIGURE_STATIC_IP:
                feature = FeatureValue.WIFI_LOCK_SUPPORT_STATIC_IP;
                break;
            case OperationType.SET_LOCK_SOUND_VOLUME:
            case OperationType.GET_LOCK_SOUND_VOLUME:
                feature = FeatureValue.SOUND_VOLUME_AND_LANGUAGE_SETTING;
                break;
            case OperationType.ADD_DOOR_SENSOR:
            case OperationType.DELETE_DOOR_SENSOR:
                feature = FeatureValue.DOOR_SENSOR;
                break;
            default:
                break;
        }

        return FeatureValueUtil.isSupportFeature(lockData, feature);
    }

//    private static boolean isSupportOperation(int callbackType,int specialValue){
//        int feature = 1;
//        switch (callbackType){
//            case OperationType.GET_ADMIN_PASSCODE:
//                feature = Feature.GET_ADMIN_CODE;
//                break;
//            case OperationType.GET_MUTE_MODE_STATE:
//            case OperationType.SET_MUTE_MODE_STATE:
//                feature = Feature.AUDIO_MANAGEMENT;
//                break;
//            case OperationType.SET_AUTO_LOCK_PERIOD:
//                feature = Feature.AUTO_LOCK;
//                break;
//            case OperationType.GET_REMOTE_UNLOCK_STATE:
//            case OperationType.SET_REMOTE_UNLOCK_STATE:
//                feature = Feature.CONFIG_GATEWAY_UNLOCK;
//                break;
//            case OperationType.MODIFY_PASSCODE:
//                feature = Feature.MODIFY_PASSCODE_FUNCTION;
//                break;
//            case OperationType.ADD_FINGERPRINT:
//                feature = Feature.FINGERPRINT;
//                break;
//            case OperationType.ADD_IC_CARD:
//                feature = Feature.CARD;
//                break;
//            case OperationType.CREATE_CUSTOM_PASSCODE:
//                feature = Feature.PASSCODE;
//                break;
//            case OperationType.GET_PASSCODE_VISIBLE_STATE:
//            case OperationType.SET_PASSCODE_VISIBLE_STATE:
//                feature = Feature.PASSWORD_DISPLAY_OR_HIDE;
//                break;
//            case OperationType.SET_NB_SERVER:
//                feature = Feature.NB_LOCK;
//                break;
//            case OperationType.GET_PASSAGE_MODE:
//            case OperationType.SET_PASSAGE_MODE:
//                feature = Feature.PASSAGE_MODE;
//                break;
//            case OperationType.GET_LOCK_FREEZE_STATE:
//            case OperationType.SET_LOCK_FREEZE_STATE:
//                feature = Feature.FREEZE_LOCK;
//                break;
//            case OperationType.GET_LIGHT_TIME:
//            case OperationType.SET_LIGHT_TIME:
//                feature = Feature.LAMP;
//                break;
//            default:
//                break;
//        }
//
//        return SpecialValueUtil.isSupportFeature(specialValue,feature);
//    }


}
