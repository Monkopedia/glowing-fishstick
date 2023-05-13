package com.ttlock.bl.sdk.callback

import com.ttlock.bl.sdk.entity.LockError
import com.ttlock.bl.sdk.entity.SoundVolume

/**
 * Created on  2019/4/10 0010 13:13
 *
 * @author theodre
 */
interface GetLockSoundWithSoundVolumeCallback : LockCallback {
    fun onGetLockSoundSuccess(enable: Boolean, soundVolume: SoundVolume?)
    override fun onFail(error: LockError)
}
