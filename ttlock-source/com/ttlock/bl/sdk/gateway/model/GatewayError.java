package com.ttlock.bl.sdk.gateway.model;

/**
 * Created by TTLock on 2019/4/24.
 */

public enum GatewayError {

    SUCCESS(0, "success"),
    FAILED(1, "failed"),
    BAD_WIFI_NAME(3, "bad wifi name"),
    BAD_WIFI_PASSWORD(4, "bad wifi password"),

    INVALID_COMMAND(6, "invalid command"),

    TIME_OUT(7, "time out"),//7
    NO_SIM_CARD(8, "no sim card"),//8
    NO_CABLE(9, "no cable"),//9

    FAILED_TO_CONFIGURE_ROUTER(0x200,"failed to configure router"),
    FAILED_TO_CONFIGURE_SERVER(0x201,"failed to configure server"),
    FAILED_TO_CONFIGURE_ACCOUNT(0x202,"failed to configure account"),



    COMMUNICATION_DISCONNECTED(0x203, "communication disconnected"),
    UNCONNECTED(0x204, "please connect the gateway first"),
    CONNECT_TIMEOUT(0x205, "connect the device time out"),

//    GATEWAY_CONNECT_FAIL(0x400,"gateway connect time out"),
//    GATEWAY_IS_BUSY(0x401,"only one command can be proceed at a time"),

    DATA_FORMAT_ERROR(0x403,"parameter format or content is incorrect");



    private int errorCode;
    private String description;

    private GatewayError(int errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }

    public static GatewayError getInstance(int errorCode) {
        switch (errorCode) {
            case 0:
                return SUCCESS;
            case 1:
                return FAILED;
            case 3:
                return BAD_WIFI_NAME;
            case 4:
                return BAD_WIFI_PASSWORD;
            case 6:
                return INVALID_COMMAND;
            case 7:
                return TIME_OUT;
            case 8:
                return NO_SIM_CARD;
            case 9:
                return NO_CABLE;
            default:
                return FAILED;
        }
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }
}
