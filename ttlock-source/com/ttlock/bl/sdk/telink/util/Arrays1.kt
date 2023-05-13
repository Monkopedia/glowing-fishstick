/*
 * Copyright (C) 2015 The Telink Bluetooth Light Project
 *
 */
package com.ttlock.bl.sdk.telink.util

import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.util.*
import kotlin.Throws

/**
 * 数组工具类
 */
object Arrays {
    /**
     * 反转byte数组
     *
     * @param a
     * @return
     */
    fun reverse(a: ByteArray?): ByteArray? {
        if (a == null) return null
        var p1 = 0
        var p2 = a.size
        val result = ByteArray(p2)
        while (--p2 >= 0) {
            result[p2] = a[p1++]
        }
        return result
    }

    /**
     * 反转byte数组中的某一段
     *
     * @param arr
     * @param begin
     * @param end
     * @return
     */
    fun reverse(arr: ByteArray, begin: Int, end: Int): ByteArray {
        var begin = begin
        var end = end
        while (begin < end) {
            val temp = arr[end]
            arr[end] = arr[begin]
            arr[begin] = temp
            begin++
            end--
        }
        return arr
    }

    /**
     * 比较两个byte数组中的每一项值是否相等
     *
     * @param array1
     * @param array2
     * @return
     */
    fun equals(array1: ByteArray?, array2: ByteArray?): Boolean {
        if (array1 == array2) {
            return true
        }
        if (array1 == null || array2 == null || array1.size != array2.size) {
            return false
        }
        for (i in array1.indices) {
            if (array1[i] != array2[i]) {
                return false
            }
        }
        return true
    }

    fun bytesToString(array: ByteArray?): String {
        if (array == null) {
            return "null"
        }
        if (array.size == 0) {
            return "[]"
        }
        val sb = StringBuilder(array.size * 6)
        sb.append('[')
        sb.append(array[0].toInt())
        for (i in 1 until array.size) {
            sb.append(", ")
            sb.append(array[i].toInt())
        }
        sb.append(']')
        return sb.toString()
    }

    @Throws(UnsupportedEncodingException::class)
    fun bytesToString(data: ByteArray?, charsetName: String?): String {
        return String(data!!, charsetName)
    }

    /**
     * byte数组转成十六进制字符串
     *
     * @param array     原数组
     * @param separator 分隔符
     * @return
     */
    fun bytesToHexString(array: ByteArray?, separator: String?): String {
        if (array == null || array.size == 0) return ""
        val sb = StringBuilder()
        val formatter = Formatter(sb)
        formatter.format("%02X", array[0])
        for (i in 1 until array.size) {
            if (!Strings.isEmpty(separator)) sb.append(separator)
            formatter.format("%02X", array[i])
        }
        formatter.flush()
        formatter.close()
        return sb.toString()
    }

    fun hexToBytes(hexStr: String): ByteArray {
        var hexStr = hexStr
        if (hexStr.length == 1) {
            hexStr = "0$hexStr"
        }
        val length = hexStr.length / 2
        val result = ByteArray(length)
        for (i in 0 until length) {
            result[i] = hexStr.substring(i * 2, i * 2 + 2).toInt(16).toByte()
        }
        return result
    }

    fun bytesToInt(src: ByteArray, offset: Int): Int {
        if (src.size != 4) return 0
        val value: Int
        value = (
            src[offset] and 0xFF
                or (src[offset + 1] and 0xFF shl 8)
                or (src[offset + 2] and 0xFF shl 16)
                or (src[offset + 3] and 0xFF shl 24)
            )
        return value
    }
}
