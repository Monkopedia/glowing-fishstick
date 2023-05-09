package com.ttlock.bl.sdk.util;

import android.util.Log;

import com.ttlock.bl.sdk.BuildConfig;


/**
 * Created by Sciener on 2016/5/9.
 */
public class LogUtil {

    private static boolean DBG = BuildConfig.DEBUG;

    private static String callerClazzName;

    private static String callerMethodName;

    private static int callerLineNumber;

    private static final String msg = "%s(L:%d) - %s";

    public static void setDBG(boolean DBG) {
        LogUtil.DBG = DBG;
    }

    public static boolean isDBG() {
        return DBG;
    }

    private static void generateCallerInfo() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        callerClazzName = caller.getClassName();
        int index = callerClazzName.lastIndexOf(".");
        if (index + 1 < callerClazzName.length())
            callerClazzName = callerClazzName.substring(index + 1);
        callerMethodName = caller.getMethodName();
        callerLineNumber = caller.getLineNumber();

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

    public synchronized static void d(String content) {
//        d(content, true);
        if(LogUtil.DBG) {
            generateCallerInfo();
            Log.d(callerClazzName, String.format(msg, callerMethodName, callerLineNumber, content));
        }
    }

    public synchronized static void i(String content) {
        d(content, true);
    }

    public synchronized static void w(String content) {
        w(content, true);
    }

    public synchronized static void e(String content) {
        d(content, true);
    }

    public synchronized static void d(String content, boolean DBG) {
        if(LogUtil.DBG && DBG) {
            generateCallerInfo();
            Log.d(callerClazzName, String.format(msg, callerMethodName, callerLineNumber, content));
        }
    }

    public synchronized static void i(String content, boolean DBG) {
        if(LogUtil.DBG && DBG) {
            generateCallerInfo();
            Log.i(callerClazzName, String.format(msg, callerMethodName, callerLineNumber, content));
        }
    }

    public synchronized static void w(String content, boolean DBG) {
        if(LogUtil.DBG && DBG) {
            generateCallerInfo();
            Log.w(callerClazzName, String.format(msg, callerMethodName, callerLineNumber, content));
        }
    }

    public synchronized static void e(String content, boolean DBG) {
        if(LogUtil.DBG && DBG) {
            generateCallerInfo();
            Log.e(callerClazzName, String.format(msg, callerMethodName, callerLineNumber, content));
        }
    }
}
