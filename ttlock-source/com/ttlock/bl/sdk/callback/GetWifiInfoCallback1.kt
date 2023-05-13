package com.ttlock.bl.sdk.callback

interface GetWifiInfoCallback : LockCallback {
    fun onGetWiFiInfoSuccess(wifiInfo: WifiLockInfo?)
}
