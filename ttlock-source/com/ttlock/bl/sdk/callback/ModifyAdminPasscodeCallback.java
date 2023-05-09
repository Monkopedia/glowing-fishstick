package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/10 0010 13:56
 *
 * @author theodre
 */
public interface ModifyAdminPasscodeCallback extends LockCallback {

    void onModifyAdminPasscodeSuccess(String passcode);

    @Override
    void onFail(LockError error);
}