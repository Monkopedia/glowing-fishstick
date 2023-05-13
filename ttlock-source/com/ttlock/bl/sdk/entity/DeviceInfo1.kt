package com.ttlock.bl.sdk.entity

import com.ttlock.bl.sdk.util.GsonUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Administrator on 2017/1/9 0009.
 */
class DeviceInfo {
    var lockData: String? = null

    /**
     * hex feature
     */
    var featureValue: String? = null

    /**
     * Product model(e.g. "M201")
     */
    var modelNum: String? = null

    /**
     * Hardware version(e.g. "1.3")
     */
    var hardwareRevision: String? = null

    /**
     * Firmware version(e.g. "2.1.16.705")
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
     * Date of manufacture(e.g. "20160707")
     */
    var factoryDate: String? = null

    /**
     * lock clock(e.g. "1701051531") yymmddhhmm
     */
    var lockClock: String? = null

    /**
     * 没用了 暂时保留
     */
    @Deprecated("")
    var specialValue = 0

    /**
     * 不完整密码
     */
    var passcodeKeyNumber = 0

    constructor() {}
    constructor(modelNum: String?, hardwareVersion: String?, firmwareVersion: String?) {
        this.modelNum = modelNum
        hardwareRevision = hardwareVersion
        firmwareRevision = firmwareVersion
    }

    fun setModelNum(modelNum: String?) {
        this.modelNum = modelNum
    }

    fun setHardwareRevision(hardwareRevision: String?) {
        this.hardwareRevision = hardwareRevision
    }

    fun setFirmwareRevision(firmwareRevision: String?) {
        this.firmwareRevision = firmwareRevision
    }

    fun getLockData(): String? {
        return lockData
    }

    fun setLockData(lockData: String?) {
        this.lockData = lockData
    }

    fun getFeatureValue(): String? {
        return featureValue
    }

    fun setFeatureValue(featureValue: String?) {
        this.featureValue = featureValue
    }

    fun getModelNum(): String? {
        return modelNum
    }

    fun getHardwareRevision(): String? {
        return hardwareRevision
    }

    fun getFirmwareRevision(): String? {
        return firmwareRevision
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

    fun getFactoryDate(): String? {
        return factoryDate
    }

    fun setFactoryDate(factoryDate: String?) {
        this.factoryDate = factoryDate
    }

    fun getLockClock(): String? {
        return lockClock
    }

    fun setLockClock(lockClock: String?) {
        this.lockClock = lockClock
    }

    fun getLockClockStamp(): Long { // 考虑时区问题
        val sdr = SimpleDateFormat("yyMMddHHmm", Locale.getDefault())
        var date = Date()
        try {
            date = sdr.parse(lockClock)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date.time
    }

    fun getDeviceInfo(): String? {
        return GsonUtil.toJson<DeviceInfo>(this)
    }

    override fun toString(): String {
        return "DeviceInfo{" +
            "featureValue=" + featureValue +
            ", modelNum='" + modelNum + '\'' +
            ", hardwareRevision='" + hardwareRevision + '\'' +
            ", firmwareRevision='" + firmwareRevision + '\'' +
            ", nbNodeId='" + nbNodeId + '\'' +
            ", nbOperator='" + nbOperator + '\'' +
            ", nbCardNumber='" + nbCardNumber + '\'' +
            ", nbRssi=" + nbRssi +
            ", factoryDate='" + factoryDate + '\'' +
            ", lockClock='" + lockClock + '\'' +
            '}'
    }
}
