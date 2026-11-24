package com.example.liftfuel.domain

import com.example.liftfuel.data.local.entity.ActivityLevel
import com.example.liftfuel.data.local.entity.Goal
import com.example.liftfuel.data.local.entity.Sex
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class NutritionCalculatorTest {

    @Test
    fun bmrMatchesMifflinStJeor() {
        assertEquals(1780.0, NutritionCalculator.bmr(Sex.MALE, 80.0, 180.0, 30), 0.001)
        assertEquals(1345.25, NutritionCalculator.bmr(Sex.FEMALE, 60.0, 165.0, 25), 0.001)
    }

    @Test
    fun demoUserTargets() {
        // 82.4 kg, 178 cm, 22 y, moderately active, losing weight:
        // BMR 1831.5 → TDEE 2838.8 → −500 → 2340 kcal; protein 1.8 g/kg = 148 g; fat 25 % = 65 g.
        val targets = NutritionCalculator.targets(Sex.MALE, 82.4, 178.0, 22, ActivityLevel.MODERATE, Goal.LOSE_WEIGHT)
        assertEquals(NutritionCalculator.Targets(calories = 2340, proteinG = 148, carbsG = 291, fatG = 65), targets)
    }

    @Test
    fun neverGoesBelowTheCalorieFloor() {
        val targets = NutritionCalculator.targets(Sex.FEMALE, 48.0, 152.0, 30, ActivityLevel.SEDENTARY, Goal.LOSE_WEIGHT)
        assertTrue(targets.calories >= 1200)
    }

    @Test
    fun macrosAddUpToCalories() {
        Goal.entries.forEach { goal ->
            ActivityLevel.entries.forEach { activity ->
                val t = NutritionCalculator.targets(Sex.FEMALE, 65.0, 168.0, 24, activity, goal)
                val fromMacros = t.proteinG * 4 + t.carbsG * 4 + t.fatG * 9
                assertTrue("$goal/$activity: $t", abs(fromMacros - t.calories) <= 10)
            }
        }
    }
}
