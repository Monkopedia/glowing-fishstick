package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/11 0011 10:11
 *
 * @author theodre
 */
public interface AddICCardCallback extends LockCallback {

    void onEnterAddMode();

    void onAddICCardSuccess(long cardNum);

    @Override
    void onFail(LockError error);
}
