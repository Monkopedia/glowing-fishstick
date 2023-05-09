package com.ttlock.bl.sdk.gateway.callback;


import com.ttlock.bl.sdk.gateway.model.GatewayError;
import com.ttlock.bl.sdk.gateway.model.WiFi;

import java.util.List;

/**
 * Created on  2019/4/10 0010 10:25
 *
 * @author theodre
 */
public interface ScanWiFiByGatewayCallback extends  GatewayCallback {
    void  onScanWiFiByGateway(List<WiFi> wiFis);

    void  onScanWiFiByGatewaySuccess();

    @Override
    void onFail(GatewayError error);
}
