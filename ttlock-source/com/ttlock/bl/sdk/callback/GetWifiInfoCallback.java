package com.ttlock.bl.sdk.callback;

import android.net.wifi.WifiInfo;

import com.ttlock.bl.sdk.entity.WifiLockInfo;

public interface GetWifiInfoCallback extends LockCallback {
    void onGetWiFiInfoSuccess(WifiLockInfo wifiInfo);
}
