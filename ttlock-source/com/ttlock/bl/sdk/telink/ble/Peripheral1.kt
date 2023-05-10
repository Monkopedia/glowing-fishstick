/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.ttlock.bl.sdk.telink.ble

import android.bluetooth.BluetoothDevice
import com.ttlock.bl.sdk.telink.ble.Command.Callback
import com.ttlock.bl.sdk.telink.util.Arrays
import java.util.*

open class Peripheral(device: BluetoothDevice, scanRecord: ByteArray, rssi: Int) :
    BluetoothGattCallback() {
    protected val mInputCommandQueue: Queue<CommandContext> =
        ConcurrentLinkedQueue<CommandContext>()
    protected val mOutputCommandQueue: Queue<CommandContext> =
        ConcurrentLinkedQueue<CommandContext>()
    protected val mNotificationCallbacks: MutableMap<String, CommandContext> =
        ConcurrentHashMap<String, CommandContext>()
    protected val mTimeoutHandler: Handler = Handler(Looper.getMainLooper())
    protected val mRssiUpdateHandler: Handler = Handler(Looper.getMainLooper())
    protected val mDelayHandler: Handler = Handler(Looper.getMainLooper())
    protected val mRssiUpdateRunnable: Runnable = RssiUpdateRunnable()
    protected val mCommandTimeoutRunnable: Runnable = CommandTimeoutRunnable()
    protected val mCommandDelayRunnable: Runnable = CommandDelayRunnable()
    private val mStateLock = Any()
    private val mProcessLock = Any()
    protected var device: BluetoothDevice
    var gatt: BluetoothGatt? = null
    protected var rssi: Int
    protected var scanRecord: ByteArray
    protected var name: String
    protected var mac: String
    protected var macBytes: ByteArray?
    protected var type: Int
    protected var mServices: List<BluetoothGattService?>? = null
    protected var processing = false
    protected var monitorRssi = false
    protected var updateIntervalMill = 5 * 1000
    protected var commandTimeoutMill = 10 * 1000
    private var mConnState = CONN_STATE_IDLE

    init {
        this.device = device
        this.scanRecord = scanRecord
        this.rssi = rssi
        name = device.getName()
        mac = device.getAddress()
        type = device.getType()
    }

    /********************************************************************************
     * Public API
     */
    fun getDevice(): BluetoothDevice {
        return device
    }

    fun getDeviceName(): String {
        return name
    }

    fun getMacAddress(): String {
        return mac
    }

    fun getServices(): List<BluetoothGattService?>? {
        return mServices
    }

    fun getMacBytes(): ByteArray? {
        if (macBytes == null) {
            val strArray = getMacAddress().split(":").toTypedArray()
            val length = strArray.size
            macBytes = ByteArray(length)
            for (i in 0 until length) {
                macBytes!![i] = (strArray[i].toInt(16) and 0xFF).toByte()
            }
            Arrays.reverse(macBytes, 0, length - 1)
        }
        return macBytes
    }

    fun getType(): Int {
        return type
    }

    fun getRssi(): Int {
        return rssi
    }

    fun isConnected(): Boolean {
        synchronized(mStateLock) { return mConnState == CONN_STATE_CONNECTED }
    }

    fun connect(context: Context?) {
        synchronized(mStateLock) {
            if (mConnState == CONN_STATE_IDLE) {
                TelinkLog.d(
                    "connect " + getDeviceName() + " -- "
                            + getMacAddress()
                )
                mConnState = CONN_STATE_CONNECTING
                gatt = device.connectGatt(context, false, this)
                //                this.gatt = this.device.connectGatt(context, false, this, BluetoothDevice.TRANSPORT_LE);
                requestConnectionPriority(CONNECTION_PRIORITY_HIGH) //todo:
                if (gatt == null) {
                    disconnect()
                    mConnState = CONN_STATE_IDLE
                    onDisconnect()
                }
            }
        }
    }

    fun disconnect() {
        synchronized(mStateLock) { if (mConnState != CONN_STATE_CONNECTING && mConnState != CONN_STATE_CONNECTED) return }
        TelinkLog.d(
            "disconnect " + getDeviceName() + " -- "
                    + getMacAddress()
        )
        this.clear()
        synchronized(mStateLock) {
            if (gatt != null) {
                val connState = mConnState
                if (connState == CONN_STATE_CONNECTED) {
                    gatt.disconnect()
                    mConnState = CONN_STATE_DISCONNECTING
                } else {
                    gatt.disconnect()
                    gatt.close()
                    mConnState = CONN_STATE_CLOSED
                }
            } else {
                mConnState = CONN_STATE_IDLE
            }
        }
    }

    private fun clear() {
        processing = false
        stopMonitoringRssi()
        cancelCommandTimeoutTask()
        mInputCommandQueue.clear()
        mOutputCommandQueue.clear()
        mNotificationCallbacks.clear()
        mDelayHandler.removeCallbacksAndMessages(null)
    }

    fun sendCommand(callback: Callback?, command: Command?): Boolean {
        synchronized(mStateLock) { if (mConnState != CONN_STATE_CONNECTED) return false }
        val commandContext = CommandContext(callback, command)
        postCommand(commandContext)
        return true
    }

    fun startMonitoringRssi(interval: Int) {
        monitorRssi = true
        if (interval <= 0) updateIntervalMill = RSSI_UPDATE_TIME_INTERVAL else updateIntervalMill =
            interval
    }

    fun stopMonitoringRssi() {
        monitorRssi = false
        mRssiUpdateHandler.removeCallbacks(mRssiUpdateRunnable)
        mRssiUpdateHandler.removeCallbacksAndMessages(null)
    }

    fun requestConnectionPriority(connectionPriority: Int): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && gatt.requestConnectionPriority(
            connectionPriority
        )
    }

    /********************************************************************************
     * Protected API
     */
    protected open fun onConnect() {
        //this.requestConnectionPriority(CONNECTION_PRIORITY_BALANCED);
        enableMonitorRssi(monitorRssi)
    }

    protected open fun onDisconnect() {
        enableMonitorRssi(false)
    }

    protected open fun onServicesDiscovered(services: List<BluetoothGattService?>) {
        for (service in services) { //TODO:test
            Log.e("tag", "service:" + service.getUuid())
        }
    }

    protected open fun onNotify(
        data: ByteArray?, serviceUUID: UUID?,
        characteristicUUID: UUID?, tag: Any?
    ) {
    }

    protected open fun onEnableNotify() {}
    protected open fun onDisableNotify() {}
    protected fun onRssiChanged() {}
    protected fun enableMonitorRssi(enable: Boolean) {
        if (enable) {
            mRssiUpdateHandler.removeCallbacks(mRssiUpdateRunnable)
            mRssiUpdateHandler.postDelayed(mRssiUpdateRunnable, updateIntervalMill)
        } else {
            mRssiUpdateHandler.removeCallbacks(mRssiUpdateRunnable)
            mRssiUpdateHandler.removeCallbacksAndMessages(null)
        }
    }

    /********************************************************************************
     * Command Handler API
     */
    private fun postCommand(commandContext: CommandContext) {
        TelinkLog.d("postCommand")
        mInputCommandQueue.add(commandContext)
        synchronized(mProcessLock) {
            if (!processing) {
                this.processCommand()
            }
        }
    }

    private fun processCommand() {
        TelinkLog.d("processing : " + processing)
        var commandContext: CommandContext?
        val commandType: CommandType
        synchronized(mInputCommandQueue) {
            if (mInputCommandQueue.isEmpty()) return
            commandContext = mInputCommandQueue.poll()
        }
        if (commandContext == null) return
        commandType = commandContext!!.command!!.type
        if (commandType != CommandType.ENABLE_NOTIFY && commandType != CommandType.DISABLE_NOTIFY) {
            mOutputCommandQueue.add(commandContext)
            synchronized(mProcessLock) { if (!processing) processing = true }
        }
        val delay = commandContext!!.command!!.delay
        if (delay > 0) {
            mDelayHandler.postDelayed(mCommandDelayRunnable, delay)
        } else {
            this.processCommand(commandContext!!)
        }
    }

    @Synchronized
    private fun processCommand(commandContext: CommandContext) {
        val command = commandContext.command
        val commandType: CommandType = command!!.type
        TelinkLog.d("processCommand : $command")
        when (commandType) {
            CommandType.READ -> {
                postCommandTimeoutTask()
                readCharacteristic(
                    commandContext, command.serviceUUID,
                    command.characteristicUUID
                )
            }
            CommandType.WRITE -> {
                postCommandTimeoutTask()
                writeCharacteristic(
                    commandContext, command.serviceUUID,
                    command.characteristicUUID,
                    BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT,
                    command.data
                )
            }
            CommandType.READ_DESCRIPTOR -> {
                postCommandTimeoutTask()
                readDescriptor(
                    commandContext,
                    command.serviceUUID,
                    command.characteristicUUID,
                    command.descriptorUUID
                )
            }
            CommandType.WRITE_NO_RESPONSE -> {
                postCommandTimeoutTask()
                writeCharacteristic(
                    commandContext, command.serviceUUID,
                    command.characteristicUUID,
                    BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE,
                    command.data
                )
            }
            CommandType.ENABLE_NOTIFY -> enableNotification(
                commandContext, command.serviceUUID,
                command.characteristicUUID
            )
            CommandType.DISABLE_NOTIFY -> disableNotification(
                commandContext, command.serviceUUID,
                command.characteristicUUID
            )
        }
    }

    private fun commandCompleted() {
        TelinkLog.d("commandCompleted")
        synchronized(mProcessLock) { if (processing) processing = false }
        this.processCommand()
    }

    private fun commandSuccess(commandContext: CommandContext?, data: Any?) {
        TelinkLog.d("commandSuccess")
        if (commandContext != null) {
            val command = commandContext.command
            val callback = commandContext.callback
            commandContext.clear()
            callback?.success(
                this, command,
                data
            )
        }
    }

    private fun commandSuccess(data: Any?) {
        val commandContext: CommandContext
        commandContext = mOutputCommandQueue.poll()
        this.commandSuccess(commandContext, data)
    }

    private fun commandError(commandContext: CommandContext?, errorMsg: String) {
        TelinkLog.d("commandError")
        if (commandContext != null) {
            val command = commandContext.command
            val callback = commandContext.callback
            commandContext.clear()
            callback?.error(
                this, command,
                errorMsg
            )
        }
    }

    private fun commandError(errorMsg: String) {
        val commandContext: CommandContext
        commandContext = mOutputCommandQueue.poll()
        this.commandError(commandContext, errorMsg)
    }

    private fun commandTimeout(commandContext: CommandContext?): Boolean {
        TelinkLog.d("commandTimeout")
        if (commandContext != null) {
            val command = commandContext.command
            val callback = commandContext.callback
            commandContext.clear()
            if (callback != null) {
                return callback.timeout(this, command)
            }
        }
        return false
    }

    private fun postCommandTimeoutTask() {
        if (commandTimeoutMill <= 0) return
        mTimeoutHandler.removeCallbacksAndMessages(null)
        mTimeoutHandler.postDelayed(mCommandTimeoutRunnable, commandTimeoutMill)
    }

    private fun cancelCommandTimeoutTask() {
        mTimeoutHandler.removeCallbacksAndMessages(null)
    }

    /********************************************************************************
     * Private API
     */
    private fun readDescriptor(
        commandContext: CommandContext,
        serviceUUID: UUID, characteristicUUID: UUID, descriptorUUID: UUID
    ) {
        var success = true
        var errorMsg = ""
        val service: BluetoothGattService = gatt.getService(serviceUUID)
        if (service != null) {
            val characteristic: BluetoothGattCharacteristic = service
                .getCharacteristic(characteristicUUID)
            if (characteristic != null) {
                val descriptor: BluetoothGattDescriptor =
                    characteristic.getDescriptor(descriptorUUID)
                if (descriptor != null) {
                    if (!gatt.readDescriptor(descriptor)) {
                        success = false
                        errorMsg = "read descriptor error"
                    }
                } else {
                    success = false
                    errorMsg = "read descriptor error"
                }
            } else {
                success = false
                errorMsg = "read characteristic error"
            }
        } else {
            success = false
            errorMsg = "service is not offered by the remote device"
        }
        if (!success) {
            this.commandError(errorMsg)
            commandCompleted()
        }
    }

    private fun readCharacteristic(
        commandContext: CommandContext,
        serviceUUID: UUID, characteristicUUID: UUID
    ) {
        var success = true
        var errorMsg = ""
        val service: BluetoothGattService = gatt.getService(serviceUUID)
        if (service != null) {
            val characteristic: BluetoothGattCharacteristic = service
                .getCharacteristic(characteristicUUID)
            if (characteristic != null) {
                if (!gatt.readCharacteristic(characteristic)) {
                    success = false
                    errorMsg = "read characteristic error"
                }
            } else {
                success = false
                errorMsg = "read characteristic error"
            }
        } else {
            success = false
            errorMsg = "service is not offered by the remote device"
        }
        if (!success) {
            this.commandError(errorMsg)
            commandCompleted()
        }
    }

    private fun writeCharacteristic(
        commandContext: CommandContext,
        serviceUUID: UUID, characteristicUUID: UUID, writeType: Int,
        data: ByteArray
    ) {
        var success = true
        var errorMsg = ""
        val service: BluetoothGattService = gatt.getService(serviceUUID)
        if (service != null) {
            val characteristic: BluetoothGattCharacteristic? = findWritableCharacteristic(
                service, characteristicUUID,
                writeType
            )
            if (characteristic != null) {
                characteristic.setValue(data)
                characteristic.setWriteType(writeType)
                if (!gatt.writeCharacteristic(characteristic)) {
                    success = false
                    errorMsg = "write characteristic error"
                }
            } else {
                success = false
                errorMsg = "no characteristic"
            }
        } else {
            success = false
            errorMsg = "service is not offered by the remote device"
        }
        if (!success) {
            this.commandError(errorMsg)
            commandCompleted()
        }
    }

    private fun enableNotification(
        commandContext: CommandContext,
        serviceUUID: UUID, characteristicUUID: UUID
    ) {
        var success = true
        var errorMsg = ""
        val service: BluetoothGattService = gatt.getService(serviceUUID)
        if (service != null) {
            val characteristic: BluetoothGattCharacteristic? =
                findNotifyCharacteristic(service, characteristicUUID)
            if (characteristic != null) {
                if (!gatt.setCharacteristicNotification(
                        characteristic,
                        true
                    )
                ) {
                    success = false
                    errorMsg = "enable notification error"
                } else {
                    Log.e("tag", "put mNotificationCallbacks")
                    val key = this.generateHashKey(
                        serviceUUID,
                        characteristic
                    )
                    mNotificationCallbacks[key] = commandContext
                }
            } else {
                success = false
                errorMsg = "no characteristic"
            }
        } else {
            success = false
            errorMsg = "service is not offered by the remote device"
        }
        if (!success) {
            this.commandError(commandContext, errorMsg)
        } else {
            onEnableNotify()
        }
        commandCompleted()
    }

    private fun disableNotification(
        commandContext: CommandContext,
        serviceUUID: UUID, characteristicUUID: UUID
    ) {
        var success = true
        var errorMsg = ""
        val service: BluetoothGattService = gatt.getService(serviceUUID)
        if (service != null) {
            val characteristic: BluetoothGattCharacteristic? =
                findNotifyCharacteristic(service, characteristicUUID)
            if (characteristic != null) {
                val key = this.generateHashKey(serviceUUID, characteristic)
                mNotificationCallbacks.remove(key)
                if (!gatt.setCharacteristicNotification(
                        characteristic,
                        false
                    )
                ) {
                    success = false
                    errorMsg = "disable notification error"
                }
            } else {
                success = false
                errorMsg = "no characteristic"
            }
        } else {
            success = false
            errorMsg = "service is not offered by the remote device"
        }
        if (!success) {
            this.commandError(commandContext, errorMsg)
        } else {
            onDisableNotify()
        }
        commandCompleted()
    }

    private fun findWritableCharacteristic(
        service: BluetoothGattService, characteristicUUID: UUID, writeType: Int
    ): BluetoothGattCharacteristic? {
        var characteristic: BluetoothGattCharacteristic? = null
        var writeProperty: Int = BluetoothGattCharacteristic.PROPERTY_WRITE
        if (writeType == BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE) {
            writeProperty = BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE
        }
        val characteristics: List<BluetoothGattCharacteristic> = service
            .getCharacteristics()
        for (c in characteristics) {
            if (c.getProperties() and writeProperty !== 0
                && characteristicUUID == c.getUuid()
            ) {
                characteristic = c
                break
            }
        }
        return characteristic
    }

    private fun findNotifyCharacteristic(
        service: BluetoothGattService, characteristicUUID: UUID
    ): BluetoothGattCharacteristic? {
        var characteristic: BluetoothGattCharacteristic? = null
        val characteristics: List<BluetoothGattCharacteristic> = service
            .getCharacteristics()
        for (c in characteristics) {
            if (c.getProperties() and BluetoothGattCharacteristic.PROPERTY_NOTIFY !== 0
                && characteristicUUID == c.getUuid()
            ) {
                characteristic = c
                break
            }
        }
        if (characteristic != null) return characteristic
        for (c in characteristics) {
            if (c.getProperties() and BluetoothGattCharacteristic.PROPERTY_INDICATE !== 0
                && characteristicUUID == c.getUuid()
            ) {
                characteristic = c
                break
            }
        }
        return characteristic
    }

    private fun generateHashKey(characteristic: BluetoothGattCharacteristic): String {
        return this.generateHashKey(
            characteristic.getService().getUuid(),
            characteristic
        )
    }

    protected fun generateHashKey(
        serviceUUID: UUID,
        characteristic: BluetoothGattCharacteristic
    ): String {
        return (serviceUUID.toString() + "|" + characteristic.getUuid()
                + "|" + characteristic.getInstanceId())
    }

    /********************************************************************************
     * Implements BluetoothGattCallback API
     */
    fun onConnectionStateChange(
        gatt: BluetoothGatt?, status: Int,
        newState: Int
    ) {
        TelinkLog.d(
            "onConnectionStateChange  status :" + status + " state : "
                    + newState
        )
        if (newState == BluetoothGatt.STATE_CONNECTED) {
            synchronized(mStateLock) { mConnState = CONN_STATE_CONNECTED }
            if (this.gatt == null || !this.gatt.discoverServices()) {
                TelinkLog.d(
                    "remote service discovery has been stopped status = "
                            + newState
                )
                disconnect()
            } else {
//                this.gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
                onConnect()
            }
        } else {
            synchronized(mStateLock) {
                TelinkLog.d("Close")
                if (this.gatt != null) {
                    this.gatt.close()
                    mConnState = CONN_STATE_CLOSED
                }
                this.clear()
                mConnState = CONN_STATE_IDLE
                onDisconnect()
            }
        }
    }

    fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic
    ) {
        super.onCharacteristicChanged(gatt, characteristic)

//        Log.e("tag", "onCharacteristicChanged");

//        Log.e("tag", "value:" + Arrays.bytesToHexString(characteristic.getValue(), ":"));
        val key = this.generateHashKey(characteristic)
        val commandContext = mNotificationCallbacks[key]
        if (commandContext != null) {
            onNotify(
                characteristic.getValue(),
                commandContext.command!!.serviceUUID,
                commandContext.command!!.characteristicUUID,
                commandContext.command!!.tag
            )
        }

        //todo:只接收一次通知数据
        mNotificationCallbacks.clear()
    }

    fun onCharacteristicRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic, status: Int
    ) {
        super.onCharacteristicRead(gatt, characteristic, status)
        cancelCommandTimeoutTask()
        if (status == BluetoothGatt.GATT_SUCCESS) {
            val data: ByteArray = characteristic.getValue()
            this.commandSuccess(data)
        } else {
            this.commandError("read characteristic failed")
        }
        commandCompleted()
    }

    fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?, status: Int
    ) {
        super.onCharacteristicWrite(gatt, characteristic, status)
        cancelCommandTimeoutTask()
        if (status == BluetoothGatt.GATT_SUCCESS) {
            this.commandSuccess(null)
        } else {
            this.commandError("write characteristic fail")
        }
        TelinkLog.d("onCharacteristicWrite newStatus : $status")
        commandCompleted()
    }

    fun onDescriptorRead(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor, status: Int
    ) {
        super.onDescriptorRead(gatt, descriptor, status)
        cancelCommandTimeoutTask()
        if (status == BluetoothGatt.GATT_SUCCESS) {
            val data: ByteArray = descriptor.getValue()
            this.commandSuccess(data)
        } else {
            this.commandError("read description failed")
        }
        commandCompleted()
    }

    fun onDescriptorWrite(
        gatt: BluetoothGatt,
        descriptor: BluetoothGattDescriptor, status: Int
    ) {
        super.onDescriptorWrite(gatt, descriptor, status)
        cancelCommandTimeoutTask()
        Log.e("tag", "gatt=$gatt descriptor=$descriptor status=$status")
        Log.e("tag", descriptor.getCharacteristic().getUuid().toString())
        if (status == BluetoothGatt.GATT_SUCCESS) {
            this.commandSuccess(null)
        } else {
            this.commandError("write description failed")
        }
        commandCompleted()
    }

    fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        super.onServicesDiscovered(gatt, status)
        if (status == BluetoothGatt.GATT_SUCCESS) {
//            setNotification(gatt);
            val services: List<BluetoothGattService?> = gatt.getServices()
            mServices = services
            this.onServicesDiscovered(services)
            TelinkLog.d("Service discovery success:" + services.size)
        } else {
            TelinkLog.d("Service discovery failed")
            disconnect()
        }
    }

    //    public void setNotification(BluetoothGatt mBluetoothGatt) {
    //        BluetoothGattService service = mBluetoothGatt.getService(Device.SERVICE_UUID);
    //        if (service != null) {
    //            List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();
    //            if (gattCharacteristics != null && gattCharacteristics.size() > 0) {
    //                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
    //                    LogUtil.d(gattCharacteristic.getUuid().toString(), true);
    //                    if (gattCharacteristic.getUuid().toString().equals(Device.UUID_WRITE_STRING)) {
    //                        Log.e("tag", "UUID_WRITE_STRING");
    //                        gatt.setCharacteristicNotification(gattCharacteristic, true);
    //                        BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(Device.UUID_HEART_RATE_MEASUREMENT);
    //                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
    //                        if (gatt.writeDescriptor(descriptor)) {
    //                            LogUtil.d("writeDescriptor successed", true);
    //                        } else {
    //                            LogUtil.d("writeDescriptor failed", true);
    //                        }
    //                    }
    //
    //                }
    //            } else {
    //                Log.e("tag", "gattCharacteristics is null");
    //            }
    //        } else {
    //            Log.e("tag", "service is null");
    //        }
    //    }
    fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
        super.onReadRemoteRssi(gatt, rssi, status)
        if (status == BluetoothGatt.GATT_SUCCESS) {
            if (rssi != this.rssi) {
                this.rssi = rssi
                onRssiChanged()
            }
        }
    }

    fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
        super.onMtuChanged(gatt, mtu, status)
        TelinkLog.d("mtu changed : $mtu")
    }

    fun onReliableWriteCompleted(gatt: BluetoothGatt?, status: Int) {
        super.onReliableWriteCompleted(gatt, status)
    }

    private inner class CommandContext(var callback: Callback?, var command: Command?) {
        fun clear() {
            command = null
            callback = null
        }
    }

    private inner class RssiUpdateRunnable : Runnable {
        override fun run() {
            if (!monitorRssi) return
            if (!isConnected()) return
            if (gatt != null) gatt.readRemoteRssi()
            mRssiUpdateHandler.postDelayed(mRssiUpdateRunnable, updateIntervalMill)
        }
    }

    private inner class CommandTimeoutRunnable : Runnable {
        override fun run() {
            synchronized(mOutputCommandQueue) {
                val commandContext = mOutputCommandQueue.peek()
                if (commandContext != null) {
                    val command = commandContext.command
                    val callback = commandContext.callback
                    val retry = commandTimeout(commandContext)
                    if (retry) {
                        commandContext.command = command
                        commandContext.callback = callback
                        processCommand(commandContext)
                    } else {
                        mOutputCommandQueue.poll()
                        commandCompleted()
                    }
                }
            }
        }
    }

    private inner class CommandDelayRunnable : Runnable {
        override fun run() {
            synchronized(mOutputCommandQueue) {
                val commandContext = mOutputCommandQueue.peek()
                processCommand(commandContext)
            }
        }
    }

    companion object {
        const val CONNECTION_PRIORITY_BALANCED = 0
        const val CONNECTION_PRIORITY_HIGH = 1
        const val CONNECTION_PRIORITY_LOW_POWER = 2
        private const val CONN_STATE_IDLE = 1
        private const val CONN_STATE_CONNECTING = 2
        private const val CONN_STATE_CONNECTED = 4
        private const val CONN_STATE_DISCONNECTING = 8
        private const val CONN_STATE_CLOSED = 16
        private const val RSSI_UPDATE_TIME_INTERVAL = 2000
    }
}