package com.ttlock.bl.sdk.telink.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.util.Log
import com.ttlock.bl.sdk.telink.ble.Command.Callback
import com.ttlock.bl.sdk.telink.ble.Command.CommandType
import com.ttlock.bl.sdk.telink.ble.OtaError.OTA_SUCCESS
import com.ttlock.bl.sdk.telink.util.Arrays
import com.ttlock.bl.sdk.telink.util.TelinkLog
import java.util.UUID

class Device(device: BluetoothDevice, scanRecord: ByteArray?, rssi: Int) :
    Peripheral(device, scanRecord, rssi) {
    //    private BluetoothGattCharacteristic mOTAGattCharacteristic;
    var SERVICE_UUID: UUID = UUID.fromString("73631912-6965-6E65-7269-736669727374")
    private val mOtaParser: OtaPacketParser? = OtaPacketParser()
    private val mOtaCallback = OtaCommandCallback()
    private val mCharacteristicCommandCallback = CharacteristicCommandCallback()
    private var mDeviceStateCallback: DeviceStateCallback? = null
    private val mGattOperationCallback: GattOperationCallback? = null
    private val mDescriptorCallback: DescriptorCallback? = null
    fun setDeviceStateCallback(callback: DeviceStateCallback?) {
        mDeviceStateCallback = callback
    }

    override fun onConnect() {
        super.onConnect()
        if (mDeviceStateCallback != null) {
            mDeviceStateCallback!!.onConnected(this)
        }
    }

    override fun onDisconnect() {
        super.onDisconnect()
        resetOta()
        if (mDeviceStateCallback != null) {
            mDeviceStateCallback!!.onDisconnected(this)
        }
    }

    override fun onServicesDiscovered(services: List<BluetoothGattService>) {
        super.onServicesDiscovered(services)
        //        this.enablePcmNotification();
        if (mDeviceStateCallback != null) {
            mDeviceStateCallback!!.onServicesDiscovered(this, services)
        }
    }

    override fun onNotify(
        data: ByteArray?,
        serviceUUID: UUID?,
        characteristicUUID: UUID?,
        tag: Any?
    ) {
        super.onNotify(data, serviceUUID, characteristicUUID, tag)
        Log.d(TAG, " onNotify ==> " + Arrays.bytesToHexString(data, ":"))
        if (data != null && data.size >= 3) {
            if (data[1] == (OTA_RESULT shr 8 and 0xff).toByte() && data[0].toInt() == OTA_RESULT and 0xff) {
                if (OtaError.Companion.getInstance(data[2].toInt()) == OTA_SUCCESS) {
                    resetOta()
                    onOtaSuccess()
                } else {
                    resetOta()
                    onOtaFailure()
                }
            } else {
                resetOta()
                onOtaFailure()
            }
        } else {
            resetOta()
            onOtaFailure()
        }
        if (mGattOperationCallback != null) mGattOperationCallback.onNotify(
            data,
            serviceUUID,
            characteristicUUID,
            tag
        ) else {
            Log.e("tag", "mGattOperationCallback is null")
        }
    }

    override fun onEnableNotify() {
        if (mGattOperationCallback != null) {
            mGattOperationCallback.onEnableNotify()
        }
    }

    override fun onDisableNotify() {
        if (mGattOperationCallback != null) {
            mGattOperationCallback.onDisableNotify()
        }
    }

    protected fun onOtaSuccess() {
        if (mDeviceStateCallback != null) {
            mDeviceStateCallback!!.onOtaStateChanged(this, STATE_SUCCESS)
        }
    }

    protected fun onOtaFailure() {
        if (mDeviceStateCallback != null) {
            mDeviceStateCallback!!.onOtaStateChanged(this, STATE_FAILURE)
        }
    }

    protected fun onOtaProgress() {
        if (mDeviceStateCallback != null) {
            mDeviceStateCallback!!.onOtaStateChanged(this, STATE_PROGRESS)
        }
    }

    /********************************************************************************
     * OTA API
     */
    fun startOta(firmware: ByteArray?) {
        TelinkLog.d("Start OTA")
        resetOta()
        mOtaParser!!.set(firmware)
        this.notificationToggle()
        //        this.sendOtaStartCommand();
        sendOTAPrepareCommand()
    }

    fun getOtaProgress(): Int {
        return mOtaParser!!.getProgress()
    }

    private fun resetOta() {
        mDelayHandler.removeCallbacksAndMessages(null)
        mOtaParser!!.clear()
    }

    private fun setOtaProgressChanged() {
        if (mOtaParser!!.invalidateProgress()) {
            onOtaProgress()
        }
    }

    private fun sendOTAPrepareCommand() {
        val prepareCmd: Command = Command.Companion.newInstance()
        prepareCmd.serviceUUID = SERVICE_UUID
        prepareCmd.characteristicUUID = CHARACTERISTIC_UUID_WRITE
        prepareCmd.type = CommandType.WRITE_NO_RESPONSE
        prepareCmd.tag = TAG_OTA_PREPARE
        prepareCmd.data =
            byteArrayOf((OTA_PREPARE and 0xFF).toByte(), (OTA_PREPARE shr 8 and 0xFF).toByte())
        sendCommand(mOtaCallback, prepareCmd)
    }

    // OTA 开始时发送的命令
    private fun sendOtaStartCommand() {
        val startCmd: Command = Command.Companion.newInstance()
        startCmd.serviceUUID = SERVICE_UUID
        startCmd.characteristicUUID = CHARACTERISTIC_UUID_WRITE
        startCmd.type = CommandType.WRITE_NO_RESPONSE
        startCmd.tag = TAG_OTA_START
        startCmd.data =
            byteArrayOf((OTA_START and 0xFF).toByte(), (OTA_START shr 8 and 0xFF).toByte())
        sendCommand(mOtaCallback, startCmd)
    }

    private fun sendOtaEndCommand() {
        val endCmd: Command = Command.Companion.newInstance()
        endCmd.serviceUUID = SERVICE_UUID
        endCmd.characteristicUUID = CHARACTERISTIC_UUID_WRITE
        endCmd.type = CommandType.WRITE_NO_RESPONSE
        endCmd.tag = TAG_OTA_END
        val index = mOtaParser!!.index
        /*endCmd.data = new byte[]{OTA_END & 0xFF, (byte) (OTA_END >> 8 & 0xFF),
                (byte) (index & 0xFF), (byte) (index >> 8 & 0xFF),
                (byte) (~index & 0xFF), (byte) (~index >> 8 & 0xFF)
        };*/
        val data = ByteArray(8)
        data[0] = (OTA_END and 0xFF).toByte()
        data[1] = (OTA_END shr 8 and 0xFF).toByte()
        data[2] = (index and 0xFF).toByte()
        data[3] = (index shr 8 and 0xFF).toByte()
        data[4] = (index.inv() and 0xFF).toByte()
        data[5] = (index.inv() shr 8 and 0xFF).toByte()
        val crc = mOtaParser.crc16(data)
        mOtaParser.fillCrc(data, crc)
        endCmd.data = data
        sendCommand(mOtaCallback, endCmd)
    }

    private fun sendNextOtaPacketCommand(delay: Int): Boolean {
        var result = false
        if (mOtaParser!!.hasNextPacket()) {
            val cmd: Command = Command.Companion.newInstance()
            cmd.serviceUUID = SERVICE_UUID
            cmd.characteristicUUID = CHARACTERISTIC_UUID_WRITE
            cmd.type = CommandType.WRITE_NO_RESPONSE
            cmd.data = mOtaParser.getNextPacket()
            if (mOtaParser.isLast()) {
                cmd.tag = TAG_OTA_LAST
                result = true
            } else {
                cmd.tag = TAG_OTA_WRITE
            }
            cmd.delay = delay
            sendCommand(mOtaCallback, cmd)
            /*if (this.mOtaParser.isLast()) {
                TelinkLog.d("ota last packet");
                result = true;
                //cmd.tag = TAG_OTA_LAST;
                Command end = Command.newInstance();
                end.serviceUUID = mOTAGattCharacteristic.getService().getUuid();
                end.characteristicUUID = mOTAGattCharacteristic.getUuid();
                end.type = Command.CommandType.WRITE_NO_RESPONSE;
                end.tag = TAG_OTA_LAST;
                end.delay = 0;
                byte[] endPacket = new byte[6];
                endPacket[0] = 0x02;
                endPacket[1] = (byte) 0xFF;
                endPacket[2] = cmd.data[0];
                endPacket[3] = cmd.data[1];
                endPacket[4] = (byte) (0xFF - cmd.data[0]);
                endPacket[5] = (byte) (0xFF - cmd.data[1]);
                end.data = endPacket;
                this.sendCommand(this.mOtaCallback, cmd);
                this.sendCommand(this.mOtaCallback, end);
            } else {
                this.sendCommand(this.mOtaCallback, cmd);
            }*/
        }
        return result
    }

    private fun validateOta(): Boolean {
        /**
         * 发送read指令
         */
        val sectionSize = 16 * 8
        val sendTotal = mOtaParser!!.getNextPacketIndex() * 16
        TelinkLog.i("ota onCommandSampled byte length : $sendTotal")
        if (sendTotal > 0 && sendTotal % sectionSize == 0) {
            TelinkLog.i("onCommandSampled ota read packet " + mOtaParser.getNextPacketIndex())
            val cmd: Command = Command.Companion.newInstance()
            cmd.serviceUUID = SERVICE_UUID
            cmd.characteristicUUID = CHARACTERISTIC_UUID_WRITE
            cmd.type = CommandType.READ
            cmd.tag = TAG_OTA_READ
            sendCommand(mOtaCallback, cmd)
            return true
        }
        return false
    }

    fun isNotificationEnable(characteristic: BluetoothGattCharacteristic): Boolean {
        val key = generateHashKey(
            characteristic.getService().getUuid(),
            characteristic
        )
        return mNotificationCallbacks.containsKey(key)
    }

    fun notificationToggle(characteristic: BluetoothGattCharacteristic) {
        val cmd: Command = Command.Companion.newInstance()
        cmd.serviceUUID = characteristic.getService().getUuid()
        cmd.characteristicUUID = characteristic.getUuid()
        cmd.type =
            if (!isNotificationEnable(characteristic)) CommandType.ENABLE_NOTIFY else CommandType.DISABLE_NOTIFY
        cmd.tag = TAG_GENERAL_ENABLE_NOTIFICATION
        sendCommand(mCharacteristicCommandCallback, cmd)
    }

    fun notificationToggle() {
        val cmd: Command = Command.Companion.newInstance()
        cmd.serviceUUID = SERVICE_UUID
        cmd.characteristicUUID = CHARACTERISTIC_UUID_WRITE
        cmd.type = CommandType.ENABLE_NOTIFY
        cmd.tag = TAG_GENERAL_ENABLE_NOTIFICATION
        sendCommand(mCharacteristicCommandCallback, cmd)
    }

    /**
     * 通用read方法
     *
     * @param characteristic
     */
    fun sendGeneralReadCommand(characteristic: BluetoothGattCharacteristic) {
        val cmd: Command = Command.Companion.newInstance()
        cmd.serviceUUID = characteristic.getService().getUuid()
        cmd.characteristicUUID = characteristic.getUuid()
        cmd.type = CommandType.READ
        cmd.tag = TAG_GENERAL_READ
        sendCommand(mCharacteristicCommandCallback, cmd)
    }

    fun sendGeneralWriteCommand(characteristic: BluetoothGattCharacteristic, data: ByteArray?) {
        val cmd: Command = Command.Companion.newInstance()
        cmd.serviceUUID = characteristic.getService().getUuid()
        cmd.characteristicUUID = characteristic.getUuid()
        cmd.type = CommandType.WRITE_NO_RESPONSE
        cmd.tag = TAG_GENERAL_WRITE
        cmd.data = data
        sendCommand(mCharacteristicCommandCallback, cmd)
    }

    fun sendDescriptorReadCommand(characteristic: BluetoothGattCharacteristic) {
        for (descriptor in characteristic.getDescriptors()) {
            val cmd: Command = Command.Companion.newInstance()
            cmd.serviceUUID = characteristic.getService().getUuid()
            cmd.characteristicUUID = characteristic.getUuid()
            cmd.descriptorUUID = descriptor.getUuid()
            cmd.type = CommandType.READ_DESCRIPTOR
            cmd.tag = TAG_GENERAL_READ_DESCRIPTOR
            sendCommand(mCharacteristicCommandCallback, cmd)
        }
    }

    interface DeviceStateCallback {
        fun onConnected(device: Device)
        fun onDisconnected(device: Device)
        fun onServicesDiscovered(device: Device, services: List<BluetoothGattService>)
        fun onOtaStateChanged(device: Device, state: Int)
    }

    interface GattOperationCallback {
        /**
         * characteristic read callback
         */
        fun onRead(command: Command?, obj: Any?)

        /**
         * characteristic write callback
         */
        fun onWrite(command: Command?, obj: Any?)

        /**
         * characteristic read callback
         */
        fun onNotify(data: ByteArray?, serviceUUID: UUID?, characteristicUUID: UUID?, tag: Any?)
        fun onEnableNotify()
        fun onDisableNotify()
    }

    interface DescriptorCallback {
        fun onDescriptorRead(command: Command?, obj: Any?)
    }

    private inner class CharacteristicCommandCallback : Callback {
        override fun success(peripheral: Peripheral?, command: Command, obj: Any?) {
            TelinkLog.i("CharacteristicCommandCallback success")
            when (command.type) {
                CommandType.READ -> mGattOperationCallback?.onRead(command, obj)
                CommandType.READ_DESCRIPTOR -> mDescriptorCallback?.onDescriptorRead(command, obj)
                CommandType.WRITE -> mGattOperationCallback?.onWrite(command, obj)
                CommandType.WRITE_NO_RESPONSE -> mGattOperationCallback?.onWrite(command, obj)
                CommandType.ENABLE_NOTIFY -> mGattOperationCallback?.onEnableNotify()
                CommandType.DISABLE_NOTIFY -> mGattOperationCallback?.onDisableNotify()
            }
        }

        override fun error(peripheral: Peripheral?, command: Command, errorMsg: String) {
            TelinkLog.i("CharacteristicCommandCallback success")
        }

        override fun timeout(peripheral: Peripheral?, command: Command): Boolean {
            TelinkLog.i("CharacteristicCommandCallback success")
            return false
        }
    }

    private inner class OtaCommandCallback : Callback {
        override fun success(peripheral: Peripheral?, command: Command, obj: Any?) {
            if (command.tag == TAG_OTA_PRE_READ) {
                TelinkLog.d("read =========> " + Arrays.bytesToHexString(obj as ByteArray?, "-"))
            } else if (command.tag == TAG_OTA_PREPARE) {
                sendOtaStartCommand()
            } else if (command.tag == TAG_OTA_START) {
                sendNextOtaPacketCommand(0)
            } else if (command.tag == TAG_OTA_END) {
                Log.e("tag", "TAG_OTA_END")
                setOtaProgressChanged()
                // ota success
//                resetOta();
//                setOtaProgressChanged();
//                onOtaSuccess();
            } else if (command.tag == TAG_OTA_LAST) {
//                sendLastReadCommand();
                sendOtaEndCommand()
                // OTA测试时无需发后面两个指令
                /*resetOta();
                setOtaProgressChanged();
                onOtaSuccess();*/
            } else if (command.tag == TAG_OTA_WRITE) {
//                Log.e("tag", "TAG_OTA_WRITE");
                // int delay = 0;
                // if (delay <= 0) {
                /*if (!validateOta()) {
                    sendNextOtaPacketCommand(0);
                } else {
                    sendNextOtaPacketCommand(20);
//                    mDelayHandler.postDelayed(mOtaTask, delay);
                }*/
                /*if (!validateOta()) {
                    sendNextOtaPacketCommand(0);
                } else {

                    sendNextOtaPacketCommand(DELAY_PERIOD);
                }*/
//                if (!validateOta()) {
                sendNextOtaPacketCommand(0)
                //                }
                setOtaProgressChanged()
            } else if (command.tag == TAG_OTA_READ) {
                sendNextOtaPacketCommand(0)
            } else if (command.tag == TAG_OTA_LAST_READ) {
//                sendOtaEndCommand();
            }
        }

        override fun error(peripheral: Peripheral?, command: Command, errorMsg: String) {
            TelinkLog.e("error packet : " + command.tag + " errorMsg : " + errorMsg)
            //            if (command.tag.equals(TAG_OTA_END)) {
//                // ota success
//                resetOta();
//                setOtaProgressChanged();
//                onOtaSuccess();
//            } else {
            resetOta()
            onOtaFailure()
            //            }
        }

        override fun timeout(peripheral: Peripheral?, command: Command): Boolean {
            TelinkLog.e("timeout : " + Arrays.bytesToHexString(command.data, ":"))
            //            if (command.tag.equals(TAG_OTA_END)) {
//                // ota success
//                resetOta();
//                setOtaProgressChanged();
//                onOtaSuccess();
//            } else {
            resetOta()
            onOtaFailure()
            //            }
            return false
        }
    }

    fun getIndex(): Int {
        return mOtaParser?.index ?: 0
    }

    fun getTotal(): Int {
        return mOtaParser?.getTotal() ?: 0
    }

    companion object {
        val TAG = Device::class.java.simpleName
        const val UUID_WRITE_STRING = "00010203-0405-0607-0809-0a0b0c0d2b12"
        val CHARACTERISTIC_UUID_WRITE: UUID =
            UUID.fromString("73632B12-6965-6E65-7269-736669727374")
        val UUID_HEART_RATE_MEASUREMENT: UUID =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        private const val DELAY_PERIOD = 20
        const val OTA_PREPARE = 0xFF00
        const val OTA_START = 0xFF01
        const val OTA_END = 0xFF02

        /**
         * 自定义 OTA结果指令
         */
        const val OTA_RESULT = 0xFF03
        const val STATE_SUCCESS = 1
        const val STATE_FAILURE = 0
        const val STATE_PROGRESS = 2
        private const val TAG_OTA_WRITE = 1
        private const val TAG_OTA_READ = 2
        private const val TAG_OTA_LAST = 3
        private const val TAG_OTA_LAST_READ = 10
        private const val TAG_OTA_PRE_READ = 4
        private const val TAG_OTA_PREPARE = 5 // prepare
        private const val TAG_OTA_START = 7
        private const val TAG_OTA_END = 8
        private const val TAG_OTA_ENABLE_NOTIFICATION = 9
        private const val TAG_GENERAL_READ = 11
        private const val TAG_GENERAL_WRITE = 12
        private const val TAG_GENERAL_READ_DESCRIPTOR = 13
        private const val TAG_GENERAL_ENABLE_NOTIFICATION = 14
    }
}
