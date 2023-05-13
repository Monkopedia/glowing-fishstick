package com.ttlock.bl.sdk.remote.model

class SystemInfo {
    /**
     * Product model(e.g. "M201")
     */
    var modelNum: String? = null

    /**
     * Hardware version(e.g. "1.3")
     */
    var hardwareRevision: String? = null

    /**
     * Firmware version(e.g. "2.1.16.705")
     */
    var firmwareRevision: String? = null
    fun getModelNum(): String? {
        return modelNum
    }

    fun setModelNum(modelNum: String?) {
        this.modelNum = modelNum
    }

    fun getHardwareRevision(): String? {
        return hardwareRevision
    }

    fun setHardwareRevision(hardwareRevision: String?) {
        this.hardwareRevision = hardwareRevision
    }

    fun getFirmwareRevision(): String? {
        return firmwareRevision
    }

    fun setFirmwareRevision(firmwareRevision: String?) {
        this.firmwareRevision = firmwareRevision
    }

    override fun toString(): String {
        return "SystemInfo{" +
            "modelNum='" + modelNum + '\'' +
            ", hardwareRevision='" + hardwareRevision + '\'' +
            ", firmwareRevision='" + firmwareRevision + '\'' +
            '}'
    }
}
