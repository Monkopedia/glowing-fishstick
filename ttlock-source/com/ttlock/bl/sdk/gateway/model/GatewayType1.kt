package com.ttlock.bl.sdk.gateway.model

/**
 * Created by TTLock on 2020/10/13.
 */
object GatewayType {
    const val UNKNOWN = 0

    /**
     * Wifi网关
     * 11 19
     */
    const val G2 = 2

    /**
     * 有线网关
     * 11 21
     */
    const val G3 = 3

    /**
     * 4G网关
     * 11 20
     */
    const val G4 = 4
    fun getGatewayType(data: ByteArray): Int {
        if (data[0].toInt() == 0x11 && data[1].toInt() == 0x19) {
            return GatewayType.G2
        }
        if (data[0].toInt() == 0x11 && data[1].toInt() == 0x20) {
            return GatewayType.G4
        }
        return if (data[0].toInt() == 0x11 && data[1].toInt() == 0x21) {
            GatewayType.G3
        } else GatewayType.UNKNOWN
    }
}
