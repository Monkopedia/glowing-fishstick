package com.ttlock.bl.sdk.entity

/**
 * Created by Administrator on 2016/6/14 0014.
 * 三代锁密码信息
 */
class PwdInfoV3 {
    private var year = 0
    private var code = 0
    private var secretKey: String? = null
    private var deleteDate: Long = 0
    fun getYear(): Int {
        return year
    }

    fun setYear(year: Int) {
        this.year = year
    }

    fun getCode(): Int {
        return code
    }

    fun setCode(code: Int) {
        this.code = code
    }

    fun getSecretKey(): String? {
        return secretKey
    }

    fun setSecretKey(secretKey: String?) {
        this.secretKey = secretKey
    }

    fun getDeleteDate(): Long {
        return deleteDate
    }

    fun setDeleteDate(deleteDate: Long) {
        this.deleteDate = deleteDate
    }

    companion object {
        fun getInstance(year: Int, code: Int, secretKey: String): PwdInfoV3 {
            var secretKey = secretKey
            val instance = PwdInfoV3()
            instance.year = year
            instance.code = code
            if (secretKey.length == 9) secretKey = "0$secretKey"
            instance.secretKey = secretKey
            return instance
        }

        fun getInstance(year: Int, code: Int, secretKey: String?, deleteDate: Long): PwdInfoV3 {
            val instance: PwdInfoV3 = PwdInfoV3.Companion.getInstance(year, code, secretKey)
            instance.deleteDate = deleteDate
            return instance
        }
    }
}
