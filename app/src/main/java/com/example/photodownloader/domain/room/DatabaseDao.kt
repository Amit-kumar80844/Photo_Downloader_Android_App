package com.example.photodownloader.domain.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseDao {

    @Provides
    @Singleton
    fun getInstance(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"

        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePhotoDownloderDao(database: AppDatabase): PhotoDownloaderDao {
        return database.photoDownloaderDao()
    }

}