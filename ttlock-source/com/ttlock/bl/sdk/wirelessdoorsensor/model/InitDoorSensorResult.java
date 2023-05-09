package com.ttlock.bl.sdk.wirelessdoorsensor.model;

import com.ttlock.bl.sdk.entity.FirmwareInfo;

/**
 * Created by TTLock on 2020/12/18.
 */
public class InitDoorSensorResult {

    private int batteryLevel;
    private FirmwareInfo firmwareInfo;

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public FirmwareInfo getFirmwareInfo() {
        return firmwareInfo;
    }

    public void setFirmwareInfo(FirmwareInfo firmwareInfo) {
        this.firmwareInfo = firmwareInfo;
    }
}
