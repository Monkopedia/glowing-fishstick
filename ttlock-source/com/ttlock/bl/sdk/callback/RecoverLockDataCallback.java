package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 13:31
 *
 * @author theodre
 */
public interface RecoverLockDataCallback extends LockCallback {

    void onRecoveryDataSuccess(int dataType);

    @Override
    void onFail(LockError error);
}
