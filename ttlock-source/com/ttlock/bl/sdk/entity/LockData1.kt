package com.ttlock.bl.sdk.entity

import android.util.TextUtils
import com.ttlock.bl.sdk.constant.LockDataSwitchValue
import com.ttlock.bl.sdk.util.AESUtil
import com.ttlock.bl.sdk.util.DigitUtil
import com.ttlock.bl.sdk.util.GsonUtil
import java.io.Serializable
import java.util.Base64
import java.util.TimeZone

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
    var featureValue: String = "1"
    var startDate: Long = 0
    var endDate: Long = 0
    @JvmName("getStartDate1")
    fun getStartDate(): Long {
        return startDate
    }

    @JvmName("setStartDate1")
    fun setStartDate(startDate: Long) {
        this.startDate = startDate
    }

    @JvmName("getEndDate1")
    fun getEndDate(): Long {
        return endDate
    }

    @JvmName("setEndDate1")
    fun setEndDate(endDate: Long) {
        this.endDate = endDate
    }

    @JvmName("getFeatureValue1")
    fun getFeatureValue(): String {
        return featureValue
    }

    @JvmName("setFeatureValue1")
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
                val encryptedBytes: ByteArray? =
                    AESUtil.aesEncrypt(GsonUtil.toJson<LockData>(this).toByteArray(), aesKey)
                val macBytes: ByteArray = DigitUtil.getByteArrayByMac(lockMac)
                return Base64.getEncoder().encodeToString(
                    DigitUtil.byteMerger(encryptedBytes!!, macBytes)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    @JvmName("getLockName1")
    fun getLockName(): String? {
        return lockName
    }

    @JvmName("setLockName1")
    fun setLockName(lockName: String?) {
        this.lockName = lockName
    }

    @JvmName("getLockMac1")
    fun getLockMac(): String {
        return lockMac
    }

    @JvmName("setLockMac1")
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
    @JvmName("getLockVersion1")
    fun getLockVersion(): LockVersion? {
        return lockVersion
    }

    @JvmName("setLockVersion1")
    fun setLockVersion(lockVersion: LockVersion?) {
        this.lockVersion = lockVersion
    }

    @JvmName("getAdminPwd1")
    fun getAdminPwd(): String? {
        return adminPwd
    }

    @JvmName("setAdminPwd1")
    fun setAdminPwd(adminPwd: String?) {
        this.adminPwd = adminPwd
    }

    @JvmName("getLockKey1")
    fun getLockKey(): String? {
        return lockKey
    }

    @JvmName("setLockKey1")
    fun setLockKey(lockKey: String?) {
        this.lockKey = lockKey
    }

    @JvmName("getLockFlagPos1")
    fun getLockFlagPos(): Int {
        return lockFlagPos
    }

    @JvmName("setLockFlagPos1")
    fun setLockFlagPos(lockFlagPos: Int) {
        this.lockFlagPos = lockFlagPos
    }

    @JvmName("getNoKeyPwd1")
    fun getNoKeyPwd(): String? {
        return noKeyPwd
    }

    @JvmName("setNoKeyPwd1")
    fun setNoKeyPwd(noKeyPwd: String?) {
        this.noKeyPwd = noKeyPwd
    }

    @JvmName("getDeletePwd1")
    fun getDeletePwd(): String? {
        return deletePwd
    }

    @JvmName("setDeletePwd1")
    fun setDeletePwd(deletePwd: String?) {
        this.deletePwd = deletePwd
    }

    @JvmName("getPwdInfo1")
    fun getPwdInfo(): String? {
        return pwdInfo
    }

    @JvmName("setPwdInfo1")
    fun setPwdInfo(pwdInfo: String?) {
        this.pwdInfo = pwdInfo
    }

    @JvmName("getTimestamp1")
    fun getTimestamp(): Long {
        return timestamp
    }

    @JvmName("setTimestamp1")
    fun setTimestamp(timestamp: Long) {
        this.timestamp = timestamp
    }

    @JvmName("getAesKeyStr1")
    fun getAesKeyStr(): String? {
        return aesKeyStr
    }

    @JvmName("setAesKeyStr1")
    fun setAesKeyStr(aesKeyStr: String?) {
        this.aesKeyStr = aesKeyStr
    }

    @JvmName("getSpecialValue1")
    fun getSpecialValue(): Int {
        return specialValue
    }

    @JvmName("setSpecialValue1")
    fun setSpecialValue(specialValue: Int) {
        this.specialValue = specialValue
    }

    @JvmName("getElectricQuantity1")
    fun getElectricQuantity(): Int {
        return electricQuantity
    }

    @JvmName("setElectricQuantity1")
    fun setElectricQuantity(electricQuantity: Int) {
        this.electricQuantity = electricQuantity
    }

    @JvmName("getTimezoneRawOffset1")
    fun getTimezoneRawOffset(): Long {
        return timezoneRawOffset
    }

    @JvmName("setTimezoneRawOffset1")
    fun setTimezoneRawOffset(timezoneRawOffset: Long) {
        this.timezoneRawOffset = timezoneRawOffset
    }

    @JvmName("getModelNum1")
    fun getModelNum(): String? {
        return modelNum
    }

    @JvmName("setModelNum1")
    fun setModelNum(modelNum: String?) {
        this.modelNum = modelNum
    }

    @JvmName("getHardwareRevision1")
    fun getHardwareRevision(): String? {
        return hardwareRevision
    }

    @JvmName("setHardwareRevision1")
    fun setHardwareRevision(hardwareRevision: String?) {
        this.hardwareRevision = hardwareRevision
    }

    @JvmName("getFirmwareRevision1")
    fun getFirmwareRevision(): String? {
        return firmwareRevision
    }

    @JvmName("setFirmwareRevision1")
    fun setFirmwareRevision(firmwareRevision: String?) {
        this.firmwareRevision = firmwareRevision
    }

    @JvmName("getNbNodeId1")
    fun getNbNodeId(): String? {
        return nbNodeId
    }

    @JvmName("setNbNodeId1")
    fun setNbNodeId(nbNodeId: String?) {
        this.nbNodeId = nbNodeId
    }

    @JvmName("getNbOperator1")
    fun getNbOperator(): String? {
        return nbOperator
    }

    @JvmName("setNbOperator1")
    fun setNbOperator(nbOperator: String?) {
        this.nbOperator = nbOperator
    }

    @JvmName("getNbCardNumber1")
    fun getNbCardNumber(): String? {
        return nbCardNumber
    }

    @JvmName("setNbCardNumber1")
    fun setNbCardNumber(nbCardNumber: String?) {
        this.nbCardNumber = nbCardNumber
    }

    @JvmName("getNbRssi1")
    fun getNbRssi(): Int {
        return nbRssi
    }

    @JvmName("setNbRssi1")
    fun setNbRssi(nbRssi: Int) {
        this.nbRssi = nbRssi
    }

    @JvmName("getUserType1")
    fun getUserType(): Int {
        return userType
    }

    @JvmName("setUserType1")
    fun setUserType(userType: Int) {
        this.userType = userType
    }

    @JvmName("getUid1")
    fun getUid(): Int {
        return uid
    }

    @JvmName("setUid1")
    fun setUid(uid: Int) {
        this.uid = uid
    }

    @JvmName("getAutoLockTime1")
    fun getAutoLockTime(): Int {
        return autoLockTime
    }

    @JvmName("setAutoLockTime1")
    fun setAutoLockTime(autoLockTime: Int) {
        this.autoLockTime = autoLockTime
    }

    @JvmName("getLightingTime1")
    fun getLightingTime(): Int {
        return lightingTime
    }

    @JvmName("setLightingTime1")
    fun setLightingTime(lightingTime: Int) {
        this.lightingTime = lightingTime
    }

    @JvmName("getResetButton1")
    fun getResetButton(): Int {
        return resetButton
    }

    @JvmName("setResetButton1")
    fun setResetButton(resetButton: Int) {
        this.resetButton = resetButton
    }

    @JvmName("getTamperAlert1")
    fun getTamperAlert(): Int {
        return tamperAlert
    }

    @JvmName("setTamperAlert1")
    fun setTamperAlert(tamperAlert: Int) {
        this.tamperAlert = tamperAlert
    }

    @JvmName("getPrivacyLock1")
    fun getPrivacyLock(): Int {
        return privacyLock
    }

    @JvmName("setPrivacyLock1")
    fun setPrivacyLock(privacyLock: Int) {
        this.privacyLock = privacyLock
    }

    @JvmName("getDisplayPasscode1")
    fun getDisplayPasscode(): Int {
        return displayPasscode
    }

    @JvmName("setDisplayPasscode1")
    fun setDisplayPasscode(displayPasscode: Int) {
        this.displayPasscode = displayPasscode
    }

    @JvmName("getLockSound1")
    fun getLockSound(): Int {
        return lockSound
    }

    @JvmName("setLockSound1")
    fun setLockSound(lockSound: Int) {
        this.lockSound = lockSound
    }

    @JvmName("getSettingValue1")
    fun getSettingValue(): Int? {
        return settingValue
    }

    @JvmName("setSettingValue1")
    fun setSettingValue(settingValue: Int?) {
        this.settingValue = settingValue
    }

    @JvmName("getSoundVolume1")
    fun getSoundVolume(): Int {
        return soundVolume
    }

    @JvmName("setSoundVolume1")
    fun setSoundVolume(soundVolume: Int) {
        this.soundVolume = soundVolume
    }

    @JvmName("getSensitivity1")
    fun getSensitivity(): Int {
        return sensitivity
    }

    @JvmName("setSensitivity1")
    fun setSensitivity(sensitivity: Int) {
        this.sensitivity = sensitivity
    }

    @JvmName("getPasscodeKeyNumber1")
    fun getPasscodeKeyNumber(): Int {
        return passcodeKeyNumber
    }

    @JvmName("setPasscodeKeyNumber1")
    fun setPasscodeKeyNumber(passcodeKeyNumber: Int) {
        this.passcodeKeyNumber = passcodeKeyNumber
    }
}
