package com.example.photodownloader.ui.downloads

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DownloadViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _downloadedImages = MutableStateFlow<List<String>>(emptyList())
    val downloadedImages = _downloadedImages

    init {
        loadImages()
    }

    private fun loadImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val imgs = getDownloadedImages(context)
            _downloadedImages.value = imgs
        }
    }
}


fun getDownloadedImages(context: Context): List<String> {
    val images = mutableListOf<String>()

    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME
    )

    val selection = "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?"
    val selectionArgs = arrayOf("%Pictures/PhotoDownloader/%")

    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        "${MediaStore.Images.Media.DATE_ADDED} DESC"
    )

    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

        while (it.moveToNext()) {
            val id = it.getLong(idColumn)
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon()
                .appendPath(id.toString())
                .build()
            images.add(uri.toString())
        }
    }

    return images
}
