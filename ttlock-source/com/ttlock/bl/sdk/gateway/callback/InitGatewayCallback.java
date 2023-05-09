package com.ttlock.bl.sdk.gateway.callback;


import com.ttlock.bl.sdk.gateway.model.DeviceInfo;
import com.ttlock.bl.sdk.gateway.model.GatewayError;

/**
 * Created on  2019/4/10 0010 10:25
 *
 * @author theodre
 */
public interface InitGatewayCallback extends  GatewayCallback {
    void  onInitGatewaySuccess(DeviceInfo deviceInfo);
    
    @Override
    void onFail(GatewayError error);
}
