package com.ttlock.bl.sdk.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.api.WirelessKeypadClient;
import com.ttlock.bl.sdk.gateway.api.GatewayClient;

/**
 * Created on  2019/5/30 0030 14:36
 * this provider just use for doing sdk init
 * @author theodore_hu
 */
public class InitProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        TTLockClient.getDefault().prepareBTService(getContext());
        GatewayClient.getDefault().prepareBTService(getContext());
        WirelessKeypadClient.getDefault().prepareBTService(getContext());
        return true;
    }

    @NonNull
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
