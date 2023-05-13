package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/10 0010 10:59
 *
 * @author theodre
 */
interface AddRemoteCallback : LockCallback {
    fun onAddSuccess()
    override fun onFail(error: LockError)
}
