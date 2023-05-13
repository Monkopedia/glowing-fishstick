package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError
import com.ttlock.bl.sdk.entity.NBAwakeMode

/**
 * Created on  2019/4/29 0029 16:08
 *
 * @author theodre
 */
interface GetNBAwakeModesCallback : LockCallback {
    fun onGetNBAwakeModesSuccess(nbAwakeModeList: List<NBAwakeMode?>?)
    override fun onFail(error: LockError)
}
