package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/10 0010 10:49
 *
 * @author theodre
 */
interface GetLockMuteModeStateCallback : LockCallback {
    fun onGetMuteModeStateSuccess(enabled: Boolean)
    override fun onFail(error: LockError)
}
