package com.example.liftfuel.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.liftfuel.data.local.dao.ExerciseDao
import com.example.liftfuel.data.local.dao.FavoriteMealDao
import com.example.liftfuel.data.local.dao.FoodDao
import com.example.liftfuel.data.local.dao.MealEntryDao
import com.example.liftfuel.data.local.dao.PhotoAnalysisDao
import com.example.liftfuel.data.local.dao.RecommendationDao
import com.example.liftfuel.data.local.dao.ReminderDao
import com.example.liftfuel.data.local.dao.StatsDao
import com.example.liftfuel.data.local.dao.UserProfileDao
import com.example.liftfuel.data.local.dao.WeightDao
import com.example.liftfuel.data.local.dao.WorkoutDao
import com.example.liftfuel.data.local.dao.WorkoutTemplateDao
import com.example.liftfuel.data.local.entity.Exercise
import com.example.liftfuel.data.local.entity.FavoriteMeal
import com.example.liftfuel.data.local.entity.FavoriteMealItem
import com.example.liftfuel.data.local.entity.Food
import com.example.liftfuel.data.local.entity.MealEntry
import com.example.liftfuel.data.local.entity.PhotoAnalysis
import com.example.liftfuel.data.local.entity.PhotoAnalysisItem
import com.example.liftfuel.data.local.entity.Recommendation
import com.example.liftfuel.data.local.entity.Reminder
import com.example.liftfuel.data.local.entity.TemplateExercise
import com.example.liftfuel.data.local.entity.UserProfile
import com.example.liftfuel.data.local.entity.WeightEntry
import com.example.liftfuel.data.local.entity.WorkoutSession
import com.example.liftfuel.data.local.entity.WorkoutSet
import com.example.liftfuel.data.local.entity.WorkoutTemplate

@Database(
    entities = [
        UserProfile::class,
        WeightEntry::class,
        Food::class,
        MealEntry::class,
        FavoriteMeal::class,
        FavoriteMealItem::class,
        PhotoAnalysis::class,
        PhotoAnalysisItem::class,
        Exercise::class,
        WorkoutTemplate::class,
        TemplateExercise::class,
        WorkoutSession::class,
        WorkoutSet::class,
        Recommendation::class,
        Reminder::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class LiftFuelDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao
    abstract fun weightDao(): WeightDao
    abstract fun foodDao(): FoodDao
    abstract fun mealEntryDao(): MealEntryDao
    abstract fun favoriteMealDao(): FavoriteMealDao
    abstract fun photoAnalysisDao(): PhotoAnalysisDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutTemplateDao(): WorkoutTemplateDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun recommendationDao(): RecommendationDao
    abstract fun reminderDao(): ReminderDao
    abstract fun statsDao(): StatsDao

    companion object {
        const val NAME = "lift_fuel.db"

        fun build(context: Context): LiftFuelDatabase =
            Room.databaseBuilder(context.applicationContext, LiftFuelDatabase::class.java, NAME)
                // During development a schema change simply wipes and re-seeds the database.
                // Replace with real Migrations once the app has users with data to keep.
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
    }
}
