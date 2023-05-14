package android.util

class WifiManager {
    fun getConnectionInfo(): WifiInfo? {
        return null
    }

    fun getConfiguredNetworks(): List<WifiConfiguration> {
        return emptyList()
    }

}

class WifiInfo {
    fun getSSID(): String = ""
    fun getNetworkId(): Int {
        return -1
    }
}

class WifiConfiguration {
    val SSID: String? = null
    val networkId: Int = 1
}

class ConnectivityManager {
    fun getActiveNetworkInfo(): NetworkInfo {
        return NetworkInfo()
    }

    fun getNetworkInfo(typeWifi: Any): NetworkInfo {
        return NetworkInfo()
    }

    companion object {
        val TYPE_WIFI = 0
    }

}

class NetworkInfo {
    fun getExtraInfo(): String? {
        return null
    }

    fun isConnected(): Boolean {
        return false
    }

}