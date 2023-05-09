package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/11 0011 10:09
 *
 * @author theodre
 */
public interface GetAllValidFingerprintCallback extends LockCallback {

    void onGetAllFingerprintsSuccess(String fingerprintStr);

    @Override
    void onFail(LockError error);
}
