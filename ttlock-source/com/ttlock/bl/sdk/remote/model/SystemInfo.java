package com.ttlock.bl.sdk.remote.model;

public class SystemInfo {

    /**
     *  Product model(e.g. "M201")
     */
    public String modelNum;
    /**
     * Hardware version(e.g. "1.3")
     */
    public String hardwareRevision;
    /**
     * Firmware version(e.g. "2.1.16.705")
     */
    public String firmwareRevision;

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

    @Override
    public String toString() {
        return "SystemInfo{" +
                "modelNum='" + modelNum + '\'' +
                ", hardwareRevision='" + hardwareRevision + '\'' +
                ", firmwareRevision='" + firmwareRevision + '\'' +
                '}';
    }
}
