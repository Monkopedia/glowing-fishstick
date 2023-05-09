package com.ttlock.bl.sdk.api;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Parcel;
import androidx.annotation.RequiresPermission;
import android.text.TextUtils;

import com.ttlock.bl.sdk.constant.Constant;
import com.ttlock.bl.sdk.constant.LockType;
import com.ttlock.bl.sdk.device.TTDevice;
import com.ttlock.bl.sdk.entity.HotelData;
import com.ttlock.bl.sdk.entity.LockVersion;
import com.ttlock.bl.sdk.entity.Scene;
import com.ttlock.bl.sdk.gateway.model.GatewayType;
import com.ttlock.bl.sdk.util.DigitUtil;

import java.util.Arrays;

/**
 * Created by Sciener on 2016/5/13.
 */
public class ExtendedBluetoothDevice extends TTDevice  {

    private static final long serialVersionUID = 1L;

    public static final byte GAP_ADTYPE_LOCAL_NAME_COMPLETE = 0X09;	//!< Complete local name
    public static final byte GAP_ADTYPE_POWER_LEVEL = 0X0A;	//!< TX Power Level: 0xXX: -127 to +127 dBm
    public static final byte GAP_ADTYPE_MANUFACTURER_SPECIFIC = (byte) 0XFF; //!< Manufacturer Specific Data: first 2 octets contain the Company Inentifier Code followed by the additional manufacturer specific data

    /**
     * 闭锁
     */
    public static final byte STATUS_PARK_LOCK = 0;
    /**
     * 开锁无车
     */
    public static final byte STATUS_PARK_UNLOCK_NO_CAR = 1;

    /**
     * 状态未知
     */
    public static final byte STATUS_PARK_UNKNOWN = 2;

    /**
     * 开锁有车
     */
    public static final byte STATUS_PARK_UNLOCK_HAS_CAR = 3;

    private int parkStatus;


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

    private byte protocolType;

    private byte protocolVersion;

    private byte scene;

    public byte groupId;

    public byte orgId;

    /** 锁类型 */
    private int lockType;

    /** 判断是否触摸过门锁 三代锁使用 */
    private boolean isTouch = true;

    /** 判断是否是设置模式 管理员需要在此模式下进行添加 */
//    private boolean isSettingMode = true;

    /** 判断是否是打开状态 车位锁使用 */
    private boolean isUnlock;

    private byte txPowerLevel;

    /**
     * 电池电量
     * -1 表示未获取到或者不支持
     */
//    private int batteryCapacity = -1;

    /**
     * 搜索到设备的时间
     */
    private long date = System.currentTimeMillis();

    /**
     * 判断是否是手环
     */
    private boolean isWristband;

    /**
     * 判断房间锁
     * 场景1 二代锁
     * 场景2 二代锁 带永久密码 三代锁
     * 场景3 荣域定制
     *
     */
    private boolean isRoomLock;

    /**
     * 判断是否是保险箱锁 场景是5 跟 11
     */
    private boolean isSafeLock;

    /**
     * 判断是否是自行车锁 场景是6
     */
    private boolean isBicycleLock;

    /**
     * 判断是否是车位锁
     */
    private boolean isLockcar;

    /**
     * 判断是否是门禁锁 场景是4
     */
    private boolean isGlassLock;

    /**
     * 判断是否是挂锁 场景是8
     */
    private boolean isPadLock;

    /**
     * 判断是否是锁芯 场景是9
     */
    private boolean isCyLinder;

    /**
     * scene 10
     */
    private boolean isRemoteControlDevice;

    /**
     * scene 12
     */
    private boolean isLift;

    /**
     * scene 13
     */
    private boolean isPowerSaver;

    /**
     * 判断是否是固件升级模式
     */
    private boolean isDfuMode;

    /**
     * 判断网关是否处于升级模式(泰凌蓝牙芯片)
     */
    private boolean isTelinkGatewayDfuMode;

    /**
     * judge whether the lock is in dfu mode
     */
    private boolean isNoLockService;

    /**
     * 只在添加管理员的回调中有效
     * 远程开锁开光状态
     */
    private int remoteUnlockSwitch;

    private String manufacturerId;

    public int disconnectStatus;

    /**
     * 断开连接的未知状态
     */
    public static final int DISCONNECT_STATUS_NONE = 0;

    /**
     * 连接超时
     */
    public static final int CONNECT_TIME_OUT = 1;

    /**
     * 发现服务时断开
     */
    public static final int SERVICE_DISCONNECTED = 2;

    /**
     * 指令相应超时
     */
    public static final int RESPONSE_TIME_OUT = 3;

    /**
     * 指令操作完成的断开
     */
    public static final int NORMAL_DISCONNECTED = 4;

    /**
     * 无法连接
     */
    public static final int DEVICE_CANNOT_CONNECT = 5;

    private HotelData hotelData;

    /**
     * 网关类型
     */
    private int gatewayType;

    public ExtendedBluetoothDevice() {

    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    public ExtendedBluetoothDevice(BluetoothDevice device) {
        this(device, 0, null);
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    public ExtendedBluetoothDevice(BluetoothDevice device, int rssi, byte[] scanRecord) {
        this.device = device;
        this.scanRecord = scanRecord;
        this.rssi = rssi;
        this.name = device.getName();
        this.mAddress = device.getAddress();
        if(scanRecord != null) {
            initial();
        }
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExtendedBluetoothDevice(ScanResult scanResult) {
        this.device = scanResult.getDevice();
        this.scanRecord = scanResult.getScanRecord().getBytes();
        this.rssi = scanResult.getRssi();
        this.name = device.getName();
        this.mAddress = device.getAddress();
        this.date = System.currentTimeMillis();
        initial();
    }

    @Override
    protected void initial() {
        int scanRecordLength = scanRecord.length;
        int index = 0;
        boolean nameIsScienerDfu = false;
        boolean isHasMAC = false;
        //TODO:越界
        while(index < scanRecordLength) {
            int len = scanRecord[index];
            if(len == 0) {
                break;
            }
            byte adtype = scanRecord[index + 1];
            switch (adtype) {
                case GAP_ADTYPE_LOCAL_NAME_COMPLETE:
                    byte[] nameBytes = new byte[len - 1];
                    System.arraycopy(scanRecord, index + 2, nameBytes, 0, len - 1);
                    if(name == null || name.length() == 0) {
                        setName(new String(nameBytes));
                    }

                    if (new String(nameBytes).equals("ScienerDfu")) {
                        nameIsScienerDfu = true;
                    }


                    if(name.toUpperCase().startsWith("LOCK_")) {
                        isRoomLock = true;
                    }
                    break;
                case GAP_ADTYPE_MANUFACTURER_SPECIFIC:

                    gatewayType = GatewayType.getGatewayType(Arrays.copyOfRange(scanRecord, index + 2, index + 4));

                    isHasMAC = true;
                    int offset = 2;
                    protocolType = scanRecord[index + offset++];
                    protocolVersion = scanRecord[index + offset++];

                    if (protocolType == 0x12 && protocolVersion == 0x19) {//插座DUF模式
//                        LogUtil.d("plug is in dfu mode");
                        isDfuMode = true;
                        return;
                    }

                    if (protocolType == 0x13 && protocolVersion == 0x19) {//网关DFU模式(泰凌微芯片)
                        isTelinkGatewayDfuMode = true;
                        return;
                    }

                    if (protocolType == (byte)0xff && protocolVersion == (byte)0xff) {
                        isDfuMode = true;
//                        LogUtil.d("isDfuMode:" + isDfuMode, LogUtil.isDBG());
                        return;
                    }
                    if(protocolType == 0x34 && protocolVersion == 0x12) {
                        isWristband = true;
                    }
                    if(BluetoothImpl.scanBongOnly) {//手环
                        return;
                    }
                    if(protocolType == 0x05 && protocolVersion == 0x03) {//三代锁
                        scene = scanRecord[index + offset++];

                    } else {//其它锁
                        offset = 6;	//其它协议是从第6位开始
                        protocolType = scanRecord[index + offset++];
                        protocolVersion = scanRecord[index + offset];
                        offset = 9;//scene偏移量
                        scene = scanRecord[index + offset++];
                    }

                    if(protocolType < 0x05 || getLockType() == LockType.LOCK_TYPE_V2S) {//老款锁没广播
                        isRoomLock = true;
                        return;
                    }

                    if(scene <= 3) {
                        isRoomLock = true;
                    } else {
                        switch (scene) {
                            case Scene.GLASS_LOCK://门禁
                                 isGlassLock = true;
                                 break;
                            case Scene.SAFE_LOCK://保险箱锁
                            case Scene.SAFE_LOCK_SINGLE_PASSCODE:
                                isSafeLock = true;
                                break;
                            case Scene.BICYCLE_LOCK://自行车锁
                                isBicycleLock = true;
                                break;
                            case Scene.PARKING_LOCK://车位锁
                                isLockcar = true;
                                break;
                            case Scene.PAD_LOCK://挂锁
                                isPadLock = true;
                                break;
                            case Scene.CYLINDER://锁芯
                                isCyLinder = true;
                                break;
                            case Scene.REMOTE_CONTROL_DEVICE:
                                if (protocolType == 0x05 && protocolVersion == 0x03) {   //二代车位锁场景也是10 增加一个三代锁的判断
                                    isRemoteControlDevice = true;
                                }
                                break;
                            case Scene.LIFT:
                                isLift = true;
                                break;
                            case Scene.POWER_SAVER:
                                isPowerSaver = true;
                                break;
                                default:
                                    break;
                        }
                    }

                    isUnlock = (scanRecord[index + offset] & 0x01) == 1 ? true : false;
                    //TODO:老款锁
                    isSettingMode = (scanRecord[index + offset] & 0x04) == 0 ? false : true;
                    if(getLockType() == LockType.LOCK_TYPE_V3 || getLockType() == LockType.LOCK_TYPE_V3_CAR){
                        isTouch = (scanRecord[index + offset] & 0x08) == 0 ? false : true;//三代锁表示触摸标志位

                    }
                    else if(getLockType() == LockType.LOCK_TYPE_CAR) {//二代车位锁放到最后判断 遥控设备场景是10
                        isTouch = false;//车位锁默认设置成false
                        isLockcar = true;
                    }

                    if(isLockcar) {//0位 4位组合状态
                        if(isUnlock) {
                            if((scanRecord[index + offset] & 0x10) == 1) {
                                parkStatus = STATUS_PARK_UNLOCK_HAS_CAR;
                            }
                            else {
                                parkStatus = STATUS_PARK_UNKNOWN;
                            }
                        } else {
                            if((scanRecord[index + offset] & 0x10) == 1) {
                                parkStatus = STATUS_PARK_UNLOCK_NO_CAR;
                            }
                            else {
                                parkStatus = STATUS_PARK_LOCK;
                            }
                        }
                    }
                    //电量偏移量
                    offset++;
                    batteryCapacity = scanRecord[index + offset];
                    //mac地址偏移量
                    offset+=3;
                    if(TextUtils.isEmpty(mAddress)) {
                        setAddress(DigitUtil.getMacString(Arrays.copyOfRange(scanRecord, index + offset, index + offset + 6)));
                    }
                    break;
                case GAP_ADTYPE_POWER_LEVEL:
                    txPowerLevel = scanRecord[index + 2];
                    break;
                default:
                    break;
            }
            index += len + 1;
        }

        if (nameIsScienerDfu && !isHasMAC) {
            isDfuMode = true;
        }

    }

    public boolean isDfuMode() {
        return isDfuMode;
    }

    public void setDfuMode(boolean dfuMode) {
        isDfuMode = dfuMode;
    }

    public boolean isLift() {
        return isLift;
    }

    public void setLift(boolean lift) {
        isLift = lift;
    }

    //
//    public BluetoothDevice getDevice() {
//        return device;
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
//    public void setAddress(String mAddress) {
//        this.mAddress = mAddress;
//    }

//    public String getAddress() {
//        return mAddress;
//    }
//
//    public boolean isSettingMode() {
//        return isSettingMode;
//    }

//    public void setSettingMode(boolean settingMode) {
//        isSettingMode = settingMode;
//    }
//
//    public int getBatteryCapacity() {
//        return batteryCapacity;
//    }

    public void setBatteryCapacity(byte batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public boolean isTouch() {
        return isTouch;
    }

//    public int getRssi() {
//        return rssi;
//    }
//
//    public void setRssi(int rssi) {
//        this.rssi = rssi;
//    }

    public void setTouch(boolean touch) {
        isTouch = touch;
    }

    public boolean isWristband() {
        return isWristband;
    }

    public void setWristband(boolean wristband) {
        isWristband = wristband;
    }

    public boolean isBicycleLock() {
        return isBicycleLock;
    }

    public void setBicycleLock(boolean bicycleLock) {
        isBicycleLock = bicycleLock;
    }

    public boolean isSafeLock() {
        return isSafeLock;
    }

    public void setSafeLock(boolean safeLock) {
        isSafeLock = safeLock;
    }

    public boolean isRoomLock() {
        return isRoomLock;
    }

    public void setRoomLock(boolean roomLock) {
        isRoomLock = roomLock;
    }

    public boolean isLockcar() {
        return isLockcar;
    }

    public void setLockcar(boolean lockcar) {
        isLockcar = lockcar;
    }

    public boolean isGlassLock() {
        return isGlassLock;
    }

    public boolean isPadLock() {
        return isPadLock;
    }

    public boolean isCyLinder() {
        return isCyLinder;
    }

    public boolean isRemoteControlDevice() {
        return isRemoteControlDevice;
    }

    public void setRemoteControlDevice(boolean remoteControlDevice) {
        isRemoteControlDevice = remoteControlDevice;
    }

    public void setCyLinder(boolean cyLinder) {
        isCyLinder = cyLinder;
    }

    public void setPadLock(boolean padLock) {
        isPadLock = padLock;
    }

    public void setGlassLock(boolean glassLock) {
        isGlassLock = glassLock;
    }

    public byte getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(byte protocolType) {
        this.protocolType = protocolType;
    }

    public byte getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(byte protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public byte getScene() {
        return scene;
    }

    public void setScene(byte scene) {
        this.scene = scene;
    }

    public int getRemoteUnlockSwitch() {
        return remoteUnlockSwitch;
    }

    public boolean isNoLockService() {
        return isNoLockService;
    }

    public boolean isPowerSaver() {
        return isPowerSaver;
    }

    public void setPowerSaver(boolean powerSaver) {
        isPowerSaver = powerSaver;
    }

    public void setNoLockService(boolean noLockService) {
        isNoLockService = noLockService;
    }

    public HotelData getHotelData() {
        return hotelData;
    }

    public boolean isTelinkGatewayDfuMode() {
        return isTelinkGatewayDfuMode;
    }

    public void setTelinkGatewayDfuMode(boolean telinkGatewayDfuMode) {
        isTelinkGatewayDfuMode = telinkGatewayDfuMode;
    }

    public void setHotelData(HotelData hotelData) throws ParamInvalidException{
        if (hotelData.hotelInfo != null) {
            String data = DigitUtil.decodeLockData(hotelData.getHotelInfo());
            if (!TextUtils.isEmpty(data)) {
                String[] array = data.split(",");
                hotelData.hotelNumber = Integer.valueOf(array[0]);
                if (array[1] != null && array[2] != null) {
                    hotelData.icKey = DigitUtil.convertStringDividerByDot(array[1]);
                    hotelData.aesKey = DigitUtil.convertStringDividerByDot(array[2]);
                }else {
                    throw new ParamInvalidException();
                }
            } else {
                throw new ParamInvalidException();
            }
        }else {
            throw new ParamInvalidException();
        }

        this.hotelData = hotelData;

    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
        Constant.VENDOR = manufacturerId;
    }

    public void setRemoteUnlockSwitch(int remoteUnlockSwitch) {
        this.remoteUnlockSwitch = remoteUnlockSwitch;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ExtendedBluetoothDevice) {
            return mAddress.equals(((ExtendedBluetoothDevice)o).getAddress());
        }
        return false;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getParkStatus() {
        return parkStatus;
    }

    public void setParkStatus(int parkStatus) {
        this.parkStatus = parkStatus;
    }

    //TODO:二代
    //直接根据mac地址连接的没有扫描信息 这里的数据不准
        public int getLockType() {
        if(protocolType == 0x05 && protocolVersion == 0x03 && scene == 0x07){
            lockType = LockType.LOCK_TYPE_V3_CAR;
        }
        else if(protocolType == 0x0a && protocolVersion == 0x01){
            lockType = LockType.LOCK_TYPE_CAR;
        }
        else if(protocolType == 0x0b && protocolVersion == 0x01){
            lockType = LockType.LOCK_TYPE_MOBI;
        }
        else if(protocolType == 0x05 && protocolVersion == 0x04){
            lockType = LockType.LOCK_TYPE_V2S_PLUS;

        }
        else if(protocolType == 0x05 && protocolVersion == 0x03){
            lockType = LockType.LOCK_TYPE_V3;
        }
        else if(protocolType == 0x05 && protocolVersion == 0x01 || (name != null && name.toUpperCase().startsWith("LOCK_"))){
            lockType = LockType.LOCK_TYPE_V2S;
        }
        return lockType;
    }

    //TODO:不一定准确
    public String getLockVersionJson() {
        if(name.toUpperCase().startsWith("LOCK_")) {//2S版本信息
            protocolType = 0x05;
            protocolVersion = 0x01;
        }
        return new LockVersion(protocolType, protocolVersion, scene, groupId, orgId).toGson();
    }

    @Override
    public String toString() {
        return "ExtendedBluetoothDevice{" +
                "name='" + name + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", rssi=" + rssi +
                ", protocolType=" + protocolType +
                ", protocolVersion=" + protocolVersion +
                ", scene=" + scene +
                ", groupId=" + groupId +
                ", orgId=" + orgId +
                ", lockType=" + lockType +
                ", isTouch=" + isTouch +
                ", isSettingMode=" + isSettingMode +
                ", isWristband=" + isWristband() +
                ", isUnlock=" + isUnlock +
                ", txPowerLevel=" + txPowerLevel +
                ", batteryCapacity=" + batteryCapacity +
                ", date=" + date +
                ", device=" + device +
                ", scanRecord=" + DigitUtil.byteArrayToHexString(scanRecord) +
                '}';
    }

    public int getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(int gatewayType) {
        this.gatewayType = gatewayType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.parkStatus);
        dest.writeByte(this.protocolType);
        dest.writeByte(this.protocolVersion);
        dest.writeByte(this.scene);
        dest.writeByte(this.groupId);
        dest.writeByte(this.orgId);
        dest.writeInt(this.lockType);
        dest.writeByte(this.isTouch ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isUnlock ? (byte) 1 : (byte) 0);
        dest.writeByte(this.txPowerLevel);
        dest.writeLong(this.date);
        dest.writeByte(this.isWristband ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isRoomLock ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSafeLock ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isBicycleLock ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isLockcar ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isGlassLock ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPadLock ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCyLinder ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isRemoteControlDevice ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isLift ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPowerSaver ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDfuMode ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isTelinkGatewayDfuMode ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isNoLockService ? (byte) 1 : (byte) 0);
        dest.writeInt(this.remoteUnlockSwitch);
        dest.writeString(this.manufacturerId);
        dest.writeInt(this.disconnectStatus);
        dest.writeParcelable(this.hotelData, flags);
        dest.writeInt(this.gatewayType);
        dest.writeParcelable(this.device, flags);
        dest.writeByteArray(this.scanRecord);
        dest.writeString(this.name);
        dest.writeString(this.number);
        dest.writeString(this.mAddress);
        dest.writeInt(this.rssi);
        dest.writeInt(this.batteryCapacity);
        dest.writeByte(this.isSettingMode ? (byte) 1 : (byte) 0);
    }

    protected ExtendedBluetoothDevice(Parcel in) {
        this.parkStatus = in.readInt();
        this.protocolType = in.readByte();
        this.protocolVersion = in.readByte();
        this.scene = in.readByte();
        this.groupId = in.readByte();
        this.orgId = in.readByte();
        this.lockType = in.readInt();
        this.isTouch = in.readByte() != 0;
        this.isUnlock = in.readByte() != 0;
        this.txPowerLevel = in.readByte();
        this.date = in.readLong();
        this.isWristband = in.readByte() != 0;
        this.isRoomLock = in.readByte() != 0;
        this.isSafeLock = in.readByte() != 0;
        this.isBicycleLock = in.readByte() != 0;
        this.isLockcar = in.readByte() != 0;
        this.isGlassLock = in.readByte() != 0;
        this.isPadLock = in.readByte() != 0;
        this.isCyLinder = in.readByte() != 0;
        this.isRemoteControlDevice = in.readByte() != 0;
        this.isLift = in.readByte() != 0;
        this.isPowerSaver = in.readByte() != 0;
        this.isDfuMode = in.readByte() != 0;
        this.isTelinkGatewayDfuMode = in.readByte() != 0;
        this.isNoLockService = in.readByte() != 0;
        this.remoteUnlockSwitch = in.readInt();
        this.manufacturerId = in.readString();
        this.disconnectStatus = in.readInt();
        this.hotelData = in.readParcelable(HotelData.class.getClassLoader());
        this.gatewayType = in.readInt();
        this.device = in.readParcelable(BluetoothDevice.class.getClassLoader());
        this.scanRecord = in.createByteArray();
        this.name = in.readString();
        this.number = in.readString();
        this.mAddress = in.readString();
        this.rssi = in.readInt();
        this.batteryCapacity = in.readInt();
        this.isSettingMode = in.readByte() != 0;
    }

    public static final Creator<ExtendedBluetoothDevice> CREATOR = new Creator<ExtendedBluetoothDevice>() {
        @Override
        public ExtendedBluetoothDevice createFromParcel(Parcel source) {
            return new ExtendedBluetoothDevice(source);
        }

        @Override
        public ExtendedBluetoothDevice[] newArray(int size) {
            return new ExtendedBluetoothDevice[size];
        }
    };
}
