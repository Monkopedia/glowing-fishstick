package com.ttlock.bl.sdk.gateway.callback;


import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;

/**
 * Created on  2019/3/29 0029 15:08
 *
 * @author theodre
 */
public interface ScanGatewayCallback {
    void onScanGatewaySuccess(ExtendedBluetoothDevice device);
    void onScanFailed(int errorCode);
}
