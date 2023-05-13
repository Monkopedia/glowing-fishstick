package com.ttlock.bl.sdk.constant

/**
 * Created by TTLock on 2016/10/19 0019.
 */
object ICOperate {
    const val IC_SEARCH: Byte = 1
    const val FR_SEARCH: Byte = 6
    const val ADD: Byte = 2
    const val DELETE: Byte = 3
    const val CLEAR: Byte = 4
    const val MODIFY: Byte = 5

    /**
     * 指纹模板数据包
     */
    const val WRITE_FR: Byte = 7
    const val STATUS_ADD_SUCCESS: Byte = 0x01
    const val STATUS_ENTER_ADD_MODE: Byte = 0x02
}
