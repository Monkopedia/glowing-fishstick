package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/11 0011 10:19
 *
 * @author theodre
 */
interface ClearAllICCardCallback : LockCallback {
    fun onClearAllICCardSuccess()
    override fun onFail(error: LockError)
}
