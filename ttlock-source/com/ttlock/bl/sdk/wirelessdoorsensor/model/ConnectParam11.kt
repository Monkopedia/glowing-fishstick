package com.ttlock.bl.sdk.wirelessdoorsensor.model

/**
 * Created on  2019/4/12 0012 16:34
 *
 * @author theodre
 */
class ConnectParam {
    private var lockmac: String? = null
    private var lockKey: String? = null
    private var aesKey: String? = null
    private var doorSensorMac: String? = null
    private var time = 0

    //    public String factoryDate;
    fun getLockmac(): String? {
        return lockmac
    }

    fun setLockmac(lockmac: String?) {
        this.lockmac = lockmac
    }

    fun getLockKey(): String? {
        return lockKey
    }

    fun setLockKey(lockKey: String?) {
        this.lockKey = lockKey
    }

    fun getAesKey(): String? {
        return aesKey
    }

    fun setAesKey(aesKey: String?) {
        this.aesKey = aesKey
    }

    fun getDoorSensorMac(): String? {
        return doorSensorMac
    }

    fun setDoorSensorMac(doorSensorMac: String?) {
        this.doorSensorMac = doorSensorMac
    }

    fun getTime(): Int {
        return time
    }

    fun setTime(time: Int) {
        this.time = time
    }
}
