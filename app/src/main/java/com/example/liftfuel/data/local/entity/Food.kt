package com.example.liftfuel.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.liftfuel.data.local.model.Nutrition
import java.time.Instant

/**
 * A food in the local food table. Built-in foods are seeded on first launch; foods found
 * through Open Food Facts or created by the user are cached here so search works offline.
 *
 * All nutrition values are per 100 g. Carbs are total carbohydrate including fibre.
 */
@Entity(
    tableName = "foods",
    indices = [
        Index(value = ["barcode"], unique = true),
        Index(value = ["name"]),
        Index(value = ["category"]),
    ],
)
data class Food(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val brand: String? = null,
    /** EAN-13 / UPC. Unique, but several foods may have no barcode (NULL). */
    val barcode: String? = null,
    val category: FoodCategory,
    val kcalPer100g: Double,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatPer100g: Double,
    val fiberPer100g: Double? = null,
    /** Typical portion, e.g. 50 g for "1 large egg". Used as the default on Add food. */
    val servingSizeG: Double? = null,
    val servingLabel: String? = null,
    val source: FoodSource,
    /** Product code at the source, e.g. the Open Food Facts barcode. */
    val externalId: String? = null,
    val isFavorite: Boolean = false,
    val lastUsedAt: Instant? = null,
    val createdAt: Instant,
)

/** Nutrition for a portion of [grams] of this food. */
fun Food.nutritionFor(grams: Double): Nutrition {
    val factor = grams / 100.0
    return Nutrition(
        kcal = kcalPer100g * factor,
        proteinG = proteinPer100g * factor,
        carbsG = carbsPer100g * factor,
        fatG = fatPer100g * factor,
    )
}
