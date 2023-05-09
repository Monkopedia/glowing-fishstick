package com.ttlock.bl.sdk.api;

import com.ttlock.bl.sdk.util.DigitUtil;

/**
 * Created by Smartlock on 2016/6/1.
 * 车位锁
 */
 class CommandUtil_Va {

    /**
     * 添加管理员
     * @param command
     * @param adminPassword
     * @param unlockNumber
     */
    public static void addAdmin(Command command, String adminPassword,String unlockNumber) {
        byte[] values = new byte[21];
        System.arraycopy(adminPassword.getBytes(), 0, values, 0, 10);
        values[10] = 0x20;
        System.arraycopy(unlockNumber.getBytes(), 0, values, 11, 10);
        command.setData(values);
    }

    /**
     * 校验管理员
     * @param command
     * @param adminPs
     */
    public static void checkAdmin(Command command, String adminPs) {
        command.setData(adminPs.getBytes());
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
    public static void up_down(Command command, String sum) {
        byte[] data = new byte[12];
        System.arraycopy(sum.getBytes(), 0, data, 0, sum.length());
        data[10] = 0x20;
        data[11] = '1'; //暂时设置为 1，为了后面的指令可以继续接收
        command.setData(data);
    }

    /**
     * 校验用户期限
     * @param command
     * @param sDateStr
     * @param eDateStr
     */
    public static void checkUserTime(Command command, String sDateStr, String eDateStr) {
        byte[] values = DigitUtil.convertTimeToByteArray(sDateStr+eDateStr);
        command.setData(values);
    }

    /**
     * 校准时间
     * @param command
     * @param timeStr
     */
    public static void calibationTime(Command command, String timeStr) {
        byte[] timeArray = DigitUtil.convertTimeToByteArray(timeStr);
        command.setData(timeArray);
    }

    /**
     * 获取车位锁警报记录
     * @param lockType
     */
    public static void Va_Get_Lockcar_Alarm(int lockType) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

}
