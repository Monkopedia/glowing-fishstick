package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.ActivateLiftFloorsResult
import com.ttlock.bl.sdk.entity.LockError

/**
 * Created by TTLock on 2020/7/24.
 */
interface ActivateLiftFloorsCallback : LockCallback {
    fun onActivateLiftFloorsSuccess(activateLiftFloorsResult: ActivateLiftFloorsResult?)
    override fun onFail(error: LockError)
}
