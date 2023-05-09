package com.ttlock.bl.sdk.api;

import com.ttlock.bl.sdk.util.DigitUtil;

/**
 * Created by Smartlock on 2016/6/1.
 */
 class CommandUtil_V2S {

    public static void addAdmin(Command command, String adminPassword,String unlockNumber) {
        byte[] values = new byte[14];
        int i=9,j=adminPassword.length()-1;
        while(j>=0) {
            values[i--]=(byte) (adminPassword.charAt(j--) - '0');
        }
        while(i>=0)
            values[i--]=0;
        int unlock_number = Integer.valueOf(unlockNumber);
        System.arraycopy(DigitUtil.integerToByteArray(unlock_number), 0, values, 10, 4);
        command.setData(values);
    }

    public static void checkAdmin(Command command, String adminPs, int lockFlagPos) {
        byte[] password = adminPs.getBytes();
        byte[] values = new byte[13];
        int i = 9, j = password.length - 1;
        while(j >= 0)
            values[i--] = (byte) (password[j--] - 0x30);
        while(i >= 0)
            values[i--] = 0x00;
        values[10] = (byte) (lockFlagPos >> 16);
        values[11] = (byte) (lockFlagPos >> 8);
        values[12] = (byte) lockFlagPos;
        command.setData(values);
    }

    public static void unlock(Command command, String sum) {
        byte flag = 0;
        byte[] values = new byte[5];
        int sum_int = Integer.valueOf(sum);
        System.arraycopy(DigitUtil.integerToByteArray(sum_int), 0, values, 0, 4);
        values[4] = flag;
        command.setData(values);
    }

    /**
     * 校准用户时间
     * @param command
     * @param sDateStr
     * @param eDateStr
     * @param lockFlagPos
     */
    public static void checkUserTime(Command command, String sDateStr, String eDateStr, int lockFlagPos) {
        byte[] values = new byte[13];
        byte[] time = DigitUtil.convertTimeToByteArray(sDateStr+eDateStr);
        System.arraycopy(time, 0, values, 0, 10);
        values[10] = (byte) ((lockFlagPos >> 16) & 0xFF);
        values[11] = (byte) ((lockFlagPos >> 8) & 0xFF);
        values[12] = (byte) (lockFlagPos & 0xFF);
        command.setData(values);
    }

    public static void calibationTime_V2S(Command command, String timeStr) {
        byte[] timeArray = DigitUtil.convertTimeToByteArray(timeStr);
        command.setData(timeArray);
    }

    public static void synPwd(int lockType, String pwd, int seq) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_SYN_KEYBOARD_PWD);
        byte[] values = new byte[32];
        for(int i=0;i<30;i++) {
            values[i] = (byte) (pwd.charAt(i) - '0');
        }
        values[30] = (byte) ((seq >> 8) & 0xFF);
        values[31] = (byte) (seq & 0xFF);
        command.setData(values);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 设置管理员键盘密码
     * @param command
     * @param adminKeyboardPwd 键盘密码
     */
    public static void setAdminKeyboardPwd(Command command, String adminKeyboardPwd) {
        byte[] values = new byte[10];
        int len = adminKeyboardPwd.length();
        for(int i=0;i<len;i++) {
            values[i] = (byte) (adminKeyboardPwd.charAt(i) - '0');
        }
        for(int i = len; i < 10; i++) {
            values[i] = (byte) 0xFF;
        }
        command.setData(values);
    }

    /**
     * 设置删除密码
     * @param command
     * @param deletePwd 删除密码
     */
    public static void setDeletePwd(Command command, String deletePwd) {
        byte[] values = new byte[10];
        int len = deletePwd.length();
        for(int i=0;i<len;i++) {
            values[i] = (byte) (deletePwd.charAt(i) - '0');
        }
        for(int i = len; i < 10; i++) {
            values[i] = (byte) 0xFF;
        }
        command.setData(values);
    }
}
