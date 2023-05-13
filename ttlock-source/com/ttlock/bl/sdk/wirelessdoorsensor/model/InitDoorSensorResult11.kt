package com.ttlock.bl.sdk.wirelessdoorsensor.model

import com.ttlock.bl.sdk.entity.FirmwareInfo

/**
 * Created by TTLock on 2020/12/18.
 */
class InitDoorSensorResult {
    private var batteryLevel = 0
    private var firmwareInfo: FirmwareInfo? = null
    fun getBatteryLevel(): Int {
        return batteryLevel
    }

    fun setBatteryLevel(batteryLevel: Int) {
        this.batteryLevel = batteryLevel
    }

    fun getFirmwareInfo(): FirmwareInfo? {
        return firmwareInfo
    }

    fun setFirmwareInfo(firmwareInfo: FirmwareInfo?) {
        this.firmwareInfo = firmwareInfo
    }
}
