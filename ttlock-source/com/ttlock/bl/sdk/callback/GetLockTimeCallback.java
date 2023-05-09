package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 13:25
 *
 * @author theodre
 */
public interface GetLockTimeCallback extends LockCallback {
    void onGetLockTimeSuccess(long lockTimestamp);

    @Override
    void onFail(LockError error);
}
