package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/19 0019 10:44
 *
 * @author theodre
 */
public interface GetAdminPasscodeCallback extends LockCallback{
    void onGetAdminPasscodeSuccess(String passcode);

    @Override
    void onFail(LockError error);
}
