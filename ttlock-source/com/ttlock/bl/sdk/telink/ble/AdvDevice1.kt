package com.ttlock.bl.sdk.telink.ble

import android.bluetooth.BluetoothDevice

/**
 * 广播设备实体类
 * Created by Administrator on 2017/2/22.
 */
class AdvDevice : Parcelable {
    var device: BluetoothDevice
    var rssi: Int
    var scanRecord: ByteArray

    constructor(device: BluetoothDevice, rssi: Int, scanRecord: ByteArray) {
        this.device = device
        this.rssi = rssi
        this.scanRecord = scanRecord
    }

    constructor(`in`: Parcel) {
        device = `in`.readParcelable(javaClass.classLoader)
        rssi = `in`.readInt()
        scanRecord = ByteArray(`in`.readInt())
        `in`.readByteArray(scanRecord)
    }

    fun describeContents(): Int {
        return 0
    }

    fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(device, 0)
        dest.writeInt(rssi)
        dest.writeInt(scanRecord.size)
        dest.writeByteArray(scanRecord)
    }

    companion object {
        val CREATOR: Creator<AdvDevice> = object : Creator<AdvDevice?>() {
            fun createFromParcel(`in`: Parcel): AdvDevice {
                return AdvDevice(`in`)
            }

            fun newArray(size: Int): Array<AdvDevice> {
                return arrayOfNulls(size)
            }
        }
    }
}
