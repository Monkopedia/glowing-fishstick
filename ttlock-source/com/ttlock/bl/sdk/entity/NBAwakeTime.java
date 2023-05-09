package com.ttlock.bl.sdk.entity;

/**
 * Created by TTLock on 2020/9/14.
 */
public class NBAwakeTime {
    private NBAwakeTimeType nbAwakeTimeType;
    private int minutes;

    public NBAwakeTime() {
    }

    public NBAwakeTime(NBAwakeTimeType nbAwakeTimeType, int minutes) {
        this.nbAwakeTimeType = nbAwakeTimeType;
        this.minutes = minutes;
    }

    public NBAwakeTimeType getNbAwakeTimeType() {
        return nbAwakeTimeType;
    }

    public void setNbAwakeTimeType(NBAwakeTimeType nbAwakeTimeType) {
        this.nbAwakeTimeType = nbAwakeTimeType;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
