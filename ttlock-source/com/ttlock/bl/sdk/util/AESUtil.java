package com.ttlock.bl.sdk.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



/*******************************************************************************
 * AES encrypt algorithm
 * 
 * @author arix04
 * 
 */
 
public class AESUtil {

    private static boolean DBG = true;

    public static byte[] aesEncrypt(byte[] source, byte[] aesKeyArray) {
        byte[] encryptResArray = null;
        try {
//            LogUtil.d("aesKey:" + DigitUtil.byteArrayToHexString(aesKeyArray), true);
            encryptResArray = Encrypt(source, aesKeyArray, aesKeyArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LogUtil.d("encryptResArray=" + DigitUtil.byteArrayToHexString(encryptResArray), DBG);
        return encryptResArray;
    }

    public static byte[] aesDecrypt(byte[] source, byte[] aesKeyArray) {
        byte[] decryptResArray = null;
        try {
//            LogUtil.d("aesKey:" + DigitUtil.byteArrayToHexString(aesKeyArray), true);
            decryptResArray = Decrypt(source, aesKeyArray, aesKeyArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptResArray;
    }

    private static byte[] Encrypt(byte[] source, byte[] key, byte[] IV) throws Exception {
        if (key == null) {
            LogUtil.w("Key is null", DBG);
            return null;
        }
        if (key.length != 16) {
            LogUtil.w("the length of Key is not 16", DBG);
            return null;
        }
        if(IV.length != 16) {
            LogUtil.w("the length of IV vector is not 16", DBG);
        	return null;
        }
        byte[] raw = key;
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec(IV);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(source);
        return encrypted;
    }
    
    private static byte[] Decrypt(byte[] source, byte[] key, byte[] IV) throws Exception {
        try {
            if (key == null) {
                LogUtil.e("Key is null", DBG);
                return null;
            }
            if (key.length != 16) {
                LogUtil.e("the length of IV vector is not 16", DBG);
                return null;
            }
//            byte[] raw = key.getBytes("UTF-8");
            byte[] raw = key;
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(IV);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            try {
                byte[] original = cipher.doFinal(source);
                return original;
            } catch (Exception e) {
                System.out.println(e.toString());
                LogUtil.w("source=" + DigitUtil.byteArrayToHexString(source), DBG);
                LogUtil.w("key=" + DigitUtil.byteArrayToHexString(key), DBG);
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

}