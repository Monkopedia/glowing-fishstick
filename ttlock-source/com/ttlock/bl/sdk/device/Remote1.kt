package com.ttlock.bl.sdk.device

import android.Manifest

/**
 * Created by TTLock on 2019/5/16.
 */
class Remote : TTDevice {
    private var scene: Byte = 0

    /**
     * 搜索到设备的时间
     */
    private var date = System.currentTimeMillis()

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH])
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(scanResult: ScanResult) {
        device = scanResult.getDevice()
        scanRecord = scanResult.getScanRecord().getBytes()
        rssi = scanResult.getRssi()
        name = device.getName()
        number = name
        mAddress = device.getAddress()
        this.date = System.currentTimeMillis()
        initial()
    }

    constructor(device: BluetoothDevice?) : super(device) {}

    override fun initial() {
        val scanRecordLength = scanRecord.size
        var index = 0
        //TODO:越界
        while (index < scanRecordLength) {
            val len = scanRecord[index].toInt()
            if (len == 0) break
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
                    if (protocolType.toInt() == 0x05 && protocolVersion.toInt() == 0x03) { //三代协议
                        scene = scanRecord[index + offset++]
                    }
                    isSettingMode = if (scanRecord[index + offset] and 0x04 == 0) false else true

                    //电量偏移量
                    offset++
                    batteryCapacity = scanRecord[index + offset].toInt()
                }
                TTDevice.Companion.GAP_ADTYPE_POWER_LEVEL -> {}
                else -> {}
            }
            index += len + 1
        }
    }

    override fun getDate(): Long {
        return date
    }

    override fun setDate(date: Long) {
        this.date = date
    }

    fun describeContents(): Int {
        return 0
    }

    fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(scene)
        dest.writeLong(this.date)
        dest.writeParcelable(device, flags)
        dest.writeByteArray(scanRecord)
        dest.writeString(name)
        dest.writeString(mAddress)
        dest.writeInt(rssi)
        dest.writeInt(batteryCapacity)
        dest.writeByte(if (isSettingMode) 1.toByte() else 0.toByte())
    }

    constructor(`in`: Parcel) {
        scene = `in`.readByte()
        this.date = `in`.readLong()
        device = `in`.readParcelable(BluetoothDevice::class.java.getClassLoader())
        scanRecord = `in`.createByteArray()
        name = `in`.readString()
        mAddress = `in`.readString()
        rssi = `in`.readInt()
        batteryCapacity = `in`.readInt()
        isSettingMode = `in`.readByte() !== 0
    }

    companion object {
        val CREATOR: Creator<Remote> = object : Creator<Remote?>() {
            fun createFromParcel(source: Parcel?): Remote {
                return Remote(source)
            }

            fun newArray(size: Int): Array<Remote> {
                return arrayOfNulls(size)
            }
        }
    }
}