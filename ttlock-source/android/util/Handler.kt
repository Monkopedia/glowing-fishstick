package android.util

class Handler(val looper: Looper = Looper()) {

    fun post(r: Runnable) {}
    fun postDelayed(r: Runnable, delay: Long) {}
    fun postDelayed(r: Runnable, delay: Int) {}
    fun removeCallbacks(r: Runnable) {}
    fun removeCallbacksAndMessages(v: Any?) {}
}
class Looper {
    companion object {
        fun getMainLooper(): Looper = Looper()
    }
}