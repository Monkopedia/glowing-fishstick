package com.ttlock.bl.sdk.gateway.model;

/**
 * Created by TTLock on 2020/10/13.
 */
public class GatewayType {
    public static final int UNKNOWN = 0;
    /**
     * Wifi网关
     * 11 19
     */
    public static final int G2 = 2;

    /**
     * 有线网关
     * 11 21
     */
    public static final int G3 = 3;

    /**
     * 4G网关
     * 11 20
     */
    public static final int G4 = 4;

    public static int getGatewayType(byte[] data) {
        if (data[0] == 0x11 && data[1] == 0x19) {
            return G2;
        }
        if (data[0] == 0x11 && data[1] == 0x20) {
            return G4;
        }
        if (data[0] == 0x11 && data[1] == 0x21) {
            return G3;
        }
        return UNKNOWN;
    }
}
