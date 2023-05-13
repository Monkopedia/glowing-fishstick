package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/2 0002 13:36
 *
 * @author theodre
 */
interface ConnectLockCallback : LockCallback {
    fun onConnectSuccess()
    override fun onFail(error: LockError)
}
