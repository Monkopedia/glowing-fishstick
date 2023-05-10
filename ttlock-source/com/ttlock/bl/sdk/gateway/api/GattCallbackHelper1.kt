package com.ttlock.bl.sdk.gateway.api

import android.bluetooth.BluetoothAdapter
import com.ttlock.bl.sdk.gateway.callback.ConfigIpCallback
import com.ttlock.bl.sdk.gateway.callback.ConnectCallback
import com.ttlock.bl.sdk.gateway.callback.EnterDfuCallback
import com.ttlock.bl.sdk.gateway.command.Command
import com.ttlock.bl.sdk.gateway.command.CommandUtil
import com.ttlock.bl.sdk.gateway.model.DeviceInfo
import java.lang.Exception
import java.util.*

/**
 * Created by TTLock on 2019/3/11.
 */
class GattCallbackHelper private constructor() : BluetoothGattCallback() {
    private val DBG = true
    private var modelNumberCharacteristic: BluetoothGattCharacteristic? = null
    private var wifiMacCharacteristic: BluetoothGattCharacteristic? = null
    private var hardwareRevisionCharacteristic: BluetoothGattCharacteristic? = null
    private var firmwareRevisionCharacteristic: BluetoothGattCharacteristic? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothGatt: BluetoothGatt? = null
    private var mWriteCharacteristic: BluetoothGattCharacteristic? = null

    //    private G2GatewayCallback g2GatewayCallback;
    private var device: ExtendedBluetoothDevice? = null
    private var configureInfo: ConfigureGatewayInfo? = null
    private var recDataTotalLen = 0
    private var hasRecDataLen = 0
    private val recDataBuf: ByteArray
    private var curCommand: Byte = 0
    private val wiFis: MutableList<WiFi> = ArrayList<WiFi>()

    /**
     * 接收的最大长度
     */
    private val maxBufferCount = 256
    private var deviceInfo: DeviceInfo? = null
    private var service: BluetoothGattService? = null

    /**
     * 传输数据
     */
    private var dataQueue: LinkedList<ByteArray>? = null
    private var context: Context? = null
    private val mAppExecutor: AppExecutors
    private var callback: GatewayCallback? = null
    private val handler: Handler = Handler()
    private val connectTimeOutRunable = Runnable { doWithConnectTimeout() }

    init {
        mAppExecutor = AppExecutors()
        recDataBuf = ByteArray(maxBufferCount)
    }

    fun prepare(context: Context?) {
        this.context = context
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    fun connect(mac: String?) {
        val bleDevice: BluetoothDevice = mBluetoothAdapter.getRemoteDevice(mac)
        connect(ExtendedBluetoothDevice(bleDevice))
    }

    fun connect(extendedBluetoothDevice: ExtendedBluetoothDevice?) {
        curCommand = 0
        device = extendedBluetoothDevice
        val address: String = device.getAddress()
        val bleDevice: BluetoothDevice = mBluetoothAdapter.getRemoteDevice(address)
        clear()
        removeConnectTimeout()
        startConnectTimeout()
        mBluetoothGatt = bleDevice.connectGatt(context, false, this)
    }

    //    public GattCallbackHelper(BluetoothGatt mBluetoothGatt) {
    //        this.mBluetoothGatt = mBluetoothGatt;
    //    }
    fun getDevice(): ExtendedBluetoothDevice? {
        return device
    }

    fun setDevice(device: ExtendedBluetoothDevice?) {
        this.device = device
    }

    fun getConfigureInfo(): ConfigureGatewayInfo? {
        return configureInfo
    }

    fun setConfigureInfo(configureInfo: ConfigureGatewayInfo?) {
        this.configureInfo = configureInfo
    }

    fun getBluetoothGatt(): BluetoothGatt? {
        return mBluetoothGatt
    }

    fun setBluetoothGatt(mBluetoothGatt: BluetoothGatt?) {
        this.mBluetoothGatt = mBluetoothGatt
    }

    fun sendCommand(command: ByteArray) {
        var len = command.size
        curCommand = command[2]
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
                hasRecDataLen = 0 //发送前恢复接收数据的起始位置
                mWriteCharacteristic.setValue(dataQueue.poll())
                mBluetoothGatt.writeCharacteristic(mWriteCharacteristic)
            } catch (e: Exception) {
                //TODO:
            }
        } else {
            ConnectManager.Companion.getInstance().setDisconnected()
            callback = GatewayCallbackManager.Companion.getInstance().getCallback()
            if (callback != null) {
                callback.onFail(GatewayError.FAILED)
            }
            LogUtil.d("mBluetoothGatt:$mBluetoothGatt", DBG)
            LogUtil.d("mNotifyCharacteristic or mBluetoothGatt is null", DBG)
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
            //并不是所有设备都有这个wifi mac
            wifiMacCharacteristic = null
            LogUtil.d("STATE_CONNECTED")
            LogUtil.d("Attempting to start service discovery:" + gatt.discoverServices())
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            LogUtil.d("STATE_DISCONNECTED")
            removeConnectTimeout()
            mAppExecutor.mainThread().execute(Runnable {
                doWithDisconnect()
                curCommand = 0
            })
            close()
        }
    }

    private fun doWithDisconnect() {
        //设置断开状态
        ConnectManager.Companion.getInstance().setDisconnected()
        callback = GatewayCallbackManager.Companion.getInstance().getCallback()
        LogUtil.d("callback:$callback")
        if (callback != null) {
            when (curCommand) {
                Command.Companion.COMM_CONFIGURE_WIFI -> {
                    callback.onFail(GatewayError.FAILED_TO_CONFIGURE_ROUTER)
                    return
                }
                Command.Companion.COMM_CONFIGURE_SERVER -> {
                    callback.onFail(GatewayError.FAILED_TO_CONFIGURE_SERVER)
                    return
                }
                Command.Companion.COMM_CONFIGURE_ACCOUNT -> {
                    callback.onFail(GatewayError.FAILED_TO_CONFIGURE_ACCOUNT)
                    return
                }
                else -> {
                    if (curCommand.toInt() != 0) callback.onFail(GatewayError.COMMUNICATION_DISCONNECTED)
                    return
                }
            }
        } else {
            val mConnectCallback: ConnectCallback =
                GatewayCallbackManager.Companion.getInstance().getConnectCallback()
            LogUtil.d("mConnectCallback:$mConnectCallback")
            if (mConnectCallback != null) {
                mConnectCallback.onDisconnected()
            }
        }
    }

    fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        super.onServicesDiscovered(gatt, status)
        LogUtil.d("")
        if (status == BluetoothGatt.GATT_SUCCESS) {
            val services: List<BluetoothGattService> = gatt.getServices()
            for (service in services) {
                LogUtil.d("service:" + service.getUuid())
            }
            service = gatt.getService(UUID.fromString(DEVICE_INFORMATION_SERVICE))
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
                            gatt.readCharacteristic(gattCharacteristic)
                        } else if (gattCharacteristic.getUuid().toString()
                                .equals(READ_WIFI_MAC_UUID)
                        ) {
                            wifiMacCharacteristic = gattCharacteristic
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
            } else {
                //测试出现的情况 是否再次发现一次
                LogUtil.w("service is null", true)
            }
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
                deviceInfo = DeviceInfo()
                deviceInfo!!.modelNum = String(characteristic.getValue())
                gatt.readCharacteristic(if (wifiMacCharacteristic != null) wifiMacCharacteristic else hardwareRevisionCharacteristic)
            } else if (characteristic === wifiMacCharacteristic) {
                deviceInfo!!.networkMac = String(characteristic.getValue())
                gatt.readCharacteristic(hardwareRevisionCharacteristic)
            } else if (characteristic === hardwareRevisionCharacteristic) {
                deviceInfo!!.hardwareRevision = String(characteristic.getValue())
                gatt.readCharacteristic(firmwareRevisionCharacteristic)
            } else if (characteristic === firmwareRevisionCharacteristic) {
                deviceInfo!!.firmwareRevision = String(characteristic.getValue())
                LogUtil.d("deviceInfo:$deviceInfo")
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
            LogUtil.d("gatt=$gatt characteristic=$characteristic", DBG)
            val data: ByteArray = characteristic.getValue()
            LogUtil.d("data:" + DigitUtil.byteArrayToHexString(data))
            val dataLen = data.size
            System.arraycopy(data, 0, recDataBuf, hasRecDataLen, dataLen)
            if (data.size >= 2 && data[0].toInt() == 0x72 && data[1].toInt() == 0x5b) { //数据开始
                recDataTotalLen = data[3] + 2 + 1 + 1 + 1
                LogUtil.d("recDataTotalLen:$recDataTotalLen")
            } else if (hasRecDataLen < 4) { //TODO:上一次的数据
                recDataTotalLen = data[3 - hasRecDataLen] + 2 + 1 + 1 + 1
                LogUtil.d("recDataTotalLen:$recDataTotalLen")
            }
            hasRecDataLen += dataLen
            LogUtil.d("hasRecDataLen:$hasRecDataLen")
            if (hasRecDataLen >= recDataTotalLen) {
                LogUtil.d("recDataTotalLen:$recDataTotalLen")
                doWithData(Arrays.copyOf(recDataBuf, recDataTotalLen))
                //TODO:会有缓存的数据一起发送下来
                if (recDataTotalLen != hasRecDataLen && hasRecDataLen > 0) {
                    hasRecDataLen = hasRecDataLen - recDataTotalLen
                    System.arraycopy(data, 20 - hasRecDataLen, recDataBuf, 0, hasRecDataLen)
                    if (hasRecDataLen > 3) recDataTotalLen =
                        data[20 - hasRecDataLen + 3] + 2 + 1 + 1 + 1
                    LogUtil.d("recDataTotalLen:$recDataTotalLen")
                } else {
                    hasRecDataLen = 0
                }
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
            if (device != null) CommandUtil.gatewayEcho(device.getAddress())
        } else {
            //TODO:
        }
    }

    fun clearWifi() {
        wiFis.clear()
    }

    fun connectCallback() {
        mAppExecutor.mainThread().execute(Runnable {
            removeConnectTimeout()
            ConnectManager.Companion.getInstance().onConnectSuccess(device)
        })
    }

    private fun doWithData(values: ByteArray) {
        mAppExecutor.mainThread().execute(Runnable {
            LogUtil.d("values:" + DigitUtil.byteArrayToHexString(values))
            val command = Command(values)
            command.mac = device.getAddress()
            val data = command.getData()
            LogUtil.d("command:" + DigitUtil.byteToHex(command.getCommand()))
            LogUtil.d("data:" + DigitUtil.byteArrayToHexString(data))
            callback = GatewayCallbackManager.Companion.getInstance().getCallback()
            if (callback == null && command.getCommand() != Command.Companion.COMM_ECHO) { //保持连接是默认做的 没有回调
                LogUtil.d("callback is null")
                return@Runnable
            }
            if (data == null) {
                LogUtil.d("data is null")
                return@Runnable
            }
            when (command.getCommand()) {
                Command.Companion.COMM_ECHO -> connectCallback()
                Command.Companion.COMM_GET_NEARBY_SSID -> when (data[0]) {
                    0 -> {
                        val len = data[1].toInt()
                        val wiFi = WiFi()
                        wiFi.ssid = String(Arrays.copyOfRange(data, 2, 2 + len))
                        wiFi.rssi = data[len + 2].toInt()
                        LogUtil.d("wifi:$wiFi")
                        if (!TextUtils.isEmpty(wiFi.ssid)) {
                            insertWifi(wiFi)
                            (callback as ScanWiFiByGatewayCallback).onScanWiFiByGateway(wiFis)
                        }
                    }
                    5 -> {
                        //TODO:数据一样 是不是要返回
                        (callback as ScanWiFiByGatewayCallback).onScanWiFiByGatewaySuccess()
                        wiFis.clear()
                    }
                    else -> callback.onFail(GatewayError.Companion.getInstance(data[0].toInt()))
                }
                Command.Companion.COMM_CONFIGURE_WIFI -> {
                    curCommand = 0
                    if (data[0].toInt() == 0) {
                        CommandUtil.configureServer(configureInfo)
                    } else {
                        if (data[0].toInt() != 2) {
                            if (callback != null) {
                                callback.onFail(GatewayError.Companion.getInstance(data[0].toInt()))
                            }
                        } else { //命令接收成功，正在处理
                            curCommand = Command.Companion.COMM_CONFIGURE_WIFI
                        }
                    }
                }
                Command.Companion.COMM_CONFIGURE_SERVER -> {
                    curCommand = 0
                    if (data[0].toInt() == 0) CommandUtil.configureAccount(configureInfo) else {
                        if (callback != null) {
                            callback.onFail(GatewayError.Companion.getInstance(data[0].toInt()))
                        }
                    }
                }
                Command.Companion.COMM_CONFIGURE_ACCOUNT -> {
                    curCommand = 0
                    if (data[0].toInt() == 0) {
                        GatewayCallbackManager.Companion.getInstance().clearAllCallback()
                        (callback as InitGatewayCallback).onInitGatewaySuccess(deviceInfo)
                        LogUtil.d("success")
                    } else {
                        if (callback != null) {
                            callback.onFail(GatewayError.Companion.getInstance(data[0].toInt()))
                        }
                    }
                }
                Command.Companion.COMM_ENTER_DFU -> {
                    if (data[0].toInt() == 0) {
                        (callback as EnterDfuCallback).onEnterDfuSuccess()
                    } else {
                        callback.onFail(GatewayError.Companion.getInstance(data[0].toInt()))
                    }
                    //清理缓存的回调类型
                    GatewayCallbackManager.Companion.getInstance().clearAllCallback()
                }
                Command.Companion.COMM_CONFIG_IP -> if (data[0].toInt() == 0) {
                    (callback as ConfigIpCallback).onConfigIpSuccess()
                } else {
                    callback.onFail(GatewayError.Companion.getInstance(data[0].toInt()))
                }
            }
        })
    }

    @Synchronized
    private fun insertWifi(newWifi: WiFi) {
        var hasInsert = false
        var i = 0
        while (i < wiFis.size) {
            val wiFi: WiFi = wiFis[i]
            if (newWifi.ssid == wiFi.ssid) { //信号值排序要改
                if (hasInsert) wiFis.removeAt(i) else {
                    //TODO:
                    hasInsert = true
                }
                break
            } else if (newWifi.rssi > wiFi.rssi) {
                if (!hasInsert) {
                    wiFis.add(i, newWifi)
                    hasInsert = true
                    //TODO:
                    i++
                }
            }
            i++
        }
        if (!hasInsert) wiFis.add(newWifi)
    }

    private fun close() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close()
            mBluetoothGatt = null
        }
    }

    fun disconnect() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect()
        }
    }

    fun clear() {
        disconnect()
        close()
    }

    fun startConnectTimeout() {
        handler.postDelayed(connectTimeOutRunable, CONNECT_TIMEOUT)
    }

    fun removeConnectTimeout() {
        handler.removeCallbacks(connectTimeOutRunable)
    }

    fun doWithConnectTimeout() {
        clear()
        connectTimeoutCallback()
    }

    fun connectTimeoutCallback() {
        callback = GatewayCallbackManager.Companion.getInstance().getCallback()
        if (callback != null) {
            callback.onFail(GatewayError.CONNECT_TIMEOUT)
        } else {
            ConnectManager.Companion.getInstance().setDisconnected()
            val mConnectCallback: ConnectCallback =
                GatewayCallbackManager.Companion.getInstance().getConnectCallback()
            if (mConnectCallback != null) {
                mConnectCallback.onDisconnected()
            }
        }
    }

    fun getNetworkMac(): String {
        return if (deviceInfo != null) {
            deviceInfo!!.networkMac
        } else ""
    }

    companion object {
        /**
         * 连接超时10s
         */
        private const val CONNECT_TIMEOUT = 10 * 1000
        val UUID_HEART_RATE_MEASUREMENT: UUID =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        var UUID_SERVICE = "00001911-0000-1000-8000-00805f9b34fb"
        var UUID_WRITE = "00000002-0000-1000-8000-00805f9b34fb"
        var UUID_NODIFY = "00000003-0000-1000-8000-00805f9b34fb"
        private const val DEVICE_INFORMATION_SERVICE = "0000180a-0000-1000-8000-00805f9b34fb"
        private const val READ_MODEL_NUMBER_UUID = "00002a24-0000-1000-8000-00805f9b34fb"
        private const val READ_WIFI_MAC_UUID = "00002a25-0000-1000-8000-00805f9b34fb"
        private const val READ_FIRMWARE_REVISION_UUID = "00002a26-0000-1000-8000-00805f9b34fb"
        private const val READ_HARDWARE_REVISION_UUID = "00002a27-0000-1000-8000-00805f9b34fb"
        private const val READ_MANUFACTURER_NAME_UUID = "00002a29-0000-1000-8000-00805f9b34fb"
        private val instance = GattCallbackHelper()
        fun getInstance(): GattCallbackHelper {
            return instance
        }
    }
}