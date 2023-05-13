package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created by TTLock on 2020/7/24.
 */
interface SetLiftWorkModeCallback : LockCallback {
    fun onSetLiftWorkModeSuccess()
    override fun onFail(error: LockError)
}
