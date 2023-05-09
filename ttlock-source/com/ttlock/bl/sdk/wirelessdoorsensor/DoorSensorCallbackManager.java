package com.ttlock.bl.sdk.wirelessdoorsensor;

import android.util.SparseArray;

import com.ttlock.bl.sdk.wirelessdoorsensor.callback.ConnectCallback;
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.DoorSensorCallback;
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.ScanWirelessDoorSensorCallback;
import com.ttlock.bl.sdk.wirelessdoorsensor.model.DoorSensorError;
import com.ttlock.bl.sdk.wirelessdoorsensor.model.OperationType;


/**
 * Created on  2019/4/2 0002 15:12
 *
 */
final class DoorSensorCallbackManager {

    private ScanWirelessDoorSensorCallback mScanCallback = null;
    private ConnectCallback mConnectCallback = null;
    private SparseArray<DoorSensorCallback> mCallbackArray = new SparseArray<>(1);

    private DoorSensorCallbackManager(){
        mCallbackArray.clear();
    }

    public static DoorSensorCallbackManager getInstance(){
        return InstanceHolder.mInstance;
    }

    private static class InstanceHolder{
        private final static DoorSensorCallbackManager mInstance = new DoorSensorCallbackManager();
    }

    public void setScanCallback(ScanWirelessDoorSensorCallback callback){
        mScanCallback = callback;
    }

    public ScanWirelessDoorSensorCallback getScanCallback(){
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

    public boolean isBusy(int type, DoorSensorCallback callback){
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

    public DoorSensorCallback getCallbackWithoutRemove(){
        if (mCallbackArray.size() == 0) {
            return null;
        }
        int operationType = mCallbackArray.keyAt(0);
        return mCallbackArray.get(operationType);
    }

    public DoorSensorCallback getCallback(){
        if (mCallbackArray.size() == 0) {
            return null;
        }
        int operationType = mCallbackArray.keyAt(0);
        DoorSensorCallback currentCallback = mCallbackArray.get(operationType);
        if(currentCallback != null){
            mCallbackArray.clear();
        }
        return currentCallback;
    }

    public void clearAllCallback(){
        mCallbackArray.clear();
    }
    private void multiConnectFastFail(DoorSensorCallback callback){
        callback.onFail(DoorSensorError.Device_IS_BUSY);
    }









}
