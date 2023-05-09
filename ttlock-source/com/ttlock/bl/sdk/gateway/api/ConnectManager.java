package com.ttlock.bl.sdk.gateway.api;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.callback.ConnectLockCallback;
import com.ttlock.bl.sdk.callback.LockCallback;
import com.ttlock.bl.sdk.gateway.callback.ConnectCallback;
import com.ttlock.bl.sdk.gateway.callback.GatewayCallback;
import com.ttlock.bl.sdk.gateway.model.ConnectParam;
import com.ttlock.bl.sdk.gateway.model.GatewayError;
import com.ttlock.bl.sdk.gateway.model.OperationType;
import com.ttlock.bl.sdk.util.LogUtil;

/**
 * Created on  2019/4/12 SetLockTimeCallback 11:30
 * @author theodore
 */
class ConnectManager implements ConnectCallback {
    private GatewaySDKApi mApi;
    private ConnectParam mConnectParam;
    private Handler mDataCheckHandler = new Handler();
    private String mCurrentMac;

    private ConnectCallback connectCallback;

    private int connect_status = CONNECT_STATUS_UNKNOWN;
    public static final int CONNECT_STATUS_UNKNOWN = 0;
    public static final int CONNECT_STATUS_CONNECTING = 1;
    public static final int CONNECT_STATUS_CONNECTED = 2;
    public static final int CONNECT_STATUS_DISCONNECTED = 3;


    private ConnectManager(){
        mApi = new GatewaySDKApi();
        mConnectParam = null;
    }

    private Runnable mDataCheckErrorRunnable = new Runnable() {
        @Override
        public void run() {
            GatewayCallback mCallback = GatewayCallbackManager.getInstance().getCallback();
            if(mCallback != null){
                mCallback.onFail(GatewayError.DATA_FORMAT_ERROR);
            }
        }
    };

    public static ConnectManager getInstance(){
        return ConnectManager.InstanceHolder.mInstance;
    }

    private static class InstanceHolder{
        private final static ConnectManager mInstance = new ConnectManager();
    }

    public void removeDataCheckTimer(){
        mDataCheckHandler.removeCallbacks(mDataCheckErrorRunnable);
    }

    public void storeConnectParamForCallback(ConnectParam param){
        this.mConnectParam = param;
    }

    public boolean isDeviceConnected(){
        connectStatusLog();
        return connect_status == CONNECT_STATUS_CONNECTED;
//        if (connect_status != CONNECT_STATUS_CONNECTED) {
//            return false;
//        }
//        if (TextUtils.isEmpty(mCurrentMac) || TextUtils.isEmpty(address)) {
//            return false;
//        }
//        return mCurrentMac.equals(address);
    }

    public void connect2Device(String address){
        connect2Device(address, this);
    }

    public void connect2Device(String address, ConnectCallback connectCallback){
        Log.d("OMG","==connect2Device=");
        this.connectCallback = connectCallback;
        mCurrentMac = address;
        connect_status = CONNECT_STATUS_CONNECTING;
        connectStatusLog();
        GatewayCallbackManager.getInstance().setConnectCallback(connectCallback);
        GattCallbackHelper.getInstance().connect(address);
    }

    public void disconnect() {
        connect_status = CONNECT_STATUS_DISCONNECTED;
        connectStatusLog();
        GattCallbackHelper.getInstance().disconnect();
    }

    /**
     *
     * @param device
     * @param connectCallback    对外专门提供的连接回调
     */
    public void connect2Device(ExtendedBluetoothDevice device, ConnectCallback connectCallback){
        connect_status = CONNECT_STATUS_CONNECTING;
        connectStatusLog();
        mCurrentMac = device.getAddress();
        GatewayCallbackManager.getInstance().setConnectCallback(connectCallback);
        this.connectCallback = connectCallback;
        GattCallbackHelper.getInstance().connect(device);
    }

    private void startDataCheckTimer(){
        mDataCheckHandler.postDelayed(mDataCheckErrorRunnable,500);
    }

    @Override
    public void onConnectSuccess(ExtendedBluetoothDevice device) {
        connect_status = CONNECT_STATUS_CONNECTED;
        connectStatusLog();
        int callbackType = GatewayCallbackManager.getInstance().getOperationType();

        switch (callbackType) {
            case OperationType.ENTER_DFU_MODE:
                mApi.enterDfu(mConnectParam.getMac());
                break;
            default:
                //对外 单独提供的  连接回调
                ConnectCallback mConnectCallback = GatewayCallbackManager.getInstance().getConnectCallback();
                if (mConnectCallback != null) {
                    Log.d("OMG", "====disconnect==1==");
                    mConnectCallback.onConnectSuccess(device);
                }
                break;
        }
    }

//    /**
//     * do second connect if first time connect fail.
//     * @param error
//     */
//    @Override
//    public void onFail(GatewayError error) {
//        int callbackType = GatewayCallbackManager.getInstance().getOperationType();
//        GatewayCallback mCallback = GatewayCallbackManager.getInstance().getCallback();
//        if(mCallback != null){
//            if(!TextUtils.isEmpty(mCurrentMac)){
//                retryConnect(callbackType,mCallback);
//            }else {
//                mCallback.onFail(error);
//            }
//        }
//    }

    @Override
    public void onDisconnected() {
        setDisconnected();
        ConnectCallback mCallback = GatewayCallbackManager.getInstance().getConnectCallback();
        if (mCallback != null && !(mCallback instanceof ConnectManager)) {//如果是外部 单独的连接回调 才进行断开回调(自己回调自己 死循环)
            mCallback.onDisconnected();
        }
    }

    public void setDisconnected() {
        connect_status = CONNECT_STATUS_DISCONNECTED;
        connectStatusLog();
    }

    private void retryConnect(int callbackType, GatewayCallback callback){
        Log.d("OMG","==retryConnect=" + callback);
        connect2Device(mCurrentMac, connectCallback);
        GatewayCallbackManager.getInstance().isGatewayBusy(callbackType,callback);
        mCurrentMac = "";
    }

    private void connectStatusLog() {
        LogUtil.d("connect_status:" + connect_status);
    }

}
