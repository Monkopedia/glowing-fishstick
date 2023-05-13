package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.device.WirelessKeypad
import com.ttlock.bl.sdk.entity.LockError

/**
 * Created on  2019/3/29 0029 15:08
 *
 * @author theodre
 */
interface ScanKeypadCallback : LockCallback {
    fun onScanKeyboardSuccess(device: WirelessKeypad?)
    override fun onFail(error: LockError)
}
