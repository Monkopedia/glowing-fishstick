package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2020/7/5.
 */
class HotelInfo {
    var hotelNumber = 0
    var icKey: String? = null
    var aesKey: String? = null
    fun getHotelNumber(): Int {
        return hotelNumber
    }

    fun setHotelNumber(hotelNumber: Int) {
        this.hotelNumber = hotelNumber
    }

    fun getIcKey(): String? {
        return icKey
    }

    fun setIcKey(icKey: String?) {
        this.icKey = icKey
    }

    fun getAesKey(): String? {
        return aesKey
    }

    fun setAesKey(aesKey: String?) {
        this.aesKey = aesKey
    }
}
