package com.example.liftfuel.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

/** A saved combination of foods that can be logged again in one tap. */
@Entity(tableName = "favorite_meals")
data class FavoriteMeal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val defaultMealType: MealType? = null,
    val createdAt: Instant,
    val lastUsedAt: Instant? = null,
)

@Entity(
    tableName = "favorite_meal_items",
    foreignKeys = [
        ForeignKey(
            entity = FavoriteMeal::class,
            parentColumns = ["id"],
            childColumns = ["favoriteMealId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Food::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("favoriteMealId"), Index("foodId")],
)
data class FavoriteMealItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val favoriteMealId: Long,
    val foodId: Long,
    val grams: Double,
    val position: Int,
)
