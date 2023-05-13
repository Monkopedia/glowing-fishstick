package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2018/8/23.
 */
// public class LockDataCopy implements Serializable {
//    public int uid;
//
//    public String lockName;
//
//    public String lockMac;
//
//    /**
//     * 110301 admin ; 110302 normal
//     */
//    int userType;
//
//    /**
//     * Lock version(json format)
//     */
//    public LockVersion lockVersion;
//    /**
//     * admin code, which only belongs to the admin ekey, will be used to verify the admin permission.
//     */
//    public String adminPwd;
//    /**
//     * The key data which will be used to unlock
//     */
//    public String lockKey;
//
//    /**
//     *
//     */
//    public int lockFlagPos;
//
//    /**
//     * Super passcode, which only belongs to the admin ekey, can be entered on the keypad to unlock
//     */
//    public String noKeyPwd;
//    /**
//     * Erasing passcode,which belongs to old locks, has been abandoned. Please don't use it.
//     */
//    public String deletePwd;
//    /**
//     * Initial data of passcode, which is used to create passcode
//     */
//    public String pwdInfo;
//    /**
//     * timestamp
//     */
//    public long timestamp;
//    /**
//     * AES encryption key
//     */
//    public String aesKeyStr;
//    /**
//     * characteristic value. it is used to indicate what kinds of feature do a lock support.
//     */
//    public int specialValue;
//
//    long startDate;
//    long endDate;
//
//    public long getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(long startDate) {
//        this.startDate = startDate;
//    }
//
//    public long getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(long endDate) {
//        this.endDate = endDate;
//    }
//
//    /**
//     * lock battery
//     */
//    public int electricQuantity;
//
//    public long timezoneRawOffset = TimeZone.getDefault().getOffset(System.currentTimeMillis());
//
//    /**
//     * Product model
//     */
//    public String modelNum;
//    /**
//     * Hardware version
//     */
//    public String hardwareRevision;
//    /**
//     * Firmware version
//     */
//    public String firmwareRevision;
//
//    /**
//     * NB lock IMEI
//     */
//    public String nbNodeId;
//
//    /**
//     * NB运营商
//     */
//    public String nbOperator;
//
//    /**
//     * NB lock card info
//     */
//    public String nbCardNumber;
//    /**
//     * NB lock rssi
//     */
//    public int nbRssi;
//
//    /**
//     * 校验版本号：1.0
//     */
//    public String version;
//
//    /**
//     * 出厂时间（格式:yyyyMMdd）
//     */
//    public String factoryDate;
//
//    /**
//     * 校验加密串
//     */
//    public String ref;
//
//    @Override
//    public String toString() {
//        return "LockData{" +
//                "lockName='" + lockName + '\'' +
//                ", lockMac='" + lockMac + '\'' +
//                ", lockVersion='" + lockVersion + '\'' +
//                ", adminPwd='" + adminPwd + '\'' +
//                ", lockKey='" + lockKey + '\'' +
//                ", lockFlagPos=" + lockFlagPos +
//                ", noKeyPwd='" + noKeyPwd + '\'' +
//                ", deletePwd='" + deletePwd + '\'' +
//                ", pwdInfo='" + pwdInfo + '\'' +
//                ", timestamp=" + timestamp +
//                ", aesKeyStr='" + aesKeyStr + '\'' +
//                ", specialValue=" + specialValue +
//                ", electricQuantity=" + electricQuantity +
//                ", timezoneRawOffset=" + timezoneRawOffset +
//                ", modelNum='" + modelNum + '\'' +
//                ", hardwareRevision='" + hardwareRevision + '\'' +
//                ", firmwareRevision='" + firmwareRevision + '\'' +
//                ", nbNodeId='" + nbNodeId + '\'' +
//                ", nbOperator='" + nbOperator + '\'' +
//                ", nbCardNumber='" + nbCardNumber + '\'' +
//                ", nbRssi=" + nbRssi +
//                ", version='" + version + '\'' +
//                ", factoryDate='" + factoryDate + '\'' +
//                ", ref='" + ref + '\'' +
//                '}';
//    }
//
//    public String encodeLockData() {
//        if (!TextUtils.isEmpty(lockMac)) {
//            byte[] aesKey = (lockMac.substring(0, 9) + lockMac.substring(10)).getBytes();
//            try {
//                byte[] encryptedBytes = AESUtil.aesEncrypt(GsonUtil.toJson(this).getBytes(), aesKey);
//                byte[] macBytes = DigitUtil.getByteArrayByMac(lockMac);
//                return Base64.encodeToString(DigitUtil.byteMerger(encryptedBytes, macBytes), Base64.NO_WRAP);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    public String getLockName() {
//        return lockName;
//    }
//
//    public void setLockName(String lockName) {
//        this.lockName = lockName;
//    }
//
//    public String getLockMac() {
//        return lockMac;
//    }
//
//    public void setLockMac(String lockMac) {
//        this.lockMac = lockMac;
//    }
//
//    public LockVersion getLockVersion() {
//        return lockVersion;
//    }
//
//    public void setLockVersion(LockVersion lockVersion) {
//        this.lockVersion = lockVersion;
//    }
//
//    public String getAdminPwd() {
//        return adminPwd;
//    }
//
//    public void setAdminPwd(String adminPwd) {
//        this.adminPwd = adminPwd;
//    }
//
//    public String getLockKey() {
//        return lockKey;
//    }
//
//    public void setLockKey(String lockKey) {
//        this.lockKey = lockKey;
//    }
//
//    public int getLockFlagPos() {
//        return lockFlagPos;
//    }
//
//    public void setLockFlagPos(int lockFlagPos) {
//        this.lockFlagPos = lockFlagPos;
//    }
//
//    public String getNoKeyPwd() {
//        return noKeyPwd;
//    }
//
//    public void setNoKeyPwd(String noKeyPwd) {
//        this.noKeyPwd = noKeyPwd;
//    }
//
//    public String getDeletePwd() {
//        return deletePwd;
//    }
//
//    public void setDeletePwd(String deletePwd) {
//        this.deletePwd = deletePwd;
//    }
//
//    public String getPwdInfo() {
//        return pwdInfo;
//    }
//
//    public void setPwdInfo(String pwdInfo) {
//        this.pwdInfo = pwdInfo;
//    }
//
//    public long getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(long timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public String getAesKeyStr() {
//        return aesKeyStr;
//    }
//
//    public void setAesKeyStr(String aesKeyStr) {
//        this.aesKeyStr = aesKeyStr;
//    }
//
//    public int getSpecialValue() {
//        return specialValue;
//    }
//
//    public void setSpecialValue(int specialValue) {
//        this.specialValue = specialValue;
//    }
//
//    public int getElectricQuantity() {
//        return electricQuantity;
//    }
//
//    public void setElectricQuantity(int electricQuantity) {
//        this.electricQuantity = electricQuantity;
//    }
//
//    public long getTimezoneRawOffset() {
//        return timezoneRawOffset;
//    }
//
//    public void setTimezoneRawOffset(long timezoneRawOffset) {
//        this.timezoneRawOffset = timezoneRawOffset;
//    }
//
//    public String getModelNum() {
//        return modelNum;
//    }
//
//    public void setModelNum(String modelNum) {
//        this.modelNum = modelNum;
//    }
//
//    public String getHardwareRevision() {
//        return hardwareRevision;
//    }
//
//    public void setHardwareRevision(String hardwareRevision) {
//        this.hardwareRevision = hardwareRevision;
//    }
//
//    public String getFirmwareRevision() {
//        return firmwareRevision;
//    }
//
//    public void setFirmwareRevision(String firmwareRevision) {
//        this.firmwareRevision = firmwareRevision;
//    }
//
//    public String getNbNodeId() {
//        return nbNodeId;
//    }
//
//    public void setNbNodeId(String nbNodeId) {
//        this.nbNodeId = nbNodeId;
//    }
//
//    public String getNbOperator() {
//        return nbOperator;
//    }
//
//    public void setNbOperator(String nbOperator) {
//        this.nbOperator = nbOperator;
//    }
//
//    public String getNbCardNumber() {
//        return nbCardNumber;
//    }
//
//    public void setNbCardNumber(String nbCardNumber) {
//        this.nbCardNumber = nbCardNumber;
//    }
//
//    public int getNbRssi() {
//        return nbRssi;
//    }
//
//    public void setNbRssi(int nbRssi) {
//        this.nbRssi = nbRssi;
//    }
//
//    public int getUserType() {
//        return userType;
//    }
//
//    public void setUserType(int userType) {
//        this.userType = userType;
//    }
//
//    public int getUid() {
//        return uid;
//    }
//
//    public void setUid(int uid) {
//        this.uid = uid;
//    }
//
//    public LockData convert2LockData() {
//        com.ttlock.bl.sdk.entity.LockData lockData = new LockData();
//        lockData.uid = uid;
//        lockData.userType = userType;
//        lockData.specialValue = specialValue;
//        lockData.adminPwd = adminPwd;
//        lockData.startDate = startDate;
//        lockData.endDate = endDate;
//        lockData.lockFlagPos = lockFlagPos;
//        lockData.aesKeyStr = aesKeyStr;
//        lockData.setLockVersion(GsonUtil.toJson(lockVersion));
//        lockData.lockMac = lockMac;
//        lockData.lockKey = lockKey;
//        lockData.timezoneRawOffset = timezoneRawOffset;
//        lockData.lockName = lockName;
//
//        return lockData;
//    }
// }
