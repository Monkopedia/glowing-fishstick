package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError
import com.ttlock.bl.sdk.entity.PowerSaverWorkMode

/**
 * Created on  2019/4/29 0029 16:08
 *
 * @author theodre
 */
interface GetPowerSaverWorkModesCallback : LockCallback {
    fun onGetPowerSaverWorkModesSuccess(powerSaverWorkModeList: List<PowerSaverWorkMode?>?)
    override fun onFail(error: LockError)
}
