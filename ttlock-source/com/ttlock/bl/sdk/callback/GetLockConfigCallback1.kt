package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError
import com.ttlock.bl.sdk.entity.TTLockConfigType

/**
 * Created on  2019/4/10 0010 11:07
 *
 * @author theodre
 */
interface GetLockConfigCallback : LockCallback {
    fun onGetLockConfigSuccess(ttLockConfigType: TTLockConfigType?, switchOn: Boolean)
    override fun onFail(error: LockError)
}
