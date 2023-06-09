package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/18 0018 10:35
 *
 * @author theodre
 */
interface GetAutoLockingPeriodCallback : LockCallback {
    fun onGetAutoLockingPeriodSuccess(currtentTime: Int, minTime: Int, maxTime: Int)
    override fun onFail(error: LockError)
}
