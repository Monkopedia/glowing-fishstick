package com.ttlock.bl.sdk.constant

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
 * Created by Administrator on 2016/6/22 0022.
 */
class LogOperate {
    /**
     * 记录类型
     */
    private var recordType = 0

    /**
     * 锁中记录的唯一标识
     */
    private var recordId = 0

    /**
     * 用户id
     */
    private var uid = 0

    /**
     * 密码
     */
    private var password: String? = null

    /**
     * 修改后的密码
     */
    private var newPassword: String? = null

    /**
     * 操作时间
     */
    private var operateDate: Long = 0

    /**
     * 删除密码的日期
     */
    private var deleteDate: Long = 0

    /**
     * 锁电量
     */
    private var electricQuantity = 0

    /**
     * 配件电量
     */
    private var accessoryElectricQuantity = 0

    /**
     * 遥控按键ID或按键值(1 – 开锁  2  - 闭锁)
     *
     */
    private var keyId = 0
    fun getRecordType(): Int {
        return recordType
    }

    fun setRecordType(recordType: Int) {
        this.recordType = recordType
    }

    fun getRecordId(): Int {
        return recordId
    }

    fun setRecordId(recordId: Int) {
        this.recordId = recordId
    }

    fun getUid(): Int {
        return uid
    }

    fun setUid(uid: Int) {
        this.uid = uid
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    fun getNewPassword(): String? {
        return newPassword
    }

    fun setNewPassword(newPassword: String?) {
        this.newPassword = newPassword
    }

    fun getOperateDate(): Long {
        return operateDate
    }

    fun setOperateDate(operateDate: Long) {
        this.operateDate = operateDate
    }

    fun getDeleteDate(): Long {
        return deleteDate
    }

    fun setDeleteDate(deleteDate: Long) {
        this.deleteDate = deleteDate
    }

    fun getElectricQuantity(): Int {
        return electricQuantity
    }

    fun setElectricQuantity(electricQuantity: Int) {
        this.electricQuantity = electricQuantity
    }

    fun getKeyId(): Int {
        return keyId
    }

    fun setKeyId(keyId: Int) {
        this.keyId = keyId
    }

    fun getAccessoryElectricQuantity(): Int {
        return accessoryElectricQuantity
    }

    fun setAccessoryElectricQuantity(accessoryElectricQuantity: Int) {
        this.accessoryElectricQuantity = accessoryElectricQuantity
    }

    override fun toString(): String {
        return "LogOperate{" +
                "recordType=" + recordType +
                ", recordId=" + recordId +
                ", uid=" + uid +
                ", password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", operateDate=" + operateDate +
                ", deleteDate=" + deleteDate +
                ", electricQuantity=" + electricQuantity +
                ", keyId=" + keyId +
                '}'
    }

    companion object {
        //手机开锁
        /**
         * 蓝牙开锁
         */
        const val OPERATE_TYPE_MOBILE_UNLOCK: Byte = 1

        //    //服务器开锁
        //    public static final byte OPERATE_TYPE_SERVER_UNLOCK = 3;
        //密码开锁
        const val OPERATE_TYPE_KEYBOARD_PASSWORD_UNLOCK: Byte = 4

        //键盘上修改密码
        const val OPERATE_TYPE_KEYBOARD_MODIFY_PASSWORD: Byte = 5

        //键盘上删除单个密码
        const val OPERATE_TYPE_KEYBOARD_REMOVE_SINGLE_PASSWORD: Byte = 6

        //错误密码开锁
        const val OPERATE_TYPE_ERROR_PASSWORD_UNLOCK: Byte = 7

        //键盘上删除所有密码
        const val OPERATE_TYPE_KEYBOARD_REMOVE_ALL_PASSWORDS: Byte = 8

        //密码被挤掉
        const val OPERATE_TYPE_KEYBOARD_PASSWORD_KICKED: Byte = 9

        /**
         * 带删除功能密码第一次开锁，之前密码被清空
         */
        const val OPERATE_TYPE_USE_DELETE_CODE: Byte = 10

        /**
         * 密码过期
         */
        const val OPERATE_TYPE_PASSCODE_EXPIRED: Byte = 11

        /**
         * 密码开锁失败，存储容量不足
         */
        const val OPERATE_TYPE_SPACE_INSUFFICIENT: Byte = 12

        /**
         * 密码开锁失败—密码在黑名单
         */
        const val OPERATE_TYPE_PASSCODE_IN_BLACK_LIST: Byte = 13

        /**
         * 门锁重新上电启动了（也就是重新上电池了）
         */
        const val OPERATE_TYPE_DOOR_REBOOT: Byte = 14

        /**
         * 添加IC卡成功
         */
        const val OPERATE_TYPE_ADD_IC: Byte = 15

        /**
         * 清空IC卡成功
         */
        const val OPERATE_TYPE_CLEAR_IC_SUCCEED: Byte = 16

        /**
         * IC卡开门成功
         */
        const val OPERATE_TYPE_IC_UNLOCK_SUCCEED: Byte = 17

        /**
         * 删除单个IC卡成功
         */
        const val OPERATE_TYPE_DELETE_IC_SUCCEED: Byte = 18

        /**
         * Bong手环开门成功
         */
        const val OPERATE_TYPE_BONG_UNLOCK_SUCCEED: Byte = 19

        /**
         * 指纹开门成功
         */
        const val OPERATE_TYPE_FR_UNLOCK_SUCCEED: Byte = 20

        /**
         * 指纹添加成功
         */
        const val OPERATE_TYPE_ADD_FR: Byte = 21

        /**
         * 指纹开门失败
         */
        const val OPERATE_TYPE_FR_UNLOCK_FAILED: Byte = 22

        /**
         * 删除单个指纹成功
         */
        const val OPERATE_TYPE_DELETE_FR_SUCCEED: Byte = 23

        /**
         * 清空指纹成功
         */
        const val OPERATE_TYPE_CLEAR_FR_SUCCEED: Byte = 24

        /**
         * IC卡开门失败-已过期或未生效
         */
        const val OPERATE_TYPE_IC_UNLOCK_FAILED: Byte = 25

        /**
         * 蓝牙或网关闭锁
         */
        const val OPERATE_BLE_LOCK: Byte = 26

        /**
         * 机械钥匙开锁
         */
        const val OPERATE_KEY_UNLOCK: Byte = 27

        /**
         * 网关开锁
         */
        const val GATEWAY_UNLOCK: Byte = 28

        /**
         * 非法开锁(比如脚踏)
         */
        const val ILLAGEL_UNLOCK: Byte = 29

        /**
         * 门磁合上
         */
        const val DOOR_SENSOR_LOCK: Byte = 30

        /**
         * 门磁打开
         */
        const val DOOR_SENSOR_UNLOCK: Byte = 31

        /**
         * 出门记录
         */
        const val DOOR_GO_OUT: Byte = 32

        /**
         * 指纹关锁
         */
        const val FR_LOCK: Byte = 33

        /**
         * 密码关锁
         */
        const val PASSCODE_LOCK: Byte = 34
        const val IC_LOCK: Byte = 35

        /**
         * 机械钥匙关锁
         */
        const val OPERATE_KEY_LOCK: Byte = 36

        /**
         * 遥控按键
         */
        const val REMOTE_CONTROL_KEY: Byte = 37

        /**
         * 密码开锁失败，门反锁
         */
        const val PASSCODE_UNLOCK_FAILED_LOCK_REVERSE: Byte = 38

        /**
         * IC卡开锁失败，门反锁
         */
        const val IC_UNLOCK_FAILED_LOCK_REVERSE: Byte = 39

        /**
         * 指纹开锁失败，门反锁
         */
        const val FR_UNLOCK_FAILED_LOCK_REVERSE: Byte = 40

        /**
         * app开锁失败,门反锁
         */
        const val APP_UNLOCK_FAILED_LOCK_REVERSE: Byte = 41
        //42 ~ 48 无参数
        /**
         * IC卡开锁失败—黑名单卡
         */
        const val IC_UNLOCK_FAILED_BLANKLIST: Byte = 51

        /**
         * 无线钥匙
         */
        const val WIRELESS_KEY_FOB: Byte = 55

        /**
         * 无线键盘电量
         */
        const val WIRELESS_KEY_PAD: Byte = 56

        /**
         * IC卡开锁失败—CPU安全信息错误
         */
        const val CPU_CARD_UNLOCK_FAILED: Byte = 74
    }
}