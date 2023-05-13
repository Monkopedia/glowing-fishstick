package com.ttlock.bl.sdk.remote.callback

import com.ttlock.bl.sdk.device.Remote

/**
 * Created on  2019/3/29 0029 15:08
 *
 * @author theodre
 */
interface ScanRemoteCallback {
    fun onScanRemote(device: Remote?)
}
