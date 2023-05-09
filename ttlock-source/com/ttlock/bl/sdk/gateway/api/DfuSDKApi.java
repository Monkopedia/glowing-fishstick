package com.ttlock.bl.sdk.gateway.api;

import android.Manifest;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.RequiresPermission;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.ttlock.file.FileProviderPath;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.gateway.callback.ConnectCallback;
import com.ttlock.bl.sdk.gateway.callback.DfuCallback;
import com.ttlock.bl.sdk.gateway.callback.EnterDfuCallback;
import com.ttlock.bl.sdk.gateway.callback.ScanGatewayCallback;
import com.ttlock.bl.sdk.gateway.model.GatewayError;
import com.ttlock.bl.sdk.gateway.model.GatewayUpdateInfo;
import com.ttlock.bl.sdk.net.ResponseService;
import com.ttlock.bl.sdk.service.DfuService;
import com.ttlock.bl.sdk.service.ThreadPool;
import com.ttlock.bl.sdk.telink.ble.Device;
import com.ttlock.bl.sdk.telink.util.TelinkLog;
import com.ttlock.bl.sdk.util.AESUtil;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.GsonUtil;
import com.ttlock.bl.sdk.util.IOUtil;
import com.ttlock.bl.sdk.util.LogUtil;
import com.ttlock.bl.sdk.util.NetworkUtil;

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
 * 泰凌微进入升级模式  网关DUF->蓝牙DFU
 */

class DfuSDKApi {

    private boolean DBG = true;
    private Context mContext;

    private String clientId;
    private String accessToken;
    private int gatewayId;
    String gatewayMac;

    private DfuCallback dfuCallback;

    private Handler handler;

    private boolean upgradeFailedByServer;

    private boolean downloadSuccess;
    private boolean isDFUMode;
    private boolean isTelinkDFUMode;
    /**
     * telink device object
     */
    private Device telinkDevice;

//    private boolean telinkDFUSuccess = false;

    private boolean telinkDfuDisconnectFailureCallback = true;

    private long scanTimeOut = 10 * 1000;
    private Runnable scanTimeOutRunable = new Runnable() {
        @Override
        public void run() {
            LogUtil.d("scan time out");
            ScanManager.getInstance().stopScan();
            errorCallback();
        }
    };

    //todo:可以增加一个超时
    private ScanGatewayCallback scanCallback = new ScanGatewayCallback() {
        @Override
        public void onScanGatewaySuccess(ExtendedBluetoothDevice device) {
            if (device.getAddress().equals(gatewayMac)) {
                if(device.isDfuMode()){
                    isDFUMode = true;
                }
                if (device.isTelinkGatewayDfuMode()) {
                    telinkDevice = new Device(device.getDevice(), device.getScanRecord(), device.getRssi());
                    isTelinkDFUMode = true;
                }
                if (isDFUMode || isTelinkDFUMode) {
                    if (downloadSuccess) {
                        LogUtil.d("start dfu");
                        startDfu();
                    }
                } else {//泰凌微网关一定要走蓝牙的进入升级模式
                    isDFUMode = false;
                    isTelinkDFUMode = false;
                    enterDfuByBle();
                }
                handler.removeCallbacks(scanTimeOutRunable);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            LogUtil.w("errorCode:" + errorCode);
        }
    };

    /**
     * nordic芯片 固件烧录状态回调
     */
    private DfuProgressListener mDfuProgressListener = new DfuProgressListenerAdapter() {
        @Override
        public void onDeviceConnecting(final String deviceAddress) {
        }

        @Override
        public void onDfuProcessStarting(final String deviceAddress) {
        }

        @Override
        public void onEnablingDfuMode(final String deviceAddress) {
        }

        @Override
        public void onFirmwareValidating(final String deviceAddress) {
        }

        @Override
        public void onDeviceDisconnecting(final String deviceAddress) {
        }

        @Override
        public void onDfuCompleted(final String deviceAddress) {
            dfuComplete(deviceAddress);
        }

        @Override
        public void onDfuAborted(final String deviceAddress) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dfuCallback.onDfuAborted(deviceAddress);
                }
            });
        }

        @Override
        public void onProgressChanged(final String deviceAddress, final int percent, final float speed, final float avgSpeed, final int currentPart, final int partsTotal) {
            dfuProgressChangeCallback(deviceAddress, percent, speed, avgSpeed, currentPart, partsTotal);
        }

        @Override
        public void onError(final String deviceAddress, final int error, final int errorType, final String message) {
            LogUtil.d("message:" + message);
            errorCallback();
        }
    };

    private void dfuComplete(String deviceAddress) {
        DfuServiceListenerHelper.unregisterProgressListener(mContext, mDfuProgressListener);
        upgradeSuccess(deviceAddress);
        clearFile();
        downloadSuccess = false;
    }

    private void upgradeSuccess(final String deviceAddress) {
        ThreadPool.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String json = ResponseService.plugUpgradeSuccess(clientId, accessToken, gatewayId);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    LogUtil.d("json:" + json, true);
                    int errcode = jsonObject.getInt("errcode");
                    if(errcode == 0) {
                        upgradeFailedByServer = false;
                        successCallback(deviceAddress);
                    } else {
                        upgradeFailedByServer = true;
                        errorCallback();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    upgradeFailedByServer = true;
                    errorCallback();
                }
            }
        });
    }

    private void successCallback(final String deviceAddress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                dfuCallback.onDfuSuccess(deviceAddress);
            }
        });
    }

    private void errorCallback() {
        clearData();
        telinkDfuDisconnectFailureCallback = false;
        handler.post(new Runnable() {
            @Override
            public void run() {
                dfuCallback.onError();
            }
        });
    }

    private void clearData() {
        isDFUMode = false;
        isTelinkDFUMode = false;
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    private void startScan() {
        try {
            handler.postDelayed(scanTimeOutRunable, scanTimeOut);
            GatewayClient.getDefault().startScanGateway(scanCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 中断升级
     */
    public void abortUpgradeProcess() {
        LogUtil.d("exit dfu mode", DBG);
        final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
        final Intent pauseAction = new Intent(DfuService.BROADCAST_ACTION);
        pauseAction.putExtra(DfuService.EXTRA_ACTION, DfuService.ACTION_ABORT);
        manager.sendBroadcast(pauseAction);
        if (telinkDevice != null) {
            telinkDevice.disconnect();
        }
    }

    /**
     * 锁固件升级包名
     */
    public final static String UPDATE_FILE_NAME = "realUpdate.zip";
    private String mUpdateFilePath;

    public DfuSDKApi() {

    }

    /**
     * 开始烧录固件
     */
    private void startDfu() {
        ScanManager.getInstance().stopScan();
        LogUtil.d("start dfu", DBG);
        LogUtil.d("isTelinkDFUMode:" + isTelinkDFUMode, DBG);
        LogUtil.d("isDFUMode:" + isDFUMode, DBG);
        if (isTelinkDFUMode) {
            telinkDfu();
        } else if (isDFUMode) {
            nordicDfu();
        }
    }

    private Device.DeviceStateCallback deviceCallback = new Device.DeviceStateCallback() {
        @Override
        public void onConnected(Device device) {
            TelinkLog.w("telink:" + " # onConnected");
        }

        @Override
        public void onDisconnected(Device device) {
            TelinkLog.w("telink:" + " # onDisconnected");
            if (telinkDfuDisconnectFailureCallback) {
                errorCallback();
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
                LogUtil.d("invalid firmware package");
                errorCallback();
                return;
            }
            telinkDevice.startOta(firmware);
        }

        @Override
        public void onOtaStateChanged(Device device, int state) {
            TelinkLog.w("telink:" + " # onOtaStateChanged");
            switch (state) {
                case Device.STATE_PROGRESS:
                    TelinkLog.d("ota progress : " + device.getOtaProgress());
                    //todo:数据处理
                    dfuProgressChangeCallback(device.getMacAddress(), device.getOtaProgress(), 0, 0, device.getIndex(), device.getTotal());
                    break;
                case Device.STATE_SUCCESS:
                    telinkDfuDisconnectFailureCallback = false;
                    TelinkLog.d("ota success : ");
//                    mDevice.disconnect();
                    dfuComplete(device.getMacAddress());
                    break;
                case Device.STATE_FAILURE:
                    TelinkLog.d("ota failure : ");
                    errorCallback();
                    break;
            }
        }
    };

    private void dfuProgressChangeCallback(final String deviceAddress, final int percent, final float speed, final float avgSpeed, final int currentPart, final int partsTotal) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                dfuCallback.onProgressChanged(deviceAddress, percent, speed, avgSpeed, currentPart, partsTotal);
            }
        });
    }

    private void telinkDfu() {
        LogUtil.d("telink dfu");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (telinkDevice != null) {
                    telinkDfuDisconnectFailureCallback = true;
                    telinkDevice.setDeviceStateCallback(deviceCallback);
                    telinkDevice.connect(mContext);
                } else {
                    LogUtil.d("telinkDevice is null");
                }
            }
        }, 1500);
    }

    private void nordicDfu() {
        LogUtil.d("nordic dfu");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final DfuServiceInitiator starter = new DfuServiceInitiator(gatewayMac)
                        .setForeground(false)
                        .setDisableNotification(true)
                        .setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true)
//                        .setDeviceName(mDoorkey.getLockName())
                        .setForceDfu(true)
                        .setPacketsReceiptNotificationsEnabled(true)
                        .setPrepareDataObjectDelay(400);

                starter.setZip(FileProviderPath.getUriForFile(mContext, new File(mUpdateFilePath)), mUpdateFilePath);
                starter.start(mContext, DfuService.class);

            }
        }, 2000);
    }

    private void enterDfuByServer() {
        //泰凌微网关
        //服务端发命令，会让网关重启
        //重置之后，蓝牙去连接，然后发那条升级命令
        //这样之后才可以升级
        ThreadPool.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String json = ResponseService.enterDfuMode(clientId, accessToken, gatewayId);
                try {
                    LogUtil.d("json:" + json, true);
                    LogUtil.d("isDFUMode:" + isDFUMode);
                    JSONObject jsonObject = new JSONObject(json);
                    int errcode = jsonObject.getInt("errcode");

                    startScan();//进入升级模式后 继续扫描根据广播判断是那种芯片的升级
//                    if(errcode == 0) {
//                        startScan();//进入升级模式后 继续扫描根据广播判断是那种芯片的升级
////                        startDfu();
//                    } else {
//                        if (!isDFUMode || !isTelinkDFUMode) {
//                            enterDfuByBle();
//                        }
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    startScan();//进入升级模式后 继续扫描根据广播判断是那种芯片的升级
                    //TODO:已经在升级模式
//                    if (!isDFUMode || !isTelinkDFUMode) {
//                        enterDfuByBle();
//                    }
                }
            }
        });
    }

    private void doEnterDfuByBle() {
        GatewayClient.getDefault().enterDfu(gatewayMac, new EnterDfuCallback() {
            @Override
            public void onEnterDfuSuccess() {
                startScan();
            }

            @Override
            public void onFail(GatewayError error) {
                LogUtil.d(error.getDescription());
                if (!isDFUMode && !isTelinkDFUMode) {
                    errorCallback();
                }
//                else {
//                    startDfu();
//                }
            }
        });
    }

    private void enterDfuByBle() {
        ScanManager.getInstance().stopScan();
        doEnterDfuByBle();
//        if (ConnectManager.getInstance().isDeviceConnected()) {
//            doEnterDfuByBle();
//        } else {
//            GatewayClient.getDefault().connectGateway(gatewayMac, new ConnectCallback() {
//                @Override
//                public void onConnectSuccess(ExtendedBluetoothDevice device) {//泰凌微需要重新连接 再让网关进入升级模式
//                    doEnterDfuByBle();
//                }
//
//                @Override
//                public void onDisconnected() {
//                    if (!isDFUMode && !isTelinkDFUMode) {
//                        errorCallback();
//                    }
//                }
//            });
//        }
    }

    private void initData() {
        isDFUMode = false;
        isTelinkDFUMode = false;
        downloadSuccess = false;
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void startDfu(Context context, String clientId, String accessToken, int gatewayId, String gatewayMac, DfuCallback dfuCallback) {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        //TODO:赋值
        this.clientId = clientId;
        this.accessToken = accessToken;
        this.gatewayId = gatewayId;
        this.gatewayMac = gatewayMac;
        this.dfuCallback = dfuCallback;
        this.mContext = context;
        DfuServiceListenerHelper.registerProgressListener(context, mDfuProgressListener);
        GatewayClient.getDefault().prepareBTService(context);

        initData();

        if(NetworkUtil.isNetConnected(context)) {//TODO:退出unregister
            getDownloadUrl();
        } else {
            LogUtil.d("bad network");
            errorCallback();
        }
    }

    private void getDownloadUrl() {
        ThreadPool.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String json = ResponseService.getPlugUpgradePackage(clientId, accessToken, gatewayId);
                LogUtil.d("json:" + json);
                if(TextUtils.isEmpty(json)) {
                    errorCallback();
                } else {
                    GatewayUpdateInfo gatewayUpdateInfo = GsonUtil.toObject(json, GatewayUpdateInfo.class);
                    if(gatewayUpdateInfo != null && !TextUtils.isEmpty(gatewayUpdateInfo.getUrl())) {
                        downloadUpdatePackage(gatewayUpdateInfo);
                    } else {
                        errorCallback();
                    }
                }
            }
        });
    }

    private void downloadUpdatePackage(final GatewayUpdateInfo gatewayUpdateInfo) {
        ThreadPool.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                InputStream is = null;
                try {
                    URL url = new URL(gatewayUpdateInfo.getUrl());
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

                    byte[] decryptedBytes = AESUtil.aesDecrypt(source, DigitUtil.decodeLockData(gatewayUpdateInfo.getDecryptionKey()).getBytes());
                    mUpdateFilePath = mContext.getCacheDir().getAbsolutePath() + File.separator + UPDATE_FILE_NAME;
//                    LogUtil.d("mUpdateFilePath:" + mUpdateFilePath, DBG);
                    os = new FileOutputStream(mUpdateFilePath);
                    if(decryptedBytes != null)
                        os.write(decryptedBytes);
                    //完成后关闭流
                    os.close();
                    is.close();

                    downloadSuccess = true;

                    enterDfuByServer();
//                    if (!isDFUMode || !isTelinkDFUMode) {
//                        LogUtil.d("enter dfu");
//                        enterDfuByServer();
//                    } else {
//                        startDfu();
//                    }

                } catch (IOException e) {
                    mUpdateFilePath = null;
                    e.printStackTrace();
                    errorCallback();
                } finally {

                }
            }
        });
    }

    /**
     * 升级成功之后删除文件
     */
    private void clearFile() {
        if (mUpdateFilePath == null)
            return;
        File file = new File(mUpdateFilePath);
        if (file != null && file.exists()) {
            LogUtil.d("delete file:" + file.delete());
        }
    }

    void retryEnterDfuModeByNet() {
        if (canRetry()) {
            if (TextUtils.isEmpty(mUpdateFilePath)) {
                getDownloadUrl();
            } else if (upgradeFailedByServer) {
                upgradeSuccess(gatewayMac);
            } else {
                enterDfuByServer();
            }
        } else {
            LogUtil.w("please call startDfu method first");
            errorCallback();
        }
    }

    void retryEnterDfuModeByBle() {
        if (canRetry()) {
            if (TextUtils.isEmpty(mUpdateFilePath)) {
                getDownloadUrl();
            } else if (upgradeFailedByServer) {
                upgradeSuccess(gatewayMac);
            } else {
                enterDfuByBle();
            }
        } else {
            LogUtil.w("please call startDfu method first");
            errorCallback();
        }
    }

    private boolean canRetry() {
        if (!TextUtils.isEmpty(accessToken) && !TextUtils.isEmpty(clientId))
            return true;
        return false;
    }

}
