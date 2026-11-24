package com.example.liftfuel.data.local.entity

// Room stores every enum below as TEXT (the constant's name), so renaming a constant
// is a schema change. Adding new constants at the end is always safe.

enum class Sex { MALE, FEMALE }

/** Multipliers for total daily energy expenditure (TDEE = BMR × multiplier). */
enum class ActivityLevel(val multiplier: Double) {
    SEDENTARY(1.2),
    LIGHT(1.375),
    MODERATE(1.55),
    ACTIVE(1.725),
    VERY_ACTIVE(1.9),
}

enum class Goal { LOSE_WEIGHT, GAIN_MUSCLE, MAINTAIN }

enum class UnitSystem { METRIC, IMPERIAL }

enum class MealType { BREAKFAST, LUNCH, DINNER, SNACK }

enum class FoodCategory {
    MEAT_FISH,
    DAIRY_EGGS,
    GRAINS_BREAD,
    FRUIT,
    VEGETABLES,
    LEGUMES_NUTS,
    FATS_OILS,
    SNACKS_SWEETS,
    DRINKS,
    SUPPLEMENTS,
    READY_MEALS,
    SAUCES,
}

/** Where a food's nutrition values come from. */
enum class FoodSource { BUILT_IN, OPEN_FOOD_FACTS, USER, AI_ESTIMATE }

/** How a meal entry was logged. */
enum class EntrySource { SEARCH, BARCODE, PHOTO, FAVORITE, QUICK_ADD }

enum class Confidence { LOW, MEDIUM, HIGH }

/**
 * Lifecycle of a meal photo:
 * PENDING (saved offline, waiting for network) → ANALYZING → READY (draft on Review meal)
 * → SAVED (turned into meal entries). FAILED can be retried by WorkManager.
 */
enum class PhotoStatus { PENDING, ANALYZING, READY, FAILED, SAVED }

enum class MuscleGroup {
    CHEST,
    BACK,
    SHOULDERS,
    BICEPS,
    TRICEPS,
    QUADS,
    HAMSTRINGS,
    GLUTES,
    CALVES,
    CORE,
    FULL_BODY,
}

enum class Equipment { BARBELL, DUMBBELL, MACHINE, CABLE, BODYWEIGHT, KETTLEBELL }

enum class RecommendationType { CALORIE_ADJUSTMENT, INCREASE_WEIGHT, PROTEIN_REMINDER }

enum class RecommendationStatus { ACTIVE, DISMISSED, APPLIED }

enum class ReminderType { MEAL_LOGGING, WORKOUT_DAY, DAILY_SUMMARY }
