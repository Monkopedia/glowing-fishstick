package com.ttlock.bl.sdk.device

import android.bluetooth.ScanResult

/**
 * Created by TTLock on 2019/5/16.
 */
class WirelessKeypad : TTDevice {
    //
    //    private BluetoothDevice device;
    //
    //    private byte[] scanRecord;
    //
    //    private String name;
    //
    //    /** mac 地址 */
    //    private String mAddress;
    //
    //    private int rssi;
    private var scene: Byte = 0
    //    private int batteryCapacity;
    //    private boolean isSettingMode;
    /**
     * 搜索到设备的时间
     */
    private var date = System.currentTimeMillis()

    constructor(scanResult: ScanResult) {
        device = scanResult.getDevice()
        scanRecord = scanResult.getScanRecord().getBytes()
        rssi = scanResult.getRssi()
        name = device!!.getName()
        number = name
        mAddress = device!!.getAddress()
        this.date = System.currentTimeMillis()
        initial()
    }

    override fun initial() {
        val scanRecord = scanRecord!!
        val scanRecordLength = scanRecord.size
        var index = 0
        // TODO:越界
        while (index < scanRecordLength) {
            val len = scanRecord[index].toInt()
            if (len == 0) break
            val adtype = scanRecord[index + 1]
            when (adtype) {
                GAP_ADTYPE_LOCAL_NAME_COMPLETE -> {
                    val nameBytes = ByteArray(len - 1)
                    System.arraycopy(scanRecord, index + 2, nameBytes, 0, len - 1)
                    if (name == null || name!!.length == 0) {
                        setName(String(nameBytes))
                    }
                }
                GAP_ADTYPE_MANUFACTURER_SPECIFIC -> {
                    var offset = 2
                    var protocolType = scanRecord[index + offset++]
                    var protocolVersion = scanRecord[index + offset++]
                    if (protocolType.toInt() == 0x05 && protocolVersion.toInt() == 0x03) { // 三代锁
                        scene = scanRecord[index + offset++]
                    } else { // 其它锁
                        offset = 6 // 其它协议是从第6位开始
                        protocolType = scanRecord[index + offset++]
                        protocolVersion = scanRecord[index + offset]
                        offset = 9 // scene偏移量
                        scene = scanRecord[index + offset++]
                    }

                    // TODO:老款锁
                    isSettingMode = if (scanRecord[index + offset].toInt() and 0x04 == 0) false else true

                    // 电量偏移量
                    offset++
                    batteryCapacity = scanRecord[index + offset].toInt()
                }
                GAP_ADTYPE_POWER_LEVEL -> {}
                else -> {}
            }
            index += len + 1
        }
    }

    //    public BluetoothDevice getDevice() {
    //        return device;
    //    }
    //
    //    public void setDevice(BluetoothDevice device) {
    //        this.device = device;
    //    }
    //
    //    public byte[] getScanRecord() {
    //        return scanRecord;
    //    }
    //
    //    public void setScanRecord(byte[] scanRecord) {
    //        this.scanRecord = scanRecord;
    //    }
    //
    //    public String getName() {
    //        return name;
    //    }
    //
    //    public void setName(String name) {
    //        this.name = name;
    //    }
    //
    //    public String getAddress() {
    //        return mAddress;
    //    }
    //
    //    public void setAddress(String mAddress) {
    //        this.mAddress = mAddress;
    //    }
    //
    //    public int getRssi() {
    //        return rssi;
    //    }
    //
    //    public void setRssi(int rssi) {
    //        this.rssi = rssi;
    //    }
    override fun getDate(): Long {
        return date
    }

    override fun setDate(date: Long) {
        this.date = date
    }

    //    public int getBatteryCapacity() {
    //        return batteryCapacity;
    //    }
    //
    //    public void setBatteryCapacity(int batteryCapacity) {
    //        this.batteryCapacity = batteryCapacity;
    //    }
    //
    //    public boolean isSettingMode() {
    //        return isSettingMode;
    //    }
    //
    //    public void setSettingMode(boolean settingMode) {
    //        isSettingMode = settingMode;
    //    }
    fun describeContents(): Int {
        return 0
    }

    companion object {
        const val GAP_ADTYPE_LOCAL_NAME_COMPLETE: Byte = 0X09 // !< Complete local name
        const val GAP_ADTYPE_POWER_LEVEL: Byte = 0X0A // !< TX Power Level: 0xXX: -127 to +127 dBm
        const val GAP_ADTYPE_MANUFACTURER_SPECIFIC =
            0XFF.toByte() // !< Manufacturer Specific Data: first 2 octets contain the Company Inentifier Code followed by the additional manufacturer specific data
    }
}
