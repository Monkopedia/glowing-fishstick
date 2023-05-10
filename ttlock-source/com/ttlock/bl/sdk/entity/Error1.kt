package com.ttlock.bl.sdk.entity

import com.ttlock.bl.sdk.entity.Error.FR_NOT_EXIST
import com.ttlock.bl.sdk.entity.Error.IC_CARD_NOT_EXIST
import com.ttlock.bl.sdk.entity.Error.INVALID_COMMAND
import com.ttlock.bl.sdk.entity.Error.INVALID_VENDOR
import com.ttlock.bl.sdk.entity.Error.LOCK_ADMIN_CHECK_ERROR
import com.ttlock.bl.sdk.entity.Error.LOCK_CRC_CHECK_ERROR
import com.ttlock.bl.sdk.entity.Error.LOCK_DYNAMIC_PWD_ERROR
import com.ttlock.bl.sdk.entity.Error.LOCK_INIT_KEYBOARD_FAILED
import com.ttlock.bl.sdk.entity.Error.LOCK_IS_IN_NO_SETTING_MODE
import com.ttlock.bl.sdk.entity.Error.LOCK_IS_IN_SETTING_MODE
import com.ttlock.bl.sdk.entity.Error.LOCK_KEY_FLAG_INVALID
import com.ttlock.bl.sdk.entity.Error.LOCK_NOT_EXIST_ADMIN
import com.ttlock.bl.sdk.entity.Error.LOCK_NO_FREE_MEMORY
import com.ttlock.bl.sdk.entity.Error.LOCK_NO_PERMISSION
import com.ttlock.bl.sdk.entity.Error.LOCK_NO_POWER
import com.ttlock.bl.sdk.entity.Error.LOCK_OPERATE_FAILED
import com.ttlock.bl.sdk.entity.Error.LOCK_PASSWORD_EXIST
import com.ttlock.bl.sdk.entity.Error.LOCK_PASSWORD_LENGTH_INVALID
import com.ttlock.bl.sdk.entity.Error.LOCK_PASSWORD_NOT_EXIST
import com.ttlock.bl.sdk.entity.Error.LOCK_REVERSE
import com.ttlock.bl.sdk.entity.Error.LOCK_SUPER_PASSWORD_IS_SAME_WITH_DELETE_PASSWORD
import com.ttlock.bl.sdk.entity.Error.LOCK_USER_NOT_LOGIN
import com.ttlock.bl.sdk.entity.Error.LOCK_USER_TIME_EXPIRED
import com.ttlock.bl.sdk.entity.Error.LOCK_USER_TIME_INEFFECTIVE
import com.ttlock.bl.sdk.entity.Error.RECORD_NOT_EXIST
import com.ttlock.bl.sdk.entity.Error.SUCCESS

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
import java.util.UUID
import com.ttlock.bl.sdk.callback.EnterDfuModeCallback
import com.ttlock.bl.sdk.api.BluetoothImpl
import com.ttlock.bl.sdk.entity.TransferData
import com.ttlock.bl.sdk.api.CommandUtil_Va
import com.ttlock.bl.sdk.api.CommandUtil_V2S
import com.ttlock.bl.sdk.api.CommandUtil_V2S_PLUS
import com.ttlock.bl.sdk.api.CommandUtil_V3
import java.util.TimeZone
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
import java.util.LinkedList
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
import java.util.TimerTask
import com.ttlock.bl.sdk.callback.OnScanFailedListener
import com.ttlock.bl.sdk.scanner.IScanCallback
import com.ttlock.bl.sdk.constant.CommandResponse
import java.util.Calendar
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
import java.util.LinkedHashSet
import java.util.TreeSet
import com.ttlock.bl.sdk.constant.LockDataSwitchValue
import com.ttlock.bl.sdk.callback.GetLightTimeCallback
import com.ttlock.bl.sdk.entity.NBAwakeTimeType
import com.ttlock.bl.sdk.api.LockDfuClient
import com.ttlock.bl.sdk.constant.RemoteControlManage
import com.ttlock.bl.sdk.device.TTDevice
import com.ttlock.bl.sdk.api.WirelessKeypadSDKApi
import com.ttlock.bl.sdk.callback.InitKeypadCallback
import com.ttlock.bl.sdk.api.WirelessKeypadClient
import java.util.Locale
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
import java.util.HashMap
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
 * Created by Administrator on 2016/7/21 0021.
 */
@Deprecated("")
enum class Error(
    private val errorCode: Int,
    private val description: String,
    private val errorMsg: String
) {
    SUCCESS(0, "success", "success"),

    /**
     * lock error code
     */
    LOCK_CRC_CHECK_ERROR(0x01, "CRC error", "CRC error"),  //CRC校验出错
    LOCK_NO_PERMISSION(
        0x02,
        "Not administrator, has no permission.",
        "Not administrator, has no permission."
    ),  //非管理员没有操作权限
    LOCK_ADMIN_CHECK_ERROR(
        0x03,
        "Wrong administrator password.",
        "Wrong administrator password."
    ),  //管理员校验出错
    LOCK_IS_IN_SETTING_MODE(0x05, "lock is in setting mode", "lock is in setting mode"),  //当前处于设置状态
    LOCK_NOT_EXIST_ADMIN(
        0x06,
        "lock has no administrator",
        "lock has no administrator"
    ),  //锁中不存在管理员
    LOCK_IS_IN_NO_SETTING_MODE(0x07, "Non-setting mode", "Non-setting mode"),  //添加管理员处于非设置模式
    LOCK_DYNAMIC_PWD_ERROR(0x08, "invalid dynamic code", "invalid dynamic code"),  //动态码错误
    LOCK_NO_POWER(0x0a, "run out of battery", "run out of battery"), LOCK_INIT_KEYBOARD_FAILED(
        0x0b,
        "initialize keyboard password falied",
        "initialize keyboard password falied"
    ),  //初始化(重置)键盘密码出错
    LOCK_KEY_FLAG_INVALID(
        0x0d,
        "invalid ekey, lock flag position is low",
        "invalid ekey, lock flag position is low"
    ),  //电子钥匙失效flag过低
    LOCK_USER_TIME_EXPIRED(0x0e, "ekey expired", "ekey expired"),  //电子钥匙过期
    LOCK_PASSWORD_LENGTH_INVALID(
        0x0f,
        "invalid password length",
        "invalid password length"
    ),
    LOCK_SUPER_PASSWORD_IS_SAME_WITH_DELETE_PASSWORD(
        0x10,
        "admin super password is same with delete password",
        "admin super password is same with delete password"
    ),  //管理员密码与删除密码相同
    LOCK_USER_TIME_INEFFECTIVE(
        0x11,
        "ekey hasn't become effective",
        "ekey hasn't become effective"
    ),  //电子钥匙尚未生效
    LOCK_USER_NOT_LOGIN(0x12, "user not login", "user not login"),  //未登录,无操作权限
    LOCK_OPERATE_FAILED(
        0x13,
        "Failed. Undefined error.",
        "Failed. Undefined error."
    ),
    LOCK_PASSWORD_EXIST(0x14, "password already exists.", "password already exists."),  //添加的密码已经存在
    LOCK_PASSWORD_NOT_EXIST(0x15, "password not exists.", "password not exists."),  //删除或者修改的密码不存在
    LOCK_NO_FREE_MEMORY(0x16, "out of memory", "out of memory"),  //存储空间不足(比如添加密码时，超过存储容量)
    IC_CARD_NOT_EXIST(0x18, "Card number not exist.", "Card number not exist."), FR_NOT_EXIST(
        0x1a,
        "Finger print not exist.",
        "Finger print not exist."
    ),
    INVALID_COMMAND(0x1b, "Invalid command", "Invalid command"), INVALID_VENDOR(
        0x1d,
        "invalid vendor string",
        "invalid vendor string"
    ),  //无效特殊字符串,客户使用专业字符串
    LOCK_REVERSE(0x1e, "", ""),  //门反锁了，普通用户不允许开锁
    RECORD_NOT_EXIST(0x1f, "record not exist", "record not exist"),

    /**
     * customized error code
     */
    AES_PARSE_ERROR(0x30, "aes parse error", "aes parse error"), KEY_INVALID(
        0x31,
        "key invalid, may be reset",
        "key invalid, may be reset"
    ),  //钥匙无效(锁可能被重置),解密失败
    LOCK_NOT_SUPPORT_CHANGE_PASSCODE(
        0x60,
        "the lock doesn't support to modify password.",
        "the lock doesn't support to modify password."
    );

    private var lockname: String? = null
    private var lockmac: String? = null
    private var command: Byte = 0

    /**
     * error time
     */
    private var date: Long = 0
    fun getLockname(): String? {
        return lockname
    }

    fun setLockname(lockname: String?) {
        this.lockname = lockname
    }

    fun getLockmac(): String? {
        return lockmac
    }

    fun setLockmac(lockmac: String?) {
        this.lockmac = lockmac
    }

    fun getCommand(): String {
        return if (command >= 'A'.code.toByte() && command <= 'Z'.code.toByte()) Char(command.toUShort()).toString() else String.format(
            "%#x",
            command
        )
    }

    fun setCommand(command: Byte) {
        this.command = command
    }

    fun getDate(): Long {
        return date
    }

    fun setDate(date: Long) {
        this.date = date
    }

    fun getErrorMsg(): String {
        return errorMsg
    }

    //    public void setErrorMsg(String errorMsg) {
    //        this.errorMsg = errorMsg;
    //    }
    fun getDescription(): String {
        return description
    }

    //    public void setDescription(String description) {
    //        this.description = description;
    //    }
    fun getErrorCode(): String {
        return String.format("%#x", errorCode)
    } //    public void setErrorCode(int errorCode) {

    //        this.errorCode = errorCode;
    //    }
    companion object {
        fun getInstance(errorCode: Int): com.ttlock.bl.sdk.entity.Error? {
            when (errorCode) {
                0 -> return com.ttlock.bl.sdk.entity.Error.SUCCESS
                0x01 -> return com.ttlock.bl.sdk.entity.Error.LOCK_CRC_CHECK_ERROR
                0x02 -> return com.ttlock.bl.sdk.entity.Error.LOCK_NO_PERMISSION
                0x03 -> return com.ttlock.bl.sdk.entity.Error.LOCK_ADMIN_CHECK_ERROR
                0x05 -> return com.ttlock.bl.sdk.entity.Error.LOCK_IS_IN_SETTING_MODE
                0x06 -> return com.ttlock.bl.sdk.entity.Error.LOCK_NOT_EXIST_ADMIN
                0x07 -> return com.ttlock.bl.sdk.entity.Error.LOCK_IS_IN_NO_SETTING_MODE
                0x08 -> return com.ttlock.bl.sdk.entity.Error.LOCK_DYNAMIC_PWD_ERROR
                0x0a -> return com.ttlock.bl.sdk.entity.Error.LOCK_NO_POWER
                0x0b -> return com.ttlock.bl.sdk.entity.Error.LOCK_INIT_KEYBOARD_FAILED
                0x0d -> return com.ttlock.bl.sdk.entity.Error.LOCK_KEY_FLAG_INVALID
                0x0e -> return com.ttlock.bl.sdk.entity.Error.LOCK_USER_TIME_EXPIRED
                0x0f -> return com.ttlock.bl.sdk.entity.Error.LOCK_PASSWORD_LENGTH_INVALID
                0x10 -> return com.ttlock.bl.sdk.entity.Error.LOCK_SUPER_PASSWORD_IS_SAME_WITH_DELETE_PASSWORD
                0x11 -> return com.ttlock.bl.sdk.entity.Error.LOCK_USER_TIME_INEFFECTIVE
                0x12 -> return com.ttlock.bl.sdk.entity.Error.LOCK_USER_NOT_LOGIN
                0x13 -> return com.ttlock.bl.sdk.entity.Error.LOCK_OPERATE_FAILED
                0x14 -> return com.ttlock.bl.sdk.entity.Error.LOCK_PASSWORD_EXIST
                0x15 -> return com.ttlock.bl.sdk.entity.Error.LOCK_PASSWORD_NOT_EXIST
                0x16 -> return com.ttlock.bl.sdk.entity.Error.LOCK_NO_FREE_MEMORY
                0x18 -> return com.ttlock.bl.sdk.entity.Error.IC_CARD_NOT_EXIST
                0x1a -> return com.ttlock.bl.sdk.entity.Error.FR_NOT_EXIST
                0x1b -> return com.ttlock.bl.sdk.entity.Error.INVALID_COMMAND
                0x1d -> return com.ttlock.bl.sdk.entity.Error.INVALID_VENDOR
                0x1e -> return com.ttlock.bl.sdk.entity.Error.LOCK_REVERSE
                0x1f -> return com.ttlock.bl.sdk.entity.Error.RECORD_NOT_EXIST
            }
            return null
        }
    }
}