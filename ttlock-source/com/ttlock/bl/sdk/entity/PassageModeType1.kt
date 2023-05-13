package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2019/1/25.
 */
enum class PassageModeType(private val value: Int) {
    Weekly(1), Monthly(2);

    fun getValue(): Int {
        return value
    }
}
