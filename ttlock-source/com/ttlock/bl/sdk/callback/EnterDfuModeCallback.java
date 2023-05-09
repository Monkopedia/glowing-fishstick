package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/11 0011 09:18
 *
 * @author theodre
 */
public interface EnterDfuModeCallback extends LockCallback {

   public void onEnterDfuMode();

    @Override
    void onFail(LockError error);
}
