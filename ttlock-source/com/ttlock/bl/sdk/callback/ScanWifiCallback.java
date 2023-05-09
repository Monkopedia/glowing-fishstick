package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.gateway.model.WiFi;

import java.util.List;

public interface ScanWifiCallback extends LockCallback {
    //1-扫描完成 0-扫描中
    void onScanWifi(List<WiFi> wiFis, int status);
}
