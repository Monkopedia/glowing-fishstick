package com.ttlock.bl.sdk.entity

import com.ttlock.bl.sdk.util.DigitUtil


/**
 * Created by TTLock on 2018/9/21.
 */
class HotelData {
    /**
     * hotelInfo解析内容
     */
    var icKey = "a9,65,65,4d,3d,46"
    var aesKey = "a9,65,65,4d,3d,46,94,e6,3b,68,c3,07,cf,ea,4b,54"
    var hotelNumber = 1
    var buildingNumber = 1
    var floorNumber = 1
    var hotelInfo: String? = null

    /**
     * deault 0:use all sectors
     */
    var sector: Short = 0
    var controlableFloors: ByteArray = byteArrayOf()
    var ttLiftWorkMode: TTLiftWorkMode? = null
    var controlableLockMac: String? = null

    /**
     * default:use all work mode
     */
    var powerWorkModeValue: Byte = 0

    /**
     * current set type
     */
    var paraType = TYPE_IC_KEY
    fun getICKeyByteArray(): ByteArray? {
        return DigitUtil.convertAesKeyStrToBytes(icKey)
    }

    fun getAesKeyByteArray(): ByteArray? {
        return DigitUtil.convertAesKeyStrToBytes(aesKey)
    }

    @JvmName("getBuildingNumber1")
    fun getBuildingNumber(): Int {
        return buildingNumber
    }

    @JvmName("setBuildingNumber1")
    fun setBuildingNumber(buildingNumber: Int) {
        this.buildingNumber = buildingNumber
    }

    @JvmName("getFloorNumber1")
    fun getFloorNumber(): Int {
        return floorNumber
    }

    @JvmName("setFloorNumber1")
    fun setFloorNumber(floorNumber: Int) {
        this.floorNumber = floorNumber
    }

    @JvmName("getIcKey1")
    fun getIcKey(): String {
        return icKey
    }

    @JvmName("setIcKey1")
    fun setIcKey(icKey: String) {
        this.icKey = icKey
    }

    @JvmName("getAesKey1")
    fun getAesKey(): String {
        return aesKey
    }

    @JvmName("setAesKey1")
    fun setAesKey(aesKey: String) {
        this.aesKey = aesKey
    }

    @JvmName("getHotelNumber1")
    fun getHotelNumber(): Int {
        return hotelNumber
    }

    @JvmName("setHotelNumber1")
    fun setHotelNumber(hotelNumber: Int) {
        this.hotelNumber = hotelNumber
    }

    @JvmName("getSector1")
    fun getSector(): Short {
        return sector
    }

    @JvmName("setSector1")
    fun setSector(sector: Short) {
        this.sector = sector
    }

    @JvmName("getControlableFloors1")
    fun getControlableFloors(): ByteArray {
        return controlableFloors
    }

    @JvmName("setControlableFloors1")
    fun setControlableFloors(controlableFloors: ByteArray) {
        this.controlableFloors = controlableFloors
    }

    @JvmName("getTtLiftWorkMode1")
    fun getTtLiftWorkMode(): TTLiftWorkMode? {
        return ttLiftWorkMode
    }

    @JvmName("setTtLiftWorkMode1")
    fun setTtLiftWorkMode(ttLiftWorkMode: TTLiftWorkMode?) {
        this.ttLiftWorkMode = ttLiftWorkMode
    }

    @JvmName("getControlableLockMac1")
    fun getControlableLockMac(): String? {
        return controlableLockMac
    }

    @JvmName("setControlableLockMac1")
    fun setControlableLockMac(controlableLockMac: String?) {
        this.controlableLockMac = controlableLockMac
    }

    @JvmName("getPowerWorkModeValue1")
    fun getPowerWorkModeValue(): Byte {
        return powerWorkModeValue
    }

    @JvmName("setPowerWorkModeValue1")
    fun setPowerWorkModeValue(powerWorkModeValue: Byte) {
        this.powerWorkModeValue = powerWorkModeValue
    }

    @JvmName("getHotelInfo1")
    fun getHotelInfo(): String? {
        return hotelInfo
    }

    @JvmName("setHotelInfo1")
    fun setHotelInfo(hotelInfo: String?) {
        this.hotelInfo = hotelInfo
    }

    @JvmName("getParaType1")
    fun getParaType(): Byte {
        return paraType
    }

    @JvmName("setParaType1")
    fun setParaType(paraType: Byte) {
        this.paraType = paraType
    }

    constructor() {}

    companion object {
        const val TYPE_DEFAULT: Byte = 0
        const val TYPE_IC_KEY: Byte = 1
        const val TYPE_AES_KEY: Byte = 2
        const val TYPE_HOTEL_BUILDING_FLOOR: Byte = 3
        const val TYPE_SECTOR: Byte = 4
        const val TYPE_ELEVATOR_CONTROLABLE_FLOORS: Byte = 5
        const val TYPE_ELEVATOR_WORK_MODE: Byte = 6
        const val TYPE_POWER_SAVER_WORK_MODE: Byte = 7
        const val TYPE_POWER_SAVER_CONTROLABLE_LOCK: Byte = 8

        /**
         * 查询
         */
        const val GET = 1

        /**
         * 设置
         */
        const val SET = 2
    }
}
