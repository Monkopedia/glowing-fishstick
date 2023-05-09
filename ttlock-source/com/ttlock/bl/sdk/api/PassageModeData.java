package com.ttlock.bl.sdk.api;

/**
 * Created by TTLock on 2019/1/24.
 */

public class PassageModeData {

    public int type;
    public int weekOrDay;
    public int month;
    public int startDate;
    public int endDate;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWeekOrDay() {
        return weekOrDay;
    }

    public void setWeekOrDay(int weekOrDay) {
        this.weekOrDay = weekOrDay;
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
