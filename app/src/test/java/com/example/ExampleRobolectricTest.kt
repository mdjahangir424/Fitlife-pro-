package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.config.AppConfig
import com.example.data.local.FavoriteExerciseEntity
import com.example.data.local.FitLifeDatabase
import com.example.data.local.WorkoutRecordEntity
import com.example.data.repository.ExerciseCatalogRepository
import com.example.data.repository.WorkoutHistoryRepository
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var database: FitLifeDatabase
    private lateinit var repository: WorkoutHistoryRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, FitLifeDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = WorkoutHistoryRepository(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testAppNameAndOwnerAttribution() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("FitLife Pro", appName)
        assertEquals("FitLife Pro", AppConfig.APP_NAME)
        assertEquals("Md Jahangir Alam", AppConfig.APP_OWNER)
        assertEquals("mdjahangiralam2528@gmail.com", AppConfig.OWNER_EMAIL)
        assertEquals("+8801778600028", AppConfig.OWNER_PHONE)
    }

    @Test
    fun testRoomDatabaseWorkoutPersistenceAndStreak() = runBlocking {
        // Initially empty
        val initialRecords = repository.allWorkoutRecords.first()
        assertTrue(initialRecords.isEmpty())

        // Save a completed workout
        val id = repository.saveCompletedWorkout(
            programTitle = "7-Day Kickstart",
            categoryId = "cat_full_body",
            durationSeconds = 720,
            exercisesCompleted = 5,
            totalExercises = 5,
            caloriesEst = 78,
            notes = "Great morning energy"
        )
        assertTrue(id > 0)

        // Verify retrieval
        val records = repository.allWorkoutRecords.first()
        assertEquals(1, records.size)
        assertEquals("7-Day Kickstart", records[0].programTitle)
        assertEquals(720, records[0].durationSeconds)
        assertEquals(5, records[0].exercisesCompleted)

        // Verify streak calculation
        val streak = repository.calculateStreak(records)
        assertEquals(1, streak)
    }

    @Test
    fun testRoomFavoriteExerciseOperations() = runBlocking {
        val exerciseId = "ex_jumping_jacks"
        assertFalse(repository.favoriteExerciseIds.first().contains(exerciseId))

        // Add favorite
        repository.toggleFavorite(exerciseId, isCurrentlyFav = false)
        assertTrue(repository.favoriteExerciseIds.first().contains(exerciseId))

        // Remove favorite
        repository.toggleFavorite(exerciseId, isCurrentlyFav = true)
        assertFalse(repository.favoriteExerciseIds.first().contains(exerciseId))
    }

    @Test
    fun testExerciseCatalogCategoriesAndTeenSafety() {
        // Must contain all 14 categories including Kegel & Pelvic Floor
        assertEquals(14, ExerciseCatalogRepository.categories.size)

        // Must have exercises
        assertTrue(ExerciseCatalogRepository.exercises.isNotEmpty())

        // Must have Kegel program and category
        assertNotNull(ExerciseCatalogRepository.getCategoryById("cat_kegel"))
        val kegelProgram = ExerciseCatalogRepository.getProgramById("prog_kegel_step_by_step_7d")
        assertNotNull(kegelProgram)
        assertEquals(6, kegelProgram!!.exerciseIds.size)

        // Must have teen-safe movement program
        val teenProgram = ExerciseCatalogRepository.programs.firstOrNull { it.isTeenSafe }
        assertNotNull(teenProgram)
        assertTrue(teenProgram!!.isTeenSafe)
    }

    @Test
    fun testBilingualLocalization() {
        val bnStrings = AppStrings.get(AppLanguage.BENGALI)
        val enStrings = AppStrings.get(AppLanguage.ENGLISH)

        assertEquals("ফিটলাইফ প্রো", bnStrings.appName)
        assertEquals("FitLife Pro", enStrings.appName)
        assertEquals("ওয়ার্কআউট শুরু করুন", bnStrings.startWorkout)
        assertEquals("Start Workout", enStrings.startWorkout)
        assertEquals("আজকের রুটিন", bnStrings.todayPlan)
        assertEquals("Today's Schedule", enStrings.todayPlan)
    }
}
