package com.ttlock.bl.sdk.scanner

import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice

/**
 * Created by Smartlock on 2016/5/16.
 */
interface IScanCallback {
    /**
     * scan callback
     * @param extendedBluetoothDevice
     */
    fun onScan(extendedBluetoothDevice: ExtendedBluetoothDevice)
}
