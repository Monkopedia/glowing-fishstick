package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/10 0010 10:48
 *
 * @author theodre
 */
interface SetLockMuteModeCallback : LockCallback {
    fun onSetMuteModeSuccess(enabled: Boolean)
    override fun onFail(error: LockError)
}
