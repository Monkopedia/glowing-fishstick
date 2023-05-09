package com.ttlock.bl.sdk.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by TTLock on 2017/3/21.
 */
public class NetworkUtil {
    public static String getWifiSSid(Context context) {
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
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ssid = "";
        WifiInfo wifiInfo = null;
        try {
            if (wifiManager != null) {
                wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    ssid = wifiInfo.getSSID();
                    ssid = ssid.replace("\"", "");
                }
            }

            if (ssid.equals("<unknown ssid>")) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager != null) {
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    ssid = networkInfo.getExtraInfo();
                    if (ssid != null)
                        ssid = ssid.replace("\"", "");
                }
            }

            if (TextUtils.isEmpty(ssid) && wifiInfo != null) {
                int networkId = wifiInfo.getNetworkId();
                List<WifiConfiguration> netConfList = wifiManager.getConfiguredNetworks();
                for (WifiConfiguration wificonf:netConfList)
                    if (wificonf.networkId == networkId) {
                        ssid = wificonf.SSID;
                        break;
                    }
                if (ssid != null)
                    ssid = ssid.replace("\"", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssid;
    }

    /**
     * 判断网络是否连接
     * @return
     */
    public static boolean isNetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean flag = false;
        if(connectivityManager != null) {
            NetworkInfo networkInfo =  connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null)
            flag = networkInfo.isConnected();
        }
        return flag;
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo.isConnected();
    }

}
