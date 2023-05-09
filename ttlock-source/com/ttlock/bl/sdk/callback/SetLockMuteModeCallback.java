package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 10:48
 *
 * @author theodre
 */
public interface SetLockMuteModeCallback extends LockCallback {
    void onSetMuteModeSuccess(boolean enabled);

    @Override
    void onFail(LockError error);
}
