package com.example.photodownloader.data.local

object DatabaseBuilder {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: android.content.Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = androidx.room.Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}