package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/11 0011 10:19
 *
 * @author theodre
 */
interface GetAllValidICCardCallback : LockCallback {
    fun onGetAllValidICCardSuccess(cardDataStr: String?)
    override fun onFail(error: LockError)
}
