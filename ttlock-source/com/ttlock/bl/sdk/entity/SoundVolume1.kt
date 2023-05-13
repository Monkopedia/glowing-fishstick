package com.ttlock.bl.sdk.entity

enum class SoundVolume(private val value: Int) {
    //    NO_MODIFY(0),
    FIRST_LEVEL(1), SECOND_LEVEL(2), THIRD_LEVEL(3), FOUTH_LEVEL(4), FIFTH_LEVEL(5),

    /**
     * 关闭
     */
    OFF(-1),

    /**
     * 开启
     */
    ON(-2);

    fun getValue(): Int {
        return value
    }

    companion object {
        fun getInstance(value: Int): SoundVolume {
            return when (value) {
                -1 -> SoundVolume.OFF
                -2 -> SoundVolume.ON
                1 -> SoundVolume.FIRST_LEVEL
                2 -> SoundVolume.SECOND_LEVEL
                3 -> SoundVolume.THIRD_LEVEL
                4 -> SoundVolume.FOUTH_LEVEL
                5 -> SoundVolume.FIFTH_LEVEL
                else -> SoundVolume.OFF
            }
        }
    }
}
