package com.example.liftfuel.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.liftfuel.data.local.entity.FavoriteMeal
import com.example.liftfuel.data.local.entity.FavoriteMealItem
import com.example.liftfuel.data.local.model.FavoriteMealWithItems
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
abstract class FavoriteMealDao {

    @Transaction
    @Query("SELECT * FROM favorite_meals ORDER BY COALESCE(lastUsedAt, createdAt) DESC")
    abstract fun observeAllWithItems(): Flow<List<FavoriteMealWithItems>>

    @Transaction
    @Query("SELECT * FROM favorite_meals WHERE id = :id")
    abstract suspend fun getWithItems(id: Long): FavoriteMealWithItems?

    @Insert
    abstract suspend fun insertMeal(meal: FavoriteMeal): Long

    @Insert
    abstract suspend fun insertItems(items: List<FavoriteMealItem>)

    /** Saves the meal and its items together; item favoriteMealId values are filled in here. */
    @Transaction
    open suspend fun insertWithItems(meal: FavoriteMeal, items: List<FavoriteMealItem>): Long {
        val mealId = insertMeal(meal)
        insertItems(items.mapIndexed { index, item -> item.copy(favoriteMealId = mealId, position = index) })
        return mealId
    }

    @Query("UPDATE favorite_meals SET lastUsedAt = :usedAt WHERE id = :id")
    abstract suspend fun markUsed(id: Long, usedAt: Instant)

    @Delete
    abstract suspend fun delete(meal: FavoriteMeal)
}
