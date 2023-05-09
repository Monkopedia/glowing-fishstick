package com.ttlock.bl.sdk.callback;

import com.ttlock.bl.sdk.entity.Error;

/**
 * Created by TTLock on 2017/8/20.
 */

public interface DeviceFirmwareUpdateCallback {
    public void onGetLockFirmware(int specialValue, String module, String hardware, String firmware);

    public void onStatusChanged(int status);

    public void onDfuProcessStarting(final String deviceAddress);

    public void onEnablingDfuMode(final String deviceAddress);

    public void onDfuCompleted(final String deviceAddress);

    public void onDfuAborted(final String deviceAddress);

    public void onProgressChanged(final String deviceAddress, final int percent, final float speed, final float avgSpeed, final int currentPart, final int partsTotal);

    public void onError(int errorCode, Error error, String errorContent);
}
