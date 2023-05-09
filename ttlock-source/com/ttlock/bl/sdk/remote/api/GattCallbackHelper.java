package com.ttlock.bl.sdk.remote.api;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.ttlock.bl.sdk.device.Remote;
import com.ttlock.bl.sdk.executor.AppExecutors;
import com.ttlock.bl.sdk.remote.callback.GetRemoteSystemInfoCallback;
import com.ttlock.bl.sdk.remote.model.OperationType;
import com.ttlock.bl.sdk.remote.model.SystemInfo;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.LogUtil;
import com.ttlock.bl.sdk.remote.callback.ConnectCallback;
import com.ttlock.bl.sdk.remote.callback.InitRemoteCallback;
import com.ttlock.bl.sdk.remote.callback.RemoteCallback;
import com.ttlock.bl.sdk.remote.command.Command;
import com.ttlock.bl.sdk.remote.model.InitRemoteResult;
import com.ttlock.bl.sdk.remote.model.RemoteError;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by TTLock on 2019/3/11.
 */

public class GattCallbackHelper extends BluetoothGattCallback {

    private boolean DBG = true;

    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static String UUID_SERVICE = "00001710-0000-1000-8000-00805f9b34fb";
    public static String UUID_WRITE = "00000002-0000-1000-8000-00805f9b34fb";
    public static String UUID_NODIFY = "00000003-0000-1000-8000-00805f9b34fb";

    private static final String DEVICE_INFORMATION_SERVICE = "0000180a-0000-1000-8000-00805f9b34fb";
    private static final String READ_MODEL_NUMBER_UUID = "00002a24-0000-1000-8000-00805f9b34fb";
    private static final String READ_FIRMWARE_REVISION_UUID = "00002a26-0000-1000-8000-00805f9b34fb";
    private static final String READ_HARDWARE_REVISION_UUID = "00002a27-0000-1000-8000-00805f9b34fb";
    private static final String READ_MANUFACTURER_NAME_UUID = "00002a29-0000-1000-8000-00805f9b34fb";

    private BluetoothGattCharacteristic modelNumberCharacteristic;
    private BluetoothGattCharacteristic hardwareRevisionCharacteristic;
    private BluetoothGattCharacteristic firmwareRevisionCharacteristic;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mWriteCharacteristic;
    private Remote device;

    private int recDataTotalLen;
    private int hasRecDataLen;
    private byte[] recDataBuf;

    /**
     * 接收的最大长度
     */
    private int maxBufferCount = 256;

    private BluetoothGattService service;

    /**
     * 传输数据
     */
    private LinkedList<byte[]> dataQueue;
    private static GattCallbackHelper instance = new GattCallbackHelper();
    private Context context;
    private AppExecutors mAppExecutor;
    private android.os.Handler handler;
    private SystemInfo deviceInfo;

    //TODO:
    private boolean isInitSuccess;

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    public static int mConnectionState = STATE_DISCONNECTED;
    private String mAddress;

    private Runnable noResponseRunable = new Runnable() {
        @Override
        public void run() {
            RemoteCallback callback = RemoteCallbackManager.getInstance().getCallback();
            if (callback != null)
                callback.onFail(RemoteError.NO_RESPONSE);
            disconnect();
        }
    };

    private GattCallbackHelper() {
        mAppExecutor = new AppExecutors();
        recDataBuf = new byte[maxBufferCount];
        handler = new android.os.Handler(Looper.getMainLooper());
    }

    public static GattCallbackHelper getInstance() {
        return instance;
    }

    public void prepare(Context context) {
        this.context = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

//    public void connect(String mac) {
//        final BluetoothDevice bleDevice = mBluetoothAdapter.getRemoteDevice(mac);
//        connect(new WirelessKeypad(bleDevice));
//    }

    public void connect(Remote extendedBluetoothDevice) {
        device = extendedBluetoothDevice;
        mAddress = device.getAddress();
        final BluetoothDevice bleDevice = mBluetoothAdapter.getRemoteDevice(mAddress);
        clear();
        mConnectionState = STATE_CONNECTING;
        mBluetoothGatt = bleDevice.connectGatt(context, false, this);
    }

    public Remote getDevice() {
        return device;
    }

    public void setDevice(Remote device) {
        this.device = device;
    }

    public BluetoothGatt getBluetoothGatt() {
        return mBluetoothGatt;
    }

    public void setBluetoothGatt(BluetoothGatt mBluetoothGatt) {
        this.mBluetoothGatt = mBluetoothGatt;
    }

    public void sendCommand(byte[] command) {
        int len = command.length;

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
                startResponseTimer();
                hasRecDataLen = 0;//发送前恢复接收数据的起始位置
                mWriteCharacteristic.setValue(dataQueue.poll());
                mBluetoothGatt.writeCharacteristic(mWriteCharacteristic);
            } catch (Exception e) {
               //TODO:
            }
        } else {
            LogUtil.d("mBluetoothGatt:" + mBluetoothGatt, DBG);
            LogUtil.d("mNotifyCharacteristic or mBluetoothGatt is null", DBG);
           //TODO:
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
            LogUtil.d("STATE_CONNECTED");
            LogUtil.d("Attempting to start service discovery:" + gatt.discoverServices());
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            LogUtil.d("STATE_DISCONNECTED");
            mConnectionState = STATE_DISCONNECTED;
            mAppExecutor.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    ConnectCallback mConnectCallback = RemoteCallbackManager.getInstance().getConnectCallback();
                    if(mConnectCallback != null){
                        Log.d("OMG","====disconnect==1==" + isInitSuccess);
                        mConnectCallback.onFail(RemoteError.CONNECT_FAIL);
                    }
                    isInitSuccess = false;
                }
            });
            clear();
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        LogUtil.d("");
        if (status == BluetoothGatt.GATT_SUCCESS) {

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
            gatt.readCharacteristic(modelNumberCharacteristic);

//            List<BluetoothGattService> services = gatt.getServices();
//            for (BluetoothGattService service : services) {
//                LogUtil.d("service:" + service.getUuid());
//            }
//
//            service = gatt.getService(UUID.fromString(UUID_SERVICE));
//            if (service != null) {
//                List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();
//                if (gattCharacteristics != null && gattCharacteristics.size() > 0) {
//                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
//                        LogUtil.d(gattCharacteristic.getUuid().toString(), DBG);
//                        if (gattCharacteristic.getUuid().toString().equals(UUID_WRITE)) {
//                            mWriteCharacteristic = gattCharacteristic;
//                        } else if (gattCharacteristic.getUuid().toString().equals(UUID_NODIFY)) {
//                            gatt.setCharacteristicNotification(gattCharacteristic, true);
//                            BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID_HEART_RATE_MEASUREMENT);
//                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                            if (gatt.writeDescriptor(descriptor)) {
//                                LogUtil.d("writeDescriptor successed", DBG);
//                            } else {
//                                //TODO:
//                                LogUtil.d("writeDescriptor failed", DBG);
//                            }
//                        }
//                    }
//                }
//            } else {
//                //测试出现的情况 是否再次发现一次
//                LogUtil.w("service is null", true);
//            }
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
                if (deviceInfo == null)
                    deviceInfo = new SystemInfo();
                deviceInfo.modelNum = new String(characteristic.getValue());
                gatt.readCharacteristic(hardwareRevisionCharacteristic);
            } else if (characteristic == hardwareRevisionCharacteristic) {
                deviceInfo.hardwareRevision = new String(characteristic.getValue());
                gatt.readCharacteristic(firmwareRevisionCharacteristic);
            } else if (characteristic == firmwareRevisionCharacteristic) {
                deviceInfo.firmwareRevision = new String(characteristic.getValue());
                LogUtil.d("deviceInfo:" + deviceInfo);
                int callbackType = RemoteCallbackManager.getInstance().getOperationType();

                if (callbackType == OperationType.GET_DEVICE_INFO) {//获取设备信息成功后 断开蓝牙
                    RemoteCallback callback = RemoteCallbackManager.getInstance().getCallback();
                    if (callback != null) {
                        ((GetRemoteSystemInfoCallback) callback).onGetRemoteSystemInfoSuccess(deviceInfo);
                    }
                    disconnect();
                    return;
                }

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
            removeResponseTimer();
            LogUtil.d("gatt=" + gatt + " characteristic=" + characteristic, DBG);
            byte[] data = characteristic.getValue();
            LogUtil.d("data:" + DigitUtil.byteArrayToHexString(data));
            int dataLen = data.length;
            System.arraycopy(data, 0, recDataBuf, hasRecDataLen, dataLen);
            if(data[0] == 0x72 && data[1] == 0x5b) {//数据开始
                recDataTotalLen = data[3] + 2 + 1 + 1 + 1;
                LogUtil.d("recDataTotalLen:" + recDataTotalLen);
            }
            hasRecDataLen += dataLen;
            if (data[dataLen - 2] == 0x0d && data[dataLen - 1] == 0x0a) {
                hasRecDataLen -= 2;
                doWithData(Arrays.copyOf(recDataBuf, hasRecDataLen));
                hasRecDataLen = 0;
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
            mConnectionState = STATE_CONNECTED;
            mAppExecutor.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    ConnectCallback mConnectCallback = RemoteCallbackManager.getInstance().getConnectCallback();
                    if(mConnectCallback != null){
                        Log.d("OMG","====connect success==1==");
                        mConnectCallback.onConnectSuccess(device);
                    }
                }
            });
        } else {
            //TODO:
        }
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

                if (data == null) {
                    LogUtil.d("data is null");
                    return;
                }
                if (data[1] == 1) {//成功
                    switch (data[0]) {//命令字
                            case Command.COMM_SET_LOCK:
                                InitRemoteResult initKeyFobResult = new InitRemoteResult();
                                initKeyFobResult.setBatteryLevel(data[2]);
                                initKeyFobResult.setSystemInfo(deviceInfo);
                                RemoteCallback callback = RemoteCallbackManager.getInstance().getCallback();
                                if (callback != null) {
                                    ((InitRemoteCallback) callback).onInitSuccess(initKeyFobResult);
                                }
                                //成功之后主动断开连接  后面可能还会读取固件信息就不断开连接了
//                                disconnect();
                                break;
                        }
                } else {
                    RemoteCallback callback = RemoteCallbackManager.getInstance().getCallback();
                    if (callback != null) {
                        callback.onFail(RemoteError.FAILED);
                    }
                }
            }
        });
    }

    public boolean isConnected(String address) {
        if(TextUtils.isEmpty(address)){
            return false;
        }

        if(address.equals(mAddress) && mConnectionState == STATE_CONNECTED){
            return true;
        }
        return false;
    }

    public SystemInfo getDeviceInfo() {
        return deviceInfo;
    }

    private void close() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }

    private void disconnect() {
        if (mBluetoothGatt != null) {
            mConnectionState = STATE_DISCONNECTED;
            mBluetoothGatt.disconnect();
        }
    }

    public void clear() {
        disconnect();
        close();
    }

    private void startResponseTimer(){
        handler.postDelayed(noResponseRunable,5000);
    }

    private void removeResponseTimer() {
        handler.removeCallbacks(noResponseRunable);
    }

}
