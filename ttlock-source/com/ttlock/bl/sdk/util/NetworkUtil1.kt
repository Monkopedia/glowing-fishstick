package com.ttlock.bl.sdk.util

import android.content.Context
import java.lang.Exception

/**
 * Created by TTLock on 2017/3/21.
 */
object NetworkUtil {
    fun getWifiSSid(context: Context): String? {
//        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        String ssid = "";
//        if(wifiManager != null) {
//            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//            if(wifiInfo != null) {
//                ssid = wifiInfo.getSSID();
//                if(ssid.length() > 2 && ssid.charAt(0) == '"' && ssid.charAt(ssid.length() - 1) == '"')
//                    ssid = ssid.substring(1, ssid.length() - 1);
//            }
//        }
        val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var ssid = ""
        var wifiInfo: WifiInfo? = null
        try {
            if (wifiManager != null) {
                wifiInfo = wifiManager.getConnectionInfo()
                if (wifiInfo != null) {
                    ssid = wifiInfo.getSSID()
                    ssid = ssid.replace("\"", "")
                }
            }
            if (ssid == "<unknown ssid>") {
                val connectivityManager: ConnectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (connectivityManager != null) {
                    val networkInfo: NetworkInfo = connectivityManager.getActiveNetworkInfo()
                    ssid = networkInfo.getExtraInfo()
                    if (ssid != null) ssid = ssid.replace("\"", "")
                }
            }
            if (TextUtils.isEmpty(ssid) && wifiInfo != null) {
                val networkId: Int = wifiInfo.getNetworkId()
                val netConfList: List<WifiConfiguration> = wifiManager.getConfiguredNetworks()
                for (wificonf in netConfList) if (wificonf.networkId === networkId) {
                    ssid = wificonf.SSID
                    break
                }
                if (ssid != null) ssid = ssid.replace("\"", "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ssid
    }

    /**
     * 判断网络是否连接
     * @return
     */
    fun isNetConnected(context: Context): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var flag = false
        if (connectivityManager != null) {
            val networkInfo: NetworkInfo = connectivityManager.getActiveNetworkInfo()
            if (networkInfo != null) flag = networkInfo.isConnected()
        }
        return flag
    }

    fun isWifiConnected(context: Context): Boolean {
        val manager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return networkInfo.isConnected()
    }
}
