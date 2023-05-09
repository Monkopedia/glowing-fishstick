package com.ttlock.bl.sdk.wirelessdoorsensor.command;

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

    public static byte[] defaultAeskey = new byte[]{0x17, (byte) 0x92,0x45, (byte) 0xCD,0x45,0x23, (byte) 0x99, (byte) 0xA3, (byte) 0xDF,0x62,0x7E,0x5F, (byte) 0x87, (byte) 0x09, (byte) 0xD2, (byte) 0x49};

    public static final byte COMM_READ_FEATURE = 0x01;

    /**
     * 设置锁
     */
    public static final byte COMM_SET_LOCK = 0x6F;

    public static final byte COMM_CHECK_ADMIN = 0x41;

    public static final byte COMM_CHECK_RANDOM = 0x30;

    public static final byte COMM_ENTER_DFU = 0x02;

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

//    基本指令格式，采用目前门锁的指令格式
//    字段名称	字节数	备注
//    同步头	2	0x7F 0x5A
//    固定数据1	7	为兼容门锁协议，固定填充0x05 0x03 0x02 0x00 0x01 0x00 0x01
//    命令码	1	门锁返回的，固定为0x54
//    固定数据2	1	为兼容门锁协议，固定填充 0xA0
//    参数长度	1
//    参数	N	参数长度字段指出的字节个数
//    CRC校验	1	前面所有字段的CRC校验值
//    固定数据	2	0x0D 0x0A


    public Command(byte commandType) {
        this.header = new byte[2];
        this.header[0] = 0x7f;
        this.header[1] = 0x5a;
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
        this.command  = command[9];
        this.length = command[11];
        this.data = new byte[this.length];
        System.arraycopy(command, 12, this.data, 0, this.length);
        this.checksum = command[command.length - 1];
        byte checksum = CodecUtils.crccompute(Arrays.copyOf(command, command.length - 1));
        mIsChecksumValid = (checksum == this.checksum);
//        LogUtil.d("checksum=" + checksum + " this.checksum=" + this.checksum, DBG);
//        LogUtil.d("mIsChecksumValid : " + mIsChecksumValid, DBG);
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
        byte[] aeskey = AESUtil.aesEncrypt(macBytes, defaultAeskey);
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
        byte[] commandBytes = new byte[2 + 7 + 1 + 1 + + 1 + this.length + 1 + 2];
        commandBytes[0] = this.header[0];
        commandBytes[1] = this.header[1];

        //跟门锁保持一致 版本信息 固定数据
        commandBytes[2] = 0x05;
        commandBytes[3] = 0x03;
        commandBytes[4] = 0x02;
        commandBytes[5] = 0x00;
        commandBytes[6] = 0x01;
        commandBytes[7] = 0x00;
        commandBytes[8] = 0x01;

        commandBytes[9] = this.command;

        //跟门锁一致 固定填充
        commandBytes[10] = (byte) 0xA5;

        commandBytes[11] = this.length;
        if (this.data != null && this.data.length > 0)
            System.arraycopy(this.data, 0, commandBytes, 12, this.data.length);
        byte crc = CodecUtils.crccompute(Arrays.copyOf(commandBytes, commandBytes.length - 3));
        commandBytes[commandBytes.length - 3] = crc;
        commandBytes[commandBytes.length - 2] = 0x0d;
        commandBytes[commandBytes.length - 1] = 0x0a;
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
