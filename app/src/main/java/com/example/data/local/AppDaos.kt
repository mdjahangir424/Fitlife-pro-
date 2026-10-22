package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout_records ORDER BY completedAt DESC")
    fun getAllRecords(): Flow<List<WorkoutRecordEntity>>

    @Query("SELECT * FROM workout_records ORDER BY completedAt DESC LIMIT :limit")
    fun getRecentRecords(limit: Int): Flow<List<WorkoutRecordEntity>>

    @Query("SELECT COUNT(*) FROM workout_records")
    fun getTotalWorkoutsCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(durationSeconds), 0) FROM workout_records")
    fun getTotalDurationSeconds(): Flow<Long>

    @Query("SELECT * FROM workout_records WHERE id = :id")
    suspend fun getRecordById(id: Long): WorkoutRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: WorkoutRecordEntity): Long

    @Query("DELETE FROM workout_records WHERE id = :id")
    suspend fun deleteRecordById(id: Long)

    @Query("DELETE FROM workout_records")
    suspend fun clearAllRecords()
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_exercises ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteExerciseEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_exercises WHERE exerciseId = :exerciseId)")
    fun isFavorite(exerciseId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(fav: FavoriteExerciseEntity)

    @Query("DELETE FROM favorite_exercises WHERE exerciseId = :exerciseId")
    suspend fun removeFavorite(exerciseId: String)

    @Query("DELETE FROM favorite_exercises")
    suspend fun clearFavorites()
}

@Dao
interface DailyPlanDao {
    @Query("SELECT * FROM daily_plan_status ORDER BY dateString DESC")
    fun getAllDailyStatus(): Flow<List<DailyPlanStatusEntity>>

    @Query("SELECT * FROM daily_plan_status WHERE dateString = :dateString LIMIT 1")
    suspend fun getStatusForDate(dateString: String): DailyPlanStatusEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(status: DailyPlanStatusEntity)

    @Query("DELETE FROM daily_plan_status")
    suspend fun clearDailyPlans()
}

@Dao
interface DownloadedVideoDao {
    @Query("SELECT * FROM downloaded_videos ORDER BY downloadedAt DESC")
    fun getAllDownloadedVideos(): Flow<List<DownloadedVideoEntity>>

    @Query("SELECT * FROM downloaded_videos WHERE exerciseId = :exerciseId LIMIT 1")
    suspend fun getDownloadedVideo(exerciseId: String): DownloadedVideoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(video: DownloadedVideoEntity)

    @Query("DELETE FROM downloaded_videos WHERE exerciseId = :exerciseId")
    suspend fun deleteDownload(exerciseId: String)

    @Query("DELETE FROM downloaded_videos")
    suspend fun clearDownloads()
}
