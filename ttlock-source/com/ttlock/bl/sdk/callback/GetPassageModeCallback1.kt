package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/4/17 0017 10:26
 *
 * @author theodre
 */
interface GetPassageModeCallback : LockCallback {
    fun onGetPassageModeSuccess(passageModeData: String?)
    override fun onFail(error: LockError)
}
