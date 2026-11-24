package com.example.liftfuel.data.seed

import com.example.liftfuel.data.local.entity.EntrySource
import com.example.liftfuel.data.local.entity.MealType
import com.example.liftfuel.data.local.entity.PhotoStatus
import com.example.liftfuel.data.local.entity.RecommendationType
import com.example.liftfuel.data.local.entity.dailyCalorieTarget
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class DemoDataGeneratorTest {

    private val today = TestCatalog.today
    private val history = TestCatalog.demoHistory()

    @Test
    fun isDeterministic() {
        assertEquals(history, TestCatalog.demoHistory())
    }

    @Test
    fun containsWellOver100Records() {
        val total = history.weights.size + history.meals.size + history.workouts.size +
            history.workouts.sumOf { it.sets.size } + history.recommendations.size +
            history.favorites.size + history.photoMeals.size + history.pendingPhotos.size
        assertTrue("Only $total records", total >= 300)
    }

    @Test
    fun weightsAreOnePerDayAndPlausible() {
        val dates = history.weights.map { it.date }
        assertEquals(dates.sorted(), dates)
        assertEquals(dates.size, dates.toSet().size)
        assertEquals(today, dates.last())
        assertEquals(today.minusDays(DemoDataGenerator.HISTORY_DAYS - 1L), dates.first())
        assertTrue(history.weights.size in 25..35)
        assertTrue(history.weights.all { it.weightKg in 79.0..84.0 })
    }

    @Test
    fun mealsReferToCatalogFoodsAndAddUpToRealisticDays() {
        val foodIds = TestCatalog.foods.map { it.id }.toSet()
        val allMeals = history.meals + history.photoMeals.flatMap { it.entries }
        allMeals.forEach { entry ->
            entry.foodId?.let { assertTrue(entry.foodName, it in foodIds) }
            assertTrue(entry.date in today.minusDays(DemoDataGenerator.MEAL_DAYS - 1L)..today)
            assertTrue(entry.grams > 0 && entry.kcal >= 0)
        }

        val todayTypes = allMeals.filter { it.date == today }.map { it.mealType }.toSet()
        assertEquals(setOf(MealType.BREAKFAST, MealType.LUNCH), todayTypes)

        val fullDays = allMeals.filter { it.date < today }.groupBy { it.date }
        assertEquals(DemoDataGenerator.MEAL_DAYS - 1, fullDays.size)
        val dailyKcal = fullDays.mapValues { (_, entries) -> entries.sumOf { it.kcal } }
        dailyKcal.forEach { (date, kcal) -> assertTrue("$date: $kcal kcal", kcal in 1400.0..3300.0) }

        val target = history.profile.dailyCalorieTarget
        val average = dailyKcal.values.average()
        assertTrue("Average $average vs target $target", abs(average - target) / target < 0.15)
    }

    @Test
    fun photoMealsAreLabelledAsEstimates() {
        val photo = history.photoMeals.single()
        assertEquals(PhotoStatus.SAVED, photo.analysis.status)
        assertEquals(photo.items.size, photo.entries.size)
        assertTrue(photo.entries.all { it.isEstimate && it.source == EntrySource.PHOTO && it.confidence != null })
        assertTrue(photo.items.any { it.matchedFoodId == null })
        assertEquals(PhotoStatus.PENDING, history.pendingPhotos.single().status)
    }

    @Test
    fun workoutsFollowTheWeeklyScheduleAndProgress() {
        val workouts = history.workouts
        assertEquals(15, workouts.size) // Mon/Wed/Fri over the 5 weeks before today
        workouts.forEach { workout ->
            val session = workout.session
            assertTrue(session.date < today)
            assertTrue(session.finishedAt!! > session.startedAt)
            assertTrue(workout.sets.first().isWarmup)
            assertTrue(workout.sets.filterNot { it.isWarmup }.all { it.reps > 0 && it.weightKg > 0 })
            assertEquals(workout.sets.sortedBy { it.completedAt }, workout.sets)
        }

        val bench = TestCatalog.exercises.first { it.name == DemoDataGenerator.FEATURED_EXERCISE }
        val benchSessions = workouts.filter { w -> w.sets.any { it.exerciseId == bench.id } }
        val benchWeights = benchSessions.map { w -> w.sets.first { it.exerciseId == bench.id && !it.isWarmup }.weightKg }
        assertEquals(benchWeights.sorted(), benchWeights)

        val lastBenchSets = benchSessions.last().sets.filter { it.exerciseId == bench.id && !it.isWarmup }
        assertTrue("Last bench session should hit the top of the range", lastBenchSets.all { it.reps == it.targetReps })
    }

    @Test
    fun everyTipExplainsWhy() {
        val tips = history.recommendations
        assertEquals(
            setOf(RecommendationType.CALORIE_ADJUSTMENT, RecommendationType.INCREASE_WEIGHT, RecommendationType.PROTEIN_REMINDER),
            tips.map { it.type }.toSet(),
        )
        assertEquals(tips.size, tips.map { it.dedupeKey }.toSet().size)
        tips.forEach { tip ->
            assertFalse(tip.title, tip.reason.isBlank())
            assertFalse(tip.title, tip.message.isBlank())
        }
    }

    @Test
    fun worksForAnyStartDay() {
        // Seeding happens on install, so the generator must cope with every weekday.
        (0L..6L).forEach { offset ->
            val other = TestCatalog.demoHistory(today = today.plusDays(offset))
            assertTrue(other.workouts.isNotEmpty())
            assertEquals(3, other.recommendations.size)
        }
    }
}
