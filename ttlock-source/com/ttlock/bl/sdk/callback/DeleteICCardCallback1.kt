package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/11 0011 10:18
 *
 * @author theodre
 */
interface DeleteICCardCallback : LockCallback {
    fun onDeleteICCardSuccess()
    override fun onFail(error: LockError)
}
