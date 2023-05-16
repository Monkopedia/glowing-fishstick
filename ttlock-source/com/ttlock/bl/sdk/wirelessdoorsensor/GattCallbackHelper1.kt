package com.ttlock.bl.sdk.wirelessdoorsensor

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.util.Log
import com.ttlock.bl.sdk.base.BaseGattCallbackHelper
import com.ttlock.bl.sdk.device.WirelessDoorSensor
import com.ttlock.bl.sdk.util.DigitUtil
import com.ttlock.bl.sdk.util.LogUtil
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.ConnectCallback
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.DoorSensorCallback
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.EnterDfuCallback
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.InitDoorSensorCallback
import com.ttlock.bl.sdk.wirelessdoorsensor.command.Command
import com.ttlock.bl.sdk.wirelessdoorsensor.command.CommandUtil
import com.ttlock.bl.sdk.wirelessdoorsensor.model.DoorSensorError
import com.ttlock.bl.sdk.wirelessdoorsensor.model.InitDoorSensorResult
import java.util.*

/**
 * Created by TTLock on 2019/3/11.
 */
class GattCallbackHelper : BaseGattCallbackHelper<WirelessDoorSensor?>() {
    // TODO:
    private var isInitSuccess = false
    protected override fun noResponseCallback() {
        DoorSensorCallbackManager.getInstance().getCallback()?.onFail(DoorSensorError.NO_RESPONSE)
    }

    protected override fun disconnectedCallback() {
        mAppExecutor.mainThread().execute(
            Runnable {
                val mConnectCallback: ConnectCallback? =
                    DoorSensorCallbackManager.Companion.getInstance().getConnectCallback()
                if (mConnectCallback != null) {
                    Log.d("OMG", "====disconnect==1==$isInitSuccess")
                    mConnectCallback.onFail(DoorSensorError.CONNECT_FAIL)
                }
                isInitSuccess = false
            }
        )
    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt,
        characteristic: BluetoothGattCharacteristic,
        status: Int
    ) {
        super.onCharacteristicRead(gatt, characteristic, status)
        LogUtil.d("gatt=$gatt characteristic=$characteristic status=$status", DBG)
    }

    protected override fun connectCallback() {
        mAppExecutor.mainThread().execute(
            Runnable {
                val mConnectCallback: ConnectCallback? =
                    DoorSensorCallbackManager.Companion.getInstance().getConnectCallback()
                if (mConnectCallback != null) {
                    Log.d("OMG", "====connect success==1==")
                    mConnectCallback.onConnectSuccess(device)
                }
            }
        )
    }

    override fun doWithData(values: ByteArray) {
        mAppExecutor.mainThread().execute(
            Runnable {
                val command = Command(values)
                command.mac = device!!.getAddress()
                val data = command.getData()
                LogUtil.d("command:" + DigitUtil.byteToHex(command.getCommand()))
                LogUtil.d("data:" + DigitUtil.byteArrayToHexString(data))
                if (data == null) {
                    LogUtil.d("data is null")
                    return@Runnable
                }
                if (data[1].toInt() == 1) { // 成功
                    when (data[0]) {
                        Command.Companion.COMM_SET_LOCK -> {
                            val initDoorSensorResult = InitDoorSensorResult()
                            initDoorSensorResult.setBatteryLevel(data[2].toInt())
                            initDoorSensorResult.setFirmwareInfo(firmwareInfo)
                            val callback: DoorSensorCallback? =
                                DoorSensorCallbackManager.Companion.getInstance().getCallback()
                            if (callback != null) {
                                (callback as InitDoorSensorCallback).onInitSuccess(initDoorSensorResult)
                            }
                            // 成功之后主动断开连接
                            disconnect()
                        }
                        Command.Companion.COMM_CHECK_ADMIN -> {
                            val responseRandom: Long =
                                DigitUtil.bytesToLong(Arrays.copyOfRange(data, 2, data.size))
                            CommandUtil.checkRandom(
                                device!!,
                                ConnectManager.Companion.getInstance().getConnectParam()!!,
                                responseRandom
                            )
                        }
                        Command.Companion.COMM_CHECK_RANDOM -> CommandUtil.enterDfu(device!!)
                        Command.Companion.COMM_ENTER_DFU -> {
                            val callback = DoorSensorCallbackManager.Companion.getInstance().getCallback()
                            if (callback != null) {
                                (callback as EnterDfuCallback).onEnterDfuSuccess()
                            }
                        }
                    }
                } else {
                    val callback: DoorSensorCallback? =
                        DoorSensorCallbackManager.Companion.getInstance().getCallback()
                    if (callback != null) {
                        callback.onFail(DoorSensorError.FAILED)
                    }
                }
            }
        )
    }

    companion object {
        private val instance = GattCallbackHelper()
        fun getInstance(): GattCallbackHelper {
            return instance
        }
    }
}
