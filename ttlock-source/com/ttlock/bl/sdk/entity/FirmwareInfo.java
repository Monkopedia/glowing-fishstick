package com.ttlock.bl.sdk.entity;

public class FirmwareInfo {

    /**
     * 产品型号
     */
    private String modelNum = "1.0.0";
    /**
     * 硬件版本号
     */
    private String hardwareRevision = "1.0.0";
    /**
     * 固件版本号
     */
    private String firmwareRevision = "1.0.0";

    public String getModelNum() {
        return modelNum;
    }

    public void setModelNum(String modelNum) {
        this.modelNum = modelNum;
    }

    public String getHardwareRevision() {
        return hardwareRevision;
    }

    public void setHardwareRevision(String hardwareRevision) {
        this.hardwareRevision = hardwareRevision;
    }

    public String getFirmwareRevision() {
        return firmwareRevision;
    }

    public void setFirmwareRevision(String firmwareRevision) {
        this.firmwareRevision = firmwareRevision;
    }
}
