package com.ttlock.bl.sdk.entity;

public class WifiLockInfo {

    /**
     * WiFi MAC地址
     * （6 bytes）
     * 高在前，低在后
     */
    private String wifiMac;

    /**
     * 有符号数，一般都是负数
     *
     * 未连接时，返回-127
     */
    private int wifiRssi;

    public WifiLockInfo() {
    }

    public WifiLockInfo(String wifiMac, int wifiRssi) {
        this.wifiMac = wifiMac;
        this.wifiRssi = wifiRssi;
    }

    public String getWifiMac() {
        return wifiMac;
    }

    public void setWifiMac(String wifiMac) {
        this.wifiMac = wifiMac;
    }

    public int getWifiRssi() {
        return wifiRssi;
    }

    public void setWifiRssi(int wifiRssi) {
        this.wifiRssi = wifiRssi;
    }
}
