package com.ttlock.bl.sdk.api

import java.util.*

import com.ttlock.bl.sdk.util.DigitUtil
import com.ttlock.bl.sdk.entity.LockVersion
import com.ttlock.bl.sdk.constant.LockType
import com.scaf.android.client.CodecUtils
import com.ttlock.bl.sdk.util.LogUtil
import com.ttlock.bl.sdk.util.AESUtil
import com.ttlock.bl.sdk.telink.ble.Device
import com.ttlock.bl.sdk.callback.GetLockSystemInfoCallback
import java.lang.Runnable
import com.ttlock.bl.sdk.api.TTLockClient
import com.ttlock.bl.sdk.util.FeatureValueUtil
import com.ttlock.bl.sdk.constant.FeatureValue
import com.ttlock.file.FileProviderPath
import com.ttlock.bl.sdk.service.DfuService
import com.ttlock.bl.sdk.util.NetworkUtil
import com.ttlock.bl.sdk.entity.LockUpdateInfo
import com.ttlock.bl.sdk.util.GsonUtil
import com.ttlock.bl.sdk.constant.LogType
import com.ttlock.bl.sdk.callback.GetOperationLogCallback
import com.ttlock.bl.sdk.entity.LockError
import com.ttlock.bl.sdk.callback.SetLockTimeCallback
import com.ttlock.bl.sdk.net.ResponseService
import com.ttlock.bl.sdk.callback.RecoverLockDataCallback
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import com.ttlock.bl.sdk.callback.ScanLockCallback
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice
import com.ttlock.bl.sdk.telink.ble.Device.DeviceStateCallback
import com.ttlock.bl.sdk.telink.util.TelinkLog
import com.ttlock.bl.sdk.callback.EnterDfuModeCallback
import com.ttlock.bl.sdk.api.BluetoothImpl
import com.ttlock.bl.sdk.entity.TransferData
import com.ttlock.bl.sdk.api.CommandUtil_Va
import com.ttlock.bl.sdk.api.CommandUtil_V2S
import com.ttlock.bl.sdk.api.CommandUtil_V2S_PLUS
import com.ttlock.bl.sdk.api.CommandUtil_V3
import com.ttlock.bl.sdk.constant.APICommand
import com.ttlock.bl.sdk.api.CommandUtil_V2
import com.ttlock.bl.sdk.constant.ICOperate
import com.ttlock.bl.sdk.constant.AutoLockOperate
import com.ttlock.bl.sdk.constant.DeviceInfoType
import com.ttlock.bl.sdk.device.WirelessKeypad
import com.ttlock.bl.sdk.api.WirelessKeyboardCommand
import com.ttlock.bl.sdk.entity.IpSetting
import com.ttlock.bl.sdk.callback.ScanKeypadCallback
import android.bluetooth.le.ScanCallback
import com.ttlock.bl.sdk.callback.LockCallback
import com.ttlock.bl.sdk.api.LockCallbackManager
import com.ttlock.bl.sdk.entity.HotelData
import com.ttlock.bl.sdk.entity.PowerSaverWorkMode
import com.ttlock.bl.sdk.api.DataParseUitl
import com.ttlock.bl.sdk.callback.GetPowerSaverWorkModesCallback
import com.ttlock.bl.sdk.api.TTLockSDKApi
import com.ttlock.bl.sdk.callback.InitLockCallback
import com.ttlock.bl.sdk.callback.ConnectLockCallback
import com.ttlock.bl.sdk.entity.LockData
import com.ttlock.bl.sdk.api.EncryptionUtil
import com.ttlock.bl.sdk.callback.ResetKeyCallback
import com.ttlock.bl.sdk.callback.ResetLockCallback
import com.ttlock.bl.sdk.callback.ControlLockCallback
import com.ttlock.bl.sdk.constant.ControlAction
import com.ttlock.bl.sdk.callback.GetLockMuteModeStateCallback
import com.ttlock.bl.sdk.callback.SetLockMuteModeCallback
import com.ttlock.bl.sdk.callback.SetRemoteUnlockSwitchCallback
import com.ttlock.bl.sdk.callback.GetRemoteUnlockStateCallback
import com.ttlock.bl.sdk.callback.GetLockTimeCallback
import com.ttlock.bl.sdk.callback.GetBatteryLevelCallback
import com.ttlock.bl.sdk.callback.GetLockVersionCallback
import com.ttlock.bl.sdk.callback.GetSpecialValueCallback
import com.ttlock.bl.sdk.callback.GetLockStatusCallback
import com.ttlock.bl.sdk.callback.SetAutoLockingPeriodCallback
import com.ttlock.bl.sdk.callback.GetAutoLockingPeriodCallback
import com.ttlock.bl.sdk.callback.CreateCustomPasscodeCallback
import com.ttlock.bl.sdk.callback.ModifyPasscodeCallback
import com.ttlock.bl.sdk.callback.DeletePasscodeCallback
import com.ttlock.bl.sdk.callback.ResetPasscodeCallback
import com.ttlock.bl.sdk.callback.GetAllValidPasscodeCallback
import com.ttlock.bl.sdk.callback.GetPasscodeVerificationInfoCallback
import com.ttlock.bl.sdk.callback.GetAdminPasscodeCallback
import com.ttlock.bl.sdk.callback.ModifyAdminPasscodeCallback
import com.ttlock.bl.sdk.callback.AddICCardCallback
import com.ttlock.bl.sdk.entity.ValidityInfo
import com.ttlock.bl.sdk.callback.ModifyICCardPeriodCallback
import com.ttlock.bl.sdk.callback.GetAllValidICCardCallback
import com.ttlock.bl.sdk.callback.DeleteICCardCallback
import com.ttlock.bl.sdk.callback.ReportLossCardCallback
import com.ttlock.bl.sdk.callback.ClearAllICCardCallback
import com.ttlock.bl.sdk.callback.AddFingerprintCallback
import com.ttlock.bl.sdk.callback.GetAllValidFingerprintCallback
import com.ttlock.bl.sdk.callback.DeleteFingerprintCallback
import com.ttlock.bl.sdk.callback.ClearAllFingerprintCallback
import com.ttlock.bl.sdk.callback.ModifyFingerprintPeriodCallback
import com.ttlock.bl.sdk.entity.TTLockConfigType
import com.ttlock.bl.sdk.callback.SetLockConfigCallback
import com.ttlock.bl.sdk.callback.GetLockConfigCallback
import com.ttlock.bl.sdk.callback.WriteFingerprintDataCallback
import com.ttlock.bl.sdk.callback.GetPasscodeVisibleStateCallback
import com.ttlock.bl.sdk.callback.SetPasscodeVisibleCallback
import com.ttlock.bl.sdk.callback.SetNBServerCallback
import java.lang.IllegalStateException
import com.ttlock.bl.sdk.callback.GetPassageModeCallback
import com.ttlock.bl.sdk.entity.PassageModeConfig
import com.ttlock.bl.sdk.callback.SetPassageModeCallback
import com.ttlock.bl.sdk.callback.DeletePassageModeCallback
import com.ttlock.bl.sdk.callback.ClearPassageModeCallback
import com.ttlock.bl.sdk.callback.SetLockFreezeStateCallback
import com.ttlock.bl.sdk.callback.GetLockFreezeStateCallback
import com.ttlock.bl.sdk.callback.SetLightTimeCallback
import com.ttlock.bl.sdk.callback.SetHotelCardSectorCallback
import com.ttlock.bl.sdk.callback.SetHotelDataCallback
import com.ttlock.bl.sdk.entity.HotelInfo
import com.ttlock.bl.sdk.callback.SetLiftControlableFloorsCallback
import com.ttlock.bl.sdk.entity.TTLiftWorkMode
import com.ttlock.bl.sdk.callback.SetLiftWorkModeCallback
import com.ttlock.bl.sdk.callback.ActivateLiftFloorsCallback
import com.ttlock.bl.sdk.entity.NBAwakeMode
import com.ttlock.bl.sdk.callback.SetNBAwakeModesCallback
import com.ttlock.bl.sdk.entity.NBAwakeConfig
import com.ttlock.bl.sdk.callback.GetNBAwakeModesCallback
import com.ttlock.bl.sdk.entity.NBAwakeTime
import com.ttlock.bl.sdk.callback.SetNBAwakeTimesCallback
import com.ttlock.bl.sdk.callback.GetNBAwakeTimesCallback
import com.ttlock.bl.sdk.callback.SetPowerSaverWorkModeCallback
import com.ttlock.bl.sdk.callback.SetPowerSaverControlableLockCallback
import com.ttlock.bl.sdk.callback.GetUnlockDirectionCallback
import com.ttlock.bl.sdk.entity.UnlockDirection
import com.ttlock.bl.sdk.callback.SetUnlockDirectionCallback
import com.ttlock.bl.sdk.entity.AccessoryInfo
import com.ttlock.bl.sdk.callback.GetAccessoryBatteryLevelCallback
import com.ttlock.bl.sdk.callback.AddRemoteCallback
import com.ttlock.bl.sdk.callback.ModifyRemoteValidityPeriodCallback
import com.ttlock.bl.sdk.callback.DeleteRemoteCallback
import com.ttlock.bl.sdk.callback.ClearRemoteCallback
import com.ttlock.bl.sdk.callback.ScanWifiCallback
import com.ttlock.bl.sdk.callback.ConfigWifiCallback
import com.ttlock.bl.sdk.callback.ConfigServerCallback
import com.ttlock.bl.sdk.callback.GetWifiInfoCallback
import com.ttlock.bl.sdk.entity.SoundVolume
import com.ttlock.bl.sdk.callback.SetLockSoundWithSoundVolumeCallback
import com.ttlock.bl.sdk.callback.GetLockSoundWithSoundVolumeCallback
import com.ttlock.bl.sdk.callback.AddDoorSensorCallback
import com.ttlock.bl.sdk.callback.DeleteDoorSensorCallback
import com.ttlock.bl.sdk.api.TTLockSdkApiBase
import com.ttlock.bl.sdk.constant.ActionType
import com.ttlock.bl.sdk.entity.PassageModeType
import com.ttlock.bl.sdk.executor.AppExecutors
import com.ttlock.bl.sdk.scanner.ScannerCompat
import com.ttlock.bl.sdk.api.BluetoothImpl.ScanCallback
import com.ttlock.bl.sdk.constant.LogOperate
import com.ttlock.bl.sdk.constant.RecoveryData
import com.ttlock.bl.sdk.entity.ICCard
import com.ttlock.bl.sdk.entity.FR
import com.ttlock.bl.sdk.entity.Passcode
import java.util.concurrent.locks.ReentrantLock
import com.ttlock.bl.sdk.api.PassageModeData
import com.ttlock.bl.sdk.entity.CyclicConfig
import java.lang.StringBuilder
import com.ttlock.bl.sdk.gateway.model.WiFi
import com.ttlock.bl.sdk.callback.OnScanFailedListener
import com.ttlock.bl.sdk.scanner.IScanCallback
import com.ttlock.bl.sdk.constant.CommandResponse
import com.ttlock.bl.sdk.entity.ControlLockResult
import com.ttlock.bl.sdk.entity.KeyboardPwd
import com.ttlock.bl.sdk.constant.PwdOperateType
import com.ttlock.bl.sdk.constant.KeyboardPwdType
import com.ttlock.bl.sdk.constant.ConfigRemoteUnlock
import com.ttlock.bl.sdk.entity.ActivateLiftFloorsResult
import com.ttlock.bl.sdk.constant.AudioManage
import com.ttlock.bl.sdk.entity.PwdInfoV3
import com.ttlock.bl.sdk.api.ResponseUtil
import com.ttlock.bl.sdk.constant.PassageModeOperate
import com.ttlock.bl.sdk.constant.CyclicOpType
import com.ttlock.bl.sdk.entity.AccessoryType
import com.ttlock.bl.sdk.constant.KeyFobOperationType
import com.ttlock.bl.sdk.constant.SensitivityOperationType
import com.ttlock.bl.sdk.entity.WifiLockInfo
import kotlin.jvm.Synchronized
import com.ttlock.bl.sdk.constant.LockDataSwitchValue
import com.ttlock.bl.sdk.callback.GetLightTimeCallback
import com.ttlock.bl.sdk.entity.NBAwakeTimeType
import com.ttlock.bl.sdk.api.LockDfuClient
import com.ttlock.bl.sdk.constant.RemoteControlManage
import com.ttlock.bl.sdk.device.TTDevice
import com.ttlock.bl.sdk.api.WirelessKeypadSDKApi
import com.ttlock.bl.sdk.callback.InitKeypadCallback
import com.ttlock.bl.sdk.api.WirelessKeypadClient
import com.ttlock.bl.sdk.gateway.model.GatewayType
import com.ttlock.bl.sdk.entity.Scene
import kotlin.Throws
import com.ttlock.bl.sdk.api.ParamInvalidException
import kotlin.jvm.JvmOverloads
import com.ttlock.bl.sdk.base.BaseSDKApi
import com.ttlock.bl.sdk.constant.BleConstant
import com.ttlock.bl.sdk.base.BaseScanCallback
import com.ttlock.bl.sdk.device.WirelessDoorSensor
import com.ttlock.bl.sdk.base.BaseScanManager
import com.ttlock.bl.sdk.entity.FirmwareInfo
import com.ttlock.bl.sdk.base.BaseGattCallbackHelper
import java.text.SimpleDateFormat
import com.ttlock.bl.sdk.entity.OperateLogType
import java.util.concurrent.Executor
import com.ttlock.bl.sdk.executor.DiskIOThreadExecutor
import java.util.concurrent.Executors
import com.ttlock.bl.sdk.executor.AppExecutors.MainThreadExecutor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger
import com.ttlock.bl.sdk.executor.NamedThreadFactory
import java.util.concurrent.ExecutorService
import com.ttlock.bl.sdk.gateway.callback.ScanGatewayCallback
import com.ttlock.bl.sdk.gateway.api.GatewayClient
import com.ttlock.bl.sdk.gateway.model.GatewayError
import com.ttlock.bl.sdk.gateway.model.GatewayUpdateInfo
import com.ttlock.bl.sdk.gateway.api.GatewaySDKApi
import com.ttlock.bl.sdk.gateway.model.ConfigureGatewayInfo
import com.ttlock.bl.sdk.gateway.callback.InitGatewayCallback
import com.ttlock.bl.sdk.gateway.api.GatewayCallbackManager
import com.ttlock.bl.sdk.gateway.callback.ScanWiFiByGatewayCallback
import com.ttlock.bl.sdk.gateway.callback.GetNetworkMacCallback
import java.io.UnsupportedEncodingException
import com.ttlock.bl.sdk.gateway.util.GatewayUtil
import com.ttlock.bl.sdk.gateway.callback.GatewayCallback
import com.ttlock.bl.sdk.gateway.api.GatewayDfuClient
import java.lang.AssertionError
import java.util.concurrent.TimeUnit
import com.ttlock.bl.sdk.net.OkHttpRequest
import com.ttlock.bl.sdk.remote.callback.ScanRemoteCallback
import com.ttlock.bl.sdk.remote.api.WirelessKeyFobSDKApi
import com.ttlock.bl.sdk.remote.api.RemoteClient
import com.ttlock.bl.sdk.remote.api.RemoteCallbackManager
import com.ttlock.bl.sdk.remote.callback.InitRemoteCallback
import com.ttlock.bl.sdk.remote.model.RemoteError
import com.ttlock.bl.sdk.remote.callback.GetRemoteSystemInfoCallback
import com.ttlock.bl.sdk.remote.callback.RemoteCallback
import com.ttlock.bl.sdk.remote.model.SystemInfo
import com.ttlock.bl.sdk.remote.model.InitRemoteResult
import com.ttlock.bl.sdk.scanner.ScannerLollipop
import com.ttlock.bl.sdk.scanner.ScannerImplJB
import com.ttlock.bl.sdk.scanner.ScannerLollipop.ScanCallbackImpl
import java.lang.InterruptedException
import com.ttlock.bl.sdk.telink.ble.Peripheral
import com.ttlock.bl.sdk.telink.ble.OtaPacketParser
import com.ttlock.bl.sdk.telink.ble.Device.OtaCommandCallback
import com.ttlock.bl.sdk.telink.ble.Device.CharacteristicCommandCallback
import com.ttlock.bl.sdk.telink.ble.Device.GattOperationCallback
import com.ttlock.bl.sdk.telink.ble.Device.DescriptorCallback
import com.ttlock.bl.sdk.telink.ble.OtaError
import com.ttlock.bl.sdk.telink.ble.Command.CommandType
import com.ttlock.bl.sdk.telink.ble.AdvDevice
import com.ttlock.bl.sdk.telink.ble.Peripheral.CommandContext
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentHashMap
import com.ttlock.bl.sdk.telink.ble.Peripheral.RssiUpdateRunnable
import com.ttlock.bl.sdk.telink.ble.Peripheral.CommandTimeoutRunnable
import com.ttlock.bl.sdk.telink.ble.Peripheral.CommandDelayRunnable
import com.ttlock.bl.sdk.telink.ble.BleNamesResolver
import com.ttlock.bl.sdk.telink.ble.PropertyResolver
import java.nio.charset.Charset
import java.io.FileInputStream
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import java.lang.StackTraceElement
import java.lang.StringBuffer
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.math.BigInteger
import com.ttlock.bl.sdk.wirelessdoorsensor.model.DoorSensorError
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.DoorSensorCallback
import com.ttlock.bl.sdk.wirelessdoorsensor.model.InitDoorSensorResult
import com.ttlock.bl.sdk.wirelessdoorsensor.WirelessDoorSensorSDKApi
import com.ttlock.bl.sdk.wirelessdoorsensor.DoorSensorCallbackManager
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.InitDoorSensorCallback
import com.ttlock.bl.sdk.base.BaseClient
import com.ttlock.bl.sdk.wirelessdoorsensor.WirelessDoorSensorClient
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.ScanWirelessDoorSensorCallback

/**
 * Created on  2019/6/14 0014 16:04
 *
 * @author theodore_hu
 */
internal class WirelessKeyboardCommand {
    var header // 帧首 		2 字节
            : ByteArray
    var command // 命令字 	1 字节
            : Byte
    var length // 长度		1 字节
            : Byte
    var data // 数据
            : ByteArray?
    var checksum // 校验		1 字节
            : Byte = 0
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

    fun setCommand(command: Byte) {
        this.command = command
    }

    fun getCommand(): Byte {
        return command
    }

    fun getData(): ByteArray {
        return getData(getAeskey())
    }

    fun getAeskey(): ByteArray {
        var macArr: Array<String?>? = mac!!.split(":").toTypedArray()
        macArr = reverseArray(macArr)
        val macBytes = hexStringArrToByteArr(macArr)
        return AESUtil.aesEncrypt(macBytes, WirelessKeyboardCommand.Companion.defaultAeskey)
    }

    fun getData(aesKeyArray: ByteArray?): ByteArray {
        val values: ByteArray
        values = AESUtil.aesDecrypt(data, aesKeyArray)
        return values
    }

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
        LogUtil.d(
            "data=" + DigitUtil.byteArrayToHexString(data),
            WirelessKeyboardCommand.Companion.DBG
        )
        LogUtil.d(
            "aesKeyArray=" + DigitUtil.byteArrayToHexString(aesKeyArray),
            WirelessKeyboardCommand.Companion.DBG
        )
        this.data = AESUtil.aesEncrypt(data, aesKeyArray)
        length = this.data.size.toByte()
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
        val commandBytes = ByteArray(2 + 7 + 1 + 1 + +1 + length + 1)
        commandBytes[0] = header[0]
        commandBytes[1] = header[1]

        //跟门锁保持一致 版本信息 固定数据
        commandBytes[2] = 0x05
        commandBytes[3] = 0x03
        commandBytes[4] = 0x02
        commandBytes[5] = 0x00
        commandBytes[6] = 0x01
        commandBytes[7] = 0x00
        commandBytes[8] = 0x01
        commandBytes[9] = command

        //跟门锁一致 固定填充
        commandBytes[10] = 0x55.toByte()
        commandBytes[11] = length
        if (data != null && data!!.size > 0) System.arraycopy(
            data,
            0,
            commandBytes,
            12,
            data!!.size
        )
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