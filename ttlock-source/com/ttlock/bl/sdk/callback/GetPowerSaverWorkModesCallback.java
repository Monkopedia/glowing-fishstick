package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.entity.PowerSaverWorkMode;

import java.util.List;

/**
 * Created on  2019/4/29 0029 16:08
 *
 * @author theodre
 */
public interface GetPowerSaverWorkModesCallback extends LockCallback {

    void onGetPowerSaverWorkModesSuccess(List<PowerSaverWorkMode> powerSaverWorkModeList);

    @Override
    void onFail(LockError error);
}
