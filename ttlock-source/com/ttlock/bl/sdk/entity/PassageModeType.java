package com.ttlock.bl.sdk.entity;

/**
 * Created by TTLock on 2019/1/25.
 */

public enum PassageModeType {
    Weekly(1),
    Monthly(2);

    private int value;

    PassageModeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
