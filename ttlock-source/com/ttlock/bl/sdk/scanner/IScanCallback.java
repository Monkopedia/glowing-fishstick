package com.ttlock.bl.sdk.scanner;

import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;

/**
 * Created by Smartlock on 2016/5/16.
 */
public interface IScanCallback {
    /**
     * scan callback
     * @param extendedBluetoothDevice
     */
     void onScan(ExtendedBluetoothDevice extendedBluetoothDevice);
}
