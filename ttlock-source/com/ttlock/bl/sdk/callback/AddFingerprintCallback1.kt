package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/11 0011 09:18
 *
 * @author theodre
 */
interface AddFingerprintCallback : LockCallback {
    fun onEnterAddMode(totalCount: Int)
    fun onCollectFingerprint(currentCount: Int)
    fun onAddFingerpintFinished(fingerprintNum: Long)
    override fun onFail(error: LockError)
}
