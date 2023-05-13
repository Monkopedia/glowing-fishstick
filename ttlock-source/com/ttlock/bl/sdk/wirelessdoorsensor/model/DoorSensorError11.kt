package com.ttlock.bl.sdk.wirelessdoorsensor.model

import com.ttlock.bl.sdk.wirelessdoorsensor.model.DoorSensorError.FAILED
import com.ttlock.bl.sdk.wirelessdoorsensor.model.DoorSensorError.SUCCESS

/**
 * Created by TTLock on 2019/4/24.
 */
enum class DoorSensorError(private val errorCode: Int, private val description: String) {
    SUCCESS(0, "success"), FAILED(1, "failed"), NO_RESPONSE(0x200, "no response"), CONNECT_FAIL(
        0x400,
        "device connect time out"
    ),
    Device_IS_BUSY(0x401, "only one command can be proceed at a time"), DATA_FORMAT_ERROR(
        0x403,
        "parameter format or content is incorrect"
    );

    fun getDescription(): String {
        return description
    }

    companion object {
        fun getInstance(errorCode: Int): DoorSensorError {
            return when (errorCode) {
                0 -> DoorSensorError.SUCCESS
                1 -> DoorSensorError.FAILED
                else -> DoorSensorError.FAILED
            }
        }
    }
}
