package com.example.liftfuel.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate

/** One gym visit. [finishedAt] is NULL while the workout is in progress. */
@Entity(
    tableName = "workout_sessions",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutTemplate::class,
            parentColumns = ["id"],
            childColumns = ["templateId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index("templateId"), Index("date")],
)
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val templateId: Long?,
    /** Copied from the template so history keeps its name if the template is renamed. */
    val name: String,
    val date: LocalDate,
    val startedAt: Instant,
    val finishedAt: Instant? = null,
    val notes: String? = null,
)

/**
 * A logged set (weight × reps). Exercises that already have sets cannot be deleted
 * (RESTRICT), so training history is never lost by accident.
 */
@Entity(
    tableName = "workout_sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSession::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.RESTRICT,
        ),
    ],
    indices = [Index("sessionId"), Index("exerciseId")],
)
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: Long,
    val exerciseId: Long,
    /** 1-based position of the set within this exercise in this session. */
    val setNumber: Int,
    val weightKg: Double,
    val reps: Int,
    /** Top of the planned rep range when the set was logged; drives the "add weight" tip. */
    val targetReps: Int? = null,
    val isWarmup: Boolean = false,
    val completedAt: Instant,
)

/** Epley estimate of the one-rep max, used for strength charts and personal records. */
val WorkoutSet.estimatedOneRepMaxKg: Double
    get() = if (reps <= 1) weightKg else weightKg * (1 + reps / 30.0)
