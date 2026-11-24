package com.example.liftfuel.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate
import java.time.Period

/**
 * The single user of the app (the table always has at most one row, id = 1).
 * Current body weight is not stored here: it is the latest [WeightEntry].
 */
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = SINGLE_ROW_ID,
    val name: String,
    val sex: Sex,
    val birthDate: LocalDate,
    val heightCm: Double,
    val activityLevel: ActivityLevel,
    val goal: Goal,
    val targetWeightKg: Double?,
    /** Mifflin-St Jeor TDEE plus the goal offset, before any accepted calorie tips. */
    val baseCalorieTarget: Int,
    /** Sum of calorie-adjustment tips the user accepted, e.g. -150. */
    val calorieAdjustment: Int = 0,
    val proteinTargetG: Int,
    val carbsTargetG: Int,
    val fatTargetG: Int,
    val unitSystem: UnitSystem = UnitSystem.METRIC,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        const val SINGLE_ROW_ID = 1
    }
}

/** The calorie target shown on the Today screen. */
val UserProfile.dailyCalorieTarget: Int
    get() = baseCalorieTarget + calorieAdjustment

fun UserProfile.ageOn(date: LocalDate): Int = Period.between(birthDate, date).years
