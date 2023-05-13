package com.ttlock.bl.sdk.executor

import java.lang.Runnable
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Executor that runs a task on a new background thread.
 */
class DiskIOThreadExecutor : Executor {
    private val mDiskIO: ExecutorService

    init {
        mDiskIO = Executors.newFixedThreadPool(1)
    }

    override fun execute(@NonNull command: Runnable) {
        mDiskIO.execute(command)
        mDiskIO.shutdown()
    }
}
