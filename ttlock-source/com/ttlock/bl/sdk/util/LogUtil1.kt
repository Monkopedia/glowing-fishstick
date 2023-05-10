package com.ttlock.bl.sdk.util

import android.util.Log

/**
 * Created by Sciener on 2016/5/9.
 */
object LogUtil {
    private var DBG: Boolean = BuildConfig.DEBUG
    private var callerClazzName: String? = null
    private var callerMethodName: String? = null
    private var callerLineNumber = 0
    private const val msg = "%s(L:%d) - %s"
    fun setDBG(DBG: Boolean) {
        LogUtil.DBG = DBG
    }

    fun isDBG(): Boolean {
        return DBG
    }

    private fun generateCallerInfo() {
        val caller = Throwable().stackTrace[2]
        callerClazzName = caller.className
        val index = callerClazzName.lastIndexOf(".")
        if (index + 1 < callerClazzName.length) callerClazzName =
            callerClazzName.substring(index + 1)
        callerMethodName = caller.methodName
        callerLineNumber = caller.lineNumber

        //TODO:测试  增加一个标志位进行判断是否输出
//        try {
//            if (callerMethodName.contains("disconnect")) {
//                caller = new Throwable().getStackTrace()[3];
//                Log.d(caller.getClassName().substring(caller.getClassName().lastIndexOf(".") + 1), String.format(msg, caller.getMethodName(), caller.getLineNumber(), "last caller"));
//                if (new Throwable().getStackTrace().length > 4) {
//                    caller = new Throwable().getStackTrace()[4];
//                    Log.d(caller.getClassName().substring(caller.getClassName().lastIndexOf(".") + 1), String.format(msg, caller.getMethodName(), caller.getLineNumber(), "last caller"));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Synchronized
    fun d(content: String?) {
//        d(content, true);
        if (DBG) {
            generateCallerInfo()
            Log.d(callerClazzName, String.format(msg, callerMethodName, callerLineNumber, content))
        }
    }

    @Synchronized
    fun i(content: String?) {
        d(content, true)
    }

    @Synchronized
    fun w(content: String?) {
        w(content, true)
    }

    @Synchronized
    fun e(content: String?) {
        d(content, true)
    }

    @Synchronized
    fun d(content: String?, DBG: Boolean) {
        if (LogUtil.DBG && DBG) {
            generateCallerInfo()
            Log.d(callerClazzName, String.format(msg, callerMethodName, callerLineNumber, content))
        }
    }

    @Synchronized
    fun i(content: String?, DBG: Boolean) {
        if (LogUtil.DBG && DBG) {
            generateCallerInfo()
            Log.i(callerClazzName, String.format(msg, callerMethodName, callerLineNumber, content))
        }
    }

    @Synchronized
    fun w(content: String?, DBG: Boolean) {
        if (LogUtil.DBG && DBG) {
            generateCallerInfo()
            Log.w(callerClazzName, String.format(msg, callerMethodName, callerLineNumber, content))
        }
    }

    @Synchronized
    fun e(content: String?, DBG: Boolean) {
        if (LogUtil.DBG && DBG) {
            generateCallerInfo()
            Log.e(callerClazzName, String.format(msg, callerMethodName, callerLineNumber, content))
        }
    }
}