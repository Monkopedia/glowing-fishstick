package com.ttlock.bl.sdk.remote.callback

import com.ttlock.bl.sdk.device.Remote
import com.ttlock.bl.sdk.remote.model.RemoteError

/**
 * Created on  2019/4/12 0012 11:59
 *
 * @author theodre
 */
interface ConnectCallback : RemoteCallback {
    fun onConnectSuccess(device: Remote?)
    override fun onFail(error: RemoteError?)
}
