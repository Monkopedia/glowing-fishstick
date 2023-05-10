package com.ttlock.bl.sdk.entity

import android.text.TextUtils

/**
 * Created by Administrator on 2016/8/12 0012.
 */
class TransferData {
    /**
     * 接口操作指令
     */
    private var APICommand = 0

    /**
     * 命令字
     */
    private var command: Byte = 0

    /**
     * 版本信息json
     */
    var lockVersion: String? = null

    /**
     * 判断管理员
     */
    var adminPs: String? = null

    /**
     * 开门约定数
     */
    var unlockKey: String? = null

    /**
     * 三代锁使用 账号id
     */
    var mUid = 0

    /**
     * 密码数据
     */
    var pwdInfo: String? = null

    /**
     * 时间戳
     */
    var timestamp: Long = 0

    /**
     * 键盘密码
     */
    var keyboardPwd: String? = null

    /**
     * 删除密码
     */
    var deletePwd: String? = null

    /**
     * 锁标志位
     */
    var lockFlagPos = 0

    /**
     * 设置的锁名称
     */
    var lockname: String? = null

    /**
     * 原始密码
     */
    var originalPwd: String? = null

    /**
     * 新密码
     */
    var newPwd: String? = null

    /**
     * 起始时间
     */
    var startDate: Long = 0

    /**
     * 结束时间
     */
    var endDate: Long = 0

    /**
     * 键盘密码类型
     */
    var keyboardPwdType: Byte = 0

    /**
     * 三代锁开门判重实用
     */
    //(现在不用于校准锁时间,只用来生成开门的唯一标识)
    private var unlockDate = System.currentTimeMillis()

    /**
     * 需要校准的时间
     */
    private var calibationTime: Long = 0

    /**
     * 锁时区和UTC时区时间的差数，单位milliseconds
     */
    private var timezoneOffSet: Long =
        TimeZone.getDefault().getOffset(System.currentTimeMillis()).toLong()

    /**
     * 传送数据
     */
    var transferData: ByteArray

    /**
     * IC卡卡号或指纹卡号
     */
    private var No: Long = 0

    /**
     * 手环KEY
     */
    private var wristbandKey: String? = null

    /**
     * 密码列表
     */
    private var pwds: List<String>? = null

    /**
     * 操作类型
     */
    private var op = 0

    /**
     * 恢复数据
     */
    private var json: String? = null
    private var seq: Short = 0

    /**
     * 默认-1
     * 操作值
     */
    private var opValue = -1

    //    private int feature;
    private var address = ""
    private var port: Short = 0
    private var hotelData: HotelData? = null

    /**
     * 全部跟新数据
     */
    private var operateLogType: OperateLogType? = null
    private var logType = 0
    private var sector: Short = 0
    private var validityInfo: ValidityInfo? = null
    private var nbAwakeConfig: NBAwakeConfig? = null
    private var activateFloors: List<Int>? = null
    private var unlockDirection: UnlockDirection? = null
    private var accessoryInfo: AccessoryInfo? = null
    private var keyFobMac: String? = null
    private var soundVolume: SoundVolume? = null

    /**
     * 门磁mac
     */
    private var doorSensorMac: String? = null

    /**
     * 灵敏度
     */
    private var sensitivity = 0
    private var WifiName: String? = null
    private var wifiPassword: String? = null
    fun getUnlockDirection(): UnlockDirection? {
        return unlockDirection
    }

    fun setUnlockDirection(unlockDirection: UnlockDirection?) {
        this.unlockDirection = unlockDirection
    }

    fun getLockVersion(): String? {
        return lockVersion
    }

    fun setLockVersion(lockVersion: String?) {
        this.lockVersion = lockVersion
    }

    fun getAesKeyArray(): ByteArray {
        return aesKeyArray
    }

    fun setAesKeyArray(aesKeyArray: ByteArray) {
        Companion.aesKeyArray = aesKeyArray
    }

    fun getAPICommand(): Int {
        return APICommand
    }

    fun setAPICommand(APICommand: Int) {
        this.APICommand = APICommand
    }

    fun getCommand(): Byte {
        return command
    }

    fun setCommand(command: Byte) {
        this.command = command
    }

    fun getAdminPs(): String? {
        return adminPs
    }

    fun setAdminPs(adminPs: String?) {
        this.adminPs = adminPs
    }

    fun getUnlockKey(): String? {
        return unlockKey
    }

    fun setUnlockKey(unlockKey: String?) {
        this.unlockKey = unlockKey
    }

    fun getmUid(): Int {
        return mUid
    }

    fun setmUid(mUid: Int) {
        this.mUid = mUid
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

    fun getKeyboardPwd(): String? {
        return keyboardPwd
    }

    fun setKeyboardPwd(keyboardPwd: String?) {
        this.keyboardPwd = keyboardPwd
    }

    fun getDeletePwd(): String? {
        return deletePwd
    }

    fun setDeletePwd(deletePwd: String?) {
        this.deletePwd = deletePwd
    }

    fun getLockFlagPos(): Int {
        return lockFlagPos
    }

    fun setLockFlagPos(lockFlagPos: Int) {
        this.lockFlagPos = lockFlagPos
    }

    fun getLockname(): String? {
        return lockname
    }

    fun setLockname(lockname: String?) {
        this.lockname = lockname
    }

    fun getOriginalPwd(): String? {
        return originalPwd
    }

    fun setOriginalPwd(originalPwd: String?) {
        this.originalPwd = originalPwd
    }

    fun getNewPwd(): String? {
        return newPwd
    }

    fun setNewPwd(newPwd: String?) {
        this.newPwd = newPwd
        if (this.newPwd == null) this.newPwd = ""
    }

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

    fun getKeyboardPwdType(): Byte {
        return keyboardPwdType
    }

    fun setKeyboardPwdType(keyboardPwdType: Byte) {
        this.keyboardPwdType = keyboardPwdType
    }

    fun getUnlockDate(): Long {
        return unlockDate
    }

    fun setUnlockDate(unlockDate: Long) {
        this.unlockDate = unlockDate
    }

    fun getCalibationTime(): Long {
        return calibationTime
    }

    fun setCalibationTime(calibationTime: Long) {
        this.calibationTime = calibationTime
    }

    fun getTransferData(): ByteArray {
        return transferData
    }

    fun setTransferData(transferData: ByteArray) {
        this.transferData = transferData
    }

    fun getNo(): Long {
        return No
    }

    fun setNo(no: Long) {
        No = no
    }

    fun getWristbandKey(): String? {
        return wristbandKey
    }

    fun setWristbandKey(wristbandKey: String?) {
        this.wristbandKey = wristbandKey
    }

    fun getTimezoneOffSet(): Long {
        return timezoneOffSet
    }

    fun setTimezoneOffSet(timezoneOffSet: Long) {
        if (timezoneOffSet != -1L) this.timezoneOffSet = timezoneOffSet
    }

    fun getPwds(): List<String>? {
        return pwds
    }

    fun setPwds(pwds: List<String>?) {
        this.pwds = pwds
    }

    fun getOp(): Int {
        return op
    }

    fun setOp(op: Int) {
        this.op = op
    }

    fun getJson(): String? {
        return json
    }

    fun setJson(json: String?) {
        this.json = json
    }

    fun getSeq(): Short {
        return seq
    }

    fun setSeq(seq: Short) {
        this.seq = seq
    }

    fun getOpValue(): Int {
        return opValue
    }

    fun setOpValue(opValue: Int) {
        this.opValue = opValue
    }

    //    public int getFeature() {
    //        return feature;
    //    }
    //    public void setFeature(int feature) {
    //        this.feature = feature;
    //    }
    fun getAddress(): String {
        return address
    }

    fun setAddress(address: String?) {
        if (address != null) this.address = address
    }

    fun getPort(): Short {
        return port
    }

    fun setPort(port: Short) {
        this.port = port
    }

    fun getHotelData(): HotelData? {
        return hotelData
    }

    fun setHotelData(hotelData: HotelData?) {
        this.hotelData = hotelData
    }

    fun getOperateLogType(): OperateLogType? {
        return operateLogType
    }

    fun setOperateLogType(operateLogType: OperateLogType?) {
        this.operateLogType = operateLogType
    }

    fun getLogType(): Int {
        return logType
    }

    fun setLogType(logType: Int) {
        this.logType = logType
    }

    fun getSector(): Short {
        return sector
    }

    fun setSector(sector: Short) {
        this.sector = sector
    }

    fun getValidityInfo(): ValidityInfo? {
        return validityInfo
    }

    fun setValidityInfo(validityInfo: ValidityInfo?) {
        this.validityInfo = validityInfo
    }

    fun getNbAwakeConfig(): NBAwakeConfig? {
        return nbAwakeConfig
    }

    fun setNbAwakeConfig(nbAwakeConfig: NBAwakeConfig?) {
        this.nbAwakeConfig = nbAwakeConfig
    }

    fun getActivateFloors(): List<Int>? {
        return activateFloors
    }

    fun setActivateFloors(activateFloors: List<Int>?) {
        this.activateFloors = activateFloors
    }

    fun getAccessoryInfo(): AccessoryInfo? {
        return accessoryInfo
    }

    fun setAccessoryInfo(accessoryInfo: AccessoryInfo?) {
        this.accessoryInfo = accessoryInfo
    }

    fun getKeyFobMac(): String? {
        return keyFobMac
    }

    fun setKeyFobMac(keyFobMac: String?) {
        this.keyFobMac = keyFobMac
    }

    fun hasCyclicConfig(): Boolean {
        return if (validityInfo == null || validityInfo!!.cyclicConfigs == null || validityInfo!!.cyclicConfigs.size == 0 || validityInfo!!.modeType == ValidityInfo.Companion.TIMED) {
            false
        } else true
    }

    fun getSoundVolume(): SoundVolume? {
        return soundVolume
    }

    fun setSoundVolume(soundVolume: SoundVolume?) {
        this.soundVolume = soundVolume
    }

    fun getDoorSensorMac(): String? {
        return doorSensorMac
    }

    fun setDoorSensorMac(doorSensorMac: String?) {
        this.doorSensorMac = doorSensorMac
    }

    fun getSensitivity(): Int {
        return sensitivity
    }

    fun setSensitivity(sensitivity: Int) {
        this.sensitivity = sensitivity
    }

    fun getWifiName(): String? {
        return WifiName
    }

    fun setWifiName(wifiName: String?) {
        WifiName = wifiName
    }

    fun getWifiPassword(): String? {
        return wifiPassword
    }

    fun setWifiPassword(wifiPassword: String?) {
        this.wifiPassword = wifiPassword
    }

    companion object {
        /**-------------------------指令数据--------------------- */
        var aesKeyArray: ByteArray
    }
}