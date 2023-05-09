package com.scaf.android.client;

import android.util.Log;

public class CodecUtils {

	public static native byte[] encode(byte[] sourcByteArr);
	public static native byte[] decode(byte[] sourcByteArr);
	public static native byte[] encodeWithEncrypt(byte[] sourcByteArr,byte enctypt);
	public static native byte[] decodeWithEncrypt(byte[] sourcByteArr,byte enctypt);
	public static native byte crccompute(byte[] sourcByteArr);
	public static native String  stringFromJNI();
	
    /* this is used to load the 'SCAFCodec' library on application
     * startup. The library has already been unpacked into
     * /data/data/com.scaf.android.client/lib/libSCAFCodec.so at
     * installation time by the package manager.
     */
	
    static {
    	Log.i("tag", "初始化本地jni代码");
        System.loadLibrary("LockCore");
    }

}
