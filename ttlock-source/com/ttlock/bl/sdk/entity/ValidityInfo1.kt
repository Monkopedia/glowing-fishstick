package com.ttlock.bl.sdk.entity

import com.ttlock.bl.sdk.constant.Constant
import com.ttlock.bl.sdk.util.LogUtil
import java.io.Serializable

/**
 * Created by TTLock on 2020/3/19.
 */
class ValidityInfo : Serializable {
    private var modeType: Int = ValidityInfo.Companion.TIMED

    /**
     * default permanent period
     */
    private var startDate = Constant.permanentStartDate
    private var endDate = Constant.permanentEndDate
    private var cyclicConfigs: List<CyclicConfig>? = null
    fun getModeType(): Int {
        return modeType
    }

    fun setModeType(modeType: Int) {
        this.modeType = modeType
    }

    fun getStartDate(): Long {
        return startDate
    }

    fun setStartDate(startDate: Long) {
        var startDate = startDate
        if (startDate == 0L) {
            startDate = Constant.permanentStartDate
        }
        this.startDate = startDate
    }

    fun getEndDate(): Long {
        return endDate
    }

    fun setEndDate(endDate: Long) {
        var endDate = endDate
        if (endDate == 0L) {
            endDate = Constant.permanentEndDate
        }
        this.endDate = endDate
    }

    fun getCyclicConfigs(): List<CyclicConfig>? {
        return cyclicConfigs
    }

    fun setCyclicConfigs(cyclicConfigs: List<CyclicConfig>?) {
        this.cyclicConfigs = cyclicConfigs
    }

    private fun isValidCyclicConfig(): Boolean {
        if (cyclicConfigs == null) {
            return false
        }
        for (cyclicConfig in cyclicConfigs!!) {
            if (!cyclicConfig.isValidData) {
                return false
            }
        }
        return true
    }

    fun isValidData(): Boolean {
        if (startDate > endDate) {
            LogUtil.d("startDate > endDate")
            return false
        }
        when (modeType) {
            ValidityInfo.Companion.TIMED -> {}
            ValidityInfo.Companion.CYCLIC -> return isValidCyclicConfig()
            else -> return false
        }
        return true
    }

    override fun toString(): String {
        return "ValidityInfo{" +
            "modeType=" + modeType +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", cyclicConfigs=" + cyclicConfigs +
            '}'
    }

    companion object {
        const val TIMED = 1
        const val CYCLIC = 4
        fun getTIMED(): Int {
            return ValidityInfo.Companion.TIMED
        }

        fun getCYCLIC(): Int {
            return ValidityInfo.Companion.CYCLIC
        }
    }
}
