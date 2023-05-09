package com.ttlock.bl.sdk.remote.callback;


import com.ttlock.bl.sdk.remote.model.RemoteError;

/**
 * Created on  2019/4/8 0008 15:31
 *
 * @author theodore
 */
public interface RemoteCallback {
    void onFail(RemoteError error);
}
