package com.ttlock.bl.sdk.api

import android.util.Base64
import java.lang.Exception
import java.util.*

/**
 * Created on  2019/4/28 0028 13:32
 *
 * @author theodre
 */
object EncryptionUtil {
    //    public static String parseLockData(String lockData, String lockMac) {
    //        try {
    //            if (TextUtils.isEmpty(lockData) || TextUtils.isEmpty(lockMac)) {
    //                return null;
    //            }
    //            byte[] decodeBytes = Base64.decode(lockData, Base64.DEFAULT);
    //            decodeBytes = Arrays.copyOf(decodeBytes, decodeBytes.length - 6);
    //            String aesKey = lockMac;
    //            aesKey = aesKey.substring(0, 9) + aesKey.substring(10);
    //            decodeBytes = AESUtil.aesDecrypt(decodeBytes, aesKey.getBytes());
    //            if (decodeBytes == null) {
    //                return null;
    //            }
    //            lockData = new String(decodeBytes);
    //
    //            LockDataCopy lockDataCopy = GsonUtil.toObject(lockData, LockDataCopy.class);
    //            LockData lockDataObj = lockDataCopy.convert2LockData();
    //            lockData = GsonUtil.toJson(lockDataObj);
    //            if (lockDataObj == null) {
    //                return null;
    //            }
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            return null;
    //        }
    //        return lockData;
    //    }
    //    public static String parseLockData(String lockData) {
    //        try {
    //            byte[] decodeBytes = Base64.decode(lockData, Base64.DEFAULT);
    //            byte[] macBytes = Arrays.copyOfRange(decodeBytes, decodeBytes.length - 6, decodeBytes.length);
    //            decodeBytes = Arrays.copyOf(decodeBytes, decodeBytes.length - 6);
    //            String aesKey = DigitUtil.getMacByByte(macBytes);
    //            aesKey = aesKey.substring(0, 9) + aesKey.substring(10);
    //            decodeBytes = AESUtil.aesDecrypt(decodeBytes, aesKey.getBytes());
    //            if (decodeBytes == null) {
    //                return null;
    //            }
    //            lockData = new String(decodeBytes);
    //            LockDataCopy lockDataCopy = GsonUtil.toObject(lockData, LockDataCopy.class);
    //            LockData lockDataObj = lockDataCopy.convert2LockData();
    //            lockData = GsonUtil.toJson(lockDataObj);
    //            if (lockDataObj == null) {
    //                return null;
    //            }
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            return null;
    //        }
    //        return lockData;
    //    }
    fun parseLockData(lockData: String?): LockData? {
        var lockData = lockData
        var lockDataObj: LockData? = null
        try {
            var decodeBytes: ByteArray? = Base64.decode(lockData, Base64.DEFAULT)
            val macBytes = Arrays.copyOfRange(decodeBytes, decodeBytes!!.size - 6, decodeBytes.size)
            decodeBytes = Arrays.copyOf(decodeBytes, decodeBytes.size - 6)
            var aesKey: String = DigitUtil.getMacByByte(macBytes)
            aesKey = aesKey.substring(0, 9) + aesKey.substring(10)
            decodeBytes = AESUtil.aesDecrypt(decodeBytes, aesKey.toByteArray())
            if (decodeBytes == null) {
                return null
            }
            lockData = String(decodeBytes)
            lockDataObj = GsonUtil.toObject<LockData>(lockData, LockData::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return lockDataObj
    }
}