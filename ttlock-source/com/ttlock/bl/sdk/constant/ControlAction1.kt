package com.ttlock.bl.sdk.constant

/**
 * Created on  2019/4/3 0003 09:20
 *
 * @author theodore
 */
object ControlAction {
    const val UNLOCK = 3
    const val LOCK = 3 shl 1

    /**
     * 卷闸门
     */
    const val ROLLING_GATE_UP = 1
    const val ROLLING_GATE_DOWN = 1 shl 1
    const val ROLLING_GATE_PAUSE = 1 shl 2
    const val ROLLING_GATE_LOCK = 1 shl 3

    /**
     *
     */
    const val HOLD = 3 shl 3
}
