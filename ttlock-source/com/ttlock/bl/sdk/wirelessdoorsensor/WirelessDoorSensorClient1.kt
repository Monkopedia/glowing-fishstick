package com.ttlock.bl.sdk.wirelessdoorsensor

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.util.Context
import android.util.TextUtils
import com.ttlock.bl.sdk.api.EncryptionUtil
import com.ttlock.bl.sdk.base.BaseClient
import com.ttlock.bl.sdk.device.WirelessDoorSensor
import com.ttlock.bl.sdk.entity.LockData
import com.ttlock.bl.sdk.util.LogUtil
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.EnterDfuCallback
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.InitDoorSensorCallback
import com.ttlock.bl.sdk.wirelessdoorsensor.callback.ScanWirelessDoorSensorCallback
import com.ttlock.bl.sdk.wirelessdoorsensor.model.ConnectParam
import com.ttlock.bl.sdk.wirelessdoorsensor.model.DoorSensorError
import com.ttlock.bl.sdk.wirelessdoorsensor.model.OperationType

/**
 * Created by TTLock on 2021/1/25.
 */
class WirelessDoorSensorClient private constructor() : BaseClient<WirelessDoorSensorSDKApi?>() {
    //    private WirelessDoorSensorSDKApi mApi;
    init {
        mApi = WirelessDoorSensorSDKApi()
    }

    private object InstanceHolder {
        val mInstance = WirelessDoorSensorClient()
    }

    fun startScan(callback: ScanWirelessDoorSensorCallback) {
//        Wirelessdoor.getInstance().setScanCallback(callback);
        mApi!!.startScan(callback)
    }

    fun stopScan() {
        mApi!!.stopScan()
        //        KeyFobCallbackManager.getInstance().clearScanCallback();
    }

    // todo:临时测试用
    override fun prepareBTService(context: Context) {
        LogUtil.d("prepare service")
        //        mApi.prepareBTService(context);
        GattCallbackHelper.Companion.getInstance().prepare(context)
    }

    fun stopBTService() {
        mApi.stopBTService()
    }

    /**
     * 门磁初始化(将锁信息写入门磁内)
     * @param device
     * @param lockData
     * @param callback
     */
    fun initialize(
        device: WirelessDoorSensor?,
        lockData: String?,
        callback: InitDoorSensorCallback
    ) {
        val lockParam: LockData? = EncryptionUtil.parseLockData(lockData)
        if (lockParam == null) {
            callback.onFail(DoorSensorError.DATA_FORMAT_ERROR)
            return
        }
        if (!DoorSensorCallbackManager.Companion.getInstance()
            .isBusy(OperationType.INIT, callback)
        ) {
            val connectParam = ConnectParam()
            connectParam.setLockmac(lockParam.lockMac)
            connectParam.setLockKey(lockParam.lockKey)
            connectParam.setAesKey(lockParam.aesKeyStr)
            ConnectManager.Companion.getInstance().storeConnectParamForCallback(connectParam)
            ConnectManager.Companion.getInstance().connect2Device(device)
        }
    }

    fun enterDfu(
        doorSensorMac: String?,
        lockKey: String?,
        aeskey: String?,
        callback: EnterDfuCallback
    ) {
        if (TextUtils.isEmpty(doorSensorMac)) {
            callback.onFail(DoorSensorError.DATA_FORMAT_ERROR)
            return
        }
        val bluetoothDevice: BluetoothDevice =
            BluetoothAdapter.getDefaultAdapter().getRemoteDevice(doorSensorMac!!)
        val device = WirelessDoorSensor(bluetoothDevice)
        enterDfu(device, lockKey, aeskey, callback)
    }

    fun enterDfu(
        device: WirelessDoorSensor?,
        lockKey: String?,
        aeskey: String?,
        callback: EnterDfuCallback
    ) {
        if (TextUtils.isEmpty(lockKey) || TextUtils.isEmpty(aeskey)) {
            callback.onFail(DoorSensorError.DATA_FORMAT_ERROR)
            return
        }
        if (!DoorSensorCallbackManager.Companion.getInstance()
            .isBusy(OperationType.ENTER_DFU, callback)
        ) {
            LogUtil.d("进行连接")
            val connectParam = ConnectParam()
            connectParam.setLockKey( lockKey)
            connectParam.setAesKey( aeskey)
            ConnectManager.Companion.getInstance().storeConnectParamForCallback(connectParam)
            ConnectManager.Companion.getInstance().connect2Device(device)
        } else {
            LogUtil.d("lock is busy")
        }
    }

    /**
     * 设置门未关报警
     * @param doorSensorMac
     * @param time
     * @param callback
     */
    //    public void setDoorNotClosedWarnningTime(String doorSensorMac, int time, SetDoorNotClosedWarningTimeCallback callback) {
    //        if (TextUtils.isEmpty(doorSensorMac) || time < 0) {
    //            callback.onFail(DoorSensorError.DATA_FORMAT_ERROR);
    //            return;
    //        }
    //        if(!DoorSensorCallbackManager.getInstance().isBusy(OperationType.SET_DOOR_NOT_CLOSED_WARNING_TIME, callback)){
    //            LogUtil.d("进行连接");
    //            ConnectParam connectParam = new ConnectParam();
    //            connectParam.setDoorSensorMac(doorSensorMac);
    //            connectParam.setTime(time);
    //            ConnectManager.getInstance().storeConnectParamForCallback(connectParam);
    //            ConnectManager.getInstance().connect2Device(doorSensorMac);
    //        } else {
    //            LogUtil.d("lock is busy");
    //        }
    //
    //    }
    companion object {
        fun getDefault(): WirelessDoorSensorClient {
            return InstanceHolder.mInstance
        }
    }
}
