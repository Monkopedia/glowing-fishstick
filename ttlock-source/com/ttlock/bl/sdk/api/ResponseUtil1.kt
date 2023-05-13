package com.ttlock.bl.sdk.api

import com.ttlock.bl.sdk.callback.GetPowerSaverWorkModesCallback
import com.ttlock.bl.sdk.callback.LockCallback
import com.ttlock.bl.sdk.entity.HotelData
import com.ttlock.bl.sdk.util.LogUtil
import java.util.*

/**
 * Created by TTLock on 2020/11/9.
 */
internal object ResponseUtil {
    fun getHotelData(data: ByteArray) {
        val lockCallback: LockCallback? = LockCallbackManager.Companion.getInstance().getCallback()
        if (lockCallback == null) {
            LogUtil.w("lockCallback is null")
            return
        }
        when (data[4]) {
            HotelData.Companion.TYPE_POWER_SAVER_WORK_MODE -> {
                val powerSaverWorkModes =
                    DataParseUitl.parsePowerWorkModes(Arrays.copyOfRange(data, 5, data.size))
                (lockCallback as GetPowerSaverWorkModesCallback).onGetPowerSaverWorkModesSuccess(
                    powerSaverWorkModes
                )
            }
        }
    }
}
