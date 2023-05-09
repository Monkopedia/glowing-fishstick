package com.ttlock.bl.sdk.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtil {
    public static byte[] readFirmware(String fileName) {
        try {
            InputStream stream = new FileInputStream(fileName);
            int length = stream.available();
            byte[] firmware = new byte[length];
            stream.read(firmware);
            stream.close();
            return firmware;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
