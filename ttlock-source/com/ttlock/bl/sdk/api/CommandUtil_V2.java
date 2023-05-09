package com.ttlock.bl.sdk.api;

/**
 * Created by Smartlock on 2016/6/1.
 */
 class CommandUtil_V2 {
    public static void checkUserTime_V2(Command command, String sDateStr, String eDateStr, String unlockKey, int apiCommand) {
        byte[] values = new byte[21];
        command.setCommand(Command.VERSION_LOCK_V1);
        System.arraycopy(sDateStr.getBytes(), 0, values, 0, sDateStr.getBytes().length);
        values[10] = 0x20;
        System.arraycopy(eDateStr.getBytes(), 0, values, 11, eDateStr.getBytes().length);
        command.setData(values);
    }

    public static void calibationTime_V2(Command command, String timeStr) {
        byte[] timeArray = timeStr.getBytes();
        command.setData(timeArray);
    }
}
