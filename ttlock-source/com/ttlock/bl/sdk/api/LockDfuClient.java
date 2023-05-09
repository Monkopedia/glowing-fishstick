package com.ttlock.bl.sdk.api;

import android.content.Context;

import com.ttlock.bl.sdk.callback.DfuCallback;
import com.ttlock.bl.sdk.callback.GetLockSystemInfoCallback;

/**
 * Created by TTLock on 2017/8/16.
 */

public class LockDfuClient {

    private DfuSDKApi mApi;

    public LockDfuClient() {
        mApi = new DfuSDKApi();
    }

    public static LockDfuClient getDefault(){
        return LockDfuClient.InstanceHolder.mInstance;
    }

    private static class InstanceHolder{
        private final static LockDfuClient mInstance = new LockDfuClient();
    }

    public void getLockSystemInfo(String lockData, String lockMac, GetLockSystemInfoCallback callback){
        mApi.getLockSystemInfo(lockData, lockMac, callback);
    }

    public void startDfu(Context context, String clientId, String accessToken, int lockid, String lockData, String lockMac, DfuCallback dfuCallback) {
        //TODO:校验值是否为null
        mApi.startDfu(context, clientId, accessToken, lockid, lockData, lockMac, dfuCallback);
    }

    public void startDfu(Context context, String lockData, String lockMac, String firmwarePackage, DfuCallback dfuCallback) {
        //TODO:校验值是否为null
        mApi.startDfu(context, lockData, lockMac, firmwarePackage, dfuCallback);
    }

    public void retry() {
        mApi.retry();
    }

    public void abortDfu() {
        mApi.abortUpgradeProcess();
    }
}
