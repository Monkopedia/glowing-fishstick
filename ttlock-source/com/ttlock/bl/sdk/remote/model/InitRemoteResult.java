package com.ttlock.bl.sdk.remote.model;

/**
 * Created by TTLock on 2020/12/18.
 */
public class InitRemoteResult {

    /**
     * hex format
     */
//    private String featureValue;
    private int batteryLevel;

    private SystemInfo systemInfo;

//    public String getFeatureValue() {
//        return featureValue;
//    }
//
//    public void setFeatureValue(String featureValue) {
//        this.featureValue = featureValue;
//    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public SystemInfo getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
    }
}
