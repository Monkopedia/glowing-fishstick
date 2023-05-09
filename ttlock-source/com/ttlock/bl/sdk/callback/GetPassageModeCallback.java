package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/17 0017 10:26
 *
 * @author theodre
 */
public interface GetPassageModeCallback extends LockCallback {
    void onGetPassageModeSuccess(String passageModeData);

    @Override
    void onFail(LockError error);
}
