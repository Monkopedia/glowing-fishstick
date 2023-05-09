package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 13:30
 *
 * @author theodre
 */
public interface GetLockVersionCallback extends LockCallback {
    void onGetLockVersionSuccess(String lockVersion);

    @Override
    void onFail(LockError error);
}
