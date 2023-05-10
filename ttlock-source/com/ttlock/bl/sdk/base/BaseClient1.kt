package com.ttlock.bl.sdk.base

import android.Manifest

/**
 * Created by TTLock on 2021/1/25.
 */
open class BaseClient<T : BaseSDKApi?> {
    protected var mApi: T? = null

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
    fun isBLEEnabled(context: Context): Boolean {
        return mApi!!.isBLEEnabled(context)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH)
    fun requestBleEnable(activity: Activity) {
        mApi!!.requestBleEnable(activity)
    }

    open fun prepareBTService(context: Context?) {
        LogUtil.d("prepare service")
        mApi!!.prepareBTService(context)
    }
}