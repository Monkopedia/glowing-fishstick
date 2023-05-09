package com.ttlock.bl.sdk.wirelessdoorsensor;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.ttlock.bl.sdk.base.BaseGattCallbackHelper;
import com.ttlock.bl.sdk.device.WirelessDoorSensor;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.LogUtil;
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.ConnectCallback;
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.DoorSensorCallback;
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.EnterDfuCallback;
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.InitDoorSensorCallback;
import com.ttlock.bl.sdk.wirelessdoorsensor.command.Command;
import com.ttlock.bl.sdk.wirelessdoorsensor.command.CommandUtil;
import com.ttlock.bl.sdk.wirelessdoorsensor.model.DoorSensorError;
import com.ttlock.bl.sdk.wirelessdoorsensor.model.InitDoorSensorResult;

import java.util.Arrays;

/**
 * Created by TTLock on 2019/3/11.
 */

public class GattCallbackHelper extends BaseGattCallbackHelper<WirelessDoorSensor> {

    //TODO:
    private boolean isInitSuccess;

    private static GattCallbackHelper instance = new GattCallbackHelper();

    public static GattCallbackHelper getInstance() {
        return instance;
    }


    @Override
    protected void noResponseCallback() {
        DoorSensorCallback callback = DoorSensorCallbackManager.getInstance().getCallback();
        if (callback != null)
            callback.onFail(DoorSensorError.NO_RESPONSE);
    }

    @Override
    protected void disconnectedCallback() {
            mAppExecutor.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    ConnectCallback mConnectCallback = DoorSensorCallbackManager.getInstance().getConnectCallback();
                    if(mConnectCallback != null){
                        Log.d("OMG","====disconnect==1==" + isInitSuccess);
                        mConnectCallback.onFail(DoorSensorError.CONNECT_FAIL);
                    }
                    isInitSuccess = false;
                }
            });
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        LogUtil.d("gatt=" + gatt + " characteristic=" + characteristic + " status=" + status, DBG);
    }

    @Override
    protected void connectCallback() {
            mAppExecutor.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    ConnectCallback mConnectCallback = DoorSensorCallbackManager.getInstance().getConnectCallback();
                    if(mConnectCallback != null){
                        Log.d("OMG","====connect success==1==");
                        mConnectCallback.onConnectSuccess(device);
                    }
                }
            });
    }

    @Override
    public void doWithData(final byte[] values) {
        mAppExecutor.mainThread().execute(new Runnable() {
            @Override
            public void run() {
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
                                InitDoorSensorResult initDoorSensorResult = new InitDoorSensorResult();
                                initDoorSensorResult.setBatteryLevel(data[2]);
                                initDoorSensorResult.setFirmwareInfo(firmwareInfo);
                                DoorSensorCallback callback = DoorSensorCallbackManager.getInstance().getCallback();
                                if (callback != null) {
                                    ((InitDoorSensorCallback) callback).onInitSuccess(initDoorSensorResult);
                                }
                                //成功之后主动断开连接
                                disconnect();
                                break;
                        case Command.COMM_CHECK_ADMIN:
                            long responseRandom = DigitUtil.bytesToLong(Arrays.copyOfRange(data, 2, data.length));
                            CommandUtil.checkRandom(device, ConnectManager.getInstance().getConnectParam(), responseRandom);
                            break;
                        case Command.COMM_CHECK_RANDOM:
                            CommandUtil.enterDfu(device);
                            break;
                        case Command.COMM_ENTER_DFU:
                            callback = DoorSensorCallbackManager.getInstance().getCallback();
                            if (callback != null) {
                                ((EnterDfuCallback) callback).onEnterDfuSuccess();
                            }
                            break;
//                        case Command.COMM_DOOR_NOT_CLOSED_WARNING:
//                            callback = DoorSensorCallbackManager.getInstance().getCallback();
//                            if (callback != null) {
//                                ((SetDoorNotClosedWarningTimeCallback) callback).onSetSuccess();
//                            }
//                            break;
                        }
                } else {
                    DoorSensorCallback callback = DoorSensorCallbackManager.getInstance().getCallback();
                    if (callback != null) {
                        callback.onFail(DoorSensorError.FAILED);
                    }
                }
            }
        });
    }


}
