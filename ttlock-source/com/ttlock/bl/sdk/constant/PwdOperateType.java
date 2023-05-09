package com.ttlock.bl.sdk.constant;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class PwdOperateType {
    /**
     * 清空键盘密码
     */
    public static final byte PWD_OPERATE_TYPE_CLEAR = 1;

    /**
     * 添加键盘密码
     */
    public static final byte PWD_OPERATE_TYPE_ADD = 2;

    /**
     * 删除单个键盘密码
     */
    public static final byte PWD_OPERATE_TYPE_REMOVE_ONE = 3;

    /**
     * 修改键盘密码 (老的是4,不使用了)
     */
    public static final byte PWD_OPERATE_TYPE_MODIFY = 5;

    /**
     * 恢复密码
     */
    public static final byte PWD_OPERATE_TYPE_RECOVERY = 6;
}
