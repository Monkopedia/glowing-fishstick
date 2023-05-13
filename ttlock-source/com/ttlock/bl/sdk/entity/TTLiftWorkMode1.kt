package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2019/11/15.
 */
enum class TTLiftWorkMode(var value: Int) {
    /**
     * 刷本酒店的卡，本任何楼层都按(1	不控制楼层，刷本酒店的卡，本梯控控制的任何楼层都可以手动按键选择，下面2~9字节的配置无效）
     */
    ActivateAllFloors(0x01),

    /**
     * 对应楼层只允许卡所对应的楼层按（2	控制楼层，按照下面2~9字节的参数进行控制:0-控制，1-不控制）
     */
    ActivateSpecificFloors(0x02);

    fun getValue(): Int {
        return value
    }

    fun setValue(value: Int) {
        this.value = value
    }

    companion object {
        fun getInstance(value: Int): TTLiftWorkMode? {
            when (value) {
                0x01 -> return TTLiftWorkMode.ActivateAllFloors
                0x02 -> return TTLiftWorkMode.ActivateSpecificFloors
            }
            return null
        }
    }
}
