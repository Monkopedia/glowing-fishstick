package okhttp

import java.util.concurrent.TimeUnit

class Call {
    fun execute(): Response {
        return Response()
    }

}

class OkHttpClient {
    fun newCall(request: Request): Call {
        return Call()
    }

    class Builder {
        fun readTimeout(i: Int, seconds: TimeUnit): Builder = apply {

        }

        fun build(): OkHttpClient {
            return OkHttpClient()
        }
    }
}

class MediaType {
    companion object {
        fun parse(s: String): MediaType {
            return MediaType()
        }

    }
}

class Request {
    class Builder {
        fun url(newUrl: String?): Request.Builder = apply{

        }

        fun build(): Request {
            return Request()
        }

        fun post(body: RequestBody): Request.Builder =apply{

        }

    }

}
class Response {
    fun isSuccessful(): Boolean {
        return true
    }

    fun body(): Body {

    }

    class Body {
        fun string(): String = ""
    }
}
class RequestBody {
    companion object {
        fun create(default: MediaType, params: String?): RequestBody {
            return RequestBody()
        }

    }
}


class FormBody {
    class Builder {
        fun add(key: String?, value: String) {

        }

        fun build(): RequestBody {

        }
    }
}