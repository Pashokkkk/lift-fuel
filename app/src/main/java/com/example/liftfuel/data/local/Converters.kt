package com.example.liftfuel.data.local

import androidx.room.TypeConverter
import com.example.liftfuel.data.local.entity.MuscleGroup
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

/**
 * How non-SQL types are stored. Converters take and return nullable values (the form the
 * Room docs use) so they work for both nullable and non-null columns.
 * Enums need no converter: Room stores them as their name (TEXT).
 */
class Converters {

    /** LocalDate ↔ days since 1970-01-01, so date ranges compare as numbers. */
    @TypeConverter
    fun localDateToEpochDay(value: LocalDate?): Long? = value?.toEpochDay()

    @TypeConverter
    fun epochDayToLocalDate(value: Long?): LocalDate? = value?.let(LocalDate::ofEpochDay)

    @TypeConverter
    fun instantToEpochMilli(value: Instant?): Long? = value?.toEpochMilli()

    @TypeConverter
    fun epochMilliToInstant(value: Long?): Instant? = value?.let(Instant::ofEpochMilli)

    @TypeConverter
    fun localTimeToSecondOfDay(value: LocalTime?): Int? = value?.toSecondOfDay()

    @TypeConverter
    fun secondOfDayToLocalTime(value: Int?): LocalTime? = value?.let { LocalTime.ofSecondOfDay(it.toLong()) }

    @TypeConverter
    fun muscleGroupsToText(value: List<MuscleGroup>?): String? = value?.joinToString(",") { it.name }

    @TypeConverter
    fun textToMuscleGroups(value: String?): List<MuscleGroup>? = when {
        value == null -> null
        value.isBlank() -> emptyList()
        else -> value.split(",").map { MuscleGroup.valueOf(it.trim()) }
    }

    /** Monday = bit 0 … Sunday = bit 6. */
    @TypeConverter
    fun daysToMask(value: Set<DayOfWeek>?): Int? =
        value?.fold(0) { mask, day -> mask or (1 shl (day.value - 1)) }

    @TypeConverter
    fun maskToDays(value: Int?): Set<DayOfWeek>? =
        value?.let { mask -> DayOfWeek.entries.filterTo(LinkedHashSet()) { mask and (1 shl (it.value - 1)) != 0 } }
}
