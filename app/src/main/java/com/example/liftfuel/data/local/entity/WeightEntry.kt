package com.example.liftfuel.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate

/** One body-weight reading per day (logging again on the same day replaces it). */
@Entity(
    tableName = "weight_entries",
    indices = [Index(value = ["date"], unique = true)],
)
data class WeightEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    val weightKg: Double,
    val note: String? = null,
    val createdAt: Instant,
)
