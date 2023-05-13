package com.ttlock.bl.sdk.entity

import java.io.Serializable

/**
 * Created by TTLock on 2020/12/18.
 */
class AccessoryInfo : Serializable {
    private var accessoryType: AccessoryType? = null
    private var accessoryMac: String? = null
    private var batteryDate: Long = 0
    private var accessoryBattery = 0

    constructor() {}
    constructor(
        accessoryType: AccessoryType?,
        accessoryMac: String?,
        batteryDate: Long,
        accessoryBattery: Int
    ) {
        this.accessoryType = accessoryType
        this.accessoryMac = accessoryMac
        this.batteryDate = batteryDate
        this.accessoryBattery = accessoryBattery
    }

    fun getAccessoryType(): AccessoryType? {
        return accessoryType
    }

    fun setAccessoryType(accessoryType: AccessoryType?) {
        this.accessoryType = accessoryType
    }

    fun getAccessoryMac(): String? {
        return accessoryMac
    }

    fun setAccessoryMac(accessoryMac: String?) {
        this.accessoryMac = accessoryMac
    }

    fun getBatteryDate(): Long {
        return batteryDate
    }

    fun setBatteryDate(batteryDate: Long) {
        this.batteryDate = batteryDate
    }

    fun getAccessoryBattery(): Int {
        return accessoryBattery
    }

    fun setAccessoryBattery(accessoryBattery: Int) {
        this.accessoryBattery = accessoryBattery
    }
}
