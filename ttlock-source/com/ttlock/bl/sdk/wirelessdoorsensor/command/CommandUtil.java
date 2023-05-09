package com.ttlock.bl.sdk.wirelessdoorsensor.command;

import com.ttlock.bl.sdk.device.WirelessDoorSensor;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.wirelessdoorsensor.GattCallbackHelper;
import com.ttlock.bl.sdk.wirelessdoorsensor.model.ConnectParam;

/**
 * Created by TTLock on 2019/3/12.
 */

public class CommandUtil {

    public static void setLock(ConnectParam param, WirelessDoorSensor doorSensor) {
        //门锁蓝牙地址（6 Bytes）	AES Key（16 Bytes）	约定数（4 Bytes）
        byte[] data = new byte[6 + 16 + 4];
        Command command = new Command(Command.COMM_SET_LOCK);
        try {
            command.setMac(doorSensor.getAddress());
            //低在前高在后
            byte[] lockmacBytes = DigitUtil.getReverseMacArray(param.getLockmac());
            System.arraycopy(lockmacBytes, 0, data, 0, 6);

            byte[] aeskeyBytes = DigitUtil.convertAesKeyStrToBytes(param.getAesKey());
            System.arraycopy(aeskeyBytes, 0, data, 6, 16);

            String lockKey = param.getLockKey();
            byte[] lockKeyBytes = DigitUtil.integerToByteArray(Integer.valueOf(DigitUtil.decodeLockData(lockKey)));
            System.arraycopy(lockKeyBytes, 0, data, 22, 4);

            command.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    }

    public static void checkAdmin(WirelessDoorSensor doorSensor) {
        //管理员校验都通过
        byte[] data = new byte[11];
        Command command = new Command(Command.COMM_CHECK_ADMIN);
        command.setMac(doorSensor.getAddress());
        command.setData(data);
        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    }

    /**
     * 随机数与约定数求和 验证
     * @param param
     * @param responseRandom
     */
    public static void checkRandom(WirelessDoorSensor doorSensor, ConnectParam param, long responseRandom) {
        Command command = new Command(Command.COMM_CHECK_RANDOM);
        command.setMac(doorSensor.getAddress());
        long lockKeyValue = Long.valueOf(DigitUtil.decodeLockData(param.getLockKey()));
        byte[] sumBytes = DigitUtil.getUnlockPwdBytes_new(responseRandom, lockKeyValue);
        command.setData(sumBytes);
        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
    }

    public static void enterDfu(WirelessDoorSensor doorSensor) {
        Command command = new Command(Command.COMM_ENTER_DFU);
        command.setMac(doorSensor.getAddress());
        command.setData("SCIENER".getBytes());
        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
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
