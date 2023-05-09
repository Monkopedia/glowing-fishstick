package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/2 0002 13:58
 *
 * @author theodre
 */
public interface ResetKeyCallback extends LockCallback {
    void onResetKeySuccess(String lockData);

    @Override
    void onFail(LockError error);
}
