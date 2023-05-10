package com.ttlock.bl.sdk.provider

import android.content.ContentProvider

/**
 * Created on  2019/5/30 0030 14:36
 * this provider just use for doing sdk init
 * @author theodore_hu
 */
class InitProvider : ContentProvider() {
    fun onCreate(): Boolean {
        TTLockClient.Companion.getDefault().prepareBTService(getContext())
        GatewayClient.Companion.getDefault().prepareBTService(getContext())
        WirelessKeypadClient.Companion.getDefault().prepareBTService(getContext())
        return true
    }

    @NonNull
    fun query(
        @NonNull uri: Uri?,
        @Nullable projection: Array<String?>?,
        @Nullable selection: String?,
        @Nullable selectionArgs: Array<String?>?,
        @Nullable sortOrder: String?
    ): Cursor? {
        return null
    }

    @Nullable
    fun getType(@NonNull uri: Uri?): String? {
        return null
    }

    @Nullable
    fun insert(@NonNull uri: Uri?, @Nullable values: ContentValues?): Uri? {
        return null
    }

    fun delete(
        @NonNull uri: Uri?,
        @Nullable selection: String?,
        @Nullable selectionArgs: Array<String?>?
    ): Int {
        return 0
    }

    fun update(
        @NonNull uri: Uri?,
        @Nullable values: ContentValues?,
        @Nullable selection: String?,
        @Nullable selectionArgs: Array<String?>?
    ): Int {
        return 0
    }
}