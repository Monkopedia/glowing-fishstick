package com.ttlock.bl.sdk.api;

import android.text.TextUtils;
import android.util.Log;

import com.ttlock.bl.sdk.constant.APICommand;
import com.ttlock.bl.sdk.constant.ActionType;
import com.ttlock.bl.sdk.constant.AudioManage;
import com.ttlock.bl.sdk.constant.AutoLockOperate;
import com.ttlock.bl.sdk.constant.ConfigRemoteUnlock;
import com.ttlock.bl.sdk.constant.Constant;
import com.ttlock.bl.sdk.constant.CyclicOpType;
import com.ttlock.bl.sdk.constant.ICOperate;
import com.ttlock.bl.sdk.constant.KeyFobOperationType;
import com.ttlock.bl.sdk.constant.KeyboardPwdType;
import com.ttlock.bl.sdk.constant.LogOperate;
import com.ttlock.bl.sdk.constant.OperationType;
import com.ttlock.bl.sdk.constant.PassageModeOperate;
import com.ttlock.bl.sdk.constant.PwdOperateType;
import com.ttlock.bl.sdk.constant.RemoteControlManage;
import com.ttlock.bl.sdk.constant.SensitivityOperationType;
import com.ttlock.bl.sdk.entity.CyclicConfig;
import com.ttlock.bl.sdk.entity.HotelData;
import com.ttlock.bl.sdk.entity.NBAwakeConfig;
import com.ttlock.bl.sdk.entity.NBAwakeMode;
import com.ttlock.bl.sdk.entity.NBAwakeTime;
import com.ttlock.bl.sdk.entity.PwdInfoV3;
import com.ttlock.bl.sdk.entity.SoundVolume;
import com.ttlock.bl.sdk.entity.TransferData;
import com.ttlock.bl.sdk.entity.ValidityInfo;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.GsonUtil;
import com.ttlock.bl.sdk.util.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by Smartlock on 2016/6/1.
 */
class CommandUtil_V3 {
    private static boolean DBG = true;

    public static void addAdmin_V3(Command command, String adminPassword, String unlockNumber, byte[] aesKeyArray) {
        byte[] values = new byte[4 + 4 + 7];//4 + 4 + 7
        int adminPwd = Integer.valueOf(adminPassword);
        int unlockPwd = Integer.valueOf(unlockNumber);
        byte[] adminPwd_byte = DigitUtil.integerToByteArray(adminPwd);
        byte[] unlockPwd_byte = DigitUtil.integerToByteArray(unlockPwd);
        System.arraycopy(adminPwd_byte, 0, values, 0, adminPwd_byte.length);//4
        System.arraycopy(unlockPwd_byte, 0, values, 4, unlockPwd_byte.length);//4
        System.arraycopy(Constant.SCIENER.getBytes(), 0, values, 8, 7);//7
        command.setData(values, aesKeyArray);
    }

    public static void checkAdmin(Command command, int uid, String adminPs, String unlockKey, int lockFlagPos, byte[] aesKeyArray, int apiCommand) {
        byte[] adminPsByteArray = DigitUtil.integerToByteArray(Integer.valueOf(adminPs));
        byte[]lockFlagPos_byteArray = DigitUtil.integerToByteArray(lockFlagPos);
        byte[] values = new byte[11];
//        LogUtil.d("lockFlagPos:" + lockFlagPos, DBG);
//        LogUtil.d("lockFlagPos_byteArray:" + Arrays.toString(lockFlagPos_byteArray), DBG);
        System.arraycopy(adminPsByteArray, 0, values, 0, adminPsByteArray.length);
        System.arraycopy(lockFlagPos_byteArray, 1, values, 4, 3);
        System.arraycopy(DigitUtil.integerToByteArray(uid), 0, values, 7, 4);
//        LogUtil.d("values:" + Arrays.toString(values), DBG);
        command.setData(values, aesKeyArray);
    }

    /**
     * 设置管理员键盘密码
     * @param command
     * @param adminKeyboardPwd
     * @param aesKeyArray
     */
    public static void setAdminKeyboardPwd(Command command, String adminKeyboardPwd, byte[] aesKeyArray) {
        int len = adminKeyboardPwd.length();
        byte[] values = new byte[len];
        for(int i=0;i<len;i++) {
            values[i] = (byte) (adminKeyboardPwd.charAt(i) - '0');
        }
        command.setData(values, aesKeyArray);
    }

    /**
     * 设置删除密码
     * @param command
     * @param deletePwd
     * @param aesKeyArray
     */
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

    public static void checkUserTime(Command command, int uid, String sDateStr, String eDateStr, String unlockKey, int lockFlagPos, byte[] aesKeyArray, int apiCommand) {
        byte[] values = new byte[17];//5+5+3+4
        byte[] time = DigitUtil.convertTimeToByteArray(sDateStr+eDateStr);
        System.arraycopy(time, 0, values, 0, 10);
        values[10] = (byte) ((lockFlagPos >> 16) & 0xFF);
        values[11] = (byte) ((lockFlagPos >> 8) & 0xFF);
        values[12] = (byte) (lockFlagPos & 0xFF);
        byte[] uidArray = DigitUtil.integerToByteArray(uid);
        System.arraycopy(uidArray, 0, values, 13, 4);
        command.setData(values, aesKeyArray);
    }

    public static void calibationTime_V3(Command command, String timeStr, byte[] aesKeyArray) {
        byte[] timeArray = DigitUtil.convertTimeToByteArray(timeStr);
        command.setData(timeArray, aesKeyArray);
    }

    /**
     * 开门
     * @param command
     * @param sum
     * @param dateTime      时间(也作为记录唯一标识)
     * @param aesKeyArray
     * @param timezoneRawOffSet 时间偏移量
     */
    public static void unlock(Command command, String sum, long dateTime, byte[] aesKeyArray, long timezoneRawOffSet) {
        byte[] values = new byte[4 + 4];//不再进行时间校准
//        if(dateTime > 0) //有时间进行更新
//            values = new byte[14];
//        else
//            values = new byte[8];

        int sumI = Integer.valueOf(sum);
        byte[] sumByteArray = DigitUtil.integerToByteArray(sumI);
        System.arraycopy(sumByteArray, 0, values, 0, sumByteArray.length);
        //时间唯一标识
        int date = (int) (dateTime / 1000);
        if(dateTime <= 0)//没有时间值的用系统时间
            date = (int) (System.currentTimeMillis() / 1000);
        System.arraycopy(DigitUtil.integerToByteArray(date), 0, values, 4, 4);
//        if(dateTime > 0) {//有时间的传时间
//            //根据时间偏移量重新计算时间
//            dateTime = dateTime + timezoneRawOffSet - TimeZone.getDefault().getOffset(System.currentTimeMillis());
//
//            String dateStr = DigitUtil.formateDateFromLong(dateTime, "yyMMddHHmmss");
//            byte[] dateByteArray = DigitUtil.convertTimeToByteArray(dateStr);
//            System.arraycopy(dateByteArray, 0, values, 8, dateByteArray.length);
//        }
        command.setData(values, aesKeyArray);
    }

    public static void activateLiftFloors(Command command, byte[] psFromLock, TransferData transferData) {
        command.setCommand(Command.COMM_UNLOCK);
        long psFromLockL = DigitUtil.fourBytesToLong(psFromLock);
        long unlockKeyL = Long.valueOf(transferData.getUnlockKey());
//        String sum = DigitUtil.getUnlockPwd_new(psFromLockL, unlockKeyL);
        long dateTime = transferData.getUnlockDate();
        if (dateTime <= 0) {//没有时间值的用系统时间
            dateTime = System.currentTimeMillis();
        }
        long timezoneRawOffSet = transferData.getTimezoneOffSet();
        List<Integer> floors = transferData.getActivateFloors();
        int floorSize = floors.size();

        byte[] values = new byte[4 + 4 + 6 + 1 + floorSize];//不再进行时间校准
        byte[] sumByteArray = DigitUtil.getUnlockPwdBytes_new(psFromLockL, unlockKeyL);
        System.arraycopy(sumByteArray, 0, values, 0, sumByteArray.length);
        //时间唯一标识
        int date = (int) (dateTime / 1000);
        System.arraycopy(DigitUtil.integerToByteArray(date), 0, values, 4, 4);
        //根据时间偏移量重新计算时间
        dateTime = dateTime + timezoneRawOffSet - TimeZone.getDefault().getOffset(System.currentTimeMillis());

        String dateStr = DigitUtil.formateDateFromLong(dateTime, "yyMMddHHmmss");
        byte[] dateByteArray = DigitUtil.convertTimeToByteArray(dateStr);
        System.arraycopy(dateByteArray, 0, values, 8, dateByteArray.length);

        values[14] = (byte) floorSize;
        int index = 15;
        for (Integer floorNumber : floors) {
            if (floorNumber != null) {
                values[index++] = floorNumber.byteValue();
            }
        }

        command.setData(values, transferData.getAesKeyArray());

        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void checkRandom(Command command, String sum, byte[] aesKeyArray) {
        int sumI = Integer.valueOf(sum);
        byte[] sumByteArray = DigitUtil.integerToByteArray(sumI);
        command.setData(sumByteArray, aesKeyArray);
    }

    /**
     * 三代锁密码初始化
     * @param lockType
     * @param aesKeyArray
     */
    public static void initPasswords(int lockType, byte[] aesKeyArray, int apiCommand) {
        Command command = new Command(lockType);
        command.setCommand(Command.COMM_INIT_PASSWORDS);
        byte[] values = new byte[61];
        //TODO:calender
        int year = new Date().getYear() + 1900;
        int month = new Date().getMonth();
        int day = new Date().getDate();
        if(month == 0 && day == 1) {
            year--;
        }
        LogUtil.d("year : " + year, DBG);
        Set codeSet = new LinkedHashSet<Integer>();//密码约定数
        while (codeSet.size() < 10) {//约定数最大不能超过1071
            codeSet.add(DigitUtil.getRandomIntegerByUpperBound(1071));
        }

        Set secretKeySet = new LinkedHashSet<String>();//映射数
        while (secretKeySet.size() < 10) {
            secretKeySet.add(DigitUtil.getCheckTable());
        }

        Iterator<Integer> codeIter = codeSet.iterator();
        Iterator<String> secretKeyIter = secretKeySet.iterator();
        int offset = 0;
        //取年份后两位
        values[offset++] = (byte) (year % 100);

//        JsonArray jsonArray = new JsonArray();
        List<PwdInfoV3> list = new ArrayList<PwdInfoV3>();
        for(int i=0;i<10;i++, year++) {
            int code = codeIter.next();
            String secretKey = secretKeyIter.next();
            list.add(PwdInfoV3.getInstance(year, code, secretKey));
            byte[] codeAndKey = DigitUtil.convertCodeAndKeyToByteArray(code, secretKey);
            System.arraycopy(codeAndKey, 0, values, offset, 6);
            offset += 6;
        }

        CommandUtil_V3.initPasswords(command, values, aesKeyArray);

        String pwdInfoSource = GsonUtil.toJson(list);

        long timestamp = System.currentTimeMillis();

        String pwdInfo = CommandUtil.encry(pwdInfoSource, timestamp);

        BluetoothImpl.getInstance().sendCommand(command.buildCommand(), pwdInfo, timestamp, apiCommand);
    }

    public static void initPasswords(Command command, byte[] values, byte[] aesKeyArray) {
        command.setData(values, aesKeyArray);
    }

    public static void resetKeyboardPasswordCount(Command command, byte[] values, byte[] aesKeyArray) {
        command.setData(values, aesKeyArray);
    }

    public static void setLockname(Command command, String lockname, byte[] aesKeyArray) {
        command.setData(lockname.getBytes(), aesKeyArray);
    }

    /**
     * 获取操作日志
     * @param command
     * @param seq
     * @param aesKeyArray
     */
    public static void getOperateLog(Command command, short seq, byte[] aesKeyArray) {
        byte[] values = new byte[2];
        values[0] = (byte) (seq >> 8);
        values[1] = (byte) seq;
        command.setData(values, aesKeyArray);
    }

    public static void getValidKeyboardPassword(Command command, short seq, byte[] aesKeyArray) {
        byte[] values = new byte[2];
        values[0] = (byte) (seq >> 8);
        values[1] = (byte) seq;
        command.setData(values, aesKeyArray);
    }

    public static short parseKeyboardPwd(byte[] datas) {
        //记录总长度
        short recordTotalLen = (short) (datas[0] << 8 | (datas[1] & 0xff));
        if(recordTotalLen == 0)//没有记录可读取
            return recordTotalLen;
        //请求序号
        short nextReq = (short) (datas[2] << 8 | (datas[3] & 0xff));
        //记录长度index
        int dataIndex = 4;
        while(dataIndex + 1 < datas.length) {
            //单条记录长度
            int recLen = datas[dataIndex++];
            //密码类型
            int pwdType = datas[dataIndex++];
            //密码长度
            int pwdLen = datas[dataIndex++];
            //密码
            String pwd = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + pwdLen));
            dataIndex += pwdLen;
            //原始密码长度
            int originPwdLen = datas[dataIndex++];
            //原始密码
            String originalPwd = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + originPwdLen));
            dataIndex += originPwdLen;
            switch (pwdType) {
                case KeyboardPwdType.PWD_TYPE_COUNT:
                {
                    int count = (int) DigitUtil.fourBytesToLong(Arrays.copyOfRange(datas, dataIndex, dataIndex + 4));
                    dataIndex += 4;
                    break;
                }
                case KeyboardPwdType.PWD_TYPE_PERIOD:
                {
                    int year = datas[dataIndex++];
                    int mounth = datas[dataIndex++];
                    int day = datas[dataIndex++];
                    int hour = datas[dataIndex++];
                    int minute = datas[dataIndex++];
                    break;
                }
            }
        }
        return nextReq;
    }

    public static void searchPwd(Command command, short seq, byte[] aesKey) {
        byte[] values = new byte[2];
        values[0] = (byte) (seq >> 8);
        values[1] = (byte) seq;
        command.setData(values, aesKey);
    }

    public static void manageKeyboardPassword(Command command, byte pwdOperateType, byte keyboardPwdType, int cycleType, String originalPwd, String newPwd, long startDate, long endDate, byte[] aesKeyArray, long timezoneOffset) {
        byte[] values = null;
        int index = 0;

        Calendar calendar = Calendar.getInstance();
        //根据时间偏移量计算时间
        TimeZone timeZone = TimeZone.getDefault();
        if (timeZone.inDaylightTime(new Date(System.currentTimeMillis())))
            timezoneOffset -= timeZone.getDSTSavings();
        timeZone.setRawOffset((int) timezoneOffset);
        calendar.setTimeZone(timeZone);

        Date date = new Date(startDate);
        calendar.setTime(date);

        LogUtil.e("timezoneOffset:" + timezoneOffset, DBG);
        LogUtil.e("startDate:" + startDate, DBG);
        LogUtil.e("endDate:" + endDate, DBG);
        LogUtil.d("pwdOperateType:" + pwdOperateType, DBG);

        switch (pwdOperateType) {
            case PwdOperateType.PWD_OPERATE_TYPE_CLEAR://清空
            {
                values = new byte[1];
                values[index++] = pwdOperateType;
                break;
            }
            case PwdOperateType.PWD_OPERATE_TYPE_ADD://添加
            {
//                Calendar calendar = Calendar.getInstance();
//                Date date = new Date(startDate);
//                calendar.setTime(date);

                byte originalPwdLen = (byte) originalPwd.length();
                switch (keyboardPwdType) {
                    case KeyboardPwdType.PWD_TYPE_COUNT://单次
                    case KeyboardPwdType.PWD_TYPE_PERIOD://期限
                    {
                        values = new byte[1 + 1 + 1 + originalPwdLen + 5 + 5];
                        values[index++] = pwdOperateType;
                        values[index++] = keyboardPwdType;
                        values[index++] = originalPwdLen;

                        System.arraycopy(originalPwd.getBytes(), 0, values, index, originalPwdLen);
                        index += originalPwdLen;

                        values[index++] = (byte) (calendar.get(Calendar.YEAR) % 100);
                        values[index++] = (byte) (calendar.get(Calendar.MONTH) + 1);
                        values[index++] = (byte) calendar.get(Calendar.DAY_OF_MONTH);
                        values[index++] = (byte) calendar.get(Calendar.HOUR_OF_DAY);
                        values[index++] = (byte) calendar.get(Calendar.MINUTE);
                        LogUtil.d("y:" + values[index -5]);
                        LogUtil.d("m:" + values[index -4]);
                        LogUtil.d("d:" + values[index -3]);
                        LogUtil.d("h:" + values[index -2]);
                        LogUtil.d("m:" + values[index -1]);
                        LogUtil.d("hour:" + calendar.get(Calendar.HOUR_OF_DAY), DBG);
                        date = new Date(endDate);
                        calendar.setTime(date);
                        values[index++] = (byte) (calendar.get(Calendar.YEAR) % 100);
                        values[index++] = (byte) (calendar.get(Calendar.MONTH) + 1);
                        values[index++] = (byte) calendar.get(Calendar.DAY_OF_MONTH);
                        values[index++] = (byte) calendar.get(Calendar.HOUR_OF_DAY);
                        values[index++] = (byte) calendar.get(Calendar.MINUTE);
//                        LogUtil.d("小时:" + calendar.get(Calendar.HOUR_OF_DAY), DBG);
                        break;
                    }
                    case KeyboardPwdType.PWD_TYPE_PERMANENT://永久
                    {
                        values = new byte[1 + 1 + 1 + originalPwdLen + 5];
                        values[index++] = pwdOperateType;
                        values[index++] = keyboardPwdType;
                        values[index++] = originalPwdLen;

                        System.arraycopy(originalPwd.getBytes(), 0, values, index, originalPwdLen);
                        index += originalPwdLen;

                        values[index++] = (byte) (calendar.get(Calendar.YEAR) % 100);
                        values[index++] = (byte) (calendar.get(Calendar.MONTH) + 1);
                        values[index++] = (byte) calendar.get(Calendar.DAY_OF_MONTH);
                        values[index++] = (byte) calendar.get(Calendar.HOUR_OF_DAY);
                        values[index++] = (byte) calendar.get(Calendar.MINUTE);
                        break;
                    }
                    default:
                        break;
                }
                break;
            }
            case PwdOperateType.PWD_OPERATE_TYPE_REMOVE_ONE://删除单个
            {
                byte originalPwdLen = (byte) originalPwd.length();
                values = new byte[1 + 1 + 1 + originalPwdLen];
                values[index++] = pwdOperateType;
                values[index++] = keyboardPwdType;
                values[index++] = originalPwdLen;
                System.arraycopy(originalPwd.getBytes(), 0, values, index, originalPwdLen);
                break;
            }
            case PwdOperateType.PWD_OPERATE_TYPE_MODIFY://修改
            {
                byte originalPwdLen = (byte) originalPwd.length();
                //新密码长度
                byte newPwdLen = 0;

                if(!TextUtils.isEmpty(newPwd))
                    newPwdLen = (byte) newPwd.length();

                if(startDate <= 0 || endDate <= 0)//不修改期限
                    values = new byte[1 + 1 + 1 + originalPwdLen + 1 + newPwdLen];
                else
                    values = new byte[1 + 1 + 1 + originalPwdLen + 1 + newPwdLen + 5 + 5];

                values[index++] = pwdOperateType;
                values[index++] = keyboardPwdType;
                values[index++] = originalPwdLen;

                System.arraycopy(originalPwd.getBytes(), 0, values, index, originalPwdLen);
                index += originalPwdLen;

                values[index++] = newPwdLen;
                System.arraycopy(newPwd.getBytes(), 0, values, index, newPwdLen);
                index += newPwdLen;

                if(!(startDate <= 0 || endDate <= 0)) {//修改期限
                    values[index++] = (byte) (calendar.get(Calendar.YEAR) % 100);
                    values[index++] = (byte) (calendar.get(Calendar.MONTH) + 1);
                    values[index++] = (byte) calendar.get(Calendar.DAY_OF_MONTH);
                    values[index++] = (byte) calendar.get(Calendar.HOUR_OF_DAY);
                    values[index++] = (byte) calendar.get(Calendar.MINUTE);
                    date = new Date(endDate);
                    calendar.setTime(date);
                    values[index++] = (byte) (calendar.get(Calendar.YEAR) % 100);
                    values[index++] = (byte) (calendar.get(Calendar.MONTH) + 1);
                    values[index++] = (byte) calendar.get(Calendar.DAY_OF_MONTH);
                    values[index++] = (byte) calendar.get(Calendar.HOUR_OF_DAY);
                    values[index++] = (byte) calendar.get(Calendar.MINUTE);
                }
                break;
            }
            case PwdOperateType.PWD_OPERATE_TYPE_RECOVERY://恢复
            {
                byte originalPwdLen = (byte) originalPwd.length();
                //新密码长度
                byte newPwdLen = 0;

                if(!TextUtils.isEmpty(newPwd))
                    newPwdLen = (byte) newPwd.length();

                LogUtil.e("originalPwd:" + originalPwd, DBG);
                LogUtil.e("newPwd:" + newPwd, DBG);
                LogUtil.e("keyboardPwdType:" + keyboardPwdType, DBG);

                switch (keyboardPwdType) {
                    case KeyboardPwdType.PWD_TYPE_COUNT:
                    case KeyboardPwdType.PWD_TYPE_PERMANENT:
                        values = new byte[1 + 1 + 1 + originalPwdLen + 1 + newPwdLen + 5];
                        break;
                    case KeyboardPwdType.PWD_TYPE_CIRCLE:
                        values = new byte[1 + 1 + 1 + originalPwdLen + 1 + newPwdLen + 5 + 2];
                        break;
                    case KeyboardPwdType.PWD_TYPE_PERIOD:
                        values = new byte[1 + 1 + 1 + originalPwdLen + 1 + newPwdLen + 5 + 5];
                        break;
                }

                values[index++] = pwdOperateType;
                values[index++] = keyboardPwdType;
                values[index++] = originalPwdLen;

                System.arraycopy(originalPwd.getBytes(), 0, values, index, originalPwdLen);
                index += originalPwdLen;

                values[index++] = newPwdLen;
                System.arraycopy(newPwd.getBytes(), 0, values, index, newPwdLen);
                index += newPwdLen;

                //起始时间
                values[index++] = (byte) (calendar.get(Calendar.YEAR) % 100);
                values[index++] = (byte) (calendar.get(Calendar.MONTH) + 1);
                values[index++] = (byte) calendar.get(Calendar.DAY_OF_MONTH);
                values[index++] = (byte) calendar.get(Calendar.HOUR_OF_DAY);
                values[index++] = (byte) calendar.get(Calendar.MINUTE);

                switch (keyboardPwdType) {
                    case KeyboardPwdType.PWD_TYPE_CIRCLE:
                        values[index++] = (byte) (cycleType >> 8);
                        values[index++] = (byte) cycleType;
                        break;
                    case KeyboardPwdType.PWD_TYPE_PERIOD:
                        date = new Date(endDate);
                        calendar.setTime(date);
                        values[index++] = (byte) (calendar.get(Calendar.YEAR) % 100);
                        values[index++] = (byte) (calendar.get(Calendar.MONTH) + 1);
                        values[index++] = (byte) calendar.get(Calendar.DAY_OF_MONTH);
                        values[index++] = (byte) calendar.get(Calendar.HOUR_OF_DAY);
                        values[index++] = (byte) calendar.get(Calendar.MINUTE);
                        LogUtil.e("calendar.get(Calendar.DAY_OF_MONTH):" + calendar.get(Calendar.DAY_OF_MONTH), DBG);
                        break;
                        default:
                            break;
                }

                break;
            }
            default:
                break;
        }
        command.setData(values, aesKeyArray);
    }

    public static short parseOperateLog(List<LogOperate> logOperates,byte[] datas, long timezoneOffSet) {
        //记录总长度
        LogUtil.w("begin---------------", DBG);
//        LogUtil.e("datas:" + DigitUtil.byteArrayToHexString(datas), DBG);
        int recordTotalLen = (datas[0] << 8 | (datas[1] & 0xff)) & 0xffff;
        if(recordTotalLen == 0)//没有记录可读
            return (short) 0xFFF0;//记录读取完成
        short nextReq = (short) (datas[2] << 8 | (datas[3] & 0xff));

        //索引下标
        int dataIndex = 4;
//        LogUtil.d("recordTotalLen:" + recordTotalLen, DBG);
//        LogUtil.d("nextReq:" + nextReq, DBG);
//        LogUtil.d("datas.length:" + datas.length, DBG);
        try {
            while(dataIndex + 1 < datas.length) {
//                LogUtil.d("begin", DBG);
//                LogUtil.d("dataIndex:" + dataIndex, DBG);
                LogOperate logOperate = new LogOperate();

                //单条记录长度
                int recLen = datas[dataIndex++];
                //下一条记录开始的索引值
                int nextRecIndex = dataIndex + recLen;
//                LogUtil.d("recLen:" + recLen, DBG);
                //操作类型
                int operateType = datas[dataIndex++];
//                LogUtil.d("operateType:" + operateType, DBG);
                logOperate.setRecordType(operateType);
                //年
                int year = datas[dataIndex++] + 2000;
                //月
                int month = datas[dataIndex++];
                //日
                int day = datas[dataIndex++];
                //小时
                int hour = datas[dataIndex++];
                //分钟
                int minute = datas[dataIndex++];
                //秒
                int second = datas[dataIndex++];

                Calendar calendar = Calendar.getInstance();
                //根据时间偏移量计算时间
                TimeZone timeZone = TimeZone.getDefault();
                LogUtil.d("timezoneOffSet:" + timezoneOffSet, DBG);
                if (timeZone.inDaylightTime(new Date(System.currentTimeMillis())))
                    timezoneOffSet -= timeZone.getDSTSavings();
                timeZone.setRawOffset((int) timezoneOffSet);
                calendar.setTimeZone(timeZone);

                calendar.set(year, month - 1, day, hour, minute, second);
                logOperate.setOperateDate(calendar.getTimeInMillis() / 1000 * 1000);

//                LogUtil.d("year:" + year, DBG);
//                LogUtil.d("month:" + month, DBG);
//                LogUtil.d("day:" + day, DBG);
//                LogUtil.d("hour:" + hour, DBG);
//                LogUtil.d("minute:" + minute, DBG);
//                LogUtil.d("second:" + second, DBG);

                //电量
                int electricQuantity = datas[dataIndex++];
                logOperate.setElectricQuantity(electricQuantity);

//                LogUtil.d("electricQuantity:" + electricQuantity, DBG);

                //TODO:保存解析数据
                switch(operateType) {
                    case LogOperate.OPERATE_TYPE_MOBILE_UNLOCK://手机开锁
                    case LogOperate.OPERATE_BLE_LOCK://蓝牙闭锁
                    case LogOperate.GATEWAY_UNLOCK:
                    case LogOperate.APP_UNLOCK_FAILED_LOCK_REVERSE:
                    {
                        int uid;
                        int uuid;
                        uid = (int) DigitUtil.fourBytesToLong(Arrays.copyOfRange(datas, dataIndex, dataIndex + 4));
//                        LogUtil.d("uid:" + uid, DBG);
                        dataIndex += 4;
                        uuid = (int) DigitUtil.fourBytesToLong(Arrays.copyOfRange(datas, dataIndex, dataIndex + 4));
//                        LogUtil.d("uuid:" + uuid, DBG);
                        dataIndex += 4;
                        logOperate.setUid(uid);
                        logOperate.setRecordId(uuid);
                        break;
                    }
                    case LogOperate.REMOTE_CONTROL_KEY:
                    {
                        int uid;
                        int uuid;
                        uid = (int) DigitUtil.fourBytesToLong(Arrays.copyOfRange(datas, dataIndex, dataIndex + 4));
//                        LogUtil.d("uid:" + uid, DBG);
                        dataIndex += 4;
                        uuid = (int) DigitUtil.fourBytesToLong(Arrays.copyOfRange(datas, dataIndex, dataIndex + 4));
//                        LogUtil.d("uuid:" + uuid, DBG);
                        dataIndex += 4;
                        logOperate.setUid(uid);
                        logOperate.setRecordId(uuid);
                        logOperate.setKeyId(datas[dataIndex++]);
                        break;
                    }
                    case LogOperate.OPERATE_TYPE_KEYBOARD_PASSWORD_UNLOCK://键盘密码开锁
                    case LogOperate.OPERATE_TYPE_USE_DELETE_CODE:
                    case LogOperate.OPERATE_TYPE_PASSCODE_EXPIRED:
                    case LogOperate.OPERATE_TYPE_SPACE_INSUFFICIENT:
                    case LogOperate.OPERATE_TYPE_PASSCODE_IN_BLACK_LIST:
                    case LogOperate.PASSCODE_LOCK:
                    case LogOperate.PASSCODE_UNLOCK_FAILED_LOCK_REVERSE:
                    {

//                        LogUtil.d("dataIndex:" + dataIndex, DBG);
                        //原始密码长度
                        int originalPwdLen = datas[dataIndex++];
//                        LogUtil.d("originalPwdLen:" + originalPwdLen, DBG);
                        //原始密码
                        String originalPwd = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + originalPwdLen));
//                        LogUtil.d("originalPwd:" + originalPwd, DBG);
                        dataIndex += originalPwdLen;
                        //开锁密码长度
                        int unlockPwdLen = datas[dataIndex++];
//                        LogUtil.d("unlockPwdLen:" + unlockPwdLen, DBG);
                        //开锁密码
                        String unlockPwd = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + unlockPwdLen));
//                        LogUtil.d("unlockPwd:" + unlockPwd, DBG);
                        dataIndex += unlockPwdLen;

                        logOperate.setPassword(originalPwd);
                        logOperate.setNewPassword(unlockPwd);
                        break;
                    }
                    case LogOperate.OPERATE_TYPE_KEYBOARD_MODIFY_PASSWORD://修改密码
                    {
                        //原始密码长度
                        int originalPwdLen = datas[dataIndex++];
                        //原始密码
                        String originalPwd = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + originalPwdLen));
                        dataIndex += originalPwdLen;
                        //新密码长度
                        int newPwdLen = datas[dataIndex++];
                        //新密码
                        String newPwd = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + newPwdLen));
                        dataIndex += newPwdLen;

                        logOperate.setPassword(originalPwd);
                        logOperate.setNewPassword(newPwd);
                        break;
                    }
                    case LogOperate.OPERATE_TYPE_KEYBOARD_REMOVE_SINGLE_PASSWORD://删除单个密码
                    {
                        //原始密码长度
                        int originalPwdLen = datas[dataIndex++];
                        //原始密码
                        String originalPwd = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + originalPwdLen));
                        dataIndex += originalPwdLen;
                        //删除密码长度
                        int removePwdLen = datas[dataIndex++];
                        //删除密码
                        String removePwd = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + removePwdLen));
                        dataIndex += removePwdLen;

                        logOperate.setPassword(originalPwd);
                        logOperate.setNewPassword(removePwd);
                        break;
                    }
                    case LogOperate.OPERATE_TYPE_ERROR_PASSWORD_UNLOCK://错误密码开锁
                    {
                        //错误开锁密码长度
                        int errUnlockPwdLen = datas[dataIndex++];
                        //错误开锁密码
                        String errUnlockPwd = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + errUnlockPwdLen));
                        dataIndex += errUnlockPwdLen;

                        logOperate.setPassword(errUnlockPwd);
                        break;
                    }
                    case LogOperate.OPERATE_TYPE_KEYBOARD_REMOVE_ALL_PASSWORDS://删除所有密码
                    {
                        year = datas[dataIndex++];
                        month = datas[dataIndex++];
                        day = datas[dataIndex++];
                        hour = datas[dataIndex++];
                        minute = datas[dataIndex++];

//                        LogUtil.d("year:" + year, DBG);
//                        LogUtil.d("month:" + month, DBG);
//                        LogUtil.d("day:" + day, DBG);
//                        LogUtil.d("hour:" + hour, DBG);
//                        LogUtil.d("minute:" + minute, DBG);
                        if(dataIndex < nextRecIndex) {
                            int len = datas[dataIndex++];
                            String passcode = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + len));
                            logOperate.setPassword(passcode);
                        }

                        calendar.set(2000 + year, month - 1, day, hour, minute);

                        logOperate.setDeleteDate(calendar.getTimeInMillis());
                        break;
                    }
                    case LogOperate.OPERATE_TYPE_KEYBOARD_PASSWORD_KICKED://密码被挤掉
                    {
                        //原始密码长度
                        int originalPwdLen = datas[dataIndex++];
                        //原始密码
                        String originalPwd = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + originalPwdLen));
                        dataIndex += originalPwdLen;
                        //被挤掉的密码长度
                        int kickedPwdLen = datas[dataIndex++];
                        //删除密码
                        String kickedPwd = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + kickedPwdLen));
                        dataIndex += kickedPwdLen;

                        logOperate.setPassword(originalPwd);
                        logOperate.setNewPassword(kickedPwd);
                        break;
                    }
                    case LogOperate.OPERATE_TYPE_ADD_IC:
                    case LogOperate.OPERATE_TYPE_DELETE_IC_SUCCEED:
                    case LogOperate.OPERATE_TYPE_IC_UNLOCK_SUCCEED:
                    case LogOperate.OPERATE_TYPE_IC_UNLOCK_FAILED:
                    case LogOperate.IC_LOCK:
                    case LogOperate.IC_UNLOCK_FAILED_LOCK_REVERSE:
                    case LogOperate.IC_UNLOCK_FAILED_BLANKLIST:
                    case LogOperate.CPU_CARD_UNLOCK_FAILED:
//                        Long cardNo = DigitUtil.fourBytesToLong(Arrays.copyOfRange(datas, dataIndex, dataIndex + 4));
                        Long cardNo = DigitUtil.bytesToLong(Arrays.copyOfRange(datas, dataIndex, nextRecIndex));
                        LogUtil.d("card len:" + (nextRecIndex - dataIndex));
                        LogUtil.d("cardNo:" + cardNo);
                        logOperate.setPassword(String.valueOf(cardNo));
//                        dataIndex += 4;
                        break;
                    case LogOperate.OPERATE_TYPE_BONG_UNLOCK_SUCCEED:
                        String address = DigitUtil.getMacString(Arrays.copyOfRange(datas, dataIndex, dataIndex + 6));
                        logOperate.setPassword(address);
                        dataIndex += 6;
                        break;
                    case LogOperate.OPERATE_TYPE_FR_UNLOCK_SUCCEED:
                    case LogOperate.OPERATE_TYPE_ADD_FR:
                    case LogOperate.OPERATE_TYPE_FR_UNLOCK_FAILED:
                    case LogOperate.OPERATE_TYPE_DELETE_FR_SUCCEED:
                    case LogOperate.FR_LOCK:
                    case LogOperate.FR_UNLOCK_FAILED_LOCK_REVERSE:
                        Long FNNo = DigitUtil.sixBytesToLong(Arrays.copyOfRange(datas, dataIndex, dataIndex + 6));
                        logOperate.setPassword(String.valueOf(FNNo));
                        dataIndex += 6;
                        if (dataIndex < nextRecIndex) {//还有密码记录
                            int pwdLen = datas[dataIndex++];
                            String pwd = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + pwdLen));
                            dataIndex += pwdLen;
                            //TODO:保存密码记录
                        }
                        break;
                    case LogOperate.WIRELESS_KEY_FOB:
                        String mac = DigitUtil.getMacString(Arrays.copyOfRange(datas, dataIndex, dataIndex + 6));
                        logOperate.setPassword(mac);
                        dataIndex += 6;
                        logOperate.setKeyId(datas[dataIndex++]);
                        logOperate.setAccessoryElectricQuantity(datas[dataIndex++]);
                        break;
                    case LogOperate.WIRELESS_KEY_PAD:
                        mac = DigitUtil.getMacString(Arrays.copyOfRange(datas, dataIndex, dataIndex + 6));
                        logOperate.setPassword(mac);
                        dataIndex += 6;
                        logOperate.setAccessoryElectricQuantity(datas[dataIndex++]);
                        break;
                    default:
                    {

                    }
                }
//                LogUtil.d("logOperate:" + logOperate);
                LogUtil.w("end", DBG);
                logOperates.add(logOperate);
                //增强兼容性
                dataIndex = nextRecIndex;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.d("end---------------", DBG);
        return nextReq;
    }

    /**
     *
     * @param command
     * @param ICOp  操作
     * @param seq   序号
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @param timezoneOffSet    时间偏移量
     */
    public static void ICManage(Command command, byte ICOp, short seq, String cardNoStr, long startDate, long endDate, byte[] aesKey, long timezoneOffSet) {
        byte[] values = null;
        switch (ICOp) {
            case ICOperate.IC_SEARCH:
                values = new byte[3];
                values[0] = ICOp;
                values[1] = (byte) (seq >> 8);
                values[2] = (byte) seq;
                break;
            case ICOperate.ADD:
                if(!TextUtils.isEmpty(cardNoStr)) {

                    if (cardNoStr.length() > 10)
                        values = new byte[19];
                    else
                        values = new byte[15];
                    values[0] = ICOp;

                    if(startDate == 0 || endDate == 0) {//有时间
                        startDate = CommandUtil.permanentStartDate;
                        endDate = CommandUtil.permanentEndDate;
                    }

                    long cardNo = Long.valueOf(cardNoStr);

                    byte[] cardBytes;
                    if (cardNoStr.length() > 10) {
                        cardBytes = DigitUtil.longToByteArrayWithLen(cardNo, 8);
                    } else {
                        cardBytes = DigitUtil.integerToByteArray((int)cardNo);
                    }

                    //使用时间偏移量重新计算时间
                    startDate = startDate + timezoneOffSet - TimeZone.getDefault().getOffset(System.currentTimeMillis());
                    endDate = endDate + timezoneOffSet - TimeZone.getDefault().getOffset(System.currentTimeMillis());

                    String sDate = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmm");
                    String eDate = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmm");
                    byte[] time = DigitUtil.convertTimeToByteArray(sDate + eDate);

                    LogUtil.d("sDate:" + sDate, DBG);
                    LogUtil.d("eDate:" + eDate, DBG);

                    System.arraycopy(cardBytes, 0, values, 1, cardBytes.length);
                    System.arraycopy(time, 0, values, 1 + cardBytes.length, 10);

                } else {//进入添加模式
                    values = new byte[1];
                    values[0] = ICOp;
                }
                break;
            case ICOperate.CLEAR:
                values = new byte[1];
                values[0] = ICOp;
                break;
            case ICOperate.MODIFY:
                if (cardNoStr.length() > 10)
                    values = new byte[19];
                else
                    values = new byte[15];
                values[0] = ICOp;

                long cardNo = Long.valueOf(cardNoStr);

                byte[] cardBytes;
                if (cardNoStr.length() > 10) {
                    cardBytes = DigitUtil.longToByteArrayWithLen(cardNo, 8);
                } else {
                    cardBytes = DigitUtil.integerToByteArray((int)cardNo);
                }
                //使用时间偏移量重新计算时间
                startDate = startDate + timezoneOffSet - TimeZone.getDefault().getOffset(System.currentTimeMillis());
                endDate = endDate + timezoneOffSet - TimeZone.getDefault().getOffset(System.currentTimeMillis());

                String sDate = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmm");
                String eDate = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmm");
                byte[] time = DigitUtil.convertTimeToByteArray(sDate + eDate);

                LogUtil.d("sDate:" + sDate, DBG);
                LogUtil.d("eDate:" + eDate, DBG);

                System.arraycopy(cardBytes, 0, values, 1, cardBytes.length);
                System.arraycopy(time, 0, values, 1 + cardBytes.length, 10);
                LogUtil.d(sDate + eDate, DBG);
                break;
            case ICOperate.DELETE:
                if (cardNoStr.length() > 10)
                    values = new byte[9];
                else
                    values = new byte[5];

                values[0] = ICOp;

                cardNo = Long.valueOf(cardNoStr);

                if (cardNoStr.length() > 10) {
                    cardBytes = DigitUtil.longToByteArrayWithLen(cardNo, 8);
                } else {
                    cardBytes = DigitUtil.integerToByteArray((int)cardNo);
                }

                System.arraycopy(cardBytes, 0, values, 1, cardBytes.length);
                break;
        }
        LogUtil.d("arrays:" + DigitUtil.byteArrayToHexString(values), DBG);
        command.setData(values, aesKey);
    }

    /**
     *
     * @param command
     * @param FROp  操作
     * @param seq   序号
     * @param FRNo    指纹号 6个字节
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @param timezoneOffSet    时间偏移量
     */
    public static void FRManage(Command command, byte FROp, short seq, long FRNo, long startDate, long endDate, byte[] aesKey, long timezoneOffSet) {
        byte[] values = null;
        switch (FROp) {
            case ICOperate.FR_SEARCH:
                values = new byte[3];
                values[0] = FROp;
                values[1] = (byte) (seq >> 8);
                values[2] = (byte) seq;
                break;
            case ICOperate.ADD:
                if(FRNo > 0) {//恢复
                    values = new byte[17];
                    values[0] = FROp;
                    byte[] FRBytes = DigitUtil.longToByteArrayWithLen(FRNo, 6);
                    LogUtil.d("FRBytes:" + DigitUtil.sixBytesToLong(FRBytes), DBG);
                    if(startDate == 0 || endDate == 0) {
                        startDate = CommandUtil.permanentStartDate;
                        endDate = CommandUtil.permanentEndDate;
                    }
                    //使用时间偏移量重新计算时间
                    startDate = startDate + timezoneOffSet - TimeZone.getDefault().getOffset(System.currentTimeMillis());
                    endDate = endDate + timezoneOffSet - TimeZone.getDefault().getOffset(System.currentTimeMillis());

                    String sDate = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmm");
                    String eDate = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmm");
                    byte[] time = DigitUtil.convertTimeToByteArray(sDate + eDate);
                    System.arraycopy(FRBytes, 0, values, 1, 6);
                    System.arraycopy(time, 0, values, 7, 10);
                }
//                else if(FRNo > 0) {
//                    values = new byte[7];
//                    values[0] = FROp;
//                    byte[] FRBytes = DigitUtil.longToByteArrayWithLen(FRNo, 6);
//                    System.arraycopy(FRBytes, 0, values, 1, 6);
//                }
                else {//添加
                    values = new byte[1];
                    values[0] = FROp;
                }
                break;
            case ICOperate.CLEAR:
                values = new byte[1];
                values[0] = FROp;
                break;
            case ICOperate.MODIFY:
                values = new byte[17];
                values[0] = FROp;
                LogUtil.d("FRNo:" + FRNo, DBG);
                byte[] FRBytes = DigitUtil.longToByteArrayWithLen(FRNo, 6);
                LogUtil.d("FRBytes:" + DigitUtil.sixBytesToLong(FRBytes), DBG);
                //使用时间偏移量重新计算时间
                startDate = startDate + timezoneOffSet - TimeZone.getDefault().getOffset(System.currentTimeMillis());
                endDate = endDate + timezoneOffSet - TimeZone.getDefault().getOffset(System.currentTimeMillis());

                String sDate = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmm");
                String eDate = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmm");
                byte[] time = DigitUtil.convertTimeToByteArray(sDate + eDate);
                System.arraycopy(FRBytes, 0, values, 1, 6);
                System.arraycopy(time, 0, values, 7, 10);
                LogUtil.d(sDate + eDate, DBG);
                break;
            case ICOperate.DELETE:
                values = new byte[7];
                values[0] = FROp;
                LogUtil.d("FRNO convert:" + DigitUtil.sixBytesToLong(DigitUtil.longToByteArrayWithLen(FRNo, 6)), DBG);
                System.arraycopy(DigitUtil.longToByteArrayWithLen(FRNo, 6), 0, values, 1, 6);
                LogUtil.d(DigitUtil.byteArrayToHexString(values), DBG);
                break;
        }
        command.setData(values, aesKey);
    }

    public static void FRManage(Command command, byte FROp, short seq, long FRNo, long startDate, long endDate, byte[] aesKey) {
        FRManage(command, FROp, seq, FRNo, startDate, endDate, aesKey, 0);
    }

    public static void setWristKey(Command command, String wristKey, byte[] aesKeyArray) {
        command.setData(wristKey.getBytes(), aesKeyArray);
//        command.data = wristKey.getBytes();
//        command.length = (byte) command.data.length;
    }

    /**
     * 闭锁管理
     * @param command
     * @param op 操作码
     * @param time 闭锁时间
     */
    public static void autoLockManage(Command command, byte op, short time, byte[] aesKey) {
        byte[] values = null;
        switch (op) {
            case AutoLockOperate.SEARCH:
                values = new byte[1];
                values[0] = op;
                break;
            case AutoLockOperate.MODIFY:
                values = new byte[3];
                values[0] = op;
                values[1] = (byte) (time >> 8);
                values[2] = (byte) time;
                break;
        }
//        LogUtil.d(DigitUtil.byteArrayToHexString(values), DBG);
        command.setData(values, aesKey);
    }

    /**
     *
     * @param op
     * @param opValue
     */
    public static void operateDoorSensor(Command command, byte op, byte opValue, byte[] aesKey) {
        command.setCommand(Command.COMM_AUTO_LOCK_MANAGE);
        byte[] values = null;
        switch (op) {
            case AutoLockOperate.SEARCH:
                values = new byte[1];
                values[0] = op;
                break;
            case AutoLockOperate.MODIFY:
                values = new byte[4];
                values[0] = op;
                //0xffff不修改自动闭锁时间
                values[1]= (byte)0xff;
                values[2]= (byte)0xff;
                values[3] = opValue;
                break;
        }
        command.setData(values, aesKey);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 闭锁
     * @param command
     * @param sum
     * @param dateTime      时间(也作为记录唯一标识)
     * @param aesKeyArray
     */
    public static void lock(Command command, String sum, long dateTime, byte[] aesKeyArray) {
        byte[] values = new byte[8];
        int sumI = Integer.valueOf(sum);
        byte[] sumByteArray = DigitUtil.integerToByteArray(sumI);
        System.arraycopy(sumByteArray, 0, values, 0, sumByteArray.length);
        //时间唯一标识
        int date = (int) (dateTime / 1000);
        System.arraycopy(DigitUtil.integerToByteArray(date), 0, values, 4, 4);
        command.setData(values, aesKeyArray);
    }

    /**
     * 屏幕密码管理
     * @param command
     * @param opType  1 - 查询 2 - 隐藏 3 - 显示
     * @param aesKeyArray
     */
    public static void screenPasscodeManage(Command command, int opType, byte[] aesKeyArray) {
        byte[] values;
        if(opType == 1) {//查询
            values = new byte[]{1};
        } else {
            values = new byte[]{2, (byte) (opType-2)};
        }
        command.setData(values, aesKeyArray);
    }

    /**
     * 开启或者关闭远程开锁功能
     * @param command
     * @param opType 1 - 查询 2 - 修改
     * @param opValue     0 - 关闭 1 - 开启
     */
    public static void controlRemoteUnlock(Command command, byte opType, byte opValue, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_CONTROL_REMOTE_UNLOCK);
        byte[] values = null;
        switch (opType) {
            case ConfigRemoteUnlock.OP_TYPE_SEARCH:
                values = new byte[]{opType};
                break;
            case ConfigRemoteUnlock.OP_TYPE_MODIFY:
                values = new byte[]{opType, opValue};
                break;
        }
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * query audio status or turn-on、turn-off audio
     * @param command
     * @param opType
     * @param opValue
     * @param aesKeyArray
     */
    public static void audioManage(Command command, byte opType, byte opValue, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_AUDIO_MANAGE);
        byte[] values = null;
        switch (opType) {
            case AudioManage.QUERY:
                values = new byte[]{opType};
                break;
            case AudioManage.MODIFY:
                values = new byte[]{opType, opValue};
                break;
        }
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 音量设置
     * 启用或者禁用语音提示（1 Byte）	音量（1 Byte）	语言（1 byte）
     * 0-	禁用1-	启用	1~5级音量
     *
     * 0-不修改音量	当前为第几种语言，喇叭版本才支持
     * 0-不修改语言
     * 1-第一种语音，常规为英文
     * 2-第二种语音，常规为中文
     * 3-第三种语音，一般产品没有
     * 蜂鸣器版本，此参数无效，可以设置任意值
     * @param command
     * @param transferData
     */
    public static void setLockSound(Command command, TransferData transferData) {
        command.setCommand(Command.COMM_AUDIO_MANAGE);
        byte[] values = null;
        SoundVolume soundVolume = transferData.getSoundVolume();
        //为空的情况当做关闭
        soundVolume = soundVolume == null ? SoundVolume.OFF : soundVolume;

        switch (soundVolume) {
            case OFF://关闭
                values = new byte[2];
                values[0] = AudioManage.MODIFY;
                values[1] = AudioManage.TURN_OFF;
                break;
            case ON://旧版本的开启
                values = new byte[2];
                values[0] = AudioManage.MODIFY;
                values[1] = AudioManage.TURN_ON;
                break;
            default://音量设置
                values = new byte[4];
                values[0] = AudioManage.MODIFY;
                values[1] = AudioManage.TURN_ON;
                values[2] = (byte) soundVolume.getValue();
                values[3] = 0;//当前无语言设置
                break;
        }
        command.setData(values, transferData.getAesKeyArray());
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void getLockSound(Command command, TransferData transferData) {
        command.setCommand(Command.COMM_AUDIO_MANAGE);
        byte[] values = {AudioManage.QUERY};
        command.setData(values, transferData.getAesKeyArray());
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     *
     * @param command
     * @param opType
     * @param opValue
     * @param aesKeyArray
     */
    public static void remoteControlManage(Command command, byte opType, byte opValue, byte[] unlockPwdBytes, byte[] uniqueidBytes, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_REMOTE_CONTROL_DEVICE_MANAGE);
        Log.d("OMG","=remoteControlManage==");
        byte[] values = null;
        switch (opType) {
            case RemoteControlManage.QUERY:
                values = new byte[]{opType};
                break;
            case RemoteControlManage.KEY:
                Log.d("OMG","=remoteControlManage=KEY=");
                values = new byte[1 + unlockPwdBytes.length + uniqueidBytes.length + 1];
                values[0] = opType;
                System.arraycopy(unlockPwdBytes, 0, values, 1, unlockPwdBytes.length);
                System.arraycopy(uniqueidBytes, 0, values, 1 + unlockPwdBytes.length, uniqueidBytes.length);
                values[1 + unlockPwdBytes.length + uniqueidBytes.length] = opValue;
                break;
                default:
                    break;
        }
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand(), APICommand.OP_REMOTE_CONTROL_DEVICE_MANAGEMENT);
    }

    public static void configureNBServerAddress(Command command, short port, String address, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_CONFIGURE_NB_ADDRESS);
        byte[] addByte = address.getBytes();
        byte[] values = new byte[2 + addByte.length];
        LogUtil.d("port:" + port, DBG);
        values[0] = (byte) (port >> 1);
        values[1] = (byte) port;
        System.arraycopy(addByte, 0, values, 2, address.length());
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void configureHotelData(Command command, int op, int parType, HotelData hotelData, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_CONFIGURE_HOTEL_DATA);
        byte[] values = null;
        switch (op) {
            case HotelData.GET:
                values = new byte[]{HotelData.GET, (byte) parType};
                break;
            case HotelData.SET:
                switch (parType) {
                    case HotelData.TYPE_IC_KEY:
                        values = new byte[1 + 1 + 1 + 6];
                        values[0] = values[0] = HotelData.SET;
                        values[1] = (byte) parType;
                        values[2] = 6;
                        byte[] icKey = hotelData.getICKeyByteArray();
                        System.arraycopy(icKey, 0, values, 3, icKey.length);
                        break;
                    case HotelData.TYPE_AES_KEY:
                        values = new byte[1 + 1 + 1 + 16];
                        values[0] = values[0] = HotelData.SET;
                        values[1] = (byte) parType;
                        values[2] = 16;
                        byte[] icKeyAes = hotelData.getAesKeyByteArray();
                        System.arraycopy(icKeyAes, 0, values, 3, icKeyAes.length);
                        break;
                    case HotelData.TYPE_HOTEL_BUILDING_FLOOR:
                        values = new byte[1 + 1 + 1 + 3 + 1 + 1];
                        values[0] = values[0] = HotelData.SET;
                        values[1] = (byte) parType;
                        values[2] = 3 + 1 + 1;
                        values[3] = (byte) (hotelData.hotelNumber >> 16);
                        values[4] = (byte) (hotelData.hotelNumber >> 8);
                        values[5] = (byte) hotelData.hotelNumber;
                        values[6] = (byte) hotelData.buildingNumber;
                        values[7] = (byte) hotelData.floorNumber;
                        break;
                    case HotelData.TYPE_SECTOR:
                        values = new byte[1 + 1 + 1 + 2];
                        values[0] = HotelData.SET;
                        values[1] = (byte) parType;
                        values[2] = 2;
                        values[3] = (byte) (hotelData.sector >> 8);
                        values[4] = (byte) hotelData.sector;
                        break;
                    case HotelData.TYPE_ELEVATOR_CONTROLABLE_FLOORS:
                        values = new byte[1 + 1 + 1 + hotelData.controlableFloors.length];
                        values[0] = HotelData.SET;
                        values[1] = (byte) parType;
                        values[2] = (byte) hotelData.controlableFloors.length;
                        System.arraycopy(hotelData.controlableFloors, 0, values, 3, values[2]);
                        break;
                    case HotelData.TYPE_ELEVATOR_WORK_MODE:
                        values = new byte[1 + 1 + 1 + 9];
                        values[0] = HotelData.SET;
                        values[1] = (byte) parType;
                        values[2] = 9;
                        values[3] = (byte) hotelData.ttLiftWorkMode.getValue();
                        for (int i=4;i<values.length;i++) {//0表示控制 1表示不控制
                            values[i] = 0;
                        }
                        break;
                    case HotelData.TYPE_POWER_SAVER_WORK_MODE:
                        values = new byte[1 + 1 + 1 + 1];
                        values[0] = HotelData.SET;
                        values[1] = (byte) parType;
                        values[2] = 1;
                        values[3] = hotelData.powerWorkModeValue;
                        break;
                    case HotelData.TYPE_POWER_SAVER_CONTROLABLE_LOCK:
                        values = new byte[1 + 1 + 1 + 6];
                        values[0] = HotelData.SET;
                        values[1] = (byte) parType;
                        values[2] = 6;
                        byte[] macBytes = DigitUtil.macDividerByColonToByteArray(hotelData.getControlableLockMac());
                        System.arraycopy(macBytes, 0, values, 3, 6);
                        break;
                }
                break;
        }
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void configureNBServerAddress(TransferData transferData) {
        Command command = new Command(transferData.getLockVersion());
        command.setCommand(Command.COMM_CONFIGURE_NB_ADDRESS);
        short port = (short) transferData.getPort();
        LogUtil.d("port:" + port, DBG);
        String address = transferData.getAddress();
        byte[] addByte = address.getBytes();
        byte[] values = new byte[2 + addByte.length];
        values[0] = (byte) (port >> 8);
        values[1] = (byte) port;
        System.arraycopy(addByte, 0, values, 2, addByte.length);
        command.setData(values, transferData.getAesKeyArray());
        transferData.setTransferData(command.buildCommand());
        BluetoothImpl.getInstance().sendCommand(transferData);
    }

    public static void getAdminCode(Command command) {
        command.setCommand(Command.COMM_GET_ADMIN_CODE);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void writeFR(Command command, byte[] frData, short seq, int packetLen, byte[] aeskeyArray) {
        command.setCommand(Command.COMM_FR_MANAGE);
        int srcPos = seq * packetLen;
        if (frData.length - srcPos < packetLen)
            packetLen = frData.length - srcPos;
        byte[] values = new byte[1 + 2 + packetLen];
        values[0] = ICOperate.WRITE_FR;
        values[1] = (byte) (seq >> 8);
        values[2] = (byte) seq;

        LogUtil.d("packetLen:" + packetLen);
        System.arraycopy(frData, srcPos, values, 3, packetLen);

        command.setData(values, aeskeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void addFRTemp(Command command, TransferData transferData) {
        command.setCommand(Command.COMM_FR_MANAGE);
        byte[] frTempNo = DigitUtil.integerToByteArray((int) transferData.getNo());
        byte[] frData = transferData.getTransferData();
        int num = frData.length;
        byte[] numByte = DigitUtil.shortToByteArray((short) num);
        byte[] checkSum = DigitUtil.checkSum(frData);

        long startDate = transferData.getStartDate();
        long endDate = transferData.getEndDate();

        LogUtil.d("startDate:" + startDate, DBG);
        LogUtil.d("endDate:" + endDate, DBG);
        LogUtil.d("num:" + num, DBG);

        //根据时间偏移量重新计算时间
        startDate = startDate + transferData.getTimezoneOffSet() - TimeZone.getDefault().getOffset(System.currentTimeMillis());
        endDate = endDate + transferData.getTimezoneOffSet() - TimeZone.getDefault().getOffset(System.currentTimeMillis());

        String sDateStr = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmm");
        String eDateStr = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmm");

        byte[] time = DigitUtil.convertTimeToByteArray(sDateStr + eDateStr);

        byte[] values = new byte[1 + frTempNo.length + 2 + checkSum.length + time.length];
        values[0] = ICOperate.ADD;
        System.arraycopy(frTempNo, 0, values, 1, frTempNo.length);
        System.arraycopy(numByte, 0, values, 5, numByte.length);
        System.arraycopy(checkSum, 0, values, 7, checkSum.length);
        System.arraycopy(time, 0, values, 9, time.length);

        command.setData(values, transferData.getAesKeyArray());
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void queryPassageMode(Command command, byte seq, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_CONFIGURE_PASSAGE_MODE);
        byte[] values = new byte[]{PassageModeOperate.QUERY, seq};
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void configurePassageMode(Command command, TransferData transferData, byte weekOrDay) {
        command.setCommand(Command.COMM_CONFIGURE_PASSAGE_MODE);
        byte[] values = new byte[1 + 1 + 1 + 1 + 2 + 2];
        values[0] = PassageModeOperate.ADD;
        values[1] = (byte) transferData.getOp();
        values[2] = weekOrDay;
        values[3] = (byte) transferData.getOpValue();

        int startDate = (int) transferData.getStartDate();
        int endDate = (int) transferData.getEndDate();

        values[4] = (byte) (startDate/60);
        values[5] = (byte) (startDate%60);
        values[6] = (byte) (endDate/60);
        values[7] = (byte) (endDate%60);

        LogUtil.d("s h:" + values[4]);
        LogUtil.d("s m:" + values[5]);

        LogUtil.d("e h:" + values[6]);
        LogUtil.d("e m:" + values[7]);

        command.setData(values, transferData.getAesKeyArray());
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void deletePassageMode(Command command, TransferData transferData, byte weekOrDay) {
        command.setCommand(Command.COMM_CONFIGURE_PASSAGE_MODE);
        byte[] values = new byte[1 + 1 + 1 + 1 + 2 + 2];
        values[0] = PassageModeOperate.DELETE;
        values[1] = (byte) transferData.getOp();
        values[2] = weekOrDay;
        values[3] = (byte) transferData.getOpValue();

        int startDate = (int) transferData.getStartDate();
        int endDate = (int) transferData.getEndDate();

        values[4] = (byte) (startDate/60);
        values[5] = (byte) (startDate%60);
        values[6] = (byte) (endDate/60);
        values[7] = (byte) (endDate%60);

        command.setData(values, transferData.getAesKeyArray());
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void clearPassageMode(Command command, TransferData transferData) {
        command.setCommand(Command.COMM_CONFIGURE_PASSAGE_MODE);
        byte[] values = new byte[]{PassageModeOperate.CLEAR};
        command.setData(values, transferData.getAesKeyArray());
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 开启或者关闭远程开锁功能
     * @param command
     * @param opType 1 - 查询 2 - 修改
     * @param opValue     0 - 解冻 1 - 冻结
     */
    public static void controlFreezeLock(Command command, byte opType, byte opValue, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_FREEZE_LOCK);
        LogUtil.d("opValue:" + opValue);
        byte[] values = null;
        switch (opType) {
            case OperationType.GET_STATE:
                values = new byte[]{opType};
                break;
            case OperationType.MODIFY:
                values = new byte[]{opType, opValue};
                break;
        }
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 开启或者关闭照明灯功能
     * @param command
     * @param opType 1 - 查询 2 - 修改
     * @param opValue     0 - 解冻 1 - 冻结
     */
    public static void controlLamp(Command command, byte opType, short opValue, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_LAMP);
        LogUtil.d("opValue:" + opValue);
        byte[] values = null;
        switch (opType) {
            case OperationType.GET_STATE:
                values = new byte[]{opType};
                break;
            case OperationType.MODIFY:
                values = new byte[3];
                values[0] = opType;
                values[1] = (byte) (opValue >> 8);
                values[2] = (byte) opValue;
                break;
        }
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 重置 防撬警报 隐私锁
     * @param command
     * @param switchItem
     * @param switchValue
     * @param aeskey
     */
    public static void setSwitchState(Command command, int switchItem, int switchValue, byte[] aeskey) {
        command.setCommand(Command.COMM_SWITCH);
        byte[] values = new byte[1 + 4 + 4];
        values[0] = OperationType.MODIFY;
        System.arraycopy(DigitUtil.integerToByteArray(switchItem), 0, values, 1, 4);
        System.arraycopy(DigitUtil.integerToByteArray(switchValue), 0, values, 5, 4);
        command.setData(values, aeskey);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 重置 防撬警报 隐私锁
     * @param command
     * @param aeskey
     */
    public static void getSwitchState(Command command, byte[] aeskey) {
        command.setCommand(Command.COMM_SWITCH);
        byte[] values = new byte[]{OperationType.GET_STATE};
        command.setData(values, aeskey);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void deadLock(Command command, String unlockKey, byte[] psFromLock, byte[] aesKeyArray, long unlockDate) {
        command.setCommand(Command.COMM_DEAD_LOCK);
        long psFromLockL = 0;
        long unlockKeyL = 0;
        String sum = null;
        psFromLockL = DigitUtil.fourBytesToLong(psFromLock);
        unlockKeyL = Long.valueOf(unlockKey);
        byte[] sumByteArray = DigitUtil.getUnlockPwdBytes_new(psFromLockL, unlockKeyL);
        byte[] values = new byte[8];
        System.arraycopy(sumByteArray, 0, values, 0, sumByteArray.length);
        //时间唯一标识
        int date = (int) (unlockDate / 1000);
        System.arraycopy(DigitUtil.integerToByteArray(date), 0, values, 4, 4);
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void addIcCyclicDate(Command command, long user, byte loopType, CyclicConfig cyclicConfig, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_CYCLIC_CMD);
//        ValidityInfo loopDate = transferData.getValidityInfo();
        byte userLen = 4;
        if (user > 0 && user <= 0xffffffffl)
            userLen = 4;
        else userLen = 8;
        LogUtil.d("userLen:" + userLen);
        byte[] userBytes = DigitUtil.longToByteArrayWithLen(user, userLen);
        byte[] values = new byte[1 + 1 + 1 + userLen + 1 + 1 + 1 + 8];//TODO:
        int index = 0;

        values[index++] = CyclicOpType.ADD;
        values[index++] = CyclicOpType.USER_TYPE_IC;
        values[index++] = userLen;
        System.arraycopy(userBytes, 0, values, 3, userLen);
        index += userLen;
        values[index++] = loopType;
        values[index++] = (byte) cyclicConfig.weekDay;
        values[index++] = 0;//月日的时候用
        values[index++] = (byte) (cyclicConfig.startTime / 60);
        values[index++] = (byte) (cyclicConfig.startTime % 60);
        values[index++] = (byte) (cyclicConfig.endTime / 60);
        values[index++] = (byte) (cyclicConfig.endTime % 60);
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void addFrCyclicDate(Command command, long user, byte loopType, CyclicConfig cyclicConfig, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_CYCLIC_CMD);
        byte userLen = 6;//指纹目前固定6个字节
        LogUtil.d("user:" + user);
        LogUtil.d("userLen:" + userLen);
        byte[] userBytes = DigitUtil.longToByteArrayWithLen(user, userLen);
        byte[] values = new byte[1 + 1 + 1 + userLen + 1 + 1 + 1 + 8];
        int index = 0;
        values[index++] = CyclicOpType.ADD;
        values[index++] = CyclicOpType.USER_TYPE_FR;
        values[index++] = userLen;
        System.arraycopy(userBytes, 0, values, 3, userLen);
        index += userLen;
        values[index++] = loopType;
        values[index++] = (byte) cyclicConfig.weekDay;
        values[index++] = 0;//月日的时候用
        values[index++] = (byte) (cyclicConfig.startTime / 60);
        values[index++] = (byte) (cyclicConfig.startTime % 60);
        values[index++] = (byte) (cyclicConfig.endTime / 60);
        values[index++] = (byte) (cyclicConfig.endTime % 60);
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void addKeyFobCyclicDate(Command command, String keyFobMac, byte loopType, CyclicConfig cyclicConfig, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_CYCLIC_CMD);
        byte userLen = 6;//mac地址目前固定6个字节
        byte[] userBytes = DigitUtil.getReverseMacArray(keyFobMac);//低在前高在后的顺序
        byte[] values = new byte[1 + 1 + 1 + userLen + 1 + 1 + 1 + 8];
        int index = 0;
        values[index++] = CyclicOpType.ADD;
        values[index++] = CyclicOpType.USER_TYPE_KEY_FOB;
        values[index++] = userLen;
        System.arraycopy(userBytes, 0, values, 3, userLen);
        index += userLen;
        values[index++] = loopType;
        values[index++] = (byte) cyclicConfig.weekDay;
        values[index++] = 0;//月日的时候用
        values[index++] = (byte) (cyclicConfig.startTime / 60);
        values[index++] = (byte) (cyclicConfig.startTime % 60);
        values[index++] = (byte) (cyclicConfig.endTime / 60);
        values[index++] = (byte) (cyclicConfig.endTime % 60);
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 用户ID长度为0时，也就是没有提供用户ID时，清空所有相关用户
     * @param command
     * @param user
     * @param loopType
     * @param aesKeyArray
     */
    public static void clearICCyclicPeriod(Command command, long user, byte loopType, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_CYCLIC_CMD);
        byte userLen = 4;
        if (user > 0 && user <= 0xffffffffl)
            userLen = 4;
        else userLen = 8;
        byte[] userBytes = DigitUtil.longToByteArrayWithLen(user, userLen);
        byte[] values = new byte[1 + 1 + 1 + userLen];
        int index = 0;
        values[index++] = CyclicOpType.CLEAR;
        values[index++] = CyclicOpType.USER_TYPE_IC;
        values[index++] = userLen;
        System.arraycopy(userBytes, 0, values, 3, userLen);
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void clearFrCyclicPeriod(Command command, long user, byte loopType, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_CYCLIC_CMD);
        byte userLen = 6;
        byte[] userBytes = DigitUtil.longToByteArrayWithLen(user, userLen);
        byte[] values = new byte[1 + 1 + 1 + userLen];
        int index = 0;
        values[index++] = CyclicOpType.CLEAR;
        values[index++] = CyclicOpType.USER_TYPE_FR;
        values[index++] = userLen;
        System.arraycopy(userBytes, 0, values, 3, userLen);
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void clearKeyfobCyclicPeriod(Command command, String keyfobMac, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_CYCLIC_CMD);
        byte[] keyFobBytes = DigitUtil.getReverseMacArray(keyfobMac);
        byte userLen = (byte) keyFobBytes.length;
        byte[] values = new byte[1 + 1 + 1 + userLen];
        int index = 0;
        values[index++] = CyclicOpType.CLEAR;
        values[index++] = CyclicOpType.USER_TYPE_KEY_FOB;
        values[index++] = userLen;
        System.arraycopy(keyFobBytes, 0, values, 3, userLen);
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void getNBActivateConfig(Command command, byte opType, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_NB_ACTIVATE_CONFIGURATION);
        byte[] values = new byte[]{ActionType.GET, opType};
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }
    /**
     * nb 激活配置
     * @param command
     * @param opType
     * @param nbAwakeConfig
     * @param aesKeyArray
     */
    public static void setNBActivateConfig(Command command, byte opType, NBAwakeConfig nbAwakeConfig, byte[] aesKeyArray) {
        command.setCommand(Command.COMM_NB_ACTIVATE_CONFIGURATION);
        byte[] values = null;
        switch (opType) {
            case NBAwakeConfig.ACTION_AWAKE_MODE:
                values = new byte[3];
                values[0] = ActionType.SET;
                values[1] = opType;
                byte activateMode = 0;
                if (nbAwakeConfig.getNbAwakeModeList() != null) {
                    for (NBAwakeMode nbAwakeMode : nbAwakeConfig.getNbAwakeModeList()) {
                        activateMode |= nbAwakeMode.getValue();
                    }
                }
                values[2] = activateMode;
                break;
            case NBAwakeConfig.ACTION_AWAKE_TIME:
                int configLen = nbAwakeConfig.getNbAwakeTimeList().size();
                values = new byte[1 + 1 + 1 + 3 * configLen];
                values[0] = ActionType.SET;
                values[1] = opType;
                values[2] = (byte) configLen;
                int index = 3;
                for (NBAwakeTime nbAwakeTime : nbAwakeConfig.getNbAwakeTimeList()) {
                    values[index++] = nbAwakeTime.getNbAwakeTimeType().getValue();
                    values[index++] = (byte) (nbAwakeTime.getMinutes() / 60);
                    values[index++] = (byte) (nbAwakeTime.getMinutes() % 60);
                }
                break;
        }
        command.setData(values, aesKeyArray);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void addKeyFob(Command command, TransferData transferData, byte[] aeskey) {
        command.setCommand(Command.COMM_KEY_FOB_MANAG);
        keyFobManage(command, KeyFobOperationType.ADD_MODIFY, transferData, aeskey);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void modifyKeyFob(Command command, TransferData transferData, byte[] aeskey) {
        command.setCommand(Command.COMM_KEY_FOB_MANAG);
        keyFobManage(command, KeyFobOperationType.ADD_MODIFY, transferData, aeskey);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void deleteKeyFob(Command command, TransferData transferData, byte[] aeskey) {
        command.setCommand(Command.COMM_KEY_FOB_MANAG);
        keyFobManage(command, KeyFobOperationType.DELETE, transferData, aeskey);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void clearKeyFob(Command command, byte[] aeskey) {
        command.setCommand(Command.COMM_KEY_FOB_MANAG);
        keyFobManage(command, KeyFobOperationType.CLEAR, null, aeskey);
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void keyFobManage(Command command, byte opType, TransferData transferData, byte[] aeskey) {
        byte[] values = null;
        switch (opType) {
            case KeyFobOperationType.GET:

                break;
            case KeyFobOperationType.ADD_MODIFY:
                values = new byte[1 + 6 + 5 + 5];
                values[0] = opType;// 操作类型（1 byte）
                byte[] keyFobBytes = DigitUtil.getReverseMacArray(transferData.getKeyFobMac()); // 无线钥匙MAC（6 bytes）低在前，高在后
                System.arraycopy(keyFobBytes, 0, values, 1, 6);
                ValidityInfo validityInfo = transferData.getValidityInfo();
                long startDate = validityInfo.getStartDate();
                long endDate = validityInfo.getEndDate();
                if(startDate == 0 || endDate == 0) {//永久时间
                    startDate = CommandUtil.permanentStartDate;
                    endDate = CommandUtil.permanentEndDate;
                }

                byte[] startDateBytes = DigitUtil.convertTimestamp2LockZoneBytes_yyMMddHHmm(startDate, transferData.getTimezoneOffSet());
                System.arraycopy(startDateBytes, 0, values, 7, 5);// 起始时间（5 bytes）
                byte[] endDateBytes = DigitUtil.convertTimestamp2LockZoneBytes_yyMMddHHmm(endDate, transferData.getTimezoneOffSet());
                System.arraycopy(endDateBytes, 0, values, 12, 5);// 结束时间（5 bytes）
                break;
            case KeyFobOperationType.DELETE:
                values = new byte[1 + 6];
                values[0] = opType;// 操作类型（1 byte）
                keyFobBytes = DigitUtil.getReverseMacArray(transferData.getKeyFobMac()); // 无线钥匙MAC（6 bytes）低在前，高在后
                System.arraycopy(keyFobBytes, 0, values, 1, 6);
                break;
            case KeyFobOperationType.CLEAR:
                values = new byte[1];
                values[0] = opType;// 操作类型（1 byte）
                break;
        }
        command.setData(values, aeskey);
    }

    public static void setSensitivity(Command command, TransferData transferData) {
        command.setCommand(Command.COMM_SENSITIVITY_MANAGE);
        byte[] values = {SensitivityOperationType.MODIFY, (byte) transferData.getSensitivity()};
        command.setData(values, transferData.getAesKeyArray());
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void getSensitivity(Command command, byte[] aesKey) {//初始化过程中使用TransferData.getAesKeyArray() 拿到的是默认的不是初始化设置的key
        command.setCommand(Command.COMM_SENSITIVITY_MANAGE);
        byte[] values = {SensitivityOperationType.QUERY};
        command.setData(values, aesKey);
        LogUtil.d("values:" + DigitUtil.byteArrayToHexString(values));
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    public static void addDoorSensor(Command command, TransferData transferData) {
        command.setCommand(Command.COMM_DOOR_SENSOR_MANAGE);
        byte[] values = DigitUtil.getReverseMacArray(transferData.getDoorSensorMac()); // 无线门磁MAC（6 bytes）低在前，高在后
        command.setData(values, transferData.getAesKeyArray());
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 删除门磁
     * 地址为全0时，表示删除无线门磁信息
     * @param command
     * @param transferData
     */
    public static void deleteDoorSensor(Command command, TransferData transferData) {
        command.setCommand(Command.COMM_DOOR_SENSOR_MANAGE);
        byte[] values = {0, 0, 0, 0, 0, 0}; // 无线门磁MAC（6 bytes）低在前，高在后
        command.setData(values, transferData.getAesKeyArray());
        BluetoothImpl.getInstance().sendCommand(command.buildCommand());
    }

}
