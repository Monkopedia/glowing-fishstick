package com.ttlock.bl.sdk.gateway.model;

/**
 * Created by TTLock on 2019/3/12.
 */

public class WiFi {
    public String ssid;
    public int rssi;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @Override
    public String toString() {
        return "WiFi{" +
                "ssid='" + ssid + '\'' +
                ", rssi=" + rssi +
                '}';
    }
}
