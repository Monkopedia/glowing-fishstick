package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.AccessoryInfo;
import com.ttlock.bl.sdk.entity.LockError;

/**
 * Created on  2019/4/29 0029 16:08
 *
 * @author theodre
 */
public interface GetAccessoryBatteryLevelCallback extends LockCallback {

    void onGetAccessoryBatteryLevelSuccess(AccessoryInfo accessoryInfo);

    @Override
    void onFail(LockError error);
}
