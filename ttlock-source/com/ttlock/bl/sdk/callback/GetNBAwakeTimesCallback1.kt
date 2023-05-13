package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError
import com.ttlock.bl.sdk.entity.NBAwakeTime

/**
 * Created on  2019/4/29 0029 16:08
 *
 * @author theodre
 */
interface GetNBAwakeTimesCallback : LockCallback {
    fun onGetNBAwakeTimesSuccess(nbAwakeTimes: List<NBAwakeTime?>?)
    override fun onFail(error: LockError)
}
