package com.ttlock.bl.sdk.entity;

import android.text.TextUtils;
import android.util.Base64;

import com.ttlock.bl.sdk.constant.LockDataSwitchValue;
import com.ttlock.bl.sdk.util.AESUtil;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.GsonUtil;

import java.io.Serializable;
import java.util.TimeZone;

/**
 * Created by TTLock on 2018/8/23.
 */

public class LockData implements Serializable {
    int uid;

    public String lockName;

    public String lockMac;

    /**
     * 110301 admin ; 110302 normal
     */
    int userType;

    /**
     * Lock version(json format)
     */
    public LockVersion lockVersion;
//    public String lockVersion;
    /**
     * admin code, which only belongs to the admin ekey, will be used to verify the admin permission.
     */
    public String adminPwd;
    /**
     * The key data which will be used to unlock
     */
    public String lockKey;

    /**
     *
     */
    public int lockFlagPos;

    /**
     * Super passcode, which only belongs to the admin ekey, can be entered on the keypad to unlock
     */
    public String noKeyPwd;
    /**
     * Erasing passcode,which belongs to old locks, has been abandoned. Please don't use it.
     */
    public String deletePwd;
    /**
     * Initial data of passcode, which is used to create passcode
     */
    public String pwdInfo;
    /**
     * timestamp
     */
    public long timestamp;
    /**
     * AES encryption key
     */
    public String aesKeyStr;
    /**
     * characteristic value. it is used to indicate what kinds of feature do a lock support.
     */
    public int specialValue;

    /**
     * hex feature value
     */
    public String featureValue = "1";

    long startDate;
    long endDate;

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

    public String getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(String featureValue) {
        this.featureValue = featureValue;
    }

    /**
     * lock battery
     */
    public int electricQuantity;

    public long timezoneRawOffset = TimeZone.getDefault().getOffset(System.currentTimeMillis());

    /**
     * Product model
     */
    public String modelNum;
    /**
     * Hardware version
     */
    public String hardwareRevision;
    /**
     * Firmware version
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
     * 校验版本号：1.0
     */
    public String version = "1.0";

    /**
     * 出厂时间（格式:yyyyMMdd）
     */
    public String factoryDate = "19700101";

    /**
     * 校验加密串
     */
    public String ref;

    //--------------新增字段----------------

    /**
     * 自动闭锁时间（单位秒），-1代表关闭自动闭锁 0 未知
     */
    public int autoLockTime;

    /**
     * -1未知, 0 - 关闭, 其它 时间值
     *
     */
    public int lightingTime = LockDataSwitchValue.LIGHTING_UNKNOWN;

    public int resetButton;

    public int tamperAlert;

    public int privacyLock;

    /**
     * 屏幕密码
     */
    public int displayPasscode;

    public int lockSound;

    public int sensitivity;//灵敏度：-1 未知、0-关闭、1-低、2-中、3-高

    /**
     * 不支持的情况不传该字段
     */
    public Integer settingValue;//用于表示防撬开关、重置按键开关，反锁开关、开门方向、常开模式自动开锁开关、Wifi锁省电模式开关等设置项

    /**
     * 不完整密码
     */
    public int passcodeKeyNumber;

    /**
     * 锁声音音量等级 1~5
     */
    public int soundVolume;

    @Override
    public String toString() {
        return "LockData{" +
                "uid=" + uid +
                ", lockName='" + lockName + '\'' +
                ", lockMac='" + lockMac + '\'' +
                ", userType=" + userType +
                ", lockVersion=" + lockVersion +
                ", adminPwd='" + adminPwd + '\'' +
                ", lockKey='" + lockKey + '\'' +
                ", lockFlagPos=" + lockFlagPos +
                ", noKeyPwd='" + noKeyPwd + '\'' +
                ", deletePwd='" + deletePwd + '\'' +
                ", pwdInfo='" + pwdInfo + '\'' +
                ", timestamp=" + timestamp +
                ", aesKeyStr='" + aesKeyStr + '\'' +
                ", specialValue=" + specialValue +
                ", featureValue='" + featureValue + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", electricQuantity=" + electricQuantity +
                ", timezoneRawOffset=" + timezoneRawOffset +
                ", modelNum='" + modelNum + '\'' +
                ", hardwareRevision='" + hardwareRevision + '\'' +
                ", firmwareRevision='" + firmwareRevision + '\'' +
                ", nbNodeId='" + nbNodeId + '\'' +
                ", nbOperator='" + nbOperator + '\'' +
                ", nbCardNumber='" + nbCardNumber + '\'' +
                ", nbRssi=" + nbRssi +
                ", version='" + version + '\'' +
                ", factoryDate='" + factoryDate + '\'' +
                ", ref='" + ref + '\'' +
                ", autoLockTime=" + autoLockTime +
                ", lightingTime=" + lightingTime +
                ", resetButton=" + resetButton +
                ", tamperAlert=" + tamperAlert +
                ", privacyLock=" + privacyLock +
                ", displayPasscode=" + displayPasscode +
                ", lockSound=" + lockSound +
                '}';
    }

    public String toJson() {
        if (!TextUtils.isEmpty(lockMac) &&lockMac.length() > 5 && TextUtils.isEmpty(ref)) {
            String headRef = lockMac.substring(lockMac.length() - 5);
            ref = DigitUtil.encodeLockData(headRef + factoryDate);
        }
        return GsonUtil.toJson(this);
    }

        public String encodeLockData() {
        if (!TextUtils.isEmpty(lockMac)) {
            byte[] aesKey = (lockMac.substring(0, 9) + lockMac.substring(10)).getBytes();
            try {
                byte[] encryptedBytes = AESUtil.aesEncrypt(GsonUtil.toJson(this).getBytes(), aesKey);
                byte[] macBytes = DigitUtil.getByteArrayByMac(lockMac);
                return Base64.encodeToString(DigitUtil.byteMerger(encryptedBytes, macBytes), Base64.NO_WRAP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public String getLockMac() {
        return lockMac;
    }

    public void setLockMac(String lockMac) {
        this.lockMac = lockMac;
    }

//    public String getLockVersion() {
//        return lockVersion;
//    }
//
//    public void setLockVersion(String lockVersion) {
//        this.lockVersion = lockVersion;
//    }


    public LockVersion getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(LockVersion lockVersion) {
        this.lockVersion = lockVersion;
    }

    public String getAdminPwd() {
        return adminPwd;
    }

    public void setAdminPwd(String adminPwd) {
        this.adminPwd = adminPwd;
    }

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public int getLockFlagPos() {
        return lockFlagPos;
    }

    public void setLockFlagPos(int lockFlagPos) {
        this.lockFlagPos = lockFlagPos;
    }

    public String getNoKeyPwd() {
        return noKeyPwd;
    }

    public void setNoKeyPwd(String noKeyPwd) {
        this.noKeyPwd = noKeyPwd;
    }

    public String getDeletePwd() {
        return deletePwd;
    }

    public void setDeletePwd(String deletePwd) {
        this.deletePwd = deletePwd;
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

    public String getAesKeyStr() {
        return aesKeyStr;
    }

    public void setAesKeyStr(String aesKeyStr) {
        this.aesKeyStr = aesKeyStr;
    }

    public int getSpecialValue() {
        return specialValue;
    }

    public void setSpecialValue(int specialValue) {
        this.specialValue = specialValue;
    }

    public int getElectricQuantity() {
        return electricQuantity;
    }

    public void setElectricQuantity(int electricQuantity) {
        this.electricQuantity = electricQuantity;
    }

    public long getTimezoneRawOffset() {
        return timezoneRawOffset;
    }

    public void setTimezoneRawOffset(long timezoneRawOffset) {
        this.timezoneRawOffset = timezoneRawOffset;
    }

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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getAutoLockTime() {
        return autoLockTime;
    }

    public void setAutoLockTime(int autoLockTime) {
        this.autoLockTime = autoLockTime;
    }

    public int getLightingTime() {
        return lightingTime;
    }

    public void setLightingTime(int lightingTime) {
        this.lightingTime = lightingTime;
    }

    public int getResetButton() {
        return resetButton;
    }

    public void setResetButton(int resetButton) {
        this.resetButton = resetButton;
    }

    public int getTamperAlert() {
        return tamperAlert;
    }

    public void setTamperAlert(int tamperAlert) {
        this.tamperAlert = tamperAlert;
    }

    public int getPrivacyLock() {
        return privacyLock;
    }

    public void setPrivacyLock(int privacyLock) {
        this.privacyLock = privacyLock;
    }

    public int getDisplayPasscode() {
        return displayPasscode;
    }

    public void setDisplayPasscode(int displayPasscode) {
        this.displayPasscode = displayPasscode;
    }

    public int getLockSound() {
        return lockSound;
    }

    public void setLockSound(int lockSound) {
        this.lockSound = lockSound;
    }

    public Integer getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(Integer settingValue) {
        this.settingValue = settingValue;
    }

    public int getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(int soundVolume) {
        this.soundVolume = soundVolume;
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

    public int getPasscodeKeyNumber() {
        return passcodeKeyNumber;
    }

    public void setPasscodeKeyNumber(int passcodeKeyNumber) {
        this.passcodeKeyNumber = passcodeKeyNumber;
    }

}
