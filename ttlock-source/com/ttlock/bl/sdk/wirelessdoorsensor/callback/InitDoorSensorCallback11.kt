package com.ttlock.bl.sdk.wirelessdoorsensor.callback

import com.ttlock.bl.sdk.wirelessdoorsensor.model.InitDoorSensorResult

/**
 * Created on  2019/4/10 0010 10:25
 *
 */
interface InitDoorSensorCallback : DoorSensorCallback {
    fun onInitSuccess(initDoorSensorResult: InitDoorSensorResult?)
}
