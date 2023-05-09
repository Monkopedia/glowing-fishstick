package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 11:09
 *
 * @author theodre
 */
public interface GetPasscodeVisibleStateCallback extends LockCallback {

    void onGetPasscodeVisibleStateSuccess(boolean visible);

    @Override
    void onFail(LockError error);
}
