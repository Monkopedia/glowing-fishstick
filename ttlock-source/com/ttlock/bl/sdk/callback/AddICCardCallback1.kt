package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/11 0011 10:11
 *
 * @author theodre
 */
interface AddICCardCallback : LockCallback {
    fun onEnterAddMode()
    fun onAddICCardSuccess(cardNum: Long)
    override fun onFail(error: LockError)
}
