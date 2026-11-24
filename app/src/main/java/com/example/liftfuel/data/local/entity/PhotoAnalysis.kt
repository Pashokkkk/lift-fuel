package com.example.liftfuel.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate

/**
 * A meal photo and the AI's draft result. Doubles as the offline queue: a photo taken
 * without a connection stays PENDING until WorkManager sends it.
 */
@Entity(
    tableName = "photo_analyses",
    indices = [Index("status"), Index("date")],
)
data class PhotoAnalysis(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    /** content:// or file:// URI of the (resized) photo in app storage. */
    val photoUri: String,
    val date: LocalDate,
    val mealType: MealType,
    val status: PhotoStatus,
    val attempts: Int = 0,
    val overallConfidence: Confidence? = null,
    /** Caveats from the model, e.g. "cooking oil is not visible". */
    val notes: String? = null,
    val errorMessage: String? = null,
    val modelName: String? = null,
    val createdAt: Instant,
    val analyzedAt: Instant? = null,
)

/** One food the AI found on the plate; editable on the Review meal screen. */
@Entity(
    tableName = "photo_analysis_items",
    foreignKeys = [
        ForeignKey(
            entity = PhotoAnalysis::class,
            parentColumns = ["id"],
            childColumns = ["analysisId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Food::class,
            parentColumns = ["id"],
            childColumns = ["matchedFoodId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index("analysisId"), Index("matchedFoodId")],
)
data class PhotoAnalysisItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val analysisId: Long,
    val detectedName: String,
    val estimatedGrams: Double,
    /** Food-table match used for calories and macros; NULL means the AI's own estimate. */
    val matchedFoodId: Long? = null,
    val nutritionSource: FoodSource,
    val kcal: Double,
    val proteinG: Double,
    val carbsG: Double,
    val fatG: Double,
    val confidence: Confidence,
    val position: Int,
)
