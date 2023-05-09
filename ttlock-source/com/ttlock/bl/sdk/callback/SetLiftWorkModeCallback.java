package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created by TTLock on 2020/7/24.
 */
public interface SetLiftWorkModeCallback extends LockCallback {
    void onSetLiftWorkModeSuccess();
    @Override
    void onFail(LockError error);
}
