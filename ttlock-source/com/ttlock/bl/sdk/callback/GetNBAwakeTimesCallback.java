package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.entity.NBAwakeTime;

import java.util.List;

/**
 * Created on  2019/4/29 0029 16:08
 *
 * @author theodre
 */
public interface GetNBAwakeTimesCallback extends LockCallback {

    void onGetNBAwakeTimesSuccess(List<NBAwakeTime> nbAwakeTimes);

    @Override
    void onFail(LockError error);
}
