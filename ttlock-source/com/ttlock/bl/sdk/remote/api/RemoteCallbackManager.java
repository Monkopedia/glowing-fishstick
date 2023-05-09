package com.ttlock.bl.sdk.remote.api;

import android.util.SparseArray;

import com.ttlock.bl.sdk.remote.callback.ConnectCallback;
import com.ttlock.bl.sdk.remote.callback.RemoteCallback;
import com.ttlock.bl.sdk.remote.callback.ScanRemoteCallback;
import com.ttlock.bl.sdk.remote.model.RemoteError;
import com.ttlock.bl.sdk.remote.model.OperationType;


/**
 * Created on  2019/4/2 0002 15:12
 *
 */
final class RemoteCallbackManager {

    private ScanRemoteCallback mScanCallback = null;
    private ConnectCallback mConnectCallback = null;
    private SparseArray<RemoteCallback> mCallbackArray = new SparseArray<>(1);

    private RemoteCallbackManager(){
        mCallbackArray.clear();
    }

    public static RemoteCallbackManager getInstance(){
        return InstanceHolder.mInstance;
    }

    private static class InstanceHolder{
        private final static RemoteCallbackManager mInstance = new RemoteCallbackManager();
    }

    public void setScanCallback(ScanRemoteCallback callback){
        mScanCallback = callback;
    }

    public ScanRemoteCallback getScanCallback(){
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

    public boolean isBusy(int type, RemoteCallback callback){
        boolean isLockBusy = false;
        if(mCallbackArray.size() > 0){
            multiConnectFastFail(callback);
            isLockBusy = true;
        }else {
            mCallbackArray.put(type,callback);
        }

        return isLockBusy;
    }

    public int getOperationType(){
        if (mCallbackArray.size() == 0) {
            return OperationType.INIT;
        }
        int operationType  = mCallbackArray.keyAt(0);
        return operationType;
    }

    public RemoteCallback getCallbackWithoutRemove(){
        if (mCallbackArray.size() == 0) {
            return null;
        }
        int operationType = mCallbackArray.keyAt(0);
        return mCallbackArray.get(operationType);
    }

    public RemoteCallback getCallback(){
        if (mCallbackArray.size() == 0) {
            return null;
        }
        int operationType = mCallbackArray.keyAt(0);
        RemoteCallback currentCallback = mCallbackArray.get(operationType);
        if(currentCallback != null){
            mCallbackArray.clear();
        }
        return currentCallback;
    }

    public void clearAllCallback(){
        mCallbackArray.clear();
    }
    private void multiConnectFastFail(RemoteCallback callback){
        callback.onFail(RemoteError.Device_IS_BUSY);
    }









}
