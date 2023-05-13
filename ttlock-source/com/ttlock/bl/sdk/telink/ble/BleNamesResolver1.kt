package com.ttlock.bl.sdk.telink.ble

import android.util.SparseArray

/**
 * Created by Administrator on 2017/2/23.
 */
object BleNamesResolver {
    private var mAppearance: SparseArray<String>? = null
    private var mCharacteristics: HashMap<String, String>? = null
    private var mHeartRateSensorLocation: SparseArray<String>? = null
    private val mServices: HashMap<String?, String?> = HashMap<Any?, Any?>()
    private var mValueFormats: SparseArray<String>? = null
    const val DEFAULT_CHARACTERISTIC_NAME = "Unknown Characteristic"

    init {
        mCharacteristics = HashMap<Any?, Any?>()
        mValueFormats = SparseArray(1)
        mAppearance = SparseArray(1)
        mHeartRateSensorLocation = SparseArray(1)
        mServices["00001811-0000-1000-8000-00805f9b34fb"] = "Alert Notification Service"
        mServices["0000180f-0000-1000-8000-00805f9b34fb"] = "Battery Service"
        mServices["00001810-0000-1000-8000-00805f9b34fb"] = "Blood Pressure"
        mServices["00001805-0000-1000-8000-00805f9b34fb"] = "Current Time Service"
        mServices["00001818-0000-1000-8000-00805f9b34fb"] = "Cycling Power"
        mServices["00001816-0000-1000-8000-00805f9b34fb"] = "Cycling Speed and Cadence"
        mServices["0000180a-0000-1000-8000-00805f9b34fb"] = "Device Information"
        mServices["00001800-0000-1000-8000-00805f9b34fb"] = "Generic Access"
        mServices["00001801-0000-1000-8000-00805f9b34fb"] = "Generic Attribute"
        mServices["00001808-0000-1000-8000-00805f9b34fb"] = "Glucose"
        mServices["00001809-0000-1000-8000-00805f9b34fb"] = "Health Thermometer"
        mServices["0000180d-0000-1000-8000-00805f9b34fb"] = "Heart Rate"
        mServices["00001812-0000-1000-8000-00805f9b34fb"] = "Human Interface Device"
        mServices["00001802-0000-1000-8000-00805f9b34fb"] = "Immediate Alert"
        mServices["00001803-0000-1000-8000-00805f9b34fb"] = "Link Loss"
        mServices["00001819-0000-1000-8000-00805f9b34fb"] = "Location and Navigation"
        mServices["00001807-0000-1000-8000-00805f9b34fb"] = "Next DST Change Service"
        mServices["0000180e-0000-1000-8000-00805f9b34fb"] = "Phone Alert Status Service"
        mServices["00001806-0000-1000-8000-00805f9b34fb"] = "Reference Time Update Service"
        mServices["00001814-0000-1000-8000-00805f9b34fb"] = "Running Speed and Cadence"
        mServices["00001813-0000-1000-8000-00805f9b34fb"] = "Scan Parameters"
        mServices["00001804-0000-1000-8000-00805f9b34fb"] = "Tx Power"
        mCharacteristics["00002a43-0000-1000-8000-00805f9b34fb"] = "Alert Category ID"
        mCharacteristics["00002a42-0000-1000-8000-00805f9b34fb"] = "Alert Category ID Bit Mask"
        mCharacteristics["00002a06-0000-1000-8000-00805f9b34fb"] = "Alert Level"
        mCharacteristics["00002a44-0000-1000-8000-00805f9b34fb"] =
            "Alert Notification Control Point"
        mCharacteristics["00002a3f-0000-1000-8000-00805f9b34fb"] = "Alert Status"
        mCharacteristics["00002a01-0000-1000-8000-00805f9b34fb"] = "Appearance"
        mCharacteristics["00002a19-0000-1000-8000-00805f9b34fb"] = "Battery Level"
        mCharacteristics["00002a49-0000-1000-8000-00805f9b34fb"] = "Blood Pressure Feature"
        mCharacteristics["00002a35-0000-1000-8000-00805f9b34fb"] = "Blood Pressure Measurement"
        mCharacteristics["00002a38-0000-1000-8000-00805f9b34fb"] = "Body Sensor Location"
        mCharacteristics["00002a22-0000-1000-8000-00805f9b34fb"] = "Boot Keyboard Input Report"
        mCharacteristics["00002a32-0000-1000-8000-00805f9b34fb"] = "Boot Keyboard Output Report"
        mCharacteristics["00002a33-0000-1000-8000-00805f9b34fb"] = "Boot Mouse Input Report"
        mCharacteristics["00002a5c-0000-1000-8000-00805f9b34fb"] = "CSC Feature"
        mCharacteristics["00002a5b-0000-1000-8000-00805f9b34fb"] = "CSC Measurement"
        mCharacteristics["00002a2b-0000-1000-8000-00805f9b34fb"] = "Current Time"
        mCharacteristics["00002a66-0000-1000-8000-00805f9b34fb"] = "Cycling Power Control Point"
        mCharacteristics["00002a65-0000-1000-8000-00805f9b34fb"] = "Cycling Power Feature"
        mCharacteristics["00002a63-0000-1000-8000-00805f9b34fb"] = "Cycling Power Measurement"
        mCharacteristics["00002a64-0000-1000-8000-00805f9b34fb"] = "Cycling Power Vector"
        mCharacteristics["00002a08-0000-1000-8000-00805f9b34fb"] = "Date Time"
        mCharacteristics["00002a0a-0000-1000-8000-00805f9b34fb"] = "Day Date Time"
        mCharacteristics["00002a09-0000-1000-8000-00805f9b34fb"] = "Day of Week"
        mCharacteristics["00002a00-0000-1000-8000-00805f9b34fb"] = "Device Name"
        mCharacteristics["00002a0d-0000-1000-8000-00805f9b34fb"] = "DST Offset"
        mCharacteristics["00002a0c-0000-1000-8000-00805f9b34fb"] = "Exact Time 256"
        mCharacteristics["00002a26-0000-1000-8000-00805f9b34fb"] = "Firmware Revision String"
        mCharacteristics["00002a51-0000-1000-8000-00805f9b34fb"] = "Glucose Feature"
        mCharacteristics["00002a18-0000-1000-8000-00805f9b34fb"] = "Glucose Measurement"
        mCharacteristics["00002a34-0000-1000-8000-00805f9b34fb"] = "Glucose Measurement Context"
        mCharacteristics["00002a27-0000-1000-8000-00805f9b34fb"] = "Hardware Revision String"
        mCharacteristics["00002a39-0000-1000-8000-00805f9b34fb"] = "Heart Rate Control Point"
        mCharacteristics["00002a37-0000-1000-8000-00805f9b34fb"] = "Heart Rate Measurement"
        mCharacteristics["00002a4c-0000-1000-8000-00805f9b34fb"] = "HID Control Point"
        mCharacteristics["00002a4a-0000-1000-8000-00805f9b34fb"] = "HID Information"
        mCharacteristics["00002a2a-0000-1000-8000-00805f9b34fb"] =
            "IEEE 11073-20601 Regulatory Certification Data List"
        mCharacteristics["00002a36-0000-1000-8000-00805f9b34fb"] = "Intermediate Cuff Pressure"
        mCharacteristics["00002a1e-0000-1000-8000-00805f9b34fb"] = "Intermediate Temperature"
        mCharacteristics["00002a6b-0000-1000-8000-00805f9b34fb"] = "LN Control Point"
        mCharacteristics["00002a6a-0000-1000-8000-00805f9b34fb"] = "LN Feature"
        mCharacteristics["00002a0f-0000-1000-8000-00805f9b34fb"] = "Local Time Information"
        mCharacteristics["00002a67-0000-1000-8000-00805f9b34fb"] = "Location and Speed"
        mCharacteristics["00002a29-0000-1000-8000-00805f9b34fb"] = "Manufacturer Name String"
        mCharacteristics["00002a21-0000-1000-8000-00805f9b34fb"] = "Measurement Interval"
        mCharacteristics["00002a24-0000-1000-8000-00805f9b34fb"] = "Model Number String"
        mCharacteristics["00002a68-0000-1000-8000-00805f9b34fb"] = "Navigation"
        mCharacteristics["00002a46-0000-1000-8000-00805f9b34fb"] = "New Alert"
        mCharacteristics["00002a04-0000-1000-8000-00805f9b34fb"] =
            "Peripheral Preferred Connection Parameters"
        mCharacteristics["00002a02-0000-1000-8000-00805f9b34fb"] = "Peripheral Privacy Flag"
        mCharacteristics["00002a50-0000-1000-8000-00805f9b34fb"] = "PnP ID"
        mCharacteristics["00002a69-0000-1000-8000-00805f9b34fb"] = "Position Quality"
        mCharacteristics["00002a4e-0000-1000-8000-00805f9b34fb"] = "Protocol Mode"
        mCharacteristics["00002a03-0000-1000-8000-00805f9b34fb"] = "Reconnection Address"
        mCharacteristics["00002a52-0000-1000-8000-00805f9b34fb"] = "Record Access Control Point"
        mCharacteristics["00002a14-0000-1000-8000-00805f9b34fb"] = "Reference Time Information"
        mCharacteristics["00002a4d-0000-1000-8000-00805f9b34fb"] = "Report"
        mCharacteristics["00002a4b-0000-1000-8000-00805f9b34fb"] = "Report Map"
        mCharacteristics["00002a40-0000-1000-8000-00805f9b34fb"] = "Ringer Control Point"
        mCharacteristics["00002a41-0000-1000-8000-00805f9b34fb"] = "Ringer Setting"
        mCharacteristics["00002a54-0000-1000-8000-00805f9b34fb"] = "RSC Feature"
        mCharacteristics["00002a53-0000-1000-8000-00805f9b34fb"] = "RSC Measurement"
        mCharacteristics["00002a55-0000-1000-8000-00805f9b34fb"] = "SC Control Point"
        mCharacteristics["00002a4f-0000-1000-8000-00805f9b34fb"] = "Scan Interval Window"
        mCharacteristics["00002a31-0000-1000-8000-00805f9b34fb"] = "Scan Refresh"
        mCharacteristics["00002a5d-0000-1000-8000-00805f9b34fb"] = "Sensor Location"
        mCharacteristics["00002a25-0000-1000-8000-00805f9b34fb"] = "Serial Number String"
        mCharacteristics["00002a05-0000-1000-8000-00805f9b34fb"] = "Service Changed"
        mCharacteristics["00002a28-0000-1000-8000-00805f9b34fb"] = "Software Revision String"
        mCharacteristics["00002a47-0000-1000-8000-00805f9b34fb"] = "Supported New Alert Category"
        mCharacteristics["00002a48-0000-1000-8000-00805f9b34fb"] = "Supported Unread Alert Category"
        mCharacteristics["00002a23-0000-1000-8000-00805f9b34fb"] = "System ID"
        mCharacteristics["00002a1c-0000-1000-8000-00805f9b34fb"] = "Temperature Measurement"
        mCharacteristics["00002a1d-0000-1000-8000-00805f9b34fb"] = "Temperature Type"
        mCharacteristics["00002a12-0000-1000-8000-00805f9b34fb"] = "Time Accuracy"
        mCharacteristics["00002a13-0000-1000-8000-00805f9b34fb"] = "Time Source"
        mCharacteristics["00002a16-0000-1000-8000-00805f9b34fb"] = "Time Update Control Point"
        mCharacteristics["00002a17-0000-1000-8000-00805f9b34fb"] = "Time Update State"
        mCharacteristics["00002a11-0000-1000-8000-00805f9b34fb"] = "Time with DST"
        mCharacteristics["00002a0e-0000-1000-8000-00805f9b34fb"] = "Time Zone"
        mCharacteristics["00002a07-0000-1000-8000-00805f9b34fb"] = "Tx Power Level"
        mCharacteristics["00002a45-0000-1000-8000-00805f9b34fb"] = "Unread Alert Status"
        mValueFormats.put(Integer.valueOf(52).toInt(), "32bit float")
        mValueFormats.put(Integer.valueOf(50).toInt(), "16bit float")
        mValueFormats.put(Integer.valueOf(34).toInt(), "16bit signed int")
        mValueFormats.put(Integer.valueOf(36).toInt(), "32bit signed int")
        mValueFormats.put(Integer.valueOf(33).toInt(), "8bit signed int")
        mValueFormats.put(Integer.valueOf(18).toInt(), "16bit unsigned int")
        mValueFormats.put(Integer.valueOf(20).toInt(), "32bit unsigned int")
        mValueFormats.put(Integer.valueOf(17).toInt(), "8bit unsigned int")
        mAppearance.put(Integer.valueOf(833).toInt(), "Heart Rate Sensor: Belt")
        mAppearance.put(Integer.valueOf(832).toInt(), "Generic Heart Rate Sensor")
        mAppearance.put(Integer.valueOf(0).toInt(), "Unknown")
        mAppearance.put(Integer.valueOf(64).toInt(), "Generic Phone")
        mAppearance.put(Integer.valueOf(1157).toInt(), "Cycling: Speed and Cadence Sensor")
        mAppearance.put(Integer.valueOf(1152).toInt(), "General Cycling")
        mAppearance.put(Integer.valueOf(1153).toInt(), "Cycling Computer")
        mAppearance.put(Integer.valueOf(1154).toInt(), "Cycling: Speed Sensor")
        mAppearance.put(Integer.valueOf(1155).toInt(), "Cycling: Cadence Sensor")
        mAppearance.put(Integer.valueOf(1156).toInt(), "Cycling: Speed and Cadence Sensor")
        mAppearance.put(Integer.valueOf(1157).toInt(), "Cycling: Power Sensor")
        mHeartRateSensorLocation.put(Integer.valueOf(0).toInt(), "Other")
        mHeartRateSensorLocation.put(Integer.valueOf(1).toInt(), "Chest")
        mHeartRateSensorLocation.put(Integer.valueOf(2).toInt(), "Wrist")
        mHeartRateSensorLocation.put(Integer.valueOf(3).toInt(), "Finger")
        mHeartRateSensorLocation.put(Integer.valueOf(4).toInt(), "Hand")
        mHeartRateSensorLocation.put(Integer.valueOf(5).toInt(), "Ear Lobe")
        mHeartRateSensorLocation.put(Integer.valueOf(6).toInt(), "Foot")
    }

    fun isCharacteristic(paramString: String?): Boolean {
        return mCharacteristics!!.containsKey(paramString)
    }

    fun isService(paramString: String?): Boolean {
        return mServices.containsKey(paramString)
    }

    fun resolveAppearance(paramInt: Int): String {
        val localInteger = Integer.valueOf(paramInt)
        return mAppearance.get(localInteger.toInt(), "Unknown Appearance")
    }

    fun resolveCharacteristicName(paramString: String?): String? {
        var str = mCharacteristics!![paramString]
        if (str == null) str = DEFAULT_CHARACTERISTIC_NAME
        return str
    }

    fun resolveHeartRateSensorLocation(paramInt: Int): String {
        val localInteger = Integer.valueOf(paramInt)
        return mHeartRateSensorLocation.get(localInteger.toInt(), "Other")
    }

    fun resolveServiceName(paramString: String?): String {
        var str = mServices[paramString]
        if (str == null) str = "Unknown Service"
        return str
    }

    fun resolveUuid(paramString: String?): String {
        val str1 = mServices[paramString]
        if (str1 != null) return "Service: $str1"
        val str2 = mCharacteristics!![paramString]
        return if (str2 != null) "Characteristic: $str2" else "Unknown UUID"
    }

    fun resolveValueTypeDescription(paramInt: Int): String {
        val localInteger = Integer.valueOf(paramInt)
        return mValueFormats.get(localInteger.toInt(), "Unknown Format")
    }
}
