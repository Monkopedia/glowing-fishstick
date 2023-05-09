package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.entity.NBAwakeMode;

import java.util.List;

/**
 * Created on  2019/4/29 0029 16:08
 *
 * @author theodre
 */
public interface GetNBAwakeModesCallback extends LockCallback {

    void onGetNBAwakeModesSuccess(List<NBAwakeMode> nbAwakeModeList);

    @Override
    void onFail(LockError error);
}
