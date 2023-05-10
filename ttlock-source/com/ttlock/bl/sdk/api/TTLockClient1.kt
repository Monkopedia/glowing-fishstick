package com.ttlock.bl.sdk.api

import android.Manifest
import com.ttlock.bl.sdk.callback.ConfigIpCallback
import com.ttlock.bl.sdk.callback.OperationType
import com.ttlock.bl.sdk.entity.ConnectParam
import java.lang.Exception
import java.util.ArrayList

/**
 * Created on  2019/4/11 0011 15:34
 *
 * @author theodre
 */
class TTLockClient private constructor() {
    private val mApi: TTLockSDKApi
    private var uid = 0

    /**
     *
     * Judging whether Bluetooth is available
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun isBLEEnabled(context: Context?): Boolean {
        return mApi.isBLEEnabled(context)
    }

    /**
     * Request to turn on Bluetooth
     * @param activity
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun requestBleEnable(activity: Activity?) {
        mApi.requestBleEnable(activity)
    }

    private object InstanceHolder {
        private val mInstance = TTLockClient()
    }

    /**
     * init the Bluetooth configuration
     * @param context
     */
    fun prepareBTService(context: Context?) {
        mApi.prepareBTService(context)
    }

    /**
     * stop the service to release Bluetooth resource
     */
    fun stopBTService() {
        mApi.stopBTService()
    }

    /**
     * start scan BT lock
     * @param callback
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun startScanLock(callback: ScanLockCallback?) {
        LockCallbackManager.Companion.getInstance().setLockScanCallback(callback)
        mApi.startScan()
    }

    /**
     * stop the scan
     */
    fun stopScanLock() {
        mApi.stopScan()
        LockCallbackManager.Companion.getInstance().clearScanCallback()
    }

    /**
     * init the lock
     * @param device
     * @param callback
     */
    fun initLock(device: ExtendedBluetoothDevice, callback: InitLockCallback) {
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.INIT_LOCK, callback)
        ) {
            val address = device.address
            if (ConnectManager.Companion.getInstance().isDeviceConnected(address)) {
                mApi.initLock(device)
            } else {
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(ConnectParam())
                ConnectManager.Companion.getInstance().connect2Device(device)
            }
        }
    }

    fun connectLock(lockData: String?, callback: ConnectLockCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.CONNECT_LOCK, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                callback.onConnectSuccess()
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * reset the ekey, and the lockFlagPos will change
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun resetEkey(lockData: String?, lockMac: String?, callback: ResetKeyCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        lockParam.lockFlagPos++
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.RESET_KEY, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.resetEkey(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * the lock will be reset to factory mode and if you want to use it,you should do initLock
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun resetLock(lockData: String?, lockMac: String?, callback: ResetLockCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.RESET_LOCK, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.resetLock(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun setUid(uid: Int) {
        this.uid = uid
    }

    /**
     * lock or unlock
     * @param controlAction (ControlAction.LOCK and ControlAction.UNLOCK)
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun controlLock(
        controlAction: Int,
        lockData: String?,
        lockMac: String?,
        callback: ControlLockCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        var isRollingGate = false
        if (controlAction == ControlAction.ROLLING_GATE_UP || controlAction == ControlAction.ROLLING_GATE_DOWN || controlAction == ControlAction.ROLLING_GATE_PAUSE || controlAction == ControlAction.ROLLING_GATE_LOCK) {
            isRollingGate = true
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.CONTROL_LOCK, callback, isRollingGate)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (lockParam.getUid() == 0 && uid != 0) {
                lockParam.setUid(uid)
                uid = 0
            }
            //            lockParam.lockMac = lockMac;
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setControlAction(controlAction)
                mApi.controlLock(controlAction, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.controlAction = controlAction
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * Get Audio Switch State
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun getMuteModeState(
        lockData: String?,
        lockMac: String?,
        callback: GetLockMuteModeStateCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_MUTE_MODE_STATE, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getMuteModeState(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    @Deprecated("")
    fun setMuteMode(
        enable: Boolean,
        lockData: String?,
        lockMac: String?,
        callback: SetLockMuteModeCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_MUTE_MODE_STATE, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockModeEnable(enable)
                mApi.setMuteMode(enable, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.isLockModeEnable = enable
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun setRemoteUnlockSwitchState(
        enable: Boolean,
        lockData: String?,
        lockMac: String?,
        callback: SetRemoteUnlockSwitchCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_REMOTE_UNLOCK_STATE, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockModeEnable(enable)
                mApi.setRemoteUnlockSwitchState(enable, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.isLockModeEnable = enable
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * Get remote unlock switch state(true – on, false – off)
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun getRemoteUnlockSwitchState(
        lockData: String?,
        lockMac: String?,
        callback: GetRemoteUnlockStateCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_REMOTE_UNLOCK_STATE, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getRemoteUnlockSwitchState(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * Set the lock time
     * @param timestamp
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun setLockTime(
        timestamp: Long,
        lockData: String?,
        lockMac: String?,
        callback: SetLockTimeCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_LOCK_TIME, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setTimestamp(timestamp)
                mApi.setLockTime(timestamp, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.timestamp = timestamp
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * get the lock time
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun getLockTime(lockData: String?, lockMac: String?, callback: GetLockTimeCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_LOCK_TIME, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getLockTime(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * get the lock log
     * @param logType ALL - all the operation record from lock is initialized. NEW - only the new added operation record from last time you get log.
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun getOperationLog(
        logType: Int,
        lockData: String?,
        lockMac: String?,
        callback: GetOperationLogCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_OPERATION_LOG, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLogType(logType)
                mApi.getOperationLog(logType, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.logType = logType
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * get lock battery
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun getBatteryLevel(lockData: String?, lockMac: String?, callback: GetBatteryLevelCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_ELECTRIC_QUALITY, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getBatteryLevel(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getLockVersion(lockMac: String, callback: GetLockVersionCallback) {
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_LOCK_VERSION, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockMac)) {
                mApi.getLockVersion()
            } else {
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(ConnectParam())
                ConnectManager.Companion.getInstance().connect2Device(lockMac)
            }
        }
    }

    @Deprecated("")
    fun getSpecialValue(lockData: String?, lockMac: String?, callback: GetSpecialValueCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_SPECIAL_VALUE, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getSpecialValue(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getLockSystemInfo(
        lockData: String?,
        lockMac: String?,
        callback: GetLockSystemInfoCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_SYSTEM_INFO, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getLockSystemInfo(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getLockStatus(lockData: String?, lockMac: String?, callback: GetLockStatusCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_LOCK_STATUS, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getLockStatus(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun setAutomaticLockingPeriod(
        seconds: Int,
        lockData: String?,
        lockMac: String?,
        callback: SetAutoLockingPeriodCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_AUTO_LOCK_PERIOD, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setAutoLockingPeriod(seconds)
                mApi.setAutomaticLockingPeriod(seconds, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.autoLockingPeriod = seconds
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getAutomaticLockingPeriod(lockData: String?, callback: GetAutoLockingPeriodCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_AUTO_LOCK_PERIOD, callback)
        ) {
//            没有特征值也会有自动闭锁时间
//            if(!isSupportThisOperation(lockParam)){
//                return;
//            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getAutomaticLockingPeriod(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun createCustomPasscode(
        passcode: String?,
        startDate: Long,
        endDate: Long,
        lockData: String?,
        lockMac: String?,
        callback: CreateCustomPasscodeCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.CREATE_CUSTOM_PASSCODE, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);

            //TODO:需要增加无线键盘的判断
//            if(!isSupportThisOperation(lockParam)){
//                return;
//            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                val param: ConnectParam =
                    ConnectManager.Companion.getInstance().getConnectParamForCallback()
                param.lockData = lockParam
                param.startDate = startDate
                param.endDate = endDate
                param.originalPasscode = passcode
                mApi.createCustomPasscode(passcode, startDate, endDate, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.startDate = startDate
                param.endDate = endDate
                param.originalPasscode = passcode
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun modifyPasscode(
        originalCode: String?,
        newCode: String?,
        startDate: Long,
        endDate: Long,
        lockData: String?,
        lockMac: String?,
        callback: ModifyPasscodeCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.MODIFY_PASSCODE, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
//                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                val param: ConnectParam =
                    ConnectManager.Companion.getInstance().getConnectParamForCallback()
                param.lockData = lockParam
                param.startDate = startDate
                param.endDate = endDate
                param.originalPasscode = originalCode
                param.newPasscode = newCode
                mApi.modifyPasscode(originalCode, newCode, startDate, endDate, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.startDate = startDate
                param.endDate = endDate
                param.originalPasscode = originalCode
                param.newPasscode = newCode
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun deletePasscode(
        passcode: String?,
        lockData: String?,
        lockMac: String?,
        callback: DeletePasscodeCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.DELETE_PASSCODE, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
//                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                val param: ConnectParam =
                    ConnectManager.Companion.getInstance().getConnectParamForCallback()
                param.lockData = lockParam
                param.originalPasscode = passcode
                mApi.deletePasscode(passcode, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.originalPasscode = passcode
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * reset passcode(all passcodes will be invalid)
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun resetPasscode(lockData: String?, lockMac: String?, callback: ResetPasscodeCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.RESET_PASSCODE, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.resetPasscode(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * get all valid passcodes in lock
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun getAllValidPasscodes(
        lockData: String?,
        lockMac: String?,
        callback: GetAllValidPasscodeCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_ALL_VALID_PASSCODES, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getAllValidPasscodes(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getPasscodeVerificationParams(
        lockData: String?,
        callback: GetPasscodeVerificationInfoCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_PASSCODE_INFO, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getPasscodeVerificationParams(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getAdminPasscode(lockData: String?, lockMac: String?, callback: GetAdminPasscodeCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_ADMIN_PASSCODE, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getAdminPasscode(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun modifyAdminPasscode(
        newPasscode: String?,
        lockData: String?,
        lockMac: String?,
        callback: ModifyAdminPasscodeCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.MODIFY_ADMIN_PASSCODE, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
//                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                val param: ConnectParam =
                    ConnectManager.Companion.getInstance().getConnectParamForCallback()
                param.lockData = lockParam
                param.newPasscode = newPasscode
                mApi.modifyAdminPasscode(newPasscode, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.newPasscode = newPasscode
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun addICCard(
        startDate: Long,
        endDate: Long,
        lockData: String?,
        lockMac: String?,
        callback: AddICCardCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.ADD_IC_CARD, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            val validityInfo = ValidityInfo()
            validityInfo.setModeType(ValidityInfo.Companion.TIMED)
            validityInfo.setStartDate(startDate)
            validityInfo.setEndDate(endDate)
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
//                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                val param: ConnectParam =
                    ConnectManager.Companion.getInstance().getConnectParamForCallback()
                param.lockData = lockParam
                param.validityInfo = validityInfo
                mApi.addICCard(validityInfo, lockParam)
                //                mApi.addICCard(startDate,endDate,lockParam);
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.validityInfo = validityInfo
                //                param.setStartDate(startDate);
//                param.setEndDate(endDate);
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun addICCard(validityInfo: ValidityInfo?, lockData: String?, callback: AddICCardCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || validityInfo == null || !validityInfo.isValidData()) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance().isDeviceBusy(
                if (validityInfo.getModeType() == ValidityInfo.Companion.CYCLIC) OperationType.ADD_CYCLIC_IC_CARD else OperationType.ADD_IC_CARD,
                callback
            )
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setValidityInfo(validityInfo)
                mApi.addICCard(validityInfo, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.validityInfo = validityInfo
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun modifyICCardValidityPeriod(
        startDate: Long,
        endDate: Long,
        cardNum: String?,
        lockData: String?,
        lockMac: String?,
        callback: ModifyICCardPeriodCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || TextUtils.isEmpty(cardNum)) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        val validityInfo = ValidityInfo()
        validityInfo.setModeType(ValidityInfo.Companion.TIMED)
        validityInfo.setStartDate(startDate)
        validityInfo.setEndDate(endDate)
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.MODIFY_IC_CARD_PERIOD, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
//                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                val param: ConnectParam =
                    ConnectManager.Companion.getInstance().getConnectParamForCallback()
                param.lockData = lockParam
                param.validityInfo = validityInfo
                param.attachmentNum = java.lang.Long.valueOf(cardNum)
                mApi.modifyICCardValidityPeriod(
                    validityInfo,
                    java.lang.Long.valueOf(cardNum),
                    lockParam
                )
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.validityInfo = validityInfo
                param.attachmentNum = java.lang.Long.valueOf(cardNum)
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun modifyICCardValidityPeriod(
        validityInfo: ValidityInfo?,
        cardNum: String?,
        lockData: String?,
        callback: ModifyICCardPeriodCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || TextUtils.isEmpty(cardNum)) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.MODIFY_IC_CARD_PERIOD, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
//                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                val param: ConnectParam =
                    ConnectManager.Companion.getInstance().getConnectParamForCallback()
                param.lockData = lockParam
                param.validityInfo = validityInfo
                param.attachmentNum = java.lang.Long.valueOf(cardNum)
                mApi.modifyICCardValidityPeriod(
                    validityInfo,
                    java.lang.Long.valueOf(cardNum),
                    lockParam
                )
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.validityInfo = validityInfo
                param.attachmentNum = java.lang.Long.valueOf(cardNum)
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * get all valid CARD Cards
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun getAllValidICCards(
        lockData: String?,
        lockMac: String?,
        callback: GetAllValidICCardCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_ALL_IC_CARDS, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getAllValidICCards(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun deleteICCard(
        cardNum: String?,
        lockData: String?,
        lockMac: String?,
        callback: DeleteICCardCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || TextUtils.isEmpty(cardNum)) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.DELETE_IC_CARD, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setAttachmentNum(java.lang.Long.valueOf(cardNum))
                mApi.deleteICCard(java.lang.Long.valueOf(cardNum), lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.attachmentNum = java.lang.Long.valueOf(cardNum)
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun reportLossCard(cardNum: String?, lockData: String?, callback: ReportLossCardCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || TextUtils.isEmpty(cardNum)) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.LOSS_CARD, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setAttachmentNum(java.lang.Long.valueOf(cardNum))
                mApi.lossICCard(java.lang.Long.valueOf(cardNum), lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.attachmentNum = java.lang.Long.valueOf(cardNum)
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * clear ic cards
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun clearAllICCard(lockData: String?, lockMac: String?, callback: ClearAllICCardCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.CLEAR_ALL_IC_CARD, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.clearAllICCard(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun addFingerprint(
        startDate: Long,
        endDate: Long,
        lockData: String?,
        lockMac: String?,
        callback: AddFingerprintCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        val validityInfo = ValidityInfo()
        validityInfo.setModeType(ValidityInfo.Companion.TIMED)
        validityInfo.setStartDate(startDate)
        validityInfo.setEndDate(endDate)
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.ADD_FINGERPRINT, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setValidityInfo(validityInfo)
                mApi.addFingerprint(validityInfo, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.validityInfo = validityInfo
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun addFingerprint(
        validityInfo: ValidityInfo?,
        lockData: String?,
        callback: AddFingerprintCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || validityInfo == null || !validityInfo.isValidData()) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance().isDeviceBusy(
                if (validityInfo.getModeType() == ValidityInfo.Companion.CYCLIC) OperationType.ADD_CYCLIC_FINGERPRINT else OperationType.ADD_FINGERPRINT,
                callback
            )
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setValidityInfo(validityInfo)
                mApi.addFingerprint(validityInfo, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.validityInfo = validityInfo
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * get all valid fingerprints
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun getAllValidFingerprints(
        lockData: String?,
        lockMac: String?,
        callback: GetAllValidFingerprintCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_ALL_FINGERPRINTS, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getAllValidFingerprints(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun deleteFingerprint(
        fingerprintNum: String?,
        lockData: String?,
        lockMac: String?,
        callback: DeleteFingerprintCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || TextUtils.isEmpty(fingerprintNum)) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.DELETE_FINGERPRINT, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setAttachmentNum(java.lang.Long.valueOf(fingerprintNum))
                mApi.deleteFingerprint(java.lang.Long.valueOf(fingerprintNum), lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.attachmentNum = java.lang.Long.valueOf(fingerprintNum)
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * clear all fingerprints
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun clearAllFingerprints(
        lockData: String?,
        lockMac: String?,
        callback: ClearAllFingerprintCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.CLEAR_ALL_FINGERPRINTS, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.clearAllFingerprints(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun modifyFingerprintValidityPeriod(
        startDate: Long,
        endDate: Long,
        fingerprintNum: String?,
        lockData: String?,
        lockMac: String?,
        callback: ModifyFingerprintPeriodCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || TextUtils.isEmpty(fingerprintNum)) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        val validityInfo = ValidityInfo()
        validityInfo.setModeType(ValidityInfo.Companion.TIMED)
        validityInfo.setStartDate(startDate)
        validityInfo.setEndDate(endDate)
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.MODIFY_FINGEPRINT_PERIOD, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setValidityInfo(validityInfo)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setAttachmentNum(java.lang.Long.valueOf(fingerprintNum))
                mApi.modifyFingerprintValidityPeriod(
                    validityInfo,
                    java.lang.Long.valueOf(fingerprintNum),
                    lockParam
                )
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.validityInfo = validityInfo
                param.attachmentNum = java.lang.Long.valueOf(fingerprintNum)
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun modifyFingerprintValidityPeriod(
        validityInfo: ValidityInfo?,
        fingerprintNum: String?,
        lockData: String?,
        callback: ModifyFingerprintPeriodCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || TextUtils.isEmpty(fingerprintNum)) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.MODIFY_FINGEPRINT_PERIOD, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setValidityInfo(validityInfo)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setAttachmentNum(java.lang.Long.valueOf(fingerprintNum))
                mApi.modifyFingerprintValidityPeriod(
                    validityInfo,
                    java.lang.Long.valueOf(fingerprintNum),
                    lockParam
                )
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.validityInfo = validityInfo
                param.attachmentNum = java.lang.Long.valueOf(fingerprintNum)
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun enterDfuMode(lockData: String?, lockMac: String?, callback: EnterDfuModeCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.ENTER_DFU_MODE, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.enterDfuMode(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun setLockConfig(
        ttLockConfigType: TTLockConfigType,
        switchOn: Boolean,
        lockData: String?,
        callback: SetLockConfigCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_LOCK_CONFIG, callback)
        ) {
            //todo:支持判断
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setTtLockConfigType(ttLockConfigType)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockModeEnable(switchOn)
                when (ttLockConfigType) {
                    TTLockConfigType.LOCK_SOUND -> mApi.setMuteMode(!switchOn, lockParam)
                    TTLockConfigType.PASSCODE_VISIBLE -> mApi.setPasscodeVisibleSwitchState(
                        switchOn,
                        lockParam
                    )
                    TTLockConfigType.LOCK_FREEZE -> mApi.setLockFreezeState(switchOn, lockParam)
                    TTLockConfigType.PRIVACY_LOCK, TTLockConfigType.TAMPER_ALERT, TTLockConfigType.RESET_BUTTON, TTLockConfigType.PASSAGE_MODE_AUTO_UNLOCK_SETTING, TTLockConfigType.WIFI_LOCK_POWER_SAVING_MODE -> mApi.setLockConfig(
                        ttLockConfigType,
                        switchOn,
                        lockParam
                    )
                }
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.ttLockConfigType = ttLockConfigType
                param.isLockModeEnable = switchOn
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getLockConfig(
        ttLockConfigType: TTLockConfigType?,
        lockData: String?,
        callback: GetLockConfigCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || ttLockConfigType == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_LOCK_CONFIG, callback)
        ) {
            //todo:支持判断
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setTtLockConfigType(ttLockConfigType)
                when (ttLockConfigType) {
                    TTLockConfigType.LOCK_SOUND -> mApi.getMuteModeState(lockParam)
                    TTLockConfigType.PASSCODE_VISIBLE -> mApi.getPasscodeVisibleSwitchState(
                        lockParam
                    )
                    TTLockConfigType.LOCK_FREEZE -> mApi.getLockFreezeState(lockParam)
                    TTLockConfigType.PRIVACY_LOCK, TTLockConfigType.TAMPER_ALERT, TTLockConfigType.RESET_BUTTON, TTLockConfigType.WIFI_LOCK_POWER_SAVING_MODE -> mApi.getLockConfig(
                        ttLockConfigType,
                        lockParam
                    )
                }
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.ttLockConfigType = ttLockConfigType
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * **********************未测试***开始*********************
     */
    fun writeFingerprintData(
        fingerprintData: String?,
        tempFingerprintNumber: Int,
        startDate: Long,
        endDate: Long,
        lockData: String?,
        lockMac: String?,
        callback: WriteFingerprintDataCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.WRITE_FINGERPRINT_DATA, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
//                ConnectManager.getInstance().getConnectParamForCallback().setLockData(lockParam);
                val param: ConnectParam =
                    ConnectManager.Companion.getInstance().getConnectParamForCallback()
                param.lockData = lockParam
                param.startDate = startDate
                param.endDate = endDate
                param.dataJsonStr = fingerprintData
                param.attachmentNum = tempFingerprintNumber.toLong()
                mApi.writeFingerprintData(
                    fingerprintData,
                    tempFingerprintNumber,
                    startDate,
                    endDate,
                    lockParam
                )
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.startDate = startDate
                param.endDate = endDate
                param.dataJsonStr = fingerprintData
                param.attachmentNum = tempFingerprintNumber.toLong()
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun recoverLockData(
        dataJson: String?,
        dataType: Int,
        lockData: String?,
        lockMac: String?,
        callback: RecoverLockDataCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || TextUtils.isEmpty(dataJson)) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.RECOVERY_DATA, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setRecoveryDataStr(dataJson)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setRecoveryDataType(dataType)
                mApi.recoverLockData(dataJson, dataType, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.recoveryDataStr = dataJson
                param.recoveryDataType = dataType
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getPasscodeVisibleSwitchState(
        lockData: String?,
        lockMac: String?,
        callback: GetPasscodeVisibleStateCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_PASSCODE_VISIBLE_STATE, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getRemoteUnlockSwitchState(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    @Deprecated("")
    fun setPasscodeVisibleSwitchState(
        enable: Boolean,
        lockData: String?,
        lockMac: String?,
        callback: SetPasscodeVisibleCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_PASSCODE_VISIBLE_STATE, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockModeEnable(enable)
                mApi.setPasscodeVisibleSwitchState(enable, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.isLockModeEnable = enable
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    init {
        mApi = TTLockSDKApi()
    }

    fun setNBServerInfo(
        portNumber: Short,
        serverAddress: String?,
        lockData: String,
        callback: SetNBServerCallback
    ) {
        var lockParam: LockData? = null
        try {
            if (!TextUtils.isEmpty(lockData) && lockData.contains(SPECIAL_VALUE_KEY)) {
                lockParam = GsonUtil.toObject<LockData>(lockData, LockData::class.java)
            } else {
                lockParam = EncryptionUtil.parseLockData(lockData)
                if (lockParam == null) {
                    callback.onFail(LockError.DATA_FORMAT_ERROR)
                    return
                }
                //                lockParam = GsonUtil.toObject(lockData, LockData.class);
            }
        } catch (e: IllegalStateException) {
        }
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_NB_SERVER, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setServerAddress(serverAddress)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setServerPort(portNumber)
                mApi.setNBServerInfo(portNumber, serverAddress, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.serverAddress = serverAddress
                param.serverPort = portNumber
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }
    /****************未测试 结束 */
    /**
     * get passage mode
     * @param lockData
     * @param lockMac
     * @param callback
     */
    fun getPassageMode(lockData: String?, lockMac: String?, callback: GetPassageModeCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_PASSAGE_MODE, callback)
        ) {
//            LockData lockParam = GsonUtil.toObject(decodeData, LockData.class);
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getPassageMode(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun setPassageMode(
        modeData: PassageModeConfig?,
        lockData: String?,
        lockMac: String?,
        callback: SetPassageModeCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || modeData == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_PASSAGE_MODE, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setPassageModeConfig(modeData)
                mApi.setPassageMode(modeData, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.passageModeConfig = modeData
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun deletePassageMode(
        modeData: PassageModeConfig?,
        lockData: String?,
        lockMac: String?,
        callback: DeletePassageModeCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || modeData == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.DELETE_PASSAGE_MODE, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setPassageModeConfig(modeData)
                mApi.deletePassageMode(modeData, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.passageModeConfig = modeData
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun clearPassageMode(lockData: String?, lockMac: String?, callback: ClearPassageModeCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.CLEAR_PASSAGE_MODE, callback)
        ) {
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.clearPassageMode(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * @param isOn                          true - freeze，false - unfreeze
     */
    @Deprecated("")
    fun setLockFreezeState(isOn: Boolean, lockData: String?, callback: SetLockFreezeStateCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_LOCK_FREEZE_STATE, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockModeEnable(isOn)
                mApi.setLockFreezeState(isOn, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.isLockModeEnable = isOn
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getLockFreezeState(lockData: String?, callback: GetLockFreezeStateCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_LOCK_FREEZE_STATE, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getLockFreezeState(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun setLightTime(seconds: Int, lockData: String?, callback: SetLightTimeCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_LIGHT_TIME, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setSeconds(seconds)
                mApi.setLightTime(seconds, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.seconds = seconds
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getLightTime(lockData: String?, callback: GetLockTimeCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_LIGHT_TIME, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getLightTime(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * 设置写入扇区(跟锁内保持一致)
     * @param sectorStr  从1开始计数  数据格式 "1,2,3" 表示写入扇区1、2、3
     * @param lockData
     * @param callback
     */
    fun setHotelCardSector(
        sectorStr: String?,
        lockData: String?,
        callback: SetHotelCardSectorCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_HOTEL_CARD_SECTION, callback)
        ) {
            val hotelData = HotelData()
            hotelData.sector = DigitUtil.calSectorValue(sectorStr)
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setHotelData(hotelData)
                mApi.setHotelCardSector(hotelData, lockParam)
            } else {
                val param = ConnectParam()
                param.hotelData = hotelData
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * 设置的保留扇区 跟锁内是相反的 扇区从0开始计算
     * @param sectors
     * @param lockData
     * @param callback
     */
    @Deprecated("")
    fun setHotelCardSector(
        sectors: ArrayList<Int?>?,
        lockData: String?,
        callback: SetHotelCardSectorCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_HOTEL_CARD_SECTION, callback)
        ) {
            val hotelData = HotelData()
            hotelData.sector = DigitUtil.calSectorValue(sectors)
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setHotelData(hotelData)
                mApi.setHotelCardSector(hotelData, lockParam)
            } else {
                val param = ConnectParam()
                param.hotelData = hotelData
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * 目前只用来修改icKey, aesKey, hotelNumber, buildingNumber, floorNumber
     * @param hotelData  hotelData.setHotelInfo(hotelInfo);hotelInfo从服务端获取
     * hotelData.setBuildingNumber(buildingNumber);
     * hotelData.setFloorNumber(floorNumber);
     * @param lockData
     * @param callback
     */
    fun setHotelData(hotelData: HotelData?, lockData: String?, callback: SetHotelDataCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || hotelData == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_HOTEL_DATA, callback)
        ) {
            //TODO:
//            if(!isSupportThisOperation(lockParam)){
//                return;
//            }
            try {
                val hotelInfo: HotelInfo = DigitUtil.decodeHotelInfo(hotelData.hotelInfo)
                hotelData.icKey = hotelInfo.icKey
                hotelData.aesKey = hotelInfo.aesKey
                hotelData.hotelNumber = hotelInfo.hotelNumber
            } catch (exception: Exception) {
                LogUtil.w("exception:$exception")
                callback.onFail(LockError.DATA_FORMAT_ERROR)
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setHotelData(hotelData)
                mApi.setHotelData(hotelData, lockParam)
            } else {
                val param = ConnectParam()
                param.hotelData = hotelData
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * 设置梯控的关联楼层
     * @param controlableFloors 数据格式 "1,2,3" 表示关联楼层1、2、3
     * @param lockData
     * @param callback
     */
    fun setLiftControlableFloors(
        controlableFloors: String?,
        lockData: String?,
        callback: SetLiftControlableFloorsCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || TextUtils.isEmpty(controlableFloors)) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        val hotelData = HotelData() //todo:抛出异常
        hotelData.controlableFloors = DigitUtil.getControlableFloors(controlableFloors)
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_ELEVATOR_CONTROLABLE_FLOORS, callback)
        ) {
            //TODO:
//            if(!isSupportThisOperation(lockParam)){
//                return;
//            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setHotelData(hotelData)
                mApi.setLiftControlableFloors(hotelData, lockParam)
            } else {
                val param = ConnectParam()
                param.hotelData = hotelData
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * 设置梯控的工作模式
     * @param ttLiftWorkMode
     * @param lockData
     * @param callback
     */
    fun setLiftWorkMode(
        ttLiftWorkMode: TTLiftWorkMode?,
        lockData: String?,
        callback: SetLiftWorkModeCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || ttLiftWorkMode == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        val hotelData = HotelData()
        hotelData.ttLiftWorkMode = ttLiftWorkMode
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_ELEVATOR_WORK_MODE, callback)
        ) {
            //TODO:
//            if(!isSupportThisOperation(lockParam)){
//                return;
//            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setHotelData(hotelData)
                mApi.setLiftWorkMode(hotelData, lockParam)
            } else {
                val param = ConnectParam()
                param.hotelData = hotelData
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun activateLiftFloors(
        activateFloors: List<Int?>?,
        currentDate: Long,
        lockData: String?,
        callback: ActivateLiftFloorsCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || activateFloors == null || activateFloors.size == 0) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.ACTIVATE_LIFT_FLOORS, callback)
        ) {
//            if(!isSupportThisOperation(lockParam)){
//                return;
//            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setActivateFloors(activateFloors)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setTimestamp(currentDate)
                mApi.activateLiftFloors(activateFloors, currentDate, lockParam)
            } else {
                val param = ConnectParam()
                param.activateFloors = activateFloors
                param.timestamp = currentDate
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun setNBAwakeModes(
        nbAwakeModeList: List<NBAwakeMode?>?,
        lockData: String?,
        callback: SetNBAwakeModesCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || nbAwakeModeList == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        val nbAwakeConfig = NBAwakeConfig()
        nbAwakeConfig.setNbAwakeModeList(nbAwakeModeList)
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_NB_ACTIVATE_MODE, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setNbAwakeConfig(nbAwakeConfig)
                mApi.setNBAwakeModes(nbAwakeConfig, lockParam)
            } else {
                val param = ConnectParam()
                param.nbAwakeConfig = nbAwakeConfig
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getNBAwakeModes(lockData: String?, callback: GetNBAwakeModesCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_NB_ACITATE_MODE, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getNBAwakeModes(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun setNBAwakeTimes(
        nbAwakeTimeList: List<NBAwakeTime?>?,
        lockData: String?,
        callback: SetNBAwakeTimesCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || nbAwakeTimeList == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        val nbAwakeConfig = NBAwakeConfig()
        nbAwakeConfig.setNbAwakeTimeList(nbAwakeTimeList)
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_NB_ACTIVATE_CONFIG, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setNbAwakeConfig(nbAwakeConfig)
                mApi.setNBAwakeTimes(nbAwakeConfig, lockParam)
            } else {
                val param = ConnectParam()
                param.nbAwakeConfig = nbAwakeConfig
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getNBAwakeTimes(lockData: String?, callback: GetNBAwakeTimesCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_NB_ACTIVATE_CONFIG, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getNBAwakeTimes(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun setPowerSaverWorkMode(
        powerSaverWorkMode: PowerSaverWorkMode?,
        lockData: String,
        callback: SetPowerSaverWorkModeCallback
    ) {
        val powerWorkModeList: MutableList<PowerSaverWorkMode> = ArrayList<PowerSaverWorkMode>()
        if (powerSaverWorkMode != null) {
            powerWorkModeList.add(powerSaverWorkMode)
        }
        setPowerSaverWorkModes(powerWorkModeList, lockData, callback)
    }

    /**
     *
     * @param powerWorkModeList null or size 0
     * @param lockData
     * @param callback
     */
    private fun setPowerSaverWorkModes(
        powerWorkModeList: List<PowerSaverWorkMode>,
        lockData: String,
        callback: SetPowerSaverWorkModeCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        val hotelData = HotelData()
        hotelData.paraType = HotelData.Companion.TYPE_POWER_SAVER_WORK_MODE
        hotelData.powerWorkModeValue = DigitUtil.getPowerWorkModeValue(powerWorkModeList)
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_HOTEL_DATA, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setHotelData(hotelData)
                mApi.setHotelData(hotelData, lockParam)
            } else {
                val param = ConnectParam()
                param.hotelData = hotelData
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    private fun getPowerSaverWorkModes(lockData: String, callback: GetPowerSaverWorkModesCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        val hotelData = HotelData()
        hotelData.paraType = HotelData.Companion.TYPE_POWER_SAVER_WORK_MODE
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_HOTEL_DATA, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getHotelData(hotelData, lockParam)
            } else {
                val param = ConnectParam()
                param.hotelData = hotelData
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     *
     * @param controlableLockMac null or "" means discontrolable
     * @param lockData
     * @param callback
     */
    fun setPowerSaverControlableLock(
        controlableLockMac: String?,
        lockData: String?,
        callback: SetPowerSaverControlableLockCallback
    ) {
        var controlableLockMac = controlableLockMac
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) { //todo:判断mac格式
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (TextUtils.isEmpty(controlableLockMac)) { //取电取消关联锁的时候，可以把关联的mac设成取电自己本身
            controlableLockMac = lockParam.getLockMac()
        }
        val hotelData = HotelData()
        hotelData.setControlableLockMac(controlableLockMac)
        hotelData.paraType = HotelData.Companion.TYPE_POWER_SAVER_CONTROLABLE_LOCK
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_HOTEL_DATA, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setHotelData(hotelData)
                mApi.setHotelData(hotelData, lockParam)
            } else {
                val param = ConnectParam()
                param.hotelData = hotelData
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getUnlockDirection(lockData: String?, callback: GetUnlockDirectionCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_UNLOCK_DIRECTION, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getUnlockDirection(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun setUnlockDirection(
        unlockDirection: UnlockDirection?,
        lockData: String?,
        callback: SetUnlockDirectionCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_UNLOCK_DIRECTION, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setUnlockDirection(unlockDirection)
                mApi.setUnlockDirection(unlockDirection, lockParam)
            } else {
                val param = ConnectParam()
                param.unlockDirection = unlockDirection
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getAccessoryBatteryLevel(
        accessoryInfo: AccessoryInfo?,
        lockData: String?,
        callback: GetAccessoryBatteryLevelCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || accessoryInfo == null) { //todo:判断mac格式
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_ACCESSORY_BATTERY, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setAccessoryInfo(accessoryInfo)
                mApi.getAccessoryBatteryLevel(accessoryInfo, lockParam)
            } else {
                val param = ConnectParam()
                param.accessoryInfo = accessoryInfo
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun addRemote(
        keyfobMac: String?,
        validityInfo: ValidityInfo?,
        lockData: String?,
        callback: AddRemoteCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || TextUtils.isEmpty(keyfobMac)) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.ADD_KEY_FOB, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setKeyfobMac(keyfobMac)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setValidityInfo(validityInfo)
                mApi.addRemote(keyfobMac, validityInfo, lockParam)
            } else {
                val param = ConnectParam()
                param.keyfobMac = keyfobMac
                param.validityInfo = validityInfo
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun modifyRemoteValidityPeriod(
        keyfobMac: String?,
        validityInfo: ValidityInfo?,
        lockData: String?,
        callback: ModifyRemoteValidityPeriodCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || TextUtils.isEmpty(keyfobMac)) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.UPDATE_KEY_FOB_VALIDITY, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setKeyfobMac(keyfobMac)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setValidityInfo(validityInfo)
                mApi.modifyRemoteValidity(keyfobMac, validityInfo, lockParam)
            } else {
                val param = ConnectParam()
                param.keyfobMac = keyfobMac
                param.validityInfo = validityInfo
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun deleteRemote(keyfobMac: String?, lockData: String?, callback: DeleteRemoteCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || TextUtils.isEmpty(keyfobMac)) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.DELETE_KEY_FOB, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setKeyfobMac(keyfobMac)
                mApi.deleteRemote(keyfobMac, lockParam)
            } else {
                val param = ConnectParam()
                param.keyfobMac = keyfobMac
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun clearRemote(lockData: String?, callback: ClearRemoteCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.CLEAR_KEY_FOB, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.clearRemote(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * 扫描wifi锁附近的wifi
     * @param lockData
     * @param callback
     */
    fun scanWifi(lockData: String?, callback: ScanWifiCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SCAN_WIFI, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.scanWifi(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun configWifi(
        wifiName: String?,
        wifiPassword: String?,
        lockData: String?,
        callback: ConfigWifiCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || TextUtils.isEmpty(wifiName)) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.CONFIGURE_WIFI_AP, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setWifiName(wifiName)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setWifiPassword(wifiPassword)
                mApi.configureWifiAp(wifiName, wifiPassword, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.wifiName = wifiName
                param.wifiPassword = wifiPassword
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
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
    fun configServer(
        serverAddress: String?,
        portNumber: Int,
        lockData: String?,
        callback: ConfigServerCallback
    ) {
        var serverAddress = serverAddress
        var portNumber = portNumber
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (TextUtils.isEmpty(serverAddress) || portNumber == 0) {
            serverAddress = "wifilock.ttlock.com"
            portNumber = 4999
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.CONFIGURE_SERVER, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setServerAddress(serverAddress)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setServerPort(portNumber.toShort())
                mApi.configureServer(serverAddress, portNumber, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.serverAddress = serverAddress
                param.serverPort = portNumber.toShort()
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * 获取wifi锁的 wifi mac
     * @param lockData
     * @param callback
     */
    fun getWifiInfo(lockData: String?, callback: GetWifiInfoCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_WIFI_INFO, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getWifiInfo(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * wifi锁配置静态ip
     * @param ipSetting
     * @param lockData
     * @param callback
     */
    fun configIp(ipSetting: IpSetting?, lockData: String?, callback: ConfigIpCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || ipSetting == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.CONFIGURE_STATIC_IP, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setIpSetting(ipSetting)
                mApi.configureStaticIp(ipSetting, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.ipSetting = ipSetting
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun setLockSoundWithSoundVolume(
        soundVolume: SoundVolume?,
        lockData: String?,
        callback: SetLockSoundWithSoundVolumeCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null || soundVolume == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.SET_LOCK_SOUND_VOLUME, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setSoundVolume(soundVolume)
                mApi.setLockSound(soundVolume, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.soundVolume = soundVolume
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun getLockSoundWithSoundVolume(
        lockData: String?,
        callback: GetLockSoundWithSoundVolumeCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.GET_LOCK_SOUND_VOLUME, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.getLockSound(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * 添加门磁(将门磁mac写入锁内)
     * @param doorSensorMac  门磁mac地址
     * @param lockData
     * @param callback
     */
    fun addDoorSensor(doorSensorMac: String?, lockData: String?, callback: AddDoorSensorCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.ADD_DOOR_SENSOR, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setDoorSensorMac(doorSensorMac)
                mApi.addDoorSensor(doorSensorMac, lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                param.doorSensorMac = doorSensorMac
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    /**
     * 删除锁关联的门磁
     * @param lockData
     * @param callback
     */
    fun deleteDoorSensor(lockData: String?, callback: DeleteDoorSensorCallback) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(LockError.DATA_FORMAT_ERROR)
            return
        }
        if (!LockCallbackManager.Companion.getInstance()
                .isDeviceBusy(OperationType.DELETE_DOOR_SENSOR, callback)
        ) {
            if (!isSupportThisOperation(lockParam)) {
                return
            }
            if (ConnectManager.Companion.getInstance().isDeviceConnected(lockParam.lockMac)) {
                ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
                    .setLockData(lockParam)
                mApi.deleteDoorSensor(lockParam)
            } else {
                val param = ConnectParam()
                param.lockData = lockParam
                ConnectManager.Companion.getInstance().storeConnectParamForCallback(param)
                ConnectManager.Companion.getInstance().connect2Device(lockParam.lockMac)
            }
        }
    }

    fun disconnect() {
        ConnectManager.Companion.getInstance().disconnect()
    }

    fun clearAllCallback() {
        LockCallbackManager.Companion.getInstance().clearAllCallback()
    }

    fun getSdkLog(): String {
        return BluetoothImpl.Companion.getInstance().getSdkLog()
    }

    private fun isSupportThisOperation(lockData: LockData?): Boolean {
        val isSupport = isSupportOperation(
            LockCallbackManager.Companion.getInstance().getOperationType(),
            lockData
        )
        if (!isSupport) {
            val failCallback: LockCallback =
                LockCallbackManager.Companion.getInstance().getCallback()
            if (failCallback != null) {
                failCallback.onFail(LockError.LOCK_IS_NOT_SUPPORT)
            }
        }
        return isSupport
    }

    companion object {
        fun getDefault(): TTLockClient {
            return InstanceHolder.mInstance
        }

        private const val SPECIAL_VALUE_KEY = "specialValue"
        private fun isSupportOperation(callbackType: Int, lockData: LockData): Boolean {
            var feature = 1
            when (callbackType) {
                OperationType.GET_ADMIN_PASSCODE -> feature = FeatureValue.GET_ADMIN_CODE
                OperationType.GET_MUTE_MODE_STATE, OperationType.SET_MUTE_MODE_STATE -> feature =
                    FeatureValue.AUDIO_MANAGEMENT
                OperationType.SET_AUTO_LOCK_PERIOD -> feature = FeatureValue.AUTO_LOCK.toInt()
                OperationType.GET_REMOTE_UNLOCK_STATE, OperationType.SET_REMOTE_UNLOCK_STATE -> feature =
                    FeatureValue.CONFIG_GATEWAY_UNLOCK
                OperationType.MODIFY_PASSCODE -> feature = FeatureValue.MODIFY_PASSCODE_FUNCTION
                OperationType.ADD_FINGERPRINT -> feature = FeatureValue.FINGER_PRINT.toInt()
                OperationType.ADD_IC_CARD -> feature = FeatureValue.IC.toInt()
                OperationType.CREATE_CUSTOM_PASSCODE -> feature = FeatureValue.PASSCODE.toInt()
                OperationType.GET_PASSCODE_VISIBLE_STATE, OperationType.SET_PASSCODE_VISIBLE_STATE -> feature =
                    FeatureValue.PASSWORD_DISPLAY_OR_HIDE
                OperationType.SET_NB_SERVER -> feature = FeatureValue.NB_LOCK
                OperationType.GET_PASSAGE_MODE, OperationType.SET_PASSAGE_MODE -> feature =
                    FeatureValue.PASSAGE_MODE
                OperationType.GET_LOCK_FREEZE_STATE, OperationType.SET_LOCK_FREEZE_STATE -> feature =
                    FeatureValue.FREEZE_LOCK
                OperationType.GET_LIGHT_TIME, OperationType.SET_LIGHT_TIME -> feature =
                    FeatureValue.LAMP
                OperationType.ADD_CYCLIC_IC_CARD, OperationType.ADD_CYCLIC_FINGERPRINT -> feature =
                    FeatureValue.CYCLIC_IC_OR_FINGER_PRINT
                OperationType.SET_NB_ACTIVATE_CONFIG, OperationType.GET_NB_ACTIVATE_CONFIG, OperationType.SET_NB_ACTIVATE_MODE, OperationType.GET_NB_ACITATE_MODE -> feature =
                    FeatureValue.NB_ACTIVITE_CONFIGURATION
                OperationType.SET_UNLOCK_DIRECTION, OperationType.GET_UNLOCK_DIRECTION -> feature =
                    FeatureValue.UNLOCK_DIRECTION
                OperationType.GET_ACCESSORY_BATTERY -> feature = FeatureValue.ACCESSORY_BATTERY
                OperationType.ADD_KEY_FOB, OperationType.UPDATE_KEY_FOB_VALIDITY, OperationType.DELETE_KEY_FOB, OperationType.CLEAR_KEY_FOB -> feature =
                    FeatureValue.WIRELESS_KEY_FOB
                OperationType.SCAN_WIFI, OperationType.CONFIGURE_WIFI_AP, OperationType.CONFIGURE_SERVER, OperationType.GET_WIFI_INFO -> feature =
                    FeatureValue.WIFI_LOCK
                OperationType.CONFIGURE_STATIC_IP -> feature =
                    FeatureValue.WIFI_LOCK_SUPPORT_STATIC_IP
                OperationType.SET_LOCK_SOUND_VOLUME, OperationType.GET_LOCK_SOUND_VOLUME -> feature =
                    FeatureValue.SOUND_VOLUME_AND_LANGUAGE_SETTING
                OperationType.ADD_DOOR_SENSOR, OperationType.DELETE_DOOR_SENSOR -> feature =
                    FeatureValue.DOOR_SENSOR
                else -> {}
            }
            return FeatureValueUtil.isSupportFeature(lockData, feature)
        } //    private static boolean isSupportOperation(int callbackType,int specialValue){
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
}