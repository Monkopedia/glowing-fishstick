package com.ttlock.bl.sdk.entity;

import com.ttlock.bl.sdk.constant.Constant;
import com.ttlock.bl.sdk.util.LogUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TTLock on 2020/3/19.
 */
public class ValidityInfo implements Serializable {
    public static final int TIMED = 1;
    public static final int CYCLIC = 4;


    private int modeType = TIMED;

    /**
     * default permanent period
     */
    private long startDate = Constant.permanentStartDate;
    private long endDate = Constant.permanentEndDate;

    private List<CyclicConfig> cyclicConfigs;

    public static int getTIMED() {
        return TIMED;
    }

    public static int getCYCLIC() {
        return CYCLIC;
    }

    public int getModeType() {
        return modeType;
    }

    public void setModeType(int modeType) {
        this.modeType = modeType;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        if (startDate == 0) {
            startDate = Constant.permanentStartDate;
        }
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        if (endDate == 0) {
            endDate = Constant.permanentEndDate;
        }
        this.endDate = endDate;
    }

    public List<CyclicConfig> getCyclicConfigs() {
        return cyclicConfigs;
    }

    public void setCyclicConfigs(List<CyclicConfig> cyclicConfigs) {
        this.cyclicConfigs = cyclicConfigs;
    }

    private boolean isValidCyclicConfig() {
        if (cyclicConfigs == null) {
            return false;
        }
        for (CyclicConfig cyclicConfig : cyclicConfigs) {
            if (!cyclicConfig.isValidData()) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidData() {
        if (startDate > endDate) {
            LogUtil.d("startDate > endDate");
            return false;
        }
        switch (modeType) {
            case TIMED:
                break;
            case CYCLIC:
                return isValidCyclicConfig();
            default:
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ValidityInfo{" +
                "modeType=" + modeType +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", cyclicConfigs=" + cyclicConfigs +
                '}';
    }

}
