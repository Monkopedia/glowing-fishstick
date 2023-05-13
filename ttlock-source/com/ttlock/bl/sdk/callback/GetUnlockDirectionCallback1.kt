package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError
import com.ttlock.bl.sdk.entity.UnlockDirection

/**
 * Created on  2019/4/29 0029 16:08
 *
 * @author theodre
 */
interface GetUnlockDirectionCallback : LockCallback {
    fun onGetUnlockDirectionSuccess(unlockDirection: UnlockDirection?)
    override fun onFail(error: LockError)
}
