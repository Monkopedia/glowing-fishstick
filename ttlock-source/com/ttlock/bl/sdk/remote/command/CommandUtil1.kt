package com.ttlock.bl.sdk.remote.command

import com.ttlock.bl.sdk.device.Remote
import com.ttlock.bl.sdk.remote.api.GattCallbackHelper
import com.ttlock.bl.sdk.remote.model.ConnectParam
import com.ttlock.bl.sdk.util.DigitUtil
import java.lang.Exception

/**
 * Created by TTLock on 2019/3/12.
 */
object CommandUtil {
    fun setLock(param: ConnectParam, keyFob: Remote) {
        // 门锁蓝牙地址（6 Bytes）	AES Key（16 Bytes）	约定数（4 Bytes）
        val data = ByteArray(6 + 16 + 4)
        val command: Command = Command(Command.Companion.COMM_SET_LOCK)
        try {
            command.mac = keyFob.address
            // 低在前高在后
            val lockmacBytes: ByteArray = DigitUtil.getReverseMacArray(param.lockmac)
            System.arraycopy(lockmacBytes, 0, data, 0, 6)
            val aeskeyBytes: ByteArray = DigitUtil.convertAesKeyStrToBytes(param.aesKey)
            System.arraycopy(aeskeyBytes, 0, data, 6, 16)
            val lockKey = param.lockKey
            val lockKeyBytes: ByteArray =
                DigitUtil.integerToByteArray(Integer.valueOf(DigitUtil.decodeLockData(lockKey)))
            System.arraycopy(lockKeyBytes, 0, data, 22, 4)
            command.setData(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        GattCallbackHelper.Companion.getInstance().sendCommand(command.buildCommand())
    } //    public static void readDeviceFeature(WirelessKeypad keypad) {
    //        Command command = new Command(Command.COMM_READ_FEATURE);
    //        command.setMac(keypad.getAddress());
    //        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    //    }
}
