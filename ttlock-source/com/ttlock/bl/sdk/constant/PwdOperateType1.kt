package com.ttlock.bl.sdk.constant

/**
 * Created by Administrator on 2016/6/23 0023.
 */
object PwdOperateType {
    /**
     * 清空键盘密码
     */
    const val PWD_OPERATE_TYPE_CLEAR: Byte = 1

    /**
     * 添加键盘密码
     */
    const val PWD_OPERATE_TYPE_ADD: Byte = 2

    /**
     * 删除单个键盘密码
     */
    const val PWD_OPERATE_TYPE_REMOVE_ONE: Byte = 3

    /**
     * 修改键盘密码 (老的是4,不使用了)
     */
    const val PWD_OPERATE_TYPE_MODIFY: Byte = 5

    /**
     * 恢复密码
     */
    const val PWD_OPERATE_TYPE_RECOVERY: Byte = 6
}
