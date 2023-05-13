package com.ttlock.bl.sdk.entity

import android.text.TextUtils
import java.io.Serializable
import java.lang.Exception

/**
 * Created by TTLock on 2018/8/23.
 */
class LockData : Serializable {
    var uid = 0
    var lockName: String? = null
    lateinit var lockMac: String

    /**
     * 110301 admin ; 110302 normal
     */
    var userType = 0

    /**
     * Lock version(json format)
     */
    var lockVersion: LockVersion? = null
    //    public String lockVersion;
    /**
     * admin code, which only belongs to the admin ekey, will be used to verify the admin permission.
     */
    var adminPwd: String? = null

    /**
     * The key data which will be used to unlock
     */
    var lockKey: String? = null

    /**
     *
     */
    var lockFlagPos = 0

    /**
     * Super passcode, which only belongs to the admin ekey, can be entered on the keypad to unlock
     */
    var noKeyPwd: String? = null

    /**
     * Erasing passcode,which belongs to old locks, has been abandoned. Please don't use it.
     */
    var deletePwd: String? = null

    /**
     * Initial data of passcode, which is used to create passcode
     */
    var pwdInfo: String? = null

    /**
     * timestamp
     */
    var timestamp: Long = 0

    /**
     * AES encryption key
     */
    var aesKeyStr: String? = null

    /**
     * characteristic value. it is used to indicate what kinds of feature do a lock support.
     */
    var specialValue = 0

    /**
     * hex feature value
     */
    var featureValue: String? = "1"
    var startDate: Long = 0
    var endDate: Long = 0
    fun getStartDate(): Long {
        return startDate
    }

    fun setStartDate(startDate: Long) {
        this.startDate = startDate
    }

    fun getEndDate(): Long {
        return endDate
    }

    fun setEndDate(endDate: Long) {
        this.endDate = endDate
    }

    fun getFeatureValue(): String {
        return featureValue
    }

    fun setFeatureValue(featureValue: String) {
        this.featureValue = featureValue
    }

    /**
     * lock battery
     */
    var electricQuantity = 0
    var timezoneRawOffset: Long =
        TimeZone.getDefault().getOffset(System.currentTimeMillis()).toLong()

    /**
     * Product model
     */
    var modelNum: String? = null

    /**
     * Hardware version
     */
    var hardwareRevision: String? = null

    /**
     * Firmware version
     */
    var firmwareRevision: String? = null

    /**
     * NB lock IMEI
     */
    var nbNodeId: String? = null

    /**
     * NB运营商
     */
    var nbOperator: String? = null

    /**
     * NB lock card info
     */
    var nbCardNumber: String? = null

    /**
     * NB lock rssi
     */
    var nbRssi = 0

    /**
     * 校验版本号：1.0
     */
    var version = "1.0"

    /**
     * 出厂时间（格式:yyyyMMdd）
     */
    var factoryDate = "19700101"

    /**
     * 校验加密串
     */
    var ref: String? = null
    // --------------新增字段----------------
    /**
     * 自动闭锁时间（单位秒），-1代表关闭自动闭锁 0 未知
     */
    var autoLockTime = 0

    /**
     * -1未知, 0 - 关闭, 其它 时间值
     *
     */
    var lightingTime: Int = LockDataSwitchValue.LIGHTING_UNKNOWN
    var resetButton = 0
    var tamperAlert = 0
    var privacyLock = 0

    /**
     * 屏幕密码
     */
    var displayPasscode = 0
    var lockSound = 0
    var sensitivity = // 灵敏度：-1 未知、0-关闭、1-低、2-中、3-高
        0

    /**
     * 不支持的情况不传该字段
     */
    var settingValue: Int? = // 用于表示防撬开关、重置按键开关，反锁开关、开门方向、常开模式自动开锁开关、Wifi锁省电模式开关等设置项
        null

    /**
     * 不完整密码
     */
    var passcodeKeyNumber = 0

    /**
     * 锁声音音量等级 1~5
     */
    var soundVolume = 0
    override fun toString(): String {
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
            '}'
    }

    fun toJson(): String {
        if (!TextUtils.isEmpty(lockMac) && lockMac!!.length > 5 && TextUtils.isEmpty(ref)) {
            val headRef = lockMac!!.substring(lockMac!!.length - 5)
            ref = DigitUtil.encodeLockData(headRef + factoryDate)
        }
        return GsonUtil.toJson<LockData>(this)
    }

    fun encodeLockData(): String? {
        if (!TextUtils.isEmpty(lockMac)) {
            val aesKey = (lockMac!!.substring(0, 9) + lockMac!!.substring(10)).toByteArray()
            try {
                val encryptedBytes: ByteArray =
                    AESUtil.aesEncrypt(GsonUtil.toJson<LockData>(this).toByteArray(), aesKey)
                val macBytes: ByteArray = DigitUtil.getByteArrayByMac(lockMac)
                return Base64.encodeToString(
                    DigitUtil.byteMerger(encryptedBytes, macBytes),
                    Base64.NO_WRAP
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun getLockName(): String? {
        return lockName
    }

    fun setLockName(lockName: String?) {
        this.lockName = lockName
    }

    fun getLockMac(): String {
        return lockMac
    }

    fun setLockMac(lockMac: String) {
        this.lockMac = lockMac
    }

    //    public String getLockVersion() {
    //        return lockVersion;
    //    }
    //
    //    public void setLockVersion(String lockVersion) {
    //        this.lockVersion = lockVersion;
    //    }
    fun getLockVersion(): LockVersion? {
        return lockVersion
    }

    fun setLockVersion(lockVersion: LockVersion?) {
        this.lockVersion = lockVersion
    }

    fun getAdminPwd(): String? {
        return adminPwd
    }

    fun setAdminPwd(adminPwd: String?) {
        this.adminPwd = adminPwd
    }

    fun getLockKey(): String? {
        return lockKey
    }

    fun setLockKey(lockKey: String?) {
        this.lockKey = lockKey
    }

    fun getLockFlagPos(): Int {
        return lockFlagPos
    }

    fun setLockFlagPos(lockFlagPos: Int) {
        this.lockFlagPos = lockFlagPos
    }

    fun getNoKeyPwd(): String? {
        return noKeyPwd
    }

    fun setNoKeyPwd(noKeyPwd: String?) {
        this.noKeyPwd = noKeyPwd
    }

    fun getDeletePwd(): String? {
        return deletePwd
    }

    fun setDeletePwd(deletePwd: String?) {
        this.deletePwd = deletePwd
    }

    fun getPwdInfo(): String? {
        return pwdInfo
    }

    fun setPwdInfo(pwdInfo: String?) {
        this.pwdInfo = pwdInfo
    }

    fun getTimestamp(): Long {
        return timestamp
    }

    fun setTimestamp(timestamp: Long) {
        this.timestamp = timestamp
    }

    fun getAesKeyStr(): String? {
        return aesKeyStr
    }

    fun setAesKeyStr(aesKeyStr: String?) {
        this.aesKeyStr = aesKeyStr
    }

    fun getSpecialValue(): Int {
        return specialValue
    }

    fun setSpecialValue(specialValue: Int) {
        this.specialValue = specialValue
    }

    fun getElectricQuantity(): Int {
        return electricQuantity
    }

    fun setElectricQuantity(electricQuantity: Int) {
        this.electricQuantity = electricQuantity
    }

    fun getTimezoneRawOffset(): Long {
        return timezoneRawOffset
    }

    fun setTimezoneRawOffset(timezoneRawOffset: Long) {
        this.timezoneRawOffset = timezoneRawOffset
    }

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

    fun getNbNodeId(): String? {
        return nbNodeId
    }

    fun setNbNodeId(nbNodeId: String?) {
        this.nbNodeId = nbNodeId
    }

    fun getNbOperator(): String? {
        return nbOperator
    }

    fun setNbOperator(nbOperator: String?) {
        this.nbOperator = nbOperator
    }

    fun getNbCardNumber(): String? {
        return nbCardNumber
    }

    fun setNbCardNumber(nbCardNumber: String?) {
        this.nbCardNumber = nbCardNumber
    }

    fun getNbRssi(): Int {
        return nbRssi
    }

    fun setNbRssi(nbRssi: Int) {
        this.nbRssi = nbRssi
    }

    fun getUserType(): Int {
        return userType
    }

    fun setUserType(userType: Int) {
        this.userType = userType
    }

    fun getUid(): Int {
        return uid
    }

    fun setUid(uid: Int) {
        this.uid = uid
    }

    fun getAutoLockTime(): Int {
        return autoLockTime
    }

    fun setAutoLockTime(autoLockTime: Int) {
        this.autoLockTime = autoLockTime
    }

    fun getLightingTime(): Int {
        return lightingTime
    }

    fun setLightingTime(lightingTime: Int) {
        this.lightingTime = lightingTime
    }

    fun getResetButton(): Int {
        return resetButton
    }

    fun setResetButton(resetButton: Int) {
        this.resetButton = resetButton
    }

    fun getTamperAlert(): Int {
        return tamperAlert
    }

    fun setTamperAlert(tamperAlert: Int) {
        this.tamperAlert = tamperAlert
    }

    fun getPrivacyLock(): Int {
        return privacyLock
    }

    fun setPrivacyLock(privacyLock: Int) {
        this.privacyLock = privacyLock
    }

    fun getDisplayPasscode(): Int {
        return displayPasscode
    }

    fun setDisplayPasscode(displayPasscode: Int) {
        this.displayPasscode = displayPasscode
    }

    fun getLockSound(): Int {
        return lockSound
    }

    fun setLockSound(lockSound: Int) {
        this.lockSound = lockSound
    }

    fun getSettingValue(): Int? {
        return settingValue
    }

    fun setSettingValue(settingValue: Int?) {
        this.settingValue = settingValue
    }

    fun getSoundVolume(): Int {
        return soundVolume
    }

    fun setSoundVolume(soundVolume: Int) {
        this.soundVolume = soundVolume
    }

    fun getSensitivity(): Int {
        return sensitivity
    }

    fun setSensitivity(sensitivity: Int) {
        this.sensitivity = sensitivity
    }

    fun getPasscodeKeyNumber(): Int {
        return passcodeKeyNumber
    }

    fun setPasscodeKeyNumber(passcodeKeyNumber: Int) {
        this.passcodeKeyNumber = passcodeKeyNumber
    }
}
