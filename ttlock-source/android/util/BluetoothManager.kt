package android.bluetooth

import android.util.Context
import java.util.*

class BluetoothManager {
    fun getAdapter(): BluetoothAdapter = BluetoothAdapter()
}

class BluetoothAdapter {
    fun isEnabled(): Boolean = true
    fun getRemoteDevice(addr: String): BluetoothDevice = BluetoothDevice()
    fun getBluetoothLeScanner(): BluetoothLeScanner {
        return BluetoothLeScanner()
    }

    companion object {
        fun getDefaultAdapter(): BluetoothAdapter = BluetoothAdapter()

        const val EXTRA_STATE = "extra"
        const val STATE_OFF = 0
        const val STATE_ON = 1
        const val STATE_TURNING_OFF = 2
        const val STATE_TURNING_ON = 3
        const val ACTION_STATE_CHANGED = "state_changed"
    }
}

class BluetoothDevice {
    fun connectGatt(context: Context, f: Boolean, gattCallback: BluetoothGattCallback): BluetoothGatt = BluetoothGatt()
    fun getName(): String = ""
    fun getAddress(): String = ""
}

class BluetoothGatt {
    fun discoverServices() {
    }
    fun disconnect() {}

    fun close() = Unit
    fun getService(uuid: UUID): BluetoothGattService = error("Not implemented")
    fun getServices(): List<BluetoothGattService> = emptyList()
    fun setCharacteristicNotification(
        characteristic: BluetoothGattCharacteristic,
        notify: Boolean
    ) {
    }

    fun writeDescriptor(descriptor: BluetoothGattDescriptor): Boolean = false
    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean = false
    fun readCharacteristic(modelNumberCharacteristic: BluetoothGattCharacteristic) {

    }

    companion object {
        const val GATT_SUCCESS = 0
    }
}

class BluetoothGattService {
    fun getUuid(): UUID = UUID.randomUUID()
    fun getCharacteristics(): List<BluetoothGattCharacteristic> = emptyList()
}

class BluetoothProfile {
    fun close() = Unit

    companion object {
        const val STATE_CONNECTED = 0
        const val STATE_DISCONNECTED = 1
    }
}

class BluetoothGattDescriptor {
    fun setValue(value: ByteArray) {}

    fun getUuid(): UUID = UUID.randomUUID()
    fun getCharacteristic(): BluetoothGattCharacteristic = error("")

    companion object {
        val ENABLE_NOTIFICATION_VALUE = byteArrayOf(1, 2, 3)
    }
}

abstract class BluetoothGattCallback {
    open fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int): Unit = Unit
    open fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) = Unit
    open fun onDescriptorWrite(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor,
        status: Int
    ) {
    }

    open fun onCharacteristicWrite(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) = Unit

    open fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic
    ) = Unit

    open fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) = Unit
}

class BluetoothGattCharacteristic {
    fun getUuid(): UUID = UUID.randomUUID()
    fun getValue(): ByteArray = byteArrayOf()
    fun getDescriptor(uuid: UUID): BluetoothGattDescriptor = error("")
    fun setValue(value: ByteArray) {}
}

class ScanResult {
    fun getDevice(): BluetoothDevice = BluetoothDevice()
    fun getScanRecord(): ScanRecord = ScanRecord()
    fun getRssi(): Int = 0
}

class ScanRecord {
    fun getBytes(): ByteArray = byteArrayOf()
}
open class ScanCallback {

    open fun onScanResult(callbackType: Int, result: ScanResult) {
    }
    open fun onScanFailed(errorCode: Int) {}

    companion object  {

        const val SCAN_FAILED_ALREADY_STARTED = -1
        const val SCAN_FAILED_APPLICATION_REGISTRATION_FAILED = -2
        const val SCAN_FAILED_INTERNAL_ERROR = -3
        const val SCAN_FAILED_FEATURE_UNSUPPORTED = -4
    }
}

class BluetoothLeScanner {
    fun startScan(uuidService: String, leScanCallback: ScanCallback?) {

    }

    fun stopScan(leScanCallback: ScanCallback) {

    }
}
