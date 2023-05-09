package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.entity.UnlockDirection;

/**
 * Created on  2019/4/29 0029 16:08
 *
 * @author theodre
 */
public interface GetUnlockDirectionCallback extends LockCallback {

    void onGetUnlockDirectionSuccess(UnlockDirection unlockDirection);

    @Override
    void onFail(LockError error);
}
