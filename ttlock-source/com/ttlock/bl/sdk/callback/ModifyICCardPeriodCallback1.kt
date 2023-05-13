package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/11 0011 10:15
 *
 * @author theodre
 */
interface ModifyICCardPeriodCallback : LockCallback {
    fun onModifyICCardPeriodSuccess()
    override fun onFail(error: LockError)
}
