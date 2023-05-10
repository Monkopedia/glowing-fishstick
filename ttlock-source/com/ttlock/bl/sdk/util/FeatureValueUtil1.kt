package com.ttlock.bl.sdk.util

import android.text.TextUtils

/**
 * Created on  2019/4/30 0030 16:25
 *
 * @author theodre
 */
object FeatureValueUtil {
    fun isSupportFeature(lockData: String?, newFeature: Int): Boolean {
        val lockDataObj: LockData = EncryptionUtil.parseLockData(lockData)
        return isSupportFeature(lockDataObj, newFeature)
    }

    fun isSupportFeature(lockDataObj: LockData?, newFeature: Int): Boolean {
        return if (lockDataObj == null || lockDataObj.featureValue == null) {
            false
        } else isSupportFeatureValue(lockDataObj.featureValue, newFeature)
    }

    private fun isSupportFeatureValue(featureValue: String, newFeature: Int): Boolean {
        return if (TextUtils.isEmpty(featureValue)) {
            false
        } else try {
            val bigInteger = BigInteger(featureValue, 16)
            bigInteger.testBit(newFeature)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun isSupportFeature(
        lockFeatureValue: String?,
        keypadFeatureValue: String?,
        newFeature: Int
    ): Boolean {
        return isSupportFeature(
            lockFeatureValue,
            newFeature
        ) || isSupportFeature(keypadFeatureValue, newFeature)
    }

    fun FeatureValueOr(featureValue1: String?, featureValue2: String?): String {
        val bigInteger1 = BigInteger(featureValue1, 16)
        val bigInteger2 = BigInteger(featureValue2, 16)
        val bigInteger: BigInteger = bigInteger1.or(bigInteger2)
        return bigInteger.toString(16)
    }
}