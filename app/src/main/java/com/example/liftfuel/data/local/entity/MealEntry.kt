package com.example.liftfuel.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.liftfuel.data.local.model.Nutrition
import java.time.Instant
import java.time.LocalDate

/**
 * One food logged in the diary.
 *
 * Name and nutrition are copied from the food at save time, so editing or deleting a food
 * later never changes past days. [foodId] is only a link back (NULL for photo items with no
 * match in the food table, or after the food was deleted).
 */
@Entity(
    tableName = "meal_entries",
    foreignKeys = [
        ForeignKey(
            entity = Food::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.SET_NULL,
        ),
        ForeignKey(
            entity = PhotoAnalysis::class,
            parentColumns = ["id"],
            childColumns = ["photoAnalysisId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
    indices = [Index("date"), Index("foodId"), Index("photoAnalysisId")],
)
data class MealEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    val mealType: MealType,
    val foodId: Long?,
    val foodName: String,
    val grams: Double,
    val kcal: Double,
    val proteinG: Double,
    val carbsG: Double,
    val fatG: Double,
    val source: EntrySource,
    /** Set when the entry came from a meal photo (source = PHOTO). */
    val photoAnalysisId: Long? = null,
    /** True for photo-based entries: always shown with an "estimate" label. */
    val isEstimate: Boolean = false,
    val confidence: Confidence? = null,
    val loggedAt: Instant,
)

val MealEntry.nutrition: Nutrition
    get() = Nutrition(kcal = kcal, proteinG = proteinG, carbsG = carbsG, fatG = fatG)
