package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/11 0011 09:31
 *
 * @author theodre
 */
public interface WriteFingerprintDataCallback extends LockCallback {
    void onWriteDataSuccess(int fingerprintNum);

    @Override
    void onFail(LockError error);
}
