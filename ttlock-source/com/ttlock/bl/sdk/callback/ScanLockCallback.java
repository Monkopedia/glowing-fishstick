package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/3/29 0029 15:08
 *
 * @author theodre
 */
public interface ScanLockCallback extends LockCallback {
    void onScanLockSuccess(ExtendedBluetoothDevice device);

    @Override
    void onFail(LockError error);
}
