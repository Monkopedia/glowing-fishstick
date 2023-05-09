package com.ttlock.bl.sdk.entity;

/**
 * Created by TTLock on 2019/11/15.
 */
public enum TTLiftWorkMode {
    /**
     * 刷本酒店的卡，本任何楼层都按(1	不控制楼层，刷本酒店的卡，本梯控控制的任何楼层都可以手动按键选择，下面2~9字节的配置无效）
     */
    ActivateAllFloors(0x01),
    /**
     * 对应楼层只允许卡所对应的楼层按（2	控制楼层，按照下面2~9字节的参数进行控制:0-控制，1-不控制）
     */
    ActivateSpecificFloors(0x02);


    int value;
    private TTLiftWorkMode(int value) {
        this.value = value;
    }

    public static TTLiftWorkMode getInstance(int value) {
        switch (value) {
            case 0x01:
                return ActivateAllFloors;
            case 0x02:
                return ActivateSpecificFloors;
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
