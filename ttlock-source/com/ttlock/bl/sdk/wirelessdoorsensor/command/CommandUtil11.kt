package com.ttlock.bl.sdk.wirelessdoorsensor.command

import com.ttlock.bl.sdk.device.WirelessDoorSensor
import com.ttlock.bl.sdk.util.DigitUtil
import com.ttlock.bl.sdk.wirelessdoorsensor.GattCallbackHelper
import com.ttlock.bl.sdk.wirelessdoorsensor.model.ConnectParam
import java.lang.Exception

/**
 * Created by TTLock on 2019/3/12.
 */
object CommandUtil {
    fun setLock(param: ConnectParam, doorSensor: WirelessDoorSensor) {
        // 门锁蓝牙地址（6 Bytes）	AES Key（16 Bytes）	约定数（4 Bytes）
        val data = ByteArray(6 + 16 + 4)
        val command: Command = Command(Command.Companion.COMM_SET_LOCK)
        try {
            command.mac = doorSensor.getAddress()
            // 低在前高在后
            val lockmacBytes: ByteArray = DigitUtil.getReverseMacArray(param.getLockmac()!!)!!
            System.arraycopy(lockmacBytes, 0, data, 0, 6)
            val aeskeyBytes: ByteArray = DigitUtil.convertAesKeyStrToBytes(param.getAesKey()!!)!!
            System.arraycopy(aeskeyBytes, 0, data, 6, 16)
            val lockKey = param.getLockKey()
            val lockKeyBytes: ByteArray =
                DigitUtil.integerToByteArray(Integer.valueOf(DigitUtil.decodeLockData(lockKey!!)))
            System.arraycopy(lockKeyBytes, 0, data, 22, 4)
            command.setData(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        GattCallbackHelper.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun checkAdmin(doorSensor: WirelessDoorSensor) {
        // 管理员校验都通过
        val data = ByteArray(11)
        val command: Command = Command(Command.Companion.COMM_CHECK_ADMIN)
        command.mac = doorSensor.getAddress()
        command.setData(data)
        GattCallbackHelper.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 随机数与约定数求和 验证
     * @param param
     * @param responseRandom
     */
    fun checkRandom(doorSensor: WirelessDoorSensor, param: ConnectParam, responseRandom: Long) {
        val command: Command = Command(Command.Companion.COMM_CHECK_RANDOM)
        command.mac = doorSensor.getAddress()
        val lockKeyValue: Long = java.lang.Long.valueOf(DigitUtil.decodeLockData(param.lockKey))
        val sumBytes: ByteArray = DigitUtil.getUnlockPwdBytes_new(responseRandom, lockKeyValue)
        command.setData(sumBytes)
        GattCallbackHelper.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun enterDfu(doorSensor: WirelessDoorSensor) {
        val command: Command = Command(Command.Companion.COMM_ENTER_DFU)
        command.mac = doorSensor.address
        command.setData("SCIENER".toByteArray())
        GattCallbackHelper.Companion.getInstance().sendCommand(command.buildCommand())
    }
    /**
     *
     * @param doorSensorMac
     * @param time
     * 门未关超时时间，单位秒
     * （2 bytes）
     * 高在前，低在后
     */
    //    public static void doorNotClosedWarning(String doorSensorMac, int time) {
    //        Command command = new Command(Command.COMM_DOOR_NOT_CLOSED_WARNING);
    //        command.setMac(doorSensorMac);
    //        byte[] timeByte = new byte[2];
    //        timeByte[0] = (byte) (time >> 8);
    //        timeByte[1] = (byte) time;
    //        command.setData(timeByte);
    //        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    //    }
}
