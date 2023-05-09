package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.ActivateLiftFloorsResult;
import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created by TTLock on 2020/7/24.
 */
public interface ActivateLiftFloorsCallback extends LockCallback {
    void onActivateLiftFloorsSuccess(ActivateLiftFloorsResult activateLiftFloorsResult);
    @Override
    void onFail(LockError error);
}
