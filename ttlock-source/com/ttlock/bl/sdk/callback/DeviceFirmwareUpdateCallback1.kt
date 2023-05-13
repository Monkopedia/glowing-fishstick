package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.Error

/**
 * Created by TTLock on 2017/8/20.
 */
interface DeviceFirmwareUpdateCallback {
    fun onGetLockFirmware(specialValue: Int, module: String?, hardware: String?, firmware: String?)
    fun onStatusChanged(status: Int)
    fun onDfuProcessStarting(deviceAddress: String?)
    fun onEnablingDfuMode(deviceAddress: String?)
    fun onDfuCompleted(deviceAddress: String?)
    fun onDfuAborted(deviceAddress: String?)
    fun onProgressChanged(
        deviceAddress: String?,
        percent: Int,
        speed: Float,
        avgSpeed: Float,
        currentPart: Int,
        partsTotal: Int
    )

    fun onError(errorCode: Int, error: Error?, errorContent: String?)
}
