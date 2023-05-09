package com.ttlock.bl.sdk.entity;

import java.util.List;

/**
 * Created on  2019/4/12 0012 16:34
 *
 * @author theodre
 */
public class ConnectParam {
    private LockData lockData;
    private int controlAction;
    private boolean lockModeEnable;
    private PassageModeConfig passageModeConfig;
    private long timestamp;
    private int autoLockingPeriod;
    private int logType;
    private String recoveryDataStr;
    private int recoveryDataType;
    private long startDate;
    private long endDate;
    private String originalPasscode;
    private String newPasscode;
    private long attachmentNum;
    private String dataJsonStr;
    private String serverAddress;
    private short serverPort;
    private String lockmac;
//    private String factoryDate;
    private int seconds;
    private HotelData hotelData;
    private TTLockConfigType ttLockConfigType;
    private ValidityInfo validityInfo;
    private NBAwakeConfig nbAwakeConfig;
    private List<Integer> activateFloors;
    private UnlockDirection unlockDirection;
    private AccessoryInfo accessoryInfo;
    private String keyfobMac;
    private String doorSensorMac;

    private String wifiName;
    private String wifiPassword;
    private IpSetting ipSetting;

    private SoundVolume soundVolume;

    public String getLockmac() {
        return lockmac;
    }

    public void setLockmac(String lockmac) {
        this.lockmac = lockmac;
    }

//    public String getFactoryDate() {
//        return factoryDate;
//    }
//
//    public void setFactoryDate(String factoryDate) {
//        this.factoryDate = factoryDate;
//    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public short getServerPort() {
        return serverPort;
    }

    public void setServerPort(short serverPort) {
        this.serverPort = serverPort;
    }

    public String getDataJsonStr() {
        return dataJsonStr;
    }

    public void setDataJsonStr(String dataJsonStr) {
        this.dataJsonStr = dataJsonStr;
    }

    public long getAttachmentNum() {
        return attachmentNum;
    }

    public void setAttachmentNum(long attachmentNum) {
        this.attachmentNum = attachmentNum;
    }

    public String getNewPasscode() {
        return newPasscode;
    }

    public void setNewPasscode(String newPasscode) {
        this.newPasscode = newPasscode;
    }

    public int getAutoLockingPeriod() {
        return autoLockingPeriod;
    }

    public void setAutoLockingPeriod(int autoLockingPeriod) {
        this.autoLockingPeriod = autoLockingPeriod;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getOriginalPasscode() {
        return originalPasscode;
    }

    public void setOriginalPasscode(String originalPasscode) {
        this.originalPasscode = originalPasscode;
    }

    public String getRecoveryDataStr() {
        return recoveryDataStr;
    }

    public void setRecoveryDataStr(String recoveryDataStr) {
        this.recoveryDataStr = recoveryDataStr;
    }

    public int getRecoveryDataType() {
        return recoveryDataType;
    }

    public void setRecoveryDataType(int recoveryDataType) {
        this.recoveryDataType = recoveryDataType;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public PassageModeConfig getPassageModeConfig() {
        return passageModeConfig;
    }

    public void setPassageModeConfig(PassageModeConfig passageModeConfig) {
        this.passageModeConfig = passageModeConfig;
    }

    public boolean isLockModeEnable() {
        return lockModeEnable;
    }

    public void setLockModeEnable(boolean lockModeEnable) {
        this.lockModeEnable = lockModeEnable;
    }

    public int getControlAction() {
        return controlAction;
    }

    public void setControlAction(int controlAction) {
        this.controlAction = controlAction;
    }

    public TTLockConfigType getTtLockConfigType() {
        return ttLockConfigType;
    }

    public void setTtLockConfigType(TTLockConfigType ttLockConfigType) {
        this.ttLockConfigType = ttLockConfigType;
    }

    public LockData getLockData() {
        if (lockData == null) {//给一个默认的lockdata
            lockData = new LockData();
        }
        return lockData;
    }

    public void setLockData(LockData lockData) {
        this.lockData = lockData;
    }

    public HotelData getHotelData() {
        return hotelData;
    }

    public void setHotelData(HotelData hotelData) {
        this.hotelData = hotelData;
    }

    public ValidityInfo getValidityInfo() {
        return validityInfo;
    }

    public void setValidityInfo(ValidityInfo validityInfo) {
        this.validityInfo = validityInfo;
    }

    public NBAwakeConfig getNbAwakeConfig() {
        return nbAwakeConfig;
    }

    public void setNbAwakeConfig(NBAwakeConfig nbAwakeConfig) {
        this.nbAwakeConfig = nbAwakeConfig;
    }

    public List<Integer> getActivateFloors() {
        return activateFloors;
    }

    public void setActivateFloors(List<Integer> activateFloors) {
        this.activateFloors = activateFloors;
    }

    public UnlockDirection getUnlockDirection() {
        return unlockDirection;
    }

    public void setUnlockDirection(UnlockDirection unlockDirection) {
        this.unlockDirection = unlockDirection;
    }

    public AccessoryInfo getAccessoryInfo() {
        return accessoryInfo;
    }

    public void setAccessoryInfo(AccessoryInfo accessoryInfo) {
        this.accessoryInfo = accessoryInfo;
    }

    public String getKeyfobMac() {
        return keyfobMac;
    }

    public void setKeyfobMac(String keyfobMac) {
        this.keyfobMac = keyfobMac;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

    public IpSetting getIpSetting() {
        return ipSetting;
    }

    public void setIpSetting(IpSetting ipSetting) {
        this.ipSetting = ipSetting;
    }

    public SoundVolume getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(SoundVolume soundVolume) {
        this.soundVolume = soundVolume;
    }

    public String getDoorSensorMac() {
        return doorSensorMac;
    }

    public void setDoorSensorMac(String doorSensorMac) {
        this.doorSensorMac = doorSensorMac;
    }

}
