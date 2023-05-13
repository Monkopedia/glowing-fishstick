package com.ttlock.bl.sdk.entity

import com.ttlock.bl.sdk.constant.LockType
import com.ttlock.bl.sdk.util.GsonUtil

/**
 * Created by Smartlock on 2016/5/27.
 */
class LockVersion(
    private var protocolType: Byte,
    private var protocolVersion: Byte,
    private var scene: Byte,
    private var groupId: Short,
    private var orgId: Short
) {
    fun getProtocolType(): Byte {
        return protocolType
    }

    fun setProtocolType(protocolType: Byte) {
        this.protocolType = protocolType
    }

    fun getProtocolVersion(): Byte {
        return protocolVersion
    }

    fun setProtocolVersion(protocolVersion: Byte) {
        this.protocolVersion = protocolVersion
    }

    fun getScene(): Byte {
        return scene
    }

    fun setScene(scene: Byte) {
        this.scene = scene
    }

    fun getGroupId(): Short {
        return groupId
    }

    fun setGroupId(groupId: Short) {
        this.groupId = groupId
    }

    fun getOrgId(): Short {
        return orgId
    }

    fun setOrgId(orgId: Short) {
        this.orgId = orgId
    }

    override fun toString(): String {
        return "$protocolType,$protocolVersion,$scene,$groupId,$orgId"
    }

    fun toGson(): String {
        return GsonUtil.toJson<LockVersion>(this)
    }

    companion object {
        var lockVersion_V2S_PLUS = LockVersion(
            5.toByte(), 4.toByte(), 1.toByte(),
            1.toByte()
                .toShort(),
            1.toByte().toShort()
        )
        var lockVersion_V3 = LockVersion(
            5.toByte(), 3.toByte(), 1.toByte(),
            1.toByte()
                .toShort(),
            1.toByte().toShort()
        )
        var lockVersion_V2S = LockVersion(
            5.toByte(), 1.toByte(), 1.toByte(),
            1.toByte()
                .toShort(),
            1.toByte().toShort()
        )

        /**
         * 二代车位锁场景也改为7
         */
        var lockVersion_Va = LockVersion(
            0x0a.toByte(), 1.toByte(), 0x07.toByte(),
            1.toByte()
                .toShort(),
            1.toByte().toShort()
        )

        /**
         * 电动车锁场景先改为1  目前没有电动车锁
         */
        var lockVersion_Vb = LockVersion(
            0x0b.toByte(), 1.toByte(), 0x01.toByte(),
            1.toByte()
                .toShort(),
            1.toByte().toShort()
        )
        var lockVersion_V2 = LockVersion(
            3.toByte(), 0.toByte(), 0.toByte(),
            0.toByte()
                .toShort(),
            0.toByte().toShort()
        )
        var lockVersion_V3_car = LockVersion(
            5.toByte(), 3.toByte(), 7.toByte(),
            1.toByte()
                .toShort(),
            1.toByte().toShort()
        )

        fun getLockVersion(lockType: Int): LockVersion? {
            return when (lockType) {
                LockType.LOCK_TYPE_V3_CAR -> LockVersion.Companion.lockVersion_V3_car
                LockType.LOCK_TYPE_V3 -> LockVersion.Companion.lockVersion_V3
                LockType.LOCK_TYPE_V2S_PLUS -> LockVersion.Companion.lockVersion_V2S_PLUS
                LockType.LOCK_TYPE_V2S -> LockVersion.Companion.lockVersion_V2S
                LockType.LOCK_TYPE_CAR -> LockVersion.Companion.lockVersion_Va
                LockType.LOCK_TYPE_MOBI -> LockVersion.Companion.lockVersion_Vb
                LockType.LOCK_TYPE_V2 -> LockVersion.Companion.lockVersion_V2
                else -> null
            }
        }
    }
}
