package com.ttlock.bl.sdk.gateway.model;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
public class DeviceInfo {
//    public int specialValue;
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


    /**
     * network mac
     */
    public String networkMac;

    public DeviceInfo(){}

    public DeviceInfo(String modelNum, String hardwareVersion, String firmwareVersion){
        this.modelNum = modelNum;
        this.hardwareRevision = hardwareVersion;
        this.firmwareRevision = firmwareVersion;
    }

    public void setModelNum(String modelNum) {
        this.modelNum = modelNum;
    }

    public void setHardwareRevision(String hardwareRevision) {
        this.hardwareRevision = hardwareRevision;
    }

    public void setFirmwareRevision(String firmwareRevision) {
        this.firmwareRevision = firmwareRevision;
    }

    public String getModelNum() {
        return modelNum;
    }

    public String getHardwareRevision() {
        return hardwareRevision;
    }

    public String getFirmwareRevision() {
        return firmwareRevision;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "modelNum='" + modelNum + '\'' +
                ", hardwareRevision='" + hardwareRevision + '\'' +
                ", firmwareRevision='" + firmwareRevision + '\'' +
                ", networkMac='" + networkMac + '\'' +
                '}';
    }
}
