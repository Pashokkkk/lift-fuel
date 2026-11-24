package com.example.liftfuel.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.liftfuel.data.local.entity.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders ORDER BY type ASC, timeOfDay ASC")
    fun observeAll(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE enabled = 1")
    suspend fun getEnabled(): List<Reminder>

    @Insert
    suspend fun insertAll(reminders: List<Reminder>): List<Long>

    @Update
    suspend fun update(reminder: Reminder)

    @Query("UPDATE reminders SET enabled = :enabled WHERE id = :id")
    suspend fun setEnabled(id: Long, enabled: Boolean)

    @Query("UPDATE reminders SET enabled = :enabled")
    suspend fun setAllEnabled(enabled: Boolean)
}
