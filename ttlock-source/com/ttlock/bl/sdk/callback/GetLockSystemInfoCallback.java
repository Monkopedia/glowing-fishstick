package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.DeviceInfo;
import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/8 0008 14:54
 *
 * @author theodre
 */
public interface GetLockSystemInfoCallback extends LockCallback{
    void onGetLockSystemInfoSuccess(DeviceInfo info);

    @Override
    void onFail(LockError error);
}
