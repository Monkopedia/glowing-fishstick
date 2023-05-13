package android.util

class Gson {
    fun toJson(lockVersion: Any?): String = ""

    fun <T> fromJson(lockVersionString: String?, java: Class<T>): T = error("")
}

fun <T> toObject(str: String, token: TypeToken<T>): T {
    error("Fail")
}

open class TypeToken<T> {
    fun getType(): Class<T> {
        error("")
    }
}
