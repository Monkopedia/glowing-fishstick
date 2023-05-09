package com.ttlock.bl.sdk.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.RequiresPermission;

import com.ttlock.bl.sdk.util.LogUtil;


/**
 * Created by TTLock on 2021/1/25.
 */
public class BaseClient <T extends BaseSDKApi> {

    protected T mApi;

//    private BaseClient() {
//        mApi = new BaseSDKApi();
//    }
//
//    public static BaseClient getDefault() {
//        return InstanceHolder.mInstance;
//    }

//    private static class InstanceHolder{
//        private final static BaseClient mInstance = new BaseClient();
//    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public boolean isBLEEnabled(Context context) {
        return mApi.isBLEEnabled(context);
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public void requestBleEnable(Activity activity) {
        mApi.requestBleEnable(activity);
    }

    public void prepareBTService(Context context){
        LogUtil.d("prepare service");
        mApi.prepareBTService(context);
    }

}
