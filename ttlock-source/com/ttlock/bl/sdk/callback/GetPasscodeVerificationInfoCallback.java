package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/11 0011 10:40
 *
 * @author theodre
 */
public interface GetPasscodeVerificationInfoCallback extends LockCallback {

    void onGetInfoSuccess(String lockData);

    @Override
    void onFail(LockError error);
}
