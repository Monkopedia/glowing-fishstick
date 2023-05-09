package com.ttlock.bl.sdk.device;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Parcelable;
import androidx.annotation.RequiresPermission;


/**
 * Created on  2019/6/14 0014 15:20
 *
 * @author theodore_hu
 */
public abstract class TTDevice implements Parcelable {

    public static final byte GAP_ADTYPE_LOCAL_NAME_COMPLETE = 0X09;	//!< Complete local name
    public static final byte GAP_ADTYPE_POWER_LEVEL = 0X0A;	//!< TX Power Level: 0xXX: -127 to +127 dBm
    public static final byte GAP_ADTYPE_MANUFACTURER_SPECIFIC = (byte) 0XFF; //!< Manufacturer Specific Data: first 2 octets contain the Company Inentifier Code followed by the additional manufacturer specific data

    protected BluetoothDevice device;

    protected byte[] scanRecord;

    protected String name;

    protected String number;

    /** mac 地址 */
    protected String mAddress;

    protected int rssi;

    protected int batteryCapacity;
    protected boolean isSettingMode;

    /**
     * 搜索到设备的时间
     */
    private long date = System.currentTimeMillis();

    public TTDevice() {
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TTDevice(ScanResult scanResult) {
        this.device = scanResult.getDevice();
        this.scanRecord = scanResult.getScanRecord().getBytes();
        this.rssi = scanResult.getRssi();
        this.name = device.getName();
        this.number = this.name;
        this.mAddress = device.getAddress();
        this.date = System.currentTimeMillis();
//        initial();
    }

    public TTDevice(BluetoothDevice device) {
        this.device = device;
        this.name = device.getName();
        this.mAddress = device.getAddress();
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
        this.name = device.getName();
        this.mAddress = device.getAddress();
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    protected abstract void initial();

    public byte[] getScanRecord() {
        return scanRecord;
    }

    public void setScanRecord(byte[] scanRecord) {
        this.scanRecord = scanRecord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }


    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public boolean isSettingMode() {
        return isSettingMode;
    }

    public void setSettingMode(boolean settingMode) {
        isSettingMode = settingMode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
