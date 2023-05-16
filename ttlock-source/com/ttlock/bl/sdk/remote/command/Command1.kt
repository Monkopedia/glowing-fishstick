package com.ttlock.bl.sdk.remote.command

import com.scaf.android.client.CodecUtils
import com.ttlock.bl.sdk.util.AESUtil
import com.ttlock.bl.sdk.util.DigitUtil
import com.ttlock.bl.sdk.util.LogUtil
import java.util.*

/**
 * Created by Smartlock on 2016/5/27.
 */
class Command {
    var header: ByteArray
    var command: Byte
    var length: Byte
    var data: ByteArray?
    var checksum: Byte = // 校验		1 字节
        0
    private var mIsChecksumValid = false
    private var mac: String? = null

    /**
     * 锁类型
     */
    private var lockType = 0

    //    基本指令格式，采用目前门锁的指令格式
    //    字段名称	字节数	备注
    //    同步头	2	0x7F 0x5A
    //    固定数据1	7	为兼容门锁协议，固定填充0x05 0x03 0x02 0x00 0x01 0x00 0x01
    //    命令码	1	门锁返回的，固定为0x54
    //    固定数据2	1	为兼容门锁协议，固定填充 0xA0
    //    参数长度	1
    //    参数	N	参数长度字段指出的字节个数
    //    CRC校验	1	前面所有字段的CRC校验值
    //    固定数据	2	0x0D 0x0A
    constructor(commandType: Byte) {
        header = ByteArray(2)
        header[0] = 0x7f
        header[1] = 0x5a
        command = commandType
        data = ByteArray(0)
        length = 0
    }

    /**
     * 指令解析
     * @param command
     */
    constructor(command: ByteArray) {
        header = ByteArray(2)
        header[0] = command[0]
        header[1] = command[1]
        this.command = command[9]
        length = command[11]
        data = ByteArray(length.toInt())
        System.arraycopy(command, 12, data, 0, length.toInt())
        checksum = command[command.size - 1]
        val checksum = CodecUtils.crccompute(Arrays.copyOf(command, command.size - 1))
        mIsChecksumValid = checksum == this.checksum
        //        LogUtil.d("checksum=" + checksum + " this.checksum=" + this.checksum, DBG);
//        LogUtil.d("mIsChecksumValid : " + mIsChecksumValid, DBG);
    }

    @JvmName("setCommand1")
    fun setCommand(command: Byte) {
        this.command = command
    }

    @JvmName("getCommand1")
    fun getCommand(): Byte {
        return command
    }

    @JvmName("getData1")
    fun getData(): ByteArray? {
        return getData(getAeskey()!!)
    }

    fun getAeskey(): ByteArray? {
        var macArr: Array<String?>? = mac!!.split(":").toTypedArray()
        macArr = reverseArray(macArr)
        val macBytes = hexStringArrToByteArr(macArr)
        return AESUtil.aesEncrypt(macBytes, Command.Companion.defaultAeskey)
    }

    fun getData(aesKeyArray: ByteArray): ByteArray? {
        val values: ByteArray?
        values = AESUtil.aesDecrypt(data!!, aesKeyArray)
        return values
    }

    @JvmName("setData1")
    fun setData(data: ByteArray?) {
        setData(data, getAeskey())
    }

    fun getMac(): String? {
        return mac
    }

    fun setMac(mac: String?) {
        this.mac = mac
    }

    fun setData(data: ByteArray?, aesKeyArray: ByteArray?) {
        LogUtil.d("data=" + DigitUtil.byteArrayToHexString(data), Command.Companion.DBG)
        LogUtil.d(
            "aesKeyArray=" + DigitUtil.byteArrayToHexString(aesKeyArray),
            Command.Companion.DBG
        )
        this.data = AESUtil.aesEncrypt(data, aesKeyArray)
        length = this.data!!.size.toByte()
    }

    fun isChecksumValid(): Boolean {
        return mIsChecksumValid
    }

    fun getLockType(): Int {
        return lockType
    }

    fun setLockType(lockType: Int) {
        this.lockType = lockType
    }

    fun buildCommand(): ByteArray {
        val commandBytes = ByteArray(2 + 7 + 1 + 1 + +1 + length + 1 + 2)
        commandBytes[0] = header[0]
        commandBytes[1] = header[1]

        // 跟门锁保持一致 版本信息 固定数据
        commandBytes[2] = 0x05
        commandBytes[3] = 0x03
        commandBytes[4] = 0x02
        commandBytes[5] = 0x00
        commandBytes[6] = 0x01
        commandBytes[7] = 0x00
        commandBytes[8] = 0x01
        commandBytes[9] = command

        // 跟门锁一致 固定填充
        commandBytes[10] = 0xA5.toByte()
        commandBytes[11] = length
        if (data != null && data!!.size > 0) System.arraycopy(
            data,
            0,
            commandBytes,
            12,
            data!!.size
        )
        val crc = CodecUtils.crccompute(Arrays.copyOf(commandBytes, commandBytes.size - 3))
        commandBytes[commandBytes.size - 3] = crc
        commandBytes[commandBytes.size - 2] = 0x0d
        commandBytes[commandBytes.size - 1] = 0x0a
        return commandBytes
    }

    fun hexStringArrToByteArr(arr: Array<String?>?): ByteArray? {
        if (arr == null) return null
        val bytes = ByteArray(arr.size)
        for (i in arr.indices) {
            bytes[i] = Integer.valueOf(arr[i], 16).toByte()
        }
        return bytes
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

    companion object {
        private const val DBG = true
        var defaultAeskey = byteArrayOf(
            0x17,
            0x92.toByte(),
            0x45,
            0xCD.toByte(),
            0x45,
            0x23,
            0x99.toByte(),
            0xA3.toByte(),
            0xDF.toByte(),
            0x62,
            0x7E,
            0x5F,
            0x87.toByte(),
            0x09.toByte(),
            0xD2.toByte(),
            0x49.toByte()
        )
        const val COMM_READ_FEATURE: Byte = 0x01

        /**
         * 设置锁
         */
        const val COMM_SET_LOCK: Byte = 0x6F
    }
}
