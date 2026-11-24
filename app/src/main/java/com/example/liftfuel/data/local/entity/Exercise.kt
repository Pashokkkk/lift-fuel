package com.example.liftfuel.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercises",
    indices = [Index(value = ["name"], unique = true), Index("primaryMuscle")],
)
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val primaryMuscle: MuscleGroup,
    /** Stored as comma-separated text, see Converters. */
    val secondaryMuscles: List<MuscleGroup> = emptyList(),
    val equipment: Equipment,
    val isCompound: Boolean,
    /**
     * Smallest sensible weight jump, used by the "add weight" tip
     * (e.g. 2.5 kg bench press, 5 kg squat). 0 for bodyweight exercises.
     */
    val weightIncrementKg: Double,
    /** One-line technique cue for beginners. */
    val instructions: String? = null,
    val isCustom: Boolean = false,
)

val Exercise.isBodyweight: Boolean
    get() = equipment == Equipment.BODYWEIGHT
