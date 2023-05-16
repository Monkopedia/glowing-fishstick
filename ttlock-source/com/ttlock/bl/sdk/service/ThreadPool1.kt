package com.ttlock.bl.sdk.service

import com.ttlock.bl.sdk.util.LogUtil
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by TTLock on 2016/9/8 0008.
 */
object ThreadPool {
    // 线程个数
    private val threadCount = Runtime.getRuntime().availableProcessors() * 2
    private var mThreadPool: ExecutorService? = null
    fun getThreadPool(): ExecutorService {
        if (ThreadPool.mThreadPool == null) {
            synchronized(ExecutorService::class.java) {
                if (ThreadPool.mThreadPool == null) {
                    LogUtil.d("threadCount:" + ThreadPool.threadCount, true)
                    ThreadPool.mThreadPool = Executors.newFixedThreadPool(ThreadPool.threadCount)
                }
            }
        }
        return ThreadPool.mThreadPool!!
    }
}
