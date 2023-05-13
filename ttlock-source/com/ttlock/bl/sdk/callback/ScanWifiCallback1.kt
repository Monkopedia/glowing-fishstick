package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.gateway.model.WiFi

interface ScanWifiCallback : LockCallback {
    // 1-扫描完成 0-扫描中
    fun onScanWifi(wiFis: List<WiFi?>?, status: Int)
}
