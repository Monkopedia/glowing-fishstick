package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/10 0010 13:25
 *
 * @author theodre
 */
interface GetLockTimeCallback : LockCallback {
    fun onGetLockTimeSuccess(lockTimestamp: Long)
    override fun onFail(error: LockError)
}
