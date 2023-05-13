package com.ttlock.bl.sdk.gateway.command

import com.ttlock.bl.sdk.gateway.api.GattCallbackHelper
import com.ttlock.bl.sdk.gateway.model.ConfigureGatewayInfo
import com.ttlock.bl.sdk.util.DigitUtil
import com.ttlock.bl.sdk.util.LogUtil
import java.io.UnsupportedEncodingException
import kotlin.Throws

/**
 * Created by TTLock on 2019/3/12.
 */
object CommandUtil {
    @Throws(UnsupportedEncodingException::class)
    fun configureWifi(configureInfo: ConfigureGatewayInfo) {
        val command: Command = Command(Command.Companion.COMM_CONFIGURE_WIFI)
        command.mac = GattCallbackHelper.Companion.getInstance().getDevice().getAddress()
        val ssidBytes = configureInfo.ssid.toByteArray(charset("UTF-8"))
        LogUtil.d("ssid:" + DigitUtil.byteArrayToHexString(ssidBytes))
        val ssidLen = ssidBytes.size
        val wifiPwdLen = configureInfo.wifiPwd.length
        val data = ByteArray(1 + ssidLen + 1 + wifiPwdLen)
        data[0] = ssidLen.toByte()
        System.arraycopy(ssidBytes, 0, data, 1, ssidLen)
        data[1 + ssidLen] = wifiPwdLen.toByte()
        System.arraycopy(configureInfo.wifiPwd.toByteArray(), 0, data, 2 + ssidLen, wifiPwdLen)
        command.setData(data)
        GattCallbackHelper.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun configureServer(configureInfo: ConfigureGatewayInfo) {
        val command: Command = Command(Command.Companion.COMM_CONFIGURE_SERVER)
        command.mac = GattCallbackHelper.Companion.getInstance().getDevice().getAddress()
        val addLen = configureInfo.server.length
        val data = ByteArray(1 + addLen + 2)
        data[0] = addLen.toByte()
        System.arraycopy(configureInfo.server.toByteArray(), 0, data, 1, addLen)
        data[1 + addLen] = (configureInfo.port shr 8).toByte()
        data[2 + addLen] = configureInfo.port.toByte()
        command.setData(data)
        GattCallbackHelper.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun configureAccount(configureInfo: ConfigureGatewayInfo) {
        val command: Command = Command(Command.Companion.COMM_CONFIGURE_ACCOUNT)
        command.mac = GattCallbackHelper.Companion.getInstance().getDevice().getAddress()
        val pwd = configureInfo.md5UserPwd
        val pwdLen = pwd.length
        val name = configureInfo.correntNameBytes
        val nameLen = name.size
        val data = ByteArray(4 + pwdLen + 4 + 4 + nameLen)
        System.arraycopy(DigitUtil.integerToByteArray(configureInfo.uid), 0, data, 0, 4)
        System.arraycopy(pwd.toByteArray(), 0, data, 4, pwdLen)
        System.arraycopy(
            DigitUtil.integerToByteArray(configureInfo.companyId),
            0,
            data,
            4 + pwdLen,
            4
        )
        System.arraycopy(
            DigitUtil.integerToByteArray(configureInfo.branchId),
            0,
            data,
            4 + pwdLen + 4,
            4
        )
        System.arraycopy(name, 0, data, 4 + pwdLen + 4 + 4, nameLen)
        command.setData(data)
        GattCallbackHelper.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun gatewayEcho(mac: String?) {
        val command: Command = Command(Command.Companion.COMM_ECHO)
        command.mac = mac
        command.setData("SCIENER".toByteArray())
        GattCallbackHelper.Companion.getInstance().clearWifi()
        GattCallbackHelper.Companion.getInstance().sendCommand(command.buildCommand())
    }
}
