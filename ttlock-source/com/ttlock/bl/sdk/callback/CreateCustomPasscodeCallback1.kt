package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/10 0010 13:44
 *
 * @author theodre
 */
interface CreateCustomPasscodeCallback : LockCallback {
    fun onCreateCustomPasscodeSuccess(passcode: String?)
    override fun onFail(error: LockError)
}
