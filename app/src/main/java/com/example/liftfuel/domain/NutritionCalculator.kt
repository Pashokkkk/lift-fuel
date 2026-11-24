package com.example.liftfuel.domain

import com.example.liftfuel.data.local.entity.ActivityLevel
import com.example.liftfuel.data.local.entity.Goal
import com.example.liftfuel.data.local.entity.Sex
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/** Daily calorie and macro targets from the Mifflin-St Jeor equation. */
object NutritionCalculator {

    data class Targets(
        val calories: Int,
        val proteinG: Int,
        val carbsG: Int,
        val fatG: Int,
    )

    /** Calorie floors so the app never suggests a very aggressive diet. */
    private const val MIN_CALORIES_MALE = 1500.0
    private const val MIN_CALORIES_FEMALE = 1200.0

    /** Basal metabolic rate in kcal/day. */
    fun bmr(sex: Sex, weightKg: Double, heightCm: Double, ageYears: Int): Double {
        val base = 10 * weightKg + 6.25 * heightCm - 5 * ageYears
        return if (sex == Sex.MALE) base + 5 else base - 161
    }

    fun tdee(bmr: Double, activityLevel: ActivityLevel): Double = bmr * activityLevel.multiplier

    /**
     * Goal offsets kept beginner-safe:
     * lose = −20 % of TDEE (at most −500 kcal), gain = +10 % (at most +300 kcal).
     * Protein: 1.8 g/kg when losing or gaining, 1.6 g/kg when maintaining.
     * Fat: 25 % of calories. Carbs: the rest.
     */
    fun targets(
        sex: Sex,
        weightKg: Double,
        heightCm: Double,
        ageYears: Int,
        activityLevel: ActivityLevel,
        goal: Goal,
    ): Targets {
        val tdee = tdee(bmr(sex, weightKg, heightCm, ageYears), activityLevel)
        val offset = when (goal) {
            Goal.LOSE_WEIGHT -> -min(500.0, tdee * 0.20)
            Goal.GAIN_MUSCLE -> min(300.0, tdee * 0.10)
            Goal.MAINTAIN -> 0.0
        }
        val floor = if (sex == Sex.MALE) MIN_CALORIES_MALE else MIN_CALORIES_FEMALE
        val calories = roundToNearest10(max(tdee + offset, floor))

        val proteinPerKg = if (goal == Goal.MAINTAIN) 1.6 else 1.8
        val proteinG = (weightKg * proteinPerKg).roundToInt()
        val fatG = (calories * 0.25 / 9).roundToInt()
        val carbsG = max(0, ((calories - proteinG * 4 - fatG * 9) / 4.0).roundToInt())

        return Targets(calories = calories, proteinG = proteinG, carbsG = carbsG, fatG = fatG)
    }

    private fun roundToNearest10(value: Double): Int = (value / 10).roundToInt() * 10
}
