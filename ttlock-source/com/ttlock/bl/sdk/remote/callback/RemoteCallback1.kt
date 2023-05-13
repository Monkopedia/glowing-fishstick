package com.ttlock.bl.sdk.remote.callback

import com.ttlock.bl.sdk.remote.model.RemoteError

/**
 * Created on  2019/4/8 0008 15:31
 *
 * @author theodore
 */
interface RemoteCallback {
    fun onFail(error: RemoteError?)
}
