package com.ttlock.bl.sdk.api

/**
 * Created by Smartlock on 2016/6/1.
 */
internal object CommandUtil_V2 {
    fun checkUserTime_V2(
        command: Command,
        sDateStr: String,
        eDateStr: String,
        unlockKey: String?,
        apiCommand: Int
    ) {
        val values = ByteArray(21)
        command.setCommand(Command.Companion.VERSION_LOCK_V1)
        System.arraycopy(sDateStr.toByteArray(), 0, values, 0, sDateStr.toByteArray().size)
        values[10] = 0x20
        System.arraycopy(eDateStr.toByteArray(), 0, values, 11, eDateStr.toByteArray().size)
        command.setData(values)
    }

    fun calibationTime_V2(command: Command, timeStr: String) {
        val timeArray = timeStr.toByteArray()
        command.setData(timeArray)
    }
}
