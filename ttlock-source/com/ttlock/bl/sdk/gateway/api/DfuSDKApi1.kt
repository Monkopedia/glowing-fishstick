package com.ttlock.bl.sdk.gateway.api

import com.ttlock.bl.sdk.gateway.callback.DfuCallback
import com.ttlock.bl.sdk.gateway.callback.EnterDfuCallback
import com.ttlock.bl.sdk.service.ThreadPool
import com.ttlock.bl.sdk.util.IOUtil
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.URL
import android.util.Context

/**
 * Created by TTLock on 2017/8/16.
 * 泰凌微进入升级模式  网关DUF->蓝牙DFU
 */
internal class DfuSDKApi {
    private val DBG = true
    private var mContext: Context? = null
    private var clientId: String? = null
    private var accessToken: String? = null
    private var gatewayId = 0
    var gatewayMac: String? = null
    private var dfuCallback: DfuCallback? = null
    private var handler: Handler? = null
    private var upgradeFailedByServer = false
    private var downloadSuccess = false
    private var isDFUMode = false
    private var isTelinkDFUMode = false

    /**
     * telink device object
     */
    private var telinkDevice: Device? = null

    //    private boolean telinkDFUSuccess = false;
    private var telinkDfuDisconnectFailureCallback = true
    private val scanTimeOut = (10 * 1000).toLong()
    private val scanTimeOutRunable = Runnable {
        LogUtil.d("scan time out")
        ScanManager.Companion.getInstance().stopScan()
        errorCallback()
    }

    // todo:可以增加一个超时
    private val scanCallback: ScanGatewayCallback = object : ScanGatewayCallback {
        override fun onScanGatewaySuccess(device: ExtendedBluetoothDevice) {
            if (device.getAddress() == gatewayMac) {
                if (device.isDfuMode()) {
                    isDFUMode = true
                }
                if (device.isTelinkGatewayDfuMode()) {
                    telinkDevice =
                        Device(device.getDevice(), device.getScanRecord(), device.getRssi())
                    isTelinkDFUMode = true
                }
                if (isDFUMode || isTelinkDFUMode) {
                    if (downloadSuccess) {
                        LogUtil.d("start dfu")
                        startDfu()
                    }
                } else { // 泰凌微网关一定要走蓝牙的进入升级模式
                    isDFUMode = false
                    isTelinkDFUMode = false
                    enterDfuByBle()
                }
                handler.removeCallbacks(scanTimeOutRunable)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            LogUtil.w("errorCode:$errorCode")
        }
    }

    /**
     * nordic芯片 固件烧录状态回调
     */
    private val mDfuProgressListener: DfuProgressListener = object : DfuProgressListenerAdapter() {
        fun onDeviceConnecting(deviceAddress: String?) {}
        fun onDfuProcessStarting(deviceAddress: String?) {}
        fun onEnablingDfuMode(deviceAddress: String?) {}
        fun onFirmwareValidating(deviceAddress: String?) {}
        fun onDeviceDisconnecting(deviceAddress: String?) {}
        fun onDfuCompleted(deviceAddress: String) {
            dfuComplete(deviceAddress)
        }

        fun onDfuAborted(deviceAddress: String?) {
            handler.post(Runnable { dfuCallback!!.onDfuAborted(deviceAddress) })
        }

        fun onProgressChanged(
            deviceAddress: String,
            percent: Int,
            speed: Float,
            avgSpeed: Float,
            currentPart: Int,
            partsTotal: Int
        ) {
            dfuProgressChangeCallback(
                deviceAddress,
                percent,
                speed,
                avgSpeed,
                currentPart,
                partsTotal
            )
        }

        fun onError(deviceAddress: String?, error: Int, errorType: Int, message: String) {
            LogUtil.d("message:$message")
            errorCallback()
        }
    }

    private fun dfuComplete(deviceAddress: String) {
        DfuServiceListenerHelper.unregisterProgressListener(mContext, mDfuProgressListener)
        upgradeSuccess(deviceAddress)
        clearFile()
        downloadSuccess = false
    }

    private fun upgradeSuccess(deviceAddress: String?) {
        ThreadPool.getThreadPool().execute {
            val json: String = ResponseService.plugUpgradeSuccess(clientId, accessToken, gatewayId)
            try {
                val jsonObject = JSONObject(json)
                LogUtil.d("json:$json", true)
                val errcode: Int = jsonObject.getInt("errcode")
                if (errcode == 0) {
                    upgradeFailedByServer = false
                    successCallback(deviceAddress)
                } else {
                    upgradeFailedByServer = true
                    errorCallback()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                upgradeFailedByServer = true
                errorCallback()
            }
        }
    }

    private fun successCallback(deviceAddress: String?) {
        handler.post(Runnable { dfuCallback!!.onDfuSuccess(deviceAddress) })
    }

    private fun errorCallback() {
        clearData()
        telinkDfuDisconnectFailureCallback = false
        handler.post(Runnable { dfuCallback!!.onError() })
    }

    private fun clearData() {
        isDFUMode = false
        isTelinkDFUMode = false
    }

    private fun startScan() {
        try {
            handler.postDelayed(scanTimeOutRunable, scanTimeOut)
            GatewayClient.Companion.getDefault().startScanGateway(scanCallback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 中断升级
     */
    fun abortUpgradeProcess() {
        LogUtil.d("exit dfu mode", DBG)
        val manager: LocalBroadcastManager = LocalBroadcastManager.getInstance(mContext)
        val pauseAction = Intent(BROADCAST_ACTION)
        pauseAction.putExtra(EXTRA_ACTION, ACTION_ABORT)
        manager.sendBroadcast(pauseAction)
        if (telinkDevice != null) {
            telinkDevice.disconnect()
        }
    }

    private var mUpdateFilePath: String? = null

    /**
     * 开始烧录固件
     */
    private fun startDfu() {
        ScanManager.Companion.getInstance().stopScan()
        LogUtil.d("start dfu", DBG)
        LogUtil.d("isTelinkDFUMode:$isTelinkDFUMode", DBG)
        LogUtil.d("isDFUMode:$isDFUMode", DBG)
        if (isTelinkDFUMode) {
            telinkDfu()
        } else if (isDFUMode) {
            nordicDfu()
        }
    }

    private val deviceCallback: DeviceStateCallback = object : DeviceStateCallback {
        override fun onConnected(device: Device?) {
            TelinkLog.w("telink:" + " # onConnected")
        }

        override fun onDisconnected(device: Device?) {
            TelinkLog.w("telink:" + " # onDisconnected")
            if (telinkDfuDisconnectFailureCallback) {
                errorCallback()
            }
        }

        override fun onServicesDiscovered(device: Device, services: List<BluetoothGattService?>) {
            TelinkLog.w("telink:" + " # onServicesDiscovered")
            var serviceUUID: UUID? = null
            for (service in services) {
                for (characteristic in service.getCharacteristics()) {
                    if (characteristic.getUuid()
                        .equals(Device.Companion.CHARACTERISTIC_UUID_WRITE)
                    ) {
                        serviceUUID = service.getUuid()
                        break
                    }
                }
            }
            if (serviceUUID != null) {
                device.SERVICE_UUID = serviceUUID
            }
            val firmware = IOUtil.readFirmware(mUpdateFilePath)
            if (firmware == null) {
                LogUtil.d("invalid firmware package")
                errorCallback()
                return
            }
            telinkDevice.startOta(firmware)
        }

        override fun onOtaStateChanged(device: Device, state: Int) {
            TelinkLog.w("telink:" + " # onOtaStateChanged")
            when (state) {
                Device.Companion.STATE_PROGRESS -> {
                    TelinkLog.d("ota progress : " + device.getOtaProgress())
                    // todo:数据处理
                    dfuProgressChangeCallback(
                        device.getMacAddress(),
                        device.getOtaProgress(),
                        0f,
                        0f,
                        device.getIndex(),
                        device.getTotal()
                    )
                }
                Device.Companion.STATE_SUCCESS -> {
                    telinkDfuDisconnectFailureCallback = false
                    TelinkLog.d("ota success : ")
                    //                    mDevice.disconnect();
                    dfuComplete(device.getMacAddress())
                }
                Device.Companion.STATE_FAILURE -> {
                    TelinkLog.d("ota failure : ")
                    errorCallback()
                }
            }
        }
    }

    private fun dfuProgressChangeCallback(
        deviceAddress: String,
        percent: Int,
        speed: Float,
        avgSpeed: Float,
        currentPart: Int,
        partsTotal: Int
    ) {
        handler.post(
            Runnable {
                dfuCallback!!.onProgressChanged(
                    deviceAddress,
                    percent,
                    speed,
                    avgSpeed,
                    currentPart,
                    partsTotal
                )
            }
        )
    }

    private fun telinkDfu() {
        LogUtil.d("telink dfu")
        handler.postDelayed(
            Runnable {
                if (telinkDevice != null) {
                    telinkDfuDisconnectFailureCallback = true
                    telinkDevice.setDeviceStateCallback(deviceCallback)
                    telinkDevice.connect(mContext)
                } else {
                    LogUtil.d("telinkDevice is null")
                }
            },
            1500
        )
    }

    private fun nordicDfu() {
        LogUtil.d("nordic dfu")
        handler.postDelayed(
            Runnable {
                val starter: DfuServiceInitiator = DfuServiceInitiator(gatewayMac)
                    .setForeground(false)
                    .setDisableNotification(true)
                    .setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true) //                        .setDeviceName(mDoorkey.getLockName())
                    .setForceDfu(true)
                    .setPacketsReceiptNotificationsEnabled(true)
                    .setPrepareDataObjectDelay(400)
                starter.setZip(
                    FileProviderPath.getUriForFile(mContext, File(mUpdateFilePath)),
                    mUpdateFilePath
                )
                starter.start(mContext, DfuService::class.java)
            },
            2000
        )
    }

    private fun enterDfuByServer() {
        // 泰凌微网关
        // 服务端发命令，会让网关重启
        // 重置之后，蓝牙去连接，然后发那条升级命令
        // 这样之后才可以升级
        ThreadPool.getThreadPool().execute {
            val json: String = ResponseService.enterDfuMode(clientId, accessToken, gatewayId)
            try {
                LogUtil.d("json:$json", true)
                LogUtil.d("isDFUMode:$isDFUMode")
                val jsonObject = JSONObject(json)
                val errcode: Int = jsonObject.getInt("errcode")
                startScan() // 进入升级模式后 继续扫描根据广播判断是那种芯片的升级
                //                    if(errcode == 0) {
//                        startScan();//进入升级模式后 继续扫描根据广播判断是那种芯片的升级
// //                        startDfu();
//                    } else {
//                        if (!isDFUMode || !isTelinkDFUMode) {
//                            enterDfuByBle();
//                        }
//                    }
            } catch (e: JSONException) {
                e.printStackTrace()
                startScan() // 进入升级模式后 继续扫描根据广播判断是那种芯片的升级
                // TODO:已经在升级模式
//                    if (!isDFUMode || !isTelinkDFUMode) {
//                        enterDfuByBle();
//                    }
            }
        }
    }

    private fun doEnterDfuByBle() {
        GatewayClient.Companion.getDefault().enterDfu(
            gatewayMac,
            object : EnterDfuCallback {
                override fun onEnterDfuSuccess() {
                    startScan()
                }

                override fun onFail(error: GatewayError) {
                    LogUtil.d(error.getDescription())
                    if (!isDFUMode && !isTelinkDFUMode) {
                        errorCallback()
                    }
                    //                else {
//                    startDfu();
//                }
                }
            }
        )
    }

    private fun enterDfuByBle() {
        ScanManager.Companion.getInstance().stopScan()
        doEnterDfuByBle()
        //        if (ConnectManager.getInstance().isDeviceConnected()) {
//            doEnterDfuByBle();
//        } else {
//            GatewayClient.getDefault().connectGateway(gatewayMac, new ConnectCallback() {
//                @Override
//                public void onConnectSuccess(ExtendedBluetoothDevice device) {//泰凌微需要重新连接 再让网关进入升级模式
//                    doEnterDfuByBle();
//                }
//
//                @Override
//                public void onDisconnected() {
//                    if (!isDFUMode && !isTelinkDFUMode) {
//                        errorCallback();
//                    }
//                }
//            });
//        }
    }

    private fun initData() {
        isDFUMode = false
        isTelinkDFUMode = false
        downloadSuccess = false
    }

    fun startDfu(
        context: Context?,
        clientId: String?,
        accessToken: String?,
        gatewayId: Int,
        gatewayMac: String?,
        dfuCallback: DfuCallback?
    ) {
        if (handler == null) {
            handler = Handler(Looper.getMainLooper())
        }
        // TODO:赋值
        this.clientId = clientId
        this.accessToken = accessToken
        this.gatewayId = gatewayId
        this.gatewayMac = gatewayMac
        this.dfuCallback = dfuCallback
        mContext = context
        DfuServiceListenerHelper.registerProgressListener(context, mDfuProgressListener)
        GatewayClient.Companion.getDefault().prepareBTService(context)
        initData()
        if (NetworkUtil.isNetConnected(context)) { // TODO:退出unregister
            getDownloadUrl()
        } else {
            LogUtil.d("bad network")
            errorCallback()
        }
    }

    private fun getDownloadUrl() {
        ThreadPool.getThreadPool().execute {
            val json: String =
                ResponseService.getPlugUpgradePackage(clientId, accessToken, gatewayId)
            LogUtil.d("json:$json")
            if (TextUtils.isEmpty(json)) {
                errorCallback()
            } else {
                val gatewayUpdateInfo: GatewayUpdateInfo =
                    GsonUtil.toObject<GatewayUpdateInfo>(json, GatewayUpdateInfo::class.java)
                if (gatewayUpdateInfo != null && !TextUtils.isEmpty(gatewayUpdateInfo.getUrl())) {
                    downloadUpdatePackage(gatewayUpdateInfo)
                } else {
                    errorCallback()
                }
            }
        }
    }

    private fun downloadUpdatePackage(gatewayUpdateInfo: GatewayUpdateInfo) {
        ThreadPool.getThreadPool().execute {
            var os: OutputStream? = null
            var `is`: InputStream? = null
            try {
                val url = URL(gatewayUpdateInfo.getUrl())
                val conn = url.openConnection()
                `is` = conn.getInputStream()
                val fileLen = conn.contentLength

                // TODO:一次性读完
                os = ByteArrayOutputStream(fileLen)
                // 创建字节流
                val bs = ByteArray(1024)
                var len: Int
                // 写数据
                while (`is`.read(bs).also { len = it } != -1) {
                    os!!.write(bs, 0, len)
                }
                val source: ByteArray = (os as ByteArrayOutputStream?).toByteArray()
                val decryptedBytes: ByteArray = AESUtil.aesDecrypt(
                    source,
                    DigitUtil.decodeLockData(gatewayUpdateInfo.getDecryptionKey()).toByteArray()
                )
                mUpdateFilePath =
                    mContext.getCacheDir().getAbsolutePath() + File.separator + UPDATE_FILE_NAME
                //                    LogUtil.d("mUpdateFilePath:" + mUpdateFilePath, DBG);
                os = FileOutputStream(mUpdateFilePath)
                if (decryptedBytes != null) os!!.write(decryptedBytes)
                // 完成后关闭流
                os!!.close()
                `is`.close()
                downloadSuccess = true
                enterDfuByServer()
                //                    if (!isDFUMode || !isTelinkDFUMode) {
//                        LogUtil.d("enter dfu");
//                        enterDfuByServer();
//                    } else {
//                        startDfu();
//                    }
            } catch (e: IOException) {
                mUpdateFilePath = null
                e.printStackTrace()
                errorCallback()
            } finally {
            }
        }
    }

    /**
     * 升级成功之后删除文件
     */
    private fun clearFile() {
        if (mUpdateFilePath == null) return
        val file = File(mUpdateFilePath)
        if (file != null && file.exists()) {
            LogUtil.d("delete file:" + file.delete())
        }
    }

    fun retryEnterDfuModeByNet() {
        if (canRetry()) {
            if (TextUtils.isEmpty(mUpdateFilePath)) {
                getDownloadUrl()
            } else if (upgradeFailedByServer) {
                upgradeSuccess(gatewayMac)
            } else {
                enterDfuByServer()
            }
        } else {
            LogUtil.w("please call startDfu method first")
            errorCallback()
        }
    }

    fun retryEnterDfuModeByBle() {
        if (canRetry()) {
            if (TextUtils.isEmpty(mUpdateFilePath)) {
                getDownloadUrl()
            } else if (upgradeFailedByServer) {
                upgradeSuccess(gatewayMac)
            } else {
                enterDfuByBle()
            }
        } else {
            LogUtil.w("please call startDfu method first")
            errorCallback()
        }
    }

    private fun canRetry(): Boolean {
        return if (!TextUtils.isEmpty(accessToken) && !TextUtils.isEmpty(clientId)) true else false
    }

    companion object {
        /**
         * 锁固件升级包名
         */
        const val UPDATE_FILE_NAME = "realUpdate.zip"
    }
}
