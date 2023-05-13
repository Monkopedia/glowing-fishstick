package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2019/11/15.
 */
enum class TTLockConfigType(var item: Int) {
    // 锁开关指令中的开关项
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
    UNLOCK_DIRECTION(0x10), PASSAGE_MODE_AUTO_UNLOCK_SETTING(0x20), // 合并过来的开关项

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

    fun getItem(): Int {
        return item
    }

    fun setItem(item: Int) {
        this.item = item
    }

    companion object {
        fun getInstance(item: Int): TTLockConfigType? {
            when (item) {
                0x01 -> return TTLockConfigType.TAMPER_ALERT
                0x02 -> return TTLockConfigType.RESET_BUTTON
                0x04 -> return TTLockConfigType.PRIVACY_LOCK
                0x05 -> return TTLockConfigType.LOCK_SOUND
                0x06 -> return TTLockConfigType.PASSCODE_VISIBLE
                0x07 -> return TTLockConfigType.LOCK_FREEZE
                0x10 -> return TTLockConfigType.UNLOCK_DIRECTION
                0x20 -> return TTLockConfigType.PASSAGE_MODE_AUTO_UNLOCK_SETTING
                0x80 -> return TTLockConfigType.WIFI_LOCK_POWER_SAVING_MODE
            }
            return null
        }
    }
}
