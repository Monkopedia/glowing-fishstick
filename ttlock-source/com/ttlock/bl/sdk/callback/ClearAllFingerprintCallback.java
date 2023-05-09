package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/11 0011 10:08
 *
 * @author theodre
 */
public interface ClearAllFingerprintCallback extends LockCallback{

    void onClearAllFingerprintSuccess();

    @Override
    void onFail(LockError error);

}
