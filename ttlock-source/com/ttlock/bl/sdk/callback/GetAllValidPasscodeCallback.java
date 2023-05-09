package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 18:07
 *
 * @author theodre
 */
public interface GetAllValidPasscodeCallback extends LockCallback {
    void onGetAllValidPasscodeSuccess(String passcodeStr);

    @Override
    void onFail(LockError error);
}
