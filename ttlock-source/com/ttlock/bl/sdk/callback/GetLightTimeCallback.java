package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 11:44
 *
 * @author theodre
 */
public interface GetLightTimeCallback extends LockCallback {

    void onGetLightTimeSuccess(int seconds);

    @Override
    void onFail(LockError error);
}
