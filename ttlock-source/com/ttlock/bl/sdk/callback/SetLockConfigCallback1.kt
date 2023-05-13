package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError
import com.ttlock.bl.sdk.entity.TTLockConfigType

/**
 * Created on  2019/4/10 0010 11:07
 *
 * @author theodre
 */
interface SetLockConfigCallback : LockCallback {
    fun onSetLockConfigSuccess(ttLockConfigType: TTLockConfigType?)
    override fun onFail(error: LockError)
}
