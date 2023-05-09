package com.ttlock.bl.sdk.wirelessdoorsensor.model;

/**
 * Created by TTLock on 2019/4/24.
 */

public enum DoorSensorError {

    SUCCESS(0, "success"),
    FAILED(1, "failed"),


    NO_RESPONSE(0x200, "no response"),

    CONNECT_FAIL(0x400,"device connect time out"),
    Device_IS_BUSY(0x401,"only one command can be proceed at a time"),
    DATA_FORMAT_ERROR(0x403,"parameter format or content is incorrect");


    private int errorCode;
    private String description;

    private DoorSensorError(int errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }

    public static DoorSensorError getInstance(int errorCode) {
        switch (errorCode) {
            case 0:
                return SUCCESS;
            case 1:
                return FAILED;
            default:
                return FAILED;
        }
    }

    public String getDescription() {
        return description;
    }
}
