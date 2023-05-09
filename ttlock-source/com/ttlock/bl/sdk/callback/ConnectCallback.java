package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.device.TTDevice;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;

/**
 * Created on  2019/4/12 0012 11:59
 *
 * @author theodre
 */
public interface ConnectCallback extends LockCallback {
    void onConnectSuccess(TTDevice device);
    @Override
    void onFail(LockError error);
}
