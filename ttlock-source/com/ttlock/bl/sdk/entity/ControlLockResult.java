package com.ttlock.bl.sdk.entity;

/**
 * Created by TTLock on 2020/6/24.
 */
public class ControlLockResult {
    //-1 invalid value
    public int controlAction;
    public int uniqueid;
    public int battery;
    public long lockTime;

    public ControlLockResult(int controlAction, int battery, int uniqueid, long lockTime) {
        this.controlAction = controlAction;
        this.uniqueid = uniqueid;
        this.battery = battery;
        this.lockTime = lockTime;
    }

    public int getControlAction() {
        return controlAction;
    }

    public void setControlAction(int controlAction) {
        this.controlAction = controlAction;
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

    public long getLockTime() {
        return lockTime;
    }

    public void setLockTime(long lockTime) {
        this.lockTime = lockTime;
    }
}
