package com.ttlock.bl.sdk.util

import android.util.Gson
import android.util.TypeToken
import json.JSONArray
import json.JSONException
import json.JSONObject

/**
 * Created by Smartlock on 2016/6/2.
 */
object GsonUtil {
    var gson: Gson = Gson()
    fun <T> toObject(json: String?, clazz: Class<T>): T {
        return gson.fromJson(json, clazz)
    }

    fun <T> toObject(json: String?, typeToken: TypeToken<T>): T {
        return gson.fromJson(json, typeToken.getType())
    }

    fun <T> toJson(`object`: T): String {
        return gson.toJson(`object`)
    }

    fun getJsonObject(jsonArray: JSONArray?, index: Int): JSONObject? {
        try {
            if (jsonArray != null && index >= 0 && index < jsonArray.length()) {
                return jsonArray.getJSONObject(index)
            }
        } catch (e: JSONException) {
            return null
        }
        return null
    }

    fun getJsonArray(jsonObject: JSONObject?, key: String?): JSONArray {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return jsonObject.getJSONArray(key)
            }
        } catch (e: Exception) {
            return JSONArray()
        }
        return JSONArray()
    }

    fun getJsonStringValue(jsonObject: JSONObject?, key: String?): String {
        return getJsonStringValue(jsonObject, key, "")
    }

    fun getJsonStringValue(jsonObject: JSONObject?, key: String?, defaultValue: String): String {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                var value: String = jsonObject.getString(key).trim()
                if (value == "null") {
                    value = ""
                }
                return value
            }
        } catch (e: Exception) {
            return defaultValue
        }
        return defaultValue
    }

    fun getJsonIntegerValue(json: JSONObject?, key: String?): Int {
        return getJsonIntegerValue(json, key, 0)
    }

    fun getJsonIntegerValue(jsonObject: JSONObject?, key: String?, defaultValue: Int): Int {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return jsonObject.getInt(key)
            }
        } catch (e: Exception) {
            return defaultValue
        }
        return defaultValue
    }

    fun getJsonLongValue(json: JSONObject?, key: String?): Long {
        return getJsonLongValue(json, key, 0L)
    }

    fun getJsonLongValue(jsonObject: JSONObject?, key: String?, defaultValue: Long): Long {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return jsonObject.getLong(key)
            }
        } catch (e: Exception) {
            return defaultValue
        }
        return defaultValue
    }

    fun getJsonFloatValue(jsonObject: JSONObject?, key: String?, defaultValue: Float): Float {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return java.lang.Float.valueOf(jsonObject.getString(key))
            }
        } catch (e: Exception) {
            return defaultValue
        }
        return defaultValue
    }

    fun getJsonBooleanValue(jsonObject: JSONObject?, key: String?, defaultValue: Boolean): Boolean {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return jsonObject.getBoolean(key)
            }
        } catch (e: Exception) {
            return defaultValue
        }
        return defaultValue
    }

    fun getJsonObject(jsonObject: JSONObject?, key: String?): JSONObject {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return jsonObject.getJSONObject(key)
            }
        } catch (e: Exception) {
            return JSONObject(json)
        }
        return JSONObject(json)
    }
}
