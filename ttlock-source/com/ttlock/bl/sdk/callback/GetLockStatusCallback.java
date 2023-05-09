package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/18 0018 10:12
 *
 * @author theodre
 */
public interface GetLockStatusCallback extends LockCallback {
    /**
     *
     * @param status 0-lock 1-unlock  2-unknown status 3-unlocked,has car top
     */
    void onGetLockStatusSuccess(int status);
    @Override
    void onFail(LockError error);
}
