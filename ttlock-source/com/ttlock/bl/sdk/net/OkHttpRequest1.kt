package com.ttlock.bl.sdk.net

import android.util.TextUtils
import com.ttlock.bl.sdk.util.LogUtil
import okhttp.FormBody
import okhttp.MediaType
import okhttp.OkHttpClient
import okhttp.Request
import okhttp.Request.Builder
import okhttp.RequestBody
import okhttp.Response
import java.io.IOException
import java.util.concurrent.TimeUnit


class OkHttpRequest private constructor() {
    init {
        throw AssertionError()
    }

    companion object {
        private const val DBG = true
        private val client: OkHttpClient = OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS).build()
        private val DEFAULT: MediaType = MediaType.parse("application/x-www-form-urlencoded")
        private val JSON: MediaType = MediaType.parse("application/json; charset=utf-8")
        private fun getParamUrl(params: Map<String, String>?): String {
            if (params == null || params.isEmpty()) {
                return ""
            }
            val sb = StringBuilder()
            for (key in params.keys) sb.append(key).append('=').append(params[key]).append('&')
            // 		params.forEach((key, value) -> sb.append(key).append('=').append(value).append('&'));
            return sb.substring(0, sb.length - 1)
        }

        @JvmOverloads
        operator fun get(url: String, params: Map<String, String>? = null): String {
            val paramUrl = getParamUrl(params)
            val newUrl = if (TextUtils.isEmpty(paramUrl)) url else "$url?$paramUrl"
            val requestBuilder: Request.Builder = Builder().url(newUrl)
            // 		if (headers != null) {
// 			headers.forEach((key, value) -> requestBuilder.addHeader(Utils.parseString(key), Utils.parseString(value)));
// 		}
            val request: Request = requestBuilder.build()
            val response: Response
            try {
                response = client.newCall(request).execute()
                if (!response.isSuccessful()) {
                    throw IOException("Unexpected code $response")
                }
                return response.body().string()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ""
        }

        fun sendPost(url: String, params: Map<String?, String?>?): String {
            val formBodyBuilder: FormBody.Builder = FormBody.Builder()
            LogUtil.d("url:$url", DBG)
            if (params != null) {
                for (key in params.keys) {
                    var value = params[key]
                    if (value == null) value = ""
                    LogUtil.d(String.format("%s:%s", key, value), DBG)
                    formBodyBuilder.add(key, value)
                }
                // 			params.forEach((key, value) -> formBodyBuilder.add(key, value));
            }
            val body: RequestBody = formBodyBuilder.build()
            val requestBuilder: Request.Builder = Builder().url(url).post(body)
            // 		if (headers != null) {
// 			headers.forEach((key, value) -> requestBuilder.addHeader(key, value));
// 		}
            val request: Request = requestBuilder.build()
            val response: Response
            try {
                response = client.newCall(request).execute()
                if (!response.isSuccessful()) {
                    throw IOException("Unexpected code $response")
                }
                val responseData: String = response.body().string()
                LogUtil.d("responseData:$responseData", DBG)
                return responseData
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ""
        }

        /**
         * json数据请求
         *
         * @param url
         * @param params 请求参数
         * @return
         */
        fun sendPost(url: String?, params: String?): String {
            val body: RequestBody = RequestBody.create(DEFAULT, params)
            val requestBuilder: Request.Builder = Builder().url(url).post(body)
            val request: Request = requestBuilder.build()
            val response: Response
            try {
                response = client.newCall(request).execute()
                if (!response.isSuccessful()) {
                    throw IOException("Unexpected code $response")
                }
                return response.body().string()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}
