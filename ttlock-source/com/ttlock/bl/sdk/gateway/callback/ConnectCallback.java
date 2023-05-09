package com.ttlock.bl.sdk.gateway.callback;

import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;

/**
 * Created on  2019/4/12 0012 11:59
 *
 * @author theodre
 */
public interface ConnectCallback {
    void onConnectSuccess(ExtendedBluetoothDevice device);
    void onDisconnected();
//    @Override
//    void onFail(GatewayError error);
}
