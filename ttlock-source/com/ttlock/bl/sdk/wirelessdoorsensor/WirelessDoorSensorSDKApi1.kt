package com.ttlock.bl.sdk.wirelessdoorsensor

import android.util.Context
import com.ttlock.bl.sdk.base.BaseSDKApi
import com.ttlock.bl.sdk.base.BaseScanManager
import com.ttlock.bl.sdk.constant.BleConstant
import com.ttlock.bl.sdk.device.WirelessDoorSensor
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.ScanWirelessDoorSensorCallback
import com.ttlock.bl.sdk.wirelessdoorsensor.command.CommandUtil

/**
 * Created by TTLock on 2019/4/24.
 */
class WirelessDoorSensorSDKApi : BaseSDKApi() {
    override fun prepareBTService(context: Context) {
        // TODO:
//        GattCallbackHelper.getInstance().prepare(context);
    }

    fun startScan(callback: ScanWirelessDoorSensorCallback) {
        BaseScanManager.Companion.getInstance()
            .startScan(BleConstant.UUID_SERVICE_Door_Sensor, callback)
    }

    fun stopScan() {
        BaseScanManager.Companion.getInstance().stopScan()
    }

    fun stopBTService() {
        GattCallbackHelper.Companion.getInstance().clear()
    }

    fun init(doorSensor: WirelessDoorSensor) {
        CommandUtil.setLock(ConnectManager.Companion.getInstance().getConnectParam()!!, doorSensor)
    }

    fun checkAdmin(doorSensor: WirelessDoorSensor) {
        CommandUtil.checkAdmin(doorSensor)
    } //    public void setDoorNotClosedWarningTime(String doorSensorMac, int time) {
    //        CommandUtil.doorNotClosedWarning(doorSensorMac, time);
    //    }
}
