package com.ttlock.bl.sdk.api;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import androidx.annotation.RequiresPermission;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.callback.ActivateLiftFloorsCallback;
import com.ttlock.bl.sdk.callback.AddDoorSensorCallback;
import com.ttlock.bl.sdk.callback.AddFingerprintCallback;
import com.ttlock.bl.sdk.callback.AddICCardCallback;
import com.ttlock.bl.sdk.callback.AddRemoteCallback;
import com.ttlock.bl.sdk.callback.ClearAllFingerprintCallback;
import com.ttlock.bl.sdk.callback.ClearAllICCardCallback;
import com.ttlock.bl.sdk.callback.ClearPassageModeCallback;
import com.ttlock.bl.sdk.callback.ClearRemoteCallback;
import com.ttlock.bl.sdk.callback.ConfigServerCallback;
import com.ttlock.bl.sdk.callback.ConfigIpCallback;
import com.ttlock.bl.sdk.callback.ConfigWifiCallback;
import com.ttlock.bl.sdk.callback.ConnectCallback;
import com.ttlock.bl.sdk.callback.ControlLockCallback;
import com.ttlock.bl.sdk.callback.CreateCustomPasscodeCallback;
import com.ttlock.bl.sdk.callback.DeleteDoorSensorCallback;
import com.ttlock.bl.sdk.callback.DeleteFingerprintCallback;
import com.ttlock.bl.sdk.callback.DeleteICCardCallback;
import com.ttlock.bl.sdk.callback.DeletePassageModeCallback;
import com.ttlock.bl.sdk.callback.DeletePasscodeCallback;
import com.ttlock.bl.sdk.callback.DeleteRemoteCallback;
import com.ttlock.bl.sdk.callback.EnterDfuModeCallback;
import com.ttlock.bl.sdk.callback.GetAccessoryBatteryLevelCallback;
import com.ttlock.bl.sdk.callback.GetAdminPasscodeCallback;
import com.ttlock.bl.sdk.callback.GetAllValidFingerprintCallback;
import com.ttlock.bl.sdk.callback.GetAllValidICCardCallback;
import com.ttlock.bl.sdk.callback.GetAllValidPasscodeCallback;
import com.ttlock.bl.sdk.callback.GetAutoLockingPeriodCallback;
import com.ttlock.bl.sdk.callback.GetBatteryLevelCallback;
import com.ttlock.bl.sdk.callback.GetLightTimeCallback;
import com.ttlock.bl.sdk.callback.GetLockConfigCallback;
import com.ttlock.bl.sdk.callback.GetLockFreezeStateCallback;
import com.ttlock.bl.sdk.callback.GetLockMuteModeStateCallback;
import com.ttlock.bl.sdk.callback.GetLockSoundWithSoundVolumeCallback;
import com.ttlock.bl.sdk.callback.GetLockStatusCallback;
import com.ttlock.bl.sdk.callback.GetLockSystemInfoCallback;
import com.ttlock.bl.sdk.callback.GetLockTimeCallback;
import com.ttlock.bl.sdk.callback.GetLockVersionCallback;
import com.ttlock.bl.sdk.callback.GetNBAwakeModesCallback;
import com.ttlock.bl.sdk.callback.GetNBAwakeTimesCallback;
import com.ttlock.bl.sdk.callback.GetOperationLogCallback;
import com.ttlock.bl.sdk.callback.GetPassageModeCallback;
import com.ttlock.bl.sdk.callback.GetPasscodeVerificationInfoCallback;
import com.ttlock.bl.sdk.callback.GetPasscodeVisibleStateCallback;
import com.ttlock.bl.sdk.callback.GetRemoteUnlockStateCallback;
import com.ttlock.bl.sdk.callback.GetSpecialValueCallback;
import com.ttlock.bl.sdk.callback.GetUnlockDirectionCallback;
import com.ttlock.bl.sdk.callback.GetWifiInfoCallback;
import com.ttlock.bl.sdk.callback.InitLockCallback;
import com.ttlock.bl.sdk.callback.LockCallback;
import com.ttlock.bl.sdk.callback.ModifyAdminPasscodeCallback;
import com.ttlock.bl.sdk.callback.ModifyFingerprintPeriodCallback;
import com.ttlock.bl.sdk.callback.ModifyICCardPeriodCallback;
import com.ttlock.bl.sdk.callback.ModifyPasscodeCallback;
import com.ttlock.bl.sdk.callback.ModifyRemoteValidityPeriodCallback;
import com.ttlock.bl.sdk.callback.OnScanFailedListener;
import com.ttlock.bl.sdk.callback.RecoverLockDataCallback;
import com.ttlock.bl.sdk.callback.ReportLossCardCallback;
import com.ttlock.bl.sdk.callback.ResetKeyCallback;
import com.ttlock.bl.sdk.callback.ResetLockCallback;
import com.ttlock.bl.sdk.callback.ResetPasscodeCallback;
import com.ttlock.bl.sdk.callback.ScanLockCallback;
import com.ttlock.bl.sdk.callback.ScanWifiCallback;
import com.ttlock.bl.sdk.callback.SetAutoLockingPeriodCallback;
import com.ttlock.bl.sdk.callback.SetHotelCardSectorCallback;
import com.ttlock.bl.sdk.callback.SetHotelDataCallback;
import com.ttlock.bl.sdk.callback.SetLiftControlableFloorsCallback;
import com.ttlock.bl.sdk.callback.SetLiftWorkModeCallback;
import com.ttlock.bl.sdk.callback.SetLightTimeCallback;
import com.ttlock.bl.sdk.callback.SetLockConfigCallback;
import com.ttlock.bl.sdk.callback.SetLockFreezeStateCallback;
import com.ttlock.bl.sdk.callback.SetLockMuteModeCallback;
import com.ttlock.bl.sdk.callback.SetLockSoundWithSoundVolumeCallback;
import com.ttlock.bl.sdk.callback.SetLockTimeCallback;
import com.ttlock.bl.sdk.callback.SetNBAwakeModesCallback;
import com.ttlock.bl.sdk.callback.SetNBAwakeTimesCallback;
import com.ttlock.bl.sdk.callback.SetNBServerCallback;
import com.ttlock.bl.sdk.callback.SetPassageModeCallback;
import com.ttlock.bl.sdk.callback.SetPasscodeVisibleCallback;
import com.ttlock.bl.sdk.callback.SetPowerSaverControlableLockCallback;
import com.ttlock.bl.sdk.callback.SetPowerSaverWorkModeCallback;
import com.ttlock.bl.sdk.callback.SetRemoteUnlockSwitchCallback;
import com.ttlock.bl.sdk.callback.SetUnlockDirectionCallback;
import com.ttlock.bl.sdk.constant.APICommand;
import com.ttlock.bl.sdk.constant.ActionType;
import com.ttlock.bl.sdk.constant.AudioManage;
import com.ttlock.bl.sdk.constant.AutoLockOperate;
import com.ttlock.bl.sdk.constant.CommandResponse;
import com.ttlock.bl.sdk.constant.ConfigRemoteUnlock;
import com.ttlock.bl.sdk.constant.Constant;
import com.ttlock.bl.sdk.constant.ControlAction;
import com.ttlock.bl.sdk.constant.CyclicOpType;
import com.ttlock.bl.sdk.constant.DeviceInfoType;
import com.ttlock.bl.sdk.constant.FeatureValue;
import com.ttlock.bl.sdk.constant.ICOperate;
import com.ttlock.bl.sdk.constant.KeyFobOperationType;
import com.ttlock.bl.sdk.constant.KeyboardPwdType;
import com.ttlock.bl.sdk.constant.LockDataSwitchValue;
import com.ttlock.bl.sdk.constant.LockType;
import com.ttlock.bl.sdk.constant.LogOperate;
import com.ttlock.bl.sdk.constant.LogType;
import com.ttlock.bl.sdk.constant.OperationType;
import com.ttlock.bl.sdk.constant.PassageModeOperate;
import com.ttlock.bl.sdk.constant.PwdOperateType;
import com.ttlock.bl.sdk.constant.RecoveryData;
import com.ttlock.bl.sdk.constant.RecoveryDataType;
import com.ttlock.bl.sdk.constant.SensitivityOperationType;
import com.ttlock.bl.sdk.entity.AccessoryInfo;
import com.ttlock.bl.sdk.entity.AccessoryType;
import com.ttlock.bl.sdk.entity.ActivateLiftFloorsResult;
import com.ttlock.bl.sdk.entity.ControlLockResult;
import com.ttlock.bl.sdk.entity.CyclicConfig;
import com.ttlock.bl.sdk.entity.DeviceInfo;
import com.ttlock.bl.sdk.entity.FR;
import com.ttlock.bl.sdk.entity.HotelData;
import com.ttlock.bl.sdk.entity.ICCard;
import com.ttlock.bl.sdk.entity.KeyboardPwd;
import com.ttlock.bl.sdk.entity.LockData;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.entity.LockVersion;
import com.ttlock.bl.sdk.entity.NBAwakeConfig;
import com.ttlock.bl.sdk.entity.Passcode;
import com.ttlock.bl.sdk.entity.PwdInfoV3;
import com.ttlock.bl.sdk.entity.SoundVolume;
import com.ttlock.bl.sdk.entity.TTLockConfigType;
import com.ttlock.bl.sdk.entity.TransferData;
import com.ttlock.bl.sdk.entity.UnlockDirection;
import com.ttlock.bl.sdk.entity.ValidityInfo;
import com.ttlock.bl.sdk.entity.WifiLockInfo;
import com.ttlock.bl.sdk.executor.AppExecutors;
import com.ttlock.bl.sdk.gateway.model.WiFi;
import com.ttlock.bl.sdk.scanner.IScanCallback;
import com.ttlock.bl.sdk.scanner.ScannerCompat;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.FeatureValueUtil;
import com.ttlock.bl.sdk.util.GsonUtil;
import com.ttlock.bl.sdk.util.LogUtil;
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.DoorSensorCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import static com.ttlock.bl.sdk.api.Command.COMM_READ_DEVICE_INFO;
import static com.ttlock.bl.sdk.constant.APICommand.OP_WRITE_FR;

/**
 * Created on  2019/4/11 0011 16:16
 *
 * @author theodre
 */
class BluetoothImpl {

    private static final boolean DBG = true;
    private static final String TAG = BluetoothImpl.class.getSimpleName();

    private BluetoothImpl(){
        initialize();
    }

    public static BluetoothImpl getInstance(){
        return BluetoothImpl.InstanceHolder.mInstance;
    }

    private static class InstanceHolder{
        private final static BluetoothImpl mInstance = new BluetoothImpl();
    }

    private Context mApplicationContext;

    private boolean isBtStateReceiverRegistered = false;

    private AppExecutors mAppExecutor;


    /**
     * 是否有等待指令
     */
    private boolean isWaitCommand;

    /**
     * 重连次数
     */
    private static final int MAX_CONNECT_COUNT = 3;

    /**
     * 再次连接的最长间隔时间
     */
    private static final int MAX_CONNECT_INTEVAL = 2000;

    /**
     * 进行连接的时间
     */
    private long connectTime;

    /**
     * 是否需要重连
     */
    private boolean isNeedReCon = true;

    /**
     * 已经连接次数
     */
    private int connectCnt = 0;

    private static final int PWD_TYPE_MAX_DAY_180 = 1;
    private static final int PWD_TYPE_CONTAIN_MONTH = 2;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    //可以改成device
    private String mBluetoothDeviceAddress;
    private BluetoothDevice mBluetoothDevice;
    private ExtendedBluetoothDevice mExtendedBluetoothDevice;

    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    public static int mConnectionState = STATE_DISCONNECTED;

    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static String UUID_SERVICE = "00001910-0000-1000-8000-00805f9b34fb";
    public static String UUID_WRITE = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static String UUID_READ = "0000fff4-0000-1000-8000-00805f9b34fb";

    public static final String TTL_SERVICE = "00001910-0000-1000-8000-00805f9b34fb";
    public static final String TTL_WRITE = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static final String TTL_READ = "0000fff4-0000-1000-8000-00805f9b34fb";

    private static final String BONG_SERVICE = "6e400001-b5a3-f393-e0a9-e50e24dcca1e";
    private static final String BONG_WRITE = "6e400002-b5a3-f393-e0a9-e50e24dcca1e";
    private static final String BONG_READ = "6e400003-b5a3-f393-e0a9-e50e24dcca1e";

    private static final String DEVICE_INFORMATION_SERVICE = "0000180a-0000-1000-8000-00805f9b34fb";
    private static final String READ_MODEL_NUMBER_UUID = "00002a24-0000-1000-8000-00805f9b34fb";
    private static final String READ_FIRMWARE_REVISION_UUID = "00002a26-0000-1000-8000-00805f9b34fb";
    private static final String READ_HARDWARE_REVISION_UUID = "00002a27-0000-1000-8000-00805f9b34fb";
    private static final String READ_MANUFACTURER_NAME_UUID = "00002a29-0000-1000-8000-00805f9b34fb";

    private BluetoothGattCharacteristic modelNumberCharacteristic;
    private BluetoothGattCharacteristic hardwareRevisionCharacteristic;
    private BluetoothGattCharacteristic firmwareRevisionCharacteristic;


    private Context mContext;

    private Handler mHandler;
    /**
     * 判断是否只搜索手环
     */
    public static boolean scanBongOnly;


    /**
     * 扫描器
     */
    private ScannerCompat mScanner;
    private boolean mScanning;

    /**
     * 扫描回调
     */
    private ScanCallback scanCallback;


    /**
     * 传输数据
     */
    private LinkedList<byte[]> dataQueue;

    private LinkedList<byte[]> cloneDataQueue;

    /**
     * 接收的数据
     */
    private byte[] mReceivedDataBuffer;

    /**
     * 接收数据长度
     */
    private int mReceivedBufferCount;

    /**
     * 待接收数据长度
     */
    private int leftRecDataCount;

    /**
     * 接收的最大长度
     */
    private int maxBufferCount = 256;

    /**-------------------------指令数据---------------------*/
    public static byte[] aesKeyArray;

    /**
     * 接口操作指令
     */
    private int currentAPICommand;

    /**
     * 判断管理员
     */
    String adminPs;

    /**
     * 开门约定数
     */
    String unlockKey;

    /**
     * 需要校准的时间
     */
    private long calibationTime;

    /**
     * 锁时区和UTC时区时间的差数，单位milliseconds
     */
    private long timezoneOffSet;

    /**
     * 三代锁使用 账号id
     */
    int mUid;

    /**
     * 密码数据
     */
    String pwdInfo;

    /**
     * 时间戳
     */
    long timestamp;

    /**
     * 同时有效密码数
     * 0默认值
     */
    byte validPwdNum = 0;

    /**
     * 传输数据
     */
    byte[] pwdData;

    /**
     * 传输位置
     */
    int dataPos;

    /**
     * 每次传输包长度
     */
    int packetLen = 28;

    /**
     * 键盘密码
     */
    String adminPasscode;

    /**
     * 删除密码
     */
    String deletePwd;

    /**
     * 锁标志位
     */
    int lockFlagPos;

    /**
     * 设置的锁名称
     */
    String lockname;

    /**
     * 原始密码
     */
    String originalPwd;

    /**
     * 新密码
     */
    String newPwd;

    /**
     * 起始时间
     */
    long startDate;

    /**
     * 结束时间
     */
    long endDate;

    /**
     * 键盘密码类型
     */
    byte keyboardPwdType;

    private Queue<String> pwdList;

    /**
     * 操作日志
     */
    private List<LogOperate> logOperates;

    /**
     * 动车位锁
     */
    private JSONArray moveDateArray;

    /**
     * 扫描周期
     */
    private static final long SCAN_PERIOD = 5000;

    /**
     * 三代锁开门判重使用
     */
    private long unlockDate;//TODO:

    /**
     * IC卡号或指纹卡号
     */
    private long attachmentNum;

    /**
     * 错误
     */
    Timer timer;


    LockError lockError;
    LockError tmpLockError;

    /**
     * 判断是否扫描
     */
    public boolean scan;

    /**
     * 设备特征 默认1 表示支持密码
     */
//    private int feature = 1;

    /**
     * 手环KEY
     */
    private String wristbandKey;

    /**
     * 版本信息
     */
    private String lockVersionString;

    /** ------------------ 设备信息 --------------- */

    /**
     * 产品型号
     */
    private String modelNumber;

    /**
     * 硬件版本号
     */
    private String hardwareRevision;

    /**
     * 固件版本号
     */
    private String firmwareRevision;

    /**
     * 生产日期
     */
    private String factoryDate;

    /**
     * 时钟
     */
    private String lockClock;

    /**
     * 连接超时时间
     */
    private static final int CONNECT_TIME_OUT = 10000;

    /**
     * TODO:考虑合并
     * 密码列表
     */
    private List<String> pwds;

    private List<RecoveryData> recoveryDatas;

    private int tempOptype;

    private TransferData transferData;

    private boolean isSetLockName;

    private DeviceInfo deviceInfo;

    private ArrayList<ICCard> icCards;

    private ArrayList<FR> frs;

    private ArrayList<Passcode> passcodes;

    private final ReentrantLock conLock = new ReentrantLock();

    public static boolean isCheckedLockPermission;

    private byte[] psFromLock;

    /**
     * 约定数和随机数之和 遥控设备 同一次连接多次发送值相同
     */
    public static byte[] unlockPwdBytes;
    /**
     * 遥控设备要用 同一次连接多次发送值相同
     */
    public static byte[] uniqueidBytes;

    /**
     * 判断遥控设备连续发指令
     */
    public static boolean isCanSendCommandAgain = true;

    private int commandSendCount;

    private LockData lockData;

    private HotelData hotelData;

    /**
     * 记录读取记录的次数
     */
    private int recordCnt;

    /**
     * 上一次读取记录的序号
     */
    private int lastRecordSeq;

    private List<PassageModeData> passageModeDatas;

    private JSONArray weerOrDays;


    /**
     * 循环数据位置
     */
    int cyclicPos;
    private List<CyclicConfig> cyclicConfigs;

    private RecoveryData recoveryData;

    private LinkedList<Byte> commandQueue;

    /**
     * IC跟指纹添加过程中 失败的情况 是否需要删除 卡跟指纹
     */
    private boolean failNeedDelete = false;

    /**
     * 锁配置里面的开关项
     */
    private int switchItem;
    /**
     * 锁配置里面的开关值
     */
    private int switchValue;

    private StringBuilder sdkLogBuilder = new StringBuilder();

    private String sdkLogItem;

    private List<WiFi> wiFis = new ArrayList<WiFi>();

    private int sensitivity;//灵敏度：0-关闭、1-低、2-中、3-高

    /**
     * 不支持的情况不传该字段
     */
    private Integer settingValue;//用于表示防撬开关、重置按键开关，反锁开关、开门方向、常开模式自动开锁开关、Wifi锁省电模式开关等设置项

    BroadcastReceiver bluttoothState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String stateExtra = BluetoothAdapter.EXTRA_STATE;
            int state = intent.getIntExtra(stateExtra, BluetoothAdapter.STATE_OFF);
            switch (state) {
                case BluetoothAdapter.STATE_TURNING_ON:
                    LogUtil.d("BluetoothAdapter.STATE_TURNING_ON", DBG);
                    break;
                case BluetoothAdapter.STATE_ON:
                    LogUtil.d("BluetoothAdapter.STATE_ON", DBG);
                    if(scan) {
//                        startScan();
                        if (mHandler != null){
                            mHandler.postDelayed(stateOnScanRunable, 1000);
                        }
                    } else {
                        LogUtil.d("do not start scan", DBG);
                    }
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    LogUtil.d("BluetoothAdapter.STATE_TURNING_OFF", DBG);
                    break;
                case BluetoothAdapter.STATE_OFF:
                    LogUtil.d("BluetoothAdapter.STATE_OFF", DBG);
                    mConnectionState = STATE_DISCONNECTED;
//                    close();
                    break;
                default:
                    break;
            }
        }
    };

    //TODO:
    TimerTask disTimerTask;

    Runnable stateOnScanRunable = new Runnable() {
        @Override
        public void run() {
            startScan();
        }
    };

    private OnScanFailedListener onScanFailedListener;
    private boolean scanAll;

    Runnable disConRunable = new Runnable() {
        @Override
        public void run() {
            if(mConnectionState == STATE_CONNECTED) {
                LogUtil.d("disconnecting……", DBG);
                disconnect();
            } else if(mConnectionState == STATE_CONNECTING) {
                LogUtil.d("disconnecting……", DBG);
                //TODO:这里断开 再次连接会 mNotifyCharacteristic or mBluetoothGatt is null mBluetoothGatt:null
                disconnect();
                //TODO:这里会变为null
                close();
//                startScan();
                LockCallback mTimeOutCallback = LockCallbackManager.getInstance().getConnectCallback();

               if(mTimeOutCallback != null) {
                    mExtendedBluetoothDevice.disconnectStatus = ExtendedBluetoothDevice.CONNECT_TIME_OUT;
                    mTimeOutCallback.onFail(LockError.LOCK_CONNECT_FAIL);
                } else {
                    LogUtil.w("mTTLockCallback is null", DBG);
                }
            }
        }
    };

    //TODO:设置专门的回调线程
    class ScanCallback implements IScanCallback {

        @Override
        public void onScan(final ExtendedBluetoothDevice extendedBluetoothDevice) {

            if(mScanning && mConnectionState == STATE_DISCONNECTED) {
                ScanLockCallback mScanCallback =  LockCallbackManager.getInstance().getLockScanCallback();
                if(mScanCallback == null){
                    return;
                }

                /**
                 * 手环
                 */
                if(scanBongOnly) {
                    if(extendedBluetoothDevice.isWristband()){
                        mScanCallback.onScanLockSuccess(extendedBluetoothDevice);
                    }
                } else{
                    mScanCallback.onScanLockSuccess(extendedBluetoothDevice);
                }

            }
        }
    }


    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        @RequiresPermission(Manifest.permission.BLUETOOTH)
        @TargetApi(21)
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if(mBluetoothGatt != gatt) {
                LogUtil.w("gatt=" + gatt + " status=" + status + " newState=" + newState, DBG);
                gatt.close();
                return;
            }
            failNeedDelete = false;
            LogUtil.i("gatt=" + gatt + " status=" + status + " newState=" + newState, true);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_BALANCED);

//                mConnectionState = STATE_CONNECTED;//移到写descriptor里面

                Log.i(TAG, "Connected to GATT server.");
//                isNeedReCon = false;

                mHandler.removeCallbacks(disConRunable);


//                try {
//                    Thread.sleep(600);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                //发现服务发现不了 重连需要
                connectTime = System.currentTimeMillis();
                if (mBluetoothGatt != null){
                    Log.i(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
                }else {
                    mConnectionState = STATE_DISCONNECTED;
                }


            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                isWaitCommand = false;
                isCanSendCommandAgain = true;
                isCheckedLockPermission = false;
                mHandler.removeCallbacks(disConRunable);
                if(isNeedReCon && connectCnt < MAX_CONNECT_COUNT && System.currentTimeMillis() - connectTime < MAX_CONNECT_INTEVAL) {
                    LogUtil.w("connect again:" + connectCnt, DBG);
                    //TODO:用mac地址连接的 没有广播信息 添加管理员
//                    connect(mBluetoothDevice.getAddress());
                    try {
                        conLock.lock();
                        connect(mExtendedBluetoothDevice);
                    } finally {
                        conLock.unlock();
                    }
                } else {
                    mConnectionState = STATE_DISCONNECTED;
                    Log.i(TAG, "Disconnected from GATT server.");
                    close();
                    readCacheLog();
                    mAppExecutor.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            doConnectFailedCallback();
                        }
                    });
                }
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            if (mBluetoothGatt != gatt)
                return;
            LogUtil.d("gatt=" + gatt + " status=" + status, DBG);
            LogUtil.d(Thread.currentThread().toString().toString(), DBG);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (mBluetoothGatt == null) {
                    LogUtil.w("mBluetoothGatt null", true);
                    return;
                }
                if(scanBongOnly) {
                    UUID_SERVICE = BONG_SERVICE;
                    UUID_READ = BONG_READ;
                    UUID_WRITE = BONG_WRITE;
                } else {
                    UUID_SERVICE = TTL_SERVICE;
                    UUID_READ = TTL_READ;
                    UUID_WRITE = TTL_WRITE;
                }

//                for (BluetoothGattService service : gatt.getServices()) {//TODO:test
//                    LogUtil.d("service:" + service.getUuid(), DBG);
//                }

                BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(DEVICE_INFORMATION_SERVICE));
                if (service != null) {
                    List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();
                    if (gattCharacteristics != null && gattCharacteristics.size() > 0) {
                        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                            LogUtil.d(gattCharacteristic.getUuid().toString(), DBG);
                            LogUtil.d("read characteristic:" + Thread.currentThread(), DBG);
                            if (gattCharacteristic.getUuid().toString().equals(READ_MODEL_NUMBER_UUID)) {
                                modelNumberCharacteristic = gattCharacteristic;
//                                gatt.readCharacteristic(gattCharacteristic);
                            } else if (gattCharacteristic.getUuid().toString().equals(READ_FIRMWARE_REVISION_UUID)) {
                                firmwareRevisionCharacteristic = gattCharacteristic;
                            } else if(gattCharacteristic.getUuid().toString().equals(READ_HARDWARE_REVISION_UUID)) {
                                hardwareRevisionCharacteristic = gattCharacteristic;
                            }
                        }
                    }
                }

                service = mBluetoothGatt.getService(UUID.fromString(UUID_SERVICE));
                if (service != null) {

                    List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();

                    if (gattCharacteristics != null && gattCharacteristics.size() > 0) {
                        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                            LogUtil.d(gattCharacteristic.getUuid().toString(), DBG);
                            if (gattCharacteristic.getUuid().toString().equals(UUID_WRITE)) {
                                mNotifyCharacteristic = gattCharacteristic;
                                LogUtil.d("mNotifyCharacteristic:" + mNotifyCharacteristic, DBG);
                            } else if (gattCharacteristic.getUuid().toString().equals(UUID_READ)) {
                                gatt.setCharacteristicNotification(gattCharacteristic, true);
                                BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID_HEART_RATE_MEASUREMENT);
                                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                if (gatt.writeDescriptor(descriptor)) {
                                    LogUtil.d("writeDescriptor successed", DBG);
                                } else {
                                    LogUtil.d("writeDescriptor failed", DBG);
                                }
                            }

                        }
                    }

                } else {
                    //测试出现的情况 是否再次发现一次
                    sdkLogItem = "service is null";
                    addSdkLog();

                    //TODO:当连接失败处理
                    mConnectionState = STATE_DISCONNECTED;
                    LogUtil.d("mBluetoothGatt.getServices().size():" + mBluetoothGatt.getServices().size(), DBG);
                    if (mBluetoothGatt.getServices().size() > 0)
                        mExtendedBluetoothDevice.setNoLockService(true);
                    close();
                    //再次扫描
//                    startScan();

                    mExtendedBluetoothDevice.disconnectStatus = ExtendedBluetoothDevice.SERVICE_DISCONNECTED;
                    doConnectFailedCallback();
                }
            } else {
                LogUtil.w("onServicesDiscovered received: " + status, DBG);
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if(mBluetoothGatt != gatt)
                return;
            LogUtil.d(Thread.currentThread().toString().toString(), DBG);
            super.onDescriptorWrite(gatt, descriptor, status);
            LogUtil.d("gatt=" + gatt + " descriptor=" + descriptor + " status=" + status, true);
            LogUtil.d(descriptor.getCharacteristic().getUuid().toString(), DBG);

            isNeedReCon = false;
            mConnectionState = STATE_CONNECTED;

            ConnectCallback mCallback = LockCallbackManager.getInstance().getConnectCallback();
            if(mCallback != null){
                mCallback.onConnectSuccess(mExtendedBluetoothDevice);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if(mBluetoothGatt != gatt) {
                LogUtil.w("gatt=" + gatt + " characteristic=" + characteristic + " status=" + status, DBG);
                return;
            }
            //和读数据在同一个线程里面
            LogUtil.d("gatt=" + gatt + " characteristic=" + characteristic + " status=" + status, DBG);
            if(status == BluetoothGatt.GATT_SUCCESS) {
                if (dataQueue.size() > 0) {
                    characteristic.setValue(dataQueue.poll());
                    //TODO:写成功再写下一个
                    gatt.writeCharacteristic(characteristic);
                } else {

                    mHandler.removeCallbacks(disConRunable);
                    //TODO:考虑下面的是否可以去掉
                    if (currentAPICommand != APICommand.OP_REMOTE_CONTROL_DEVICE_MANAGEMENT
                            && currentAPICommand != APICommand.OP_SCAN_WIFI
                            && currentAPICommand != APICommand.OP_SET_WIFI
                            && currentAPICommand != APICommand.OP_SET_STATIC_IP) {//遥控设备是没有响应指令 WIFI扫描需要等待的时间比较长
                        disTimerTask = new TimerTask() {
                            @Override
                            public void run() {
                                //TODO:测试 暂时注释掉
                                disconnect();
                            }
                        };
                        long delay = 2500;
                        if (currentAPICommand == APICommand.OP_RESET_LOCK || currentAPICommand == OP_WRITE_FR || isWaitCommand){
                            delay = 5500;
                        }
                        if (timer != null){
                            timer.schedule(disTimerTask, delay);
                        }
                    }
                }
            } else {
                LogUtil.w("onCharacteristicWrite failed", DBG);
            }

            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        @RequiresPermission(Manifest.permission.BLUETOOTH)
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if(mBluetoothGatt != gatt)
                return;
            super.onCharacteristicChanged(gatt, characteristic);
//            LogUtil.d(Thread.currentThread().toString().toString(), DBG);
            try {
                LogUtil.d("gatt=" + gatt + " characteristic=" + characteristic, DBG);
                byte[] data = characteristic.getValue();
                int dataLen = data.length;
                if(scanBongOnly) {
                    if("success".equals(new String(data))){
//                        error = Error.SUCCESS;
                        lockError = LockError.SUCCESS;
                    } else {
                        lockError = LockError.LOCK_OPERATE_FAILED;
                    }
                    switch (tempOptype) {
                        case 0x01:
//                            getTTLockCallback().onSetWristbandKeyToDev(error);
                            break;
                        case 0x02:
//                            getTTLockCallback().onSetWristbandKeyRssi(error);
                            break;
                        default:
                            break;
                    }
                    return;
                }
                LogUtil.d("receiver data=" + DigitUtil.byteArrayToHexString(data), DBG);
                if(mReceivedBufferCount + dataLen <= maxBufferCount) {
                    System.arraycopy(data, 0, mReceivedDataBuffer, mReceivedBufferCount, dataLen);
                    mReceivedBufferCount += dataLen;
                }
                LogUtil.d("mReceivedBufferCount:" + mReceivedBufferCount, DBG);
                LogUtil.d("dataLen:" + dataLen, DBG);
                /**
                 * 数据开始
                 */
                if(mReceivedBufferCount == dataLen && data[0] == 0x7f && data[1] == 0x5a) {
                    int valueLen = 0;
                    if(data[2] >= 5) {
                        //数据长度+1位校验位
                        valueLen = data[11] + 1;
                        leftRecDataCount = valueLen - (dataLen - 12);
                    } else {//老协议
                        valueLen = data[5] + 1;
                        leftRecDataCount = valueLen - (dataLen - 6);
                    }
                    LogUtil.d("all:" + leftRecDataCount, DBG);
                } else {
                    leftRecDataCount -= dataLen;
                }
                LogUtil.d("leftRecDataCount:" + leftRecDataCount, DBG);
                byte last = mReceivedDataBuffer[mReceivedBufferCount - 1];
                byte lastSec = mReceivedDataBuffer[mReceivedBufferCount - 2];
                /**
                 * 接收完成
                 */
                if(lastSec == 13 && last == 10 && leftRecDataCount <= 0) {
                    LogUtil.d("receive finish", DBG);
                    mReceivedBufferCount -= 2;
                    LogUtil.d("mReceivedDataBuffer=" + DigitUtil.byteArrayToHexString(Arrays.copyOf(mReceivedDataBuffer, mReceivedBufferCount)), DBG);
                    processCommandResponse(Arrays.copyOf(mReceivedDataBuffer, mReceivedBufferCount));
                    //清零
                    mReceivedBufferCount = 0;
                    if (disTimerTask != null){
                        disTimerTask.cancel();
                    }
                    if (timer != null){
                        LogUtil.d("num:" + timer.purge(), DBG);
                    }
                    /**
                     * 接收完成
                     */
                } else if(leftRecDataCount == 0) {
                    LogUtil.d("receive finish", DBG);
                    LogUtil.d("mReceivedDataBuffer=" + DigitUtil.byteArrayToHexString(Arrays.copyOf(mReceivedDataBuffer, mReceivedBufferCount)), DBG);
                    processCommandResponse(Arrays.copyOf(mReceivedDataBuffer, mReceivedBufferCount));
                    //清零
                    mReceivedBufferCount = 0;
                    if (disTimerTask != null){
                        disTimerTask.cancel();
                    }
                    if (timer != null){
                        LogUtil.d("num:" + timer.purge(), DBG);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                //清零
                mReceivedBufferCount = 0;
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            LogUtil.d(Thread.currentThread() + " " + new String(characteristic.getValue()), DBG);
            LogUtil.d(Thread.currentThread() + " " + characteristic.getUuid(), DBG);
        }
    };

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    private void initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        mHandler = new Handler();

        mReceivedDataBuffer = new byte[maxBufferCount];

        scanCallback = new ScanCallback();

        //TODO:
        mScanner = ScannerCompat.getScanner();

        //TODO:异常判断  增加关闭和启动定时器
        timer = new Timer();

        mAppExecutor = new AppExecutors();

        LogUtil.d("------------------timer----------------" + timer, DBG);

    }

    public void setOnScanFailedListener(OnScanFailedListener onScanFailedListener) {
        this.onScanFailedListener = onScanFailedListener;
    }

    public boolean isScanAll() {
        return scanAll;
    }

    public void setScanAll(boolean scanAll) {
        this.scanAll = scanAll;
    }

    public void prepareBTService(Context context){
        if(context != null && !isBtStateReceiverRegistered){
            mApplicationContext = context;
            String appReceiverPermison = context.getPackageName() + ".permission.BLE_RECEIVER";
            mApplicationContext.registerReceiver(bluttoothState, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED), appReceiverPermison, null);
            isBtStateReceiverRegistered = true;
            LogUtil.d("startScan callde by user", DBG);
            if (mBluetoothManager == null) {
                mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            }
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }
        mHandler = new Handler();
        LogUtil.d("bluetooth is prepared", DBG);
    }

    /**
     * 启动扫描
     * 权限 开启判断
     * TODO:判断蓝牙是否开启
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public void startScan() {
        scan = true;
        if(!scan) {
            LogUtil.w("Already stop scan", DBG);
            return;
        }
        LogUtil.d("start scan", DBG);
        if(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            LogUtil.w("BluetoothAdapter is disabled", true);
            LockCallback mScanCallback = LockCallbackManager.getInstance().getLockScanCallback();
            if(mScanCallback != null){
                mScanCallback.onFail(LockError.BLE_SERVER_NOT_INIT);
            }
            return;
        }
        if(mScanner == null){
            mScanner = ScannerCompat.getScanner();
        }

        if(scanCallback == null){
            scanCallback = new ScanCallback();
        }


//        stopScan();
        mScanner.setScanAll(scanAll);
        mScanner.setOnScanFailedListener(onScanFailedListener);
        LogUtil.d("start ---");
        mScanner.startScan(scanCallback);
        mScanning = true;

        if(mConnectionState != STATE_DISCONNECTED) {//考虑没有断开的情况
            LogUtil.w("Ble not disconnected", DBG);
        }
    }

    /**
     * 停止扫描
     */
    public void stopScan() {
        LogUtil.d("mScanner:" + mScanner, DBG);
        LogUtil.d("mScanning:" + mScanning, DBG);
        scan = false;

        if(mScanner != null && mScanning) {
            mScanning = false;
            try {
                mScanner.stopScan();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public boolean connect(final String address) {

        try {
            conLock.lock();
//            stopScan();
            connectCnt++;
            connectTime = System.currentTimeMillis();

            if (mBluetoothAdapter == null || address == null) {
                Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
                LogUtil.w("mBluetoothAdapter:" + mBluetoothAdapter, DBG);
                LogUtil.w("address:" + address, DBG);
                ConnectCallback mConnectCallback = LockCallbackManager.getInstance().getConnectCallback();
                if(mConnectCallback != null){
                    mConnectCallback.onFail(LockError.BLE_SERVER_NOT_INIT);
                }
                return false;
            }

            if(!mBluetoothAdapter.isEnabled()){
                ConnectCallback mConnectCallback = LockCallbackManager.getInstance().getConnectCallback();
                if(mConnectCallback != null){
                    mConnectCallback.onFail(LockError.BLE_SERVER_NOT_INIT);
                }
                return false;
            }

            if (mBluetoothGatt != null) {
                LogUtil.d("mBluetoothGatt not null", DBG);
                disconnect();
                close();
            }

            final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
            // We want to directly connect to the device, so we are setting the autoConnect
            // parameter to false.
            LogUtil.d("connect ……", DBG);
            mHandler.removeCallbacks(disConRunable);
            mHandler.postDelayed(disConRunable, CONNECT_TIME_OUT);
            mBluetoothGatt = device.connectGatt(mApplicationContext, false, mGattCallback);
            mConnectionState = STATE_CONNECTING;
            LogUtil.d("mBluetoothGatt:" + mBluetoothGatt, DBG);
            Log.i(TAG, "Trying to create a new connection.");
            LogUtil.i("connected mBluetoothGatt:" + mBluetoothGatt, DBG);
            mBluetoothDeviceAddress = address;
            mBluetoothDevice = device;
            mExtendedBluetoothDevice = new ExtendedBluetoothDevice(device);
            return true;
        } finally {
            conLock.unlock();
        }


    }

    //TODO:连接之后很快断开以及连接之后没反应 判断一下重连未达最大次数的情况
    //增加连接超时5s
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public boolean connect(final ExtendedBluetoothDevice extendedBluetoothDevice) {
        //停止扫描
        //TODO:4.4停不掉

        connectCnt++;
        connectTime = System.currentTimeMillis();
        LogUtil.i("extendedBluetoothDevice:" + extendedBluetoothDevice, DBG);
        String address = extendedBluetoothDevice.getDevice().getAddress();
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            LogUtil.w("mBluetoothAdapter:" + mBluetoothAdapter, DBG);
            LogUtil.w("address:" + address, DBG);
            return false;
        }

        if (mBluetoothGatt != null) {
            LogUtil.d("mBluetoothGatt not null", DBG);
            disconnect();
            close();
        }
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);


        LogUtil.d("connect ……", DBG);
        mHandler.removeCallbacks(disConRunable);
        //TODO:暂时取消
        mHandler.postDelayed(disConRunable, CONNECT_TIME_OUT);

        //恢复默认状态
        extendedBluetoothDevice.disconnectStatus = ExtendedBluetoothDevice.DISCONNECT_STATUS_NONE;

        mBluetoothGatt = device.connectGatt(mApplicationContext, false, mGattCallback);
        mConnectionState = STATE_CONNECTING;
        Log.i(TAG, "Trying to create a new connection.");
        LogUtil.i("connected mBluetoothGatt:" + mBluetoothGatt, DBG);
        mBluetoothDeviceAddress = address;
        mBluetoothDevice = device;
        mExtendedBluetoothDevice = extendedBluetoothDevice;
        return true;
//        } finally {
//            conLock.unlock();
//        }
    }

    /**
     * 今后都用这个方法
     * @param transferData
     */
    public void sendCommand(TransferData transferData) {
        this.transferData = transferData;
        mUid = transferData.getmUid();
        adminPs = transferData.getAdminPs();
        unlockKey = transferData.getUnlockKey();
        this.startDate = transferData.getStartDate();
        this.endDate = transferData.getEndDate();
        aesKeyArray = transferData.getAesKeyArray();
        unlockDate = transferData.getUnlockDate();
        lockFlagPos = transferData.getLockFlagPos();
        originalPwd = transferData.getOriginalPwd();
        newPwd = transferData.getNewPwd();
        attachmentNum = transferData.getNo();
        pwds = transferData.getPwds();
        wristbandKey = transferData.getWristbandKey();
        calibationTime = transferData.getCalibationTime();
        timezoneOffSet = transferData.getTimezoneOffSet();
//        feature = transferData.getFeature();
        hotelData = transferData.getHotelData();

//        LogUtil.d("send command:" + (char) transferData.getCommand() + "-" + String.format("%#x", transferData.getCommand()), DBG);;
        sendCommand(transferData.getTransferData(), transferData.getAPICommand());
    }

    public void sendCommand(byte[] commandSrc, int uid, String unlockKey, byte[] aesKey, long date, int apiCommand) {
        mUid = uid;
        this.unlockKey = unlockKey;
        aesKeyArray = aesKey;
        currentAPICommand = apiCommand;
        calibationTime = date;
        sendCommand(commandSrc, apiCommand);
    }

    public void sendCommand(byte[] commandSrc, String adminPs, String unlockKey, byte[] aesKey, int apiCommand) {
        this.adminPs = adminPs;
        this.unlockKey = unlockKey;
        aesKeyArray = aesKey;
        currentAPICommand = apiCommand;
        sendCommand(commandSrc, apiCommand);
    }

    public void sendCommand(byte[] commandSrc, String adminPs, String unlockKey, int lockFlagPos, byte[] aesKey, byte validPwdNum, byte keyboardPwdType, String originalPwd, String string, long startDate, long endDate, int apiCommand) {
        this.originalPwd = originalPwd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.keyboardPwdType = keyboardPwdType;
        sendCommand(commandSrc, adminPs, unlockKey, lockFlagPos, aesKey, validPwdNum, string, apiCommand);
    }

    public void sendCommand(byte[] commandSrc, String adminPs, String unlockKey, int lockFlagPos, byte[] aesKey, byte validPwdNum, String string, int apiCommand) {
        this.adminPs = adminPs;
        this.unlockKey = unlockKey;
        this.validPwdNum = validPwdNum;
        this.lockFlagPos = lockFlagPos;
        aesKeyArray = aesKey;
        currentAPICommand = apiCommand;
        switch (apiCommand) {
            case APICommand.OP_SET_DELETE_PASSWORD:
                deletePwd = string;
                break;
            case APICommand.OP_SET_LOCK_NAME:
                lockname = string;
                break;
            case APICommand.OP_SET_KEYBOARD_PASSWORD:
            case APICommand.OP_ADD_ONCE_KEYBOARD_PASSWORD:
            case APICommand.OP_ADD_PERIOD_KEYBOARD_PASSWORD:
            case APICommand.OP_ADD_PERMANENT_KEYBOARD_PASSWORD:
            case APICommand.OP_REMOVE_ONE_PASSWORD:
            case APICommand.OP_MODIFY_KEYBOARD_PASSWORD:
                newPwd = string;
                break;
                default:
                    break;
        }

        sendCommand(commandSrc, apiCommand);
    }

    public void sendCommand(byte[] commandSrc, int apiCommand, byte[] aesKeyArray) {
        this.aesKeyArray = aesKeyArray;
        sendCommand(commandSrc, apiCommand);
    }

    public void sendCommand(byte[] commandSrc, int apiCommand) {
        currentAPICommand = apiCommand;
        lockData = ConnectManager.getInstance().getConnectParamForCallback().getLockData();
        if(currentAPICommand == APICommand.OP_GET_OPERATE_LOG) {
            //TODO:根据版本进行判断
            LogUtil.d("init logOperates");
            //初始化操作日志
            if (logOperates == null)
                logOperates = new ArrayList<>();
            //初始化动车位锁记录
            moveDateArray = new JSONArray();
        }
        sendCommand(commandSrc);
    }

    public void sendCommand(byte[] commandSrc, String pwdInfo, long timestamp, int apiCommand) {
        currentAPICommand = apiCommand;
        this.pwdInfo = pwdInfo;
        this.timestamp = timestamp;
        sendCommand(commandSrc);
    }

    public void sendCommand(byte[] commandSrc) {
        ConnectManager.getInstance().removeDataCheckTimer();
//        LogUtil.d("currentAPICommand" + currentAPICommand, DBG);
        int length = commandSrc.length;
        // 每条指令添加结束缚 13，10
        byte[] command = new byte[commandSrc.length + 2];
        System.arraycopy(commandSrc, 0, command, 0, length);
        command[length] = 13;
        command[length + 1] = 10;

        sdkLogBuilder = new StringBuilder();
        sdkLogItem = "send data:" + DigitUtil.byteArrayToHexString(commandSrc);
        addSdkLog();

        int len = length + 2;
        if (dataQueue == null)
            dataQueue = new LinkedList<byte[]>();
        dataQueue.clear();
        int startPos = 0;
        while (len > 0) {
            int ln = Math.min(len, 20);
            byte[] data = new byte[ln];
            System.arraycopy(command, startPos, data, 0, ln);
            dataQueue.add(data);
            len -= 20;
            startPos += 20;
        }

        cloneDataQueue = (LinkedList<byte[]>) dataQueue.clone();

        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            try {
                commandSendCount = 0;
                mNotifyCharacteristic.setValue(dataQueue.poll());
                mBluetoothGatt.writeCharacteristic(mNotifyCharacteristic);
            } catch (Exception e) {
                mConnectionState = STATE_DISCONNECTED;
                sdkLogItem = e.getMessage();
                addSdkLog();
                doConnectFailedCallback();
            }
        } else {
            LogUtil.d("mNotifyCharacteristic:" + mNotifyCharacteristic, DBG);
            LogUtil.d("mBluetoothGatt:" + mBluetoothGatt, DBG);
            sdkLogItem = "mNotifyCharacteristic or mBluetoothGatt is null";
            addSdkLog();

            mConnectionState = STATE_DISCONNECTED;
            doConnectFailedCallback();
        }
    }

    /**
     * 发送手环指令
     */
    public void sendBongCommand(String wristbandKey) {
        byte[] values = new byte[12];
        byte[] constant = {0x31, 0, 0, 0};
        byte cmd = 0x01;
        byte enable = 0x01;
        System.arraycopy(constant, 0, values, 0, 4);
        values[4] = cmd;
        values[5] = enable;
        System.arraycopy(wristbandKey.getBytes(), 0, values, 6, 6);
        tempOptype = cmd;
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            mNotifyCharacteristic.setValue(values);
            mBluetoothGatt.writeCharacteristic(mNotifyCharacteristic);
        } else {
            LogUtil.d("mNotifyCharacteristic or mBluetoothGatt is null", DBG);
        }
    }

    /**
     * 设置手环开门信号值
     * @param rssi  必须是正数(先将负数转成正数)
     */
    public void setBongRssi(byte rssi) {
        byte[] values = new byte[6];
        byte[] constant = {0x31, 0, 0, 0};
        byte cmd = 0x02;
        System.arraycopy(constant, 0, values, 0, 4);
        values[4] = cmd;
        values[5] = rssi;
        tempOptype = cmd;
        if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
            mNotifyCharacteristic.setValue(values);
            mBluetoothGatt.writeCharacteristic(mNotifyCharacteristic);
        } else {
            LogUtil.d("mNotifyCharacteristic or mBluetoothGatt is null", DBG);
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    private void processCommandResponse(final byte[] values) {
        mAppExecutor.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                mExtendedBluetoothDevice.disconnectStatus = ExtendedBluetoothDevice.NORMAL_DISCONNECTED;
                final Command command = new Command(values);
                if(!command.isChecksumValid()) {
                    lockError = LockError.LOCK_CRC_CHECK_ERROR;
                    lockError.setCommand(command.getCommand());
                    errorCallback(lockError);
                    return ;
                }
                if(currentAPICommand == APICommand.OP_GET_LOCK_VERSION) {
                    LockCallback mVersionCallback = LockCallbackManager.getInstance().getCallback();
                    if(mVersionCallback == null){
                        return;
                    }
                    byte protocolType = 0;
                    byte protocolVersion = 0;
                    byte scene = 0;
                    short groupId = 0;
                    short orgId = 0;
                    if(values[0] == 0x7f && values[1] == 0x5a) {
                        lockError = LockError.SUCCESS;
                        protocolType = values[2];
                        protocolVersion = values[3];
                        scene = values[4];
                        groupId = DigitUtil.byteArrayToShort(new byte[]{values[5], values[6]});
                        orgId = DigitUtil.byteArrayToShort(new byte[]{values[7], values[8]});
                    } else {
                        lockError = LockError.LOCK_OPERATE_FAILED;
                    }
                    LockVersion mLockVersion = new LockVersion(protocolType,protocolVersion,scene,groupId,orgId);
                    ((GetLockVersionCallback)mVersionCallback).onGetLockVersionSuccess(GsonUtil.toJson(mLockVersion));
                    return;
                }

                //更新版本信息
                byte groupId = command.organization[0];
                byte orgId = command.sub_organization[0];
                mExtendedBluetoothDevice.groupId = groupId;
                mExtendedBluetoothDevice.orgId = orgId;

                byte[] data = null;

                sdkLogItem = "currentAPICommand : " + currentAPICommand;
                addSdkLog();

                sdkLogItem = "command:" + (char) command.getCommand() + "-" + String.format("%#x",command.getCommand());
                addSdkLog();

                switch (command.getLockType()) {
                    case LockType.LOCK_TYPE_V2:
                    case LockType.LOCK_TYPE_V2S:
                    case LockType.LOCK_TYPE_MOBI:
                    case LockType.LOCK_TYPE_CAR:
                        data = command.getData();
                        break;
                    case LockType.LOCK_TYPE_V3:
                    case LockType.LOCK_TYPE_V3_CAR:
                        mHandler.removeCallbacks(disConRunable);
                        data = command.getData(aesKeyArray);
                        break;
                    case LockType.LOCK_TYPE_V2S_PLUS:
                        data = command.getData(aesKeyArray);
                        break;
                    default:
                        break;
                }

                if(data == null || data.length == 0) {
                    //TODO:添加的时候走了E指令 可能走到这里了
                    switch (currentAPICommand) {
                        case APICommand.OP_ADD_ADMIN:
                            lockError = LockError.LOCK_IS_IN_NO_SETTING_MODE;
                            lockError.setCommand(command.getCommand());
                            errorCallback(lockError);
                            break;
                        case APICommand.OP_RESET_LOCK:
                            if (command.length == 0) {//重置 没有包体 表示重置成功
                                LogUtil.d("reset - success");
                                LockCallback mResetLockCallback = LockCallbackManager.getInstance().getCallback();
                                if(mResetLockCallback != null){
                                    ((ResetLockCallback)mResetLockCallback).onResetLockSuccess();
                                }
                            }
                            break;
                        default:
                            lockError = LockError.KEY_INVALID;
                            lockError.setCommand(command.getCommand());
                            errorCallback(lockError);
                            break;
                    }
                    return ;
                }

                sdkLogItem = "values:" + DigitUtil.byteArrayToHexString(values);
                addSdkLog();

                sdkLogItem = "feedback comman:" + (char) data[0] + "-" + String.format("%#x",data[0]);
                addSdkLog();

                sdkLogItem = "response data:" + DigitUtil.byteArrayToHexString(data);
                addSdkLog();

                if(command.getCommand() == Command.COMM_GET_AES_KEY) {
                    if(data[1] == 1) {
                        aesKeyArray = Arrays.copyOfRange(data, 2, data.length);
                        adminPs = new String(DigitUtil.generateDynamicPassword(10));
                        unlockKey = new String(DigitUtil.generateDynamicPassword(10));
                        CommandUtil.V_addAdmin(command.getLockType(), adminPs, unlockKey, aesKeyArray);
                    } else {//失败
                        lockError = LockError.getInstance(data[2]);//错误码
                        lockError.setCommand(command.getCommand());
                        errorCallback(lockError);
                    }
                } else if(command.getCommand() == Command.COMM_RESPONSE) {
                    if(data[1] == CommandResponse.SUCCESS) {
                        LogUtil.d("success", DBG);
                        //成功
                        lockError = LockError.SUCCESS;
                        lockError.setLockname(mExtendedBluetoothDevice.getName());
                        lockError.setLockmac(mExtendedBluetoothDevice.getAddress());
                        lockError.setCommand(data[0]);
                        lockError.setDate(System.currentTimeMillis());
                        switch (data[0]) {//指令
                            case (byte) 0xFF:
                                if(currentAPICommand == APICommand.OP_SET_LOCK_NAME)
                                    isSetLockName = true;
                                break;
                            case Command.COMM_ADD_ADMIN: {//V
                                //TODO:是否判断车位锁的
                                if (command.getLockType() == LockType.LOCK_TYPE_V3) {
                                    if (!Constant.SCIENER.equals(new String(data, 2, 7))) {
                                        lockError = LockError.AES_PARSE_ERROR;
                                        lockError.setCommand(command.getCommand());
                                        errorCallback(lockError);
                                        return;
                                    }
                                }
                                //先校准时间
                                CommandUtil.C_calibationTime(command.getLockType(), System.currentTimeMillis(), TimeZone.getDefault().getOffset(System.currentTimeMillis()), aesKeyArray);
                                break;
                            }
                            case Command.COMM_SEARCH_BICYCLE_STATUS: {//自行车锁状态
                                isWaitCommand = true;
                                mExtendedBluetoothDevice.setParkStatus(data[3]);
                                if (currentAPICommand == APICommand.OP_DETECT_DOOR_SENSOR){
//                                    TTLockAPI.getTTLockCallback().onGetDoorSensorState(mExtendedBluetoothDevice, data[2], data[4], error);
                                }else {
                                    LockCallback mStatusCallback = LockCallbackManager.getInstance().getCallbackWithoutRemove();
                                    if(mStatusCallback != null){
                                         if(mStatusCallback instanceof  GetLockStatusCallback) {
                                             LockCallbackManager.getInstance().clearAllCallback();
                                             ((GetLockStatusCallback) mStatusCallback).onGetLockStatusSuccess(data[3]);
                                         }
                                    }
                                }
                                break;
                            }
                            case Command.COMM_FUNCTION_LOCK: {

                                byte battery = -1;
                                int len = data.length;
                                if(len > 2) {
                                    battery = data[2];
                                }

                                int uid = (int) DigitUtil.fourBytesToLong(Arrays.copyOfRange(data, 3, 7));
                                int uniqueid = (int) DigitUtil.fourBytesToLong(Arrays.copyOfRange(data, 7, 11));
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(2000 + data[11], data[12] - 1, data[13], data[14], data[15], data[16]);
                                //根据时间偏移量计算时间
                                TimeZone timeZone = TimeZone.getDefault();
                                LogUtil.d("timezoneOffSet:" + timezoneOffSet, DBG);
                                if (timeZone.inDaylightTime(new Date(System.currentTimeMillis()))){
                                    timezoneOffSet -= timeZone.getDSTSavings();
                                }
                                timeZone.setRawOffset((int) timezoneOffSet);
                                calendar.setTimeZone(timeZone);
                                long lockTime = calendar.getTimeInMillis();
                                mExtendedBluetoothDevice.setBatteryCapacity(data[2]);
                                LockCallback mControlLockCallback = LockCallbackManager.getInstance().getCallback();
                                if(mControlLockCallback != null){
                                    ((ControlLockCallback)mControlLockCallback).onControlLockSuccess(new ControlLockResult(ControlAction.LOCK, battery, uniqueid, lockTime));
                                }

                                break;
                            }
                            case Command.COMM_SHOW_PASSWORD://todo:从锁读取
                                int status = transferData.getOp();
                                boolean isQuery = data[3] == ActionType.GET;
                                if(isQuery) {
                                    status = data[4];
                                    lockData.displayPasscode = status;
                                    doResponse(data[0], command);
                                } else {
                                    status -= 2;
                                    LockCallback lockCallback = LockCallbackManager.getInstance().getCallback();
                                    if(lockCallback instanceof SetLockConfigCallback) {
                                        ((SetLockConfigCallback)lockCallback).onSetLockConfigSuccess(TTLockConfigType.PASSCODE_VISIBLE);
                                    } else if (lockCallback instanceof SetPasscodeVisibleCallback) {//回调返回参数可以不要
                                        ((SetPasscodeVisibleCallback)lockCallback).onSetPasscodeVisibleSuccess(status == 1);
                                    }
                                }
                                break;
                            /**
                             * command A
                             */
                            case Command.COMM_CHECK_ADMIN: {
                                int len = data.length - 2;
                                psFromLock = new byte[len];
                                System.arraycopy(data, 2, psFromLock, 0, len);
                                switch (currentAPICommand) {
                                    case APICommand.OP_ACTIVATE_FLOORS:
                                        CommandUtil_V3.activateLiftFloors(command, psFromLock, transferData);
                                        break;
                                    case APICommand.OP_UNLOCK_ADMIN:
                                    case APICommand.OP_UNLOCK_EKEY:
                                        CommandUtil.G_unlock(command.getLockType(), unlockKey, psFromLock, aesKeyArray, unlockDate, timezoneOffSet);
                                        break;
                                    case APICommand.OP_LOCK_ADMIN:
                                    case APICommand.OP_LOCK_EKEY:
                                        if(command.getLockType() == LockType.LOCK_TYPE_V3_CAR){
                                            CommandUtil.lock(command.getLockType(), unlockKey, psFromLock, aesKeyArray, unlockDate);
                                        } else{
                                            CommandUtil.L_lock(command.getLockType(), unlockKey, psFromLock, aesKeyArray);
                                        }

                                        break;
                                    //设置管理员键盘密码
                                    case APICommand.OP_SET_KEYBOARD_PASSWORD:
                                        //三代锁要校验随机数
                                        if(command.getLockType() == LockType.LOCK_TYPE_V3){
                                            CommandUtil.checkRandom(command.getLockType(), unlockKey, psFromLock, aesKeyArray);
                                        } else{
                                            CommandUtil.S_setAdminKeyboardPwd(command.getLockType(), newPwd, aesKeyArray);
                                        }
                                        break;
                                    case APICommand.OP_SET_DELETE_PASSWORD:
                                        if(command.getLockType() == LockType.LOCK_TYPE_V3)//三代锁要校验随机数
                                            CommandUtil.checkRandom(command.getLockType(), unlockKey, psFromLock, aesKeyArray);
                                        else
                                            CommandUtil.D_setDeletePassword(command.getLockType(), deletePwd, aesKeyArray);
                                        break;
                                    case APICommand.OP_RESET_EKEY:
                                        LockCallback mResetKeyCallback = LockCallbackManager.getInstance().getCallback();
                                        if(mResetKeyCallback != null){
                                            lockData = ConnectManager.getInstance().getConnectParamForCallback().getLockData();
                                            if(lockData != null) {
                                                lockData.lockFlagPos = lockFlagPos;
                                                ((ResetKeyCallback) mResetKeyCallback).onResetKeySuccess(lockData.encodeLockData());
                                            }
                                        }
                                        break;
                                    case APICommand.OP_SET_LOCK_NAME:
                                        CommandUtil.checkRandom(command.getLockType(), unlockKey, psFromLock, aesKeyArray);
                                        break;
                                    case APICommand.OP_RESET_LOCK:
                                        if(command.getLockType() == LockType.LOCK_TYPE_V3 || command.getLockType() == LockType.LOCK_TYPE_V3_CAR){
                                            CommandUtil.checkRandom(command.getLockType(), unlockKey, psFromLock, aesKeyArray);
                                        } else {
                                            CommandUtil.R_resetLock(command.getLockType());
                                        }
                                        break;
                                    case APICommand.OP_INIT_PWD:
                                        switch (command.getLockType()) {
                                            case LockType.LOCK_TYPE_V3://三代锁需要校验随机数
                                                CommandUtil.checkRandom(command.getLockType(), unlockKey, psFromLock, aesKeyArray);
                                                break;
                                            case LockType.LOCK_TYPE_V2S_PLUS:
                                                pwdData = new byte[1624];
                                                generateTransmissionData(command.getScene(), pwdData, validPwdNum);
                                                dataPos = 0;
                                                CommandUtil_V2S_PLUS.synPwd(command.getLockType(), Arrays.copyOfRange(pwdData, dataPos, dataPos + packetLen), dataPos, aesKeyArray);
                                                break;
                                            case LockType.LOCK_TYPE_V2S:
                                                pwdList = new LinkedList<String>();
                                                String pwds = generatePwd(KeyboardPwd.ONE_DAY_PWD);
                                                CommandUtil_V2S.synPwd(command.getLockType(), pwds, 0);
                                                break;
                                            default:
                                                break;
                                        }
                                        break;
                                    default:
                                        CommandUtil.checkRandom(command.getLockType(), unlockKey, psFromLock, aesKeyArray);
                                        break;
                                }
                                break;
                            }//A指令结束
                            case Command.COMM_SET_ADMIN_KEYBOARD_PWD://S 管理员键盘密码
                                if(currentAPICommand == APICommand.OP_ADD_ADMIN) {//补充管理员后续指令
                                    if(command.getLockType() == LockType.LOCK_TYPE_V3) //三代没有删除码
                                        CommandUtil_V3.initPasswords(command.getLockType(), aesKeyArray, currentAPICommand);
                                    else {
                                        deletePwd = DigitUtil.generatePwdByLength(7);
                                        CommandUtil.D_setDeletePassword(command.getLockType(), deletePwd, aesKeyArray);
                                    }
                                } else{
                                    LockCallback mModifyAdminPwd = LockCallbackManager.getInstance().getCallback();
                                    if(mModifyAdminPwd != null){
                                        ((ModifyAdminPasscodeCallback)mModifyAdminPwd).onModifyAdminPasscodeSuccess(newPwd);
                                    }
                                }
                                break;
                            case Command.COMM_SET_DELETE_PWD://D  删除密码
                                LogUtil.d("set delete pwd success", DBG);
                                if(currentAPICommand == APICommand.OP_ADD_ADMIN) {//添加管理员接后续指令
                                    switch (command.getLockType()) {
                                        case LockType.LOCK_TYPE_V2S_PLUS:
                                            pwdData = new byte[1624];
                                            generateTransmissionData(command.getScene(), pwdData, validPwdNum);
                                            dataPos = 0;
                                            CommandUtil_V2S_PLUS.synPwd(command.getLockType(), Arrays.copyOfRange(pwdData, dataPos, dataPos + packetLen), dataPos, aesKeyArray);
                                            break;
                                        case LockType.LOCK_TYPE_V2S:
                                            pwdList = new LinkedList<String>();
                                            String pwds = generatePwd(KeyboardPwd.ONE_DAY_PWD);
                                            CommandUtil_V2S.synPwd(command.getLockType(), pwds, 0);
                                            break;
                                    }
                                } else{
//                                    TTLockAPI.getTTLockCallback().onSetDeletePassword(mExtendedBluetoothDevice, deletePwd, error);
                                }
                                break;
                            //同步键盘密码
                            case Command.COMM_SYN_KEYBOARD_PWD:{
                                switch (command.getLockType()) {
                                    case LockType.LOCK_TYPE_V2S_PLUS: {
                                        dataPos += packetLen;
                                        if(dataPos + 1 < pwdData.length) {
                                            CommandUtil_V2S_PLUS.synPwd(command.getLockType(), Arrays.copyOfRange(pwdData, dataPos, dataPos + packetLen), dataPos, aesKeyArray);
                                        } else {
                                            LogUtil.e("LOCK_TYPE_V2S_PLUS", DBG);
                                            //TODO:添加管理员回调
                                            if(currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                                lockData = getLockInfoObj();
                                                lockData.lockVersion = command.getLockVersion();
                                                lockData.specialValue = 1;
                                                LockCallback mInitCallback = LockCallbackManager.getInstance().getCallback();
                                                if(mInitCallback != null){
                                                    ((InitLockCallback)mInitCallback).onInitLockSuccess(lockData.encodeLockData());
                                                }

                                                disconnect();
                                            } else{
                                                LockCallback mResetPasscodeCallback = LockCallbackManager.getInstance().getCallback();
                                                lockData.pwdInfo = pwdInfo;
                                                lockData.timestamp = timestamp;
                                                ((ResetPasscodeCallback)mResetPasscodeCallback).onResetPasscodeSuccess(lockData.encodeLockData());
                                            }
                                        }
                                        break;
                                    }
                                    case LockType.LOCK_TYPE_V2S: {//900个密码
                                        int seq = pwdList.size();
                                        int pwdType = 0;
                                        if (seq < 300)
                                            pwdType = 1;
                                        else if (seq < 450)
                                            pwdType = 2;
                                        else if (seq < 550)
                                            pwdType = 3;
                                        else if (seq < 650)
                                            pwdType = 4;
                                        else if (seq < 700)
                                            pwdType = 5;
                                        else if (seq < 750)
                                            pwdType = 6;
                                        else if (seq < 800)
                                            pwdType = 7;
                                        else if (seq < 900)
                                            pwdType = 8;
                                        else {
                                            try {
                                                String pwdInfoOrigin = DigitUtil.generateKeyboardPwd_Json(pwdList);
                                                timestamp = System.currentTimeMillis();
                                                pwdInfo = CommandUtil.encry(pwdInfoOrigin, timestamp);
                                                LogUtil.d("pwdInfoOrigin:" + pwdInfoOrigin, DBG);
                                                //TODO:开门回调
                                                if(currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                                    LockData lockData = getLockInfoObj();
                                                    lockData.lockVersion = command.getLockVersion();
                                                    lockData.specialValue = 1;

                                                    LockCallback mInitLockCallback = LockCallbackManager.getInstance().getCallback();
                                                    if(mInitLockCallback != null){
                                                        ((InitLockCallback)mInitLockCallback).onInitLockSuccess(lockData.encodeLockData());
                                                    }
                                                } else{
                                                    LockCallback mResetPasscodeCallback = LockCallbackManager.getInstance().getCallback();
                                                    lockData.pwdInfo = pwdInfo;
                                                    lockData.timestamp = timestamp;
                                                    ((ResetPasscodeCallback)mResetPasscodeCallback).onResetPasscodeSuccess(lockData.encodeLockData());
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        }
                                        String pwds = generatePwd(pwdType);
                                        CommandUtil_V2S.synPwd(command.getLockType(), pwds, seq);
                                    }
                                }
                                break;
                            }
                            case Command.COMM_CHECK_RANDOM: {//随机数验证
                                LogUtil.d("api:" + currentAPICommand);
                                isCheckedLockPermission = true;
                                switch (currentAPICommand) {
                                    //设置管理员键盘密码
                                    case APICommand.OP_SET_KEYBOARD_PASSWORD:
                                        CommandUtil.S_setAdminKeyboardPwd(command.getLockType(), newPwd, aesKeyArray);
                                        break;
                                    case APICommand.OP_SET_DELETE_PASSWORD://设置删除密码
                                        CommandUtil.D_setDeletePassword(command.getLockType(), deletePwd, aesKeyArray);
                                        break;
                                    case APICommand.OP_INIT_PWD:
                                        CommandUtil_V3.initPasswords(command.getLockType(), aesKeyArray, APICommand.OP_RESET_KEYBOARD_PASSWORD);
                                        break;
                                    case APICommand.OP_SET_LOCK_NAME:
//                                    CommandUtil.N_setLockname(command.getLockType(), lockname, aesKeyArray);
                                        CommandUtil.AT_setLockname(command.getLockType(), lockname, aesKeyArray);
                                        break;
                                    case APICommand.OP_RESET_LOCK:
                                        CommandUtil.R_resetLock(command.getLockType());
                                        break;
                                    case APICommand.OP_ADD_ONCE_KEYBOARD_PASSWORD://添加单次
                                        CommandUtil.manageKeyboardPassword(command.getLockType(), PwdOperateType.PWD_OPERATE_TYPE_ADD, KeyboardPwdType.PWD_TYPE_COUNT, originalPwd, "", startDate, endDate, aesKeyArray, timezoneOffSet);
                                        break;
                                    case APICommand.OP_ADD_PERIOD_KEYBOARD_PASSWORD://期限
                                        CommandUtil.manageKeyboardPassword(command.getLockType(), PwdOperateType.PWD_OPERATE_TYPE_ADD, KeyboardPwdType.PWD_TYPE_PERIOD, originalPwd, "", startDate, endDate, aesKeyArray, timezoneOffSet);
                                        break;
                                    case APICommand.OP_ADD_PERMANENT_KEYBOARD_PASSWORD://永久
                                        CommandUtil.manageKeyboardPassword(command.getLockType(), PwdOperateType.PWD_OPERATE_TYPE_ADD, KeyboardPwdType.PWD_TYPE_PERMANENT, originalPwd, "", startDate, endDate, aesKeyArray, timezoneOffSet);
                                        break;
                                    case APICommand.OP_MODIFY_KEYBOARD_PASSWORD://修改
                                        CommandUtil.manageKeyboardPassword(command.getLockType(), PwdOperateType.PWD_OPERATE_TYPE_MODIFY, keyboardPwdType, originalPwd, newPwd, startDate, endDate, aesKeyArray, timezoneOffSet);
                                        break;
                                    case APICommand.OP_REMOVE_ONE_PASSWORD://删除单个
                                        CommandUtil.manageKeyboardPassword(command.getLockType(), PwdOperateType.PWD_OPERATE_TYPE_REMOVE_ONE, keyboardPwdType, originalPwd, newPwd, 0, 0, aesKeyArray);
                                        break;
                                    case APICommand.OP_BATCH_DELETE_PASSWORD://批量删除密码
                                        dataPos = 0;
                                        CommandUtil.manageKeyboardPassword(command.getLockType(), PwdOperateType.PWD_OPERATE_TYPE_REMOVE_ONE, keyboardPwdType, pwds.get(dataPos), newPwd, 0, 0, aesKeyArray);
                                        break;
                                    case APICommand.OP_REMOVE_ALL_KEYBOARD_PASSWORD://删除锁内所有键盘密码
                                        CommandUtil.manageKeyboardPassword(command.getLockType(), PwdOperateType.PWD_OPERATE_TYPE_CLEAR, (byte) 0, "", "", 0, 0, aesKeyArray);
                                        break;
                                    case APICommand.OP_CALIBRATE_TIME:
                                        CommandUtil.C_calibationTime(command.getLockType(), calibationTime, timezoneOffSet, aesKeyArray);
                                        break;
                                    case APICommand.OP_GET_DEVICE_INFO://获取设备信息增加特征值的获取
                                    case APICommand.OP_SEARCH_DEVICE_FEATURE:
                                        CommandUtil.searchDeviceFeature(command.getLockType());
                                        break;
                                    case APICommand.OP_SEARCH_IC_CARD_NO:
                                        icCards = new ArrayList<ICCard>();
                                        CommandUtil.searchICCardNo(command.getLockType(), (short) 0, aesKeyArray);
                                        break;
                                    case APICommand.OP_SEARCH_FR:
                                        frs = new ArrayList<FR>();
                                        CommandUtil.searchFRNo(command.getLockType(), (short) 0, aesKeyArray);
                                        break;
                                    case APICommand.OP_SEARCH_PWD:
                                        passcodes = new ArrayList<Passcode>();
                                        CommandUtil.searchPasscode(command.getLockType(), (short) 0, aesKeyArray);
                                        break;
                                    case APICommand.OP_ADD_IC:
                                        CommandUtil.addICCard(command.getLockType(), aesKeyArray);
                                        break;
                                    case APICommand.OP_MODIFY_IC_PERIOD:
                                        CommandUtil.modifyICCardPeriod(command.getLockType(), String.valueOf(attachmentNum), startDate, endDate, aesKeyArray, timezoneOffSet);
                                        break;
                                    case APICommand.OP_DELETE_IC:
                                        CommandUtil.deleteICCard(command.getLockType(), String.valueOf(attachmentNum), aesKeyArray);
                                        break;
                                    case APICommand.OP_LOSS_IC:
                                        CommandUtil.deleteICCard(command.getLockType(), String.valueOf(attachmentNum), aesKeyArray);
                                        break;
                                    case APICommand.OP_CLEAR_IC:
                                        CommandUtil.clearICCard(command.getLockType(), aesKeyArray);
                                        break;
                                    case APICommand.OP_SET_WRIST_KEY:
                                        CommandUtil.setWristbandKey(command.getLockType(), wristbandKey, aesKeyArray);
                                        break;
                                    case APICommand.OP_ADD_FR:
                                        CommandUtil.addFR(command.getLockType(), aesKeyArray);
                                        break;
                                    case APICommand.OP_MODIFY_FR_PERIOD:
                                        LogUtil.d("attachmentNum:" + attachmentNum, DBG);
                                        CommandUtil.modifyFRPeriod(command.getLockType(), attachmentNum, startDate, endDate, aesKeyArray, timezoneOffSet);
                                        break;
                                    case APICommand.OP_DELETE_FR:
                                        CommandUtil.deleteFR(command.getLockType(), attachmentNum, aesKeyArray);
                                        break;
                                    case APICommand.OP_CLEAR_FR:
                                        CommandUtil.clearFR(command.getLockType(), aesKeyArray);
                                        break;
                                    case APICommand.OP_SEARCH_AUTO_LOCK_PERIOD:
                                        CommandUtil.searchAutoLockTime(command.getLockType(), aesKeyArray);
                                        break;
                                    case APICommand.OP_DOOR_SENSOR:
                                        CommandUtil_V3.operateDoorSensor(command, (byte)transferData.getOp(), (byte)transferData.getOpValue(), aesKeyArray);
                                        break;
                                    case APICommand.OP_SET_AUTO_LOCK_TIME:
                                        CommandUtil.modifyAutoLockTime(command.getLockType(), (short) calibationTime, aesKeyArray);
                                        break;
                                    case APICommand.OP_ENTER_DFU_MODE:
                                        CommandUtil.enterDFUMode(command.getLockType(), aesKeyArray);
                                        break;
                                    case APICommand.OP_SHOW_PASSWORD_ON_SCREEN:
                                        CommandUtil.screenPasscodeManage(command.getLockType(), transferData.getOp(), aesKeyArray);
                                        break;
                                    case APICommand.OP_READ_PWD_PARA:
                                        CommandUtil.readPwdPara(command.getLockType(), aesKeyArray);
                                        break;
                                    case APICommand.OP_CONTROL_REMOTE_UNLOCK:
                                        CommandUtil_V3.controlRemoteUnlock(command, (byte)transferData.getOp(), (byte)transferData.getOpValue(), aesKeyArray);
                                        break;
                                    case APICommand.OP_AUDIO_MANAGEMENT:
                                        CommandUtil_V3.audioManage(command, (byte)transferData.getOp(), (byte)transferData.getOpValue(), aesKeyArray);
                                        break;
                                    case APICommand.OP_SET_LOCK_SOUND:
                                        CommandUtil_V3.setLockSound(command, transferData);
                                        break;
                                    case APICommand.OP_GET_LOCK_SOUND:
                                        CommandUtil_V3.getLockSound(command, transferData);
                                        break;
                                    case APICommand.OP_REMOTE_CONTROL_DEVICE_MANAGEMENT:
                                        isWaitCommand = true;//不主动断开蓝牙
                                        long psFromLockL = 0;
                                        if (psFromLock != null)
                                            psFromLockL = DigitUtil.fourBytesToLong(psFromLock);
                                        long unlockKeyL = 0;
                                        if (!TextUtils.isEmpty(unlockKey)){
                                            unlockKeyL = Long.valueOf(unlockKey);
                                        }
                                        unlockPwdBytes = DigitUtil.getUnlockPwdBytes_new(psFromLockL, unlockKeyL);

                                        uniqueidBytes = DigitUtil.integerToByteArray((int) (System.currentTimeMillis() / 1000));

                                        CommandUtil_V3.remoteControlManage(command, (byte)transferData.getOp(), (byte)transferData.getOpValue(), unlockPwdBytes, uniqueidBytes, aesKeyArray);
                                        isCanSendCommandAgain = true;
                                        break;
                                    case APICommand.OP_GET_ADMIN_KEYBOARD_PASSWORD:
                                        CommandUtil_V3.getAdminCode(command);
                                        break;
                                    case OP_WRITE_FR:
                                        transferData.setTransferData(DigitUtil.hexString2ByteArray(transferData.getJson()));
                                        //TODO:
                                        CommandUtil_V3.addFRTemp(command, transferData);
                                        break;
                                    case APICommand.OP_RECOVERY_DATA:
                                        recoveryDatas = GsonUtil.toObject(transferData.getJson(), new TypeToken<ArrayList<RecoveryData>>(){});
                                        LogUtil.e("transferData.getJson():" + transferData.getJson(), true);
                                        LogUtil.e("transferData.getOp():" + transferData.getOp(), true);
                                        if(recoveryDatas == null || recoveryDatas.size() == 0) {
                                            LogUtil.d("recoveryDatas is null", DBG);
                                            LockCallback mRecoveryDataCallback = LockCallbackManager.getInstance().getCallback();
                                            if(mRecoveryDataCallback != null){
                                                ((RecoverLockDataCallback)mRecoveryDataCallback).onRecoveryDataSuccess(transferData.getOp());
                                            }
                                            break;
                                        }
                                        dataPos = 0;
                                        recoveryData = recoveryDatas.get(0);
                                        switch (transferData.getOp()) {
                                            case RecoveryDataType.PASSCODE:
                                                CommandUtil.manageKeyboardPassword(command.getLockType(), PwdOperateType.PWD_OPERATE_TYPE_RECOVERY, (byte) (recoveryData.keyboardPwdType == 2 ? 1 :recoveryData.keyboardPwdType), recoveryData.cycleType, recoveryData.keyboardPwd, recoveryData.keyboardPwd, recoveryData.startDate, recoveryData.endDate, aesKeyArray, timezoneOffSet);
                                                break;
                                            case RecoveryDataType.IC_CARD:
                                                CommandUtil.recoveryICCardPeriod(command.getLockType(), recoveryData.cardNumber, recoveryData.startDate, recoveryData.endDate, aesKeyArray, timezoneOffSet);
                                                break;
                                            case RecoveryDataType.FINGERPRINT:
                                                CommandUtil.recoveryFRPeriod(command.getLockType(), Long.valueOf(recoveryData.fingerprintNumber), recoveryData.startDate, recoveryData.endDate, aesKeyArray, timezoneOffSet);
                                                break;
                                                default:
                                                    break;
                                        }
                                        break;
                                    case APICommand.OP_CONFIGURE_NB_SERVER_ADDRESS:
                                        LogUtil.d("config NB");
                                        CommandUtil_V3.configureNBServerAddress(transferData);
                                        break;
                                    case APICommand.OP_QUERY_PASSAGE_MODE:
                                        LogUtil.d("send query passage mode");
                                        passageModeDatas = new ArrayList<PassageModeData>();
                                        CommandUtil_V3.queryPassageMode(command, (byte) 0, aesKeyArray);
                                        break;
                                    case APICommand.OP_ADD_OR_MODIFY_PASSAGE_MODE:
                                        try {
                                            dataPos = 0;
                                            weerOrDays = new JSONArray(transferData.getJson());
                                            if (dataPos < weerOrDays.length())
                                                CommandUtil_V3.configurePassageMode(command, transferData, ((Integer)weerOrDays.get(dataPos)).byteValue());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case APICommand.OP_DELETE_PASSAGE_MODE:
                                        try {
                                            dataPos = 0;
                                            weerOrDays = new JSONArray(transferData.getJson());
                                            if (dataPos < weerOrDays.length())
                                                CommandUtil_V3.deletePassageMode(command, transferData, ((Integer)weerOrDays.get(dataPos)).byteValue());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case APICommand.OP_CLEAR_PASSAGE_MODE:
                                        CommandUtil_V3.clearPassageMode(command, transferData);
                                        break;
                                    case APICommand.OP_FREEZE_LOCK:
                                        CommandUtil_V3.controlFreezeLock(command, (byte)transferData.getOp(), (byte)transferData.getOpValue(), aesKeyArray);
                                        break;
                                    case APICommand.OP_LOCK_LAMP:
                                        CommandUtil_V3.controlLamp(command, (byte)transferData.getOp(), (short) transferData.getOpValue(), aesKeyArray);
                                        break;
                                    case APICommand.OP_SET_HOTEL_DATA:
                                        if (hotelData != null && transferData != null && aesKeyArray != null) {//取电添加成功后 先后设置工作模式跟关连锁时
                                            CommandUtil_V3.configureHotelData(command, HotelData.SET, hotelData.getParaType(), transferData.getHotelData(), aesKeyArray);
                                        } else {
                                            LogUtil.w("hotelData:" + hotelData);
                                            LogUtil.w("transferData:" + transferData);
                                            LogUtil.w("aesKeyArray:" + aesKeyArray);
                                        }
                                        break;
                                    case APICommand.OP_GET_HOTEL_DATA:
                                        CommandUtil_V3.configureHotelData(command, HotelData.GET, hotelData.getParaType(), transferData.getHotelData(), aesKeyArray);
                                        break;
                                    case APICommand.OP_SET_HOTEL_CARD_SECTION:
                                        CommandUtil_V3.configureHotelData(command, HotelData.SET, HotelData.TYPE_SECTOR, transferData.getHotelData(), aesKeyArray);
                                        break;
                                    case APICommand.OP_SET_ELEVATOR_CONTROL_FLOORS:
                                        CommandUtil_V3.configureHotelData(command, HotelData.SET, HotelData.TYPE_ELEVATOR_CONTROLABLE_FLOORS, transferData.getHotelData(), aesKeyArray);
                                        break;
                                    case APICommand.OP_SET_ELEVATOR_WORK_MODE:
                                        CommandUtil_V3.configureHotelData(command, HotelData.SET, HotelData.TYPE_ELEVATOR_WORK_MODE, transferData.getHotelData(), aesKeyArray);
                                        break;
                                    case APICommand.OP_ADD_DOOR_SENSOR:
                                        CommandUtil_V3.addDoorSensor(command, transferData);
                                        break;
                                    case APICommand.OP_DELETE_DOOR_SENSOR:
                                        CommandUtil_V3.deleteDoorSensor(command, transferData);
                                        break;
                                    case APICommand.OP_SET_SWITCH://先获取再设置
                                    case APICommand.OP_GET_SWITCH:
                                    case APICommand.OP_SET_UNLOCK_DIRECTION:
                                    case APICommand.OP_GET_UNLOCK_DIRECTION:
                                        CommandUtil_V3.getSwitchState(command, aesKeyArray);
                                        break;
                                    case APICommand.OP_DEAD_LOCK:
                                        CommandUtil_V3.deadLock(command, unlockKey, psFromLock, aesKeyArray, unlockDate);
                                        break;
                                    case APICommand.OP_GET_NB_ACTIVATE_MODE:
                                        CommandUtil_V3.getNBActivateConfig(command, NBAwakeConfig.ACTION_AWAKE_MODE, aesKeyArray);
                                        break;
                                    case APICommand.OP_GET_NB_ACTIVATE_CONFIG:
                                        CommandUtil_V3.getNBActivateConfig(command, NBAwakeConfig.ACTION_AWAKE_TIME, aesKeyArray);
                                        break;
                                    case APICommand.OP_SET_NB_ACTIVATE_MODE:
                                        CommandUtil_V3.setNBActivateConfig(command, NBAwakeConfig.ACTION_AWAKE_MODE, transferData.getNbAwakeConfig(), aesKeyArray);
                                        break;
                                    case APICommand.OP_SET_NB_ACTIVATE_CONFIG:
                                        CommandUtil_V3.setNBActivateConfig(command, NBAwakeConfig.ACTION_AWAKE_TIME, transferData.getNbAwakeConfig(), aesKeyArray);
                                        break;
                                    case APICommand.OP_ADD_KEY_FOB:
                                        CommandUtil_V3.addKeyFob(command, transferData, aesKeyArray);
                                        break;
                                    case APICommand.OP_MODIFY_KEY_FOB_PERIOD:
                                        CommandUtil_V3.modifyKeyFob(command, transferData, aesKeyArray);
                                        break;
                                    case APICommand.OP_DELETE_KEY_FOB:
                                        CommandUtil_V3.deleteKeyFob(command, transferData, aesKeyArray);
                                        break;
                                    case APICommand.OP_CLEAR_KEY_FOB:
                                        CommandUtil_V3.clearKeyFob(command, aesKeyArray);
                                        break;
                                        default:
                                            break;
                                }
                                break;
                            }
                            case Command.COMM_MANAGE_KEYBOARD_PASSWORD: {//管理键盘密码
                                switch (data[3]) {
                                    case PwdOperateType.PWD_OPERATE_TYPE_ADD://添加
                                        switch (currentAPICommand) {
                                            case APICommand.OP_ADD_ONCE_KEYBOARD_PASSWORD:
                                                keyboardPwdType = KeyboardPwdType.PWD_TYPE_PERIOD;//返回类型换一下,同服务端一致
                                                break;
                                            case APICommand.OP_ADD_PERIOD_KEYBOARD_PASSWORD:
                                                keyboardPwdType = KeyboardPwdType.PWD_TYPE_COUNT;//返回类型换一下,同服务端一致
                                                break;
                                            case APICommand.OP_ADD_PERMANENT_KEYBOARD_PASSWORD:
                                                keyboardPwdType = KeyboardPwdType.PWD_TYPE_PERMANENT;
                                                break;
                                                default:
                                                    break;
                                        }
                                        LockCallback mCreatePasscodeCallback = LockCallbackManager.getInstance().getCallback();
                                        if(mCreatePasscodeCallback != null){
                                            ((CreateCustomPasscodeCallback)mCreatePasscodeCallback).onCreateCustomPasscodeSuccess(originalPwd);
                                        }
                                        break;
                                    case PwdOperateType.PWD_OPERATE_TYPE_REMOVE_ONE://删除单个
                                        if(currentAPICommand == APICommand.OP_BATCH_DELETE_PASSWORD) {//批量删除 TODO:处理失败情况
                                            dataPos++;
                                            if(dataPos < pwds.size()) {
                                                CommandUtil.manageKeyboardPassword(command.getLockType(), PwdOperateType.PWD_OPERATE_TYPE_REMOVE_ONE, keyboardPwdType, pwds.get(dataPos), newPwd, 0, 0, aesKeyArray);
                                            }
//                                        else TTLockAPI.getTTLockCallback().onDeleteKeyboardPasswords(mExtendedBluetoothDevice, pwds, error);
                                        } else{
                                            LockCallback mDeletePwdCallback = LockCallbackManager.getInstance().getCallback();
                                            if(mDeletePwdCallback != null){
                                                ((DeletePasscodeCallback)mDeletePwdCallback).onDeletePasscodeSuccess();
                                            }
                                        }
                                        break;
                                    case PwdOperateType.PWD_OPERATE_TYPE_MODIFY:
                                        LockCallback mModifyCallback = LockCallbackManager.getInstance().getCallback();
                                        if(mModifyCallback != null){
                                            ((ModifyPasscodeCallback)mModifyCallback).onModifyPasscodeSuccess();
                                        }
                                        break;
                                    case PwdOperateType.PWD_OPERATE_TYPE_RECOVERY://恢复
                                        dataPos++;
                                        LogUtil.e("dataPos:" + dataPos, DBG);
                                        if(dataPos < recoveryDatas.size()) {
                                            final RecoveryData recoveryData = recoveryDatas.get(dataPos);
                                            CommandUtil.manageKeyboardPassword(command.getLockType(), PwdOperateType.PWD_OPERATE_TYPE_RECOVERY, (byte) (recoveryData.keyboardPwdType == 2 ? 1 :recoveryData.keyboardPwdType), recoveryData.cycleType, recoveryData.keyboardPwd, recoveryData.keyboardPwd, recoveryData.startDate, recoveryData.endDate, aesKeyArray, timezoneOffSet);
                                        } else {
                                            LockCallback mRecoveryDataCallback = LockCallbackManager.getInstance().getCallback();
                                            if(mRecoveryDataCallback != null){
                                                ((RecoverLockDataCallback)mRecoveryDataCallback).onRecoveryDataSuccess(transferData.getOp());
                                            }

                                        }
                                        break;
                                        default:
                                            break;
                                }
                                break;
                            }
                            case Command.COMM_RESET_LOCK: {//R重置锁
                                LogUtil.d("reset");
                                LockCallback mResetLockCallback = LockCallbackManager.getInstance().getCallback();
                                if(mResetLockCallback != null){
                                    ((ResetLockCallback)mResetLockCallback).onResetLockSuccess();
                                }
                                break;
                            }
                            case Command.COMM_INIT_PASSWORDS: {//初始化密码
                                LogUtil.d("currentAPICommand:" + currentAPICommand + " COMM_INIT_PASSWORDS", DBG);
                                if(currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                    //todo:流程改了
                                    // TODO:写酒店锁数据
                                    if (hotelData != null) {
                                        isWaitCommand = true;
                                        CommandUtil_V3.configureHotelData(command, HotelData.SET, HotelData.TYPE_IC_KEY, hotelData, aesKeyArray);
                                        //TODO:如果是酒店锁就不会写这个数据了
                                    } else if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.CONFIG_GATEWAY_UNLOCK)) {
                                        CommandUtil_V3.controlRemoteUnlock(command, ConfigRemoteUnlock.OP_TYPE_SEARCH, ConfigRemoteUnlock.OP_CLOSE, aesKeyArray);
                                    } else {
                                        readModelNumber(command);
//                                        CommandUtil.operateFinished(command.getLockType());
                                    }
                                } else{
                                    LockCallback mResetPasscodeCallback = LockCallbackManager.getInstance().getCallback();
                                    lockData.pwdInfo = pwdInfo;
                                    lockData.timestamp = timestamp;
                                    ((ResetPasscodeCallback)mResetPasscodeCallback).onResetPasscodeSuccess(lockData.encodeLockData());
                                }
                                break;
                            }

                            //获取锁时间
                            case Command.COMM_GET_LOCK_TIME: {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(2000 + data[2], data[3] - 1, data[4], data[5], data[6], data[7]);
                                LogUtil.d(data[2] + ":" + data[3] + ":"+ data[4] + ":" +data[5] + ":" + data[6], DBG);
                                //根据时间偏移量计算时间
                                TimeZone timeZone = TimeZone.getDefault();
                                LogUtil.d("timezoneOffSet:" + timezoneOffSet, DBG);
                                if (timeZone.inDaylightTime(new Date(System.currentTimeMillis())))
                                    timezoneOffSet -= timeZone.getDSTSavings();
                                timeZone.setRawOffset((int) timezoneOffSet);
                                calendar.setTimeZone(timeZone);
                                LogUtil.d("calendar.getTimeInMillis():" + calendar.getTimeInMillis(), DBG);
                                LockCallback mTimeCallback = LockCallbackManager.getInstance().getCallback();
                                if(mTimeCallback != null){
                                    ((GetLockTimeCallback)mTimeCallback).onGetLockTimeSuccess(calendar.getTimeInMillis());
                                }
                                break;
                            }
                            case Command.COMM_CHECK_USER_TIME: {//U 校验用户期限
                                int len = data.length - 2;
                                psFromLock = new byte[len];
                                System.arraycopy(data, 2, psFromLock, 0, len);
                                switch (currentAPICommand) {
                                    case APICommand.OP_ACTIVATE_FLOORS:
                                        CommandUtil_V3.activateLiftFloors(command, psFromLock, transferData);
                                        break;
                                    case APICommand.OP_UNLOCK_ADMIN://开锁
                                    case APICommand.OP_UNLOCK_EKEY:
//                                    unlockDate = System.currentTimeMillis();
                                        CommandUtil.G_unlock(command.getLockType(), unlockKey, psFromLock, aesKeyArray, 0, timezoneOffSet);
                                        break;
                                    case APICommand.OP_LOCK_ADMIN://关锁
                                    case APICommand.OP_LOCK_EKEY:
                                        int lockType = command.getLockType();
                                        if(lockType == LockType.LOCK_TYPE_V3_CAR || lockType == LockType.LOCK_TYPE_V3){
                                            CommandUtil.lock(command.getLockType(), unlockKey, psFromLock, aesKeyArray, unlockDate);
                                        }
                                        else{
                                            CommandUtil.L_lock(command.getLockType(), unlockKey, psFromLock, aesKeyArray);
                                        }
                                        break;
                                    case APICommand.OP_CALIBRATE_TIME: {
                                        /**
                                         * 三代锁要校验随机数
                                         */
                                        if(command.getLockType() == LockType.LOCK_TYPE_V3 || command.getLockType() == LockType.LOCK_TYPE_V3_CAR)
                                            CommandUtil.checkRandom(command.getLockType(), unlockKey, psFromLock, aesKeyArray);
                                        else
                                            CommandUtil.C_calibationTime(command.getLockType(), calibationTime, timezoneOffSet, aesKeyArray);
                                        break;
                                    }
                                    case APICommand.OP_LOCK:
                                        CommandUtil.lock(command.getLockType(), unlockKey, psFromLock, aesKeyArray, unlockDate);
                                        break;
                                    case APICommand.OP_READ_PWD_PARA:
                                    case APICommand.OP_REMOTE_CONTROL_DEVICE_MANAGEMENT:
                                        CommandUtil.checkRandom(command.getLockType(), unlockKey, psFromLock, aesKeyArray);
                                        break;
                                        default:
                                            break;
                                }
                                break;
                            }
                            case Command.COMM_UNLOCK: {//G

                                //默认没电量的情况
                                byte battery = -1;
                                //表示不存在用户
                                int uid = 0;
                                Calendar calendar = Calendar.getInstance();
                                //门锁时间 默认设置成门锁时间
                                long lockTime = calendar.getTimeInMillis();
                                //记录唯一标识   默认使用门锁时间
                                int uniqueid = (int) (lockTime / 1000);
                                int len = data.length;
                                if (len > 2) {//电量
                                    battery = data[2];
                                    if (len >= 17) {
                                        uid = (int) DigitUtil.fourBytesToLong(Arrays.copyOfRange(data, 3, 7));
                                        uniqueid = (int) DigitUtil.fourBytesToLong(Arrays.copyOfRange(data, 7, 11));
                                        calendar.set(2000 + data[11], data[12] - 1, data[13], data[14], data[15], data[16]);
                                        //根据时间偏移量计算时间
                                        TimeZone timeZone = TimeZone.getDefault();
                                        LogUtil.d("timezoneOffSet:" + timezoneOffSet, DBG);
                                        if (timeZone.inDaylightTime(new Date(System.currentTimeMillis())))
                                            timezoneOffSet -= timeZone.getDSTSavings();
                                        timeZone.setRawOffset((int) timezoneOffSet);
                                        calendar.setTimeZone(timeZone);
                                        lockTime = calendar.getTimeInMillis();
                                    }
                                }
                                mExtendedBluetoothDevice.setBatteryCapacity(battery);

                                switch (currentAPICommand) {
                                    case APICommand.OP_ACTIVATE_FLOORS: {
                                        LockCallback mControlLockCallback = LockCallbackManager.getInstance().getCallback();
                                        if (mControlLockCallback != null) {
                                            ((ActivateLiftFloorsCallback) mControlLockCallback).onActivateLiftFloorsSuccess(new ActivateLiftFloorsResult(battery, uniqueid, lockTime));
                                        }
                                        break;
                                    }
                                    default:
                                        /**
                                         *车位锁进行相反回调
                                         */
                                        if (command.getLockType() == LockType.LOCK_TYPE_CAR) {
                                            LockCallback mControlLockCallback = LockCallbackManager.getInstance().getCallback();
                                            if (mControlLockCallback != null) {
                                                ((ControlLockCallback) mControlLockCallback).onControlLockSuccess(new ControlLockResult(ControlAction.LOCK, battery, uniqueid, lockTime));
                                            }
                                        } else {
                                            LockCallback mControlLockCallback = LockCallbackManager.getInstance().getCallback();
                                            if (mControlLockCallback != null) {
                                                ((ControlLockCallback) mControlLockCallback).onControlLockSuccess(new ControlLockResult(ControlAction.UNLOCK, battery, uniqueid, lockTime));
                                            }
                                        }
                                        break;
                                }
                                break;
                            }
                            case Command.COMM_LOCK: {//L
                                byte battery = -1;//默认没电量的情况
                                if(data.length > 2)//电量
                                    battery = data[2];
                                mExtendedBluetoothDevice.setBatteryCapacity(battery);
                                //车位锁进行相反回调
                                LockCallback mControlLockCallback = LockCallbackManager.getInstance().getCallback();
                                if(mControlLockCallback != null){
                                    ((ControlLockCallback)mControlLockCallback).onControlLockSuccess(new ControlLockResult(ControlAction.UNLOCK,battery,(int) (System.currentTimeMillis()/1000), System.currentTimeMillis()));
                                }                                break;
                            }
                            case Command.COMM_TIME_CALIBRATE://C
                                if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                    initLock(command);
                                } else {
                                    LockCallback mSetTimeCallback = LockCallbackManager.getInstance().getCallback();
                                    if (mSetTimeCallback != null) {
                                        ((SetLockTimeCallback) mSetTimeCallback).onSetTimeSuccess();
                                    }
                                }
                                break;
                            case Command.COMM_GET_OPERATE_LOG://读取操作日志
                            {
                                synchronized (logOperates) {
                                    short nextSeq = CommandUtil_V3.parseOperateLog(logOperates, Arrays.copyOfRange(data, 2, data.length), timezoneOffSet);

                                    //读取全部 第二圈读完
                                    if (transferData.getLogType() == LogType.ALL && recordCnt == 1 && nextSeq <= lastRecordSeq) {
                                        LockCallback mLogCallback = LockCallbackManager.getInstance().getCallback();
                                        if(mLogCallback != null){
                                            ((GetOperationLogCallback)mLogCallback).onGetLogSuccess(GsonUtil.toJson(logOperates));
                                        }
                                        clearRecordCnt();
                                        logOperates.clear();
                                        return;
                                    }
                                    //记录读取完成
                                    if (nextSeq == (short) 0xFFF0) {
                                        //固件升级用
                                        recordCnt++;
                                        //读全部的开始读第二圈
                                        if (transferData.getLogType() == LogType.ALL && recordCnt == 1) {
                                            LogUtil.d("recordCnt:" + recordCnt);
                                            CommandUtil.getOperateLog(command.getLockType(), (short)(lastRecordSeq+1) , aesKeyArray);
                                            return;
                                        }
                                        LockCallback mLogCallback = LockCallbackManager.getInstance().getCallback();
                                        if(mLogCallback != null){
                                            ((GetOperationLogCallback)mLogCallback).onGetLogSuccess(GsonUtil.toJson(logOperates));
                                        }
                                        clearRecordCnt();
                                        logOperates.clear();
                                    } else {
                                        lastRecordSeq = nextSeq;
                                        CommandUtil.getOperateLog(command.getLockType(), nextSeq, aesKeyArray);
                                    }
                                }
                                break;
                            }
                            case Command.COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED://读取车位锁警报记录和三代锁指令完成
                            {
                                if(command.getLockType() == LockType.LOCK_TYPE_V3 || command.getLockType() == LockType.LOCK_TYPE_V3_CAR) {//三代锁添加完成
                                    LockCallback mLockInitCallback = LockCallbackManager.getInstance().getCallback();
                                    if (mLockInitCallback != null) {
                                        ((InitLockCallback) mLockInitCallback).onInitLockSuccess(lockData.encodeLockData());
                                    }
//                                    //读取版本信息
//                                    tempOptype = DeviceInfoType.MODEL_NUMBER;
//                                    CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.MODEL_NUMBER, aesKeyArray);
//                                    mExtendedBluetoothDevice.disconnectStatus = ExtendedBluetoothDevice.RESPONSE_TIME_OUT;
//                                    mHandler.postDelayed(disConRunable, 1500);
                                } else {
                                    int len = data.length;
                                    if(len == 8) {
                                        if(data[7] == 1) {//还有后续数据要读
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.set(2000 + data[2], data[3] - 1, data[4], data[5], data[6]);
                                            //根据时间偏移量计算时间
                                            TimeZone timeZone = TimeZone.getDefault();
                                            LogUtil.d("timezoneOffSet:" + timezoneOffSet, DBG);
                                            if (timeZone.inDaylightTime(new Date(System.currentTimeMillis())))
                                                timezoneOffSet -= timeZone.getDSTSavings();
                                            timeZone.setRawOffset((int) timezoneOffSet);
                                            calendar.setTimeZone(timeZone);
                                            moveDateArray.put(calendar.getTimeInMillis());
                                            CommandUtil_Va.Va_Get_Lockcar_Alarm(command.getLockType());
                                        } else {
                                            LogUtil.w("get the data of parking lock failed", DBG);
                                        }
                                    } else if(len == 3) {//没有数据了
                                        LockCallback getLogCallback = LockCallbackManager.getInstance().getCallback();
                                        if(getLogCallback != null){
                                            ((GetOperationLogCallback)getLogCallback).onGetLogSuccess( moveDateArray.toString());
                                        }
                                    }
                                }
                                break;
                            }
                            case Command.COMM_GET_VALID_KEYBOARD_PASSWORD://获取锁内有效键盘密码
                            {
                                short nextSeq = CommandUtil_V3.parseKeyboardPwd(Arrays.copyOfRange(data, 2, data.length));
                                if(nextSeq == 0 || nextSeq == (short) 0xFFFF) {//密码读完

                                } else {
                                    CommandUtil.getValidKeyboardPassword(command.getLockType(), nextSeq, aesKeyArray);
                                }
                                break;
                            }
                            case Command.COMM_SEARCHE_DEVICE_FEATURE://查询设备特征
                                if (mExtendedBluetoothDevice != null) {
                                    mExtendedBluetoothDevice.setBatteryCapacity(data[2]);
                                }

                                lockData.specialValue = (int) DigitUtil.fourBytesToLong(Arrays.copyOfRange(data, 3, 7));
                                lockData.featureValue = DigitUtil.convertToFeatureValue(Arrays.copyOfRange(data, 3, data.length));
                                switch (currentAPICommand) {
                                    case APICommand.OP_GET_DEVICE_INFO:
                                        deviceInfo = new DeviceInfo();
                                        deviceInfo.featureValue = lockData.featureValue;
                                        tempOptype = DeviceInfoType.MODEL_NUMBER;
                                        CommandUtil.readDeviceInfo(transferData);
                                        break;
                                    case APICommand.OP_ADD_ADMIN:
                                        genCommandQue(command);
                                        break;
                                    case APICommand.OP_MODIFY_KEYBOARD_PASSWORD:
                                        lockError = tmpLockError;
                                        //不支持提示不支持
//                                        if(!DigitUtil.isSupportModifyPasscode(feature)) {
//                                            lockError = LockError.LOCK_NOT_SUPPORT_CHANGE_PASSCODE;
//                                            lockError.setCommand(Command.COMM_MANAGE_KEYBOARD_PASSWORD);
//                                        }
                                        LockCallback mModifyErrorCallback = LockCallbackManager.getInstance().getCallback();
                                        if(mModifyErrorCallback != null){
                                            mModifyErrorCallback.onFail(lockError);
                                        }
                                        break;
                                    case APICommand.OP_CONTROL_REMOTE_UNLOCK:
                                        LockCallback mRemoteUnlockCallback = LockCallbackManager.getInstance().getCallback();
                                        if(transferData.getOp() == ActionType.SET){
                                            ((SetRemoteUnlockSwitchCallback)mRemoteUnlockCallback).onSetRemoteUnlockSwitchSuccess(lockData.encodeLockData());
                                        }else {
                                            ((GetRemoteUnlockStateCallback)mRemoteUnlockCallback).onGetRemoteUnlockSwitchStateSuccess(transferData.getOpValue() == ActionType.GET);
                                        }
                                        break;
                                    case APICommand.OP_GET_POW:
                                        LockCallback mBatteryCallback = LockCallbackManager.getInstance().getCallback();
                                        if(mBatteryCallback != null){
                                            ((GetBatteryLevelCallback)mBatteryCallback).onGetBatteryLevelSuccess(data[2]);
                                        }
                                        break;
                                    default://todo:后面去掉
                                        LockCallback mSpecialValueCallback = LockCallbackManager.getInstance().getCallback();
                                        if(mSpecialValueCallback != null){
                                            ((GetSpecialValueCallback)mSpecialValueCallback).onGetSpecialValueSuccess(lockData.specialValue);
                                        }
                                        break;
                                }
                                break;
                            case Command.COMM_CONTROL_REMOTE_UNLOCK: {
                                if (data[3] == ConfigRemoteUnlock.OP_TYPE_SEARCH) {
                                    if (transferData == null){
                                        transferData = new TransferData();
                                    }
                                    transferData.setOpValue(data[4]);
                                }
                                switch (currentAPICommand) {
                                    case APICommand.OP_CONTROL_REMOTE_UNLOCK:
                                        transferData.setOp(data[3]);
                                        CommandUtil.searchDeviceFeature(command.getLockType());
                                        break;
                                    case APICommand.OP_ADD_ADMIN:
//                                        CommandUtil.operateFinished(command.getLockType());
                                        readModelNumber(command);
                                        break;
                                        default:
                                            break;
                                }
                                break;
                            }
                            case Command.COMM_AUDIO_MANAGE: //todo:添加管理员读取数据
                                switch (data[3]) {
                                    case AudioManage.QUERY:
                                        transferData.setOpValue(data[4]);
                                        lockData.lockSound = data[4];//音量开关
                                        if (data.length > 5) {
                                            lockData.soundVolume = data[5];
                                            transferData.setSoundVolume(SoundVolume.getInstance(data[5]));
                                        }
                                        doResponse(data[0], command);
                                        break;
                                    case AudioManage.MODIFY:
                                        LockCallback mSoundCallback = LockCallbackManager.getInstance().getCallback();
                                        if (mSoundCallback instanceof SetLockConfigCallback) {
                                            ((SetLockConfigCallback) mSoundCallback).onSetLockConfigSuccess(TTLockConfigType.LOCK_SOUND);
                                        } else if (mSoundCallback instanceof SetLockMuteModeCallback) {//回调应该不需要有参数
                                            ((SetLockMuteModeCallback) mSoundCallback).onSetMuteModeSuccess(transferData.getOpValue() == 0);
                                        } else if (mSoundCallback instanceof SetLockSoundWithSoundVolumeCallback) {
                                            ((SetLockSoundWithSoundVolumeCallback) mSoundCallback).onSetLockSoundSuccess();
                                        }
                                        break;
                                }
                                break;
                            case Command.COMM_REMOTE_CONTROL_DEVICE_MANAGE: {
                                LockCallback rollGateCallback = LockCallbackManager.getInstance().getCallback();
                                if (rollGateCallback != null){
                                    ((ControlLockCallback)rollGateCallback).onControlLockSuccess(new ControlLockResult(data[4],data[2],-1, -1));
                                }
                                LogUtil.d("data[3]:" + data[3], DBG);
                                LogUtil.d("length:" + Arrays.toString(data), DBG);
                                break;
                            }
                            case Command.COMM_PWD_LIST: {
                                int totalRecord = (short) (data[2] << 8 | (data[3] & 0xff));
                                //无数据
                                if(totalRecord == 0) {
                                    LockCallback mGetPasscodeCallback = LockCallbackManager.getInstance().getCallback();
                                    if(mGetPasscodeCallback != null){
                                        ((GetAllValidPasscodeCallback)mGetPasscodeCallback).onGetAllValidPasscodeSuccess(GsonUtil.toJson(passcodes));
                                    }
                                } else {
                                    short nextSeq = parsePasscode(Arrays.copyOfRange(data, 4, data.length));
                                    if (nextSeq == -1) {
                                        LockCallback mGetPasscodeCallback = LockCallbackManager.getInstance().getCallback();
                                        if(mGetPasscodeCallback != null){
                                            ((GetAllValidPasscodeCallback)mGetPasscodeCallback).onGetAllValidPasscodeSuccess(GsonUtil.toJson(passcodes));
                                        }
                                    } else{
                                        CommandUtil.searchPasscode(command.getLockType(), nextSeq, aesKeyArray);
                                    }
                                }
                                break;
                            }
                            case Command.COMM_IC_MANAGE:
                                int battery = data[2];
                                switch (data[3]) {
                                    case ICOperate.IC_SEARCH:
                                        LogUtil.d("ic bytes:" + Arrays.toString(data));
                                        short nextSeq = parseIC(Arrays.copyOfRange(data, 4, data.length));
                                        LogUtil.d("search:" + nextSeq, DBG);
                                        if (nextSeq == -1) {
                                            //TODO:
                                            LockCallback mGetAllCards = LockCallbackManager.getInstance().getCallback();
                                            if(mGetAllCards != null){
                                                ((GetAllValidICCardCallback)mGetAllCards).onGetAllValidICCardSuccess(GsonUtil.toJson(icCards));
                                            }
                                        } else
                                            CommandUtil.searchICCardNo(command.getLockType(), nextSeq, aesKeyArray);
                                        break;
                                    case ICOperate.ADD:
                                        addICResponse(command, data);
                                        break;
                                    case ICOperate.MODIFY:
                                        modifyICPeriodResponse(command);
                                        break;
                                    case ICOperate.DELETE:
                                        switch (currentAPICommand) {
                                            case APICommand.OP_DELETE_IC:
                                                LockCallback mDeleteCard = LockCallbackManager.getInstance().getCallback();
                                                if (mDeleteCard != null) {
                                                    ((DeleteICCardCallback) mDeleteCard).onDeleteICCardSuccess();
                                                }
                                                break;
                                            case APICommand.OP_LOSS_IC:
                                                mDeleteCard = LockCallbackManager.getInstance().getCallback();
                                                if (mDeleteCard != null) {
                                                    ((ReportLossCardCallback) mDeleteCard).onReportLossCardSuccess();
                                                }
                                                break;
                                            case APICommand.OP_ADD_IC:
                                                errorCallback(tmpLockError);
                                                break;
                                        }
                                        break;
                                    case ICOperate.CLEAR:
                                        LockCallback mClearCard = LockCallbackManager.getInstance().getCallback();
                                        if(mClearCard != null){
                                            ((ClearAllICCardCallback)mClearCard).onClearAllICCardSuccess();
                                        }
                                        break;
                                        default:
                                            break;
                                }
                                break;
                            case Command.COMM_FR_MANAGE:
                            {
                                battery = data[2];
                                switch (data[3]) {
                                    case ICOperate.FR_SEARCH:
                                        short nextSeq = parseFR(Arrays.copyOfRange(data, 4, data.length));
                                        LogUtil.d("search:" + nextSeq, DBG);
                                        if (nextSeq == -1) {
                                            //TODO:
                                            LockCallback mGetAllFingerprints = LockCallbackManager.getInstance().getCallback();
                                            if(mGetAllFingerprints != null){
                                                ((GetAllValidFingerprintCallback)mGetAllFingerprints).onGetAllFingerprintsSuccess(GsonUtil.toJson(frs));
                                            }
                                            LogUtil.d("finish", DBG);
                                        } else
                                            CommandUtil.searchFRNo(command.getLockType(), nextSeq, aesKeyArray);
                                        break;
                                    case ICOperate.ADD:
                                        addFRResponse(command, data);
                                        break;
                                    case ICOperate.MODIFY:
                                        modifyFrPeriodResponse(command);
                                        break;
                                    case ICOperate.DELETE:
                                        switch (currentAPICommand) {
                                            case APICommand.OP_DELETE_FR:
                                                LockCallback mDeleteFrPeriod = LockCallbackManager.getInstance().getCallback();
                                                if (mDeleteFrPeriod != null) {
                                                    ((DeleteFingerprintCallback) mDeleteFrPeriod).onDeleteFingerprintSuccess();
                                                }
                                                break;
                                            case APICommand.OP_ADD_FR:
                                                errorCallback(tmpLockError);
                                                break;
                                        }
                                        break;
                                    case ICOperate.CLEAR:
                                        LockCallback mClearFingerprint = LockCallbackManager.getInstance().getCallback();
                                        if(mClearFingerprint != null){
                                            ((ClearAllFingerprintCallback)mClearFingerprint).onClearAllFingerprintSuccess();
                                        }
                                        break;
                                    case ICOperate.WRITE_FR:
                                        isWaitCommand = true;
                                        short seq = (short) (((data[4] << 8) | (data[5] & 0xff)) + 1);
                                        int leftLen = transferData.getTransferData().length - (seq * packetLen);
                                        LogUtil.d("leftLen:" + leftLen);
                                        LogUtil.d("seq:" + seq);
                                        if (leftLen > 0) {
//                                        if (packetLen > leftLen)
//                                            packetLen = leftLen;
                                            CommandUtil_V3.writeFR(command, transferData.getTransferData(), seq, packetLen, transferData.getAesKeyArray());
                                        }
                                        break;
                                }
                                break;
                            }
                            case Command.COMM_SET_WRIST_BAND_KEY:
                                battery = data[2];
//                                onSetWristbandKeyToLock(mExtendedBluetoothDevice, battery, error);
                                break;
                            case Command.COMM_AUTO_LOCK_MANAGE:
                                battery = data[2];
                                byte op = data[3];
                                switch (op) {
                                    case AutoLockOperate.SEARCH:
                                        LogUtil.e(DigitUtil.byteArrayToHexString(data), true);
                                        short currentTime = (short) ((data[4] << 8) | (data[5] & 0x000000ff));
                                        short minTime = (short) ((data[6] << 8) | (data[7]& 0x000000ff));
                                        short maxTime = (short) ((data[8] << 8) | (data[9] & 0x000000ff));
                                        lockData.autoLockTime = currentTime;
                                        switch (currentAPICommand) {
                                            case APICommand.OP_DOOR_SENSOR:
                                                byte operationValue = 0;
                                                if (data.length == 11)
                                                    operationValue = data[10];
//                                            onOperateDoorSensorLocking(mExtendedBluetoothDevice, battery, op, operationValue, error);
                                                break;
                                            case APICommand.OP_ADD_ADMIN:
                                                doQueryCommand(command);
                                                break;
                                            case APICommand.OP_SEARCH_AUTO_LOCK_PERIOD:
                                                LockCallback mAutoLockingCallback = LockCallbackManager.getInstance().getCallback();
                                                if(mAutoLockingCallback != null){
                                                    ((GetAutoLockingPeriodCallback)mAutoLockingCallback).onGetAutoLockingPeriodSuccess(currentTime, minTime, maxTime);
                                                }
                                                break;
                                        }
                                        break;
                                    case AutoLockOperate.MODIFY:
                                        if(currentAPICommand == APICommand.OP_DOOR_SENSOR) {
//                                            onOperateDoorSensorLocking(mExtendedBluetoothDevice, battery, op, transferData.getOpValue(), error);
                                        } else{
                                            LockCallback mAutoLockingCallback = LockCallbackManager.getInstance().getCallback();
                                            if(mAutoLockingCallback != null){
                                                ((SetAutoLockingPeriodCallback)mAutoLockingCallback).onSetAutoLockingPeriodSuccess();
                                            }
                                        }
                                        break;
                                        default:
                                            break;
                                }
                                break;
                            case COMM_READ_DEVICE_INFO:
                                LogUtil.d("tempOptype:" + tempOptype, DBG);
                                LogUtil.d("COMM_READ_DEVICE_INFO:" + DigitUtil.byteArrayToHexString(data), DBG);
                                LogUtil.d("COMM_READ_DEVICE_INFO:" + new String(Arrays.copyOfRange(data, 2, data.length - 1)), DBG);
                                switch (tempOptype) {
                                    case DeviceInfoType.MODEL_NUMBER://型号
                                        if (deviceInfo == null){
                                            deviceInfo = new DeviceInfo();
                                        }
                                        LogUtil.d("deviceInfo:" + deviceInfo, DBG);
                                        modelNumber = new String(Arrays.copyOfRange(data, 2, data.length - 1));
                                        if (lockData != null) {
                                            lockData.setModelNum(modelNumber);
                                        }
                                        deviceInfo.modelNum = modelNumber;
                                        tempOptype = DeviceInfoType.HARDWARE_REVISION;
                                        CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.HARDWARE_REVISION, aesKeyArray);
                                        break;
                                    case DeviceInfoType.HARDWARE_REVISION://硬件版本号
                                        LogUtil.w("deviceInfo:" + deviceInfo, DBG);
                                        hardwareRevision = new String(Arrays.copyOfRange(data, 2, data.length - 1));
                                        if (lockData != null) {
                                            lockData.setHardwareRevision(hardwareRevision);
                                        }
                                        deviceInfo.hardwareRevision = hardwareRevision;
                                        tempOptype = DeviceInfoType.FIRMWARE_REVISION;
                                        CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.FIRMWARE_REVISION, aesKeyArray);
                                        break;
                                    case DeviceInfoType.FIRMWARE_REVISION://固件版本号
                                        firmwareRevision = new String(Arrays.copyOfRange(data, 2, data.length - 1));
                                        if (lockData != null) {
                                            lockData.setFirmwareRevision(firmwareRevision);
                                        }
                                        deviceInfo.firmwareRevision = firmwareRevision;
                                        tempOptype = DeviceInfoType.MANUFACTURE_DATE;
                                        CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.MANUFACTURE_DATE, aesKeyArray);

                                        break;
                                    case DeviceInfoType.MANUFACTURE_DATE://生产日期
                                        factoryDate = new String(Arrays.copyOfRange(data, 2, data.length - 1));
                                        if (lockData != null) {
                                            lockData.factoryDate = factoryDate;
                                        }
                                        deviceInfo.factoryDate = factoryDate;
                                        tempOptype = DeviceInfoType.LOCK_CLOCK;
//                                    CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.LOCK_CLOCK, aesKeyArray);

                                        //NB锁
                                        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.NB_LOCK)) {
                                            if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                                tempOptype = DeviceInfoType.NB_OPERATOR;
                                            }
                                            CommandUtil.readDeviceInfo(command.getLockType(), (byte) tempOptype, aesKeyArray);
                                        } else if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.INCOMPLETE_PASSCODE)) {
                                            //不读锁时钟了
                                            tempOptype = DeviceInfoType.PASSCODE_KEY_NUMBER;
                                            CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.PASSCODE_KEY_NUMBER, aesKeyArray);
                                        } else {
                                            if (currentAPICommand == APICommand.OP_ADD_ADMIN) {//三代锁
                                                if (transferData != null)
                                                    mExtendedBluetoothDevice.setRemoteUnlockSwitch(transferData.getOpValue());
                                                lockData = getLockInfoObj();
                                                lockData.lockVersion = command.getLockVersion();
                                                CommandUtil.operateFinished(command.getLockType());
//                                                LockCallback mLockInitCallback = LockCallbackManager.getInstance().getCallback();
//                                                if(mLockInitCallback != null){
//                                                    ((InitLockCallback)mLockInitCallback).onInitLockSuccess(lockData.encodeLockData());
//                                                }
                                            } else{
                                                CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.LOCK_CLOCK, aesKeyArray);
                                            }
                                        }
                                        break;
                                    case DeviceInfoType.LOCK_CLOCK://时钟
                                        lockClock = "";
                                        for(int i=2;i<data.length - 1;i++) {
                                            lockClock = lockClock + String.format("%02d", data[i]);
                                        }
                                        deviceInfo.lockClock = lockClock;
                                        //NB锁
                                        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.NB_LOCK)) {
                                            tempOptype = DeviceInfoType.NB_OPERATOR;
                                            CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.NB_OPERATOR, aesKeyArray);
                                        } else {
                                            tempOptype = -1;
                                            deviceInfo.featureValue = lockData.featureValue;
                                            if (lockData != null) {
                                                deviceInfo.lockData = lockData.encodeLockData();
                                            }
                                            LockCallback mInfoCallback = LockCallbackManager.getInstance().getCallback();
                                            if(mInfoCallback != null){
                                                ((GetLockSystemInfoCallback)mInfoCallback).onGetLockSystemInfoSuccess(deviceInfo);
                                            }
                                        }
                                        break;
                                    case DeviceInfoType.NB_OPERATOR:
                                        deviceInfo.nbOperator = new String(Arrays.copyOfRange(data, 2, data.length - 1));
                                        if (lockData != null) {
                                            lockData.setNbOperator(deviceInfo.nbOperator);
                                        }
                                        tempOptype = DeviceInfoType.NB_IMEI;
                                        CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.NB_IMEI, aesKeyArray);
                                        break;
                                    case DeviceInfoType.NB_IMEI://TODO:
                                        deviceInfo.nbNodeId = new String(Arrays.copyOfRange(data, 2, data.length - 1));
                                        if (lockData != null) {
                                            lockData.setNbNodeId(deviceInfo.getNbNodeId());
                                        }
                                        tempOptype = DeviceInfoType.NB_CARD_INFO;
                                        CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.NB_CARD_INFO, aesKeyArray);
                                        LogUtil.d("NB_IMEI:" + deviceInfo.nbNodeId, DBG);
                                        break;
                                    case DeviceInfoType.NB_CARD_INFO:
                                        deviceInfo.nbCardNumber = new String(Arrays.copyOfRange(data, 2, data.length - 1));
                                        if (lockData != null) {
                                            lockData.setNbCardNumber(deviceInfo.nbCardNumber);
                                        }
                                        tempOptype = DeviceInfoType.NB_RSSI;
                                        CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.NB_RSSI, aesKeyArray);
                                        LogUtil.d("NB_CARD_INFO:" + deviceInfo.nbCardNumber, DBG);
                                        break;
                                    case DeviceInfoType.NB_RSSI:
                                        deviceInfo.nbRssi = data[2];
                                        if (lockData != null) {
                                            lockData.setNbRssi(deviceInfo.nbRssi);
                                        }
                                        LogUtil.d("NB_RSSI:" + deviceInfo.nbRssi, DBG);
                                        //TODO:
                                        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.INCOMPLETE_PASSCODE)) {
                                            tempOptype = DeviceInfoType.PASSCODE_KEY_NUMBER;
                                            CommandUtil.readDeviceInfo(command.getLockType(), (byte) tempOptype, aesKeyArray);
                                        } else if (currentAPICommand == APICommand.OP_ADD_ADMIN) {//nb

                                            if (transferData.getPort() != 0 && !TextUtils.isEmpty(transferData.getAddress()))
                                                CommandUtil_V3.configureNBServerAddress(command, (short) transferData.getPort(), transferData.getAddress(), aesKeyArray);
                                            else {
                                                lockData = getLockInfoObj();
                                                lockData.lockVersion = command.getLockVersion();
                                                CommandUtil.operateFinished(command.getLockType());
//                                                LockCallback mLockCallback = LockCallbackManager.getInstance().getCallback();
//                                                if(mLockCallback != null){
//                                                    ((InitLockCallback)mLockCallback).onInitLockSuccess(lockData.encodeLockData());
//                                                }
                                            }
                                        } else {
                                            tempOptype = -1;
                                            LockCallback mInfoCallback = LockCallbackManager.getInstance().getCallback();
                                            if(mInfoCallback != null){
                                                if (lockData != null) {
                                                    deviceInfo.lockData = lockData.encodeLockData();
                                                }
                                                ((GetLockSystemInfoCallback)mInfoCallback).onGetLockSystemInfoSuccess(deviceInfo);
                                            }
                                        }
                                        break;
                                    case DeviceInfoType.PASSCODE_KEY_NUMBER:
                                        deviceInfo.passcodeKeyNumber = data[2];
                                        if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                            if (transferData.getPort() != 0 && !TextUtils.isEmpty(transferData.getAddress()))
                                                CommandUtil_V3.configureNBServerAddress(command, (short) transferData.getPort(), transferData.getAddress(), aesKeyArray);
                                            else {
                                                lockData = getLockInfoObj();
                                                lockData.lockVersion = command.getLockVersion();
                                                CommandUtil.operateFinished(command.getLockType());
                                            }
                                        } else {
                                            tempOptype = -1;
                                            LockCallback mInfoCallback = LockCallbackManager.getInstance().getCallback();
                                            if(mInfoCallback != null){
                                                if (lockData != null) {
                                                    deviceInfo.lockData = lockData.encodeLockData();
                                                }
                                                ((GetLockSystemInfoCallback)mInfoCallback).onGetLockSystemInfoSuccess(deviceInfo);
                                            }
                                        }
                                        break;
                                    default:
                                        //默认读取型号
                                        if (deviceInfo == null)
                                            deviceInfo = new DeviceInfo();
                                        modelNumber = new String(Arrays.copyOfRange(data, 2, data.length - 1));
                                        deviceInfo.modelNum = modelNumber;
                                        tempOptype = DeviceInfoType.HARDWARE_REVISION;
                                        CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.HARDWARE_REVISION, aesKeyArray);
                                        break;
                                }
                                break;
                            case Command.COMM_ENTER_DFU_MODE:
                                LockCallback mEnterDfuCallback = LockCallbackManager.getInstance().getCallback();
                                if(mEnterDfuCallback != null){
                                    ((EnterDfuModeCallback)mEnterDfuCallback).onEnterDfuMode();
                                }
                                break;
                            case Command.COMM_READ_PWD_PARA:
                                battery = data[2];
                                int code = ((data[3] << 4) | ((data[4] >> 4) & 0x0f)) & 0x0fff;
                                LogUtil.d("bytes:" + DigitUtil.byteArrayToHexString(data), DBG);

                                long secretKey = (((data[4] * 1l << 32)&0x0f00000000l) | ((data[5] << 24l) & 0xff000000l) | ((data[6] << 16l) & 0x00ff0000) | ((data[7] << 8l) & 0xff00) | (data[8] & 0xff)) & 0x0fffffffffl;
                                LogUtil.d("code:" + code, DBG);
                                LogUtil.d("secretKey:" + secretKey, DBG);
                                LogUtil.d("sec:" + DigitUtil.byteArrayToHexString(data), DBG);
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(2000+data[9], data[10] - 1, data[11], data[12], data[13]);
                                LogUtil.d("data[9]:" + data[9], DBG);
                                //根据时间偏移量计算时间
                                TimeZone timeZone = TimeZone.getDefault();
                                LogUtil.d("timezoneOffSet:" + timezoneOffSet, DBG);
                                if (timeZone.inDaylightTime(new Date(System.currentTimeMillis())))
                                    timezoneOffSet -= timeZone.getDSTSavings();
                                timeZone.setRawOffset((int) timezoneOffSet);
                                calendar.setTimeZone(timeZone);
                                long deleteTime = calendar.getTimeInMillis();
                                if(data[9] == 0)
                                    deleteTime = 0;
                                LogUtil.d("code:" + code, DBG);
                                PwdInfoV3 pwdInfoV3 = PwdInfoV3.getInstance(calendar.get(Calendar.YEAR), code, String.valueOf(secretKey), deleteTime);
                                String pwdInfoSource = GsonUtil.toJson(pwdInfoV3);
                                long timestamp = calendar.getTimeInMillis();
                                String pwdInfo = CommandUtil.encry(pwdInfoSource, timestamp);
                                LockCallback mGetPwdInfoCalback = LockCallbackManager.getInstance().getCallback();
                                if(mGetPwdInfoCalback != null){
                                    lockData.pwdInfo = pwdInfo;
                                    lockData.timestamp = timestamp;
                                    ((GetPasscodeVerificationInfoCallback)mGetPwdInfoCalback).onGetInfoSuccess(lockData.encodeLockData());
                                }
                                break;
                            case Command.COMM_CONFIGURE_NB_ADDRESS:
                                LogUtil.d("COMM_CONFIGURE_NB_ADDRESS - battery:" + data[2], DBG);
                                if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                    lockData = getLockInfoObj();
                                    lockData.lockVersion = command.getLockVersion();
                                    CommandUtil.operateFinished(command.getLockType());
//                                    LockCallback mCallback = LockCallbackManager.getInstance().getCallback();
//                                    if(mCallback != null){
//                                        ((InitLockCallback)mCallback).onInitLockSuccess(lockData.encodeLockData());
//                                    }

                                } else {
                                    LockCallback mNbCallback = LockCallbackManager.getInstance().getCallback();
                                    if(mNbCallback != null){
                                        ((SetNBServerCallback)mNbCallback).onSetNBServerSuccess( data[2]);
                                    }
                                }
                                break;
                            case Command.COMM_CONFIGURE_HOTEL_DATA:
                                //TODO:
                                LogUtil.d("COMM_CONFIGURE_HOTEL_DATA:");
                                if (data[3] == HotelData.GET) {
                                    ResponseUtil.getHotelData(data);
                                } else if (data[3] == HotelData.SET) {
                                    switch (data[4]) {
                                        case HotelData.TYPE_IC_KEY:
                                            CommandUtil_V3.configureHotelData(command, HotelData.SET, HotelData.TYPE_AES_KEY, hotelData, aesKeyArray);
                                            break;
                                        case HotelData.TYPE_AES_KEY:
                                             CommandUtil_V3.configureHotelData(command, HotelData.SET, HotelData.TYPE_HOTEL_BUILDING_FLOOR, hotelData, aesKeyArray);
                                            break;
                                        case HotelData.TYPE_HOTEL_BUILDING_FLOOR:
                                            switch (currentAPICommand) {
                                                case APICommand.OP_SET_HOTEL_DATA://修改楼栋楼层
                                                    LockCallback lockCallback = LockCallbackManager.getInstance().getCallback();
                                                    if (lockCallback != null) {
                                                        ((SetHotelDataCallback)lockCallback).onSetHotelDataSuccess();
                                                    }
                                                    break;
                                                default:
                                                    CommandUtil_V3.configureHotelData(command, HotelData.SET, HotelData.TYPE_SECTOR, hotelData, aesKeyArray);
//                                                    //添加管理员后续
//                                                    CommandUtil.operateFinished(command.getLockType());
                                                    break;
                                            }
                                            break;
                                        case HotelData.TYPE_SECTOR:
                                            switch (currentAPICommand) {
//                                                case APICommand.OP_SET_HOTEL_DATA:
//                                                    TTLockAPI.getTTLockCallback().onSetHotelData(mExtendedBluetoothDevice, error);
//                                                    break;
                                                case APICommand.OP_ADD_ADMIN:
                                                    if (hotelData.controlableFloors != null) {
                                                        CommandUtil_V3.configureHotelData(command, HotelData.SET, HotelData.TYPE_ELEVATOR_CONTROLABLE_FLOORS, hotelData, aesKeyArray);
                                                    } else {
                                                        readModelNumber(command);
//                                                        CommandUtil.operateFinished(command.getLockType());
                                                    }
                                                     break;
                                                case APICommand.OP_SET_HOTEL_CARD_SECTION:
                                                    LockCallback callback = LockCallbackManager.getInstance().getCallback();
                                                    if (callback != null) {
                                                        ((SetHotelCardSectorCallback) callback).onSetHotelCardSectorSuccess();
                                                    }
//                                                    TTLockAPI.getTTLockCallback().onSetHotelCardSector(mExtendedBluetoothDevice, error);
                                                    break;
                                            }
                                            break;
                                        case HotelData.TYPE_ELEVATOR_CONTROLABLE_FLOORS:
                                            if (hotelData.ttLiftWorkMode != null) {
                                                CommandUtil_V3.configureHotelData(command, HotelData.SET, HotelData.TYPE_ELEVATOR_WORK_MODE, hotelData, aesKeyArray);
                                            } else {
                                                LockCallback callback = LockCallbackManager.getInstance().getCallback();
                                                if (callback != null) {
                                                    ((SetLiftControlableFloorsCallback) callback).onSetLiftControlableFloorsSuccess();
                                                }
                                            }
                                            break;
                                        case HotelData.TYPE_ELEVATOR_WORK_MODE:
                                            switch (currentAPICommand) {
                                                case APICommand.OP_ADD_ADMIN:
                                                    readModelNumber(command);
//                                                    CommandUtil.operateFinished(command.getLockType());
                                                    break;
                                                default:
                                                    LockCallback callback = LockCallbackManager.getInstance().getCallback();
                                                    if (callback != null) {
                                                        ((SetLiftWorkModeCallback) callback).onSetLiftWorkModeSuccess();
                                                    }
                                                    break;
                                            }
                                            break;
                                        case HotelData.TYPE_POWER_SAVER_WORK_MODE:
                                            LockCallback callback = LockCallbackManager.getInstance().getCallback();
                                            if (callback != null) {
                                                ((SetPowerSaverWorkModeCallback) callback).onSetPowerSaverWorkModeSuccess();
                                            }
                                            break;
                                        case HotelData.TYPE_POWER_SAVER_CONTROLABLE_LOCK:
                                            callback = LockCallbackManager.getInstance().getCallback();
                                            if (callback != null) {
                                                ((SetPowerSaverControlableLockCallback) callback).onSetPowerSaverControlableLockSuccess();
                                            }
                                            break;
                                    }
                                }
                                break;
                            case Command.COMM_GET_ADMIN_CODE:
                                int len = data[3];
                                adminPasscode = new String(Arrays.copyOfRange(data, 4, data.length));
                                LogUtil.d("adminCode:" + adminPasscode);
//                            adminCode = DigitUtil.encodeLockData(adminCode);
                                if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
                                    if (TextUtils.isEmpty(adminPasscode)) {//密码是空 就重新设置
                                        adminPasscode = DigitUtil.generatePwdByLength(7);
                                        CommandUtil.S_setAdminKeyboardPwd(command.getLockType(), adminPasscode, aesKeyArray);
                                    } else {
                                        CommandUtil_V3.initPasswords(command.getLockType(), aesKeyArray, currentAPICommand);
                                    }
                                } else if (currentAPICommand == APICommand.OP_GET_ADMIN_KEYBOARD_PASSWORD) {
                                    LockCallback mGetAdminPwdCallback = LockCallbackManager.getInstance().getCallback();
                                    if(mGetAdminPwdCallback != null){
                                        ((GetAdminPasscodeCallback)mGetAdminPwdCallback).onGetAdminPasscodeSuccess(adminPasscode);
                                    }
                                }
                                //TODO:
                                break;
                            case Command.COMM_CONFIGURE_PASSAGE_MODE: {
                                switch (data[3]) {
                                    case PassageModeOperate.QUERY:
                                        if (data[4] == (byte)0xff) {
                                            LockCallback mGetPassageModeCallback = LockCallbackManager.getInstance().getCallback();
                                            if(mGetPassageModeCallback != null){
                                                ((GetPassageModeCallback)mGetPassageModeCallback).onGetPassageModeSuccess(GsonUtil.toJson(passageModeDatas));
                                            }
                                        } else {
                                            doWithPassageModeData(Arrays.copyOfRange(data, 5, data.length));
                                            CommandUtil_V3.queryPassageMode(command, data[4], aesKeyArray);
                                        }
                                        break;
                                    case PassageModeOperate.ADD:
                                        try {
                                            dataPos++;
                                            if (dataPos < weerOrDays.length()) {
                                                CommandUtil_V3.configurePassageMode(command, transferData, ((Integer) weerOrDays.get(dataPos)).byteValue());
                                            } else {
                                                LockCallback mSetPassageModeCallback = LockCallbackManager.getInstance().getCallback();
                                                if(mSetPassageModeCallback != null){
                                                    ((SetPassageModeCallback)mSetPassageModeCallback).onSetPassageModeSuccess();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case PassageModeOperate.DELETE:
                                        try {
                                            dataPos++;
                                            if (dataPos < weerOrDays.length()) {
                                                CommandUtil_V3.deletePassageMode(command, transferData, ((Integer) weerOrDays.get(dataPos)).byteValue());
                                            } else {
                                                LockCallback mDeletePassageModeCallback = LockCallbackManager.getInstance().getCallback();
                                                if(mDeletePassageModeCallback != null){
                                                    ((DeletePassageModeCallback)mDeletePassageModeCallback).onDeletePassageModeSuccess();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case PassageModeOperate.CLEAR:
                                        LockCallback mClearModeCallback = LockCallbackManager.getInstance().getCallback();
                                        if(mClearModeCallback != null){
                                            ((ClearPassageModeCallback)mClearModeCallback).onClearPassageModeSuccess();
                                        }
                                        break;
                                        default:
                                            break;
                                }
                                break;
                            }
                            case Command.COMM_FREEZE_LOCK://todo:从服务器读取
                                LockCallback lockCallback = LockCallbackManager.getInstance().getCallback();
                                if (lockCallback != null) {
                                    switch (data[3]) {
                                        case OperationType.GET_STATE:
                                            if (lockCallback instanceof GetLockConfigCallback) {
                                                ((GetLockConfigCallback)lockCallback).onGetLockConfigSuccess(TTLockConfigType.LOCK_FREEZE, data[4] == 1);
                                            } else if (lockCallback instanceof GetLockFreezeStateCallback) {
                                                ((GetLockFreezeStateCallback)lockCallback).onGetLockFreezeStateSuccess(data[4] == 1);
                                            } else {
                                                LogUtil.d("lockCallback:" + lockCallback);
                                            }
                                            break;
                                        case OperationType.MODIFY:
                                            if (lockCallback instanceof SetLockConfigCallback) {
                                                ((SetLockConfigCallback)lockCallback).onSetLockConfigSuccess(TTLockConfigType.LOCK_FREEZE);
                                            } else if (lockCallback instanceof SetLockFreezeStateCallback) {
                                                ((SetLockFreezeStateCallback)lockCallback).onSetLockFreezeStateSuccess();
                                            }
                                            break;
                                    }
                                }
                                break;
                            case Command.COMM_LAMP:
                                switch (data[3]) {
                                    case OperationType.GET_STATE:
                                        lockData.lightingTime = ((data[5] & 0xff) | ((data[4] << 8) & 0xff00)) & 0xffff;
                                        doResponse(data[0], command);
                                        break;
                                    case OperationType.MODIFY:
                                        lockCallback = LockCallbackManager.getInstance().getCallback();
                                        ((SetLightTimeCallback) lockCallback).onSetLightTimeSuccess();
                                        break;
                                }
                                break;
                            case Command.COMM_SWITCH:
                                switch (data[3]) {
                                    case OperationType.GET_STATE:
                                        switchItem = (int) DigitUtil.bytesToLong(Arrays.copyOfRange(data, 4, 8));
                                        switchValue = (int) DigitUtil.bytesToLong(Arrays.copyOfRange(data, 8, 12));
                                        settingValue = Integer.valueOf(switchValue);
                                        LogUtil.d("switchItem:" + switchItem);
                                        LogUtil.d("switchValue:" + switchValue);
                                        LogUtil.d("transferData.getOp():" + transferData.getOp());
                                        switch (currentAPICommand) {
                                            case APICommand.OP_SET_SWITCH:
                                                if (transferData.getOpValue() > 0) {
                                                    switchValue |= transferData.getOp();
                                                } else {
                                                    switchValue &= ~transferData.getOp();
                                                }
                                                LogUtil.d("new switchValue:" + switchValue);
                                                CommandUtil_V3.setSwitchState(command, switchItem, switchValue, aesKeyArray);
                                                break;
                                            case APICommand.OP_SET_UNLOCK_DIRECTION:
                                                if (transferData.getUnlockDirection().getValue() > 0) {
                                                    switchValue |= TTLockConfigType.UNLOCK_DIRECTION.getItem();
                                                } else {
                                                    switchValue &= ~TTLockConfigType.UNLOCK_DIRECTION.getItem();
                                                }
                                                LogUtil.d("new switchValue:" + switchValue);
                                                CommandUtil_V3.setSwitchState(command, switchItem, switchValue, aesKeyArray);
                                                break;
                                            case APICommand.OP_GET_UNLOCK_DIRECTION:
                                                lockCallback = LockCallbackManager.getInstance().getCallback();
                                                if ((switchValue & TTLockConfigType.UNLOCK_DIRECTION.getItem()) != 0) {//1左开 0右开
                                                    ((GetUnlockDirectionCallback) lockCallback).onGetUnlockDirectionSuccess(UnlockDirection.LEFT);
                                                } else {
                                                    ((GetUnlockDirectionCallback) lockCallback).onGetUnlockDirectionSuccess(UnlockDirection.RIGHT);
                                                }
                                                break;
                                            default:
                                                doResponse(data[0], command);
                                                break;
                                        }
                                        break;
                                    case OperationType.MODIFY:
                                        lockCallback = LockCallbackManager.getInstance().getCallback();
                                        switch (currentAPICommand) {
                                            case APICommand.OP_SET_SWITCH:
                                                ((SetLockConfigCallback) lockCallback).onSetLockConfigSuccess(TTLockConfigType.getInstance(transferData.getOp()));
                                                break;
                                            case APICommand.OP_SET_UNLOCK_DIRECTION:
                                                ((SetUnlockDirectionCallback) lockCallback).onSetUnlockDirectionSuccess();
                                                break;
                                        }

                                        break;
                                }
                                break;
                            case Command.COMM_DEAD_LOCK: {
                                battery = -1;//默认没电量的情况
                                int uid = 0;//表示不存在用户
                                calendar = Calendar.getInstance();
                                long lockTime = calendar.getTimeInMillis();//门锁时间 默认设置成门锁时间
                                int uniqueid = (int) (lockTime / 1000);//记录唯一标识   默认使用门锁时间
                                battery = data[2];
                                uid = (int) DigitUtil.fourBytesToLong(Arrays.copyOfRange(data, 3, 7));
                                uniqueid = (int) DigitUtil.fourBytesToLong(Arrays.copyOfRange(data, 7, 11));
                                calendar.set(2000 + data[11], data[12] - 1, data[13], data[14], data[15], data[16]);
                                //根据时间偏移量计算时间
                                timeZone = TimeZone.getDefault();
                                LogUtil.d("timezoneOffSet:" + timezoneOffSet, DBG);
                                if (timeZone.inDaylightTime(new Date(System.currentTimeMillis())))
                                    timezoneOffSet -= timeZone.getDSTSavings();
                                timeZone.setRawOffset((int) timezoneOffSet);
                                calendar.setTimeZone(timeZone);
                                lockTime = calendar.getTimeInMillis();
                                mExtendedBluetoothDevice.setBatteryCapacity((byte) battery);
                                //todo:
//                                TTLockAPI.getTTLockCallback().onDeadlock(mExtendedBluetoothDevice, battery, uid, (int) (unlockDate / 1000), deviceTime, error);
                                break;
                            }
                            case Command.COMM_CYCLIC_CMD: {
                                switch (data[3]) {
                                    case CyclicOpType.QUERY:
                                        break;
                                    case CyclicOpType.ADD:
                                        addCyclicDataResponse(command);
                                        break;
                                    case CyclicOpType.REMOVE:
                                        break;
                                    case CyclicOpType.CLEAR:
                                        clearCyclicDataResponse(command);
                                        break;
                                }
                                break;
                            }
                            case Command.COMM_NB_ACTIVATE_CONFIGURATION:
                                switch (data[3]) {
                                    case ActionType.SET:
                                        lockCallback = LockCallbackManager.getInstance().getCallback();
                                        switch (data[4]) {
                                            case NBAwakeConfig.ACTION_AWAKE_MODE:
                                                ((SetNBAwakeModesCallback) lockCallback).onSetNBAwakeModesSuccess();
                                                break;
                                            case NBAwakeConfig.ACTION_AWAKE_TIME:
                                                ((SetNBAwakeTimesCallback) lockCallback).onSetNBAwakeTimesSuccess();
                                                break;
                                        }
                                        break;
                                    case ActionType.GET:
                                        lockCallback = LockCallbackManager.getInstance().getCallback();
                                        switch (data[4]) {
                                            case NBAwakeConfig.ACTION_AWAKE_MODE:
                                                ((GetNBAwakeModesCallback) lockCallback).onGetNBAwakeModesSuccess(DataParseUitl.parseNBActivateMode(data[5]));
                                                break;
                                            case NBAwakeConfig.ACTION_AWAKE_TIME:
                                                ((GetNBAwakeTimesCallback) lockCallback).onGetNBAwakeTimesSuccess(DataParseUitl.parseNBActivateConfig(Arrays.copyOfRange(data, 5, data.length)));
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case Command.COMM_ACCESSORY_BATTERY:
                                battery = data[2];
                                AccessoryType accessoryType = AccessoryType.getInstance(data[3]);
                                String accessoryMac = DigitUtil.getMacString(Arrays.copyOfRange(data, 4, 10));
                                long saveDate = DigitUtil.convertTimestampWithTimezoneOffset(Arrays.copyOfRange(data, 10, 16), (int) timezoneOffSet);
                                int accessoryBattery = data[16];
                                lockCallback = LockCallbackManager.getInstance().getCallback();
                                ((GetAccessoryBatteryLevelCallback) lockCallback).onGetAccessoryBatteryLevelSuccess(new AccessoryInfo(accessoryType, accessoryMac, saveDate, accessoryBattery));
                                break;
                            case Command.COMM_DOOR_SENSOR_MANAGE:
                                switch (currentAPICommand) {
                                    case APICommand.OP_ADD_DOOR_SENSOR:
                                        lockCallback = LockCallbackManager.getInstance().getCallback();
                                        ((AddDoorSensorCallback) lockCallback).onAddSuccess();
                                        break;
                                    case APICommand.OP_DELETE_DOOR_SENSOR:
                                        lockCallback = LockCallbackManager.getInstance().getCallback();
                                        ((DeleteDoorSensorCallback) lockCallback).onDeleteSuccess();
                                        break;
                                }
                                break;
                            case Command.COMM_KEY_FOB_MANAG:
                                battery = data[2];
                                switch (data[3]) {
                                    case KeyFobOperationType.GET:

                                        break;
                                    case KeyFobOperationType.ADD_MODIFY:
                                        //有循环操作的都先清空循环配置
                                        CommandUtil_V3.clearKeyfobCyclicPeriod(command, transferData.getKeyFobMac(), aesKeyArray);
                                        break;
                                    case KeyFobOperationType.DELETE:
                                        lockCallback = LockCallbackManager.getInstance().getCallback();
                                        switch (currentAPICommand) {
                                            case APICommand.OP_ADD_KEY_FOB://循环失败的情况
                                                ((AddRemoteCallback) lockCallback).onFail(tmpLockError);
                                                break;
                                            case APICommand.OP_MODIFY_KEY_FOB_PERIOD:
                                                ((ModifyRemoteValidityPeriodCallback) lockCallback).onFail(tmpLockError);
                                                break;
                                            default:
                                                ((DeleteRemoteCallback) lockCallback).onDeleteSuccess();
                                                break;
                                        }
                                        break;
                                    case KeyFobOperationType.CLEAR:
                                        lockCallback = LockCallbackManager.getInstance().getCallback();
                                        ((ClearRemoteCallback) lockCallback).onClearSuccess();

                                }
                                break;
                            case Command.COMM_SENSITIVITY_MANAGE:
                                switch (data[3]) {
                                    case SensitivityOperationType.QUERY:
                                        sensitivity = data[4];
                                        doResponse(data[0], command);
                                        break;
//                                    case SensitivityOperationType.MODIFY:
//                                        TTLockAPI.getTTLockCallback().onSetSensitivity(mExtendedBluetoothDevice, error);
//                                        break;
                                }
                                break;
                            case Command.COMM_SCAN_WIFI:
                                if (data.length == 3) {//wifi 收集成功
                                    lockCallback = LockCallbackManager.getInstance().getCallback();
                                    if (lockCallback != null) {
                                        ((ScanWifiCallback) lockCallback).onScanWifi(wiFis, 1);
                                    }
                                } else {
                                    int wifiLen = data[3];
                                    WiFi wiFi = new WiFi();
                                    wiFi.setSsid(new String(Arrays.copyOfRange(data, 4, 4 + wifiLen)));
                                    wiFi.setRssi(data[4 + wifiLen]);
                                    LogUtil.d("wifi:" + wiFi.getSsid());
                                    insertWifi(wiFi);
                                    lockCallback = LockCallbackManager.getInstance().getCallbackWithoutRemove();
                                    if (lockCallback != null) {
                                        ((ScanWifiCallback) lockCallback).onScanWifi(wiFis, 0);
                                    }
                                }
                                break;
                            case Command.COMM_CONFIG_WIFI_AP:
                                lockCallback = LockCallbackManager.getInstance().getCallback();
                                if (lockCallback != null && lockCallback instanceof ConfigWifiCallback) {
                                    ((ConfigWifiCallback) lockCallback).onConfigWifiSuccess();
                                }
                                break;
                            case Command.COMM_CONFIG_STATIC_IP:
                                lockCallback = LockCallbackManager.getInstance().getCallback();
                                if (lockCallback != null) {
                                    ((ConfigIpCallback) lockCallback).onConfigIpSuccess();
                                }
                                break;
                            case Command.COMM_CONFIG_SERVER:
                                lockCallback = LockCallbackManager.getInstance().getCallback();
                                if (lockCallback != null && lockCallback instanceof ConfigServerCallback) {
                                    ((ConfigServerCallback) lockCallback).onConfigServerSuccess();
                                } else {
                                    LogUtil.w("receive config server callback");
                                }
                                break;
                            case Command.COMM_GET_WIFI_INFO:
                                byte[] wifiMacBytes = Arrays.copyOfRange(data, 3, 3 + 6);
                                DigitUtil.reverseArray(wifiMacBytes);
                                int rssi = data[9];
                                lockCallback = LockCallbackManager.getInstance().getCallback();
                                if (lockCallback != null) {
                                    ((GetWifiInfoCallback) lockCallback).onGetWiFiInfoSuccess(new WifiLockInfo(DigitUtil.getMacString(wifiMacBytes), rssi));
                                }
                                break;
//                            case Command.COMM_DOOR_NOT_CLOSED_WARNING:
//                                TTLockAPI.getTTLockCallback().onSetDoorNotClosedWarningTime(error);
//                                break;
                            default: {//
                                LogUtil.w("invalid command", DBG);
                                break;
                            }
                        }
                    } else {//失败
                        errorResponse(command, data);
                    }
                }
                if(command.getLockType() == LockType.LOCK_TYPE_V3 && !isWaitCommand) {
//            LogUtil.d("start the time", true);
                    //TODO:响应完了 是不是 不应该再计时
                    long delay = 2000;
                    if(currentAPICommand == APICommand.OP_RESET_LOCK || currentAPICommand == APICommand.OP_ADD_FR || currentAPICommand == APICommand.OP_SET_WIFI)//12条指纹
                        delay = 7000;
                    mExtendedBluetoothDevice.disconnectStatus = ExtendedBluetoothDevice.RESPONSE_TIME_OUT;
                    mHandler.postDelayed(disConRunable, delay);
                }

            }
        });
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        readCacheLog();
        mConnectionState = STATE_DISCONNECTED;
        LogUtil.d("dis ble connect", DBG);
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        try {
            mBluetoothGatt.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public synchronized void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    private void commandSendAgain() {

        try {
            LogUtil.d("commandSendAgain:", DBG);
            mNotifyCharacteristic.setValue(dataQueue.poll());
            mBluetoothGatt.writeCharacteristic(mNotifyCharacteristic);
        } catch (Exception e) {
            mConnectionState = STATE_DISCONNECTED;
            sdkLogItem = e.getMessage();
            addSdkLog();
            doConnectFailedCallback();
        }

    }

    //TODO:补全

    /**
     * 错误回调
     * @param error
     */
    @RequiresPermission((Manifest.permission.BLUETOOTH))
    private void errorCallback(final LockError error) {
        error.setLockname(mExtendedBluetoothDevice.getName());
        error.setLockmac(mExtendedBluetoothDevice.getAddress());
        error.setDate(System.currentTimeMillis());
        LogUtil.d("commandSendCount:" + commandSendCount, DBG);
        //TODO:这里返回
        commandSendCount++;
        if (commandSendCount == 1 && (error == LockError.LOCK_CRC_CHECK_ERROR || error == LockError.KEY_INVALID) && cloneDataQueue != null) {
            if (mNotifyCharacteristic != null && mBluetoothGatt != null) {
                dataQueue = cloneDataQueue;
                commandSendAgain();
                return;
            }
        }

        if(mAppExecutor == null){
            mAppExecutor = new AppExecutors();
        }
        mAppExecutor.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                LockCallback mFailCallback = LockCallbackManager.getInstance().getCallback();
                if(mFailCallback != null){
                    setSdkLog(error);
                    mFailCallback.onFail(error);
                }
            }
        });

        LogUtil.d("error" + error, DBG);
    }

    public String generateTransmissionData(int scene,byte[] values, byte validPwdNum) {
        //300个密码
        Set pwdSet = new LinkedHashSet<String>();
        StringBuilder fourKeyboardPwdList = new StringBuilder();
        fourKeyboardPwdList.append('[');
        while(pwdSet.size() < 300) {
            String pwd = DigitUtil.generatePwdByLength(4);
            if(pwdSet.add(pwd)) {
                fourKeyboardPwdList.append(pwd);
                fourKeyboardPwdList.append(",");
            }
        }
        fourKeyboardPwdList.replace(fourKeyboardPwdList.length() - 1, fourKeyboardPwdList.length(), "]");

        Iterator iterator = pwdSet.iterator();
        int pointer = 0;
        while(iterator.hasNext()) {
            String pwd = (String) iterator.next();
            int pwdInt = Integer.valueOf(pwd);
            values[pointer ++] = (byte) pwdInt;
            values[pointer ++] = (byte) (pwdInt >> 8);
        }
        //时间对照表 1000字节
        byte[] timeTable = new byte[1000];
        for(int i=0;i<1000;i++)
            timeTable[i] = (byte) 0xFF;
        StringBuilder timeControlTbBuilder = new StringBuilder();
        timeControlTbBuilder.append('{');
        int pwdType = PWD_TYPE_MAX_DAY_180;
        switch (scene) {
            case 1:
                pwdType = PWD_TYPE_MAX_DAY_180;
                break;
            case 2:
            case 3:
                pwdType = PWD_TYPE_CONTAIN_MONTH;
                break;
        }
        switch (pwdType) {
            case PWD_TYPE_MAX_DAY_180://最多180天
                for(int i=0; i < 218;i++) {
                    while(true) {
                        int random = DigitUtil.generateRandomIntegerByUpperBound(1000);
                        if(timeTable[random] == (byte) 0xFF) {
                            timeTable[random] = (byte) (i < 10 ? 0 : i - 9);//单次的10种 后面顺延
                            if(i == 0) {
                                timeControlTbBuilder.append(0);
                                timeControlTbBuilder.append(':');
                                timeControlTbBuilder.append('[');
                                timeControlTbBuilder.append(String.format("%03d", random));
                            } else if(i >0 && i < 9) {
                                timeControlTbBuilder.append(',');
                                timeControlTbBuilder.append(String.format("%03d", random));
                            } else if(i == 9) {	//单次拼接完成
                                timeControlTbBuilder.append(',');
                                timeControlTbBuilder.append(String.format("%03d", random));
                                timeControlTbBuilder.append(']');
                            } else {		//其它类型
                                timeControlTbBuilder.append(',');
                                timeControlTbBuilder.append(i - 9);
                                timeControlTbBuilder.append(':');
                                timeControlTbBuilder.append(String.format("%03d", random));
                            }
                            break;
                        }
                    }
                }
                break;
            case PWD_TYPE_CONTAIN_MONTH://包含月份 永久
                for(int i=0;i<255;i++) {
                    while(true) {
                        int random = DigitUtil.generateRandomIntegerByUpperBound(1000);
                        if(timeTable[random] == (byte) 0xFF) {
                            timeTable[random] = (byte) (i < 10 ? 0 : i - 9);//单次的10种 后面顺延
                            if(i == 0) {
                                timeControlTbBuilder.append(0);
                                timeControlTbBuilder.append(':');
                                timeControlTbBuilder.append('[');
                                timeControlTbBuilder.append(String.format("%03d", random));
                            } else if(i >0 && i < 9) {
                                timeControlTbBuilder.append(',');
                                timeControlTbBuilder.append(String.format("%03d", random));
                            } else if(i == 9) {	//单次拼接完成
                                timeControlTbBuilder.append(',');
                                timeControlTbBuilder.append(String.format("%03d", random));
                                timeControlTbBuilder.append(']');
                            } else {		//其它类型
                                if(i < 138) {//100天内
                                    timeControlTbBuilder.append(',');
                                    timeControlTbBuilder.append(i - 9);
                                    timeControlTbBuilder.append(':');
                                    timeControlTbBuilder.append(String.format("%03d", random));
                                } else if(i < 233) {//1 ~ 24个月
                                    i = (i == 138 ? 209 : i);
                                    timeControlTbBuilder.append(',');
                                    timeControlTbBuilder.append(i);
                                    timeControlTbBuilder.append(':');
                                    timeControlTbBuilder.append(String.format("%03d", random));
                                } else if(i == 233) {//永久
                                    i = 254;
                                    timeControlTbBuilder.append(',');
                                    timeControlTbBuilder.append(i);
                                    timeControlTbBuilder.append(':');
                                    timeControlTbBuilder.append(String.format("%03d", random));
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            default:
                break;
        }

        timeControlTbBuilder.append('}');

        System.arraycopy(timeTable, 0, values, pointer, 1000);
        pointer += 1000;
        //位置表
        byte[] timePos = new byte[3];	//时间位置表
        Set<Byte> posSet = new TreeSet<Byte>();

        while(posSet.size() < 3) {
            posSet.add((byte) (DigitUtil.generateRandomIntegerByUpperBound(7) + 1));
        }

        StringBuilder positionBuilder = new StringBuilder();
        positionBuilder.append('[');
        Iterator<Byte> posIterator = posSet.iterator();
        for(int i=0;i<3;i++) {
            timePos[i] = posIterator.next();
            positionBuilder.append(timePos[i]);
            positionBuilder.append(',');
        }
        positionBuilder.replace(positionBuilder.length() - 1, positionBuilder.length(), "]");
        System.arraycopy(timePos, 0, values, pointer, 3);
        pointer += 3;
        //校验对照表
        byte[] checkingTable = new byte[10];
        Set<Byte> convertSet = new LinkedHashSet<Byte>();
        while(convertSet.size() < 10) {
            convertSet.add((byte) DigitUtil.generateRandomIntegerByUpperBound(10));
        }
        Iterator<Byte> convertIterator = convertSet.iterator();
        StringBuilder checkDigitBuilder = new StringBuilder();
        for(int i=0;i<10;i++) {
            checkingTable[i] = convertIterator.next();
            checkDigitBuilder.append(checkingTable[i]);
        }
        System.arraycopy(checkingTable, 0, values, pointer, 10);

        values[1613] = (byte) validPwdNum;
        StringBuilder res = new StringBuilder();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("position", positionBuilder);
            jsonObject.put("currentIndex", -1);
            jsonObject.put("timeControlTb", timeControlTbBuilder);
            jsonObject.put("fourKeyboardPwdList", fourKeyboardPwdList);
            jsonObject.put("checkDigit", checkDigitBuilder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pwdInfo = CommandUtil.encry(jsonObject.toString(), timestamp);
        LogUtil.d("values:" + DigitUtil.byteArrayToHexString(Arrays.copyOfRange(values, 0, 1000)), DBG);
        LogUtil.d("values:" + DigitUtil.byteArrayToHexString(Arrays.copyOfRange(values, 1000, 1613)), DBG);
        return jsonObject.toString();
//        return values;
    }

    private String generatePwd(int pwdType) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            String pwd = DigitUtil.generatePwdByType(pwdType);
            pwdList.add(pwd);
            stringBuilder.append(pwd);
        }
        return stringBuilder.toString();
    }

    public boolean isConnected(String address) {
        if(TextUtils.isEmpty(address)){
            return false;
        }

        if(address.equals(mBluetoothDeviceAddress) && mConnectionState == STATE_CONNECTED){
            return true;
        }
        return false;
    }

    public void clearTask() {
        LogUtil.w("clear task", DBG);
        if(mHandler != null)
            mHandler.removeCallbacks(disConRunable);
        if(disTimerTask != null)
            disTimerTask.cancel();
        if(timer != null)
            timer.purge();
    }



    public void stopBTService(){

        stopScan();
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }

        if(disTimerTask != null){
            disTimerTask.cancel();
        }
        isCanSendCommandAgain = true;
        disTimerTask = null;
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = null;
        //TODO:后续调整
        disconnect();
        close();
        try {
            if (mApplicationContext != null && isBtStateReceiverRegistered) {
                mApplicationContext.unregisterReceiver(bluttoothState);
                mApplicationContext = null;
                isBtStateReceiverRegistered = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LockCallbackManager.getInstance().clearAllCallback();
    }

    public int getConnectCnt() {
        return connectCnt;
    }

    public void setConnectCnt(int connectCnt) {
        this.connectCnt = connectCnt;
    }

    public boolean isNeedReCon() {
        return isNeedReCon;
    }

    public void setNeedReCon(boolean needReCon) {
        isNeedReCon = needReCon;
    }

    public boolean isScan() {
        return scan;
    }

    public synchronized void setScan(boolean scan) {
        this.scan = scan;
    }

    public void setScanBongOnly(boolean scanBongOnly) {
        this.scanBongOnly = scanBongOnly;
    }


    /**
     * 在每次close前，先将BluetoothGatt  refresh一下，应该就可以了，这里的refresh只有通过反射的方式去执行，直接上代码：
     * 清理本地的BluetoothGatt 的缓存，以保证在蓝牙连接设备的时候，设备的服务、特征是最新的
     * @param gatt
     * @return
     */
    public boolean refreshDeviceCache(BluetoothGatt gatt) {
        if(null != gatt){
            try {
                BluetoothGatt localBluetoothGatt = gatt;
                Method localMethod = localBluetoothGatt.getClass().getMethod( "refresh", new Class[0]);
                if (localMethod != null) {
                    boolean bool = ((Boolean) localMethod.invoke(
                            localBluetoothGatt, new Object[0])).booleanValue();
                    return bool;
                }
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
        return false;
    }

    public short parseIC(byte[] datas) {

        short nextReq = (short) (datas[0] << 8 | (datas[1] & 0xff));
        int dataIndex = 2;
        while (dataIndex < datas.length) {
            ICCard icCard = new ICCard();
            LogUtil.d("datas.length:" + datas.length);
            int cardLen = 4;
            if (datas.length == 20)
                cardLen = 8;

            LogUtil.d("cardLen:" + cardLen);

            //TODO:8位
            long cardNo = DigitUtil.bytesToLong(Arrays.copyOfRange(datas, dataIndex, dataIndex + cardLen));
            icCard.cardNumber = String.valueOf(cardNo);

            dataIndex += cardLen;

            int year = datas[dataIndex++] + 2000;
            //月
            int month = datas[dataIndex++];
            //日
            int day = datas[dataIndex++];
            //小时
            int hour = datas[dataIndex++];
            //分钟
            int minute = datas[dataIndex++];

            Calendar calendar = Calendar.getInstance();
            //根据时间偏移量计算时间
            TimeZone timeZone = TimeZone.getDefault();
            if (timeZone.inDaylightTime(new Date(System.currentTimeMillis()))){
                timezoneOffSet -= timeZone.getDSTSavings();
            }
            timeZone.setRawOffset((int) timezoneOffSet);
            calendar.setTimeZone(timeZone);

            calendar.set(year, month - 1, day, hour, minute);

            icCard.startDate = calendar.getTimeInMillis();

            year = datas[dataIndex++] + 2000;
            //月
            month = datas[dataIndex++];
            //日
            day = datas[dataIndex++];
            //小时
            hour = datas[dataIndex++];
            //分钟
            minute = datas[dataIndex++];

            calendar.setTimeZone(timeZone);

            calendar.set(year, month - 1, day, hour, minute);

            icCard.endDate = calendar.getTimeInMillis();

            icCards.add(icCard);
        }

        return nextReq;
    }

    public short parseFR(byte[] datas) {

        short nextReq = (short) (datas[0] << 8 | (datas[1] & 0xff));
        int dataIndex = 2;
        while (dataIndex < datas.length) {
            FR fr = new FR();
            long cardNo = DigitUtil.sixBytesToLong(Arrays.copyOfRange(datas, dataIndex, dataIndex + 6));
            fr.fingerprintNumber = String.valueOf(cardNo);

            dataIndex += 6;

            int year = datas[dataIndex++] + 2000;
            //月
            int month = datas[dataIndex++];
            //日
            int day = datas[dataIndex++];
            //小时
            int hour = datas[dataIndex++];
            //分钟
            int minute = datas[dataIndex++];

            Calendar calendar = Calendar.getInstance();
            //根据时间偏移量计算时间
            TimeZone timeZone = TimeZone.getDefault();
            if (timeZone.inDaylightTime(new Date(System.currentTimeMillis()))){
                timezoneOffSet -= timeZone.getDSTSavings();
            }
            timeZone.setRawOffset((int) timezoneOffSet);
            calendar.setTimeZone(timeZone);

            calendar.set(year, month - 1, day, hour, minute);

            fr.startDate = calendar.getTimeInMillis();

            year = datas[dataIndex++] + 2000;
            //月
            month = datas[dataIndex++];
            //日
            day = datas[dataIndex++];
            //小时
            hour = datas[dataIndex++];
            //分钟
            minute = datas[dataIndex++];

            calendar.setTimeZone(timeZone);

            calendar.set(year, month - 1, day, hour, minute);

            fr.endDate = calendar.getTimeInMillis();

            frs.add(fr);
        }

        return nextReq;
    }

    private short parsePasscode(byte[] datas) {
        short nextReq = (short) (datas[0] << 8 | (datas[1] & 0xff));
        int dataIndex = 2;
        while (dataIndex < datas.length) {
            int recordLen = datas[dataIndex++];
            Passcode passcode = new Passcode();
            passcode.keyboardPwdType = datas[dataIndex++];
            int passcodeLen = datas[dataIndex++];
            passcode.newKeyboardPwd = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + passcodeLen));
            dataIndex += passcodeLen;

            passcodeLen = datas[dataIndex++];
            passcode.keyboardPwd = new String(Arrays.copyOfRange(datas, dataIndex, dataIndex + passcodeLen));
            dataIndex += passcodeLen;

            int year = datas[dataIndex++] + 2000;

            //月
            int month = datas[dataIndex++];
            //日
            int day = datas[dataIndex++];
            //小时
            int hour = datas[dataIndex++];
            //分钟
            int minute = datas[dataIndex++];

            LogUtil.d("S year:" + year);
            LogUtil.d("S month:" + month);
            LogUtil.d("S day:" + day);
            LogUtil.d("S hour:" + hour);
            LogUtil.d("S minute:" + minute);

            Calendar calendar = Calendar.getInstance();
            //根据时间偏移量计算时间
            TimeZone timeZone = TimeZone.getDefault();
            if (timeZone.inDaylightTime(new Date(System.currentTimeMillis()))){
                timezoneOffSet -= timeZone.getDSTSavings();
            }
            timeZone.setRawOffset((int) timezoneOffSet);
            calendar.setTimeZone(timeZone);

            calendar.set(year, month - 1, day, hour, minute, 0);

            passcode.startDate = calendar.getTimeInMillis();

            switch (passcode.keyboardPwdType) {
                case 1://永久密码

                    break;
                case 2://单次密码
                case 3://限时密码
                    year = datas[dataIndex++] + 2000;
                    //月
                    month = datas[dataIndex++];
                    //日
                    day = datas[dataIndex++];
                    //小时
                    hour = datas[dataIndex++];
                    //分钟
                    minute = datas[dataIndex++];

                    calendar.setTimeZone(timeZone);

                    LogUtil.d("year:" + year);
                    LogUtil.d("month:" + month);
                    LogUtil.d("day:" + day);

                    calendar.set(year, month - 1, day, hour, minute);
                    passcode.endDate = calendar.getTimeInMillis();
                    break;
                case 4://循环密码
                    passcode.cycleType = (short) (datas[dataIndex++] << 8 | (datas[dataIndex++] & 0xff));
                    break;
                    default:
                        break;
            }

            //TODO:跟服务器一致
            if(passcode.keyboardPwdType == 1)
                passcode.keyboardPwdType = 2;
            else if(passcode.keyboardPwdType == 2)
                passcode.keyboardPwdType = 1;

            passcodes.add(passcode);
        }
        return nextReq;
    }

    private LockData getLockInfoObj() {
//        LockData lockData = new LockData();
        lockData.lockName = mExtendedBluetoothDevice.getName();
        lockData.lockMac = mExtendedBluetoothDevice.getAddress();
        lockData.electricQuantity = mExtendedBluetoothDevice.getBatteryCapacity();
        lockData.adminPwd = DigitUtil.encodeLockData(adminPs);
        lockData.lockKey = DigitUtil.encodeLockData(unlockKey);
        lockData.noKeyPwd = adminPasscode;
        lockData.deletePwd = deletePwd;
        lockData.pwdInfo = pwdInfo;
        lockData.timestamp = timestamp;
        lockData.aesKeyStr = DigitUtil.encodeAesKey(aesKeyArray);
//        lockData.specialValue = feature;
        lockData.modelNum = modelNumber;
        lockData.hardwareRevision = hardwareRevision;
        lockData.firmwareRevision = firmwareRevision;
        if (!TextUtils.isEmpty(factoryDate)) {
            lockData.factoryDate = factoryDate;
        }
        String headRef = lockData.lockMac.substring(lockData.lockMac.length() - 5);
        lockData.ref = DigitUtil.encodeLockData(headRef + lockData.factoryDate);
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.NB_LOCK) && deviceInfo != null) {
            lockData.nbNodeId = deviceInfo.nbNodeId;
            lockData.nbCardNumber = deviceInfo.nbCardNumber;
            lockData.nbRssi = deviceInfo.nbRssi;
            lockData.nbOperator = deviceInfo.nbOperator;
        }

        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.AUTO_LOCK)) {//锁内关闭跟服务端定义的值不一样
            lockData.autoLockTime = lockData.autoLockTime == 0 ? LockDataSwitchValue.AUTO_LOCK_CLOSE : lockData.autoLockTime;
        }

        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.PASSWORD_DISPLAY_OR_HIDE)) {
            lockData.displayPasscode = lockData.displayPasscode != 0 ? LockDataSwitchValue.OPEN : LockDataSwitchValue.CLOSE;
        }

        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.AUDIO_MANAGEMENT)) {
            lockData.lockSound = lockData.lockSound != 0 ? LockDataSwitchValue.OPEN : LockDataSwitchValue.CLOSE;
        }

        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.SENSITIVITY)) {
            lockData.setSensitivity(sensitivity);
        }

        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.INCOMPLETE_PASSCODE)) {
            lockData.passcodeKeyNumber = deviceInfo.passcodeKeyNumber;
        }

        //读取的时候直接放到了lockData里面
//        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.SOUND_VOLUME_AND_LANGUAGE_SETTING)) {
//            lockData.soundVolume = soundVolume;
//        }

        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.RESET_BUTTON)) {
            lockData.resetButton = (switchValue & TTLockConfigType.RESET_BUTTON.getItem()) != 0 ? LockDataSwitchValue.OPEN : LockDataSwitchValue.CLOSE;
        }

        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.TAMPER_ALERT)) {
            lockData.tamperAlert = (switchValue & TTLockConfigType.TAMPER_ALERT.getItem()) != 0 ? LockDataSwitchValue.OPEN : LockDataSwitchValue.CLOSE;
        }

        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.PRIVACY_LOCK)) {
            lockData.privacyLock = (switchValue & TTLockConfigType.PRIVACY_LOCK.getItem()) != 0 ? LockDataSwitchValue.OPEN : LockDataSwitchValue.CLOSE;
        }

        lockData.setSettingValue(settingValue);

        return lockData;
    }


    /**
     *     读取新的操作记录需要返回缓存的数据
     */
    private void readCacheLog() {
        if (currentAPICommand == APICommand.OP_GET_OPERATE_LOG && logOperates != null && logOperates.size() > 0 && transferData != null && transferData.getLogType() == LogType.NEW) {
            returnCacheLog();
        }
    }

    private void returnCacheLog() {
        synchronized (logOperates) {
            if (logOperates != null && logOperates.size() > 0) {
                LogUtil.d("cache log");
//                getTTLockCallback().onGetOperateLog(mExtendedBluetoothDevice, GsonUtil.toJson(logOperates), Error.SUCCESS);
                logOperates.clear();
            }
        }
    }

    public void clearRecordCnt() {
        LogUtil.d("recordCnt:" + recordCnt);
        lastRecordSeq = 0;
        recordCnt = 0;
    }

    public void doWithPassageModeData(byte[] datas) {
        int len = datas.length;
        LogUtil.d("len:" + len);
        int index = 0;
        while (index < len) {
            PassageModeData passageModeData = new PassageModeData();
            passageModeData.type = datas[index++];
            passageModeData.weekOrDay = datas[index++];
            passageModeData.month = datas[index++];

            int startHour = datas[index++];
            int startMinute = datas[index++];
            passageModeData.startDate = startHour * 60 + startMinute;

            int endHour = datas[index++];
            int endMinute = datas[index++];
            passageModeData.endDate = endHour * 60 + endMinute;

            passageModeDatas.add(passageModeData);
        }

    }

    private void initLock(Command command) {
        if (command.getLockType() == LockType.LOCK_TYPE_V3_CAR || command.getLockType() == LockType.LOCK_TYPE_V3) {
            CommandUtil.searchDeviceFeature(command.getLockType());
            //车位锁没有后续指令
        } else if (command.getLockType() == LockType.LOCK_TYPE_CAR) {
            LockData lockData = getLockInfoObj();
            lockData.lockVersion = command.getLockVersion();
            lockData.noKeyPwd = "";
            lockData.deletePwd = "";
            lockData.pwdInfo = "";
            lockData.timestamp = 0;
            lockData.aesKeyStr = "";
            lockData.specialValue = 0;
            LockCallback addLockCallback = LockCallbackManager.getInstance().getCallback();
            if (addLockCallback != null) {
                ((InitLockCallback) addLockCallback).onInitLockSuccess(lockData.encodeLockData());
            }
        } else {
            //接后续指令 全部完成再回调
            adminPasscode = DigitUtil.generatePwdByLength(7);
            CommandUtil.S_setAdminKeyboardPwd(command.getLockType(), adminPasscode, aesKeyArray);
        }
    }

    private void addICResponse(Command command, byte[] data) {
        switch (data[4]) {
            case ICOperate.STATUS_ADD_SUCCESS:
                if (currentAPICommand == APICommand.OP_RECOVERY_DATA) {//循环的恢复

                    switch (recoveryData.cardType) {//todo:恢复卡类型
                        case ValidityInfo.TIMED:
                            dataPos++;
                            if (dataPos < recoveryDatas.size()) {
                                final RecoveryData recoveryData = recoveryDatas.get(dataPos);
                                CommandUtil.recoveryICCardPeriod(command.getLockType(), recoveryData.cardNumber, recoveryData.startDate, recoveryData.endDate, aesKeyArray, timezoneOffSet);
                            } else {
                                LockCallback mRecoveryDataCallback = LockCallbackManager.getInstance().getCallback();
                                if (mRecoveryDataCallback != null) {
                                    ((RecoverLockDataCallback) mRecoveryDataCallback).onRecoveryDataSuccess(transferData.getOp());
                                }
                            }
                            break;
                        case ValidityInfo.CYCLIC:
                            attachmentNum = Long.valueOf(recoveryData.cardNumber);
                            initIcAndFrCyclicData(recoveryData.cyclicConfig);
                            if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
                                CommandUtil_V3.addIcCyclicDate(command, attachmentNum, CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
                            }
                            break;
                    }              } else {//添加ic卡
                    int len = data.length;
                    if (len > 8 && data[len - 1] == (byte) 0xff && data[len - 2] == (byte) 0xff && data[len - 3] == (byte) 0xff && data[len - 4] == (byte) 0xff) {
                        len -= 4;
                    }
                    long cardNo = DigitUtil.bytesToLong(Arrays.copyOfRange(data, 5, len));
                    failNeedDelete = true;
                    //添加完了都走修改有效期指令
                    attachmentNum = cardNo;
                    CommandUtil.modifyICCardPeriod(command.getLockType(), String.valueOf(cardNo), startDate, endDate, aesKeyArray, timezoneOffSet);
                }
                break;
            case ICOperate.STATUS_ENTER_ADD_MODE:
                LogUtil.d("entry into the add mode of ic card", DBG);
                if (currentAPICommand == APICommand.OP_RECOVERY_DATA) {//恢复模式
                    dataPos++;
                    LogUtil.e("dataPos:" + dataPos, DBG);
                    if (dataPos < recoveryDatas.size()) {
                        RecoveryData recoveryData = recoveryDatas.get(dataPos);
                        CommandUtil.recoveryICCardPeriod(command.getLockType(), recoveryData.cardNumber, recoveryData.startDate, recoveryData.endDate, aesKeyArray, timezoneOffSet);
                    } else {
                        LockCallback mRecoveryDataCallback = LockCallbackManager.getInstance().getCallback();
                        if (mRecoveryDataCallback != null) {
                            ((RecoverLockDataCallback) mRecoveryDataCallback).onRecoveryDataSuccess(transferData.getOp());
                        }
                    }
                } else {//添加模式
                    isWaitCommand = true;
                    LockCallback mAddCardCallback = LockCallbackManager.getInstance().getCallbackWithoutRemove();
                    if (mAddCardCallback != null) {
                        ((AddICCardCallback) mAddCardCallback).onEnterAddMode();
                    }
                }
                break;
        }
    }

    private void modifyICPeriodResponse(Command command) {
        switch (currentAPICommand) {
            case APICommand.OP_ADD_IC: {
                switch (transferData.getValidityInfo().getModeType()) {
                    case ValidityInfo.TIMED:
                        LockCallback addIcCardCallback = LockCallbackManager.getInstance().getCallback();
                        if (addIcCardCallback != null) {
                            ((AddICCardCallback) addIcCardCallback).onAddICCardSuccess(attachmentNum);
                        }
                        break;
                    case ValidityInfo.CYCLIC:
                        //todo:失败要删除
                        initIcAndFrCyclicData(transferData.getValidityInfo().getCyclicConfigs());
                        if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
                            CommandUtil_V3.addIcCyclicDate(command, attachmentNum, CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
                        } else {//没有循环数据的当做普通卡添加
                            addIcCardCallback = LockCallbackManager.getInstance().getCallback();
                            if (addIcCardCallback != null) {
                                ((AddICCardCallback) addIcCardCallback).onAddICCardSuccess(attachmentNum);
                            }
                        }
                        break;
                }
                break;
            }
            case APICommand.OP_MODIFY_IC_PERIOD: {
                switch (transferData.getValidityInfo().getModeType()) {
                    case ValidityInfo.TIMED:
                        LockCallback mModifyCallback = LockCallbackManager.getInstance().getCallback();
                        if (mModifyCallback != null) {
                            ((ModifyICCardPeriodCallback) mModifyCallback).onModifyICCardPeriodSuccess();
                        }
                        break;
                    case ValidityInfo.CYCLIC://循环类型要先清空再设置
                        CommandUtil_V3.clearICCyclicPeriod(command, attachmentNum, CyclicOpType.CYCLIC_TYPE_WEEK, aesKeyArray);
                        break;
                }
                break;
            }
        }
    }

    private void addFRResponse(Command command, byte[] data) {
        if (data[4] == 0x01) {//添加成功
            if(currentAPICommand == APICommand.OP_RECOVERY_DATA) {
                switch (recoveryData.fingerprintType) {//todo:恢复卡类型
                    case ValidityInfo.TIMED:
                        dataPos++;
                        if(dataPos < recoveryDatas.size()) {
                            //TODO:暂时延时
                            final RecoveryData recoveryData = recoveryDatas.get(dataPos);
                            CommandUtil.recoveryFRPeriod(command.getLockType(), Long.valueOf(recoveryData.fingerprintNumber), recoveryData.startDate, recoveryData.endDate, aesKeyArray, timezoneOffSet);
                        } else {
                            LockCallback mRecoveryDataCallback = LockCallbackManager.getInstance().getCallback();
                            if(mRecoveryDataCallback != null){
                                ((RecoverLockDataCallback)mRecoveryDataCallback).onRecoveryDataSuccess(transferData.getOp());
                            }
                        }
                        break;
                    case ValidityInfo.CYCLIC:
                        attachmentNum = Long.valueOf(recoveryData.fingerprintNumber);
                        initIcAndFrCyclicData(recoveryData.cyclicConfig);
                        if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
                            CommandUtil_V3.addFrCyclicDate(command, attachmentNum, CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
                        }
                        break;
                }

            } else {//添加指纹
                long FRNo = DigitUtil.sixBytesToLong(Arrays.copyOfRange(data, 5, data.length));
                failNeedDelete = true;
                //添加完了走修改有效期接口
                attachmentNum = FRNo;
                CommandUtil.modifyFRPeriod(command.getLockType(), attachmentNum, startDate, endDate, aesKeyArray, timezoneOffSet);
            }
        } else if (data[4] == 0x02) {//成功启动添加指纹模式，这时候App可以提示“请按手指”
            isWaitCommand = true;

            int totalCnt = -1;
            LogUtil.d("data.length:" + data.length, DBG);
            if(data.length == 6){
                totalCnt = data[5];
            }
            LockCallback mFingerptAddModeCallback = LockCallbackManager.getInstance().getCallbackWithoutRemove();
            if(mFingerptAddModeCallback != null){
                ((AddFingerprintCallback)mFingerptAddModeCallback).onEnterAddMode(totalCnt);
            }
            LogUtil.d("entry into the add mode of fingerprint", DBG);
        } else if (data[4] == 0x03) {//指纹采集进度
            isWaitCommand = true;
            LogUtil.d("the first collection successed，then collect the second", DBG);
            int curCnt = -1;
            int totalCnt = -1;
            if(data.length == 7) {
                curCnt = data[5];
                totalCnt = data[6];
            }
            if(curCnt == 0) {
                LockCallback mPrepareModeAddFR = LockCallbackManager.getInstance().getCallbackWithoutRemove();
                if(mPrepareModeAddFR != null){
                    ((AddFingerprintCallback)mPrepareModeAddFR).onEnterAddMode(totalCnt);
                }
                LogUtil.d("entry into the add mode of fingerprint", DBG);
            } else{
                LockCallback mCollectFR = LockCallbackManager.getInstance().getCallbackWithoutRemove();
                if(mCollectFR != null){
                    ((AddFingerprintCallback)mCollectFR).onCollectFingerprint(curCnt);
                }
            }
        } else if (data[4] == 0x04) {//准备接收指纹模板
            isWaitCommand = true;
            short seq = (short) ((data[5] << 8) | (data[6] & 0xff));
            packetLen = (short)(data[7] << 8 | (data[8] & 0xff));
            CommandUtil_V3.writeFR(command, transferData.getTransferData(), seq, packetLen, transferData.getAesKeyArray());
        }
    }

    private void modifyFrPeriodResponse(Command command) {
        switch (currentAPICommand) {
            case APICommand.OP_ADD_FR:
                switch (transferData.getValidityInfo().getModeType()) {
                    case ValidityInfo.TIMED:
                        LockCallback mAddFingerprint = LockCallbackManager.getInstance().getCallback();
                        if(mAddFingerprint != null){
                            ((AddFingerprintCallback)mAddFingerprint).onAddFingerpintFinished(attachmentNum);
                        }
                        break;
                    case ValidityInfo.CYCLIC:
                        //todo:失败要删除
                        initIcAndFrCyclicData(transferData.getValidityInfo().getCyclicConfigs());
                        if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
                            CommandUtil_V3.addFrCyclicDate(command, attachmentNum, CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
                        } else {//没有循环数据的当做普通指纹添加
                            mAddFingerprint = LockCallbackManager.getInstance().getCallback();
                            if(mAddFingerprint != null){
                                ((AddFingerprintCallback)mAddFingerprint).onAddFingerpintFinished(attachmentNum);
                            }
                        }
                        break;
                }
                break;
            case APICommand.OP_MODIFY_FR_PERIOD:
                switch (transferData.getValidityInfo().getModeType()) {
                    case ValidityInfo.TIMED:
                        LockCallback mModifyFrPeriod = LockCallbackManager.getInstance().getCallback();
                        if(mModifyFrPeriod != null){
                            ((ModifyFingerprintPeriodCallback)mModifyFrPeriod).onModifyPeriodSuccess();
                        }
                        break;
                    case ValidityInfo.CYCLIC://先清空之前的循环有效期
                        CommandUtil_V3.clearFrCyclicPeriod(command, attachmentNum, CyclicOpType.CYCLIC_TYPE_WEEK, aesKeyArray);
                        break;
                }
                break;
        }
    }

    private void addCyclicDataResponse(Command command) {
        cyclicPos++;
        if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
            switch (currentAPICommand) {
                case APICommand.OP_ADD_IC:
                case APICommand.OP_MODIFY_IC_PERIOD:
                    CommandUtil_V3.addIcCyclicDate(command, attachmentNum, CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
                    break;
                case APICommand.OP_ADD_FR:
                case APICommand.OP_MODIFY_FR_PERIOD:
                    CommandUtil_V3.addFrCyclicDate(command, attachmentNum, CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
                    break;
//                case APICommand.OP_ADD_FACE:
//                case APICommand.OP_MODIFY_FACE_PERIOD:
//                    CommandUtil_V3.addFaceCyclicDate(command, No, cyclicConfigs.get(cyclicPos), aesKeyArray);
//                    break;
                case APICommand.OP_ADD_KEY_FOB:
                case APICommand.OP_MODIFY_KEY_FOB_PERIOD:
                    CommandUtil_V3.addKeyFobCyclicDate(command, transferData.getKeyFobMac(), CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
                    break;
                case APICommand.OP_RECOVERY_DATA:
                    switch (transferData.getOp()) {
                        case RecoveryDataType.IC_CARD://CARD
                            CommandUtil_V3.addIcCyclicDate(command, attachmentNum, CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
                            break;
                        case RecoveryDataType.FINGERPRINT://指纹
                            CommandUtil_V3.addFrCyclicDate(command, attachmentNum, CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
                            break;
                    }
                    break;
            }
        } else {
            switch (currentAPICommand) {
                case APICommand.OP_ADD_IC:
                    LockCallback addIcCardCallback = LockCallbackManager.getInstance().getCallback();
                    if (addIcCardCallback != null) {
                        ((AddICCardCallback) addIcCardCallback).onAddICCardSuccess(attachmentNum);
                    }
                    break;
                case APICommand.OP_MODIFY_IC_PERIOD:
                    LockCallback mModifyCallback = LockCallbackManager.getInstance().getCallback();
                    if (mModifyCallback != null) {
                        ((ModifyICCardPeriodCallback) mModifyCallback).onModifyICCardPeriodSuccess();
                    }
                    break;
                case APICommand.OP_ADD_FR:
                    LockCallback mAddFingerprint = LockCallbackManager.getInstance().getCallback();
                    if (mAddFingerprint != null) {
                        ((AddFingerprintCallback) mAddFingerprint).onAddFingerpintFinished(attachmentNum);
                    }
                    break;
                case APICommand.OP_MODIFY_FR_PERIOD:
                    LockCallback mModifyFrPeriod = LockCallbackManager.getInstance().getCallback();
                    if (mModifyFrPeriod != null) {
                        ((ModifyFingerprintPeriodCallback) mModifyFrPeriod).onModifyPeriodSuccess();
                    }
                    break;
//                case APICommand.OP_ADD_FACE:
//                    TTLockAPI.getTTLockCallback().onAddFace(mExtendedBluetoothDevice, No, error);
//                    break;
//                case APICommand.OP_MODIFY_FACE_PERIOD:
//                    TTLockAPI.getTTLockCallback().onModifyFaceValidityPeriod(mExtendedBluetoothDevice, error);
//                    break;
                case APICommand.OP_ADD_KEY_FOB:
                    LockCallback mAddRemote = LockCallbackManager.getInstance().getCallback();
                    if (mAddRemote != null) {
                        ((AddRemoteCallback) mAddRemote).onAddSuccess();
                    }
                    break;
                case APICommand.OP_MODIFY_KEY_FOB_PERIOD:
                    LockCallback mModifyRemote = LockCallbackManager.getInstance().getCallback();
                    if (mModifyRemote != null) {
                        ((ModifyRemoteValidityPeriodCallback) mModifyRemote).onModifySuccess();
                    }
//                    TTLockAPI.getTTLockCallback().onModifyWirelessKeyFobValidityPeriod(mExtendedBluetoothDevice, error);
                    break;
                case APICommand.OP_RECOVERY_DATA://继续恢复
                    dataPos++;
                    if (dataPos < recoveryDatas.size()) {
                        recoveryData = recoveryDatas.get(dataPos);
                        switch (transferData.getOp()) {
                            case 2://CARD
                                CommandUtil.recoveryICCardPeriod(command.getLockType(), recoveryData.cardNumber, recoveryData.startDate, recoveryData.endDate, aesKeyArray, timezoneOffSet);
                                break;
                            case 3://指纹
                                CommandUtil.recoveryFRPeriod(command.getLockType(), Long.valueOf(recoveryData.fingerprintNumber), recoveryData.startDate, recoveryData.endDate, aesKeyArray, timezoneOffSet);
                                break;
                        }
                    } else {
                        LockCallback mRecoveryDataCallback = LockCallbackManager.getInstance().getCallback();
                        if (mRecoveryDataCallback != null) {
                            ((RecoverLockDataCallback) mRecoveryDataCallback).onRecoveryDataSuccess(transferData.getOp());
                        }
                    }
                    break;
            }
        }
    }

    private void clearCyclicDataResponse(Command command) {
        switch (currentAPICommand) {
            case APICommand.OP_MODIFY_IC_PERIOD://清空了之前的设置 开始设置新的循环日期
                initIcAndFrCyclicData(transferData.getValidityInfo().getCyclicConfigs());
                if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
                    CommandUtil_V3.addIcCyclicDate(command, attachmentNum, CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
                } else {//没有循环数据的情况当做修改普通有效期
                    LockCallback mModifyCallback = LockCallbackManager.getInstance().getCallback();
                    if (mModifyCallback != null) {
                        ((ModifyICCardPeriodCallback) mModifyCallback).onModifyICCardPeriodSuccess();
                    }
                }
                break;
            case APICommand.OP_MODIFY_FR_PERIOD:
                initIcAndFrCyclicData(transferData.getValidityInfo().getCyclicConfigs());
                if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
                    CommandUtil_V3.addFrCyclicDate(command, attachmentNum, CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
                } else {//没有循环数据的情况当做修改普通有效期
                    LockCallback mModifyFrPeriod = LockCallbackManager.getInstance().getCallback();
                    if(mModifyFrPeriod != null){
                        ((ModifyFingerprintPeriodCallback)mModifyFrPeriod).onModifyPeriodSuccess();
                    }
                }
                break;
        }
    }

    private void initIcAndFrCyclicData(List<CyclicConfig> cyclicConfigList) {
        cyclicPos = 0;
        cyclicConfigs = cyclicConfigList;
    }

    private void errorResponse(Command command, byte[] data) {
        lockError = LockError.getInstance(data[2]);
        lockError.setCommand(data[0]);
        if (data[2] == LockError.COMMAND_RECEIVED.getIntErrorCode()) {//设置wifi AP的时候表示命令接收成功，正在处理
            //什么都不做 等待后续反馈
        }else if (data[0] == Command.COMM_CYCLIC_CMD && data[2] == LockError.RECORD_NOT_EXIST.getIntErrorCode()) {//循环指令 清空报记录不存在的情况
            //循环记录不存在的情况都当做清楚成功 继续后续操作
            clearCyclicDateSuccessResponse(command);
        } else if (currentAPICommand == APICommand.OP_ADD_ADMIN && data[0] == Command.COMM_SWITCH) {//初始化锁的过程中 不支持读取开关状态的 继续读取其它数据
            doQueryCommand(command);
        } else if (lockError == LockError.IC_CARD_NOT_EXIST && currentAPICommand == APICommand.OP_LOSS_IC) {//挂失卡 卡号不存在的当成功处理
            LockCallback lockCallback = LockCallbackManager.getInstance().getCallback();
            if (lockCallback != null) {
                ((ReportLossCardCallback) lockCallback).onReportLossCardSuccess();
            }
        } else if (currentAPICommand == APICommand.OP_ADD_ADMIN) {
            switch (data[0]) {
                case Command.COMM_TIME_CALIBRATE://添加管理员校准时间失败的情况 继续走添加流程
                    initLock(command);
                    break;
                case Command.COMM_ADD_ADMIN://V指令失败没有错误码
                    lockError = LockError.Failed;
                    errorCallback(lockError);
                    break;
                default://其它情况走正常的错误流程
                    errorCallback(lockError);
                    break;
            }
        } else if (currentAPICommand == APICommand.OP_CALIBRATE_TIME && data[0] == Command.COMM_CHECK_USER_TIME) {//锁时间错误 的情况 直接校准
            CommandUtil.C_calibationTime(command.getLockType(), calibationTime, timezoneOffSet, aesKeyArray);
        } else if ((currentAPICommand == APICommand.OP_ADD_KEY_FOB) && transferData.hasCyclicConfig()) {//循环日期出错 做删除操作
            tmpLockError = lockError;
            CommandUtil_V3.deleteKeyFob(command, transferData, aesKeyArray);
        }
//        else if (currentAPICommand == APICommand.OP_ADD_FACE && failNeedDelete) {
//            tmpError = Error.getInstance(data[2]);
//            failNeedDelete = false;
//            CommandUtil_V3.deleteFace(command, transferData);
//        }
        else if (currentAPICommand == APICommand.OP_ADD_IC && failNeedDelete) {//有效期跟循环日期出错 做删除操作
            tmpLockError = lockError;
            failNeedDelete = false;
            //删除IC卡
            CommandUtil.deleteICCard(command.getLockType(), String.valueOf(attachmentNum), aesKeyArray);
        } else if (currentAPICommand == APICommand.OP_ADD_FR && failNeedDelete) {
            //删除指纹
            tmpLockError = lockError;
            failNeedDelete = false;
            CommandUtil.deleteFR(command.getLockType(), attachmentNum, aesKeyArray);
        } else if (currentAPICommand == APICommand.OP_MODIFY_IC_PERIOD && data[2] == LockError.RECORD_NOT_EXIST.getIntErrorCode()) {//失败当成功处理
            initIcAndFrCyclicData(transferData.getValidityInfo().getCyclicConfigs());
            if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
                CommandUtil_V3.addIcCyclicDate(command, attachmentNum, CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
            }
        } else if (currentAPICommand == APICommand.OP_MODIFY_FR_PERIOD && data[2] == LockError.RECORD_NOT_EXIST.getIntErrorCode()) {//失败当成功处理
            initIcAndFrCyclicData(transferData.getValidityInfo().getCyclicConfigs());
            if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
                CommandUtil_V3.addFrCyclicDate(command, attachmentNum, CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
            }
        } else if (currentAPICommand == APICommand.OP_RECOVERY_DATA && data[2] != 0x16) {//如果是恢复密码的情况 且错误情况下 继续恢复(除了锁空间不足)
            dataPos++;
            if (dataPos < recoveryDatas.size()) {
                final RecoveryData recoveryData = recoveryDatas.get(dataPos);
                switch (transferData.getOp()) {
                    case RecoveryDataType.PASSCODE:
                        CommandUtil.manageKeyboardPassword(command.getLockType(), PwdOperateType.PWD_OPERATE_TYPE_RECOVERY, (byte) (recoveryData.keyboardPwdType == 2 ? 1 : recoveryData.keyboardPwdType), recoveryData.cycleType, recoveryData.keyboardPwd, recoveryData.keyboardPwd, recoveryData.startDate, recoveryData.endDate, aesKeyArray, timezoneOffSet);
                        break;
                    case RecoveryDataType.IC_CARD:
                        CommandUtil.recoveryICCardPeriod(command.getLockType(), recoveryData.cardNumber, recoveryData.startDate, recoveryData.endDate, aesKeyArray, timezoneOffSet);
                        break;
                    case RecoveryDataType.FINGERPRINT:
                        CommandUtil.recoveryFRPeriod(command.getLockType(), Long.valueOf(recoveryData.fingerprintNumber), recoveryData.startDate, recoveryData.endDate, aesKeyArray, timezoneOffSet);
                        break;
                    default:
                        break;
                }

            } else {
                LockCallback mRecoveryDataCallback = LockCallbackManager.getInstance().getCallback();
                if (mRecoveryDataCallback != null) {
                    ((RecoverLockDataCallback) mRecoveryDataCallback).onRecoveryDataSuccess(transferData.getOp());
                }
            }
        } else if (data[0] == Command.COMM_MANAGE_KEYBOARD_PASSWORD && data[3] == PwdOperateType.PWD_OPERATE_TYPE_MODIFY) {//修改密码失败 再判断一下特征值
            tmpLockError = lockError;
            CommandUtil.searchDeviceFeature(command.getLockType());
        } else {
            errorCallback(lockError);
        }
    }


    private void genCommandQue(Command command) {
        commandQueue = new LinkedList<Byte>();

//        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.RESET_BUTTON)
//                || FeatureValueUtil.isSupportFeature(lockData, FeatureValue.TAMPER_ALERT)
//                || FeatureValueUtil.isSupportFeature(lockData, FeatureValue.PRIVACY_LOCK)) {
            commandQueue.add(Command.COMM_SWITCH);//默认都调用获取开关状态接口 不支持失败的继续走其它指令
//        }

        if(FeatureValueUtil.isSupportFeature(lockData, FeatureValue.SENSITIVITY)) {
            commandQueue.add(Command.COMM_SENSITIVITY_MANAGE);
        }

        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.AUDIO_MANAGEMENT)) {
            commandQueue.add(Command.COMM_AUDIO_MANAGE);
        }

        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.PASSWORD_DISPLAY_OR_HIDE)) {
            commandQueue.add(Command.COMM_SHOW_PASSWORD);
        }

        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.AUTO_LOCK)) {
            commandQueue.add(Command.COMM_AUTO_LOCK_MANAGE);
        }

        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.LAMP)) {
            commandQueue.add(Command.COMM_LAMP);
        }

        //TODO:后续调整
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.GET_ADMIN_CODE)) {
            commandQueue.add(Command.COMM_GET_ADMIN_CODE);
        } else if (command.getLockType() == LockType.LOCK_TYPE_V3_CAR) {
            commandQueue.add(Command.COMM_READ_DEVICE_INFO);
//            commandQueue.add(Command.COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED);
        } else if(command.getLockType() == LockType.LOCK_TYPE_V3){//剩下三代锁走设置管理码
            commandQueue.add(Command.COMM_SET_ADMIN_KEYBOARD_PWD);
        }

        doQueryCommand(command);
    }

    private void doResponse(byte comm, Command command) {
        switch (currentAPICommand) {
            case APICommand.OP_ADD_ADMIN:
                doQueryCommand(command);
                break;
            default:
                callback(comm);
                break;
        }
    }

    private void callback(byte comm) {
        LockCallback lockCallback = LockCallbackManager.getInstance().getCallback();
        switch (comm) {
            case Command.COMM_SWITCH:
                TTLockConfigType ttLockConfigType = TTLockConfigType.getInstance(transferData.getOp());
                boolean switchOn = (switchValue & transferData.getOp()) != 0;
                if (TTLockConfigType.WIFI_LOCK_POWER_SAVING_MODE == ttLockConfigType) {//0是省电模式 跟其它开关项正好相反
                    switchOn = !switchOn;
                }
                ((GetLockConfigCallback) lockCallback).onGetLockConfigSuccess(ttLockConfigType, switchOn);
                break;
            case Command.COMM_AUDIO_MANAGE:
                if (lockCallback instanceof GetLockConfigCallback) {
                    ((GetLockConfigCallback) lockCallback).onGetLockConfigSuccess(TTLockConfigType.LOCK_SOUND, transferData.getOpValue() == 1);
                } else if (lockCallback instanceof GetLockMuteModeStateCallback) {//静音模式
                    ((GetLockMuteModeStateCallback) lockCallback).onGetMuteModeStateSuccess(transferData.getOpValue() == 0);
                } else if (lockCallback instanceof GetLockSoundWithSoundVolumeCallback) {
                    ((GetLockSoundWithSoundVolumeCallback) lockCallback).onGetLockSoundSuccess(transferData.getOpValue() == 1, transferData.getSoundVolume());
                } else {
                    LogUtil.d("mMuteModeCallback:" + lockCallback);
                }
                break;
            case Command.COMM_SHOW_PASSWORD:
                if (lockCallback instanceof GetLockConfigCallback) {
                    ((GetLockConfigCallback) lockCallback).onGetLockConfigSuccess(TTLockConfigType.PASSCODE_VISIBLE, lockData.displayPasscode == 1);
                } else if (lockCallback instanceof GetPasscodeVisibleStateCallback) {
                    ((GetPasscodeVisibleStateCallback) lockCallback).onGetPasscodeVisibleStateSuccess(lockData.displayPasscode == 1);
                }
                break;
            case Command.COMM_LAMP:
                ((GetLightTimeCallback) lockCallback).onGetLightTimeSuccess(lockData.lightingTime);
                break;
        }
    }

    /**
     * 获取下一条需要操作的指令
     * @param command
     */
    private void doQueryCommand(Command command) {
        if (commandQueue != null && commandQueue.size() > 0) {
            switch (commandQueue.poll()) {
                case Command.COMM_SWITCH:
                    settingValue = null;
                    CommandUtil_V3.getSwitchState(command, aesKeyArray);
                    break;
                case Command.COMM_AUDIO_MANAGE:
                    CommandUtil_V3.audioManage(command, AudioManage.QUERY, (byte)transferData.getOpValue(), aesKeyArray);
                    break;
                case Command.COMM_SHOW_PASSWORD:
                    CommandUtil.screenPasscodeManage(command.getLockType(), OperationType.GET_STATE, aesKeyArray);
                    break;
                case Command.COMM_AUTO_LOCK_MANAGE:
                    CommandUtil.searchAutoLockTime(command.getLockType(), aesKeyArray);
                    break;
                case Command.COMM_LAMP:
                    CommandUtil_V3.controlLamp(command, OperationType.GET_STATE, (short) transferData.getOpValue(), aesKeyArray);
                    break;
                case Command.COMM_GET_ADMIN_CODE:
                    CommandUtil_V3.getAdminCode(command);
                    break;
                case Command.COMM_READ_DEVICE_INFO:
                    readModelNumber(command);
                    break;
                case Command.COMM_GET_ALARM_ERRCORD_OR_OPERATION_FINISHED:
                    CommandUtil.operateFinished(command.getLockType());
                    break;
                case Command.COMM_SET_ADMIN_KEYBOARD_PWD:
                    adminPasscode = DigitUtil.generatePwdByLength(7);
                    CommandUtil.S_setAdminKeyboardPwd(command.getLockType(), adminPasscode, aesKeyArray);
                    break;
                case Command.COMM_SENSITIVITY_MANAGE:
                    CommandUtil_V3.getSensitivity(command, aesKeyArray);
                    break;
            }
        }
    }

    private void addSdkLog() {
        addSdkLog(sdkLogItem);
    }

    public void addSdkLog(String log) {
        if (sdkLogBuilder != null) {
            sdkLogBuilder.append(log);
            sdkLogBuilder.append('\n');
        }
        LogUtil.d(log, DBG);
    }

    private void doConnectFailedCallback() {
        ConnectCallback mConnectCallback = LockCallbackManager.getInstance().getConnectCallback();
        if(mConnectCallback != null){
            lockError = LockError.LOCK_CONNECT_FAIL;
            setSdkLog();
            mConnectCallback.onFail(lockError);
        }
    }

    private void clearSdkLog() {
        if (sdkLogBuilder != null) {
            sdkLogBuilder.setLength(0);
        }
    }

    private void setSdkLog() {
        setSdkLog(lockError);
    }

    private void setSdkLog(LockError lockError) {
        if (lockError != null) {
            lockError.setSdkLog(getSdkLog());
        }
    }

    public String getSdkLog() {
        if (sdkLogBuilder != null && sdkLogBuilder.length() > 0) {
            return sdkLogBuilder.toString();
        }
        return "null log";
    }

    public void clearWifi() {
        wiFis.clear();
    }

    private synchronized void insertWifi(WiFi newWifi) {
        boolean hasInsert = false;
        for (int i=0;i<wiFis.size();i++) {
            WiFi wiFi = wiFis.get(i);
            if (newWifi.ssid.equals(wiFi.ssid)) {//信号值排序要改
                if (newWifi.rssi > wiFi.rssi) {
                    wiFi.setRssi(newWifi.getRssi());
                }
                hasInsert = true;
                break;
            }
        }
        if (!hasInsert) {
            wiFis.add(newWifi);
        }
    }

    /**
     * 有循环操作的都需要先清空循环时间
     * @param command
     */
    private void clearCyclicDateSuccessResponse(Command command) {
        ValidityInfo validityInfo = transferData.getValidityInfo();
        switch (currentAPICommand) {
//            case APICommand.OP_MODIFY_CYCLIC_IC_PERIOD://清空了之前的设置 开始设置新的循环日期
//                initCyclicData(validityInfo.cyclicConfig);
//                if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
//                    CommandUtil_V3.addIcCyclicDate(command, No, LoopOpType.LOOP_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
//                }
//                break;
//            case APICommand.OP_MODIFY_CYCLIC_FR_PERIOD:
//                initCyclicData(validityInfo.cyclicConfig);
//                if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
//                    CommandUtil_V3.addFrCyclicDate(command, No, LoopOpType.LOOP_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
//                }
//                break;
            case APICommand.OP_ADD_KEY_FOB:
                if (transferData.hasCyclicConfig()) {//循环配置
                    initIcAndFrCyclicData(validityInfo.getCyclicConfigs());
                    if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
                        CommandUtil_V3.addKeyFobCyclicDate(command, transferData.getKeyFobMac(), CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
                    }
                } else {//普通类型
                    LockCallback lockCallback = LockCallbackManager.getInstance().getCallback();
                    ((AddRemoteCallback) lockCallback).onAddSuccess();
                }
                break;
            case APICommand.OP_MODIFY_KEY_FOB_PERIOD:
                if (transferData.hasCyclicConfig()) {//循环配置
                    initIcAndFrCyclicData(validityInfo.getCyclicConfigs());
                    if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
                        CommandUtil_V3.addKeyFobCyclicDate(command, transferData.getKeyFobMac(), CyclicOpType.CYCLIC_TYPE_WEEK, cyclicConfigs.get(cyclicPos), aesKeyArray);
                    }
                } else {//普通类型
                    LockCallback lockCallback = LockCallbackManager.getInstance().getCallback();
                    ((ModifyRemoteValidityPeriodCallback) lockCallback).onModifySuccess();
                }
                break;
//            case APICommand.OP_ADD_FACE:
//                //循环有效期的继续走添加循环时间 普通的直接操作成功
//                switch (validityInfo.type) {//普通永久
//                    case ValidityInfo.NORMAL:
//                        TTLockAPI.getTTLockCallback().onAddFace(mExtendedBluetoothDevice, No, error);
//                        break;
//                    case ValidityInfo.CYCLIC://循环类型
//                        try {
//                            failNeedDelete = true;
//                            initCyclicData(validityInfo.cyclicConfig);
//                            if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
//                                CommandUtil_V3.addFaceCyclicDate(command, No, cyclicConfigs.get(cyclicPos), aesKeyArray);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                }
//                break;
//            case APICommand.OP_MODIFY_FACE_PERIOD:
//                switch (validityInfo.type) {
//                    case ValidityInfo.NORMAL:
//                        TTLockAPI.getTTLockCallback().onModifyFaceValidityPeriod(mExtendedBluetoothDevice, error);
//                        break;
//                    case ValidityInfo.CYCLIC://循环类型
//                        initCyclicData(validityInfo.cyclicConfig);
//                        if (cyclicConfigs != null && cyclicPos < cyclicConfigs.size()) {
//                            CommandUtil_V3.addFaceCyclicDate(command, transferData.getNo(), cyclicConfigs.get(cyclicPos), aesKeyArray);
//                        }
//                        break;
//                }
//                break;
        }
    }

    private void readModelNumber(Command command) {
        //读取版本信息
        tempOptype = DeviceInfoType.MODEL_NUMBER;
        CommandUtil.readDeviceInfo(command.getLockType(), DeviceInfoType.MODEL_NUMBER, aesKeyArray);
        mExtendedBluetoothDevice.disconnectStatus = ExtendedBluetoothDevice.RESPONSE_TIME_OUT;
        mHandler.postDelayed(disConRunable, 1500);
    }

}
