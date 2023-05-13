package com.ttlock.bl.sdk.gateway.callback

/**
 * Created by TTLock on 2017/8/20.
 */
interface DfuCallback {
    fun onDfuSuccess(deviceAddress: String?)
    fun onDfuAborted(deviceAddress: String?)
    fun onProgressChanged(
        deviceAddress: String?,
        percent: Int,
        speed: Float,
        avgSpeed: Float,
        currentPart: Int,
        partsTotal: Int
    )

    fun onError()
}
