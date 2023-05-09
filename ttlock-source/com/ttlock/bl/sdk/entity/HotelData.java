package com.ttlock.bl.sdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ttlock.bl.sdk.util.DigitUtil;

/**
 * Created by TTLock on 2018/9/21.
 */

public class HotelData implements Parcelable {

    public static final byte TYPE_DEFAULT = 0;
    public static final byte TYPE_IC_KEY = 1;
    public static final byte TYPE_AES_KEY = 2;
    public static final byte TYPE_HOTEL_BUILDING_FLOOR = 3;
    public static final byte TYPE_SECTOR = 4;
    public static final byte TYPE_ELEVATOR_CONTROLABLE_FLOORS = 5;
    public static final byte TYPE_ELEVATOR_WORK_MODE = 6;
    public static final byte TYPE_POWER_SAVER_WORK_MODE = 7;
    public static final byte TYPE_POWER_SAVER_CONTROLABLE_LOCK = 8;


    /**
     * 查询
     */
    public static final int GET = 1;
    /**
     * 设置
     */
    public static final int SET = 2;

    /**
     * hotelInfo解析内容
     */
    public String icKey = "a9,65,65,4d,3d,46";
    public String aesKey = "a9,65,65,4d,3d,46,94,e6,3b,68,c3,07,cf,ea,4b,54";
    public int hotelNumber = 1;

    public int buildingNumber = 1;
    public int floorNumber = 1;
    public String hotelInfo;
    /**
     * deault 0:use all sectors
     */
    public short sector;
    public byte[] controlableFloors;
    public TTLiftWorkMode ttLiftWorkMode;
    public String controlableLockMac;

    /**
     * default:use all work mode
     */
    public byte powerWorkModeValue;

    /**
     * current set type
     */
    public byte paraType = TYPE_IC_KEY;


    public byte[] getICKeyByteArray() {
        return DigitUtil.convertAesKeyStrToBytes(icKey);
    }

    public byte[] getAesKeyByteArray() {
        return DigitUtil.convertAesKeyStrToBytes(aesKey);
    }

    public int getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(int buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getIcKey() {
        return icKey;
    }

    public void setIcKey(String icKey) {
        this.icKey = icKey;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public int getHotelNumber() {
        return hotelNumber;
    }

    public void setHotelNumber(int hotelNumber) {
        this.hotelNumber = hotelNumber;
    }

    public short getSector() {
        return sector;
    }

    public void setSector(short sector) {
        this.sector = sector;
    }

    public byte[] getControlableFloors() {
        return controlableFloors;
    }

    public void setControlableFloors(byte[] controlableFloors) {
        this.controlableFloors = controlableFloors;
    }

    public TTLiftWorkMode getTtLiftWorkMode() {
        return ttLiftWorkMode;
    }

    public void setTtLiftWorkMode(TTLiftWorkMode ttLiftWorkMode) {
        this.ttLiftWorkMode = ttLiftWorkMode;
    }

    public String getControlableLockMac() {
        return controlableLockMac;
    }

    public void setControlableLockMac(String controlableLockMac) {
        this.controlableLockMac = controlableLockMac;
    }

    public byte getPowerWorkModeValue() {
        return powerWorkModeValue;
    }

    public void setPowerWorkModeValue(byte powerWorkModeValue) {
        this.powerWorkModeValue = powerWorkModeValue;
    }

    public String getHotelInfo() {
        return hotelInfo;
    }

    public void setHotelInfo(String hotelInfo) {
        this.hotelInfo = hotelInfo;
    }

    public byte getParaType() {
        return paraType;
    }

    public void setParaType(byte paraType) {
        this.paraType = paraType;
    }

    public HotelData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.icKey);
        dest.writeString(this.aesKey);
        dest.writeInt(this.hotelNumber);
        dest.writeInt(this.buildingNumber);
        dest.writeInt(this.floorNumber);
        dest.writeString(this.hotelInfo);
        dest.writeInt(this.sector);
        dest.writeByteArray(this.controlableFloors);
        dest.writeInt(this.ttLiftWorkMode == null ? -1 : this.ttLiftWorkMode.ordinal());
        dest.writeByte(this.powerWorkModeValue);
        dest.writeByte(this.paraType);
    }

    protected HotelData(Parcel in) {
        this.icKey = in.readString();
        this.aesKey = in.readString();
        this.hotelNumber = in.readInt();
        this.buildingNumber = in.readInt();
        this.floorNumber = in.readInt();
        this.hotelInfo = in.readString();
        this.sector = (short) in.readInt();
        this.controlableFloors = in.createByteArray();
        int tmpTtLiftWorkMode = in.readInt();
        this.ttLiftWorkMode = tmpTtLiftWorkMode == -1 ? null : TTLiftWorkMode.values()[tmpTtLiftWorkMode];
        this.powerWorkModeValue = in.readByte();
        this.paraType = in.readByte();
    }

    public static final Creator<HotelData> CREATOR = new Creator<HotelData>() {
        @Override
        public HotelData createFromParcel(Parcel source) {
            return new HotelData(source);
        }

        @Override
        public HotelData[] newArray(int size) {
            return new HotelData[size];
        }
    };
}
