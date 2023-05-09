package com.ttlock.bl.sdk.gateway.api;

import android.content.Context;

import com.ttlock.bl.sdk.gateway.callback.DfuCallback;

/**
 * Created by TTLock on 2017/8/16.
 */

public class GatewayDfuClient {

    private DfuSDKApi mApi;

    public GatewayDfuClient() {
        mApi = new DfuSDKApi();
    }

    public static GatewayDfuClient getDefault(){
        return GatewayDfuClient.InstanceHolder.mInstance;
    }

    private static class InstanceHolder{
        private final static GatewayDfuClient mInstance = new GatewayDfuClient();
    }

    public void startDfu(Context context, String clientId, String accessToken, int gatewayId, String gatewayMac, DfuCallback dfuCallback) {
        //TODO:校验值是否为null
        mApi.startDfu(context, clientId, accessToken, gatewayId, gatewayMac, dfuCallback);
    }

    public void retryEnterDfuModeByNet() {
        mApi.retryEnterDfuModeByNet();
    }

    public void retryEnterDfuModeByBle() {
        mApi.retryEnterDfuModeByBle();
    }

    public void abortDfu() {
        mApi.abortUpgradeProcess();
    }
}
