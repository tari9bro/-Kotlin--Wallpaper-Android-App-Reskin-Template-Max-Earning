package com.tari9bro.wallpapers.helpers

import android.content.ContentProvider
import android.content.ContentValues
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.net.Uri
import android.os.CancellationSignal
import java.io.FileNotFoundException
import java.io.IOException


class AssetsProvider : ContentProvider() {
    @Throws(FileNotFoundException::class)
    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        val am = context!!.assets
        val file_name = uri.lastPathSegment ?: throw FileNotFoundException()
        var afd: AssetFileDescriptor? = null
        try {
            afd = am.openFd(file_name)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return afd //super.openAssetFile(uri, mode);
    }

    override fun getType(p1: Uri): String? {
        // TODO: Implement this method
        return null
    }

    override fun delete(p1: Uri, p2: String?, p3: Array<String>?): Int {
        // TODO: Implement this method
        return 0
    }

    override fun query(
        p1: Uri,
        p2: Array<String>?,
        p3: String?,
        p4: Array<String>?,
        p5: String?
    ): Cursor? {
        // TODO: Implement this method
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?,
        cancellationSignal: CancellationSignal?
    ): Cursor? {
        // TODO: Implement this method
        return super.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal)
    }

    override fun insert(p1: Uri, p2: ContentValues?): Uri? {
        // TODO: Implement this method
        return null
    }

    override fun onCreate(): Boolean {
        // TODO: Implement this method
        return false
    }

    override fun update(p1: Uri, p2: ContentValues?, p3: String?, p4: Array<String>?): Int {
        // TODO: Implement this method
        return 0
    }
}




