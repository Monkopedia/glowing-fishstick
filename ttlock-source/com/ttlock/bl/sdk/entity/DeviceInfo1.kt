package com.ttlock.bl.sdk.entityimport

import com.ttlock.bl.sdk.entity.DeviceInfo
import java.text.ParseException
import java.util.*

com.ttlock.bl.sdk.util.DigitUtil
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
 * Created by Administrator on 2017/1/9 0009.
 */
class DeviceInfo {
    var lockData: String? = null

    /**
     * hex feature
     */
    var featureValue: String? = null

    /**
     * Product model(e.g. "M201")
     */
    var modelNum: String? = null

    /**
     * Hardware version(e.g. "1.3")
     */
    var hardwareRevision: String? = null

    /**
     * Firmware version(e.g. "2.1.16.705")
     */
    var firmwareRevision: String? = null

    /**
     * NB lock IMEI
     */
    var nbNodeId: String? = null

    /**
     * NB运营商
     */
    var nbOperator: String? = null

    /**
     * NB lock card info
     */
    var nbCardNumber: String? = null

    /**
     * NB lock rssi
     */
    var nbRssi = 0

    /**
     * Date of manufacture(e.g. "20160707")
     */
    var factoryDate: String? = null

    /**
     * lock clock(e.g. "1701051531") yymmddhhmm
     */
    var lockClock: String? = null

    /**
     * 没用了 暂时保留
     */
    @Deprecated("")
    var specialValue = 0

    /**
     * 不完整密码
     */
    var passcodeKeyNumber = 0

    constructor() {}
    constructor(modelNum: String?, hardwareVersion: String?, firmwareVersion: String?) {
        this.modelNum = modelNum
        hardwareRevision = hardwareVersion
        firmwareRevision = firmwareVersion
    }

    fun setModelNum(modelNum: String?) {
        this.modelNum = modelNum
    }

    fun setHardwareRevision(hardwareRevision: String?) {
        this.hardwareRevision = hardwareRevision
    }

    fun setFirmwareRevision(firmwareRevision: String?) {
        this.firmwareRevision = firmwareRevision
    }

    fun getLockData(): String? {
        return lockData
    }

    fun setLockData(lockData: String?) {
        this.lockData = lockData
    }

    fun getFeatureValue(): String? {
        return featureValue
    }

    fun setFeatureValue(featureValue: String?) {
        this.featureValue = featureValue
    }

    fun getModelNum(): String? {
        return modelNum
    }

    fun getHardwareRevision(): String? {
        return hardwareRevision
    }

    fun getFirmwareRevision(): String? {
        return firmwareRevision
    }

    fun getNbNodeId(): String? {
        return nbNodeId
    }

    fun setNbNodeId(nbNodeId: String?) {
        this.nbNodeId = nbNodeId
    }

    fun getNbOperator(): String? {
        return nbOperator
    }

    fun setNbOperator(nbOperator: String?) {
        this.nbOperator = nbOperator
    }

    fun getNbCardNumber(): String? {
        return nbCardNumber
    }

    fun setNbCardNumber(nbCardNumber: String?) {
        this.nbCardNumber = nbCardNumber
    }

    fun getNbRssi(): Int {
        return nbRssi
    }

    fun setNbRssi(nbRssi: Int) {
        this.nbRssi = nbRssi
    }

    fun getFactoryDate(): String? {
        return factoryDate
    }

    fun setFactoryDate(factoryDate: String?) {
        this.factoryDate = factoryDate
    }

    fun getLockClock(): String? {
        return lockClock
    }

    fun setLockClock(lockClock: String?) {
        this.lockClock = lockClock
    }

    fun getLockClockStamp(): Long { //考虑时区问题
        val sdr = SimpleDateFormat("yyMMddHHmm", Locale.getDefault())
        var date = Date()
        try {
            date = sdr.parse(lockClock)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date.time
    }

    fun getDeviceInfo(): String? {
        return GsonUtil.toJson<DeviceInfo>(this)
    }

    override fun toString(): String {
        return "DeviceInfo{" +
                "featureValue=" + featureValue +
                ", modelNum='" + modelNum + '\'' +
                ", hardwareRevision='" + hardwareRevision + '\'' +
                ", firmwareRevision='" + firmwareRevision + '\'' +
                ", nbNodeId='" + nbNodeId + '\'' +
                ", nbOperator='" + nbOperator + '\'' +
                ", nbCardNumber='" + nbCardNumber + '\'' +
                ", nbRssi=" + nbRssi +
                ", factoryDate='" + factoryDate + '\'' +
                ", lockClock='" + lockClock + '\'' +
                '}'
    }
}