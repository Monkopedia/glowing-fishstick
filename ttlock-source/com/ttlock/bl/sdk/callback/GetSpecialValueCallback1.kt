package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/17 0017 16:21
 *
 * @author theodre
 */
@Deprecated("")
interface GetSpecialValueCallback : LockCallback {
    fun onGetSpecialValueSuccess(specialValue: Int)
    override fun onFail(error: LockError)
}
