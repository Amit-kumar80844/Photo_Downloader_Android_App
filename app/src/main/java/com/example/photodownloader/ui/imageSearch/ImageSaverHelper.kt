package com.example.photodownloader.ui.imageSearch

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore

fun saveImageToGallery(context: Context, data: ByteArray): Boolean {
    return try {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/PhotoDownloader")
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        ) ?: return false

        resolver.openOutputStream(uri)?.use { it.write(data) }

        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
