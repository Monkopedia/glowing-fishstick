package com.ttlock.bl.sdk.entity

class FirmwareInfo {
    /**
     * 产品型号
     */
    private var modelNum = "1.0.0"

    /**
     * 硬件版本号
     */
    private var hardwareRevision = "1.0.0"

    /**
     * 固件版本号
     */
    private var firmwareRevision = "1.0.0"
    fun getModelNum(): String {
        return modelNum
    }

    fun setModelNum(modelNum: String) {
        this.modelNum = modelNum
    }

    fun getHardwareRevision(): String {
        return hardwareRevision
    }

    fun setHardwareRevision(hardwareRevision: String) {
        this.hardwareRevision = hardwareRevision
    }

    fun getFirmwareRevision(): String {
        return firmwareRevision
    }

    fun setFirmwareRevision(firmwareRevision: String) {
        this.firmwareRevision = firmwareRevision
    }
}
