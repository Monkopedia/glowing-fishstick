package com.ttlock.bl.sdk.api

import com.ttlock.bl.sdk.util.DigitUtil

/**
 * Created by Smartlock on 2016/6/1.
 */
internal object CommandUtil_V2S_PLUS {
    private const val DBG = false
    fun addAdmin_V2S_PLUS(
        command: Command,
        adminPassword: String,
        unlockNumber: String?,
        aesKeyArray: ByteArray?
    ) {
        val values = ByteArray(14)
        var i = 9
        var j = adminPassword.length - 1
        while (j >= 0) {
            values[i--] = (adminPassword[j--] - '0').toByte()
        }
        while (i >= 0) values[i--] = 0
        val unlock_number = Integer.valueOf(unlockNumber)
        System.arraycopy(DigitUtil.integerToByteArray(unlock_number), 0, values, 10, 4)
        command.setData(values, aesKeyArray)
    }

    fun checkAdmin_V2S_PLUS(
        command: Command,
        adminPs: String,
        unlockKey: String?,
        lockFlagPos: Int,
        aesKeyArray: ByteArray?,
        apiCommand: Int
    ) {
        val values = ByteArray(13)
        val password = adminPs.toByteArray()
        var i = 9
        var j = password.size - 1
        while (j >= 0) values[i--] = (password[j--] - 0x30).toByte()
        while (i >= 0) values[i--] = 0x00
        values[10] = (lockFlagPos shr 16).toByte()
        values[11] = (lockFlagPos shr 8).toByte()
        values[12] = lockFlagPos.toByte()
        command.setData(values, aesKeyArray)
    }

    fun setAdminKeyboardPwd(command: Command, adminKeyboardPwd: String, aesKeyArray: ByteArray?) {
        val values = ByteArray(10)
        val len = adminKeyboardPwd.length
        for (i in 0 until len) {
            values[i] = (adminKeyboardPwd[i] - '0').toByte()
        }
        for (i in len..9) {
            values[i] = 0xFF.toByte()
        }
        command.setData(values, aesKeyArray)
    }

    fun setDeletePwd(command: Command, deletePwd: String, aesKeyArray: ByteArray?) {
        val values = ByteArray(10)
        val len = deletePwd.length
        for (i in 0 until len) {
            values[i] = (deletePwd[i] - '0').toByte()
        }
        for (i in len..9) {
            values[i] = 0xFF.toByte()
        }
        command.setData(values, aesKeyArray)
    }

    fun checkUserTime_V2S_PLUS(
        command: Command,
        sDateStr: String,
        eDateStr: String,
        unlockKey: String?,
        lockFlagPos: Int,
        aesKeyArray: ByteArray?,
        apiCommand: Int
    ) {
        val values = ByteArray(13)
        val time: ByteArray = DigitUtil.convertTimeToByteArray(sDateStr + eDateStr)
        System.arraycopy(time, 0, values, 0, 10)
        values[10] = (lockFlagPos shr 16 and 0xFF).toByte()
        values[11] = (lockFlagPos shr 8 and 0xFF).toByte()
        values[12] = (lockFlagPos and 0xFF).toByte()
        command.setData(values, aesKeyArray)
    }

    fun unlock_V2S_PLUS(command: Command, sum: String?, aesKeyArray: ByteArray?) {
        val flag: Byte = 0x00 // 操作flag
        val values = ByteArray(5)
        val sumI = Integer.valueOf(sum)
        val sumByteArray: ByteArray = DigitUtil.integerToByteArray(sumI)
        System.arraycopy(sumByteArray, 0, values, 0, sumByteArray.size)
        values[4] = flag
        command.setData(values, aesKeyArray)
    }

    /**
     * 同步键盘密码（300个密码)
     * @param lockType
     * @param packet
     * @param seq
     */
    fun synPwd(lockType: Int, packet: ByteArray, seq: Int, aesKeyArray: ByteArray?) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_SYN_KEYBOARD_PWD)
        val values = ByteArray(30)
        for (i in 0..27) {
            values[i] = packet[i]
        }
        // 低位在前，高位在后
        values[28] = seq.toByte()
        values[29] = (seq shr 8).toByte()
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun calibationTime_V2S_PLUS(command: Command, timeStr: String, aesKeyArray: ByteArray) {
        val timeArray: ByteArray = DigitUtil.convertTimeToByteArray(timeStr)
        command.setData(timeArray, aesKeyArray)
    }
}
