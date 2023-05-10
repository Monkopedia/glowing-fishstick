package com.ttlock.bl.sdk.api

import com.google.gson.Gson
import java.lang.Exception

/**
 * Created by Smartlock on 2016/5/27.
 */
class Command {
    var header // 帧首 		2 字节
            : ByteArray
    var protocol_type // 预留	 	1 字节 //reserved 修改为protocol_type
            : Byte
    var command // 命令字 	1 字节
            : Byte = 0
    var encrypt // 加密字		1 字节
            : Byte = 0
    var length // 长度		1 字节
            : Byte = 0
    var data // 数据
            : ByteArray?
    var checksum // 校验		1 字节
            : Byte = 0

    // V4版本新加
    private var sub_version: Byte = 0
    private var scene: Byte = 0
    var organization: ByteArray
    var sub_organization: ByteArray
    private var mIsChecksumValid = false

    /**
     * 锁类型
     */
    private var lockType = 0

    @Deprecated("")
    constructor(commandType: Byte) {
        header = ByteArray(2)
        header[0] = 0x7F
        header[1] = 0x5A
        // new version add
//        organization = new byte[2];
//        sub_organization = new byte[2];
        encrypt = DigitUtil.generateRandomByte()
        protocol_type = commandType
        data = ByteArray(0)
        length = 0
        generateLockType()
    }

    constructor(lockVersion: LockVersion) {
        header = ByteArray(2)
        header[0] = 0x7F
        header[1] = 0x5A
        protocol_type = lockVersion.getProtocolType()
        sub_version = lockVersion.getProtocolVersion()
        scene = lockVersion.getScene()
        organization = DigitUtil.shortToByteArray(lockVersion.getGroupId())
        sub_organization = DigitUtil.shortToByteArray(lockVersion.getOrgId())
        encrypt = APP_COMMAND
        generateLockType()
    }

    constructor(lockVersionString: String?) {
        val lockVersion: LockVersion = gson.fromJson(lockVersionString, LockVersion::class.java)
        header = ByteArray(2)
        header[0] = 0x7F
        header[1] = 0x5A
        protocol_type = lockVersion.getProtocolType()
        sub_version = lockVersion.getProtocolVersion()
        scene = lockVersion.getScene()
        organization = DigitUtil.shortToByteArray(lockVersion.getGroupId())
        sub_organization = DigitUtil.shortToByteArray(lockVersion.getOrgId())
        encrypt = APP_COMMAND
        generateLockType()
    }

    constructor(lockType: Int) {
        val lockVersion: LockVersion = LockVersion.Companion.getLockVersion(lockType)
        header = ByteArray(2)
        header[0] = 0x7F
        header[1] = 0x5A
        protocol_type = lockVersion.getProtocolType()
        sub_version = lockVersion.getProtocolVersion()
        scene = lockVersion.getScene()
        organization = DigitUtil.shortToByteArray(lockVersion.getGroupId())
        sub_organization = DigitUtil.shortToByteArray(lockVersion.getOrgId())
        encrypt = APP_COMMAND
        if (lockType == LockType.LOCK_TYPE_V2) {
            encrypt = DigitUtil.generateRandomByte()
            data = ByteArray(0)
        }
        generateLockType()
    }

    /**
     * 指令解析
     * @param command
     */
    constructor(command: ByteArray) {
        header = ByteArray(2)
        header[0] = command[0]
        header[1] = command[1]
        protocol_type = command[2]
        try {
            if (protocol_type >= 5) { //新协议
                organization = ByteArray(2)
                sub_organization = ByteArray(2)
                sub_version = command[3]
                scene = command[4]
                organization[0] = command[5]
                organization[1] = command[6]
                sub_organization[0] = command[7]
                sub_organization[1] = command[8]
                this.command = command[9]
                encrypt = command[10]
                length = command[11]
                data = ByteArray(length.toInt())
                System.arraycopy(command, 12, data, 0, length.toInt())
            } else {
                this.command = command[3]
                encrypt = command[4]
                length = command[5]
                data = ByteArray(length.toInt())
                System.arraycopy(command, 6, data, 0, length.toInt())
            }
            checksum = command[command.size - 1]
            val commandWithoutChecksum = ByteArray(command.size - 1)
            System.arraycopy(command, 0, commandWithoutChecksum, 0, commandWithoutChecksum.size)
            val checksum: Byte = CodecUtils.crccompute(commandWithoutChecksum)
            mIsChecksumValid = checksum == this.checksum
            LogUtil.d("checksum=" + checksum + " this.checksum=" + this.checksum, DBG)
            LogUtil.d("mIsChecksumValid : $mIsChecksumValid", DBG)
            generateLockType()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setCommand(command: Byte) {
        this.command = command
    }

    fun getCommand(): Byte {
        return command
    }

    fun getScene(): Byte {
        return scene
    }

    fun setScene(scene: Byte) {
        this.scene = scene
    }

    fun setData(data: ByteArray?) {
        this.data = CodecUtils.encodeWithEncrypt(data, encrypt)
        length = this.data!!.size.toByte()
    }

    fun getData(): ByteArray {
        val values: ByteArray
        values = CodecUtils.decodeWithEncrypt(data, encrypt)
        return values
    }

    fun getData(aesKeyArray: ByteArray?): ByteArray {
        val values: ByteArray
        values = AESUtil.aesDecrypt(data, aesKeyArray)
        return values
    }

    fun setData(data: ByteArray?, aesKeyArray: ByteArray?) {
        LogUtil.d("data=" + DigitUtil.byteArrayToHexString(data), DBG)
        LogUtil.d("aesKeyArray=" + DigitUtil.byteArrayToHexString(aesKeyArray), DBG)
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

    fun getLockVersionString(): String {
        return gson.toJson(getLockVersion())
    }

    fun getLockVersion(): LockVersion {
        val org: Short = DigitUtil.byteArrayToShort(organization)
        val sub_org: Short = DigitUtil.byteArrayToShort(sub_organization)
        return LockVersion(protocol_type, sub_version, scene, org, sub_org)
    }

    private fun generateLockType() {
        if (protocol_type.toInt() == 0x05 && sub_version.toInt() == 0x03 && scene.toInt() == 0x07) setLockType(
            LockType.LOCK_TYPE_V3_CAR
        ) else if (protocol_type.toInt() == 0x0a && sub_version.toInt() == 0x01) setLockType(
            LockType.LOCK_TYPE_CAR
        ) else if (protocol_type.toInt() == 0x05 && sub_version.toInt() == 0x03) setLockType(
            LockType.LOCK_TYPE_V3
        ) else if (protocol_type.toInt() == 0x05 && sub_version.toInt() == 0x04) setLockType(
            LockType.LOCK_TYPE_V2S_PLUS
        ) else if (protocol_type.toInt() == 0x05 && sub_version.toInt() == 0x01) setLockType(
            LockType.LOCK_TYPE_V2S
        ) else if (protocol_type.toInt() == 0x0b && sub_version.toInt() == 0x01) setLockType(
            LockType.LOCK_TYPE_MOBI
        ) else if (protocol_type.toInt() == 0x03) setLockType(LockType.LOCK_TYPE_V2)
    }

    fun buildCommand(): ByteArray {
        return if (protocol_type >= 0x05) {
            val commandWithoutChecksum = ByteArray(2 + 1 + 1 + 1 + 2 + 2 + 1 + 1 + 1 + length)
            commandWithoutChecksum[0] = header[0]
            commandWithoutChecksum[1] = header[1]
            commandWithoutChecksum[2] = protocol_type
            commandWithoutChecksum[3] = sub_version
            commandWithoutChecksum[4] = scene
            commandWithoutChecksum[5] = organization[0]
            commandWithoutChecksum[6] = organization[1]
            commandWithoutChecksum[7] = sub_organization[0]
            commandWithoutChecksum[8] = sub_organization[1]
            commandWithoutChecksum[9] = command
            commandWithoutChecksum[10] = encrypt
            commandWithoutChecksum[11] = length
            if (data != null && data!!.size > 0) System.arraycopy(
                data,
                0,
                commandWithoutChecksum,
                12,
                data!!.size
            )
            val commandWithChecksum = ByteArray(commandWithoutChecksum.size + 1)
            val checksumJava: Byte = CodecUtils.crccompute(commandWithoutChecksum)
            System.arraycopy(
                commandWithoutChecksum,
                0,
                commandWithChecksum,
                0,
                commandWithoutChecksum.size
            )
            commandWithChecksum[commandWithChecksum.size - 1] = checksumJava
            LogUtil.d(
                "buildCommand : " + Char(command.toUShort()) + "-" + String.format(
                    "%#x",
                    command
                ), DBG
            )
            commandWithChecksum
        } else {    //V4之前的版本使用
            val commandWithoutChecksum = ByteArray(2 + 1 + 1 + 1 + 1 + length)
            commandWithoutChecksum[0] = header[0]
            commandWithoutChecksum[1] = header[1]
            commandWithoutChecksum[2] = protocol_type
            commandWithoutChecksum[3] = command
            commandWithoutChecksum[4] = encrypt
            commandWithoutChecksum[5] = length
            if (data!!.size > 0) System.arraycopy(data, 0, commandWithoutChecksum, 6, data!!.size)
            val commandWithChecksum = ByteArray(commandWithoutChecksum.size + 1)
            val checksumJava: Byte = CodecUtils.crccompute(commandWithoutChecksum)
            System.arraycopy(
                commandWithoutChecksum,
                0,
                commandWithChecksum,
                0,
                commandWithoutChecksum.size
            )
            commandWithChecksum[commandWithChecksum.size - 1] = checksumJava
            LogUtil.d("buildCommand : " + Char(command.toUShort()), DBG)
            commandWithChecksum
        }
    }

    companion object {
        private val gson: Gson = Gson()
        private const val DBG = false
        const val COMM_INITIALIZATION = 'E'.code.toByte()
        const val COMM_GET_AES_KEY: Byte = 0x19
        const val COMM_RESPONSE = 'T'.code.toByte()

        /**
         * 添加管理
         */
        const val COMM_ADD_ADMIN = 'V'.code.toByte()

        /**
         * 校验管理员
         */
        const val COMM_CHECK_ADMIN = 'A'.code.toByte()

        /**
         * 管理员键盘密码
         */
        const val COMM_SET_ADMIN_KEYBOARD_PWD = 'S'.code.toByte()

        /**
         * 删除密码
         */
        const val COMM_SET_DELETE_PWD = 'D'.code.toByte()

        /**
         * 设置锁名称
         */
        const val COMM_SET_LOCK_NAME = 'N'.code.toByte()

        /**
         * 同步键盘密码
         */
        const val COMM_SYN_KEYBOARD_PWD = 'I'.code.toByte()

        /**
         * 校验用户时间
         */
        const val COMM_CHECK_USER_TIME = 'U'.code.toByte()

        /**
         * 获取车位锁警报记录(动了车位锁)
         * 判断添加以及密码等操作全部完成的指令
         */
        const val COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED = 'W'.code.toByte()

        /**
         * 开门
         */
        const val COMM_UNLOCK = 'G'.code.toByte()

        /**
         * 关门
         */
        const val COMM_LOCK = 'L'.code.toByte()

        /**
         * 校准时间
         */
        const val COMM_TIME_CALIBRATE = 'C'.code.toByte()

        /**
         * 管理键盘密码
         */
        const val COMM_MANAGE_KEYBOARD_PASSWORD: Byte = 0x03

        /**
         * 获取锁内有效键盘密码
         */
        const val COMM_GET_VALID_KEYBOARD_PASSWORD: Byte = 0x04

        /**
         * 获取操作记录
         */
        const val COMM_GET_OPERATE_LOG: Byte = 0x25

        /**
         * 随机数验证
         */
        const val COMM_CHECK_RANDOM: Byte = 0x30

        /**
         * 三代
         * 密码初始化
         */
        const val COMM_INIT_PASSWORDS: Byte = 0x31

        /**
         * 读取密码参数
         */
        const val COMM_READ_PWD_PARA: Byte = 0x32

        /**
         * 修改有效键盘密码数量 三代锁
         */
        const val COMM_RESET_KEYBOARD_PWD_COUNT: Byte = 0x33

        /**
         * 读取门锁时间
         */
        const val COMM_GET_LOCK_TIME: Byte = 0x34

        /**
         * 重置锁
         */
        const val COMM_RESET_LOCK = 'R'.code.toByte()

        /**
         * 查询设备特征
         */
        const val COMM_SEARCHE_DEVICE_FEATURE = 0x01.toByte()

        /**
         * IC卡管理
         */
        const val COMM_IC_MANAGE: Byte = 0x05

        /**
         * 指纹管理
         */
        const val COMM_FR_MANAGE: Byte = 0x06

        /**
         * 获取密码列表
         */
        const val COMM_PWD_LIST: Byte = 0x07

        /**
         * 设置手环KEY
         */
        const val COMM_SET_WRIST_BAND_KEY: Byte = 0x35

        /**
         * 自动闭锁管理(包含门磁)
         */
        const val COMM_AUTO_LOCK_MANAGE: Byte = 0x36

        /**
         * 读取设备信息
         */
        const val COMM_READ_DEVICE_INFO = 0x90.toByte()

        /**
         * 进入升级模式
         */
        const val COMM_ENTER_DFU_MODE: Byte = 0x02

        /**
         * 查询自行车状态(包含门磁)
         */
        const val COMM_SEARCH_BICYCLE_STATUS: Byte = 0x14

        /**
         * 闭锁
         */
        const val COMM_FUNCTION_LOCK: Byte = 0x58

        /**
         * 屏幕显示密码
         */
        const val COMM_SHOW_PASSWORD: Byte = 0x59

        /**
         * 控制远程开锁
         */
        const val COMM_CONTROL_REMOTE_UNLOCK: Byte = 0x37
        const val COMM_AUDIO_MANAGE: Byte = 0x62
        const val COMM_REMOTE_CONTROL_DEVICE_MANAGE: Byte = 0x63

        /**
         * 对于NB联网门锁，通过这条命令，App告诉门锁服务器的地址信息
         */
        const val COMM_CONFIGURE_NB_ADDRESS: Byte = 0x12

        /**
         * 酒店锁参数配置
         */
        const val COMM_CONFIGURE_HOTEL_DATA: Byte = 0x64

        /**
         * 读取管理员密码
         */
        const val COMM_GET_ADMIN_CODE: Byte = 0x65

        /**
         * 常开模式管理
         */
        const val COMM_CONFIGURE_PASSAGE_MODE: Byte = 0x66

        /**
         * 开关控制指令(隐私锁、防撬警报、重置锁)
         */
        const val COMM_SWITCH: Byte = 0x68
        const val COMM_FREEZE_LOCK: Byte = 0x61
        const val COMM_LAMP: Byte = 0x67

        /**
         * 死锁指令
         */
        const val COMM_DEAD_LOCK: Byte = 0x69

        /**
         * 循环指令
         */
        const val COMM_CYCLIC_CMD: Byte = 0x70

        /**
         * 无线钥匙管理
         */
        const val COMM_KEY_FOB_MANAG: Byte = 0x73

        /**
         * 获取配件电量
         */
        const val COMM_ACCESSORY_BATTERY: Byte = 0x74

        /**
         * 无线门磁管理
         */
        const val COMM_DOOR_SENSOR_MANAGE: Byte = 0x76

        /**
         * 设置接近感应
         * 接近感应设置，3D人脸检测或者二维码检测时，有接近感应功能，当人体或者手机等物体靠近锁时，会自动启动人脸检测或者二维码检测，此功能用于设置开启或者关闭这个感应功能，或者设置感应的距离（远、中、近）
         */
        const val COMM_SENSITIVITY_MANAGE = 0x80.toByte()

        /**
         * 此命令用于控制WiFi锁搜索周围的WiFi路由器的SSID
         */
        const val COMM_SCAN_WIFI = 0xf2.toByte()

        /**
         * 此命令用于告诉锁使用哪个WiFi路由器
         */
        const val COMM_CONFIG_WIFI_AP = 0xf3.toByte()

        /**
         * 配置服务器地址
         */
        const val COMM_CONFIG_SERVER = 0xf5.toByte()

        /**
         * 配置锁本地IP地址
         */
        const val COMM_CONFIG_STATIC_IP = 0xf4.toByte()

        /**
         * 读取锁WiFi MAC地址 信号强弱
         */
        const val COMM_GET_WIFI_INFO = 0xf6.toByte()
        const val COMM_NB_ACTIVATE_CONFIGURATION: Byte = 0x13
        const val VERSION_LOCK_V1: Byte = 1
        private const val APP_COMMAND = 0xaa.toByte() //app命令
        const val ENCRY_OLD = 1
        const val ENCRY_AES_CBC = 2
    }
}