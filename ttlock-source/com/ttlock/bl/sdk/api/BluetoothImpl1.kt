package com.ttlock.bl.sdk.api

import android.bluetooth.*
import android.util.*
import com.ttlock.bl.sdk.callback.ActivateLiftFloorsCallback
import com.ttlock.bl.sdk.callback.SetUnlockDirectionCallback
import com.ttlock.bl.sdk.callback.GetAllValidICCardCallback
import com.ttlock.bl.sdk.callback.AddDoorSensorCallback
import com.ttlock.bl.sdk.callback.SetRemoteUnlockSwitchCallback
import com.ttlock.bl.sdk.callback.GetAllValidFingerprintCallback
import com.ttlock.bl.sdk.callback.AddFingerprintCallback
import com.ttlock.bl.sdk.callback.SetLockTimeCallback
import com.ttlock.bl.sdk.callback.GetOperationLogCallback
import com.ttlock.bl.sdk.callback.AddICCardCallback
import com.ttlock.bl.sdk.callback.AddRemoteCallback
import com.ttlock.bl.sdk.callback.ClearAllFingerprintCallback
import com.ttlock.bl.sdk.callback.ClearAllICCardCallback
import com.ttlock.bl.sdk.callback.ClearPassageModeCallback
import com.ttlock.bl.sdk.callback.ClearRemoteCallback
import com.ttlock.bl.sdk.callback.ConfigIpCallback
import com.ttlock.bl.sdk.callback.ConfigServerCallback
import com.ttlock.bl.sdk.callback.ConfigWifiCallback
import com.ttlock.bl.sdk.callback.ConnectCallback
import com.ttlock.bl.sdk.callback.ControlLockCallback
import com.ttlock.bl.sdk.callback.CreateCustomPasscodeCallback
import com.ttlock.bl.sdk.callback.DeleteDoorSensorCallback
import com.ttlock.bl.sdk.callback.DeleteFingerprintCallback
import com.ttlock.bl.sdk.callback.DeleteICCardCallback
import com.ttlock.bl.sdk.callback.DeletePassageModeCallback
import com.ttlock.bl.sdk.callback.DeletePasscodeCallback
import com.ttlock.bl.sdk.callback.DeleteRemoteCallback
import com.ttlock.bl.sdk.callback.EnterDfuModeCallback
import com.ttlock.bl.sdk.callback.GetAccessoryBatteryLevelCallback
import com.ttlock.bl.sdk.callback.GetAdminPasscodeCallback
import com.ttlock.bl.sdk.callback.GetAllValidPasscodeCallback
import com.ttlock.bl.sdk.callback.GetAutoLockingPeriodCallback
import com.ttlock.bl.sdk.callback.GetBatteryLevelCallback
import com.ttlock.bl.sdk.callback.GetLightTimeCallback
import com.ttlock.bl.sdk.callback.GetLockConfigCallback
import com.ttlock.bl.sdk.callback.GetLockFreezeStateCallback
import com.ttlock.bl.sdk.callback.GetLockMuteModeStateCallback
import com.ttlock.bl.sdk.callback.GetLockSoundWithSoundVolumeCallback
import com.ttlock.bl.sdk.callback.GetLockStatusCallback
import com.ttlock.bl.sdk.callback.GetLockSystemInfoCallback
import com.ttlock.bl.sdk.callback.GetLockTimeCallback
import com.ttlock.bl.sdk.callback.GetLockVersionCallback
import com.ttlock.bl.sdk.callback.GetNBAwakeModesCallback
import com.ttlock.bl.sdk.callback.GetNBAwakeTimesCallback
import com.ttlock.bl.sdk.callback.GetPassageModeCallback
import com.ttlock.bl.sdk.callback.GetPasscodeVerificationInfoCallback
import com.ttlock.bl.sdk.callback.GetPasscodeVisibleStateCallback
import com.ttlock.bl.sdk.callback.GetRemoteUnlockStateCallback
import com.ttlock.bl.sdk.callback.GetSpecialValueCallback
import com.ttlock.bl.sdk.callback.GetUnlockDirectionCallback
import com.ttlock.bl.sdk.callback.GetWifiInfoCallback
import com.ttlock.bl.sdk.callback.InitLockCallback
import com.ttlock.bl.sdk.callback.LockCallback
import com.ttlock.bl.sdk.callback.ModifyAdminPasscodeCallback
import com.ttlock.bl.sdk.callback.ModifyFingerprintPeriodCallback
import com.ttlock.bl.sdk.callback.ModifyICCardPeriodCallback
import com.ttlock.bl.sdk.callback.ModifyPasscodeCallback
import com.ttlock.bl.sdk.callback.ModifyRemoteValidityPeriodCallback
import com.ttlock.bl.sdk.callback.OnScanFailedListener
import com.ttlock.bl.sdk.callback.RecoverLockDataCallback
import com.ttlock.bl.sdk.callback.ReportLossCardCallback
import com.ttlock.bl.sdk.callback.ResetKeyCallback
import com.ttlock.bl.sdk.callback.ResetLockCallback
import com.ttlock.bl.sdk.callback.ResetPasscodeCallback
import com.ttlock.bl.sdk.callback.ScanLockCallback
import com.ttlock.bl.sdk.callback.ScanWifiCallback
import com.ttlock.bl.sdk.callback.SetAutoLockingPeriodCallback
import com.ttlock.bl.sdk.callback.SetHotelCardSectorCallback
import com.ttlock.bl.sdk.callback.SetHotelDataCallback
import com.ttlock.bl.sdk.callback.SetLiftControlableFloorsCallback
import com.ttlock.bl.sdk.callback.SetLiftWorkModeCallback
import com.ttlock.bl.sdk.callback.SetLightTimeCallback
import com.ttlock.bl.sdk.callback.SetLockConfigCallback
import com.ttlock.bl.sdk.callback.SetLockFreezeStateCallback
import com.ttlock.bl.sdk.callback.SetLockMuteModeCallback
import com.ttlock.bl.sdk.callback.SetLockSoundWithSoundVolumeCallback
import com.ttlock.bl.sdk.callback.SetNBAwakeModesCallback
import com.ttlock.bl.sdk.callback.SetNBAwakeTimesCallback
import com.ttlock.bl.sdk.callback.SetNBServerCallback
import com.ttlock.bl.sdk.callback.SetPassageModeCallback
import com.ttlock.bl.sdk.callback.SetPasscodeVisibleCallback
import com.ttlock.bl.sdk.callback.SetPowerSaverControlableLockCallback
import com.ttlock.bl.sdk.callback.SetPowerSaverWorkModeCallback
import com.ttlock.bl.sdk.constant.*
import com.ttlock.bl.sdk.entity.AccessoryInfo
import com.ttlock.bl.sdk.entity.AccessoryType
import com.ttlock.bl.sdk.entity.ActivateLiftFloorsResult
import com.ttlock.bl.sdk.entity.ControlLockResult
import com.ttlock.bl.sdk.entity.CyclicConfig
import com.ttlock.bl.sdk.entity.DeviceInfo
import com.ttlock.bl.sdk.entity.FR
import com.ttlock.bl.sdk.entity.HotelData
import com.ttlock.bl.sdk.entity.ICCard
import com.ttlock.bl.sdk.entity.KeyboardPwd
import com.ttlock.bl.sdk.entity.LockData
import com.ttlock.bl.sdk.entity.LockError
import com.ttlock.bl.sdk.entity.LockVersion
import com.ttlock.bl.sdk.entity.NBAwakeConfig
import com.ttlock.bl.sdk.entity.Passcode
import com.ttlock.bl.sdk.entity.PwdInfoV3
import com.ttlock.bl.sdk.entity.SoundVolume
import com.ttlock.bl.sdk.entity.TTLockConfigType
import com.ttlock.bl.sdk.entity.TransferData
import com.ttlock.bl.sdk.entity.UnlockDirection
import com.ttlock.bl.sdk.entity.ValidityInfo
import com.ttlock.bl.sdk.entity.WifiLockInfo
import com.ttlock.bl.sdk.gateway.model.WiFi
import com.ttlock.bl.sdk.scanner.IScanCallback
import com.ttlock.bl.sdk.scanner.ScannerCompat
import com.ttlock.bl.sdk.util.DigitUtil
import com.ttlock.bl.sdk.util.FeatureValueUtil
import com.ttlock.bl.sdk.util.GsonUtil
import com.ttlock.bl.sdk.util.LogUtil
import json.JSONArray
import json.JSONException
import json.JSONObject
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.locks.*

/**
 * Created on  2019/4/11 0011 16:16
 *
 * @author theodre
 */
internal class BluetoothImpl private constructor() {
    private object InstanceHolder {
        val mInstance = BluetoothImpl()
    }

    private var mApplicationContext: Context? = null
    private var isBtStateReceiverRegistered = false
    private var mAppExecutor: AppExecutors? = null

    /**
     * 是否有等待指令
     */
    private var isWaitCommand = false

    /**
     * 进行连接的时间
     */
    private var connectTime: Long = 0

    /**
     * 是否需要重连
     */
    private var isNeedReCon = true

    /**
     * 已经连接次数
     */
    private var connectCnt = 0
    private var mBluetoothManager: BluetoothManager? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null

    // 可以改成device
    private var mBluetoothDeviceAddress: String? = null
    private var mBluetoothDevice: BluetoothDevice? = null
    private var mExtendedBluetoothDevice: ExtendedBluetoothDevice? = null
    private var mBluetoothGatt: BluetoothGatt? = null
    private var mNotifyCharacteristic: BluetoothGattCharacteristic? = null
    private var modelNumberCharacteristic: BluetoothGattCharacteristic? = null
    private var hardwareRevisionCharacteristic: BluetoothGattCharacteristic? = null
    private var firmwareRevisionCharacteristic: BluetoothGattCharacteristic? = null
    private val mContext: Context? = null
    private var mHandler: Handler? = null

    /**
     * 扫描器
     */
    private var mScanner: ScannerCompat? = null
    private var mScanning = false

    /**
     * 扫描回调
     */
    private var scanCallback: ScanCallback? = null

    /**
     * 传输数据
     */
    private var dataQueue: LinkedList<ByteArray>? = null
    private var cloneDataQueue: LinkedList<ByteArray>? = null

    /**
     * 接收的数据
     */
    private var mReceivedDataBuffer: ByteArray = ByteArray(0)

    /**
     * 接收数据长度
     */
    private var mReceivedBufferCount = 0

    /**
     * 待接收数据长度
     */
    private var leftRecDataCount = 0

    /**
     * 接收的最大长度
     */
    private val maxBufferCount = 256

    /**
     * 接口操作指令
     */
    private var currentAPICommand = 0

    /**
     * 判断管理员
     */
    var adminPs: String? = null

    /**
     * 开门约定数
     */
    var unlockKey: String? = null

    /**
     * 需要校准的时间
     */
    private var calibationTime: Long = 0

    /**
     * 锁时区和UTC时区时间的差数，单位milliseconds
     */
    private var timezoneOffSet: Long = 0

    /**
     * 三代锁使用 账号id
     */
    var mUid = 0

    /**
     * 密码数据
     */
    var pwdInfo: String? = null

    /**
     * 时间戳
     */
    var timestamp: Long = 0

    /**
     * 同时有效密码数
     * 0默认值
     */
    var validPwdNum: Byte = 0

    /**
     * 传输数据
     */
    var pwdData: ByteArray = ByteArray(0)

    /**
     * 传输位置
     */
    var dataPos = 0

    /**
     * 每次传输包长度
     */
    var packetLen = 28

    /**
     * 键盘密码
     */
    var adminPasscode: String? = null

    /**
     * 删除密码
     */
    var deletePwd: String? = null

    /**
     * 锁标志位
     */
    var lockFlagPos = 0

    /**
     * 设置的锁名称
     */
    var lockname: String? = null

    /**
     * 原始密码
     */
    var originalPwd: String? = null

    /**
     * 新密码
     */
    var newPwd: String? = null

    /**
     * 起始时间
     */
    var startDate: Long = 0

    /**
     * 结束时间
     */
    var endDate: Long = 0

    /**
     * 键盘密码类型
     */
    var keyboardPwdType: Byte = 0
    private var pwdList: Queue<String>? = null

    /**
     * 操作日志
     */
    private var logOperates: MutableList<LogOperate>? = null

    /**
     * 动车位锁
     */
    private var moveDateArray: JSONArray? = null

    /**
     * 三代锁开门判重使用
     */
    private var unlockDate: Long = // TODO:
        0

    /**
     * IC卡号或指纹卡号
     */
    private var attachmentNum: Long = 0

    /**
     * 错误
     */
    var timer: Timer? = null
    var lockError: LockError? = null
    var tmpLockError: LockError? = null

    /**
     * 判断是否扫描
     */
    var scan = false
    /**
     * 设备特征 默认1 表示支持密码
     */
    //    private int feature = 1;
    /**
     * 手环KEY
     */
    private var wristbandKey: String? = null

    /**
     * 版本信息
     */
    private val lockVersionString: String? = null
    /** ------------------ 设备信息 ---------------  */
    /**
     * 产品型号
     */
    private var modelNumber: String? = null

    /**
     * 硬件版本号
     */
    private var hardwareRevision: String? = null

    /**
     * 固件版本号
     */
    private var firmwareRevision: String? = null

    /**
     * 生产日期
     */
    private var factoryDate: String? = null

    /**
     * 时钟
     */
    private var lockClock: String? = null

    /**
     * TODO:考虑合并
     * 密码列表
     */
    private var pwds: List<String>? = null
    private var recoveryDatas: List<RecoveryData>? = null
    private var tempOptype = 0
    private var transferData: TransferData? = null
    private var isSetLockName = false
    private var deviceInfo: DeviceInfo? = null
    private var icCards: ArrayList<ICCard>? = null
    private var frs: ArrayList<FR>? = null
    private var passcodes: ArrayList<Passcode>? = null
    private val conLock: ReentrantLock = ReentrantLock()
    private var psFromLock: ByteArray? = null
    private var commandSendCount = 0
    private var lockData: LockData? = null
    private var hotelData: HotelData? = null

    /**
     * 记录读取记录的次数
     */
    private var recordCnt = 0

    /**
     * 上一次读取记录的序号
     */
    private var lastRecordSeq = 0
    private var passageModeDatas: MutableList<PassageModeData>? = null
    private var weerOrDays: JSONArray? = null

    /**
     * 循环数据位置
     */
    var cyclicPos = 0
    private var cyclicConfigs: List<CyclicConfig>? = null
    private var recoveryData: RecoveryData? = null
    private var commandQueue: LinkedList<Byte>? = null

    /**
     * IC跟指纹添加过程中 失败的情况 是否需要删除 卡跟指纹
     */
    private var failNeedDelete = false

    /**
     * 锁配置里面的开关项
     */
    private var switchItem = 0

    /**
     * 锁配置里面的开关值
     */
    private var switchValue = 0
    private var sdkLogBuilder: StringBuilder? = StringBuilder()
    private var sdkLogItem: String? = null
    private val wiFis: MutableList<WiFi> = ArrayList<WiFi>()
    private var sensitivity = // 灵敏度：0-关闭、1-低、2-中、3-高
        0

    /**
     * 不支持的情况不传该字段
     */
    private var settingValue: Int? = // 用于表示防撬开关、重置按键开关，反锁开关、开门方向、常开模式自动开锁开关、Wifi锁省电模式开关等设置项
        null
    var bluttoothState: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val stateExtra: String = BluetoothAdapter.EXTRA_STATE
            val state: Int = intent.getIntExtra(stateExtra, BluetoothAdapter.STATE_OFF)
            when (state) {
                BluetoothAdapter.STATE_TURNING_ON -> LogUtil.d(
                    "BluetoothAdapter.STATE_TURNING_ON",
                    DBG
                )
                BluetoothAdapter.STATE_ON -> {
                    LogUtil.d("BluetoothAdapter.STATE_ON", DBG)
                    if (scan) {
//                        startScan();
                        mHandler?.postDelayed(stateOnScanRunable, 1000)
                    } else {
                        LogUtil.d("do not start scan", DBG)
                    }
                }
                BluetoothAdapter.STATE_TURNING_OFF -> LogUtil.d(
                    "BluetoothAdapter.STATE_TURNING_OFF",
                    DBG
                )
                BluetoothAdapter.STATE_OFF -> {
                    LogUtil.d("BluetoothAdapter.STATE_OFF", DBG)
                    mConnectionState = STATE_DISCONNECTED
                }
                else -> {}
            }
        }
    }

    // TODO:
    var disTimerTask: TimerTask? = null
    var stateOnScanRunable = Runnable { startScan() }
    private var onScanFailedListener: OnScanFailedListener? = null
    private var scanAll = false
    var disConRunable = Runnable {
        if (mConnectionState == STATE_CONNECTED) {
            LogUtil.d("disconnecting……", DBG)
            disconnect()
        } else if (mConnectionState == STATE_CONNECTING) {
            LogUtil.d("disconnecting……", DBG)
            // TODO:这里断开 再次连接会 mNotifyCharacteristic or mBluetoothGatt is null mBluetoothGatt:null
            disconnect()
            // TODO:这里会变为null
            close()
            //                startScan();
            val mTimeOutCallback: LockCallback? =
                LockCallbackManager.Companion.getInstance().getConnectCallback()
            if (mTimeOutCallback != null) {
                mExtendedBluetoothDevice!!.disconnectStatus =
                    ExtendedBluetoothDevice.Companion.CONNECT_TIME_OUT
                mTimeOutCallback.onFail(LockError.LOCK_CONNECT_FAIL)
            } else {
                LogUtil.w("mTTLockCallback is null", DBG)
            }
        }
    }

    // TODO:设置专门的回调线程
    internal open inner class ScanCallback : IScanCallback {
        override fun onScan(extendedBluetoothDevice: ExtendedBluetoothDevice) {
            if (mScanning && mConnectionState == STATE_DISCONNECTED) {
                val mScanCallback: ScanLockCallback =
                    LockCallbackManager.Companion.getInstance().getLockScanCallback()
                        ?: return
                /**
                 * 手环
                 */
                if (scanBongOnly) {
                    if (extendedBluetoothDevice.isWristband()) {
                        mScanCallback.onScanLockSuccess(extendedBluetoothDevice)
                    }
                } else {
                    mScanCallback.onScanLockSuccess(extendedBluetoothDevice)
                }
            }
        }
    }

    private val mGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (mBluetoothGatt !== gatt) {
                LogUtil.w("gatt=$gatt status=$status newState=$newState", DBG)
                gatt.close()
                return
            }
            failNeedDelete = false
            LogUtil.i("gatt=$gatt status=$status newState=$newState", true)
            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_BALANCED);

//                mConnectionState = STATE_CONNECTED;//移到写descriptor里面
                Log.i(TAG, "Connected to GATT server.")
                //                isNeedReCon = false;
                mHandler?.removeCallbacks(disConRunable)

//                try {
//                    Thread.sleep(600);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                // 发现服务发现不了 重连需要
                connectTime = System.currentTimeMillis()
                mBluetoothGatt?.let {
                    Log.i(
                        TAG,
                        "Attempting to start service discovery:" + it.discoverServices()
                    )
                } ?: run {
                    mConnectionState = STATE_DISCONNECTED
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                isWaitCommand = false
                isCanSendCommandAgain = true
                isCheckedLockPermission = false
                mHandler?.removeCallbacks(disConRunable)
                if (isNeedReCon && connectCnt < MAX_CONNECT_COUNT && System.currentTimeMillis() - connectTime < MAX_CONNECT_INTEVAL) {
                    LogUtil.w("connect again:$connectCnt", DBG)
                    // TODO:用mac地址连接的 没有广播信息 添加管理员
//                    connect(mBluetoothDevice.getAddress());
                    try {
                        conLock.lock()
                        connect(mExtendedBluetoothDevice)
                    } finally {
                        conLock.unlock()
                    }
                } else {
                    mConnectionState = STATE_DISCONNECTED
                    Log.i(TAG, "Disconnected from GATT server.")
                    close()
                    readCacheLog()
                    mAppExecutor?.mainThread()?.execute(Runnable { doConnectFailedCallback() })
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (mBluetoothGatt !== gatt) return
            LogUtil.d("gatt=$gatt status=$status", DBG)
            LogUtil.d(Thread.currentThread().toString(), DBG)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (mBluetoothGatt == null) {
                    LogUtil.w("mBluetoothGatt null", true)
                    return
                }
                if (scanBongOnly) {
                    UUID_SERVICE = BONG_SERVICE
                    UUID_READ = BONG_READ
                    UUID_WRITE = BONG_WRITE
                } else {
                    UUID_SERVICE = TTL_SERVICE
                    UUID_READ = TTL_READ
                    UUID_WRITE = TTL_WRITE
                }

//                for (BluetoothGattService service : gatt.getServices()) {//TODO:test
//                    LogUtil.d("service:" + service.getUuid(), DBG);
//                }
                var service: BluetoothGattService? = mBluetoothGatt!!.getService(
                    UUID.fromString(
                        DEVICE_INFORMATION_SERVICE
                    )
                )
                if (service != null) {
                    val gattCharacteristics: List<BluetoothGattCharacteristic>? =
                        service.getCharacteristics()
                    if (gattCharacteristics != null && gattCharacteristics.size > 0) {
                        for (gattCharacteristic in gattCharacteristics) {
                            LogUtil.d(gattCharacteristic.getUuid().toString(), DBG)
                            LogUtil.d("read characteristic:" + Thread.currentThread(), DBG)
                            if (gattCharacteristic.getUuid().toString().equals(
                                    READ_MODEL_NUMBER_UUID
                                )
                            ) {
                                modelNumberCharacteristic = gattCharacteristic
                                //                                gatt.readCharacteristic(gattCharacteristic);
                            } else if (gattCharacteristic.getUuid().toString().equals(
                                    READ_FIRMWARE_REVISION_UUID
                                )
                            ) {
                                firmwareRevisionCharacteristic = gattCharacteristic
                            } else if (gattCharacteristic.getUuid().toString().equals(
                                    READ_HARDWARE_REVISION_UUID
                                )
                            ) {
                                hardwareRevisionCharacteristic = gattCharacteristic
                            }
                        }
                    }
                }
                service = mBluetoothGatt?.getService(UUID.fromString(UUID_SERVICE))
                if (service != null) {
                    val gattCharacteristics: List<BluetoothGattCharacteristic>? =
                        service.getCharacteristics()
                    if (gattCharacteristics != null && gattCharacteristics.size > 0) {
                        for (gattCharacteristic in gattCharacteristics) {
                            LogUtil.d(gattCharacteristic.getUuid().toString(), DBG)
                            if (gattCharacteristic.getUuid().toString().equals(UUID_WRITE)) {
                                mNotifyCharacteristic = gattCharacteristic
                                LogUtil.d("mNotifyCharacteristic:$mNotifyCharacteristic", DBG)
                            } else if (gattCharacteristic.getUuid().toString().equals(UUID_READ)) {
                                gatt.setCharacteristicNotification(gattCharacteristic, true)
                                val descriptor: BluetoothGattDescriptor =
                                    gattCharacteristic.getDescriptor(
                                        UUID_HEART_RATE_MEASUREMENT
                                    )
                                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                                if (gatt.writeDescriptor(descriptor)) {
                                    LogUtil.d("writeDescriptor successed", DBG)
                                } else {
                                    LogUtil.d("writeDescriptor failed", DBG)
                                }
                            }
                        }
                    }
                } else {
                    // 测试出现的情况 是否再次发现一次
                    sdkLogItem = "service is null"
                    addSdkLog()

                    // TODO:当连接失败处理
                    mConnectionState = STATE_DISCONNECTED
                    LogUtil.d(
                        "mBluetoothGatt.getServices().size():" + mBluetoothGatt?.getServices()
                            ?.size,
                        DBG
                    )
                    if ((mBluetoothGatt?.getServices()?.size ?: 0) > 0
                    ) mExtendedBluetoothDevice!!.setNoLockService(true)
                    close()
                    // 再次扫描
//                    startScan();
                    mExtendedBluetoothDevice!!.disconnectStatus =
                        ExtendedBluetoothDevice.Companion.SERVICE_DISCONNECTED
                    doConnectFailedCallback()
                }
            } else {
                LogUtil.w("onServicesDiscovered received: $status", DBG)
            }
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int
        ) {
            if (mBluetoothGatt !== gatt) return
            LogUtil.d(Thread.currentThread().toString(), DBG)
            super.onDescriptorWrite(gatt, descriptor, status)
            LogUtil.d("gatt=$gatt descriptor=$descriptor status=$status", true)
            LogUtil.d(descriptor.getCharacteristic().getUuid().toString(), DBG)
            isNeedReCon = false
            mConnectionState = STATE_CONNECTED
            val mCallback: ConnectCallback? =
                LockCallbackManager.Companion.getInstance().getConnectCallback()
            mCallback?.onConnectSuccess(mExtendedBluetoothDevice!!)
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (mBluetoothGatt !== gatt) {
                LogUtil.w("gatt=$gatt characteristic=$characteristic status=$status", DBG)
                return
            }
            // 和读数据在同一个线程里面
            LogUtil.d("gatt=$gatt characteristic=$characteristic status=$status", DBG)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (dataQueue!!.size > 0) {
                    characteristic.setValue(dataQueue!!.poll())
                    // TODO:写成功再写下一个
                    gatt.writeCharacteristic(characteristic)
                } else {
                    mHandler?.removeCallbacks(disConRunable)
                    // TODO:考虑下面的是否可以去掉
                    if (currentAPICommand != APICommand.OP_REMOTE_CONTROL_DEVICE_MANAGEMENT && currentAPICommand != APICommand.OP_SCAN_WIFI && currentAPICommand != APICommand.OP_SET_WIFI && currentAPICommand != APICommand.OP_SET_STATIC_IP) { // 遥控设备是没有响应指令 WIFI扫描需要等待的时间比较长
                        disTimerTask = object : TimerTask() {
                            override fun run() {
                                // TODO:测试 暂时注释掉
                                disconnect()
                            }
                        }
                        var delay: Long = 2500
                        if (currentAPICommand == APICommand.OP_RESET_LOCK || currentAPICommand == APICommand.OP_WRITE_FR || isWaitCommand) {
                            delay = 5500
                        }
                        if (timer != null) {
                            timer!!.schedule(disTimerTask, delay)
                        }
                    }
                }
            } else {
                LogUtil.w("onCharacteristicWrite failed", DBG)
            }
            super.onCharacteristicWrite(gatt, characteristic, status)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            if (mBluetoothGatt !== gatt) return
            super.onCharacteristicChanged(gatt, characteristic)
            //            LogUtil.d(Thread.currentThread().toString().toString(), DBG);
            try {
                LogUtil.d("gatt=$gatt characteristic=$characteristic", DBG)
                val data: ByteArray = characteristic.getValue()
                val dataLen = data.size
                if (scanBongOnly) {
                    lockError = if ("success" == String(data)) {
//                        error = Error.SUCCESS;
                        LockError.SUCCESS
                    } else {
                        LockError.LOCK_OPERATE_FAILED
                    }
                    when (tempOptype) {
                        0x01 -> {}
                        0x02 -> {}
                        else -> {}
                    }
                    return
                }
                LogUtil.d("receiver data=" + DigitUtil.byteArrayToHexString(data), DBG)
                if (mReceivedBufferCount + dataLen <= maxBufferCount) {
                    System.arraycopy(data, 0, mReceivedDataBuffer, mReceivedBufferCount, dataLen)
                    mReceivedBufferCount += dataLen
                }
                LogUtil.d("mReceivedBufferCount:$mReceivedBufferCount", DBG)
                LogUtil.d("dataLen:$dataLen", DBG)
                /**
                 * 数据开始
                 */
                if (mReceivedBufferCount == dataLen && data[0].toInt() == 0x7f && data[1].toInt() == 0x5a) {
                    var valueLen = 0
                    if (data[2] >= 5) {
                        // 数据长度+1位校验位
                        valueLen = data[11] + 1
                        leftRecDataCount = valueLen - (dataLen - 12)
                    } else { // 老协议
                        valueLen = data[5] + 1
                        leftRecDataCount = valueLen - (dataLen - 6)
                    }
                    LogUtil.d("all:$leftRecDataCount", DBG)
                } else {
                    leftRecDataCount -= dataLen
                }
                LogUtil.d("leftRecDataCount:$leftRecDataCount", DBG)
                val last = mReceivedDataBuffer[mReceivedBufferCount - 1]
                val lastSec = mReceivedDataBuffer[mReceivedBufferCount - 2]
                /**
                 * 接收完成
                 */
                if (lastSec.toInt() == 13 && last.toInt() == 10 && leftRecDataCount <= 0) {
                    LogUtil.d("receive finish", DBG)
                    mReceivedBufferCount -= 2
                    LogUtil.d(
                        "mReceivedDataBuffer=" + DigitUtil.byteArrayToHexString(
                            Arrays.copyOf(
                                mReceivedDataBuffer,
                                mReceivedBufferCount
                            )
                        ),
                        DBG
                    )
                    processCommandResponse(Arrays.copyOf(mReceivedDataBuffer, mReceivedBufferCount))
                    // 清零
                    mReceivedBufferCount = 0
                    disTimerTask?.cancel()
                    if (timer != null) {
                        LogUtil.d("num:" + timer!!.purge(), DBG)
                    }
                    /**
                     * 接收完成
                     */
                } else if (leftRecDataCount == 0) {
                    LogUtil.d("receive finish", DBG)
                    LogUtil.d(
                        "mReceivedDataBuffer=" + DigitUtil.byteArrayToHexString(
                            Arrays.copyOf(
                                mReceivedDataBuffer,
                                mReceivedBufferCount
                            )
                        ),
                        DBG
                    )
                    processCommandResponse(Arrays.copyOf(mReceivedDataBuffer, mReceivedBufferCount))
                    // 清零
                    mReceivedBufferCount = 0
                    disTimerTask?.cancel()
                    if (timer != null) {
                        LogUtil.d("num:" + timer!!.purge(), DBG)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // 清零
                mReceivedBufferCount = 0
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            LogUtil.d(
                Thread.currentThread().toString() + " " + String(characteristic.getValue()),
                DBG
            )
            LogUtil.d(Thread.currentThread().toString() + " " + characteristic.getUuid(), DBG)
        }
    }

    init {
        initialize()
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    private fun initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        mHandler = Handler()
        mReceivedDataBuffer = ByteArray(maxBufferCount)
        scanCallback = ScanCallback()

        // TODO:
        mScanner = ScannerCompat.Companion.getScanner()

        // TODO:异常判断  增加关闭和启动定时器
        timer = Timer()
        mAppExecutor = AppExecutors()
        LogUtil.d("------------------timer----------------$timer", DBG)
    }

    fun setOnScanFailedListener(onScanFailedListener: OnScanFailedListener?) {
        this.onScanFailedListener = onScanFailedListener
    }

    fun isScanAll(): Boolean {
        return scanAll
    }

    fun setScanAll(scanAll: Boolean) {
        this.scanAll = scanAll
    }

    fun prepareBTService(context: Context?) {
        if (context != null && !isBtStateReceiverRegistered) {
            mApplicationContext = context
            val appReceiverPermison: String = context.getPackageName() + ".permission.BLE_RECEIVER"
            context.registerReceiver(
                bluttoothState,
                IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED),
                appReceiverPermison,
                null
            )
            isBtStateReceiverRegistered = true
            LogUtil.d("startScan callde by user", DBG)
            if (mBluetoothManager == null) {
                mBluetoothManager =
                    context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            }
            mBluetoothAdapter = mBluetoothManager?.getAdapter()
        }
        mHandler = Handler()
        LogUtil.d("bluetooth is prepared", DBG)
    }

    /**
     * 启动扫描
     * 权限 开启判断
     * TODO:判断蓝牙是否开启
     */

    fun startScan() {
        scan = true
        if (!scan) {
            LogUtil.w("Already stop scan", DBG)
            return
        }
        LogUtil.d("start scan", DBG)
        if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled()) {
            LogUtil.w("BluetoothAdapter is disabled", true)
            val mScanCallback: LockCallback? =
                LockCallbackManager.Companion.getInstance().getLockScanCallback()
            if (mScanCallback != null) {
                mScanCallback.onFail(LockError.BLE_SERVER_NOT_INIT)
            }
            return
        }
        if (mScanner == null) {
            mScanner = ScannerCompat.Companion.getScanner()
        }
        if (scanCallback == null) {
            scanCallback = ScanCallback()
        }

//        stopScan();
        mScanner?.setScanAll(scanAll)
        mScanner?.setOnScanFailedListener(onScanFailedListener)
        LogUtil.d("start ---")
        mScanner?.startScan(scanCallback!!)
        mScanning = true
        if (mConnectionState != STATE_DISCONNECTED) { // 考虑没有断开的情况
            LogUtil.w("Ble not disconnected", DBG)
        }
    }

    /**
     * 停止扫描
     */
    fun stopScan() {
        LogUtil.d("mScanner:$mScanner", DBG)
        LogUtil.d("mScanning:$mScanning", DBG)
        scan = false
        if (mScanner != null && mScanning) {
            mScanning = false
            try {
                mScanner?.stopScan()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * `BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)`
     * callback.
     */

    fun connect(address: String?): Boolean {
        return try {
            conLock.lock()
            //            stopScan();
            connectCnt++
            connectTime = System.currentTimeMillis()
            if (mBluetoothAdapter == null || address == null) {
                Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.")
                LogUtil.w("mBluetoothAdapter:$mBluetoothAdapter", DBG)
                LogUtil.w("address:$address", DBG)
                val mConnectCallback: ConnectCallback? =
                    LockCallbackManager.Companion.getInstance().getConnectCallback()
                if (mConnectCallback != null) {
                    mConnectCallback.onFail(LockError.BLE_SERVER_NOT_INIT)
                }
                return false
            }
            if (!mBluetoothAdapter!!.isEnabled()) {
                LockCallbackManager.getInstance().getConnectCallback()
                    ?.onFail(LockError.BLE_SERVER_NOT_INIT)
                return false
            }
            if (mBluetoothGatt != null) {
                LogUtil.d("mBluetoothGatt not null", DBG)
                disconnect()
                close()
            }
            val device: BluetoothDevice = mBluetoothAdapter!!.getRemoteDevice(address)
            // We want to directly connect to the device, so we are setting the autoConnect
            // parameter to false.
            LogUtil.d("connect ……", DBG)
            mHandler?.removeCallbacks(disConRunable)
            mHandler?.postDelayed(disConRunable, CONNECT_TIME_OUT)
            mBluetoothGatt = device?.connectGatt(mApplicationContext!!, false, mGattCallback)
            mConnectionState = STATE_CONNECTING
            LogUtil.d("mBluetoothGatt:$mBluetoothGatt", DBG)
            Log.i(TAG, "Trying to create a new connection.")
            LogUtil.i("connected mBluetoothGatt:$mBluetoothGatt", DBG)
            mBluetoothDeviceAddress = address
            mBluetoothDevice = device
            mExtendedBluetoothDevice = ExtendedBluetoothDevice(device)
            true
        } finally {
            conLock.unlock()
        }
    }

    // TODO:连接之后很快断开以及连接之后没反应 判断一下重连未达最大次数的情况
    // 增加连接超时5s

    fun connect(extendedBluetoothDevice: ExtendedBluetoothDevice?): Boolean {
        // 停止扫描
        // TODO:4.4停不掉
        connectCnt++
        connectTime = System.currentTimeMillis()
        LogUtil.i("extendedBluetoothDevice:$extendedBluetoothDevice", DBG)
        val address: String? = extendedBluetoothDevice?.getDevice()?.getAddress()
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.")
            LogUtil.w("mBluetoothAdapter:$mBluetoothAdapter", DBG)
            LogUtil.w("address:$address", DBG)
            return false
        }
        if (mBluetoothGatt != null) {
            LogUtil.d("mBluetoothGatt not null", DBG)
            disconnect()
            close()
        }
        val device: BluetoothDevice = mBluetoothAdapter!!.getRemoteDevice(address)
        LogUtil.d("connect ……", DBG)
        mHandler?.removeCallbacks(disConRunable)
        // TODO:暂时取消
        mHandler?.postDelayed(disConRunable, CONNECT_TIME_OUT)

        // 恢复默认状态
        extendedBluetoothDevice!!.disconnectStatus =
            ExtendedBluetoothDevice.Companion.DISCONNECT_STATUS_NONE
        mBluetoothGatt = device.connectGatt(mApplicationContext!!, false, mGattCallback)
        mConnectionState = STATE_CONNECTING
        Log.i(TAG, "Trying to create a new connection.")
        LogUtil.i("connected mBluetoothGatt:$mBluetoothGatt", DBG)
        mBluetoothDeviceAddress = address
        mBluetoothDevice = device
        mExtendedBluetoothDevice = extendedBluetoothDevice
        return true
        //        } finally {
//            conLock.unlock();
//        }
    }

    /**
     * 今后都用这个方法
     * @param transferData
     */
    fun sendCommand(transferData: TransferData) {
        this.transferData = transferData
        mUid = transferData.getmUid()
        adminPs = transferData.getAdminPs()
        unlockKey = transferData.getUnlockKey()
        startDate = transferData.getStartDate()
        endDate = transferData.getEndDate()
        aesKeyArray = transferData.getAesKeyArray()
        unlockDate = transferData.getUnlockDate()
        lockFlagPos = transferData.getLockFlagPos()
        originalPwd = transferData.getOriginalPwd()
        newPwd = transferData.getNewPwd()
        attachmentNum = transferData.getNo()
        pwds = transferData.getPwds()
        wristbandKey = transferData.getWristbandKey()
        calibationTime = transferData.getCalibationTime()
        timezoneOffSet = transferData.getTimezoneOffSet()
        //        feature = transferData.getFeature();
        hotelData = transferData.getHotelData()

//        LogUtil.d("send command:" + (char) transferData.getCommand() + "-" + String.format("%#x", transferData.getCommand()), DBG);;
        sendCommand(transferData.getTransferData(), transferData.getAPICommand())
    }

    fun sendCommand(
        commandSrc: ByteArray?,
        uid: Int,
        unlockKey: String?,
        aesKey: ByteArray?,
        date: Long,
        apiCommand: Int
    ) {
        mUid = uid
        this.unlockKey = unlockKey
        aesKeyArray = aesKey
        currentAPICommand = apiCommand
        calibationTime = date
        sendCommand(commandSrc, apiCommand)
    }

    fun sendCommand(
        commandSrc: ByteArray?,
        adminPs: String?,
        unlockKey: String?,
        aesKey: ByteArray?,
        apiCommand: Int
    ) {
        this.adminPs = adminPs
        this.unlockKey = unlockKey
        aesKeyArray = aesKey
        currentAPICommand = apiCommand
        sendCommand(commandSrc, apiCommand)
    }

    fun sendCommand(
        commandSrc: ByteArray?,
        adminPs: String?,
        unlockKey: String?,
        lockFlagPos: Int,
        aesKey: ByteArray?,
        validPwdNum: Byte,
        keyboardPwdType: Byte,
        originalPwd: String?,
        string: String?,
        startDate: Long,
        endDate: Long,
        apiCommand: Int
    ) {
        this.originalPwd = originalPwd
        this.startDate = startDate
        this.endDate = endDate
        this.keyboardPwdType = keyboardPwdType
        sendCommand(
            commandSrc,
            adminPs,
            unlockKey,
            lockFlagPos,
            aesKey,
            validPwdNum,
            string,
            apiCommand
        )
    }

    fun sendCommand(
        commandSrc: ByteArray?,
        adminPs: String?,
        unlockKey: String?,
        lockFlagPos: Int,
        aesKey: ByteArray?,
        validPwdNum: Byte,
        string: String?,
        apiCommand: Int
    ) {
        this.adminPs = adminPs
        this.unlockKey = unlockKey
        this.validPwdNum = validPwdNum
        this.lockFlagPos = lockFlagPos
        aesKeyArray = aesKey
        currentAPICommand = apiCommand
        when (apiCommand) {
            APICommand.OP_SET_DELETE_PASSWORD -> deletePwd = string
            APICommand.OP_SET_LOCK_NAME -> lockname = string
            APICommand.OP_SET_KEYBOARD_PASSWORD, APICommand.OP_ADD_ONCE_KEYBOARD_PASSWORD, APICommand.OP_ADD_PERIOD_KEYBOARD_PASSWORD, APICommand.OP_ADD_PERMANENT_KEYBOARD_PASSWORD, APICommand.OP_REMOVE_ONE_PASSWORD, APICommand.OP_MODIFY_KEYBOARD_PASSWORD ->
                newPwd =
                    string
            else -> {}
        }
        sendCommand(commandSrc, apiCommand)
    }

    fun sendCommand(commandSrc: ByteArray?, apiCommand: Int, aesKeyArray: ByteArray) {
        BluetoothImpl.aesKeyArray = aesKeyArray
        sendCommand(commandSrc, apiCommand)
    }

    fun sendCommand(commandSrc: ByteArray?, apiCommand: Int) {
        currentAPICommand = apiCommand
        lockData = ConnectManager.Companion.getInstance().getConnectParamForCallback()!!
            .getLockData()
        if (currentAPICommand == APICommand.OP_GET_OPERATE_LOG) {
            // TODO:根据版本进行判断
            LogUtil.d("init logOperates")
            // 初始化操作日志
            if (logOperates == null) logOperates = ArrayList<LogOperate>()
            // 初始化动车位锁记录
            moveDateArray = JSONArray()
        }
        sendCommand(commandSrc)
    }

    fun sendCommand(commandSrc: ByteArray?, pwdInfo: String?, timestamp: Long, apiCommand: Int) {
        currentAPICommand = apiCommand
        this.pwdInfo = pwdInfo
        this.timestamp = timestamp
        sendCommand(commandSrc)
    }

    fun sendCommand(commandSrc: ByteArray?) {
        ConnectManager.Companion.getInstance().removeDataCheckTimer()
        //        LogUtil.d("currentAPICommand" + currentAPICommand, DBG);
        val length = commandSrc!!.size
        // 每条指令添加结束缚 13，10
        val command = ByteArray(commandSrc.size + 2)
        System.arraycopy(commandSrc, 0, command, 0, length)
        command[length] = 13
        command[length + 1] = 10
        sdkLogBuilder = StringBuilder()
        sdkLogItem = "send data:" + DigitUtil.byteArrayToHexString(commandSrc)
        addSdkLog()
        var len = length + 2
        if (dataQueue == null) dataQueue = LinkedList<ByteArray>()
        dataQueue!!.clear()
        var startPos = 0
        while (len > 0) {
            val ln = Math.min(len, 20)
            val data = ByteArray(ln)
            System.arraycopy(command, startPos, data, 0, ln)
            dataQueue!!.add(data)
            len -= 20
            startPos += 20
        }
        cloneDataQueue = dataQueue!!.clone() as LinkedList<ByteArray>
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            try {
                commandSendCount = 0
                mNotifyCharacteristic!!.setValue(dataQueue!!.poll())
                mBluetoothGatt!!.writeCharacteristic(mNotifyCharacteristic!!)
            } catch (e: Exception) {
                mConnectionState = STATE_DISCONNECTED
                sdkLogItem = e.message
                addSdkLog()
                doConnectFailedCallback()
            }
        } else {
            LogUtil.d("mNotifyCharacteristic:$mNotifyCharacteristic", DBG)
            LogUtil.d("mBluetoothGatt:$mBluetoothGatt", DBG)
            sdkLogItem = "mNotifyCharacteristic or mBluetoothGatt is null"
            addSdkLog()
            mConnectionState = STATE_DISCONNECTED
            doConnectFailedCallback()
        }
    }

    /**
     * 发送手环指令
     */
    fun sendBongCommand(wristbandKey: String) {
        val values = ByteArray(12)
        val constant = byteArrayOf(0x31, 0, 0, 0)
        val cmd: Byte = 0x01
        val enable: Byte = 0x01
        System.arraycopy(constant, 0, values, 0, 4)
        values[4] = cmd
        values[5] = enable
        System.arraycopy(wristbandKey.toByteArray(), 0, values, 6, 6)
        tempOptype = cmd.toInt()
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            mNotifyCharacteristic!!.setValue(values)
            mBluetoothGatt!!.writeCharacteristic(mNotifyCharacteristic!!)
        } else {
            LogUtil.d("mNotifyCharacteristic or mBluetoothGatt is null", DBG)
        }
    }

    /**
     * 设置手环开门信号值
     * @param rssi  必须是正数(先将负数转成正数)
     */
    fun setBongRssi(rssi: Byte) {
        val values = ByteArray(6)
        val constant = byteArrayOf(0x31, 0, 0, 0)
        val cmd: Byte = 0x02
        System.arraycopy(constant, 0, values, 0, 4)
        values[4] = cmd
        values[5] = rssi
        tempOptype = cmd.toInt()
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            mNotifyCharacteristic!!.setValue(values)
            mBluetoothGatt!!.writeCharacteristic(mNotifyCharacteristic!!)
        } else {
            LogUtil.d("mNotifyCharacteristic or mBluetoothGatt is null", DBG)
        }
    }

    private fun processCommandResponse(values: ByteArray) {
        mAppExecutor?.mainThread()?.execute(
            Runnable {
                mExtendedBluetoothDevice!!.disconnectStatus =
                    ExtendedBluetoothDevice.Companion.NORMAL_DISCONNECTED
                val command = Command(values)
                if (!command.isChecksumValid()) {
                    lockError = LockError.LOCK_CRC_CHECK_ERROR
                    lockError?.setCommand(command.getCommand())
                    errorCallback(lockError!!)
                    return@Runnable
                }
                if (currentAPICommand == APICommand.OP_GET_LOCK_VERSION) {
                    val mVersionCallback: LockCallback =
                        LockCallbackManager.Companion.getInstance().getCallback()
                            ?: return@Runnable
                    var protocolType: Byte = 0
                    var protocolVersion: Byte = 0
                    var scene: Byte = 0
                    var groupId: Short = 0
                    var orgId: Short = 0
                    if (values[0].toInt() == 0x7f && values[1].toInt() == 0x5a) {
                        lockError = LockError.SUCCESS
                        protocolType = values[2]
                        protocolVersion = values[3]
                        scene = values[4]
                        groupId = DigitUtil.byteArrayToShort(byteArrayOf(values[5], values[6]))
                        orgId = DigitUtil.byteArrayToShort(byteArrayOf(values[7], values[8]))
                    } else {
                        lockError = LockError.LOCK_OPERATE_FAILED
                    }
                    val mLockVersion =
                        LockVersion(protocolType, protocolVersion, scene, groupId, orgId)
                    (mVersionCallback as GetLockVersionCallback).onGetLockVersionSuccess(
                        GsonUtil.toJson<LockVersion>(
                            mLockVersion
                        )
                    )
                    return@Runnable
                }

                // 更新版本信息
                val groupId = command.organization!![0]
                val orgId = command.sub_organization!![0]
                mExtendedBluetoothDevice!!.groupId = groupId
                mExtendedBluetoothDevice!!.orgId = orgId
                var data: ByteArray? = null
                sdkLogItem = "currentAPICommand : $currentAPICommand"
                addSdkLog()
                sdkLogItem =
                    "command:" + Char(command.getCommand().toUShort()) + "-" + String.format(
                    "%#x",
                    command.getCommand()
                )
                addSdkLog()
                when (command.getLockType()) {
                    LockType.LOCK_TYPE_V2, LockType.LOCK_TYPE_V2S, LockType.LOCK_TYPE_MOBI, LockType.LOCK_TYPE_CAR ->
                        data =
                            command.getData()
                    LockType.LOCK_TYPE_V3, LockType.LOCK_TYPE_V3_CAR -> {
                        mHandler?.removeCallbacks(disConRunable)
                        data = command.getData(aesKeyArray!!)
                    }
                    LockType.LOCK_TYPE_V2S_PLUS -> data = command.getData(aesKeyArray!!)
                    else -> {}
                }
                if (data == null || data!!.size == 0) {
                    // TODO:添加的时候走了E指令 可能走到这里了
                    when (currentAPICommand) {
                        APICommand.OP_ADD_ADMIN -> {
                            lockError = LockError.LOCK_IS_IN_NO_SETTING_MODE
                            lockError?.setCommand(command.getCommand())
                            errorCallback(lockError!!)
                        }
                        APICommand.OP_RESET_LOCK -> if (command.length.toInt() == 0) { // 重置 没有包体 表示重置成功
                            LogUtil.d("reset - success")
                            val mResetLockCallback: LockCallback? =
                                LockCallbackManager.Companion.getInstance().getCallback()
                            if (mResetLockCallback != null) {
                                (mResetLockCallback as ResetLockCallback).onResetLockSuccess()
                            }
                        }
                        else -> {
                            lockError = LockError.KEY_INVALID
                            lockError?.setCommand(command.getCommand())
                            errorCallback(lockError!!)
                        }
                    }
                    return@Runnable
                }
                sdkLogItem = "values:" + DigitUtil.byteArrayToHexString(values)
                addSdkLog()
                sdkLogItem =
                    "feedback comman:" + Char(data[0].toUShort()) + "-" + String.format(
                    "%#x",
                    data[0]
                )
                addSdkLog()
                sdkLogItem = "response data:" + DigitUtil.byteArrayToHexString(data)
                addSdkLog()
                if (command.getCommand() == Command.Companion.COMM_GET_AES_KEY) {
                    if (data[1].toInt() == 1) {
                        aesKeyArray = Arrays.copyOfRange(data, 2, data.size)
                        adminPs = String(DigitUtil.generateDynamicPassword(10))
                        unlockKey = String(DigitUtil.generateDynamicPassword(10))
                        CommandUtil.V_addAdmin(
                            command.getLockType(),
                            adminPs!!,
                            unlockKey!!,
                            aesKeyArray
                        )
                    } else { // 失败
                        lockError = LockError.Companion.getInstance(data[2].toInt()) // 错误码
                        lockError?.setCommand(command.getCommand())
                        errorCallback(lockError!!)
                    }
                } else if (command.getCommand() == Command.Companion.COMM_RESPONSE) {
                    if (data[1] == CommandResponse.SUCCESS) {
                        LogUtil.d("success", DBG)
                        // 成功
                        lockError = LockError.SUCCESS
                        lockError?.setLockname(mExtendedBluetoothDevice!!.getName())
                        lockError?.setLockmac(mExtendedBluetoothDevice!!.getAddress())
                        lockError?.setCommand(data[0])
                        lockError?.setDate(System.currentTimeMillis())
                        var battery: Byte = -1 // 默认没电量的情况
                        when (data[0]) {
                            0xFF.toByte() -> if (currentAPICommand == APICommand.OP_SET_LOCK_NAME) isSetLockName =
                                true
                            Command.Companion.COMM_ADD_ADMIN -> {
                                // V
                                // TODO:是否判断车位锁的
                                if (command.getLockType() == LockType.LOCK_TYPE_V3) {
                                    if (Constant.SCIENER != String(data, 2, 7)) {
                                        lockError = LockError.AES_PARSE_ERROR
                                        lockError?.setCommand(command.getCommand())
                                        errorCallback(lockError!!)
                                        return@Runnable
                                    }
                                }
                                // 先校准时间
                                CommandUtil.C_calibationTime(
                                    command.getLockType(),
                                    System.currentTimeMillis(),
                                    TimeZone.getDefault().getOffset(
                                        System.currentTimeMillis()
                                    ).toLong(),
                                    aesKeyArray!!
                                )
                            }
                            Command.Companion.COMM_SEARCH_BICYCLE_STATUS -> {
                                // 自行车锁状态
                                isWaitCommand = true
                                mExtendedBluetoothDevice!!.setParkStatus(data[3].toInt())
                                if (currentAPICommand == APICommand.OP_DETECT_DOOR_SENSOR) {
//                                    TTLockAPI.getTTLockCallback().onGetDoorSensorState(mExtendedBluetoothDevice, data[2], data[4], error);
                                } else {
                                    val mStatusCallback: LockCallback? =
                                        LockCallbackManager.Companion.getInstance()
                                            .getCallbackWithoutRemove()
                                    if (mStatusCallback != null) {
                                        if (mStatusCallback is GetLockStatusCallback) {
                                            LockCallbackManager.Companion.getInstance()
                                                .clearAllCallback()
                                            (mStatusCallback as GetLockStatusCallback).onGetLockStatusSuccess(
                                                data[3].toInt()
                                            )
                                        }
                                    }
                                }
                            }
                            Command.Companion.COMM_FUNCTION_LOCK -> {
                                var battery: Byte = -1
                                val len = data.size
                                if (len > 2) {
                                    battery = data[2]
                                }
                                val uid: Int =
                                    DigitUtil.fourBytesToLong(Arrays.copyOfRange(data, 3, 7))
                                        .toInt()
                                val uniqueid: Int =
                                    DigitUtil.fourBytesToLong(Arrays.copyOfRange(data, 7, 11))
                                        .toInt()
                                val calendar: Calendar = Calendar.getInstance()
                                calendar.set(
                                    2000 + data[11],
                                    data[12] - 1,
                                    data[13].toInt(),
                                    data[14].toInt(),
                                    data[15].toInt(),
                                    data[16].toInt()
                                )
                                // 根据时间偏移量计算时间
                                val timeZone: TimeZone = TimeZone.getDefault()
                                LogUtil.d("timezoneOffSet:$timezoneOffSet", DBG)
                                if (timeZone.inDaylightTime(Date(System.currentTimeMillis()))) {
                                    timezoneOffSet -= timeZone.getDSTSavings().toLong()
                                }
                                timeZone.setRawOffset(timezoneOffSet.toInt())
                                calendar.setTimeZone(timeZone)
                                val lockTime: Long = calendar.getTimeInMillis()
                                mExtendedBluetoothDevice!!.setBatteryCapacity(data[2])
                                val mControlLockCallback: LockCallback? =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (mControlLockCallback != null) {
                                    (mControlLockCallback as ControlLockCallback).onControlLockSuccess(
                                        ControlLockResult(
                                            ControlAction.LOCK,
                                            battery.toInt(),
                                            uniqueid,
                                            lockTime
                                        )
                                    )
                                }
                            }
                            Command.Companion.COMM_SHOW_PASSWORD -> {
                                var status: Int = transferData!!.getOp()
                                val isQuery = data[3] == ActionType.GET
                                if (isQuery) {
                                    status = data[4].toInt()
                                    lockData!!.displayPasscode = status
                                    doResponse(data[0], command)
                                } else {
                                    status -= 2
                                    val lockCallback: LockCallback? =
                                        LockCallbackManager.Companion.getInstance().getCallback()
                                    if (lockCallback is SetLockConfigCallback) {
                                        (lockCallback as SetLockConfigCallback).onSetLockConfigSuccess(
                                            TTLockConfigType.PASSCODE_VISIBLE
                                        )
                                    } else if (lockCallback is SetPasscodeVisibleCallback) { // 回调返回参数可以不要
                                        (lockCallback as SetPasscodeVisibleCallback).onSetPasscodeVisibleSuccess(
                                            status == 1
                                        )
                                    }
                                }
                            }
                            Command.Companion.COMM_CHECK_ADMIN -> {
                                val len = data.size - 2
                                psFromLock = ByteArray(len)
                                System.arraycopy(data, 2, psFromLock, 0, len)
                                when (currentAPICommand) {
                                    APICommand.OP_ACTIVATE_FLOORS -> CommandUtil_V3.activateLiftFloors(
                                        command,
                                        psFromLock!!,
                                        transferData!!
                                    )
                                    APICommand.OP_UNLOCK_ADMIN, APICommand.OP_UNLOCK_EKEY -> CommandUtil.G_unlock(
                                        command.getLockType(),
                                        unlockKey,
                                        psFromLock,
                                        aesKeyArray,
                                        unlockDate,
                                        timezoneOffSet
                                    )
                                    APICommand.OP_LOCK_ADMIN, APICommand.OP_LOCK_EKEY -> if (command.getLockType() == LockType.LOCK_TYPE_V3_CAR) {
                                        CommandUtil.lock(
                                            command.getLockType(),
                                            unlockKey,
                                            psFromLock,
                                            aesKeyArray,
                                            unlockDate
                                        )
                                    } else {
                                        CommandUtil.L_lock(
                                            command.getLockType(),
                                            unlockKey,
                                            psFromLock,
                                            aesKeyArray
                                        )
                                    }
                                    APICommand.OP_SET_KEYBOARD_PASSWORD -> // 三代锁要校验随机数
                                        if (command.getLockType() == LockType.LOCK_TYPE_V3) {
                                            CommandUtil.checkRandom(
                                                command.getLockType(),
                                                unlockKey,
                                                psFromLock,
                                                aesKeyArray
                                            )
                                        } else {
                                            CommandUtil.S_setAdminKeyboardPwd(
                                                command.getLockType(),
                                                newPwd,
                                                aesKeyArray
                                            )
                                        }
                                    APICommand.OP_SET_DELETE_PASSWORD -> if (command.getLockType() == LockType.LOCK_TYPE_V3) // 三代锁要校验随机数
                                        CommandUtil.checkRandom(
                                            command.getLockType(),
                                            unlockKey,
                                            psFromLock,
                                            aesKeyArray
                                        ) else CommandUtil.D_setDeletePassword(
                                        command.getLockType(),
                                        deletePwd,
                                        aesKeyArray
                                    )
                                    APICommand.OP_RESET_EKEY -> {
                                        val mResetKeyCallback: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mResetKeyCallback != null) {
                                            lockData = ConnectManager.Companion.getInstance()
                                                .getConnectParamForCallback()!!
                                                .getLockData()
                                            if (lockData != null) {
                                                lockData!!.lockFlagPos = lockFlagPos
                                                (mResetKeyCallback as ResetKeyCallback).onResetKeySuccess(
                                                    lockData!!.encodeLockData()
                                                )
                                            }
                                        }
                                    }
                                    APICommand.OP_SET_LOCK_NAME -> CommandUtil.checkRandom(
                                        command.getLockType(),
                                        unlockKey,
                                        psFromLock,
                                        aesKeyArray
                                    )
                                    APICommand.OP_RESET_LOCK -> if (command.getLockType() == LockType.LOCK_TYPE_V3 || command.getLockType() == LockType.LOCK_TYPE_V3_CAR) {
                                        CommandUtil.checkRandom(
                                            command.getLockType(),
                                            unlockKey,
                                            psFromLock,
                                            aesKeyArray
                                        )
                                    } else {
                                        CommandUtil.R_resetLock(command.getLockType())
                                    }
                                    APICommand.OP_INIT_PWD -> when (command.getLockType()) {
                                        LockType.LOCK_TYPE_V3 -> CommandUtil.checkRandom(
                                            command.getLockType(),
                                            unlockKey,
                                            psFromLock,
                                            aesKeyArray
                                        )
                                        LockType.LOCK_TYPE_V2S_PLUS -> {
                                            pwdData = ByteArray(1624)
                                            generateTransmissionData(
                                                command.getScene().toInt(),
                                                pwdData,
                                                validPwdNum
                                            )
                                            dataPos = 0
                                            CommandUtil_V2S_PLUS.synPwd(
                                                command.getLockType(),
                                                Arrays.copyOfRange(
                                                    pwdData,
                                                    dataPos,
                                                    dataPos + packetLen
                                                ),
                                                dataPos,
                                                aesKeyArray
                                            )
                                        }
                                        LockType.LOCK_TYPE_V2S -> {
                                            pwdList = LinkedList<String>()
                                            val pwds =
                                                generatePwd(KeyboardPwd.Companion.ONE_DAY_PWD)
                                            CommandUtil_V2S.synPwd(command.getLockType(), pwds, 0)
                                        }
                                        else -> {}
                                    }
                                    else -> CommandUtil.checkRandom(
                                        command.getLockType(),
                                        unlockKey,
                                        psFromLock,
                                        aesKeyArray
                                    )
                                }
                            } // A指令结束
                            Command.Companion.COMM_SET_ADMIN_KEYBOARD_PWD -> if (currentAPICommand == APICommand.OP_ADD_ADMIN) { // 补充管理员后续指令
                                if (command.getLockType() == LockType.LOCK_TYPE_V3) // 三代没有删除码
                                    CommandUtil_V3.initPasswords(
                                        command.getLockType(),
                                        aesKeyArray,
                                        currentAPICommand
                                    ) else {
                                    deletePwd = DigitUtil.generatePwdByLength(7)
                                    CommandUtil.D_setDeletePassword(
                                        command.getLockType(),
                                        deletePwd,
                                        aesKeyArray
                                    )
                                }
                            } else {
                                val mModifyAdminPwd: LockCallback? =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (mModifyAdminPwd != null) {
                                    (mModifyAdminPwd as ModifyAdminPasscodeCallback).onModifyAdminPasscodeSuccess(
                                        newPwd
                                    )
                                }
                            }
                            Command.Companion.COMM_SET_DELETE_PWD -> {
                                LogUtil.d("set delete pwd success", DBG)
                                if (currentAPICommand == APICommand.OP_ADD_ADMIN) { // 添加管理员接后续指令
                                    when (command.getLockType()) {
                                        LockType.LOCK_TYPE_V2S_PLUS -> {
                                            pwdData = ByteArray(1624)
                                            generateTransmissionData(
                                                command.getScene().toInt(),
                                                pwdData,
                                                validPwdNum
                                            )
                                            dataPos = 0
                                            CommandUtil_V2S_PLUS.synPwd(
                                                command.getLockType(),
                                                Arrays.copyOfRange(
                                                    pwdData,
                                                    dataPos,
                                                    dataPos + packetLen
                                                ),
                                                dataPos,
                                                aesKeyArray
                                            )
                                        }
                                        LockType.LOCK_TYPE_V2S -> {
                                            pwdList = LinkedList<String>()
                                            val pwds =
                                                generatePwd(KeyboardPwd.Companion.ONE_DAY_PWD)
                                            CommandUtil_V2S.synPwd(command.getLockType(), pwds, 0)
                                        }
                                    }
                                } else {
//                                    TTLockAPI.getTTLockCallback().onSetDeletePassword(mExtendedBluetoothDevice, deletePwd, error);
                                }
                            }
                            Command.Companion.COMM_SYN_KEYBOARD_PWD -> {
                                when (command.getLockType()) {
                                    LockType.LOCK_TYPE_V2S_PLUS -> {
                                        dataPos += packetLen
                                        if (dataPos + 1 < pwdData.size) {
                                            CommandUtil_V2S_PLUS.synPwd(
                                                command.getLockType(),
                                                Arrays.copyOfRange(
                                                    pwdData,
                                                    dataPos,
                                                    dataPos + packetLen
                                                ),
                                                dataPos,
                                                aesKeyArray
                                            )
                                        } else {
                                            LogUtil.e("LOCK_TYPE_V2S_PLUS", DBG)
                                            // TODO:添加管理员回调
                                            if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                                lockData = getLockInfoObj()
                                                lockData?.lockVersion = command.getLockVersion()
                                                lockData?.specialValue = 1
                                                val mInitCallback: LockCallback? =
                                                    LockCallbackManager.Companion.getInstance()
                                                        .getCallback()
                                                if (mInitCallback != null) {
                                                    (mInitCallback as InitLockCallback).onInitLockSuccess(
                                                        lockData!!.encodeLockData()
                                                    )
                                                }
                                                disconnect()
                                            } else {
                                                val mResetPasscodeCallback: LockCallback? =
                                                    LockCallbackManager.Companion.getInstance()
                                                        .getCallback()
                                                lockData!!.pwdInfo = pwdInfo
                                                lockData!!.timestamp = timestamp
                                                (mResetPasscodeCallback as ResetPasscodeCallback).onResetPasscodeSuccess(
                                                    lockData!!.encodeLockData()
                                                )
                                            }
                                        }
                                    }
                                    LockType.LOCK_TYPE_V2S -> {
                                        // 900个密码
                                        val seq = pwdList!!.size
                                        var pwdType = 0
                                        if (seq < 300) pwdType = 1
                                        else if (seq < 450) pwdType = 2
                                        else if (seq < 550) pwdType = 3
                                        else if (seq < 650) pwdType = 4
                                        else if (seq < 700) pwdType = 5
                                        else if (seq < 750) pwdType = 6
                                        else if (seq < 800) pwdType = 7
                                        else if (seq < 900) pwdType = 8
                                        else {
                                            try {
                                                val pwdInfoOrigin: String =
                                                    DigitUtil.generateKeyboardPwd_Json(pwdList!!)
                                                timestamp = System.currentTimeMillis()
                                                pwdInfo =
                                                    CommandUtil.encry(pwdInfoOrigin, timestamp)
                                                LogUtil.d("pwdInfoOrigin:$pwdInfoOrigin", DBG)
                                                // TODO:开门回调
                                                if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                                    val lockData: LockData? = getLockInfoObj()
                                                    lockData!!.lockVersion =
                                                        command.getLockVersion()
                                                    lockData!!.specialValue = 1
                                                    val mInitLockCallback: LockCallback? =
                                                        LockCallbackManager.Companion.getInstance()
                                                            .getCallback()
                                                    if (mInitLockCallback != null) {
                                                        (mInitLockCallback as InitLockCallback).onInitLockSuccess(
                                                            lockData.encodeLockData()
                                                        )
                                                    }
                                                } else {
                                                    val mResetPasscodeCallback: LockCallback? =
                                                        LockCallbackManager.Companion.getInstance()
                                                            .getCallback()
                                                    lockData!!.pwdInfo = pwdInfo
                                                    lockData!!.timestamp = timestamp
                                                    (mResetPasscodeCallback as ResetPasscodeCallback).onResetPasscodeSuccess(
                                                        lockData!!.encodeLockData()
                                                    )
                                                }
                                            } catch (e: JSONException) {
                                                e.printStackTrace()
                                            }
                                        }
                                        if (seq < 900) {
                                            val pwds = generatePwd(pwdType)
                                            CommandUtil_V2S.synPwd(command.getLockType(), pwds, seq)
                                        }
                                    }
                                }
                            }
                            Command.Companion.COMM_CHECK_RANDOM -> {
                                // 随机数验证
                                LogUtil.d("api:$currentAPICommand")
                                isCheckedLockPermission = true
                                when (currentAPICommand) {
                                    APICommand.OP_SET_KEYBOARD_PASSWORD -> CommandUtil.S_setAdminKeyboardPwd(
                                        command.getLockType(),
                                        newPwd,
                                        aesKeyArray
                                    )
                                    APICommand.OP_SET_DELETE_PASSWORD -> CommandUtil.D_setDeletePassword(
                                        command.getLockType(),
                                        deletePwd,
                                        aesKeyArray
                                    )
                                    APICommand.OP_INIT_PWD -> CommandUtil_V3.initPasswords(
                                        command.getLockType(),
                                        aesKeyArray,
                                        APICommand.OP_RESET_KEYBOARD_PASSWORD
                                    )
                                    APICommand.OP_SET_LOCK_NAME -> //                                    CommandUtil.N_setLockname(command.getLockType(), lockname, aesKeyArray);
                                        CommandUtil.AT_setLockname(
                                            command.getLockType(),
                                            lockname!!,
                                            aesKeyArray
                                        )
                                    APICommand.OP_RESET_LOCK -> CommandUtil.R_resetLock(command.getLockType())
                                    APICommand.OP_ADD_ONCE_KEYBOARD_PASSWORD -> CommandUtil.manageKeyboardPassword(
                                        command.getLockType(),
                                        PwdOperateType.PWD_OPERATE_TYPE_ADD,
                                        KeyboardPwdType.PWD_TYPE_COUNT,
                                        originalPwd,
                                        "",
                                        startDate,
                                        endDate,
                                        aesKeyArray,
                                        timezoneOffSet
                                    )
                                    APICommand.OP_ADD_PERIOD_KEYBOARD_PASSWORD -> CommandUtil.manageKeyboardPassword(
                                        command.getLockType(),
                                        PwdOperateType.PWD_OPERATE_TYPE_ADD,
                                        KeyboardPwdType.PWD_TYPE_PERIOD,
                                        originalPwd,
                                        "",
                                        startDate,
                                        endDate,
                                        aesKeyArray,
                                        timezoneOffSet
                                    )
                                    APICommand.OP_ADD_PERMANENT_KEYBOARD_PASSWORD -> CommandUtil.manageKeyboardPassword(
                                        command.getLockType(),
                                        PwdOperateType.PWD_OPERATE_TYPE_ADD,
                                        KeyboardPwdType.PWD_TYPE_PERMANENT,
                                        originalPwd,
                                        "",
                                        startDate,
                                        endDate,
                                        aesKeyArray,
                                        timezoneOffSet
                                    )
                                    APICommand.OP_MODIFY_KEYBOARD_PASSWORD -> CommandUtil.manageKeyboardPassword(
                                        command.getLockType(),
                                        PwdOperateType.PWD_OPERATE_TYPE_MODIFY,
                                        keyboardPwdType,
                                        originalPwd,
                                        newPwd,
                                        startDate,
                                        endDate,
                                        aesKeyArray,
                                        timezoneOffSet
                                    )
                                    APICommand.OP_REMOVE_ONE_PASSWORD -> CommandUtil.manageKeyboardPassword(
                                        command.getLockType(),
                                        PwdOperateType.PWD_OPERATE_TYPE_REMOVE_ONE,
                                        keyboardPwdType,
                                        originalPwd,
                                        newPwd,
                                        0,
                                        0,
                                        aesKeyArray
                                    )
                                    APICommand.OP_BATCH_DELETE_PASSWORD -> {
                                        dataPos = 0
                                        CommandUtil.manageKeyboardPassword(
                                            command.getLockType(),
                                            PwdOperateType.PWD_OPERATE_TYPE_REMOVE_ONE,
                                            keyboardPwdType,
                                            pwds!![dataPos],
                                            newPwd,
                                            0,
                                            0,
                                            aesKeyArray
                                        )
                                    }
                                    APICommand.OP_REMOVE_ALL_KEYBOARD_PASSWORD -> CommandUtil.manageKeyboardPassword(
                                        command.getLockType(),
                                        PwdOperateType.PWD_OPERATE_TYPE_CLEAR,
                                        0.toByte(),
                                        "",
                                        "",
                                        0,
                                        0,
                                        aesKeyArray
                                    )
                                    APICommand.OP_CALIBRATE_TIME -> CommandUtil.C_calibationTime(
                                        command.getLockType(),
                                        calibationTime,
                                        timezoneOffSet,
                                        aesKeyArray!!
                                    )
                                    APICommand.OP_GET_DEVICE_INFO, APICommand.OP_SEARCH_DEVICE_FEATURE -> CommandUtil.searchDeviceFeature(
                                        command.getLockType()
                                    )
                                    APICommand.OP_SEARCH_IC_CARD_NO -> {
                                        icCards = ArrayList<ICCard>()
                                        CommandUtil.searchICCardNo(
                                            command.getLockType(),
                                            0.toShort(),
                                            aesKeyArray
                                        )
                                    }
                                    APICommand.OP_SEARCH_FR -> {
                                        frs = ArrayList<FR>()
                                        CommandUtil.searchFRNo(
                                            command.getLockType(),
                                            0.toShort(),
                                            aesKeyArray
                                        )
                                    }
                                    APICommand.OP_SEARCH_PWD -> {
                                        passcodes = ArrayList<Passcode>()
                                        CommandUtil.searchPasscode(
                                            command.getLockType(),
                                            0.toShort(),
                                            aesKeyArray
                                        )
                                    }
                                    APICommand.OP_ADD_IC -> CommandUtil.addICCard(
                                        command.getLockType(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_MODIFY_IC_PERIOD -> CommandUtil.modifyICCardPeriod(
                                        command.getLockType(),
                                        attachmentNum.toString(),
                                        startDate,
                                        endDate,
                                        aesKeyArray,
                                        timezoneOffSet
                                    )
                                    APICommand.OP_DELETE_IC -> CommandUtil.deleteICCard(
                                        command.getLockType(),
                                        attachmentNum.toString(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_LOSS_IC -> CommandUtil.deleteICCard(
                                        command.getLockType(),
                                        attachmentNum.toString(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_CLEAR_IC -> CommandUtil.clearICCard(
                                        command.getLockType(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_SET_WRIST_KEY -> CommandUtil.setWristbandKey(
                                        command.getLockType(),
                                        wristbandKey,
                                        aesKeyArray
                                    )
                                    APICommand.OP_ADD_FR -> CommandUtil.addFR(
                                        command.getLockType(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_MODIFY_FR_PERIOD -> {
                                        LogUtil.d("attachmentNum:$attachmentNum", DBG)
                                        CommandUtil.modifyFRPeriod(
                                            command.getLockType(),
                                            attachmentNum,
                                            startDate,
                                            endDate,
                                            aesKeyArray,
                                            timezoneOffSet
                                        )
                                    }
                                    APICommand.OP_DELETE_FR -> CommandUtil.deleteFR(
                                        command.getLockType(),
                                        attachmentNum,
                                        aesKeyArray
                                    )
                                    APICommand.OP_CLEAR_FR -> CommandUtil.clearFR(
                                        command.getLockType(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_SEARCH_AUTO_LOCK_PERIOD -> CommandUtil.searchAutoLockTime(
                                        command.getLockType(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_DOOR_SENSOR -> CommandUtil_V3.operateDoorSensor(
                                        command,
                                        transferData!!.getOp().toByte(),
                                        transferData!!.getOpValue().toByte(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_SET_AUTO_LOCK_TIME -> CommandUtil.modifyAutoLockTime(
                                        command.getLockType(),
                                        calibationTime.toShort(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_ENTER_DFU_MODE -> CommandUtil.enterDFUMode(
                                        command.getLockType(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_SHOW_PASSWORD_ON_SCREEN -> CommandUtil.screenPasscodeManage(
                                        command.getLockType(),
                                        transferData!!.getOp(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_READ_PWD_PARA -> CommandUtil.readPwdPara(
                                        command.getLockType(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_CONTROL_REMOTE_UNLOCK -> CommandUtil_V3.controlRemoteUnlock(
                                        command,
                                        transferData!!.getOp().toByte(),
                                        transferData!!.getOpValue().toByte(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_AUDIO_MANAGEMENT -> CommandUtil_V3.audioManage(
                                        command,
                                        transferData!!.getOp().toByte(),
                                        transferData!!.getOpValue().toByte(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_SET_LOCK_SOUND -> CommandUtil_V3.setLockSound(
                                        command,
                                        transferData!!
                                    )
                                    APICommand.OP_GET_LOCK_SOUND -> CommandUtil_V3.getLockSound(
                                        command,
                                        transferData!!
                                    )
                                    APICommand.OP_REMOTE_CONTROL_DEVICE_MANAGEMENT -> {
                                        isWaitCommand = true // 不主动断开蓝牙
                                        var psFromLockL: Long = 0
                                        if (psFromLock != null) psFromLockL =
                                            DigitUtil.fourBytesToLong(psFromLock!!)
                                        var unlockKeyL: Long = 0
                                        if (!TextUtils.isEmpty(unlockKey)) {
                                            unlockKeyL = java.lang.Long.valueOf(unlockKey)
                                        }
                                        unlockPwdBytes =
                                            DigitUtil.getUnlockPwdBytes_new(psFromLockL, unlockKeyL)
                                        uniqueidBytes =
                                            DigitUtil.integerToByteArray((System.currentTimeMillis() / 1000).toInt())
                                        CommandUtil_V3.remoteControlManage(
                                            command,
                                            transferData!!.getOp().toByte(),
                                            transferData!!.getOpValue().toByte(),
                                            unlockPwdBytes,
                                            uniqueidBytes,
                                            aesKeyArray
                                        )
                                        isCanSendCommandAgain = true
                                    }
                                    APICommand.OP_GET_ADMIN_KEYBOARD_PASSWORD -> CommandUtil_V3.getAdminCode(
                                        command
                                    )
                                    APICommand.OP_WRITE_FR -> {
                                        transferData!!.setTransferData(
                                            DigitUtil.hexString2ByteArray(
                                                transferData!!.getJson()
                                            )
                                        )
                                        // TODO:
                                        CommandUtil_V3.addFRTemp(command, transferData!!)
                                    }
                                    APICommand.OP_RECOVERY_DATA -> {
                                        recoveryDatas = toObject(
                                            transferData!!.getJson(),
                                            object : TypeToken<ArrayList<RecoveryData>?>() {}
                                        )
                                        LogUtil.e(
                                            "transferData.getJson():" + transferData!!.getJson(),
                                            true
                                        )
                                        LogUtil.e(
                                            "transferData.getOp():" + transferData!!.getOp(),
                                            true
                                        )
                                        if (recoveryDatas == null || recoveryDatas!!.size == 0) {
                                            LogUtil.d("recoveryDatas is null", DBG)
                                            val mRecoveryDataCallback: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (mRecoveryDataCallback != null) {
                                                (mRecoveryDataCallback as RecoverLockDataCallback).onRecoveryDataSuccess(
                                                    transferData!!.getOp()
                                                )
                                            }
                                        } else {
                                            dataPos = 0
                                            recoveryData = recoveryDatas!![0]
                                            when (transferData!!.getOp()) {
                                                RecoveryDataType.PASSCODE -> CommandUtil.manageKeyboardPassword(
                                                    command.getLockType(),
                                                    PwdOperateType.PWD_OPERATE_TYPE_RECOVERY,
                                                    (if (recoveryData!!.keyboardPwdType == 2) 1 else recoveryData!!.keyboardPwdType).toByte(),
                                                    recoveryData!!.cycleType,
                                                    recoveryData!!.keyboardPwd,
                                                    recoveryData!!.keyboardPwd,
                                                    recoveryData!!.startDate,
                                                    recoveryData!!.endDate,
                                                    aesKeyArray,
                                                    timezoneOffSet
                                                )
                                                RecoveryDataType.IC_CARD -> CommandUtil.recoveryICCardPeriod(
                                                    command.getLockType(),
                                                    recoveryData!!.cardNumber,
                                                    recoveryData!!.startDate,
                                                    recoveryData!!.endDate,
                                                    aesKeyArray,
                                                    timezoneOffSet
                                                )
                                                RecoveryDataType.FINGERPRINT -> CommandUtil.recoveryFRPeriod(
                                                    command.getLockType(),
                                                    java.lang.Long.valueOf(recoveryData!!.fingerprintNumber),
                                                    recoveryData!!.startDate,
                                                    recoveryData!!.endDate,
                                                    aesKeyArray,
                                                    timezoneOffSet
                                                )
                                                else -> {}
                                            }
                                        }
                                    }
                                    APICommand.OP_CONFIGURE_NB_SERVER_ADDRESS -> {
                                        LogUtil.d("config NB")
                                        CommandUtil_V3.configureNBServerAddress(transferData!!)
                                    }
                                    APICommand.OP_QUERY_PASSAGE_MODE -> {
                                        LogUtil.d("send query passage mode")
                                        passageModeDatas = ArrayList()
                                        CommandUtil_V3.queryPassageMode(
                                            command,
                                            0.toByte(),
                                            aesKeyArray
                                        )
                                    }
                                    APICommand.OP_ADD_OR_MODIFY_PASSAGE_MODE -> try {
                                        dataPos = 0
                                        weerOrDays = JSONArray(transferData!!.getJson())
                                        if (dataPos < weerOrDays!!.length()) CommandUtil_V3.configurePassageMode(
                                            command,
                                            transferData!!,
                                            (weerOrDays!!.get(dataPos) as Int).toByte()
                                        )
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    APICommand.OP_DELETE_PASSAGE_MODE -> try {
                                        dataPos = 0
                                        weerOrDays = JSONArray(transferData!!.getJson())
                                        if (dataPos < weerOrDays!!.length()) CommandUtil_V3.deletePassageMode(
                                            command,
                                            transferData!!,
                                            (weerOrDays!!.get(dataPos) as Int).toByte()
                                        )
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    APICommand.OP_CLEAR_PASSAGE_MODE -> CommandUtil_V3.clearPassageMode(
                                        command,
                                        transferData!!
                                    )
                                    APICommand.OP_FREEZE_LOCK -> CommandUtil_V3.controlFreezeLock(
                                        command,
                                        transferData!!.getOp().toByte(),
                                        transferData!!.getOpValue().toByte(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_LOCK_LAMP -> CommandUtil_V3.controlLamp(
                                        command,
                                        transferData!!.getOp().toByte(),
                                        transferData!!.getOpValue().toShort(),
                                        aesKeyArray
                                    )
                                    APICommand.OP_SET_HOTEL_DATA -> if (hotelData != null && transferData != null && aesKeyArray != null) { // 取电添加成功后 先后设置工作模式跟关连锁时
                                        CommandUtil_V3.configureHotelData(
                                            command,
                                            HotelData.Companion.SET,
                                            hotelData!!.getParaType().toInt(),
                                            transferData!!.getHotelData()!!,
                                            aesKeyArray
                                        )
                                    } else {
                                        LogUtil.w("hotelData:$hotelData")
                                        LogUtil.w("transferData:$transferData")
                                        LogUtil.w("aesKeyArray:" + aesKeyArray)
                                    }
                                    APICommand.OP_GET_HOTEL_DATA -> CommandUtil_V3.configureHotelData(
                                        command,
                                        HotelData.Companion.GET,
                                        hotelData!!.getParaType().toInt(),
                                        transferData!!.getHotelData()!!,
                                        aesKeyArray
                                    )
                                    APICommand.OP_SET_HOTEL_CARD_SECTION -> CommandUtil_V3.configureHotelData(
                                        command,
                                        HotelData.Companion.SET,
                                        HotelData.Companion.TYPE_SECTOR.toInt(),
                                        transferData!!.getHotelData()!!,
                                        aesKeyArray
                                    )
                                    APICommand.OP_SET_ELEVATOR_CONTROL_FLOORS -> CommandUtil_V3.configureHotelData(
                                        command,
                                        HotelData.Companion.SET,
                                        HotelData.Companion.TYPE_ELEVATOR_CONTROLABLE_FLOORS.toInt(),
                                        transferData!!.getHotelData()!!,
                                        aesKeyArray
                                    )
                                    APICommand.OP_SET_ELEVATOR_WORK_MODE -> CommandUtil_V3.configureHotelData(
                                        command,
                                        HotelData.Companion.SET,
                                        HotelData.Companion.TYPE_ELEVATOR_WORK_MODE.toInt(),
                                        transferData!!.getHotelData()!!,
                                        aesKeyArray
                                    )
                                    APICommand.OP_ADD_DOOR_SENSOR -> CommandUtil_V3.addDoorSensor(
                                        command,
                                        transferData!!
                                    )
                                    APICommand.OP_DELETE_DOOR_SENSOR -> CommandUtil_V3.deleteDoorSensor(
                                        command,
                                        transferData!!
                                    )
                                    APICommand.OP_SET_SWITCH, APICommand.OP_GET_SWITCH, APICommand.OP_SET_UNLOCK_DIRECTION, APICommand.OP_GET_UNLOCK_DIRECTION -> CommandUtil_V3.getSwitchState(
                                        command,
                                        aesKeyArray
                                    )
                                    APICommand.OP_DEAD_LOCK -> CommandUtil_V3.deadLock(
                                        command,
                                        unlockKey,
                                        psFromLock!!,
                                        aesKeyArray,
                                        unlockDate
                                    )
                                    APICommand.OP_GET_NB_ACTIVATE_MODE -> CommandUtil_V3.getNBActivateConfig(
                                        command,
                                        NBAwakeConfig.Companion.ACTION_AWAKE_MODE,
                                        aesKeyArray
                                    )
                                    APICommand.OP_GET_NB_ACTIVATE_CONFIG -> CommandUtil_V3.getNBActivateConfig(
                                        command,
                                        NBAwakeConfig.Companion.ACTION_AWAKE_TIME,
                                        aesKeyArray
                                    )
                                    APICommand.OP_SET_NB_ACTIVATE_MODE -> CommandUtil_V3.setNBActivateConfig(
                                        command,
                                        NBAwakeConfig.Companion.ACTION_AWAKE_MODE,
                                        transferData!!.getNbAwakeConfig()!!,
                                        aesKeyArray
                                    )
                                    APICommand.OP_SET_NB_ACTIVATE_CONFIG -> CommandUtil_V3.setNBActivateConfig(
                                        command,
                                        NBAwakeConfig.Companion.ACTION_AWAKE_TIME,
                                        transferData!!.getNbAwakeConfig()!!,
                                        aesKeyArray
                                    )
                                    APICommand.OP_ADD_KEY_FOB -> CommandUtil_V3.addKeyFob(
                                        command,
                                        transferData,
                                        aesKeyArray
                                    )
                                    APICommand.OP_MODIFY_KEY_FOB_PERIOD -> CommandUtil_V3.modifyKeyFob(
                                        command,
                                        transferData,
                                        aesKeyArray
                                    )
                                    APICommand.OP_DELETE_KEY_FOB -> CommandUtil_V3.deleteKeyFob(
                                        command,
                                        transferData,
                                        aesKeyArray
                                    )
                                    APICommand.OP_CLEAR_KEY_FOB -> CommandUtil_V3.clearKeyFob(
                                        command,
                                        aesKeyArray
                                    )
                                    else -> {}
                                }
                            }
                            Command.Companion.COMM_MANAGE_KEYBOARD_PASSWORD -> {
                                when (data[3]) {
                                    PwdOperateType.PWD_OPERATE_TYPE_ADD -> {
                                        when (currentAPICommand) {
                                            APICommand.OP_ADD_ONCE_KEYBOARD_PASSWORD ->
                                                keyboardPwdType =
                                                    KeyboardPwdType.PWD_TYPE_PERIOD // 返回类型换一下,同服务端一致
                                            APICommand.OP_ADD_PERIOD_KEYBOARD_PASSWORD ->
                                                keyboardPwdType =
                                                    KeyboardPwdType.PWD_TYPE_COUNT // 返回类型换一下,同服务端一致
                                            APICommand.OP_ADD_PERMANENT_KEYBOARD_PASSWORD ->
                                                keyboardPwdType =
                                                    KeyboardPwdType.PWD_TYPE_PERMANENT
                                            else -> {}
                                        }
                                        val mCreatePasscodeCallback: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mCreatePasscodeCallback != null) {
                                            (mCreatePasscodeCallback as CreateCustomPasscodeCallback).onCreateCustomPasscodeSuccess(
                                                originalPwd
                                            )
                                        }
                                    }
                                    PwdOperateType.PWD_OPERATE_TYPE_REMOVE_ONE -> if (currentAPICommand == APICommand.OP_BATCH_DELETE_PASSWORD) { // 批量删除 TODO:处理失败情况
                                        dataPos++
                                        if (dataPos < pwds!!.size) {
                                            CommandUtil.manageKeyboardPassword(
                                                command.getLockType(),
                                                PwdOperateType.PWD_OPERATE_TYPE_REMOVE_ONE,
                                                keyboardPwdType,
                                                pwds!![dataPos],
                                                newPwd,
                                                0,
                                                0,
                                                aesKeyArray
                                            )
                                        }
                                        //                                        else TTLockAPI.getTTLockCallback().onDeleteKeyboardPasswords(mExtendedBluetoothDevice, pwds, error);
                                    } else {
                                        val mDeletePwdCallback: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mDeletePwdCallback != null) {
                                            (mDeletePwdCallback as DeletePasscodeCallback).onDeletePasscodeSuccess()
                                        }
                                    }
                                    PwdOperateType.PWD_OPERATE_TYPE_MODIFY -> {
                                        val mModifyCallback: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mModifyCallback != null) {
                                            (mModifyCallback as ModifyPasscodeCallback).onModifyPasscodeSuccess()
                                        }
                                    }
                                    PwdOperateType.PWD_OPERATE_TYPE_RECOVERY -> {
                                        dataPos++
                                        LogUtil.e("dataPos:$dataPos", DBG)
                                        if (dataPos < recoveryDatas!!.size) {
                                            val recoveryData: RecoveryData =
                                                recoveryDatas!![dataPos]
                                            CommandUtil.manageKeyboardPassword(
                                                command.getLockType(),
                                                PwdOperateType.PWD_OPERATE_TYPE_RECOVERY,
                                                (if (recoveryData!!.keyboardPwdType == 2) 1 else recoveryData!!.keyboardPwdType).toByte(),
                                                recoveryData!!.cycleType,
                                                recoveryData!!.keyboardPwd,
                                                recoveryData!!.keyboardPwd,
                                                recoveryData!!.startDate,
                                                recoveryData!!.endDate,
                                                aesKeyArray,
                                                timezoneOffSet
                                            )
                                        } else {
                                            val mRecoveryDataCallback: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (mRecoveryDataCallback != null) {
                                                (mRecoveryDataCallback as RecoverLockDataCallback).onRecoveryDataSuccess(
                                                    transferData!!.getOp()
                                                )
                                            }
                                        }
                                    }
                                    else -> {}
                                }
                            }
                            Command.Companion.COMM_RESET_LOCK -> {
                                // R重置锁
                                LogUtil.d("reset")
                                val mResetLockCallback: LockCallback? =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (mResetLockCallback != null) {
                                    (mResetLockCallback as ResetLockCallback).onResetLockSuccess()
                                }
                            }
                            Command.Companion.COMM_INIT_PASSWORDS -> {
                                // 初始化密码
                                LogUtil.d(
                                    "currentAPICommand:$currentAPICommand COMM_INIT_PASSWORDS",
                                    DBG
                                )
                                if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                    // todo:流程改了
                                    // TODO:写酒店锁数据
                                    if (hotelData != null) {
                                        isWaitCommand = true
                                        CommandUtil_V3.configureHotelData(
                                            command,
                                            HotelData.Companion.SET,
                                            HotelData.Companion.TYPE_IC_KEY.toInt(),
                                            hotelData!!,
                                            aesKeyArray
                                        )
                                        // TODO:如果是酒店锁就不会写这个数据了
                                    } else if (FeatureValueUtil.isSupportFeature(
                                            lockData,
                                            FeatureValue.CONFIG_GATEWAY_UNLOCK
                                        )
                                    ) {
                                        CommandUtil_V3.controlRemoteUnlock(
                                            command,
                                            ConfigRemoteUnlock.OP_TYPE_SEARCH,
                                            ConfigRemoteUnlock.OP_CLOSE,
                                            aesKeyArray
                                        )
                                    } else {
                                        readModelNumber(command)
                                        //                                        CommandUtil.operateFinished(command.getLockType());
                                    }
                                } else {
                                    val mResetPasscodeCallback: LockCallback? =
                                        LockCallbackManager.Companion.getInstance().getCallback()
                                    lockData?.pwdInfo = pwdInfo
                                    lockData?.timestamp = timestamp
                                    (mResetPasscodeCallback as ResetPasscodeCallback).onResetPasscodeSuccess(
                                        lockData!!.encodeLockData()
                                    )
                                }
                            }
                            Command.Companion.COMM_GET_LOCK_TIME -> {
                                val calendar: Calendar = Calendar.getInstance()
                                calendar.set(
                                    2000 + data[2],
                                    data[3] - 1,
                                    data[4].toInt(),
                                    data[5].toInt(),
                                    data[6].toInt(),
                                    data[7].toInt()
                                )
                                LogUtil.d(
                                    data[2].toString() + ":" + data[3] + ":" + data[4] + ":" + data[5] + ":" + data[6],
                                    DBG
                                )
                                // 根据时间偏移量计算时间
                                val timeZone: TimeZone = TimeZone.getDefault()
                                LogUtil.d("timezoneOffSet:$timezoneOffSet", DBG)
                                if (timeZone.inDaylightTime(Date(System.currentTimeMillis()))) timezoneOffSet -= timeZone.getDSTSavings()
                                    .toLong()
                                timeZone.setRawOffset(timezoneOffSet.toInt())
                                calendar.setTimeZone(timeZone)
                                LogUtil.d(
                                    "calendar.getTimeInMillis():" + calendar.getTimeInMillis(),
                                    DBG
                                )
                                val mTimeCallback: LockCallback? =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (mTimeCallback != null) {
                                    (mTimeCallback as GetLockTimeCallback).onGetLockTimeSuccess(
                                        calendar.getTimeInMillis()
                                    )
                                }
                            }
                            Command.Companion.COMM_CHECK_USER_TIME -> {
                                // U 校验用户期限
                                val len = data.size - 2
                                psFromLock = ByteArray(len)
                                System.arraycopy(data, 2, psFromLock, 0, len)
                                when (currentAPICommand) {
                                    APICommand.OP_ACTIVATE_FLOORS -> CommandUtil_V3.activateLiftFloors(
                                        command,
                                        psFromLock!!,
                                        transferData!!
                                    )
                                    APICommand.OP_UNLOCK_ADMIN, APICommand.OP_UNLOCK_EKEY -> //                                    unlockDate = System.currentTimeMillis();
                                        CommandUtil.G_unlock(
                                            command.getLockType(),
                                            unlockKey,
                                            psFromLock,
                                            aesKeyArray,
                                            0,
                                            timezoneOffSet
                                        )
                                    APICommand.OP_LOCK_ADMIN, APICommand.OP_LOCK_EKEY -> {
                                        val lockType = command.getLockType()
                                        if (lockType == LockType.LOCK_TYPE_V3_CAR || lockType == LockType.LOCK_TYPE_V3) {
                                            CommandUtil.lock(
                                                command.getLockType(),
                                                unlockKey,
                                                psFromLock,
                                                aesKeyArray,
                                                unlockDate
                                            )
                                        } else {
                                            CommandUtil.L_lock(
                                                command.getLockType(),
                                                unlockKey,
                                                psFromLock,
                                                aesKeyArray
                                            )
                                        }
                                    }
                                    APICommand.OP_CALIBRATE_TIME -> {
                                        /**
                                         * 三代锁要校验随机数
                                         */
                                        /**
                                         * 三代锁要校验随机数
                                         */
                                        /**
                                         * 三代锁要校验随机数
                                         */
                                        /**
                                         * 三代锁要校验随机数
                                         */
                                        if (command.getLockType() == LockType.LOCK_TYPE_V3 || command.getLockType() == LockType.LOCK_TYPE_V3_CAR) CommandUtil.checkRandom(
                                            command.getLockType(),
                                            unlockKey,
                                            psFromLock,
                                            aesKeyArray
                                        ) else CommandUtil.C_calibationTime(
                                            command.getLockType(),
                                            calibationTime,
                                            timezoneOffSet,
                                            aesKeyArray!!
                                        )
                                    }
                                    APICommand.OP_LOCK -> CommandUtil.lock(
                                        command.getLockType(),
                                        unlockKey,
                                        psFromLock,
                                        aesKeyArray,
                                        unlockDate
                                    )
                                    APICommand.OP_READ_PWD_PARA, APICommand.OP_REMOTE_CONTROL_DEVICE_MANAGEMENT -> CommandUtil.checkRandom(
                                        command.getLockType(),
                                        unlockKey,
                                        psFromLock,
                                        aesKeyArray
                                    )
                                    else -> {}
                                }
                            }
                            Command.Companion.COMM_UNLOCK -> {
                                // G

                                // 默认没电量的情况
                                var battery: Byte = -1
                                // 表示不存在用户
                                var uid = 0
                                val calendar: Calendar = Calendar.getInstance()
                                // 门锁时间 默认设置成门锁时间
                                var lockTime: Long = calendar.getTimeInMillis()
                                // 记录唯一标识   默认使用门锁时间
                                var uniqueid = (lockTime / 1000).toInt()
                                val len = data.size
                                if (len > 2) { // 电量
                                    battery = data[2]
                                    if (len >= 17) {
                                        uid = DigitUtil.fourBytesToLong(
                                            Arrays.copyOfRange(
                                                data,
                                                3,
                                                7
                                            )
                                        )
                                            .toInt()
                                        uniqueid =
                                            DigitUtil.fourBytesToLong(
                                                Arrays.copyOfRange(
                                                    data,
                                                    7,
                                                    11
                                                )
                                            )
                                                .toInt()
                                        calendar.set(
                                            2000 + data[11],
                                            data[12] - 1,
                                            data[13].toInt(),
                                            data[14].toInt(),
                                            data[15].toInt(),
                                            data[16].toInt()
                                        )
                                        // 根据时间偏移量计算时间
                                        val timeZone: TimeZone = TimeZone.getDefault()
                                        LogUtil.d("timezoneOffSet:$timezoneOffSet", DBG)
                                        if (timeZone.inDaylightTime(Date(System.currentTimeMillis()))) timezoneOffSet -= timeZone.getDSTSavings()
                                            .toLong()
                                        timeZone.setRawOffset(timezoneOffSet.toInt())
                                        calendar.setTimeZone(timeZone)
                                        lockTime = calendar.getTimeInMillis()
                                    }
                                }
                                mExtendedBluetoothDevice!!.setBatteryCapacity(battery)
                                when (currentAPICommand) {
                                    APICommand.OP_ACTIVATE_FLOORS -> {
                                        val mControlLockCallback: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mControlLockCallback != null) {
                                            (mControlLockCallback as ActivateLiftFloorsCallback).onActivateLiftFloorsSuccess(
                                                ActivateLiftFloorsResult(
                                                    battery.toInt(),
                                                    uniqueid,
                                                    lockTime
                                                )
                                            )
                                        }
                                    }
                                    else ->
                                        /**
                                         * 车位锁进行相反回调
                                         */
                                        /**
                                         * 车位锁进行相反回调
                                         */
                                        /**
                                         * 车位锁进行相反回调
                                         */
                                        /**
                                         * 车位锁进行相反回调
                                         */
                                        if (command.getLockType() == LockType.LOCK_TYPE_CAR) {
                                            val mControlLockCallback: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (mControlLockCallback != null) {
                                                (mControlLockCallback as ControlLockCallback).onControlLockSuccess(
                                                    ControlLockResult(
                                                        ControlAction.LOCK,
                                                        battery.toInt(),
                                                        uniqueid,
                                                        lockTime
                                                    )
                                                )
                                            }
                                        } else {
                                            val mControlLockCallback: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (mControlLockCallback != null) {
                                                (mControlLockCallback as ControlLockCallback).onControlLockSuccess(
                                                    ControlLockResult(
                                                        ControlAction.UNLOCK,
                                                        battery.toInt(),
                                                        uniqueid,
                                                        lockTime
                                                    )
                                                )
                                            }
                                        }
                                }
                            }
                            Command.Companion.COMM_LOCK -> {
                                // L
                                if (data.size > 2) // 电量
                                    battery = data[2]
                                mExtendedBluetoothDevice!!.setBatteryCapacity(battery)
                                // 车位锁进行相反回调
                                val mControlLockCallback: LockCallback? =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (mControlLockCallback != null) {
                                    (mControlLockCallback as ControlLockCallback).onControlLockSuccess(
                                        ControlLockResult(
                                            ControlAction.UNLOCK,
                                            battery.toInt(),
                                            (System.currentTimeMillis() / 1000).toInt(),
                                            System.currentTimeMillis()
                                        )
                                    )
                                }
                            }
                            Command.Companion.COMM_TIME_CALIBRATE -> if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                initLock(command)
                            } else {
                                val mSetTimeCallback: LockCallback? =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (mSetTimeCallback != null) {
                                    (mSetTimeCallback as SetLockTimeCallback).onSetTimeSuccess()
                                }
                            }
                            Command.Companion.COMM_GET_OPERATE_LOG -> {
                                synchronized(logOperates!!) {
                                    val nextSeq = CommandUtil_V3.parseOperateLog(
                                        logOperates!!,
                                        Arrays.copyOfRange(data, 2, data.size),
                                        timezoneOffSet
                                    )

                                    // 读取全部 第二圈读完
                                    if (transferData!!.getLogType() == LogType.ALL && recordCnt == 1 && nextSeq <= lastRecordSeq) {
                                        val mLogCallback: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mLogCallback != null) {
                                            (mLogCallback as GetOperationLogCallback).onGetLogSuccess(
                                                GsonUtil.toJson<List<LogOperate>>(logOperates!!)
                                            )
                                        }
                                        clearRecordCnt()
                                        logOperates!!.clear()
                                        return@Runnable
                                    }
                                    // 记录读取完成
                                    if (nextSeq == 0xFFF0.toShort()) {
                                        // 固件升级用
                                        recordCnt++
                                        // 读全部的开始读第二圈
                                        if (transferData!!.getLogType() == LogType.ALL && recordCnt == 1) {
                                            LogUtil.d("recordCnt:$recordCnt")
                                            CommandUtil.getOperateLog(
                                                command.getLockType(),
                                                (lastRecordSeq + 1).toShort(),
                                                aesKeyArray
                                            )
                                            return@Runnable
                                        }
                                        val mLogCallback: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mLogCallback != null) {
                                            (mLogCallback as GetOperationLogCallback).onGetLogSuccess(
                                                GsonUtil.toJson<List<LogOperate>>(logOperates!!)
                                            )
                                        }
                                        clearRecordCnt()
                                        logOperates!!.clear()
                                    } else {
                                        lastRecordSeq = nextSeq.toInt()
                                        CommandUtil.getOperateLog(
                                            command.getLockType(),
                                            nextSeq,
                                            aesKeyArray
                                        )
                                    }
                                }
                            }
                            Command.Companion.COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED -> {
                                if (command.getLockType() == LockType.LOCK_TYPE_V3 || command.getLockType() == LockType.LOCK_TYPE_V3_CAR) { // 三代锁添加完成
                                    val mLockInitCallback: LockCallback? =
                                        LockCallbackManager.Companion.getInstance().getCallback()
                                    if (mLockInitCallback != null) {
                                        (mLockInitCallback as InitLockCallback).onInitLockSuccess(
                                            lockData?.encodeLockData()
                                        )
                                    }
                                    //                                    //读取版本信息
//                                    tempOptype = DeviceInfoType.MODEL_NUMBER;
//                                    CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.MODEL_NUMBER, aesKeyArray);
//                                    mExtendedBluetoothDevice.disconnectStatus = ExtendedBluetoothDevice.RESPONSE_TIME_OUT;
//                                    mHandler.postDelayed(disConRunable, 1500);
                                } else {
                                    val len = data.size
                                    if (len == 8) {
                                        if (data[7].toInt() == 1) { // 还有后续数据要读
                                            val calendar: Calendar = Calendar.getInstance()
                                            calendar.set(
                                                2000 + data[2],
                                                data[3] - 1,
                                                data[4].toInt(),
                                                data[5].toInt(),
                                                data[6].toInt()
                                            )
                                            // 根据时间偏移量计算时间
                                            val timeZone: TimeZone = TimeZone.getDefault()
                                            LogUtil.d("timezoneOffSet:$timezoneOffSet", DBG)
                                            if (timeZone.inDaylightTime(Date(System.currentTimeMillis()))) timezoneOffSet -= timeZone.getDSTSavings()
                                                .toLong()
                                            timeZone.setRawOffset(timezoneOffSet.toInt())
                                            calendar.setTimeZone(timeZone)
                                            moveDateArray!!.put(calendar.getTimeInMillis())
                                            CommandUtil_Va.Va_Get_Lockcar_Alarm(command.getLockType())
                                        } else {
                                            LogUtil.w("get the data of parking lock failed", DBG)
                                        }
                                    } else if (len == 3) { // 没有数据了
                                        val getLogCallback: LockCallback ?=
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (getLogCallback != null) {
                                            (getLogCallback as GetOperationLogCallback).onGetLogSuccess(
                                                moveDateArray.toString()
                                            )
                                        }
                                    }
                                }
                            }
                            Command.Companion.COMM_GET_VALID_KEYBOARD_PASSWORD -> {
                                val nextSeq = CommandUtil_V3.parseKeyboardPwd(
                                    Arrays.copyOfRange(
                                        data,
                                        2,
                                        data.size
                                    )
                                )
                                if (nextSeq.toInt() == 0 || nextSeq == 0xFFFF.toShort()) { // 密码读完
                                } else {
                                    CommandUtil.getValidKeyboardPassword(
                                        command.getLockType(),
                                        nextSeq,
                                        aesKeyArray
                                    )
                                }
                            }
                            Command.Companion.COMM_SEARCHE_DEVICE_FEATURE -> {
                                if (mExtendedBluetoothDevice != null) {
                                    mExtendedBluetoothDevice!!.setBatteryCapacity(data[2])
                                }
                                lockData!!.specialValue =
                                    DigitUtil.fourBytesToLong(Arrays.copyOfRange(data, 3, 7))
                                        .toInt()
                                lockData!!.featureValue = DigitUtil.convertToFeatureValue(
                                    Arrays.copyOfRange(
                                        data,
                                        3,
                                        data.size
                                    )
                                )!!
                                when (currentAPICommand) {
                                    APICommand.OP_GET_DEVICE_INFO -> {
                                        deviceInfo = DeviceInfo()
                                        deviceInfo!!.featureValue = lockData!!.featureValue
                                        tempOptype = DeviceInfoType.MODEL_NUMBER.toInt()
                                        CommandUtil.readDeviceInfo(transferData!!)
                                    }
                                    APICommand.OP_ADD_ADMIN -> genCommandQue(command)
                                    APICommand.OP_MODIFY_KEYBOARD_PASSWORD -> {
                                        lockError = tmpLockError
                                        // 不支持提示不支持
//                                        if(!DigitUtil.isSupportModifyPasscode(feature)) {
//                                            lockError = LockError.LOCK_NOT_SUPPORT_CHANGE_PASSCODE;
//                                            lockError.setCommand(Command.COMM_MANAGE_KEYBOARD_PASSWORD);
//                                        }
                                        val mModifyErrorCallback: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mModifyErrorCallback != null) {
                                            mModifyErrorCallback.onFail(lockError!!)
                                        }
                                    }
                                    APICommand.OP_CONTROL_REMOTE_UNLOCK -> {
                                        val mRemoteUnlockCallback: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (transferData!!.getOp() == ActionType.SET.toInt()) {
                                            (mRemoteUnlockCallback as SetRemoteUnlockSwitchCallback).onSetRemoteUnlockSwitchSuccess(
                                                lockData!!.encodeLockData()
                                            )
                                        } else {
                                            (mRemoteUnlockCallback as GetRemoteUnlockStateCallback).onGetRemoteUnlockSwitchStateSuccess(
                                                transferData!!.getOpValue() == ActionType.GET.toInt()
                                            )
                                        }
                                    }
                                    APICommand.OP_GET_POW -> {
                                        val mBatteryCallback: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mBatteryCallback != null) {
                                            (mBatteryCallback as GetBatteryLevelCallback).onGetBatteryLevelSuccess(
                                                data[2].toInt()
                                            )
                                        }
                                    }
                                    else -> {
                                        val mSpecialValueCallback: LockCallback ?=
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mSpecialValueCallback != null) {
                                            (mSpecialValueCallback as GetSpecialValueCallback).onGetSpecialValueSuccess(
                                                lockData!!.specialValue
                                            )
                                        }
                                    }
                                }
                            }
                            Command.Companion.COMM_CONTROL_REMOTE_UNLOCK -> {
                                if (data[3] == ConfigRemoteUnlock.OP_TYPE_SEARCH) {
                                    if (transferData == null) {
                                        transferData = TransferData()
                                    }
                                    transferData!!.setOpValue(data[4].toInt())
                                }
                                when (currentAPICommand) {
                                    APICommand.OP_CONTROL_REMOTE_UNLOCK -> {
                                        transferData!!.setOp(data[3].toInt())
                                        CommandUtil.searchDeviceFeature(command.getLockType())
                                    }
                                    APICommand.OP_ADD_ADMIN -> //                                        CommandUtil.operateFinished(command.getLockType());
                                        readModelNumber(command)
                                    else -> {}
                                }
                            }
                            Command.Companion.COMM_AUDIO_MANAGE -> when (data[3]) {
                                AudioManage.QUERY -> {
                                    transferData!!.setOpValue(data[4].toInt())
                                    lockData!!.lockSound = data[4].toInt() // 音量开关
                                    if (data.size > 5) {
                                        lockData!!.soundVolume = data[5].toInt()
                                        transferData!!.setSoundVolume(
                                            SoundVolume.Companion.getInstance(
                                                data[5].toInt()
                                            )
                                        )
                                    }
                                    doResponse(data[0], command)
                                }
                                AudioManage.MODIFY -> {
                                    val mSoundCallback: LockCallback? =
                                        LockCallbackManager.Companion.getInstance().getCallback()
                                    if (mSoundCallback is SetLockConfigCallback) {
                                        (mSoundCallback as SetLockConfigCallback).onSetLockConfigSuccess(
                                            TTLockConfigType.LOCK_SOUND
                                        )
                                    } else if (mSoundCallback is SetLockMuteModeCallback) { // 回调应该不需要有参数
                                        (mSoundCallback as SetLockMuteModeCallback).onSetMuteModeSuccess(
                                            transferData!!.getOpValue() == 0
                                        )
                                    } else if (mSoundCallback is SetLockSoundWithSoundVolumeCallback) {
                                        (mSoundCallback as SetLockSoundWithSoundVolumeCallback).onSetLockSoundSuccess()
                                    }
                                }
                            }
                            Command.Companion.COMM_REMOTE_CONTROL_DEVICE_MANAGE -> {
                                val rollGateCallback: LockCallback? =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (rollGateCallback != null) {
                                    (rollGateCallback as ControlLockCallback).onControlLockSuccess(
                                        ControlLockResult(
                                            data[4].toInt(), data[2].toInt(), -1, -1
                                        )
                                    )
                                }
                                LogUtil.d("data[3]:" + data[3], DBG)
                                LogUtil.d("length:" + Arrays.toString(data), DBG)
                            }
                            Command.Companion.COMM_PWD_LIST -> {
                                val totalRecord: Int =
                                    (data[2].toInt() shl 8 or (data[3].toInt() and 0xff)).toShort().toInt()
                                // 无数据
                                if (totalRecord == 0) {
                                    val mGetPasscodeCallback: LockCallback? =
                                        LockCallbackManager.Companion.getInstance().getCallback()
                                    if (mGetPasscodeCallback != null) {
                                        (mGetPasscodeCallback as GetAllValidPasscodeCallback).onGetAllValidPasscodeSuccess(
                                            GsonUtil.toJson<ArrayList<Passcode>>(passcodes!!)
                                        )
                                    }
                                } else {
                                    val nextSeq =
                                        parsePasscode(Arrays.copyOfRange(data, 4, data.size))
                                    if (nextSeq.toInt() == -1) {
                                        val mGetPasscodeCallback: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mGetPasscodeCallback != null) {
                                            (mGetPasscodeCallback as GetAllValidPasscodeCallback).onGetAllValidPasscodeSuccess(
                                                GsonUtil.toJson<ArrayList<Passcode>>(passcodes!!)
                                            )
                                        }
                                    } else {
                                        CommandUtil.searchPasscode(
                                            command.getLockType(),
                                            nextSeq,
                                            aesKeyArray
                                        )
                                    }
                                }
                            }
                            Command.Companion.COMM_IC_MANAGE -> {
                                battery = data[2]
                                when (data[3]) {
                                    ICOperate.IC_SEARCH -> {
                                        LogUtil.d("ic bytes:" + Arrays.toString(data))
                                        val nextSeq =
                                            parseIC(Arrays.copyOfRange(data, 4, data.size))
                                        LogUtil.d("search:$nextSeq", DBG)
                                        if (nextSeq.toInt() == -1) {
                                            // TODO:
                                            val mGetAllCards: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (mGetAllCards != null) {
                                                (mGetAllCards as GetAllValidICCardCallback).onGetAllValidICCardSuccess(
                                                    GsonUtil.toJson<ArrayList<ICCard>>(icCards!!)
                                                )
                                            }
                                        } else CommandUtil.searchICCardNo(
                                            command.getLockType(),
                                            nextSeq,
                                            aesKeyArray
                                        )
                                    }
                                    ICOperate.ADD -> addICResponse(command, data)
                                    ICOperate.MODIFY -> modifyICPeriodResponse(command)
                                    ICOperate.DELETE -> when (currentAPICommand) {
                                        APICommand.OP_DELETE_IC -> {
                                            val mDeleteCard: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (mDeleteCard != null) {
                                                (mDeleteCard as DeleteICCardCallback).onDeleteICCardSuccess()
                                            }
                                        }
                                        APICommand.OP_LOSS_IC -> {
                                            val mDeleteCard =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (mDeleteCard != null) {
                                                (mDeleteCard as ReportLossCardCallback).onReportLossCardSuccess()
                                            }
                                        }
                                        APICommand.OP_ADD_IC -> errorCallback(tmpLockError!!)
                                    }
                                    ICOperate.CLEAR -> {
                                        val mClearCard: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mClearCard != null) {
                                            (mClearCard as ClearAllICCardCallback).onClearAllICCardSuccess()
                                        }
                                    }
                                    else -> {}
                                }
                            }
                            Command.Companion.COMM_FR_MANAGE -> {
                                battery = data[2]
                                when (data[3]) {
                                    ICOperate.FR_SEARCH -> {
                                        val nextSeq =
                                            parseFR(Arrays.copyOfRange(data, 4, data.size))
                                        LogUtil.d("search:$nextSeq", DBG)
                                        if (nextSeq.toInt() == -1) {
                                            // TODO:
                                            val mGetAllFingerprints: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (mGetAllFingerprints != null) {
                                                (mGetAllFingerprints as GetAllValidFingerprintCallback).onGetAllFingerprintsSuccess(
                                                    GsonUtil.toJson<ArrayList<FR>>(frs!!)
                                                )
                                            }
                                            LogUtil.d("finish", DBG)
                                        } else CommandUtil.searchFRNo(
                                            command.getLockType(),
                                            nextSeq,
                                            aesKeyArray
                                        )
                                    }
                                    ICOperate.ADD -> addFRResponse(command, data)
                                    ICOperate.MODIFY -> modifyFrPeriodResponse(command)
                                    ICOperate.DELETE -> when (currentAPICommand) {
                                        APICommand.OP_DELETE_FR -> {
                                            val mDeleteFrPeriod: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (mDeleteFrPeriod != null) {
                                                (mDeleteFrPeriod as DeleteFingerprintCallback).onDeleteFingerprintSuccess()
                                            }
                                        }
                                        APICommand.OP_ADD_FR -> errorCallback(tmpLockError!!)
                                    }
                                    ICOperate.CLEAR -> {
                                        val mClearFingerprint: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mClearFingerprint != null) {
                                            (mClearFingerprint as ClearAllFingerprintCallback).onClearAllFingerprintSuccess()
                                        }
                                    }
                                    ICOperate.WRITE_FR -> {
                                        isWaitCommand = true
                                        val seq: Short =
                                            ((data[4].toInt() shl 8 or (data[5].toInt() and 0xff)) + 1).toShort()
                                        val leftLen: Int =
                                            transferData!!.getTransferData().size - seq * packetLen
                                        LogUtil.d("leftLen:$leftLen")
                                        LogUtil.d("seq:$seq")
                                        if (leftLen > 0) {
//                                        if (packetLen > leftLen)
//                                            packetLen = leftLen;
                                            CommandUtil_V3.writeFR(
                                                command,
                                                transferData!!.getTransferData(),
                                                seq,
                                                packetLen,
                                                transferData!!.getAesKeyArray()
                                            )
                                        }
                                    }
                                }
                            }
                            Command.Companion.COMM_SET_WRIST_BAND_KEY -> battery = data[2]
                            Command.Companion.COMM_AUTO_LOCK_MANAGE -> {
                                battery = data[2]
                                val op = data[3]
                                when (op) {
                                    AutoLockOperate.SEARCH -> {
                                        LogUtil.e(DigitUtil.byteArrayToHexString(data), true)
                                        val currentTime: Short =
                                            (data[4].toInt() shl 8 or (data[5].toInt() and 0x000000ff)).toShort()
                                        val minTime: Short =
                                            (data[6].toInt() shl 8 or (data[7].toInt() and 0x000000ff)).toShort()
                                        val maxTime: Short =
                                            (data[8].toInt() shl 8 or (data[9].toInt() and 0x000000ff)).toShort()
                                        lockData!!.autoLockTime = currentTime.toInt()
                                        when (currentAPICommand) {
                                            APICommand.OP_DOOR_SENSOR -> {
                                                var operationValue: Byte = 0
                                                if (data.size == 11) operationValue = data[10]
                                            }
                                            APICommand.OP_ADD_ADMIN -> doQueryCommand(command)
                                            APICommand.OP_SEARCH_AUTO_LOCK_PERIOD -> {
                                                val mAutoLockingCallback: LockCallback? =
                                                    LockCallbackManager.Companion.getInstance()
                                                        .getCallback()
                                                if (mAutoLockingCallback != null) {
                                                    (mAutoLockingCallback as GetAutoLockingPeriodCallback).onGetAutoLockingPeriodSuccess(
                                                        currentTime.toInt(),
                                                        minTime.toInt(),
                                                        maxTime.toInt()
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    AutoLockOperate.MODIFY -> if (currentAPICommand == APICommand.OP_DOOR_SENSOR) {
//                                            onOperateDoorSensorLocking(mExtendedBluetoothDevice, battery, op, transferData.getOpValue(), error);
                                    } else {
                                        val mAutoLockingCallback: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mAutoLockingCallback != null) {
                                            (mAutoLockingCallback as SetAutoLockingPeriodCallback).onSetAutoLockingPeriodSuccess()
                                        }
                                    }
                                    else -> {}
                                }
                            }
                            Command.Companion.COMM_READ_DEVICE_INFO -> {
                                LogUtil.d("tempOptype:$tempOptype", DBG)
                                LogUtil.d(
                                    "COMM_READ_DEVICE_INFO:" + DigitUtil.byteArrayToHexString(data),
                                    DBG
                                )
                                LogUtil.d(
                                    "COMM_READ_DEVICE_INFO:" + String(
                                        Arrays.copyOfRange(
                                            data,
                                            2,
                                            data.size - 1
                                        )
                                    ),
                                    DBG
                                )
                                when (tempOptype.toByte()) {
                                    DeviceInfoType.MODEL_NUMBER -> {
                                        if (deviceInfo == null) {
                                            deviceInfo = DeviceInfo()
                                        }
                                        LogUtil.d("deviceInfo:$deviceInfo", DBG)
                                        modelNumber =
                                            String(Arrays.copyOfRange(data, 2, data.size - 1))
                                        if (lockData != null) {
                                            lockData?.setModelNum(modelNumber)
                                        }
                                        deviceInfo!!.modelNum = modelNumber
                                        tempOptype = DeviceInfoType.HARDWARE_REVISION.toInt()
                                        CommandUtil.readDeviceInfo(
                                            command.getLockType(),
                                            DeviceInfoType.HARDWARE_REVISION,
                                            aesKeyArray
                                        )
                                    }
                                    DeviceInfoType.HARDWARE_REVISION -> {
                                        LogUtil.w("deviceInfo:$deviceInfo", DBG)
                                        hardwareRevision =
                                            String(Arrays.copyOfRange(data, 2, data.size - 1))
                                        if (lockData != null) {
                                            lockData?.setHardwareRevision(hardwareRevision)
                                        }
                                        deviceInfo!!.hardwareRevision = hardwareRevision
                                        tempOptype = DeviceInfoType.FIRMWARE_REVISION.toInt()
                                        CommandUtil.readDeviceInfo(
                                            command.getLockType(),
                                            DeviceInfoType.FIRMWARE_REVISION,
                                            aesKeyArray
                                        )
                                    }
                                    DeviceInfoType.FIRMWARE_REVISION -> {
                                        firmwareRevision =
                                            String(Arrays.copyOfRange(data, 2, data.size - 1))
                                        if (lockData != null) {
                                            lockData?.setFirmwareRevision(firmwareRevision)
                                        }
                                        deviceInfo!!.firmwareRevision = firmwareRevision
                                        tempOptype = DeviceInfoType.MANUFACTURE_DATE.toInt()
                                        CommandUtil.readDeviceInfo(
                                            command.getLockType(),
                                            DeviceInfoType.MANUFACTURE_DATE,
                                            aesKeyArray
                                        )
                                    }
                                    DeviceInfoType.MANUFACTURE_DATE -> {
                                        factoryDate =
                                            String(Arrays.copyOfRange(data, 2, data.size - 1))
                                        if (lockData != null) {
                                            lockData?.factoryDate = factoryDate!!
                                        }
                                        deviceInfo!!.factoryDate = factoryDate
                                        tempOptype = DeviceInfoType.LOCK_CLOCK.toInt()
                                        //                                    CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.LOCK_CLOCK, aesKeyArray);

                                        // NB锁
                                        if (FeatureValueUtil.isSupportFeature(
                                                lockData,
                                                FeatureValue.NB_LOCK
                                            )
                                        ) {
                                            if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                                tempOptype = DeviceInfoType.NB_OPERATOR.toInt()
                                            }
                                            CommandUtil.readDeviceInfo(
                                                command.getLockType(),
                                                tempOptype.toByte(),
                                                aesKeyArray
                                            )
                                        } else if (FeatureValueUtil.isSupportFeature(
                                                lockData,
                                                FeatureValue.INCOMPLETE_PASSCODE
                                            )
                                        ) {
                                            // 不读锁时钟了
                                            tempOptype = DeviceInfoType.PASSCODE_KEY_NUMBER.toInt()
                                            CommandUtil.readDeviceInfo(
                                                command.getLockType(),
                                                DeviceInfoType.PASSCODE_KEY_NUMBER,
                                                aesKeyArray
                                            )
                                        } else {
                                            if (currentAPICommand == APICommand.OP_ADD_ADMIN) { // 三代锁
                                                if (transferData != null) mExtendedBluetoothDevice!!.setRemoteUnlockSwitch(
                                                    transferData!!.getOpValue())
                                                lockData = getLockInfoObj()
                                                lockData!!.lockVersion = command.getLockVersion()
                                                CommandUtil.operateFinished(command.getLockType())
                                                //                                                LockCallback mLockInitCallback? = LockCallbackManager.getInstance().getCallback();
//                                                if(mLockInitCallback != null){
//                                                    ((InitLockCallback)mLockInitCallback).onInitLockSuccess(lockData.encodeLockData());
//                                                }
                                            } else {
                                                CommandUtil.readDeviceInfo(
                                                    command.getLockType(),
                                                    DeviceInfoType.LOCK_CLOCK,
                                                    aesKeyArray
                                                )
                                            }
                                        }
                                    }
                                    DeviceInfoType.LOCK_CLOCK -> {
                                        lockClock = ""
                                        var i = 2
                                        while (i < data.size - 1) {
                                            lockClock = lockClock + String.format("%02d", data[i])
                                            i++
                                        }
                                        deviceInfo!!.lockClock = lockClock
                                        // NB锁
                                        if (FeatureValueUtil.isSupportFeature(
                                                lockData,
                                                FeatureValue.NB_LOCK
                                            )
                                        ) {
                                            tempOptype = DeviceInfoType.NB_OPERATOR.toInt()
                                            CommandUtil.readDeviceInfo(
                                                command.getLockType(),
                                                DeviceInfoType.NB_OPERATOR,
                                                aesKeyArray
                                            )
                                        } else {
                                            tempOptype = -1
                                            deviceInfo!!.featureValue = lockData?.featureValue
                                            if (lockData != null) {
                                                deviceInfo!!.lockData = lockData?.encodeLockData()
                                            }
                                            val mInfoCallback: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (mInfoCallback != null) {
                                                (mInfoCallback as GetLockSystemInfoCallback).onGetLockSystemInfoSuccess(
                                                    deviceInfo
                                                )
                                            }
                                        }
                                    }
                                    DeviceInfoType.NB_OPERATOR -> {
                                        deviceInfo!!.nbOperator =
                                            String(Arrays.copyOfRange(data, 2, data.size - 1))
                                        if (lockData != null) {
                                            lockData?.setNbOperator(deviceInfo!!.nbOperator)
                                        }
                                        tempOptype = DeviceInfoType.NB_IMEI.toInt()
                                        CommandUtil.readDeviceInfo(
                                            command.getLockType(),
                                            DeviceInfoType.NB_IMEI,
                                            aesKeyArray
                                        )
                                    }
                                    DeviceInfoType.NB_IMEI -> {
                                        deviceInfo!!.nbNodeId =
                                            String(Arrays.copyOfRange(data, 2, data.size - 1))
                                        if (lockData != null) {
                                            lockData?.setNbNodeId(deviceInfo!!.getNbNodeId())
                                        }
                                        tempOptype = DeviceInfoType.NB_CARD_INFO.toInt()
                                        CommandUtil.readDeviceInfo(
                                            command.getLockType(),
                                            DeviceInfoType.NB_CARD_INFO,
                                            aesKeyArray
                                        )
                                        LogUtil.d("NB_IMEI:" + deviceInfo!!.nbNodeId, DBG)
                                    }
                                    DeviceInfoType.NB_CARD_INFO -> {
                                        deviceInfo!!.nbCardNumber =
                                            String(Arrays.copyOfRange(data, 2, data.size - 1))
                                        if (lockData != null) {
                                            lockData?.setNbCardNumber(deviceInfo!!.nbCardNumber)
                                        }
                                        tempOptype = DeviceInfoType.NB_RSSI.toInt()
                                        CommandUtil.readDeviceInfo(
                                            command.getLockType(),
                                            DeviceInfoType.NB_RSSI,
                                            aesKeyArray
                                        )
                                        LogUtil.d("NB_CARD_INFO:" + deviceInfo!!.nbCardNumber, DBG)
                                    }
                                    DeviceInfoType.NB_RSSI -> {
                                        deviceInfo!!.nbRssi = data[2].toInt()
                                        if (lockData != null) {
                                            lockData?.setNbRssi(deviceInfo!!.nbRssi)
                                        }
                                        LogUtil.d("NB_RSSI:" + deviceInfo!!.nbRssi, DBG)
                                        // TODO:
                                        if (FeatureValueUtil.isSupportFeature(
                                                lockData,
                                                FeatureValue.INCOMPLETE_PASSCODE
                                            )
                                        ) {
                                            tempOptype = DeviceInfoType.PASSCODE_KEY_NUMBER.toInt()
                                            CommandUtil.readDeviceInfo(
                                                command.getLockType(),
                                                tempOptype.toByte(),
                                                aesKeyArray
                                            )
                                        } else if (currentAPICommand == APICommand.OP_ADD_ADMIN) { // nb
                                            if (transferData!!.getPort()
                                                .toInt() != 0 && !TextUtils.isEmpty(transferData!!.getAddress())
                                            ) CommandUtil_V3.configureNBServerAddress(
                                                command,
                                                transferData!!.getPort() as Short,
                                                transferData!!.getAddress(),
                                                aesKeyArray
                                            ) else {
                                                lockData = getLockInfoObj()
                                                lockData!!.lockVersion = command.getLockVersion()
                                                CommandUtil.operateFinished(command.getLockType())
                                                //                                                LockCallback mLockCallback? = LockCallbackManager.getInstance().getCallback();
//                                                if(mLockCallback != null){
//                                                    ((InitLockCallback)mLockCallback).onInitLockSuccess(lockData.encodeLockData());
//                                                }
                                            }
                                        } else {
                                            tempOptype = -1
                                            val mInfoCallback: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (mInfoCallback != null) {
                                                if (lockData != null) {
                                                    deviceInfo!!.lockData =
                                                        lockData?.encodeLockData()
                                                }
                                                (mInfoCallback as GetLockSystemInfoCallback).onGetLockSystemInfoSuccess(
                                                    deviceInfo
                                                )
                                            }
                                        }
                                    }
                                    DeviceInfoType.PASSCODE_KEY_NUMBER -> {
                                        deviceInfo!!.passcodeKeyNumber = data[2].toInt()
                                        if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                            if (transferData!!.getPort()
                                                .toInt() != 0 && !TextUtils.isEmpty(transferData!!.getAddress())
                                            ) CommandUtil_V3.configureNBServerAddress(
                                                command,
                                                transferData!!.getPort() as Short,
                                                transferData!!.getAddress(),
                                                aesKeyArray
                                            ) else {
                                                lockData = getLockInfoObj()
                                                lockData!!.lockVersion = command.getLockVersion()
                                                CommandUtil.operateFinished(command.getLockType())
                                            }
                                        } else {
                                            tempOptype = -1
                                            val mInfoCallback: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (mInfoCallback != null) {
                                                if (lockData != null) {
                                                    deviceInfo!!.lockData =
                                                        lockData?.encodeLockData()
                                                }
                                                (mInfoCallback as GetLockSystemInfoCallback).onGetLockSystemInfoSuccess(
                                                    deviceInfo
                                                )
                                            }
                                        }
                                    }
                                    else -> {
                                        // 默认读取型号
                                        if (deviceInfo == null) deviceInfo = DeviceInfo()
                                        modelNumber =
                                            String(Arrays.copyOfRange(data, 2, data.size - 1))
                                        deviceInfo!!.modelNum = modelNumber
                                        tempOptype = DeviceInfoType.HARDWARE_REVISION.toInt()
                                        CommandUtil.readDeviceInfo(
                                            command.getLockType(),
                                            DeviceInfoType.HARDWARE_REVISION,
                                            aesKeyArray
                                        )
                                    }
                                }
                            }
                            Command.Companion.COMM_ENTER_DFU_MODE -> {
                                val mEnterDfuCallback: LockCallback? =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (mEnterDfuCallback != null) {
                                    (mEnterDfuCallback as EnterDfuModeCallback).onEnterDfuMode()
                                }
                            }
                            Command.Companion.COMM_READ_PWD_PARA -> {
                                battery = data[2]
                                val code: Int = data[3].toInt() shl 4 or (data[4].toInt() shr 4 and 0x0f) and 0x0fff
                                LogUtil.d("bytes:" + DigitUtil.byteArrayToHexString(data), DBG)
                                val secretKey =
                                    data[4].toLong() * 1L shl 32 and 0x0f00000000L or (data[5].toLong() shl 24 and 0xff000000L) or (data[6].toLong() shl 16 and 0x00ff0000L) or (data[7].toLong() shl 8 and 0xff00L) or (data[8].toLong() and 0xffL) and 0x0fffffffffL
                                LogUtil.d("code:$code", DBG)
                                LogUtil.d("secretKey:$secretKey", DBG)
                                LogUtil.d("sec:" + DigitUtil.byteArrayToHexString(data), DBG)
                                val calendar: Calendar = Calendar.getInstance()
                                calendar.set(
                                    2000 + data[9],
                                    data[10] - 1,
                                    data[11].toInt(),
                                    data[12].toInt(),
                                    data[13].toInt()
                                )
                                LogUtil.d("data[9]:" + data[9], DBG)
                                // 根据时间偏移量计算时间
                                val timeZone: TimeZone = TimeZone.getDefault()
                                LogUtil.d("timezoneOffSet:$timezoneOffSet", DBG)
                                if (timeZone.inDaylightTime(Date(System.currentTimeMillis()))) timezoneOffSet -= timeZone.getDSTSavings()
                                    .toLong()
                                timeZone.setRawOffset(timezoneOffSet.toInt())
                                calendar.setTimeZone(timeZone)
                                var deleteTime: Long = calendar.getTimeInMillis()
                                if (data[9].toInt() == 0) deleteTime = 0
                                LogUtil.d("code:$code", DBG)
                                val pwdInfoV3: PwdInfoV3 = PwdInfoV3.Companion.getInstance(
                                    calendar.get(Calendar.YEAR),
                                    code,
                                    secretKey.toString(),
                                    deleteTime
                                )
                                val pwdInfoSource: String = GsonUtil.toJson<PwdInfoV3>(pwdInfoV3)
                                val timestamp: Long = calendar.getTimeInMillis()
                                val pwdInfo = CommandUtil.encry(pwdInfoSource, timestamp)
                                val mGetPwdInfoCalback: LockCallback? =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (mGetPwdInfoCalback != null) {
                                    lockData!!.pwdInfo = pwdInfo
                                    lockData!!.timestamp = timestamp
                                    (mGetPwdInfoCalback as GetPasscodeVerificationInfoCallback).onGetInfoSuccess(
                                        lockData!!.encodeLockData()
                                    )
                                }
                            }
                            Command.Companion.COMM_CONFIGURE_NB_ADDRESS -> {
                                LogUtil.d("COMM_CONFIGURE_NB_ADDRESS - battery:" + data[2], DBG)
                                if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                    lockData = getLockInfoObj()
                                    lockData!!.lockVersion = command.getLockVersion()
                                    CommandUtil.operateFinished(command.getLockType())
                                    //                                    LockCallback mCallback? = LockCallbackManager.getInstance().getCallback();
//                                    if(mCallback != null){
//                                        ((InitLockCallback)mCallback).onInitLockSuccess(lockData.encodeLockData());
//                                    }
                                } else {
                                    val mNbCallback: LockCallback? =
                                        LockCallbackManager.Companion.getInstance().getCallback()
                                    if (mNbCallback != null) {
                                        (mNbCallback as SetNBServerCallback).onSetNBServerSuccess(
                                            data[2].toInt()
                                        )
                                    }
                                }
                            }
                            Command.Companion.COMM_CONFIGURE_HOTEL_DATA -> {
                                // TODO:
                                LogUtil.d("COMM_CONFIGURE_HOTEL_DATA:")
                                if (data[3].toInt() == HotelData.Companion.GET) {
                                    ResponseUtil.getHotelData(data)
                                } else if (data[3].toInt() == HotelData.Companion.SET) {
                                    when (data[4]) {
                                        HotelData.Companion.TYPE_IC_KEY -> CommandUtil_V3.configureHotelData(
                                            command,
                                            HotelData.Companion.SET,
                                            HotelData.Companion.TYPE_AES_KEY.toInt(),
                                            hotelData!!,
                                            aesKeyArray
                                        )
                                        HotelData.Companion.TYPE_AES_KEY -> CommandUtil_V3.configureHotelData(
                                            command,
                                            HotelData.Companion.SET,
                                            HotelData.Companion.TYPE_HOTEL_BUILDING_FLOOR.toInt(),
                                            hotelData!!,
                                            aesKeyArray
                                        )
                                        HotelData.Companion.TYPE_HOTEL_BUILDING_FLOOR -> when (currentAPICommand) {
                                            APICommand.OP_SET_HOTEL_DATA -> {
                                                val lockCallback: LockCallback? =
                                                    LockCallbackManager.Companion.getInstance()
                                                        .getCallback()
                                                if (lockCallback != null) {
                                                    (lockCallback as SetHotelDataCallback).onSetHotelDataSuccess()
                                                }
                                            }
                                            else -> CommandUtil_V3.configureHotelData(
                                                command,
                                                HotelData.Companion.SET,
                                                HotelData.Companion.TYPE_SECTOR.toInt(),
                                                hotelData!!,
                                                aesKeyArray
                                            )
                                        }
                                        HotelData.Companion.TYPE_SECTOR -> when (currentAPICommand) {
                                            APICommand.OP_ADD_ADMIN -> if (hotelData!!.controlableFloors != null) {
                                                CommandUtil_V3.configureHotelData(
                                                    command,
                                                    HotelData.Companion.SET,
                                                    HotelData.Companion.TYPE_ELEVATOR_CONTROLABLE_FLOORS.toInt(),
                                                    hotelData!!,
                                                    aesKeyArray
                                                )
                                            } else {
                                                readModelNumber(command)
                                                //                                                        CommandUtil.operateFinished(command.getLockType());
                                            }
                                            APICommand.OP_SET_HOTEL_CARD_SECTION -> {
                                                val callback: LockCallback? =
                                                    LockCallbackManager.Companion.getInstance()
                                                        .getCallback()
                                                if (callback != null) {
                                                    (callback as SetHotelCardSectorCallback).onSetHotelCardSectorSuccess()
                                                }
                                            }
                                        }
                                        HotelData.Companion.TYPE_ELEVATOR_CONTROLABLE_FLOORS -> if (hotelData!!.ttLiftWorkMode != null) {
                                            CommandUtil_V3.configureHotelData(
                                                command,
                                                HotelData.Companion.SET,
                                                HotelData.Companion.TYPE_ELEVATOR_WORK_MODE.toInt(),
                                                hotelData!!,
                                                aesKeyArray
                                            )
                                        } else {
                                            val callback: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (callback != null) {
                                                (callback as SetLiftControlableFloorsCallback).onSetLiftControlableFloorsSuccess()
                                            }
                                        }
                                        HotelData.Companion.TYPE_ELEVATOR_WORK_MODE -> when (currentAPICommand) {
                                            APICommand.OP_ADD_ADMIN -> readModelNumber(command)
                                            else -> {
                                                val callback: LockCallback? =
                                                    LockCallbackManager.Companion.getInstance()
                                                        .getCallback()
                                                if (callback != null) {
                                                    (callback as SetLiftWorkModeCallback).onSetLiftWorkModeSuccess()
                                                }
                                            }
                                        }
                                        HotelData.Companion.TYPE_POWER_SAVER_WORK_MODE -> {
                                            val callback: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (callback != null) {
                                                (callback as SetPowerSaverWorkModeCallback).onSetPowerSaverWorkModeSuccess()
                                            }
                                        }
                                        HotelData.Companion.TYPE_POWER_SAVER_CONTROLABLE_LOCK -> {
                                            val callback: LockCallback? = LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                            if (callback != null) {
                                                (callback as SetPowerSaverControlableLockCallback).onSetPowerSaverControlableLockSuccess()
                                            }
                                        }
                                    }
                                }
                            }
                            Command.Companion.COMM_GET_ADMIN_CODE -> {
                                val len = data[3].toInt()
                                adminPasscode = String(Arrays.copyOfRange(data, 4, data.size))
                                LogUtil.d("adminCode:$adminPasscode")
                                //                            adminCode = DigitUtil.encodeLockData(adminCode);
                                if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                    if (TextUtils.isEmpty(adminPasscode)) { // 密码是空 就重新设置
                                        adminPasscode = DigitUtil.generatePwdByLength(7)
                                        CommandUtil.S_setAdminKeyboardPwd(
                                            command.getLockType(),
                                            adminPasscode,
                                            aesKeyArray
                                        )
                                    } else {
                                        CommandUtil_V3.initPasswords(
                                            command.getLockType(),
                                            aesKeyArray,
                                            currentAPICommand
                                        )
                                    }
                                } else if (currentAPICommand == APICommand.OP_GET_ADMIN_KEYBOARD_PASSWORD) {
                                    val mGetAdminPwdCallback: LockCallback? =
                                        LockCallbackManager.Companion.getInstance().getCallback()
                                    if (mGetAdminPwdCallback != null) {
                                        (mGetAdminPwdCallback as GetAdminPasscodeCallback).onGetAdminPasscodeSuccess(
                                            adminPasscode
                                        )
                                    }
                                }
                            }
                            Command.Companion.COMM_CONFIGURE_PASSAGE_MODE -> {
                                when (data[3]) {
                                    PassageModeOperate.QUERY -> if (data[4] == 0xff.toByte()) {
                                        val mGetPassageModeCallback: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mGetPassageModeCallback != null) {
                                            (mGetPassageModeCallback as GetPassageModeCallback).onGetPassageModeSuccess(
                                                GsonUtil.toJson<List<PassageModeData>>(
                                                    passageModeDatas!!
                                                )
                                            )
                                        }
                                    } else {
                                        doWithPassageModeData(
                                            Arrays.copyOfRange(
                                                data,
                                                5,
                                                data.size
                                            )
                                        )
                                        CommandUtil_V3.queryPassageMode(
                                            command,
                                            data[4],
                                            aesKeyArray
                                        )
                                    }
                                    PassageModeOperate.ADD -> try {
                                        dataPos++
                                        if (dataPos < weerOrDays!!.length()) {
                                            CommandUtil_V3.configurePassageMode(
                                                command,
                                                transferData!!,
                                                (weerOrDays!!.get(dataPos) as Int).toByte()
                                            )
                                        } else {
                                            val mSetPassageModeCallback: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (mSetPassageModeCallback != null) {
                                                (mSetPassageModeCallback as SetPassageModeCallback).onSetPassageModeSuccess()
                                            }
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    PassageModeOperate.DELETE -> try {
                                        dataPos++
                                        if (dataPos < weerOrDays!!.length()) {
                                            CommandUtil_V3.deletePassageMode(
                                                command,
                                                transferData!!,
                                                (weerOrDays!!.get(dataPos) as Int).toByte()
                                            )
                                        } else {
                                            val mDeletePassageModeCallback: LockCallback? =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (mDeletePassageModeCallback != null) {
                                                (mDeletePassageModeCallback as DeletePassageModeCallback).onDeletePassageModeSuccess()
                                            }
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    PassageModeOperate.CLEAR -> {
                                        val mClearModeCallback: LockCallback? =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        if (mClearModeCallback != null) {
                                            (mClearModeCallback as ClearPassageModeCallback).onClearPassageModeSuccess()
                                        }
                                    }
                                    else -> {}
                                }
                            }
                            Command.Companion.COMM_FREEZE_LOCK -> {
                                val lockCallback: LockCallback? =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (lockCallback != null) {
                                    when (data[3]) {
                                        OperationType.GET_STATE -> if (lockCallback is GetLockConfigCallback) {
                                            (lockCallback as GetLockConfigCallback).onGetLockConfigSuccess(
                                                TTLockConfigType.LOCK_FREEZE,
                                                data[4].toInt() == 1
                                            )
                                        } else if (lockCallback is GetLockFreezeStateCallback) {
                                            (lockCallback as GetLockFreezeStateCallback).onGetLockFreezeStateSuccess(
                                                data[4].toInt() == 1
                                            )
                                        } else {
                                            LogUtil.d("lockCallback:$lockCallback")
                                        }
                                        OperationType.MODIFY -> if (lockCallback is SetLockConfigCallback) {
                                            (lockCallback as SetLockConfigCallback).onSetLockConfigSuccess(
                                                TTLockConfigType.LOCK_FREEZE
                                            )
                                        } else if (lockCallback is SetLockFreezeStateCallback) {
                                            (lockCallback as SetLockFreezeStateCallback).onSetLockFreezeStateSuccess()
                                        }
                                    }
                                }
                            }
                            Command.Companion.COMM_LAMP -> when (data[3]) {
                                OperationType.GET_STATE -> {
                                    lockData!!.lightingTime =
                                        data[5].toInt() and 0xff or (data[4].toInt() shl 8 and 0xff00) and 0xffff
                                    doResponse(data[0], command)
                                }
                                OperationType.MODIFY -> {
                                    val lockCallback =
                                        LockCallbackManager.Companion.getInstance().getCallback()
                                    (lockCallback as SetLightTimeCallback).onSetLightTimeSuccess()
                                }
                            }
                            Command.Companion.COMM_SWITCH -> when (data[3]) {
                                OperationType.GET_STATE -> {
                                    switchItem =
                                        DigitUtil.bytesToLong(Arrays.copyOfRange(data, 4, 8))
                                            .toInt()
                                    switchValue =
                                        DigitUtil.bytesToLong(Arrays.copyOfRange(data, 8, 12))
                                            .toInt()
                                    settingValue = Integer.valueOf(switchValue)
                                    LogUtil.d("switchItem:$switchItem")
                                    LogUtil.d("switchValue:$switchValue")
                                    LogUtil.d("transferData.getOp():" + transferData!!.getOp())
                                    when (currentAPICommand) {
                                        APICommand.OP_SET_SWITCH -> {
                                            switchValue = if (transferData!!.getOpValue() > 0) {
                                                switchValue or transferData!!.getOp()
                                            } else {
                                                switchValue and transferData!!.getOp().inv()
                                            }
                                            LogUtil.d("new switchValue:$switchValue")
                                            CommandUtil_V3.setSwitchState(
                                                command,
                                                switchItem,
                                                switchValue,
                                                aesKeyArray
                                            )
                                        }
                                        APICommand.OP_SET_UNLOCK_DIRECTION -> {
                                            switchValue =
                                                if (transferData!!.getUnlockDirection()
                                                    !!.getValue() > 0
                                                ) {
                                                    switchValue or TTLockConfigType.UNLOCK_DIRECTION.getItem()
                                                } else {
                                                    switchValue and TTLockConfigType.UNLOCK_DIRECTION.getItem()
                                                        .inv()
                                                }
                                            LogUtil.d("new switchValue:$switchValue")
                                            CommandUtil_V3.setSwitchState(
                                                command,
                                                switchItem,
                                                switchValue,
                                                aesKeyArray
                                            )
                                        }
                                        APICommand.OP_GET_UNLOCK_DIRECTION -> {
                                            val lockCallback =
                                                LockCallbackManager.Companion.getInstance()
                                                    .getCallback()
                                            if (switchValue and TTLockConfigType.UNLOCK_DIRECTION.getItem() != 0) { // 1左开 0右开
                                                (lockCallback as GetUnlockDirectionCallback).onGetUnlockDirectionSuccess(
                                                    UnlockDirection.LEFT
                                                )
                                            } else {
                                                (lockCallback as GetUnlockDirectionCallback).onGetUnlockDirectionSuccess(
                                                    UnlockDirection.RIGHT
                                                )
                                            }
                                        }
                                        else -> doResponse(data[0], command)
                                    }
                                }
                                OperationType.MODIFY -> {
                                    val lockCallback =
                                        LockCallbackManager.Companion.getInstance().getCallback()
                                    when (currentAPICommand) {
                                        APICommand.OP_SET_SWITCH -> (lockCallback as SetLockConfigCallback).onSetLockConfigSuccess(
                                            TTLockConfigType.Companion.getInstance(transferData!!.getOp())
                                        )
                                        APICommand.OP_SET_UNLOCK_DIRECTION -> (lockCallback as SetUnlockDirectionCallback).onSetUnlockDirectionSuccess()
                                    }
                                }
                            }
                            Command.Companion.COMM_DEAD_LOCK -> {
                                battery = -1 // 默认没电量的情况
                                var uid = 0 // 表示不存在用户
                                val calendar = Calendar.getInstance()
                                var lockTime: Long = calendar.getTimeInMillis() // 门锁时间 默认设置成门锁时间
                                var uniqueid = (lockTime / 1000).toInt() // 记录唯一标识   默认使用门锁时间
                                battery = data[2]
                                uid = DigitUtil.fourBytesToLong(Arrays.copyOfRange(data, 3, 7))
                                    .toInt()
                                uniqueid =
                                    DigitUtil.fourBytesToLong(Arrays.copyOfRange(data, 7, 11))
                                        .toInt()
                                calendar.set(
                                    2000 + data[11],
                                    data[12] - 1,
                                    data[13].toInt(),
                                    data[14].toInt(),
                                    data[15].toInt(),
                                    data[16].toInt()
                                )
                                // 根据时间偏移量计算时间
                                val timeZone = TimeZone.getDefault()
                                LogUtil.d("timezoneOffSet:$timezoneOffSet", DBG)
                                if (timeZone.inDaylightTime(Date(System.currentTimeMillis()))) timezoneOffSet -= timeZone.getDSTSavings()
                                    .toLong()
                                timeZone.setRawOffset(timezoneOffSet.toInt())
                                calendar.setTimeZone(timeZone)
                                lockTime = calendar.getTimeInMillis()
                                mExtendedBluetoothDevice!!.setBatteryCapacity(battery.toByte())
                            }
                            Command.Companion.COMM_CYCLIC_CMD -> {
                                when (data[3]) {
                                    CyclicOpType.QUERY -> {}
                                    CyclicOpType.ADD -> addCyclicDataResponse(command)
                                    CyclicOpType.REMOVE -> {}
                                    CyclicOpType.CLEAR -> clearCyclicDataResponse(command)
                                }
                            }
                            Command.Companion.COMM_NB_ACTIVATE_CONFIGURATION -> when (data[3]) {
                                ActionType.SET -> {
                                    val lockCallback =
                                        LockCallbackManager.Companion.getInstance().getCallback()
                                    when (data[4]) {
                                        NBAwakeConfig.Companion.ACTION_AWAKE_MODE -> (lockCallback as SetNBAwakeModesCallback).onSetNBAwakeModesSuccess()
                                        NBAwakeConfig.Companion.ACTION_AWAKE_TIME -> (lockCallback as SetNBAwakeTimesCallback).onSetNBAwakeTimesSuccess()
                                    }
                                }
                                ActionType.GET -> {
                                    val lockCallback =
                                        LockCallbackManager.Companion.getInstance().getCallback()
                                    when (data[4]) {
                                        NBAwakeConfig.Companion.ACTION_AWAKE_MODE -> (lockCallback as GetNBAwakeModesCallback).onGetNBAwakeModesSuccess(
                                            DataParseUitl.parseNBActivateMode(
                                                data[5]
                                            )
                                        )
                                        NBAwakeConfig.Companion.ACTION_AWAKE_TIME -> (lockCallback as GetNBAwakeTimesCallback).onGetNBAwakeTimesSuccess(
                                            DataParseUitl.parseNBActivateConfig(
                                                Arrays.copyOfRange(data, 5, data.size)
                                            )
                                        )
                                    }
                                }
                            }
                            Command.Companion.COMM_ACCESSORY_BATTERY -> {
                                battery = data[2]
                                val accessoryType: AccessoryType? =
                                    AccessoryType.Companion.getInstance(data[3].toInt())
                                val accessoryMac: String =
                                    DigitUtil.getMacString(Arrays.copyOfRange(data, 4, 10))
                                val saveDate: Long = DigitUtil.convertTimestampWithTimezoneOffset(
                                    Arrays.copyOfRange(
                                        data,
                                        10,
                                        16
                                    ),
                                    timezoneOffSet.toInt()
                                )
                                val accessoryBattery = data[16].toInt()
                                val lockCallback =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                (lockCallback as GetAccessoryBatteryLevelCallback).onGetAccessoryBatteryLevelSuccess(
                                    AccessoryInfo(
                                        accessoryType,
                                        accessoryMac,
                                        saveDate,
                                        accessoryBattery
                                    )
                                )
                            }
                            Command.Companion.COMM_DOOR_SENSOR_MANAGE -> when (currentAPICommand) {
                                APICommand.OP_ADD_DOOR_SENSOR -> {
                                    val lockCallback =
                                        LockCallbackManager.Companion.getInstance().getCallback()
                                    (lockCallback as AddDoorSensorCallback).onAddSuccess()
                                }
                                APICommand.OP_DELETE_DOOR_SENSOR -> {
                                    val lockCallback =
                                        LockCallbackManager.Companion.getInstance().getCallback()
                                    (lockCallback as DeleteDoorSensorCallback).onDeleteSuccess()
                                }
                            }
                            Command.Companion.COMM_KEY_FOB_MANAG -> {
                                battery = data[2]
                                when (data[3]) {
                                    KeyFobOperationType.GET -> {}
                                    KeyFobOperationType.ADD_MODIFY -> // 有循环操作的都先清空循环配置
                                        CommandUtil_V3.clearKeyfobCyclicPeriod(
                                            command,
                                            transferData!!.getKeyFobMac()!!,
                                            aesKeyArray
                                        )
                                    KeyFobOperationType.DELETE -> {
                                        val lockCallback =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        when (currentAPICommand) {
                                            APICommand.OP_ADD_KEY_FOB -> (lockCallback as AddRemoteCallback).onFail(
                                                tmpLockError!!
                                            )
                                            APICommand.OP_MODIFY_KEY_FOB_PERIOD -> (lockCallback as ModifyRemoteValidityPeriodCallback).onFail(
                                                tmpLockError!!
                                            )
                                            else -> (lockCallback as DeleteRemoteCallback).onDeleteSuccess()
                                        }
                                    }
                                    KeyFobOperationType.CLEAR -> {
                                        val lockCallback =
                                            LockCallbackManager.Companion.getInstance()
                                                .getCallback()
                                        (lockCallback as ClearRemoteCallback).onClearSuccess()
                                    }
                                }
                            }
                            Command.Companion.COMM_SENSITIVITY_MANAGE -> when (data[3]) {
                                SensitivityOperationType.QUERY -> {
                                    sensitivity = data[4].toInt()
                                    doResponse(data[0], command)
                                }
                            }
                            Command.Companion.COMM_SCAN_WIFI -> if (data.size == 3) { // wifi 收集成功
                                val lockCallback =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (lockCallback != null) {
                                    (lockCallback as ScanWifiCallback).onScanWifi(wiFis, 1)
                                }
                            } else {
                                val wifiLen = data[3].toInt()
                                val wiFi = WiFi()
                                wiFi.setSsid(String(Arrays.copyOfRange(data, 4, 4 + wifiLen)))
                                wiFi.setRssi(data[4 + wifiLen].toInt())
                                LogUtil.d("wifi:" + wiFi.getSsid())
                                insertWifi(wiFi)
                                val lockCallback = LockCallbackManager.Companion.getInstance()
                                    .getCallbackWithoutRemove()
                                if (lockCallback != null) {
                                    (lockCallback as ScanWifiCallback).onScanWifi(wiFis, 0)
                                }
                            }
                            Command.Companion.COMM_CONFIG_WIFI_AP -> {
                                val lockCallback =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (lockCallback != null && lockCallback is ConfigWifiCallback) {
                                    (lockCallback as ConfigWifiCallback).onConfigWifiSuccess()
                                }
                            }
                            Command.Companion.COMM_CONFIG_STATIC_IP -> {
                                val lockCallback =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (lockCallback != null) {
                                    (lockCallback as ConfigIpCallback).onConfigIpSuccess()
                                }
                            }
                            Command.Companion.COMM_CONFIG_SERVER -> {
                                val lockCallback =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (lockCallback != null && lockCallback is ConfigServerCallback) {
                                    (lockCallback as ConfigServerCallback).onConfigServerSuccess()
                                } else {
                                    LogUtil.w("receive config server callback")
                                }
                            }
                            Command.Companion.COMM_GET_WIFI_INFO -> {
                                val wifiMacBytes = Arrays.copyOfRange(data, 3, 3 + 6)
                                DigitUtil.reverseArray(wifiMacBytes)
                                val rssi = data[9].toInt()
                                val lockCallback =
                                    LockCallbackManager.Companion.getInstance().getCallback()
                                if (lockCallback != null) {
                                    (lockCallback as GetWifiInfoCallback).onGetWiFiInfoSuccess(
                                        WifiLockInfo(DigitUtil.getMacString(wifiMacBytes), rssi)
                                    )
                                }
                            }
                            else -> {
                                //
                                LogUtil.w("invalid command", DBG)
                            }
                        }
                    } else { // 失败
                        errorResponse(command, data)
                    }
                }
                if (command.getLockType() == LockType.LOCK_TYPE_V3 && !isWaitCommand) {
//            LogUtil.d("start the time", true);
                    // TODO:响应完了 是不是 不应该再计时
                    var delay: Long = 2000
                    if (currentAPICommand == APICommand.OP_RESET_LOCK || currentAPICommand == APICommand.OP_ADD_FR || currentAPICommand == APICommand.OP_SET_WIFI) // 12条指纹
                        delay = 7000
                    mExtendedBluetoothDevice!!.disconnectStatus =
                        ExtendedBluetoothDevice.Companion.RESPONSE_TIME_OUT
                    mHandler?.postDelayed(disConRunable, delay)
                }
            }
        )
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * `BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)`
     * callback.
     */
    fun disconnect() {
        readCacheLog()
        mConnectionState = STATE_DISCONNECTED
        LogUtil.d("dis ble connect", DBG)
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }
        try {
            mBluetoothGatt?.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    @Synchronized
    fun close() {
        if (mBluetoothGatt == null) {
            return
        }
        mBluetoothGatt?.close()
        mBluetoothGatt = null
    }

    private fun commandSendAgain() {
        try {
            LogUtil.d("commandSendAgain:", DBG)
            mNotifyCharacteristic?.setValue(dataQueue!!.poll())
            mBluetoothGatt?.writeCharacteristic(mNotifyCharacteristic!!)
        } catch (e: Exception) {
            mConnectionState = STATE_DISCONNECTED
            sdkLogItem = e.message
            addSdkLog()
            doConnectFailedCallback()
        }
    }
    // TODO:补全
    /**
     * 错误回调
     * @param error
     */

    private fun errorCallback(error: LockError) {
        error.setLockname(mExtendedBluetoothDevice!!.getName())
        error.setLockmac(mExtendedBluetoothDevice!!.getAddress())
        error.setDate(System.currentTimeMillis())
        LogUtil.d("commandSendCount:$commandSendCount", DBG)
        // TODO:这里返回
        commandSendCount++
        if (commandSendCount == 1 && (error == LockError.LOCK_CRC_CHECK_ERROR || error == LockError.KEY_INVALID) && cloneDataQueue != null) {
            if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
                dataQueue = cloneDataQueue
                commandSendAgain()
                return
            }
        }
        if (mAppExecutor == null) {
            mAppExecutor = AppExecutors()
        }
        mAppExecutor?.mainThread()?.execute(
            Runnable {
                val mFailCallback: LockCallback? =
                    LockCallbackManager.Companion.getInstance().getCallback()
                if (mFailCallback != null) {
                    setSdkLog(error)
                    mFailCallback.onFail(error)
                }
            }
        )
        LogUtil.d("error$error", DBG)
    }

    fun generateTransmissionData(scene: Int, values: ByteArray, validPwdNum: Byte): String {
        // 300个密码
        val pwdSet: MutableSet<String> = LinkedHashSet<String>()
        val fourKeyboardPwdList = StringBuilder()
        fourKeyboardPwdList.append('[')
        while (pwdSet.size < 300) {
            val pwd: String = DigitUtil.generatePwdByLength(4)
            if (pwdSet.add(pwd)) {
                fourKeyboardPwdList.append(pwd)
                fourKeyboardPwdList.append(",")
            }
        }
        fourKeyboardPwdList.replace(fourKeyboardPwdList.length - 1, fourKeyboardPwdList.length, "]")
        val iterator: Iterator<*> = pwdSet.iterator()
        var pointer = 0
        while (iterator.hasNext()) {
            val pwd = iterator.next() as String
            val pwdInt = Integer.valueOf(pwd)
            values[pointer++] = pwdInt.toByte()
            values[pointer++] = (pwdInt shr 8).toByte()
        }
        // 时间对照表 1000字节
        val timeTable = ByteArray(1000)
        for (i in 0..999) timeTable[i] = 0xFF.toByte()
        val timeControlTbBuilder = StringBuilder()
        timeControlTbBuilder.append('{')
        var pwdType = PWD_TYPE_MAX_DAY_180
        when (scene) {
            1 -> pwdType = PWD_TYPE_MAX_DAY_180
            2, 3 -> pwdType = PWD_TYPE_CONTAIN_MONTH
        }
        when (pwdType) {
            PWD_TYPE_MAX_DAY_180 -> {
                var i = 0
                while (i < 218) {
                    while (true) {
                        val random: Int = DigitUtil.generateRandomIntegerByUpperBound(1000)
                        if (timeTable[random] == 0xFF.toByte()) {
                            timeTable[random] = (if (i < 10) 0 else i - 9).toByte() // 单次的10种 后面顺延
                            if (i == 0) {
                                timeControlTbBuilder.append(0)
                                timeControlTbBuilder.append(':')
                                timeControlTbBuilder.append('[')
                                timeControlTbBuilder.append(String.format("%03d", random))
                            } else if (i > 0 && i < 9) {
                                timeControlTbBuilder.append(',')
                                timeControlTbBuilder.append(String.format("%03d", random))
                            } else if (i == 9) { // 单次拼接完成
                                timeControlTbBuilder.append(',')
                                timeControlTbBuilder.append(String.format("%03d", random))
                                timeControlTbBuilder.append(']')
                            } else { // 其它类型
                                timeControlTbBuilder.append(',')
                                timeControlTbBuilder.append(i - 9)
                                timeControlTbBuilder.append(':')
                                timeControlTbBuilder.append(String.format("%03d", random))
                            }
                            break
                        }
                    }
                    i++
                }
            }
            PWD_TYPE_CONTAIN_MONTH -> {
                var i = 0
                while (i < 255) {
                    while (true) {
                        val random: Int = DigitUtil.generateRandomIntegerByUpperBound(1000)
                        if (timeTable[random] == 0xFF.toByte()) {
                            timeTable[random] = (if (i < 10) 0 else i - 9).toByte() // 单次的10种 后面顺延
                            if (i == 0) {
                                timeControlTbBuilder.append(0)
                                timeControlTbBuilder.append(':')
                                timeControlTbBuilder.append('[')
                                timeControlTbBuilder.append(String.format("%03d", random))
                            } else if (i > 0 && i < 9) {
                                timeControlTbBuilder.append(',')
                                timeControlTbBuilder.append(String.format("%03d", random))
                            } else if (i == 9) { // 单次拼接完成
                                timeControlTbBuilder.append(',')
                                timeControlTbBuilder.append(String.format("%03d", random))
                                timeControlTbBuilder.append(']')
                            } else { // 其它类型
                                if (i < 138) { // 100天内
                                    timeControlTbBuilder.append(',')
                                    timeControlTbBuilder.append(i - 9)
                                    timeControlTbBuilder.append(':')
                                    timeControlTbBuilder.append(String.format("%03d", random))
                                } else if (i < 233) { // 1 ~ 24个月
                                    i = if (i == 138) 209 else i
                                    timeControlTbBuilder.append(',')
                                    timeControlTbBuilder.append(i)
                                    timeControlTbBuilder.append(':')
                                    timeControlTbBuilder.append(String.format("%03d", random))
                                } else if (i == 233) { // 永久
                                    i = 254
                                    timeControlTbBuilder.append(',')
                                    timeControlTbBuilder.append(i)
                                    timeControlTbBuilder.append(':')
                                    timeControlTbBuilder.append(String.format("%03d", random))
                                }
                            }
                            break
                        }
                    }
                    i++
                }
            }
            else -> {}
        }
        timeControlTbBuilder.append('}')
        System.arraycopy(timeTable, 0, values, pointer, 1000)
        pointer += 1000
        // 位置表
        val timePos = ByteArray(3) // 时间位置表
        val posSet: MutableSet<Byte> = TreeSet<Byte>()
        while (posSet.size < 3) {
            posSet.add((DigitUtil.generateRandomIntegerByUpperBound(7) + 1).toByte())
        }
        val positionBuilder = StringBuilder()
        positionBuilder.append('[')
        val posIterator: Iterator<Byte> = posSet.iterator()
        for (i in 0..2) {
            timePos[i] = posIterator.next()
            positionBuilder.append(timePos[i].toInt())
            positionBuilder.append(',')
        }
        positionBuilder.replace(positionBuilder.length - 1, positionBuilder.length, "]")
        System.arraycopy(timePos, 0, values, pointer, 3)
        pointer += 3
        // 校验对照表
        val checkingTable = ByteArray(10)
        val convertSet: MutableSet<Byte> = LinkedHashSet()
        while (convertSet.size < 10) {
            convertSet.add(DigitUtil.generateRandomIntegerByUpperBound(10).toByte())
        }
        val convertIterator: Iterator<Byte> = convertSet.iterator()
        val checkDigitBuilder = StringBuilder()
        for (i in 0..9) {
            checkingTable[i] = convertIterator.next()
            checkDigitBuilder.append(checkingTable[i].toInt())
        }
        System.arraycopy(checkingTable, 0, values, pointer, 10)
        values[1613] = validPwdNum
        val res = StringBuilder()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("position", positionBuilder)
            jsonObject.put("currentIndex", -1)
            jsonObject.put("timeControlTb", timeControlTbBuilder)
            jsonObject.put("fourKeyboardPwdList", fourKeyboardPwdList)
            jsonObject.put("checkDigit", checkDigitBuilder)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        pwdInfo = CommandUtil.encry(jsonObject.toString(), timestamp)
        LogUtil.d(
            "values:" + DigitUtil.byteArrayToHexString(Arrays.copyOfRange(values, 0, 1000)),
            DBG
        )
        LogUtil.d(
            "values:" + DigitUtil.byteArrayToHexString(
                Arrays.copyOfRange(
                    values,
                    1000,
                    1613
                )
            ),
            DBG
        )
        return jsonObject.toString()
        //        return values;
    }

    private fun generatePwd(pwdType: Int): String {
        val stringBuilder = StringBuilder()
        for (i in 0..4) {
            val pwd: String = DigitUtil.generatePwdByType(pwdType)
            pwdList!!.add(pwd)
            stringBuilder.append(pwd)
        }
        return stringBuilder.toString()
    }

    fun isConnected(address: String): Boolean {
        if (TextUtils.isEmpty(address)) {
            return false
        }
        return if (address == mBluetoothDeviceAddress && mConnectionState == STATE_CONNECTED) {
            true
        } else false
    }

    fun clearTask() {
        LogUtil.w("clear task", DBG)
        mHandler?.removeCallbacks(disConRunable)
        disTimerTask?.cancel()
        timer?.purge()
    }

    fun stopBTService() {
        stopScan()
            mHandler?.removeCallbacksAndMessages(null)
            disTimerTask?.cancel()
        isCanSendCommandAgain = true
        disTimerTask = null
        if (timer != null) {
            timer!!.cancel()
            timer!!.purge()
        }
        timer = null
        // TODO:后续调整
        disconnect()
        close()
        try {
            if (mApplicationContext != null && isBtStateReceiverRegistered) {
                mApplicationContext?.unregisterReceiver(bluttoothState)
                mApplicationContext = null
                isBtStateReceiverRegistered = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        LockCallbackManager.Companion.getInstance().clearAllCallback()
    }

    fun getConnectCnt(): Int {
        return connectCnt
    }

    fun setConnectCnt(connectCnt: Int) {
        this.connectCnt = connectCnt
    }

    fun isNeedReCon(): Boolean {
        return isNeedReCon
    }

    fun setNeedReCon(needReCon: Boolean) {
        isNeedReCon = needReCon
    }

    fun isScan(): Boolean {
        return scan
    }

    @Synchronized
    fun setScan(scan: Boolean) {
        this.scan = scan
    }

    fun setScanBongOnly(scanBongOnly: Boolean) {
        BluetoothImpl.scanBongOnly = scanBongOnly
    }

    /**
     * 在每次close前，先将BluetoothGatt  refresh一下，应该就可以了，这里的refresh只有通过反射的方式去执行，直接上代码：
     * 清理本地的BluetoothGatt 的缓存，以保证在蓝牙连接设备的时候，设备的服务、特征是最新的
     * @param gatt
     * @return
     */
    fun refreshDeviceCache(gatt: BluetoothGatt?): Boolean {
        if (null != gatt) {
            try {
                val localBluetoothGatt: BluetoothGatt = gatt
                val localMethod: Method =
                    localBluetoothGatt::class.java.getMethod("refresh")
                if (localMethod != null) {
                    return (
                        localMethod.invoke(
                            localBluetoothGatt, arrayOfNulls<Any>(0)
                        ) as Boolean
                        )
                }
            } catch (localException: Exception) {
                localException.printStackTrace()
            }
        }
        return false
    }

    fun parseIC(datas: ByteArray): Short {
        val nextReq: Short = (datas[0].toInt() shl 8 or (datas[1].toInt() and 0xff)).toShort()
        var dataIndex = 2
        while (dataIndex < datas.size) {
            val icCard = ICCard()
            LogUtil.d("datas.length:" + datas.size)
            var cardLen = 4
            if (datas.size == 20) cardLen = 8
            LogUtil.d("cardLen:$cardLen")

            // TODO:8位
            val cardNo: Long =
                DigitUtil.bytesToLong(Arrays.copyOfRange(datas, dataIndex, dataIndex + cardLen))
            icCard.cardNumber = cardNo.toString()
            dataIndex += cardLen
            var year = datas[dataIndex++] + 2000
            // 月
            var month = datas[dataIndex++].toInt()
            // 日
            var day = datas[dataIndex++].toInt()
            // 小时
            var hour = datas[dataIndex++].toInt()
            // 分钟
            var minute = datas[dataIndex++].toInt()
            val calendar: Calendar = Calendar.getInstance()
            // 根据时间偏移量计算时间
            val timeZone: TimeZone = TimeZone.getDefault()
            if (timeZone.inDaylightTime(Date(System.currentTimeMillis()))) {
                timezoneOffSet -= timeZone.getDSTSavings().toLong()
            }
            timeZone.setRawOffset(timezoneOffSet.toInt())
            calendar.setTimeZone(timeZone)
            calendar.set(year, month - 1, day, hour, minute)
            icCard.startDate = calendar.getTimeInMillis()
            year = datas[dataIndex++] + 2000
            // 月
            month = datas[dataIndex++].toInt()
            // 日
            day = datas[dataIndex++].toInt()
            // 小时
            hour = datas[dataIndex++].toInt()
            // 分钟
            minute = datas[dataIndex++].toInt()
            calendar.setTimeZone(timeZone)
            calendar.set(year, month - 1, day, hour, minute)
            icCard.endDate = calendar.getTimeInMillis()
            icCards!!.add(icCard)
        }
        return nextReq
    }

    fun parseFR(datas: ByteArray): Short {
        val nextReq: Short = (datas[0].toInt() shl 8 or (datas[1].toInt() and 0xff)).toShort()
        var dataIndex = 2
        while (dataIndex < datas.size) {
            val fr = FR()
            val cardNo: Long =
                DigitUtil.sixBytesToLong(Arrays.copyOfRange(datas, dataIndex, dataIndex + 6))
            fr.fingerprintNumber = cardNo.toString()
            dataIndex += 6
            var year = datas[dataIndex++] + 2000
            // 月
            var month = datas[dataIndex++].toInt()
            // 日
            var day = datas[dataIndex++].toInt()
            // 小时
            var hour = datas[dataIndex++].toInt()
            // 分钟
            var minute = datas[dataIndex++].toInt()
            val calendar: Calendar = Calendar.getInstance()
            // 根据时间偏移量计算时间
            val timeZone: TimeZone = TimeZone.getDefault()
            if (timeZone.inDaylightTime(Date(System.currentTimeMillis()))) {
                timezoneOffSet -= timeZone.getDSTSavings().toLong()
            }
            timeZone.setRawOffset(timezoneOffSet.toInt())
            calendar.setTimeZone(timeZone)
            calendar.set(year, month - 1, day, hour, minute)
            fr.startDate = calendar.getTimeInMillis()
            year = datas[dataIndex++] + 2000
            // 月
            month = datas[dataIndex++].toInt()
            // 日
            day = datas[dataIndex++].toInt()
            // 小时
            hour = datas[dataIndex++].toInt()
            // 分钟
            minute = datas[dataIndex++].toInt()
            calendar.setTimeZone(timeZone)
            calendar.set(year, month - 1, day, hour, minute)
            fr.endDate = calendar.getTimeInMillis()
            frs!!.add(fr)
        }
        return nextReq
    }

    private fun parsePasscode(datas: ByteArray): Short {
        val nextReq: Short = (datas[0].toInt() shl 8 or (datas[1].toInt() and 0xff)).toShort()
        var dataIndex = 2
        while (dataIndex < datas.size) {
            val recordLen = datas[dataIndex++].toInt()
            val passcode = Passcode()
            passcode.keyboardPwdType = datas[dataIndex++].toInt()
            var passcodeLen = datas[dataIndex++].toInt()
            passcode.newKeyboardPwd =
                String(Arrays.copyOfRange(datas, dataIndex, dataIndex + passcodeLen))
            dataIndex += passcodeLen
            passcodeLen = datas[dataIndex++].toInt()
            passcode.keyboardPwd =
                String(Arrays.copyOfRange(datas, dataIndex, dataIndex + passcodeLen))
            dataIndex += passcodeLen
            var year = datas[dataIndex++] + 2000

            // 月
            var month = datas[dataIndex++].toInt()
            // 日
            var day = datas[dataIndex++].toInt()
            // 小时
            var hour = datas[dataIndex++].toInt()
            // 分钟
            var minute = datas[dataIndex++].toInt()
            LogUtil.d("S year:$year")
            LogUtil.d("S month:$month")
            LogUtil.d("S day:$day")
            LogUtil.d("S hour:$hour")
            LogUtil.d("S minute:$minute")
            val calendar: Calendar = Calendar.getInstance()
            // 根据时间偏移量计算时间
            val timeZone: TimeZone = TimeZone.getDefault()
            if (timeZone.inDaylightTime(Date(System.currentTimeMillis()))) {
                timezoneOffSet -= timeZone.getDSTSavings().toLong()
            }
            timeZone.setRawOffset(timezoneOffSet.toInt())
            calendar.setTimeZone(timeZone)
            calendar.set(year, month - 1, day, hour, minute, 0)
            passcode.startDate = calendar.getTimeInMillis()
            when (passcode.keyboardPwdType) {
                1 -> {}
                2, 3 -> {
                    year = datas[dataIndex++] + 2000
                    // 月
                    month = datas[dataIndex++].toInt()
                    // 日
                    day = datas[dataIndex++].toInt()
                    // 小时
                    hour = datas[dataIndex++].toInt()
                    // 分钟
                    minute = datas[dataIndex++].toInt()
                    calendar.setTimeZone(timeZone)
                    LogUtil.d("year:$year")
                    LogUtil.d("month:$month")
                    LogUtil.d("day:$day")
                    calendar.set(year, month - 1, day, hour, minute)
                    passcode.endDate = calendar.getTimeInMillis()
                }
                4 ->
                    passcode.cycleType =
                        (datas[dataIndex++].toInt() shl 8 or (datas[dataIndex++].toInt() and 0xff)).toShort()
                            .toInt()
                else -> {}
            }

            // TODO:跟服务器一致
            if (passcode.keyboardPwdType == 1) passcode.keyboardPwdType =
                2 else if (passcode.keyboardPwdType == 2) passcode.keyboardPwdType = 1
            passcodes!!.add(passcode)
        }
        return nextReq
    }

    private fun getLockInfoObj(): LockData? {
//        LockData lockData = new LockData();
        lockData!!.lockName = mExtendedBluetoothDevice!!.getName()
        lockData!!.lockMac = mExtendedBluetoothDevice!!.getAddress()
        lockData!!.electricQuantity = mExtendedBluetoothDevice!!.getBatteryCapacity()
        lockData!!.adminPwd = DigitUtil.encodeLockData(adminPs!!)
        lockData!!.lockKey = DigitUtil.encodeLockData(unlockKey!!)
        lockData!!.noKeyPwd = adminPasscode
        lockData!!.deletePwd = deletePwd
        lockData!!.pwdInfo = pwdInfo
        lockData!!.timestamp = timestamp
        lockData!!.aesKeyStr = DigitUtil.encodeAesKey(aesKeyArray)
        //        lockData.specialValue = feature;
        lockData!!.modelNum = modelNumber
        lockData!!.hardwareRevision = hardwareRevision
        lockData!!.firmwareRevision = firmwareRevision
        if (!TextUtils.isEmpty(factoryDate)) {
            lockData!!.factoryDate = factoryDate!!
        }
        val headRef: String = lockData!!.lockMac!!.substring(lockData!!.lockMac!!.length - 5)
        lockData!!.ref = DigitUtil.encodeLockData(headRef + lockData!!.factoryDate)
        if (FeatureValueUtil.isSupportFeature(
                lockData,
                FeatureValue.NB_LOCK
            ) && deviceInfo != null
        ) {
            lockData!!.nbNodeId = deviceInfo!!.nbNodeId
            lockData!!.nbCardNumber = deviceInfo!!.nbCardNumber
            lockData!!.nbRssi = deviceInfo!!.nbRssi
            lockData!!.nbOperator = deviceInfo!!.nbOperator
        }
        if (FeatureValueUtil.isSupportFeature(
                lockData,
                FeatureValue.AUTO_LOCK.toInt()
            )
        ) { // 锁内关闭跟服务端定义的值不一样
            lockData!!.autoLockTime =
                if (lockData!!.autoLockTime == 0) LockDataSwitchValue.AUTO_LOCK_CLOSE else lockData!!.autoLockTime
        }
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.PASSWORD_DISPLAY_OR_HIDE)) {
            lockData!!.displayPasscode =
                if (lockData!!.displayPasscode != 0) LockDataSwitchValue.OPEN else LockDataSwitchValue.CLOSE
        }
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.AUDIO_MANAGEMENT)) {
            lockData!!.lockSound =
                if (lockData!!.lockSound != 0) LockDataSwitchValue.OPEN else LockDataSwitchValue.CLOSE
        }
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.SENSITIVITY)) {
            lockData!!.setSensitivity(sensitivity)
        }
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.INCOMPLETE_PASSCODE)) {
            lockData!!.passcodeKeyNumber = deviceInfo!!.passcodeKeyNumber
        }

        // 读取的时候直接放到了lockData里面
//        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.SOUND_VOLUME_AND_LANGUAGE_SETTING)) {
//            lockData.soundVolume = soundVolume;
//        }
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.RESET_BUTTON)) {
            lockData!!.resetButton =
                if (switchValue and TTLockConfigType.RESET_BUTTON.getItem() != 0) LockDataSwitchValue.OPEN else LockDataSwitchValue.CLOSE
        }
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.TAMPER_ALERT)) {
            lockData!!.tamperAlert =
                if (switchValue and TTLockConfigType.TAMPER_ALERT.getItem() != 0) LockDataSwitchValue.OPEN else LockDataSwitchValue.CLOSE
        }
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.PRIVACY_LOCK)) {
            lockData!!.privacyLock =
                if (switchValue and TTLockConfigType.PRIVACY_LOCK.getItem() != 0) LockDataSwitchValue.OPEN else LockDataSwitchValue.CLOSE
        }
        lockData!!.setSettingValue(settingValue)
        return lockData
    }

    /**
     * 读取新的操作记录需要返回缓存的数据
     */
    private fun readCacheLog() {
        if (currentAPICommand == APICommand.OP_GET_OPERATE_LOG && logOperates != null && logOperates!!.size > 0 && transferData != null && transferData!!.getLogType() == LogType.NEW) {
            returnCacheLog()
        }
    }

    private fun returnCacheLog() {
        synchronized(logOperates!!) {
            if (logOperates != null && logOperates!!.size > 0) {
                LogUtil.d("cache log")
                //                getTTLockCallback().onGetOperateLog(mExtendedBluetoothDevice, GsonUtil.toJson(logOperates), Error.SUCCESS);
                logOperates!!.clear()
            }
        }
    }

    fun clearRecordCnt() {
        LogUtil.d("recordCnt:$recordCnt")
        lastRecordSeq = 0
        recordCnt = 0
    }

    fun doWithPassageModeData(datas: ByteArray) {
        val len = datas.size
        LogUtil.d("len:$len")
        var index = 0
        while (index < len) {
            val passageModeData = PassageModeData()
            passageModeData.type = datas[index++].toInt()
            passageModeData.weekOrDay = datas[index++].toInt()
            passageModeData.month = datas[index++].toInt()
            val startHour = datas[index++].toInt()
            val startMinute = datas[index++].toInt()
            passageModeData.startDate = startHour * 60 + startMinute
            val endHour = datas[index++].toInt()
            val endMinute = datas[index++].toInt()
            passageModeData.endDate = endHour * 60 + endMinute
            passageModeDatas!!.add(passageModeData)
        }
    }

    private fun initLock(command: Command) {
        if (command.getLockType() == LockType.LOCK_TYPE_V3_CAR || command.getLockType() == LockType.LOCK_TYPE_V3) {
            CommandUtil.searchDeviceFeature(command.getLockType())
            // 车位锁没有后续指令
        } else if (command.getLockType() == LockType.LOCK_TYPE_CAR) {
            val lockData: LockData = getLockInfoObj()!!
            lockData.lockVersion = command.getLockVersion()
            lockData.noKeyPwd = ""
            lockData.deletePwd = ""
            lockData.pwdInfo = ""
            lockData.timestamp = 0
            lockData.aesKeyStr = ""
            lockData.specialValue = 0
            val addLockCallback: LockCallback? =
                LockCallbackManager.Companion.getInstance().getCallback()
            if (addLockCallback != null) {
                (addLockCallback as InitLockCallback).onInitLockSuccess(lockData.encodeLockData())
            }
        } else {
            // 接后续指令 全部完成再回调
            adminPasscode = DigitUtil.generatePwdByLength(7)
            CommandUtil.S_setAdminKeyboardPwd(command.getLockType(), adminPasscode, aesKeyArray)
        }
    }

    private fun addICResponse(command: Command, data: ByteArray) {
        when (data[4]) {
            ICOperate.STATUS_ADD_SUCCESS -> if (currentAPICommand == APICommand.OP_RECOVERY_DATA) { // 循环的恢复
                when (recoveryData!!.cardType) {
                    ValidityInfo.Companion.TIMED -> {
                        dataPos++
                        if (dataPos < recoveryDatas!!.size) {
                            val recoveryData: RecoveryData = recoveryDatas!![dataPos]
                            CommandUtil.recoveryICCardPeriod(
                                command.getLockType(),
                                recoveryData!!.cardNumber,
                                recoveryData!!.startDate,
                                recoveryData!!.endDate,
                                aesKeyArray,
                                timezoneOffSet
                            )
                        } else {
                            val mRecoveryDataCallback: LockCallback? =
                                LockCallbackManager.Companion.getInstance().getCallback()
                            if (mRecoveryDataCallback != null) {
                                (mRecoveryDataCallback as RecoverLockDataCallback).onRecoveryDataSuccess(
                                    transferData!!.getOp()
                                )
                            }
                        }
                    }
                    ValidityInfo.Companion.CYCLIC -> {
                        attachmentNum = java.lang.Long.valueOf(recoveryData!!.cardNumber)
                        initIcAndFrCyclicData(recoveryData!!.cyclicConfig!!)
                        if (cyclicConfigs != null && cyclicPos < cyclicConfigs!!.size) {
                            CommandUtil_V3.addIcCyclicDate(
                                command,
                                attachmentNum,
                                CyclicOpType.CYCLIC_TYPE_WEEK,
                                cyclicConfigs!![cyclicPos],
                                aesKeyArray
                            )
                        }
                    }
                }
            } else { // 添加ic卡
                var len = data.size
                if (len > 8 && data[len - 1] == 0xff.toByte() && data[len - 2] == 0xff.toByte() && data[len - 3] == 0xff.toByte() && data[len - 4] == 0xff.toByte()) {
                    len -= 4
                }
                val cardNo: Long = DigitUtil.bytesToLong(Arrays.copyOfRange(data, 5, len))
                failNeedDelete = true
                // 添加完了都走修改有效期指令
                attachmentNum = cardNo
                CommandUtil.modifyICCardPeriod(
                    command.getLockType(),
                    cardNo.toString(),
                    startDate,
                    endDate,
                    aesKeyArray,
                    timezoneOffSet
                )
            }
            ICOperate.STATUS_ENTER_ADD_MODE -> {
                LogUtil.d("entry into the add mode of ic card", DBG)
                if (currentAPICommand == APICommand.OP_RECOVERY_DATA) { // 恢复模式
                    dataPos++
                    LogUtil.e("dataPos:$dataPos", DBG)
                    if (dataPos < recoveryDatas!!.size) {
                        val recoveryData: RecoveryData = recoveryDatas!![dataPos]
                        CommandUtil.recoveryICCardPeriod(
                            command.getLockType(),
                            recoveryData!!.cardNumber,
                            recoveryData!!.startDate,
                            recoveryData!!.endDate,
                            aesKeyArray,
                            timezoneOffSet
                        )
                    } else {
                        val mRecoveryDataCallback: LockCallback? =
                            LockCallbackManager.Companion.getInstance().getCallback()
                        if (mRecoveryDataCallback != null) {
                            (mRecoveryDataCallback as RecoverLockDataCallback).onRecoveryDataSuccess(
                                transferData!!.getOp()
                            )
                        }
                    }
                } else { // 添加模式
                    isWaitCommand = true
                    val mAddCardCallback: LockCallback? =
                        LockCallbackManager.Companion.getInstance().getCallbackWithoutRemove()
                    if (mAddCardCallback != null) {
                        (mAddCardCallback as AddICCardCallback).onEnterAddMode()
                    }
                }
            }
        }
    }

    private fun modifyICPeriodResponse(command: Command) {
        when (currentAPICommand) {
            APICommand.OP_ADD_IC -> {
                when (transferData!!.getValidityInfo()!!.getModeType()) {
                    ValidityInfo.Companion.TIMED -> {
                        val addIcCardCallback: LockCallback? =
                            LockCallbackManager.Companion.getInstance().getCallback()
                        if (addIcCardCallback != null) {
                            (addIcCardCallback as AddICCardCallback).onAddICCardSuccess(
                                attachmentNum
                            )
                        }
                    }
                    ValidityInfo.Companion.CYCLIC -> {
                        // todo:失败要删除
                        initIcAndFrCyclicData(transferData!!.getValidityInfo()!!.getCyclicConfigs())
                        if (cyclicConfigs != null && cyclicPos < cyclicConfigs!!.size) {
                            CommandUtil_V3.addIcCyclicDate(
                                command,
                                attachmentNum,
                                CyclicOpType.CYCLIC_TYPE_WEEK,
                                cyclicConfigs!![cyclicPos],
                                aesKeyArray
                            )
                        } else { // 没有循环数据的当做普通卡添加
                            val addIcCardCallback =
                                LockCallbackManager.Companion.getInstance().getCallback()
                            if (addIcCardCallback != null) {
                                (addIcCardCallback as AddICCardCallback).onAddICCardSuccess(
                                    attachmentNum
                                )
                            }
                        }
                    }
                }
            }
            APICommand.OP_MODIFY_IC_PERIOD -> {
                when (transferData!!.getValidityInfo()!!.getModeType()) {
                    ValidityInfo.Companion.TIMED -> {
                        val mModifyCallback: LockCallback? =
                            LockCallbackManager.Companion.getInstance().getCallback()
                        if (mModifyCallback != null) {
                            (mModifyCallback as ModifyICCardPeriodCallback).onModifyICCardPeriodSuccess()
                        }
                    }
                    ValidityInfo.Companion.CYCLIC -> CommandUtil_V3.clearICCyclicPeriod(
                        command,
                        attachmentNum,
                        CyclicOpType.CYCLIC_TYPE_WEEK,
                        aesKeyArray
                    )
                }
            }
        }
    }

    private fun addFRResponse(command: Command, data: ByteArray) {
        if (data[4].toInt() == 0x01) { // 添加成功
            if (currentAPICommand == APICommand.OP_RECOVERY_DATA) {
                when (recoveryData!!.fingerprintType) {
                    ValidityInfo.Companion.TIMED -> {
                        dataPos++
                        if (dataPos < recoveryDatas!!.size) {
                            // TODO:暂时延时
                            val recoveryData: RecoveryData = recoveryDatas!![dataPos]
                            CommandUtil.recoveryFRPeriod(
                                command.getLockType(),
                                java.lang.Long.valueOf(recoveryData!!.fingerprintNumber),
                                recoveryData!!.startDate,
                                recoveryData!!.endDate,
                                aesKeyArray,
                                timezoneOffSet
                            )
                        } else {
                            val mRecoveryDataCallback: LockCallback? =
                                LockCallbackManager.Companion.getInstance().getCallback()
                            if (mRecoveryDataCallback != null) {
                                (mRecoveryDataCallback as RecoverLockDataCallback).onRecoveryDataSuccess(
                                    transferData!!.getOp()
                                )
                            }
                        }
                    }
                    ValidityInfo.Companion.CYCLIC -> {
                        attachmentNum = java.lang.Long.valueOf(recoveryData!!.fingerprintNumber)
                        initIcAndFrCyclicData(recoveryData!!.cyclicConfig)
                        if (cyclicConfigs != null && cyclicPos < cyclicConfigs!!.size) {
                            CommandUtil_V3.addFrCyclicDate(
                                command,
                                attachmentNum,
                                CyclicOpType.CYCLIC_TYPE_WEEK,
                                cyclicConfigs!![cyclicPos],
                                aesKeyArray
                            )
                        }
                    }
                }
            } else { // 添加指纹
                val FRNo: Long = DigitUtil.sixBytesToLong(Arrays.copyOfRange(data, 5, data.size))
                failNeedDelete = true
                // 添加完了走修改有效期接口
                attachmentNum = FRNo
                CommandUtil.modifyFRPeriod(
                    command.getLockType(),
                    attachmentNum,
                    startDate,
                    endDate,
                    aesKeyArray,
                    timezoneOffSet
                )
            }
        } else if (data[4].toInt() == 0x02) { // 成功启动添加指纹模式，这时候App可以提示“请按手指”
            isWaitCommand = true
            var totalCnt = -1
            LogUtil.d("data.length:" + data.size, DBG)
            if (data.size == 6) {
                totalCnt = data[5].toInt()
            }
            val mFingerptAddModeCallback: LockCallback? =
                LockCallbackManager.Companion.getInstance().getCallbackWithoutRemove()
            if (mFingerptAddModeCallback != null) {
                (mFingerptAddModeCallback as AddFingerprintCallback).onEnterAddMode(totalCnt)
            }
            LogUtil.d("entry into the add mode of fingerprint", DBG)
        } else if (data[4].toInt() == 0x03) { // 指纹采集进度
            isWaitCommand = true
            LogUtil.d("the first collection successed，then collect the second", DBG)
            var curCnt = -1
            var totalCnt = -1
            if (data.size == 7) {
                curCnt = data[5].toInt()
                totalCnt = data[6].toInt()
            }
            if (curCnt == 0) {
                val mPrepareModeAddFR: LockCallback? =
                    LockCallbackManager.Companion.getInstance().getCallbackWithoutRemove()
                if (mPrepareModeAddFR != null) {
                    (mPrepareModeAddFR as AddFingerprintCallback).onEnterAddMode(totalCnt)
                }
                LogUtil.d("entry into the add mode of fingerprint", DBG)
            } else {
                val mCollectFR: LockCallback? =
                    LockCallbackManager.Companion.getInstance().getCallbackWithoutRemove()
                if (mCollectFR != null) {
                    (mCollectFR as AddFingerprintCallback).onCollectFingerprint(curCnt)
                }
            }
        } else if (data[4].toInt() == 0x04) { // 准备接收指纹模板
            isWaitCommand = true
            val seq: Short = (data[5].toInt() shl 8 or (data[6].toInt() and 0xff)).toShort()
            packetLen = (data[7].toInt() shl 8 or (data[8].toInt() and 0xff)).toShort().toInt()
            CommandUtil_V3.writeFR(
                command,
                transferData!!.getTransferData(),
                seq,
                packetLen,
                transferData!!.getAesKeyArray()
            )
        }
    }

    private fun modifyFrPeriodResponse(command: Command) {
        when (currentAPICommand) {
            APICommand.OP_ADD_FR -> when (transferData!!.getValidityInfo()!!.getModeType()) {
                ValidityInfo.Companion.TIMED -> {
                    val mAddFingerprint: LockCallback? =
                        LockCallbackManager.Companion.getInstance().getCallback()
                    if (mAddFingerprint != null) {
                        (mAddFingerprint as AddFingerprintCallback).onAddFingerpintFinished(
                            attachmentNum
                        )
                    }
                }
                ValidityInfo.Companion.CYCLIC -> {
                    // todo:失败要删除
                    initIcAndFrCyclicData(transferData!!.getValidityInfo()!!.getCyclicConfigs())
                    if (cyclicConfigs != null && cyclicPos < cyclicConfigs!!.size) {
                        CommandUtil_V3.addFrCyclicDate(
                            command,
                            attachmentNum,
                            CyclicOpType.CYCLIC_TYPE_WEEK,
                            cyclicConfigs!![cyclicPos],
                            aesKeyArray
                        )
                    } else { // 没有循环数据的当做普通指纹添加
                        val mAddFingerprint = LockCallbackManager.Companion.getInstance().getCallback()
                        if (mAddFingerprint != null) {
                            (mAddFingerprint as AddFingerprintCallback).onAddFingerpintFinished(
                                attachmentNum
                            )
                        }
                    }
                }
            }
            APICommand.OP_MODIFY_FR_PERIOD -> when (
                transferData!!.getValidityInfo()
                    !!.getModeType()
            ) {
                ValidityInfo.Companion.TIMED -> {
                    val mModifyFrPeriod: LockCallback? =
                        LockCallbackManager.Companion.getInstance().getCallback()
                    if (mModifyFrPeriod != null) {
                        (mModifyFrPeriod as ModifyFingerprintPeriodCallback).onModifyPeriodSuccess()
                    }
                }
                ValidityInfo.Companion.CYCLIC -> CommandUtil_V3.clearFrCyclicPeriod(
                    command,
                    attachmentNum,
                    CyclicOpType.CYCLIC_TYPE_WEEK,
                    aesKeyArray
                )
            }
        }
    }

    private fun addCyclicDataResponse(command: Command) {
        cyclicPos++
        if (cyclicConfigs != null && cyclicPos < cyclicConfigs!!.size) {
            when (currentAPICommand) {
                APICommand.OP_ADD_IC, APICommand.OP_MODIFY_IC_PERIOD -> CommandUtil_V3.addIcCyclicDate(
                    command,
                    attachmentNum,
                    CyclicOpType.CYCLIC_TYPE_WEEK,
                    cyclicConfigs!![cyclicPos],
                    aesKeyArray
                )
                APICommand.OP_ADD_FR, APICommand.OP_MODIFY_FR_PERIOD -> CommandUtil_V3.addFrCyclicDate(
                    command,
                    attachmentNum,
                    CyclicOpType.CYCLIC_TYPE_WEEK,
                    cyclicConfigs!![cyclicPos],
                    aesKeyArray
                )
                APICommand.OP_ADD_KEY_FOB, APICommand.OP_MODIFY_KEY_FOB_PERIOD -> CommandUtil_V3.addKeyFobCyclicDate(
                    command,
                    transferData!!.getKeyFobMac()!!,
                    CyclicOpType.CYCLIC_TYPE_WEEK,
                    cyclicConfigs!![cyclicPos],
                    aesKeyArray
                )
                APICommand.OP_RECOVERY_DATA -> when (transferData!!.getOp()) {
                    RecoveryDataType.IC_CARD -> CommandUtil_V3.addIcCyclicDate(
                        command,
                        attachmentNum,
                        CyclicOpType.CYCLIC_TYPE_WEEK,
                        cyclicConfigs!![cyclicPos],
                        aesKeyArray
                    )
                    RecoveryDataType.FINGERPRINT -> CommandUtil_V3.addFrCyclicDate(
                        command,
                        attachmentNum,
                        CyclicOpType.CYCLIC_TYPE_WEEK,
                        cyclicConfigs!![cyclicPos],
                        aesKeyArray
                    )
                }
            }
        } else {
            when (currentAPICommand) {
                APICommand.OP_ADD_IC -> {
                    val addIcCardCallback: LockCallback? =
                        LockCallbackManager.Companion.getInstance().getCallback()
                    if (addIcCardCallback != null) {
                        (addIcCardCallback as AddICCardCallback).onAddICCardSuccess(attachmentNum)
                    }
                }
                APICommand.OP_MODIFY_IC_PERIOD -> {
                    val mModifyCallback: LockCallback? =
                        LockCallbackManager.Companion.getInstance().getCallback()
                    if (mModifyCallback != null) {
                        (mModifyCallback as ModifyICCardPeriodCallback).onModifyICCardPeriodSuccess()
                    }
                }
                APICommand.OP_ADD_FR -> {
                    val mAddFingerprint: LockCallback? =
                        LockCallbackManager.Companion.getInstance().getCallback()
                    if (mAddFingerprint != null) {
                        (mAddFingerprint as AddFingerprintCallback).onAddFingerpintFinished(
                            attachmentNum
                        )
                    }
                }
                APICommand.OP_MODIFY_FR_PERIOD -> {
                    val mModifyFrPeriod: LockCallback? =
                        LockCallbackManager.Companion.getInstance().getCallback()
                    if (mModifyFrPeriod != null) {
                        (mModifyFrPeriod as ModifyFingerprintPeriodCallback).onModifyPeriodSuccess()
                    }
                }
                APICommand.OP_ADD_KEY_FOB -> {
                    val mAddRemote: LockCallback? =
                        LockCallbackManager.Companion.getInstance().getCallback()
                    if (mAddRemote != null) {
                        (mAddRemote as AddRemoteCallback).onAddSuccess()
                    }
                }
                APICommand.OP_MODIFY_KEY_FOB_PERIOD -> {
                    val mModifyRemote: LockCallback? =
                        LockCallbackManager.Companion.getInstance().getCallback()
                    if (mModifyRemote != null) {
                        (mModifyRemote as ModifyRemoteValidityPeriodCallback).onModifySuccess()
                    }
                }
                APICommand.OP_RECOVERY_DATA -> {
                    dataPos++
                    if (dataPos < recoveryDatas!!.size) {
                        recoveryData = recoveryDatas!![dataPos]
                        when (transferData!!.getOp()) {
                            2 -> CommandUtil.recoveryICCardPeriod(
                                command.getLockType(),
                                recoveryData!!.cardNumber,
                                recoveryData!!.startDate,
                                recoveryData!!.endDate,
                                aesKeyArray,
                                timezoneOffSet
                            )
                            3 -> CommandUtil.recoveryFRPeriod(
                                command.getLockType(),
                                java.lang.Long.valueOf(recoveryData!!.fingerprintNumber),
                                recoveryData!!.startDate,
                                recoveryData!!.endDate,
                                aesKeyArray,
                                timezoneOffSet
                            )
                        }
                    } else {
                        val mRecoveryDataCallback: LockCallback? =
                            LockCallbackManager.Companion.getInstance().getCallback()
                        if (mRecoveryDataCallback != null) {
                            (mRecoveryDataCallback as RecoverLockDataCallback).onRecoveryDataSuccess(
                                transferData!!.getOp()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun clearCyclicDataResponse(command: Command) {
        when (currentAPICommand) {
            APICommand.OP_MODIFY_IC_PERIOD -> {
                initIcAndFrCyclicData(transferData!!.getValidityInfo()!!.getCyclicConfigs())
                if (cyclicConfigs != null && cyclicPos < cyclicConfigs!!.size) {
                    CommandUtil_V3.addIcCyclicDate(
                        command,
                        attachmentNum,
                        CyclicOpType.CYCLIC_TYPE_WEEK,
                        cyclicConfigs!![cyclicPos],
                        aesKeyArray
                    )
                } else { // 没有循环数据的情况当做修改普通有效期
                    val mModifyCallback: LockCallback? =
                        LockCallbackManager.Companion.getInstance()!!.getCallback()
                    if (mModifyCallback != null) {
                        (mModifyCallback as ModifyICCardPeriodCallback).onModifyICCardPeriodSuccess()
                    }
                }
            }
            APICommand.OP_MODIFY_FR_PERIOD -> {
                initIcAndFrCyclicData(transferData!!.getValidityInfo()!!.getCyclicConfigs())
                if (cyclicConfigs != null && cyclicPos < cyclicConfigs!!.size) {
                    CommandUtil_V3.addFrCyclicDate(
                        command,
                        attachmentNum,
                        CyclicOpType.CYCLIC_TYPE_WEEK,
                        cyclicConfigs!![cyclicPos],
                        aesKeyArray
                    )
                } else { // 没有循环数据的情况当做修改普通有效期
                    val mModifyFrPeriod: LockCallback? =
                        LockCallbackManager.Companion.getInstance().getCallback()
                    if (mModifyFrPeriod != null) {
                        (mModifyFrPeriod as ModifyFingerprintPeriodCallback).onModifyPeriodSuccess()
                    }
                }
            }
        }
    }

    private fun initIcAndFrCyclicData(cyclicConfigList: List<CyclicConfig>?) {
        cyclicPos = 0
        cyclicConfigs = cyclicConfigList
    }

    private fun errorResponse(command: Command, data: ByteArray) {
        lockError = LockError.Companion.getInstance(data[2].toInt())
        lockError!!.setCommand(data[0])
        if (data[2].toInt() == LockError.COMMAND_RECEIVED.getIntErrorCode()) { // 设置wifi AP的时候表示命令接收成功，正在处理
            // 什么都不做 等待后续反馈
        } else if (data[0] == Command.Companion.COMM_CYCLIC_CMD && data[2].toInt() == LockError.RECORD_NOT_EXIST.getIntErrorCode()) { // 循环指令 清空报记录不存在的情况
            // 循环记录不存在的情况都当做清楚成功 继续后续操作
            clearCyclicDateSuccessResponse(command)
        } else if (currentAPICommand == APICommand.OP_ADD_ADMIN && data[0] == Command.Companion.COMM_SWITCH) { // 初始化锁的过程中 不支持读取开关状态的 继续读取其它数据
            doQueryCommand(command)
        } else if (lockError == LockError.IC_CARD_NOT_EXIST && currentAPICommand == APICommand.OP_LOSS_IC) { // 挂失卡 卡号不存在的当成功处理
            val lockCallback: LockCallback? =
                LockCallbackManager.Companion.getInstance().getCallback()
            if (lockCallback != null) {
                (lockCallback as ReportLossCardCallback).onReportLossCardSuccess()
            }
        } else if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
            when (data[0]) {
                Command.Companion.COMM_TIME_CALIBRATE -> initLock(command)
                Command.Companion.COMM_ADD_ADMIN -> {
                    lockError = LockError.Failed
                    errorCallback(lockError!!)
                }
                else -> errorCallback(lockError!!)
            }
        } else if (currentAPICommand == APICommand.OP_CALIBRATE_TIME && data[0] == Command.Companion.COMM_CHECK_USER_TIME) { // 锁时间错误 的情况 直接校准
            CommandUtil.C_calibationTime(
                command.getLockType(),
                calibationTime,
                timezoneOffSet,
                aesKeyArray!!
            )
        } else if (currentAPICommand == APICommand.OP_ADD_KEY_FOB && transferData!!.hasCyclicConfig()) { // 循环日期出错 做删除操作
            tmpLockError = lockError
            CommandUtil_V3.deleteKeyFob(command, transferData, aesKeyArray)
        } else if (currentAPICommand == APICommand.OP_ADD_IC && failNeedDelete) { // 有效期跟循环日期出错 做删除操作
            tmpLockError = lockError
            failNeedDelete = false
            // 删除IC卡
            CommandUtil.deleteICCard(command.getLockType(), attachmentNum.toString(), aesKeyArray)
        } else if (currentAPICommand == APICommand.OP_ADD_FR && failNeedDelete) {
            // 删除指纹
            tmpLockError = lockError
            failNeedDelete = false
            CommandUtil.deleteFR(command.getLockType(), attachmentNum, aesKeyArray)
        } else if (currentAPICommand == APICommand.OP_MODIFY_IC_PERIOD && data[2].toInt() == LockError.RECORD_NOT_EXIST.getIntErrorCode()) { // 失败当成功处理
            initIcAndFrCyclicData(transferData!!.getValidityInfo()!!.getCyclicConfigs())
            if (cyclicConfigs != null && cyclicPos < cyclicConfigs!!.size) {
                CommandUtil_V3.addIcCyclicDate(
                    command,
                    attachmentNum,
                    CyclicOpType.CYCLIC_TYPE_WEEK,
                    cyclicConfigs!![cyclicPos],
                    aesKeyArray
                )
            }
        } else if (currentAPICommand == APICommand.OP_MODIFY_FR_PERIOD && data[2].toInt() == LockError.RECORD_NOT_EXIST.getIntErrorCode()) { // 失败当成功处理
            initIcAndFrCyclicData(transferData!!.getValidityInfo()!!.getCyclicConfigs())
            if (cyclicConfigs != null && cyclicPos < cyclicConfigs!!.size) {
                CommandUtil_V3.addFrCyclicDate(
                    command,
                    attachmentNum,
                    CyclicOpType.CYCLIC_TYPE_WEEK,
                    cyclicConfigs!![cyclicPos],
                    aesKeyArray
                )
            }
        } else if (currentAPICommand == APICommand.OP_RECOVERY_DATA && data[2].toInt() != 0x16) { // 如果是恢复密码的情况 且错误情况下 继续恢复(除了锁空间不足)
            dataPos++
            if (dataPos < recoveryDatas!!.size) {
                val recoveryData: RecoveryData = recoveryDatas!![dataPos]
                when (transferData!!.getOp()) {
                    RecoveryDataType.PASSCODE -> CommandUtil.manageKeyboardPassword(
                        command.getLockType(),
                        PwdOperateType.PWD_OPERATE_TYPE_RECOVERY,
                        (if (recoveryData!!.keyboardPwdType == 2) 1 else recoveryData!!.keyboardPwdType).toByte(),
                        recoveryData!!.cycleType,
                        recoveryData!!.keyboardPwd,
                        recoveryData!!.keyboardPwd,
                        recoveryData!!.startDate,
                        recoveryData!!.endDate,
                        aesKeyArray,
                        timezoneOffSet
                    )
                    RecoveryDataType.IC_CARD -> CommandUtil.recoveryICCardPeriod(
                        command.getLockType(),
                        recoveryData.cardNumber,
                        recoveryData.startDate,
                        recoveryData.endDate,
                        aesKeyArray,
                        timezoneOffSet
                    )
                    RecoveryDataType.FINGERPRINT -> CommandUtil.recoveryFRPeriod(
                        command.getLockType(),
                        java.lang.Long.valueOf(recoveryData.fingerprintNumber),
                        recoveryData.startDate,
                        recoveryData.endDate,
                        aesKeyArray,
                        timezoneOffSet
                    )
                    else -> {}
                }
            } else {
                val mRecoveryDataCallback: LockCallback? =
                    LockCallbackManager.Companion.getInstance().getCallback()
                if (mRecoveryDataCallback != null) {
                    (mRecoveryDataCallback as RecoverLockDataCallback).onRecoveryDataSuccess(
                        transferData!!.getOp()
                    )
                }
            }
        } else if (data[0] == Command.Companion.COMM_MANAGE_KEYBOARD_PASSWORD && data[3] == PwdOperateType.PWD_OPERATE_TYPE_MODIFY) { // 修改密码失败 再判断一下特征值
            tmpLockError = lockError
            CommandUtil.searchDeviceFeature(command.getLockType())
        } else {
            errorCallback(lockError!!)
        }
    }

    private fun genCommandQue(command: Command) {
        commandQueue = LinkedList<Byte>()

//        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.RESET_BUTTON)
//                || FeatureValueUtil.isSupportFeature(lockData, FeatureValue.TAMPER_ALERT)
//                || FeatureValueUtil.isSupportFeature(lockData, FeatureValue.PRIVACY_LOCK)) {
        commandQueue!!.add(Command.Companion.COMM_SWITCH) // 默认都调用获取开关状态接口 不支持失败的继续走其它指令
        //        }
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.SENSITIVITY)) {
            commandQueue!!.add(Command.Companion.COMM_SENSITIVITY_MANAGE)
        }
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.AUDIO_MANAGEMENT)) {
            commandQueue!!.add(Command.Companion.COMM_AUDIO_MANAGE)
        }
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.PASSWORD_DISPLAY_OR_HIDE)) {
            commandQueue!!.add(Command.Companion.COMM_SHOW_PASSWORD)
        }
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.AUTO_LOCK.toInt())) {
            commandQueue!!.add(Command.Companion.COMM_AUTO_LOCK_MANAGE)
        }
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.LAMP)) {
            commandQueue!!.add(Command.Companion.COMM_LAMP)
        }

        // TODO:后续调整
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.GET_ADMIN_CODE)) {
            commandQueue!!.add(Command.Companion.COMM_GET_ADMIN_CODE)
        } else if (command.getLockType() == LockType.LOCK_TYPE_V3_CAR) {
            commandQueue!!.add(Command.Companion.COMM_READ_DEVICE_INFO)
            //            commandQueue.add(Command.COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED);
        } else if (command.getLockType() == LockType.LOCK_TYPE_V3) { // 剩下三代锁走设置管理码
            commandQueue!!.add(Command.Companion.COMM_SET_ADMIN_KEYBOARD_PWD)
        }
        doQueryCommand(command)
    }

    private fun doResponse(comm: Byte, command: Command) {
        when (currentAPICommand) {
            APICommand.OP_ADD_ADMIN -> doQueryCommand(command)
            else -> callback(comm)
        }
    }

    private fun callback(comm: Byte) {
        val lockCallback: LockCallback? = LockCallbackManager.Companion.getInstance().getCallback()
        when (comm) {
            Command.Companion.COMM_SWITCH -> {
                val ttLockConfigType: TTLockConfigType? =
                    TTLockConfigType.Companion.getInstance(transferData!!.getOp())
                var switchOn = switchValue and transferData!!.getOp() != 0
                if (TTLockConfigType.WIFI_LOCK_POWER_SAVING_MODE == ttLockConfigType) { // 0是省电模式 跟其它开关项正好相反
                    switchOn = !switchOn
                }
                (lockCallback as GetLockConfigCallback).onGetLockConfigSuccess(
                    ttLockConfigType,
                    switchOn
                )
            }
            Command.Companion.COMM_AUDIO_MANAGE -> if (lockCallback is GetLockConfigCallback) {
                (lockCallback as GetLockConfigCallback).onGetLockConfigSuccess(
                    TTLockConfigType.LOCK_SOUND,
                    transferData!!.getOpValue() == 1
                )
            } else if (lockCallback is GetLockMuteModeStateCallback) { // 静音模式
                (lockCallback as GetLockMuteModeStateCallback).onGetMuteModeStateSuccess(
                    transferData!!.getOpValue() == 0
                )
            } else if (lockCallback is GetLockSoundWithSoundVolumeCallback) {
                (lockCallback as GetLockSoundWithSoundVolumeCallback).onGetLockSoundSuccess(
                    transferData!!.getOpValue() == 1,
                    transferData!!.getSoundVolume()
                )
            } else {
                LogUtil.d("mMuteModeCallback:$lockCallback")
            }
            Command.Companion.COMM_SHOW_PASSWORD -> if (lockCallback is GetLockConfigCallback) {
                (lockCallback as GetLockConfigCallback).onGetLockConfigSuccess(
                    TTLockConfigType.PASSCODE_VISIBLE,
                    lockData!!.displayPasscode == 1
                )
            } else if (lockCallback is GetPasscodeVisibleStateCallback) {
                (lockCallback as GetPasscodeVisibleStateCallback).onGetPasscodeVisibleStateSuccess(
                    lockData!!.displayPasscode == 1
                )
            }
            Command.Companion.COMM_LAMP -> (lockCallback as GetLightTimeCallback).onGetLightTimeSuccess(
                lockData!!.lightingTime
            )
        }
    }

    /**
     * 获取下一条需要操作的指令
     * @param command
     */
    private fun doQueryCommand(command: Command) {
        if (commandQueue != null && commandQueue!!.size > 0) {
            when (commandQueue!!.poll()) {
                Command.Companion.COMM_SWITCH -> {
                    settingValue = null
                    CommandUtil_V3.getSwitchState(command, aesKeyArray)
                }
                Command.Companion.COMM_AUDIO_MANAGE -> CommandUtil_V3.audioManage(
                    command,
                    AudioManage.QUERY,
                    transferData!!.getOpValue().toByte(),
                    aesKeyArray
                )
                Command.Companion.COMM_SHOW_PASSWORD -> CommandUtil.screenPasscodeManage(
                    command.getLockType(),
                    OperationType.GET_STATE.toInt(),
                    aesKeyArray
                )
                Command.Companion.COMM_AUTO_LOCK_MANAGE -> CommandUtil.searchAutoLockTime(
                    command.getLockType(),
                    aesKeyArray
                )
                Command.Companion.COMM_LAMP -> CommandUtil_V3.controlLamp(
                    command,
                    OperationType.GET_STATE,
                    transferData!!.getOpValue().toShort(),
                    aesKeyArray
                )
                Command.Companion.COMM_GET_ADMIN_CODE -> CommandUtil_V3.getAdminCode(command)
                Command.Companion.COMM_READ_DEVICE_INFO -> readModelNumber(command)
                Command.Companion.COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED -> CommandUtil.operateFinished(
                    command.getLockType()
                )
                Command.Companion.COMM_SET_ADMIN_KEYBOARD_PWD -> {
                    adminPasscode = DigitUtil.generatePwdByLength(7)
                    CommandUtil.S_setAdminKeyboardPwd(
                        command.getLockType(),
                        adminPasscode,
                        aesKeyArray
                    )
                }
                Command.Companion.COMM_SENSITIVITY_MANAGE -> CommandUtil_V3.getSensitivity(
                    command,
                    aesKeyArray
                )
            }
        }
    }

    private fun addSdkLog() {
        addSdkLog(sdkLogItem)
    }

    fun addSdkLog(log: String?) {
        if (sdkLogBuilder != null) {
            sdkLogBuilder!!.append(log)
            sdkLogBuilder!!.append('\n')
        }
        LogUtil.d(log, DBG)
    }

    private fun doConnectFailedCallback() {
        val mConnectCallback: ConnectCallback? =
            LockCallbackManager.Companion.getInstance().getConnectCallback()
        if (mConnectCallback != null) {
            lockError = LockError.LOCK_CONNECT_FAIL
            setSdkLog()
            mConnectCallback.onFail(lockError!!)
        }
    }

    private fun clearSdkLog() {
        if (sdkLogBuilder != null) {
            sdkLogBuilder!!.setLength(0)
        }
    }

    private fun setSdkLog() {
        setSdkLog(lockError)
    }

    private fun setSdkLog(lockError: LockError?) {
        if (lockError != null) {
            lockError.setSdkLog(getSdkLog())
        }
    }

    fun getSdkLog(): String {
        return if (sdkLogBuilder != null && sdkLogBuilder!!.length > 0) {
            sdkLogBuilder.toString()
        } else "null log"
    }

    fun clearWifi() {
        wiFis.clear()
    }

    @Synchronized
    private fun insertWifi(newWifi: WiFi) {
        var hasInsert = false
        for (i in wiFis.indices) {
            val wiFi: WiFi = wiFis[i]
            if (newWifi.ssid == wiFi.ssid) { // 信号值排序要改
                if (newWifi.rssi > wiFi.rssi) {
                    wiFi.setRssi(newWifi.getRssi())
                }
                hasInsert = true
                break
            }
        }
        if (!hasInsert) {
            wiFis.add(newWifi)
        }
    }

    /**
     * 有循环操作的都需要先清空循环时间
     * @param command
     */
    private fun clearCyclicDateSuccessResponse(command: Command) {
        val validityInfo: ValidityInfo = transferData!!.getValidityInfo()!!
        when (currentAPICommand) {
            APICommand.OP_ADD_KEY_FOB -> if (transferData!!.hasCyclicConfig()) { // 循环配置
                initIcAndFrCyclicData(validityInfo.getCyclicConfigs())
                if (cyclicConfigs != null && cyclicPos < cyclicConfigs!!.size) {
                    CommandUtil_V3.addKeyFobCyclicDate(
                        command,
                        transferData!!.getKeyFobMac()!!,
                        CyclicOpType.CYCLIC_TYPE_WEEK,
                        cyclicConfigs!![cyclicPos],
                        aesKeyArray
                    )
                }
            } else { // 普通类型
                val lockCallback: LockCallback? =
                    LockCallbackManager.Companion.getInstance().getCallback()
                (lockCallback as AddRemoteCallback).onAddSuccess()
            }
            APICommand.OP_MODIFY_KEY_FOB_PERIOD -> if (transferData!!.hasCyclicConfig()) { // 循环配置
                initIcAndFrCyclicData(validityInfo.getCyclicConfigs())
                if (cyclicConfigs != null && cyclicPos < cyclicConfigs!!.size) {
                    CommandUtil_V3.addKeyFobCyclicDate(
                        command,
                        transferData!!.getKeyFobMac()!!,
                        CyclicOpType.CYCLIC_TYPE_WEEK,
                        cyclicConfigs!![cyclicPos],
                        aesKeyArray
                    )
                }
            } else { // 普通类型
                val lockCallback: LockCallback? =
                    LockCallbackManager.Companion.getInstance().getCallback()
                (lockCallback as ModifyRemoteValidityPeriodCallback).onModifySuccess()
            }
        }
    }

    private fun readModelNumber(command: Command) {
        // 读取版本信息
        tempOptype = DeviceInfoType.MODEL_NUMBER.toInt()
        CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.MODEL_NUMBER, aesKeyArray)
        mExtendedBluetoothDevice!!.disconnectStatus =
            ExtendedBluetoothDevice.Companion.RESPONSE_TIME_OUT
        mHandler?.postDelayed(disConRunable, 1500)
    }

    companion object {
        private const val DBG = true
        private val TAG = BluetoothImpl::class.java.simpleName
        fun getInstance(): BluetoothImpl {
            return InstanceHolder.mInstance!!
        }

        /**
         * 重连次数
         */
        private const val MAX_CONNECT_COUNT = 3

        /**
         * 再次连接的最长间隔时间
         */
        private const val MAX_CONNECT_INTEVAL = 2000
        private const val PWD_TYPE_MAX_DAY_180 = 1
        private const val PWD_TYPE_CONTAIN_MONTH = 2
        const val STATE_DISCONNECTED = 0
        const val STATE_CONNECTING = 1
        const val STATE_CONNECTED = 2
        var mConnectionState = STATE_DISCONNECTED
        val UUID_HEART_RATE_MEASUREMENT: UUID =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        var UUID_SERVICE = "00001910-0000-1000-8000-00805f9b34fb"
        var UUID_WRITE = "0000fff2-0000-1000-8000-00805f9b34fb"
        var UUID_READ = "0000fff4-0000-1000-8000-00805f9b34fb"
        const val TTL_SERVICE = "00001910-0000-1000-8000-00805f9b34fb"
        const val TTL_WRITE = "0000fff2-0000-1000-8000-00805f9b34fb"
        const val TTL_READ = "0000fff4-0000-1000-8000-00805f9b34fb"
        private const val BONG_SERVICE = "6e400001-b5a3-f393-e0a9-e50e24dcca1e"
        private const val BONG_WRITE = "6e400002-b5a3-f393-e0a9-e50e24dcca1e"
        private const val BONG_READ = "6e400003-b5a3-f393-e0a9-e50e24dcca1e"
        private const val DEVICE_INFORMATION_SERVICE = "0000180a-0000-1000-8000-00805f9b34fb"
        private const val READ_MODEL_NUMBER_UUID = "00002a24-0000-1000-8000-00805f9b34fb"
        private const val READ_FIRMWARE_REVISION_UUID = "00002a26-0000-1000-8000-00805f9b34fb"
        private const val READ_HARDWARE_REVISION_UUID = "00002a27-0000-1000-8000-00805f9b34fb"
        private const val READ_MANUFACTURER_NAME_UUID = "00002a29-0000-1000-8000-00805f9b34fb"

        /**
         * 判断是否只搜索手环
         */
        var scanBongOnly = false

        /**-------------------------指令数据--------------------- */
        var aesKeyArray: ByteArray? = null

        /**
         * 扫描周期
         */
        private const val SCAN_PERIOD: Long = 5000

        /**
         * 连接超时时间
         */
        private const val CONNECT_TIME_OUT = 10000
        var isCheckedLockPermission = false

        /**
         * 约定数和随机数之和 遥控设备 同一次连接多次发送值相同
         */
        var unlockPwdBytes: ByteArray = byteArrayOf()

        /**
         * 遥控设备要用 同一次连接多次发送值相同
         */
        var uniqueidBytes: ByteArray = byteArrayOf()

        /**
         * 判断遥控设备连续发指令
         */
        var isCanSendCommandAgain = true
    }
}
