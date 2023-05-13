package com.ttlock.bl.sdk.api

import com.ttlock.bl.sdk.util.DigitUtil

/**
 * Created by Smartlock on 2016/6/1.
 * 车位锁
 */
internal object CommandUtil_Va {
    /**
     * 添加管理员
     * @param command
     * @param adminPassword
     * @param unlockNumber
     */
    fun addAdmin(command: Command, adminPassword: String, unlockNumber: String) {
        val values = ByteArray(21)
        System.arraycopy(adminPassword.toByteArray(), 0, values, 0, 10)
        values[10] = 0x20
        System.arraycopy(unlockNumber.toByteArray(), 0, values, 11, 10)
        command.setData(values)
    }

    /**
     * 校验管理员
     * @param command
     * @param adminPs
     */
    fun checkAdmin(command: Command, adminPs: String) {
        command.setData(adminPs.toByteArray())
    }
    //    public static void unlock(Command command, String sum) {
    //        byte[] data = new byte[12];
    //        System.arraycopy(sum.getBytes(), 0, data, 0, sum.length());
    //        data[10] = 0x20;
    //        data[11] = '1'; //暂时设置为 1，为了后面的指令可以继续接收
    //        command.setData(data);
    //    }
    //
    //    public static void lock(Command command, String sum) {
    //        byte[] data = new byte[12];
    //        System.arraycopy(sum.getBytes(), 0, data, 0, sum.length());
    //        data[10] = 0x20;
    //        data[11] = '1'; //暂时设置为 1，为了后面的指令可以继续接收
    //        command.setData(data);
    //    }
    /**
     * 车位锁升降数据格式
     * @param command
     * @param sum
     */
    fun up_down(command: Command, sum: String) {
        val data = ByteArray(12)
        System.arraycopy(sum.toByteArray(), 0, data, 0, sum.length)
        data[10] = 0x20
        data[11] = '1'.code.toByte() // 暂时设置为 1，为了后面的指令可以继续接收
        command.setData(data)
    }

    /**
     * 校验用户期限
     * @param command
     * @param sDateStr
     * @param eDateStr
     */
    fun checkUserTime(command: Command, sDateStr: String, eDateStr: String) {
        val values: ByteArray = DigitUtil.convertTimeToByteArray(sDateStr + eDateStr)
        command.setData(values)
    }

    /**
     * 校准时间
     * @param command
     * @param timeStr
     */
    fun calibationTime(command: Command, timeStr: String) {
        val timeArray: ByteArray = DigitUtil.convertTimeToByteArray(timeStr)
        command.setData(timeArray)
    }

    /**
     * 获取车位锁警报记录
     * @param lockType
     */
    fun Va_Get_Lockcar_Alarm(lockType: Int) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }
}
