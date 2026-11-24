package com.example.liftfuel.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.liftfuel.data.local.entity.MealEntry
import com.example.liftfuel.data.local.model.DailyNutrition
import com.example.liftfuel.data.local.model.Nutrition
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MealEntryDao {

    @Query("SELECT * FROM meal_entries WHERE date = :date ORDER BY loggedAt ASC")
    fun observeForDate(date: LocalDate): Flow<List<MealEntry>>

    /** Calorie ring and macro bars on the Today screen. Returns zeros for an empty day. */
    @Query(
        """
        SELECT COALESCE(SUM(kcal), 0) AS kcal,
               COALESCE(SUM(proteinG), 0) AS proteinG,
               COALESCE(SUM(carbsG), 0) AS carbsG,
               COALESCE(SUM(fatG), 0) AS fatG
        FROM meal_entries
        WHERE date = :date
        """,
    )
    fun observeTotalsForDate(date: LocalDate): Flow<Nutrition>

    /** One row per day that has entries; days with nothing logged are absent. */
    @Query(
        """
        SELECT date,
               SUM(kcal) AS kcal,
               SUM(proteinG) AS proteinG,
               SUM(carbsG) AS carbsG,
               SUM(fatG) AS fatG,
               COUNT(*) AS entryCount
        FROM meal_entries
        WHERE date BETWEEN :from AND :to
        GROUP BY date
        ORDER BY date ASC
        """,
    )
    fun observeDailyTotals(from: LocalDate, to: LocalDate): Flow<List<DailyNutrition>>

    @Query(
        """
        SELECT date,
               SUM(kcal) AS kcal,
               SUM(proteinG) AS proteinG,
               SUM(carbsG) AS carbsG,
               SUM(fatG) AS fatG,
               COUNT(*) AS entryCount
        FROM meal_entries
        WHERE date BETWEEN :from AND :to
        GROUP BY date
        ORDER BY date ASC
        """,
    )
    suspend fun getDailyTotals(from: LocalDate, to: LocalDate): List<DailyNutrition>

    @Query("SELECT * FROM meal_entries WHERE id = :id")
    suspend fun getById(id: Long): MealEntry?

    @Insert
    suspend fun insert(entry: MealEntry): Long

    @Insert
    suspend fun insertAll(entries: List<MealEntry>): List<Long>

    @Update
    suspend fun update(entry: MealEntry)

    @Delete
    suspend fun delete(entry: MealEntry)
}
