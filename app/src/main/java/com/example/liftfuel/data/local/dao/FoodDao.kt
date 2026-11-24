package com.example.liftfuel.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.liftfuel.data.local.entity.Food
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface FoodDao {

    /** Case-insensitive search by name or brand; favourites and recently used foods first. */
    @Query(
        """
        SELECT * FROM foods
        WHERE name LIKE '%' || :query || '%' OR brand LIKE '%' || :query || '%'
        ORDER BY isFavorite DESC, COALESCE(lastUsedAt, 0) DESC, name COLLATE NOCASE ASC
        LIMIT :limit
        """,
    )
    fun search(query: String, limit: Int): Flow<List<Food>>

    @Query(
        """
        SELECT * FROM foods
        WHERE name LIKE '%' || :query || '%' OR brand LIKE '%' || :query || '%'
        ORDER BY isFavorite DESC, COALESCE(lastUsedAt, 0) DESC, name COLLATE NOCASE ASC
        LIMIT :limit
        """,
    )
    suspend fun searchOnce(query: String, limit: Int): List<Food>

    /** Exact, case-insensitive match; first step when matching AI photo items. */
    @Query("SELECT * FROM foods WHERE name = :name COLLATE NOCASE LIMIT 1")
    suspend fun findByName(name: String): Food?

    @Query("SELECT * FROM foods WHERE barcode = :barcode LIMIT 1")
    suspend fun findByBarcode(barcode: String): Food?

    @Query("SELECT * FROM foods WHERE id = :id")
    suspend fun getById(id: Long): Food?

    @Query("SELECT * FROM foods ORDER BY name COLLATE NOCASE ASC")
    suspend fun getAll(): List<Food>

    @Query("SELECT * FROM foods WHERE isFavorite = 1 ORDER BY name COLLATE NOCASE ASC")
    fun observeFavorites(): Flow<List<Food>>

    @Query("SELECT * FROM foods WHERE lastUsedAt IS NOT NULL ORDER BY lastUsedAt DESC LIMIT :limit")
    fun observeRecent(limit: Int): Flow<List<Food>>

    @Query("SELECT COUNT(*) FROM foods")
    suspend fun count(): Int

    @Insert
    suspend fun insert(food: Food): Long

    @Insert
    suspend fun insertAll(foods: List<Food>): List<Long>

    @Update
    suspend fun update(food: Food)

    @Query("UPDATE foods SET isFavorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: Long, favorite: Boolean)

    @Query("UPDATE foods SET lastUsedAt = :usedAt WHERE id = :id")
    suspend fun markUsed(id: Long, usedAt: Instant)
}
