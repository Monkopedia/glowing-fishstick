package com.ttlock.bl.sdk.gateway.command;

import com.ttlock.bl.sdk.gateway.api.GattCallbackHelper;
import com.ttlock.bl.sdk.gateway.model.ConfigureGatewayInfo;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.LogUtil;

import java.io.UnsupportedEncodingException;

/**
 * Created by TTLock on 2019/3/12.
 */

public class CommandUtil {

    public static void configureWifi(ConfigureGatewayInfo configureInfo) throws UnsupportedEncodingException {
            Command command = new Command(Command.COMM_CONFIGURE_WIFI);
            command.setMac(GattCallbackHelper.getInstance().getDevice().getAddress());
            byte[] ssidBytes = configureInfo.ssid.getBytes("UTF-8");
            LogUtil.d("ssid:" + DigitUtil.byteArrayToHexString(ssidBytes));
            int ssidLen = ssidBytes.length;
            int wifiPwdLen = configureInfo.wifiPwd.length();
            byte[] data = new byte[1 + ssidLen + 1 + wifiPwdLen];
            data[0] = (byte) ssidLen;
            System.arraycopy(ssidBytes, 0, data, 1, ssidLen);
            data[1 + ssidLen] = (byte) wifiPwdLen;
            System.arraycopy(configureInfo.wifiPwd.getBytes(), 0, data, 2 + ssidLen, wifiPwdLen);
            command.setData(data);
            GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    }

    public static void configureServer(ConfigureGatewayInfo configureInfo) {
        Command command = new Command(Command.COMM_CONFIGURE_SERVER);
        command.setMac(GattCallbackHelper.getInstance().getDevice().getAddress());
        int addLen = configureInfo.server.length();
        byte[] data = new byte[1 + addLen + 2];
        data[0] = (byte) addLen;
        System.arraycopy(configureInfo.server.getBytes(), 0, data, 1, addLen);
        data[1 + addLen] = (byte) (configureInfo.port >> 8);
        data[2 + addLen] = (byte) configureInfo.port;
        command.setData(data);
        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    }

    public static void configureAccount(ConfigureGatewayInfo configureInfo) {
        Command command = new Command(Command.COMM_CONFIGURE_ACCOUNT);
        command.setMac(GattCallbackHelper.getInstance().getDevice().getAddress());
        String pwd = configureInfo.getMd5UserPwd();
        int pwdLen = pwd.length();
        byte[] name = configureInfo.getCorrentNameBytes();
        int nameLen = name.length;
        byte[] data = new byte[4 + pwdLen + 4 + 4 + nameLen];

        System.arraycopy(DigitUtil.integerToByteArray(configureInfo.uid), 0, data, 0, 4);
        System.arraycopy(pwd.getBytes(), 0, data, 4, pwdLen);
        System.arraycopy(DigitUtil.integerToByteArray(configureInfo.companyId), 0, data, 4 + pwdLen, 4);
        System.arraycopy(DigitUtil.integerToByteArray(configureInfo.branchId), 0, data, 4 + pwdLen + 4, 4);
        System.arraycopy(name, 0, data, 4 + pwdLen + 4 + 4, nameLen);
        command.setData(data);
        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    }

    public static void gatewayEcho(String mac) {
        Command command = new Command(Command.COMM_ECHO);
        command.setMac(mac);
        command.setData("SCIENER".getBytes());
        GattCallbackHelper.getInstance().clearWifi();
        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    }
}
