package com.ttlock.bl.sdk.gateway.api

import com.ttlock.bl.sdk.gateway.callback.ConfigIpCallback
import com.ttlock.bl.sdk.gateway.callback.ConnectCallback
import com.ttlock.bl.sdk.gateway.callback.EnterDfuCallback
import com.ttlock.bl.sdk.gateway.model.ConnectParam
import com.ttlock.bl.sdk.gateway.model.OperationType
import android.util.Context
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice
import com.ttlock.bl.sdk.entity.IpSetting
import com.ttlock.bl.sdk.gateway.callback.GetNetworkMacCallback
import com.ttlock.bl.sdk.gateway.callback.InitGatewayCallback
import com.ttlock.bl.sdk.gateway.callback.ScanGatewayCallback
import com.ttlock.bl.sdk.gateway.callback.ScanWiFiByGatewayCallback
import com.ttlock.bl.sdk.gateway.model.ConfigureGatewayInfo
import com.ttlock.bl.sdk.gateway.model.GatewayError

/**
 * Created by TTLock on 2019/4/24.
 */
class GatewayClient private constructor() {
    private val mApi: GatewaySDKApi

    init {
        mApi = GatewaySDKApi()
    }

    private object InstanceHolder {
        val mInstance = GatewayClient()
    }

    fun isBLEEnabled(context: Context): Boolean {
        return mApi.isBLEEnabled(context)
    }

    fun prepareBTService(context: Context?) {
        mApi.prepareBTService(context)
    }

    fun startScanGateway(callback: ScanGatewayCallback?) { // TODO:
//        GatewayCallbackManager.getInstance().setGatewayScanCallback(callback);
        mApi.startScanGateway(callback)
    }

    fun stopScanGateway() {
        mApi.stopScanGateway()
        //        GatewayCallbackManager.getInstance().clearScanCallback();
    }

    fun connectGateway(mac: String, connectCallback: ConnectCallback?) {
        ConnectManager.Companion.getInstance().connect2Device(mac, connectCallback)
    }

    fun connectGateway(
        extendedBluetoothDevice: ExtendedBluetoothDevice,
        connectCallback: ConnectCallback?
    ) {
        ConnectManager.Companion.getInstance()
            .connect2Device(extendedBluetoothDevice, connectCallback)
    }

    fun disconnectGateway() {
        ConnectManager.Companion.getInstance().disconnect()
    }

    fun initGateway(configureInfo: ConfigureGatewayInfo, callback: InitGatewayCallback) {
        if (ConnectManager.Companion.getInstance().isDeviceConnected()) {
            GatewayCallbackManager.Companion.getInstance()
                .isGatewayBusy(OperationType.INIT_GATEWAY, callback)
            mApi.initializeGateway(configureInfo, callback)
        } else {
            callback.onFail(GatewayError.UNCONNECTED)
        }
    }

    fun enterDfu(mac: String, callback: EnterDfuCallback) {
        GatewayCallbackManager.Companion.getInstance()
            .isGatewayBusy(OperationType.ENTER_DFU_MODE, callback)
        if (ConnectManager.Companion.getInstance().isDeviceConnected()) {
            mApi.enterDfu(mac)
        } else {
            val connectParam = ConnectParam()
            connectParam.mac = mac
            ConnectManager.Companion.getInstance().storeConnectParamForCallback(connectParam)
            ConnectManager.Companion.getInstance().connect2Device(mac)
        }
    }

    fun scanWiFiByGateway(mac: String?, callback: ScanWiFiByGatewayCallback) {
        if (ConnectManager.Companion.getInstance().isDeviceConnected()) {
            GatewayCallbackManager.Companion.getInstance()
                .isGatewayBusy(OperationType.SCAN_WIFI, callback)
            mApi.scanWiFiByGateway(mac)
        } else {
            callback.onFail(GatewayError.UNCONNECTED)
        }
    }

    fun configIp(mac: String?, ipSetting: IpSetting?, callback: ConfigIpCallback) {
        if (ConnectManager.Companion.getInstance().isDeviceConnected()) {
            GatewayCallbackManager.Companion.getInstance()
                .isGatewayBusy(OperationType.CONFIG_IP, callback)
            mApi.configIp(mac, ipSetting, callback)
        } else {
            callback.onFail(GatewayError.UNCONNECTED)
        }
    }

    /**
     * 获取网关的网络mac(只有锁处于连接状态下才可获取)
     * @param callback
     */
    fun getNetworkMac(callback: GetNetworkMacCallback) {
        if (ConnectManager.Companion.getInstance().isDeviceConnected()) {
            callback.onGetNetworkMacSuccess(
                GattCallbackHelper.Companion.getInstance().getNetworkMac()
            )
        } else {
            callback.onFail(GatewayError.UNCONNECTED)
        }
    }

    companion object {
        const val REQUEST_ENABLE_BT = 1
        fun getDefault(): GatewayClient {
            return InstanceHolder.mInstance
        }
    }
}
