package json

class JSONArray(val str: String = "") {
    fun length(): Int = 0
    fun get(index: Int): Any? = null
    fun put(index: Int, value: Any?): Any? = null
    fun put(value: Any?): Any? = null
    fun getJSONObject(index: Int): JSONObject? {
return null
    }
}

class JSONObject(json: String = "") {
    fun put(key: String, value: Any?) = {}
    fun has(key: String?): Boolean = false
    fun getJSONArray(key: String?): JSONArray {
        error("F")
    }

    fun getString(key: String?): String {
        return ""
    }

    fun getInt(key: String?): Int {
        return 0
    }

    fun getLong(key: String?): Long {
        return 0L
    }

    fun getBoolean(key: String?): Boolean {
        return false

    }

    fun getJSONObject(key: String?): JSONObject {
        error("")
    }
}

class JSONException : Throwable()