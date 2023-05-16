package com.ttlock.bl.sdk.util

import java.lang.Exception
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.Throws

/*******************************************************************************
 * AES encrypt algorithm
 *
 * @author arix04
 */
object AESUtil {
    private const val DBG = true
    fun aesEncrypt(source: ByteArray?, aesKeyArray: ByteArray?): ByteArray? {
        var encryptResArray: ByteArray? = null
        try {
//            LogUtil.d("aesKey:" + DigitUtil.byteArrayToHexString(aesKeyArray), true);
            encryptResArray = AESUtil.Encrypt(source!!, aesKeyArray, aesKeyArray!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //        LogUtil.d("encryptResArray=" + DigitUtil.byteArrayToHexString(encryptResArray), DBG);
        return encryptResArray
    }

    fun aesDecrypt(source: ByteArray, aesKeyArray: ByteArray): ByteArray? {
        var decryptResArray: ByteArray? = null
        try {
//            LogUtil.d("aesKey:" + DigitUtil.byteArrayToHexString(aesKeyArray), true);
            decryptResArray = AESUtil.Decrypt(source, aesKeyArray, aesKeyArray)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return decryptResArray
    }

    @Throws(Exception::class)
    private fun Encrypt(
        source: ByteArray,
        key: ByteArray?,
        IV: ByteArray
    ): ByteArray? {
        if (key == null) {
            LogUtil.w("Key is null", AESUtil.DBG)
            return null
        }
        if (key.size != 16) {
            LogUtil.w("the length of Key is not 16", AESUtil.DBG)
            return null
        }
        if (IV.size != 16) {
            LogUtil.w("the length of IV vector is not 16", AESUtil.DBG)
            return null
        }
        val raw: ByteArray = key
        val skeySpec = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding") // "算法/模式/补码方式"
        val iv = IvParameterSpec(IV) // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
        return cipher.doFinal(source)
    }

    @Throws(Exception::class)
    private fun Decrypt(source: ByteArray, key: ByteArray?, IV: ByteArray): ByteArray? {
        return try {
            if (key == null) {
                LogUtil.e("Key is null", AESUtil.DBG)
                return null
            }
            if (key.size != 16) {
                LogUtil.e("the length of IV vector is not 16", AESUtil.DBG)
                return null
            }
            //            byte[] raw = key.getBytes("UTF-8");
            val raw: ByteArray = key
            val skeySpec = SecretKeySpec(raw, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val iv = IvParameterSpec(IV)
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
            try {
                cipher.doFinal(source)
            } catch (e: Exception) {
                println(e.toString())
                LogUtil.w("source=" + DigitUtil.byteArrayToHexString(source), AESUtil.DBG)
                LogUtil.w("key=" + DigitUtil.byteArrayToHexString(key), AESUtil.DBG)
                null
            }
        } catch (ex: Exception) {
            println(ex.toString())
            null
        }
    }
}
