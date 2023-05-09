package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 10:49
 *
 * @author theodre
 */
public interface GetLockMuteModeStateCallback extends LockCallback {

    void onGetMuteModeStateSuccess(boolean enabled);

    @Override
    void onFail(LockError error);
}
