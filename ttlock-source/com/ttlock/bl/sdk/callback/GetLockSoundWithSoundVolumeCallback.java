package com.ttlock.bl.sdk.callback;


import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.entity.SoundVolume;

/**
 * Created on  2019/4/10 0010 13:13
 *
 * @author theodre
 */
public interface GetLockSoundWithSoundVolumeCallback extends LockCallback {

    void onGetLockSoundSuccess(boolean enable, SoundVolume soundVolume);

    @Override
    void onFail(LockError error);
}
