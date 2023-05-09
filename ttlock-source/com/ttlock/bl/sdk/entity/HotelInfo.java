package com.ttlock.bl.sdk.entity;

/**
 * Created by TTLock on 2020/7/5.
 */
public class HotelInfo {
    public int hotelNumber;
    public String icKey;
    public String aesKey;

    public int getHotelNumber() {
        return hotelNumber;
    }

    public void setHotelNumber(int hotelNumber) {
        this.hotelNumber = hotelNumber;
    }

    public String getIcKey() {
        return icKey;
    }

    public void setIcKey(String icKey) {
        this.icKey = icKey;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }
}
