package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/10 0010 11:50
 *
 * @author theodre
 */
interface ClearPassageModeCallback : LockCallback {
    fun onClearPassageModeSuccess()
    override fun onFail(error: LockError)
}
