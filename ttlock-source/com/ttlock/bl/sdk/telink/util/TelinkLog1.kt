/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.ttlock.bl.sdk.telink.util

import android.util.Log

object TelinkLog {
    const val TAG = "TelinkBluetoothSDK"
    var ENABLE = true
    fun isLoggable(level: Int): Boolean {
        return if (ENABLE) Log.isLoggable(TAG, level) else false
    }

    fun getStackTraceString(th: Throwable): String? {
        return if (ENABLE) Log.getStackTraceString(th) else th.message
    }

    fun println(level: Int, msg: String?): Int {
        return if (ENABLE) Log.println(level, TAG, msg) else 0
    }

    fun v(msg: String?): Int {
        return if (ENABLE) Log.v(TAG, msg) else 0
    }

    fun v(msg: String?, th: Throwable?): Int {
        return if (ENABLE) Log.v(TAG, msg, th) else 0
    }

    fun d(msg: String?): Int {
        return if (ENABLE) Log.d(TAG, msg) else 0
    }

    fun d(msg: String?, th: Throwable?): Int {
        return if (ENABLE) Log.d(TAG, msg, th) else 0
    }

    fun i(msg: String?): Int {
        return if (ENABLE) Log.i(TAG, msg) else 0
    }

    fun i(msg: String?, th: Throwable?): Int {
        return if (ENABLE) Log.i(TAG, msg, th) else 0
    }

    fun w(msg: String?): Int {
        return if (ENABLE) Log.w(TAG, msg) else 0
    }

    fun w(msg: String?, th: Throwable?): Int {
        return if (ENABLE) Log.w(TAG, msg, th) else 0
    }

    fun w(th: Throwable?): Int {
        return if (ENABLE) Log.w(TAG, th) else 0
    }

    fun e(msg: String?): Int {
        return if (ENABLE) Log.w(TAG, msg) else 0
    }

    fun e(msg: String?, th: Throwable?): Int {
        return if (ENABLE) Log.e(TAG, msg, th) else 0
    }
}
