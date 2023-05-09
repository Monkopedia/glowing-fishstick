package com.ttlock.bl.sdk.api;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.ttlock.bl.sdk.callback.ConnectCallback;
import com.ttlock.bl.sdk.callback.ConnectLockCallback;
import com.ttlock.bl.sdk.callback.ControlLockCallback;
import com.ttlock.bl.sdk.callback.LockCallback;
import com.ttlock.bl.sdk.callback.OperationType;
import com.ttlock.bl.sdk.constant.ControlAction;
import com.ttlock.bl.sdk.device.TTDevice;
import com.ttlock.bl.sdk.device.WirelessKeypad;
import com.ttlock.bl.sdk.entity.ConnectParam;
import com.ttlock.bl.sdk.entity.ControlLockResult;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.util.LogUtil;

/**
 * Created on  2019/4/12 SetLockTimeCallback 11:30
 * @author theodore
 */
class ConnectManager implements ConnectCallback {
    private TTLockSDKApi mApi;
    private ConnectParam mConnectParam;
    private Handler mDataCheckHandler = new Handler();
    private String mCurrentMac;


    private ConnectManager(){
        mApi = new TTLockSDKApi();
        mConnectParam = null;
    }

    private Runnable mDataCheckErrorRunnable = new Runnable() {
        @Override
        public void run() {
            LockCallback mCallback = LockCallbackManager.getInstance().getCallback();
            if(mCallback != null){
                BluetoothImpl.getInstance().addSdkLog("response time out");
                LockError lockError = LockError.DATA_FORMAT_ERROR;
                lockError.setSdkLog(BluetoothImpl.getInstance().getSdkLog());
                mCallback.onFail(lockError);
            }
        }
    };

    public static ConnectManager getInstance(){
        return ConnectManager.InstanceHolder.mInstance;
    }

    private static class InstanceHolder{
        private final static ConnectManager mInstance = new ConnectManager();
    }

    public void removeDataCheckTimer(){
        mDataCheckHandler.removeCallbacks(mDataCheckErrorRunnable);
    }

    public void storeConnectParamForCallback(ConnectParam param){
        this.mConnectParam = param;
    }

    public ConnectParam getConnectParamForCallback() {
        return mConnectParam;
    }

    public boolean isDeviceConnected(String address){
        return BluetoothImpl.getInstance().isConnected(address);
    }

    public void connect2Device(String address){
        mCurrentMac = address;
        LockCallbackManager.getInstance().setConnectCallback(this);
        BluetoothImpl.getInstance().connect(address);
    }

    public void connect2Device(ExtendedBluetoothDevice device){
        LockCallbackManager.getInstance().setConnectCallback(this);
        BluetoothImpl.getInstance().connect(device);
    }

    public void connect2Device(WirelessKeypad device){
        LockCallbackManager.getInstance().setConnectCallback(this);
        GattCallbackHelper.getInstance().connect(device);
    }

    public void disconnect() {
        BluetoothImpl.getInstance().disconnect();
    }

    private void startDataCheckTimer(){
        mDataCheckHandler.postDelayed(mDataCheckErrorRunnable,500);
    }

    @Override
    public void onConnectSuccess(TTDevice device) {
        int callbackType = LockCallbackManager.getInstance().getOperationType();
        if (callbackType == OperationType.CONNECT_LOCK) {//连接回调
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    LockCallback mCallback = LockCallbackManager.getInstance().getCallback();
                    ((ConnectLockCallback)mCallback).onConnectSuccess();
                }
            });
            return;
        }
        if (callbackType == OperationType.UNKNOWN_TYPE) {
            LogUtil.d("unknown type");
            return;
        }
        startDataCheckTimer();
        if(mConnectParam == null && callbackType != OperationType.INIT_LOCK
                && callbackType != OperationType.GET_LOCK_VERSION){
            return;
        }
        switch (callbackType){
            case OperationType.INIT_LOCK:
                mApi.initLock((ExtendedBluetoothDevice) device);
                break;
            case OperationType.INIT_KEYPAD:
                WirelessKeypadSDKApi.initKeyapd((WirelessKeypad)device,mConnectParam);
                break;
            case OperationType.GET_LOCK_VERSION:
                mApi.getLockVersion();
                break;
            case OperationType.CONTROL_LOCK:
                mApi.controlLock(mConnectParam.getControlAction(),mConnectParam.getLockData());
                if (isRollingGate())
                    rollingGateCallback();
                break;
            case OperationType.RESET_KEY:
                mApi.resetEkey(mConnectParam.getLockData());
                break;
            case OperationType.RESET_LOCK:
                mApi.resetLock(mConnectParam.getLockData());
                break;
            case OperationType.GET_MUTE_MODE_STATE:
                mApi.getMuteModeState(mConnectParam.getLockData());
                break;
            case OperationType.SET_MUTE_MODE_STATE:
                 mApi.setMuteMode(mConnectParam.isLockModeEnable(),mConnectParam.getLockData());
                break;
            case OperationType.GET_REMOTE_UNLOCK_STATE:
                 mApi.getRemoteUnlockSwitchState(mConnectParam.getLockData());
                 break;
            case OperationType.SET_REMOTE_UNLOCK_STATE:
                 mApi.setRemoteUnlockSwitchState(mConnectParam.isLockModeEnable(),mConnectParam.getLockData());
                 break;
            case OperationType.GET_PASSCODE_VISIBLE_STATE:
                mApi.getPasscodeVisibleSwitchState(mConnectParam.getLockData());
                break;
            case OperationType.SET_PASSCODE_VISIBLE_STATE:
                mApi.setPasscodeVisibleSwitchState(mConnectParam.isLockModeEnable(),mConnectParam.getLockData());
                break;
            case OperationType.GET_PASSAGE_MODE:
                mApi.getPassageMode(mConnectParam.getLockData());
                break;
            case OperationType.SET_PASSAGE_MODE:
                mApi.setPassageMode(mConnectParam.getPassageModeConfig(),mConnectParam.getLockData());
                break;
            case OperationType.DELETE_PASSAGE_MODE:
                mApi.deletePassageMode(mConnectParam.getPassageModeConfig(),mConnectParam.getLockData());
                break;
            case OperationType.CLEAR_PASSAGE_MODE:
                mApi.clearPassageMode(mConnectParam.getLockData());
                break;
            case OperationType.GET_LOCK_TIME:
                mApi.getLockTime(mConnectParam.getLockData());
                break;
            case OperationType.SET_LOCK_TIME:
                mApi.setLockTime(mConnectParam.getTimestamp(),mConnectParam.getLockData());
                break;
            case OperationType.GET_OPERATION_LOG:
                mApi.getOperationLog(mConnectParam.getLogType(),mConnectParam.getLockData());
                break;
            case OperationType.GET_ELECTRIC_QUALITY:
                mApi.getBatteryLevel(mConnectParam.getLockData());
                break;
            case OperationType.GET_SPECIAL_VALUE:
                mApi.getSpecialValue(mConnectParam.getLockData());
                break;
            case OperationType.RECOVERY_DATA:
                mApi.recoverLockData(mConnectParam.getRecoveryDataStr(),mConnectParam.getRecoveryDataType(),mConnectParam.getLockData());
                break;
            case OperationType.GET_SYSTEM_INFO:
                mApi.getLockSystemInfo(mConnectParam.getLockData());
                break;
            case OperationType.CREATE_CUSTOM_PASSCODE:
                mApi.createCustomPasscode(mConnectParam.getOriginalPasscode(),mConnectParam.getStartDate(),mConnectParam.getEndDate(),mConnectParam.getLockData());
                break;
            case OperationType.GET_LOCK_STATUS:
                mApi.getLockStatus(mConnectParam.getLockData());
                break;
            case OperationType.SET_AUTO_LOCK_PERIOD:
                mApi.setAutomaticLockingPeriod(mConnectParam.getAutoLockingPeriod(),mConnectParam.getLockData());
                break;
            case OperationType.GET_AUTO_LOCK_PERIOD:
                mApi.getAutomaticLockingPeriod(mConnectParam.getLockData());
                break;
            case OperationType.MODIFY_PASSCODE:
                mApi.modifyPasscode(mConnectParam.getOriginalPasscode(),mConnectParam.getNewPasscode(),mConnectParam.getStartDate(),mConnectParam.getEndDate(),mConnectParam.getLockData());
                break;
            case OperationType.DELETE_PASSCODE:
                mApi.deletePasscode(mConnectParam.getOriginalPasscode(),mConnectParam.getLockData());
                break;
            case OperationType.RESET_PASSCODE:
                mApi.resetPasscode(mConnectParam.getLockData());
                break;
            case OperationType.GET_ALL_VALID_PASSCODES:
                mApi.getAllValidPasscodes(mConnectParam.getLockData());
                break;
            case OperationType.GET_PASSCODE_INFO:
                mApi.getPasscodeVerificationParams(mConnectParam.getLockData());
                break;
            case OperationType.GET_ADMIN_PASSCODE:
                mApi.getAdminPasscode(mConnectParam.getLockData());
                break;
            case OperationType.MODIFY_ADMIN_PASSCODE:
                mApi.modifyAdminPasscode(mConnectParam.getNewPasscode(),mConnectParam.getLockData());
                break;
            case OperationType.ADD_IC_CARD:
            case OperationType.ADD_CYCLIC_IC_CARD:
                mApi.addICCard(mConnectParam.getValidityInfo(), mConnectParam.getLockData());
//                mApi.addICCard(mConnectParam.getStartDate(),mConnectParam.getEndDate(),mConnectParam.getLockData());
                break;
            case OperationType.MODIFY_IC_CARD_PERIOD:
                mApi.modifyICCardValidityPeriod(mConnectParam.getValidityInfo(), mConnectParam.getAttachmentNum(),mConnectParam.getLockData());
//                mApi.modifyICCardValidityPeriod(mConnectParam.getStartDate(),mConnectParam.getEndDate(),mConnectParam.getAttachmentNum(),mConnectParam.getLockData());
                break;
            case OperationType.GET_ALL_IC_CARDS:
                mApi.getAllValidICCards(mConnectParam.getLockData());
                break;
            case OperationType.DELETE_IC_CARD:
                mApi.deleteICCard(mConnectParam.getAttachmentNum(),mConnectParam.getLockData());
                break;
            case OperationType.LOSS_CARD:
                mApi.lossICCard(mConnectParam.getAttachmentNum(),mConnectParam.getLockData());
                break;
            case OperationType.CLEAR_ALL_IC_CARD:
                mApi.clearAllICCard(mConnectParam.getLockData());
                break;
            case OperationType.ADD_FINGERPRINT:
            case OperationType.ADD_CYCLIC_FINGERPRINT:
                mApi.addFingerprint(mConnectParam.getValidityInfo(), mConnectParam.getLockData());
//                mApi.addFingerprint(mConnectParam.getStartDate(),mConnectParam.getEndDate(),mConnectParam.getLockData());
                break;
            case OperationType.MODIFY_FINGEPRINT_PERIOD:
                mApi.modifyFingerprintValidityPeriod(mConnectParam.getValidityInfo(), mConnectParam.getAttachmentNum(),mConnectParam.getLockData());
//                mApi.modifyFingerprintValidityPeriod(mConnectParam.getStartDate(),mConnectParam.getEndDate(),mConnectParam.getAttachmentNum(),mConnectParam.getLockData());
                break;
            case OperationType.GET_ALL_FINGERPRINTS:
                mApi.getAllValidFingerprints(mConnectParam.getLockData());
                break;
            case OperationType.DELETE_FINGERPRINT:
                mApi.deleteFingerprint(mConnectParam.getAttachmentNum(),mConnectParam.getLockData());
                break;
            case OperationType.CLEAR_ALL_FINGERPRINTS:
                mApi.clearAllFingerprints(mConnectParam.getLockData());
                break;
            case OperationType.WRITE_FINGERPRINT_DATA:
                mApi.writeFingerprintData(mConnectParam.getDataJsonStr(),(int)mConnectParam.getAttachmentNum(),mConnectParam.getStartDate(),mConnectParam.getEndDate(),mConnectParam.getLockData());
                break;
            case OperationType.ENTER_DFU_MODE:
                mApi.enterDfuMode(mConnectParam.getLockData());
                break;
            case OperationType.SET_NB_SERVER:
                mApi.setNBServerInfo(mConnectParam.getServerPort(),mConnectParam.getServerAddress(),mConnectParam.getLockData());
                 break;
            case OperationType.SET_LOCK_FREEZE_STATE:
                mApi.setLockFreezeState(mConnectParam.isLockModeEnable(), mConnectParam.getLockData());
                break;
            case OperationType.GET_LOCK_FREEZE_STATE:
                mApi.getLockFreezeState(mConnectParam.getLockData());
                break;
            case OperationType.SET_LIGHT_TIME:
                mApi.setLightTime(mConnectParam.getSeconds(), mConnectParam.getLockData());
                break;
            case OperationType.GET_LIGHT_TIME:
                mApi.getLightTime(mConnectParam.getLockData());
                break;
            case OperationType.SET_HOTEL_CARD_SECTION:
                mApi.setHotelCardSector(mConnectParam.getHotelData(), mConnectParam.getLockData());
                break;
            case OperationType.SET_LOCK_CONFIG:
                switch (mConnectParam.getTtLockConfigType()) {
                    case LOCK_SOUND:
                        mApi.setMuteMode(!mConnectParam.isLockModeEnable(), mConnectParam.getLockData());
                        break;
                    case PASSCODE_VISIBLE:
                        mApi.setPasscodeVisibleSwitchState(mConnectParam.isLockModeEnable(), mConnectParam.getLockData());
                        break;
                    case LOCK_FREEZE:
                        mApi.setLockFreezeState(mConnectParam.isLockModeEnable(), mConnectParam.getLockData());
                        break;
                    case PRIVACY_LOCK:
                    case TAMPER_ALERT:
                    case RESET_BUTTON:
                    case WIFI_LOCK_POWER_SAVING_MODE:
                        mApi.setLockConfig(mConnectParam.getTtLockConfigType(), mConnectParam.isLockModeEnable(), mConnectParam.getLockData());
                        break;
                }
                break;
            case OperationType.GET_LOCK_CONFIG:
                switch (mConnectParam.getTtLockConfigType()) {
                    case LOCK_SOUND:
                        mApi.getMuteModeState(mConnectParam.getLockData());
                        break;
                    case PASSCODE_VISIBLE:
                        mApi.getPasscodeVisibleSwitchState(mConnectParam.getLockData());
                        break;
                    case LOCK_FREEZE:
                        mApi.getLockFreezeState(mConnectParam.getLockData());
                        break;
                    case PRIVACY_LOCK:
                    case TAMPER_ALERT:
                    case RESET_BUTTON:
                    case WIFI_LOCK_POWER_SAVING_MODE:
                        mApi.getLockConfig(mConnectParam.getTtLockConfigType(), mConnectParam.getLockData());
                        break;
                }
                break;
            case OperationType.SET_HOTEL_DATA:
                mApi.setHotelData(mConnectParam.getHotelData(), mConnectParam.getLockData());
                break;
            case OperationType.SET_ELEVATOR_CONTROLABLE_FLOORS:
                mApi.setLiftControlableFloors(mConnectParam.getHotelData(), mConnectParam.getLockData());
                break;
            case OperationType.SET_ELEVATOR_WORK_MODE:
                mApi.setLiftWorkMode(mConnectParam.getHotelData(), mConnectParam.getLockData());
                break;
            case OperationType.SET_NB_ACTIVATE_MODE:
                mApi.setNBAwakeModes(mConnectParam.getNbAwakeConfig(), mConnectParam.getLockData());
                break;
            case OperationType.GET_NB_ACITATE_MODE:
                mApi.getNBAwakeModes(mConnectParam.getLockData());
                break;
            case OperationType.SET_NB_ACTIVATE_CONFIG:
                mApi.setNBAwakeTimes(mConnectParam.getNbAwakeConfig(), mConnectParam.getLockData());
                break;
            case OperationType.GET_NB_ACTIVATE_CONFIG:
                mApi.getNBAwakeTimes(mConnectParam.getLockData());
                break;
            case OperationType.ACTIVATE_LIFT_FLOORS:
                mApi.activateLiftFloors(mConnectParam.getActivateFloors(), mConnectParam.getTimestamp(), mConnectParam.getLockData());
                break;
            case OperationType.SET_UNLOCK_DIRECTION:
                mApi.setUnlockDirection(mConnectParam.getUnlockDirection(), mConnectParam.getLockData());
                break;
            case OperationType.GET_UNLOCK_DIRECTION:
                mApi.getUnlockDirection(mConnectParam.getLockData());
                break;
            case OperationType.GET_ACCESSORY_BATTERY:
                mApi.getAccessoryBatteryLevel(mConnectParam.getAccessoryInfo(), mConnectParam.getLockData());
                break;
            case OperationType.ADD_KEY_FOB:
                mApi.addRemote(mConnectParam.getKeyfobMac(), mConnectParam.getValidityInfo(), mConnectParam.getLockData());
                break;
            case OperationType.UPDATE_KEY_FOB_VALIDITY:
                mApi.modifyRemoteValidity(mConnectParam.getKeyfobMac(), mConnectParam.getValidityInfo(), mConnectParam.getLockData());
                break;
            case OperationType.DELETE_KEY_FOB:
                mApi.deleteRemote(mConnectParam.getKeyfobMac(), mConnectParam.getLockData());
                break;
            case OperationType.CLEAR_KEY_FOB:
                mApi.clearRemote(mConnectParam.getLockData());
                break;
            case OperationType.SCAN_WIFI:
                mApi.scanWifi(mConnectParam.getLockData());
                break;
            case OperationType.CONFIGURE_WIFI_AP:
                mApi.configureWifiAp(mConnectParam.getWifiName(), mConnectParam.getWifiPassword(), mConnectParam.getLockData());
                break;
            case OperationType.CONFIGURE_SERVER:
                mApi.configureServer(mConnectParam.getServerAddress(), mConnectParam.getServerPort(), mConnectParam.getLockData());
                break;
            case OperationType.GET_WIFI_INFO:
                mApi.getWifiInfo(mConnectParam.getLockData());
                break;
            case OperationType.CONFIGURE_STATIC_IP:
                mApi.configureStaticIp(mConnectParam.getIpSetting(), mConnectParam.getLockData());
                break;
            case OperationType.SET_LOCK_SOUND_VOLUME:
                mApi.setLockSound(mConnectParam.getSoundVolume(), mConnectParam.getLockData());
                break;
            case OperationType.GET_LOCK_SOUND_VOLUME:
                mApi.getLockSound(mConnectParam.getLockData());
                break;
            case OperationType.ADD_DOOR_SENSOR:
                mApi.addDoorSensor(mConnectParam.getDoorSensorMac(), mConnectParam.getLockData());
                break;
            case OperationType.DELETE_DOOR_SENSOR:
                mApi.deleteDoorSensor(mConnectParam.getLockData());
                break;
            default:
                break;
        }
    }

    /**
     * do second connect if first time connect fail.
     * @param error
     */
    @Override
    public void onFail(LockError error) {
        if (!LockCallbackManager.getInstance().callbackArrayIsEmpty()) {
            int callbackType = LockCallbackManager.getInstance().getOperationType();
            if (callbackType == OperationType.UNKNOWN_TYPE) {
                LogUtil.d("unknown type");
                return;
            }
            LockCallback mCallback = LockCallbackManager.getInstance().getCallback();
            if (mCallback != null) {
                if (!TextUtils.isEmpty(mCurrentMac) && error != LockError.BLE_SERVER_NOT_INIT && !isRollingGate()) {
                    retryConnect(callbackType, mCallback);
                } else {
                    mCallback.onFail(error);
                }
            }
        }
    }

    /**
     * 判断是否是遥控设备指令
     * @return
     */
    private boolean isRollingGate() {
        if (mConnectParam != null && LockCallbackManager.getInstance().getOperationType() == OperationType.CONTROL_LOCK) {
            int action = mConnectParam.getControlAction();
            if (action == ControlAction.ROLLING_GATE_UP || action == ControlAction.ROLLING_GATE_DOWN
                    || action == ControlAction.ROLLING_GATE_PAUSE || action == ControlAction.ROLLING_GATE_LOCK) {
                return true;
            }
        }
        return false;
    }

    private void rollingGateCallback() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                LockCallback mControlLockCallback = LockCallbackManager.getInstance().getCallback();
                if (mControlLockCallback != null && mConnectParam != null) {
                    ((ControlLockCallback) mControlLockCallback).onControlLockSuccess(new ControlLockResult(mConnectParam.getControlAction(), -1, -1, -1));
                }
            }
        });
    }

    private void retryConnect(int callbackType,LockCallback callback){
        connect2Device(mCurrentMac);
        LockCallbackManager.getInstance().isDeviceBusy(callbackType,callback);
        mCurrentMac = "";
    }

}
