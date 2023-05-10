package com.ttlock.bl.sdk.api

import android.text.TextUtils
import com.ttlock.bl.sdk.constant.Constant
import com.ttlock.bl.sdk.constant.OperationType
import java.lang.Exception
import java.util.*
import kotlin.collections.LinkedHashSet

/**
 * Created by Smartlock on 2016/6/1.
 */
internal object CommandUtil_V3 {
    private const val DBG = true
    fun addAdmin_V3(
        command: Command,
        adminPassword: String?,
        unlockNumber: String?,
        aesKeyArray: ByteArray?
    ) {
        val values = ByteArray(4 + 4 + 7) //4 + 4 + 7
        val adminPwd = Integer.valueOf(adminPassword)
        val unlockPwd = Integer.valueOf(unlockNumber)
        val adminPwd_byte: ByteArray = DigitUtil.integerToByteArray(adminPwd)
        val unlockPwd_byte: ByteArray = DigitUtil.integerToByteArray(unlockPwd)
        System.arraycopy(adminPwd_byte, 0, values, 0, adminPwd_byte.size) //4
        System.arraycopy(unlockPwd_byte, 0, values, 4, unlockPwd_byte.size) //4
        System.arraycopy(Constant.SCIENER.toByteArray(), 0, values, 8, 7) //7
        command.setData(values, aesKeyArray)
    }

    fun checkAdmin(
        command: Command,
        uid: Int,
        adminPs: String?,
        unlockKey: String?,
        lockFlagPos: Int,
        aesKeyArray: ByteArray?,
        apiCommand: Int
    ) {
        val adminPsByteArray: ByteArray = DigitUtil.integerToByteArray(Integer.valueOf(adminPs))
        val lockFlagPos_byteArray: ByteArray = DigitUtil.integerToByteArray(lockFlagPos)
        val values = ByteArray(11)
        //        LogUtil.d("lockFlagPos:" + lockFlagPos, DBG);
//        LogUtil.d("lockFlagPos_byteArray:" + Arrays.toString(lockFlagPos_byteArray), DBG);
        System.arraycopy(adminPsByteArray, 0, values, 0, adminPsByteArray.size)
        System.arraycopy(lockFlagPos_byteArray, 1, values, 4, 3)
        System.arraycopy(DigitUtil.integerToByteArray(uid), 0, values, 7, 4)
        //        LogUtil.d("values:" + Arrays.toString(values), DBG);
        command.setData(values, aesKeyArray)
    }

    /**
     * 设置管理员键盘密码
     * @param command
     * @param adminKeyboardPwd
     * @param aesKeyArray
     */
    fun setAdminKeyboardPwd(command: Command, adminKeyboardPwd: String, aesKeyArray: ByteArray?) {
        val len = adminKeyboardPwd.length
        val values = ByteArray(len)
        for (i in 0 until len) {
            values[i] = (adminKeyboardPwd[i] - '0').toByte()
        }
        command.setData(values, aesKeyArray)
    }

    /**
     * 设置删除密码
     * @param command
     * @param deletePwd
     * @param aesKeyArray
     */
    fun setDeletePwd(command: Command, deletePwd: String, aesKeyArray: ByteArray?) {
        val values = ByteArray(10)
        val len = deletePwd.length
        for (i in 0 until len) {
            values[i] = (deletePwd[i] - '0').toByte()
        }
        for (i in len..9) {
            values[i] = 0xFF.toByte()
        }
        command.setData(values, aesKeyArray)
    }

    fun checkUserTime(
        command: Command,
        uid: Int,
        sDateStr: String,
        eDateStr: String,
        unlockKey: String?,
        lockFlagPos: Int,
        aesKeyArray: ByteArray?,
        apiCommand: Int
    ) {
        val values = ByteArray(17) //5+5+3+4
        val time: ByteArray = DigitUtil.convertTimeToByteArray(sDateStr + eDateStr)
        System.arraycopy(time, 0, values, 0, 10)
        values[10] = (lockFlagPos shr 16 and 0xFF).toByte()
        values[11] = (lockFlagPos shr 8 and 0xFF).toByte()
        values[12] = (lockFlagPos and 0xFF).toByte()
        val uidArray: ByteArray = DigitUtil.integerToByteArray(uid)
        System.arraycopy(uidArray, 0, values, 13, 4)
        command.setData(values, aesKeyArray)
    }

    fun calibationTime_V3(command: Command, timeStr: String?, aesKeyArray: ByteArray?) {
        val timeArray: ByteArray = DigitUtil.convertTimeToByteArray(timeStr)
        command.setData(timeArray, aesKeyArray)
    }

    /**
     * 开门
     * @param command
     * @param sum
     * @param dateTime      时间(也作为记录唯一标识)
     * @param aesKeyArray
     * @param timezoneRawOffSet 时间偏移量
     */
    fun unlock(
        command: Command,
        sum: String?,
        dateTime: Long,
        aesKeyArray: ByteArray?,
        timezoneRawOffSet: Long
    ) {
        val values = ByteArray(4 + 4) //不再进行时间校准
        //        if(dateTime > 0) //有时间进行更新
//            values = new byte[14];
//        else
//            values = new byte[8];
        val sumI = Integer.valueOf(sum)
        val sumByteArray: ByteArray = DigitUtil.integerToByteArray(sumI)
        System.arraycopy(sumByteArray, 0, values, 0, sumByteArray.size)
        //时间唯一标识
        var date = (dateTime / 1000).toInt()
        if (dateTime <= 0) //没有时间值的用系统时间
            date = (System.currentTimeMillis() / 1000).toInt()
        System.arraycopy(DigitUtil.integerToByteArray(date), 0, values, 4, 4)
        //        if(dateTime > 0) {//有时间的传时间
//            //根据时间偏移量重新计算时间
//            dateTime = dateTime + timezoneRawOffSet - TimeZone.getDefault().getOffset(System.currentTimeMillis());
//
//            String dateStr = DigitUtil.formateDateFromLong(dateTime, "yyMMddHHmmss");
//            byte[] dateByteArray = DigitUtil.convertTimeToByteArray(dateStr);
//            System.arraycopy(dateByteArray, 0, values, 8, dateByteArray.length);
//        }
        command.setData(values, aesKeyArray)
    }

    fun activateLiftFloors(command: Command, psFromLock: ByteArray?, transferData: TransferData) {
        command.setCommand(Command.Companion.COMM_UNLOCK)
        val psFromLockL: Long = DigitUtil.fourBytesToLong(psFromLock)
        val unlockKeyL: Long = java.lang.Long.valueOf(transferData.getUnlockKey())
        //        String sum = DigitUtil.getUnlockPwd_new(psFromLockL, unlockKeyL);
        var dateTime: Long = transferData.getUnlockDate()
        if (dateTime <= 0) { //没有时间值的用系统时间
            dateTime = System.currentTimeMillis()
        }
        val timezoneRawOffSet: Long = transferData.getTimezoneOffSet()
        val floors: List<Int> = transferData.getActivateFloors()
        val floorSize = floors.size
        val values = ByteArray(4 + 4 + 6 + 1 + floorSize) //不再进行时间校准
        val sumByteArray: ByteArray = DigitUtil.getUnlockPwdBytes_new(psFromLockL, unlockKeyL)
        System.arraycopy(sumByteArray, 0, values, 0, sumByteArray.size)
        //时间唯一标识
        val date = (dateTime / 1000).toInt()
        System.arraycopy(DigitUtil.integerToByteArray(date), 0, values, 4, 4)
        //根据时间偏移量重新计算时间
        dateTime = dateTime + timezoneRawOffSet - TimeZone.getDefault()
            .getOffset(System.currentTimeMillis())
        val dateStr: String = DigitUtil.formateDateFromLong(dateTime, "yyMMddHHmmss")
        val dateByteArray: ByteArray = DigitUtil.convertTimeToByteArray(dateStr)
        System.arraycopy(dateByteArray, 0, values, 8, dateByteArray.size)
        values[14] = floorSize.toByte()
        var index = 15
        for (floorNumber in floors) {
            if (floorNumber != null) {
                values[index++] = floorNumber.toByte()
            }
        }
        command.setData(values, transferData.getAesKeyArray())
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun checkRandom(command: Command, sum: String?, aesKeyArray: ByteArray?) {
        val sumI = Integer.valueOf(sum)
        val sumByteArray: ByteArray = DigitUtil.integerToByteArray(sumI)
        command.setData(sumByteArray, aesKeyArray)
    }

    /**
     * 三代锁密码初始化
     * @param lockType
     * @param aesKeyArray
     */
    fun initPasswords(lockType: Int, aesKeyArray: ByteArray?, apiCommand: Int) {
        val command = Command(lockType)
        command.setCommand(Command.Companion.COMM_INIT_PASSWORDS)
        val values = ByteArray(61)
        //TODO:calender
        var year = Date().year + 1900
        val month = Date().month
        val day = Date().date
        if (month == 0 && day == 1) {
            year--
        }
        LogUtil.d("year : $year", DBG)
        val codeSet: MutableSet<*> = LinkedHashSet<Int>() //密码约定数
        while (codeSet.size < 10) { //约定数最大不能超过1071
            codeSet.add(DigitUtil.getRandomIntegerByUpperBound(1071))
        }
        val secretKeySet: MutableSet<*> = LinkedHashSet<String>() //映射数
        while (secretKeySet.size < 10) {
            secretKeySet.add(DigitUtil.getCheckTable())
        }
        val codeIter: Iterator<Int> = codeSet.iterator()
        val secretKeyIter: Iterator<String> = secretKeySet.iterator()
        var offset = 0
        //取年份后两位
        values[offset++] = (year % 100).toByte()

//        JsonArray jsonArray = new JsonArray();
        val list: MutableList<PwdInfoV3> = ArrayList<PwdInfoV3>()
        var i = 0
        while (i < 10) {
            val code = codeIter.next()
            val secretKey = secretKeyIter.next()
            list.add(PwdInfoV3.Companion.getInstance(year, code, secretKey))
            val codeAndKey: ByteArray = DigitUtil.convertCodeAndKeyToByteArray(code, secretKey)
            System.arraycopy(codeAndKey, 0, values, offset, 6)
            offset += 6
            i++
            year++
        }
        initPasswords(command, values, aesKeyArray)
        val pwdInfoSource: String = GsonUtil.toJson<List<PwdInfoV3>>(list)
        val timestamp = System.currentTimeMillis()
        val pwdInfo = CommandUtil.encry(pwdInfoSource, timestamp)
        BluetoothImpl.Companion.getInstance()
            .sendCommand(command.buildCommand(), pwdInfo, timestamp, apiCommand)
    }

    fun initPasswords(command: Command, values: ByteArray?, aesKeyArray: ByteArray?) {
        command.setData(values, aesKeyArray)
    }

    fun resetKeyboardPasswordCount(command: Command, values: ByteArray?, aesKeyArray: ByteArray?) {
        command.setData(values, aesKeyArray)
    }

    fun setLockname(command: Command, lockname: String, aesKeyArray: ByteArray?) {
        command.setData(lockname.toByteArray(), aesKeyArray)
    }

    /**
     * 获取操作日志
     * @param command
     * @param seq
     * @param aesKeyArray
     */
    fun getOperateLog(command: Command, seq: Short, aesKeyArray: ByteArray?) {
        val values = ByteArray(2)
        values[0] = (seq shr 8).toByte()
        values[1] = seq.toByte()
        command.setData(values, aesKeyArray)
    }

    fun getValidKeyboardPassword(command: Command, seq: Short, aesKeyArray: ByteArray?) {
        val values = ByteArray(2)
        values[0] = (seq shr 8).toByte()
        values[1] = seq.toByte()
        command.setData(values, aesKeyArray)
    }

    fun parseKeyboardPwd(datas: ByteArray): Short {
        //记录总长度
        val recordTotalLen: Short = (datas[0] shl 8 or (datas[1] and 0xff)).toShort()
        if (recordTotalLen.toInt() == 0) //没有记录可读取
            return recordTotalLen
        //请求序号
        val nextReq: Short = (datas[2] shl 8 or (datas[3] and 0xff)).toShort()
        //记录长度index
        var dataIndex = 4
        while (dataIndex + 1 < datas.size) {
            //单条记录长度
            val recLen = datas[dataIndex++].toInt()
            //密码类型
            val pwdType = datas[dataIndex++].toInt()
            //密码长度
            val pwdLen = datas[dataIndex++].toInt()
            //密码
            val pwd = String(Arrays.copyOfRange(datas, dataIndex, dataIndex + pwdLen))
            dataIndex += pwdLen
            //原始密码长度
            val originPwdLen = datas[dataIndex++].toInt()
            //原始密码
            val originalPwd = String(Arrays.copyOfRange(datas, dataIndex, dataIndex + originPwdLen))
            dataIndex += originPwdLen
            when (pwdType) {
                KeyboardPwdType.PWD_TYPE_COUNT -> {
                    val count: Int = DigitUtil.fourBytesToLong(
                        Arrays.copyOfRange(
                            datas,
                            dataIndex,
                            dataIndex + 4
                        )
                    ).toInt()
                    dataIndex += 4
                }
                KeyboardPwdType.PWD_TYPE_PERIOD -> {
                    val year = datas[dataIndex++].toInt()
                    val mounth = datas[dataIndex++].toInt()
                    val day = datas[dataIndex++].toInt()
                    val hour = datas[dataIndex++].toInt()
                    val minute = datas[dataIndex++].toInt()
                }
            }
        }
        return nextReq
    }

    fun searchPwd(command: Command, seq: Short, aesKey: ByteArray?) {
        val values = ByteArray(2)
        values[0] = (seq shr 8).toByte()
        values[1] = seq.toByte()
        command.setData(values, aesKey)
    }

    fun manageKeyboardPassword(
        command: Command,
        pwdOperateType: Byte,
        keyboardPwdType: Byte,
        cycleType: Int,
        originalPwd: String,
        newPwd: String,
        startDate: Long,
        endDate: Long,
        aesKeyArray: ByteArray?,
        timezoneOffset: Long
    ) {
        var timezoneOffset = timezoneOffset
        var values: ByteArray? = null
        var index = 0
        val calendar: Calendar = Calendar.getInstance()
        //根据时间偏移量计算时间
        val timeZone: TimeZone = TimeZone.getDefault()
        if (timeZone.inDaylightTime(Date(System.currentTimeMillis()))) timezoneOffset -= timeZone.getDSTSavings()
            .toLong()
        timeZone.setRawOffset(timezoneOffset.toInt())
        calendar.setTimeZone(timeZone)
        var date = Date(startDate)
        calendar.setTime(date)
        LogUtil.e("timezoneOffset:$timezoneOffset", DBG)
        LogUtil.e("startDate:$startDate", DBG)
        LogUtil.e("endDate:$endDate", DBG)
        LogUtil.d("pwdOperateType:$pwdOperateType", DBG)
        when (pwdOperateType) {
            PwdOperateType.PWD_OPERATE_TYPE_CLEAR -> {
                values = ByteArray(1)
                values[index++] = pwdOperateType
            }
            PwdOperateType.PWD_OPERATE_TYPE_ADD -> {

//                Calendar calendar = Calendar.getInstance();
//                Date date = new Date(startDate);
//                calendar.setTime(date);
                val originalPwdLen = originalPwd.length.toByte()
                when (keyboardPwdType) {
                    KeyboardPwdType.PWD_TYPE_COUNT, KeyboardPwdType.PWD_TYPE_PERIOD -> {
                        values = ByteArray(1 + 1 + 1 + originalPwdLen + 5 + 5)
                        values[index++] = pwdOperateType
                        values[index++] = keyboardPwdType
                        values[index++] = originalPwdLen
                        System.arraycopy(
                            originalPwd.toByteArray(),
                            0,
                            values,
                            index,
                            originalPwdLen.toInt()
                        )
                        index += originalPwdLen.toInt()
                        values[index++] = (calendar.get(Calendar.YEAR) % 100).toByte()
                        values[index++] = (calendar.get(Calendar.MONTH) + 1).toByte()
                        values[index++] = calendar.get(Calendar.DAY_OF_MONTH).toByte()
                        values[index++] = calendar.get(Calendar.HOUR_OF_DAY).toByte()
                        values[index++] = calendar.get(Calendar.MINUTE).toByte()
                        LogUtil.d("y:" + values[index - 5])
                        LogUtil.d("m:" + values[index - 4])
                        LogUtil.d("d:" + values[index - 3])
                        LogUtil.d("h:" + values[index - 2])
                        LogUtil.d("m:" + values[index - 1])
                        LogUtil.d("hour:" + calendar.get(Calendar.HOUR_OF_DAY), DBG)
                        date = Date(endDate)
                        calendar.setTime(date)
                        values[index++] = (calendar.get(Calendar.YEAR) % 100).toByte()
                        values[index++] = (calendar.get(Calendar.MONTH) + 1).toByte()
                        values[index++] = calendar.get(Calendar.DAY_OF_MONTH).toByte()
                        values[index++] = calendar.get(Calendar.HOUR_OF_DAY).toByte()
                        values[index++] = calendar.get(Calendar.MINUTE).toByte()
                    }
                    KeyboardPwdType.PWD_TYPE_PERMANENT -> {
                        values = ByteArray(1 + 1 + 1 + originalPwdLen + 5)
                        values[index++] = pwdOperateType
                        values[index++] = keyboardPwdType
                        values[index++] = originalPwdLen
                        System.arraycopy(
                            originalPwd.toByteArray(),
                            0,
                            values,
                            index,
                            originalPwdLen.toInt()
                        )
                        index += originalPwdLen.toInt()
                        values[index++] = (calendar.get(Calendar.YEAR) % 100).toByte()
                        values[index++] = (calendar.get(Calendar.MONTH) + 1).toByte()
                        values[index++] = calendar.get(Calendar.DAY_OF_MONTH).toByte()
                        values[index++] = calendar.get(Calendar.HOUR_OF_DAY).toByte()
                        values[index++] = calendar.get(Calendar.MINUTE).toByte()
                    }
                    else -> {}
                }
            }
            PwdOperateType.PWD_OPERATE_TYPE_REMOVE_ONE -> {
                val originalPwdLen = originalPwd.length.toByte()
                values = ByteArray(1 + 1 + 1 + originalPwdLen)
                values[index++] = pwdOperateType
                values[index++] = keyboardPwdType
                values[index++] = originalPwdLen
                System.arraycopy(
                    originalPwd.toByteArray(),
                    0,
                    values,
                    index,
                    originalPwdLen.toInt()
                )
            }
            PwdOperateType.PWD_OPERATE_TYPE_MODIFY -> {
                val originalPwdLen = originalPwd.length.toByte()
                //新密码长度
                var newPwdLen: Byte = 0
                if (!TextUtils.isEmpty(newPwd)) newPwdLen = newPwd.length.toByte()
                values = if (startDate <= 0 || endDate <= 0) //不修改期限
                    ByteArray(1 + 1 + 1 + originalPwdLen + 1 + newPwdLen) else ByteArray(1 + 1 + 1 + originalPwdLen + 1 + newPwdLen + 5 + 5)
                values[index++] = pwdOperateType
                values[index++] = keyboardPwdType
                values[index++] = originalPwdLen
                System.arraycopy(
                    originalPwd.toByteArray(),
                    0,
                    values,
                    index,
                    originalPwdLen.toInt()
                )
                index += originalPwdLen.toInt()
                values[index++] = newPwdLen
                System.arraycopy(newPwd.toByteArray(), 0, values, index, newPwdLen.toInt())
                index += newPwdLen.toInt()
                if (!(startDate <= 0 || endDate <= 0)) { //修改期限
                    values[index++] = (calendar.get(Calendar.YEAR) % 100).toByte()
                    values[index++] = (calendar.get(Calendar.MONTH) + 1).toByte()
                    values[index++] = calendar.get(Calendar.DAY_OF_MONTH).toByte()
                    values[index++] = calendar.get(Calendar.HOUR_OF_DAY).toByte()
                    values[index++] = calendar.get(Calendar.MINUTE).toByte()
                    date = Date(endDate)
                    calendar.setTime(date)
                    values[index++] = (calendar.get(Calendar.YEAR) % 100).toByte()
                    values[index++] = (calendar.get(Calendar.MONTH) + 1).toByte()
                    values[index++] = calendar.get(Calendar.DAY_OF_MONTH).toByte()
                    values[index++] = calendar.get(Calendar.HOUR_OF_DAY).toByte()
                    values[index++] = calendar.get(Calendar.MINUTE).toByte()
                }
            }
            PwdOperateType.PWD_OPERATE_TYPE_RECOVERY -> {
                val originalPwdLen = originalPwd.length.toByte()
                //新密码长度
                var newPwdLen: Byte = 0
                if (!TextUtils.isEmpty(newPwd)) newPwdLen = newPwd.length.toByte()
                LogUtil.e("originalPwd:$originalPwd", DBG)
                LogUtil.e("newPwd:$newPwd", DBG)
                LogUtil.e("keyboardPwdType:$keyboardPwdType", DBG)
                when (keyboardPwdType) {
                    KeyboardPwdType.PWD_TYPE_COUNT, KeyboardPwdType.PWD_TYPE_PERMANENT -> values =
                        ByteArray(1 + 1 + 1 + originalPwdLen + 1 + newPwdLen + 5)
                    KeyboardPwdType.PWD_TYPE_CIRCLE -> values =
                        ByteArray(1 + 1 + 1 + originalPwdLen + 1 + newPwdLen + 5 + 2)
                    KeyboardPwdType.PWD_TYPE_PERIOD -> values =
                        ByteArray(1 + 1 + 1 + originalPwdLen + 1 + newPwdLen + 5 + 5)
                }
                values!![index++] = pwdOperateType
                values[index++] = keyboardPwdType
                values[index++] = originalPwdLen
                System.arraycopy(
                    originalPwd.toByteArray(),
                    0,
                    values,
                    index,
                    originalPwdLen.toInt()
                )
                index += originalPwdLen.toInt()
                values[index++] = newPwdLen
                System.arraycopy(newPwd.toByteArray(), 0, values, index, newPwdLen.toInt())
                index += newPwdLen.toInt()

                //起始时间
                values[index++] = (calendar.get(Calendar.YEAR) % 100).toByte()
                values[index++] = (calendar.get(Calendar.MONTH) + 1).toByte()
                values[index++] = calendar.get(Calendar.DAY_OF_MONTH).toByte()
                values[index++] = calendar.get(Calendar.HOUR_OF_DAY).toByte()
                values[index++] = calendar.get(Calendar.MINUTE).toByte()
                when (keyboardPwdType) {
                    KeyboardPwdType.PWD_TYPE_CIRCLE -> {
                        values[index++] = (cycleType shr 8).toByte()
                        values[index++] = cycleType.toByte()
                    }
                    KeyboardPwdType.PWD_TYPE_PERIOD -> {
                        date = Date(endDate)
                        calendar.setTime(date)
                        values[index++] = (calendar.get(Calendar.YEAR) % 100).toByte()
                        values[index++] = (calendar.get(Calendar.MONTH) + 1).toByte()
                        values[index++] = calendar.get(Calendar.DAY_OF_MONTH).toByte()
                        values[index++] = calendar.get(Calendar.HOUR_OF_DAY).toByte()
                        values[index++] = calendar.get(Calendar.MINUTE).toByte()
                        LogUtil.e(
                            "calendar.get(Calendar.DAY_OF_MONTH):" + calendar.get(Calendar.DAY_OF_MONTH),
                            DBG
                        )
                    }
                    else -> {}
                }
            }
            else -> {}
        }
        command.setData(values, aesKeyArray)
    }

    fun parseOperateLog(
        logOperates: MutableList<LogOperate?>,
        datas: ByteArray,
        timezoneOffSet: Long
    ): Short {
        //记录总长度
        var timezoneOffSet = timezoneOffSet
        LogUtil.w("begin---------------", DBG)
        //        LogUtil.e("datas:" + DigitUtil.byteArrayToHexString(datas), DBG);
        val recordTotalLen: Int = datas[0] shl 8 or (datas[1] and 0xff) and 0xffff
        if (recordTotalLen == 0) //没有记录可读
            return 0xFFF0.toShort() //记录读取完成
        val nextReq: Short = (datas[2] shl 8 or (datas[3] and 0xff)).toShort()

        //索引下标
        var dataIndex = 4
        //        LogUtil.d("recordTotalLen:" + recordTotalLen, DBG);
//        LogUtil.d("nextReq:" + nextReq, DBG);
//        LogUtil.d("datas.length:" + datas.length, DBG);
        try {
            while (dataIndex + 1 < datas.size) {
//                LogUtil.d("begin", DBG);
//                LogUtil.d("dataIndex:" + dataIndex, DBG);
                val logOperate = LogOperate()

                //单条记录长度
                val recLen = datas[dataIndex++].toInt()
                //下一条记录开始的索引值
                val nextRecIndex = dataIndex + recLen
                //                LogUtil.d("recLen:" + recLen, DBG);
                //操作类型
                val operateType = datas[dataIndex++].toInt()
                //                LogUtil.d("operateType:" + operateType, DBG);
                logOperate.setRecordType(operateType)
                //年
                var year = datas[dataIndex++] + 2000
                //月
                var month = datas[dataIndex++].toInt()
                //日
                var day = datas[dataIndex++].toInt()
                //小时
                var hour = datas[dataIndex++].toInt()
                //分钟
                var minute = datas[dataIndex++].toInt()
                //秒
                val second = datas[dataIndex++].toInt()
                val calendar: Calendar = Calendar.getInstance()
                //根据时间偏移量计算时间
                val timeZone: TimeZone = TimeZone.getDefault()
                LogUtil.d("timezoneOffSet:$timezoneOffSet", DBG)
                if (timeZone.inDaylightTime(Date(System.currentTimeMillis()))) timezoneOffSet -= timeZone.getDSTSavings()
                    .toLong()
                timeZone.setRawOffset(timezoneOffSet.toInt())
                calendar.setTimeZone(timeZone)
                calendar.set(year, month - 1, day, hour, minute, second)
                logOperate.setOperateDate(calendar.getTimeInMillis() / 1000 * 1000)

//                LogUtil.d("year:" + year, DBG);
//                LogUtil.d("month:" + month, DBG);
//                LogUtil.d("day:" + day, DBG);
//                LogUtil.d("hour:" + hour, DBG);
//                LogUtil.d("minute:" + minute, DBG);
//                LogUtil.d("second:" + second, DBG);

                //电量
                val electricQuantity = datas[dataIndex++].toInt()
                logOperate.setElectricQuantity(electricQuantity)
                when (operateType) {
                    LogOperate.Companion.OPERATE_TYPE_MOBILE_UNLOCK, LogOperate.Companion.OPERATE_BLE_LOCK, LogOperate.Companion.GATEWAY_UNLOCK, LogOperate.Companion.APP_UNLOCK_FAILED_LOCK_REVERSE -> {
                        var uid: Int
                        var uuid: Int
                        uid = DigitUtil.fourBytesToLong(
                            Arrays.copyOfRange(
                                datas,
                                dataIndex,
                                dataIndex + 4
                            )
                        ).toInt()
                        //                        LogUtil.d("uid:" + uid, DBG);
                        dataIndex += 4
                        uuid = DigitUtil.fourBytesToLong(
                            Arrays.copyOfRange(
                                datas,
                                dataIndex,
                                dataIndex + 4
                            )
                        ).toInt()
                        //                        LogUtil.d("uuid:" + uuid, DBG);
                        dataIndex += 4
                        logOperate.setUid(uid)
                        logOperate.setRecordId(uuid)
                    }
                    LogOperate.Companion.REMOTE_CONTROL_KEY -> {
                        var uid: Int
                        var uuid: Int
                        uid = DigitUtil.fourBytesToLong(
                            Arrays.copyOfRange(
                                datas,
                                dataIndex,
                                dataIndex + 4
                            )
                        ).toInt()
                        //                        LogUtil.d("uid:" + uid, DBG);
                        dataIndex += 4
                        uuid = DigitUtil.fourBytesToLong(
                            Arrays.copyOfRange(
                                datas,
                                dataIndex,
                                dataIndex + 4
                            )
                        ).toInt()
                        //                        LogUtil.d("uuid:" + uuid, DBG);
                        dataIndex += 4
                        logOperate.setUid(uid)
                        logOperate.setRecordId(uuid)
                        logOperate.setKeyId(datas[dataIndex++].toInt())
                    }
                    LogOperate.Companion.OPERATE_TYPE_KEYBOARD_PASSWORD_UNLOCK, LogOperate.Companion.OPERATE_TYPE_USE_DELETE_CODE, LogOperate.Companion.OPERATE_TYPE_PASSCODE_EXPIRED, LogOperate.Companion.OPERATE_TYPE_SPACE_INSUFFICIENT, LogOperate.Companion.OPERATE_TYPE_PASSCODE_IN_BLACK_LIST, LogOperate.Companion.PASSCODE_LOCK, LogOperate.Companion.PASSCODE_UNLOCK_FAILED_LOCK_REVERSE -> {


//                        LogUtil.d("dataIndex:" + dataIndex, DBG);
                        //原始密码长度
                        val originalPwdLen = datas[dataIndex++].toInt()
                        //                        LogUtil.d("originalPwdLen:" + originalPwdLen, DBG);
                        //原始密码
                        val originalPwd =
                            String(Arrays.copyOfRange(datas, dataIndex, dataIndex + originalPwdLen))
                        //                        LogUtil.d("originalPwd:" + originalPwd, DBG);
                        dataIndex += originalPwdLen
                        //开锁密码长度
                        val unlockPwdLen = datas[dataIndex++].toInt()
                        //                        LogUtil.d("unlockPwdLen:" + unlockPwdLen, DBG);
                        //开锁密码
                        val unlockPwd =
                            String(Arrays.copyOfRange(datas, dataIndex, dataIndex + unlockPwdLen))
                        //                        LogUtil.d("unlockPwd:" + unlockPwd, DBG);
                        dataIndex += unlockPwdLen
                        logOperate.setPassword(originalPwd)
                        logOperate.setNewPassword(unlockPwd)
                    }
                    LogOperate.Companion.OPERATE_TYPE_KEYBOARD_MODIFY_PASSWORD -> {

                        //原始密码长度
                        val originalPwdLen = datas[dataIndex++].toInt()
                        //原始密码
                        val originalPwd =
                            String(Arrays.copyOfRange(datas, dataIndex, dataIndex + originalPwdLen))
                        dataIndex += originalPwdLen
                        //新密码长度
                        val newPwdLen = datas[dataIndex++].toInt()
                        //新密码
                        val newPwd =
                            String(Arrays.copyOfRange(datas, dataIndex, dataIndex + newPwdLen))
                        dataIndex += newPwdLen
                        logOperate.setPassword(originalPwd)
                        logOperate.setNewPassword(newPwd)
                    }
                    LogOperate.Companion.OPERATE_TYPE_KEYBOARD_REMOVE_SINGLE_PASSWORD -> {

                        //原始密码长度
                        val originalPwdLen = datas[dataIndex++].toInt()
                        //原始密码
                        val originalPwd =
                            String(Arrays.copyOfRange(datas, dataIndex, dataIndex + originalPwdLen))
                        dataIndex += originalPwdLen
                        //删除密码长度
                        val removePwdLen = datas[dataIndex++].toInt()
                        //删除密码
                        val removePwd =
                            String(Arrays.copyOfRange(datas, dataIndex, dataIndex + removePwdLen))
                        dataIndex += removePwdLen
                        logOperate.setPassword(originalPwd)
                        logOperate.setNewPassword(removePwd)
                    }
                    LogOperate.Companion.OPERATE_TYPE_ERROR_PASSWORD_UNLOCK -> {

                        //错误开锁密码长度
                        val errUnlockPwdLen = datas[dataIndex++].toInt()
                        //错误开锁密码
                        val errUnlockPwd = String(
                            Arrays.copyOfRange(
                                datas,
                                dataIndex,
                                dataIndex + errUnlockPwdLen
                            )
                        )
                        dataIndex += errUnlockPwdLen
                        logOperate.setPassword(errUnlockPwd)
                    }
                    LogOperate.Companion.OPERATE_TYPE_KEYBOARD_REMOVE_ALL_PASSWORDS -> {
                        year = datas[dataIndex++].toInt()
                        month = datas[dataIndex++].toInt()
                        day = datas[dataIndex++].toInt()
                        hour = datas[dataIndex++].toInt()
                        minute = datas[dataIndex++].toInt()

//                        LogUtil.d("year:" + year, DBG);
//                        LogUtil.d("month:" + month, DBG);
//                        LogUtil.d("day:" + day, DBG);
//                        LogUtil.d("hour:" + hour, DBG);
//                        LogUtil.d("minute:" + minute, DBG);
                        if (dataIndex < nextRecIndex) {
                            val len = datas[dataIndex++].toInt()
                            val passcode =
                                String(Arrays.copyOfRange(datas, dataIndex, dataIndex + len))
                            logOperate.setPassword(passcode)
                        }
                        calendar.set(2000 + year, month - 1, day, hour, minute)
                        logOperate.setDeleteDate(calendar.getTimeInMillis())
                    }
                    LogOperate.Companion.OPERATE_TYPE_KEYBOARD_PASSWORD_KICKED -> {

                        //原始密码长度
                        val originalPwdLen = datas[dataIndex++].toInt()
                        //原始密码
                        val originalPwd =
                            String(Arrays.copyOfRange(datas, dataIndex, dataIndex + originalPwdLen))
                        dataIndex += originalPwdLen
                        //被挤掉的密码长度
                        val kickedPwdLen = datas[dataIndex++].toInt()
                        //删除密码
                        val kickedPwd =
                            String(Arrays.copyOfRange(datas, dataIndex, dataIndex + kickedPwdLen))
                        dataIndex += kickedPwdLen
                        logOperate.setPassword(originalPwd)
                        logOperate.setNewPassword(kickedPwd)
                    }
                    LogOperate.Companion.OPERATE_TYPE_ADD_IC, LogOperate.Companion.OPERATE_TYPE_DELETE_IC_SUCCEED, LogOperate.Companion.OPERATE_TYPE_IC_UNLOCK_SUCCEED, LogOperate.Companion.OPERATE_TYPE_IC_UNLOCK_FAILED, LogOperate.Companion.IC_LOCK, LogOperate.Companion.IC_UNLOCK_FAILED_LOCK_REVERSE, LogOperate.Companion.IC_UNLOCK_FAILED_BLANKLIST, LogOperate.Companion.CPU_CARD_UNLOCK_FAILED -> {
                        //                        Long cardNo = DigitUtil.fourBytesToLong(Arrays.copyOfRange(datas, dataIndex, dataIndex + 4));
                        val cardNo: Long = DigitUtil.bytesToLong(
                            Arrays.copyOfRange(
                                datas,
                                dataIndex,
                                nextRecIndex
                            )
                        )
                        LogUtil.d("card len:" + (nextRecIndex - dataIndex))
                        LogUtil.d("cardNo:$cardNo")
                        logOperate.setPassword(cardNo.toString())
                    }
                    LogOperate.Companion.OPERATE_TYPE_BONG_UNLOCK_SUCCEED -> {
                        val address: String = DigitUtil.getMacString(
                            Arrays.copyOfRange(
                                datas,
                                dataIndex,
                                dataIndex + 6
                            )
                        )
                        logOperate.setPassword(address)
                        dataIndex += 6
                    }
                    LogOperate.Companion.OPERATE_TYPE_FR_UNLOCK_SUCCEED, LogOperate.Companion.OPERATE_TYPE_ADD_FR, LogOperate.Companion.OPERATE_TYPE_FR_UNLOCK_FAILED, LogOperate.Companion.OPERATE_TYPE_DELETE_FR_SUCCEED, LogOperate.Companion.FR_LOCK, LogOperate.Companion.FR_UNLOCK_FAILED_LOCK_REVERSE -> {
                        val FNNo: Long = DigitUtil.sixBytesToLong(
                            Arrays.copyOfRange(
                                datas,
                                dataIndex,
                                dataIndex + 6
                            )
                        )
                        logOperate.setPassword(FNNo.toString())
                        dataIndex += 6
                        if (dataIndex < nextRecIndex) { //还有密码记录
                            val pwdLen = datas[dataIndex++].toInt()
                            val pwd =
                                String(Arrays.copyOfRange(datas, dataIndex, dataIndex + pwdLen))
                            dataIndex += pwdLen
                            //TODO:保存密码记录
                        }
                    }
                    LogOperate.Companion.WIRELESS_KEY_FOB -> {
                        val mac: String = DigitUtil.getMacString(
                            Arrays.copyOfRange(
                                datas,
                                dataIndex,
                                dataIndex + 6
                            )
                        )
                        logOperate.setPassword(mac)
                        dataIndex += 6
                        logOperate.setKeyId(datas[dataIndex++].toInt())
                        logOperate.setAccessoryElectricQuantity(datas[dataIndex++].toInt())
                    }
                    LogOperate.Companion.WIRELESS_KEY_PAD -> {
                        mac = DigitUtil.getMacString(
                            Arrays.copyOfRange(
                                datas,
                                dataIndex,
                                dataIndex + 6
                            )
                        )
                        logOperate.setPassword(mac)
                        dataIndex += 6
                        logOperate.setAccessoryElectricQuantity(datas[dataIndex++].toInt())
                    }
                    else -> {}
                }
                //                LogUtil.d("logOperate:" + logOperate);
                LogUtil.w("end", DBG)
                logOperates.add(logOperate)
                //增强兼容性
                dataIndex = nextRecIndex
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        LogUtil.d("end---------------", DBG)
        return nextReq
    }

    /**
     *
     * @param command
     * @param ICOp  操作
     * @param seq   序号
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @param timezoneOffSet    时间偏移量
     */
    fun ICManage(
        command: Command,
        ICOp: Byte,
        seq: Short,
        cardNoStr: String,
        startDate: Long,
        endDate: Long,
        aesKey: ByteArray?,
        timezoneOffSet: Long
    ) {
        var startDate = startDate
        var endDate = endDate
        var values: ByteArray? = null
        when (ICOp) {
            ICOperate.IC_SEARCH -> {
                values = ByteArray(3)
                values[0] = ICOp
                values[1] = (seq shr 8).toByte()
                values[2] = seq.toByte()
            }
            ICOperate.ADD -> if (!TextUtils.isEmpty(cardNoStr)) {
                values = if (cardNoStr.length > 10) ByteArray(19) else ByteArray(15)
                values[0] = ICOp
                if (startDate == 0L || endDate == 0L) { //有时间
                    startDate = CommandUtil.permanentStartDate
                    endDate = CommandUtil.permanentEndDate
                }
                val cardNo = java.lang.Long.valueOf(cardNoStr)
                val cardBytes: ByteArray
                cardBytes = if (cardNoStr.length > 10) {
                    DigitUtil.longToByteArrayWithLen(cardNo, 8)
                } else {
                    DigitUtil.integerToByteArray(cardNo.toInt())
                }

                //使用时间偏移量重新计算时间
                startDate = startDate + timezoneOffSet - TimeZone.getDefault()
                    .getOffset(System.currentTimeMillis())
                endDate = endDate + timezoneOffSet - TimeZone.getDefault()
                    .getOffset(System.currentTimeMillis())
                val sDate: String = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmm")
                val eDate: String = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmm")
                val time: ByteArray = DigitUtil.convertTimeToByteArray(sDate + eDate)
                LogUtil.d("sDate:$sDate", DBG)
                LogUtil.d("eDate:$eDate", DBG)
                System.arraycopy(cardBytes, 0, values, 1, cardBytes.size)
                System.arraycopy(time, 0, values, 1 + cardBytes.size, 10)
            } else { //进入添加模式
                values = ByteArray(1)
                values[0] = ICOp
            }
            ICOperate.CLEAR -> {
                values = ByteArray(1)
                values[0] = ICOp
            }
            ICOperate.MODIFY -> {
                values = if (cardNoStr.length > 10) ByteArray(19) else ByteArray(15)
                values[0] = ICOp
                val cardNo = java.lang.Long.valueOf(cardNoStr)
                val cardBytes: ByteArray
                cardBytes = if (cardNoStr.length > 10) {
                    DigitUtil.longToByteArrayWithLen(cardNo, 8)
                } else {
                    DigitUtil.integerToByteArray(cardNo.toInt())
                }
                //使用时间偏移量重新计算时间
                startDate = startDate + timezoneOffSet - TimeZone.getDefault()
                    .getOffset(System.currentTimeMillis())
                endDate = endDate + timezoneOffSet - TimeZone.getDefault()
                    .getOffset(System.currentTimeMillis())
                val sDate: String = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmm")
                val eDate: String = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmm")
                val time: ByteArray = DigitUtil.convertTimeToByteArray(sDate + eDate)
                LogUtil.d("sDate:$sDate", DBG)
                LogUtil.d("eDate:$eDate", DBG)
                System.arraycopy(cardBytes, 0, values, 1, cardBytes.size)
                System.arraycopy(time, 0, values, 1 + cardBytes.size, 10)
                LogUtil.d(sDate + eDate, DBG)
            }
            ICOperate.DELETE -> {
                values = if (cardNoStr.length > 10) ByteArray(9) else ByteArray(5)
                values[0] = ICOp
                cardNo = java.lang.Long.valueOf(cardNoStr)
                if (cardNoStr.length > 10) {
                    cardBytes = DigitUtil.longToByteArrayWithLen(cardNo, 8)
                } else {
                    cardBytes = DigitUtil.integerToByteArray(cardNo.toInt())
                }
                System.arraycopy(cardBytes, 0, values, 1, cardBytes.size)
            }
        }
        LogUtil.d("arrays:" + DigitUtil.byteArrayToHexString(values), DBG)
        command.setData(values, aesKey)
    }

    /**
     *
     * @param command
     * @param FROp  操作
     * @param seq   序号
     * @param FRNo    指纹号 6个字节
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @param timezoneOffSet    时间偏移量
     */
    @JvmOverloads
    fun FRManage(
        command: Command,
        FROp: Byte,
        seq: Short,
        FRNo: Long,
        startDate: Long,
        endDate: Long,
        aesKey: ByteArray?,
        timezoneOffSet: Long = 0
    ) {
        var startDate = startDate
        var endDate = endDate
        var values: ByteArray? = null
        when (FROp) {
            ICOperate.FR_SEARCH -> {
                values = ByteArray(3)
                values[0] = FROp
                values[1] = (seq shr 8).toByte()
                values[2] = seq.toByte()
            }
            ICOperate.ADD -> if (FRNo > 0) { //恢复
                values = ByteArray(17)
                values[0] = FROp
                val FRBytes: ByteArray = DigitUtil.longToByteArrayWithLen(FRNo, 6)
                LogUtil.d("FRBytes:" + DigitUtil.sixBytesToLong(FRBytes), DBG)
                if (startDate == 0L || endDate == 0L) {
                    startDate = CommandUtil.permanentStartDate
                    endDate = CommandUtil.permanentEndDate
                }
                //使用时间偏移量重新计算时间
                startDate = startDate + timezoneOffSet - TimeZone.getDefault()
                    .getOffset(System.currentTimeMillis())
                endDate = endDate + timezoneOffSet - TimeZone.getDefault()
                    .getOffset(System.currentTimeMillis())
                val sDate: String = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmm")
                val eDate: String = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmm")
                val time: ByteArray = DigitUtil.convertTimeToByteArray(sDate + eDate)
                System.arraycopy(FRBytes, 0, values, 1, 6)
                System.arraycopy(time, 0, values, 7, 10)
            } else { //添加
                values = ByteArray(1)
                values[0] = FROp
            }
            ICOperate.CLEAR -> {
                values = ByteArray(1)
                values[0] = FROp
            }
            ICOperate.MODIFY -> {
                values = ByteArray(17)
                values[0] = FROp
                LogUtil.d("FRNo:$FRNo", DBG)
                val FRBytes: ByteArray = DigitUtil.longToByteArrayWithLen(FRNo, 6)
                LogUtil.d("FRBytes:" + DigitUtil.sixBytesToLong(FRBytes), DBG)
                //使用时间偏移量重新计算时间
                startDate = startDate + timezoneOffSet - TimeZone.getDefault()
                    .getOffset(System.currentTimeMillis())
                endDate = endDate + timezoneOffSet - TimeZone.getDefault()
                    .getOffset(System.currentTimeMillis())
                val sDate: String = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmm")
                val eDate: String = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmm")
                val time: ByteArray = DigitUtil.convertTimeToByteArray(sDate + eDate)
                System.arraycopy(FRBytes, 0, values, 1, 6)
                System.arraycopy(time, 0, values, 7, 10)
                LogUtil.d(sDate + eDate, DBG)
            }
            ICOperate.DELETE -> {
                values = ByteArray(7)
                values[0] = FROp
                LogUtil.d(
                    "FRNO convert:" + DigitUtil.sixBytesToLong(
                        DigitUtil.longToByteArrayWithLen(
                            FRNo,
                            6
                        )
                    ), DBG
                )
                System.arraycopy(DigitUtil.longToByteArrayWithLen(FRNo, 6), 0, values, 1, 6)
                LogUtil.d(DigitUtil.byteArrayToHexString(values), DBG)
            }
        }
        command.setData(values, aesKey)
    }

    fun setWristKey(command: Command, wristKey: String, aesKeyArray: ByteArray?) {
        command.setData(wristKey.toByteArray(), aesKeyArray)
        //        command.data = wristKey.getBytes();
//        command.length = (byte) command.data.length;
    }

    /**
     * 闭锁管理
     * @param command
     * @param op 操作码
     * @param time 闭锁时间
     */
    fun autoLockManage(command: Command, op: Byte, time: Short, aesKey: ByteArray?) {
        var values: ByteArray? = null
        when (op) {
            AutoLockOperate.SEARCH -> {
                values = ByteArray(1)
                values[0] = op
            }
            AutoLockOperate.MODIFY -> {
                values = ByteArray(3)
                values[0] = op
                values[1] = (time shr 8).toByte()
                values[2] = time.toByte()
            }
        }
        //        LogUtil.d(DigitUtil.byteArrayToHexString(values), DBG);
        command.setData(values, aesKey)
    }

    /**
     *
     * @param op
     * @param opValue
     */
    fun operateDoorSensor(command: Command, op: Byte, opValue: Byte, aesKey: ByteArray?) {
        command.setCommand(Command.Companion.COMM_AUTO_LOCK_MANAGE)
        var values: ByteArray? = null
        when (op) {
            AutoLockOperate.SEARCH -> {
                values = ByteArray(1)
                values[0] = op
            }
            AutoLockOperate.MODIFY -> {
                values = ByteArray(4)
                values[0] = op
                //0xffff不修改自动闭锁时间
                values[1] = 0xff.toByte()
                values[2] = 0xff.toByte()
                values[3] = opValue
            }
        }
        command.setData(values, aesKey)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 闭锁
     * @param command
     * @param sum
     * @param dateTime      时间(也作为记录唯一标识)
     * @param aesKeyArray
     */
    fun lock(command: Command, sum: String?, dateTime: Long, aesKeyArray: ByteArray?) {
        val values = ByteArray(8)
        val sumI = Integer.valueOf(sum)
        val sumByteArray: ByteArray = DigitUtil.integerToByteArray(sumI)
        System.arraycopy(sumByteArray, 0, values, 0, sumByteArray.size)
        //时间唯一标识
        val date = (dateTime / 1000).toInt()
        System.arraycopy(DigitUtil.integerToByteArray(date), 0, values, 4, 4)
        command.setData(values, aesKeyArray)
    }

    /**
     * 屏幕密码管理
     * @param command
     * @param opType  1 - 查询 2 - 隐藏 3 - 显示
     * @param aesKeyArray
     */
    fun screenPasscodeManage(command: Command, opType: Int, aesKeyArray: ByteArray?) {
        val values: ByteArray
        values = if (opType == 1) { //查询
            byteArrayOf(1)
        } else {
            byteArrayOf(2, (opType - 2).toByte())
        }
        command.setData(values, aesKeyArray)
    }

    /**
     * 开启或者关闭远程开锁功能
     * @param command
     * @param opType 1 - 查询 2 - 修改
     * @param opValue     0 - 关闭 1 - 开启
     */
    fun controlRemoteUnlock(
        command: Command,
        opType: Byte,
        opValue: Byte,
        aesKeyArray: ByteArray?
    ) {
        command.setCommand(Command.Companion.COMM_CONTROL_REMOTE_UNLOCK)
        var values: ByteArray? = null
        when (opType) {
            ConfigRemoteUnlock.OP_TYPE_SEARCH -> values = byteArrayOf(opType)
            ConfigRemoteUnlock.OP_TYPE_MODIFY -> values = byteArrayOf(opType, opValue)
        }
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * query audio status or turn-on、turn-off audio
     * @param command
     * @param opType
     * @param opValue
     * @param aesKeyArray
     */
    fun audioManage(command: Command, opType: Byte, opValue: Byte, aesKeyArray: ByteArray?) {
        command.setCommand(Command.Companion.COMM_AUDIO_MANAGE)
        var values: ByteArray? = null
        when (opType) {
            AudioManage.QUERY -> values = byteArrayOf(opType)
            AudioManage.MODIFY -> values = byteArrayOf(opType, opValue)
        }
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 音量设置
     * 启用或者禁用语音提示（1 Byte）	音量（1 Byte）	语言（1 byte）
     * 0-	禁用1-	启用	1~5级音量
     *
     * 0-不修改音量	当前为第几种语言，喇叭版本才支持
     * 0-不修改语言
     * 1-第一种语音，常规为英文
     * 2-第二种语音，常规为中文
     * 3-第三种语音，一般产品没有
     * 蜂鸣器版本，此参数无效，可以设置任意值
     * @param command
     * @param transferData
     */
    fun setLockSound(command: Command, transferData: TransferData) {
        command.setCommand(Command.Companion.COMM_AUDIO_MANAGE)
        var values: ByteArray? = null
        var soundVolume: SoundVolume = transferData.getSoundVolume()
        //为空的情况当做关闭
        soundVolume = if (soundVolume == null) SoundVolume.OFF else soundVolume
        when (soundVolume) {
            SoundVolume.OFF -> {
                values = ByteArray(2)
                values[0] = AudioManage.MODIFY
                values[1] = AudioManage.TURN_OFF.toByte()
            }
            SoundVolume.ON -> {
                values = ByteArray(2)
                values[0] = AudioManage.MODIFY
                values[1] = AudioManage.TURN_ON.toByte()
            }
            else -> {
                values = ByteArray(4)
                values[0] = AudioManage.MODIFY
                values[1] = AudioManage.TURN_ON.toByte()
                values[2] = soundVolume.getValue().toByte()
                values[3] = 0 //当前无语言设置
            }
        }
        command.setData(values, transferData.getAesKeyArray())
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun getLockSound(command: Command, transferData: TransferData) {
        command.setCommand(Command.Companion.COMM_AUDIO_MANAGE)
        val values = byteArrayOf(AudioManage.QUERY)
        command.setData(values, transferData.getAesKeyArray())
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     *
     * @param command
     * @param opType
     * @param opValue
     * @param aesKeyArray
     */
    fun remoteControlManage(
        command: Command,
        opType: Byte,
        opValue: Byte,
        unlockPwdBytes: ByteArray,
        uniqueidBytes: ByteArray,
        aesKeyArray: ByteArray?
    ) {
        command.setCommand(Command.Companion.COMM_REMOTE_CONTROL_DEVICE_MANAGE)
        Log.d("OMG", "=remoteControlManage==")
        var values: ByteArray? = null
        when (opType) {
            RemoteControlManage.QUERY -> values = byteArrayOf(opType)
            RemoteControlManage.KEY -> {
                Log.d("OMG", "=remoteControlManage=KEY=")
                values = ByteArray(1 + unlockPwdBytes.size + uniqueidBytes.size + 1)
                values[0] = opType
                System.arraycopy(unlockPwdBytes, 0, values, 1, unlockPwdBytes.size)
                System.arraycopy(
                    uniqueidBytes,
                    0,
                    values,
                    1 + unlockPwdBytes.size,
                    uniqueidBytes.size
                )
                values[1 + unlockPwdBytes.size + uniqueidBytes.size] = opValue
            }
            else -> {}
        }
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance()
            .sendCommand(command.buildCommand(), APICommand.OP_REMOTE_CONTROL_DEVICE_MANAGEMENT)
    }

    fun configureNBServerAddress(
        command: Command,
        port: Short,
        address: String,
        aesKeyArray: ByteArray?
    ) {
        command.setCommand(Command.Companion.COMM_CONFIGURE_NB_ADDRESS)
        val addByte = address.toByteArray()
        val values = ByteArray(2 + addByte.size)
        LogUtil.d("port:$port", DBG)
        values[0] = (port shr 1).toByte()
        values[1] = port.toByte()
        System.arraycopy(addByte, 0, values, 2, address.length)
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun configureHotelData(
        command: Command,
        op: Int,
        parType: Int,
        hotelData: HotelData,
        aesKeyArray: ByteArray?
    ) {
        command.setCommand(Command.Companion.COMM_CONFIGURE_HOTEL_DATA)
        var values: ByteArray? = null
        when (op) {
            HotelData.Companion.GET -> values =
                byteArrayOf(HotelData.Companion.GET.toByte(), parType.toByte())
            HotelData.Companion.SET -> when (parType) {
                HotelData.Companion.TYPE_IC_KEY -> {
                    values = ByteArray(1 + 1 + 1 + 6)
                    run {
                        values!![0] = HotelData.Companion.SET.toByte()
                        values!![0] = values!![0]
                    }
                    values[1] = parType.toByte()
                    values[2] = 6
                    val icKey: ByteArray = hotelData.getICKeyByteArray()
                    System.arraycopy(icKey, 0, values, 3, icKey.size)
                }
                HotelData.Companion.TYPE_AES_KEY -> {
                    values = ByteArray(1 + 1 + 1 + 16)
                    run {
                        values!![0] = HotelData.Companion.SET.toByte()
                        values!![0] = values!![0]
                    }
                    values[1] = parType.toByte()
                    values[2] = 16
                    val icKeyAes: ByteArray = hotelData.getAesKeyByteArray()
                    System.arraycopy(icKeyAes, 0, values, 3, icKeyAes.size)
                }
                HotelData.Companion.TYPE_HOTEL_BUILDING_FLOOR -> {
                    values = ByteArray(1 + 1 + 1 + 3 + 1 + 1)
                    run {
                        values!![0] = HotelData.Companion.SET.toByte()
                        values!![0] = values!![0]
                    }
                    values[1] = parType.toByte()
                    values[2] = (3 + 1 + 1).toByte()
                    values[3] = (hotelData.hotelNumber shr 16).toByte()
                    values[4] = (hotelData.hotelNumber shr 8).toByte()
                    values[5] = hotelData.hotelNumber.toByte()
                    values[6] = hotelData.buildingNumber.toByte()
                    values[7] = hotelData.floorNumber.toByte()
                }
                HotelData.Companion.TYPE_SECTOR -> {
                    values = ByteArray(1 + 1 + 1 + 2)
                    values[0] = HotelData.Companion.SET.toByte()
                    values[1] = parType.toByte()
                    values[2] = 2
                    values[3] = (hotelData.sector shr 8).toByte()
                    values[4] = hotelData.sector.toByte()
                }
                HotelData.Companion.TYPE_ELEVATOR_CONTROLABLE_FLOORS -> {
                    values = ByteArray(1 + 1 + 1 + hotelData.controlableFloors.size)
                    values[0] = HotelData.Companion.SET.toByte()
                    values[1] = parType.toByte()
                    values[2] = hotelData.controlableFloors.size.toByte()
                    System.arraycopy(hotelData.controlableFloors, 0, values, 3, values[2].toInt())
                }
                HotelData.Companion.TYPE_ELEVATOR_WORK_MODE -> {
                    values = ByteArray(1 + 1 + 1 + 9)
                    values[0] = HotelData.Companion.SET.toByte()
                    values[1] = parType.toByte()
                    values[2] = 9
                    values[3] = hotelData.ttLiftWorkMode.getValue().toByte()
                    var i = 4
                    while (i < values.size) {
                        //0表示控制 1表示不控制
                        values[i] = 0
                        i++
                    }
                }
                HotelData.Companion.TYPE_POWER_SAVER_WORK_MODE -> {
                    values = ByteArray(1 + 1 + 1 + 1)
                    values[0] = HotelData.Companion.SET.toByte()
                    values[1] = parType.toByte()
                    values[2] = 1
                    values[3] = hotelData.powerWorkModeValue
                }
                HotelData.Companion.TYPE_POWER_SAVER_CONTROLABLE_LOCK -> {
                    values = ByteArray(1 + 1 + 1 + 6)
                    values[0] = HotelData.Companion.SET.toByte()
                    values[1] = parType.toByte()
                    values[2] = 6
                    val macBytes: ByteArray =
                        DigitUtil.macDividerByColonToByteArray(hotelData.getControlableLockMac())
                    System.arraycopy(macBytes, 0, values, 3, 6)
                }
            }
        }
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun configureNBServerAddress(transferData: TransferData) {
        val command: Command = Command(transferData.getLockVersion())
        command.setCommand(Command.Companion.COMM_CONFIGURE_NB_ADDRESS)
        val port = transferData.getPort() as Short
        LogUtil.d("port:$port", DBG)
        val address: String = transferData.getAddress()
        val addByte = address.toByteArray()
        val values = ByteArray(2 + addByte.size)
        values[0] = (port shr 8).toByte()
        values[1] = port.toByte()
        System.arraycopy(addByte, 0, values, 2, addByte.size)
        command.setData(values, transferData.getAesKeyArray())
        transferData.setTransferData(command.buildCommand())
        BluetoothImpl.Companion.getInstance().sendCommand(transferData)
    }

    fun getAdminCode(command: Command) {
        command.setCommand(Command.Companion.COMM_GET_ADMIN_CODE)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun writeFR(
        command: Command,
        frData: ByteArray,
        seq: Short,
        packetLen: Int,
        aeskeyArray: ByteArray?
    ) {
        var packetLen = packetLen
        command.setCommand(Command.Companion.COMM_FR_MANAGE)
        val srcPos = seq * packetLen
        if (frData.size - srcPos < packetLen) packetLen = frData.size - srcPos
        val values = ByteArray(1 + 2 + packetLen)
        values[0] = ICOperate.WRITE_FR
        values[1] = (seq shr 8).toByte()
        values[2] = seq.toByte()
        LogUtil.d("packetLen:$packetLen")
        System.arraycopy(frData, srcPos, values, 3, packetLen)
        command.setData(values, aeskeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun addFRTemp(command: Command, transferData: TransferData) {
        command.setCommand(Command.Companion.COMM_FR_MANAGE)
        val frTempNo: ByteArray = DigitUtil.integerToByteArray(transferData.getNo().toInt())
        val frData: ByteArray = transferData.getTransferData()
        val num = frData.size
        val numByte: ByteArray = DigitUtil.shortToByteArray(num.toShort())
        val checkSum: ByteArray = DigitUtil.checkSum(frData)
        var startDate: Long = transferData.getStartDate()
        var endDate: Long = transferData.getEndDate()
        LogUtil.d("startDate:$startDate", DBG)
        LogUtil.d("endDate:$endDate", DBG)
        LogUtil.d("num:$num", DBG)

        //根据时间偏移量重新计算时间
        startDate = startDate + transferData.getTimezoneOffSet() - TimeZone.getDefault().getOffset(
            System.currentTimeMillis()
        )
        endDate = endDate + transferData.getTimezoneOffSet() - TimeZone.getDefault().getOffset(
            System.currentTimeMillis()
        )
        val sDateStr: String = DigitUtil.formateDateFromLong(startDate, "yyMMddHHmm")
        val eDateStr: String = DigitUtil.formateDateFromLong(endDate, "yyMMddHHmm")
        val time: ByteArray = DigitUtil.convertTimeToByteArray(sDateStr + eDateStr)
        val values = ByteArray(1 + frTempNo.size + 2 + checkSum.size + time.size)
        values[0] = ICOperate.ADD
        System.arraycopy(frTempNo, 0, values, 1, frTempNo.size)
        System.arraycopy(numByte, 0, values, 5, numByte.size)
        System.arraycopy(checkSum, 0, values, 7, checkSum.size)
        System.arraycopy(time, 0, values, 9, time.size)
        command.setData(values, transferData.getAesKeyArray())
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun queryPassageMode(command: Command, seq: Byte, aesKeyArray: ByteArray?) {
        command.setCommand(Command.Companion.COMM_CONFIGURE_PASSAGE_MODE)
        val values = byteArrayOf(PassageModeOperate.QUERY, seq)
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun configurePassageMode(command: Command, transferData: TransferData, weekOrDay: Byte) {
        command.setCommand(Command.Companion.COMM_CONFIGURE_PASSAGE_MODE)
        val values = ByteArray(1 + 1 + 1 + 1 + 2 + 2)
        values[0] = PassageModeOperate.ADD
        values[1] = transferData.getOp().toByte()
        values[2] = weekOrDay
        values[3] = transferData.getOpValue().toByte()
        val startDate: Int = transferData.getStartDate().toInt()
        val endDate: Int = transferData.getEndDate().toInt()
        values[4] = (startDate / 60).toByte()
        values[5] = (startDate % 60).toByte()
        values[6] = (endDate / 60).toByte()
        values[7] = (endDate % 60).toByte()
        LogUtil.d("s h:" + values[4])
        LogUtil.d("s m:" + values[5])
        LogUtil.d("e h:" + values[6])
        LogUtil.d("e m:" + values[7])
        command.setData(values, transferData.getAesKeyArray())
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun deletePassageMode(command: Command, transferData: TransferData, weekOrDay: Byte) {
        command.setCommand(Command.Companion.COMM_CONFIGURE_PASSAGE_MODE)
        val values = ByteArray(1 + 1 + 1 + 1 + 2 + 2)
        values[0] = PassageModeOperate.DELETE
        values[1] = transferData.getOp().toByte()
        values[2] = weekOrDay
        values[3] = transferData.getOpValue().toByte()
        val startDate: Int = transferData.getStartDate().toInt()
        val endDate: Int = transferData.getEndDate().toInt()
        values[4] = (startDate / 60).toByte()
        values[5] = (startDate % 60).toByte()
        values[6] = (endDate / 60).toByte()
        values[7] = (endDate % 60).toByte()
        command.setData(values, transferData.getAesKeyArray())
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun clearPassageMode(command: Command, transferData: TransferData) {
        command.setCommand(Command.Companion.COMM_CONFIGURE_PASSAGE_MODE)
        val values = byteArrayOf(PassageModeOperate.CLEAR)
        command.setData(values, transferData.getAesKeyArray())
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 开启或者关闭远程开锁功能
     * @param command
     * @param opType 1 - 查询 2 - 修改
     * @param opValue     0 - 解冻 1 - 冻结
     */
    fun controlFreezeLock(command: Command, opType: Byte, opValue: Byte, aesKeyArray: ByteArray?) {
        command.setCommand(Command.Companion.COMM_FREEZE_LOCK)
        LogUtil.d("opValue:$opValue")
        var values: ByteArray? = null
        when (opType) {
            OperationType.GET_STATE -> values = byteArrayOf(opType)
            OperationType.MODIFY -> values = byteArrayOf(opType, opValue)
        }
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 开启或者关闭照明灯功能
     * @param command
     * @param opType 1 - 查询 2 - 修改
     * @param opValue     0 - 解冻 1 - 冻结
     */
    fun controlLamp(command: Command, opType: Byte, opValue: Short, aesKeyArray: ByteArray?) {
        command.setCommand(Command.Companion.COMM_LAMP)
        LogUtil.d("opValue:$opValue")
        var values: ByteArray? = null
        when (opType) {
            OperationType.GET_STATE -> values = byteArrayOf(opType)
            OperationType.MODIFY -> {
                values = ByteArray(3)
                values[0] = opType
                values[1] = (opValue shr 8).toByte()
                values[2] = opValue.toByte()
            }
        }
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 重置 防撬警报 隐私锁
     * @param command
     * @param switchItem
     * @param switchValue
     * @param aeskey
     */
    fun setSwitchState(command: Command, switchItem: Int, switchValue: Int, aeskey: ByteArray?) {
        command.setCommand(Command.Companion.COMM_SWITCH)
        val values = ByteArray(1 + 4 + 4)
        values[0] = OperationType.MODIFY
        System.arraycopy(DigitUtil.integerToByteArray(switchItem), 0, values, 1, 4)
        System.arraycopy(DigitUtil.integerToByteArray(switchValue), 0, values, 5, 4)
        command.setData(values, aeskey)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 重置 防撬警报 隐私锁
     * @param command
     * @param aeskey
     */
    fun getSwitchState(command: Command, aeskey: ByteArray?) {
        command.setCommand(Command.Companion.COMM_SWITCH)
        val values = byteArrayOf(OperationType.GET_STATE)
        command.setData(values, aeskey)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun deadLock(
        command: Command,
        unlockKey: String?,
        psFromLock: ByteArray?,
        aesKeyArray: ByteArray?,
        unlockDate: Long
    ) {
        command.setCommand(Command.Companion.COMM_DEAD_LOCK)
        var psFromLockL: Long = 0
        var unlockKeyL: Long = 0
        val sum: String? = null
        psFromLockL = DigitUtil.fourBytesToLong(psFromLock)
        unlockKeyL = java.lang.Long.valueOf(unlockKey)
        val sumByteArray: ByteArray = DigitUtil.getUnlockPwdBytes_new(psFromLockL, unlockKeyL)
        val values = ByteArray(8)
        System.arraycopy(sumByteArray, 0, values, 0, sumByteArray.size)
        //时间唯一标识
        val date = (unlockDate / 1000).toInt()
        System.arraycopy(DigitUtil.integerToByteArray(date), 0, values, 4, 4)
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun addIcCyclicDate(
        command: Command,
        user: Long,
        loopType: Byte,
        cyclicConfig: CyclicConfig,
        aesKeyArray: ByteArray?
    ) {
        command.setCommand(Command.Companion.COMM_CYCLIC_CMD)
        //        ValidityInfo loopDate = transferData.getValidityInfo();
        var userLen: Byte = 4
        userLen = if (user > 0 && user <= 0xffffffffL) 4 else 8
        LogUtil.d("userLen:$userLen")
        val userBytes: ByteArray = DigitUtil.longToByteArrayWithLen(user, userLen.toInt())
        val values = ByteArray(1 + 1 + 1 + userLen + 1 + 1 + 1 + 8) //TODO:
        var index = 0
        values[index++] = CyclicOpType.ADD
        values[index++] = CyclicOpType.USER_TYPE_IC
        values[index++] = userLen
        System.arraycopy(userBytes, 0, values, 3, userLen.toInt())
        index += userLen.toInt()
        values[index++] = loopType
        values[index++] = cyclicConfig.weekDay.toByte()
        values[index++] = 0 //月日的时候用
        values[index++] = (cyclicConfig.startTime / 60).toByte()
        values[index++] = (cyclicConfig.startTime % 60).toByte()
        values[index++] = (cyclicConfig.endTime / 60).toByte()
        values[index++] = (cyclicConfig.endTime % 60).toByte()
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun addFrCyclicDate(
        command: Command,
        user: Long,
        loopType: Byte,
        cyclicConfig: CyclicConfig,
        aesKeyArray: ByteArray?
    ) {
        command.setCommand(Command.Companion.COMM_CYCLIC_CMD)
        val userLen: Byte = 6 //指纹目前固定6个字节
        LogUtil.d("user:$user")
        LogUtil.d("userLen:$userLen")
        val userBytes: ByteArray = DigitUtil.longToByteArrayWithLen(user, userLen.toInt())
        val values = ByteArray(1 + 1 + 1 + userLen + 1 + 1 + 1 + 8)
        var index = 0
        values[index++] = CyclicOpType.ADD
        values[index++] = CyclicOpType.USER_TYPE_FR
        values[index++] = userLen
        System.arraycopy(userBytes, 0, values, 3, userLen.toInt())
        index += userLen.toInt()
        values[index++] = loopType
        values[index++] = cyclicConfig.weekDay.toByte()
        values[index++] = 0 //月日的时候用
        values[index++] = (cyclicConfig.startTime / 60).toByte()
        values[index++] = (cyclicConfig.startTime % 60).toByte()
        values[index++] = (cyclicConfig.endTime / 60).toByte()
        values[index++] = (cyclicConfig.endTime % 60).toByte()
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun addKeyFobCyclicDate(
        command: Command,
        keyFobMac: String?,
        loopType: Byte,
        cyclicConfig: CyclicConfig,
        aesKeyArray: ByteArray?
    ) {
        command.setCommand(Command.Companion.COMM_CYCLIC_CMD)
        val userLen: Byte = 6 //mac地址目前固定6个字节
        val userBytes: ByteArray = DigitUtil.getReverseMacArray(keyFobMac) //低在前高在后的顺序
        val values = ByteArray(1 + 1 + 1 + userLen + 1 + 1 + 1 + 8)
        var index = 0
        values[index++] = CyclicOpType.ADD
        values[index++] = CyclicOpType.USER_TYPE_KEY_FOB
        values[index++] = userLen
        System.arraycopy(userBytes, 0, values, 3, userLen.toInt())
        index += userLen.toInt()
        values[index++] = loopType
        values[index++] = cyclicConfig.weekDay.toByte()
        values[index++] = 0 //月日的时候用
        values[index++] = (cyclicConfig.startTime / 60).toByte()
        values[index++] = (cyclicConfig.startTime % 60).toByte()
        values[index++] = (cyclicConfig.endTime / 60).toByte()
        values[index++] = (cyclicConfig.endTime % 60).toByte()
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 用户ID长度为0时，也就是没有提供用户ID时，清空所有相关用户
     * @param command
     * @param user
     * @param loopType
     * @param aesKeyArray
     */
    fun clearICCyclicPeriod(command: Command, user: Long, loopType: Byte, aesKeyArray: ByteArray?) {
        command.setCommand(Command.Companion.COMM_CYCLIC_CMD)
        var userLen: Byte = 4
        userLen = if (user > 0 && user <= 0xffffffffL) 4 else 8
        val userBytes: ByteArray = DigitUtil.longToByteArrayWithLen(user, userLen.toInt())
        val values = ByteArray(1 + 1 + 1 + userLen)
        var index = 0
        values[index++] = CyclicOpType.CLEAR
        values[index++] = CyclicOpType.USER_TYPE_IC
        values[index++] = userLen
        System.arraycopy(userBytes, 0, values, 3, userLen.toInt())
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun clearFrCyclicPeriod(command: Command, user: Long, loopType: Byte, aesKeyArray: ByteArray?) {
        command.setCommand(Command.Companion.COMM_CYCLIC_CMD)
        val userLen: Byte = 6
        val userBytes: ByteArray = DigitUtil.longToByteArrayWithLen(user, userLen.toInt())
        val values = ByteArray(1 + 1 + 1 + userLen)
        var index = 0
        values[index++] = CyclicOpType.CLEAR
        values[index++] = CyclicOpType.USER_TYPE_FR
        values[index++] = userLen
        System.arraycopy(userBytes, 0, values, 3, userLen.toInt())
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun clearKeyfobCyclicPeriod(command: Command, keyfobMac: String?, aesKeyArray: ByteArray?) {
        command.setCommand(Command.Companion.COMM_CYCLIC_CMD)
        val keyFobBytes: ByteArray = DigitUtil.getReverseMacArray(keyfobMac)
        val userLen = keyFobBytes.size.toByte()
        val values = ByteArray(1 + 1 + 1 + userLen)
        var index = 0
        values[index++] = CyclicOpType.CLEAR
        values[index++] = CyclicOpType.USER_TYPE_KEY_FOB
        values[index++] = userLen
        System.arraycopy(keyFobBytes, 0, values, 3, userLen.toInt())
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun getNBActivateConfig(command: Command, opType: Byte, aesKeyArray: ByteArray?) {
        command.setCommand(Command.Companion.COMM_NB_ACTIVATE_CONFIGURATION)
        val values = byteArrayOf(ActionType.GET, opType)
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * nb 激活配置
     * @param command
     * @param opType
     * @param nbAwakeConfig
     * @param aesKeyArray
     */
    fun setNBActivateConfig(
        command: Command,
        opType: Byte,
        nbAwakeConfig: NBAwakeConfig,
        aesKeyArray: ByteArray?
    ) {
        command.setCommand(Command.Companion.COMM_NB_ACTIVATE_CONFIGURATION)
        var values: ByteArray? = null
        when (opType) {
            NBAwakeConfig.Companion.ACTION_AWAKE_MODE -> {
                values = ByteArray(3)
                values[0] = ActionType.SET
                values[1] = opType
                var activateMode: Byte = 0
                if (nbAwakeConfig.getNbAwakeModeList() != null) {
                    for (nbAwakeMode in nbAwakeConfig.getNbAwakeModeList()) {
                        activateMode = activateMode or nbAwakeMode.getValue()
                    }
                }
                values[2] = activateMode
            }
            NBAwakeConfig.Companion.ACTION_AWAKE_TIME -> {
                val configLen: Int = nbAwakeConfig.getNbAwakeTimeList().size
                values = ByteArray(1 + 1 + 1 + 3 * configLen)
                values[0] = ActionType.SET
                values[1] = opType
                values[2] = configLen.toByte()
                var index = 3
                for (nbAwakeTime in nbAwakeConfig.getNbAwakeTimeList()) {
                    values[index++] = nbAwakeTime.getNbAwakeTimeType().getValue()
                    values[index++] = (nbAwakeTime.getMinutes() / 60).toByte()
                    values[index++] = (nbAwakeTime.getMinutes() % 60).toByte()
                }
            }
        }
        command.setData(values, aesKeyArray)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun addKeyFob(command: Command, transferData: TransferData?, aeskey: ByteArray?) {
        command.setCommand(Command.Companion.COMM_KEY_FOB_MANAG)
        keyFobManage(command, KeyFobOperationType.ADD_MODIFY, transferData, aeskey)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun modifyKeyFob(command: Command, transferData: TransferData?, aeskey: ByteArray?) {
        command.setCommand(Command.Companion.COMM_KEY_FOB_MANAG)
        keyFobManage(command, KeyFobOperationType.ADD_MODIFY, transferData, aeskey)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun deleteKeyFob(command: Command, transferData: TransferData?, aeskey: ByteArray?) {
        command.setCommand(Command.Companion.COMM_KEY_FOB_MANAG)
        keyFobManage(command, KeyFobOperationType.DELETE, transferData, aeskey)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun clearKeyFob(command: Command, aeskey: ByteArray?) {
        command.setCommand(Command.Companion.COMM_KEY_FOB_MANAG)
        keyFobManage(command, KeyFobOperationType.CLEAR, null, aeskey)
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun keyFobManage(
        command: Command,
        opType: Byte,
        transferData: TransferData,
        aeskey: ByteArray?
    ) {
        var values: ByteArray? = null
        when (opType) {
            KeyFobOperationType.GET -> {}
            KeyFobOperationType.ADD_MODIFY -> {
                values = ByteArray(1 + 6 + 5 + 5)
                values[0] = opType // 操作类型（1 byte）
                val keyFobBytes: ByteArray =
                    DigitUtil.getReverseMacArray(transferData.getKeyFobMac()) // 无线钥匙MAC（6 bytes）低在前，高在后
                System.arraycopy(keyFobBytes, 0, values, 1, 6)
                val validityInfo: ValidityInfo = transferData.getValidityInfo()
                var startDate: Long = validityInfo.getStartDate()
                var endDate: Long = validityInfo.getEndDate()
                if (startDate == 0L || endDate == 0L) { //永久时间
                    startDate = CommandUtil.permanentStartDate
                    endDate = CommandUtil.permanentEndDate
                }
                val startDateBytes: ByteArray = DigitUtil.convertTimestamp2LockZoneBytes_yyMMddHHmm(
                    startDate,
                    transferData.getTimezoneOffSet()
                )
                System.arraycopy(startDateBytes, 0, values, 7, 5) // 起始时间（5 bytes）
                val endDateBytes: ByteArray = DigitUtil.convertTimestamp2LockZoneBytes_yyMMddHHmm(
                    endDate,
                    transferData.getTimezoneOffSet()
                )
                System.arraycopy(endDateBytes, 0, values, 12, 5) // 结束时间（5 bytes）
            }
            KeyFobOperationType.DELETE -> {
                values = ByteArray(1 + 6)
                values[0] = opType // 操作类型（1 byte）
                keyFobBytes =
                    DigitUtil.getReverseMacArray(transferData.getKeyFobMac()) // 无线钥匙MAC（6 bytes）低在前，高在后
                System.arraycopy(keyFobBytes, 0, values, 1, 6)
            }
            KeyFobOperationType.CLEAR -> {
                values = ByteArray(1)
                values[0] = opType // 操作类型（1 byte）
            }
        }
        command.setData(values, aeskey)
    }

    fun setSensitivity(command: Command, transferData: TransferData) {
        command.setCommand(Command.Companion.COMM_SENSITIVITY_MANAGE)
        val values =
            byteArrayOf(SensitivityOperationType.MODIFY, transferData.getSensitivity().toByte())
        command.setData(values, transferData.getAesKeyArray())
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun getSensitivity(
        command: Command,
        aesKey: ByteArray?
    ) { //初始化过程中使用TransferData.getAesKeyArray() 拿到的是默认的不是初始化设置的key
        command.setCommand(Command.Companion.COMM_SENSITIVITY_MANAGE)
        val values = byteArrayOf(SensitivityOperationType.QUERY)
        command.setData(values, aesKey)
        LogUtil.d("values:" + DigitUtil.byteArrayToHexString(values))
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    fun addDoorSensor(command: Command, transferData: TransferData) {
        command.setCommand(Command.Companion.COMM_DOOR_SENSOR_MANAGE)
        val values: ByteArray =
            DigitUtil.getReverseMacArray(transferData.getDoorSensorMac()) // 无线门磁MAC（6 bytes）低在前，高在后
        command.setData(values, transferData.getAesKeyArray())
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }

    /**
     * 删除门磁
     * 地址为全0时，表示删除无线门磁信息
     * @param command
     * @param transferData
     */
    fun deleteDoorSensor(command: Command, transferData: TransferData) {
        command.setCommand(Command.Companion.COMM_DOOR_SENSOR_MANAGE)
        val values = byteArrayOf(0, 0, 0, 0, 0, 0) // 无线门磁MAC（6 bytes）低在前，高在后
        command.setData(values, transferData.getAesKeyArray())
        BluetoothImpl.Companion.getInstance().sendCommand(command.buildCommand())
    }
}