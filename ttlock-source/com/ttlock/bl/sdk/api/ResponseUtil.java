package com.ttlock.bl.sdk.api;

import com.ttlock.bl.sdk.callback.GetPowerSaverWorkModesCallback;
import com.ttlock.bl.sdk.callback.LockCallback;
import com.ttlock.bl.sdk.entity.HotelData;
import com.ttlock.bl.sdk.entity.PowerSaverWorkMode;
import com.ttlock.bl.sdk.util.LogUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by TTLock on 2020/11/9.
 */
class ResponseUtil {

    static void getHotelData(byte[] data) {
        LockCallback lockCallback = LockCallbackManager.getInstance().getCallback();
        if (lockCallback == null) {
            LogUtil.w("lockCallback is null");
            return;
        }
        switch (data[4]) {
            case HotelData.TYPE_POWER_SAVER_WORK_MODE:
                List<PowerSaverWorkMode> powerSaverWorkModes = DataParseUitl.parsePowerWorkModes(Arrays.copyOfRange(data, 5, data.length));
                ((GetPowerSaverWorkModesCallback)lockCallback).onGetPowerSaverWorkModesSuccess(powerSaverWorkModes);
                break;
        }
    }

}
