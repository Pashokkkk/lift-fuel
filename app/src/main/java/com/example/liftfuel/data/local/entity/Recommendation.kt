package com.example.liftfuel.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

/**
 * A rule-based tip shown on the Today screen. Every tip carries a plain-language [reason]
 * so the user always sees why it appears.
 */
@Entity(
    tableName = "recommendations",
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["relatedExerciseId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [
        Index(value = ["dedupeKey"], unique = true),
        Index("status"),
        Index("relatedExerciseId"),
    ],
)
data class Recommendation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: RecommendationType,
    val title: String,
    val message: String,
    val reason: String,
    /** E.g. -150 (kcal) for a calorie tip or 62.5 (kg) for an "add weight" tip. */
    val suggestedValue: Double? = null,
    val relatedExerciseId: Long? = null,
    /**
     * Stops the rule engine from creating the same tip twice,
     * e.g. "INCREASE_WEIGHT:3:2026-11-16". Inserts use IGNORE on conflict.
     */
    val dedupeKey: String,
    val status: RecommendationStatus = RecommendationStatus.ACTIVE,
    val createdAt: Instant,
    val resolvedAt: Instant? = null,
)
