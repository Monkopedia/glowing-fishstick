package com.ttlock.bl.sdk.api

import android.util.Handler
import android.util.Looper
import android.util.TextUtils
import com.ttlock.bl.sdk.callback.ConnectCallback
import com.ttlock.bl.sdk.callback.ConnectLockCallback
import com.ttlock.bl.sdk.callback.ControlLockCallback
import com.ttlock.bl.sdk.callback.LockCallback
import com.ttlock.bl.sdk.callback.OperationType
import com.ttlock.bl.sdk.constant.ControlAction
import com.ttlock.bl.sdk.device.TTDevice
import com.ttlock.bl.sdk.device.WirelessKeypad
import com.ttlock.bl.sdk.entity.ConnectParam
import com.ttlock.bl.sdk.entity.ControlLockResult
import com.ttlock.bl.sdk.entity.LockError
import com.ttlock.bl.sdk.entity.TTLockConfigType
import com.ttlock.bl.sdk.util.LogUtil

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
        val mCallback: LockCallback? = LockCallbackManager.Companion.getInstance().getCallback()
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
        val mInstance = ConnectManager()
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

    fun connect2Device(device: WirelessKeypad) {
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
        if (callbackType == OperationType.CONNECT_LOCK) { // 连接回调
            Handler(Looper.getMainLooper()).post(
                Runnable {
                    val mCallback: LockCallback? =
                        LockCallbackManager.Companion.getInstance().getCallback()
                    (mCallback as ConnectLockCallback).onConnectSuccess()
                }
            )
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
                mApi.controlLock(mConnectParam!!.getControlAction(), mConnectParam!!.getLockData())
                if (isRollingGate()) rollingGateCallback()
            }
            OperationType.RESET_KEY -> mApi.resetEkey(mConnectParam!!.getLockData())
            OperationType.RESET_LOCK -> mApi.resetLock(mConnectParam!!.getLockData())
            OperationType.GET_MUTE_MODE_STATE -> mApi.getMuteModeState(
                mConnectParam!!.getLockData()
            )
            OperationType.SET_MUTE_MODE_STATE -> mApi.setMuteMode(
                mConnectParam!!.isLockModeEnable(), mConnectParam!!.getLockData()
            )
            OperationType.GET_REMOTE_UNLOCK_STATE -> mApi.getRemoteUnlockSwitchState(
                mConnectParam!!.getLockData()
            )
            OperationType.SET_REMOTE_UNLOCK_STATE -> mApi.setRemoteUnlockSwitchState(
                mConnectParam!!.isLockModeEnable(), mConnectParam!!.getLockData()
            )
            OperationType.GET_PASSCODE_VISIBLE_STATE -> mApi.getPasscodeVisibleSwitchState(
                mConnectParam!!.getLockData()
            )
            OperationType.SET_PASSCODE_VISIBLE_STATE -> mApi.setPasscodeVisibleSwitchState(
                mConnectParam!!.isLockModeEnable(), mConnectParam!!.getLockData()
            )
            OperationType.GET_PASSAGE_MODE -> mApi.getPassageMode(
                mConnectParam!!.getLockData()
            )
            OperationType.SET_PASSAGE_MODE -> mApi.setPassageMode(
                mConnectParam!!.getPassageModeConfig()!!, mConnectParam!!.getLockData()
            )
            OperationType.DELETE_PASSAGE_MODE -> mApi.deletePassageMode(
                mConnectParam!!.getPassageModeConfig()!!, mConnectParam!!.getLockData()
            )
            OperationType.CLEAR_PASSAGE_MODE -> mApi.clearPassageMode(
                mConnectParam!!.getLockData()
            )
            OperationType.GET_LOCK_TIME -> mApi.getLockTime(mConnectParam!!.getLockData())
            OperationType.SET_LOCK_TIME -> mApi.setLockTime(
                mConnectParam!!.getTimestamp(),
                mConnectParam!!.getLockData()
            )
            OperationType.GET_OPERATION_LOG -> mApi.getOperationLog(
                mConnectParam!!.getLogType(), mConnectParam!!.getLockData()
            )
            OperationType.GET_ELECTRIC_QUALITY -> mApi.getBatteryLevel(
                mConnectParam!!.getLockData()
            )
            OperationType.GET_SPECIAL_VALUE -> mApi.getSpecialValue(
                mConnectParam!!.getLockData()
            )
            OperationType.RECOVERY_DATA -> mApi.recoverLockData(
                mConnectParam!!.getRecoveryDataStr(),
                mConnectParam!!.getRecoveryDataType(),
                mConnectParam!!.getLockData()
            )
            OperationType.GET_SYSTEM_INFO -> mApi.getLockSystemInfo(
                mConnectParam!!.getLockData()
            )
            OperationType.CREATE_CUSTOM_PASSCODE -> mApi.createCustomPasscode(
                mConnectParam!!.getOriginalPasscode(),
                mConnectParam!!.getStartDate(),
                mConnectParam!!.getEndDate(),
                mConnectParam!!.getLockData()
            )
            OperationType.GET_LOCK_STATUS -> mApi.getLockStatus(
                mConnectParam!!.getLockData()
            )
            OperationType.SET_AUTO_LOCK_PERIOD -> mApi.setAutomaticLockingPeriod(
                mConnectParam!!.getAutoLockingPeriod(), mConnectParam!!.getLockData()
            )
            OperationType.GET_AUTO_LOCK_PERIOD -> mApi.getAutomaticLockingPeriod(
                mConnectParam!!.getLockData()
            )
            OperationType.MODIFY_PASSCODE -> mApi.modifyPasscode(
                mConnectParam!!.getOriginalPasscode(),
                mConnectParam!!.getNewPasscode(),
                mConnectParam!!.getStartDate(),
                mConnectParam!!.getEndDate(),
                mConnectParam!!.getLockData()
            )
            OperationType.DELETE_PASSCODE -> mApi.deletePasscode(
                mConnectParam!!.getOriginalPasscode(), mConnectParam!!.getLockData()
            )
            OperationType.RESET_PASSCODE -> mApi.resetPasscode(
                mConnectParam!!.getLockData()
            )
            OperationType.GET_ALL_VALID_PASSCODES -> mApi.getAllValidPasscodes(
                mConnectParam!!.getLockData()
            )
            OperationType.GET_PASSCODE_INFO -> mApi.getPasscodeVerificationParams(
                mConnectParam!!.getLockData()
            )
            OperationType.GET_ADMIN_PASSCODE -> mApi.getAdminPasscode(
                mConnectParam!!.getLockData()
            )
            OperationType.MODIFY_ADMIN_PASSCODE -> mApi.modifyAdminPasscode(
                mConnectParam!!.getNewPasscode(), mConnectParam!!.getLockData()
            )
            OperationType.ADD_IC_CARD, OperationType.ADD_CYCLIC_IC_CARD -> mApi.addICCard(
                mConnectParam!!.getValidityInfo(), mConnectParam!!.getLockData()
            )
            OperationType.MODIFY_IC_CARD_PERIOD -> mApi.modifyICCardValidityPeriod(
                mConnectParam!!.getValidityInfo(),
                mConnectParam!!.getAttachmentNum(),
                mConnectParam!!.getLockData()
            )
            OperationType.GET_ALL_IC_CARDS -> mApi.getAllValidICCards(
                mConnectParam!!.getLockData()
            )
            OperationType.DELETE_IC_CARD -> mApi.deleteICCard(
                mConnectParam!!.getAttachmentNum(), mConnectParam!!.getLockData()
            )
            OperationType.LOSS_CARD -> mApi.lossICCard(
                mConnectParam!!.getAttachmentNum(),
                mConnectParam!!.getLockData()
            )
            OperationType.CLEAR_ALL_IC_CARD -> mApi.clearAllICCard(
                mConnectParam!!.getLockData()
            )
            OperationType.ADD_FINGERPRINT, OperationType.ADD_CYCLIC_FINGERPRINT -> mApi.addFingerprint(
                mConnectParam!!.getValidityInfo(), mConnectParam!!.getLockData()
            )
            OperationType.MODIFY_FINGEPRINT_PERIOD -> mApi.modifyFingerprintValidityPeriod(
                mConnectParam!!.getValidityInfo(),
                mConnectParam!!.getAttachmentNum(),
                mConnectParam!!.getLockData()
            )
            OperationType.GET_ALL_FINGERPRINTS -> mApi.getAllValidFingerprints(
                mConnectParam!!.getLockData()
            )
            OperationType.DELETE_FINGERPRINT -> mApi.deleteFingerprint(
                mConnectParam!!.getAttachmentNum(), mConnectParam!!.getLockData()
            )
            OperationType.CLEAR_ALL_FINGERPRINTS -> mApi.clearAllFingerprints(
                mConnectParam!!.getLockData()
            )
            OperationType.WRITE_FINGERPRINT_DATA -> mApi.writeFingerprintData(
                mConnectParam!!.getDataJsonStr(),
                mConnectParam!!.getAttachmentNum().toInt(),
                mConnectParam!!.getStartDate(),
                mConnectParam!!.getEndDate(),
                mConnectParam!!.getLockData()
            )
            OperationType.ENTER_DFU_MODE -> mApi.enterDfuMode(
                mConnectParam!!.getLockData()
            )
            OperationType.SET_NB_SERVER -> mApi.setNBServerInfo(
                mConnectParam!!.getServerPort(), mConnectParam!!.getServerAddress(), mConnectParam!!.getLockData()
            )
            OperationType.SET_LOCK_FREEZE_STATE -> mApi.setLockFreezeState(
                mConnectParam!!.isLockModeEnable(), mConnectParam!!.getLockData()
            )
            OperationType.GET_LOCK_FREEZE_STATE -> mApi.getLockFreezeState(
                mConnectParam!!.getLockData()
            )
            OperationType.SET_LIGHT_TIME -> mApi.setLightTime(
                mConnectParam!!.getSeconds(), mConnectParam!!.getLockData()
            )
            OperationType.GET_LIGHT_TIME -> mApi.getLightTime(
                mConnectParam!!.getLockData()
            )
            OperationType.SET_HOTEL_CARD_SECTION -> mApi.setHotelCardSector(
                mConnectParam!!.getHotelData(), mConnectParam!!.getLockData()
            )
            OperationType.SET_LOCK_CONFIG -> when (mConnectParam!!.getTtLockConfigType()) {
                TTLockConfigType.LOCK_SOUND -> mApi.setMuteMode(
                    !mConnectParam!!.isLockModeEnable(),
                    mConnectParam!!.getLockData()
                )
                TTLockConfigType.PASSCODE_VISIBLE -> mApi.setPasscodeVisibleSwitchState(
                    mConnectParam!!.isLockModeEnable(), mConnectParam!!.getLockData()
                )
                TTLockConfigType.LOCK_FREEZE -> mApi.setLockFreezeState(
                    mConnectParam!!.isLockModeEnable(),
                    mConnectParam!!.getLockData()
                )
                TTLockConfigType.PRIVACY_LOCK, TTLockConfigType.TAMPER_ALERT, TTLockConfigType.RESET_BUTTON, TTLockConfigType.WIFI_LOCK_POWER_SAVING_MODE -> mApi.setLockConfig(
                    mConnectParam!!.getTtLockConfigType()!!,
                    mConnectParam!!.isLockModeEnable(),
                    mConnectParam!!.getLockData()
                )
                else ->  {}
            }
            OperationType.GET_LOCK_CONFIG -> when (mConnectParam!!.getTtLockConfigType()) {
                TTLockConfigType.LOCK_SOUND -> mApi.getMuteModeState(
                    mConnectParam!!.getLockData()
                )
                TTLockConfigType.PASSCODE_VISIBLE -> mApi.getPasscodeVisibleSwitchState(
                    mConnectParam!!.getLockData()
                )
                TTLockConfigType.LOCK_FREEZE -> mApi.getLockFreezeState(mConnectParam!!.getLockData())
                TTLockConfigType.PRIVACY_LOCK, TTLockConfigType.TAMPER_ALERT, TTLockConfigType.RESET_BUTTON, TTLockConfigType.WIFI_LOCK_POWER_SAVING_MODE -> mApi.getLockConfig(
                    mConnectParam!!.getTtLockConfigType()!!, mConnectParam!!.getLockData()
                )
                else ->  {}
            }
            OperationType.SET_HOTEL_DATA -> mApi.setHotelData(
                mConnectParam!!.getHotelData(), mConnectParam!!.getLockData()
            )
            OperationType.SET_ELEVATOR_CONTROLABLE_FLOORS -> mApi.setLiftControlableFloors(
                mConnectParam!!.getHotelData()!!, mConnectParam!!.getLockData()
            )
            OperationType.SET_ELEVATOR_WORK_MODE -> mApi.setLiftWorkMode(
                mConnectParam!!.getHotelData()!!, mConnectParam!!.getLockData()
            )
            OperationType.SET_NB_ACTIVATE_MODE -> mApi.setNBAwakeModes(
                mConnectParam!!.getNbAwakeConfig(), mConnectParam!!.getLockData()
            )
            OperationType.GET_NB_ACITATE_MODE -> mApi.getNBAwakeModes(
                mConnectParam!!.getLockData()
            )
            OperationType.SET_NB_ACTIVATE_CONFIG -> mApi.setNBAwakeTimes(
                mConnectParam!!.getNbAwakeConfig(), mConnectParam!!.getLockData()
            )
            OperationType.GET_NB_ACTIVATE_CONFIG -> mApi.getNBAwakeTimes(
                mConnectParam!!.getLockData()
            )
            OperationType.ACTIVATE_LIFT_FLOORS -> mApi.activateLiftFloors(
                mConnectParam!!.getActivateFloors(), mConnectParam!!.getTimestamp(), mConnectParam!!.getLockData()
            )
            OperationType.SET_UNLOCK_DIRECTION -> mApi.setUnlockDirection(
                mConnectParam!!.getUnlockDirection(), mConnectParam!!.getLockData()
            )
            OperationType.GET_UNLOCK_DIRECTION -> mApi.getUnlockDirection(
                mConnectParam!!.getLockData()
            )
            OperationType.GET_ACCESSORY_BATTERY -> mApi.getAccessoryBatteryLevel(
                mConnectParam!!.getAccessoryInfo(), mConnectParam!!.getLockData()
            )
            OperationType.ADD_KEY_FOB -> mApi.addRemote(
                mConnectParam!!.getKeyfobMac(),
                mConnectParam!!.getValidityInfo(),
                mConnectParam!!.getLockData()
            )
            OperationType.UPDATE_KEY_FOB_VALIDITY -> mApi.modifyRemoteValidity(
                mConnectParam!!.getKeyfobMac(), mConnectParam!!.getValidityInfo(), mConnectParam!!.getLockData()
            )
            OperationType.DELETE_KEY_FOB -> mApi.deleteRemote(
                mConnectParam!!.getKeyfobMac(), mConnectParam!!.getLockData()
            )
            OperationType.CLEAR_KEY_FOB -> mApi.clearRemote(mConnectParam!!.getLockData())
            OperationType.SCAN_WIFI -> mApi.scanWifi(mConnectParam!!.getLockData())
            OperationType.CONFIGURE_WIFI_AP -> mApi.configureWifiAp(
                mConnectParam!!.getWifiName(), mConnectParam!!.getWifiPassword(), mConnectParam!!.getLockData()
            )
            OperationType.CONFIGURE_SERVER -> mApi.configureServer(
                mConnectParam!!.getServerAddress(),
                mConnectParam!!.getServerPort().toInt(),
                mConnectParam!!.getLockData()
            )
            OperationType.GET_WIFI_INFO -> mApi.getWifiInfo(mConnectParam!!.getLockData())
            OperationType.CONFIGURE_STATIC_IP -> mApi.configureStaticIp(
                mConnectParam!!.getIpSetting()!!, mConnectParam!!.getLockData()
            )
            OperationType.SET_LOCK_SOUND_VOLUME -> mApi.setLockSound(
                mConnectParam!!.getSoundVolume(), mConnectParam!!.getLockData()
            )
            OperationType.GET_LOCK_SOUND_VOLUME -> mApi.getLockSound(
                mConnectParam!!.getLockData()
            )
            OperationType.ADD_DOOR_SENSOR -> mApi.addDoorSensor(
                mConnectParam!!.getDoorSensorMac(), mConnectParam!!.getLockData()
            )
            OperationType.DELETE_DOOR_SENSOR -> mApi.deleteDoorSensor(
                mConnectParam!!.getLockData()
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
            val mCallback: LockCallback? = LockCallbackManager.Companion.getInstance().getCallback()
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
            val action = mConnectParam!!.getControlAction()
            if (action == ControlAction.ROLLING_GATE_UP || action == ControlAction.ROLLING_GATE_DOWN || action == ControlAction.ROLLING_GATE_PAUSE || action == ControlAction.ROLLING_GATE_LOCK) {
                return true
            }
        }
        return false
    }

    private fun rollingGateCallback() {
        Handler(Looper.getMainLooper()).post(
            Runnable {
                val mControlLockCallback: LockCallback? =
                    LockCallbackManager.Companion.getInstance().getCallback()
                if (mControlLockCallback != null && mConnectParam != null) {
                    (mControlLockCallback as ControlLockCallback).onControlLockSuccess(
                        ControlLockResult(
                            mConnectParam!!.getControlAction(), -1, -1, -1
                        )
                    )
                }
            }
        )
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
