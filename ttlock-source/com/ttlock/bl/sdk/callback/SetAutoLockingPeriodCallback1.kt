package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/18 0018 10:35
 *
 * @author theodre
 */
interface SetAutoLockingPeriodCallback : LockCallback {
    fun onSetAutoLockingPeriodSuccess()
    override fun onFail(error: LockError)
}
