package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/11 0011 10:07
 *
 * @author theodre
 */
interface DeleteFingerprintCallback : LockCallback {
    fun onDeleteFingerprintSuccess()
    override fun onFail(error: LockError)
}
