package com.ttlock.bl.sdk.api

import android.util.Log
import android.util.TextUtils
import com.ttlock.bl.sdk.api.CommandUtil_V3.FRManage
import com.ttlock.bl.sdk.constant.APICommand
import com.ttlock.bl.sdk.constant.AutoLockOperate
import com.ttlock.bl.sdk.constant.Constant
import com.ttlock.bl.sdk.constant.DeviceInfoType
import com.ttlock.bl.sdk.constant.ICOperate
import com.ttlock.bl.sdk.constant.LockType
import com.ttlock.bl.sdk.device.WirelessKeypad
import com.ttlock.bl.sdk.entity.ConnectParam
import com.ttlock.bl.sdk.entity.IpSetting
import com.ttlock.bl.sdk.entity.LockVersion
import com.ttlock.bl.sdk.entity.TransferData
import com.ttlock.bl.sdk.util.AESUtil
import com.ttlock.bl.sdk.util.DigitUtil
import com.ttlock.bl.sdk.util.LogUtil
import java.lang.Exception
import java.util.*

/**
 * Created by Smartlock on 2016/5/27.
 */
internal object CommandUtil {
    private const val DBG = false

    /**
     * 时间2000.2.1
     */
    const val permanentStartDate = 949338000000L

    /**
     * 时间2099.12.1
     */
    const val permanentEndDate = 4099741200000L
    private val defaultAesKeyArray = byteArrayOf(
        0x98.toByte(),
        0x76.toByte(),
        0x23.toByte(),
        0xE8.toByte(),
        0xA9.toByte(),
        0x23.toByte(),
        0xA1.toByte(),
        0xBB.toByte(),
        0x3D.toByte(),
        0x9E.toByte(),
        0x7D.toByte(),
        0x03.toByte(),
        0x78.toByte(),
        0x12.toByte(),
        0x45.toByte(),
        0x88.toByte()
    )

    /**
     * 获取版本号
     */
    fun E_getLockVersion(apiCommand: Int) {
        val command: Command = Command(LockType.LOCK_TYPE_V3)
        //        byte[] values = new byte[1];
        command.setCommand(Command.Companion.COMM_INITIALIZATION)
        //        values[0] = 0x01;
//        command.setData(values, defaultAesKeyArray);
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand(), apiCommand)
    }

    fun A_addAdmin_V2S_Plus() {}

    /**
     * 今后都转换为这个
     * 校验管理员
     * @param transferData
     */
    fun A_checkAdmin(transferData: TransferData) {
        val command: Command = Command(transferData.getLockVersion())
        command.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        var adminPs: String = transferData.getAdminPs()!!
        var unlockKey: String? = transferData.getUnlockKey()
        if (adminPs.length > 10) {
            adminPs = String(
                DigitUtil.decodeDefaultPassword(
                    DigitUtil.stringDividerByDotToByteArray(adminPs)
                )!!
            )
        }
        if (adminPs.length < 10) {
            adminPs = String.format("%10s", adminPs).replace(" ", "0")
        }
        if (unlockKey != null && unlockKey.length > 10) {
            unlockKey = DigitUtil.decodeDefaultPassword(
                DigitUtil.stringDividerByDotToByteArray(unlockKey)
            )?.let {
                String(
                    it
                )
            }
        }
        transferData.setAdminPs(adminPs)
        transferData.setUnlockKey(unlockKey)
        when (command.getLockType()) {
            LockType.LOCK_TYPE_V2 -> {}
            LockType.LOCK_TYPE_CAR -> CommandUtil_Va.checkAdmin(command, adminPs)
            LockType.LOCK_TYPE_MOBI -> {}
            LockType.LOCK_TYPE_V2S -> CommandUtil_V2S.checkAdmin(
                command,
                adminPs,
                transferData.getLockFlagPos()
            )
            LockType.LOCK_TYPE_V2S_PLUS -> CommandUtil_V2S_PLUS.checkAdmin_V2S_PLUS(
                command,
                adminPs,
                unlockKey,
                transferData.getLockFlagPos(),
                transferData.getAesKeyArray(),
                transferData.getAPICommand()
            )
            LockType.LOCK_TYPE_V3, LockType.LOCK_TYPE_V3_CAR -> CommandUtil_V3.checkAdmin(
                command,
                transferData.getmUid(),
                adminPs,
                unlockKey,
                transferData.getLockFlagPos(),
                transferData.getAesKeyArray(),
                transferData.getAPICommand()
            )
            else -> {}
        }
        transferData.setTransferData(command.buildCommand())
        BluetoothImpl.Companion.getInstance().sendCommand(transferData)
    }

    /**
     * 校验用户时间
     * @param transferData
     */
    fun U_checkUserTime(transferData: TransferData) {
        val command: Command = Command(transferData.getLockVersion())
        command.setCommand(Command.Companion.COMM_CHECK_USER_TIME)
        var unlockKey: String = transferData.getUnlockKey()!!
        var startDate: Long = transferData.getStartDate()
        var endDate: Long = transferData.getEndDate()
        LogUtil.d("startDate:$startDate", DBG)
        LogUtil.d("endDate:$endDate", DBG)
        if (unlockKey.length > 10) {
            unlockKey = String(
                DigitUtil.decodeDefaultPassword(
                    DigitUtil.stringDividerByDotToByteArray(unlockKey)
                )!!
            )
        }
        transferData.setUnlockKey(unlockKey)
        // 永久钥匙
        if (startDate == 0L || endDate == 0L) {
            startDate = permanentStartDate
            endDate = permanentEndDate
        }

        // 时间戳应该没用了
        transferData.setStartDate(startDate)
        transferData.setEndDate(endDate)

        // 根据时间偏移量重新计算时间
        startDate = startDate + transferData.getTimezoneOffSet() - TimeZone.getDefault().getOffset(
            System.currentTimeMillis()
        )
        endDate = endDate + transferData.getTimezoneOffSet() - TimeZone.getDefault().getOffset(
            System.currentTimeMillis()
        )
        val sDateStr: String = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmm")
        val eDateStr: String = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmm")
        when (command.getLockType()) {
            LockType.LOCK_TYPE_V2 -> {}
            LockType.LOCK_TYPE_CAR -> CommandUtil_Va.checkUserTime(command, sDateStr, eDateStr)
            LockType.LOCK_TYPE_MOBI -> {}
            LockType.LOCK_TYPE_V2S -> CommandUtil_V2S.checkUserTime(
                command,
                sDateStr,
                eDateStr,
                transferData.getLockFlagPos()
            )
            LockType.LOCK_TYPE_V2S_PLUS -> CommandUtil_V2S_PLUS.checkUserTime_V2S_PLUS(
                command,
                sDateStr,
                eDateStr,
                unlockKey,
                transferData.getLockFlagPos(),
                transferData.getAesKeyArray(),
                transferData.getAPICommand()
            )
            LockType.LOCK_TYPE_V3, LockType.LOCK_TYPE_V3_CAR -> CommandUtil_V3.checkUserTime(
                command,
                transferData.getmUid(),
                sDateStr,
                eDateStr,
                unlockKey,
                transferData.getLockFlagPos(),
                transferData.getAesKeyArray(),
                transferData.getAPICommand()
            )
            else -> {}
        }
        transferData.setTransferData(command.buildCommand())
        BluetoothImpl.Companion.getInstance().sendCommand(transferData)
    }

    /**
     * 获取AESkey
     */
    fun getAESKey(lockVersion: LockVersion, manufacturer: String, apiCommand: Int) {
        BluetoothImpl.Companion.aesKeyArray = defaultAesKeyArray
        LogUtil.d(lockVersion.toString(), DBG)
        val command: Command = Command(lockVersion)
        command.setCommand(Command.Companion.COMM_GET_AES_KEY)
        command.setData(manufacturer.toByteArray(), defaultAesKeyArray)
        BluetoothImpl.Companion.getInstance()
            .sendCommand(command.buildCommand(), apiCommand, defaultAesKeyArray)
    }

    fun getAESKey(transferData: TransferData) {
        BluetoothImpl.Companion.aesKeyArray = defaultAesKeyArray
        transferData.setAesKeyArray(defaultAesKeyArray)
        val command: Command = Command(LockVersion.Companion.lockVersion_V3)
        command.setCommand(Command.Companion.COMM_GET_AES_KEY)
        command.setData(Constant.VENDOR.toByteArray(), defaultAesKeyArray)
        transferData.setTransferData(command.buildCommand())
        BluetoothImpl.Companion.getInstance().sendCommand(transferData)
    }

    fun V_addAdmin(
        lockType: Int,
        adminPassword: String,
        unlockNumber: String,
        aesKeyArray: ByteArray?
    ) {
        LogUtil.d("lockType=$lockType adminPassword=$adminPassword unlockNumber=$unlockNumber", DBG)
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_ADD_ADMIN)
        when (lockType) {
            LockType.LOCK_TYPE_V2 -> {}
            LockType.LOCK_TYPE_CAR -> CommandUtil_Va.addAdmin(command, adminPassword, unlockNumber)
            LockType.LOCK_TYPE_MOBI -> {}
            LockType.LOCK_TYPE_V2S -> CommandUtil_V2S.addAdmin(command, adminPassword, unlockNumber)
            LockType.LOCK_TYPE_V2S_PLUS -> CommandUtil_V2S_PLUS.addAdmin_V2S_PLUS(
                command,
                adminPassword,
                unlockNumber,
                aesKeyArray
            )
            LockType.LOCK_TYPE_V3, LockType.LOCK_TYPE_V3_CAR -> CommandUtil_V3.addAdmin_V3(
                command,
                adminPassword,
                unlockNumber,
                aesKeyArray
            )
        }
        // TODO:接后续指令
        BluetoothImpl.Companion.getInstance().sendCommand(
            command.buildCommand(),
            adminPassword,
            unlockNumber,
            aesKeyArray,
            APICommand.OP_ADD_ADMIN
        )
    }

    fun A_checkAdmin(
        uid: Int,
        lockVersionString: String?,
        adminPs: String,
        unlockKey: String?,
        lockFlagPos: Int,
        aesKeyArray: ByteArray?,
        string: String?,
        apiCommand: Int
    ) {
        A_checkAdmin(
            uid,
            lockVersionString,
            adminPs,
            unlockKey,
            lockFlagPos,
            aesKeyArray,
            0,
            string,
            apiCommand
        )
    }

    fun A_checkAdmin(
        uid: Int,
        lockVersionString: String?,
        adminPs: String,
        unlockKey: String?,
        lockFlagPos: Int,
        aesKeyArray: ByteArray?,
        apiCommand: Int
    ) {
        A_checkAdmin(
            uid,
            lockVersionString,
            adminPs,
            unlockKey,
            lockFlagPos,
            aesKeyArray,
            0,
            null,
            apiCommand
        )
    }

    // TODO:
    fun A_checkAdmin(
        uid: Int,
        lockVersionString: String?,
        adminPs: String,
        unlockKey: String,
        lockFlagPos: Int,
        aesKeyArray: ByteArray?,
        validPwdNum: Int,
        pwdType: Int,
        originalPwd: String?,
        string: String?,
        startDate: Long,
        endDate: Long,
        apiCommand: Int
    ) {
        var adminPs = adminPs
        var unlockKey = unlockKey
        val command = Command(lockVersionString)
        command.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        if (adminPs.length > 10) {
            adminPs = String(
                DigitUtil.decodeDefaultPassword(
                    DigitUtil.stringDividerByDotToByteArray(adminPs)
                )!!
            )
        }
        if (adminPs.length < 10) {
            adminPs = String.format("%10s", adminPs).replace(" ", "0")
        }
        if (unlockKey.length > 10) {
            unlockKey = String(
                DigitUtil.decodeDefaultPassword(
                    DigitUtil.stringDividerByDotToByteArray(unlockKey)
                )!!
            )
        }
        when (command.getLockType()) {
            LockType.LOCK_TYPE_V2 -> {}
            LockType.LOCK_TYPE_CAR -> CommandUtil_Va.checkAdmin(command, adminPs)
            LockType.LOCK_TYPE_MOBI -> {}
            LockType.LOCK_TYPE_V2S -> CommandUtil_V2S.checkAdmin(command, adminPs, lockFlagPos)
            LockType.LOCK_TYPE_V2S_PLUS -> CommandUtil_V2S_PLUS.checkAdmin_V2S_PLUS(
                command,
                adminPs,
                unlockKey,
                lockFlagPos,
                aesKeyArray,
                apiCommand
            )
            LockType.LOCK_TYPE_V3, LockType.LOCK_TYPE_V3_CAR -> CommandUtil_V3.checkAdmin(
                command,
                uid,
                adminPs,
                unlockKey,
                lockFlagPos,
                aesKeyArray,
                apiCommand
            )
        }
        BluetoothImpl.Companion.getInstance().sendCommand(
            command.buildCommand(),
            adminPs,
            unlockKey,
            lockFlagPos,
            aesKeyArray,
            validPwdNum.toByte(),
            pwdType.toByte(),
            originalPwd,
            string,
            startDate,
            endDate,
            apiCommand
        )
    }

    fun A_checkAdmin(
        uid: Int,
        lockVersionString: String?,
        adminPs: String,
        unlockKey: String?,
        lockFlagPos: Int,
        aesKeyArray: ByteArray?,
        validPwdNum: Int,
        string: String?,
        apiCommand: Int
    ) {
        var adminPs = adminPs
        var unlockKey = unlockKey
        val command = Command(lockVersionString)
        command.setCommand(Command.Companion.COMM_CHECK_ADMIN)
        if (adminPs.length > 10) {
            adminPs = String(
                DigitUtil.decodeDefaultPassword(
                    DigitUtil.stringDividerByDotToByteArray(adminPs)
                )!!
            )
        }
        if (adminPs.length < 10) {
            adminPs = String.format("%10s", adminPs).replace(" ", "0")
        }
        if (unlockKey != null && unlockKey.length > 10) {
            unlockKey = String(
                DigitUtil.decodeDefaultPassword(
                    DigitUtil.stringDividerByDotToByteArray(unlockKey)
                )!!
            )
        }
        when (command.getLockType()) {
            LockType.LOCK_TYPE_V2 -> {}
            LockType.LOCK_TYPE_CAR -> CommandUtil_Va.checkAdmin(command, adminPs)
            LockType.LOCK_TYPE_MOBI -> {}
            LockType.LOCK_TYPE_V2S -> CommandUtil_V2S.checkAdmin(command, adminPs, lockFlagPos)
            LockType.LOCK_TYPE_V2S_PLUS -> CommandUtil_V2S_PLUS.checkAdmin_V2S_PLUS(
                command,
                adminPs,
                unlockKey,
                lockFlagPos,
                aesKeyArray,
                apiCommand
            )
            LockType.LOCK_TYPE_V3, LockType.LOCK_TYPE_V3_CAR -> CommandUtil_V3.checkAdmin(
                command,
                uid,
                adminPs,
                unlockKey,
                lockFlagPos,
                aesKeyArray,
                apiCommand
            )
            else -> {}
        }
        BluetoothImpl.Companion.getInstance().sendCommand(
            command.buildCommand(),
            adminPs,
            unlockKey,
            lockFlagPos,
            aesKeyArray,
            validPwdNum.toByte(),
            string,
            apiCommand
        )
    }

    /**
     * 设置管理员键盘密码
     */
    fun S_setAdminKeyboardPwd(
        lockType: Int,
        adminKeyboardPassword: String?,
        aesKeyArray: ByteArray?
    ) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_SET_ADMIN_KEYBOARD_PWD)
        when (lockType) {
            LockType.LOCK_TYPE_V2 -> {}
            LockType.LOCK_TYPE_V2S -> CommandUtil_V2S.setAdminKeyboardPwd(
                command,
                adminKeyboardPassword!!
            )
            LockType.LOCK_TYPE_V2S_PLUS -> CommandUtil_V2S_PLUS.setAdminKeyboardPwd(
                command,
                adminKeyboardPassword!!,
                aesKeyArray
            )
            LockType.LOCK_TYPE_V3 -> CommandUtil_V3.setAdminKeyboardPwd(
                command,
                adminKeyboardPassword!!,
                aesKeyArray
            )
            else -> {}
        }
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 设置删除密码
     * @param lockType
     * @param deletePwd 删除密码
     * @param aesKeyArray
     */
    fun D_setDeletePassword(lockType: Int, deletePwd: String?, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_SET_DELETE_PWD)
        when (lockType) {
            LockType.LOCK_TYPE_V2 -> {}
            LockType.LOCK_TYPE_V2S -> CommandUtil_V2S.setDeletePwd(command, deletePwd!!)
            LockType.LOCK_TYPE_V2S_PLUS -> CommandUtil_V2S_PLUS.setDeletePwd(
                command,
                deletePwd!!,
                aesKeyArray
            )
            LockType.LOCK_TYPE_V3 -> CommandUtil_V3.setDeletePwd(command, deletePwd!!, aesKeyArray)
        }
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    @Deprecated("")
    fun U_checkUserTime(
        uid: Int,
        lockVersionString: String?,
        startDate: Long,
        endDate: Long,
        unlockKey: String,
        lockFlagPos: Int,
        aesKeyArray: ByteArray?,
        date: Long,
        apiCommand: Int
    ) {
        var startDate = startDate
        var endDate = endDate
        var unlockKey = unlockKey
        val command = Command(lockVersionString)
        command.setCommand(Command.Companion.COMM_CHECK_USER_TIME)
        if (unlockKey.length > 10) {
            unlockKey = String(
                DigitUtil.decodeDefaultPassword(
                    DigitUtil.stringDividerByDotToByteArray(unlockKey)
                )!!
            )
        }
        // 永久钥匙
        if (startDate == 0L) startDate = permanentStartDate
        if (endDate == 0L) endDate = permanentEndDate
        val sDateStr: String = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmm")
        val eDateStr: String = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmm")
        when (command.getLockType()) {
            LockType.LOCK_TYPE_V2 -> {}
            LockType.LOCK_TYPE_CAR -> CommandUtil_Va.checkUserTime(command, sDateStr, eDateStr)
            LockType.LOCK_TYPE_MOBI -> {}
            LockType.LOCK_TYPE_V2S -> // TODO:
                CommandUtil_V2S.checkUserTime(command, sDateStr, eDateStr, lockFlagPos)
            LockType.LOCK_TYPE_V2S_PLUS -> CommandUtil_V2S_PLUS.checkUserTime_V2S_PLUS(
                command,
                sDateStr,
                eDateStr,
                unlockKey,
                lockFlagPos,
                aesKeyArray,
                apiCommand
            )
            LockType.LOCK_TYPE_V3, LockType.LOCK_TYPE_V3_CAR -> //                sDateStr = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmmss");
//                eDateStr = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmmss");
                CommandUtil_V3.checkUserTime(
                    command,
                    uid,
                    sDateStr,
                    eDateStr,
                    unlockKey,
                    lockFlagPos,
                    aesKeyArray,
                    apiCommand
                )
        }
        BluetoothImpl.Companion.getInstance()
            .sendCommand(command.buildCommand(), uid, unlockKey, aesKeyArray, date, apiCommand)
    }

    fun G_unlock(
        lockType: Int,
        unlockKey: String?,
        psFromLock: ByteArray?,
        aesKeyArray: ByteArray?,
        unlockDate: Long,
        timezoneRawOffSet: Long
    ) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_UNLOCK)
        var psFromLockL: Long = 0
        var unlockKeyL: Long = 0
        var sum: String? = null
        when (lockType) {
            LockType.LOCK_TYPE_V2 -> {
                psFromLockL = java.lang.Long.valueOf(String(psFromLock!!))
                unlockKeyL = java.lang.Long.valueOf(unlockKey)
                sum = DigitUtil.getUnlockPassword(psFromLockL, unlockKeyL)
            }
            LockType.LOCK_TYPE_CAR -> {
                psFromLockL = java.lang.Long.valueOf(String(psFromLock!!))
                unlockKeyL = java.lang.Long.valueOf(unlockKey)
                sum = DigitUtil.getUnlockPassword(psFromLockL, unlockKeyL)
                // 车位锁调用相反的
                command.setCommand(Command.Companion.COMM_LOCK)
                CommandUtil_Va.up_down(command, sum)
            }
            LockType.LOCK_TYPE_MOBI -> {}
            LockType.LOCK_TYPE_V2S -> {
                psFromLockL = DigitUtil.fourBytesToLong(psFromLock!!)
                unlockKeyL = java.lang.Long.valueOf(unlockKey)
                sum = DigitUtil.getUnlockPwd_new(psFromLockL, unlockKeyL)
                CommandUtil_V2S.unlock(command, sum)
            }
            LockType.LOCK_TYPE_V2S_PLUS -> {
                psFromLockL = DigitUtil.fourBytesToLong(psFromLock!!)
                unlockKeyL = java.lang.Long.valueOf(unlockKey)
                sum = DigitUtil.getUnlockPwd_new(psFromLockL, unlockKeyL)
                CommandUtil_V2S_PLUS.unlock_V2S_PLUS(command, sum, aesKeyArray)
            }
            LockType.LOCK_TYPE_V3, LockType.LOCK_TYPE_V3_CAR -> {
                psFromLockL = DigitUtil.fourBytesToLong(psFromLock!!)
                unlockKeyL = java.lang.Long.valueOf(unlockKey)
                sum = DigitUtil.getUnlockPwd_new(psFromLockL, unlockKeyL)
                CommandUtil_V3.unlock(command, sum, unlockDate, aesKeyArray, timezoneRawOffSet)
            }
        }
        // TODO:接后续指令
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 关锁
     * @param lockType
     * @param unlockKey
     * @param psFromLock
     * @param aesKeyArray
     */
    fun L_lock(lockType: Int, unlockKey: String?, psFromLock: ByteArray?, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        // 车位锁进行相反操作
        command.setCommand(Command.Companion.COMM_UNLOCK)
        var psFromLockL: Long = 0
        var unlockKeyL: Long = 0
        var sum: String? = null
        when (lockType) {
            LockType.LOCK_TYPE_CAR -> {
                psFromLockL = java.lang.Long.valueOf(String(psFromLock!!))
                unlockKeyL = java.lang.Long.valueOf(unlockKey)
                sum = DigitUtil.getUnlockPassword(psFromLockL, unlockKeyL)
                CommandUtil_Va.up_down(command, sum)
            }
            LockType.LOCK_TYPE_MOBI -> {}
        }
        // TODO:接后续指令
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun checkRandom(
        lockType: Int,
        unlockKey: String?,
        psFromLock: ByteArray?,
        aesKeyArray: ByteArray?
    ) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_CHECK_RANDOM)
        val psFromLockL: Long = DigitUtil.fourBytesToLong(psFromLock!!)
        val unlockKeyL = java.lang.Long.valueOf(unlockKey)
        val sum: String = DigitUtil.getUnlockPwd_new(psFromLockL, unlockKeyL)
        CommandUtil_V3.checkRandom(command, sum, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    //    @Deprecated
    //    public static void C_calibationTime(int uid, String lockVersionString, String unlockKey, long date, int lockFlagPos, byte[] aesKeyArray, int apiCommand) {
    //        U_checkUserTime(uid, lockVersionString, permanentStartDate, permanentEndDate, unlockKey, lockFlagPos, aesKeyArray, date, apiCommand);
    //    }
    fun C_calibationTime(transferData: TransferData) {
        transferData.setStartDate(permanentStartDate)
        transferData.setEndDate(permanentEndDate)
        U_checkUserTime(transferData)
    }

    /**
     * 校准时间
     * @param lockType
     * @param date                时间撮
     * @param timezoneRawOffSet   时间偏移量
     * @param aesKeyArray
     */
    fun C_calibationTime(
        lockType: Int,
        date: Long,
        timezoneRawOffSet: Long,
        aesKeyArray: ByteArray
    ) {
        var date = date
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_TIME_CALIBRATE)
        // 根据时区来重新计算时间
        date =
            date + timezoneRawOffSet - TimeZone.getDefault().getOffset(System.currentTimeMillis())
        LogUtil.d("timezoneRawOffSet:$timezoneRawOffSet", DBG)
        LogUtil.d(
            "TimeZone.getDefault().getOffset(System.currentTimeMillis()):" + TimeZone.getDefault()
                .getOffset(
                    System.currentTimeMillis()
                ),
            DBG
        )
        LogUtil.d("date:$date", DBG)
        val timeStr: String = DigitUtil.formateDateFromLong(date, "yyMMddHHmmss")
        LogUtil.d("timeStr:$timeStr", DBG)
        when (lockType) {
            LockType.LOCK_TYPE_V2 -> CommandUtil_V2.calibationTime_V2(
                command,
                timeStr.substring(0, timeStr.length - 2)
            )
            LockType.LOCK_TYPE_CAR -> CommandUtil_Va.calibationTime(
                command,
                timeStr.substring(0, timeStr.length - 2)
            )
            LockType.LOCK_TYPE_MOBI -> {}
            LockType.LOCK_TYPE_V2S -> CommandUtil_V2S.calibationTime_V2S(
                command,
                timeStr.substring(0, timeStr.length - 2)
            )
            LockType.LOCK_TYPE_V2S_PLUS -> CommandUtil_V2S_PLUS.calibationTime_V2S_PLUS(
                command,
                timeStr.substring(0, timeStr.length - 2),
                aesKeyArray
            )
            LockType.LOCK_TYPE_V3, LockType.LOCK_TYPE_V3_CAR -> CommandUtil_V3.calibationTime_V3(
                command,
                timeStr,
                aesKeyArray
            )
            else -> {}
        }
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun N_setLockname(lockType: Int, lockname: String, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_SET_LOCK_NAME)
        CommandUtil_V3.setLockname(command, lockname, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun AT_setLockname(lockType: Int, lockname: String, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(0xFF.toByte()) // AT指令
        command.setData("AT+NAME=$lockname".toByteArray(), aesKeyArray)
        //        CommandUtil_V3.setLockname(command, lockname, aesKeyArray);
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun R_resetLock(lockType: Int) {
        LogUtil.d("lockType:$lockType")
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_RESET_LOCK)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }
    //    public static void resetKeyboardPassword(int lockType, byte validPwdNum, byte[] aesKeyArray) {
    //        Command command = new Command(lockType);
    //        command.setCommand(Command.COMM_INIT_PASSWORDS);
    //        String pwdInfo = null;
    //        long timestamp = System.currentTimeMillis();
    //        String pwdInfoOrigin = null;
    //        byte[] values = new byte[]{validPwdNum};
    //        switch (lockType) {
    //            case LockType.LOCK_TYPE_V2S:
    //                break;
    //            case LockType.LOCK_TYPE_V2S_PLUS:
    //                pwdInfoOrigin = CommandUtil_V2S_PLUS.resetKeyboardPassword(command, validPwdNum, timestamp, aesKeyArray);
    //                break;
    //            case LockType.LOCK_TYPE_V3:
    //                break;
    //        }
    //        pwdInfo = encry(pwdInfoOrigin, timestamp);
    //        BluetoothImpl.getInstance().sendCommand(command.buildCommand(), pwdInfo, timestamp, APICommand.OP_RESET_KEYBOARD_PASSWORD);
    //    }
    /**
     * 三代锁
     * @param lockType
     * @param validPwdNum
     * @param aesKeyArray
     */
    fun resetKeyboardPasswordCount(lockType: Int, validPwdNum: Byte, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_RESET_KEYBOARD_PWD_COUNT)
        val values = byteArrayOf(validPwdNum)
        when (lockType) {
            LockType.LOCK_TYPE_V3 -> CommandUtil_V3.resetKeyboardPasswordCount(
                command,
                values,
                aesKeyArray
            )
        }
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    @Deprecated("")
    fun getLockTime(lockVersion: String?) {
        val command = Command(lockVersion)
        command.setCommand(Command.Companion.COMM_GET_LOCK_TIME)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun getLockTime(transferData: TransferData) {
        val command: Command = Command(transferData.getLockVersion())
        command.setCommand(Command.Companion.COMM_GET_LOCK_TIME)
        transferData.setTransferData(command.buildCommand())
        BluetoothImpl.Companion.getInstance().sendCommand(transferData)
    }

    /**
     * 获取操作日志
     * @param lockVersion
     * @param aesKeyArray
     */
    @Deprecated("")
    fun getOperateLog(lockVersion: String?, aesKeyArray: ByteArray?) {
        val command = Command(lockVersion)
        when (command.getLockType()) {
            LockType.LOCK_TYPE_CAR -> command.setCommand(Command.Companion.COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED)
            LockType.LOCK_TYPE_V3, LockType.LOCK_TYPE_V3_CAR -> {
                command.setCommand(Command.Companion.COMM_GET_OPERATE_LOG)
                // 初始请求序列设置的默认值
                CommandUtil_V3.getOperateLog(command, 0xffff.toShort(), aesKeyArray)
            }
            else -> {}
        }
        BluetoothImpl.Companion.getInstance()
            .sendCommand(command.buildCommand(), APICommand.OP_GET_OPERATE_LOG)
    }

    /**
     * 获取操作日志
     * @param transferData
     */
    fun getOperateLog(transferData: TransferData) {
        Log.d("OMG", "=getOperateLog=")
        val command: Command = Command(transferData.getLockVersion())
        when (command.getLockType()) {
            LockType.LOCK_TYPE_CAR -> command.setCommand(Command.Companion.COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED)
            LockType.LOCK_TYPE_V3, LockType.LOCK_TYPE_V3_CAR -> {
                command.setCommand(Command.Companion.COMM_GET_OPERATE_LOG)
                // 初始请求序列设置的默认值
                CommandUtil_V3.getOperateLog(
                    command,
                    transferData.getSeq(),
                    transferData.getAesKeyArray()
                )
            }
            else -> {}
        }
        transferData.setAPICommand(APICommand.OP_GET_OPERATE_LOG)
        transferData.setTransferData(command.buildCommand())
        BluetoothImpl.Companion.getInstance().sendCommand(transferData)
        Log.d("OMG", "=sendCommand=")
    }

    fun getOperateLog(lockVersion: String?, seq: Short, aesKeyArray: ByteArray?) {
        val command = Command(lockVersion)
        command.setCommand(Command.Companion.COMM_GET_OPERATE_LOG)
        CommandUtil_V3.getOperateLog(command, seq, aesKeyArray)
        BluetoothImpl.Companion.getInstance()
            .sendCommand(command.buildCommand(), APICommand.OP_GET_OPERATE_LOG)
    }

    fun getOperateLog(lockType: Int, seq: Short, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_GET_OPERATE_LOG)
        CommandUtil_V3.getOperateLog(command, seq, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun getValidKeyboardPassword(lockVersion: String?, seq: Short, aesKeyArray: ByteArray?) {
        val command = Command(lockVersion)
        command.setCommand(Command.Companion.COMM_GET_VALID_KEYBOARD_PASSWORD)
        CommandUtil_V3.getValidKeyboardPassword(command, seq, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun getValidKeyboardPassword(lockType: Int, seq: Short, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_GET_VALID_KEYBOARD_PASSWORD)
        CommandUtil_V3.getValidKeyboardPassword(command, seq, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun manageKeyboardPassword(
        lockType: Int,
        pwdOperateType: Byte,
        keyboardPwdType: Byte,
        originalPwd: String?,
        newPwd: String?,
        startDate: Long,
        endDate: Long,
        defaultAesKeyArray: ByteArray?
    ) {
        manageKeyboardPassword(
            lockType,
            pwdOperateType,
            keyboardPwdType,
            0,
            originalPwd,
            newPwd,
            startDate,
            endDate,
            defaultAesKeyArray,
            -1
        )
    }

    fun manageKeyboardPassword(
        lockType: Int,
        pwdOperateType: Byte,
        keyboardPwdType: Byte,
        originalPwd: String?,
        newPwd: String?,
        startDate: Long,
        endDate: Long,
        defaultAesKeyArray: ByteArray?,
        timezoneOffset: Long
    ) {
        manageKeyboardPassword(
            lockType,
            pwdOperateType,
            keyboardPwdType,
            0,
            originalPwd,
            newPwd,
            startDate,
            endDate,
            defaultAesKeyArray,
            timezoneOffset
        )
    }

    @JvmOverloads
    fun manageKeyboardPassword(
        lockType: Int,
        pwdOperateType: Byte,
        keyboardPwdType: Byte,
        circleType: Int,
        originalPwd: String?,
        newPwd: String?,
        startDate: Long,
        endDate: Long,
        defaultAesKeyArray: ByteArray?,
        timezoneOffset: Long = -1
    ) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_MANAGE_KEYBOARD_PASSWORD)
        CommandUtil_V3.manageKeyboardPassword(
            command,
            pwdOperateType,
            keyboardPwdType,
            circleType,
            originalPwd!!,
            newPwd!!,
            startDate,
            endDate,
            defaultAesKeyArray,
            timezoneOffset
        )
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun encry(pwdInfoSource: String, timestamp: Long): String {
        val timestampStr = String.format("%-16d", timestamp).replace(" ", "0").substring(0, 16)
        LogUtil.d("$pwdInfoSource $timestampStr", DBG)
        val encryRes: ByteArray =
            AESUtil.aesEncrypt(pwdInfoSource.toByteArray(), timestampStr.toByteArray())!!
        return Base64.getEncoder().encodeToString(encryRes)
    }

    /**
     * 判断添加管理员所有操作完成指令
     */
    fun operateFinished(lockType: Int) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 查询设备特征
     * @param lockType
     * @return
     */
    fun searchDeviceFeature(lockType: Int) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_SEARCHE_DEVICE_FEATURE)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 查询IC卡号
     * @param lockType
     * @param seq
     * @param aesKeyArray
     */
    fun searchICCardNo(lockType: Int, seq: Short, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_IC_MANAGE)
        CommandUtil_V3.ICManage(command, ICOperate.IC_SEARCH, seq, null, 0, 0, aesKeyArray, 0)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 添加IC卡
     * @param lockType
     * @param aesKeyArray
     */
    fun addICCard(lockType: Int, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_IC_MANAGE)
        CommandUtil_V3.ICManage(command, ICOperate.ADD, 0.toShort(), null, 0L, 0L, aesKeyArray, 0)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 恢复IC卡
     * @param lockType
     * @param aesKeyArray
     */
    fun recoveryICCardPeriod(
        lockType: Int,
        cardNo: String?,
        startDate: Long,
        endDate: Long,
        aesKeyArray: ByteArray?,
        timezoneOffSet: Long
    ) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_IC_MANAGE)
        CommandUtil_V3.ICManage(
            command,
            ICOperate.ADD,
            0.toShort(),
            cardNo,
            startDate,
            endDate,
            aesKeyArray,
            timezoneOffSet
        )
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 修改IC卡有效期
     * @param lockType
     * @param cardNo            卡号
     * @param startDate         起始时间
     * @param endDate           结束时间
     * @param aesKeyArray
     * @param timezoneOffSet    时间偏移量
     */
    fun modifyICCardPeriod(
        lockType: Int,
        cardNo: String?,
        startDate: Long,
        endDate: Long,
        aesKeyArray: ByteArray?,
        timezoneOffSet: Long
    ) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_IC_MANAGE)
        CommandUtil_V3.ICManage(
            command,
            ICOperate.MODIFY,
            0.toShort(),
            cardNo,
            startDate,
            endDate,
            aesKeyArray,
            timezoneOffSet
        )
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 删除IC卡
     * @param lockType
     * @param cardNo
     * @param aesKeyArray
     */
    fun deleteICCard(lockType: Int, cardNo: String?, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_IC_MANAGE)
        CommandUtil_V3.ICManage(
            command,
            ICOperate.DELETE,
            0.toShort(),
            cardNo,
            0,
            0,
            aesKeyArray,
            0
        )
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 清空IC卡
     * @param lockType
     * @param aesKeyArray
     */
    fun clearICCard(lockType: Int, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_IC_MANAGE)
        CommandUtil_V3.ICManage(command, ICOperate.CLEAR, 0.toShort(), null, 0, 0, aesKeyArray, 0)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 设置手环KEY
     * @param lockType
     * @param wristKey
     * @param aesKeyArray
     */
    fun setWristbandKey(lockType: Int, wristKey: String?, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_SET_WRIST_BAND_KEY)
        CommandUtil_V3.setWristKey(command, wristKey!!, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun searchPasscode(lockType: Int, seq: Short, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_PWD_LIST)
        CommandUtil_V3.searchPwd(command, seq, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 查询指纹
     * @param lockType
     * @param seq
     * @param aesKeyArray
     */
    fun searchFRNo(lockType: Int, seq: Short, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_FR_MANAGE)
        FRManage(command, ICOperate.FR_SEARCH, seq, 0, 0, 0, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 添加指纹
     * @param lockType
     * @param aesKeyArray
     */
    fun addFR(lockType: Int, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_FR_MANAGE)
        FRManage(command, ICOperate.ADD, 0.toShort(), 0, 0L, 0L, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 修改指纹有效期
     * @param lockType
     * @param FRNo 卡号
     * @param startDate
     * @param endDate
     * @param aesKeyArray
     * @param timezoneOffSet    时间偏移量
     */
    fun modifyFRPeriod(
        lockType: Int,
        FRNo: Long,
        startDate: Long,
        endDate: Long,
        aesKeyArray: ByteArray?,
        timezoneOffSet: Long
    ) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_FR_MANAGE)
        CommandUtil_V3.FRManage(
            command,
            ICOperate.MODIFY,
            0.toShort(),
            FRNo,
            startDate,
            endDate,
            aesKeyArray,
            timezoneOffSet
        )
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun recoveryFRPeriod(
        lockType: Int,
        FRNo: Long,
        startDate: Long,
        endDate: Long,
        aesKeyArray: ByteArray?,
        timezoneOffSet: Long
    ) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_FR_MANAGE)
        CommandUtil_V3.FRManage(
            command,
            ICOperate.ADD,
            0.toShort(),
            FRNo,
            startDate,
            endDate,
            aesKeyArray,
            timezoneOffSet
        )
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 删除指纹
     * @param lockType
     * @param FRNo
     * @param aesKeyArray
     */
    fun deleteFR(lockType: Int, FRNo: Long, aesKeyArray: ByteArray?) {
        LogUtil.d("FRNo:$FRNo", DBG)
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_FR_MANAGE)
        FRManage(command, ICOperate.DELETE, 0.toShort(), FRNo, 0, 0, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 清空IC卡
     * @param lockType
     * @param aesKeyArray
     */
    fun clearFR(lockType: Int, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_FR_MANAGE)
        CommandUtil_V3.FRManage(command, ICOperate.CLEAR, 0.toShort(), 0, 0, 0, aesKeyArray, 0)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 查询闭锁时间
     * @param lockType
     * @param aesKeyArray
     */
    fun searchAutoLockTime(lockType: Int, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_AUTO_LOCK_MANAGE)
        CommandUtil_V3.autoLockManage(command, AutoLockOperate.SEARCH, 0.toShort(), aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 修改闭锁时间
     * @param lockType
     * @param aesKeyArray
     */
    fun modifyAutoLockTime(lockType: Int, time: Short, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_AUTO_LOCK_MANAGE)
        CommandUtil_V3.autoLockManage(command, AutoLockOperate.MODIFY, time, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 读取设备信息
     */
    fun readDeviceInfo(transferData: TransferData) {
        val command: Command = Command(transferData.getLockVersion())
        command.setCommand(Command.Companion.COMM_READ_DEVICE_INFO)
        command.setData(byteArrayOf(DeviceInfoType.MODEL_NUMBER), transferData.getAesKeyArray())
        // TODO:暂时这样存
        transferData.setTransferData(command.buildCommand())
        BluetoothImpl.Companion.getInstance().sendCommand(transferData)
    }

    /**
     * 读取设备信息
     * @param lockType
     * @param opType    信息类型
     * @param aesKeyArray
     */
    fun readDeviceInfo(lockType: Int, opType: Byte, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_READ_DEVICE_INFO)
        command.setData(byteArrayOf(opType), aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 进入升级模式
     * @param lockType
     * @param aesKeyArray
     */
    fun enterDFUMode(lockType: Int, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_ENTER_DFU_MODE)
        command.setData(Constant.SCIENER.toByteArray(), aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 查询自行车锁当前开关状态
     */
    fun searchBicycleStatus(transferData: TransferData) {
        val command: Command = Command(transferData.getLockVersion())
        command.setCommand(Command.Companion.COMM_SEARCH_BICYCLE_STATUS)
        command.setData(Constant.SCIENER.toByteArray(), transferData.getAesKeyArray())
        transferData.setTransferData(command.buildCommand())
        BluetoothImpl.Companion.getInstance().sendCommand(transferData)
    }

    /**
     * 闭锁
     * @param lockType
     * @param unlockKey
     * @param psFromLock
     * @param aesKeyArray
     * @param unlockDate
     */
    fun lock(
        lockType: Int,
        unlockKey: String?,
        psFromLock: ByteArray?,
        aesKeyArray: ByteArray?,
        unlockDate: Long
    ) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_FUNCTION_LOCK)
        var psFromLockL: Long = 0
        var unlockKeyL: Long = 0
        var sum: String? = null
        psFromLockL = DigitUtil.fourBytesToLong(psFromLock!!)
        unlockKeyL = java.lang.Long.valueOf(unlockKey)
        sum = DigitUtil.getUnlockPwd_new(psFromLockL, unlockKeyL)
        CommandUtil_V3.lock(command, sum, unlockDate, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 屏幕密码管理
     * @param lockType
     * @param opType    1 - 查询  2 - 隐藏  3 - 显示
     * @param aesKeyArray
     */
    fun screenPasscodeManage(lockType: Int, opType: Int, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_SHOW_PASSWORD)
        CommandUtil_V3.screenPasscodeManage(command, opType, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 读取密码参数
     * @param lockType
     * @param aesKeyArray
     */
    fun readPwdPara(lockType: Int, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_READ_PWD_PARA)
        command.setData("".toByteArray(), aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun getPow(transferData: TransferData) {
        val command: Command = Command(transferData.getLockVersion())
        command.setCommand(transferData.getCommand())
        transferData.setTransferData(command.buildCommand())
        BluetoothImpl.Companion.getInstance().sendCommand(transferData)
    }

    fun getAccessoryBattery(transferData: TransferData) {
        val command: Command = Command(transferData.getLockVersion())
        command.setCommand(Command.Companion.COMM_ACCESSORY_BATTERY)
        val values = ByteArray(1 + 6)
        values[0] = transferData.getAccessoryInfo()!!.getAccessoryType()!!.getValue()
        val mac: ByteArray =
            DigitUtil.getReverseMacArray(transferData.getAccessoryInfo()!!.getAccessoryMac()!!)!!
        System.arraycopy(mac, 0, values, 1, 6)
        command.setData(values, transferData.getAesKeyArray())
        transferData.setTransferData(command.buildCommand())
        BluetoothImpl.Companion.getInstance().sendCommand(transferData)
        //        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    }

    fun setLock(param: ConnectParam, keypad: WirelessKeypad) {
        val command = WirelessKeyboardCommand(WirelessKeyboardCommand.Companion.COMM_SET_LOCK)
        command.setMac(keypad.getAddress())
        val data = ByteArray(6 + 16)

        // 低在前高在后
        var macArr: Array<String?> = param.getLockmac()!!.split(":").toTypedArray()
        macArr = command.reverseArray(macArr)!!
        val lockmacBytes: ByteArray = command.hexStringArrToByteArr(macArr)!!
        System.arraycopy(lockmacBytes, 0, data, 0, 6)
        val baseAesKey = ByteArray(6)
        macArr = keypad.getAddress()!!.split(":").toTypedArray()
        macArr = command.reverseArray(macArr)!!
        val keypadMacBytes: ByteArray = command.hexStringArrToByteArr(macArr)!!
        System.arraycopy(keypadMacBytes, 0, baseAesKey, 0, 6)
        //        System.arraycopy(param.getFactoryDate().getBytes(), 0, baseAesKey, 6, 8);
        val defaultAesKey = byteArrayOf(
            0x45,
            0x32,
            0xAE.toByte(),
            0x44,
            0x98.toByte(),
            0x45,
            0xE4.toByte(),
            0x09,
            0xD3.toByte(),
            0x62,
            0x7E,
            0x5F,
            0x56,
            0x91.toByte(),
            0xE3.toByte(),
            0x67
        )
        val encryAesKey: ByteArray = AESUtil.aesEncrypt(baseAesKey, defaultAesKey)!!
        System.arraycopy(encryAesKey, 0, data, 6, 16)
        command.setData(data)
        GattCallbackHelper.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun readDeviceFeature(keypad: WirelessKeypad) {
        val command = WirelessKeyboardCommand(WirelessKeyboardCommand.Companion.COMM_READ_FEATURE)
        command.setMac( keypad.getAddress())
        GattCallbackHelper.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * wifi锁 搜索附近可用wifi
     * @param transferData
     */
    fun scanWifi(transferData: TransferData) {
        // 扫描wifi前清空之前的数据
        BluetoothImpl.Companion.getInstance().clearWifi()
        val command: Command = Command(LockType.LOCK_TYPE_V3)
        command.setCommand(Command.Companion.COMM_SCAN_WIFI)
        command.setData("SCIENER".toByteArray(), transferData.getAesKeyArray())
        transferData.setTransferData(command.buildCommand())
        BluetoothImpl.Companion.getInstance().sendCommand(transferData)
    }

    fun configureWifiAp(transferData: TransferData) {
        try {
            val command: Command = Command(LockType.LOCK_TYPE_V3)
            command.setCommand(Command.Companion.COMM_CONFIG_WIFI_AP)
            if (TextUtils.isEmpty(transferData.getWifiPassword())) {
                transferData.setWifiPassword("")
            }
            val wifiNameBytes: ByteArray = transferData.getWifiName()!!.toByteArray(charset("UTF-8"))
            val wifiNameBytesLength = wifiNameBytes.size.toByte()
            val values =
                ByteArray(1 + wifiNameBytesLength + 1 + transferData.getWifiPassword()!!.length)
            values[0] = wifiNameBytesLength
            System.arraycopy(wifiNameBytes, 0, values, 1, wifiNameBytesLength.toInt())
            values[1 + wifiNameBytesLength] = transferData.getWifiPassword()!!.length.toByte()
            System.arraycopy(
                transferData.getWifiPassword()!!.toByteArray(),
                0,
                values,
                1 + wifiNameBytesLength + 1,
                transferData.getWifiPassword()!!.length
            )
            command.setData(values, transferData.getAesKeyArray())
            transferData.setTransferData(command.buildCommand())
            BluetoothImpl.Companion.getInstance().sendCommand(transferData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setServer(transferData: TransferData) {
        val command: Command = Command(LockType.LOCK_TYPE_V3)
        command.setCommand(Command.Companion.COMM_CONFIG_SERVER)
        if (TextUtils.isEmpty(transferData.getAddress())) {
            transferData.setAddress("")
        }
        val values = ByteArray(1 + transferData.getAddress().length + 2)
        values[0] = transferData.getAddress().length.toByte()
        System.arraycopy(
            transferData.getAddress().toByteArray(),
            0,
            values,
            1,
            transferData.getAddress().length
        )
        val portByteArray: ByteArray = DigitUtil.shortToByteArray(transferData.getPort())
        System.arraycopy(portByteArray, 0, values, 1 + transferData.getAddress().length, 2)
        command.setData(values, transferData.getAesKeyArray())
        transferData.setTransferData(command.buildCommand())
        BluetoothImpl.Companion.getInstance().sendCommand(transferData)
    }

    fun getWifiInfo(transferData: TransferData) {
        val command: Command = Command(LockType.LOCK_TYPE_V3)
        command.setCommand(Command.Companion.COMM_GET_WIFI_INFO)
        command.setData("".toByteArray(), transferData.getAesKeyArray())
        transferData.setTransferData(command.buildCommand())
        BluetoothImpl.Companion.getInstance().sendCommand(transferData)
    }

    /**
     * 是否使用DHCP获取地址
     * （1 byte）	IP地址
     * （4 Bytes）	子网掩码
     * （4 Bytes）	默认网关
     * （4 Bytes）	首选DNS服务器
     * （4 Bytes）	备用DNS服务器
     * （4 Bytes）
     * 0-	固定IP
     * 1-	DHCP自动获取IP地址
     * 固定IP地址时有效，使用DHCP时，参数无效，填全0即可	固定IP地址时有效，使用DHCP时，参数无效，填全0即可	固定IP地址时有效，使用DHCP时，参数无效，填全0即可	固定IP地址时有效，使用DHCP时，参数无效，填全0即可	固定IP地址时有效，使用DHCP时，参数无效，填全0即可
     * @param transferData
     */
    fun configStaticIp(wifiLockNetworkConfiguration: IpSetting, transferData: TransferData) {
        val command: Command = Command(LockType.LOCK_TYPE_V3)
        command.setCommand(Command.Companion.COMM_CONFIG_STATIC_IP)
        val values = ByteArray(1 + 4 + 4 + 4 + 4 + 4)
        values[0] = wifiLockNetworkConfiguration.getType().toByte()
        if (values[0].toInt() == IpSetting.Companion.STATIC_IP) {
            val ip: String = wifiLockNetworkConfiguration.getIpAddress()!!
            if (!TextUtils.isEmpty(ip)) {
                val ipByteArr: ByteArray = DigitUtil.convertIp2Bytes(ip)!!
                System.arraycopy(ipByteArr, 0, values, 1, 4)
            }
            val subMask: String = wifiLockNetworkConfiguration.getSubnetMask()!!
            if (!TextUtils.isEmpty(subMask)) {
                val submaskByteArr: ByteArray = DigitUtil.convertIp2Bytes(subMask)!!
                System.arraycopy(submaskByteArr, 0, values, 5, 4)
            }
            val router: String = wifiLockNetworkConfiguration.getRouter()!!
            if (!TextUtils.isEmpty(router)) {
                val routerByteArr: ByteArray = DigitUtil.convertIp2Bytes(router)!!
                System.arraycopy(routerByteArr, 0, values, 9, 4)
            }
            val dns1: String = wifiLockNetworkConfiguration.getPreferredDns()!!
            if (!TextUtils.isEmpty(dns1)) {
                val dns1ByteArr: ByteArray = DigitUtil.convertIp2Bytes(dns1)!!
                System.arraycopy(dns1ByteArr, 0, values, 13, 4)
            }
            val dns2: String = wifiLockNetworkConfiguration.getAlternateDns()!!
            if (!TextUtils.isEmpty(dns2)) {
                val dns2ByteArr: ByteArray = DigitUtil.convertIp2Bytes(dns2)!!
                System.arraycopy(dns2ByteArr, 0, values, 17, 4)
            }
        }
        command.setData(values, transferData.getAesKeyArray())
        transferData.setTransferData(command.buildCommand())
        BluetoothImpl.Companion.getInstance().sendCommand(transferData)
    }
}
