package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/2 0002 13:36
 *
 * @author theodre
 */
public interface ConnectLockCallback extends LockCallback{

    void onConnectSuccess();

    @Override
    void onFail(LockError error);
}
