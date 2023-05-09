package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 13:27
 *
 * @author theodre
 */
public interface GetOperationLogCallback extends LockCallback {
    void onGetLogSuccess(String log);

    @Override
    void onFail(LockError error);
}
