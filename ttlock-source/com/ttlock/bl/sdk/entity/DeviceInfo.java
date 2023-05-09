package com.ttlock.bl.sdk.entity;

import com.ttlock.bl.sdk.util.GsonUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
public class DeviceInfo {

    public String lockData;

    /**
     * hex feature
     */
    public String featureValue;

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
     * NB lock IMEI
     */
    public String nbNodeId;

    /**
     * NB运营商
     */
    public String nbOperator;

    /**
     * NB lock card info
     */
    public String nbCardNumber;
    /**
     * NB lock rssi
     */
    public int nbRssi;
    /**
     * Date of manufacture(e.g. "20160707")
     */
    public String factoryDate;
    /**
     * lock clock(e.g. "1701051531") yymmddhhmm
     */
    public String lockClock;

    /**
     * 没用了 暂时保留
     */
    @Deprecated
    public int specialValue;

    /**
     * 不完整密码
     */
    public int passcodeKeyNumber;

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

    public String getLockData() {
        return lockData;
    }

    public void setLockData(String lockData) {
        this.lockData = lockData;
    }

    public String getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(String featureValue) {
        this.featureValue = featureValue;
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

    public String getNbNodeId() {
        return nbNodeId;
    }

    public void setNbNodeId(String nbNodeId) {
        this.nbNodeId = nbNodeId;
    }

    public String getNbOperator() {
        return nbOperator;
    }

    public void setNbOperator(String nbOperator) {
        this.nbOperator = nbOperator;
    }

    public String getNbCardNumber() {
        return nbCardNumber;
    }

    public void setNbCardNumber(String nbCardNumber) {
        this.nbCardNumber = nbCardNumber;
    }

    public int getNbRssi() {
        return nbRssi;
    }

    public void setNbRssi(int nbRssi) {
        this.nbRssi = nbRssi;
    }

    public String getFactoryDate() {
        return factoryDate;
    }

    public void setFactoryDate(String factoryDate) {
        this.factoryDate = factoryDate;
    }

    public String getLockClock() {
        return lockClock;
    }

    public void setLockClock(String lockClock) {
        this.lockClock = lockClock;
    }

    public long getLockClockStamp() {//考虑时区问题
        SimpleDateFormat sdr = new SimpleDateFormat("yyMMddHHmm", Locale.getDefault());
        Date date = new Date();
        try {
            date = sdr.parse(lockClock);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public String getDeviceInfo() {
        return GsonUtil.toJson(this);
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "featureValue=" + featureValue +
                ", modelNum='" + modelNum + '\'' +
                ", hardwareRevision='" + hardwareRevision + '\'' +
                ", firmwareRevision='" + firmwareRevision + '\'' +
                ", nbNodeId='" + nbNodeId + '\'' +
                ", nbOperator='" + nbOperator + '\'' +
                ", nbCardNumber='" + nbCardNumber + '\'' +
                ", nbRssi=" + nbRssi +
                ", factoryDate='" + factoryDate + '\'' +
                ", lockClock='" + lockClock + '\'' +
                '}';
    }
}
