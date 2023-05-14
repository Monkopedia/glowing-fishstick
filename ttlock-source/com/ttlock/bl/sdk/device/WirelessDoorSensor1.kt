package com.ttlock.bl.sdk.device

import android.bluetooth.BluetoothDevice
import android.bluetooth.ScanResult

/**
 * Created by TTLock on 2019/5/16.
 */
class WirelessDoorSensor : TTDevice {
    private var scene: Byte = 0

    constructor(scanResult: ScanResult) : super(scanResult) {
        initial()
    }

    constructor(device: BluetoothDevice) : super(device) {
    }

    override fun initial() {
        val scanRecord = scanRecord!!
        val scanRecordLength = scanRecord.size
        var index = 0
        // TODO:越界
        while (index < scanRecordLength) {
            val len = scanRecord[index].toInt()
            if (len == 0) {
                break
            }
            val adtype = scanRecord[index + 1]
            when (adtype) {
                TTDevice.Companion.GAP_ADTYPE_LOCAL_NAME_COMPLETE -> {
                    val nameBytes = ByteArray(len - 1)
                    System.arraycopy(scanRecord, index + 2, nameBytes, 0, len - 1)
                    if (name == null || name!!.length == 0) {
                        setName(String(nameBytes))
                    }
                }
                TTDevice.Companion.GAP_ADTYPE_MANUFACTURER_SPECIFIC -> {
                    var offset = 2
                    val protocolType = scanRecord[index + offset++]
                    val protocolVersion = scanRecord[index + offset++]
                    if (protocolType.toInt() == 0x05 && protocolVersion.toInt() == 0x03) { // 三代协议
                        scene = scanRecord[index + offset++]
                    }
                    isSettingMode = true // 门磁扫描到就是可添加
                    //                    isSettingMode = (scanRecord[index + offset] & 0x04) == 0 ? false : true;

                    // 电量偏移量
                    offset++
                    batteryCapacity = scanRecord[index + offset].toInt()
                }
                TTDevice.Companion.GAP_ADTYPE_POWER_LEVEL -> {}
                else -> {}
            }
            index += len + 1
        }
    }

}
