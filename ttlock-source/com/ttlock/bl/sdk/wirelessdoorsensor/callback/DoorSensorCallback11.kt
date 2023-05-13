package com.ttlock.bl.sdk.wirelessdoorsensor.callback

import com.ttlock.bl.sdk.wirelessdoorsensor.model.DoorSensorError

/**
 * Created on  2019/4/8 0008 15:31
 *
 * @author theodore
 */
interface DoorSensorCallback {
    fun onFail(error: DoorSensorError?)
}
