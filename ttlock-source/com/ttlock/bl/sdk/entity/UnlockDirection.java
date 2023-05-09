package com.ttlock.bl.sdk.entity;

/**
 * Created by TTLock on 2020/12/16.
 */
public enum UnlockDirection {
    LEFT(1),
    RIGHT(0);
    int value;

    private UnlockDirection(int value) {
        this.value = value;
    }

    public static UnlockDirection getInstance(int value) {
        switch (value) {
            case 1:
                return LEFT;
            default:
                return RIGHT;
        }
    }

    public int getValue() {
        return value;
    }
}
