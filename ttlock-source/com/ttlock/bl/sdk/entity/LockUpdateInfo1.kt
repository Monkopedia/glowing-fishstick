package com.ttlock.bl.sdk.entity

/**
 * Created by Administrator on 2017/1/4 0004.
 */
class LockUpdateInfo {
    var errcode = 0
    var errmsg: String? = null
    var needUpdate = // 是否需要升级：0-否，1-是，2-未知
        0
    var url: String? = null
    var releaseNote: String? = //
        null
    var title: String? = // 升级或者已经是最新版 提示
        null
    var modelNum: String? = // 产品型号
        null
    var hardwareRevision: String? = // 硬件版本号
        null
    var firmwareRevision: String? = // 固件版本号
        null
    var decryptionKey: String? = // 解密密钥
        null
    var version: String? = // 版本号
        null

    fun getNeedUpdate(): Int {
        return needUpdate
    }

    fun getReleaseNote(): String? {
        return releaseNote
    }

    fun getUrl(): String? {
        return url
    }

    fun setNeedUpdate(needUpdate: Int) {
        this.needUpdate = needUpdate
    }

    fun setReleaseNote(releaseNote: String?) {
        this.releaseNote = releaseNote
    }

    fun setUrl(url: String?) {
        this.url = url
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?) {
        this.title = title
    }

    fun getDecryptionKey(): String? {
        return decryptionKey
    }

    fun getFirmwareRevision(): String? {
        return firmwareRevision
    }

    fun getHardwareRevision(): String? {
        return hardwareRevision
    }

    fun getModelNum(): String? {
        return modelNum
    }

    fun setDecryptionKey(decryptionKey: String?) {
        this.decryptionKey = decryptionKey
    }

    fun setFirmwareRevision(firmwareRevision: String?) {
        this.firmwareRevision = firmwareRevision
    }

    fun setHardwareRevision(hardwareRevision: String?) {
        this.hardwareRevision = hardwareRevision
    }

    fun setModelNum(modelNum: String?) {
        this.modelNum = modelNum
    }

    fun getVersion(): String? {
        return version
    }

    fun setVersion(version: String?) {
        this.version = version
    }

    override fun toString(): String {
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
            '}'
    }

    companion object {
        const val IS_LATEST_VERSION = 0
        const val NEW_VERSION_AVAILABLE = 1
        const val UNKNOWN_LOCK_VERSION = 2
    }
}
