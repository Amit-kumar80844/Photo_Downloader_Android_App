package com.example.photodownloader.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDownloaderDao {
    // ---------------- Downloaded Images ----------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownloadedImage(image: DownloadedImage)

    @Query("SELECT * FROM downloaded_images ORDER BY downloadedAt DESC")
    fun getAllDownloadedImages(): Flow<List<DownloadedImage>>

    @Query("DELETE FROM downloaded_images WHERE id = :id")
    suspend fun deleteDownloadedImageById(id: Int)

    @Query("DELETE FROM downloaded_images")
    suspend fun clearAllDownloadedImages()


    // ---------------- Previous Searches ----------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreviousSearch(search: PreviousSearch)

    @Query("SELECT * FROM previous_search ORDER BY id DESC")
    fun getAllPreviousSearches(): Flow<List<PreviousSearch>>

    @Query("DELETE FROM previous_search WHERE id = :id")
    suspend fun deletePreviousSearchById(id: Int)

    @Query("DELETE FROM previous_search")
    suspend fun clearAllPreviousSearches()
}
