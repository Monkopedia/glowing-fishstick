package com.ttlock.bl.sdk.wirelessdoorsensor;

import android.os.Handler;
import android.text.TextUtils;

import com.ttlock.bl.sdk.device.WirelessDoorSensor;
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.ConnectCallback;
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.DoorSensorCallback;
import com.ttlock.bl.sdk.wirelessdoorsensor.model.ConnectParam;
import com.ttlock.bl.sdk.wirelessdoorsensor.model.DoorSensorError;
import com.ttlock.bl.sdk.wirelessdoorsensor.model.OperationType;

/**
 * Created on  2019/4/12 SetLockTimeCallback 11:30
 * @author theodore
 */
class ConnectManager implements ConnectCallback {
    private WirelessDoorSensorSDKApi mApi;
    private Handler mDataCheckHandler = new Handler();
    private String mCurrentMac;
    private ConnectParam mConnectParam;


    private ConnectManager(){
        mApi = new WirelessDoorSensorSDKApi();
        mConnectParam = null;
    }

//    private Runnable mDataCheckErrorRunnable = new Runnable() {
//        @Override
//        public void run() {
//            KeyFobCallback mCallback = KeyFobCallbackManager.getInstance().getCallback();
//            if(mCallback != null){
//                mCallback.onFail(KeyFobError.DATA_FORMAT_ERROR);
//            }
//        }
//    };

    public static ConnectManager getInstance() {
        return InstanceHolder.mInstance;
    }

    public ConnectParam getConnectParam() {
        return mConnectParam;
    }

    @Override
    public void onConnectSuccess(WirelessDoorSensor device) {
        int callbackType = DoorSensorCallbackManager.getInstance().getOperationType();
        //TODO:
        if (mConnectParam == null)
            return;
        switch (callbackType){
            case OperationType.INIT:
                mApi.init(device);
                break;
            case OperationType.ENTER_DFU:
                mApi.checkAdmin(device);
                break;
//            case OperationType.SET_DOOR_NOT_CLOSED_WARNING_TIME:
//                mApi.setDoorNotClosedWarningTime(mConnectParam.getDoorSensorMac(), mConnectParam.getTime());
//                break;
            default:
                break;
        }
    }

    public void storeConnectParamForCallback(ConnectParam param){
        this.mConnectParam = param;
    }

    @Override
    public void onFail(DoorSensorError error) {
        int callbackType = DoorSensorCallbackManager.getInstance().getOperationType();
        DoorSensorCallback mCallback = DoorSensorCallbackManager.getInstance().getCallback();
        if(mCallback != null){
            if(!TextUtils.isEmpty(mCurrentMac)){
                retryConnect(callbackType, mCallback);
            }else {
                mCallback.onFail(error);
            }
        }
    }

    private static class InstanceHolder{
        private final static ConnectManager mInstance = new ConnectManager();
    }

//    public void removeDataCheckTimer(){
//        mDataCheckHandler.removeCallbacks(mDataCheckErrorRunnable);
//    }

    public boolean isDeviceConnected(String address){
        //TODO:
//        return BluetoothImpl.getInstance().isConnected(address);
        return false;
    }

    public void connect2Device(String address){
        mCurrentMac = address;
        DoorSensorCallbackManager.getInstance().setConnectCallback(this);
    }

    public void connect2Device(WirelessDoorSensor device){
        DoorSensorCallbackManager.getInstance().setConnectCallback(this);
        GattCallbackHelper.getInstance().connect(device);
    }

//    private void startDataCheckTimer(){
//        mDataCheckHandler.postDelayed(mDataCheckErrorRunnable,500);
//    }


    private void retryConnect(int callbackType, DoorSensorCallback callback){
        connect2Device(mCurrentMac);
        DoorSensorCallbackManager.getInstance().isBusy(callbackType,callback);
        mCurrentMac = "";
    }

}
