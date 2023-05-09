package com.ttlock.bl.sdk.entity;

/**
 * Created by TTLock on 2020/6/24.
 */
public class ActivateLiftFloorsResult {
    //-1 invalid value
//    public int controlAction;
    public int uniqueid;
    public int battery;
    public long deviceTime;

    public ActivateLiftFloorsResult(int battery, int uniqueid, long deviceTime) {
        this.uniqueid = uniqueid;
        this.battery = battery;
        this.deviceTime = deviceTime;
    }

    public int getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(int uniqueid) {
        this.uniqueid = uniqueid;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public long getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(long deviceTime) {
        this.deviceTime = deviceTime;
    }
}
