package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/10 0010 13:30
 *
 * @author theodre
 */
interface GetLockVersionCallback : LockCallback {
    fun onGetLockVersionSuccess(lockVersion: String?)
    override fun onFail(error: LockError)
}
