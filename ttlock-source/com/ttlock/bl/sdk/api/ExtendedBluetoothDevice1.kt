package com.ttlock.bl.sdk.api

import com.ttlock.bl.sdk.constant.Constant
import com.ttlock.bl.sdk.device.TTDevice
import com.ttlock.bl.sdk.entity.HotelData
import java.util.*
import android.util.*
import android.bluetooth.*
import com.ttlock.bl.sdk.constant.LockType
import com.ttlock.bl.sdk.entity.LockVersion
import com.ttlock.bl.sdk.entity.Scene
import com.ttlock.bl.sdk.gateway.model.GatewayType
import com.ttlock.bl.sdk.util.DigitUtil

/**
 * Created by Sciener on 2016/5/13.
 */
class ExtendedBluetoothDevice : TTDevice {
    private var parkStatus = 0

    //    private BluetoothDevice device;
    //
    //    private byte[] scanRecord;
    //
    //    private String name;
    //
    //    /** mac 地址 */
    //    private String mAddress;
    //
    //    private int rssi;
    private var protocolType: Byte = 0
    private var protocolVersion: Byte = 0
    private var scene: Byte = 0
    var groupId: Byte = 0
    var orgId: Byte = 0

    /** 锁类型  */
    private var lockTypeImpl = 0
    val lockType: Int
        get() = getLockType()

    /** 判断是否触摸过门锁 三代锁使用  */
    private var isTouch = true
    /** 判断是否是设置模式 管理员需要在此模式下进行添加  */ //    private boolean isSettingMode = true;
    /** 判断是否是打开状态 车位锁使用  */
    private var isUnlock = false
    private var txPowerLevel: Byte = 0
    /**
     * 电池电量
     * -1 表示未获取到或者不支持
     */
    //    private int batteryCapacity = -1;
    /**
     * 搜索到设备的时间
     */
    private var date = System.currentTimeMillis()

    /**
     * 判断是否是手环
     */
    private var isWristband = false

    /**
     * 判断房间锁
     * 场景1 二代锁
     * 场景2 二代锁 带永久密码 三代锁
     * 场景3 荣域定制
     *
     */
    private var isRoomLock = false

    /**
     * 判断是否是保险箱锁 场景是5 跟 11
     */
    private var isSafeLock = false

    /**
     * 判断是否是自行车锁 场景是6
     */
    private var isBicycleLock = false

    /**
     * 判断是否是车位锁
     */
    private var isLockcar = false

    /**
     * 判断是否是门禁锁 场景是4
     */
    private var isGlassLock = false

    /**
     * 判断是否是挂锁 场景是8
     */
    private var isPadLock = false

    /**
     * 判断是否是锁芯 场景是9
     */
    private var isCyLinder = false

    /**
     * scene 10
     */
    private var isRemoteControlDevice = false

    /**
     * scene 12
     */
    private var isLift = false

    /**
     * scene 13
     */
    private var isPowerSaver = false

    /**
     * 判断是否是固件升级模式
     */
    private var isDfuMode = false

    /**
     * 判断网关是否处于升级模式(泰凌蓝牙芯片)
     */
    private var isTelinkGatewayDfuMode = false

    /**
     * judge whether the lock is in dfu mode
     */
    private var isNoLockService = false

    /**
     * 只在添加管理员的回调中有效
     * 远程开锁开光状态
     */
    private var remoteUnlockSwitch = 0
    private var manufacturerId: String? = null
    var disconnectStatus = 0
    var hotelData: HotelData? = null
    private set

    /**
     * 网关类型
     */
    private var gatewayType = 0

    constructor() {}

    constructor(device: BluetoothDevice) : this(device, 0, null) {
    }

    constructor(device: BluetoothDevice, rssi: Int, scanRecord: ByteArray?) {
        this.device = device
        this.scanRecord = scanRecord
        this.rssi = rssi
        this.name = device.getName()
        this.mAddress = device.getAddress()
        if (scanRecord != null) {
            initial()
        }
    }

    constructor(scanResult: ScanResult) {
        this.device = scanResult.getDevice()
        this.scanRecord = scanResult.getScanRecord().getBytes()
        this.rssi = scanResult.getRssi()
        this.name = device?.getName()
        this.mAddress = device?.getAddress()
        date = System.currentTimeMillis()
        initial()
    }

    protected override fun initial() {
        val scanRecordLength: Int = scanRecord!!.size
        var index = 0
        var nameIsScienerDfu = false
        var isHasMAC = false
        // TODO:越界
        while (index < scanRecordLength) {
            val len: Int = scanRecord!!.get(index).toInt()
            if (len == 0) {
                break
            }
            val adtype: Byte = scanRecord!!.get(index + 1)
            when (adtype) {
                GAP_ADTYPE_LOCAL_NAME_COMPLETE -> {
                    val nameBytes = ByteArray(len - 1)
                    System.arraycopy(scanRecord, index + 2, nameBytes, 0, len - 1)
                    if (name == null || name!!.length == 0) {
                        setName(String(nameBytes))
                    }
                    if (String(nameBytes) == "ScienerDfu") {
                        nameIsScienerDfu = true
                    }
                    if (name!!.uppercase(Locale.getDefault()).startsWith("LOCK_")) {
                        isRoomLock = true
                    }
                }
                GAP_ADTYPE_MANUFACTURER_SPECIFIC -> {
                    gatewayType = GatewayType.getGatewayType(
                        Arrays.copyOfRange(
                            scanRecord,
                            index + 2,
                            index + 4
                        )
                    )
                    isHasMAC = true
                    var offset = 2
                    protocolType = scanRecord!!.get(index + offset++)
                    protocolVersion = scanRecord!!.get(index + offset++)
                    if (protocolType.toInt() == 0x12 && protocolVersion.toInt() == 0x19) { // 插座DUF模式
//                        LogUtil.d("plug is in dfu mode");
                        isDfuMode = true
                        return
                    }
                    if (protocolType.toInt() == 0x13 && protocolVersion.toInt() == 0x19) { // 网关DFU模式(泰凌微芯片)
                        isTelinkGatewayDfuMode = true
                        return
                    }
                    if (protocolType == 0xff.toByte() && protocolVersion == 0xff.toByte()) {
                        isDfuMode = true
                        //                        LogUtil.d("isDfuMode:" + isDfuMode, LogUtil.isDBG());
                        return
                    }
                    if (protocolType.toInt() == 0x34 && protocolVersion.toInt() == 0x12) {
                        isWristband = true
                    }
                    if (BluetoothImpl.Companion.scanBongOnly) { // 手环
                        return
                    }
                    if (protocolType.toInt() == 0x05 && protocolVersion.toInt() == 0x03) { // 三代锁
                        scene = scanRecord!!.get(index + offset++)
                    } else { // 其它锁
                        offset = 6 // 其它协议是从第6位开始
                        protocolType = scanRecord!!.get(index + offset++)
                        protocolVersion = scanRecord!!.get(index + offset)
                        offset = 9 // scene偏移量
                        scene = scanRecord!!.get(index + offset++)
                    }
                    if (protocolType < 0x05 || getLockType() == LockType.LOCK_TYPE_V2S) { // 老款锁没广播
                        isRoomLock = true
                        return
                    }
                    if (scene <= 3) {
                        isRoomLock = true
                    } else {
                        when (scene) {
                            Scene.GLASS_LOCK -> isGlassLock = true
                            Scene.SAFE_LOCK, Scene.SAFE_LOCK_SINGLE_PASSCODE -> isSafeLock = true
                            Scene.BICYCLE_LOCK -> isBicycleLock = true
                            Scene.PARKING_LOCK -> isLockcar = true
                            Scene.PAD_LOCK -> isPadLock = true
                            Scene.CYLINDER -> isCyLinder = true
                            Scene.REMOTE_CONTROL_DEVICE -> if (protocolType.toInt() == 0x05 && protocolVersion.toInt() == 0x03) { // 二代车位锁场景也是10 增加一个三代锁的判断
                                isRemoteControlDevice = true
                            }
                            Scene.LIFT -> isLift = true
                            Scene.POWER_SAVER -> isPowerSaver = true
                            else -> {}
                        }
                    }
                    isUnlock = if (scanRecord!!.get(index + offset).toInt() and 0x01 == 1) true else false
                    // TODO:老款锁
                    isSettingMode =
                        if (scanRecord!!.get(index + offset).toInt() and 0x04 == 0) false else true
                    if (getLockType() == LockType.LOCK_TYPE_V3 || getLockType() == LockType.LOCK_TYPE_V3_CAR) {
                        isTouch =
                            if (scanRecord!!.get(index + offset).toInt() and 0x08 == 0) false else true // 三代锁表示触摸标志位
                    } else if (getLockType() == LockType.LOCK_TYPE_CAR) { // 二代车位锁放到最后判断 遥控设备场景是10
                        isTouch = false // 车位锁默认设置成false
                        isLockcar = true
                    }
                    if (isLockcar) { // 0位 4位组合状态
                        parkStatus = if (isUnlock) {
                            if (scanRecord!!.get(index + offset).toInt() and 0x10 == 1) {
                                STATUS_PARK_UNLOCK_HAS_CAR.toInt()
                            } else {
                                STATUS_PARK_UNKNOWN.toInt()
                            }
                        } else {
                            if (scanRecord!!.get(index + offset).toInt() and 0x10 == 1) {
                                STATUS_PARK_UNLOCK_NO_CAR.toInt()
                            } else {
                                STATUS_PARK_LOCK.toInt()
                            }
                        }
                    }
                    // 电量偏移量
                    offset++
                    batteryCapacity = scanRecord!!.get(index + offset).toInt()
                    // mac地址偏移量
                    offset += 3
                    if (TextUtils.isEmpty(mAddress)) {
                        setAddress(
                            DigitUtil.getMacString(
                                Arrays.copyOfRange(
                                    scanRecord,
                                    index + offset,
                                    index + offset + 6
                                )
                            )
                        )
                    }
                }
                GAP_ADTYPE_POWER_LEVEL -> txPowerLevel = scanRecord!!.get(index + 2)
                else -> {}
            }
            index += len + 1
        }
        if (nameIsScienerDfu && !isHasMAC) {
            isDfuMode = true
        }
    }

    fun isDfuMode(): Boolean {
        return isDfuMode
    }

    fun setDfuMode(dfuMode: Boolean) {
        isDfuMode = dfuMode
    }

    fun isLift(): Boolean {
        return isLift
    }

    fun setLift(lift: Boolean) {
        isLift = lift
    }

    //
    //    public BluetoothDevice getDevice() {
    //        return device;
    //    }
    //
    //    public String getName() {
    //        return name;
    //    }
    //
    //    public void setName(String name) {
    //        this.name = name;
    //    }
    //
    //    public void setAddress(String mAddress) {
    //        this.mAddress = mAddress;
    //    }
    //    public String getAddress() {
    //        return mAddress;
    //    }
    //
    //    public boolean isSettingMode() {
    //        return isSettingMode;
    //    }
    //    public void setSettingMode(boolean settingMode) {
    //        isSettingMode = settingMode;
    //    }
    //
    //    public int getBatteryCapacity() {
    //        return batteryCapacity;
    //    }
    fun setBatteryCapacity(batteryCapacity: Byte) {
        this.batteryCapacity = batteryCapacity.toInt()
    }

    fun isTouch(): Boolean {
        return isTouch
    }

    //    public int getRssi() {
    //        return rssi;
    //    }
    //
    //    public void setRssi(int rssi) {
    //        this.rssi = rssi;
    //    }
    fun setTouch(touch: Boolean) {
        isTouch = touch
    }

    fun isWristband(): Boolean {
        return isWristband
    }

    fun setWristband(wristband: Boolean) {
        isWristband = wristband
    }

    fun isBicycleLock(): Boolean {
        return isBicycleLock
    }

    fun setBicycleLock(bicycleLock: Boolean) {
        isBicycleLock = bicycleLock
    }

    fun isSafeLock(): Boolean {
        return isSafeLock
    }

    fun setSafeLock(safeLock: Boolean) {
        isSafeLock = safeLock
    }

    fun isRoomLock(): Boolean {
        return isRoomLock
    }

    fun setRoomLock(roomLock: Boolean) {
        isRoomLock = roomLock
    }

    fun isLockcar(): Boolean {
        return isLockcar
    }

    fun setLockcar(lockcar: Boolean) {
        isLockcar = lockcar
    }

    fun isGlassLock(): Boolean {
        return isGlassLock
    }

    fun isPadLock(): Boolean {
        return isPadLock
    }

    fun isCyLinder(): Boolean {
        return isCyLinder
    }

    fun isRemoteControlDevice(): Boolean {
        return isRemoteControlDevice
    }

    fun setRemoteControlDevice(remoteControlDevice: Boolean) {
        isRemoteControlDevice = remoteControlDevice
    }

    fun setCyLinder(cyLinder: Boolean) {
        isCyLinder = cyLinder
    }

    fun setPadLock(padLock: Boolean) {
        isPadLock = padLock
    }

    fun setGlassLock(glassLock: Boolean) {
        isGlassLock = glassLock
    }

    fun getProtocolType(): Byte {
        return protocolType
    }

    fun setProtocolType(protocolType: Byte) {
        this.protocolType = protocolType
    }

    fun getProtocolVersion(): Byte {
        return protocolVersion
    }

    fun setProtocolVersion(protocolVersion: Byte) {
        this.protocolVersion = protocolVersion
    }

    fun getScene(): Byte {
        return scene
    }

    fun setScene(scene: Byte) {
        this.scene = scene
    }

    fun getRemoteUnlockSwitch(): Int {
        return remoteUnlockSwitch
    }

    fun isNoLockService(): Boolean {
        return isNoLockService
    }

    fun isPowerSaver(): Boolean {
        return isPowerSaver
    }

    fun setPowerSaver(powerSaver: Boolean) {
        isPowerSaver = powerSaver
    }

    fun setNoLockService(noLockService: Boolean) {
        isNoLockService = noLockService
    }

    fun getHotelData(): HotelData? {
        return hotelData
    }

    fun isTelinkGatewayDfuMode(): Boolean {
        return isTelinkGatewayDfuMode
    }

    fun setTelinkGatewayDfuMode(telinkGatewayDfuMode: Boolean) {
        isTelinkGatewayDfuMode = telinkGatewayDfuMode
    }

    @Throws(ParamInvalidException::class)
    fun setHotelData(hotelData: HotelData) {
        if (hotelData.hotelInfo != null) {
            val data: String = DigitUtil.decodeLockData(hotelData.getHotelInfo()!!)
            if (!TextUtils.isEmpty(data)) {
                val array: Array<String?> = data.split(",").toTypedArray()
                hotelData.hotelNumber = Integer.valueOf(array[0])
                if (array[1] != null && array[2] != null) {
                    hotelData.icKey = DigitUtil.convertStringDividerByDot(array[1]!!)
                    hotelData.aesKey = DigitUtil.convertStringDividerByDot(array[2]!!)
                } else {
                    throw ParamInvalidException()
                }
            } else {
                throw ParamInvalidException()
            }
        } else {
            throw ParamInvalidException()
        }
        this.hotelData = hotelData
    }

    fun getManufacturerId(): String? {
        return manufacturerId
    }

    fun setManufacturerId(manufacturerId: String?) {
        this.manufacturerId = manufacturerId
        Constant.VENDOR = manufacturerId!!
    }

    fun setRemoteUnlockSwitch(remoteUnlockSwitch: Int) {
        this.remoteUnlockSwitch = remoteUnlockSwitch
    }

    override fun equals(o: Any?): Boolean {
        return if (o is ExtendedBluetoothDevice) {
            mAddress == o.getAddress()
        } else false
    }

    override fun getDate(): Long {
        return date
    }

    override fun setDate(date: Long) {
        this.date = date
    }

    fun getParkStatus(): Int {
        return parkStatus
    }

    fun setParkStatus(parkStatus: Int) {
        this.parkStatus = parkStatus
    }

    // TODO:二代
    // 直接根据mac地址连接的没有扫描信息 这里的数据不准
    fun getLockType(): Int {
        if (protocolType.toInt() == 0x05 && protocolVersion.toInt() == 0x03 && scene.toInt() == 0x07) {
            lockTypeImpl = LockType.LOCK_TYPE_V3_CAR
        } else if (protocolType.toInt() == 0x0a && protocolVersion.toInt() == 0x01) {
            lockTypeImpl = LockType.LOCK_TYPE_CAR
        } else if (protocolType.toInt() == 0x0b && protocolVersion.toInt() == 0x01) {
            lockTypeImpl = LockType.LOCK_TYPE_MOBI
        } else if (protocolType.toInt() == 0x05 && protocolVersion.toInt() == 0x04) {
            lockTypeImpl = LockType.LOCK_TYPE_V2S_PLUS
        } else if (protocolType.toInt() == 0x05 && protocolVersion.toInt() == 0x03) {
            lockTypeImpl = LockType.LOCK_TYPE_V3
        } else if (protocolType.toInt() == 0x05 && protocolVersion.toInt() == 0x01 || name != null && name!!.uppercase(
                Locale.getDefault()
            ).startsWith("LOCK_")
        ) {
            lockTypeImpl = LockType.LOCK_TYPE_V2S
        }
        return lockTypeImpl
    }

    // TODO:不一定准确
    fun getLockVersionJson(): String {
        if (name!!.uppercase(Locale.getDefault()).startsWith("LOCK_")) { // 2S版本信息
            protocolType = 0x05
            protocolVersion = 0x01
        }
        return LockVersion(protocolType, protocolVersion, scene, groupId.toShort(), orgId.toShort()).toGson()
    }

    override fun toString(): String {
        return "ExtendedBluetoothDevice{" +
            "name='" + name + '\'' +
            ", mAddress='" + mAddress + '\'' +
            ", rssi=" + rssi +
            ", protocolType=" + protocolType +
            ", protocolVersion=" + protocolVersion +
            ", scene=" + scene +
            ", groupId=" + groupId +
            ", orgId=" + orgId +
            ", lockType=" + lockTypeImpl +
            ", isTouch=" + isTouch +
            ", isSettingMode=" + isSettingMode +
            ", isWristband=" + isWristband() +
            ", isUnlock=" + isUnlock +
            ", txPowerLevel=" + txPowerLevel +
            ", batteryCapacity=" + batteryCapacity +
            ", date=" + date +
            ", device=" + device +
            ", scanRecord=" + DigitUtil.byteArrayToHexString(scanRecord) +
            '}'
    }

    fun getGatewayType(): Int {
        return gatewayType
    }

    fun setGatewayType(gatewayType: Int) {
        this.gatewayType = gatewayType
    }

    companion object {
        const val GAP_ADTYPE_LOCAL_NAME_COMPLETE: Byte = 0X09 // !< Complete local name
        const val GAP_ADTYPE_POWER_LEVEL: Byte = 0X0A // !< TX Power Level: 0xXX: -127 to +127 dBm
        const val GAP_ADTYPE_MANUFACTURER_SPECIFIC =
            0XFF.toByte() // !< Manufacturer Specific Data: first 2 octets contain the Company Inentifier Code followed by the additional manufacturer specific data

        /**
         * 闭锁
         */
        const val STATUS_PARK_LOCK: Byte = 0

        /**
         * 开锁无车
         */
        const val STATUS_PARK_UNLOCK_NO_CAR: Byte = 1

        /**
         * 状态未知
         */
        const val STATUS_PARK_UNKNOWN: Byte = 2

        /**
         * 开锁有车
         */
        const val STATUS_PARK_UNLOCK_HAS_CAR: Byte = 3

        /**
         * 断开连接的未知状态
         */
        const val DISCONNECT_STATUS_NONE = 0

        /**
         * 连接超时
         */
        const val CONNECT_TIME_OUT = 1

        /**
         * 发现服务时断开
         */
        const val SERVICE_DISCONNECTED = 2

        /**
         * 指令相应超时
         */
        const val RESPONSE_TIME_OUT = 3

        /**
         * 指令操作完成的断开
         */
        const val NORMAL_DISCONNECTED = 4

        /**
         * 无法连接
         */
        const val DEVICE_CANNOT_CONNECT = 5

    }
}
