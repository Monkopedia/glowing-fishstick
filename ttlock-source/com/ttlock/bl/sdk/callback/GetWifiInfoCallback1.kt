package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.WifiLockInfo

interface GetWifiInfoCallback : LockCallback {
    fun onGetWiFiInfoSuccess(wifiInfo: WifiLockInfo?)
}
