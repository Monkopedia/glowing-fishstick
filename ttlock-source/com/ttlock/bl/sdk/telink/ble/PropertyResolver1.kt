package com.ttlock.bl.sdk.telink.ble

import android.bluetooth.BluetoothGattCharacteristic

/**
 * Created by Administrator on 2017/3/1.
 */
class PropertyResolver(private val mProp: Int) {
    private val properties: MutableMap<String, Boolean>

    init {
        properties = HashMap()
        properties[READ] = mProp and BluetoothGattCharacteristic.PROPERTY_READ !== 0
        properties[WRITE] = mProp and BluetoothGattCharacteristic.PROPERTY_WRITE !== 0
        properties[NOTIFY] = mProp and BluetoothGattCharacteristic.PROPERTY_NOTIFY !== 0
        properties[INDICATE] = mProp and BluetoothGattCharacteristic.PROPERTY_INDICATE !== 0
        properties[WRITE_NO_RESPONSE] =
            mProp and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE !== 0
    }

    operator fun contains(key: String): Boolean {
        return properties.containsKey(key) && properties[key]!!
    }

    fun getGattCharacteristicPropDesc(): String {
        var desc = " "
        if (mProp and BluetoothGattCharacteristic.PROPERTY_READ !== 0) desc = desc + "read "
        if (mProp and BluetoothGattCharacteristic.PROPERTY_WRITE !== 0) desc = desc + "write "
        if (mProp and BluetoothGattCharacteristic.PROPERTY_NOTIFY !== 0) desc = desc + "notify "
        if (mProp and BluetoothGattCharacteristic.PROPERTY_INDICATE !== 0) desc = desc + "indicate "
        if (mProp and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE !== 0) desc =
            desc + "write_no_response "
        return desc
    }

    companion object {
        const val READ = "read"
        const val WRITE = "write"
        const val NOTIFY = "notify"
        const val INDICATE = "indicate"
        const val WRITE_NO_RESPONSE = "write_no_response"
    }
}
