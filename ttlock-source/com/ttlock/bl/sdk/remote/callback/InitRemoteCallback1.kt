package com.ttlock.bl.sdk.remote.callback

import com.ttlock.bl.sdk.remote.model.InitRemoteResult

/**
 * Created on  2019/4/10 0010 10:25
 *
 */
interface InitRemoteCallback : RemoteCallback {
    fun onInitSuccess(initKeyFobResult: InitRemoteResult?)
}
