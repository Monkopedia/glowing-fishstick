package com.ttlock.bl.sdk.entity

/**
 * Created by TTLock on 2019/4/23.
 */
class ServerError {
    /**
     * errcode : 10000
     * errmsg : invalid client_id
     * description : client_id不存在
     */
    var errcode = 0
    var errmsg: String? = null
    var description: String? = null
    fun getErrcode(): Int {
        return errcode
    }

    fun setErrcode(errcode: Int) {
        this.errcode = errcode
    }

    fun getErrmsg(): String? {
        return errmsg
    }

    fun setErrmsg(errmsg: String?) {
        this.errmsg = errmsg
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String?) {
        this.description = description
    }
}
