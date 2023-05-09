package com.ttlock.bl.sdk.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TTLock on 2020/9/14.
 */
public class NBAwakeConfig {
    public static final byte ACTION_AWAKE_MODE = 1;
    public static final byte ACTION_AWAKE_TIME = 2;

    private List<NBAwakeMode> nbAwakeModeList;
    private List<NBAwakeTime> nbAwakeTimeList;

    public List<NBAwakeMode> getNbAwakeModeList() {
        return nbAwakeModeList;
    }

    public void setNbAwakeModeList(List<NBAwakeMode> nbAwakeModeList) {
        if (nbAwakeModeList == null) {
            nbAwakeModeList = new ArrayList<>();
        }
        this.nbAwakeModeList = nbAwakeModeList;
    }

    public List<NBAwakeTime> getNbAwakeTimeList() {
        return nbAwakeTimeList;
    }

    public void setNbAwakeTimeList(List<NBAwakeTime> nbAwakeTimeList) {
        if (nbAwakeTimeList == null) {
            nbAwakeTimeList = new ArrayList<>();
        }
        this.nbAwakeTimeList = nbAwakeTimeList;
    }
}
