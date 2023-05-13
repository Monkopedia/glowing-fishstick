package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/10 0010 11:07
 *
 * @author theodre
 */
interface SetPasscodeVisibleCallback : LockCallback {
    fun onSetPasscodeVisibleSuccess(visible: Boolean)
    override fun onFail(error: LockError)
}
