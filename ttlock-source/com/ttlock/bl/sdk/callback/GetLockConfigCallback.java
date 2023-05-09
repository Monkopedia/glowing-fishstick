package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.TTLockConfigType;
import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 11:07
 *
 * @author theodre
 */
public interface GetLockConfigCallback extends LockCallback {

    void onGetLockConfigSuccess(TTLockConfigType ttLockConfigType, boolean switchOn);

    @Override
    void onFail(LockError error);
}
