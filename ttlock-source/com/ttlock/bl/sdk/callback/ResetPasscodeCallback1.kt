package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/10 0010 14:02
 *
 * @author theodre
 */
interface ResetPasscodeCallback : LockCallback {
    fun onResetPasscodeSuccess(lockData: String?)
    override fun onFail(error: LockError)
}
