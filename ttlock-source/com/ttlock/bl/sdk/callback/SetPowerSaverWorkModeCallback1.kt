package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/10 0010 13:13
 *
 * @author theodre
 */
interface SetPowerSaverWorkModeCallback : LockCallback {
    fun onSetPowerSaverWorkModeSuccess()
    override fun onFail(error: LockError)
}
