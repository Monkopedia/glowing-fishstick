package com.ttlock.bl.sdk.base;

import com.ttlock.bl.sdk.device.TTDevice;

/**
 * Created on  2019/3/29 0029 15:08
 *
 * @author theodre
 */
public interface BaseScanCallback <T extends TTDevice> {
     void onScan(T device);
}
