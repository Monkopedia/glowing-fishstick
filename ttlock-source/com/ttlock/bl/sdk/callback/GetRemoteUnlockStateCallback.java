package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 11:01
 *
 * @author theodre
 */
public interface GetRemoteUnlockStateCallback extends LockCallback {

    void onGetRemoteUnlockSwitchStateSuccess(boolean enabled);

    @Override
    void onFail(LockError error);
}
