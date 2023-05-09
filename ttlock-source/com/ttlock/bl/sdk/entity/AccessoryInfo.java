package com.ttlock.bl.sdk.entity;

import java.io.Serializable;

/**
 * Created by TTLock on 2020/12/18.
 */
public class AccessoryInfo implements Serializable {
    private AccessoryType accessoryType;
    private String accessoryMac;
    private long batteryDate;
    private int accessoryBattery;

    public AccessoryInfo() {
    }

    public AccessoryInfo(AccessoryType accessoryType, String accessoryMac, long batteryDate, int accessoryBattery) {
        this.accessoryType = accessoryType;
        this.accessoryMac = accessoryMac;
        this.batteryDate = batteryDate;
        this.accessoryBattery = accessoryBattery;
    }

    public AccessoryType getAccessoryType() {
        return accessoryType;
    }

    public void setAccessoryType(AccessoryType accessoryType) {
        this.accessoryType = accessoryType;
    }

    public String getAccessoryMac() {
        return accessoryMac;
    }

    public void setAccessoryMac(String accessoryMac) {
        this.accessoryMac = accessoryMac;
    }

    public long getBatteryDate() {
        return batteryDate;
    }

    public void setBatteryDate(long batteryDate) {
        this.batteryDate = batteryDate;
    }

    public int getAccessoryBattery() {
        return accessoryBattery;
    }

    public void setAccessoryBattery(int accessoryBattery) {
        this.accessoryBattery = accessoryBattery;
    }
}
