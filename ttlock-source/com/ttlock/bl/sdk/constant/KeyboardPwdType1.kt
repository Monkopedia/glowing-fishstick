package com.ttlock.bl.sdk.constant

/**
 * Created by Administrator on 2016/6/22 0022.
 */
object KeyboardPwdType {
    /**
     * 无限制
     */
    const val PWD_TYPE_PERMANENT: Byte = 1

    /**
     * 限次数
     */
    const val PWD_TYPE_COUNT: Byte = 2

    /**
     * 限时间
     */
    const val PWD_TYPE_PERIOD: Byte = 3

    /**
     * 循环
     */
    const val PWD_TYPE_CIRCLE: Byte = 4
}
