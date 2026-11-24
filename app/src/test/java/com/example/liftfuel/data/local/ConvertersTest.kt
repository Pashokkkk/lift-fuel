package com.example.liftfuel.data.local

import com.example.liftfuel.data.local.entity.MuscleGroup
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun datesAndTimesRoundTrip() {
        val date = LocalDate.of(2026, 11, 17)
        val instant = Instant.parse("2026-11-17T18:30:15.123Z")
        val time = LocalTime.of(13, 30)
        assertEquals(date, converters.epochDayToLocalDate(converters.localDateToEpochDay(date)))
        assertEquals(instant, converters.epochMilliToInstant(converters.instantToEpochMilli(instant)))
        assertEquals(time, converters.secondOfDayToLocalTime(converters.localTimeToSecondOfDay(time)))
    }

    @Test
    fun muscleGroupsRoundTrip() {
        val muscles = listOf(MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS)
        assertEquals("TRICEPS,SHOULDERS", converters.muscleGroupsToText(muscles))
        assertEquals(muscles, converters.textToMuscleGroups(converters.muscleGroupsToText(muscles)))
        assertEquals(emptyList<MuscleGroup>(), converters.textToMuscleGroups(""))
    }

    @Test
    fun daysOfWeekRoundTrip() {
        val days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
        assertEquals(0b0010101, converters.daysToMask(days))
        assertEquals(days, converters.maskToDays(converters.daysToMask(days)))
        assertEquals(127, converters.daysToMask(DayOfWeek.entries.toSet()))
    }
}
