package com.ttlock.bl.sdk.util

import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

object IOUtil {
    fun readFirmware(fileName: String?): ByteArray? {
        try {
            val stream: InputStream = FileInputStream(fileName)
            val length = stream.available()
            val firmware = ByteArray(length)
            stream.read(firmware)
            stream.close()
            return firmware
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}
