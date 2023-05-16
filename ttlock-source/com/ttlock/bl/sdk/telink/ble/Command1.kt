/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.ttlock.bl.sdk.telink.ble

import com.ttlock.bl.sdk.telink.ble.Command.CommandType
import com.ttlock.bl.sdk.telink.ble.Command.CommandType.WRITE
import com.ttlock.bl.sdk.telink.util.Arrays
import java.util.*
import java.util.UUID
import kotlin.jvm.JvmOverloads

class Command @JvmOverloads constructor(
    var serviceUUID: UUID? = null,
    var characteristicUUID: UUID? = null,
    var type: CommandType = WRITE,
    var data: ByteArray? = null,
    var tag: Any? = null
) {
    var descriptorUUID: UUID? = null
    var delay = 0
    fun clear() {
        serviceUUID = null
        characteristicUUID = null
        descriptorUUID = null
        data = null
    }

    override fun toString(): String {
        var d = ""
        if (data != null) d = Arrays.bytesToHexString(data, ",")
        return (
            "{ tag : " + tag + ", type : " + type +
                " CHARACTERISTIC_UUID :" + characteristicUUID.toString() + " data: " + d + " delay :" + delay + "}"
            )
    }

    enum class CommandType {
        READ, READ_DESCRIPTOR, WRITE, WRITE_NO_RESPONSE, ENABLE_NOTIFY, DISABLE_NOTIFY
    }

    interface Callback {
        fun success(peripheral: Peripheral?, command: Command, obj: Any?)
        fun error(peripheral: Peripheral?, command: Command, errorMsg: String)
        fun timeout(peripheral: Peripheral?, command: Command): Boolean
    }

    companion object {
        fun newInstance(): Command {
            return Command()
        }
    }
}
