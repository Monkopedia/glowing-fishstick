package com.ttlock.bl.sdk.remote.model

/**
 * Created on  2019/4/12 0012 16:34
 *
 * @author theodre
 */
class ConnectParam {
    private var lockmac: String? = null
    private var lockKey: String? = null
    private var aesKey: String? = null

    //    public String factoryDate;
    fun getLockmac(): String? {
        return lockmac
    }

    fun setLockmac(lockmac: String?) {
        this.lockmac = lockmac
    }

    fun getLockKey(): String? {
        return lockKey
    }

    fun setLockKey(lockKey: String?) {
        this.lockKey = lockKey
    }

    fun getAesKey(): String? {
        return aesKey
    }

    fun setAesKey(aesKey: String?) {
        this.aesKey = aesKey
    }
}
