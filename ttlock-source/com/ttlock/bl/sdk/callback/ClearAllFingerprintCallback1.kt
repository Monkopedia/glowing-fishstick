package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/11 0011 10:08
 *
 * @author theodre
 */
interface ClearAllFingerprintCallback : LockCallback {
    fun onClearAllFingerprintSuccess()
    override fun onFail(error: LockError)
}
