package com.ttlock.bl.sdk.remote.callback

import com.ttlock.bl.sdk.remote.model.SystemInfo

/**
 * Created on  2019/4/10 0010 10:25
 *
 */
interface GetRemoteSystemInfoCallback : RemoteCallback {
    fun onGetRemoteSystemInfoSuccess(systemInfo: SystemInfo?)
}
