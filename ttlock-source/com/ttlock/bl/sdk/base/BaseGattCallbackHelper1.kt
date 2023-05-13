package com.ttlock.bl.sdk.base

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.util.Context
import android.util.Handler
import android.util.Looper
import com.ttlock.bl.sdk.device.TTDevice
import com.ttlock.bl.sdk.entity.FirmwareInfo
import com.ttlock.bl.sdk.executor.AppExecutors
import com.ttlock.bl.sdk.util.DigitUtil
import com.ttlock.bl.sdk.util.LogUtil
import java.lang.Exception
import java.util.*

/**
 * Created by TTLock on 2019/3/11.
 */
open class BaseGattCallbackHelper<T : TTDevice?> protected constructor() : BluetoothGattCallback() {
    protected var DBG = true
    private var modelNumberCharacteristic: BluetoothGattCharacteristic? = null
    private var hardwareRevisionCharacteristic: BluetoothGattCharacteristic? = null
    private var firmwareRevisionCharacteristic: BluetoothGattCharacteristic? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothGatt: BluetoothGatt? = null
    private var mWriteCharacteristic: BluetoothGattCharacteristic? = null
    protected var device: T? = null
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
    protected var context: Context? = null
    protected var mAppExecutor: AppExecutors
    protected var handler: Handler
    protected var firmwareInfo: FirmwareInfo? = null

    // TODO:
    private val isInitSuccess = false
    private val noResponseRunable = Runnable {
        noResponseCallback()
        // todo:回调
//            KeypadCallback callback = KeypadCallbackManager.getInstance().getCallback();
//            if (callback != null)
//                callback.onFail(KeypadError.NO_RESPONSE);
        disconnect()
    }

    protected open fun noResponseCallback() {}

    init {
        mAppExecutor = AppExecutors()
        recDataBuf = ByteArray(maxBufferCount)
        handler = Handler(Looper.getMainLooper())
    }

    fun prepare(context: Context) {
        this.context = context
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        LogUtil.d("context:$context")
        LogUtil.d("mBluetoothAdapter:$mBluetoothAdapter")
    }

    //    public void connect(String mac) {
    //        final BluetoothDevice bleDevice = mBluetoothAdapter.getRemoteDevice(mac);
    //        connect(new WirelessKeypad(bleDevice));
    //    }
    fun connect(ttDevice: T) {
        LogUtil.d("context:$context")
        LogUtil.d("mBluetoothAdapter:$mBluetoothAdapter")
        try {
            device = ttDevice
            val address: String = device.getAddress()
            val bleDevice: BluetoothDevice = mBluetoothAdapter.getRemoteDevice(address)
            clear()
            mBluetoothGatt = bleDevice.connectGatt(context!!, false, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getDevice(): TTDevice? {
        return device
    }

    fun setDevice(device: T) {
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
            disconnectedCallback()
            // 连接失败回调todo:
//            mAppExecutor.mainThread().execute(new Runnable() {
//                @Override
//                public void run() {
//                    ConnectCallback mConnectCallback = KeypadCallbackManager.getInstance().getConnectCallback();
//                    if(mConnectCallback != null){
//                        Log.d("OMG","====disconnect==1==" + isInitSuccess);
//                        mConnectCallback.onFail(KeypadError.KEYBOARD_CONNECT_FAIL);
//                    }
//                    isInitSuccess = false;
//                }
//            });
            clear()
        }
    }

    protected open fun disconnectedCallback() {}
    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        super.onServicesDiscovered(gatt, status)
        LogUtil.d("")
        if (status == BluetoothGatt.GATT_SUCCESS) {
            val service: BluetoothGattService? = mBluetoothGatt!!.getService(
                UUID.fromString(
                    DEVICE_INFORMATION_SERVICE
                )
            )
            if (service != null) {
                val gattCharacteristics: List<BluetoothGattCharacteristic>? =
                    service.getCharacteristics()
                if (gattCharacteristics != null && gattCharacteristics.isNotEmpty()) {
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
            gatt.readCharacteristic(modelNumberCharacteristic!!)

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

    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        super.onCharacteristicRead(gatt, characteristic, status)
        LogUtil.d("gatt=$gatt characteristic=$characteristic status=$status", DBG)
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (characteristic === modelNumberCharacteristic) {
                if (firmwareInfo == null) firmwareInfo = FirmwareInfo()
                firmwareInfo!!.setModelNum(String(characteristic.getValue()))
                gatt.readCharacteristic(hardwareRevisionCharacteristic!!)
            } else if (characteristic === hardwareRevisionCharacteristic) {
                firmwareInfo!!.setHardwareRevision(String(characteristic.getValue()))
                gatt.readCharacteristic(firmwareRevisionCharacteristic!!)
            } else if (characteristic === firmwareRevisionCharacteristic) {
                firmwareInfo!!.setFirmwareRevision(String(characteristic.getValue()))
                LogUtil.d("deviceInfo:$firmwareInfo")
                service = gatt.getService(UUID.fromString(UUID_SERVICE))
                if (service != null) {
                    val gattCharacteristics: List<BluetoothGattCharacteristic>? =
                        service!!.getCharacteristics()
                    if (gattCharacteristics != null && gattCharacteristics.isNotEmpty()) {
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
            }
        }
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
            if (dataQueue.size > 0) {
                characteristic.setValue(dataQueue.poll())
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

    fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor?, status: Int) {
        super.onDescriptorWrite(gatt, descriptor, status)
        if (mBluetoothGatt !== gatt) return
        LogUtil.d("")
        if (status == BluetoothGatt.GATT_SUCCESS) {
            connectCallback()
        } else {
            disconnectedCallback()
        }
    }

    protected open fun connectCallback() {}
    protected open fun doWithData(values: ByteArray?) {}
    private fun close() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close()
            mBluetoothGatt = null
        }
    }

    protected fun disconnect() {
        if (mBluetoothGatt != null) {
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
        var UUID_SERVICE: String = BleConstant.UUID_SERVICE_Door_Sensor
        var UUID_WRITE = "00000002-0000-1000-8000-00805f9b34fb"
        var UUID_NODIFY = "00000003-0000-1000-8000-00805f9b34fb"
        private const val DEVICE_INFORMATION_SERVICE = "0000180a-0000-1000-8000-00805f9b34fb"
        private const val READ_MODEL_NUMBER_UUID = "00002a24-0000-1000-8000-00805f9b34fb"
        private const val READ_FIRMWARE_REVISION_UUID = "00002a26-0000-1000-8000-00805f9b34fb"
        private const val READ_HARDWARE_REVISION_UUID = "00002a27-0000-1000-8000-00805f9b34fb"
        private const val READ_MANUFACTURER_NAME_UUID = "00002a29-0000-1000-8000-00805f9b34fb"
        private val instance: BaseGattCallbackHelper<*> = BaseGattCallbackHelper<Any?>()
        fun getInstance(): BaseGattCallbackHelper<*> {
            return instance
        }
    }
}
