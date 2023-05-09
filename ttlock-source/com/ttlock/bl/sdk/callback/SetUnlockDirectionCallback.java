package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/29 0029 16:08
 *
 * @author theodre
 */
public interface SetUnlockDirectionCallback extends LockCallback {

    void onSetUnlockDirectionSuccess();

    @Override
    void onFail(LockError error);
}
