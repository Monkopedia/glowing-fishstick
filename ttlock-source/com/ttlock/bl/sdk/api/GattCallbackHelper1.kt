package com.ttlock.bl.sdk.api

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import com.ttlock.bl.sdk.callback.ConnectCallback
import java.lang.Exception
import java.util.*

import android.util.Context
import android.util.Handler
import android.util.Looper
import com.ttlock.bl.sdk.callback.InitKeypadCallback
import com.ttlock.bl.sdk.callback.LockCallback
import com.ttlock.bl.sdk.device.WirelessKeypad
import com.ttlock.bl.sdk.entity.LockError
import com.ttlock.bl.sdk.executor.AppExecutors
import com.ttlock.bl.sdk.util.DigitUtil
import com.ttlock.bl.sdk.util.LogUtil

/**
 * Created by TTLock on 2019/3/11.
 */
class GattCallbackHelper private constructor() : BluetoothGattCallback() {
    private val DBG = true
    private val modelNumberCharacteristic: BluetoothGattCharacteristic? = null
    private val hardwareRevisionCharacteristic: BluetoothGattCharacteristic? = null
    private val firmwareRevisionCharacteristic: BluetoothGattCharacteristic? = null
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private var mBluetoothGatt: BluetoothGatt? = null
    private var mWriteCharacteristic: BluetoothGattCharacteristic? = null
    private var device: WirelessKeypad? = null
    private var recDataTotalLen = 0
    private var hasRecDataLen = 0
    private val recDataBuf: ByteArray

    /**
     * 接收的最大长度
     */
    private val maxBufferCount = 256
    private var service: BluetoothGattService? = null

    /**
     * 传输数据
     */
    private var dataQueue: LinkedList<ByteArray>? = null
    private var context: Context? = null
    private val mAppExecutor: AppExecutors
    private val handler: Handler

    // TODO:
    private var isInitSuccess = false
    private val noResponseRunable = Runnable {
        val callback: LockCallback? = LockCallbackManager.Companion.getInstance().getCallback()
        if (callback != null) {
            callback.onFail(LockError.WIRELESS_KEYBOARD_NO_RESPONSE)
        }
        disconnect()
    }

    init {
        mAppExecutor = AppExecutors()
        recDataBuf = ByteArray(maxBufferCount)
        handler = Handler(Looper.getMainLooper())
    }

    fun prepare(context: Context?) {
        this.context = context
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    fun connect(extendedBluetoothDevice: WirelessKeypad) {
        device = extendedBluetoothDevice
        val address: String = device!!.getAddress()!!
        val bleDevice: BluetoothDevice = mBluetoothAdapter.getRemoteDevice(address)
        clear()
        mBluetoothGatt = bleDevice.connectGatt(context!!, false, this)
    }

    fun getDevice(): WirelessKeypad? {
        return device
    }

    fun setDevice(device: WirelessKeypad?) {
        this.device = device
    }

    fun getBluetoothGatt(): BluetoothGatt? {
        return mBluetoothGatt
    }

    fun setBluetoothGatt(mBluetoothGatt: BluetoothGatt?) {
        this.mBluetoothGatt = mBluetoothGatt
    }

    fun sendCommand(command: ByteArray) {
        var len = command.size
        LogUtil.d("send datas:" + DigitUtil.byteArrayToHexString(command), DBG)
        if (dataQueue == null) dataQueue = LinkedList<ByteArray>()
        dataQueue!!.clear()
        var startPos = 0
        while (len > 0) {
            val ln = Math.min(len, 20)
            val data = ByteArray(ln)
            System.arraycopy(command, startPos, data, 0, ln)
            dataQueue!!.add(data)
            len -= 20
            startPos += 20
        }
        if (mWriteCharacteristic != null && mBluetoothGatt != null) {
            try {
                startResponseTimer()
                hasRecDataLen = 0 // 发送前恢复接收数据的起始位置
                mWriteCharacteristic!!.setValue(dataQueue!!.poll())
                mBluetoothGatt!!.writeCharacteristic(mWriteCharacteristic!!)
            } catch (e: Exception) {
                // TODO:
            }
        } else {
            LogUtil.d("mBluetoothGatt:$mBluetoothGatt", DBG)
            LogUtil.d("mNotifyCharacteristic or mBluetoothGatt is null", DBG)
            // TODO:
        }
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        if (mBluetoothGatt !== gatt) return
        try {
            Thread.sleep(600)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            LogUtil.d("STATE_CONNECTED")
            LogUtil.d("Attempting to start service discovery:" + gatt.discoverServices())
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            LogUtil.d("STATE_DISCONNECTED")
            mAppExecutor.mainThread().execute(
                Runnable {
                    val mConnectCallback: ConnectCallback? =
                        LockCallbackManager.Companion.getInstance().getConnectCallback()
                    if (mConnectCallback != null) {
                        mConnectCallback.onFail(LockError.DEVICE_CONNECT_FAILED)
                    }
                    isInitSuccess = false
                }
            )
            clear()
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        super.onServicesDiscovered(gatt, status)
        LogUtil.d("")
        if (status == BluetoothGatt.GATT_SUCCESS) {
            val services: List<BluetoothGattService> = gatt.getServices()
            for (service in services) {
                LogUtil.d("service:" + service.getUuid())
            }
            service = gatt.getService(UUID.fromString(UUID_SERVICE))
            if (service != null) {
                val gattCharacteristics: List<BluetoothGattCharacteristic>? =
                    service!!.getCharacteristics()
                if (gattCharacteristics != null && gattCharacteristics.isNotEmpty()) {
                    for (gattCharacteristic in gattCharacteristics) {
                        LogUtil.d(gattCharacteristic.getUuid().toString(), DBG)
                        if (gattCharacteristic.getUuid().toString().equals(UUID_WRITE)) {
                            mWriteCharacteristic = gattCharacteristic
                        } else if (gattCharacteristic.getUuid().toString().equals(UUID_NODIFY)) {
                            gatt.setCharacteristicNotification(gattCharacteristic, true)
                            val descriptor: BluetoothGattDescriptor =
                                gattCharacteristic.getDescriptor(
                                    UUID_HEART_RATE_MEASUREMENT
                                )
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                            if (gatt.writeDescriptor(descriptor)) {
                                LogUtil.d("writeDescriptor successed", DBG)
                            } else {
                                // TODO:
                                LogUtil.d("writeDescriptor failed", DBG)
                            }
                        }
                    }
                }
            } else {
                // 测试出现的情况 是否再次发现一次
                LogUtil.w("service is null", true)
            }
        } else {
            LogUtil.w("onServicesDiscovered received: $status", DBG)
        }
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        super.onCharacteristicRead(gatt, characteristic, status)
        LogUtil.d("gatt=$gatt characteristic=$characteristic status=$status", DBG)
    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        if (mBluetoothGatt !== gatt) {
            LogUtil.w("gatt=$gatt characteristic=$characteristic status=$status", DBG)
            return
        }
        LogUtil.d("gatt=$gatt characteristic=$characteristic status=$status", DBG)
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (dataQueue!!.size > 0) {
                characteristic.setValue(dataQueue!!.poll())
                // TODO:写成功再写下一个
                gatt.writeCharacteristic(characteristic)
            } else {
            }
        } else {
            LogUtil.w("onCharacteristicWrite failed", DBG)
        }
        super.onCharacteristicWrite(gatt, characteristic, status)
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        super.onCharacteristicChanged(gatt, characteristic)
        LogUtil.d("")
        if (mBluetoothGatt !== gatt) return
        super.onCharacteristicChanged(gatt, characteristic)
        try {
            removeResponseTimer()
            LogUtil.d("gatt=$gatt characteristic=$characteristic", DBG)
            val data: ByteArray = characteristic.getValue()
            LogUtil.d("data:" + DigitUtil.byteArrayToHexString(data))
            val dataLen = data.size
            System.arraycopy(data, 0, recDataBuf, hasRecDataLen, dataLen)
            if (data[0].toInt() == 0x72 && data[1].toInt() == 0x5b) { // 数据开始
                recDataTotalLen = data[3] + 2 + 1 + 1 + 1
                LogUtil.d("recDataTotalLen:$recDataTotalLen")
            }
            hasRecDataLen += dataLen
            if (data[dataLen - 2].toInt() == 0x0d && data[dataLen - 1].toInt() == 0x0a) {
                hasRecDataLen -= 2
                doWithData(Arrays.copyOf(recDataBuf, hasRecDataLen))
                hasRecDataLen = 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // 清零
            hasRecDataLen = 0
        }
    }

    override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
        super.onDescriptorWrite(gatt, descriptor, status)
        if (mBluetoothGatt !== gatt) return
        LogUtil.d("")
        if (status == BluetoothGatt.GATT_SUCCESS) {
            mAppExecutor.mainThread().execute(
                Runnable {
                    val mConnectCallback: ConnectCallback? =
                        LockCallbackManager.Companion.getInstance().getConnectCallback()
                    if (mConnectCallback != null) {
                        mConnectCallback.onConnectSuccess(device!!)
                    }
                }
            )
        } else {
            // TODO:
        }
    }

    private fun doWithData(values: ByteArray) {
        mAppExecutor.mainThread().execute(
            Runnable {
                LogUtil.d("values:" + DigitUtil.byteArrayToHexString(values))
                val command = WirelessKeyboardCommand(values)
                command.setMac( device!!.getAddress())
                val data = command.getData()
                LogUtil.d("command:" + DigitUtil.byteToHex(command.getCommand()))
                LogUtil.d("data:" + DigitUtil.byteArrayToHexString(data))
                if (data == null) {
                    LogUtil.d("data is null")
                    return@Runnable
                }
                if (data[1].toInt() == 1) {
                    when (data[0]) {
                        WirelessKeyboardCommand.Companion.COMM_SET_LOCK -> CommandUtil.readDeviceFeature(
                            device!!
                        )
                        WirelessKeyboardCommand.Companion.COMM_READ_FEATURE -> {
                            val feature: Int =
                                DigitUtil.fourBytesToLong(Arrays.copyOfRange(data, 3, data.size))
                                    .toInt()
                            LogUtil.d("feature:$feature")
                            val callback: LockCallback? =
                                LockCallbackManager.Companion.getInstance().getCallback()
                            if (callback != null) {
                                (callback as InitKeypadCallback).onInitKeypadSuccess(feature)
                            }
                            // 成功之后主动断开连接
                            disconnect()
                        }
                        else -> {}
                    }
                } else {
                    val callback: LockCallback? =
                        LockCallbackManager.Companion.getInstance().getCallback()
                    if (callback != null) {
                        callback.onFail(LockError.INIT_WIRELESS_KEYBOARD_FAILED)
                    }
                }
            }
        )
    }

    private fun close() {
            mBluetoothGatt?.close()
            mBluetoothGatt = null
    }

    private fun disconnect() {
            mBluetoothGatt?.disconnect()
    }

    fun clear() {
        disconnect()
        close()
    }

    private fun startResponseTimer() {
        handler.postDelayed(noResponseRunable, 5000)
    }

    private fun removeResponseTimer() {
        handler.removeCallbacks(noResponseRunable)
    }

    companion object {
        val UUID_HEART_RATE_MEASUREMENT: UUID =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        var UUID_SERVICE = "00001810-0000-1000-8000-00805f9b34fb"
        var UUID_WRITE = "00000002-0000-1000-8000-00805f9b34fb"
        var UUID_NODIFY = "00000003-0000-1000-8000-00805f9b34fb"
        private const val DEVICE_INFORMATION_SERVICE = "0000180a-0000-1000-8000-00805f9b34fb"
        private const val READ_MODEL_NUMBER_UUID = "00002a24-0000-1000-8000-00805f9b34fb"
        private const val READ_FIRMWARE_REVISION_UUID = "00002a26-0000-1000-8000-00805f9b34fb"
        private const val READ_HARDWARE_REVISION_UUID = "00002a27-0000-1000-8000-00805f9b34fb"
        private const val READ_MANUFACTURER_NAME_UUID = "00002a29-0000-1000-8000-00805f9b34fb"
        private val instance = GattCallbackHelper()
        fun getInstance(): GattCallbackHelper {
            return instance
        }
    }
}
