package com.ttlock.bl.sdk.entity;

/**
 * Created by TTLock on 2019/11/15.
 */
public enum TTLockConfigType {
    //锁开关指令中的开关项
    /**
     * 防撬警报
     * 开启后锁被破坏将会发出警报声
     */
    TAMPER_ALERT(0x01),
    /**
     * 重置键
     * 关闭后，长按重置键 将无法重置锁(即无法被重新初始化)
     */
    RESET_BUTTON(0x02),

    /**
     * 反锁
     * 需要接口跟锁上同时开启才会生效，有一个未开启都不生效
     * 锁上跟接口都开启后，只有管理员钥匙、管理员密码、可开反锁的卡能够开锁
     * 其余的电子钥匙、服务端生成的密码、自定义密码、添加的IC、指纹都无法开锁
     */
    PRIVACY_LOCK(0x04),
    /**
     * 开门方向
     * UnlockDirection.LEFT : true
     * UnlockDirection.RIGHT : false
     */
    UNLOCK_DIRECTION(0x10),
    PASSAGE_MODE_AUTO_UNLOCK_SETTING(0x20),

    //合并过来的开关项
    /**
     * 锁声音
     */
    LOCK_SOUND(0x05),
    /**
     * 国外客户保险箱锁是否显示密码
     */
    PASSCODE_VISIBLE(0x06),

    /**
     * 冻结锁
     */
    LOCK_FREEZE(0x07),

    /**
     * WiFi锁是否启用长连接工作模式
     * 1-启用长连接  false
     * 0-不启用长连接，(省电模式) true
     */
    WIFI_LOCK_POWER_SAVING_MODE(0x80);

    int item;
    private TTLockConfigType(int item) {
        this.item = item;
    }

    public static TTLockConfigType getInstance(int item) {
        switch (item) {
            case 0x01:
                return TAMPER_ALERT;
            case 0x02:
                return RESET_BUTTON;
            case 0x04:
                return PRIVACY_LOCK;
            case 0x05:
                return LOCK_SOUND;
            case 0x06:
                return PASSCODE_VISIBLE;
            case 0x07:
                return LOCK_FREEZE;
            case 0x10:
                return UNLOCK_DIRECTION;
            case 0x20:
                return PASSAGE_MODE_AUTO_UNLOCK_SETTING;
            case 0x80:
                return WIFI_LOCK_POWER_SAVING_MODE;
        }
        return null;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }}
