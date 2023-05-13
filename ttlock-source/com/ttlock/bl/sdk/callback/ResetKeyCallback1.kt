package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/2 0002 13:58
 *
 * @author theodre
 */
interface ResetKeyCallback : LockCallback {
    fun onResetKeySuccess(lockData: String?)
    override fun onFail(error: LockError)
}
