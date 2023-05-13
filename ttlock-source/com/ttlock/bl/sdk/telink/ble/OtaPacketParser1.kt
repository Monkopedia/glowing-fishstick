package com.ttlock.bl.sdk.telink.ble

import com.ttlock.bl.sdk.telink.util.Arrays
import com.ttlock.bl.sdk.telink.util.TelinkLog

class OtaPacketParser {
    private var total = 0
    private var index = -1
    private var data: ByteArray?
    private var progress = 0
    fun set(data: ByteArray?) {
        this.clear()
        this.data = data
        val length = this.data!!.size
        val size = 16
        total = if (length % size == 0) {
            length / size
        } else {
            Math.floor((length / size + 1).toDouble()).toInt()
        }
    }

    fun clear() {
        progress = 0
        total = 0
        index = -1
        data = null
    }

    fun hasNextPacket(): Boolean {
        return total > 0 && index + 1 < total
    }

    fun isLast(): Boolean {
        return index + 1 == total
    }

    fun getNextPacketIndex(): Int {
        return index + 1
    }

    fun getNextPacket(): ByteArray {
        val index = getNextPacketIndex()
        val packet = getPacket(index)
        this.index = index
        return packet
    }

    fun getPacket(index: Int): ByteArray {
        val length = data!!.size
        val size = 16
        var packetSize: Int
        packetSize = if (length > size) {
            if (index + 1 == total) {
                length - index * size
            } else {
                size
            }
        } else {
            length
        }
        packetSize = packetSize + 4
        val packet = ByteArray(20)
        for (i in 0..19) {
            packet[i] = 0xFF.toByte()
        }
        System.arraycopy(data, index * size, packet, 2, packetSize - 4)
        fillIndex(packet, index)
        val crc = crc16(packet)
        fillCrc(packet, crc)
        TelinkLog.d(
            "ota packet ---> index : " + index + " total : " + total + " crc : " + crc + " content : " + Arrays.bytesToHexString(
                packet,
                ":"
            )
        )
        return packet
    }

    fun getCheckPacket(): ByteArray {
        val packet = ByteArray(16)
        for (i in 0..15) {
            packet[i] = 0xFF.toByte()
        }
        val index = getNextPacketIndex()
        fillIndex(packet, index)
        val crc = crc16(packet)
        fillCrc(packet, crc)
        TelinkLog.d(
            "ota check packet ---> index : " + index + " crc : " + crc + " content : " + Arrays.bytesToHexString(
                packet,
                ":"
            )
        )
        return packet
    }

    fun fillIndex(packet: ByteArray, index: Int) {
        var offset = 0
        packet[offset++] = (index and 0xFF).toByte()
        packet[offset] = (index shr 8 and 0xFF).toByte()
    }

    fun fillCrc(packet: ByteArray, crc: Int) {
        var offset = packet.size - 2
        packet[offset++] = (crc and 0xFF).toByte()
        packet[offset] = (crc shr 8 and 0xFF).toByte()
    }

    fun crc16(packet: ByteArray): Int {
        val length = packet.size - 2
        val poly = shortArrayOf(0, 0xA001.toShort())
        var crc = 0xFFFF
        var ds: Int
        for (j in 0 until length) {
            ds = packet[j].toInt()
            for (i in 0..7) {
                crc = crc shr 1 xor poly[crc xor ds and 1].toInt() and 0xFFFF
                ds = ds shr 1
            }
        }
        return crc
    }

    fun invalidateProgress(): Boolean {
        val a = getNextPacketIndex().toFloat()
        val b = total.toFloat()
        val progress = Math.floor((a / b * 100).toDouble()).toInt()
        if (progress == this.progress) return false
        this.progress = progress
        return true
    }

    fun getProgress(): Int {
        return progress
    }

    fun getIndex(): Int {
        return index
    }

    fun getTotal(): Int {
        return total
    }
}
