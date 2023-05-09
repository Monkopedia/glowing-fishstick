package com.ttlock.bl.sdk.api;

import com.ttlock.bl.sdk.util.DigitUtil;

/**
 * Created by Smartlock on 2016/6/1.
 */
 class CommandUtil_V2S_PLUS {

    private static boolean DBG = false;

    public static void addAdmin_V2S_PLUS(Command command, String adminPassword, String unlockNumber, byte[] aesKeyArray) {
        byte[] values = new byte[14];
        int i=9,j=adminPassword.length()-1;
        while(j>=0){
            values[i--]=(byte) (adminPassword.charAt(j--) - '0');
        }
        while(i>=0)
            values[i--]=0;
        int unlock_number = Integer.valueOf(unlockNumber);
        System.arraycopy(DigitUtil.integerToByteArray(unlock_number), 0, values, 10, 4);
        command.setData(values, aesKeyArray);
    }

    public static void checkAdmin_V2S_PLUS(Command command, String adminPs, String unlockKey, int lockFlagPos, byte[] aesKeyArray, int apiCommand) {
        byte[] values = new byte[13];
        byte[] password = adminPs.getBytes();
        int i = 9,j = password.length - 1;
        while(j >= 0)
            values[i--] = (byte) (password[j--] - 0x30);
        while(i >= 0)
            values[i--] = 0x00;
        values[10] = (byte) (lockFlagPos >> 16);
        values[11] = (byte) (lockFlagPos >> 8);
        values[12] = (byte) lockFlagPos;
        command.setData(values, aesKeyArray);
    }

    public static void setAdminKeyboardPwd(Command command, String adminKeyboardPwd, byte[] aesKeyArray) {
        byte[] values = new byte[10];
        int len = adminKeyboardPwd.length();
        for(int i=0;i<len;i++) {
            values[i] = (byte) (adminKeyboardPwd.charAt(i) - '0');
        }
        for(int i = len; i < 10; i++) {
            values[i] = (byte) 0xFF;
        }
        command.setData(values, aesKeyArray);
    }

    public static void setDeletePwd(Command command, String deletePwd, byte[] aesKeyArray) {
        byte[] values = new byte[10];
        int len = deletePwd.length();
        for(int i=0;i<len;i++) {
            values[i] = (byte) (deletePwd.charAt(i) - '0');
        }
        for(int i = len; i < 10; i++) {
            values[i] = (byte) 0xFF;
        }
        command.setData(values, aesKeyArray);
    }

    public static void checkUserTime_V2S_PLUS(Command command, String sDateStr, String eDateStr, String unlockKey, int lockFlagPos, byte[] aesKeyArray, int apiCommand) {
        byte[] values = new byte[13];
        byte[] time = DigitUtil.convertTimeToByteArray(sDateStr+eDateStr);
        System.arraycopy(time, 0, values, 0, 10);
        values[10] = (byte) ((lockFlagPos >> 16) & 0xFF);
        values[11] = (byte) ((lockFlagPos >> 8) & 0xFF);
        values[12] = (byte) (lockFlagPos & 0xFF);
        command.setData(values, aesKeyArray);
    }

    public static void unlock_V2S_PLUS(Command command, String sum, byte[] aesKeyArray) {
        byte flag = 0x00;//操作flag
        byte[] values = new byte[5];
        int sumI = Integer.valueOf(sum);
        byte[] sumByteArray = DigitUtil.integerToByteArray(sumI);
        System.arraycopy(sumByteArray, 0, values, 0, sumByteArray.length);
        values[4] = flag;
        command.setData(values, aesKeyArray);
    }



    /**
     * 同步键盘密码（300个密码)
     * @param lockType
     * @param packet
     * @param seq
     */
    public static void synPwd(int lockType, byte[] packet, int seq, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_SYN_KEYBOARD_PWD);
        byte[] values = new byte[30];
        for(int i=0;i<28;i++) {
            values[i] = packet[i];
        }
        //低位在前，高位在后
        values[28] = (byte) (seq);
        values[29] = (byte) (seq >> 8);
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void calibationTime_V2S_PLUS(Command command, String timeStr, byte[] aesKeyArray) {
        byte[] timeArray = DigitUtil.convertTimeToByteArray(timeStr);
        command.setData(timeArray, aesKeyArray);
    }
}
