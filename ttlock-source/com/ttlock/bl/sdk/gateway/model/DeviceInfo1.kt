package com.ttlock.bl.sdk.gateway.model

/**
 * Created by Administrator on 2017/1/9 0009.
 */
class DeviceInfo {
    //    public int specialValue;
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

    /**
     * network mac
     */
    var networkMac: String? = null

    constructor() {}
    constructor(modelNum: String?, hardwareVersion: String?, firmwareVersion: String?) {
        this.modelNum = modelNum
        hardwareRevision = hardwareVersion
        firmwareRevision = firmwareVersion
    }

    fun setModelNum(modelNum: String?) {
        this.modelNum = modelNum
    }

    fun setHardwareRevision(hardwareRevision: String?) {
        this.hardwareRevision = hardwareRevision
    }

    fun setFirmwareRevision(firmwareRevision: String?) {
        this.firmwareRevision = firmwareRevision
    }

    fun getModelNum(): String? {
        return modelNum
    }

    fun getHardwareRevision(): String? {
        return hardwareRevision
    }

    fun getFirmwareRevision(): String? {
        return firmwareRevision
    }

    override fun toString(): String {
        return "DeviceInfo{" +
            "modelNum='" + modelNum + '\'' +
            ", hardwareRevision='" + hardwareRevision + '\'' +
            ", firmwareRevision='" + firmwareRevision + '\'' +
            ", networkMac='" + networkMac + '\'' +
            '}'
    }
}
