package com.example.liftfuel.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.liftfuel.data.local.entity.WeightEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WeightDao {

    @Query("SELECT * FROM weight_entries ORDER BY date ASC")
    fun observeAll(): Flow<List<WeightEntry>>

    @Query("SELECT * FROM weight_entries WHERE date BETWEEN :from AND :to ORDER BY date ASC")
    fun observeBetween(from: LocalDate, to: LocalDate): Flow<List<WeightEntry>>

    @Query("SELECT * FROM weight_entries WHERE date BETWEEN :from AND :to ORDER BY date ASC")
    suspend fun getBetween(from: LocalDate, to: LocalDate): List<WeightEntry>

    @Query("SELECT * FROM weight_entries ORDER BY date DESC LIMIT 1")
    fun observeLatest(): Flow<WeightEntry?>

    @Query("SELECT * FROM weight_entries ORDER BY date DESC LIMIT 1")
    suspend fun getLatest(): WeightEntry?

    /** The date column is unique, so logging twice on one day replaces the earlier value. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(entry: WeightEntry): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<WeightEntry>): List<Long>

    @Delete
    suspend fun delete(entry: WeightEntry)
}
