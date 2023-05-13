package com.scaf.android.client

import android.util.Log

object CodecUtils {
    external fun encode(sourcByteArr: ByteArray?): ByteArray?
    external fun decode(sourcByteArr: ByteArray?): ByteArray?
    external fun encodeWithEncrypt(sourcByteArr: ByteArray?, enctypt: Byte): ByteArray?
    external fun decodeWithEncrypt(sourcByteArr: ByteArray?, enctypt: Byte): ByteArray?
    external fun crccompute(sourcByteArr: ByteArray?): Byte
    external fun stringFromJNI(): String?

    /* this is used to load the 'SCAFCodec' library on application
     * startup. The library has already been unpacked into
     * /data/data/com.scaf.android.client/lib/libSCAFCodec.so at
     * installation time by the package manager.
     */
    init {
        Log.i("tag", "初始化本地jni代码")
        System.loadLibrary("LockCore")
    }
}
