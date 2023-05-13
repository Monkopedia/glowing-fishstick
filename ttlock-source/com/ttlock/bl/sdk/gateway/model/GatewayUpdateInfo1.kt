package com.ttlock.bl.sdk.gateway.model

/**
 * Created by TTLock on 2019/4/30.
 */
class GatewayUpdateInfo {
    /**
     * decryptionKey : LTM0LC0zOSwtMzQsLTMzLC00MSwtNDEsLTQwLC00MiwtMzcsLTM4LC00MSwtNDEsLTMzLC0zMywtMzYsLTM0LDExNA==
     * needUpgrade : 1
     * firmwareInfo : {"modelNum":"SN227","hardwareRevision":"1.1","firmwareRevision":"1.0.19.0404"}
     * version : 1.3
     * releaseNote : lock firmware update
     * url : http://qiniupackage.sciener.cn/packages/BLEGateway11.zip
     */
    private var decryptionKey: String? = null
    private var needUpgrade = 0
    private var firmwareInfo: DeviceInfo? = null
    private var version: String? = null
    private var releaseNote: String? = null
    private var url: String? = null
    fun getDecryptionKey(): String? {
        return decryptionKey
    }

    fun setDecryptionKey(decryptionKey: String?) {
        this.decryptionKey = decryptionKey
    }

    fun getNeedUpgrade(): Int {
        return needUpgrade
    }

    fun setNeedUpgrade(needUpgrade: Int) {
        this.needUpgrade = needUpgrade
    }

    fun getFirmwareInfo(): DeviceInfo? {
        return firmwareInfo
    }

    fun setFirmwareInfo(firmwareInfo: DeviceInfo?) {
        this.firmwareInfo = firmwareInfo
    }

    fun getVersion(): String? {
        return version
    }

    fun setVersion(version: String?) {
        this.version = version
    }

    fun getReleaseNote(): String? {
        return releaseNote
    }

    fun setReleaseNote(releaseNote: String?) {
        this.releaseNote = releaseNote
    }

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String?) {
        this.url = url
    }
}
