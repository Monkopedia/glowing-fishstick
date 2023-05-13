package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/10 0010 13:27
 *
 * @author theodre
 */
interface GetOperationLogCallback : LockCallback {
    fun onGetLogSuccess(log: String?)
    override fun onFail(error: LockError)
}
