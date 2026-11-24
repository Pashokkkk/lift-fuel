package com.example.liftfuel.data.seed

import com.example.liftfuel.data.local.entity.Equipment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogSeedTest {

    private val exercises = TestCatalog.exercises

    @Test
    fun exerciseNamesAreUnique() {
        assertTrue(exercises.size >= 40)
        assertEquals(exercises.size, exercises.map { it.name.lowercase() }.toSet().size)
    }

    @Test
    fun onlyBodyweightExercisesHaveNoWeightIncrement() {
        exercises.forEach { exercise ->
            val bodyweight = exercise.equipment == Equipment.BODYWEIGHT
            assertEquals(exercise.name, bodyweight, exercise.weightIncrementKg == 0.0)
            assertFalse(exercise.name, exercise.primaryMuscle in exercise.secondaryMuscles)
            assertFalse(exercise.instructions.isNullOrBlank())
        }
    }

    @Test
    fun templatesUseKnownExercisesAndSensiblePlans() {
        val names = exercises.map { it.name }.toSet()
        assertEquals(7, TemplateSeed.templates.size)
        TemplateSeed.templates.forEach { template ->
            assertTrue(template.name, template.items.isNotEmpty())
            template.items.forEach { item ->
                val where = "${template.name} / ${item.exerciseName}"
                assertTrue("$where: unknown exercise", item.exerciseName in names)
                assertTrue(where, item.sets in 1..5)
                assertTrue(where, item.repMin in 1..item.repMax)
                assertTrue(where, item.restSeconds in 30..300)
            }
        }
    }

    @Test
    fun defaultRemindersStartSwitchedOff() {
        val reminders = ReminderSeed.defaults()
        assertEquals(3, reminders.size)
        assertTrue(reminders.none { it.enabled })
        assertTrue(reminders.all { it.days.isNotEmpty() })
    }
}
