package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        WorkoutRecordEntity::class,
        FavoriteExerciseEntity::class,
        DailyPlanStatusEntity::class,
        DownloadedVideoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FitLifeDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun dailyPlanDao(): DailyPlanDao
    abstract fun downloadedVideoDao(): DownloadedVideoDao

    companion object {
        @Volatile
        private var INSTANCE: FitLifeDatabase? = null

        fun getInstance(context: Context): FitLifeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitLifeDatabase::class.java,
                    "fitlife_pro.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
