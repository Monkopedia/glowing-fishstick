package com.ttlock.bl.sdk.entity;

/**
 * Created by Administrator on 2017/1/4 0004.
 */
public class LockUpdateInfo {
    public int errcode;
    public String errmsg;

    int needUpdate;//是否需要升级：0-否，1-是，2-未知
    String url;
    String releaseNote;//
    String title;//升级或者已经是最新版 提示
    String modelNum;//产品型号
    String hardwareRevision;//硬件版本号
    String firmwareRevision;//固件版本号
    String decryptionKey;//解密密钥
    String version;//版本号


    public int getNeedUpdate() {
        return needUpdate;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public String getUrl() {
        return url;
    }

    public void setNeedUpdate(int needUpdate) {
        this.needUpdate = needUpdate;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecryptionKey() {
        return decryptionKey;
    }

    public String getFirmwareRevision() {
        return firmwareRevision;
    }

    public String getHardwareRevision() {
        return hardwareRevision;
    }

    public String getModelNum() {
        return modelNum;
    }

    public void setDecryptionKey(String decryptionKey) {
        this.decryptionKey = decryptionKey;
    }

    public void setFirmwareRevision(String firmwareRevision) {
        this.firmwareRevision = firmwareRevision;
    }

    public void setHardwareRevision(String hardwareRevision) {
        this.hardwareRevision = hardwareRevision;
    }

    public void setModelNum(String modelNum) {
        this.modelNum = modelNum;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static final int IS_LATEST_VERSION = 0;
    public static final int NEW_VERSION_AVAILABLE = 1;
    public static final int UNKNOWN_LOCK_VERSION = 2;

    @Override
    public String toString() {
        return "LockUpdateInfo{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", needUpdate=" + needUpdate +
                ", url='" + url + '\'' +
                ", releaseNote='" + releaseNote + '\'' +
                ", title='" + title + '\'' +
                ", modelNum='" + modelNum + '\'' +
                ", hardwareRevision='" + hardwareRevision + '\'' +
                ", firmwareRevision='" + firmwareRevision + '\'' +
                ", decryptionKey='" + decryptionKey + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
