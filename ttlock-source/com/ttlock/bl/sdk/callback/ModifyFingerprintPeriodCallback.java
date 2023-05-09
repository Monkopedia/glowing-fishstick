package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/11 0011 10:04
 *
 * @author theodre
 */
public interface ModifyFingerprintPeriodCallback extends LockCallback {

    void onModifyPeriodSuccess();

    @Override
    public void onFail(LockError error);
}
