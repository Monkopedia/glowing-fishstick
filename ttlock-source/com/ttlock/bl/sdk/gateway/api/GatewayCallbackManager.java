package com.ttlock.bl.sdk.gateway.api;

import android.util.SparseArray;

import com.ttlock.bl.sdk.gateway.callback.ConnectCallback;
import com.ttlock.bl.sdk.gateway.callback.GatewayCallback;
import com.ttlock.bl.sdk.gateway.callback.ScanGatewayCallback;

/**
 * Created by TTLock on 2019/4/24.
 */

final class GatewayCallbackManager {

    private ScanGatewayCallback mScanCallback = null;
    private ConnectCallback mConnectCallback = null;
    private SparseArray<GatewayCallback> mCallbackArray = new SparseArray<>(1);

    private GatewayCallbackManager(){
        mCallbackArray.clear();
    }

    public static GatewayCallbackManager getInstance(){
        return InstanceHolder.mInstance;
    }

    private static class InstanceHolder{
        private final static GatewayCallbackManager mInstance = new GatewayCallbackManager();
    }

    public void setGatewayScanCallback(ScanGatewayCallback callback){
        mScanCallback = callback;
    }

    public ScanGatewayCallback getGatewayScanCallback(){
        return mScanCallback;
    }

    public void clearScanCallback(){
        mScanCallback = null;
    }

    public void setConnectCallback(ConnectCallback connectCallback){
        this.mConnectCallback = connectCallback;
    }

    public ConnectCallback getConnectCallback(){
        return this.mConnectCallback;
    }

    public boolean isGatewayBusy(int type,GatewayCallback callback){
        boolean isGatewayBusy = false;
//        if(mCallbackArray.size() > 0){
//            multiConnectFastFail(callback);
//            isGatewayBusy = true;
//        }else {
//            mCallbackArray.put(type,callback);
//        }
        //todo:
        if(mCallbackArray.size() > 0){
            mCallbackArray.clear();
        }
        mCallbackArray.put(type,callback);
        return isGatewayBusy;
    }

    public int getOperationType(){
        if (mCallbackArray.size() == 0) {
            return 0;
        }
        int operationType  = mCallbackArray.keyAt(0);
        return operationType;
    }

//    public GatewayCallback getCallbackWithoutRemove(){
//        int operationType = mCallbackArray.keyAt(0);
//        return mCallbackArray.get(operationType);
//    }

    public GatewayCallback getCallback() {
        if (mCallbackArray.size() == 0) {
            return null;
        }
        int operationType = mCallbackArray.keyAt(0);
        GatewayCallback currentCallback = mCallbackArray.get(operationType);
        return currentCallback;
    }

    public void clearAllCallback(){
        mCallbackArray.clear();
    }
//    private void multiConnectFastFail(GatewayCallback callback){
//        callback.onFail(GatewayError.GATEWAY_IS_BUSY);
//    }


}
