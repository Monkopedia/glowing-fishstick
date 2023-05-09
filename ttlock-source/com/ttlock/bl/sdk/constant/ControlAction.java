package com.ttlock.bl.sdk.constant;

/**
 * Created on  2019/4/3 0003 09:20
 *
 * @author theodore
 */
public class ControlAction {
    public final static int UNLOCK = 3;
    public final static int LOCK = 3 << 1;
    /**
     * 卷闸门
     */
    public final static int ROLLING_GATE_UP = 1;
    public final static int ROLLING_GATE_DOWN = 1 << 1;
    public final static int ROLLING_GATE_PAUSE = 1 << 2;
    public final static int ROLLING_GATE_LOCK = 1 << 3;
    /**
     *
     */
    public final static int HOLD = 3 << 3;
}
