package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/29 0029 16:08
 *
 * @author theodre
 */
public interface SetNBServerCallback extends LockCallback {

    void onSetNBServerSuccess(int battery);

    @Override
    void onFail(LockError error);
}
