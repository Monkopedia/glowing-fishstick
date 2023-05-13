package com.ttlock.bl.sdk.base

import android.util.Context
import com.ttlock.bl.sdk.util.LogUtil

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

    fun isBLEEnabled(context: Context): Boolean {
        return mApi!!.isBLEEnabled(context)
    }

    open fun prepareBTService(context: Context?) {
        LogUtil.d("prepare service")
        mApi!!.prepareBTService(context)
    }
}
