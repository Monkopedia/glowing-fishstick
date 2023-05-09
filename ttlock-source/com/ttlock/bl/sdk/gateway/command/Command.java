package com.ttlock.bl.sdk.gateway.command;

import com.scaf.android.client.CodecUtils;
import com.ttlock.bl.sdk.util.AESUtil;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.util.LogUtil;

import java.util.Arrays;

/**
 * Created by Smartlock on 2016/5/27.
 */
public class Command {

    private static boolean DBG = true;
    public static byte[] defaultAeskey = new byte[]{0x33, (byte) 0xA0,0x3E,0x78,0x23,0x6A,0x4D,0x53, (byte) 0x88,062,0x7A,0x32, (byte) 0xA3, (byte) 0xBB, (byte) 0xF2, (byte) 0xEF};

    /**
     * 搜索WIFI Ap
     */
    public static final byte COMM_GET_NEARBY_SSID = 0x01;

    /**
     * 配置WiFi Ap
     */
    public static final byte COMM_CONFIGURE_WIFI = 0x02;

    /**
     * 配置服务器地址
     */
    public static final byte COMM_CONFIGURE_SERVER = 0x03;

    /**
     * 配置账号
     */
    public static final byte COMM_CONFIGURE_ACCOUNT = 0x04;

    /**
     * 进入DFU升级模式
     */
    public static final byte COMM_ENTER_DFU = 0x05;

    /**
     * 静态ip
     */
    public static final byte COMM_CONFIG_IP = 0x06;

    /**
     * 保持连接时间
     */
    public static final byte COMM_ECHO = 0x45;


    public byte[] header;    // 帧首 		2 字节
    public byte command;    // 命令字 	1 字节
    public byte length;    // 长度		1 字节
    public byte[] data;    // 数据
    public byte checksum;    // 校验		1 字节

    private boolean mIsChecksumValid;

    private String mac;

    /**
     * 锁类型
     */
    private int lockType;

    public Command(byte commandType) {
        this.header = new byte[2];
        this.header[0] = 0x72;
        this.header[1] = 0x5b;
        this.command = commandType;
        data = new byte[0];
        this.length = 0;
    }

    /**
     * 指令解析
     * @param command
     */
    public Command(byte[] command) {
        this.header = new byte[2];
        this.header[0] = command[0];
        this.header[1] = command[1];
        this.command  = command[2];
        this.length = command[3];
        this.data = new byte[this.length];
        if (this.length == command.length - 5) {
            System.arraycopy(command, 4, this.data, 0, this.length);
            this.checksum = command[command.length - 1];
            byte checksum = CodecUtils.crccompute(Arrays.copyOf(command, command.length - 1));
            mIsChecksumValid = (checksum == this.checksum);
        }
    }

    public void setCommand(byte command) {
        this.command = command;
    }

    public byte getCommand() {
        return command;
    }

    public byte[] getData() {
        return getData(getAeskey());
    }

    public byte[] getAeskey() {
        String[] macArr = mac.split(":");
        macArr = reverseArray(macArr);
        byte[] macBytes = hexStringArrToByteArr(macArr);
        LogUtil.d("getAeskey macBytes:" + DigitUtil.byteArrayToHexString(macBytes));
        byte[] aeskey = AESUtil.aesEncrypt(macBytes, defaultAeskey);
        LogUtil.d("getAeskey aeskey:" + DigitUtil.byteArrayToHexString(aeskey));
        return aeskey;
    }

    public byte[] getData(byte[] aesKeyArray) {
        byte[] values;
        values = AESUtil.aesDecrypt(data, aesKeyArray);
        return values;
    }

    public void setData(byte[] data) {
        setData(data, getAeskey());
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setData(byte[] data, byte[] aesKeyArray) {
        LogUtil.d("data=" + DigitUtil.byteArrayToHexString(data), DBG);
        LogUtil.d("aesKeyArray=" + DigitUtil.byteArrayToHexString(aesKeyArray), DBG);
        this.data = AESUtil.aesEncrypt(data, aesKeyArray);
        this.length = (byte) this.data.length;
    }

    public boolean isChecksumValid() {
        return mIsChecksumValid;
    }

    public int getLockType() {
        return lockType;
    }

    public void setLockType(int lockType) {
        this.lockType = lockType;
    }

    public byte[] buildCommand() {
        byte[] commandBytes = new byte[2 + 1 + 1 + this.length + 1];
        commandBytes[0] = this.header[0];
        commandBytes[1] = this.header[1];
        commandBytes[2] = this.command;
        commandBytes[3] = this.length;
        if (this.data != null && this.data.length > 0)
            System.arraycopy(this.data, 0, commandBytes, 4, this.data.length);
        byte crc = CodecUtils.crccompute(Arrays.copyOf(commandBytes, commandBytes.length - 1));
        commandBytes[commandBytes.length - 1] = crc;
        return commandBytes;
    }

    public byte[] hexStringArrToByteArr(String[] arr) {
        if (arr == null)
            return null;
        byte[] bytes = new byte[arr.length];
        for(int i=0;i<arr.length;i++) {
            bytes[i] = Integer.valueOf(arr[i], 16).byteValue();
        }
        return bytes;
    }

    public String[] reverseArray(String[] array) {
        if (array != null) {
            int len = array.length;
            for (int i=0;i<len/2;i++) {
                String temp = array[i];
                array[i] = array[len - i -1];
                array[len - i - 1] = temp;
            }
        }
        return array;
    }
}
