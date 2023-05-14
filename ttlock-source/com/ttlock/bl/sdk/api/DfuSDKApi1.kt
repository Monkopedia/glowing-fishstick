package com.ttlock.bl.sdk.api

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import com.ttlock.bl.sdk.callback.DfuCallback
import com.ttlock.bl.sdk.entity.DeviceInfo
import com.ttlock.bl.sdk.entity.RecoveryDataType
import com.ttlock.bl.sdk.entity.ServerError
import com.ttlock.bl.sdk.service.ThreadPool
import com.ttlock.bl.sdk.util.IOUtil
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import android.util.Context
import android.util.DfuProgressListener
import android.util.DfuProgressListenerAdapter
import android.util.DfuServiceInitiator
import android.util.DfuServiceListenerHelper
import android.util.Handler
import android.util.Looper
import android.util.TextUtils
import android.util.TypeToken
import android.util.toObject
import com.ttlock.bl.sdk.callback.EnterDfuModeCallback
import com.ttlock.bl.sdk.callback.GetLockSystemInfoCallback
import com.ttlock.bl.sdk.callback.GetOperationLogCallback
import com.ttlock.bl.sdk.callback.RecoverLockDataCallback
import com.ttlock.bl.sdk.callback.ScanLockCallback
import com.ttlock.bl.sdk.callback.SetLockTimeCallback
import com.ttlock.bl.sdk.constant.FeatureValue
import com.ttlock.bl.sdk.constant.LogType
import com.ttlock.bl.sdk.entity.LockError
import com.ttlock.bl.sdk.entity.LockUpdateInfo
import com.ttlock.bl.sdk.net.ResponseService
import com.ttlock.bl.sdk.service.DfuService
import com.ttlock.bl.sdk.telink.ble.Device
import com.ttlock.bl.sdk.telink.ble.Device.DeviceStateCallback
import com.ttlock.bl.sdk.telink.util.TelinkLog
import com.ttlock.bl.sdk.util.AESUtil
import com.ttlock.bl.sdk.util.DigitUtil
import com.ttlock.bl.sdk.util.FeatureValueUtil
import com.ttlock.bl.sdk.util.GsonUtil
import com.ttlock.bl.sdk.util.LogUtil
import com.ttlock.bl.sdk.util.NetworkUtil
import com.ttlock.file.FileProviderPath
import json.JSONException
import json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 * Created by TTLock on 2017/8/16.
 * todo:锁一直处于升级模式 直接进行升级
 */
internal class DfuSDKApi {
    private val DBG = true
    private var mContext: Context? = null
    private var clientId: String? = null
    private var accessToken: String? = null
    private var lockid = 0
    private var lockData: String? = null
    var lockmac: String? = null
    private var pwdJson: String? = null
    private var ICJson: String? = null
    private var FRJson: String? = null
    private var operateLog: String? = null

    //    private int feature;
    private var deviceInfo: DeviceInfo? = null
    private var telinkDevice: Device? = null

    //    private boolean telinkDFUSuccess = false;
    private var lockSystemInfoCallback: GetLockSystemInfoCallback? = null
    private var dfuCallback: DfuCallback? = null

    /**
     * the status of upgrading
     */
    private var upgradeStatus = 0
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var attemptTime = 0
    private var isDfuMode: Boolean? = null
    private val timeOutRunnable = Runnable { // 超时之后不终止
        LogUtil.w("enter DFU time out", DBG)
        TTLockClient.Companion.getDefault().stopScanLock()
        errorCallback(DfuFailed, DFU_FAILED)
    }
    private var telinkDfuDisconnectFailureCallback = true

    /**
     * 固件烧录状态回调
     */
    private val mDfuProgressListener: DfuProgressListener = object : DfuProgressListenerAdapter() {
        override fun onDeviceConnecting(deviceAddress: String) {
            LogUtil.d("deviceAddress:$deviceAddress", DBG)
        }

        override fun onDfuProcessStarting(deviceAddress: String) {
            LogUtil.d("deviceAddress:$deviceAddress", DBG)
        }

        override fun onEnablingDfuMode(deviceAddress: String) {
            handler.removeCallbacks(timeOutRunnable)
            LogUtil.d("deviceAddress:$deviceAddress", DBG)
        }

        override fun onFirmwareValidating(deviceAddress: String) {
            LogUtil.d("deviceAddress:$deviceAddress", DBG)
        }

        override fun onDeviceDisconnecting(deviceAddress: String) {
            LogUtil.d("deviceAddress:$deviceAddress", DBG)
        }

        override fun onDfuCompleted(deviceAddress: String) {
            LogUtil.d("deviceAddress:$deviceAddress", DBG)
            DfuServiceListenerHelper.unregisterProgressListener(mContext, this)
            if (TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(clientId)) // 只做固件升级部分 不做网络请求
                successCallback() else {
                Handler(Looper.getMainLooper()).postDelayed(
                    Runnable {
                        attemptTime = 1
                        recoveryData()
                    },
                    4500
                )
            }
        }

        override fun onDfuAborted(deviceAddress: String) {
            handler.post(Runnable { dfuCallback!!.onDfuAborted(deviceAddress) })
            attemptTime = 0
            LogUtil.d("deviceAddress:$deviceAddress", DBG)
        }

        override fun onProgressChanged(
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

        override fun onError(deviceAddress: String, error: Int, errorType: Int, message: String) {
            LogUtil.d("deviceAddress:$deviceAddress", DBG)
            handler.removeCallbacks(timeOutRunnable)
            errorCallback(DfuFailed, message)
        }
    }

    private fun errorCallback(errorcode: Int, errmsg: String) {
        telinkDfuDisconnectFailureCallback = false
        handler.post(Runnable { dfuCallback!!.onError(errorcode, errmsg) })
    }

    /**
     * 中断升级
     */
    fun abortUpgradeProcess() {
        LogUtil.d("exit dfu mode", DBG)
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.TELINK_CHIP)) {
                telinkDevice?.disconnect()
        } else {
            // Commented by monk
//            val manager: LocalBroadcastManager = LocalBroadcastManager.getInstance(mContext)
//            val pauseAction = Intent(BROADCAST_ACTION)
//            pauseAction.putExtra(EXTRA_ACTION, ACTION_ABORT)
//            manager.sendBroadcast(pauseAction)
        }
    }

    private var mUpdateFilePath: String? = null
    private fun canRetry(): Boolean {
        LogUtil.d("accessToken:$accessToken")
        LogUtil.d("clientId:$clientId")
        if (!TextUtils.isEmpty(accessToken) && !TextUtils.isEmpty(clientId)) return true
        LogUtil.w("please call startDfu method first")
        return false
    }

    fun retry() {
        if (!canRetry()) return
        LogUtil.d("retry:$upgradeStatus", DBG)
        attemptTime++
        when (upgradeStatus) {
            GetDeviceInfo -> getLockSystemInfo(lockData, lockmac, lockSystemInfoCallback!!)
            UpgradeOprationPreparing -> getOperationLog(lockData, lockmac)
            UpgradeOprationUpgrading -> enableLockDfuMode()
            EnterDfu -> enableLockDfuMode()
            UpgradeOprationRecovering -> recoveryData()
            UploadOperateLog -> uploadOperateLog(operateLog)
            Download -> getDownloadUrl()
            GetData -> getRecoverData()
            InformServerSuccess -> upgradeSuccess()
        }
    }

    /**
     * 开始烧录固件
     */
    private fun startDfu() {
        upgradeStatus = UpgradeOprationUpgrading
        statusCallback(UpgradeOprationUpgrading)
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.TELINK_CHIP)) {
            telinkDfu()
        } else {
            nordicDfu()
        }
    }

    private fun nordicDfu() {
        handler.postDelayed(
            Runnable {
                LogUtil.d("start dfu", DBG)
                val starter: DfuServiceInitiator = DfuServiceInitiator(lockmac!!)
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
                // 一分钟的超时
                handler.postDelayed(timeOutRunnable, 60000)
            },
            3500
        )
    }

    fun getLockSystemInfo(
        lockData: String?,
        lockMac: String?,
        callback: GetLockSystemInfoCallback
    ) {
        lockSystemInfoCallback = callback
        TTLockClient.Companion.getDefault().getLockSystemInfo(lockData, lockMac, callback)
    }

    fun startDfu(
        context: Context?,
        clientId: String,
        accessToken: String,
        lockid: Int,
        lockData: String?,
        lockMac: String?,
        dfuCallback: DfuCallback?
    ) {
        this.clientId = clientId
        this.accessToken = accessToken
        this.lockid = lockid
        lockmac = lockMac
        this.dfuCallback = dfuCallback
        this.lockData = lockData
        mContext = context
        LogUtil.d("accessToken:$accessToken")
        LogUtil.d("clientId:$clientId")
        getDownloadUrl()

//        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.TELINK_CHIP)) {
//            getDownloadUrl();
//        } else {
//            attemptTime = 1;
//            DfuServiceListenerHelper.registerProgressListener(context, mDfuProgressListener);
//
//            if (NetworkUtil.isNetConnected(context)) {
//                setLockTime();
// //            getOperationLog(lockData, lockmac);
//            } else {
//                errorCallback(NetError, NET_UNABLE);
//            }
//        }
    }

    fun startDfu(
        context: Context?,
        lockData: String?,
        lockMac: String?,
        firmwarePackage: String?,
        dfuCallback: DfuCallback?
    ) {
        var firmwarePackage = firmwarePackage
        this.lockData = lockData
        mContext = context
        lockmac = lockMac
        this.dfuCallback = dfuCallback
        DfuServiceListenerHelper.registerProgressListener(context, mDfuProgressListener)
        if (!TextUtils.isEmpty(firmwarePackage)) firmwarePackage =
            DigitUtil.decodeLockData(firmwarePackage!!)
        if (NetworkUtil.isNetConnected(context)) {
            if (!TextUtils.isEmpty(firmwarePackage)) {
                val lockUpdateInfo: LockUpdateInfo =
                    GsonUtil.toObject<LockUpdateInfo>(firmwarePackage, LockUpdateInfo::class.java)
                if (lockUpdateInfo.getDecryptionKey() != null) lockUpdateInfo.setDecryptionKey(
                    DigitUtil.encodeLockData(lockUpdateInfo.getDecryptionKey()!!)
                )
                downloadUpdatePackage(lockUpdateInfo, lockUpdateInfo.getUrl()!!)
            }
        } else {
            errorCallback(NetError, NET_UNABLE)
        }
    }

    private fun getOperationLog(lockData: String?, lockMac: String?) {
        /**
         * 准备中
         */
        upgradeStatus = UpgradeOprationPreparing
        statusCallback(UpgradeOprationPreparing)

        // TODO:读全部还是只读新的
        TTLockClient.Companion.getDefault()
            .getOperationLog(
                LogType.NEW, lockData, lockMac,
                object : GetOperationLogCallback {
                    override fun onGetLogSuccess(log: String?) {
                        operateLog = log
                        uploadOperateLog(log)
                    }

                    override fun onFail(error: LockError) {
                        errorCallback(BLECommunicationError, error.getErrorMsg())
                    }
                }
            )
    }

    private fun recoveryData() {
        upgradeStatus = UpgradeOprationRecovering
        statusCallback(UpgradeOprationRecovering)
        if (!dataIsEmpty(pwdJson)) {
            recoveryDataByBle(1, pwdJson)
        } else {
            if (!dataIsEmpty(ICJson)) {
                recoveryDataByBle(2, ICJson)
            } else {
                if (!dataIsEmpty(FRJson)) {
                    recoveryDataByBle(3, FRJson)
                } else { // 升级成功不需要恢复
                    getFeature()
                }
            }
        }
    }

    private fun statusCallback(status: Int) {
        handler.post(Runnable { dfuCallback!!.onStatusChanged(status) })
    }

    private fun getFeature() {
//        TTLockClient.getDefault().getSpecialValue(lockData, lockmac, new GetSpecialValueCallback() {
//            @Override
//            public void onGetSpecialValueSuccess(int specialValue) {
//                feature = specialValue;
//                upgradeSuccess();
//            }
//
//            @Override
//            public void onFail(LockError error) {
//
//            }
//        });
        TTLockClient.Companion.getDefault()
            .getLockSystemInfo(
                lockData, null,
                object : GetLockSystemInfoCallback {
                    override fun onGetLockSystemInfoSuccess(info: DeviceInfo?) {
                        deviceInfo = info
                        upgradeSuccess()
                    }

                    override fun onFail(error: LockError) {
                        errorCallback(BLECommunicationError, error.getErrorMsg())
                    }
                }
            )
    }

    private fun setLockTime() { // 校准时间放到准备过程中 失败之后不影响升级 继续走后续的获取操作记录
        upgradeStatus = UpgradeOprationPreparing
        TTLockClient.Companion.getDefault().setLockTime(
            System.currentTimeMillis(),
            lockData,
            lockmac,
            object : SetLockTimeCallback {
                override fun onSetTimeSuccess() {
                    getOperationLog(lockData, lockmac)
                    //                upgradeSuccess();
                }

                override fun onFail(error: LockError) {
                    errorCallback(BLECommunicationError, error.getErrorMsg())
                }
            }
        )
    }

    private fun upgradeSuccess() {
        upgradeStatus = InformServerSuccess
        ThreadPool.getThreadPool().execute {
            val json: String = ResponseService.lockUpgradeSuccess(
                clientId,
                accessToken,
                lockid,
                deviceInfo!!.lockData
            )
            if (TextUtils.isEmpty(json)) {
                errorCallback(NetError, NET_UNABLE)
            } else {
                val error: ServerError? =
                    GsonUtil.toObject<ServerError>(json, ServerError::class.java)
                if (error != null) {
                    if (error.errcode == 0) {
                        successCallback()
                    } else {
                        errorCallback(RequestError, error.errmsg ?: "")
                    }
                } else {
                    errorCallback(RequestError, json)
                }
            }
        }
    }

    private fun successCallback() {
        handler.post(
            Runnable {
                upgradeStatus = UpgradeOprationSuccess
                dfuCallback!!.onDfuSuccess(lockmac)
            }
        )
    }

    private fun recoveryDataByBle(op: Int, json: String?) {
        LogUtil.d("recovery data", DBG)
        upgradeStatus = UpgradeOprationRecovering
        TTLockClient.Companion.getDefault()
            .recoverLockData(
                json, op, lockData, lockmac,
                object : RecoverLockDataCallback {
                    override fun onRecoveryDataSuccess(dataType: Int) {
                        when (dataType) {
                            RecoveryDataType.PASSCODE -> {
                                pwdJson = null
                                recoveryData()
                            }
                            RecoveryDataType.IC -> {
                                ICJson = null
                                recoveryData()
                            }
                            RecoveryDataType.FINGERPRINT -> {
                                FRJson = null
                                getFeature()
                            }
                        }
                    }

                    override fun onFail(error: LockError) {
                        errorCallback(BLECommunicationError, error.getErrorMsg())
                    }
                }
            )
    }

    private fun getRecoverData() {
        upgradeStatus = GetData
        ThreadPool.getThreadPool().execute {
            val json: String = ResponseService.getRecoverData(clientId, accessToken, lockid)
            LogUtil.d("json:$json", true)
            if (TextUtils.isEmpty(json)) {
                errorCallback(NetError, NET_UNABLE)
            } else {
                try {
                    val jsonObject = JSONObject(json)
                    if (jsonObject.has("errcode")) {
                        errorCallback(RequestError, jsonObject.getString("errmsg"))
                    } else {
                        pwdJson = jsonObject.getString("keyboardPwdList")
                        ICJson = jsonObject.getString("identityCardList")
                        FRJson = jsonObject.getString("fingerprintList")
                        // 流程变了 获取恢复数据后 直接进入升级模式 进行升级
                        enableLockDfuMode()
                        //                                getDownloadUrl();
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    errorCallback(RequestError, e.message ?: "")
                }
            }
        }
    }

    private fun getDownloadUrl() {
        upgradeStatus = Download
        ThreadPool.getThreadPool().execute {
            val json: String = ResponseService.getUpgradePackage(clientId, accessToken, lockid)
            if (TextUtils.isEmpty(json)) {
                errorCallback(NetError, NET_UNABLE)
            } else {
                val lockUpdateInfo: LockUpdateInfo? =
                    toObject(json, object : TypeToken<LockUpdateInfo?>() {})
                if (lockUpdateInfo != null) {
                    if (lockUpdateInfo.errcode == 0) {
                        downloadUpdatePackage(lockUpdateInfo, lockUpdateInfo.getUrl()!!)
                    } else {
                        errorCallback(RequestError, lockUpdateInfo.errmsg ?: "")
                    }
                } else {
                    errorCallback(RequestError, json)
                }
            }
        }
    }

    private fun uploadOperateLog(records: String?) {
        upgradeStatus = UploadOperateLog
        ThreadPool.getThreadPool().execute {
            val json: String =
                ResponseService.uploadOperateLog(clientId, accessToken, lockid, records)
            if (TextUtils.isEmpty(json)) {
                errorCallback(NetError, NET_UNABLE)
            } else {
                try {
                    val jsonObject = JSONObject(json)
                    LogUtil.e("json:$json", true)
                    val errcode: Int = jsonObject.getInt("errcode")
                    if (errcode == 0) {
                        // 获取恢复数据
                        getRecoverData()
                    } else {
                        errorCallback(RequestError, jsonObject.getString("errmsg"))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    errorCallback(RequestError, e.message ?: "")
                }
            }
        }
    }

    private fun downloadUpdatePackage(lockUpdateInfo: LockUpdateInfo, fileUrl: String) {
        ThreadPool.getThreadPool().execute {
            var os: OutputStream? = null
            var `is`: InputStream? = null
            try {
                val url = URL(fileUrl)
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
                val source: ByteArray = os.toByteArray()
                val decryptedBytes: ByteArray = AESUtil.aesDecrypt(
                    source,
                    DigitUtil.decodeLockData(lockUpdateInfo.getDecryptionKey()!!).toByteArray()
                )!!
                mUpdateFilePath =
                    mContext!!.getCacheDir().getAbsolutePath() + File.separator + UPDATE_FILE_NAME
                //                    LogUtil.d("mUpdateFilePath:" + mUpdateFilePath, DBG);
                os = FileOutputStream(mUpdateFilePath)
                if (decryptedBytes != null) os!!.write(decryptedBytes)
                // 完成后关闭流
                os!!.close()
                `is`.close()
                handler.post(
                    Runnable {
                        startScan()
                        //                            enableLockDfuMode();
                    }
                )
            } catch (e: IOException) {
                e.printStackTrace()
                errorCallback(NetError, e.message ?: "")
            } finally {
            }
        }
    }

    private fun startScan() {
        isDfuMode = false
        handler.postDelayed(timeOutRunnable, 10000)
        TTLockClient.Companion.getDefault().startScanLock(object : ScanLockCallback {
            override fun onScanLockSuccess(device: ExtendedBluetoothDevice) {
                if (device.getAddress() == lockmac) {
                    synchronized(isDfuMode!!) {
                        if (isDfuMode!!) {
                            return
                        }
                        handler.removeCallbacks(timeOutRunnable)
                        TTLockClient.Companion.getDefault().stopScanLock()
                        if (device.isDfuMode()) {
                            isDfuMode = true
                            startDfu()
                        } else {
                            doWithAfterDownloadFirmware()
                            //                        enableLockDfuMode();
                        }
                    }
                }
            }

            override fun onFail(error: LockError) {}
        })
    }

    private fun doWithAfterDownloadFirmware() {
        if (TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(clientId)) { // 只做固件升级部分 不做网络请求
            enableLockDfuMode()
            return
        }
        if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.TELINK_CHIP)) {
            enableLockDfuMode()
        } else {
            attemptTime = 1
            DfuServiceListenerHelper.registerProgressListener(mContext, mDfuProgressListener)
            if (NetworkUtil.isNetConnected(mContext)) {
                setLockTime()
                //            getOperationLog(lockData, lockmac);
            } else {
                errorCallback(NetError, NET_UNABLE)
            }
        }
    }

    //    private Device.DeviceStateCallback deviceCallback = new Device.DeviceStateCallback() {
    //        @Override
    //        public void onConnected(Device device) {
    //            TelinkLog.w(TAG + " # onConnected");
    //
    // //            mConnectState = BluetoothGatt.STATE_CONNECTED;
    // //            runOnUiThread(new Runnable() {
    // //                @Override
    // //                public void run() {
    // //                    CommonUtils.showLongMessage("device connected");
    // //                }
    // //            });
    //        }
    //
    //        @Override
    //        public void onDisconnected(Device device) {
    //            TelinkLog.w(TAG + " # onDisconnected");
    //            runOnUiThread(new Runnable() {//todo:失败
    //                @Override
    //                public void run() {
    //                    if (!telinkDFUSuccess) {
    //                        onFailure();
    //                    }
    //                }
    //            });
    //        }
    //
    //        @Override
    //        public void onServicesDiscovered(Device device, final List<BluetoothGattService> services) {
    //            TelinkLog.w(TAG + " # onServicesDiscovered");
    //            UUID serviceUUID = null;
    //            for (BluetoothGattService service: services){
    //                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()){
    //                    if (characteristic.getUuid().equals(Device.CHARACTERISTIC_UUID_WRITE)){
    //                        serviceUUID = service.getUuid();
    //                        break;
    //                    }
    //                }
    //            }
    //
    //            if (serviceUUID != null){
    //                device.SERVICE_UUID = serviceUUID;
    //            }
    //
    //            runOnUiThread(new Runnable() {
    //                @Override
    //                public void run() {
    //                    byte[] firmware = AppUtil.readFirmware(mUpdateFilePath);
    //                    if (firmware == null) {
    //                        CommonUtils.showLongMessage(R.string.firmware_invalid);
    //                        return;
    //                    }
    //                    mDevice.startOta(firmware);
    //                }
    //            });
    //        }
    //
    //        @Override
    //        public void onOtaStateChanged(Device device, int state) {
    //            TelinkLog.w(TAG + " # onOtaStateChanged");
    //            switch (state) {
    //                case Device.STATE_PROGRESS:
    //                    TelinkLog.d("ota progress : " + device.getOtaProgress());
    //                    runOnUiThread(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            binding.progress.setText(String.valueOf(device.getOtaProgress()));
    //                        }
    //                    });
    // //                    mInfoHandler.obtainMessage(MSG_PROGRESS, device.getOtaProgress()).sendToTarget();
    //                    break;
    //                case Device.STATE_SUCCESS:
    //                    telinkDFUSuccess = true;
    //                    TelinkLog.d("ota success : ");
    // //                    mDevice.disconnect();
    //                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            getLockSpecialValue();
    //                        }
    //                    }, 3000);
    //                    break;
    //                case Device.STATE_FAILURE:
    //                    runOnUiThread(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            onFailure();
    //                        }
    //                    });
    //
    //                    TelinkLog.d("ota failure : ");
    //                    break;
    //            }
    //        }
    //    };
    private val deviceCallback: DeviceStateCallback = object : DeviceStateCallback {
        override fun onConnected(device: Device) {
            TelinkLog.w("telink:" + " # onConnected")
        }

        override fun onDisconnected(device: Device) {
            TelinkLog.w("telink:" + " # onDisconnected")
            if (telinkDfuDisconnectFailureCallback) {
                errorCallback(DfuFailed, "disconnected")
            }
        }

        override fun onServicesDiscovered(device: Device, services: List<BluetoothGattService>) {
            TelinkLog.w("telink:" + " # onServicesDiscovered")
            var serviceUUID: UUID? = null
            for (service in services) {
                for (characteristic in service.getCharacteristics() ?: emptyList()) {
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
//                LogUtil.d("invalid firmware package");
                errorCallback(DfuFailed, "invalid firmware package")
                return
            }
            telinkDevice!!.startOta(firmware)
        }

        override fun onOtaStateChanged(device: Device, state: Int) {
            TelinkLog.w("telink:" + " # onOtaStateChanged")
            when (state) {
                Device.Companion.STATE_PROGRESS -> {
                    TelinkLog.d("ota progress : " + device.getOtaProgress())
                    // todo:数据处理
                    handler.post(
                        Runnable {
                            dfuCallback!!.onProgressChanged(
                                device.getMacAddress(),
                                device.getOtaProgress(),
                                0f,
                                0f,
                                device.getIndex(),
                                device.getTotal()
                            )
                        }
                    )
                }
                Device.Companion.STATE_SUCCESS -> {
                    telinkDfuDisconnectFailureCallback = false
                    TelinkLog.d("ota success : ")
                    if (TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(clientId)) // 只做固件升级部分 不做网络请求
                        successCallback() else {
                        getFeature()
                    }
                }
                Device.Companion.STATE_FAILURE -> {
                    TelinkLog.d("ota failure : ")
                    errorCallback(DfuFailed, "ota failed")
                }
            }
        }
    }

    private fun telinkDfu() {
        // 不做延迟，延迟之后会 会导致无法升级
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
        if (telinkDevice == null) {
            val bluetoothDevice: BluetoothDevice =
                (mContext!!.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).getAdapter()
                    .getRemoteDevice(lockmac!!)
            telinkDevice = Device(bluetoothDevice, null, -1)
            telinkDevice!!.setDeviceStateCallback(deviceCallback)
        }
        telinkDfuDisconnectFailureCallback = true
        telinkDevice!!.connect(mContext!!)
        //            }
//        }, 500);
    }

    /**
     * 向锁发送进入升级模式指令
     */
    private fun enableLockDfuMode() {
        upgradeStatus = EnterDfu
        LogUtil.d("enter dfu mode", DBG)
        TTLockClient.Companion.getDefault()
            .enterDfuMode(
                lockData, lockmac,
                object : EnterDfuModeCallback {
                    override fun onEnterDfuMode() {
                        BluetoothImpl.Companion.getInstance().disconnect()
                        // 成功之后的步骤 断开连接再 启动DFU
                        startDfu()
                        //                if (FeatureValueUtil.isSupportFeature(lockData, FeatureValue.TELINK_CHIP)) {
//                    telinkDfu();
//                } else {
//                    startDfu();
//                }
                    }

                    override fun onFail(error: LockError) {
                        errorCallback(BLECommunicationError, error.getErrorMsg())
                    }
                }
            )
    }

    private fun dataIsEmpty(data: String?): Boolean {
        LogUtil.d("data:$data", DBG)
        return if (TextUtils.isEmpty(data) || data == "[]") true else false
    }

    companion object {
        const val GetDeviceInfo = -1
        const val UpgradeOprationPreparing = 1 // preparing
        const val UpgradeOprationUpgrading = 2 // upgrading
        const val UpgradeOprationRecovering = 3 // recoverying

        /**
         * inner used status
         */
        const val UpgradeOprationSuccess = 4 // upgrade success
        const val UploadOperateLog = 5
        const val Download = 6
        const val GetData = 7 // 获取密码 CARD FR数据
        const val EnterDfu = 8 // 指令进入dfu
        const val InformServerSuccess = 9

        //    public static final int SET_LOCK_TIME = 10;//校准时间过程
        const val DfuFailed = 1 // 固件升级失败
        const val BLECommunicationError = 3 // 蓝牙通信错误
        const val RequestError = 4 // 服务器请求错误
        const val NetError = 5 // 网络错误
        const val FINE_LOCATION_PERMISSION_NOT_GRANTED = 6 // 位置权限未授权
        private const val DFU_FAILED = "dfu failed"
        private const val NET_UNABLE = "network unavailable"
        private const val GRANT_FINE_LOCATION_PERMISSION = "please grant fine location permission"

        /**
         * 锁固件升级包名
         */
        const val UPDATE_FILE_NAME = "realUpdate.zip"
    }
}
