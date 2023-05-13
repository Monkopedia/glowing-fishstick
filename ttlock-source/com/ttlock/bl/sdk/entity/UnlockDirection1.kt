package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2020/12/16.
 */
enum class UnlockDirection(var value: Int) {
    LEFT(1), RIGHT(0);

    fun getValue(): Int {
        return value
    }

    companion object {
        fun getInstance(value: Int): UnlockDirection {
            return when (value) {
                1 -> UnlockDirection.LEFT
                else -> UnlockDirection.RIGHT
            }
        }
    }
}
