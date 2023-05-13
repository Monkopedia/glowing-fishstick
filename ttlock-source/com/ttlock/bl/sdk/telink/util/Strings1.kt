/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.ttlock.bl.sdk.telink.util

import java.nio.charset.Charset
import kotlin.jvm.JvmOverloads

object Strings {
    @JvmOverloads
    fun stringToBytes(str: String, length: Int = 0): ByteArray {
        val srcBytes: ByteArray
        if (length <= 0) {
            return str.toByteArray(Charset.defaultCharset())
        }
        val result = ByteArray(length)
        srcBytes = str.toByteArray(Charset.defaultCharset())
        if (srcBytes.size <= length) {
            System.arraycopy(srcBytes, 0, result, 0, srcBytes.size)
        } else {
            System.arraycopy(srcBytes, 0, result, 0, length)
        }
        return result
    }

    fun bytesToString(data: ByteArray?): String? {
        return if (data == null || data.size <= 0) null else String(
            data,
            Charset.defaultCharset()
        ).trim { it <= ' ' }
    }

    fun isEmpty(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' }.isEmpty()
    }
}
