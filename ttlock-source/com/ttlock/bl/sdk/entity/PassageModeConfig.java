package com.ttlock.bl.sdk.entity;

/**
 * Created on  2019/4/17 0017 10:13
 *
 * @author theodre
 */
public class PassageModeConfig {
     private PassageModeType modeType;
    /**
     * 1~7 周一到周日
     * json数组
     * 0: means ervery day
     */
    private String repeatWeekOrDays;
    private int month;

    /**
     * the minutes of cycle start time
     * 0:0 means all day
     */
    private int startDate;

    /**
     * the minutes of cycle end time
     */
    private int endDate;

    public PassageModeType getModeType() {
        return modeType;
    }

    public void setModeType(PassageModeType modeType) {
        this.modeType = modeType;
    }

    public String getRepeatWeekOrDays() {
        return repeatWeekOrDays;
    }

    public void setRepeatWeekOrDays(String repeatWeekOrDays) {
        this.repeatWeekOrDays = repeatWeekOrDays;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }
}
