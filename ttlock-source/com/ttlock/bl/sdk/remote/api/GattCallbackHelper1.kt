package com.ttlock.bl.sdk.remote.api

import android.bluetooth.BluetoothAdapter
import com.ttlock.bl.sdk.device.Remote
import com.ttlock.bl.sdk.remote.callback.ConnectCallback
import com.ttlock.bl.sdk.remote.command.Command
import com.ttlock.bl.sdk.remote.model.OperationType
import java.lang.Exception
import java.util.*

/**
 * Created by TTLock on 2019/3/11.
 */
class GattCallbackHelper private constructor() : BluetoothGattCallback() {
    private val DBG = true
    private var modelNumberCharacteristic: BluetoothGattCharacteristic? = null
    private var hardwareRevisionCharacteristic: BluetoothGattCharacteristic? = null
    private var firmwareRevisionCharacteristic: BluetoothGattCharacteristic? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothGatt: BluetoothGatt? = null
    private var mWriteCharacteristic: BluetoothGattCharacteristic? = null
    private var device: Remote? = null
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
    private val handler: android.os.Handler
    private var deviceInfo: SystemInfo? = null

    //TODO:
    private var isInitSuccess = false
    private var mAddress: String? = null
    private val noResponseRunable = Runnable {
        val callback: RemoteCallback = RemoteCallbackManager.Companion.getInstance().getCallback()
        if (callback != null) callback.onFail(RemoteError.NO_RESPONSE)
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

    //    public void connect(String mac) {
    //        final BluetoothDevice bleDevice = mBluetoothAdapter.getRemoteDevice(mac);
    //        connect(new WirelessKeypad(bleDevice));
    //    }
    fun connect(extendedBluetoothDevice: Remote?) {
        device = extendedBluetoothDevice
        mAddress = device!!.address
        val bleDevice: BluetoothDevice = mBluetoothAdapter.getRemoteDevice(mAddress)
        clear()
        mConnectionState = STATE_CONNECTING
        mBluetoothGatt = bleDevice.connectGatt(context, false, this)
    }

    fun getDevice(): Remote? {
        return device
    }

    fun setDevice(device: Remote?) {
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
        dataQueue.clear()
        var startPos = 0
        while (len > 0) {
            val ln = Math.min(len, 20)
            val data = ByteArray(ln)
            System.arraycopy(command, startPos, data, 0, ln)
            dataQueue.add(data)
            len -= 20
            startPos += 20
        }
        if (mWriteCharacteristic != null && mBluetoothGatt != null) {
            try {
                startResponseTimer()
                hasRecDataLen = 0 //发送前恢复接收数据的起始位置
                mWriteCharacteristic.setValue(dataQueue.poll())
                mBluetoothGatt.writeCharacteristic(mWriteCharacteristic)
            } catch (e: Exception) {
                //TODO:
            }
        } else {
            LogUtil.d("mBluetoothGatt:$mBluetoothGatt", DBG)
            LogUtil.d("mNotifyCharacteristic or mBluetoothGatt is null", DBG)
            //TODO:
        }
    }

    fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
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
            mConnectionState = STATE_DISCONNECTED
            mAppExecutor.mainThread().execute(Runnable {
                val mConnectCallback: ConnectCallback =
                    RemoteCallbackManager.Companion.getInstance().getConnectCallback()
                if (mConnectCallback != null) {
                    Log.d("OMG", "====disconnect==1==$isInitSuccess")
                    mConnectCallback.onFail(RemoteError.CONNECT_FAIL)
                }
                isInitSuccess = false
            })
            clear()
        }
    }

    fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        super.onServicesDiscovered(gatt, status)
        LogUtil.d("")
        if (status == BluetoothGatt.GATT_SUCCESS) {
            val service: BluetoothGattService = mBluetoothGatt.getService(
                UUID.fromString(
                    DEVICE_INFORMATION_SERVICE
                )
            )
            if (service != null) {
                val gattCharacteristics: List<BluetoothGattCharacteristic> =
                    service.getCharacteristics()
                if (gattCharacteristics != null && gattCharacteristics.size > 0) {
                    for (gattCharacteristic in gattCharacteristics) {
                        LogUtil.d(gattCharacteristic.getUuid().toString(), DBG)
                        LogUtil.d("read characteristic:" + Thread.currentThread(), DBG)
                        if (gattCharacteristic.getUuid().toString()
                                .equals(READ_MODEL_NUMBER_UUID)
                        ) {
                            modelNumberCharacteristic = gattCharacteristic
                            //                                gatt.readCharacteristic(gattCharacteristic);
                        } else if (gattCharacteristic.getUuid().toString().equals(
                                READ_FIRMWARE_REVISION_UUID
                            )
                        ) {
                            firmwareRevisionCharacteristic = gattCharacteristic
                        } else if (gattCharacteristic.getUuid().toString().equals(
                                READ_HARDWARE_REVISION_UUID
                            )
                        ) {
                            hardwareRevisionCharacteristic = gattCharacteristic
                        }
                    }
                }
            }
            gatt.readCharacteristic(modelNumberCharacteristic)

//            List<BluetoothGattService> services = gatt.getServices();
//            for (BluetoothGattService service : services) {
//                LogUtil.d("service:" + service.getUuid());
//            }
//
//            service = gatt.getService(UUID.fromString(UUID_SERVICE));
//            if (service != null) {
//                List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();
//                if (gattCharacteristics != null && gattCharacteristics.size() > 0) {
//                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
//                        LogUtil.d(gattCharacteristic.getUuid().toString(), DBG);
//                        if (gattCharacteristic.getUuid().toString().equals(UUID_WRITE)) {
//                            mWriteCharacteristic = gattCharacteristic;
//                        } else if (gattCharacteristic.getUuid().toString().equals(UUID_NODIFY)) {
//                            gatt.setCharacteristicNotification(gattCharacteristic, true);
//                            BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID_HEART_RATE_MEASUREMENT);
//                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                            if (gatt.writeDescriptor(descriptor)) {
//                                LogUtil.d("writeDescriptor successed", DBG);
//                            } else {
//                                //TODO:
//                                LogUtil.d("writeDescriptor failed", DBG);
//                            }
//                        }
//                    }
//                }
//            } else {
//                //测试出现的情况 是否再次发现一次
//                LogUtil.w("service is null", true);
//            }
        } else {
            LogUtil.w("onServicesDiscovered received: $status", DBG)
        }
    }

    fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        super.onCharacteristicRead(gatt, characteristic, status)
        LogUtil.d("gatt=$gatt characteristic=$characteristic status=$status", DBG)
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (characteristic === modelNumberCharacteristic) {
                if (deviceInfo == null) deviceInfo = SystemInfo()
                deviceInfo.modelNum = String(characteristic.getValue())
                gatt.readCharacteristic(hardwareRevisionCharacteristic)
            } else if (characteristic === hardwareRevisionCharacteristic) {
                deviceInfo.hardwareRevision = String(characteristic.getValue())
                gatt.readCharacteristic(firmwareRevisionCharacteristic)
            } else if (characteristic === firmwareRevisionCharacteristic) {
                deviceInfo.firmwareRevision = String(characteristic.getValue())
                LogUtil.d("deviceInfo:$deviceInfo")
                val callbackType: Int =
                    RemoteCallbackManager.Companion.getInstance().getOperationType()
                if (callbackType == OperationType.GET_DEVICE_INFO) { //获取设备信息成功后 断开蓝牙
                    val callback: RemoteCallback =
                        RemoteCallbackManager.Companion.getInstance().getCallback()
                    if (callback != null) {
                        (callback as GetRemoteSystemInfoCallback).onGetRemoteSystemInfoSuccess(
                            deviceInfo
                        )
                    }
                    disconnect()
                    return
                }
                service = gatt.getService(UUID.fromString(UUID_SERVICE))
                if (service != null) {
                    val gattCharacteristics: List<BluetoothGattCharacteristic> =
                        service.getCharacteristics()
                    if (gattCharacteristics != null && gattCharacteristics.size > 0) {
                        for (gattCharacteristic in gattCharacteristics) {
                            LogUtil.d(gattCharacteristic.getUuid().toString(), DBG)
                            if (gattCharacteristic.getUuid().toString().equals(UUID_WRITE)) {
                                mWriteCharacteristic = gattCharacteristic
                            } else if (gattCharacteristic.getUuid().toString()
                                    .equals(UUID_NODIFY)
                            ) {
                                gatt.setCharacteristicNotification(gattCharacteristic, true)
                                val descriptor: BluetoothGattDescriptor =
                                    gattCharacteristic.getDescriptor(
                                        UUID_HEART_RATE_MEASUREMENT
                                    )
                                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                                if (gatt.writeDescriptor(descriptor)) {
                                    LogUtil.d("writeDescriptor successed", DBG)
                                } else {
                                    //TODO:
                                    LogUtil.d("writeDescriptor failed", DBG)
                                }
                            }
                        }
                    }
                } else {
                    //测试出现的情况 是否再次发现一次
                    LogUtil.w("service is null", true)
                }
            }
        }
    }

    fun onCharacteristicWrite(
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
            if (dataQueue.size > 0) {
                characteristic.setValue(dataQueue.poll())
                //TODO:写成功再写下一个
                gatt.writeCharacteristic(characteristic)
            } else {
            }
        } else {
            LogUtil.w("onCharacteristicWrite failed", DBG)
        }
        super.onCharacteristicWrite(gatt, characteristic, status)
    }

    fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
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
            if (data[0].toInt() == 0x72 && data[1].toInt() == 0x5b) { //数据开始
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
            //清零
            hasRecDataLen = 0
        }
    }

    fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor?, status: Int) {
        super.onDescriptorWrite(gatt, descriptor, status)
        if (mBluetoothGatt !== gatt) return
        LogUtil.d("")
        if (status == BluetoothGatt.GATT_SUCCESS) {
            mConnectionState = STATE_CONNECTED
            mAppExecutor.mainThread().execute(Runnable {
                val mConnectCallback: ConnectCallback =
                    RemoteCallbackManager.Companion.getInstance().getConnectCallback()
                if (mConnectCallback != null) {
                    Log.d("OMG", "====connect success==1==")
                    mConnectCallback.onConnectSuccess(device)
                }
            })
        } else {
            //TODO:
        }
    }

    private fun doWithData(values: ByteArray) {
        mAppExecutor.mainThread().execute(Runnable {
            LogUtil.d("values:" + DigitUtil.byteArrayToHexString(values))
            val command = Command(values)
            command.mac = device!!.address
            val data = command.getData()
            LogUtil.d("command:" + DigitUtil.byteToHex(command.getCommand()))
            LogUtil.d("data:" + DigitUtil.byteArrayToHexString(data))
            if (data == null) {
                LogUtil.d("data is null")
                return@Runnable
            }
            if (data[1].toInt() == 1) { //成功
                when (data[0]) {
                    Command.Companion.COMM_SET_LOCK -> {
                        val initKeyFobResult = InitRemoteResult()
                        initKeyFobResult.setBatteryLevel(data[2].toInt())
                        initKeyFobResult.setSystemInfo(deviceInfo)
                        val callback: RemoteCallback =
                            RemoteCallbackManager.Companion.getInstance().getCallback()
                        if (callback != null) {
                            (callback as InitRemoteCallback).onInitSuccess(initKeyFobResult)
                        }
                    }
                }
            } else {
                val callback: RemoteCallback =
                    RemoteCallbackManager.Companion.getInstance().getCallback()
                if (callback != null) {
                    callback.onFail(RemoteError.FAILED)
                }
            }
        })
    }

    fun isConnected(address: String): Boolean {
        if (TextUtils.isEmpty(address)) {
            return false
        }
        return if (address == mAddress && mConnectionState == STATE_CONNECTED) {
            true
        } else false
    }

    fun getDeviceInfo(): SystemInfo? {
        return deviceInfo
    }

    private fun close() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close()
            mBluetoothGatt = null
        }
    }

    private fun disconnect() {
        if (mBluetoothGatt != null) {
            mConnectionState = STATE_DISCONNECTED
            mBluetoothGatt.disconnect()
        }
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
        var UUID_SERVICE = "00001710-0000-1000-8000-00805f9b34fb"
        var UUID_WRITE = "00000002-0000-1000-8000-00805f9b34fb"
        var UUID_NODIFY = "00000003-0000-1000-8000-00805f9b34fb"
        private const val DEVICE_INFORMATION_SERVICE = "0000180a-0000-1000-8000-00805f9b34fb"
        private const val READ_MODEL_NUMBER_UUID = "00002a24-0000-1000-8000-00805f9b34fb"
        private const val READ_FIRMWARE_REVISION_UUID = "00002a26-0000-1000-8000-00805f9b34fb"
        private const val READ_HARDWARE_REVISION_UUID = "00002a27-0000-1000-8000-00805f9b34fb"
        private const val READ_MANUFACTURER_NAME_UUID = "00002a29-0000-1000-8000-00805f9b34fb"
        private val instance = GattCallbackHelper()
        const val STATE_DISCONNECTED = 0
        const val STATE_CONNECTING = 1
        const val STATE_CONNECTED = 2
        var mConnectionState = STATE_DISCONNECTED
        fun getInstance(): GattCallbackHelper {
            return instance
        }
    }
}