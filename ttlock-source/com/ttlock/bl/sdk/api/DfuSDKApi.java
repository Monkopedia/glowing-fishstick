package com.ttlock.bl.sdk.api;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.ttlock.bl.sdk.callback.DfuCallback;
import com.ttlock.bl.sdk.callback.EnterDfuModeCallback;
import com.ttlock.bl.sdk.callback.GetLockSystemInfoCallback;
import com.ttlock.bl.sdk.callback.GetOperationLogCallback;
import com.ttlock.bl.sdk.callback.RecoverLockDataCallback;
import com.ttlock.bl.sdk.callback.ScanLockCallback;
import com.ttlock.bl.sdk.callback.SetLockTimeCallback;
import com.ttlock.bl.sdk.constant.FeatureValue;
import com.ttlock.bl.sdk.constant.LogType;
import com.ttlock.bl.sdk.entity.DeviceInfo;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.entity.LockUpdateInfo;
import com.ttlock.bl.sdk.entity.RecoveryDataType;
import com.ttlock.bl.sdk.entity.ServerError;
import com.ttlock.bl.sdk.net.ResponseService;
import com.ttlock.bl.sdk.service.DfuService;
import com.ttlock.bl.sdk.service.ThreadPool;
import com.ttlock.bl.sdk.telink.ble.Device;
import com.ttlock.bl.sdk.telink.util.TelinkLog;
import com.ttlock.bl.sdk.util.AESUtil;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.FeatureValueUtil;
import com.ttlock.bl.sdk.util.GsonUtil;
import com.ttlock.bl.sdk.util.IOUtil;
import com.ttlock.bl.sdk.util.LogUtil;
import com.ttlock.bl.sdk.util.NetworkUtil;
import com.ttlock.file.FileProviderPath;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.UUID;

import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

/**
 * Created by TTLock on 2017/8/16.
 * todo:锁一直处于升级模式 直接进行升级
 */

class DfuSDKApi {

    private boolean DBG = true;
    private Context mContext;

    private String clientId;
    private String accessToken;
    private int lockid;
    private String lockData;
    String lockmac;


    private String pwdJson;
    private String ICJson;
    private String FRJson;

    private String operateLog;
//    private int feature;
    private DeviceInfo deviceInfo;
    private Device telinkDevice;
//    private boolean telinkDFUSuccess = false;

    private GetLockSystemInfoCallback lockSystemInfoCallback;

    private DfuCallback dfuCallback;

    /**
     * the status of upgrading
     */
    private int upgradeStatus;

    public static final int GetDeviceInfo = -1;
    public static final  int UpgradeOprationPreparing  = 1;             //preparing
    public static final  int UpgradeOprationUpgrading = 2;              //upgrading
    public static final  int UpgradeOprationRecovering = 3;             //recoverying

    /**
     * inner used status
     */
    public static final  int UpgradeOprationSuccess = 4;                //upgrade success

    public static final int UploadOperateLog = 5;
    public static final int Download = 6;
    public static final int GetData = 7;//获取密码 CARD FR数据
    public static final int EnterDfu = 8;//指令进入dfu
    public static final int InformServerSuccess = 9;
//    public static final int SET_LOCK_TIME = 10;//校准时间过程


    public static final int DfuFailed = 1;//固件升级失败
    public static final int BLECommunicationError = 3;//蓝牙通信错误
    public static final int RequestError = 4;//服务器请求错误
    public static final int NetError = 5;//网络错误
    public static final int FINE_LOCATION_PERMISSION_NOT_GRANTED = 6;//位置权限未授权

    private static String DFU_FAILED = "dfu failed";
    private static String NET_UNABLE = "network unavailable";
    private static String GRANT_FINE_LOCATION_PERMISSION = "please grant fine location permission";

    private Handler handler;

    private int attemptTime = 0;

    private Boolean isDfuMode;

    private Runnable timeOutRunnable = new Runnable() {
        @Override
        public void run() {
            //超时之后不终止
            LogUtil.w("enter DFU time out", DBG);
            TTLockClient.getDefault().stopScanLock();
            errorCallback(DfuFailed, DFU_FAILED);
        }
    };

    private boolean telinkDfuDisconnectFailureCallback = true;

    /**
     * 固件烧录状态回调
     */
    private DfuProgressListener mDfuProgressListener = new DfuProgressListenerAdapter() {
        @Override
        public void onDeviceConnecting(final String deviceAddress) {
            LogUtil.d("deviceAddress:" + deviceAddress, DBG);
        }

        @Override
        public void onDfuProcessStarting(final String deviceAddress) {
            LogUtil.d("deviceAddress:" + deviceAddress, DBG);
        }

        @Override
        public void onEnablingDfuMode(final String deviceAddress) {
            handler.removeCallbacks(timeOutRunnable);
            LogUtil.d("deviceAddress:" + deviceAddress, DBG);
        }

        @Override
        public void onFirmwareValidating(final String deviceAddress) {
            LogUtil.d("deviceAddress:" + deviceAddress, DBG);
        }

        @Override
        public void onDeviceDisconnecting(final String deviceAddress) {
            LogUtil.d("deviceAddress:" + deviceAddress, DBG);
        }

        @Override
        public void onDfuCompleted(final String deviceAddress) {
            LogUtil.d("deviceAddress:" + deviceAddress, DBG);

            DfuServiceListenerHelper.unregisterProgressListener(mContext, mDfuProgressListener);

            if (TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(clientId))//只做固件升级部分 不做网络请求
                successCallback();
            else {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        attemptTime = 1;
                        recoveryData();
                    }
                }, 4500);
            }

        }

        @Override
        public void onDfuAborted(final String deviceAddress) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dfuCallback.onDfuAborted(deviceAddress);
                }
            });
            attemptTime = 0;
            LogUtil.d("deviceAddress:" + deviceAddress, DBG);
        }

        @Override
        public void onProgressChanged(final String deviceAddress, final int percent, final float speed, final float avgSpeed, final int currentPart, final int partsTotal) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dfuCallback.onProgressChanged(deviceAddress, percent, speed, avgSpeed, currentPart, partsTotal);
                }
            });
        }

        @Override
        public void onError(final String deviceAddress, final int error, final int errorType, final String message) {
            LogUtil.d("deviceAddress:" + deviceAddress, DBG);
            handler.removeCallbacks(timeOutRunnable);
            errorCallback(DfuFailed, message);
        }
    };

    private void errorCallback(final int errorcode, final String errmsg) {
        telinkDfuDisconnectFailureCallback = false;
        handler.post(new Runnable() {
            @Override
            public void run() {
                dfuCallback.onError(errorcode, errmsg);
            }
        });
    }

    /**
     * 中断升级
     */
    public void abortUpgradeProcess() {
        LogUtil.d("exit dfu mode", DBG);
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.TELINK_CHIP)) {
            if (telinkDevice != null) {
                telinkDevice.disconnect();
            }
        } else {
            final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
            final Intent pauseAction = new Intent(DfuService.BROADCAST_ACTION);
            pauseAction.putExtra(DfuService.EXTRA_ACTION, DfuService.ACTION_ABORT);
            manager.sendBroadcast(pauseAction);
        }
    }

    /**
     * 锁固件升级包名
     */
    public final static String UPDATE_FILE_NAME = "realUpdate.zip";
    private String mUpdateFilePath;

    public DfuSDKApi() {
        handler = new Handler(Looper.getMainLooper());
    }

    private boolean canRetry() {
        LogUtil.d("accessToken:" + accessToken);
        LogUtil.d("clientId:" + clientId);
        if (!TextUtils.isEmpty(accessToken) && !TextUtils.isEmpty(clientId))
            return true;
        LogUtil.w("please call startDfu method first");
        return false;
    }

    public void retry() {
        if (!canRetry())
            return;
        LogUtil.d("retry:" + upgradeStatus, DBG);
        attemptTime++;
        switch (upgradeStatus) {
//            case SET_LOCK_TIME:
//                setLockTime();
//                break;
            case GetDeviceInfo:
                getLockSystemInfo(lockData, lockmac, lockSystemInfoCallback);
                break;
            case UpgradeOprationPreparing:
                getOperationLog(lockData, lockmac);
                break;
            case UpgradeOprationUpgrading:
                enableLockDfuMode();
                break;
            case EnterDfu:
                enableLockDfuMode();
                break;
            case UpgradeOprationRecovering:
                recoveryData();
                break;
            case UploadOperateLog:
                uploadOperateLog(operateLog);
                break;
            case Download:
                getDownloadUrl();
                break;
            case GetData:
                getRecoverData();
                break;
            case InformServerSuccess:
                upgradeSuccess();
                break;
        }
    }

    /**
     * 开始烧录固件
     */
    private void startDfu() {
        upgradeStatus = UpgradeOprationUpgrading;
        statusCallback(UpgradeOprationUpgrading);
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.TELINK_CHIP)) {
            telinkDfu();
        } else {
            nordicDfu();
        }
    }

    private void nordicDfu() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("start dfu", DBG);
                final DfuServiceInitiator starter = new DfuServiceInitiator(lockmac)
                        .setForeground(false)
                        .setDisableNotification(true)
                        .setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true)
//                        .setDeviceName(mDoorkey.getLockName())
                        .setForceDfu(true)
                        .setPacketsReceiptNotificationsEnabled(true)
                        .setPrepareDataObjectDelay(400);
                starter.setZip(FileProviderPath.getUriForFile(mContext, new File(mUpdateFilePath)), mUpdateFilePath);
                starter.start(mContext, DfuService.class);
                //一分钟的超时
                handler.postDelayed(timeOutRunnable, 60000);
            }
        }, 3500);
    }

    public void getLockSystemInfo(String lockData, String lockMac, GetLockSystemInfoCallback callback){
        this.lockSystemInfoCallback = callback;
        TTLockClient.getDefault().getLockSystemInfo(lockData, lockMac, callback);
    }

    public void startDfu(Context context, String clientId, String accessToken, int lockid, String lockData, String lockMac, DfuCallback dfuCallback) {

        this.clientId = clientId;
        this.accessToken = accessToken;
        this.lockid = lockid;
        this.lockmac = lockMac;
        this.dfuCallback = dfuCallback;
        this.lockData = lockData;
        this.mContext = context;

        LogUtil.d("accessToken:" + accessToken);
        LogUtil.d("clientId:" + clientId);

        getDownloadUrl();

//        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.TELINK_CHIP)) {
//            getDownloadUrl();
//        } else {
//            attemptTime = 1;
//            DfuServiceListenerHelper.registerProgressListener(context, mDfuProgressListener);
//
//            if (NetworkUtil.isNetConnected(context)) {
//                setLockTime();
////            getOperationLog(lockData, lockmac);
//            } else {
//                errorCallback(NetError, NET_UNABLE);
//            }
//        }
    }

    public void startDfu(Context context, String lockData, String lockMac, String firmwarePackage, DfuCallback dfuCallback) {
        this.lockData = lockData;
        this.mContext = context;
        this.lockmac = lockMac;
        this.dfuCallback = dfuCallback;
        DfuServiceListenerHelper.registerProgressListener(context, mDfuProgressListener);
        if (!TextUtils.isEmpty(firmwarePackage))
            firmwarePackage = DigitUtil.decodeLockData(firmwarePackage);
        if(NetworkUtil.isNetConnected(context)) {
            if (!TextUtils.isEmpty(firmwarePackage)) {
                LockUpdateInfo lockUpdateInfo = GsonUtil.toObject(firmwarePackage, LockUpdateInfo.class);
                if (lockUpdateInfo.getDecryptionKey() != null)
                    lockUpdateInfo.setDecryptionKey(DigitUtil.encodeLockData(lockUpdateInfo.getDecryptionKey()));
                downloadUpdatePackage(lockUpdateInfo, lockUpdateInfo.getUrl());
            }
        } else {
            errorCallback(NetError, NET_UNABLE);
        }
    }

    private void getOperationLog(String lockData, String lockMac) {
        /**
         * 准备中
         */
        upgradeStatus = UpgradeOprationPreparing;
        statusCallback(UpgradeOprationPreparing);

        //TODO:读全部还是只读新的
        TTLockClient.getDefault().getOperationLog(LogType.NEW, lockData, lockMac, new GetOperationLogCallback() {
            @Override
            public void onGetLogSuccess(String log) {
                operateLog = log;
                uploadOperateLog(log);
            }

            @Override
            public void onFail(LockError error) {
                errorCallback(BLECommunicationError, error.getErrorMsg());
            }
        });
    }

    private void recoveryData() {
        upgradeStatus = UpgradeOprationRecovering;
        statusCallback(UpgradeOprationRecovering);
        if(!dataIsEmpty(pwdJson)) {
            recoveryDataByBle(1, pwdJson);
        } else {
            if(!dataIsEmpty(ICJson)) {
                recoveryDataByBle(2, ICJson);
            } else {
                if(!dataIsEmpty(FRJson)) {
                    recoveryDataByBle(3, FRJson);
                } else {//升级成功不需要恢复
                    getFeature();
                }
            }
        }
    }

    private void statusCallback(final int status) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                dfuCallback.onStatusChanged(status);
            }
        });
    }

    private void getFeature() {
//        TTLockClient.getDefault().getSpecialValue(lockData, lockmac, new GetSpecialValueCallback() {
//            @Override
//            public void onGetSpecialValueSuccess(int specialValue) {
//                feature = specialValue;
//                upgradeSuccess();
//            }
//
//            @Override
//            public void onFail(LockError error) {
//
//            }
//        });
        TTLockClient.getDefault().getLockSystemInfo(lockData, null, new GetLockSystemInfoCallback() {
            @Override
            public void onGetLockSystemInfoSuccess(DeviceInfo info) {
                deviceInfo = info;
                upgradeSuccess();
            }

            @Override
            public void onFail(LockError error) {
                errorCallback(BLECommunicationError, error.getErrorMsg());
            }
        });
    }

    private void setLockTime() {//校准时间放到准备过程中 失败之后不影响升级 继续走后续的获取操作记录
        upgradeStatus = UpgradeOprationPreparing;
        TTLockClient.getDefault().setLockTime(System.currentTimeMillis(), lockData, lockmac, new SetLockTimeCallback() {
            @Override
            public void onSetTimeSuccess() {
                getOperationLog(lockData, lockmac);
//                upgradeSuccess();
            }

            @Override
            public void onFail(LockError error) {
                errorCallback(BLECommunicationError, error.getErrorMsg());
            }
        });
    }

    private void upgradeSuccess() {
        upgradeStatus = InformServerSuccess;
        ThreadPool.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String json = ResponseService.lockUpgradeSuccess(clientId, accessToken, lockid, deviceInfo.lockData);
                if(TextUtils.isEmpty(json)) {
                    errorCallback(NetError, NET_UNABLE);
                } else {
                    ServerError error = GsonUtil.toObject(json, ServerError.class);
                    if (error != null) {
                        if (error.errcode == 0) {
                            successCallback();
                        } else {
                            errorCallback(RequestError, error.errmsg);
                        }
                    } else {
                        errorCallback(RequestError, json);
                    }
                }
            }
        });
    }

    private void successCallback() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                upgradeStatus = UpgradeOprationSuccess;
                dfuCallback.onDfuSuccess(lockmac);
            }
        });
    }

    private void recoveryDataByBle(int op, String json) {
        LogUtil.d("recovery data", DBG);
        upgradeStatus = UpgradeOprationRecovering;

        TTLockClient.getDefault().recoverLockData(json, op, lockData, lockmac, new RecoverLockDataCallback() {
            @Override
            public void onRecoveryDataSuccess(int dataType) {
                switch (dataType) {
                    case RecoveryDataType.PASSCODE:
                        pwdJson = null;
                        recoveryData();
                        break;
                    case RecoveryDataType.IC:
                        ICJson = null;
                        recoveryData();
                        break;
                    case RecoveryDataType.FINGERPRINT:
                        FRJson = null;
                        getFeature();
                        break;
                }
            }

            @Override
            public void onFail(LockError error) {
                errorCallback(BLECommunicationError, error.getErrorMsg());
            }
        });
    }

    private void getRecoverData() {
        upgradeStatus = GetData;
        ThreadPool.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String json = ResponseService.getRecoverData(clientId, accessToken, lockid);
                LogUtil.d("json:" + json, true);
                if(TextUtils.isEmpty(json)) {
                    errorCallback(NetError, NET_UNABLE);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.has("errcode")) {
                                errorCallback(RequestError, jsonObject.getString("errmsg"));
                            } else {
                                pwdJson = jsonObject.getString("keyboardPwdList");
                                ICJson = jsonObject.getString("identityCardList");
                                FRJson = jsonObject.getString("fingerprintList");
                                //流程变了 获取恢复数据后 直接进入升级模式 进行升级
                                enableLockDfuMode();
//                                getDownloadUrl();
                            }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorCallback(RequestError, e.getMessage());
                    }
                }

            }
        });
    }

    private void getDownloadUrl() {
        upgradeStatus = Download;
        ThreadPool.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String json = ResponseService.getUpgradePackage(clientId, accessToken, lockid);
                if(TextUtils.isEmpty(json)) {
                    errorCallback(NetError, NET_UNABLE);
                } else {
                    LockUpdateInfo lockUpdateInfo = GsonUtil.toObject(json, new TypeToken<LockUpdateInfo>(){});
                    if (lockUpdateInfo != null) {
                        if (lockUpdateInfo.errcode == 0) {
                            downloadUpdatePackage(lockUpdateInfo, lockUpdateInfo.getUrl());
                        } else {
                            errorCallback(RequestError, lockUpdateInfo.errmsg);
                        }
                    } else {
                        errorCallback(RequestError, json);
                    }
                }
            }
        });
    }

    private void uploadOperateLog(final String records) {
        upgradeStatus = UploadOperateLog;
        ThreadPool.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String json = ResponseService.uploadOperateLog(clientId, accessToken, lockid, records);
                if(TextUtils.isEmpty(json)) {
                    errorCallback(NetError, NET_UNABLE);
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        LogUtil.e("json:" + json, true);
                        int errcode = jsonObject.getInt("errcode");
                        if(errcode == 0) {
                            //获取恢复数据
                            getRecoverData();
                        } else {
                            errorCallback(RequestError, jsonObject.getString("errmsg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorCallback(RequestError, e.getMessage());
                    }
                }
            }
        });
    }

    private void downloadUpdatePackage(final LockUpdateInfo lockUpdateInfo, final String fileUrl) {
        ThreadPool.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                InputStream is = null;
                try {
                    URL url = new URL(fileUrl);
                    URLConnection conn = url.openConnection();
                    is = conn.getInputStream();
                    int fileLen = conn.getContentLength();

                    //TODO:一次性读完
                    os = new ByteArrayOutputStream(fileLen);
                    //创建字节流
                    byte[] bs = new byte[1024];
                    int len;
                    //写数据
                    while ((len = is.read(bs)) != -1) {
                        os.write(bs, 0, len);
                    }
                    byte[] source = ((ByteArrayOutputStream) os).toByteArray();

                    byte[] decryptedBytes = AESUtil.aesDecrypt(source, DigitUtil.decodeLockData(lockUpdateInfo.getDecryptionKey()).getBytes());
                    mUpdateFilePath = mContext.getCacheDir().getAbsolutePath() + File.separator + UPDATE_FILE_NAME;
//                    LogUtil.d("mUpdateFilePath:" + mUpdateFilePath, DBG);
                    os = new FileOutputStream(mUpdateFilePath);
                    if(decryptedBytes != null)
                        os.write(decryptedBytes);
                    //完成后关闭流
                    os.close();
                    is.close();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            startScan();
//                            enableLockDfuMode();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    errorCallback(NetError, e.getMessage());
                } finally {

                }
            }
        });
    }

    private void startScan() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            errorCallback(FINE_LOCATION_PERMISSION_NOT_GRANTED, GRANT_FINE_LOCATION_PERMISSION);
            return;
        }
        isDfuMode = false;
        handler.postDelayed(timeOutRunnable, 10000);
        TTLockClient.getDefault().startScanLock(new ScanLockCallback() {
            @Override
            public void onScanLockSuccess(ExtendedBluetoothDevice device) {
                if (device.getAddress().equals(lockmac)) {
                    synchronized (isDfuMode) {
                        if (isDfuMode) {
                            return;
                        }
                        handler.removeCallbacks(timeOutRunnable);
                        TTLockClient.getDefault().stopScanLock();
                        if (device.isDfuMode()) {
                            isDfuMode = true;
                            startDfu();
                        } else {
                            doWithAfterDownloadFirmware();
//                        enableLockDfuMode();
                        }
                    }
                }
            }

            @Override
            public void onFail(LockError error) {

            }
        });
    }

    private void doWithAfterDownloadFirmware() {

        if (TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(clientId)) {//只做固件升级部分 不做网络请求
            enableLockDfuMode();
            return;
        }


        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.TELINK_CHIP)) {
            enableLockDfuMode();
        } else {
            attemptTime = 1;
            DfuServiceListenerHelper.registerProgressListener(mContext, mDfuProgressListener);

            if (NetworkUtil.isNetConnected(mContext)) {
                setLockTime();
//            getOperationLog(lockData, lockmac);
            } else {
                errorCallback(NetError, NET_UNABLE);
            }
        }
    }

//    private Device.DeviceStateCallback deviceCallback = new Device.DeviceStateCallback() {
//        @Override
//        public void onConnected(Device device) {
//            TelinkLog.w(TAG + " # onConnected");
//
////            mConnectState = BluetoothGatt.STATE_CONNECTED;
////            runOnUiThread(new Runnable() {
////                @Override
////                public void run() {
////                    CommonUtils.showLongMessage("device connected");
////                }
////            });
//        }
//
//        @Override
//        public void onDisconnected(Device device) {
//            TelinkLog.w(TAG + " # onDisconnected");
//            runOnUiThread(new Runnable() {//todo:失败
//                @Override
//                public void run() {
//                    if (!telinkDFUSuccess) {
//                        onFailure();
//                    }
//                }
//            });
//        }
//
//        @Override
//        public void onServicesDiscovered(Device device, final List<BluetoothGattService> services) {
//            TelinkLog.w(TAG + " # onServicesDiscovered");
//            UUID serviceUUID = null;
//            for (BluetoothGattService service: services){
//                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()){
//                    if (characteristic.getUuid().equals(Device.CHARACTERISTIC_UUID_WRITE)){
//                        serviceUUID = service.getUuid();
//                        break;
//                    }
//                }
//            }
//
//            if (serviceUUID != null){
//                device.SERVICE_UUID = serviceUUID;
//            }
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    byte[] firmware = AppUtil.readFirmware(mUpdateFilePath);
//                    if (firmware == null) {
//                        CommonUtils.showLongMessage(R.string.firmware_invalid);
//                        return;
//                    }
//                    mDevice.startOta(firmware);
//                }
//            });
//        }
//
//        @Override
//        public void onOtaStateChanged(Device device, int state) {
//            TelinkLog.w(TAG + " # onOtaStateChanged");
//            switch (state) {
//                case Device.STATE_PROGRESS:
//                    TelinkLog.d("ota progress : " + device.getOtaProgress());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            binding.progress.setText(String.valueOf(device.getOtaProgress()));
//                        }
//                    });
////                    mInfoHandler.obtainMessage(MSG_PROGRESS, device.getOtaProgress()).sendToTarget();
//                    break;
//                case Device.STATE_SUCCESS:
//                    telinkDFUSuccess = true;
//                    TelinkLog.d("ota success : ");
////                    mDevice.disconnect();
//                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            getLockSpecialValue();
//                        }
//                    }, 3000);
//                    break;
//                case Device.STATE_FAILURE:
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            onFailure();
//                        }
//                    });
//
//                    TelinkLog.d("ota failure : ");
//                    break;
//            }
//        }
//    };

    private Device.DeviceStateCallback deviceCallback = new Device.DeviceStateCallback() {
        @Override
        public void onConnected(Device device) {
            TelinkLog.w("telink:" + " # onConnected");
        }

        @Override
        public void onDisconnected(Device device) {
            TelinkLog.w("telink:" + " # onDisconnected");
            if (telinkDfuDisconnectFailureCallback) {
                errorCallback(DfuFailed, "disconnected");
            }
        }

        @Override
        public void onServicesDiscovered(Device device, final List<BluetoothGattService> services) {
            TelinkLog.w("telink:" + " # onServicesDiscovered");
            UUID serviceUUID = null;
            for (BluetoothGattService service : services) {
                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    if (characteristic.getUuid().equals(Device.CHARACTERISTIC_UUID_WRITE)) {
                        serviceUUID = service.getUuid();
                        break;
                    }
                }
            }

            if (serviceUUID != null) {
                device.SERVICE_UUID = serviceUUID;
            }

            byte[] firmware = IOUtil.readFirmware(mUpdateFilePath);
            if (firmware == null) {
//                LogUtil.d("invalid firmware package");
                errorCallback(DfuFailed, "invalid firmware package");
                return;
            }
            telinkDevice.startOta(firmware);
        }

        @Override
        public void onOtaStateChanged(final Device device, int state) {
            TelinkLog.w("telink:" + " # onOtaStateChanged");
            switch (state) {
                case Device.STATE_PROGRESS:
                    TelinkLog.d("ota progress : " + device.getOtaProgress());
                    //todo:数据处理
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            dfuCallback.onProgressChanged(device.getMacAddress(), device.getOtaProgress(), 0, 0, device.getIndex(), device.getTotal());
                        }
                    });
                    break;
                case Device.STATE_SUCCESS:
                    telinkDfuDisconnectFailureCallback = false;
                    TelinkLog.d("ota success : ");
                    if (TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(clientId))//只做固件升级部分 不做网络请求
                        successCallback();
                    else {
                        getFeature();
                    }
//                    mDevice.disconnect();
//                    dfuComplete(device.getMacAddress());
                    break;
                case Device.STATE_FAILURE:
                    TelinkLog.d("ota failure : ");
                    errorCallback(DfuFailed, "ota failed");
//                    errorCallback();
                    break;
            }
        }
    };

    private void telinkDfu() {
        //不做延迟，延迟之后会 会导致无法升级
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
                if (telinkDevice == null) {
                    BluetoothDevice bluetoothDevice =  ((BluetoothManager)mContext.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().getRemoteDevice(lockmac);
                    telinkDevice = new Device(bluetoothDevice, null, -1);
                    telinkDevice.setDeviceStateCallback(deviceCallback);
                }
                telinkDfuDisconnectFailureCallback = true;
                telinkDevice.connect(mContext);
//            }
//        }, 500);
    }

    /**
     * 向锁发送进入升级模式指令
     */
    private void enableLockDfuMode() {
        upgradeStatus = EnterDfu;
        LogUtil.d("enter dfu mode", DBG);

        TTLockClient.getDefault().enterDfuMode(lockData, lockmac, new EnterDfuModeCallback() {
            @Override
            public void onEnterDfuMode() {
                BluetoothImpl.getInstance().disconnect();
                //成功之后的步骤 断开连接再 启动DFU
                startDfu();
//                if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.TELINK_CHIP)) {
//                    telinkDfu();
//                } else {
//                    startDfu();
//                }
            }

            @Override
            public void onFail(LockError error) {
                errorCallback(BLECommunicationError, error.getErrorMsg());
            }
        });
    }

    private boolean dataIsEmpty(String data) {
        LogUtil.d("data:" + data, DBG);
        if(TextUtils.isEmpty(data) || data.equals("[]"))
            return true;
        return false;
    }

}
