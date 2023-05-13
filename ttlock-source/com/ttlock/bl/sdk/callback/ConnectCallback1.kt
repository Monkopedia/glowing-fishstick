package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.device.TTDevice
import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/12 0012 11:59
 *
 * @author theodre
 */
interface ConnectCallback : LockCallback {
    fun onConnectSuccess(device: TTDevice)
    override fun onFail(error: LockError)
}
