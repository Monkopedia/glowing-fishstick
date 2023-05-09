package com.ttlock.bl.sdk.service;

import com.ttlock.bl.sdk.util.LogUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by TTLock on 2016/9/8 0008.
 */
public class ThreadPool {
    //线程个数
    private static final int threadCount = Runtime.getRuntime().availableProcessors() * 2;

    private static ExecutorService mThreadPool = null;

    public static ExecutorService getThreadPool() {
        if(mThreadPool == null) {
            synchronized (ExecutorService.class) {
                if(mThreadPool == null) {
                    LogUtil.d("threadCount:" + threadCount, true);
                    mThreadPool = Executors.newFixedThreadPool(threadCount);
                }
            }
        }
        return mThreadPool;
    }
}
