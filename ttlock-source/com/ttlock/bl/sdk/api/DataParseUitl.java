package com.ttlock.bl.sdk.api;

import com.ttlock.bl.sdk.entity.NBAwakeMode;
import com.ttlock.bl.sdk.entity.NBAwakeTimeType;
import com.ttlock.bl.sdk.entity.NBAwakeTime;
import com.ttlock.bl.sdk.entity.PowerSaverWorkMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TTLock on 2020/9/14.
 */
class DataParseUitl {

    static List<NBAwakeTime> parseNBActivateConfig(byte[] data) {
        List<NBAwakeTime> nbAwakeTimes = new ArrayList<>();
        if (data == null || data.length == 0) {
            return nbAwakeTimes;
        }
        int index = 0;
        int cnt = data[index++];
        while (index < data.length) {
            NBAwakeTime nbAwakeTime = new NBAwakeTime();
            nbAwakeTime.setNbAwakeTimeType(NBAwakeTimeType.getInstance(data[index++]));
            nbAwakeTime.setMinutes(data[index++] * 60 + data[index++]);
            nbAwakeTimes.add(nbAwakeTime);
        }
        return nbAwakeTimes;
    }

    static List<NBAwakeMode> parseNBActivateMode(byte activateMode) {
        List<NBAwakeMode> nbAwakeModes = new ArrayList<>();
        for (NBAwakeMode nbAwakeMode : NBAwakeMode.values()) {
            if ((activateMode & nbAwakeMode.getValue()) != 0) {
                nbAwakeModes.add(nbAwakeMode);
            }
        }
        return nbAwakeModes;
    }

    static List<PowerSaverWorkMode> parsePowerWorkModes(byte data[]) {
        List<PowerSaverWorkMode> powerSaverWorkModes = new ArrayList<>();
        if (data == null || data.length == 0) {
            return powerSaverWorkModes;
        }
        int index = 0;
        int len = data[index++];
        int powerWorkModeValue = data[index++];
        for (PowerSaverWorkMode powerSaverWorkMode : PowerSaverWorkMode.values()) {
            if ((powerWorkModeValue & powerSaverWorkMode.getValue()) != 0) {
                powerSaverWorkModes.add(powerSaverWorkMode);
            }
        }
        return powerSaverWorkModes;
    }
}
