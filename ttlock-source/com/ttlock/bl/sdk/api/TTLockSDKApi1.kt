package com.ttlock.bl.sdk.api

import android.Manifest
import com.ttlock.bl.sdk.constant.Constant
import com.ttlock.bl.sdk.constant.OperationType

/**
 * Created on  2019/4/11 0011 15:41
 *
 * @author theodore
 */
internal class TTLockSDKApi : TTLockSdkApiBase() {
    private var lastKey = 0
    public override fun prepareBTService(context: Context?) {
        BluetoothImpl.Companion.getInstance().prepareBTService(context)
    }

    public override fun stopBTService() {
        BluetoothImpl.Companion.getInstance().stopBTService()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public override fun startScan() {
        BluetoothImpl.Companion.getInstance().startScan()
    }

    public override fun stopScan() {
        BluetoothImpl.Companion.getInstance().stopScan()
    }

    fun initLock(device: ExtendedBluetoothDevice) {
        val lockType = device.lockType
        //TODO:注释掉有些锁搜索广播慢
        //TODO:E指令有時候彙報KEY_INVALID 查一下原因 是不是 沒有進入設置模式造成的
        //TODO:车位锁添加的时候反馈有异常指令
        if (lockType > LockType.LOCK_TYPE_V2S && !device.isSettingMode) {
            val mCallback: LockCallback = LockCallbackManager.Companion.getInstance().getCallback()
            if (mCallback != null) {
                mCallback.onFail(LockError.LOCK_IS_IN_NO_SETTING_MODE)
            }
            return
        }
        when (lockType) {
            LockType.LOCK_TYPE_CAR -> {
                val adminPs: String = String(DigitUtil.generateDynamicPassword(10))
                val unlockKey: String = String(DigitUtil.generateDynamicPassword(10))
                CommandUtil.V_addAdmin(LockType.LOCK_TYPE_CAR, adminPs, unlockKey, null)
            }
            LockType.LOCK_TYPE_MOBI -> {}
            LockType.LOCK_TYPE_V2S -> {
                adminPs = String(DigitUtil.generateDynamicPassword(10))
                unlockKey = String(DigitUtil.generateDynamicPassword(10))
                CommandUtil.V_addAdmin(LockType.LOCK_TYPE_V2S, adminPs, unlockKey, null)
            }
            LockType.LOCK_TYPE_V2S_PLUS -> CommandUtil.getAESKey(
                LockVersion.Companion.lockVersion_V2S_PLUS,
                "",
                APICommand.OP_ADD_ADMIN
            )
            LockType.LOCK_TYPE_V3, LockType.LOCK_TYPE_V3_CAR -> {
                val transferData = TransferData()
                transferData.setAPICommand(APICommand.OP_ADD_ADMIN)
                transferData.setHotelData(device.hotelData)
                CommandUtil.getAESKey(transferData)
                Constant.VENDOR = Constant.SCIENER
            }
            else ->                 //TODO:
                CommandUtil.E_getLockVersion(APICommand.OP_ADD_ADMIN)
        }
    }

    fun setNBServerInfo(portNumber: Short, serverAddress: String?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_CONFIGURE_NB_SERVER_ADDRESS)
        transferData.setCommand(Command.Companion.COMM_CONFIGURE_NB_ADDRESS)
        transferData.setPort(portNumber)
        transferData.setAddress(serverAddress)
        CommandUtil.A_checkAdmin(transferData)
        //        CommandUtil_V3.configureNBServerAddress(transferData);
    }

    fun resetEkey(lockData: LockData) {
        val aesKeyStr: String = lockData.getAesKeyStr()
        var aesKeyArray: ByteArray? = null
        if (!TextUtils.isEmpty(aesKeyStr)) {
            aesKeyArray = DigitUtil.convertAesKeyStrToBytes(aesKeyStr)
        }
        CommandUtil.A_checkAdmin(
            lockData.getUid(),
            GsonUtil.toJson<LockVersion>(lockData.getLockVersion()),
            lockData.getAdminPwd(),
            null,
            lockData.getLockFlagPos(),
            aesKeyArray,
            0,
            null,
            APICommand.OP_RESET_EKEY
        )
    }

    fun resetLock(lockData: LockData) {
        val aesKeyArray: ByteArray = DigitUtil.convertAesKeyStrToBytes(lockData.getAesKeyStr())
        CommandUtil.A_checkAdmin(
            lockData.getUid(),
            GsonUtil.toJson<LockVersion>(lockData.getLockVersion()),
            lockData.getAdminPwd(),
            lockData.getLockKey(),
            lockData.getLockFlagPos(),
            aesKeyArray,
            APICommand.OP_RESET_LOCK
        )
    }

    /**
     *
     * @param controlAction
     * @param keyData
     */
    fun controlLock(controlAction: Int, keyData: LockData) {
        when (controlAction) {
            ControlAction.UNLOCK -> if (keyData.getUserType() == 110302) unlockByUser(keyData) else unlockByAdmin(
                keyData
            )
            ControlAction.LOCK -> lockByUser(keyData)
            ControlAction.ROLLING_GATE_DOWN, ControlAction.ROLLING_GATE_UP, ControlAction.ROLLING_GATE_PAUSE, ControlAction.ROLLING_GATE_LOCK -> operateRollingGate(
                controlAction,
                keyData,
                2
            )
            else -> {}
        }
    }

    fun getMuteModeState(lockData: LockData) {
        operateMuteModeState(1, 0, lockData)
    }

    fun setMuteMode(enable: Boolean, lockData: LockData) {
        operateMuteModeState(ActionType.SET.toInt(), if (enable) 0 else 1, lockData)
    }

    fun getRemoteUnlockSwitchState(lockData: LockData) {
        operateRemoteUnlockProperty(ActionType.GET.toInt(), 0, lockData)
    }

    fun setRemoteUnlockSwitchState(enable: Boolean, lockData: LockData) {
        operateRemoteUnlockProperty(ActionType.SET.toInt(), if (enable) 1 else 0, lockData)
    }

    fun getPasscodeVisibleSwitchState(lockData: LockData) {
        operatePasscodeVisibleMode(ActionType.GET.toInt(), lockData)
    }

    fun setPasscodeVisibleSwitchState(enable: Boolean, lockData: LockData) {
        operatePasscodeVisibleMode(if (enable) 3 else 2, lockData)
    }

    /**
     *
     * @param operateType  1 - query 、 2 - modify
     * @param state  1 - on、0 - off
     * @param lockData lock data
     */
    private fun operateMuteModeState(operateType: Int, state: Int, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_AUDIO_MANAGEMENT)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        transferData.setOp(operateType)
        transferData.setOpValue(state)
        CommandUtil.A_checkAdmin(transferData)
    }

    /**
     *
     * @param operateType  1 - query 、 2 - modify
     * @param state  1 - on、0 - off
     * @param lockData lock data
     */
    private fun operateRemoteUnlockProperty(operateType: Int, state: Int, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_CONTROL_REMOTE_UNLOCK)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        transferData.setOp(operateType)
        transferData.setOpValue(state)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun setLockTime(timestamp: Long, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_CALIBRATE_TIME)
        transferData.setCalibationTime(timestamp)
        CommandUtil.C_calibationTime(transferData)
    }

    fun getLockTime(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_GET_LOCK_TIME)
        CommandUtil.getLockTime(transferData)
    }

    fun getOperationLog(logType: Int, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setLogType(logType)
        if (logType == LogType.ALL) {
            BluetoothImpl.Companion.getInstance().clearRecordCnt()
            transferData.setSeq(0.toShort())
        } else {
            transferData.setSeq(0xffff.toShort())
        }
        CommandUtil.getOperateLog(transferData)
    }

    fun getBatteryLevel(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_GET_POW)
        transferData.setCommand(Command.Companion.COMM_SEARCHE_DEVICE_FEATURE)
        CommandUtil.getPow(transferData)
    }

    fun getLockVersion() {
        CommandUtil.E_getLockVersion(APICommand.OP_GET_LOCK_VERSION)
    }

    fun getSpecialValue(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SEARCH_DEVICE_FEATURE)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun recoverLockData(dataJson: String?, dataType: Int, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_RECOVERY_DATA)
        var command: Byte = 0
        when (dataType) {
            1 -> command = Command.Companion.COMM_MANAGE_KEYBOARD_PASSWORD
            2 -> command = Command.Companion.COMM_IC_MANAGE
            3 -> command = Command.Companion.COMM_FR_MANAGE
            else -> {}
        }
        transferData.setCommand(command)
        transferData.setOp(dataType)
        transferData.setJson(dataJson)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getLockSystemInfo(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_GET_DEVICE_INFO)
        //        CommandUtil.readDeviceInfo(transferData);
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getLockStatus(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_GET_LOCK_SWITCH_STATE)
        transferData.setCommand(Command.Companion.COMM_SEARCH_BICYCLE_STATUS)
        CommandUtil.searchBicycleStatus(transferData)
    }

    fun setAutomaticLockingPeriod(period: Int, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SET_AUTO_LOCK_TIME)
        transferData.setCommand(Command.Companion.COMM_AUTO_LOCK_MANAGE)
        transferData.setCalibationTime(period.toLong())
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getAutomaticLockingPeriod(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SEARCH_AUTO_LOCK_PERIOD)
        transferData.setCommand(Command.Companion.COMM_AUTO_LOCK_MANAGE)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun createCustomPasscode(
        passcode: String?,
        startDate: Long,
        endDate: Long,
        lockData: LockData
    ) {
        var startDate = startDate
        var endDate = endDate
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_ADD_PERIOD_KEYBOARD_PASSWORD)
        if (startDate == 0L) {
            startDate = Constant.permanentStartDate
        }
        if (endDate == 0L) {
            endDate = Constant.permanentEndDate
        }
        transferData.setStartDate(startDate)
        transferData.setEndDate(endDate)
        transferData.setOriginalPwd(passcode)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun modifyPasscode(
        originalCode: String?,
        newCode: String?,
        startDate: Long,
        endDate: Long,
        lockData: LockData
    ) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_MODIFY_KEYBOARD_PASSWORD)
        transferData.setStartDate(startDate)
        transferData.setEndDate(endDate)
        transferData.setOriginalPwd(originalCode)
        transferData.setNewPwd(newCode)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getAdminPasscode(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_GET_ADMIN_KEYBOARD_PASSWORD)
        transferData.setCommand(Command.Companion.COMM_GET_ADMIN_CODE)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun modifyAdminPasscode(newPasscode: String?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SET_KEYBOARD_PASSWORD)
        transferData.setNewPwd(newPasscode)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun deletePasscode(passcode: String?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_REMOVE_ONE_PASSWORD)
        transferData.setOriginalPwd(passcode)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun resetPasscode(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_INIT_PWD)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getAllValidPasscodes(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SEARCH_PWD)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getPasscodeVerificationParams(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_READ_PWD_PARA)
        transferData.setCommand(Command.Companion.COMM_READ_PWD_PARA)
        CommandUtil.U_checkUserTime(transferData)
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
    fun addICCard(validityInfo: ValidityInfo?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_ADD_IC)
        transferData.setValidityInfo(validityInfo)
        if (validityInfo != null && validityInfo.getStartDate() > 0 && validityInfo.getEndDate() > 0) { //兼容之前的使用方式
            transferData.setStartDate(validityInfo.getStartDate())
            transferData.setEndDate(validityInfo.getEndDate())
        }
        CommandUtil.A_checkAdmin(transferData)
    }

    fun modifyICCardValidityPeriod(validityInfo: ValidityInfo?, CardNum: Long, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_MODIFY_IC_PERIOD)
        transferData.setValidityInfo(validityInfo)
        transferData.setNo(CardNum)
        if (validityInfo != null && validityInfo.getStartDate() > 0 && validityInfo.getEndDate() > 0) { //兼容之前的使用方式
            transferData.setStartDate(validityInfo.getStartDate())
            transferData.setEndDate(validityInfo.getEndDate())
        }
        CommandUtil.A_checkAdmin(transferData)
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
    fun getAllValidICCards(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SEARCH_IC_CARD_NO)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun deleteICCard(cardNum: Long, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_DELETE_IC)
        transferData.setCommand(Command.Companion.COMM_IC_MANAGE)
        transferData.setNo(cardNum)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun lossICCard(cardNum: Long, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_LOSS_IC)
        transferData.setCommand(Command.Companion.COMM_IC_MANAGE)
        transferData.setNo(cardNum)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun clearAllICCard(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_CLEAR_IC)
        transferData.setCommand(Command.Companion.COMM_IC_MANAGE)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun addFingerprint(validityInfo: ValidityInfo?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_ADD_FR)
        transferData.setValidityInfo(validityInfo)
        if (validityInfo != null && validityInfo.getStartDate() > 0 && validityInfo.getEndDate() > 0) { //兼容之前的使用方式
            transferData.setStartDate(validityInfo.getStartDate())
            transferData.setEndDate(validityInfo.getEndDate())
        }
        CommandUtil.A_checkAdmin(transferData)
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
    fun modifyFingerprintValidityPeriod(
        validityInfo: ValidityInfo?,
        FingerprintNum: Long,
        lockData: LockData
    ) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_MODIFY_FR_PERIOD)
        transferData.setValidityInfo(validityInfo)
        transferData.setNo(FingerprintNum)
        if (validityInfo != null && validityInfo.getStartDate() > 0 && validityInfo.getEndDate() > 0) { //兼容之前的使用方式
            transferData.setStartDate(validityInfo.getStartDate())
            transferData.setEndDate(validityInfo.getEndDate())
        }
        CommandUtil.A_checkAdmin(transferData)
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
    fun getAllValidFingerprints(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SEARCH_FR)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun deleteFingerprint(cardNum: Long, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_DELETE_FR)
        transferData.setCommand(Command.Companion.COMM_FR_MANAGE)
        transferData.setNo(cardNum)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun clearAllFingerprints(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_CLEAR_FR)
        transferData.setCommand(Command.Companion.COMM_FR_MANAGE)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun writeFingerprintData(
        fingerprintData: String?,
        tempFingerprintNumber: Int,
        startDate: Long,
        endDate: Long,
        lockData: LockData
    ) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_WRITE_FR)
        transferData.setCommand(Command.Companion.COMM_FR_MANAGE)
        transferData.setStartDate(startDate)
        transferData.setEndDate(endDate)
        transferData.setJson(fingerprintData)
        transferData.setNo(tempFingerprintNumber.toLong())
        CommandUtil.A_checkAdmin(transferData)
    }

    fun enterDfuMode(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_ENTER_DFU_MODE)
        transferData.setCommand(Command.Companion.COMM_ENTER_DFU_MODE)
        CommandUtil.A_checkAdmin(transferData)
    }

    /**
     *
     * @param operateType operation type 1 - get passcode status   2 - hide passcode 3 - show passcode
     * @param lockData lock data
     */
    private fun operatePasscodeVisibleMode(operateType: Int, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SHOW_PASSWORD_ON_SCREEN)
        transferData.setCommand(Command.Companion.COMM_SHOW_PASSWORD)
        transferData.setOp(operateType)
        CommandUtil.A_checkAdmin(transferData)
    }

    private fun unlockByAdmin(keyData: LockData) {
        val startDate: Long = keyData.getStartDate()
        val endDate: Long = keyData.getEndDate()
        val transferData: TransferData = getPreparedData(keyData)
        transferData.setAPICommand(APICommand.OP_UNLOCK_ADMIN)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        transferData.setmUid(keyData.getUid())
        CommandUtil.A_checkAdmin(transferData)
    }

    private fun unlockByUser(keyData: LockData) {
        val startDate: Long = keyData.getStartDate()
        val endDate: Long = keyData.getEndDate()
        val transferData: TransferData = getPreparedData(keyData)
        transferData.setAPICommand(APICommand.OP_UNLOCK_EKEY)
        transferData.setCommand(Command.Companion.COMM_CHECK_USER_TIME)
        transferData.setmUid(keyData.getUid())
        transferData.setStartDate(startDate)
        transferData.setEndDate(endDate)
        CommandUtil.U_checkUserTime(transferData)
    }

    private fun lockByUser(keyData: LockData) {
        val startDate: Long = keyData.getStartDate()
        val endDate: Long = keyData.getEndDate()
        val transferData: TransferData = getPreparedData(keyData)
        transferData.setAPICommand(APICommand.OP_LOCK_EKEY)
        transferData.setCommand(Command.Companion.COMM_CHECK_USER_TIME)
        transferData.setmUid(keyData.getUid())
        transferData.setStartDate(startDate)
        transferData.setEndDate(endDate)
        CommandUtil.U_checkUserTime(transferData)
    }
    //********************************卷闸门***测试未通过******************************
    /**
     *
     * @param controlAction the key witch is pressed
     * @param keyData   ekey data
     * @param operateType       1 - query 、 2 - key operation
     */
    private fun operateRollingGate(controlAction: Int, keyData: LockData, operateType: Int) {
        val startDate: Long = keyData.getStartDate()
        val endDate: Long = keyData.getEndDate()
        val transferData: TransferData = getPreparedData(keyData)
        transferData.setAPICommand(APICommand.OP_REMOTE_CONTROL_DEVICE_MANAGEMENT)
        transferData.setOp(operateType)
        transferData.setOpValue(controlAction)
        transferData.setStartDate(startDate)
        transferData.setEndDate(endDate)
        if (BluetoothImpl.Companion.isCheckedLockPermission && BluetoothImpl.Companion.mConnectionState == BluetoothImpl.Companion.STATE_CONNECTED) {
            val command: Command = Command(LockType.LOCK_TYPE_V3)
            if (lastKey != controlAction) {
                BluetoothImpl.Companion.uniqueidBytes =
                    DigitUtil.integerToByteArray((System.currentTimeMillis() / 1000).toInt())
            }
            if (BluetoothImpl.Companion.unlockPwdBytes != null && BluetoothImpl.Companion.uniqueidBytes != null) {
                CommandUtil_V3.remoteControlManage(
                    command,
                    transferData.getOp().toByte(),
                    transferData.getOpValue().toByte(),
                    BluetoothImpl.Companion.unlockPwdBytes,
                    BluetoothImpl.Companion.uniqueidBytes,
                    transferData.getAesKeyArray()
                )
            }
        } else if (BluetoothImpl.Companion.isCanSendCommandAgain) {
            lastKey = controlAction
            BluetoothImpl.Companion.isCanSendCommandAgain = false
            if (keyData.getUserType() == 1) {
                transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
                CommandUtil.A_checkAdmin(transferData)
            } else {
                transferData.setCommand(Command.Companion.COMM_CHECK_USER_TIME)
                CommandUtil.U_checkUserTime(transferData)
            }
        }
    }

    fun getPassageMode(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_QUERY_PASSAGE_MODE)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun setPassageMode(config: PassageModeConfig, lockData: LockData) {
        val type: PassageModeType = config.getModeType()
        val weekDays: String = config.getRepeatWeekOrDays()
        //        if (type == PassageModeType.Weekly) {
//            weekDays = DigitUtil.convertWeekDays(weekDays);
//        }
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_ADD_OR_MODIFY_PASSAGE_MODE)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        transferData.setOp(type.getValue())
        transferData.setJson(weekDays)
        transferData.setOpValue(config.getMonth())
        transferData.setStartDate(config.getStartDate())
        transferData.setEndDate(config.getEndDate())
        CommandUtil.A_checkAdmin(transferData)
    }

    fun deletePassageMode(config: PassageModeConfig, lockData: LockData) {
        val type: PassageModeType = config.getModeType()
        val weekDays: String = config.getRepeatWeekOrDays()
        //        if (type == PassageModeType.Weekly) {
//            weekDays = DigitUtil.convertWeekDays(weekDays);
//        }
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_DELETE_PASSAGE_MODE)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        transferData.setOp(type.getValue())
        transferData.setJson(weekDays)
        transferData.setOpValue(config.getMonth())
        transferData.setStartDate(config.getStartDate())
        transferData.setEndDate(config.getEndDate())
        CommandUtil.A_checkAdmin(transferData)
    }

    fun clearPassageMode(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_CLEAR_PASSAGE_MODE)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun setLockFreezeState(freeze: Boolean, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_FREEZE_LOCK)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        transferData.setOp(OperationType.MODIFY.toInt())
        transferData.setOpValue(if (freeze) 1 else 0)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getLockFreezeState(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_FREEZE_LOCK)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        transferData.setOp(OperationType.GET_STATE.toInt())
        CommandUtil.A_checkAdmin(transferData)
    }

    fun setLightTime(seconds: Int, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_LOCK_LAMP)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        transferData.setOp(OperationType.MODIFY.toInt())
        transferData.setOpValue(seconds)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getLightTime(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_LOCK_LAMP)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        transferData.setOp(OperationType.GET_STATE.toInt())
        CommandUtil.A_checkAdmin(transferData)
    }

    fun setHotelCardSector(hotelData: HotelData?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setHotelData(hotelData)
        transferData.setAPICommand(APICommand.OP_SET_HOTEL_CARD_SECTION)
        transferData.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun setHotelData(hotelData: HotelData?, lockData: LockData) {
//        hotelData.setParaType(HotelData.TYPE_HOTEL_BUILDING_FLOOR);
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setHotelData(hotelData)
        transferData.setAPICommand(APICommand.OP_SET_HOTEL_DATA)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getHotelData(hotelData: HotelData?, lockData: LockData) {
//        hotelData.setParaType(HotelData.TYPE_HOTEL_BUILDING_FLOOR);
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setHotelData(hotelData)
        transferData.setAPICommand(APICommand.OP_GET_HOTEL_DATA)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun setLiftControlableFloors(hotelData: HotelData, lockData: LockData) {
        hotelData.setParaType(HotelData.Companion.TYPE_HOTEL_BUILDING_FLOOR)
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setHotelData(hotelData)
        transferData.setAPICommand(APICommand.OP_SET_ELEVATOR_CONTROL_FLOORS)
        //        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        CommandUtil.A_checkAdmin(transferData)
    }

    fun setLiftWorkMode(hotelData: HotelData, lockData: LockData) {
        hotelData.setParaType(HotelData.Companion.TYPE_HOTEL_BUILDING_FLOOR)
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setHotelData(hotelData)
        transferData.setAPICommand(APICommand.OP_SET_ELEVATOR_WORK_MODE)
        //        transferData.setCommand(Command.COMM_CHECK_ADMIN);
        CommandUtil.A_checkAdmin(transferData)
    }

    fun activateLiftFloors(activateFloors: List<Int?>?, currentDate: Long, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_ACTIVATE_FLOORS)
        transferData.setActivateFloors(activateFloors)
        transferData.setUnlockDate(currentDate)
        if (!TextUtils.isEmpty(transferData.getAdminPs())) {
            CommandUtil.A_checkAdmin(transferData)
        } else {
            CommandUtil.U_checkUserTime(transferData)
        }
    }

    fun setLockConfig(ttLockConfigType: TTLockConfigType, enable: Boolean, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SET_SWITCH)
        transferData.setOp(ttLockConfigType.getItem())
        if (ttLockConfigType == TTLockConfigType.WIFI_LOCK_POWER_SAVING_MODE) { //省电模式 这里 0 表示开启  跟其它配置项正好相反
            transferData.setOpValue(if (enable) 0 else ttLockConfigType.getItem())
        } else {
            transferData.setOpValue(if (enable) ttLockConfigType.getItem() else 0)
        }
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getLockConfig(ttLockConfigType: TTLockConfigType, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_GET_SWITCH)
        transferData.setOp(ttLockConfigType.getItem())
        CommandUtil.A_checkAdmin(transferData)
    }

    private fun getPreparedData(lockData: LockData): TransferData {
        val transferData = TransferData()
        var aesKeyArray: ByteArray? = null
        val aesKeyStr: String = lockData.getAesKeyStr()
        if (!TextUtils.isEmpty(aesKeyStr)) {
            aesKeyArray = DigitUtil.convertAesKeyStrToBytes(aesKeyStr)
        }
        transferData.setmUid(lockData.getUid())
        transferData.setLockVersion(GsonUtil.toJson<LockVersion>(lockData.getLockVersion()))
        transferData.setAdminPs(lockData.getAdminPwd())
        transferData.setUnlockKey(lockData.getLockKey())
        transferData.setLockFlagPos(lockData.getLockFlagPos())
        transferData.setAesKeyArray(aesKeyArray)
        transferData.setTimezoneOffSet(lockData.getTimezoneRawOffset())
        return transferData
    }

    //todo:
    private fun setLockSwitch(lockData: LockData) {}
    fun setNBAwakeModes(nbAwakeConfig: NBAwakeConfig?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SET_NB_ACTIVATE_MODE)
        transferData.setNbAwakeConfig(nbAwakeConfig)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun setNBAwakeTimes(nbAwakeConfig: NBAwakeConfig?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SET_NB_ACTIVATE_CONFIG)
        transferData.setNbAwakeConfig(nbAwakeConfig)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getNBAwakeModes(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_GET_NB_ACTIVATE_MODE)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getNBAwakeTimes(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_GET_NB_ACTIVATE_CONFIG)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun setUnlockDirection(unlockDirection: UnlockDirection?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SET_UNLOCK_DIRECTION)
        transferData.setUnlockDirection(unlockDirection)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getUnlockDirection(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_GET_UNLOCK_DIRECTION)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getAccessoryBatteryLevel(accessoryInfo: AccessoryInfo?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAccessoryInfo(accessoryInfo)
        transferData.setAPICommand(APICommand.OP_GET_ACCESSORY_BATTERY)
        CommandUtil.getAccessoryBattery(transferData)
    }

    fun addRemote(remoteMac: String?, validityInfo: ValidityInfo?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_ADD_KEY_FOB)
        transferData.setKeyFobMac(remoteMac)
        transferData.setValidityInfo(validityInfo)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun modifyRemoteValidity(remoteMac: String?, validityInfo: ValidityInfo?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_MODIFY_KEY_FOB_PERIOD)
        transferData.setKeyFobMac(remoteMac)
        transferData.setValidityInfo(validityInfo)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun deleteRemote(remoteMac: String?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_DELETE_KEY_FOB)
        transferData.setKeyFobMac(remoteMac)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun clearRemote(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_CLEAR_KEY_FOB)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun scanWifi(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SCAN_WIFI)
        CommandUtil.scanWifi(transferData)
    }

    fun configureWifiAp(wifiName: String?, wifiPassword: String?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setWifiName(wifiName)
        transferData.setWifiPassword(wifiPassword)
        transferData.setAPICommand(APICommand.OP_SET_WIFI)
        CommandUtil.configureWifiAp(transferData)
    }

    fun configureServer(serverAddress: String?, port: Int, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAddress(serverAddress)
        transferData.setPort(port.toShort())
        transferData.setAPICommand(APICommand.OP_SET_SERVER)
        CommandUtil.setServer(transferData)
    }

    fun getWifiInfo(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_GET_WIFI_INFO)
        CommandUtil.getWifiInfo(transferData)
    }

    fun configureStaticIp(ipSetting: IpSetting, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SET_STATIC_IP)
        CommandUtil.configStaticIp(ipSetting, transferData)
    }

    fun setLockSound(soundVolume: SoundVolume?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_SET_LOCK_SOUND)
        transferData.setSoundVolume(soundVolume)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun getLockSound(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_GET_LOCK_SOUND)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun addDoorSensor(doorSensorMac: String?, lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_ADD_DOOR_SENSOR)
        transferData.setDoorSensorMac(doorSensorMac)
        CommandUtil.A_checkAdmin(transferData)
    }

    fun deleteDoorSensor(lockData: LockData) {
        val transferData: TransferData = getPreparedData(lockData)
        transferData.setAPICommand(APICommand.OP_DELETE_DOOR_SENSOR)
        CommandUtil.A_checkAdmin(transferData)
    }
}