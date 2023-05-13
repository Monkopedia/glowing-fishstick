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

    fun getSystemService(name: String): Any = BluetoothManager()
    fun getCacheDir(): File {
        error("")
    }

    companion object {
        const val BLUETOOTH_SERVICE = "bluetooth_service"
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
