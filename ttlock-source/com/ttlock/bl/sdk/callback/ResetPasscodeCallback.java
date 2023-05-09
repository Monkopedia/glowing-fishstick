package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 14:02
 *
 * @author theodre
 */
public interface ResetPasscodeCallback extends LockCallback {

    void onResetPasscodeSuccess(String lockData);

    @Override
    void onFail(LockError error);
}
