package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 13:44
 *
 * @author theodre
 */
public interface CreateCustomPasscodeCallback extends LockCallback {
    void onCreateCustomPasscodeSuccess(String passcode);

    @Override
    void onFail(LockError error);
}
