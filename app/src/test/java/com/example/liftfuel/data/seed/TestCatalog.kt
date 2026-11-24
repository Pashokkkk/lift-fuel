package com.example.liftfuel.data.seed

import com.example.liftfuel.data.local.entity.WorkoutTemplate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/** The seeded catalog with the ids Room would assign (1, 2, 3 … in insertion order). */
object TestCatalog {
    val now: Instant = Instant.parse("2026-11-17T09:00:00Z")
    val today: LocalDate = LocalDate.of(2026, 11, 17) // a Tuesday
    val zone: ZoneId = ZoneId.of("Europe/Dublin")

    val foods = FoodSeed.foods(now).mapIndexed { index, food -> food.copy(id = index + 1L) }
    val exercises = ExerciseSeed.exercises().mapIndexed { index, exercise -> exercise.copy(id = index + 1L) }
    val templates = TemplateSeed.templates.mapIndexed { index, template ->
        WorkoutTemplate(
            id = index + 1L,
            name = template.name,
            description = template.description,
            sortOrder = index,
            isBuiltIn = true,
            createdAt = now,
        )
    }

    val lookup = CatalogLookup(foods, exercises, templates)

    fun demoHistory(today: LocalDate = this.today): DemoHistory =
        DemoDataGenerator(today = today, zone = zone, catalog = lookup).generate()
}
