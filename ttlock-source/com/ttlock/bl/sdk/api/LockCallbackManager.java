package com.ttlock.bl.sdk.api;

import android.util.SparseArray;

import com.ttlock.bl.sdk.callback.ConnectCallback;
import com.ttlock.bl.sdk.callback.LockCallback;
import com.ttlock.bl.sdk.callback.OperationType;
import com.ttlock.bl.sdk.callback.ScanLockCallback;
import com.ttlock.bl.sdk.entity.LockError;


/**
 * Created on  2019/4/2 0002 15:12
 *
 * @author theodre
 */
final class LockCallbackManager {

    private ScanLockCallback mScanCallback = null;
    private ConnectCallback mConnectCallback = null;
    private SparseArray<LockCallback> mCallbackArray = new SparseArray<>(1);

    private LockCallbackManager(){
        mCallbackArray.clear();
    }

    public static LockCallbackManager getInstance(){
        return LockCallbackManager.InstanceHolder.mInstance;
    }

    private static class InstanceHolder{
        private final static LockCallbackManager mInstance = new LockCallbackManager();
    }

    public void setLockScanCallback(ScanLockCallback callback){
        mScanCallback = callback;
    }

    public ScanLockCallback getLockScanCallback(){
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

    public boolean isDeviceBusy(int type, LockCallback callback, boolean isRollingGate){
        boolean isDeviceBusy = false;
        if(mCallbackArray.size() > 0){
            if (!isRollingGate || getOperationType() != type) {
                multiConnectFastFail(callback);
                isDeviceBusy = true;
            }
        }else {
            mCallbackArray.put(type,callback);
        }

        return isDeviceBusy;
    }

    public boolean isDeviceBusy(int type, LockCallback callback){
        boolean isDeviceBusy = false;
        if(mCallbackArray.size() > 0){
            multiConnectFastFail(callback);
            isDeviceBusy = true;
        }else {
            mCallbackArray.put(type,callback);
        }

        return isDeviceBusy;
    }

    public int getOperationType() {
        if (mCallbackArray.size() > 0)
            return mCallbackArray.keyAt(0);
        return OperationType.UNKNOWN_TYPE;
    }

    public LockCallback getCallbackWithoutRemove(){
        if (mCallbackArray.size() > 0) {
            int operationType = mCallbackArray.keyAt(0);
            return mCallbackArray.get(operationType);
        }
        return null;
    }

    public LockCallback getCallback(){
        if (mCallbackArray.size() > 0) {
            int operationType = mCallbackArray.keyAt(0);
            LockCallback currentCallback = mCallbackArray.get(operationType);
            if (currentCallback != null) {
                mCallbackArray.clear();
            }
            return currentCallback;
        }
        return null;
    }

    public void clearAllCallback(){
        mCallbackArray.clear();
    }
    private void multiConnectFastFail(LockCallback callback){
        callback.onFail(LockError.LOCK_IS_BUSY);
    }

    public boolean callbackArrayIsEmpty() {
       return mCallbackArray.size() == 0;
    }







}
