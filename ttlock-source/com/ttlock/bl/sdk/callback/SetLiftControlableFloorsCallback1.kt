package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created by TTLock on 2020/7/24.
 */
interface SetLiftControlableFloorsCallback : LockCallback {
    fun onSetLiftControlableFloorsSuccess()
    override fun onFail(error: LockError)
}
