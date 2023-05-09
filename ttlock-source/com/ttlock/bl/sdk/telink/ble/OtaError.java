package com.ttlock.bl.sdk.telink.ble;

/**
 * Created by TTLock on 2020/8/18.
 */
public enum OtaError {

    OTA_SUCCESS(0x00, "success"),            //
    OTA_PACKET_LOSS(0x01, "lost one or more OTA PDU"),        // lost one or more OTA PDU
    OTA_DATA_CRC_ERR(0x02, "data CRC err"),       // data CRC err
    OTA_WRITE_FLASH_ERR(0x03, "write OTA data to flash ERR"),    // write OTA data to flash ERR
    OTA_DATA_UNCOMPLETE(0x04, "lost last one or more OTA PDU"),    // lost last one or more OTA PDU
    OTA_TIMEOUT(0x05, "time out"),
    OTA_FW_CHECK_ERR(0x06, "finally whole bin file will be checked by CRC");        // finally whole bin file will be checked by CRC

    private int errorCode;
    private String errorMsg;

    OtaError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public static OtaError getInstance(int errorCode) {
        switch (errorCode) {
            case 0x00:
                return OTA_SUCCESS;
            case 0x01:
                return OTA_PACKET_LOSS;
            case 0x02:
                return OTA_DATA_CRC_ERR;
            case 0x03:
                return OTA_WRITE_FLASH_ERR;
            case 0x04:
                return OTA_DATA_UNCOMPLETE;
            case 0x05:
                return OTA_TIMEOUT;
            case 0x06:
                return OTA_FW_CHECK_ERR;
        }
        return null;
    }
}
