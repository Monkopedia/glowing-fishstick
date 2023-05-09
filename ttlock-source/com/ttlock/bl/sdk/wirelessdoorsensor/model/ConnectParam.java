package com.ttlock.bl.sdk.wirelessdoorsensor.model;

/**
 * Created on  2019/4/12 0012 16:34
 *
 * @author theodre
 */
public class ConnectParam {
    private String lockmac;
    private String lockKey;
    private String aesKey;
    private String doorSensorMac;
    private int time;
//    public String factoryDate;


    public String getLockmac() {
        return lockmac;
    }

    public void setLockmac(String lockmac) {
        this.lockmac = lockmac;
    }

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getDoorSensorMac() {
        return doorSensorMac;
    }

    public void setDoorSensorMac(String doorSensorMac) {
        this.doorSensorMac = doorSensorMac;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
