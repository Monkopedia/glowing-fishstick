package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/11 0011 10:04
 *
 * @author theodre
 */
interface ModifyFingerprintPeriodCallback : LockCallback {
    fun onModifyPeriodSuccess()
    override fun onFail(error: LockError)
}
