package com.ttlock.bl.sdk.api

import com.ttlock.bl.sdk.util.DigitUtil

/**
 * Created by Smartlock on 2016/6/1.
 */
internal object CommandUtil_V2S {
    fun addAdmin(command: Command, adminPassword: String, unlockNumber: String?) {
        val values = ByteArray(14)
        var i = 9
        var j = adminPassword.length - 1
        while (j >= 0) {
            values[i--] = (adminPassword[j--] - '0').toByte()
        }
        while (i >= 0) values[i--] = 0
        val unlock_number = Integer.valueOf(unlockNumber)
        System.arraycopy(DigitUtil.integerToByteArray(unlock_number), 0, values, 10, 4)
        command.setData(values)
    }

    fun checkAdmin(command: Command, adminPs: String, lockFlagPos: Int) {
        val password = adminPs.toByteArray()
        val values = ByteArray(13)
        var i = 9
        var j = password.size - 1
        while (j >= 0) values[i--] = (password[j--] - 0x30).toByte()
        while (i >= 0) values[i--] = 0x00
        values[10] = (lockFlagPos shr 16).toByte()
        values[11] = (lockFlagPos shr 8).toByte()
        values[12] = lockFlagPos.toByte()
        command.setData(values)
    }

    fun unlock(command: Command, sum: String?) {
        val flag: Byte = 0
        val values = ByteArray(5)
        val sum_int = Integer.valueOf(sum)
        System.arraycopy(DigitUtil.integerToByteArray(sum_int), 0, values, 0, 4)
        values[4] = flag
        command.setData(values)
    }

    /**
     * 校准用户时间
     * @param command
     * @param sDateStr
     * @param eDateStr
     * @param lockFlagPos
     */
    fun checkUserTime(command: Command, sDateStr: String, eDateStr: String, lockFlagPos: Int) {
        val values = ByteArray(13)
        val time: ByteArray = DigitUtil.convertTimeToByteArray(sDateStr + eDateStr)
        System.arraycopy(time, 0, values, 0, 10)
        values[10] = (lockFlagPos shr 16 and 0xFF).toByte()
        values[11] = (lockFlagPos shr 8 and 0xFF).toByte()
        values[12] = (lockFlagPos and 0xFF).toByte()
        command.setData(values)
    }

    fun calibationTime_V2S(command: Command, timeStr: String) {
        val timeArray: ByteArray = DigitUtil.convertTimeToByteArray(timeStr)
        command.setData(timeArray)
    }

    fun synPwd(lockType: Int, pwd: String, seq: Int) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_SYN_KEYBOARD_PWD)
        val values = ByteArray(32)
        for (i in 0..29) {
            values[i] = (pwd[i] - '0').toByte()
        }
        values[30] = (seq shr 8 and 0xFF).toByte()
        values[31] = (seq and 0xFF).toByte()
        command.setData(values)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 设置管理员键盘密码
     * @param command
     * @param adminKeyboardPwd 键盘密码
     */
    fun setAdminKeyboardPwd(command: Command, adminKeyboardPwd: String) {
        val values = ByteArray(10)
        val len = adminKeyboardPwd.length
        for (i in 0 until len) {
            values[i] = (adminKeyboardPwd[i] - '0').toByte()
        }
        for (i in len..9) {
            values[i] = 0xFF.toByte()
        }
        command.setData(values)
    }

    /**
     * 设置删除密码
     * @param command
     * @param deletePwd 删除密码
     */
    fun setDeletePwd(command: Command, deletePwd: String) {
        val values = ByteArray(10)
        val len = deletePwd.length
        for (i in 0 until len) {
            values[i] = (deletePwd[i] - '0').toByte()
        }
        for (i in len..9) {
            values[i] = 0xFF.toByte()
        }
        command.setData(values)
    }
}
