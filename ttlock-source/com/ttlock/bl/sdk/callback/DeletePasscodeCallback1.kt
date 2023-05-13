package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/18 0018 17:24
 *
 * @author theodre
 */
interface DeletePasscodeCallback : LockCallback {
    fun onDeletePasscodeSuccess()
    override fun onFail(error: LockError)
}
