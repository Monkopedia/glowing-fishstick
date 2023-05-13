package com.ttlock.bl.sdk.gateway.model

/**
 * Created by TTLock on 2019/4/24.
 */
enum class GatewayError(private val errorCode: Int, private val description: String) {
    SUCCESS(0, "success"), FAILED(1, "failed"), BAD_WIFI_NAME(
        3,
        "bad wifi name"
    ),
    BAD_WIFI_PASSWORD(4, "bad wifi password"), INVALID_COMMAND(6, "invalid command"), TIME_OUT(
        7,
        "time out"
    ), // 7
    NO_SIM_CARD(8, "no sim card"), // 8
    NO_CABLE(9, "no cable"), // 9
    FAILED_TO_CONFIGURE_ROUTER(0x200, "failed to configure router"), FAILED_TO_CONFIGURE_SERVER(
        0x201,
        "failed to configure server"
    ),
    FAILED_TO_CONFIGURE_ACCOUNT(0x202, "failed to configure account"), COMMUNICATION_DISCONNECTED(
        0x203,
        "communication disconnected"
    ),
    UNCONNECTED(0x204, "please connect the gateway first"), CONNECT_TIMEOUT(
        0x205,
        "connect the device time out"
    ), //    GATEWAY_CONNECT_FAIL(0x400,"gateway connect time out"),

    //    GATEWAY_IS_BUSY(0x401,"only one command can be proceed at a time"),
    DATA_FORMAT_ERROR(0x403, "parameter format or content is incorrect");

    fun getErrorCode(): Int {
        return errorCode
    }

    fun getDescription(): String {
        return description
    }

    companion object {
        fun getInstance(errorCode: Int): GatewayError {
            return when (errorCode) {
                0 -> GatewayError.SUCCESS
                1 -> GatewayError.FAILED
                3 -> GatewayError.BAD_WIFI_NAME
                4 -> GatewayError.BAD_WIFI_PASSWORD
                6 -> GatewayError.INVALID_COMMAND
                7 -> GatewayError.TIME_OUT
                8 -> GatewayError.NO_SIM_CARD
                9 -> GatewayError.NO_CABLE
                else -> GatewayError.FAILED
            }
        }
    }
}
