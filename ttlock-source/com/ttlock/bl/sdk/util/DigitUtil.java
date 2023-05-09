package com.ttlock.bl.sdk.util;

import android.text.TextUtils;
import android.util.Base64;

import com.scaf.android.client.CodecUtils;
import com.ttlock.bl.sdk.api.ParamInvalidException;
import com.ttlock.bl.sdk.constant.Feature;
import com.ttlock.bl.sdk.entity.HotelInfo;
import com.ttlock.bl.sdk.entity.PowerSaverWorkMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Smartlock on 2016/5/27.
 */
public class DigitUtil {

    private static final boolean DBG = false;

    public static byte generateRandomByte() {
        byte randomByte = 0;
        do {
            randomByte = (byte) (Math.random() * 128);
        } while (randomByte == 0);
        return randomByte;
    }

    /**
     * generate dynamic password, the first position is zero
     *
     * @param length
     * @return
     */
    public static byte[] generateDynamicPassword(int length) {
        byte[] bytes = new byte[length];
        bytes[0] = 48;
        for (int i = 1; i < length; i++) {
            double r = Math.random() * 10;
            if (r >= 10) {
                r = 9;
            }
            bytes[i] = (byte) (r + 48);
        }
        return bytes;
    }


    /**
     * @param macBytes
     * @return
     */
    public static String getMacString(byte[] macBytes) {
        String value = "";
        for (int i = macBytes.length - 1; i >= 0; i--) {    //小端在前
            String sTemp = Integer.toHexString(0xFF & macBytes[i]);
            if (sTemp.length() == 1)
                sTemp = "0" + sTemp;
            value = value + sTemp + ":";
        }
        value = value.substring(0, value.lastIndexOf(":"));
        return value.toUpperCase();
    }

    public static byte[] shortToByteArray(short value) {
        byte[] shortByteArray = new byte[2];
        for(int i = 1; i >= 0; i --) {
            shortByteArray[i] = (byte) value;
            value >>= 8;
        }
        return shortByteArray;

    }

    public static byte[] integerToByteArray(int value) {
        byte[] bytes = new byte[4];
        byte[] offset = new byte[]{24, 16, 8, 0};
        for(int i=0;i<4;i++) {
            bytes[i] = (byte) (value >> offset[i]);
        }
        return bytes;
    }

    public static byte[] reverseArray(byte[] array) {
        if (array != null) {
            int len = array.length;
            for (int i=0;i<len/2;i++) {
                byte temp = array[i];
                array[i] = array[len - i -1];
                array[len - i - 1] = temp;
            }
        }
        return array;
    }

    public static String[] reverseArray(String[] array) {
        if (array != null) {
            int len = array.length;
            for (int i=0;i<len/2;i++) {
                String temp = array[i];
                array[i] = array[len - i -1];
                array[len - i - 1] = temp;
            }
        }
        return array;
    }

    public static String appendWithColon(String[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        if (array != null && array.length > 0) {
            stringBuilder.append(array[0]);
            for (int i=1;i<array.length;i++) {
                stringBuilder.append(":");
                stringBuilder.append(array[i]);
            }
        }
        return stringBuilder.toString();
    }

    public static byte[] longToByteArrayWithLen(long value, int len) {
        byte[] bytes = new byte[len];
        for(int i=len - 1;i>=0;i--) {
            bytes[i] = (byte) value;
            value>>=8;
        }
        return bytes;
    }

    public static short byteArrayToShort(byte[] array) {
        short value = (short) ((array[1] & 0xff) | (array[0] << 8));
        return value;
    }

    public static String byteToHex(byte value) {
        String hex = Integer.toHexString(value & 0xFF);
        if(hex.length() == 1)
            hex = "0" + hex;
        return hex;
    }

    public static long fourBytesToLong(byte[] data) {
        long res = 0;
        res |= ((data[0] << 24L) & 0xFF000000L);
        res |= ((data[1] << 16L) & 0x00FF0000);
        res |= ((data[2] << 8L) & 0x0000FF00);
        res |= (data[3] & 0xFF);
        LogUtil.d("res:" + res, DBG);
        return res;
    }

    public static long bytesToLong(byte[] data) {
        long res = 0;
        int len = data.length;
        int tmpLen = len;
        long[] mask = {0xFFL, 0xFF00L, 0xFF0000L, 0xFF000000L, 0xFF00000000L, 0xFF0000000000L, 0xFF000000000000L, 0xFF00000000000000L};
        while (tmpLen-- > 0) {
            res |= (long)data[len - tmpLen - 1] << (tmpLen * 8L) & mask[tmpLen];
        }
        return res;
    }

    //TODO:
    public static long sixBytesToLong(byte[] data) {
        long res = 0l;
        res |= (((long)data[0] << 40L) & 0xFF0000000000L);
        res |= (((long)data[1] << 32L) & 0xFF00000000L);
        res |= (((long)data[2] << 24L) & 0x0000FF000000L);
        res |= (((long)data[3] << 16L) & 0x000000FF0000L);
        res |= (((long)data[4] << 8L) & 0x00000000FF00L);
        res |= ((long)data[5] & 0xFFL);
        LogUtil.d("res:" + res, DBG);
        return res;
    }

    /**
     * convert byte array to 16 hexadecimal string
     */
    public static String byteArrayToHexString(byte[] array) {
        if(array == null)
            return null;
//        LogUtil.
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        if(array == null || array.length == 0) {
            stringBuilder.append(']');
        }
        for(int i=0;i<array.length;i++) {
            stringBuilder.append(byteToHex(array[i]));
            stringBuilder.append(',');
        }
        stringBuilder.replace(stringBuilder.length() -1, stringBuilder.length(), "]");
        return stringBuilder.toString();
    }

    public static byte[] hexString2ByteArray(String hexStr) {
        int len = hexStr.length();
        byte[] array = new byte[len/2];
        for (int i=0;i<len;i+=2) {
            array[i/2] = Integer.valueOf(hexStr.substring(i, i+2), 16).byteValue();
        }
        return array;
    }

    /**
     * 计算16位校验和
     * @param bytes
     * @return
     */
    public static byte[] checkSum(byte[] bytes){
        int length = bytes.length;
        short sum = 0;

        for(int i = 0; i < length; i++){
            sum += (bytes[i] & 0xff);
        }
        LogUtil.d("sum:" + sum);
        return shortToByteArray(sum);
    }

    public static byte[] macDividerByColonToByteArray(String mac) {
        if (TextUtils.isEmpty(mac))
            return null;
        String[] macArray = mac.split(":");
        byte[] macByte = new byte[macArray.length];
        for(int i=0;i<macByte.length;i++) {
            macByte[i] = Integer.valueOf(macArray[i], 16).byteValue();
        }
        return macByte;
    }

    public static byte[] hexStringArrToByteArr(String[] arr) {
        if (arr == null)
            return null;
        byte[] bytes = new byte[arr.length];
        for(int i=0;i<arr.length;i++) {
            bytes[i] = Integer.valueOf(arr[i], 16).byteValue();
        }
        return bytes;
    }

    public static byte[] stringDividerByDotToByteArray(String source) {
        byte[] originalBytes = source.getBytes();
        String originalString = new String(Base64.decode(originalBytes, Base64.DEFAULT));
        LogUtil.d("originalString:" + originalString, DBG);
        String[] strings = originalString.split(",");
        int len = strings.length;
        byte[] resBytes = new byte[len];
        try {
            for(int i=0;i<len;i++) {
                resBytes[i] = Integer.valueOf(strings[i]).byteValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resBytes;
    }

    public static byte[] getControlableFloors(String floorStr) {
        byte[] floors = new byte[0];
        try {
            String[] strings = floorStr.split(",");
            floors = new byte[strings.length];
            for (int i=0;i<strings.length;i++) {
                floors[i] = Byte.valueOf(strings[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return floors;
    }

    /**
     *
     * @param oriWeekDays ori : 1-sunday, 2-monday, ……, 7-saturday
     * @return newWeekDays: 1-monday, ……, 7-sunday.
     */
    public static String convertWeekDays(String oriWeekDays) {
        if (!TextUtils.isEmpty(oriWeekDays)) {
            try {
                JSONArray jsonArray = new JSONArray(oriWeekDays);
                JSONArray newJsonArray = new JSONArray();
                for (int i=0;i<jsonArray.length();i++) {
                    int week = (int) jsonArray.get(i);
                    if (week == 1)
                        week = 7;
                    else week = week - 1;
                    newJsonArray.put(week);
                }
                return newJsonArray.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * format the date
     * @param time
     * @param formate
     * @return
     */
    public static String formateDateFromLong(long time, String formate){
        //yyyy-MM-dd HH:mm:ss
        Date date=new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat(formate);
        return formatter.format(date);
    }

    /**
     * yyMMddHHmmss format
     * @param time
     * @return
     */
    public static byte[] convertTimeToByteArray(String time) {
        int len = time.length();
        len/=2;
        byte[] values = new byte[len];
        for(int i = 0;i < len; i++) {
            String string = time.substring(i * 2, (i * 2) + 2);
            values[i] = Byte.valueOf(string);
        }
        return values;
    }

    /**
     * encrypt
     * @param sourceBytes
     * @return
     */
    public static byte[] encodeDefaultPassword(byte[] sourceBytes){
        if(sourceBytes == null || sourceBytes.length == 0) {
//            LogUtil.d("sourceBytes=" + sourceBytes, DBG);
            return sourceBytes;
        }
        byte[] bytes = CodecUtils.encode(sourceBytes);
        return bytes;
    }

    /**
     * decrypt
     * @param sourceBytes
     * @return
     */
    public static byte[] decodeDefaultPassword(byte[] sourceBytes){
        if(sourceBytes == null || sourceBytes.length == 0) {
//            LogUtil.d("sourceBytes=" + sourceBytes, DBG);
            return sourceBytes;
        }
//        LogUtil.d("sourceBytes=" + DigitUtil.byteArrayToHexString(sourceBytes), DBG);
        byte[] bytes = CodecUtils.decode(sourceBytes);
        return bytes;
    }

    /**
     * 开锁的密码 老版本使用
     * @param passwordFromLock
     * @param localPassword
     * @return
     */
    public static String getUnlockPassword(long passwordFromLock, long localPassword){
        long result = (passwordFromLock + localPassword) % 2000000000l;
        return String.valueOf(result);
    }

    /**
     * 2S及2S升级 V3 版本不取余
     * @param passwordFromLock
     * @param localPassword
     * @return
     */
    public static String getUnlockPwd_new(long passwordFromLock, long localPassword){
        int result = (int) (passwordFromLock + localPassword);
        return String.valueOf(result);
    }

    public static byte[] getUnlockPwdBytes_new(long passwordFromLock, long localPassword) {
        int result = (int) (passwordFromLock + localPassword);
        return DigitUtil.integerToByteArray(result);
    }

    /**
     * 获取10位校验表 不重复
     * 0123456789
     */
    public static String getCheckTable() {
        Set set = new LinkedHashSet();
        while (set.size() < 10) {
            set.add(getRandomIntegerByUpperBound(10));
        }
        StringBuilder stringBuilder = new StringBuilder();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next());
        }
        return stringBuilder.toString();
    }

    public static int getRandomIntegerByUpperBound(int upperBound) {
        Random random = new Random();
        return random.nextInt(upperBound);
    }

    public static byte getPowerWorkModeValue(List<PowerSaverWorkMode> powerSaverWorkModeList) {
        if (powerSaverWorkModeList == null || powerSaverWorkModeList.size() == 0) {
            return 0;
        }
        int powerWorkModeValue = 0;
        for (PowerSaverWorkMode powerSaverWorkMode : powerSaverWorkModeList) {
            powerWorkModeValue |= powerSaverWorkMode.getValue();
        }
        return (byte) powerWorkModeValue;
    }

    /**
     * 扇区从0开始计算 这里算的是保留扇区跟锁内相反
     * @param sectors
     * @return
     */
    public static short calSectorValue(ArrayList<Integer> sectors) {
        if (sectors == null)
            return 0;
        short sectorValue = 0;
        for (int i=0;i<sectors.size();i++) {
            sectorValue |= (short)(((short)1)<<(15 - sectors.get(i)));
        }
        if (sectorValue == -1) {//全部设置
            return 0;
        }
        return sectorValue;
    }

    /**
     * 扇区从1开始计算 计算的是写入扇区跟锁内一致
     * @param sectorStr
     * @return
     */
    public static short calSectorValue(String sectorStr) {
        if (sectorStr == null) {
            return 0;
        }
        short sectorValue = 0;
        try {
            String[] sectors = sectorStr.split(",");
            for (int i=0;i<sectors.length;i++) {
                sectorValue |= (short)(((short)1)<<(16 - Integer.valueOf(sectors[i])));
            }
            //todo:
            if (sectorValue == -1) {//全部设置
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sectorValue;
    }

    /**
     * 三代锁获取密码约定数和映射数6 bytes
     * @param code 1.5bytes
     * @param secretKey 4.5bytes
     * @return
     */
    public static byte[] convertCodeAndKeyToByteArray(int code, String secretKey) {
        LogUtil.d("code:" + code, DBG);
        LogUtil.d("secretKey:" + secretKey, DBG);
        byte[] res = new byte[6];
        res[0] = (byte) (code >> 4);
        res[1] = (byte) (code << 4);
        long secretKeyLong = Long.valueOf(secretKey);
        int offset = 32;
        res[1] = (byte) (res[1] | (secretKeyLong >> offset));//增加增加映射数的半个字节
        for(int i=2;i<6;i++) {
            offset -= 8;
            res[i] = (byte) (secretKeyLong >> offset);
        }
        return res;
    }

    /**
     * 生成固定长度密码
     * @param length
     * @return
     */
    public static String generatePwdByLength(int length	) {
        char[] pwd = new char[length];
        Random random = new Random();
        for(int i=0;i<length;i++) {
            pwd[i] =(char) ('0' + random.nextInt(10));
        }
        return new String(pwd);
    }

    public static int generateRandomIntegerByUpperBound(int upperBound) {
        Random random = new Random();
        return random.nextInt(upperBound);
    }

    /**
     * 900个密码根据密码类型生成
     * @param pwdType
     * @return
     */
    public static String generatePwdByType(int pwdType) {
        char[] pwd = new char[6];
        pwd[0] = (char) ('0' + pwdType);
        Random random = new Random();
        for(int i=1;i<6;i++) {
            pwd[i] = (char) ('0' + random.nextInt(9));
        }
        return new String(pwd);
    }

    /**
     * ase解密转换工具
     */
    public static byte[] convertAesKeyStrToBytes(String aesKeyStr) {
        if (TextUtils.isEmpty(aesKeyStr))
            return null;
        String[] aesKeyStrings = aesKeyStr.split(",");
        int len = aesKeyStrings.length;
        byte[] aesKey = new byte[len];
        for(int i=0;i<len;i++) {
            aesKey[i] =  Integer.valueOf(aesKeyStrings[i], 16).byteValue();
        }
        return aesKey;
    }

    /**
     * 判断是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 加密aeskey
     * @param aeskey
     * @return
     */
    public static String encodeAesKey(byte[] aeskey) {
        if(aeskey != null && aeskey.length > 0) {
            String aeskeyStr = byteArrayToHexString(aeskey);
            aeskeyStr = aeskeyStr.substring(1, aeskeyStr.length() - 1);
            return aeskeyStr;
        }
        return null;
    }

    /**
     * 加密锁中adminPs、unlockKey等数据
     * @param originalStr   原字符串
     * @return
     */
    public static String encodeLockData(String originalStr) {
        byte[] encodedBytes = encodeDefaultPassword(originalStr.getBytes());
        String encodedString = byteArrayToStringDividerByDot(encodedBytes);
        return Base64.encodeToString(encodedString.getBytes(), Base64.NO_WRAP);
    }

    /**
     * 解密锁中admiinPs、unlockKey等数据
     * @param originalStr
     * @return
     */
    public static String decodeLockData(String originalStr) {
        String decodeData = "";
        try {
            decodeData = new String(DigitUtil.decodeDefaultPassword(DigitUtil.stringDividerByDotToByteArray(originalStr)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodeData;
    }

    public static HotelInfo decodeHotelInfo(String hotelInfoStr) throws ParamInvalidException {
        HotelInfo hotelInfo = new HotelInfo();
        String data = DigitUtil.decodeLockData(hotelInfoStr);
        if (!TextUtils.isEmpty(data)) {
            String[] array = data.split(",");
            hotelInfo.hotelNumber = Integer.valueOf(array[0]);
            if (array[1] != null && array[2] != null) {
                hotelInfo.icKey = DigitUtil.convertStringDividerByDot(array[1]);
                hotelInfo.aesKey = DigitUtil.convertStringDividerByDot(array[2]);
            } else {
                throw new ParamInvalidException();
            }
        } else {
            throw new ParamInvalidException();
        }
        return hotelInfo;
    }
//    public static String parseLockData(String lockData, String lockmac) {
//        if (TextUtils.isEmpty(lockData) || TextUtils.isEmpty(lockmac)){
//            return null;
//        }
//        byte[] decodeBytes = Base64.decode(lockData, Base64.DEFAULT);
//        decodeBytes = Arrays.copyOf(decodeBytes, decodeBytes.length - 6);
//        String aeskey = lockmac;
//        aeskey = aeskey.substring(0, 9) + aeskey.substring(10);
//        decodeBytes = AESUtil.aesDecrypt(decodeBytes, aeskey.getBytes());
//        lockData = new String(decodeBytes);
//
//        LockDataCopy lockDataCopy = GsonUtil.toObject(lockData, LockDataCopy.class);
//        LockData lockDataObj = lockDataCopy.convert2LockData();
//        lockData = GsonUtil.toJson(lockDataObj);
//        if (lockDataObj == null){
//            return null;
//        }
//        return lockData;
//    }

    /**
     * byte转成以逗号分隔的字符串
     * @param array
     * @return
     */
    public static String byteArrayToStringDividerByDot(byte[] array) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(array.length * 4);
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            sb.append(",");
            sb.append(array[i]);
        }
        return sb.toString();
    }

    public static String convertToFeatureValue(byte[] array) {
        if(array == null)
            return null;
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder groupBuilder = new StringBuilder();//4个字节一组
        for(int i=0;i<array.length;i++) {
            groupBuilder.append(byteToHex(array[i]));
            if (i % 4 == 3) {
                stringBuilder.insert(0, groupBuilder);
                groupBuilder.setLength(0);
            }
        }
        int index=0;
        while (index < stringBuilder.length() && stringBuilder.charAt(index) == '0')
            index++;
        if (index == stringBuilder.length())
            return "0";
        return stringBuilder.substring(index).toUpperCase();
    }

    public static String getMD5(String source) {
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte[] e = md.digest();
            for(int offset = 0; offset < e.length; ++offset) {
                int i = e[offset];
                if(i < 0) {
                    i += 256;
                }
                if(i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException var6) {
            var6.printStackTrace();
        }
        return buf.toString();
    }

    public static String generateKeyboardPwd_Json(Queue<String> pwdList) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        StringBuilder one_day_pwd = new StringBuilder(pwdList.poll());
        for(int i=1;i<300;i++) {
            one_day_pwd.append(",");
            one_day_pwd.append(pwdList.poll());
        }
        LogUtil.d("one_day_pwd:" + one_day_pwd, DBG);
        jsonObject.put("oneDay", one_day_pwd);
        jsonObject.put("oneDaySequence", 0);

        StringBuilder two_days_pwd = new StringBuilder(pwdList.poll());
        for(int i=1;i<150;i++) {
            two_days_pwd.append(",");
            two_days_pwd.append(pwdList.poll());
        }
        jsonObject.put("twoDays", two_days_pwd);
        jsonObject.put("twoDaysSequence", 0);
        LogUtil.d("twoDays:" + two_days_pwd, DBG);

        StringBuilder three_days_pwd = new StringBuilder(pwdList.poll());
        for(int i=1;i<100;i++) {
            three_days_pwd.append(",");
            three_days_pwd.append(pwdList.poll());
        }
        jsonObject.put("threeDays", three_days_pwd);
        jsonObject.put("threeDaysSequence", 0);
        LogUtil.d("three_days_pwd:" + three_days_pwd, DBG);

        StringBuilder four_days_pwd = new StringBuilder(pwdList.poll());
        for(int i=1;i<100;i++) {
            four_days_pwd.append(",");
            four_days_pwd.append(pwdList.poll());
        }
        jsonObject.put("fourDays", four_days_pwd);
        jsonObject.put("fourDaysSequence", 0);

        StringBuilder five_days_pwd = new StringBuilder(pwdList.poll());
        for(int i=1;i<50;i++) {
            five_days_pwd.append(",");
            five_days_pwd.append(pwdList.poll());
        }
        jsonObject.put("fiveDays", five_days_pwd);
        jsonObject.put("fiveDaysSequence", 0);

        StringBuilder six_days_pwd = new StringBuilder(pwdList.poll());
        for(int i=1;i<50;i++) {
            six_days_pwd.append(",");
            six_days_pwd.append(pwdList.poll());
        }
        jsonObject.put("sixDays", six_days_pwd);
        jsonObject.put("sixDaysSequence", 0);

        StringBuilder seven_days_pwd = new StringBuilder(pwdList.poll());
        for(int i=1;i<50;i++) {
            seven_days_pwd.append(",");
            seven_days_pwd.append(pwdList.poll());
        }
        jsonObject.put("sevenDays", seven_days_pwd);
        jsonObject.put("sevenDaysSequence", 0);

        StringBuilder ten_minutes_pwd = new StringBuilder(pwdList.poll());
        for(int i=1;i<100;i++) {
            ten_minutes_pwd.append(",");
            ten_minutes_pwd.append(pwdList.poll());
        }
        jsonObject.put("tenMinutes", ten_minutes_pwd);
        jsonObject.put("tenMinutesSequence", 0);
        LogUtil.d("ten_minutes_pwd:" + ten_minutes_pwd, DBG);

        return jsonObject.toString();
    }

    /**
     * Whether the passcode is supported
     * @param specialValue
     * @return
     */
    public static boolean isSupportPasscode(int specialValue) {
        return (specialValue & Feature.PASSCODE) != 0 ;
    }

    /**
     * Whether the CARD card is supported
     * @param specialValue
     * @return
     */
    public static boolean isSupportIC(int specialValue) {
        return (specialValue & Feature.IC) != 0;
    }

    /**
     * Whether the fingerprint is supported
     * @param specialValue
     * @return
     */
    public static boolean isSupportFingerPrint(int specialValue) {
        return (specialValue & Feature.FINGER_PRINT) != 0;
    }

    /**
     * Whether the wristband is supported
     * @param specialValue
     * @return
     */
    public static boolean isSupportWristband(int specialValue) {
        return (specialValue & Feature.WRIST_BAND) != 0 ? true : false;
    }

    /**
     * Whether the function of auto lock is supported
     * 是否支持自动闭锁
     * @param specialValue
     * @return
     */
    public static boolean isSupportAutoLock(int specialValue) {
        return (specialValue & Feature.AUTO_LOCK) != 0;
    }

    /**
     * Whether the modification of passcode is supported
     * @param specialValue
     * @return
     */
    public static boolean isSupportModifyPasscode(int specialValue) {
        return (specialValue & Feature.MODIFY_PASSCODE_FUNCTION) != 0;
    }

    /**
     * Whether the function of locking is supported
     * 判断是否支持闭锁指令
     * @param specialValue
     * @return
     */
    public static boolean isSupportManualLock(int specialValue) {
        return (specialValue & Feature.MANUAL_LOCK) != 0 ? true : false;
    }

    /**
     * Whether the function of displaying or hiddening passcode is supported
     * 判断是否可以显示或隐藏密码
     * @param specialValue
     * @return
     */
    public static boolean isSupportShowPasscode(int specialValue) {
        return (specialValue & Feature.PASSWORD_DISPLAY_OR_HIDE) != 0;
    }

    /**
     * Whether the cyclic passcode is supported
     * @param specialValue
     * @return
     */
    public static boolean isSupportCyclicPasscode(int specialValue) {
        return (specialValue & Feature.CYCLIC_PASSWORD) != 0 ? true : false;
    }

    /**
     * Whether the function of gateway unlocking is supported
     * @param specialValue
     * @return
     */
    public static boolean isSupportRemoteUnlock(int specialValue) {
//        LogUtil.d("specialValue:" + DigitUtil.byteArrayToHexString(DigitUtil.integerToByteArray(specialValue)), DBG);
        return (specialValue & Feature.GATEWAY_UNLOCK) != 0 ;
    }

    /**
     * Whether the door sensor is supported
     * 是否支持门磁
     * @param specialValue
     * @return
     */
    public static boolean isSupportDoorSensor(int specialValue) {
        return (specialValue & Feature.MAGNETOMETER) != 0 ? true : false;
    }

    /**
     * Whether the function of controling remote unlock switch is supported
     * @param specialValue
     * @return
     */
    public static boolean isSupportRemoteUnlockSwitch(int specialValue) {
//        LogUtil.d("specialValue:" + DigitUtil.byteArrayToHexString(DigitUtil.integerToByteArray(specialValue)), DBG);
        return (specialValue & Feature.CONFIG_GATEWAY_UNLOCK) != 0 ;
    }

    /**
     * Whether the audio management is supported
     * @param specialValue
     * @return
     */
    public static boolean isSupportAudioManagement(int specialValue) {
//        LogUtil.d("specialValue:" + specialValue, DBG);
        return (specialValue & Feature.AUDIO_MANAGEMENT) != 0;
    }

    /**
     * Whether the NB Lock is supported
     * @param specialValue
     * @return
     */
    public static boolean isSupportNBLock(int specialValue) {
        return (specialValue & Feature.NB_LOCK) != 0;
    }

    public static String convertStringDividerByDot(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0;i<str.length();i+=2) {
                stringBuilder.append(str.substring(i, i+2));
                stringBuilder.append(',');
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }
    }

    /**
     * 把mac地址转化为长度为6的byte[]
     *
     * @param mac mac地址，形式：B4:99:4C:67:96:79
     * @return byte[]，如 [0xB4, 0x99, 0x4C, 0x67, 0x96, 0x79]
     */
    public static byte[] getByteArrayByMac(final String mac) {
        // 判断是否符合mac地址格式
        if (!isMacFormat(mac)) {
            return new byte[] {};
        }

        byte[] bytes = new byte[6];
        String[] strs = mac.split(":");
        for (int i = 0; i < 6; i++) {
            bytes[i] = (byte) Integer.parseInt(strs[i], 16);
        }
        return bytes;
    }

    /** java 合并两个byte数组 */
    public static byte[] byteMerger(byte[] byte1, byte[] byte2){
        byte[] byte3 = new byte[byte1.length+byte2.length];
        System.arraycopy(byte1, 0, byte3, 0, byte1.length);
        System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);
        return byte3;
    }

    /** 判断字符串是否符合mac地址格式，冒号相连 */
    public static boolean isMacFormat(final String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        final String regex = "[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]";
        return str.matches(regex);
    }

    public static String getMacByByte(byte[] macBytes) {
        String value = "";
        for (int i = 0; i < macBytes.length; i++) {
            String sTemp = Integer.toHexString(0xFF & macBytes[i]);
            if (sTemp.length() == 1)
                sTemp = "0" + sTemp;
            value = value + sTemp + ":";
        }
        value = value.substring(0, value.lastIndexOf(":"));
        return value.toUpperCase();
    }

    public static byte[] getReverseMacArray(String mac) {
        byte[] array = macDividerByColonToByteArray(mac);
        array = reverseArray(array);
        return array;
    }

    /**
     * 根据时间偏移量转换时间磋
     * @param timeBytes
     * @param timezoneOffSet
     * @return
     */
    public static long convertTimestampWithTimezoneOffset(byte[] timeBytes, int timezoneOffSet) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000 + timeBytes[0], timeBytes[1] - 1, timeBytes[2], timeBytes[3], timeBytes[4], timeBytes[5]);
        //根据时间偏移量计算时间
        TimeZone timeZone = TimeZone.getDefault();
        LogUtil.d("timezoneOffSet:" + timezoneOffSet, DBG);
        if (timeZone.inDaylightTime(new Date(System.currentTimeMillis())))
            timezoneOffSet -= timeZone.getDSTSavings();
        timeZone.setRawOffset((int) timezoneOffSet);
        calendar.setTimeZone(timeZone);
        return calendar.getTimeInMillis();
    }

    /**
     * 将时间磋转换成锁所在时区的时间磋
     * @param timestamp
     * @param lockZoneOffSet
     * @return
     */
    public static long convertTimestamp2LockZoneTimestamp(long timestamp, long lockZoneOffSet) {
        return timestamp + lockZoneOffSet - TimeZone.getDefault().getOffset(System.currentTimeMillis());
    }

    public static byte[] convertTimestamp2LockZoneBytes_yyMMddHHmm(long timestamp, long lockZoneOffSet) {
        long lockTimestamp = convertTimestamp2LockZoneTimestamp(timestamp, lockZoneOffSet);
        String lockTimeStr = DigitUtil.formateDateFromLong(lockTimestamp, "yyMMddHHmm");
        return DigitUtil.convertTimeToByteArray(lockTimeStr);
    }

    public static byte[] convertIp2Bytes(String ip) {
        byte[] bytes = new byte[4];
        if (TextUtils.isEmpty(ip)) {
            return bytes;
        }
        try {
            String[] dividerList = ip.split("\\.");
            if (dividerList.length != 4) {
                return null;
            }
            for (int i=0;i<4;i++) {
                int temp = Integer.valueOf(dividerList[i]);
                if (temp < 0 || temp > 255) {
                    return null;
                }
                bytes[i] = (byte) temp;
            }
        } catch (Exception e) {
            return null;
        }
        return bytes;
    }

}
