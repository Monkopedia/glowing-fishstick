package com.ttlock.bl.sdk.remote.model

/**
 * Created by TTLock on 2019/4/24.
 */
enum class RemoteError(private val errorCode: Int, private val description: String) {
    SUCCESS(0, "success"), FAILED(1, "failed"), NO_RESPONSE(0x200, "no response"), CONNECT_FAIL(
        0x400,
        "device connect time out"
    ),
    Device_IS_BUSY(0x401, "only one command can be proceed at a time"), DATA_FORMAT_ERROR(
        0x403,
        "parameter format or content is incorrect"
    );

    fun getDescription(): String {
        return description
    }

    fun getErrorCode(): Int {
        return errorCode
    }

    companion object {
        fun getInstance(errorCode: Int): RemoteError {
            return when (errorCode) {
                0 -> RemoteError.SUCCESS
                1 -> RemoteError.FAILED
                else -> RemoteError.FAILED
            }
        }
    }
}
