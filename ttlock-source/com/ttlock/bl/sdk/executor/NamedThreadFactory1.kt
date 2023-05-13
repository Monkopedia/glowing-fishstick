package com.ttlock.bl.sdk.executor

import java.lang.Runnable
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class NamedThreadFactory : ThreadFactory {
    private val group: ThreadGroup
    private val threadNumber = AtomicInteger(1)
    private val namePrefix: String

    internal constructor(nameTitle: String) {
        val s = System.getSecurityManager()
        group = if (s != null) s.threadGroup else Thread.currentThread().threadGroup
        namePrefix = nameTitle +
            NamedThreadFactory.Companion.poolNumber.getAndIncrement() +
            "-ttlock-sdk-thread-"
    }

    internal constructor() {
        val s = System.getSecurityManager()
        group = if (s != null) s.threadGroup else Thread.currentThread().threadGroup
        namePrefix = "ttlock-pool" +
            NamedThreadFactory.Companion.poolNumber.getAndIncrement() +
            "-thread-"
    }

    override fun newThread(r: Runnable): Thread {
        val t = Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0)
        if (t.isDaemon) {
            t.isDaemon = false
        }
        if (t.priority != Thread.NORM_PRIORITY) {
            t.priority = Thread.NORM_PRIORITY
        }
        return t
    }

    companion object {
        private val poolNumber = AtomicInteger(1)
    }
}
