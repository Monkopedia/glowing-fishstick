package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/10 0010 18:07
 *
 * @author theodre
 */
interface GetAllValidPasscodeCallback : LockCallback {
    fun onGetAllValidPasscodeSuccess(passcodeStr: String?)
    override fun onFail(error: LockError)
}
