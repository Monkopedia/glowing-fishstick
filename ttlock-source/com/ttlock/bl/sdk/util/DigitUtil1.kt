package com.ttlock.bl.sdk.util

import android.util.TextUtils
import com.scaf.android.client.CodecUtils
import com.ttlock.bl.sdk.api.ParamInvalidException
import com.ttlock.bl.sdk.constant.Feature
import com.ttlock.bl.sdk.entity.HotelInfo
import com.ttlock.bl.sdk.entity.PowerSaverWorkMode
import json.JSONArray
import json.JSONException
import json.JSONObject
import java.lang.Exception
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.LinkedHashSet

/**
 * Created by Smartlock on 2016/5/27.
 */
object DigitUtil {
    private const val DBG = false
    fun generateRandomByte(): Byte {
        var randomByte: Byte = 0
        do {
            randomByte = (Math.random() * 128).toInt().toByte()
        } while (randomByte.toInt() == 0)
        return randomByte
    }

    /**
     * generate dynamic password, the first position is zero
     *
     * @param length
     * @return
     */
    fun generateDynamicPassword(length: Int): ByteArray {
        val bytes = ByteArray(length)
        bytes[0] = 48
        for (i in 1 until length) {
            var r = Math.random() * 10
            if (r >= 10) {
                r = 9.0
            }
            bytes[i] = (r + 48).toInt().toByte()
        }
        return bytes
    }

    /**
     * @param macBytes
     * @return
     */
    fun getMacString(macBytes: ByteArray): String {
        var value = ""
        for (i in macBytes.indices.reversed()) { // 小端在前
            var sTemp = Integer.toHexString(
                0xFF and macBytes[i]
                    .toInt()
            )
            if (sTemp.length == 1) sTemp = "0$sTemp"
            value = "$value$sTemp:"
        }
        value = value.substring(0, value.lastIndexOf(":"))
        return value.uppercase(Locale.getDefault())
    }

    fun shortToByteArray(value: Short): ByteArray {
        var value = value.toInt()
        val shortByteArray = ByteArray(2)
        for (i in 1 downTo 0) {
            shortByteArray[i] = value.toByte()
            value = value shr 8
        }
        return shortByteArray
    }

    fun integerToByteArray(value: Int): ByteArray {
        val bytes = ByteArray(4)
        val offset = byteArrayOf(24, 16, 8, 0)
        for (i in 0..3) {
            bytes[i] = (value shr offset[i].toInt()).toByte()
        }
        return bytes
    }

    fun reverseArray(array: ByteArray?): ByteArray? {
        if (array != null) {
            val len = array.size
            for (i in 0 until len / 2) {
                val temp = array[i]
                array[i] = array[len - i - 1]
                array[len - i - 1] = temp
            }
        }
        return array
    }

    fun reverseArray(array: Array<String?>?): Array<String?>? {
        if (array != null) {
            val len = array.size
            for (i in 0 until len / 2) {
                val temp = array[i]
                array[i] = array[len - i - 1]
                array[len - i - 1] = temp
            }
        }
        return array
    }

    fun appendWithColon(array: Array<String?>?): String {
        val stringBuilder = StringBuilder()
        if (array != null && array.size > 0) {
            stringBuilder.append(array[0])
            for (i in 1 until array.size) {
                stringBuilder.append(":")
                stringBuilder.append(array[i])
            }
        }
        return stringBuilder.toString()
    }

    fun longToByteArrayWithLen(value: Long, len: Int): ByteArray {
        var value = value
        val bytes = ByteArray(len)
        for (i in len - 1 downTo 0) {
            bytes[i] = value.toByte()
            value = value shr 8
        }
        return bytes
    }

    fun byteArrayToShort(array: ByteArray): Short {
        return (array[1].toInt() and 0xff or (array[0].toInt() shl 8)).toShort()
    }

    fun byteToHex(value: Byte): String {
        var hex = Integer.toHexString(value.toInt() and 0xFF)
        if (hex.length == 1) hex = "0$hex"
        return hex
    }

    fun fourBytesToLong(data: ByteArray): Long {
        var res: Long = 0
        res = res or (data[0].toLong() shl 24 and 0xFF000000L)
        res = res or (data[1].toLong() shl 16 and 0x00FF0000)
        res = res or (data[2].toLong() shl 8 and 0x0000FF00)
        res = res or (data[3].toLong() and 0xFF)
        LogUtil.d("res:$res", DBG)
        return res
    }

    fun bytesToLong(data: ByteArray): Long {
        var res: Long = 0
        val len = data.size
        var tmpLen = len
        val mask = longArrayOf(
            0xFFL,
            0xFF00L,
            0xFF0000L,
            0xFF000000L,
            0xFF00000000L,
            0xFF0000000000L,
            0xFF000000000000L,
            -0x100000000000000L
        )
        while (tmpLen-- > 0) {
            res =
                res or (data[len - tmpLen - 1].toLong() shl (tmpLen * 8L).toInt() and mask[tmpLen])
        }
        return res
    }

    // TODO:
    fun sixBytesToLong(data: ByteArray): Long {
        var res = 0L
        res = res or (data[0].toLong() shl 40 and 0xFF0000000000L)
        res = res or (data[1].toLong() shl 32 and 0xFF00000000L)
        res = res or (data[2].toLong() shl 24 and 0x0000FF000000L)
        res = res or (data[3].toLong() shl 16 and 0x000000FF0000L)
        res = res or (data[4].toLong() shl 8 and 0x00000000FF00L)
        res = res or (data[5].toLong() and 0xFFL)
        LogUtil.d("res:$res", DBG)
        return res
    }

    /**
     * convert byte array to 16 hexadecimal string
     */
    fun byteArrayToHexString(array: ByteArray?): String? {
        if (array == null) return null
        //        LogUtil.
        val stringBuilder = StringBuilder()
        stringBuilder.append('[')
        if (array == null || array.size == 0) {
            stringBuilder.append(']')
        }
        for (i in array.indices) {
            stringBuilder.append(byteToHex(array[i]))
            stringBuilder.append(',')
        }
        stringBuilder.replace(stringBuilder.length - 1, stringBuilder.length, "]")
        return stringBuilder.toString()
    }

    fun hexString2ByteArray(hexStr: String): ByteArray {
        val len = hexStr.length
        val array = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            array[i / 2] = Integer.valueOf(hexStr.substring(i, i + 2), 16).toByte()
            i += 2
        }
        return array
    }

    /**
     * 计算16位校验和
     * @param bytes
     * @return
     */
    fun checkSum(bytes: ByteArray): ByteArray {
        val length = bytes.size
        var sum: Short = 0
        for (i in 0 until length) {
            sum = (sum + (bytes[i].toInt() and 0xff)).toShort()
        }
        LogUtil.d("sum:$sum")
        return shortToByteArray(sum)
    }

    fun macDividerByColonToByteArray(mac: String): ByteArray? {
        if (TextUtils.isEmpty(mac)) return null
        val macArray = mac.split(":").toTypedArray()
        val macByte = ByteArray(macArray.size)
        for (i in macByte.indices) {
            macByte[i] = Integer.valueOf(macArray[i], 16).toByte()
        }
        return macByte
    }

    fun hexStringArrToByteArr(arr: Array<String?>?): ByteArray? {
        if (arr == null) return null
        val bytes = ByteArray(arr.size)
        for (i in arr.indices) {
            bytes[i] = Integer.valueOf(arr[i], 16).toByte()
        }
        return bytes
    }

    fun stringDividerByDotToByteArray(source: String): ByteArray {
        val originalBytes = source.toByteArray()
        val originalString: String = String(Base64.getDecoder().decode(originalBytes))
        LogUtil.d("originalString:$originalString", DBG)
        val strings = originalString.split(",").toTypedArray()
        val len = strings.size
        val resBytes = ByteArray(len)
        try {
            for (i in 0 until len) {
                resBytes[i] = Integer.valueOf(strings[i]).toByte()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resBytes
    }

    fun getControlableFloors(floorStr: String): ByteArray {
        var floors = ByteArray(0)
        try {
            val strings = floorStr.split(",").toTypedArray()
            floors = ByteArray(strings.size)
            for (i in strings.indices) {
                floors[i] = java.lang.Byte.valueOf(strings[i])
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return floors
    }

    /**
     *
     * @param oriWeekDays ori : 1-sunday, 2-monday, ……, 7-saturday
     * @return newWeekDays: 1-monday, ……, 7-sunday.
     */
    fun convertWeekDays(oriWeekDays: String?): String? {
        if (!TextUtils.isEmpty(oriWeekDays)) {
            try {
                val jsonArray = JSONArray(oriWeekDays!!)
                val newJsonArray = JSONArray()
                for (i in 0 until jsonArray.length()) {
                    var week = jsonArray.get(i) as Int
                    week = if (week == 1) 7 else week - 1
                    newJsonArray.put(week)
                }
                return newJsonArray.toString()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * format the date
     * @param time
     * @param formate
     * @return
     */
    fun formateDateFromLong(time: Long, formate: String?): String {
        // yyyy-MM-dd HH:mm:ss
        val date = Date(time)
        val formatter = SimpleDateFormat(formate)
        return formatter.format(date)
    }

    /**
     * yyMMddHHmmss format
     * @param time
     * @return
     */
    fun convertTimeToByteArray(time: String): ByteArray {
        var len = time.length
        len /= 2
        val values = ByteArray(len)
        for (i in 0 until len) {
            val string = time.substring(i * 2, i * 2 + 2)
            values[i] = java.lang.Byte.valueOf(string)
        }
        return values
    }

    /**
     * encrypt
     * @param sourceBytes
     * @return
     */
    fun encodeDefaultPassword(sourceBytes: ByteArray?): ByteArray? {
        return if (sourceBytes == null || sourceBytes.size == 0) {
//            LogUtil.d("sourceBytes=" + sourceBytes, DBG);
            sourceBytes
        } else CodecUtils.encode(sourceBytes)
    }

    /**
     * decrypt
     * @param sourceBytes
     * @return
     */
    fun decodeDefaultPassword(sourceBytes: ByteArray?): ByteArray? {
        return if (sourceBytes == null || sourceBytes.size == 0) {
//            LogUtil.d("sourceBytes=" + sourceBytes, DBG);
            sourceBytes
        } else CodecUtils.decode(sourceBytes)
        //        LogUtil.d("sourceBytes=" + DigitUtil.byteArrayToHexString(sourceBytes), DBG);
    }

    /**
     * 开锁的密码 老版本使用
     * @param passwordFromLock
     * @param localPassword
     * @return
     */
    fun getUnlockPassword(passwordFromLock: Long, localPassword: Long): String {
        val result = (passwordFromLock + localPassword) % 2000000000L
        return result.toString()
    }

    /**
     * 2S及2S升级 V3 版本不取余
     * @param passwordFromLock
     * @param localPassword
     * @return
     */
    fun getUnlockPwd_new(passwordFromLock: Long, localPassword: Long): String {
        val result = (passwordFromLock + localPassword).toInt()
        return result.toString()
    }

    fun getUnlockPwdBytes_new(passwordFromLock: Long, localPassword: Long): ByteArray {
        val result = (passwordFromLock + localPassword).toInt()
        return integerToByteArray(result)
    }

    /**
     * 获取10位校验表 不重复
     * 0123456789
     */
    fun getCheckTable(): String {
        val set: MutableSet<Int> = LinkedHashSet()
        while (set.size < 10) {
            set.add(getRandomIntegerByUpperBound(10))
        }
        val stringBuilder = StringBuilder()
        val iterator: Iterator<*> = set.iterator()
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next())
        }
        return stringBuilder.toString()
    }

    fun getRandomIntegerByUpperBound(upperBound: Int): Int {
        val random = Random()
        return random.nextInt(upperBound)
    }

    fun getPowerWorkModeValue(powerSaverWorkModeList: List<PowerSaverWorkMode>?): Byte {
        if (powerSaverWorkModeList == null || powerSaverWorkModeList.size == 0) {
            return 0
        }
        var powerWorkModeValue = 0
        for (powerSaverWorkMode in powerSaverWorkModeList) {
            powerWorkModeValue = powerWorkModeValue or powerSaverWorkMode.getValue()
        }
        return powerWorkModeValue.toByte()
    }

    /**
     * 扇区从0开始计算 这里算的是保留扇区跟锁内相反
     * @param sectors
     * @return
     */
    fun calSectorValue(sectors: ArrayList<Int>?): Short {
        if (sectors == null) return 0
        var sectorValue: Int = 0
        for (i in sectors.indices) {
            sectorValue = sectorValue or (1 shl 15 - sectors[i])
        }
        return if (sectorValue.toInt() == -1) { // 全部设置
            0
        } else sectorValue.toShort()
    }

    /**
     * 扇区从1开始计算 计算的是写入扇区跟锁内一致
     * @param sectorStr
     * @return
     */
    fun calSectorValue(sectorStr: String?): Short {
        if (sectorStr == null) {
            return 0
        }
        var sectorValue: Int = 0
        try {
            val sectors = sectorStr.split(",").toTypedArray()
            for (i in sectors.indices) {
                sectorValue =
                    sectorValue or (1 shl 16 - Integer.valueOf(sectors[i]))
            }
            // todo:
            if (sectorValue.toInt() == -1) { // 全部设置
                return 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sectorValue.toShort()
    }

    /**
     * 三代锁获取密码约定数和映射数6 bytes
     * @param code 1.5bytes
     * @param secretKey 4.5bytes
     * @return
     */
    fun convertCodeAndKeyToByteArray(code: Int, secretKey: String): ByteArray {
        LogUtil.d("code:$code", DBG)
        LogUtil.d("secretKey:$secretKey", DBG)
        val res = ByteArray(6)
        res[0] = (code shr 4).toByte()
        res[1] = (code shl 4).toByte()
        val secretKeyLong = java.lang.Long.valueOf(secretKey)
        var offset = 32
        res[1] = (res[1].toInt() or (secretKeyLong shr offset).toInt()).toByte() // 增加增加映射数的半个字节
        for (i in 2..5) {
            offset -= 8
            res[i] = (secretKeyLong shr offset).toByte()
        }
        return res
    }

    /**
     * 生成固定长度密码
     * @param length
     * @return
     */
    fun generatePwdByLength(length: Int): String {
        val pwd = CharArray(length)
        val random = Random()
        for (i in 0 until length) {
            pwd[i] = ('0'.code + random.nextInt(10)).toChar()
        }
        return String(pwd)
    }

    fun generateRandomIntegerByUpperBound(upperBound: Int): Int {
        val random = Random()
        return random.nextInt(upperBound)
    }

    /**
     * 900个密码根据密码类型生成
     * @param pwdType
     * @return
     */
    fun generatePwdByType(pwdType: Int): String {
        val pwd = CharArray(6)
        pwd[0] = ('0'.code + pwdType).toChar()
        val random = Random()
        for (i in 1..5) {
            pwd[i] = ('0'.code + random.nextInt(9)).toChar()
        }
        return String(pwd)
    }

    /**
     * ase解密转换工具
     */
    fun convertAesKeyStrToBytes(aesKeyStr: String): ByteArray? {
        if (TextUtils.isEmpty(aesKeyStr)) return null
        val aesKeyStrings = aesKeyStr.split(",").toTypedArray()
        val len = aesKeyStrings.size
        val aesKey = ByteArray(len)
        for (i in 0 until len) {
            aesKey[i] = Integer.valueOf(aesKeyStrings[i], 16).toByte()
        }
        return aesKey
    }

    /**
     * 判断是否是数字
     * @param str
     * @return
     */
    fun isNumeric(str: String?): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        val isNum = pattern.matcher(str)
        return if (!isNum.matches()) {
            false
        } else true
    }

    /**
     * 加密aeskey
     * @param aeskey
     * @return
     */
    fun encodeAesKey(aeskey: ByteArray?): String? {
        if (aeskey != null && aeskey.size > 0) {
            var aeskeyStr = byteArrayToHexString(aeskey)
            aeskeyStr = aeskeyStr!!.substring(1, aeskeyStr.length - 1)
            return aeskeyStr
        }
        return null
    }

    /**
     * 加密锁中adminPs、unlockKey等数据
     * @param originalStr   原字符串
     * @return
     */
    fun encodeLockData(originalStr: String): String {
        val encodedBytes = encodeDefaultPassword(originalStr.toByteArray())
        val encodedString = byteArrayToStringDividerByDot(encodedBytes)
        return Base64.getEncoder().encodeToString(encodedString.toByteArray())
    }

    /**
     * 解密锁中admiinPs、unlockKey等数据
     * @param originalStr
     * @return
     */
    fun decodeLockData(originalStr: String): String {
        var decodeData = ""
        try {
            decodeData = String(decodeDefaultPassword(stringDividerByDotToByteArray(originalStr))!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return decodeData
    }

    @Throws(ParamInvalidException::class)
    fun decodeHotelInfo(hotelInfoStr: String): HotelInfo {
        val hotelInfo = HotelInfo()
        val data = decodeLockData(hotelInfoStr)
        if (!TextUtils.isEmpty(data)) {
            val array = data.split(",").toTypedArray()
            hotelInfo.hotelNumber = Integer.valueOf(array[0])
            if (array[1] != null && array[2] != null) {
                hotelInfo.icKey = convertStringDividerByDot(array[1])
                hotelInfo.aesKey = convertStringDividerByDot(array[2])
            } else {
                throw ParamInvalidException()
            }
        } else {
            throw ParamInvalidException()
        }
        return hotelInfo
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
    fun byteArrayToStringDividerByDot(array: ByteArray?): String {
        if (array == null) {
            return "null"
        }
        if (array.size == 0) {
            return ""
        }
        val sb = StringBuilder(array.size * 4)
        sb.append(array[0].toInt())
        for (i in 1 until array.size) {
            sb.append(",")
            sb.append(array[i].toInt())
        }
        return sb.toString()
    }

    fun convertToFeatureValue(array: ByteArray?): String? {
        if (array == null) return null
        val stringBuilder = StringBuilder()
        val groupBuilder = StringBuilder() // 4个字节一组
        for (i in array.indices) {
            groupBuilder.append(byteToHex(array[i]))
            if (i % 4 == 3) {
                stringBuilder.insert(0, groupBuilder)
                groupBuilder.setLength(0)
            }
        }
        var index = 0
        while (index < stringBuilder.length && stringBuilder[index] == '0') index++
        return if (index == stringBuilder.length) "0" else stringBuilder.substring(index)
            .uppercase(Locale.getDefault())
    }

    fun getMD5(source: String): String {
        val buf = StringBuffer("")
        try {
            val md: MessageDigest = MessageDigest.getInstance("MD5")
            md.update(source.toByteArray())
            val e: ByteArray = md.digest()
            for (offset in e.indices) {
                var i = e[offset].toInt()
                if (i < 0) {
                    i += 256
                }
                if (i < 16) {
                    buf.append("0")
                }
                buf.append(Integer.toHexString(i))
            }
        } catch (var6: NoSuchAlgorithmException) {
            var6.printStackTrace()
        }
        return buf.toString()
    }

    @Throws(JSONException::class)
    fun generateKeyboardPwd_Json(pwdList: Queue<String>): String {
        val jsonObject = JSONObject()
        val one_day_pwd = StringBuilder(pwdList.poll())
        for (i in 1..299) {
            one_day_pwd.append(",")
            one_day_pwd.append(pwdList.poll())
        }
        LogUtil.d("one_day_pwd:$one_day_pwd", DBG)
        jsonObject.put("oneDay", one_day_pwd)
        jsonObject.put("oneDaySequence", 0)
        val two_days_pwd = StringBuilder(pwdList.poll())
        for (i in 1..149) {
            two_days_pwd.append(",")
            two_days_pwd.append(pwdList.poll())
        }
        jsonObject.put("twoDays", two_days_pwd)
        jsonObject.put("twoDaysSequence", 0)
        LogUtil.d("twoDays:$two_days_pwd", DBG)
        val three_days_pwd = StringBuilder(pwdList.poll())
        for (i in 1..99) {
            three_days_pwd.append(",")
            three_days_pwd.append(pwdList.poll())
        }
        jsonObject.put("threeDays", three_days_pwd)
        jsonObject.put("threeDaysSequence", 0)
        LogUtil.d("three_days_pwd:$three_days_pwd", DBG)
        val four_days_pwd = StringBuilder(pwdList.poll())
        for (i in 1..99) {
            four_days_pwd.append(",")
            four_days_pwd.append(pwdList.poll())
        }
        jsonObject.put("fourDays", four_days_pwd)
        jsonObject.put("fourDaysSequence", 0)
        val five_days_pwd = StringBuilder(pwdList.poll())
        for (i in 1..49) {
            five_days_pwd.append(",")
            five_days_pwd.append(pwdList.poll())
        }
        jsonObject.put("fiveDays", five_days_pwd)
        jsonObject.put("fiveDaysSequence", 0)
        val six_days_pwd = StringBuilder(pwdList.poll())
        for (i in 1..49) {
            six_days_pwd.append(",")
            six_days_pwd.append(pwdList.poll())
        }
        jsonObject.put("sixDays", six_days_pwd)
        jsonObject.put("sixDaysSequence", 0)
        val seven_days_pwd = StringBuilder(pwdList.poll())
        for (i in 1..49) {
            seven_days_pwd.append(",")
            seven_days_pwd.append(pwdList.poll())
        }
        jsonObject.put("sevenDays", seven_days_pwd)
        jsonObject.put("sevenDaysSequence", 0)
        val ten_minutes_pwd = StringBuilder(pwdList.poll())
        for (i in 1..99) {
            ten_minutes_pwd.append(",")
            ten_minutes_pwd.append(pwdList.poll())
        }
        jsonObject.put("tenMinutes", ten_minutes_pwd)
        jsonObject.put("tenMinutesSequence", 0)
        LogUtil.d("ten_minutes_pwd:$ten_minutes_pwd", DBG)
        return jsonObject.toString()
    }

    /**
     * Whether the passcode is supported
     * @param specialValue
     * @return
     */
    fun isSupportPasscode(specialValue: Int): Boolean {
        return specialValue and Feature.PASSCODE.toInt() != 0
    }

    /**
     * Whether the CARD card is supported
     * @param specialValue
     * @return
     */
    fun isSupportIC(specialValue: Int): Boolean {
        return specialValue and Feature.IC.toInt() != 0
    }

    /**
     * Whether the fingerprint is supported
     * @param specialValue
     * @return
     */
    fun isSupportFingerPrint(specialValue: Int): Boolean {
        return specialValue and Feature.FINGER_PRINT.toInt() != 0
    }

    /**
     * Whether the wristband is supported
     * @param specialValue
     * @return
     */
    fun isSupportWristband(specialValue: Int): Boolean {
        return if (specialValue and Feature.WRIST_BAND.toInt() != 0) true else false
    }

    /**
     * Whether the function of auto lock is supported
     * 是否支持自动闭锁
     * @param specialValue
     * @return
     */
    fun isSupportAutoLock(specialValue: Int): Boolean {
        return specialValue and Feature.AUTO_LOCK.toInt() != 0
    }

    /**
     * Whether the modification of passcode is supported
     * @param specialValue
     * @return
     */
    fun isSupportModifyPasscode(specialValue: Int): Boolean {
        return specialValue and Feature.MODIFY_PASSCODE_FUNCTION != 0
    }

    /**
     * Whether the function of locking is supported
     * 判断是否支持闭锁指令
     * @param specialValue
     * @return
     */
    fun isSupportManualLock(specialValue: Int): Boolean {
        return if (specialValue and Feature.MANUAL_LOCK != 0) true else false
    }

    /**
     * Whether the function of displaying or hiddening passcode is supported
     * 判断是否可以显示或隐藏密码
     * @param specialValue
     * @return
     */
    fun isSupportShowPasscode(specialValue: Int): Boolean {
        return specialValue and Feature.PASSWORD_DISPLAY_OR_HIDE != 0
    }

    /**
     * Whether the cyclic passcode is supported
     * @param specialValue
     * @return
     */
    fun isSupportCyclicPasscode(specialValue: Int): Boolean {
        return if (specialValue and Feature.CYCLIC_PASSWORD != 0) true else false
    }

    /**
     * Whether the function of gateway unlocking is supported
     * @param specialValue
     * @return
     */
    fun isSupportRemoteUnlock(specialValue: Int): Boolean {
//        LogUtil.d("specialValue:" + DigitUtil.byteArrayToHexString(DigitUtil.integerToByteArray(specialValue)), DBG);
        return specialValue and Feature.GATEWAY_UNLOCK != 0
    }

    /**
     * Whether the door sensor is supported
     * 是否支持门磁
     * @param specialValue
     * @return
     */
    fun isSupportDoorSensor(specialValue: Int): Boolean {
        return if (specialValue and Feature.MAGNETOMETER != 0) true else false
    }

    /**
     * Whether the function of controling remote unlock switch is supported
     * @param specialValue
     * @return
     */
    fun isSupportRemoteUnlockSwitch(specialValue: Int): Boolean {
//        LogUtil.d("specialValue:" + DigitUtil.byteArrayToHexString(DigitUtil.integerToByteArray(specialValue)), DBG);
        return specialValue and Feature.CONFIG_GATEWAY_UNLOCK != 0
    }

    /**
     * Whether the audio management is supported
     * @param specialValue
     * @return
     */
    fun isSupportAudioManagement(specialValue: Int): Boolean {
//        LogUtil.d("specialValue:" + specialValue, DBG);
        return specialValue and Feature.AUDIO_MANAGEMENT != 0
    }

    /**
     * Whether the NB Lock is supported
     * @param specialValue
     * @return
     */
    fun isSupportNBLock(specialValue: Int): Boolean {
        return specialValue and Feature.NB_LOCK != 0
    }

    fun convertStringDividerByDot(str: String): String {
        return if (TextUtils.isEmpty(str)) {
            str
        } else {
            val stringBuilder = StringBuilder()
            var i = 0
            while (i < str.length) {
                stringBuilder.append(str.substring(i, i + 2))
                stringBuilder.append(',')
                i += 2
            }
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
            stringBuilder.toString()
        }
    }

    /**
     * 把mac地址转化为长度为6的byte[]
     *
     * @param mac mac地址，形式：B4:99:4C:67:96:79
     * @return byte[]，如 [0xB4, 0x99, 0x4C, 0x67, 0x96, 0x79]
     */
    fun getByteArrayByMac(mac: String): ByteArray {
        // 判断是否符合mac地址格式
        if (!isMacFormat(mac)) {
            return byteArrayOf()
        }
        val bytes = ByteArray(6)
        val strs = mac.split(":").toTypedArray()
        for (i in 0..5) {
            bytes[i] = strs[i].toInt(16).toByte()
        }
        return bytes
    }

    /** java 合并两个byte数组  */
    fun byteMerger(byte1: ByteArray, byte2: ByteArray): ByteArray {
        val byte3 = ByteArray(byte1.size + byte2.size)
        System.arraycopy(byte1, 0, byte3, 0, byte1.size)
        System.arraycopy(byte2, 0, byte3, byte1.size, byte2.size)
        return byte3
    }

    /** 判断字符串是否符合mac地址格式，冒号相连  */
    fun isMacFormat(str: String): Boolean {
        if (TextUtils.isEmpty(str)) {
            return false
        }
        val regex =
            Regex("[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]:[0-9A-F][0-9A-F]")
        return str.matches(regex)
    }

    fun getMacByByte(macBytes: ByteArray): String {
        var value = ""
        for (i in macBytes.indices) {
            var sTemp = Integer.toHexString(
                0xFF and macBytes[i]
                    .toInt()
            )
            if (sTemp.length == 1) sTemp = "0$sTemp"
            value = "$value$sTemp:"
        }
        value = value.substring(0, value.lastIndexOf(":"))
        return value.uppercase(Locale.getDefault())
    }

    fun getReverseMacArray(mac: String): ByteArray? {
        var array = macDividerByColonToByteArray(mac)
        array = reverseArray(array)
        return array
    }

    /**
     * 根据时间偏移量转换时间磋
     * @param timeBytes
     * @param timezoneOffSet
     * @return
     */
    fun convertTimestampWithTimezoneOffset(timeBytes: ByteArray, timezoneOffSet: Int): Long {
        var timezoneOffSet = timezoneOffSet
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(
            2000 + timeBytes[0],
            timeBytes[1] - 1,
            timeBytes[2].toInt(),
            timeBytes[3].toInt(),
            timeBytes[4].toInt(),
            timeBytes[5].toInt()
        )
        // 根据时间偏移量计算时间
        val timeZone: TimeZone = TimeZone.getDefault()
        LogUtil.d("timezoneOffSet:$timezoneOffSet", DBG)
        if (timeZone.inDaylightTime(Date(System.currentTimeMillis()))) timezoneOffSet -= timeZone.getDSTSavings()
        timeZone.setRawOffset(timezoneOffSet)
        calendar.setTimeZone(timeZone)
        return calendar.getTimeInMillis()
    }

    /**
     * 将时间磋转换成锁所在时区的时间磋
     * @param timestamp
     * @param lockZoneOffSet
     * @return
     */
    fun convertTimestamp2LockZoneTimestamp(timestamp: Long, lockZoneOffSet: Long): Long {
        return timestamp + lockZoneOffSet - TimeZone.getDefault()
            .getOffset(System.currentTimeMillis())
    }

    fun convertTimestamp2LockZoneBytes_yyMMddHHmm(
        timestamp: Long,
        lockZoneOffSet: Long
    ): ByteArray {
        val lockTimestamp = convertTimestamp2LockZoneTimestamp(timestamp, lockZoneOffSet)
        val lockTimeStr = formateDateFromLong(lockTimestamp, "yyMMddHHmm")
        return convertTimeToByteArray(lockTimeStr)
    }

    fun convertIp2Bytes(ip: String): ByteArray? {
        val bytes = ByteArray(4)
        if (TextUtils.isEmpty(ip)) {
            return bytes
        }
        try {
            val dividerList = ip.split("\\.").toTypedArray()
            if (dividerList.size != 4) {
                return null
            }
            for (i in 0..3) {
                val temp = Integer.valueOf(dividerList[i])
                if (temp < 0 || temp > 255) {
                    return null
                }
                bytes[i] = temp.toByte()
            }
        } catch (e: Exception) {
            return null
        }
        return bytes
    }
}
