package com.ttlock.bl.sdk.remote.callback;


import com.ttlock.bl.sdk.remote.model.InitRemoteResult;
import com.ttlock.bl.sdk.remote.model.SystemInfo;

/**
 * Created on  2019/4/10 0010 10:25
 *
 */
public interface GetRemoteSystemInfoCallback extends RemoteCallback {
    void  onGetRemoteSystemInfoSuccess(SystemInfo systemInfo);

}
