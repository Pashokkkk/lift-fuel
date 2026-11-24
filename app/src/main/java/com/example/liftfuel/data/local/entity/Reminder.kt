package com.example.liftfuel.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalTime

@Entity(tableName = "reminders", indices = [Index("type")])
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: ReminderType,
    val label: String,
    val timeOfDay: LocalTime,
    /** Stored as a 7-bit mask (Monday = bit 0), see Converters. */
    val days: Set<DayOfWeek>,
    val enabled: Boolean = false,
)
