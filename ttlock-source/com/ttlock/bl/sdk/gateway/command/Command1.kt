package com.ttlock.bl.sdk.gateway.command

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
    var mac: String? = null

    /**
     * 锁类型
     */
    private var lockType = 0

    constructor(commandType: Byte) {
        header = ByteArray(2)
        header[0] = 0x72
        header[1] = 0x5b
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
        this.command = command[2]
        length = command[3]
        data = ByteArray(length.toInt())
        if (length.toInt() == command.size - 5) {
            System.arraycopy(command, 4, data, 0, length.toInt())
            checksum = command[command.size - 1]
            val checksum = CodecUtils.crccompute(Arrays.copyOf(command, command.size - 1))
            mIsChecksumValid = checksum == this.checksum
        }
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
        LogUtil.d("getAeskey macBytes:" + DigitUtil.byteArrayToHexString(macBytes))
        val aeskey = AESUtil.aesEncrypt(macBytes, Command.Companion.defaultAeskey)
        LogUtil.d("getAeskey aeskey:" + DigitUtil.byteArrayToHexString(aeskey))
        return aeskey
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

    @JvmName("getMac1")
    fun getMac(): String? {
        return mac
    }

    @JvmName("setMac1")
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
        val commandBytes = ByteArray(2 + 1 + 1 + length + 1)
        commandBytes[0] = header[0]
        commandBytes[1] = header[1]
        commandBytes[2] = command
        commandBytes[3] = length
        if (data != null && data!!.size > 0) System.arraycopy(data, 0, commandBytes, 4, data!!.size)
        val crc = CodecUtils.crccompute(Arrays.copyOf(commandBytes, commandBytes.size - 1))
        commandBytes[commandBytes.size - 1] = crc
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
            0x33,
            0xA0.toByte(),
            0x3E,
            0x78,
            0x23,
            0x6A,
            0x4D,
            0x53,
            0x88.toByte(),
            50,
            0x7A,
            0x32,
            0xA3.toByte(),
            0xBB.toByte(),
            0xF2.toByte(),
            0xEF.toByte()
        )

        /**
         * 搜索WIFI Ap
         */
        const val COMM_GET_NEARBY_SSID: Byte = 0x01

        /**
         * 配置WiFi Ap
         */
        const val COMM_CONFIGURE_WIFI: Byte = 0x02

        /**
         * 配置服务器地址
         */
        const val COMM_CONFIGURE_SERVER: Byte = 0x03

        /**
         * 配置账号
         */
        const val COMM_CONFIGURE_ACCOUNT: Byte = 0x04

        /**
         * 进入DFU升级模式
         */
        const val COMM_ENTER_DFU: Byte = 0x05

        /**
         * 静态ip
         */
        const val COMM_CONFIG_IP: Byte = 0x06

        /**
         * 保持连接时间
         */
        const val COMM_ECHO: Byte = 0x45
    }
}
