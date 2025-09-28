package com.example.photodownloader.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DownloadedImage::class, PreviousSearch::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDownloaderDao(): PhotoDownloaderDao
}