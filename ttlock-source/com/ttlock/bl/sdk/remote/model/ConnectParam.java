package com.ttlock.bl.sdk.remote.model;

/**
 * Created on  2019/4/12 0012 16:34
 *
 * @author theodre
 */
public class ConnectParam {
    private String lockmac;
    private String lockKey;
    private String aesKey;
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
}
