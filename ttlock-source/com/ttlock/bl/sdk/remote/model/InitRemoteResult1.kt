package com.ttlock.bl.sdk.remote.model

/**
 * Created by TTLock on 2020/12/18.
 */
class InitRemoteResult {
    /**
     * hex format
     */
    //    private String featureValue;
    private var batteryLevel = 0
    private var systemInfo: SystemInfo? = null

    //    public String getFeatureValue() {
    //        return featureValue;
    //    }
    //
    //    public void setFeatureValue(String featureValue) {
    //        this.featureValue = featureValue;
    //    }
    fun getBatteryLevel(): Int {
        return batteryLevel
    }

    fun setBatteryLevel(batteryLevel: Int) {
        this.batteryLevel = batteryLevel
    }

    fun getSystemInfo(): SystemInfo? {
        return systemInfo
    }

    fun setSystemInfo(systemInfo: SystemInfo?) {
        this.systemInfo = systemInfo
    }
}
