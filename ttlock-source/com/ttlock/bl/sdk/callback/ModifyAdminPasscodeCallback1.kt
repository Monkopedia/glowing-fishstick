package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/10 0010 13:56
 *
 * @author theodre
 */
interface ModifyAdminPasscodeCallback : LockCallback {
    fun onModifyAdminPasscodeSuccess(passcode: String?)
    override fun onFail(error: LockError)
}
