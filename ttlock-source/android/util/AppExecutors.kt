package android.util

import java.util.concurrent.Executor

class AppExecutors {
    fun mainThread(): Executor = Executor { it.run() }
}