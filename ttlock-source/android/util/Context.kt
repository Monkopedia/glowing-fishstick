package android.util

import android.bluetooth.BluetoothManager
import java.io.File

class Context {
    fun getPackageName(): String = ""

    fun registerReceiver(receiver: BroadcastReceiver, intentFilter: IntentFilter) {
    }

    fun registerReceiver(
        receiver: BroadcastReceiver,
        intentFilter: IntentFilter,
        permission: String,
        other: String?
    ) {
    }

    fun unregisterReceiver(receiver: BroadcastReceiver) {}

    fun getSystemService(name: String): Any = if (name == BLUETOOTH_SERVICE) BluetoothManager()  else if (WIFI_SERVICE == name) WifiManager() else ConnectivityManager()
    fun getCacheDir(): File {
        error("")
    }

    companion object {
        const val WIFI_SERVICE: String = "wifi_service"
        const val BLUETOOTH_SERVICE = "bluetooth_service"
        const val CONNECTIVITY_SERVICE = "connectivity_service"
    }
}

fun getContext(): Context {
    return Context()
}

abstract class BroadcastReceiver() {
    abstract fun onReceive(context: Context?, intent: Intent)
}

class Intent {
    fun getIntExtra(key: String, def: Int): Int = def
}

class IntentFilter(val action: String = "")

class LocalBroadcastManager {
    companion object {
        fun getInstance(context: Context): LocalBroadcastManager = LocalBroadcastManager()
    }
}
