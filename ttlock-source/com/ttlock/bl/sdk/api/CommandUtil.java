package com.ttlock.bl.sdk.api;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.ttlock.bl.sdk.constant.APICommand;
import com.ttlock.bl.sdk.constant.AutoLockOperate;
import com.ttlock.bl.sdk.constant.Constant;
import com.ttlock.bl.sdk.constant.DeviceInfoType;
import com.ttlock.bl.sdk.constant.ICOperate;
import com.ttlock.bl.sdk.constant.LockType;
import com.ttlock.bl.sdk.device.WirelessKeypad;
import com.ttlock.bl.sdk.entity.ConnectParam;
import com.ttlock.bl.sdk.entity.IpSetting;
import com.ttlock.bl.sdk.entity.LockVersion;
import com.ttlock.bl.sdk.entity.TransferData;
import com.ttlock.bl.sdk.util.AESUtil;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.LogUtil;

import java.util.TimeZone;

/**
 * Created by Smartlock on 2016/5/27.
 */

 class CommandUtil {

    private static final boolean DBG = false;

    /**
     * 时间2000.2.1
     */
    public static final long permanentStartDate = 949338000000l;
    /**
     * 时间2099.12.1
     */
    public static final long permanentEndDate = 4099741200000l;

    private static final byte[] defaultAesKeyArray = {
            (byte) 0x98, (byte) 0x76, (byte) 0x23, (byte) 0xE8,
            (byte) 0xA9, (byte) 0x23, (byte) 0xA1, (byte) 0xBB,
            (byte) 0x3D, (byte) 0x9E, (byte) 0x7D, (byte) 0x03,
            (byte) 0x78, (byte) 0x12, (byte) 0x45, (byte) 0x88};

    /**
     * 获取版本号
     */
    public static void E_getLockVersion(int apiCommand) {
        Command command = new Command(LockType.LOCK_TYPE_V3);
//        byte[] values = new byte[1];
        command.setCommand(Command.COMM_INITIALIZATION);
//        values[0] = 0x01;
//        command.setData(values, defaultAesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand(), apiCommand);
    }

    public static void A_addAdmin_V2S_Plus() {

    }

    /**
     * 今后都转换为这个
     * 校验管理员
     * @param transferData
     */
    public static void A_checkAdmin(TransferData transferData) {
        Command command = new Command(transferData.getLockVersion());
        command.setCommand(Command.COMM_CHECK_ADMIN);
        String adminPs = transferData.getAdminPs();
        String unlockKey = transferData.getUnlockKey();
        if(adminPs.length() > 10) {
            adminPs = new String(DigitUtil.decodeDefaultPassword(DigitUtil.stringDividerByDotToByteArray(adminPs)));
        }
        if(adminPs.length() < 10) {
            adminPs = String.format("%10s", adminPs).replace(" ", "0");
        }
        if(unlockKey != null && unlockKey.length() > 10) {
            unlockKey = new String(DigitUtil.decodeDefaultPassword(DigitUtil.stringDividerByDotToByteArray(unlockKey)));
        }
        transferData.setAdminPs(adminPs);
        transferData.setUnlockKey(unlockKey);
        switch (command.getLockType()) {
            case LockType.LOCK_TYPE_V2:
                //TODO:
                break;
            case LockType.LOCK_TYPE_CAR:
                    CommandUtil_Va.checkAdmin(command, adminPs);
                break;
            case LockType.LOCK_TYPE_MOBI:
                break;
            case LockType.LOCK_TYPE_V2S:
                CommandUtil_V2S.checkAdmin(command, adminPs, transferData.getLockFlagPos());
                break;
            case LockType.LOCK_TYPE_V2S_PLUS:
                CommandUtil_V2S_PLUS.checkAdmin_V2S_PLUS(command, adminPs, unlockKey, transferData.getLockFlagPos(), transferData.getAesKeyArray(), transferData.getAPICommand());
                break;
            case LockType.LOCK_TYPE_V3:
            case LockType.LOCK_TYPE_V3_CAR:
                CommandUtil_V3.checkAdmin(command, transferData.getmUid(), adminPs, unlockKey, transferData.getLockFlagPos(), transferData.getAesKeyArray(), transferData.getAPICommand());
                break;
                default:
                    break;
        }
        transferData.setTransferData(command.buildCommand());
        BluetoothImpl.getInstance().sendCommand(transferData);
    }

    /**
     * 校验用户时间
     * @param transferData
     */
    public static void U_checkUserTime(TransferData transferData) {
        Command command = new Command(transferData.getLockVersion());
        command.setCommand(Command.COMM_CHECK_USER_TIME);
        String unlockKey = transferData.getUnlockKey();
        long startDate = transferData.getStartDate();
        long endDate = transferData.getEndDate();

        LogUtil.d("startDate:" + startDate, DBG);
        LogUtil.d("endDate:" + endDate, DBG);

        if(unlockKey.length() > 10) {
            unlockKey = new String(DigitUtil.decodeDefaultPassword(DigitUtil.stringDividerByDotToByteArray(unlockKey)));
        }
        transferData.setUnlockKey(unlockKey);
        //永久钥匙
        if(startDate == 0 || endDate == 0) {
            startDate = permanentStartDate;
            endDate = permanentEndDate;
        }

        //时间戳应该没用了
        transferData.setStartDate(startDate);
        transferData.setEndDate(endDate);

        //根据时间偏移量重新计算时间
        startDate = startDate + transferData.getTimezoneOffSet() - TimeZone.getDefault().getOffset(System.currentTimeMillis());
        endDate = endDate + transferData.getTimezoneOffSet() - TimeZone.getDefault().getOffset(System.currentTimeMillis());

        String sDateStr = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmm");
        String eDateStr = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmm");
        switch (command.getLockType()) {
            case LockType.LOCK_TYPE_V2:
                //TODO:
                break;
            case LockType.LOCK_TYPE_CAR:
                CommandUtil_Va.checkUserTime(command, sDateStr, eDateStr);
                break;
            case LockType.LOCK_TYPE_MOBI:
                break;
            case LockType.LOCK_TYPE_V2S:
                CommandUtil_V2S.checkUserTime(command, sDateStr, eDateStr, transferData.getLockFlagPos());
                break;
            case LockType.LOCK_TYPE_V2S_PLUS:
                CommandUtil_V2S_PLUS.checkUserTime_V2S_PLUS(command, sDateStr, eDateStr, unlockKey, transferData.getLockFlagPos(), transferData.getAesKeyArray(), transferData.getAPICommand());
                break;
            case LockType.LOCK_TYPE_V3:
            case LockType.LOCK_TYPE_V3_CAR:
                CommandUtil_V3.checkUserTime(command, transferData.getmUid(), sDateStr, eDateStr, unlockKey, transferData.getLockFlagPos(),  transferData.getAesKeyArray(), transferData.getAPICommand());
                break;
                default:
                    break;
        }
        transferData.setTransferData(command.buildCommand());
        BluetoothImpl.getInstance().sendCommand(transferData);
    }

    /**
     * 获取AESkey
     */
    public static void getAESKey(LockVersion lockVersion, String manufacturer, int apiCommand) {
        BluetoothImpl.aesKeyArray = defaultAesKeyArray;
        LogUtil.d(lockVersion.toString(), DBG);
        Command command = new Command(lockVersion);
        command.setCommand(Command.COMM_GET_AES_KEY);
        command.setData(manufacturer.getBytes(), defaultAesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand(), apiCommand, defaultAesKeyArray);
    }

    public static void getAESKey(TransferData transferData) {
        BluetoothImpl.aesKeyArray = defaultAesKeyArray;
        transferData.setAesKeyArray(defaultAesKeyArray);
        Command command = new Command(LockVersion.lockVersion_V3);
        command.setCommand(Command.COMM_GET_AES_KEY);
        command.setData(Constant.VENDOR.getBytes(), defaultAesKeyArray);
        transferData.setTransferData(command.buildCommand());
        BluetoothImpl.getInstance().sendCommand(transferData);
    }

    public static void V_addAdmin(int lockType, String adminPassword, String unlockNumber, byte[] aesKeyArray) {
        LogUtil.d("lockType=" + lockType + " adminPassword=" + adminPassword + " unlockNumber=" + unlockNumber, DBG);
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_ADD_ADMIN);
        switch (lockType) {
            case LockType.LOCK_TYPE_V2:

                break;
            case LockType.LOCK_TYPE_CAR:
                CommandUtil_Va.addAdmin(command, adminPassword, unlockNumber);
                break;
            case LockType.LOCK_TYPE_MOBI:
                break;
            case LockType.LOCK_TYPE_V2S:
                CommandUtil_V2S.addAdmin(command, adminPassword, unlockNumber);
                break;
            case LockType.LOCK_TYPE_V2S_PLUS:
                CommandUtil_V2S_PLUS.addAdmin_V2S_PLUS(command, adminPassword, unlockNumber, aesKeyArray);
                break;
            case LockType.LOCK_TYPE_V3:
            case LockType.LOCK_TYPE_V3_CAR:
                CommandUtil_V3.addAdmin_V3(command, adminPassword, unlockNumber, aesKeyArray);
                break;
        }
        //TODO:接后续指令
        BluetoothImpl.getInstance().sendCommand(command.buildCommand(), adminPassword, unlockNumber, aesKeyArray, APICommand.OP_ADD_ADMIN);
    }

    public static void A_checkAdmin(int uid, String lockVersionString, String adminPs, String unlockKey, int lockFlagPos, byte[] aesKeyArray, String string, int apiCommand) {
        A_checkAdmin(uid, lockVersionString, adminPs, unlockKey, lockFlagPos, aesKeyArray, 0, string, apiCommand);
    }

    public static void A_checkAdmin(int uid, String lockVersionString, String adminPs, String unlockKey, int lockFlagPos, byte[] aesKeyArray, int apiCommand) {
        A_checkAdmin(uid, lockVersionString, adminPs, unlockKey, lockFlagPos, aesKeyArray, 0, null, apiCommand);
    }

    //TODO:
    public static void A_checkAdmin(int uid, String lockVersionString, String adminPs, String unlockKey, int lockFlagPos, byte[] aesKeyArray, int validPwdNum, int pwdType, String originalPwd, String string, long startDate, long endDate, int apiCommand) {
        Command command = new Command(lockVersionString);
        command.setCommand(Command.COMM_CHECK_ADMIN);
        if(adminPs.length() > 10) {
            adminPs = new String(DigitUtil.decodeDefaultPassword(DigitUtil.stringDividerByDotToByteArray(adminPs)));
        }
        if(adminPs.length() < 10) {
            adminPs = String.format("%10s", adminPs).replace(" ", "0");
        }
        if(unlockKey.length() > 10) {
            unlockKey = new String(DigitUtil.decodeDefaultPassword(DigitUtil.stringDividerByDotToByteArray(unlockKey)));
        }
        switch (command.getLockType()) {
            case LockType.LOCK_TYPE_V2:
                //TODO:
                break;
            case LockType.LOCK_TYPE_CAR:
                CommandUtil_Va.checkAdmin(command, adminPs);
                break;
            case LockType.LOCK_TYPE_MOBI:
                break;
            case LockType.LOCK_TYPE_V2S:
                CommandUtil_V2S.checkAdmin(command, adminPs, lockFlagPos);
                break;
            case LockType.LOCK_TYPE_V2S_PLUS:
                CommandUtil_V2S_PLUS.checkAdmin_V2S_PLUS(command, adminPs, unlockKey, lockFlagPos, aesKeyArray, apiCommand);
                break;
            case LockType.LOCK_TYPE_V3:
            case LockType.LOCK_TYPE_V3_CAR:
                CommandUtil_V3.checkAdmin(command, uid, adminPs, unlockKey, lockFlagPos, aesKeyArray, apiCommand);
                //TODO:
                break;
        }
        BluetoothImpl.getInstance().sendCommand(command.buildCommand(), adminPs, unlockKey, lockFlagPos, aesKeyArray, (byte) validPwdNum, (byte) pwdType, originalPwd, string, startDate, endDate, apiCommand);
    }

    public static void A_checkAdmin(int uid, String lockVersionString, String adminPs, String unlockKey, int lockFlagPos, byte[] aesKeyArray, int validPwdNum, String string, int apiCommand) {
        Command command = new Command(lockVersionString);
        command.setCommand(Command.COMM_CHECK_ADMIN);
        if(adminPs.length() > 10) {
            adminPs = new String(DigitUtil.decodeDefaultPassword(DigitUtil.stringDividerByDotToByteArray(adminPs)));
        }
        if(adminPs.length() < 10) {
            adminPs = String.format("%10s", adminPs).replace(" ", "0");
        }
        if(unlockKey != null && unlockKey.length() > 10) {
            unlockKey = new String(DigitUtil.decodeDefaultPassword(DigitUtil.stringDividerByDotToByteArray(unlockKey)));
        }
        switch (command.getLockType()) {
            case LockType.LOCK_TYPE_V2:
                //TODO:
                break;
            case LockType.LOCK_TYPE_CAR:
                CommandUtil_Va.checkAdmin(command, adminPs);
                break;
            case LockType.LOCK_TYPE_MOBI:
                break;
            case LockType.LOCK_TYPE_V2S:
                CommandUtil_V2S.checkAdmin(command, adminPs, lockFlagPos);
                break;
            case LockType.LOCK_TYPE_V2S_PLUS:
                CommandUtil_V2S_PLUS.checkAdmin_V2S_PLUS(command, adminPs, unlockKey, lockFlagPos, aesKeyArray, apiCommand);
                break;
            case LockType.LOCK_TYPE_V3:
            case LockType.LOCK_TYPE_V3_CAR:
                CommandUtil_V3.checkAdmin(command, uid, adminPs, unlockKey, lockFlagPos, aesKeyArray, apiCommand);
                //TODO:
                break;
            default:
                break;
        }
        BluetoothImpl.getInstance().sendCommand(command.buildCommand(), adminPs, unlockKey, lockFlagPos, aesKeyArray, (byte) validPwdNum, string, apiCommand);
    }

    /**
     * 设置管理员键盘密码
     */
    public static void S_setAdminKeyboardPwd(int lockType, String adminKeyboardPassword, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_SET_ADMIN_KEYBOARD_PWD);
        switch (lockType) {
            case LockType.LOCK_TYPE_V2:
                break;
            case LockType.LOCK_TYPE_V2S:
                CommandUtil_V2S.setAdminKeyboardPwd(command, adminKeyboardPassword);
                break;
            case LockType.LOCK_TYPE_V2S_PLUS:
                CommandUtil_V2S_PLUS.setAdminKeyboardPwd(command, adminKeyboardPassword, aesKeyArray);
                break;
            case LockType.LOCK_TYPE_V3:
                CommandUtil_V3.setAdminKeyboardPwd(command, adminKeyboardPassword, aesKeyArray);
                break;
                default:
                    break;
        }
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 设置删除密码
     * @param lockType
     * @param deletePwd 删除密码
     * @param aesKeyArray
     */
    public static void D_setDeletePassword(int lockType, String deletePwd, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_SET_DELETE_PWD);
        switch (lockType) {
            case LockType.LOCK_TYPE_V2:
                break;
            case LockType.LOCK_TYPE_V2S:
                CommandUtil_V2S.setDeletePwd(command, deletePwd);
                break;
            case LockType.LOCK_TYPE_V2S_PLUS:
                CommandUtil_V2S_PLUS.setDeletePwd(command, deletePwd, aesKeyArray);
                break;
            case LockType.LOCK_TYPE_V3:
                CommandUtil_V3.setDeletePwd(command, deletePwd, aesKeyArray);
                break;
        }
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    @Deprecated
    public static void U_checkUserTime(int uid, String lockVersionString, long startDate, long endDate, String unlockKey, int lockFlagPos, byte[] aesKeyArray, long date, int apiCommand) {
        Command command = new Command(lockVersionString);
        command.setCommand(Command.COMM_CHECK_USER_TIME);
        if(unlockKey.length() > 10) {
            unlockKey = new String(DigitUtil.decodeDefaultPassword(DigitUtil.stringDividerByDotToByteArray(unlockKey)));
        }
        //永久钥匙
        if(startDate == 0)
            startDate = permanentStartDate;
        if(endDate == 0)
            endDate = permanentEndDate;

        String sDateStr = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmm");
        String eDateStr = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmm");
        switch (command.getLockType()) {
            case LockType.LOCK_TYPE_V2:
                //TODO:
                break;
            case LockType.LOCK_TYPE_CAR:
                CommandUtil_Va.checkUserTime(command, sDateStr, eDateStr);
                break;
            case LockType.LOCK_TYPE_MOBI:
                break;
            case LockType.LOCK_TYPE_V2S:
                //TODO:
                CommandUtil_V2S.checkUserTime(command, sDateStr, eDateStr, lockFlagPos);
                break;
            case LockType.LOCK_TYPE_V2S_PLUS:
                CommandUtil_V2S_PLUS.checkUserTime_V2S_PLUS(command, sDateStr, eDateStr, unlockKey, lockFlagPos, aesKeyArray, apiCommand);
                break;
            case LockType.LOCK_TYPE_V3:
            case LockType.LOCK_TYPE_V3_CAR:
//                sDateStr = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmmss");
//                eDateStr = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmmss");
                CommandUtil_V3.checkUserTime(command, uid, sDateStr, eDateStr, unlockKey, lockFlagPos, aesKeyArray, apiCommand);
                break;
        }
        BluetoothImpl.getInstance().sendCommand(command.buildCommand(), uid, unlockKey, aesKeyArray, date, apiCommand);
    }

    public static void G_unlock(int lockType, String unlockKey, byte[] psFromLock, byte[] aesKeyArray, long unlockDate, long timezoneRawOffSet) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_UNLOCK);
        long psFromLockL = 0;
        long unlockKeyL = 0;
        String sum = null;
        switch (lockType) {
            case LockType.LOCK_TYPE_V2:
                psFromLockL = Long.valueOf(new String(psFromLock));
                unlockKeyL = Long.valueOf(unlockKey);
                sum = DigitUtil.getUnlockPassword(psFromLockL, unlockKeyL);
                //TODO:
                break;
            case LockType.LOCK_TYPE_CAR:
                psFromLockL = Long.valueOf(new String(psFromLock));
                unlockKeyL = Long.valueOf(unlockKey);
                sum = DigitUtil.getUnlockPassword(psFromLockL, unlockKeyL);
                //车位锁调用相反的
                command.setCommand(Command.COMM_LOCK);
                CommandUtil_Va.up_down(command, sum);
                break;
            case LockType.LOCK_TYPE_MOBI:
                break;
            case LockType.LOCK_TYPE_V2S:
                psFromLockL = DigitUtil.fourBytesToLong(psFromLock);
                unlockKeyL = Long.valueOf(unlockKey);
                sum = DigitUtil.getUnlockPwd_new(psFromLockL, unlockKeyL);
                CommandUtil_V2S.unlock(command, sum);
                break;
            case LockType.LOCK_TYPE_V2S_PLUS:
                psFromLockL = DigitUtil.fourBytesToLong(psFromLock);
                unlockKeyL = Long.valueOf(unlockKey);
                sum = DigitUtil.getUnlockPwd_new(psFromLockL, unlockKeyL);
                CommandUtil_V2S_PLUS.unlock_V2S_PLUS(command, sum, aesKeyArray);
                break;
            case LockType.LOCK_TYPE_V3:
            case LockType.LOCK_TYPE_V3_CAR:
                psFromLockL = DigitUtil.fourBytesToLong(psFromLock);
                unlockKeyL = Long.valueOf(unlockKey);
                sum = DigitUtil.getUnlockPwd_new(psFromLockL, unlockKeyL);
                CommandUtil_V3.unlock(command, sum, unlockDate, aesKeyArray, timezoneRawOffSet);
                break;
        }
        //TODO:接后续指令
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 关锁
     * @param lockType
     * @param unlockKey
     * @param psFromLock
     * @param aesKeyArray
     */
    public static void L_lock(int lockType, String unlockKey, byte[] psFromLock, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        //车位锁进行相反操作
        command.setCommand(Command.COMM_UNLOCK);
        long psFromLockL = 0;
        long unlockKeyL = 0;
        String sum = null;
        switch (lockType) {
            case LockType.LOCK_TYPE_CAR:
                psFromLockL = Long.valueOf(new String(psFromLock));
                unlockKeyL = Long.valueOf(unlockKey);
                sum = DigitUtil.getUnlockPassword(psFromLockL, unlockKeyL);
                CommandUtil_Va.up_down(command, sum);
                break;
            case LockType.LOCK_TYPE_MOBI:
                break;
        }
        //TODO:接后续指令
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void checkRandom(int lockType, String unlockKey, byte[] psFromLock, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_CHECK_RANDOM);
        long psFromLockL = DigitUtil.fourBytesToLong(psFromLock);
        long unlockKeyL = Long.valueOf(unlockKey);
        String sum = DigitUtil.getUnlockPwd_new(psFromLockL, unlockKeyL);
        CommandUtil_V3.checkRandom(command, sum, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

//    @Deprecated
//    public static void C_calibationTime(int uid, String lockVersionString, String unlockKey, long date, int lockFlagPos, byte[] aesKeyArray, int apiCommand) {
//        U_checkUserTime(uid, lockVersionString, permanentStartDate, permanentEndDate, unlockKey, lockFlagPos, aesKeyArray, date, apiCommand);
//    }

    public static void C_calibationTime(TransferData transferData) {
        transferData.setStartDate(permanentStartDate);
        transferData.setEndDate(permanentEndDate);
        U_checkUserTime(transferData);
    }

    /**
     * 校准时间
     * @param lockType
     * @param date                时间撮
     * @param timezoneRawOffSet   时间偏移量
     * @param aesKeyArray
     */
    public static void C_calibationTime(int lockType, long date, long timezoneRawOffSet, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_TIME_CALIBRATE);
        //根据时区来重新计算时间
        date = date + timezoneRawOffSet - TimeZone.getDefault().getOffset(System.currentTimeMillis());
        LogUtil.d("timezoneRawOffSet:" + timezoneRawOffSet, DBG);
        LogUtil.d("TimeZone.getDefault().getOffset(System.currentTimeMillis()):" + TimeZone.getDefault().getOffset(System.currentTimeMillis()), DBG);
        LogUtil.d("date:" + date, DBG);
        String timeStr = DigitUtil.formateDateFromLong(date, "yyMMddHHmmss");
        LogUtil.d("timeStr:" + timeStr, DBG);
        switch (lockType) {
            case LockType.LOCK_TYPE_V2:
                CommandUtil_V2.calibationTime_V2(command, timeStr.substring(0, timeStr.length() - 2));
                break;
            case LockType.LOCK_TYPE_CAR:
                CommandUtil_Va.calibationTime(command, timeStr.substring(0, timeStr.length() - 2));
                break;
            case LockType.LOCK_TYPE_MOBI:
                break;
            case LockType.LOCK_TYPE_V2S:
                CommandUtil_V2S.calibationTime_V2S(command, timeStr.substring(0, timeStr.length() - 2));
                break;
            case LockType.LOCK_TYPE_V2S_PLUS:
                CommandUtil_V2S_PLUS.calibationTime_V2S_PLUS(command, timeStr.substring(0, timeStr.length() - 2), aesKeyArray);
                break;
            case LockType.LOCK_TYPE_V3:
            case LockType.LOCK_TYPE_V3_CAR:
                CommandUtil_V3.calibationTime_V3(command, timeStr, aesKeyArray);
                break;
                default:
                    break;
        }
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void N_setLockname(int lockType, String lockname, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_SET_LOCK_NAME);
        CommandUtil_V3.setLockname(command, lockname, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void AT_setLockname(int lockType, String lockname, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand((byte) 0xFF);//AT指令
        command.setData(("AT+NAME=" + lockname).getBytes(), aesKeyArray);
//        CommandUtil_V3.setLockname(command, lockname, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void R_resetLock(int lockType) {
        LogUtil.d("lockType:" + lockType);
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_RESET_LOCK);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
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
    public static void resetKeyboardPasswordCount(int lockType, byte validPwdNum, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_RESET_KEYBOARD_PWD_COUNT);
        byte[] values = new byte[]{validPwdNum};
        switch (lockType) {
            case LockType.LOCK_TYPE_V3:
                CommandUtil_V3.resetKeyboardPasswordCount(command, values, aesKeyArray);
                break;
        }
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    @Deprecated
    public static void getLockTime(String lockVersion) {
        Command command = new Command(lockVersion);
        command.setCommand(Command.COMM_GET_LOCK_TIME);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void getLockTime(TransferData transferData) {
        Command command = new Command(transferData.getLockVersion());
        command.setCommand(Command.COMM_GET_LOCK_TIME);
        transferData.setTransferData(command.buildCommand());
        BluetoothImpl.getInstance().sendCommand(transferData);
    }

    /**
     * 获取操作日志
     * @param lockVersion
     * @param aesKeyArray
     */
    @Deprecated
    public static void getOperateLog(String lockVersion, byte[] aesKeyArray) {
        Command command = new Command(lockVersion);
        switch (command.getLockType()) {
            case LockType.LOCK_TYPE_CAR:
                command.setCommand(Command.COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED);
                break;
            case LockType.LOCK_TYPE_V3:
            case LockType.LOCK_TYPE_V3_CAR:
                command.setCommand(Command.COMM_GET_OPERATE_LOG);
                //初始请求序列设置的默认值
                CommandUtil_V3.getOperateLog(command, (short) 0xffff, aesKeyArray);
                break;
            default:
                break;
        }
        BluetoothImpl.getInstance().sendCommand(command.buildCommand(), APICommand.OP_GET_OPERATE_LOG);
    }

    /**
     * 获取操作日志
     * @param transferData
     */
    public static void getOperateLog(TransferData transferData) {
        Log.d("OMG","=getOperateLog=");
        Command command = new Command(transferData.getLockVersion());
        switch (command.getLockType()) {
            case LockType.LOCK_TYPE_CAR:
                command.setCommand(Command.COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED);
                break;
            case LockType.LOCK_TYPE_V3:
            case LockType.LOCK_TYPE_V3_CAR:
                command.setCommand(Command.COMM_GET_OPERATE_LOG);
                //初始请求序列设置的默认值
                CommandUtil_V3.getOperateLog(command, transferData.getSeq(), transferData.getAesKeyArray());
                break;
            default:
                break;
        }
        transferData.setAPICommand(APICommand.OP_GET_OPERATE_LOG);
        transferData.setTransferData(command.buildCommand());
        BluetoothImpl.getInstance().sendCommand(transferData);
        Log.d("OMG","=sendCommand=");
    }

    public static void getOperateLog(String lockVersion, short seq, byte[] aesKeyArray) {
        Command command = new Command(lockVersion);
        command.setCommand(Command.COMM_GET_OPERATE_LOG);
        CommandUtil_V3.getOperateLog(command, seq, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand(), APICommand.OP_GET_OPERATE_LOG);
    }

    public static void getOperateLog(int lockType, short seq, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_GET_OPERATE_LOG);
        CommandUtil_V3.getOperateLog(command, seq, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void getValidKeyboardPassword(String lockVersion, short seq, byte[] aesKeyArray) {
        Command command = new Command(lockVersion);
        command.setCommand(Command.COMM_GET_VALID_KEYBOARD_PASSWORD);
        CommandUtil_V3.getValidKeyboardPassword(command, seq, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void getValidKeyboardPassword(int lockType, short seq, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_GET_VALID_KEYBOARD_PASSWORD);
        CommandUtil_V3.getValidKeyboardPassword(command, seq, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void manageKeyboardPassword(int lockType, byte pwdOperateType, byte keyboardPwdType, String originalPwd, String newPwd, long startDate, long endDate, byte[] defaultAesKeyArray) {
        manageKeyboardPassword(lockType, pwdOperateType, keyboardPwdType, 0, originalPwd, newPwd, startDate, endDate, defaultAesKeyArray, -1);
    }

    public static void manageKeyboardPassword(int lockType, byte pwdOperateType, byte keyboardPwdType, int circleType, String originalPwd, String newPwd, long startDate, long endDate, byte[] defaultAesKeyArray) {
        manageKeyboardPassword(lockType, pwdOperateType, keyboardPwdType, circleType, originalPwd, newPwd, startDate, endDate, defaultAesKeyArray, -1);
    }

    public static void manageKeyboardPassword(int lockType, byte pwdOperateType, byte keyboardPwdType, String originalPwd, String newPwd, long startDate, long endDate, byte[] defaultAesKeyArray, long timezoneOffset) {
        manageKeyboardPassword(lockType, pwdOperateType, keyboardPwdType, 0, originalPwd, newPwd, startDate, endDate, defaultAesKeyArray, timezoneOffset);
    }

    public static void manageKeyboardPassword(int lockType, byte pwdOperateType, byte keyboardPwdType, int circleType, String originalPwd, String newPwd, long startDate, long endDate, byte[] defaultAesKeyArray, long timezoneOffset) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_MANAGE_KEYBOARD_PASSWORD);
        CommandUtil_V3.manageKeyboardPassword(command, pwdOperateType, keyboardPwdType, circleType, originalPwd, newPwd, startDate, endDate, defaultAesKeyArray, timezoneOffset);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static String encry(String pwdInfoSource, long timestamp) {
        String timestampStr = String.format("%-16d", timestamp).replace(" ", "0").substring(0, 16);
        LogUtil.d(pwdInfoSource + " " + timestampStr, DBG);
        byte[] encryRes = AESUtil.aesEncrypt(pwdInfoSource.getBytes(), timestampStr.getBytes());
        return Base64.encodeToString(encryRes, Base64.NO_WRAP);
    }

    /**
     * 判断添加管理员所有操作完成指令
     */
    public static void operateFinished(int lockType) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 查询设备特征
     * @param lockType
     * @return
     */
    public static void searchDeviceFeature(int lockType) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_SEARCHE_DEVICE_FEATURE);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 查询IC卡号
     * @param lockType
     * @param seq
     * @param aesKeyArray
     */
    public static void searchICCardNo(int lockType, short seq, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_IC_MANAGE);
        CommandUtil_V3.ICManage(command, ICOperate.IC_SEARCH, seq, null, 0, 0, aesKeyArray, 0);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 添加IC卡
     * @param lockType
     * @param aesKeyArray
     */
    public static void addICCard(int lockType, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_IC_MANAGE);
        CommandUtil_V3.ICManage(command, ICOperate.ADD, (short)0, null, 0L, 0L, aesKeyArray, 0);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 恢复IC卡
     * @param lockType
     * @param aesKeyArray
     */
    public static void recoveryICCardPeriod(int lockType, String cardNo, long startDate, long endDate, byte[] aesKeyArray, long timezoneOffSet) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_IC_MANAGE);
        CommandUtil_V3.ICManage(command, ICOperate.ADD, (short)0, cardNo, startDate, endDate, aesKeyArray, timezoneOffSet);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
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
    public static void modifyICCardPeriod(int lockType, String cardNo, long startDate, long endDate, byte[] aesKeyArray, long timezoneOffSet) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_IC_MANAGE);
        CommandUtil_V3.ICManage(command, ICOperate.MODIFY, (short)0, cardNo, startDate, endDate, aesKeyArray, timezoneOffSet);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 删除IC卡
     * @param lockType
     * @param cardNo
     * @param aesKeyArray
     */
    public static void deleteICCard(int lockType, String cardNo, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_IC_MANAGE);
        CommandUtil_V3.ICManage(command, ICOperate.DELETE, (short)0, cardNo, 0, 0, aesKeyArray, 0);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 清空IC卡
     * @param lockType
     * @param aesKeyArray
     */
    public static void clearICCard(int lockType, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_IC_MANAGE);
        CommandUtil_V3.ICManage(command, ICOperate.CLEAR, (short)0, null, 0, 0, aesKeyArray, 0);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 设置手环KEY
     * @param lockType
     * @param wristKey
     * @param aesKeyArray
     */
    public static void setWristbandKey(int lockType, String wristKey, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_SET_WRIST_BAND_KEY);
        CommandUtil_V3.setWristKey(command, wristKey, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }


    public static void searchPasscode(int lockType, short seq, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_PWD_LIST);
        CommandUtil_V3.searchPwd(command, seq, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 查询指纹
     * @param lockType
     * @param seq
     * @param aesKeyArray
     */
    public static void searchFRNo(int lockType, short seq, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_FR_MANAGE);
        CommandUtil_V3.FRManage(command, ICOperate.FR_SEARCH, seq, 0, 0, 0, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }



    /**
     * 添加指纹
     * @param lockType
     * @param aesKeyArray
     */
    public static void addFR(int lockType, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_FR_MANAGE);
        CommandUtil_V3.FRManage(command, ICOperate.ADD, (short)0, 0, 0L, 0L, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
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
    public static void modifyFRPeriod(int lockType, long FRNo, long startDate, long endDate, byte[] aesKeyArray, long timezoneOffSet) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_FR_MANAGE);
        CommandUtil_V3.FRManage(command, ICOperate.MODIFY, (short)0, FRNo, startDate, endDate, aesKeyArray, timezoneOffSet);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void recoveryFRPeriod(int lockType, long FRNo, long startDate, long endDate, byte[] aesKeyArray, long timezoneOffSet) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_FR_MANAGE);
        CommandUtil_V3.FRManage(command, ICOperate.ADD, (short)0, FRNo, startDate, endDate, aesKeyArray, timezoneOffSet);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 删除指纹
     * @param lockType
     * @param FRNo
     * @param aesKeyArray
     */
    public static void deleteFR(int lockType, long FRNo, byte[] aesKeyArray) {
        LogUtil.d("FRNo:" + FRNo, DBG);
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_FR_MANAGE);
        CommandUtil_V3.FRManage(command, ICOperate.DELETE, (short)0, FRNo, 0, 0, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 清空IC卡
     * @param lockType
     * @param aesKeyArray
     */
    public static void clearFR(int lockType, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_FR_MANAGE);
        CommandUtil_V3.FRManage(command, ICOperate.CLEAR, (short)0, 0, 0, 0, aesKeyArray, 0);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 查询闭锁时间
     * @param lockType
     * @param aesKeyArray
     */
    public static void searchAutoLockTime(int lockType, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_AUTO_LOCK_MANAGE);
        CommandUtil_V3.autoLockManage(command, AutoLockOperate.SEARCH, (short)0, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 修改闭锁时间
     * @param lockType
     * @param aesKeyArray
     */
    public static void modifyAutoLockTime(int lockType, short time, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_AUTO_LOCK_MANAGE);
        CommandUtil_V3.autoLockManage(command, AutoLockOperate.MODIFY, time, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }



    /**
     * 读取设备信息
     */
    public static void readDeviceInfo(TransferData transferData) {
        Command command = new Command(transferData.getLockVersion());
        command.setCommand(Command.COMM_READ_DEVICE_INFO);
        command.setData(new byte[]{DeviceInfoType.MODEL_NUMBER}, transferData.getAesKeyArray());
        //TODO:暂时这样存
        transferData.setTransferData(command.buildCommand());
        BluetoothImpl.getInstance().sendCommand(transferData);
    }

    /**
     * 读取设备信息
     * @param lockType
     * @param opType    信息类型
     * @param aesKeyArray
     */
    public static void readDeviceInfo(int lockType, byte opType, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_READ_DEVICE_INFO);
        command.setData(new byte[]{opType}, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 进入升级模式
     * @param lockType
     * @param aesKeyArray
     */
    public static void enterDFUMode(int lockType, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_ENTER_DFU_MODE);
        command.setData(Constant.SCIENER.getBytes(), aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 查询自行车锁当前开关状态
     */
    public static void searchBicycleStatus(TransferData transferData) {
        Command command = new Command(transferData.getLockVersion());
        command.setCommand(Command.COMM_SEARCH_BICYCLE_STATUS);
        command.setData(Constant.SCIENER.getBytes(), transferData.getAesKeyArray());
        transferData.setTransferData(command.buildCommand());
        BluetoothImpl.getInstance().sendCommand(transferData);
    }

    /**
     * 闭锁
     * @param lockType
     * @param unlockKey
     * @param psFromLock
     * @param aesKeyArray
     * @param unlockDate
     */
    public static void lock(int lockType, String unlockKey, byte[] psFromLock, byte[] aesKeyArray, long unlockDate) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_FUNCTION_LOCK);
        long psFromLockL = 0;
        long unlockKeyL = 0;
        String sum = null;
        psFromLockL = DigitUtil.fourBytesToLong(psFromLock);
        unlockKeyL = Long.valueOf(unlockKey);
        sum = DigitUtil.getUnlockPwd_new(psFromLockL, unlockKeyL);
        CommandUtil_V3.lock(command, sum, unlockDate, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 屏幕密码管理
     * @param lockType
     * @param opType    1 - 查询  2 - 隐藏  3 - 显示
     * @param aesKeyArray
     */
    public static void screenPasscodeManage(int lockType, int opType, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_SHOW_PASSWORD);
        CommandUtil_V3.screenPasscodeManage(command, opType, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 读取密码参数
     * @param lockType
     * @param aesKeyArray
     */
    public static void readPwdPara(int lockType, byte[] aesKeyArray) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_READ_PWD_PARA);
        command.setData("".getBytes(), aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void getPow(TransferData transferData) {
        Command command = new Command(transferData.getLockVersion());
        command.setCommand(transferData.getCommand());
        transferData.setTransferData(command.buildCommand());
        BluetoothImpl.getInstance().sendCommand(transferData);
    }

    public static void getAccessoryBattery(TransferData transferData) {
        Command command = new Command(transferData.getLockVersion());
        command.setCommand(Command.COMM_ACCESSORY_BATTERY);
        byte[] values = new byte[1 + 6];
        values[0] = transferData.getAccessoryInfo().getAccessoryType().getValue();
        byte[] mac = DigitUtil.getReverseMacArray(transferData.getAccessoryInfo().getAccessoryMac());
        System.arraycopy(mac, 0, values, 1, 6);
        command.setData(values, transferData.getAesKeyArray());
        transferData.setTransferData(command.buildCommand());
        BluetoothImpl.getInstance().sendCommand(transferData);
//        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    }

    public static void setLock(ConnectParam param, WirelessKeypad keypad) {
        WirelessKeyboardCommand command = new WirelessKeyboardCommand(WirelessKeyboardCommand.COMM_SET_LOCK);
        command.setMac(keypad.getAddress());
        byte[] data = new byte[6 + 16];

        //低在前高在后
        String[] macArr = param.getLockmac().split(":");
        macArr = command.reverseArray(macArr);
        byte[] lockmacBytes = command.hexStringArrToByteArr(macArr);
        System.arraycopy(lockmacBytes, 0, data, 0, 6);

        byte[] baseAesKey = new byte[6];
        macArr = keypad.getAddress().split(":");
        macArr = command.reverseArray(macArr);
        byte[] keypadMacBytes = command.hexStringArrToByteArr(macArr);
        System.arraycopy(keypadMacBytes, 0, baseAesKey, 0, 6);
//        System.arraycopy(param.getFactoryDate().getBytes(), 0, baseAesKey, 6, 8);
        byte[] defaultAesKey = new byte[]{0x45,0x32, (byte) 0xAE,0x44, (byte) 0x98,0x45, (byte) 0xE4,0x09, (byte) 0xD3,0x62,0x7E,0x5F,0x56, (byte) 0x91, (byte) 0xE3,0x67};
        byte[] encryAesKey = AESUtil.aesEncrypt(baseAesKey, defaultAesKey);

        System.arraycopy(encryAesKey, 0, data, 6, 16);

        command.setData(data);
        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    }

    public static void readDeviceFeature(WirelessKeypad keypad) {
        WirelessKeyboardCommand command = new WirelessKeyboardCommand(WirelessKeyboardCommand.COMM_READ_FEATURE);
        command.setMac(keypad.getAddress());
        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * wifi锁 搜索附近可用wifi
     * @param transferData
     */
    public static void scanWifi(TransferData transferData) {
        //扫描wifi前清空之前的数据
        BluetoothImpl.getInstance().clearWifi();
        Command command = new Command(LockType.LOCK_TYPE_V3);
        command.setCommand(Command.COMM_SCAN_WIFI);
        command.setData("SCIENER".getBytes(), transferData.getAesKeyArray());
        transferData.setTransferData(command.buildCommand());
        BluetoothImpl.getInstance().sendCommand(transferData);
    }

    public static void configureWifiAp(TransferData transferData) {
        try {
            Command command = new Command(LockType.LOCK_TYPE_V3);
            command.setCommand(Command.COMM_CONFIG_WIFI_AP);
            if (TextUtils.isEmpty(transferData.getWifiPassword())) {
                transferData.setWifiPassword("");
            }
            byte[] wifiNameBytes = transferData.getWifiName().getBytes("UTF-8");
            byte wifiNameBytesLength = (byte) wifiNameBytes.length;
            byte[] values = new byte[1 + wifiNameBytesLength + 1 + transferData.getWifiPassword().length()];
            values[0] = wifiNameBytesLength;
            System.arraycopy(wifiNameBytes, 0, values, 1, wifiNameBytesLength);
            values[1 + wifiNameBytesLength] = (byte) transferData.getWifiPassword().length();
            System.arraycopy(transferData.getWifiPassword().getBytes(), 0, values, 1 + wifiNameBytesLength + 1, transferData.getWifiPassword().length());
            command.setData(values, transferData.getAesKeyArray());
            transferData.setTransferData(command.buildCommand());
            BluetoothImpl.getInstance().sendCommand(transferData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setServer(TransferData transferData) {
        Command command = new Command(LockType.LOCK_TYPE_V3);
        command.setCommand(Command.COMM_CONFIG_SERVER);
        if (TextUtils.isEmpty(transferData.getAddress())) {
            transferData.setAddress("");
        }

        byte[] values = new byte[1 + transferData.getAddress().length() + 2];
        values[0] = (byte) transferData.getAddress().length();
        System.arraycopy(transferData.getAddress().getBytes(), 0, values, 1, transferData.getAddress().length());

        byte[] portByteArray = DigitUtil.shortToByteArray(transferData.getPort());
        System.arraycopy(portByteArray, 0, values, 1 + transferData.getAddress().length(), 2);
        command.setData(values, transferData.getAesKeyArray());
        transferData.setTransferData(command.buildCommand());
        BluetoothImpl.getInstance().sendCommand(transferData);
    }

    public static void getWifiInfo(TransferData transferData) {
        Command command = new Command(LockType.LOCK_TYPE_V3);
        command.setCommand(Command.COMM_GET_WIFI_INFO);
        command.setData("".getBytes(), transferData.getAesKeyArray());
        transferData.setTransferData(command.buildCommand());
        BluetoothImpl.getInstance().sendCommand(transferData);
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
    public static void configStaticIp(IpSetting wifiLockNetworkConfiguration, TransferData transferData) {
        Command command = new Command(LockType.LOCK_TYPE_V3);
        command.setCommand(Command.COMM_CONFIG_STATIC_IP);
        byte[] values = new byte[1 + 4 + 4 + 4 + 4 + 4];
        values[0] = (byte) wifiLockNetworkConfiguration.getType();
        if (values[0] == IpSetting.STATIC_IP) {
            String ip = wifiLockNetworkConfiguration.getIpAddress();
            if (!TextUtils.isEmpty(ip)) {
                byte[] ipByteArr = DigitUtil.convertIp2Bytes(ip);
                System.arraycopy(ipByteArr, 0, values, 1, 4);
            }
            String subMask = wifiLockNetworkConfiguration.getSubnetMask();
            if(!TextUtils.isEmpty(subMask)) {
                byte[] submaskByteArr = DigitUtil.convertIp2Bytes(subMask);
                System.arraycopy(submaskByteArr, 0, values, 5, 4);
            }
            String router = wifiLockNetworkConfiguration.getRouter();
            if(!TextUtils.isEmpty(router)) {
                byte[] routerByteArr = DigitUtil.convertIp2Bytes(router);
                System.arraycopy(routerByteArr, 0, values, 9, 4);
            }
            String dns1 = wifiLockNetworkConfiguration.getPreferredDns();
            if (!TextUtils.isEmpty(dns1)) {
                byte[] dns1ByteArr = DigitUtil.convertIp2Bytes(dns1);
                System.arraycopy(dns1ByteArr, 0, values, 13, 4);
            }
            String dns2 = wifiLockNetworkConfiguration.getAlternateDns();
            if (!TextUtils.isEmpty(dns2)) {
                byte[] dns2ByteArr = DigitUtil.convertIp2Bytes(dns2);
                System.arraycopy(dns2ByteArr, 0, values, 17, 4);
            }
        }
        command.setData(values, transferData.getAesKeyArray());
        transferData.setTransferData(command.buildCommand());
        BluetoothImpl.getInstance().sendCommand(transferData);
    }

}
