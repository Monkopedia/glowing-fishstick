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

public class Remote extends TTDevice {
    private byte scene;

    /**
     * 搜索到设备的时间
     */
    private long date = System.currentTimeMillis();

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Remote(ScanResult scanResult) {
        this.device = scanResult.getDevice();
        this.scanRecord = scanResult.getScanRecord().getBytes();
        this.rssi = scanResult.getRssi();
        this.name = device.getName();
        this.number = this.name;
        this.mAddress = device.getAddress();
        this.date = System.currentTimeMillis();
        initial();
    }

    public Remote(BluetoothDevice device) {
        super(device);
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

                    if(protocolType == 0x05 && protocolVersion == 0x03) {//三代协议
                        scene = scanRecord[index + offset++];
                    }

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

    @Override
    public long getDate() {
        return date;
    }

    @Override
    public void setDate(long date) {
        this.date = date;
    }

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

    protected Remote(Parcel in) {
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

    public static final Creator<Remote> CREATOR = new Creator<Remote>() {
        @Override
        public Remote createFromParcel(Parcel source) {
            return new Remote(source);
        }

        @Override
        public Remote[] newArray(int size) {
            return new Remote[size];
        }
    };
}
