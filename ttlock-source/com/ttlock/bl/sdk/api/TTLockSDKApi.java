package com.ttlock.bl.sdk.api;

import android.Manifest;
import android.content.Context;
import androidx.annotation.RequiresPermission;
import android.text.TextUtils;

import com.ttlock.bl.sdk.callback.LockCallback;
import com.ttlock.bl.sdk.constant.APICommand;
import com.ttlock.bl.sdk.constant.ActionType;
import com.ttlock.bl.sdk.constant.Constant;
import com.ttlock.bl.sdk.constant.ControlAction;
import com.ttlock.bl.sdk.constant.LockType;
import com.ttlock.bl.sdk.constant.LogType;
import com.ttlock.bl.sdk.constant.OperationType;
import com.ttlock.bl.sdk.entity.AccessoryInfo;
import com.ttlock.bl.sdk.entity.HotelData;
import com.ttlock.bl.sdk.entity.IpSetting;
import com.ttlock.bl.sdk.entity.LockData;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.entity.LockVersion;
import com.ttlock.bl.sdk.entity.NBAwakeConfig;
import com.ttlock.bl.sdk.entity.PassageModeConfig;
import com.ttlock.bl.sdk.entity.PassageModeType;
import com.ttlock.bl.sdk.entity.SoundVolume;
import com.ttlock.bl.sdk.entity.TTLockConfigType;
import com.ttlock.bl.sdk.entity.TransferData;
import com.ttlock.bl.sdk.entity.UnlockDirection;
import com.ttlock.bl.sdk.entity.ValidityInfo;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.GsonUtil;

import java.util.List;

/**
 * Created on  2019/4/11 0011 15:41
 *
 * @author theodore
 */
class TTLockSDKApi extends TTLockSdkApiBase{

    private int lastKey;

    @Override
    public void prepareBTService(Context context){
        BluetoothImpl.getInstance().prepareBTService(context);
    }
    @Override
    public void stopBTService(){
        BluetoothImpl.getInstance().stopBTService();
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    @Override
    public void startScan(){
        BluetoothImpl.getInstance().startScan();
    }

    @Override
    public void stopScan(){
        BluetoothImpl.getInstance().stopScan();
    }

    public void initLock(ExtendedBluetoothDevice device){

        int lockType = device.getLockType();
        //TODO:注释掉有些锁搜索广播慢
        //TODO:E指令有時候彙報KEY_INVALID 查一下原因 是不是 沒有進入設置模式造成的
        //TODO:车位锁添加的时候反馈有异常指令
        if(lockType > LockType.LOCK_TYPE_V2S && !device.isSettingMode()) {
            LockCallback mCallback = LockCallbackManager.getInstance().getCallback();
            if(mCallback != null){
                mCallback.onFail(LockError.LOCK_IS_IN_NO_SETTING_MODE);
            }
            return ;
        }
        switch (lockType) {
            case LockType.LOCK_TYPE_CAR:
                String adminPs = new String(DigitUtil.generateDynamicPassword(10));
                String unlockKey = new String(DigitUtil.generateDynamicPassword(10));
                CommandUtil.V_addAdmin(LockType.LOCK_TYPE_CAR, adminPs, unlockKey, null);
                break;
            case LockType.LOCK_TYPE_MOBI:
                break;
            case LockType.LOCK_TYPE_V2S:
                adminPs = new String(DigitUtil.generateDynamicPassword(10));
                unlockKey = new String(DigitUtil.generateDynamicPassword(10));
                CommandUtil.V_addAdmin(LockType.LOCK_TYPE_V2S, adminPs, unlockKey, null);
                break;
            case LockType.LOCK_TYPE_V2S_PLUS:
                CommandUtil.getAESKey(LockVersion.lockVersion_V2S_PLUS, "", APICommand.OP_ADD_ADMIN);
                break;
            case LockType.LOCK_TYPE_V3:
            case LockType.LOCK_TYPE_V3_CAR:
                TransferData transferData = new TransferData();
                transferData.setAPICommand(APICommand.OP_ADD_ADMIN);
                transferData.setHotelData(device.getHotelData());
                CommandUtil.getAESKey(transferData);
                Constant.VENDOR = Constant.SCIENER;
                break;
            default:
                //TODO:
                CommandUtil.E_getLockVersion(APICommand.OP_ADD_ADMIN);
                break;
        }

    }

    public void setNBServerInfo(short portNumber, String serverAddress, LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_CONFIGURE_NB_SERVER_ADDRESS);
        transferData.setCommand(Command.COMM_CONFIGURE_NB_ADDRESS);
        transferData.setPort(portNumber);
        transferData.setAddress(serverAddress);
        CommandUtil.A_checkAdmin(transferData);
//        CommandUtil_V3.configureNBServerAddress(transferData);
    }


        public void resetEkey(LockData lockData){
        String aesKeyStr = lockData.getAesKeyStr();
        byte[] aesKeyArray = null;
        if(!TextUtils.isEmpty(aesKeyStr)){
            aesKeyArray = DigitUtil.convertAesKeyStrToBytes(aesKeyStr);
        }
        CommandUtil.A_checkAdmin(lockData.getUid(), GsonUtil.toJson(lockData.getLockVersion()), lockData.getAdminPwd(), null, lockData.getLockFlagPos(), aesKeyArray, 0, null, APICommand.OP_RESET_EKEY);
    }

    public void resetLock(LockData lockData){
        byte[] aesKeyArray = DigitUtil.convertAesKeyStrToBytes(lockData.getAesKeyStr());
        CommandUtil.A_checkAdmin(lockData.getUid(),  GsonUtil.toJson(lockData.getLockVersion()), lockData.getAdminPwd(), lockData.getLockKey(),  lockData.getLockFlagPos(), aesKeyArray, APICommand.OP_RESET_LOCK);
    }

    /**
     *
     * @param controlAction
     * @param keyData
     */
    public void controlLock(int controlAction,LockData keyData){
        switch (controlAction){
            case ControlAction.UNLOCK:
                if (keyData.getUserType() == 110302)
                    unlockByUser(keyData);
                else unlockByAdmin(keyData);
                break;
            case ControlAction.LOCK:
                lockByUser(keyData);
                break;
            case ControlAction.ROLLING_GATE_DOWN:
            case ControlAction.ROLLING_GATE_UP:
            case ControlAction.ROLLING_GATE_PAUSE:
            case ControlAction.ROLLING_GATE_LOCK:
                operateRollingGate(controlAction,keyData,2);
                break;
                default:
                    break;
        }
    }

    public void getMuteModeState(LockData lockData){
        operateMuteModeState(1,0,lockData);
    }

    public void setMuteMode(boolean enable,LockData lockData){
        operateMuteModeState(ActionType.SET,enable ? 0 : 1,lockData);
    }

    public void getRemoteUnlockSwitchState(LockData lockData){
        operateRemoteUnlockProperty(ActionType.GET,0,lockData);
    }

    public void setRemoteUnlockSwitchState(boolean enable,LockData lockData){
        operateRemoteUnlockProperty(ActionType.SET,enable ? 1 : 0,lockData);
    }

    public void getPasscodeVisibleSwitchState(LockData lockData){
        operatePasscodeVisibleMode(ActionType.GET,lockData);
    }

    public void setPasscodeVisibleSwitchState(boolean enable,LockData lockData){
        operatePasscodeVisibleMode(enable ? 3 : 2,lockData);
    }

    /**
     *
     * @param operateType  1 - query 、 2 - modify
     * @param state  1 - on、0 - off
     * @param lockData lock data
     */
    private void operateMuteModeState(int operateType,int state,LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_AUDIO_MANAGEMENT);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        transferData.setOp(operateType);
        transferData.setOpValue(state);
        CommandUtil.A_checkAdmin(transferData);
    }

    /**
     *
     * @param operateType  1 - query 、 2 - modify
     * @param state  1 - on、0 - off
     * @param lockData lock data
     */
    private void operateRemoteUnlockProperty(int operateType,int state,LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_CONTROL_REMOTE_UNLOCK);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        transferData.setOp(operateType);
        transferData.setOpValue(state);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void setLockTime(long timestamp,LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_CALIBRATE_TIME);
        transferData.setCalibationTime(timestamp);
        CommandUtil.C_calibationTime(transferData);
    }

    public void getLockTime(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_GET_LOCK_TIME);
        CommandUtil.getLockTime(transferData);
    }

    public void getOperationLog(int logType,LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setLogType(logType);

        if (logType == LogType.ALL) {
            BluetoothImpl.getInstance().clearRecordCnt();
            transferData.setSeq((short) 0);
        } else {
            transferData.setSeq((short) 0xffff);
        }
        CommandUtil.getOperateLog(transferData);
    }

    public void getBatteryLevel(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_GET_POW);
        transferData.setCommand(Command.COMM_SEARCHE_DEVICE_FEATURE);
        CommandUtil.getPow(transferData);
    }

    public void getLockVersion(){
        CommandUtil.E_getLockVersion(APICommand.OP_GET_LOCK_VERSION);
    }

    public void getSpecialValue(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SEARCH_DEVICE_FEATURE);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void recoverLockData(String dataJson, int dataType, LockData lockData){

        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_RECOVERY_DATA);

        byte command = 0;
        switch (dataType) {
            case 1:
                command = Command.COMM_MANAGE_KEYBOARD_PASSWORD;
                break;
            case 2:
                command = Command.COMM_IC_MANAGE;
                break;
            case 3:
                command = Command.COMM_FR_MANAGE;
                break;
                default:
                    break;
        }
        transferData.setCommand(command);
        transferData.setOp(dataType);
        transferData.setJson(dataJson);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getLockSystemInfo(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_GET_DEVICE_INFO);
//        CommandUtil.readDeviceInfo(transferData);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getLockStatus(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_GET_LOCK_SWITCH_STATE);
        transferData.setCommand(Command.COMM_SEARCH_BICYCLE_STATUS);
        CommandUtil.searchBicycleStatus(transferData);
    }

    public void setAutomaticLockingPeriod(int period,LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SET_AUTO_LOCK_TIME);
        transferData.setCommand(Command.COMM_AUTO_LOCK_MANAGE);
        transferData.setCalibationTime(period);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getAutomaticLockingPeriod(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SEARCH_AUTO_LOCK_PERIOD);
        transferData.setCommand(Command.COMM_AUTO_LOCK_MANAGE);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void createCustomPasscode(String passcode, long startDate, long endDate, LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_ADD_PERIOD_KEYBOARD_PASSWORD);
        if (startDate == 0) {
            startDate = Constant.permanentStartDate;
        }
        if (endDate == 0) {
            endDate = Constant.permanentEndDate;
        }
        transferData.setStartDate(startDate);
        transferData.setEndDate(endDate);
        transferData.setOriginalPwd(passcode);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void modifyPasscode(String originalCode, String newCode, long startDate, long endDate,LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_MODIFY_KEYBOARD_PASSWORD);
        transferData.setStartDate(startDate);
        transferData.setEndDate(endDate);
        transferData.setOriginalPwd(originalCode);
        transferData.setNewPwd(newCode);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getAdminPasscode(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_GET_ADMIN_KEYBOARD_PASSWORD);
        transferData.setCommand(Command.COMM_GET_ADMIN_CODE);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void modifyAdminPasscode(String newPasscode,LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SET_KEYBOARD_PASSWORD);
        transferData.setNewPwd(newPasscode);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void deletePasscode(String passcode,LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_REMOVE_ONE_PASSWORD);
        transferData.setOriginalPwd(passcode);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void resetPasscode(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_INIT_PWD);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getAllValidPasscodes(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SEARCH_PWD);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getPasscodeVerificationParams(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_READ_PWD_PARA);
        transferData.setCommand(Command.COMM_READ_PWD_PARA);
        CommandUtil.U_checkUserTime(transferData);
    }

//    public void addICCard(long startDate,long endDate,LockData lockData){
//        TransferData transferData = getPreparedData(lockData);
//        transferData.setAPICommand(APICommand.OP_ADD_IC);
//        transferData.setCommand(Command.COMM_IC_MANAGE);
//        if(startDate > 0 && endDate > 0){
//            transferData.setStartDate(startDate);
//            transferData.setEndDate(endDate);
//        }
//        CommandUtil.A_checkAdmin(transferData);
//    }

    public void addICCard(ValidityInfo validityInfo, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_ADD_IC);
        transferData.setValidityInfo(validityInfo);

        if(validityInfo != null && validityInfo.getStartDate() > 0 && validityInfo.getEndDate() > 0){//兼容之前的使用方式
            transferData.setStartDate(validityInfo.getStartDate());
            transferData.setEndDate(validityInfo.getEndDate());
        }

        CommandUtil.A_checkAdmin(transferData);
    }

    public void modifyICCardValidityPeriod(ValidityInfo validityInfo, long CardNum,LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_MODIFY_IC_PERIOD);
        transferData.setValidityInfo(validityInfo);
        transferData.setNo(CardNum);

        if(validityInfo != null && validityInfo.getStartDate() > 0 && validityInfo.getEndDate() > 0){//兼容之前的使用方式
            transferData.setStartDate(validityInfo.getStartDate());
            transferData.setEndDate(validityInfo.getEndDate());
        }

        CommandUtil.A_checkAdmin(transferData);
    }

//    public void modifyICCardValidityPeriod(long startDate, long endDate, long CardNum,LockData lockData){
//        TransferData transferData = getPreparedData(lockData);
//        transferData.setAPICommand(APICommand.OP_MODIFY_IC_PERIOD);
//        transferData.setCommand(Command.COMM_IC_MANAGE);
//        transferData.setNo(CardNum);
//        transferData.setStartDate(startDate);
//        transferData.setEndDate(endDate);
//        CommandUtil.A_checkAdmin(transferData);
//    }

    public void getAllValidICCards(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SEARCH_IC_CARD_NO);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void deleteICCard(long cardNum,LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_DELETE_IC);
        transferData.setCommand(Command.COMM_IC_MANAGE);
        transferData.setNo(cardNum);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void lossICCard(long cardNum,LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_LOSS_IC);
        transferData.setCommand(Command.COMM_IC_MANAGE);
        transferData.setNo(cardNum);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void clearAllICCard(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_CLEAR_IC);
        transferData.setCommand(Command.COMM_IC_MANAGE);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void addFingerprint(ValidityInfo validityInfo, LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_ADD_FR);
        transferData.setValidityInfo(validityInfo);

        if(validityInfo != null && validityInfo.getStartDate() > 0 && validityInfo.getEndDate() > 0){//兼容之前的使用方式
            transferData.setStartDate(validityInfo.getStartDate());
            transferData.setEndDate(validityInfo.getEndDate());
        }

        CommandUtil.A_checkAdmin(transferData);
    }

//    public void addFingerprint(long startDate,long endDate,LockData lockData){
//        TransferData transferData = getPreparedData(lockData);
//        transferData.setAPICommand(APICommand.OP_ADD_FR);
//        transferData.setCommand(Command.COMM_FR_MANAGE);
//        if(startDate > 0 && endDate > 0){
//            transferData.setStartDate(startDate);
//            transferData.setEndDate(endDate);
//        }
//        CommandUtil.A_checkAdmin(transferData);
//    }

    public void modifyFingerprintValidityPeriod(ValidityInfo validityInfo, long FingerprintNum,LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_MODIFY_FR_PERIOD);
        transferData.setValidityInfo(validityInfo);
        transferData.setNo(FingerprintNum);

        if(validityInfo != null && validityInfo.getStartDate() > 0 && validityInfo.getEndDate() > 0){//兼容之前的使用方式
            transferData.setStartDate(validityInfo.getStartDate());
            transferData.setEndDate(validityInfo.getEndDate());
        }

        CommandUtil.A_checkAdmin(transferData);
    }

//    public void modifyFingerprintValidityPeriod(long startDate, long endDate, long FingerprintNum,LockData lockData) {
//        TransferData transferData = getPreparedData(lockData);
//        transferData.setAPICommand(APICommand.OP_MODIFY_FR_PERIOD);
//        transferData.setCommand(Command.COMM_FR_MANAGE);
//        transferData.setNo(FingerprintNum);
//        transferData.setStartDate(startDate);
//        transferData.setEndDate(endDate);
//        CommandUtil.A_checkAdmin(transferData);
//    }

    public void getAllValidFingerprints(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SEARCH_FR);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void deleteFingerprint(long cardNum,LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_DELETE_FR);
        transferData.setCommand(Command.COMM_FR_MANAGE);
        transferData.setNo(cardNum);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void clearAllFingerprints(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_CLEAR_FR);
        transferData.setCommand(Command.COMM_FR_MANAGE);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void writeFingerprintData(String fingerprintData, int tempFingerprintNumber, long startDate, long endDate, LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_WRITE_FR);
        transferData.setCommand(Command.COMM_FR_MANAGE);
        transferData.setStartDate(startDate);
        transferData.setEndDate(endDate);
        transferData.setJson(fingerprintData);
        transferData.setNo(tempFingerprintNumber);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void enterDfuMode(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_ENTER_DFU_MODE);
        transferData.setCommand(Command.COMM_ENTER_DFU_MODE);
        CommandUtil.A_checkAdmin(transferData);
    }

    /**
         *
         *  @param operateType operation type 1 - get passcode status   2 - hide passcode 3 - show passcode
         *  @param lockData lock data
         */
    private void operatePasscodeVisibleMode(int operateType,LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SHOW_PASSWORD_ON_SCREEN);
        transferData.setCommand(Command.COMM_SHOW_PASSWORD);
        transferData.setOp(operateType);
        CommandUtil.A_checkAdmin(transferData);
    }

    private void unlockByAdmin(LockData keyData){
        long startDate = keyData.getStartDate();
        long endDate = keyData.getEndDate();
        TransferData transferData = getPreparedData(keyData);
        transferData.setAPICommand(APICommand.OP_UNLOCK_ADMIN);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        transferData.setmUid(keyData.getUid());
        CommandUtil.A_checkAdmin(transferData);
    }

    private void unlockByUser(LockData keyData){
        long startDate = keyData.getStartDate();
        long endDate = keyData.getEndDate();
        TransferData transferData = getPreparedData(keyData);
        transferData.setAPICommand(APICommand.OP_UNLOCK_EKEY);
        transferData.setCommand(Command.COMM_CHECK_USER_TIME);
        transferData.setmUid(keyData.getUid());
        transferData.setStartDate(startDate);
        transferData.setEndDate(endDate);
        CommandUtil.U_checkUserTime(transferData);
    }

    private void lockByUser(LockData keyData){
        long startDate = keyData.getStartDate();
        long endDate = keyData.getEndDate();
        TransferData transferData = getPreparedData(keyData);
        transferData.setAPICommand(APICommand.OP_LOCK_EKEY);
        transferData.setCommand(Command.COMM_CHECK_USER_TIME);
        transferData.setmUid(keyData.getUid());
        transferData.setStartDate(startDate);
        transferData.setEndDate(endDate);
        CommandUtil.U_checkUserTime(transferData);
    }

    //********************************卷闸门***测试未通过******************************
    /**
     *
     * @param controlAction the key witch is pressed
     * @param keyData   ekey data
     * @param operateType       1 - query 、 2 - key operation
     */
    private void operateRollingGate(int controlAction,LockData keyData,int operateType){
        long startDate = keyData.getStartDate();
        long endDate = keyData.getEndDate();

        TransferData transferData = getPreparedData(keyData);
        transferData.setAPICommand(APICommand.OP_REMOTE_CONTROL_DEVICE_MANAGEMENT);
        transferData.setOp(operateType);
        transferData.setOpValue(controlAction);
        transferData.setStartDate(startDate);
        transferData.setEndDate(endDate);

        if (BluetoothImpl.getInstance().isCheckedLockPermission && BluetoothImpl.getInstance().mConnectionState == BluetoothImpl.getInstance().STATE_CONNECTED) {
            Command command = new Command(LockType.LOCK_TYPE_V3);
            if (lastKey != controlAction) {
                BluetoothImpl.getInstance().uniqueidBytes = DigitUtil.integerToByteArray((int) (System.currentTimeMillis() / 1000));
            }

            if (BluetoothImpl.getInstance().unlockPwdBytes != null && BluetoothImpl.getInstance().uniqueidBytes != null){
                CommandUtil_V3.remoteControlManage(command, (byte)transferData.getOp(), (byte)transferData.getOpValue(), BluetoothImpl.getInstance().unlockPwdBytes, BluetoothImpl.getInstance().uniqueidBytes, transferData.getAesKeyArray());
            }
        } else if (BluetoothImpl.getInstance().isCanSendCommandAgain) {
            lastKey = controlAction;
            BluetoothImpl.getInstance().isCanSendCommandAgain = false;
            if (keyData.getUserType() == 1) {
                transferData.setCommand(Command.COMM_CHECK_ADMIN);
                CommandUtil.A_checkAdmin(transferData);
            } else {
                transferData.setCommand(Command.COMM_CHECK_USER_TIME);
                CommandUtil.U_checkUserTime(transferData);
            }
        }
    }

    public void getPassageMode(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_QUERY_PASSAGE_MODE);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void setPassageMode(PassageModeConfig config, LockData lockData){
        PassageModeType type = config.getModeType();
        String weekDays = config.getRepeatWeekOrDays();
//        if (type == PassageModeType.Weekly) {
//            weekDays = DigitUtil.convertWeekDays(weekDays);
//        }
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_ADD_OR_MODIFY_PASSAGE_MODE);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        transferData.setOp(type.getValue());
        transferData.setJson(weekDays);
        transferData.setOpValue(config.getMonth());
        transferData.setStartDate(config.getStartDate());
        transferData.setEndDate(config.getEndDate());
        CommandUtil.A_checkAdmin(transferData);
    }

    public void deletePassageMode(PassageModeConfig config,LockData lockData){
        PassageModeType type = config.getModeType();
        String weekDays = config.getRepeatWeekOrDays();
//        if (type == PassageModeType.Weekly) {
//            weekDays = DigitUtil.convertWeekDays(weekDays);
//        }
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_DELETE_PASSAGE_MODE);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        transferData.setOp(type.getValue());
        transferData.setJson(weekDays);
        transferData.setOpValue(config.getMonth());
        transferData.setStartDate(config.getStartDate());
        transferData.setEndDate(config.getEndDate());
        CommandUtil.A_checkAdmin(transferData);
    }

    public void clearPassageMode(LockData lockData){
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_CLEAR_PASSAGE_MODE);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void setLockFreezeState(boolean freeze, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_FREEZE_LOCK);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        transferData.setOp(OperationType.MODIFY);
        transferData.setOpValue(freeze ? 1 : 0);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getLockFreezeState(LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_FREEZE_LOCK);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        transferData.setOp(OperationType.GET_STATE);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void setLightTime(int seconds, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_LOCK_LAMP);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        transferData.setOp(OperationType.MODIFY);
        transferData.setOpValue(seconds);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getLightTime(LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_LOCK_LAMP);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        transferData.setOp(OperationType.GET_STATE);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void setHotelCardSector(HotelData hotelData, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setHotelData(hotelData);
        transferData.setAPICommand(APICommand.OP_SET_HOTEL_CARD_SECTION);
        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void setHotelData(HotelData hotelData, LockData lockData) {
//        hotelData.setParaType(HotelData.TYPE_HOTEL_BUILDING_FLOOR);
        TransferData transferData = getPreparedData(lockData);
        transferData.setHotelData(hotelData);
        transferData.setAPICommand(APICommand.OP_SET_HOTEL_DATA);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getHotelData(HotelData hotelData, LockData lockData) {
//        hotelData.setParaType(HotelData.TYPE_HOTEL_BUILDING_FLOOR);
        TransferData transferData = getPreparedData(lockData);
        transferData.setHotelData(hotelData);
        transferData.setAPICommand(APICommand.OP_GET_HOTEL_DATA);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void setLiftControlableFloors(HotelData hotelData, LockData lockData) {
        hotelData.setParaType(HotelData.TYPE_HOTEL_BUILDING_FLOOR);
        TransferData transferData = getPreparedData(lockData);
        transferData.setHotelData(hotelData);
        transferData.setAPICommand(APICommand.OP_SET_ELEVATOR_CONTROL_FLOORS);
//        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void setLiftWorkMode(HotelData hotelData, LockData lockData) {
        hotelData.setParaType(HotelData.TYPE_HOTEL_BUILDING_FLOOR);
        TransferData transferData = getPreparedData(lockData);
        transferData.setHotelData(hotelData);
        transferData.setAPICommand(APICommand.OP_SET_ELEVATOR_WORK_MODE);
//        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void activateLiftFloors(List<Integer> activateFloors, long currentDate, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_ACTIVATE_FLOORS);
        transferData.setActivateFloors(activateFloors);
        transferData.setUnlockDate(currentDate);
        if (!TextUtils.isEmpty(transferData.getAdminPs())) {
            CommandUtil.A_checkAdmin(transferData);
        } else {
            CommandUtil.U_checkUserTime(transferData);
        }
    }

    public void setLockConfig(TTLockConfigType ttLockConfigType, boolean enable, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SET_SWITCH);
        transferData.setOp(ttLockConfigType.getItem());
        if (ttLockConfigType == TTLockConfigType.WIFI_LOCK_POWER_SAVING_MODE) {//省电模式 这里 0 表示开启  跟其它配置项正好相反
            transferData.setOpValue(enable ? 0 : ttLockConfigType.getItem());
        } else {
            transferData.setOpValue(enable ? ttLockConfigType.getItem() : 0);
        }
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getLockConfig(TTLockConfigType ttLockConfigType, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_GET_SWITCH);
        transferData.setOp(ttLockConfigType.getItem());
        CommandUtil.A_checkAdmin(transferData);
    }

    private TransferData getPreparedData(LockData lockData){
        TransferData transferData = new TransferData();

        byte[] aesKeyArray = null;
        String aesKeyStr = lockData.getAesKeyStr();
        if(!TextUtils.isEmpty(aesKeyStr)) {
            aesKeyArray = DigitUtil.convertAesKeyStrToBytes(aesKeyStr);
        }
        transferData.setmUid(lockData.getUid());
        transferData.setLockVersion(GsonUtil.toJson(lockData.getLockVersion()));
        transferData.setAdminPs(lockData.getAdminPwd());
        transferData.setUnlockKey(lockData.getLockKey());
        transferData.setLockFlagPos(lockData.getLockFlagPos());
        transferData.setAesKeyArray(aesKeyArray);
        transferData.setTimezoneOffSet(lockData.getTimezoneRawOffset());
        return transferData;
    }

    //todo:
    private void setLockSwitch(LockData lockData) {

    }

    public void setNBAwakeModes(NBAwakeConfig nbAwakeConfig, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SET_NB_ACTIVATE_MODE);
        transferData.setNbAwakeConfig(nbAwakeConfig);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void setNBAwakeTimes(NBAwakeConfig nbAwakeConfig, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SET_NB_ACTIVATE_CONFIG);
        transferData.setNbAwakeConfig(nbAwakeConfig);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getNBAwakeModes(LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_GET_NB_ACTIVATE_MODE);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getNBAwakeTimes(LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_GET_NB_ACTIVATE_CONFIG);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void setUnlockDirection(UnlockDirection unlockDirection, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SET_UNLOCK_DIRECTION);
        transferData.setUnlockDirection(unlockDirection);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getUnlockDirection(LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_GET_UNLOCK_DIRECTION);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getAccessoryBatteryLevel(AccessoryInfo accessoryInfo, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAccessoryInfo(accessoryInfo);
        transferData.setAPICommand(APICommand.OP_GET_ACCESSORY_BATTERY);
        CommandUtil.getAccessoryBattery(transferData);
    }

    public void addRemote(String remoteMac, ValidityInfo validityInfo, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_ADD_KEY_FOB);
        transferData.setKeyFobMac(remoteMac);
        transferData.setValidityInfo(validityInfo);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void modifyRemoteValidity(String remoteMac, ValidityInfo validityInfo, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_MODIFY_KEY_FOB_PERIOD);
        transferData.setKeyFobMac(remoteMac);
        transferData.setValidityInfo(validityInfo);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void deleteRemote(String remoteMac, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_DELETE_KEY_FOB);
        transferData.setKeyFobMac(remoteMac);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void clearRemote(LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_CLEAR_KEY_FOB);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void scanWifi(LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SCAN_WIFI);
        CommandUtil.scanWifi(transferData);
    }

    public void configureWifiAp(String wifiName, String wifiPassword, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setWifiName(wifiName);
        transferData.setWifiPassword(wifiPassword);
        transferData.setAPICommand(APICommand.OP_SET_WIFI);
        CommandUtil.configureWifiAp(transferData);
    }

    public void configureServer(String serverAddress, int port, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAddress(serverAddress);
        transferData.setPort((short) port);
        transferData.setAPICommand(APICommand.OP_SET_SERVER);
        CommandUtil.setServer(transferData);
    }

    public void getWifiInfo(LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_GET_WIFI_INFO);
        CommandUtil.getWifiInfo(transferData);
    }

    public void configureStaticIp(IpSetting ipSetting, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SET_STATIC_IP);
        CommandUtil.configStaticIp(ipSetting, transferData);
    }

    public void setLockSound(SoundVolume soundVolume, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_SET_LOCK_SOUND);
        transferData.setSoundVolume(soundVolume);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void getLockSound(LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_GET_LOCK_SOUND);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void addDoorSensor(String doorSensorMac, LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_ADD_DOOR_SENSOR);
        transferData.setDoorSensorMac(doorSensorMac);
        CommandUtil.A_checkAdmin(transferData);
    }

    public void deleteDoorSensor(LockData lockData) {
        TransferData transferData = getPreparedData(lockData);
        transferData.setAPICommand(APICommand.OP_DELETE_DOOR_SENSOR);
        CommandUtil.A_checkAdmin(transferData);
    }

}
