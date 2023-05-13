package com.ttlock.bl.sdk.gateway.api

import com.ttlock.bl.sdk.gateway.callback.ConfigIpCallback
import android.util.Context
import com.ttlock.bl.sdk.gateway.command.Command
import com.ttlock.bl.sdk.gateway.command.CommandUtil

/**
 * Created by TTLock on 2019/4/24.
 */
internal class GatewaySDKApi {

    fun isBLEEnabled(context: Context): Boolean {
        val manager: BluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter: BluetoothAdapter = manager.getAdapter()
        return adapter != null && adapter.isEnabled()
    }

    fun requestBleEnable(activity: Activity) {
        val mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
        }
    }

    fun prepareBTService(context: Context?) {
        GattCallbackHelper.Companion.getInstance().prepare(context)
    }

    fun startScanGateway(callback: ScanGatewayCallback?) {
        ScanManager.Companion.getInstance().startScan(callback)
    }

    fun stopScanGateway() {
        ScanManager.Companion.getInstance().stopScan()
    }

    fun initializeGateway(
        @NonNull configureInfo: ConfigureGatewayInfo?,
        initGatewayCallback: InitGatewayCallback?
    ) {
        GattCallbackHelper.Companion.getInstance().setConfigureInfo(configureInfo)
        if (configureInfo == null || TextUtils.isEmpty(configureInfo.ssid)) {
            LogUtil.d("configureInfo is null")
            if (initGatewayCallback != null) {
                initGatewayCallback.onFail(GatewayError.DATA_FORMAT_ERROR)
            }
        } else {
            LogUtil.d("plugversion:" + configureInfo.plugVersion)
            try {
                if (configureInfo.plugVersion == GatewayType.G2) {
                    CommandUtil.configureWifi(configureInfo)
                } else { // G3 G4网关不需要走配置WIFI
                    CommandUtil.configureServer(configureInfo)
                }
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                if (initGatewayCallback != null) {
                    initGatewayCallback.onFail(GatewayError.DATA_FORMAT_ERROR)
                }
            }
        }
    }

    fun enterDfu(mac: String?) {
        val command: Command = Command(Command.Companion.COMM_ENTER_DFU)
        command.mac = mac
        command.setData("SCIENER".toByteArray())
        GattCallbackHelper.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun scanWiFiByGateway(mac: String?) {
        val command: Command = Command(Command.Companion.COMM_GET_NEARBY_SSID)
        command.mac = mac
        command.setData("SCIENER".toByteArray())
        GattCallbackHelper.Companion.getInstance().clearWifi()
        GattCallbackHelper.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun configIp(mac: String?, ipSetting: IpSetting?, configIpCallback: ConfigIpCallback?) {
        if (configIpCallback == null) {
            return
        }
        if (ipSetting == null) {
            LogUtil.d("ipSetting is null")
            if (configIpCallback != null) {
                configIpCallback.onFail(GatewayError.DATA_FORMAT_ERROR)
            }
        } else {
            val command: Command = Command(Command.Companion.COMM_CONFIG_IP)
            command.mac = mac
            val data = ByteArray(1 + 4 + 4 + 4 + 4 + 4) // 类型、ip、子网掩码、默认网关、首选dns服务器、备选dns服务器
            data[0] = ipSetting.getType().toByte()
            var bytes: ByteArray = GatewayUtil.convertIp2Bytes(ipSetting.getIpAddress())
            if (bytes == null) {
                configIpCallback.onFail(GatewayError.DATA_FORMAT_ERROR)
                return
            } else {
                System.arraycopy(bytes, 0, data, 1, 4)
            }
            bytes = GatewayUtil.convertIp2Bytes(ipSetting.getSubnetMask())
            if (bytes == null) {
                configIpCallback.onFail(GatewayError.DATA_FORMAT_ERROR)
                return
            } else {
                System.arraycopy(bytes, 0, data, 5, 4)
            }
            bytes = GatewayUtil.convertIp2Bytes(ipSetting.getRouter())
            if (bytes == null) {
                configIpCallback.onFail(GatewayError.DATA_FORMAT_ERROR)
                return
            } else {
                System.arraycopy(bytes, 0, data, 9, 4)
            }
            bytes = GatewayUtil.convertIp2Bytes(ipSetting.getPreferredDns())
            if (bytes == null) {
                configIpCallback.onFail(GatewayError.DATA_FORMAT_ERROR)
                return
            } else {
                System.arraycopy(bytes, 0, data, 13, 4)
            }
            bytes = GatewayUtil.convertIp2Bytes(ipSetting.getAlternateDns())
            if (bytes == null) {
                configIpCallback.onFail(GatewayError.DATA_FORMAT_ERROR)
                return
            } else {
                System.arraycopy(bytes, 0, data, 17, 4)
            }
            command.setData(data)
            GattCallbackHelper.Companion.getInstance().sendCommand(command.buildCommand())
        }
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }
}
