package com.example.liftfuel.data.local.model

import androidx.room.Embedded
import com.example.liftfuel.data.local.entity.WorkoutSession
import java.time.LocalDate

// Read-only shapes returned by aggregate queries. Column aliases in the DAO queries
// must match these property names.

data class Nutrition(
    val kcal: Double,
    val proteinG: Double,
    val carbsG: Double,
    val fatG: Double,
) {
    operator fun plus(other: Nutrition) = Nutrition(
        kcal = kcal + other.kcal,
        proteinG = proteinG + other.proteinG,
        carbsG = carbsG + other.carbsG,
        fatG = fatG + other.fatG,
    )

    companion object {
        val ZERO = Nutrition(0.0, 0.0, 0.0, 0.0)
    }
}

/** Totals for one diary day. */
data class DailyNutrition(
    val date: LocalDate,
    val kcal: Double,
    val proteinG: Double,
    val carbsG: Double,
    val fatG: Double,
    val entryCount: Int,
)

/** A row of the workout history list (warm-up sets excluded from the numbers). */
data class WorkoutSessionSummary(
    @Embedded val session: WorkoutSession,
    val setCount: Int,
    val exerciseCount: Int,
    val totalVolumeKg: Double,
)

/** One point on an exercise's strength chart (one per finished session). */
data class ExerciseProgressPoint(
    val sessionId: Long,
    val date: LocalDate,
    val topWeightKg: Double,
    val bestEstimated1RmKg: Double,
    val totalVolumeKg: Double,
    val totalReps: Int,
)

data class PersonalRecord(
    val exerciseId: Long,
    val exerciseName: String,
    val maxWeightKg: Double,
    val bestEstimated1RmKg: Double,
    val sessionCount: Int,
)

/** Row counts per table, used by the temporary database check screen. */
data class TableCounts(
    val foods: Int,
    val exercises: Int,
    val templates: Int,
    val weightEntries: Int,
    val mealEntries: Int,
    val favoriteMeals: Int,
    val photoAnalyses: Int,
    val workoutSessions: Int,
    val workoutSets: Int,
    val recommendations: Int,
    val reminders: Int,
)
