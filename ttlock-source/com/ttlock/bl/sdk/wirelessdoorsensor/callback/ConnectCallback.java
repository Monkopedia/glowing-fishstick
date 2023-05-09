package com.ttlock.bl.sdk.wirelessdoorsensor.callback;

import com.ttlock.bl.sdk.device.WirelessDoorSensor;

/**
 * Created on  2019/4/12 0012 11:59
 *
 * @author theodre
 */
public interface ConnectCallback extends DoorSensorCallback {
    void onConnectSuccess(WirelessDoorSensor device);
}
