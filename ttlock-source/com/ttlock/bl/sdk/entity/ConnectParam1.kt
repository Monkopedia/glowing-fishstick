package com.ttlock.bl.sdk.entity

/**
 * Created on  2019/4/12 0012 16:34
 *
 * @author theodre
 */
class ConnectParam {
    private var lockDataImpl: LockData? = null
    var lockData: LockData
        get() = lockDataImpl ?: LockData().also { lockDataImpl = it }
        set(value) {
            lockDataImpl = value
        }
    var controlAction = 0
    var lockModeEnable = false
    var passageModeConfig: PassageModeConfig? = null
    var timestamp: Long = 0
    var autoLockingPeriod = 0
    var logType = 0
    var recoveryDataStr: String? = null
    var recoveryDataType = 0
    var startDate: Long = 0
    var endDate: Long = 0
    var originalPasscode: String? = null
    var newPasscode: String? = null
    var attachmentNum: Long = 0
    var dataJsonStr: String? = null
    var serverAddress: String? = null
    var serverPort: Short = 0
    var lockmac: String? = null

    //    private String factoryDate;
    var seconds = 0
    var hotelData: HotelData? = null
    var ttLockConfigType: TTLockConfigType? = null
    var validityInfo: ValidityInfo? = null
    var nbAwakeConfig: NBAwakeConfig? = null
    var activateFloors: List<Int>? = null
    var unlockDirection: UnlockDirection? = null
    var accessoryInfo: AccessoryInfo? = null
    var keyfobMac: String? = null
    var doorSensorMac: String? = null
    var wifiName: String? = null
    var wifiPassword: String? = null
    var ipSetting: IpSetting? = null
    var soundVolume: SoundVolume? = null
    fun getLockmac(): String? {
        return lockmac
    }

    fun setLockmac(lockmac: String?) {
        this.lockmac = lockmac
    }

    //    public String getFactoryDate() {
    //        return factoryDate;
    //    }
    //
    //    public void setFactoryDate(String factoryDate) {
    //        this.factoryDate = factoryDate;
    //    }
    fun getServerAddress(): String? {
        return serverAddress
    }

    fun setServerAddress(serverAddress: String?) {
        this.serverAddress = serverAddress
    }

    fun getServerPort(): Short {
        return serverPort
    }

    fun setServerPort(serverPort: Short) {
        this.serverPort = serverPort
    }

    fun getDataJsonStr(): String? {
        return dataJsonStr
    }

    fun setDataJsonStr(dataJsonStr: String?) {
        this.dataJsonStr = dataJsonStr
    }

    fun getAttachmentNum(): Long {
        return attachmentNum
    }

    fun setAttachmentNum(attachmentNum: Long) {
        this.attachmentNum = attachmentNum
    }

    fun getNewPasscode(): String? {
        return newPasscode
    }

    fun setNewPasscode(newPasscode: String?) {
        this.newPasscode = newPasscode
    }

    fun getAutoLockingPeriod(): Int {
        return autoLockingPeriod
    }

    fun setAutoLockingPeriod(autoLockingPeriod: Int) {
        this.autoLockingPeriod = autoLockingPeriod
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

    fun getOriginalPasscode(): String? {
        return originalPasscode
    }

    fun setOriginalPasscode(originalPasscode: String?) {
        this.originalPasscode = originalPasscode
    }

    fun getRecoveryDataStr(): String? {
        return recoveryDataStr
    }

    fun setRecoveryDataStr(recoveryDataStr: String?) {
        this.recoveryDataStr = recoveryDataStr
    }

    fun getRecoveryDataType(): Int {
        return recoveryDataType
    }

    fun setRecoveryDataType(recoveryDataType: Int) {
        this.recoveryDataType = recoveryDataType
    }

    fun getLogType(): Int {
        return logType
    }

    fun setLogType(logType: Int) {
        this.logType = logType
    }

    fun getTimestamp(): Long {
        return timestamp
    }

    fun setTimestamp(timestamp: Long) {
        this.timestamp = timestamp
    }

    fun getSeconds(): Int {
        return seconds
    }

    fun setSeconds(seconds: Int) {
        this.seconds = seconds
    }

    fun getPassageModeConfig(): PassageModeConfig? {
        return passageModeConfig
    }

    fun setPassageModeConfig(passageModeConfig: PassageModeConfig?) {
        this.passageModeConfig = passageModeConfig
    }

    fun isLockModeEnable(): Boolean {
        return lockModeEnable
    }

    fun setLockModeEnable(lockModeEnable: Boolean) {
        this.lockModeEnable = lockModeEnable
    }

    fun getControlAction(): Int {
        return controlAction
    }

    fun setControlAction(controlAction: Int) {
        this.controlAction = controlAction
    }

    fun getTtLockConfigType(): TTLockConfigType? {
        return ttLockConfigType
    }

    fun setTtLockConfigType(ttLockConfigType: TTLockConfigType?) {
        this.ttLockConfigType = ttLockConfigType
    }

    fun getLockData(): LockData {
        return lockData
    }

    fun setLockData(lockData: LockData?) {
        this.lockDataImpl = lockData
    }

    fun getHotelData(): HotelData? {
        return hotelData
    }

    fun setHotelData(hotelData: HotelData?) {
        this.hotelData = hotelData
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

    fun getUnlockDirection(): UnlockDirection? {
        return unlockDirection
    }

    fun setUnlockDirection(unlockDirection: UnlockDirection?) {
        this.unlockDirection = unlockDirection
    }

    fun getAccessoryInfo(): AccessoryInfo? {
        return accessoryInfo
    }

    fun setAccessoryInfo(accessoryInfo: AccessoryInfo?) {
        this.accessoryInfo = accessoryInfo
    }

    fun getKeyfobMac(): String? {
        return keyfobMac
    }

    fun setKeyfobMac(keyfobMac: String?) {
        this.keyfobMac = keyfobMac
    }

    fun getWifiName(): String? {
        return wifiName
    }

    fun setWifiName(wifiName: String?) {
        this.wifiName = wifiName
    }

    fun getWifiPassword(): String? {
        return wifiPassword
    }

    fun setWifiPassword(wifiPassword: String?) {
        this.wifiPassword = wifiPassword
    }

    fun getIpSetting(): IpSetting? {
        return ipSetting
    }

    fun setIpSetting(ipSetting: IpSetting?) {
        this.ipSetting = ipSetting
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
}
