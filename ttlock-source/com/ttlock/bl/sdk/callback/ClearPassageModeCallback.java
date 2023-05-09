package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 11:50
 *
 * @author theodre
 */
public interface ClearPassageModeCallback extends LockCallback {

    void onClearPassageModeSuccess();

    @Override
    public void onFail(LockError error);
}
