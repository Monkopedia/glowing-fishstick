package com.ttlock.bl.sdk.device;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Parcel;
import androidx.annotation.RequiresPermission;

/**
 * Created by TTLock on 2019/5/16.
 */

public class WirelessKeypad extends TTDevice {


    public static final byte GAP_ADTYPE_LOCAL_NAME_COMPLETE = 0X09;	//!< Complete local name
    public static final byte GAP_ADTYPE_POWER_LEVEL = 0X0A;	//!< TX Power Level: 0xXX: -127 to +127 dBm
    public static final byte GAP_ADTYPE_MANUFACTURER_SPECIFIC = (byte) 0XFF; //!< Manufacturer Specific Data: first 2 octets contain the Company Inentifier Code followed by the additional manufacturer specific data

//
//    private BluetoothDevice device;
//
//    private byte[] scanRecord;
//
//    private String name;
//
//    /** mac 地址 */
//    private String mAddress;
//
//    private int rssi;

    private byte scene;
//    private int batteryCapacity;
//    private boolean isSettingMode;

    /**
     * 搜索到设备的时间
     */
    private long date = System.currentTimeMillis();

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WirelessKeypad(ScanResult scanResult) {
        this.device = scanResult.getDevice();
        this.scanRecord = scanResult.getScanRecord().getBytes();
        this.rssi = scanResult.getRssi();
        this.name = device.getName();
        this.number = this.name;
        this.mAddress = device.getAddress();
        this.date = System.currentTimeMillis();
        initial();
    }

    @Override
    protected void initial() {
        int scanRecordLength = scanRecord.length;
        int index = 0;
        //TODO:越界
        while(index < scanRecordLength) {
            int len = scanRecord[index];
            if(len == 0)
                break;
            byte adtype = scanRecord[index + 1];
            switch (adtype) {
                case GAP_ADTYPE_LOCAL_NAME_COMPLETE:
                    byte[] nameBytes = new byte[len - 1];
                    System.arraycopy(scanRecord, index + 2, nameBytes, 0, len - 1);
                    if(name == null || name.length() == 0) {
                        setName(new String(nameBytes));
                    }
                    break;
                case GAP_ADTYPE_MANUFACTURER_SPECIFIC:
                    int offset = 2;
                    byte protocolType = scanRecord[index + offset++];
                    byte protocolVersion = scanRecord[index + offset++];

                    if(protocolType == 0x05 && protocolVersion == 0x03) {//三代锁
                        scene = scanRecord[index + offset++];

                    } else {//其它锁
                        offset = 6;	//其它协议是从第6位开始
                        protocolType = scanRecord[index + offset++];
                        protocolVersion = scanRecord[index + offset];
                        offset = 9;//scene偏移量
                        scene = scanRecord[index + offset++];
                    }

                    //TODO:老款锁
                    isSettingMode = (scanRecord[index + offset] & 0x04) == 0 ? false : true;

                    //电量偏移量
                    offset++;
                    batteryCapacity = scanRecord[index + offset];

                    break;
                case GAP_ADTYPE_POWER_LEVEL:
//                    txPowerLevel = scanRecord[index + 2];
                    break;
                default:
                    break;
            }
            index += len + 1;
        }
    }

//    public BluetoothDevice getDevice() {
//        return device;
//    }
//
//    public void setDevice(BluetoothDevice device) {
//        this.device = device;
//    }
//
//    public byte[] getScanRecord() {
//        return scanRecord;
//    }
//
//    public void setScanRecord(byte[] scanRecord) {
//        this.scanRecord = scanRecord;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getAddress() {
//        return mAddress;
//    }
//
//    public void setAddress(String mAddress) {
//        this.mAddress = mAddress;
//    }
//
//    public int getRssi() {
//        return rssi;
//    }
//
//    public void setRssi(int rssi) {
//        this.rssi = rssi;
//    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

//    public int getBatteryCapacity() {
//        return batteryCapacity;
//    }
//
//    public void setBatteryCapacity(int batteryCapacity) {
//        this.batteryCapacity = batteryCapacity;
//    }
//
//    public boolean isSettingMode() {
//        return isSettingMode;
//    }
//
//    public void setSettingMode(boolean settingMode) {
//        isSettingMode = settingMode;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.scene);
        dest.writeLong(this.date);
        dest.writeParcelable(this.device, flags);
        dest.writeByteArray(this.scanRecord);
        dest.writeString(this.name);
        dest.writeString(this.mAddress);
        dest.writeInt(this.rssi);
        dest.writeInt(this.batteryCapacity);
        dest.writeByte(this.isSettingMode ? (byte) 1 : (byte) 0);
    }

    protected WirelessKeypad(Parcel in) {
        this.scene = in.readByte();
        this.date = in.readLong();
        this.device = in.readParcelable(BluetoothDevice.class.getClassLoader());
        this.scanRecord = in.createByteArray();
        this.name = in.readString();
        this.mAddress = in.readString();
        this.rssi = in.readInt();
        this.batteryCapacity = in.readInt();
        this.isSettingMode = in.readByte() != 0;
    }

    public static final Creator<WirelessKeypad> CREATOR = new Creator<WirelessKeypad>() {
        @Override
        public WirelessKeypad createFromParcel(Parcel source) {
            return new WirelessKeypad(source);
        }

        @Override
        public WirelessKeypad[] newArray(int size) {
            return new WirelessKeypad[size];
        }
    };
}
