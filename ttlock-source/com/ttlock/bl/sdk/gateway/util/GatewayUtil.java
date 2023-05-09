package com.ttlock.bl.sdk.gateway.util;

import android.text.TextUtils;

public class GatewayUtil {

    public static byte[] convertIp2Bytes(String ip) {
        byte[] bytes = new byte[4];
        if (TextUtils.isEmpty(ip)) {
            return bytes;
        }
        try {
            String[] dividerList = ip.split("\\.");
            if (dividerList.length != 4) {
                return null;
            }
            for (int i=0;i<4;i++) {
                int temp = Integer.valueOf(dividerList[i]);
                if (temp < 0 || temp > 255) {
                    return null;
                }
                bytes[i] = (byte) temp;
            }
        } catch (Exception e) {
            return null;
        }
        return bytes;
    }
}
