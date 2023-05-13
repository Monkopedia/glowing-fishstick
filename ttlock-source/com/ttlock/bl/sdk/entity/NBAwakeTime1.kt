package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2020/9/14.
 */
class NBAwakeTime {
    private var nbAwakeTimeType: NBAwakeTimeType? = null
    private var minutes = 0

    constructor() {}
    constructor(nbAwakeTimeType: NBAwakeTimeType?, minutes: Int) {
        this.nbAwakeTimeType = nbAwakeTimeType
        this.minutes = minutes
    }

    fun getNbAwakeTimeType(): NBAwakeTimeType? {
        return nbAwakeTimeType
    }

    fun setNbAwakeTimeType(nbAwakeTimeType: NBAwakeTimeType?) {
        this.nbAwakeTimeType = nbAwakeTimeType
    }

    fun getMinutes(): Int {
        return minutes
    }

    fun setMinutes(minutes: Int) {
        this.minutes = minutes
    }
}
