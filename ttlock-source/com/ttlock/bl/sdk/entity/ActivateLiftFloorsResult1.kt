package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2020/6/24.
 */
class ActivateLiftFloorsResult(
    var battery: Int, // -1 invalid value
    //    public int controlAction;
    var uniqueid: Int,
    var deviceTime: Long
) {
    fun getUniqueid(): Int {
        return uniqueid
    }

    fun setUniqueid(uniqueid: Int) {
        this.uniqueid = uniqueid
    }

    fun getBattery(): Int {
        return battery
    }

    fun setBattery(battery: Int) {
        this.battery = battery
    }

    fun getDeviceTime(): Long {
        return deviceTime
    }

    fun setDeviceTime(deviceTime: Long) {
        this.deviceTime = deviceTime
    }
}
