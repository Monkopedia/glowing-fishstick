package com.ttlock.bl.sdk.entity

import android.os.Parcel

/**
 * Created by TTLock on 2018/9/21.
 */
class HotelData : Parcelable {
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
    var controlableFloors: ByteArray
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
    fun getICKeyByteArray(): ByteArray {
        return DigitUtil.convertAesKeyStrToBytes(icKey)
    }

    fun getAesKeyByteArray(): ByteArray {
        return DigitUtil.convertAesKeyStrToBytes(aesKey)
    }

    fun getBuildingNumber(): Int {
        return buildingNumber
    }

    fun setBuildingNumber(buildingNumber: Int) {
        this.buildingNumber = buildingNumber
    }

    fun getFloorNumber(): Int {
        return floorNumber
    }

    fun setFloorNumber(floorNumber: Int) {
        this.floorNumber = floorNumber
    }

    fun getIcKey(): String {
        return icKey
    }

    fun setIcKey(icKey: String) {
        this.icKey = icKey
    }

    fun getAesKey(): String {
        return aesKey
    }

    fun setAesKey(aesKey: String) {
        this.aesKey = aesKey
    }

    fun getHotelNumber(): Int {
        return hotelNumber
    }

    fun setHotelNumber(hotelNumber: Int) {
        this.hotelNumber = hotelNumber
    }

    fun getSector(): Short {
        return sector
    }

    fun setSector(sector: Short) {
        this.sector = sector
    }

    fun getControlableFloors(): ByteArray {
        return controlableFloors
    }

    fun setControlableFloors(controlableFloors: ByteArray) {
        this.controlableFloors = controlableFloors
    }

    fun getTtLiftWorkMode(): TTLiftWorkMode? {
        return ttLiftWorkMode
    }

    fun setTtLiftWorkMode(ttLiftWorkMode: TTLiftWorkMode?) {
        this.ttLiftWorkMode = ttLiftWorkMode
    }

    fun getControlableLockMac(): String? {
        return controlableLockMac
    }

    fun setControlableLockMac(controlableLockMac: String?) {
        this.controlableLockMac = controlableLockMac
    }

    fun getPowerWorkModeValue(): Byte {
        return powerWorkModeValue
    }

    fun setPowerWorkModeValue(powerWorkModeValue: Byte) {
        this.powerWorkModeValue = powerWorkModeValue
    }

    fun getHotelInfo(): String? {
        return hotelInfo
    }

    fun setHotelInfo(hotelInfo: String?) {
        this.hotelInfo = hotelInfo
    }

    fun getParaType(): Byte {
        return paraType
    }

    fun setParaType(paraType: Byte) {
        this.paraType = paraType
    }

    constructor() {}

    fun describeContents(): Int {
        return 0
    }

    fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(icKey)
        dest.writeString(aesKey)
        dest.writeInt(hotelNumber)
        dest.writeInt(buildingNumber)
        dest.writeInt(floorNumber)
        dest.writeString(hotelInfo)
        dest.writeInt(sector)
        dest.writeByteArray(controlableFloors)
        dest.writeInt(if (ttLiftWorkMode == null) -1 else ttLiftWorkMode!!.ordinal)
        dest.writeByte(powerWorkModeValue)
        dest.writeByte(paraType)
    }

    protected constructor(`in`: Parcel) {
        icKey = `in`.readString()
        aesKey = `in`.readString()
        hotelNumber = `in`.readInt()
        buildingNumber = `in`.readInt()
        floorNumber = `in`.readInt()
        hotelInfo = `in`.readString()
        sector = `in`.readInt()
        controlableFloors = `in`.createByteArray()
        val tmpTtLiftWorkMode: Int = `in`.readInt()
        ttLiftWorkMode =
            if (tmpTtLiftWorkMode == -1) null else TTLiftWorkMode.values()[tmpTtLiftWorkMode]
        powerWorkModeValue = `in`.readByte()
        paraType = `in`.readByte()
    }

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
        val CREATOR: Creator<HotelData> = object : Creator<HotelData?>() {
            fun createFromParcel(source: Parcel): HotelData {
                return HotelData(source)
            }

            fun newArray(size: Int): Array<HotelData> {
                return arrayOfNulls(size)
            }
        }
    }
}
