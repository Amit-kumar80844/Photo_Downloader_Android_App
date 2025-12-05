package com.example.photodownloader.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloaded_images")
data class DownloadedImage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fileName: String,
    val uriString: String,
    val downloadedAt: Long
)

@Entity(tableName = "previous_search")
data class PreviousSearch(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val previousQuery: String
)

@Entity(tableName = "Setting_")
data class Setting(
    @PrimaryKey val id: Int = 1,
    val safeSearch:Boolean,
    val onlyEditorsChoice:Boolean,
    val minHeight:Int,
    val minWidth:Int,
    val perPage:Int,
)