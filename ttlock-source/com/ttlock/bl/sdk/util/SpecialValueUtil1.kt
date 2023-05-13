package com.ttlock.bl.sdk.util

/**
 * Created on  2019/4/30 0030 16:25
 *
 * @author theodre
 */
@Deprecated("")
object SpecialValueUtil {
    fun isSupportFeature(specialValue: Int, feature: Int): Boolean {
        return specialValue and feature != 0
    }
}
