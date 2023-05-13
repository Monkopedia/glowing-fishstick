package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/2 0002 14:01
 *
 * @author theodre
 */
interface ResetLockCallback : LockCallback {
    fun onResetLockSuccess()
    override fun onFail(error: LockError)
}
