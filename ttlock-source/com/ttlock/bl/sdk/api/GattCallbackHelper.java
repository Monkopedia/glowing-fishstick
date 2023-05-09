package com.ttlock.bl.sdk.api;

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

import com.ttlock.bl.sdk.callback.ConnectCallback;
import com.ttlock.bl.sdk.callback.InitKeypadCallback;
import com.ttlock.bl.sdk.callback.LockCallback;
import com.ttlock.bl.sdk.device.WirelessKeypad;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.executor.AppExecutors;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.LogUtil;


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
    public static String UUID_SERVICE = "00001810-0000-1000-8000-00805f9b34fb";
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
    private WirelessKeypad device;

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

    //TODO:
    private boolean isInitSuccess;

    private Runnable noResponseRunable = new Runnable() {
        @Override
        public void run() {
            LockCallback callback = LockCallbackManager.getInstance().getCallback();
            if (callback != null){
                callback.onFail(LockError.WIRELESS_KEYBOARD_NO_RESPONSE);
            }
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


    public void connect(WirelessKeypad extendedBluetoothDevice) {
        device = extendedBluetoothDevice;
        String address = device.getAddress();
        final BluetoothDevice bleDevice = mBluetoothAdapter.getRemoteDevice(address);
        clear();
        mBluetoothGatt = bleDevice.connectGatt(context, false, this);
    }

    public WirelessKeypad getDevice() {
        return device;
    }

    public void setDevice(WirelessKeypad device) {
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

            mAppExecutor.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    ConnectCallback mConnectCallback = LockCallbackManager.getInstance().getConnectCallback();
                    if(mConnectCallback != null){
                        mConnectCallback.onFail(LockError.DEVICE_CONNECT_FAILED);
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

            List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                LogUtil.d("service:" + service.getUuid());
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
        } else {
            LogUtil.w("onServicesDiscovered received: " + status, DBG);
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        LogUtil.d("gatt=" + gatt + " characteristic=" + characteristic + " status=" + status, DBG);
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
            mAppExecutor.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    ConnectCallback mConnectCallback = LockCallbackManager.getInstance().getConnectCallback();
                    if(mConnectCallback != null){
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
                WirelessKeyboardCommand command = new WirelessKeyboardCommand(values);
                command.setMac(device.getAddress());
                byte[] data = command.getData();
                LogUtil.d("command:" + DigitUtil.byteToHex(command.getCommand()));
                LogUtil.d("data:" + DigitUtil.byteArrayToHexString(data));

                if (data == null) {
                    LogUtil.d("data is null");
                    return;
                }
                if (data[1] == 1) {
                    switch (data[0]) {
                            case WirelessKeyboardCommand.COMM_SET_LOCK:
                                CommandUtil.readDeviceFeature(device);
                                break;
                            case WirelessKeyboardCommand.COMM_READ_FEATURE:
                                int feature = (int) DigitUtil.fourBytesToLong(Arrays.copyOfRange(data, 3, data.length));
                                LogUtil.d("feature:" + feature);
                                LockCallback callback = LockCallbackManager.getInstance().getCallback();
                                if (callback != null){
                                    ((InitKeypadCallback)callback).onInitKeypadSuccess(feature);
                                }
                                //成功之后主动断开连接
                                disconnect();
                                break;
                                default:
                                    break;
                        }
                } else {
                    LockCallback callback = LockCallbackManager.getInstance().getCallback();
                    if (callback != null){
                        callback.onFail(LockError.INIT_WIRELESS_KEYBOARD_FAILED);
                    }
                }
            }
        });
    }

    private void close() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }

    private void disconnect() {
        if (mBluetoothGatt != null) {
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
