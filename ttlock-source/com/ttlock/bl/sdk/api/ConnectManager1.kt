package com.ttlock.bl.sdk.api

import android.os.Handler
import com.ttlock.bl.sdk.callback.ConnectCallback
import com.ttlock.bl.sdk.callback.OperationType
import com.ttlock.bl.sdk.entity.ConnectParam

/**
 * Created on  2019/4/12 SetLockTimeCallback 11:30
 * @author theodore
 */
internal class ConnectManager private constructor() : ConnectCallback {
    private val mApi: TTLockSDKApi
    private var mConnectParam: ConnectParam?
    private val mDataCheckHandler: Handler = Handler()
    private var mCurrentMac: String? = null
    private val mDataCheckErrorRunnable = Runnable {
        val mCallback: LockCallback = LockCallbackManager.Companion.getInstance().getCallback()
        if (mCallback != null) {
            BluetoothImpl.Companion.getInstance().addSdkLog("response time out")
            val lockError: LockError = LockError.DATA_FORMAT_ERROR
            lockError.setSdkLog(BluetoothImpl.Companion.getInstance().getSdkLog())
            mCallback.onFail(lockError)
        }
    }

    init {
        mApi = TTLockSDKApi()
        mConnectParam = null
    }

    private object InstanceHolder {
        private val mInstance = ConnectManager()
    }

    fun removeDataCheckTimer() {
        mDataCheckHandler.removeCallbacks(mDataCheckErrorRunnable)
    }

    fun storeConnectParamForCallback(param: ConnectParam?) {
        mConnectParam = param
    }

    fun getConnectParamForCallback(): ConnectParam? {
        return mConnectParam
    }

    fun isDeviceConnected(address: String): Boolean {
        return BluetoothImpl.Companion.getInstance().isConnected(address)
    }

    fun connect2Device(address: String?) {
        mCurrentMac = address
        LockCallbackManager.Companion.getInstance().setConnectCallback(this)
        BluetoothImpl.Companion.getInstance().connect(address)
    }

    fun connect2Device(device: ExtendedBluetoothDevice?) {
        LockCallbackManager.Companion.getInstance().setConnectCallback(this)
        BluetoothImpl.Companion.getInstance().connect(device)
    }

    fun connect2Device(device: WirelessKeypad?) {
        LockCallbackManager.Companion.getInstance().setConnectCallback(this)
        GattCallbackHelper.Companion.getInstance().connect(device)
    }

    fun disconnect() {
        BluetoothImpl.Companion.getInstance().disconnect()
    }

    private fun startDataCheckTimer() {
        mDataCheckHandler.postDelayed(mDataCheckErrorRunnable, 500)
    }

    override fun onConnectSuccess(device: TTDevice) {
        val callbackType: Int = LockCallbackManager.Companion.getInstance().getOperationType()
        if (callbackType == OperationType.CONNECT_LOCK) { //连接回调
            Handler(Looper.getMainLooper()).post(Runnable {
                val mCallback: LockCallback =
                    LockCallbackManager.Companion.getInstance().getCallback()
                (mCallback as ConnectLockCallback).onConnectSuccess()
            })
            return
        }
        if (callbackType == OperationType.UNKNOWN_TYPE) {
            LogUtil.d("unknown type")
            return
        }
        startDataCheckTimer()
        if (mConnectParam == null && callbackType != OperationType.INIT_LOCK && callbackType != OperationType.GET_LOCK_VERSION) {
            return
        }
        when (callbackType) {
            OperationType.INIT_LOCK -> mApi.initLock(device as ExtendedBluetoothDevice)
            OperationType.INIT_KEYPAD -> WirelessKeypadSDKApi.Companion.initKeyapd(
                device as WirelessKeypad,
                mConnectParam
            )
            OperationType.GET_LOCK_VERSION -> mApi.getLockVersion()
            OperationType.CONTROL_LOCK -> {
                mApi.controlLock(mConnectParam!!.controlAction, mConnectParam!!.lockData)
                if (isRollingGate()) rollingGateCallback()
            }
            OperationType.RESET_KEY -> mApi.resetEkey(mConnectParam!!.lockData)
            OperationType.RESET_LOCK -> mApi.resetLock(mConnectParam!!.lockData)
            OperationType.GET_MUTE_MODE_STATE -> mApi.getMuteModeState(
                mConnectParam!!.lockData
            )
            OperationType.SET_MUTE_MODE_STATE -> mApi.setMuteMode(
                mConnectParam!!.isLockModeEnable, mConnectParam!!.lockData
            )
            OperationType.GET_REMOTE_UNLOCK_STATE -> mApi.getRemoteUnlockSwitchState(
                mConnectParam!!.lockData
            )
            OperationType.SET_REMOTE_UNLOCK_STATE -> mApi.setRemoteUnlockSwitchState(
                mConnectParam!!.isLockModeEnable, mConnectParam!!.lockData
            )
            OperationType.GET_PASSCODE_VISIBLE_STATE -> mApi.getPasscodeVisibleSwitchState(
                mConnectParam!!.lockData
            )
            OperationType.SET_PASSCODE_VISIBLE_STATE -> mApi.setPasscodeVisibleSwitchState(
                mConnectParam!!.isLockModeEnable, mConnectParam!!.lockData
            )
            OperationType.GET_PASSAGE_MODE -> mApi.getPassageMode(
                mConnectParam!!.lockData
            )
            OperationType.SET_PASSAGE_MODE -> mApi.setPassageMode(
                mConnectParam!!.passageModeConfig, mConnectParam!!.lockData
            )
            OperationType.DELETE_PASSAGE_MODE -> mApi.deletePassageMode(
                mConnectParam!!.passageModeConfig, mConnectParam!!.lockData
            )
            OperationType.CLEAR_PASSAGE_MODE -> mApi.clearPassageMode(
                mConnectParam!!.lockData
            )
            OperationType.GET_LOCK_TIME -> mApi.getLockTime(mConnectParam!!.lockData)
            OperationType.SET_LOCK_TIME -> mApi.setLockTime(
                mConnectParam!!.timestamp,
                mConnectParam!!.lockData
            )
            OperationType.GET_OPERATION_LOG -> mApi.getOperationLog(
                mConnectParam!!.logType, mConnectParam!!.lockData
            )
            OperationType.GET_ELECTRIC_QUALITY -> mApi.getBatteryLevel(
                mConnectParam!!.lockData
            )
            OperationType.GET_SPECIAL_VALUE -> mApi.getSpecialValue(
                mConnectParam!!.lockData
            )
            OperationType.RECOVERY_DATA -> mApi.recoverLockData(
                mConnectParam!!.recoveryDataStr,
                mConnectParam!!.recoveryDataType,
                mConnectParam!!.lockData
            )
            OperationType.GET_SYSTEM_INFO -> mApi.getLockSystemInfo(
                mConnectParam!!.lockData
            )
            OperationType.CREATE_CUSTOM_PASSCODE -> mApi.createCustomPasscode(
                mConnectParam!!.originalPasscode,
                mConnectParam!!.startDate,
                mConnectParam!!.endDate,
                mConnectParam!!.lockData
            )
            OperationType.GET_LOCK_STATUS -> mApi.getLockStatus(
                mConnectParam!!.lockData
            )
            OperationType.SET_AUTO_LOCK_PERIOD -> mApi.setAutomaticLockingPeriod(
                mConnectParam!!.autoLockingPeriod, mConnectParam!!.lockData
            )
            OperationType.GET_AUTO_LOCK_PERIOD -> mApi.getAutomaticLockingPeriod(
                mConnectParam!!.lockData
            )
            OperationType.MODIFY_PASSCODE -> mApi.modifyPasscode(
                mConnectParam!!.originalPasscode,
                mConnectParam!!.newPasscode,
                mConnectParam!!.startDate,
                mConnectParam!!.endDate,
                mConnectParam!!.lockData
            )
            OperationType.DELETE_PASSCODE -> mApi.deletePasscode(
                mConnectParam!!.originalPasscode, mConnectParam!!.lockData
            )
            OperationType.RESET_PASSCODE -> mApi.resetPasscode(
                mConnectParam!!.lockData
            )
            OperationType.GET_ALL_VALID_PASSCODES -> mApi.getAllValidPasscodes(
                mConnectParam!!.lockData
            )
            OperationType.GET_PASSCODE_INFO -> mApi.getPasscodeVerificationParams(
                mConnectParam!!.lockData
            )
            OperationType.GET_ADMIN_PASSCODE -> mApi.getAdminPasscode(
                mConnectParam!!.lockData
            )
            OperationType.MODIFY_ADMIN_PASSCODE -> mApi.modifyAdminPasscode(
                mConnectParam!!.newPasscode, mConnectParam!!.lockData
            )
            OperationType.ADD_IC_CARD, OperationType.ADD_CYCLIC_IC_CARD -> mApi.addICCard(
                mConnectParam!!.validityInfo, mConnectParam!!.lockData
            )
            OperationType.MODIFY_IC_CARD_PERIOD -> mApi.modifyICCardValidityPeriod(
                mConnectParam!!.validityInfo,
                mConnectParam!!.attachmentNum,
                mConnectParam!!.lockData
            )
            OperationType.GET_ALL_IC_CARDS -> mApi.getAllValidICCards(
                mConnectParam!!.lockData
            )
            OperationType.DELETE_IC_CARD -> mApi.deleteICCard(
                mConnectParam!!.attachmentNum, mConnectParam!!.lockData
            )
            OperationType.LOSS_CARD -> mApi.lossICCard(
                mConnectParam!!.attachmentNum,
                mConnectParam!!.lockData
            )
            OperationType.CLEAR_ALL_IC_CARD -> mApi.clearAllICCard(
                mConnectParam!!.lockData
            )
            OperationType.ADD_FINGERPRINT, OperationType.ADD_CYCLIC_FINGERPRINT -> mApi.addFingerprint(
                mConnectParam!!.validityInfo, mConnectParam!!.lockData
            )
            OperationType.MODIFY_FINGEPRINT_PERIOD -> mApi.modifyFingerprintValidityPeriod(
                mConnectParam!!.validityInfo,
                mConnectParam!!.attachmentNum,
                mConnectParam!!.lockData
            )
            OperationType.GET_ALL_FINGERPRINTS -> mApi.getAllValidFingerprints(
                mConnectParam!!.lockData
            )
            OperationType.DELETE_FINGERPRINT -> mApi.deleteFingerprint(
                mConnectParam!!.attachmentNum, mConnectParam!!.lockData
            )
            OperationType.CLEAR_ALL_FINGERPRINTS -> mApi.clearAllFingerprints(
                mConnectParam!!.lockData
            )
            OperationType.WRITE_FINGERPRINT_DATA -> mApi.writeFingerprintData(
                mConnectParam!!.dataJsonStr,
                mConnectParam!!.attachmentNum.toInt(),
                mConnectParam!!.startDate,
                mConnectParam!!.endDate,
                mConnectParam!!.lockData
            )
            OperationType.ENTER_DFU_MODE -> mApi.enterDfuMode(
                mConnectParam!!.lockData
            )
            OperationType.SET_NB_SERVER -> mApi.setNBServerInfo(
                mConnectParam!!.serverPort, mConnectParam!!.serverAddress, mConnectParam!!.lockData
            )
            OperationType.SET_LOCK_FREEZE_STATE -> mApi.setLockFreezeState(
                mConnectParam!!.isLockModeEnable, mConnectParam!!.lockData
            )
            OperationType.GET_LOCK_FREEZE_STATE -> mApi.getLockFreezeState(
                mConnectParam!!.lockData
            )
            OperationType.SET_LIGHT_TIME -> mApi.setLightTime(
                mConnectParam!!.seconds, mConnectParam!!.lockData
            )
            OperationType.GET_LIGHT_TIME -> mApi.getLightTime(
                mConnectParam!!.lockData
            )
            OperationType.SET_HOTEL_CARD_SECTION -> mApi.setHotelCardSector(
                mConnectParam!!.hotelData, mConnectParam!!.lockData
            )
            OperationType.SET_LOCK_CONFIG -> when (mConnectParam!!.ttLockConfigType) {
                TTLockConfigType.LOCK_SOUND -> mApi.setMuteMode(
                    !mConnectParam!!.isLockModeEnable,
                    mConnectParam!!.lockData
                )
                TTLockConfigType.PASSCODE_VISIBLE -> mApi.setPasscodeVisibleSwitchState(
                    mConnectParam!!.isLockModeEnable, mConnectParam!!.lockData
                )
                TTLockConfigType.LOCK_FREEZE -> mApi.setLockFreezeState(
                    mConnectParam!!.isLockModeEnable,
                    mConnectParam!!.lockData
                )
                TTLockConfigType.PRIVACY_LOCK, TTLockConfigType.TAMPER_ALERT, TTLockConfigType.RESET_BUTTON, TTLockConfigType.WIFI_LOCK_POWER_SAVING_MODE -> mApi.setLockConfig(
                    mConnectParam!!.ttLockConfigType,
                    mConnectParam!!.isLockModeEnable,
                    mConnectParam!!.lockData
                )
            }
            OperationType.GET_LOCK_CONFIG -> when (mConnectParam!!.ttLockConfigType) {
                TTLockConfigType.LOCK_SOUND -> mApi.getMuteModeState(
                    mConnectParam!!.lockData
                )
                TTLockConfigType.PASSCODE_VISIBLE -> mApi.getPasscodeVisibleSwitchState(
                    mConnectParam!!.lockData
                )
                TTLockConfigType.LOCK_FREEZE -> mApi.getLockFreezeState(mConnectParam!!.lockData)
                TTLockConfigType.PRIVACY_LOCK, TTLockConfigType.TAMPER_ALERT, TTLockConfigType.RESET_BUTTON, TTLockConfigType.WIFI_LOCK_POWER_SAVING_MODE -> mApi.getLockConfig(
                    mConnectParam!!.ttLockConfigType, mConnectParam!!.lockData
                )
            }
            OperationType.SET_HOTEL_DATA -> mApi.setHotelData(
                mConnectParam!!.hotelData, mConnectParam!!.lockData
            )
            OperationType.SET_ELEVATOR_CONTROLABLE_FLOORS -> mApi.setLiftControlableFloors(
                mConnectParam!!.hotelData, mConnectParam!!.lockData
            )
            OperationType.SET_ELEVATOR_WORK_MODE -> mApi.setLiftWorkMode(
                mConnectParam!!.hotelData, mConnectParam!!.lockData
            )
            OperationType.SET_NB_ACTIVATE_MODE -> mApi.setNBAwakeModes(
                mConnectParam!!.nbAwakeConfig, mConnectParam!!.lockData
            )
            OperationType.GET_NB_ACITATE_MODE -> mApi.getNBAwakeModes(
                mConnectParam!!.lockData
            )
            OperationType.SET_NB_ACTIVATE_CONFIG -> mApi.setNBAwakeTimes(
                mConnectParam!!.nbAwakeConfig, mConnectParam!!.lockData
            )
            OperationType.GET_NB_ACTIVATE_CONFIG -> mApi.getNBAwakeTimes(
                mConnectParam!!.lockData
            )
            OperationType.ACTIVATE_LIFT_FLOORS -> mApi.activateLiftFloors(
                mConnectParam!!.activateFloors, mConnectParam!!.timestamp, mConnectParam!!.lockData
            )
            OperationType.SET_UNLOCK_DIRECTION -> mApi.setUnlockDirection(
                mConnectParam!!.unlockDirection, mConnectParam!!.lockData
            )
            OperationType.GET_UNLOCK_DIRECTION -> mApi.getUnlockDirection(
                mConnectParam!!.lockData
            )
            OperationType.GET_ACCESSORY_BATTERY -> mApi.getAccessoryBatteryLevel(
                mConnectParam!!.accessoryInfo, mConnectParam!!.lockData
            )
            OperationType.ADD_KEY_FOB -> mApi.addRemote(
                mConnectParam!!.keyfobMac,
                mConnectParam!!.validityInfo,
                mConnectParam!!.lockData
            )
            OperationType.UPDATE_KEY_FOB_VALIDITY -> mApi.modifyRemoteValidity(
                mConnectParam!!.keyfobMac, mConnectParam!!.validityInfo, mConnectParam!!.lockData
            )
            OperationType.DELETE_KEY_FOB -> mApi.deleteRemote(
                mConnectParam!!.keyfobMac, mConnectParam!!.lockData
            )
            OperationType.CLEAR_KEY_FOB -> mApi.clearRemote(mConnectParam!!.lockData)
            OperationType.SCAN_WIFI -> mApi.scanWifi(mConnectParam!!.lockData)
            OperationType.CONFIGURE_WIFI_AP -> mApi.configureWifiAp(
                mConnectParam!!.wifiName, mConnectParam!!.wifiPassword, mConnectParam!!.lockData
            )
            OperationType.CONFIGURE_SERVER -> mApi.configureServer(
                mConnectParam!!.serverAddress,
                mConnectParam!!.serverPort.toInt(),
                mConnectParam!!.lockData
            )
            OperationType.GET_WIFI_INFO -> mApi.getWifiInfo(mConnectParam!!.lockData)
            OperationType.CONFIGURE_STATIC_IP -> mApi.configureStaticIp(
                mConnectParam!!.ipSetting, mConnectParam!!.lockData
            )
            OperationType.SET_LOCK_SOUND_VOLUME -> mApi.setLockSound(
                mConnectParam!!.soundVolume, mConnectParam!!.lockData
            )
            OperationType.GET_LOCK_SOUND_VOLUME -> mApi.getLockSound(
                mConnectParam!!.lockData
            )
            OperationType.ADD_DOOR_SENSOR -> mApi.addDoorSensor(
                mConnectParam!!.doorSensorMac, mConnectParam!!.lockData
            )
            OperationType.DELETE_DOOR_SENSOR -> mApi.deleteDoorSensor(
                mConnectParam!!.lockData
            )
            else -> {}
        }
    }

    /**
     * do second connect if first time connect fail.
     * @param error
     */
    override fun onFail(error: LockError) {
        if (!LockCallbackManager.Companion.getInstance().callbackArrayIsEmpty()) {
            val callbackType: Int = LockCallbackManager.Companion.getInstance().getOperationType()
            if (callbackType == OperationType.UNKNOWN_TYPE) {
                LogUtil.d("unknown type")
                return
            }
            val mCallback: LockCallback = LockCallbackManager.Companion.getInstance().getCallback()
            if (mCallback != null) {
                if (!TextUtils.isEmpty(mCurrentMac) && error != LockError.BLE_SERVER_NOT_INIT && !isRollingGate()) {
                    retryConnect(callbackType, mCallback)
                } else {
                    mCallback.onFail(error)
                }
            }
        }
    }

    /**
     * 判断是否是遥控设备指令
     * @return
     */
    private fun isRollingGate(): Boolean {
        if (mConnectParam != null && LockCallbackManager.Companion.getInstance()
                .getOperationType() == OperationType.CONTROL_LOCK
        ) {
            val action = mConnectParam!!.controlAction
            if (action == ControlAction.ROLLING_GATE_UP || action == ControlAction.ROLLING_GATE_DOWN || action == ControlAction.ROLLING_GATE_PAUSE || action == ControlAction.ROLLING_GATE_LOCK) {
                return true
            }
        }
        return false
    }

    private fun rollingGateCallback() {
        Handler(Looper.getMainLooper()).post(Runnable {
            val mControlLockCallback: LockCallback =
                LockCallbackManager.Companion.getInstance().getCallback()
            if (mControlLockCallback != null && mConnectParam != null) {
                (mControlLockCallback as ControlLockCallback).onControlLockSuccess(
                    ControlLockResult(
                        mConnectParam!!.controlAction, -1, -1, -1
                    )
                )
            }
        })
    }

    private fun retryConnect(callbackType: Int, callback: LockCallback) {
        connect2Device(mCurrentMac)
        LockCallbackManager.Companion.getInstance().isDeviceBusy(callbackType, callback)
        mCurrentMac = ""
    }

    companion object {
        fun getInstance(): ConnectManager {
            return InstanceHolder.mInstance
        }
    }
}