package com.ttlock.bl.sdk.remote.api;

import android.os.Handler;
import android.text.TextUtils;

import com.ttlock.bl.sdk.device.Remote;
import com.ttlock.bl.sdk.remote.callback.ConnectCallback;
import com.ttlock.bl.sdk.remote.callback.RemoteCallback;
import com.ttlock.bl.sdk.remote.model.ConnectParam;
import com.ttlock.bl.sdk.remote.model.RemoteError;
import com.ttlock.bl.sdk.remote.model.OperationType;

/**
 * Created on  2019/4/12 SetLockTimeCallback 11:30
 * @author theodore
 */
class ConnectManager implements ConnectCallback {
    private WirelessKeyFobSDKApi mApi;
    private Handler mDataCheckHandler = new Handler();
    private String mCurrentMac;
    private ConnectParam mConnectParam;


    private ConnectManager(){
        mApi = new WirelessKeyFobSDKApi();
        mConnectParam = null;
    }

    private Runnable mDataCheckErrorRunnable = new Runnable() {
        @Override
        public void run() {
            RemoteCallback mCallback = RemoteCallbackManager.getInstance().getCallback();
            if(mCallback != null){
                mCallback.onFail(RemoteError.DATA_FORMAT_ERROR);
            }
        }
    };

    public static ConnectManager getInstance() {
        return InstanceHolder.mInstance;
    }

    public ConnectParam getConnectParam() {
        return mConnectParam;
    }

    @Override
    public void onConnectSuccess(Remote device) {
        int callbackType = RemoteCallbackManager.getInstance().getOperationType();
        //TODO:
//        startDataCheckTimer();
        if (mConnectParam == null)
            return;
        switch (callbackType){
            case OperationType.INIT:
                mApi.init(device);
                break;
            default:
                break;
        }
    }

    public void storeConnectParamForCallback(ConnectParam param){
        this.mConnectParam = param;
    }

    @Override
    public void onFail(RemoteError error) {
        int callbackType = RemoteCallbackManager.getInstance().getOperationType();
        RemoteCallback mCallback = RemoteCallbackManager.getInstance().getCallback();
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

    public void removeDataCheckTimer(){
        mDataCheckHandler.removeCallbacks(mDataCheckErrorRunnable);
    }

    public boolean isDeviceConnected(String address){
        //TODO:
        return GattCallbackHelper.getInstance().isConnected(address);
//        return false;
    }

    public void connect2Device(String address){
        mCurrentMac = address;
        RemoteCallbackManager.getInstance().setConnectCallback(this);
        //TODO:
//        BluetoothImpl.getInstance().connect(address);
    }

    public void connect2Device(Remote device){
        RemoteCallbackManager.getInstance().setConnectCallback(this);
        GattCallbackHelper.getInstance().connect(device);
    }

    private void startDataCheckTimer(){
        mDataCheckHandler.postDelayed(mDataCheckErrorRunnable,500);
    }


    private void retryConnect(int callbackType, RemoteCallback callback){
        connect2Device(mCurrentMac);
        RemoteCallbackManager.getInstance().isBusy(callbackType,callback);
        mCurrentMac = "";
    }

}
