package com.ttlock.bl.sdk.device

import android.bluetooth.BluetoothDevice
import android.bluetooth.ScanResult

/**
 * Created on  2019/6/14 0014 15:20
 *
 * @author theodore_hu
 */
abstract class TTDevice {
    protected var device: BluetoothDevice? = null
    protected var scanRecord: ByteArray? = null
    protected var name: String? = null
    protected var number: String? = null

    /** mac 地址  */
    protected var mAddress: String? = null
    protected var rssi = 0
    protected var batteryCapacity = 0
    var isSettingMode = false

    /**
     * 搜索到设备的时间
     */
    private var date = System.currentTimeMillis()

    constructor() {}

    constructor(scanResult: ScanResult) {
        device = scanResult.getDevice()
        scanRecord = scanResult.getScanRecord().getBytes()
        rssi = scanResult.getRssi()
        name = device?.getName()
        number = name
        mAddress = device?.getAddress()
        date = System.currentTimeMillis()
        //        initial();
    }

    constructor(device: BluetoothDevice) {
        this.device = device
        name = device.getName()
        mAddress = device.getAddress()
    }

    @JvmName("getDevice1")
    fun getDevice(): BluetoothDevice? {
        return device
    }

    @JvmName("setDevice1")
    fun setDevice(device: BluetoothDevice) {
        this.device = device
        name = device.getName()
        mAddress = device.getAddress()
    }

    open fun setDate(date: Long) {
        this.date = date
    }

    open fun getDate(): Long {
        return date
    }

    protected abstract fun initial()

    @JvmName("getScanRecord1")
    fun getScanRecord(): ByteArray? {
        return scanRecord
    }

    @JvmName("setScanRecord1")
    fun setScanRecord(scanRecord: ByteArray) {
        this.scanRecord = scanRecord
    }

    @JvmName("getName1")
    fun getName(): String? {
        return name
    }

    @JvmName("setName1")
    fun setName(name: String?) {
        this.name = name
    }

    fun getAddress(): String {
        return mAddress ?: error("No address yet")
    }

    fun setAddress(mAddress: String?) {
        this.mAddress = mAddress
    }

    @JvmName("getRssi1")
    fun getRssi(): Int {
        return rssi
    }

    @JvmName("setRssi1")
    fun setRssi(rssi: Int) {
        this.rssi = rssi
    }

    @JvmName("getBatteryCapacity1")
    fun getBatteryCapacity(): Int {
        return batteryCapacity
    }

    @JvmName("setBatteryCapacity1")
    fun setBatteryCapacity(batteryCapacity: Int) {
        this.batteryCapacity = batteryCapacity
    }

    @JvmName("isSettingMode1")
    fun isSettingMode(): Boolean {
        return isSettingMode
    }

    @JvmName("setSettingMode1")
    fun setSettingMode(settingMode: Boolean) {
        isSettingMode = settingMode
    }

    @JvmName("getNumber1")
    fun getNumber(): String? {
        return number
    }

    @JvmName("setNumber1")
    fun setNumber(number: String?) {
        this.number = number
    }

    companion object {
        const val GAP_ADTYPE_LOCAL_NAME_COMPLETE: Byte = 0X09 // !< Complete local name
        const val GAP_ADTYPE_POWER_LEVEL: Byte = 0X0A // !< TX Power Level: 0xXX: -127 to +127 dBm
        const val GAP_ADTYPE_MANUFACTURER_SPECIFIC =
            0XFF.toByte() // !< Manufacturer Specific Data: first 2 octets contain the Company Inentifier Code followed by the additional manufacturer specific data
    }
}
