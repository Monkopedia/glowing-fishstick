package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 11:47
 *
 * @author theodre
 */
public interface DeletePassageModeCallback extends LockCallback {

    void onDeletePassageModeSuccess();

    @Override
    void onFail(LockError error);
}
