package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/10 0010 11:09
 *
 * @author theodre
 */
interface GetPasscodeVisibleStateCallback : LockCallback {
    fun onGetPasscodeVisibleStateSuccess(visible: Boolean)
    override fun onFail(error: LockError)
}
