package com.example.liftfuel.data.seed

import com.example.liftfuel.data.local.entity.FoodCategory
import com.example.liftfuel.data.local.entity.FoodSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs
import kotlin.math.max

class FoodSeedTest {

    private val foods = TestCatalog.foods

    @Test
    fun hasAtLeast100Foods() {
        assertTrue("Only ${foods.size} foods", foods.size >= 100)
    }

    @Test
    fun namesAreUnique() {
        val duplicates = foods.groupBy { it.name.lowercase() }.filterValues { it.size > 1 }.keys
        assertTrue("Duplicate names: $duplicates", duplicates.isEmpty())
    }

    @Test
    fun everyCategoryHasFoods() {
        val missing = FoodCategory.entries - foods.map { it.category }.toSet()
        assertTrue("Empty categories: $missing", missing.isEmpty())
    }

    @Test
    fun allFoodsAreBuiltInWithSaneValues() {
        foods.forEach { food ->
            assertEquals(FoodSource.BUILT_IN, food.source)
            assertTrue(food.name, food.kcalPer100g in 0.0..900.0)
            assertTrue(food.name, food.proteinPer100g >= 0 && food.carbsPer100g >= 0 && food.fatPer100g >= 0)
            assertTrue(food.name, food.proteinPer100g + food.carbsPer100g + food.fatPer100g <= 100.5)
            assertTrue(food.name, (food.fiberPer100g ?: 0.0) <= food.carbsPer100g)
            food.servingSizeG?.let { assertTrue(food.name, it > 0) }
        }
    }

    /**
     * Calories should roughly match the macros (Atwater factors): 4 kcal/g protein,
     * 4 kcal/g digestible carbs, 2 kcal/g fibre, 9 kcal/g fat. Real foods differ a little,
     * so allow 12 % or 15 kcal, whichever is larger.
     */
    @Test
    fun caloriesMatchMacros() {
        val offenders = foods.mapNotNull { food ->
            val fiber = food.fiberPer100g ?: 0.0
            val estimate = 4 * food.proteinPer100g + 4 * (food.carbsPer100g - fiber) + 2 * fiber + 9 * food.fatPer100g
            val tolerance = max(15.0, food.kcalPer100g * 0.12)
            if (abs(estimate - food.kcalPer100g) > tolerance) {
                "${food.name}: label ${food.kcalPer100g}, macros give ${"%.0f".format(estimate)}"
            } else {
                null
            }
        }
        assertTrue(offenders.joinToString("\n"), offenders.isEmpty())
    }

    @Test
    fun barcodesAreValidInStoreEan13AndUnique() {
        val barcodes = foods.mapNotNull { it.barcode }
        assertTrue(barcodes.size >= 10)
        assertEquals(barcodes.size, barcodes.toSet().size)
        barcodes.forEach { code ->
            assertTrue(code, code.length == 13 && code.all(Char::isDigit))
            assertTrue("$code should use the in-store prefix 2", code.startsWith("2"))
            val sum = code.take(12).mapIndexed { i, c -> (c - '0') * if (i % 2 == 0) 1 else 3 }.sum()
            assertEquals("Bad check digit in $code", (10 - sum % 10) % 10, code.last() - '0')
        }
    }
}
