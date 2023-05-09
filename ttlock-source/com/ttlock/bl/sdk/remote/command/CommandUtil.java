package com.ttlock.bl.sdk.remote.command;

import com.ttlock.bl.sdk.device.Remote;
import com.ttlock.bl.sdk.util.DigitUtil;
import com.ttlock.bl.sdk.remote.api.GattCallbackHelper;
import com.ttlock.bl.sdk.remote.model.ConnectParam;

/**
 * Created by TTLock on 2019/3/12.
 */

public class CommandUtil {

    public static void setLock(ConnectParam param, Remote keyFob) {
        //门锁蓝牙地址（6 Bytes）	AES Key（16 Bytes）	约定数（4 Bytes）
        byte[] data = new byte[6 + 16 + 4];
        Command command = new Command(Command.COMM_SET_LOCK);
        try {
            command.setMac(keyFob.getAddress());
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

//    public static void readDeviceFeature(WirelessKeypad keypad) {
//        Command command = new Command(Command.COMM_READ_FEATURE);
//        command.setMac(keypad.getAddress());
//        GattCallbackHelper.getInstance().sendCommand(command.buildCommand());
//    }

}
