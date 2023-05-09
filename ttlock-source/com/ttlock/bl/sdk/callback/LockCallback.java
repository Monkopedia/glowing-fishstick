package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/8 0008 15:31
 *
 * @author theodore
 */
public interface LockCallback {
    void onFail(LockError error);
}
