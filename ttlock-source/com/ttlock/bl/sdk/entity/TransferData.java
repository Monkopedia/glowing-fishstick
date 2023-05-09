package com.ttlock.bl.sdk.entity;

import android.text.TextUtils;

import com.ttlock.bl.sdk.util.LogUtil;

import java.util.List;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/8/12 0012.
 */
public class TransferData {

    /**-------------------------指令数据---------------------*/
    public static byte[] aesKeyArray;

    /**
     * 接口操作指令
     */
    private int APICommand;

    /**
     * 命令字
     */
    private byte command;

    /**
     * 版本信息json
     */
    String lockVersion;

    /**
     * 判断管理员
     */
    String adminPs;

    /**
     * 开门约定数
     */
    String unlockKey;

    /**
     * 三代锁使用 账号id
     */
    int mUid;

    /**
     * 密码数据
     */
    String pwdInfo;

    /**
     * 时间戳
     */
    long timestamp;

    /**
     * 键盘密码
     */
    String keyboardPwd;

    /**
     * 删除密码
     */
    String deletePwd;

    /**
     * 锁标志位
     */
    int lockFlagPos;

    /**
     * 设置的锁名称
     */
    String lockname;

    /**
     * 原始密码
     */
    String originalPwd;

    /**
     * 新密码
     */
    String newPwd;

    /**
     * 起始时间
     */
    long startDate;

    /**
     * 结束时间
     */
    long endDate;

    /**
     * 键盘密码类型
     */
    byte keyboardPwdType;

    /**
     * 三代锁开门判重实用
     */
    //(现在不用于校准锁时间,只用来生成开门的唯一标识)
    private long unlockDate = System.currentTimeMillis();

    /**
     * 需要校准的时间
     */
    private long calibationTime;

    /**
     * 锁时区和UTC时区时间的差数，单位milliseconds
     */
    private long timezoneOffSet = TimeZone.getDefault().getOffset(System.currentTimeMillis());

    /**
     * 传送数据
     */
    byte[] transferData;

    /**
     * IC卡卡号或指纹卡号
     */
    private long No;

    /**
     * 手环KEY
     */
    private String wristbandKey;

    /**
     * 密码列表
     */
    private List<String> pwds;

    /**
     * 操作类型
     */
    private int op;

    /**
     * 恢复数据
     */
    private String json;

    private short seq;

    /**
     * 默认-1
     * 操作值
     */
    private int opValue = -1;

//    private int feature;

    private String address = "";

    private short port;

    private HotelData hotelData;

    /**
     * 全部跟新数据
     */
    private OperateLogType operateLogType;

    private int logType;

    private short sector;

    private ValidityInfo validityInfo;

    private NBAwakeConfig nbAwakeConfig;

    private List<Integer> activateFloors;

    private UnlockDirection unlockDirection;

    private AccessoryInfo accessoryInfo;

    private String keyFobMac;

    private SoundVolume soundVolume;

    /**
     * 门磁mac
     */
    private String doorSensorMac;

    /**
     * 灵敏度
     */
    private int sensitivity;

    private String WifiName;

    private String wifiPassword;

    public UnlockDirection getUnlockDirection() {
        return unlockDirection;
    }

    public void setUnlockDirection(UnlockDirection unlockDirection) {
        this.unlockDirection = unlockDirection;
    }

    public String getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(String lockVersion) {
        this.lockVersion = lockVersion;
    }

    public byte[] getAesKeyArray() {
        return aesKeyArray;
    }

    public void setAesKeyArray(byte[] aesKeyArray) {
        TransferData.aesKeyArray = aesKeyArray;
    }

    public int getAPICommand() {
        return APICommand;
    }

    public void setAPICommand(int APICommand) {
        this.APICommand = APICommand;
    }

    public byte getCommand() {
        return command;
    }

    public void setCommand(byte command) {
        this.command = command;
    }

    public String getAdminPs() {
        return adminPs;
    }

    public void setAdminPs(String adminPs) {
        this.adminPs = adminPs;
    }

    public String getUnlockKey() {
        return unlockKey;
    }

    public void setUnlockKey(String unlockKey) {
        this.unlockKey = unlockKey;
    }

    public int getmUid() {
        return mUid;
    }

    public void setmUid(int mUid) {
        this.mUid = mUid;
    }

    public String getPwdInfo() {
        return pwdInfo;
    }

    public void setPwdInfo(String pwdInfo) {
        this.pwdInfo = pwdInfo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getKeyboardPwd() {
        return keyboardPwd;
    }

    public void setKeyboardPwd(String keyboardPwd) {
        this.keyboardPwd = keyboardPwd;
    }

    public String getDeletePwd() {
        return deletePwd;
    }

    public void setDeletePwd(String deletePwd) {
        this.deletePwd = deletePwd;
    }

    public int getLockFlagPos() {
        return lockFlagPos;
    }

    public void setLockFlagPos(int lockFlagPos) {
        this.lockFlagPos = lockFlagPos;
    }

    public String getLockname() {
        return lockname;
    }

    public void setLockname(String lockname) {
        this.lockname = lockname;
    }

    public String getOriginalPwd() {
        return originalPwd;
    }

    public void setOriginalPwd(String originalPwd) {
        this.originalPwd = originalPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
        if(this.newPwd == null)
            this.newPwd = "";
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

    public byte getKeyboardPwdType() {
        return keyboardPwdType;
    }

    public void setKeyboardPwdType(byte keyboardPwdType) {
        this.keyboardPwdType = keyboardPwdType;
    }

    public long getUnlockDate() {
        return unlockDate;
    }

    public void setUnlockDate(long unlockDate) {
        this.unlockDate = unlockDate;
    }

    public long getCalibationTime() {
        return calibationTime;
    }

    public void setCalibationTime(long calibationTime) {
        this.calibationTime = calibationTime;
    }

    public byte[] getTransferData() {
        return transferData;
    }

    public void setTransferData(byte[] transferData) {
        this.transferData = transferData;
    }

    public long getNo() {
        return No;
    }

    public void setNo(long no) {
        this.No = no;
    }

    public String getWristbandKey() {
        return wristbandKey;
    }

    public void setWristbandKey(String wristbandKey) {
        this.wristbandKey = wristbandKey;
    }

    public long getTimezoneOffSet() {
        return timezoneOffSet;
    }

    public void setTimezoneOffSet(long timezoneOffSet) {
        if(timezoneOffSet != -1)
            this.timezoneOffSet = timezoneOffSet;
    }

    public List<String> getPwds() {
        return pwds;
    }

    public void setPwds(List<String> pwds) {
        this.pwds = pwds;
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public short getSeq() {
        return seq;
    }

    public void setSeq(short seq) {
        this.seq = seq;
    }

    public int getOpValue() {
        return opValue;
    }

    public void setOpValue(int opValue) {
        this.opValue = opValue;
    }

//    public int getFeature() {
//        return feature;
//    }

//    public void setFeature(int feature) {
//        this.feature = feature;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address != null)
            this.address = address;
    }

    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }

    public HotelData getHotelData() {
        return hotelData;
    }

    public void setHotelData(HotelData hotelData) {
        this.hotelData = hotelData;
    }

    public OperateLogType getOperateLogType() {
        return operateLogType;
    }

    public void setOperateLogType(OperateLogType operateLogType) {
        this.operateLogType = operateLogType;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public short getSector() {
        return sector;
    }

    public void setSector(short sector) {
        this.sector = sector;
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

    public AccessoryInfo getAccessoryInfo() {
        return accessoryInfo;
    }

    public void setAccessoryInfo(AccessoryInfo accessoryInfo) {
        this.accessoryInfo = accessoryInfo;
    }

    public String getKeyFobMac() {
        return keyFobMac;
    }

    public void setKeyFobMac(String keyFobMac) {
        this.keyFobMac = keyFobMac;
    }

    public boolean hasCyclicConfig() {
        if (validityInfo == null || validityInfo.getCyclicConfigs() == null || validityInfo.getCyclicConfigs().size() == 0 || validityInfo.getModeType() == ValidityInfo.TIMED) {
            return false;
        }
        return true;
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

    public int getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

    public String getWifiName() {
        return WifiName;
    }

    public void setWifiName(String wifiName) {
        WifiName = wifiName;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }
}
