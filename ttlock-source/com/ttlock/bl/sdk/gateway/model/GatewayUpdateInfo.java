package com.ttlock.bl.sdk.gateway.model;

/**
 * Created by TTLock on 2019/4/30.
 */

public class GatewayUpdateInfo {
    /**
     * decryptionKey : LTM0LC0zOSwtMzQsLTMzLC00MSwtNDEsLTQwLC00MiwtMzcsLTM4LC00MSwtNDEsLTMzLC0zMywtMzYsLTM0LDExNA==
     * needUpgrade : 1
     * firmwareInfo : {"modelNum":"SN227","hardwareRevision":"1.1","firmwareRevision":"1.0.19.0404"}
     * version : 1.3
     * releaseNote : lock firmware update
     * url : http://qiniupackage.sciener.cn/packages/BLEGateway11.zip
     */

    private String decryptionKey;
    private int needUpgrade;
    private DeviceInfo firmwareInfo;
    private String version;
    private String releaseNote;
    private String url;

    public String getDecryptionKey() {
        return decryptionKey;
    }

    public void setDecryptionKey(String decryptionKey) {
        this.decryptionKey = decryptionKey;
    }

    public int getNeedUpgrade() {
        return needUpgrade;
    }

    public void setNeedUpgrade(int needUpgrade) {
        this.needUpgrade = needUpgrade;
    }

    public DeviceInfo getFirmwareInfo() {
        return firmwareInfo;
    }

    public void setFirmwareInfo(DeviceInfo firmwareInfo) {
        this.firmwareInfo = firmwareInfo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
