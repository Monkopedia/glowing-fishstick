package com.ttlock.bl.sdk.remote.callback;

import com.ttlock.bl.sdk.device.Remote;
import com.ttlock.bl.sdk.remote.model.RemoteError;

/**
 * Created on  2019/4/12 0012 11:59
 *
 * @author theodre
 */
public interface ConnectCallback extends RemoteCallback {
    void onConnectSuccess(Remote device);
    @Override
    void onFail(RemoteError error);
}
