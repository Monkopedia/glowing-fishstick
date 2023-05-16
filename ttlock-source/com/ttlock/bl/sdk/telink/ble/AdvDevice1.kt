package com.ttlock.bl.sdk.telink.ble

import android.bluetooth.BluetoothDevice

/**
 * 广播设备实体类
 * Created by Administrator on 2017/2/22.
 */
class AdvDevice {
    var device: BluetoothDevice
    var rssi: Int
    var scanRecord: ByteArray

    constructor(device: BluetoothDevice, rssi: Int, scanRecord: ByteArray) {
        this.device = device
        this.rssi = rssi
        this.scanRecord = scanRecord
    }
}
