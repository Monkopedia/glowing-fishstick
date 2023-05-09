package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 10:25
 *
 * @author theodre
 */
public interface InitKeypadCallback extends LockCallback {
    void  onInitKeypadSuccess(int specialValue);
    
    @Override
    void onFail(LockError error);
}
