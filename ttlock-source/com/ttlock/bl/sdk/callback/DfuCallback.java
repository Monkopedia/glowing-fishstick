package com.ttlock.bl.sdk.callback;

/**
 * Created by TTLock on 2017/8/20.
 */

public interface DfuCallback {
    public void onDfuSuccess(final String deviceAddress);

    public void onStatusChanged(int status);

    public void onDfuAborted(final String deviceAddress);

    public void onProgressChanged(final String deviceAddress, final int percent, final float speed, final float avgSpeed, final int currentPart, final int partsTotal);

    public void onError(int errorCode, String errorContent);
}
