package com.ttlock.bl.sdk.telink.ble

/**
 * Created by TTLock on 2020/8/18.
 */
enum class OtaError( // finally whole bin file will be checked by CRC
    private val errorCode: Int,
    private val errorMsg: String
) {
    OTA_SUCCESS(0x00, "success"), //
    OTA_PACKET_LOSS(0x01, "lost one or more OTA PDU"), // lost one or more OTA PDU
    OTA_DATA_CRC_ERR(0x02, "data CRC err"), // data CRC err
    OTA_WRITE_FLASH_ERR(0x03, "write OTA data to flash ERR"), // write OTA data to flash ERR
    OTA_DATA_UNCOMPLETE(0x04, "lost last one or more OTA PDU"), // lost last one or more OTA PDU
    OTA_TIMEOUT(0x05, "time out"), OTA_FW_CHECK_ERR(
        0x06,
        "finally whole bin file will be checked by CRC"
    );

    companion object {
        fun getInstance(errorCode: Int): OtaError? {
            when (errorCode) {
                0x00 -> return OtaError.OTA_SUCCESS
                0x01 -> return OtaError.OTA_PACKET_LOSS
                0x02 -> return OtaError.OTA_DATA_CRC_ERR
                0x03 -> return OtaError.OTA_WRITE_FLASH_ERR
                0x04 -> return OtaError.OTA_DATA_UNCOMPLETE
                0x05 -> return OtaError.OTA_TIMEOUT
                0x06 -> return OtaError.OTA_FW_CHECK_ERR
            }
            return null
        }
    }
}
