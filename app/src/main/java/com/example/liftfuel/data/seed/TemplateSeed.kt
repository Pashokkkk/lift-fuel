package com.example.liftfuel.data.seed

import com.example.liftfuel.data.local.entity.Reminder
import com.example.liftfuel.data.local.entity.ReminderType
import java.time.DayOfWeek
import java.time.LocalTime

/** Built-in workout templates. Exercises are referenced by name (see ExerciseSeed). */
object TemplateSeed {

    data class Item(
        val exerciseName: String,
        val sets: Int,
        val repMin: Int,
        val repMax: Int,
        val restSeconds: Int,
    )

    data class Template(
        val name: String,
        val description: String,
        val items: List<Item>,
    )

    val templates: List<Template> = listOf(
        Template(
            name = "Push Day",
            description = "Chest, shoulders and triceps.",
            items = listOf(
                Item("Barbell bench press", 3, 6, 10, 150),
                Item("Overhead press", 3, 6, 10, 120),
                Item("Incline dumbbell press", 3, 8, 12, 90),
                Item("Lateral raise", 3, 12, 15, 60),
                Item("Triceps pushdown", 3, 10, 15, 60),
            ),
        ),
        Template(
            name = "Pull Day",
            description = "Back and biceps.",
            items = listOf(
                Item("Barbell row", 3, 6, 10, 120),
                Item("Lat pulldown", 3, 8, 12, 90),
                Item("Seated cable row", 3, 8, 12, 90),
                Item("Face pull", 3, 12, 15, 60),
                Item("Dumbbell curl", 3, 10, 12, 60),
            ),
        ),
        Template(
            name = "Leg Day",
            description = "Quads, hamstrings, glutes and calves.",
            items = listOf(
                Item("Back squat", 3, 5, 8, 180),
                Item("Romanian deadlift", 3, 8, 10, 120),
                Item("Leg press", 3, 10, 12, 90),
                Item("Lying leg curl", 3, 10, 15, 60),
                Item("Standing calf raise", 3, 12, 15, 60),
            ),
        ),
        Template(
            name = "Full Body A",
            description = "Three days a week, alternate with Full Body B.",
            items = listOf(
                Item("Back squat", 3, 5, 8, 180),
                Item("Barbell bench press", 3, 5, 8, 150),
                Item("Barbell row", 3, 6, 10, 120),
                Item("Hanging leg raise", 3, 8, 12, 60),
            ),
        ),
        Template(
            name = "Full Body B",
            description = "Three days a week, alternate with Full Body A.",
            items = listOf(
                Item("Deadlift", 2, 3, 5, 180),
                Item("Overhead press", 3, 6, 10, 120),
                Item("Lat pulldown", 3, 8, 12, 90),
                Item("Bulgarian split squat", 3, 8, 10, 90),
                Item("Cable crunch", 3, 10, 15, 60),
            ),
        ),
        Template(
            name = "Upper Body (machines)",
            description = "Beginner-friendly machines and cables.",
            items = listOf(
                Item("Chest press machine", 3, 10, 12, 90),
                Item("Lat pulldown", 3, 10, 12, 90),
                Item("Seated dumbbell shoulder press", 3, 10, 12, 90),
                Item("Seated cable row", 3, 10, 12, 90),
                Item("Cable curl", 2, 12, 15, 60),
                Item("Triceps pushdown", 2, 12, 15, 60),
            ),
        ),
        Template(
            name = "Lower Body (machines)",
            description = "Beginner-friendly machines.",
            items = listOf(
                Item("Leg press", 3, 10, 12, 90),
                Item("Leg extension", 3, 12, 15, 60),
                Item("Seated leg curl", 3, 12, 15, 60),
                Item("Hip thrust", 3, 10, 12, 90),
                Item("Seated calf raise", 3, 12, 15, 60),
            ),
        ),
    )
}

/** Default reminders, created switched off; the user enables them in Settings. */
object ReminderSeed {

    private val EVERY_DAY: Set<DayOfWeek> = DayOfWeek.entries.toSet()

    fun defaults(): List<Reminder> = listOf(
        Reminder(
            type = ReminderType.MEAL_LOGGING,
            label = "Log your lunch",
            timeOfDay = LocalTime.of(13, 30),
            days = EVERY_DAY,
        ),
        Reminder(
            type = ReminderType.WORKOUT_DAY,
            label = "Workout today",
            timeOfDay = LocalTime.of(17, 0),
            days = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
        ),
        Reminder(
            type = ReminderType.DAILY_SUMMARY,
            label = "Your daily summary",
            timeOfDay = LocalTime.of(21, 0),
            days = EVERY_DAY,
        ),
    )
}
