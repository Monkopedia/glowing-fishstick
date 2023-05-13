package com.ttlock.bl.sdk.api

/**
 * Created on  2019/6/11 0011 14:27
 *
 * @author theodore_hu
 */
class ParamInvalidException : Exception() {
    override fun toString(): String {
        return "parameter value invalid exception"
    }
}
