package com.ttlock.bl.sdk.entity;

import com.ttlock.bl.sdk.util.LogUtil;

import java.io.Serializable;

/**
 * Created by TTLock on 2020/3/30.
 */
public class CyclicConfig implements Serializable {

    public static final int MAX_DAY_MINUTES = 24 * 60;
    /**
     * 1-7 means monday to sunday
     */
    public int weekDay;
    /**
     * the minutes of cycle start time
     */
    public int startTime;
    /**
     * the minutes of cycle end time
     */
    public int endTime;

    public CyclicConfig() {
    }

    public CyclicConfig(int weekDay, int startTime, int endTime) {
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public boolean isValidData() {
        if (weekDay >= 1 && weekDay <= 7
        && startTime >= 0 && startTime <= MAX_DAY_MINUTES
        && endTime >= 0 && endTime <= MAX_DAY_MINUTES
        && startTime <= endTime) {
            return true;
        }
        LogUtil.d(this.toString());
        return false;
    }

    @Override
    public String toString() {
        return "CyclicConfig{" +
                "weekDay=" + weekDay +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
