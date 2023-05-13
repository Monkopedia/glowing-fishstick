package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/11 0011 09:31
 *
 * @author theodre
 */
interface WriteFingerprintDataCallback : LockCallback {
    fun onWriteDataSuccess(fingerprintNum: Int)
    override fun onFail(error: LockError)
}
