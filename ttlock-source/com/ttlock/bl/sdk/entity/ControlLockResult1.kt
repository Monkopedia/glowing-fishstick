package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2020/6/24.
 */
class ControlLockResult( // -1 invalid value
    var controlAction: Int,
    var battery: Int,
    var uniqueid: Int,
    var lockTime: Long
) {
    fun getControlAction(): Int {
        return controlAction
    }

    fun setControlAction(controlAction: Int) {
        this.controlAction = controlAction
    }

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

    fun getLockTime(): Long {
        return lockTime
    }

    fun setLockTime(lockTime: Long) {
        this.lockTime = lockTime
    }
}
