package com.ttlock.bl.sdk.gateway.api;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.executor.AppExecutors;
import com.ttlock.bl.sdk.gateway.callback.ConfigIpCallback;
import com.ttlock.bl.sdk.gateway.callback.ConnectCallback;
import com.ttlock.bl.sdk.gateway.callback.EnterDfuCallback;
import com.ttlock.bl.sdk.gateway.callback.GatewayCallback;
import com.ttlock.bl.sdk.gateway.callback.InitGatewayCallback;
import com.ttlock.bl.sdk.gateway.callback.ScanWiFiByGatewayCallback;
import com.ttlock.bl.sdk.gateway.command.Command;
import com.ttlock.bl.sdk.gateway.command.CommandUtil;
import com.ttlock.bl.sdk.gateway.model.ConfigureGatewayInfo;
import com.ttlock.bl.sdk.gateway.model.DeviceInfo;
import com.ttlock.bl.sdk.gateway.model.GatewayError;
import com.ttlock.bl.sdk.gateway.model.WiFi;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by TTLock on 2019/3/11.
 */

public class GattCallbackHelper extends BluetoothGattCallback {

    private boolean DBG = true;

    /**
     * 连接超时10s
     */
    private static final int CONNECT_TIMEOUT = 10 * 1000;

    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static String UUID_SERVICE = "00001911-0000-1000-8000-00805f9b34fb";
    public static String UUID_WRITE = "00000002-0000-1000-8000-00805f9b34fb";
    public static String UUID_NODIFY = "00000003-0000-1000-8000-00805f9b34fb";

    private static final String DEVICE_INFORMATION_SERVICE = "0000180a-0000-1000-8000-00805f9b34fb";
    private static final String READ_MODEL_NUMBER_UUID = "00002a24-0000-1000-8000-00805f9b34fb";
    private static final String READ_WIFI_MAC_UUID = "00002a25-0000-1000-8000-00805f9b34fb";
    private static final String READ_FIRMWARE_REVISION_UUID = "00002a26-0000-1000-8000-00805f9b34fb";
    private static final String READ_HARDWARE_REVISION_UUID = "00002a27-0000-1000-8000-00805f9b34fb";
    private static final String READ_MANUFACTURER_NAME_UUID = "00002a29-0000-1000-8000-00805f9b34fb";

    private BluetoothGattCharacteristic modelNumberCharacteristic;
    private BluetoothGattCharacteristic wifiMacCharacteristic;
    private BluetoothGattCharacteristic hardwareRevisionCharacteristic;
    private BluetoothGattCharacteristic firmwareRevisionCharacteristic;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mWriteCharacteristic;
//    private G2GatewayCallback g2GatewayCallback;
    private ExtendedBluetoothDevice device;
    private ConfigureGatewayInfo configureInfo;

    private int recDataTotalLen;
    private int hasRecDataLen;
    private byte[] recDataBuf;
    private byte curCommand;
    private List<WiFi> wiFis = new ArrayList<WiFi>();

    /**
     * 接收的最大长度
     */
    private int maxBufferCount = 256;

    private DeviceInfo deviceInfo;

    private BluetoothGattService service;

    /**
     * 传输数据
     */
    private LinkedList<byte[]> dataQueue;
    private static GattCallbackHelper instance = new GattCallbackHelper();
    private Context context;
    private AppExecutors mAppExecutor;

    private GatewayCallback callback;
    private Handler handler = new Handler();

    private GattCallbackHelper() {
        mAppExecutor = new AppExecutors();
        recDataBuf = new byte[maxBufferCount];
    }

    public static GattCallbackHelper getInstance() {
        return instance;
    }

    private Runnable connectTimeOutRunable = new Runnable() {
        @Override
        public void run() {
            doWithConnectTimeout();
        }
    };

    public void prepare(Context context) {
        this.context = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void connect(String mac) {
        final BluetoothDevice bleDevice = mBluetoothAdapter.getRemoteDevice(mac);
        connect(new ExtendedBluetoothDevice(bleDevice));
    }

    public void connect(ExtendedBluetoothDevice extendedBluetoothDevice) {
        curCommand = 0;
        device = extendedBluetoothDevice;
        String address = device.getAddress();
        final BluetoothDevice bleDevice = mBluetoothAdapter.getRemoteDevice(address);
        clear();
        removeConnectTimeout();
        startConnectTimeout();
        mBluetoothGatt = bleDevice.connectGatt(context, false, this);
    }

//    public GattCallbackHelper(BluetoothGatt mBluetoothGatt) {
//        this.mBluetoothGatt = mBluetoothGatt;
//    }


    public ExtendedBluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(ExtendedBluetoothDevice device) {
        this.device = device;
    }

    public ConfigureGatewayInfo getConfigureInfo() {
        return configureInfo;
    }

    public void setConfigureInfo(ConfigureGatewayInfo configureInfo) {
        this.configureInfo = configureInfo;
    }

    public BluetoothGatt getBluetoothGatt() {
        return mBluetoothGatt;
    }

    public void setBluetoothGatt(BluetoothGatt mBluetoothGatt) {
        this.mBluetoothGatt = mBluetoothGatt;
    }

    public void sendCommand(byte[] command) {
        int len = command.length;
        curCommand = command[2];
        LogUtil.d("send datas:" + DigitUtil.byteArrayToHexString(command), DBG);
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

        if (mWriteCharacteristic != null && mBluetoothGatt != null) {
            try {
                hasRecDataLen = 0;//发送前恢复接收数据的起始位置
                mWriteCharacteristic.setValue(dataQueue.poll());
                mBluetoothGatt.writeCharacteristic(mWriteCharacteristic);
            } catch (Exception e) {
               //TODO:
            }
        } else {
            ConnectManager.getInstance().setDisconnected();
            callback = GatewayCallbackManager.getInstance().getCallback();
            if (callback != null) {
                callback.onFail(GatewayError.FAILED);
            }
            LogUtil.d("mBluetoothGatt:" + mBluetoothGatt, DBG);
            LogUtil.d("mNotifyCharacteristic or mBluetoothGatt is null", DBG);
        }
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        if (mBluetoothGatt != gatt)
            return;
        try {
            Thread.sleep(600);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (newState == BluetoothProfile.STATE_CONNECTED) {
            //并不是所有设备都有这个wifi mac
            wifiMacCharacteristic = null;
            LogUtil.d("STATE_CONNECTED");
            LogUtil.d("Attempting to start service discovery:" + gatt.discoverServices());
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            LogUtil.d("STATE_DISCONNECTED");
            removeConnectTimeout();
            mAppExecutor.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    doWithDisconnect();
                    curCommand = 0;
                }
            });
            close();
        }
    }

    private void doWithDisconnect() {
        //设置断开状态
        ConnectManager.getInstance().setDisconnected();
        callback = GatewayCallbackManager.getInstance().getCallback();
        LogUtil.d("callback:" + callback);
        if (callback != null) {
            switch (curCommand) {
                case Command.COMM_CONFIGURE_WIFI:
                    callback.onFail(GatewayError.FAILED_TO_CONFIGURE_ROUTER);
                    return;
                case Command.COMM_CONFIGURE_SERVER:
                    callback.onFail(GatewayError.FAILED_TO_CONFIGURE_SERVER);
                    return;
                case Command.COMM_CONFIGURE_ACCOUNT:
                    callback.onFail(GatewayError.FAILED_TO_CONFIGURE_ACCOUNT);
                    return;
                default:
                    if (curCommand != 0)
                        callback.onFail(GatewayError.COMMUNICATION_DISCONNECTED);
                    return;
            }
        } else {
            ConnectCallback mConnectCallback = GatewayCallbackManager.getInstance().getConnectCallback();
            LogUtil.d("mConnectCallback:" + mConnectCallback);
            if (mConnectCallback != null) {
                mConnectCallback.onDisconnected();
            }
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        LogUtil.d("");
        if (status == BluetoothGatt.GATT_SUCCESS) {

            List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                LogUtil.d("service:" + service.getUuid());
            }

            service = gatt.getService(UUID.fromString(DEVICE_INFORMATION_SERVICE));
            if (service != null) {
                List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();
                if (gattCharacteristics != null && gattCharacteristics.size() > 0) {
                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                        LogUtil.d(gattCharacteristic.getUuid().toString(), DBG);
                        LogUtil.d("read characteristic:" + Thread.currentThread(), DBG);
                        if (gattCharacteristic.getUuid().toString().equals(READ_MODEL_NUMBER_UUID)) {
                            modelNumberCharacteristic = gattCharacteristic;
                            gatt.readCharacteristic(gattCharacteristic);
                        } else if (gattCharacteristic.getUuid().toString().equals(READ_WIFI_MAC_UUID)) {
                            wifiMacCharacteristic = gattCharacteristic;
                        } else if (gattCharacteristic.getUuid().toString().equals(READ_FIRMWARE_REVISION_UUID)) {
                            firmwareRevisionCharacteristic = gattCharacteristic;
                        } else if (gattCharacteristic.getUuid().toString().equals(READ_HARDWARE_REVISION_UUID)) {
                            hardwareRevisionCharacteristic = gattCharacteristic;
                        }
                    }
                }
            } else {
                //测试出现的情况 是否再次发现一次
                LogUtil.w("service is null", true);
            }
        } else {
            LogUtil.w("onServicesDiscovered received: " + status, DBG);
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        LogUtil.d("gatt=" + gatt + " characteristic=" + characteristic + " status=" + status, DBG);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (characteristic == modelNumberCharacteristic) {
                deviceInfo = new DeviceInfo();
                deviceInfo.modelNum = new String(characteristic.getValue());
                gatt.readCharacteristic(wifiMacCharacteristic != null ? wifiMacCharacteristic : hardwareRevisionCharacteristic);
            } else if (characteristic == wifiMacCharacteristic) {
                deviceInfo.networkMac = new String(characteristic.getValue());
                gatt.readCharacteristic(hardwareRevisionCharacteristic);
            } else if (characteristic == hardwareRevisionCharacteristic) {
                deviceInfo.hardwareRevision = new String(characteristic.getValue());
                gatt.readCharacteristic(firmwareRevisionCharacteristic);
            } else if (characteristic == firmwareRevisionCharacteristic) {
                deviceInfo.firmwareRevision = new String(characteristic.getValue());
                LogUtil.d("deviceInfo:" + deviceInfo);

                service = gatt.getService(UUID.fromString(UUID_SERVICE));
                if (service != null) {
                    List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();
                    if (gattCharacteristics != null && gattCharacteristics.size() > 0) {
                        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                            LogUtil.d(gattCharacteristic.getUuid().toString(), DBG);
                            if (gattCharacteristic.getUuid().toString().equals(UUID_WRITE)) {
                                mWriteCharacteristic = gattCharacteristic;
                            } else if (gattCharacteristic.getUuid().toString().equals(UUID_NODIFY)) {
                                gatt.setCharacteristicNotification(gattCharacteristic, true);
                                BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID_HEART_RATE_MEASUREMENT);
                                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                if (gatt.writeDescriptor(descriptor)) {
                                    LogUtil.d("writeDescriptor successed", DBG);
                                } else {
                                    //TODO:
                                    LogUtil.d("writeDescriptor failed", DBG);
                                }
                            }
                        }
                    }
                } else {
                    //测试出现的情况 是否再次发现一次
                    LogUtil.w("service is null", true);
                }

            }
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (mBluetoothGatt != gatt) {
            LogUtil.w("gatt=" + gatt + " characteristic=" + characteristic + " status=" + status, DBG);
            return;
        }
        LogUtil.d("gatt=" + gatt + " characteristic=" + characteristic + " status=" + status, DBG);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (dataQueue.size() > 0) {
                characteristic.setValue(dataQueue.poll());
                //TODO:写成功再写下一个
                gatt.writeCharacteristic(characteristic);
            } else {

            }
        } else {
            LogUtil.w("onCharacteristicWrite failed", DBG);
        }

        super.onCharacteristicWrite(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        LogUtil.d("");
        if(mBluetoothGatt != gatt)
            return;
        super.onCharacteristicChanged(gatt, characteristic);
        try {
            LogUtil.d("gatt=" + gatt + " characteristic=" + characteristic, DBG);
            byte[] data = characteristic.getValue();
            LogUtil.d("data:" + DigitUtil.byteArrayToHexString(data));
            int dataLen = data.length;
            System.arraycopy(data, 0, recDataBuf, hasRecDataLen, dataLen);
            if(data.length >= 2 && data[0] == 0x72 && data[1] == 0x5b) {//数据开始
                recDataTotalLen = data[3] + 2 + 1 + 1 + 1;
                LogUtil.d("recDataTotalLen:" + recDataTotalLen);
            } else if (hasRecDataLen < 4) {//TODO:上一次的数据
                recDataTotalLen = data[3 - hasRecDataLen] + 2 + 1 + 1 + 1;
                LogUtil.d("recDataTotalLen:" + recDataTotalLen);
            }
            hasRecDataLen += dataLen;

            LogUtil.d("hasRecDataLen:" + hasRecDataLen);
            if (hasRecDataLen >= recDataTotalLen) {
                LogUtil.d("recDataTotalLen:" + recDataTotalLen);
                doWithData(Arrays.copyOf(recDataBuf, recDataTotalLen));
                //TODO:会有缓存的数据一起发送下来
                if (recDataTotalLen != hasRecDataLen && hasRecDataLen > 0) {
                    hasRecDataLen = hasRecDataLen - recDataTotalLen;
                    System.arraycopy(data, 20 - hasRecDataLen, recDataBuf, 0, hasRecDataLen);
                    if (hasRecDataLen > 3)
                        recDataTotalLen = data[20 - hasRecDataLen + 3] + 2 + 1 + 1 + 1;
                    LogUtil.d("recDataTotalLen:" + recDataTotalLen);
                } else {
                    hasRecDataLen = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //清零
            hasRecDataLen = 0;
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
        if (mBluetoothGatt != gatt)
            return;
        LogUtil.d("");
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (device != null)
                CommandUtil.gatewayEcho(device.getAddress());
        } else {
            //TODO:
        }
    }

    public void clearWifi() {
        wiFis.clear();
    }

    public void connectCallback() {
        mAppExecutor.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                removeConnectTimeout();
                ConnectManager.getInstance().onConnectSuccess(device);
            }
        });
    }

    private void doWithData(final byte[] values) {
        mAppExecutor.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("values:" + DigitUtil.byteArrayToHexString(values));
                Command command = new Command(values);
                command.setMac(device.getAddress());
                byte[] data = command.getData();
                LogUtil.d("command:" + DigitUtil.byteToHex(command.getCommand()));
                LogUtil.d("data:" + DigitUtil.byteArrayToHexString(data));
                callback = GatewayCallbackManager.getInstance().getCallback();
                if (callback == null && command.getCommand() != Command.COMM_ECHO) {//保持连接是默认做的 没有回调
                    LogUtil.d("callback is null");
                    return;
                }
                if (data == null) {
                    LogUtil.d("data is null");
                    return;
                }
                switch (command.getCommand()) {
                    case Command.COMM_ECHO:
                        connectCallback();
                        break;
                    case Command.COMM_GET_NEARBY_SSID:
                        switch (data[0]) {
                            case 0://成功
                                int len = data[1];
                                WiFi wiFi = new WiFi();
                                wiFi.ssid = new String(Arrays.copyOfRange(data, 2, 2 + len));
                                wiFi.rssi = data[len + 2];
                                LogUtil.d("wifi:" + wiFi);
                                if (!TextUtils.isEmpty(wiFi.ssid)) {
                                    insertWifi(wiFi);
                                    ((ScanWiFiByGatewayCallback) callback).onScanWiFiByGateway(wiFis);
                                }
                                break;
                            case 5://完成
                                //TODO:数据一样 是不是要返回
                                ((ScanWiFiByGatewayCallback) callback).onScanWiFiByGatewaySuccess();
                                wiFis.clear();
                                break;
                            default:
                                callback.onFail(GatewayError.getInstance(data[0]));
                                break;
                        }
                        break;
                    case Command.COMM_CONFIGURE_WIFI:
                        curCommand = 0;
                        if (data[0] == 0) {
                            CommandUtil.configureServer(configureInfo);
                        } else {
                            if (data[0] != 2) {
                                if (callback != null) {
                                    callback.onFail(GatewayError.getInstance(data[0]));
                                }
                            } else {//命令接收成功，正在处理
                                curCommand = Command.COMM_CONFIGURE_WIFI;
                            }
                        }
                        break;
                    case Command.COMM_CONFIGURE_SERVER:
                        curCommand = 0;
                        if (data[0] == 0)
                            CommandUtil.configureAccount(configureInfo);
                        else {
                            if (callback != null) {
                                callback.onFail(GatewayError.getInstance(data[0]));
                            }
                        }
                        break;
                    case Command.COMM_CONFIGURE_ACCOUNT:
                        curCommand = 0;
                        if (data[0] == 0) {
                            GatewayCallbackManager.getInstance().clearAllCallback();
                            ((InitGatewayCallback)callback).onInitGatewaySuccess(deviceInfo);
                            LogUtil.d("success");
                        } else {
                            if (callback != null) {
                                callback.onFail(GatewayError.getInstance(data[0]));
                            }
                        }
                        break;
                    case Command.COMM_ENTER_DFU:
                        if (data[0] == 0) {
                            ((EnterDfuCallback) callback).onEnterDfuSuccess();
                        } else {
                            callback.onFail(GatewayError.getInstance(data[0]));
                        }
                        //清理缓存的回调类型
                        GatewayCallbackManager.getInstance().clearAllCallback();
                        break;
                    case Command.COMM_CONFIG_IP:
                        if (data[0] == 0) {
                            ((ConfigIpCallback) callback).onConfigIpSuccess();
                        } else {
                            callback.onFail(GatewayError.getInstance(data[0]));
                        }
                        break;
                }
            }
        });
    }

    private synchronized void insertWifi(WiFi newWifi) {
        boolean hasInsert = false;
        for (int i=0;i<wiFis.size();i++) {
            WiFi wiFi = wiFis.get(i);
            if (newWifi.ssid.equals(wiFi.ssid)) {//信号值排序要改
                if (hasInsert)
                    wiFis.remove(i);
                else {
                    //TODO:
                    hasInsert = true;
                }
                break;
            } else if (newWifi.rssi > wiFi.rssi) {
                if (!hasInsert) {
                    wiFis.add(i, newWifi);
                    hasInsert = true;
                    //TODO:
                    i++;
                }
            }
        }
        if (!hasInsert)
            wiFis.add(newWifi);
    }

    private void close() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }

    public void disconnect() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }
    }

    public void clear() {
        disconnect();
        close();
    }

    public void startConnectTimeout() {
        handler.postDelayed(connectTimeOutRunable, CONNECT_TIMEOUT);
    }

    public void removeConnectTimeout() {
        handler.removeCallbacks(connectTimeOutRunable);
    }

    public void doWithConnectTimeout() {
        clear();
        connectTimeoutCallback();
    }

    public void connectTimeoutCallback() {
        callback = GatewayCallbackManager.getInstance().getCallback();
        if (callback != null) {
            callback.onFail(GatewayError.CONNECT_TIMEOUT);
        } else {
            ConnectManager.getInstance().setDisconnected();
            ConnectCallback mConnectCallback = GatewayCallbackManager.getInstance().getConnectCallback();
            if (mConnectCallback != null) {
                mConnectCallback.onDisconnected();
            }
        }
    }

    public String getNetworkMac() {
        if (deviceInfo != null) {
            return deviceInfo.networkMac;
        }
        return "";
    }

}
