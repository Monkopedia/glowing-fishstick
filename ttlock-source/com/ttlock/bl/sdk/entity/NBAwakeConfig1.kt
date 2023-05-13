package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2020/9/14.
 */
class NBAwakeConfig {
    private var nbAwakeModeList: List<NBAwakeMode>? = null
    private var nbAwakeTimeList: List<NBAwakeTime>? = null
    fun getNbAwakeModeList(): List<NBAwakeMode>? {
        return nbAwakeModeList
    }

    fun setNbAwakeModeList(nbAwakeModeList: List<NBAwakeMode>?) {
        var nbAwakeModeList = nbAwakeModeList
        if (nbAwakeModeList == null) {
            nbAwakeModeList = ArrayList()
        }
        this.nbAwakeModeList = nbAwakeModeList
    }

    fun getNbAwakeTimeList(): List<NBAwakeTime>? {
        return nbAwakeTimeList
    }

    fun setNbAwakeTimeList(nbAwakeTimeList: List<NBAwakeTime>?) {
        var nbAwakeTimeList = nbAwakeTimeList
        if (nbAwakeTimeList == null) {
            nbAwakeTimeList = ArrayList()
        }
        this.nbAwakeTimeList = nbAwakeTimeList
    }

    companion object {
        const val ACTION_AWAKE_MODE: Byte = 1
        const val ACTION_AWAKE_TIME: Byte = 2
    }
}
