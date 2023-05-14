package android.util

interface DfuProgressListener{
    fun onDeviceConnecting(deviceAddress: String) = Unit

    fun onDfuProcessStarting(deviceAddress: String) = Unit

    fun onEnablingDfuMode(deviceAddress: String) = Unit

    fun onFirmwareValidating(deviceAddress: String) = Unit

    fun onDeviceDisconnecting(deviceAddress: String) = Unit

    fun onDfuCompleted(deviceAddress: String) = Unit

    fun onDfuAborted(deviceAddress: String) = Unit

    fun onProgressChanged(
        deviceAddress: String,
        percent: Int,
        speed: Float,
        avgSpeed: Float,
        currentPart: Int,
        partsTotal: Int
    ) = Unit

    fun onError(deviceAddress: String, error: Int, errorType: Int, message: String) = Unit
}
open class DfuProgressListenerAdapter : DfuProgressListener{
    override fun onDeviceConnecting(deviceAddress: String) = Unit

    override fun onDfuProcessStarting(deviceAddress: String) = Unit

    override fun onEnablingDfuMode(deviceAddress: String) = Unit

    override fun onFirmwareValidating(deviceAddress: String) = Unit

    override fun onDeviceDisconnecting(deviceAddress: String) = Unit

    override fun onDfuCompleted(deviceAddress: String) = Unit

    override fun onDfuAborted(deviceAddress: String) = Unit

    override fun onProgressChanged(
        deviceAddress: String,
        percent: Int,
        speed: Float,
        avgSpeed: Float,
        currentPart: Int,
        partsTotal: Int
    ) = Unit

    override fun onError(deviceAddress: String, error: Int, errorType: Int, message: String) = Unit
}

object DfuServiceListenerHelper {
    fun unregisterProgressListener(mContext: Context?, dfuProgressListenerAdapter: DfuProgressListener) {

    }

    fun registerProgressListener(context: Context?, mDfuProgressListener: DfuProgressListener) {

    }
}

class DfuServiceInitiator(mac: String) {
    fun setForeground(b: Boolean) = also {  }
    fun setDisableNotification(b: Boolean) = also {  }
    fun setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(b: Boolean) = also {  }
    fun setForceDfu(b: Boolean) = also {  }
    fun setPacketsReceiptNotificationsEnabled(b: Boolean) = also {  }
    fun setPrepareDataObjectDelay(delay: Int) = also {  }
    fun setZip(uriForFile: Any, mUpdateFilePath: String?) {

    }

    fun start(mContext: Context?, java: Class<*>) {

    }
}