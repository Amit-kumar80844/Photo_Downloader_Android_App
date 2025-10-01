package com.example.photodownloader.domain.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.photodownloader.data.local.DownloadedImage
import com.example.photodownloader.data.local.PreviousSearch

@Database(entities = [DownloadedImage::class, PreviousSearch::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDownloaderDao(): PhotoDownloaderDao
}