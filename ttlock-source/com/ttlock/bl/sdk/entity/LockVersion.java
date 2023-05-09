package com.ttlock.bl.sdk.entity;

import com.ttlock.bl.sdk.constant.LockType;
import com.ttlock.bl.sdk.util.GsonUtil;

/**
 * Created by Smartlock on 2016/5/27.
 */
public class LockVersion {

    public static LockVersion lockVersion_V2S_PLUS = new LockVersion((byte) 5, (byte)4, (byte)1, (byte)1, (byte)1);
    public static LockVersion lockVersion_V3 = new LockVersion((byte) 5, (byte)3, (byte)1, (byte)1, (byte)1);
    public static LockVersion lockVersion_V2S = new LockVersion((byte) 5, (byte)1, (byte)1, (byte)1, (byte)1);
    /**
     * 二代车位锁场景也改为7
     */
    public static LockVersion lockVersion_Va = new LockVersion((byte) 0x0a, (byte)1, (byte)0x07, (byte)1, (byte)1);
    /**
     * 电动车锁场景先改为1  目前没有电动车锁
     */
    public static LockVersion lockVersion_Vb = new LockVersion((byte) 0x0b, (byte)1, (byte)0x01, (byte)1, (byte)1);
    public static LockVersion lockVersion_V2 = new LockVersion((byte) 3, (byte)0, (byte)0, (byte)0, (byte)0);
    public static LockVersion lockVersion_V3_car = new LockVersion((byte) 5, (byte)3, (byte)7, (byte)1, (byte)1);

    private byte protocolType;
    private byte protocolVersion;
    private byte scene;
    private short groupId;
    private short orgId;

    public LockVersion(byte protocolType, byte protocolVersion, byte scene, short groupId, short orgId) {
        super();
        this.protocolType = protocolType;
        this.protocolVersion = protocolVersion;
        this.scene = scene;
        this.groupId = groupId;
        this.orgId = orgId;
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
    public short getGroupId() {
        return groupId;
    }
    public void setGroupId(short groupId) {
        this.groupId = groupId;
    }
    public short getOrgId() {
        return orgId;
    }
    public void setOrgId(short orgId) {
        this.orgId = orgId;
    }

    public static LockVersion getLockVersion(int lockType) {
        switch (lockType) {
            case LockType.LOCK_TYPE_V3_CAR:
                return lockVersion_V3_car;
            case LockType.LOCK_TYPE_V3:
                return lockVersion_V3;
            case LockType.LOCK_TYPE_V2S_PLUS:
                return lockVersion_V2S_PLUS;
            case LockType.LOCK_TYPE_V2S:
                return lockVersion_V2S;
            case LockType.LOCK_TYPE_CAR:
                return lockVersion_Va;
            case LockType.LOCK_TYPE_MOBI:
                return lockVersion_Vb;
            case LockType.LOCK_TYPE_V2:
                return lockVersion_V2;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return protocolType + "," + protocolVersion + "," + scene + "," + groupId + "," + orgId;
    }

    public String toGson() {
       return GsonUtil.toJson(this);
    }

}
