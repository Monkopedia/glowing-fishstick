package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/19 0019 10:44
 *
 * @author theodre
 */
interface GetAdminPasscodeCallback : LockCallback {
    fun onGetAdminPasscodeSuccess(passcode: String?)
    override fun onFail(error: LockError)
}
