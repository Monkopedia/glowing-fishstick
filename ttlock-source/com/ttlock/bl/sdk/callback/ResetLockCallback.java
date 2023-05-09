package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/2 0002 14:01
 *
 * @author theodre
 */
public interface ResetLockCallback extends LockCallback{
    void onResetLockSuccess();

    @Override
    void onFail(LockError error);
}
