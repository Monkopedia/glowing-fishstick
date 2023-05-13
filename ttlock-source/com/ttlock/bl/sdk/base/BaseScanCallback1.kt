package com.ttlock.bl.sdk.base

import com.ttlock.bl.sdk.device.TTDevice

/**
 * Created on  2019/3/29 0029 15:08
 *
 * @author theodre
 */
interface BaseScanCallback<T : TTDevice?> {
    fun onScan(device: T)
}
