package android.util

object Log {

    fun isLoggable(tag: String, level: Int): Boolean {
        return true
    }

    fun getStackTraceString(th: Throwable): String? {
        return null
    }

    fun println(level: Int, tag: String, msg: String?): Int {
        return 0
    }

    fun v(tag: String, msg: String?, th: Throwable? = null): Int {
        return 0
    }

    fun i(tag: String, msg: String?, th: Throwable? = null): Int {
        return 0
    }

    fun d(tag: String, msg: String?, th: Throwable? = null): Int {
        return 0
    }

    fun w(tag: String, msg: String?, th: Throwable? = null): Int {
        return 0
    }

    fun w(tag: String, th: Throwable? = null): Int {
        return 0
    }

    fun e(tag: String, msg: String?, th: Throwable? = null): Int {
        return 0
    }
}