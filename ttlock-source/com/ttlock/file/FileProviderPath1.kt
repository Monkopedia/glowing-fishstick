package com.ttlock.file

import android.content.Context
import java.io.File

/**
 * Created by Administrator on 2017/11/16 0016.
 */
object FileProviderPath {
    fun getUriForFile(context: Context, file: File?): Uri? {
        var fileUri: Uri? = null
        fileUri = if (Build.VERSION.SDK_INT >= 24) {
            getUriForFile24(context, file)
        } else {
            Uri.fromFile(file)
        }
        Log.d("TAG", "uri path:" + fileUri.getPath())
        return fileUri
    }

    fun getUriForFile24(context: Context, file: File?): Uri {
        return FileProvider.getUriForFile(
            context,
            context.getPackageName() + ".android7.fileprovider",
            file
        )
    }

    fun setIntentDataAndType(
        context: Context,
        intent: Intent,
        type: String?,
        file: File?,
        writeAble: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(getUriForFile(context, file), type)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
    }

    fun setIntentData(
        context: Context,
        intent: Intent,
        file: File?,
        writeAble: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setData(getUriForFile(context, file))
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.setData(Uri.fromFile(file))
        }
    }

    fun grantPermissions(context: Context, intent: Intent, uri: Uri?, writeAble: Boolean) {
        var flag: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
        if (writeAble) {
            flag = flag or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        }
        intent.addFlags(flag)
        val resInfoList: List<ResolveInfo> = context.getPackageManager()
            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName: String = resolveInfo.activityInfo.packageName
            context.grantUriPermission(packageName, uri, flag)
        }
    }
}