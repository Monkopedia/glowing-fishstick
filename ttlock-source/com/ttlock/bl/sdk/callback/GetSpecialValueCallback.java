package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/17 0017 16:21
 *
 * @author theodre
 */
@Deprecated
public interface GetSpecialValueCallback extends LockCallback {
   void onGetSpecialValueSuccess(int specialValue);
    @Override
    void onFail(LockError error);
}
