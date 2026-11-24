package com.example.liftfuel.data.seed

import com.example.liftfuel.data.local.entity.ActivityLevel
import com.example.liftfuel.data.local.entity.Confidence
import com.example.liftfuel.data.local.entity.EntrySource
import com.example.liftfuel.data.local.entity.Exercise
import com.example.liftfuel.data.local.entity.FavoriteMeal
import com.example.liftfuel.data.local.entity.FavoriteMealItem
import com.example.liftfuel.data.local.entity.Food
import com.example.liftfuel.data.local.entity.FoodSource
import com.example.liftfuel.data.local.entity.Goal
import com.example.liftfuel.data.local.entity.MealEntry
import com.example.liftfuel.data.local.entity.MealType
import com.example.liftfuel.data.local.entity.PhotoAnalysis
import com.example.liftfuel.data.local.entity.PhotoAnalysisItem
import com.example.liftfuel.data.local.entity.PhotoStatus
import com.example.liftfuel.data.local.entity.Recommendation
import com.example.liftfuel.data.local.entity.RecommendationType
import com.example.liftfuel.data.local.entity.Sex
import com.example.liftfuel.data.local.entity.UnitSystem
import com.example.liftfuel.data.local.entity.UserProfile
import com.example.liftfuel.data.local.entity.WeightEntry
import com.example.liftfuel.data.local.entity.WorkoutSession
import com.example.liftfuel.data.local.entity.WorkoutSet
import com.example.liftfuel.data.local.entity.WorkoutTemplate
import com.example.liftfuel.data.local.entity.dailyCalorieTarget
import com.example.liftfuel.data.local.entity.nutritionFor
import com.example.liftfuel.domain.NutritionCalculator
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random

/** Finds seeded catalog rows by name; fails loudly if demo data mentions something missing. */
class CatalogLookup(
    foods: List<Food>,
    exercises: List<Exercise>,
    templates: List<WorkoutTemplate>,
) {
    private val foodsByName = foods.associateBy { it.name }
    private val exercisesByName = exercises.associateBy { it.name }
    private val templatesByName = templates.associateBy { it.name }

    fun food(name: String): Food =
        foodsByName[name] ?: error("Demo data refers to unknown food \"$name\"")

    fun exercise(name: String): Exercise =
        exercisesByName[name] ?: error("Demo data refers to unknown exercise \"$name\"")

    fun template(name: String): WorkoutTemplate =
        templatesByName[name] ?: error("Demo data refers to unknown template \"$name\"")
}

/**
 * Everything the demo user has logged. Child rows carry 0 as the parent id
 * (sessionId, analysisId, favoriteMealId, photoAnalysisId); DatabaseSeeder fills them in.
 */
data class DemoHistory(
    val profile: UserProfile,
    val weights: List<WeightEntry>,
    val meals: List<MealEntry>,
    val photoMeals: List<DemoPhotoMeal>,
    val pendingPhotos: List<PhotoAnalysis>,
    val favorites: List<DemoFavorite>,
    val workouts: List<DemoWorkout>,
    val recommendations: List<Recommendation>,
)

data class DemoPhotoMeal(
    val analysis: PhotoAnalysis,
    val items: List<PhotoAnalysisItem>,
    val entries: List<MealEntry>,
)

data class DemoFavorite(val meal: FavoriteMeal, val items: List<FavoriteMealItem>)

data class DemoWorkout(val session: WorkoutSession, val sets: List<WorkoutSet>)

/**
 * Builds a realistic, deterministic history for the demo user "Alex" relative to [today]:
 *
 * - 35 days of weigh-ins: steady loss for three weeks, then a two-week plateau
 *   (so the calorie-adjustment tip has a reason to appear);
 * - 21 days of meals, with fewer protein snacks in the last week (protein tip);
 * - Push / Pull / Legs on Mon / Wed / Fri with double progression; the latest bench press
 *   session hits the top of the rep range on every set ("add weight" tip);
 * - one saved photo meal and one photo still waiting for the network.
 *
 * The same [today] and [randomSeed] always produce the same data.
 */
class DemoDataGenerator(
    private val today: LocalDate,
    private val zone: ZoneId,
    private val catalog: CatalogLookup,
    randomSeed: Int = 42,
) {
    private val random = Random(randomSeed)
    private val startDate: LocalDate = today.minusDays(HISTORY_DAYS - 1L)

    fun generate(): DemoHistory {
        val weights = weights()
        val profile = profile()
        val photoMeal = photoMeal(date = today.minusDays(PHOTO_MEAL_DAYS_AGO))
        val meals = meals(skipDinnerOn = photoMeal.analysis.date)
        val workouts = workouts()
        val recommendations = recommendations(profile, weights, meals + photoMeal.entries, workouts)
        return DemoHistory(
            profile = profile,
            weights = weights,
            meals = meals,
            photoMeals = listOf(photoMeal),
            pendingPhotos = listOf(pendingPhoto()),
            favorites = favorites(),
            workouts = workouts,
            recommendations = recommendations,
        )
    }

    // ---------------------------------------------------------------- Profile & weight

    private fun profile(): UserProfile {
        val birthDate = today.minusYears(22).minusDays(97)
        val age = Period.between(birthDate, startDate).years
        val targets = NutritionCalculator.targets(
            sex = Sex.MALE,
            weightKg = START_WEIGHT_KG,
            heightCm = HEIGHT_CM,
            ageYears = age,
            activityLevel = ActivityLevel.MODERATE,
            goal = Goal.LOSE_WEIGHT,
        )
        val createdAt = at(startDate, 7, 15)
        return UserProfile(
            name = "Alex",
            sex = Sex.MALE,
            birthDate = birthDate,
            heightCm = HEIGHT_CM,
            activityLevel = ActivityLevel.MODERATE,
            goal = Goal.LOSE_WEIGHT,
            targetWeightKg = 76.0,
            baseCalorieTarget = targets.calories,
            proteinTargetG = targets.proteinG,
            carbsTargetG = targets.carbsG,
            fatTargetG = targets.fatG,
            unitSystem = UnitSystem.METRIC,
            createdAt = createdAt,
            updatedAt = createdAt,
        )
    }

    private fun weights(): List<WeightEntry> {
        val slopeDays = HISTORY_DAYS - PLATEAU_DAYS
        val plateauWeight = START_WEIGHT_KG - slopeDays * WEEKLY_LOSS_KG / 7
        val entries = mutableListOf<WeightEntry>()
        for (daysAgo in HISTORY_DAYS - 1 downTo 0) {
            val date = today.minusDays(daysAgo.toLong())
            val dayIndex = HISTORY_DAYS - 1 - daysAgo
            val trend = if (daysAgo >= PLATEAU_DAYS) {
                START_WEIGHT_KG - dayIndex * WEEKLY_LOSS_KG / 7
            } else {
                plateauWeight
            }
            val noise = random.nextDouble(-0.3, 0.3)
            val skipped = random.nextDouble() < 0.15
            val mustLog = daysAgo == 0 || daysAgo == HISTORY_DAYS - 1
            if (skipped && !mustLog) continue
            entries += WeightEntry(
                date = date,
                weightKg = round1(if (dayIndex == 0) START_WEIGHT_KG else trend + noise),
                createdAt = at(date, 7, 30 + random.nextInt(0, 20)),
            )
        }
        return entries
    }

    // ---------------------------------------------------------------- Meals

    private class Portion(val foodName: String, val grams: Double)

    private fun plan(vararg items: Pair<String, Number>) = items.map { (name, grams) -> Portion(name, grams.toDouble()) }

    private val breakfasts = listOf(
        plan("Oats, rolled" to 70, "Milk, semi-skimmed" to 250, "Banana" to 120, "Peanut butter" to 16),
        plan("Greek yogurt 0% fat" to 250, "Granola" to 60, "Blueberries" to 80, "Honey" to 10),
        plan("Egg, whole, boiled" to 150, "Wholemeal bread" to 76, "Butter" to 10, "Orange juice" to 200),
        plan("Skyr, plain" to 200, "Strawberries" to 150, "Oats, rolled" to 40, "Banana" to 120),
        plan("Bagel, plain" to 85, "Cream cheese, light" to 40, "Smoked salmon" to 70, "Latte, semi-skimmed milk" to 350),
        plan("Overnight oats with milk" to 300, "Raspberries" to 80, "Almonds" to 20, "Coffee, black" to 250),
    )

    private val lunches = listOf(
        plan("Chicken breast, cooked" to 150, "Basmati rice, cooked" to 250, "Broccoli, boiled" to 120, "Olive oil" to 10),
        plan("Tuna mayo sandwich" to 180, "Apple" to 150, "Potato crisps" to 40),
        plan("Chicken Caesar salad" to 300, "Sourdough bread" to 50, "Cola, zero sugar" to 330),
        plan("Chicken burrito" to 350, "Orange" to 130),
        plan("Lentils, cooked" to 150, "Quinoa, cooked" to 150, "Spinach, raw" to 50, "Feta cheese" to 40, "Olive oil" to 10),
        plan("Chicken salad wrap" to 220, "Greek yogurt, full fat" to 170, "Banana" to 120),
    )

    private val dinners = listOf(
        plan("Salmon fillet, baked" to 150, "Sweet potato, baked" to 250, "Green beans, boiled" to 120, "Olive oil" to 10),
        plan("Spaghetti bolognese" to 450, "Parmesan" to 10, "Mixed salad leaves" to 50),
        plan("Beef mince 5% fat, cooked" to 150, "Pasta, cooked" to 250, "Tomato pasta sauce" to 150, "Parmesan" to 15),
        plan("Chicken curry with rice" to 450),
        plan("Chicken stir-fry with vegetables" to 350, "Egg noodles, cooked" to 200, "Soy sauce" to 15),
        plan("Sirloin steak, cooked" to 200, "Potatoes, boiled" to 300, "Mixed vegetables, frozen, cooked" to 150, "Butter" to 10),
        plan("Margherita pizza" to 321),
        plan("Tofu, firm" to 200, "White rice, cooked" to 250, "Broccoli, boiled" to 120, "Soy sauce" to 15, "Sunflower oil" to 10),
    )

    private val proteinSnacks = listOf(
        plan("Whey protein powder" to 30, "Milk, semi-skimmed" to 300),
        plan("Protein bar" to 60),
        plan("High-protein pudding" to 200),
        plan("Cottage cheese" to 150, "Pineapple" to 100),
    )

    private val otherSnacks = listOf(
        plan("Almonds" to 30),
        plan("Apple" to 150, "Peanut butter" to 20),
        plan("Dark chocolate 70%" to 30),
        plan("Potato crisps" to 40),
        plan("Rice cakes" to 18, "Peanut butter" to 16),
        plan("Digestive biscuit" to 30, "Latte, semi-skimmed milk" to 350),
        plan("Milk chocolate" to 45),
    )

    /** Plans that are also saved as favourites are logged with source FAVORITE. */
    private val favoritePlans = listOf(
        Triple("Oats, banana & peanut butter", MealType.BREAKFAST, breakfasts[0]),
        Triple("Chicken, rice & broccoli", MealType.LUNCH, lunches[0]),
        Triple("Protein shake", MealType.SNACK, proteinSnacks[0]),
    )

    private fun favorites(): List<DemoFavorite> = favoritePlans.mapIndexed { index, (name, mealType, portions) ->
        DemoFavorite(
            meal = FavoriteMeal(
                name = name,
                defaultMealType = mealType,
                createdAt = at(startDate.plusDays(index + 1L), 20, 0),
                lastUsedAt = null,
            ),
            items = portions.mapIndexed { position, portion ->
                FavoriteMealItem(
                    favoriteMealId = 0,
                    foodId = catalog.food(portion.foodName).id,
                    grams = portion.grams,
                    position = position,
                )
            },
        )
    }

    private fun meals(skipDinnerOn: LocalDate): List<MealEntry> {
        val entries = mutableListOf<MealEntry>()
        for (daysAgo in MEAL_DAYS - 1 downTo 0) {
            val date = today.minusDays(daysAgo.toLong())
            val lastWeek = daysAgo <= 7

            entries += logPlan(date, MealType.BREAKFAST, breakfasts.random(random), at(date, 8, random.nextInt(0, 45)))
            entries += logPlan(date, MealType.LUNCH, lunches.random(random), at(date, 13, random.nextInt(0, 45)))
            if (daysAgo == 0) continue // today: only breakfast and lunch so far

            // Fewer protein snacks in the last week, so the protein tip has a reason to appear.
            val proteinSnackChance = if (lastWeek) 0.25 else 0.75
            if (random.nextDouble() < proteinSnackChance) {
                entries += logPlan(date, MealType.SNACK, proteinSnacks.random(random), at(date, 16, random.nextInt(0, 40)))
            }
            if (date != skipDinnerOn) {
                entries += logPlan(date, MealType.DINNER, dinners.random(random), at(date, 19, random.nextInt(0, 60)))
            }
            if (random.nextDouble() < 0.7) {
                entries += logPlan(date, MealType.SNACK, otherSnacks.random(random), at(date, 21, random.nextInt(0, 40)))
            }
        }
        return entries
    }

    private fun logPlan(date: LocalDate, mealType: MealType, portions: List<Portion>, loggedAt: Instant): List<MealEntry> {
        val isFavorite = favoritePlans.any { it.third === portions }
        return portions.mapIndexed { index, portion ->
            val food = catalog.food(portion.foodName)
            val source = when {
                isFavorite -> EntrySource.FAVORITE
                food.barcode != null -> EntrySource.BARCODE
                else -> EntrySource.SEARCH
            }
            mealEntry(date, mealType, food, portion.grams, source, loggedAt.plusSeconds(index * 40L))
        }
    }

    private fun mealEntry(
        date: LocalDate,
        mealType: MealType,
        food: Food,
        grams: Double,
        source: EntrySource,
        loggedAt: Instant,
    ): MealEntry {
        val nutrition = food.nutritionFor(grams)
        return MealEntry(
            date = date,
            mealType = mealType,
            foodId = food.id,
            foodName = food.name,
            grams = grams,
            kcal = round1(nutrition.kcal),
            proteinG = round1(nutrition.proteinG),
            carbsG = round1(nutrition.carbsG),
            fatG = round1(nutrition.fatG),
            source = source,
            loggedAt = loggedAt,
        )
    }

    // ---------------------------------------------------------------- Photos

    private fun photoMeal(date: LocalDate): DemoPhotoMeal {
        val takenAt = at(date, 19, 10)
        val analysis = PhotoAnalysis(
            photoUri = "demo://photos/stir_fry.jpg",
            date = date,
            mealType = MealType.DINNER,
            status = PhotoStatus.SAVED,
            attempts = 1,
            overallConfidence = Confidence.MEDIUM,
            notes = "Cooking oil and sauce are hard to see in a photo, so fat may be higher than shown.",
            modelName = "demo",
            createdAt = takenAt,
            analyzedAt = takenAt.plusSeconds(6),
        )

        fun matched(detectedName: String, foodName: String, grams: Double, confidence: Confidence, position: Int): PhotoAnalysisItem {
            val food = catalog.food(foodName)
            val nutrition = food.nutritionFor(grams)
            return PhotoAnalysisItem(
                analysisId = 0,
                detectedName = detectedName,
                estimatedGrams = grams,
                matchedFoodId = food.id,
                nutritionSource = food.source,
                kcal = round1(nutrition.kcal),
                proteinG = round1(nutrition.proteinG),
                carbsG = round1(nutrition.carbsG),
                fatG = round1(nutrition.fatG),
                confidence = confidence,
                position = position,
            )
        }

        val items = listOf(
            matched("Chicken and vegetable stir-fry", "Chicken stir-fry with vegetables", 330.0, Confidence.HIGH, 0),
            matched("Egg noodles", "Egg noodles, cooked", 180.0, Confidence.MEDIUM, 1),
            PhotoAnalysisItem(
                analysisId = 0,
                detectedName = "Cooking oil (estimated)",
                estimatedGrams = 8.0,
                matchedFoodId = null,
                nutritionSource = FoodSource.AI_ESTIMATE,
                kcal = 71.0,
                proteinG = 0.0,
                carbsG = 0.0,
                fatG = 8.0,
                confidence = Confidence.LOW,
                position = 2,
            ),
        )

        val savedAt = takenAt.plusSeconds(90)
        val entries = items.mapIndexed { index, item ->
            MealEntry(
                date = date,
                mealType = MealType.DINNER,
                foodId = item.matchedFoodId,
                foodName = item.detectedName,
                grams = item.estimatedGrams,
                kcal = item.kcal,
                proteinG = item.proteinG,
                carbsG = item.carbsG,
                fatG = item.fatG,
                source = EntrySource.PHOTO,
                photoAnalysisId = 0,
                isEstimate = true,
                confidence = item.confidence,
                loggedAt = savedAt.plusSeconds(index.toLong()),
            )
        }
        return DemoPhotoMeal(analysis, items, entries)
    }

    /** A snack photo taken offline today, still waiting for WorkManager to send it. */
    private fun pendingPhoto() = PhotoAnalysis(
        photoUri = "demo://photos/pending_snack.jpg",
        date = today,
        mealType = MealType.SNACK,
        status = PhotoStatus.PENDING,
        createdAt = at(today, 10, 40),
    )

    // ---------------------------------------------------------------- Workouts

    private class LiftState(var weightKg: Double, var reps: Int)

    private val schedule = mapOf(
        DayOfWeek.MONDAY to "Push Day",
        DayOfWeek.WEDNESDAY to "Pull Day",
        DayOfWeek.FRIDAY to "Leg Day",
    )

    private val startingWeightsKg = mapOf(
        "Barbell bench press" to 50.0,
        "Overhead press" to 30.0,
        "Incline dumbbell press" to 16.0,
        "Lateral raise" to 6.0,
        "Triceps pushdown" to 20.0,
        "Barbell row" to 45.0,
        "Lat pulldown" to 45.0,
        "Seated cable row" to 45.0,
        "Face pull" to 15.0,
        "Dumbbell curl" to 10.0,
        "Back squat" to 60.0,
        "Romanian deadlift" to 60.0,
        "Leg press" to 100.0,
        "Lying leg curl" to 30.0,
        "Standing calf raise" to 50.0,
    )

    private fun sessionDates(): List<Pair<LocalDate, TemplateSeed.Template>> =
        (HISTORY_DAYS - 1 downTo 1).mapNotNull { daysAgo ->
            val date = today.minusDays(daysAgo.toLong())
            schedule[date.dayOfWeek]?.let { name -> date to TemplateSeed.templates.first { it.name == name } }
        }

    private fun workouts(): List<DemoWorkout> {
        val plannedSessions = sessionDates()
        val featuredSessions = plannedSessions.count { (_, template) ->
            template.items.any { it.exerciseName == FEATURED_EXERCISE }
        }
        val states = mutableMapOf<String, LiftState>()

        return plannedSessions.map { (date, template) ->
            val templateRow = catalog.template(template.name)
            var clock = at(date, 18, random.nextInt(0, 31))
            val startedAt = clock
            val sets = mutableListOf<WorkoutSet>()

            template.items.forEachIndexed { exerciseIndex, item ->
                val exercise = catalog.exercise(item.exerciseName)
                val state = states.getOrPut(item.exerciseName) {
                    val startReps = if (item.exerciseName == FEATURED_EXERCISE) {
                        // Climb one rep per session so the latest session lands exactly on repMax.
                        max(item.repMin, item.repMax - (featuredSessions - 1))
                    } else {
                        random.nextInt(item.repMin, item.repMax + 1)
                    }
                    LiftState(startingWeightsKg.getValue(item.exerciseName), startReps)
                }
                var setNumber = 1

                if (exerciseIndex == 0) {
                    clock = clock.plus(Duration.ofMinutes(4))
                    sets += WorkoutSet(
                        sessionId = 0,
                        exerciseId = exercise.id,
                        setNumber = setNumber++,
                        weightKg = roundToIncrement(state.weightKg * 0.5, exercise.weightIncrementKg),
                        reps = 8,
                        isWarmup = true,
                        completedAt = clock,
                    )
                }

                val repsPerSet = List(item.sets) { index ->
                    val tiredLastSet = index == item.sets - 1 &&
                        item.exerciseName != FEATURED_EXERCISE &&
                        random.nextDouble() < 0.3
                    if (tiredLastSet) state.reps - 1 else state.reps
                }
                repsPerSet.forEach { reps ->
                    clock = clock.plusSeconds(item.restSeconds + 40L)
                    sets += WorkoutSet(
                        sessionId = 0,
                        exerciseId = exercise.id,
                        setNumber = setNumber++,
                        weightKg = state.weightKg,
                        reps = reps,
                        targetReps = item.repMax,
                        completedAt = clock,
                    )
                }

                // Double progression: add weight once every set reaches the top of the range.
                val lowest = repsPerSet.min()
                if (lowest >= item.repMax) {
                    state.weightKg += exercise.weightIncrementKg
                    state.reps = item.repMin
                } else if (lowest >= state.reps) {
                    state.reps = min(item.repMax, state.reps + 1)
                }
            }

            DemoWorkout(
                session = WorkoutSession(
                    templateId = templateRow.id,
                    name = template.name,
                    date = date,
                    startedAt = startedAt,
                    finishedAt = clock.plus(Duration.ofMinutes(5)),
                ),
                sets = sets,
            )
        }
    }

    // ---------------------------------------------------------------- Tips

    private fun recommendations(
        profile: UserProfile,
        weights: List<WeightEntry>,
        meals: List<MealEntry>,
        workouts: List<DemoWorkout>,
    ): List<Recommendation> {
        val createdAt = at(today, 7, 45)
        val tips = mutableListOf<Recommendation>()

        // 1. Weight trend off-goal for two weeks → small calorie cut.
        val recentAvg = weights.filter { it.date > today.minusDays(7) }.map { it.weightKg }.average()
        val earlierAvg = weights.filter { it.date in today.minusDays(20)..today.minusDays(14) }.map { it.weightKg }.average()
        val target = profile.dailyCalorieTarget
        tips += Recommendation(
            type = RecommendationType.CALORIE_ADJUSTMENT,
            title = "Lower your daily target by 150 kcal",
            message = "Your goal is to lose weight, but your weight has hardly changed for two weeks. " +
                "Try ${target - CALORIE_STEP} kcal a day instead of $target kcal.",
            reason = "Your 7-day average weight went from ${fmt(earlierAvg)} kg to ${fmt(recentAvg)} kg in 14 days. " +
                "For your goal we look for a drop of about 0.25–0.5 kg per week.",
            suggestedValue = -CALORIE_STEP.toDouble(),
            dedupeKey = "CALORIE_ADJUSTMENT:$today",
            createdAt = createdAt,
        )

        // 2. All sets at the top of the rep range → add weight.
        val featured = catalog.exercise(FEATURED_EXERCISE)
        val lastFeatured = workouts.last { workout -> workout.sets.any { it.exerciseId == featured.id && !it.isWarmup } }
        val workingSets = lastFeatured.sets.filter { it.exerciseId == featured.id && !it.isWarmup }
        val weight = workingSets.first().weightKg
        val repMax = workingSets.first().targetReps ?: workingSets.first().reps
        tips += Recommendation(
            type = RecommendationType.INCREASE_WEIGHT,
            title = "Add ${fmt(featured.weightIncrementKg)} kg to ${featured.name.lowercase()}",
            message = "Next time try ${fmt(weight + featured.weightIncrementKg)} kg. " +
                "It's fine if you get fewer reps at first.",
            reason = "On ${lastFeatured.session.date.dayOfWeek.displayName()} you did all ${workingSets.size} sets " +
                "at the top of your range: $repMax reps at ${fmt(weight)} kg.",
            suggestedValue = weight + featured.weightIncrementKg,
            relatedExerciseId = featured.id,
            dedupeKey = "INCREASE_WEIGHT:${featured.id}:${lastFeatured.session.date}",
            createdAt = createdAt,
        )

        // 3. Protein under 90 % of target over the last 7 full days → reminder.
        val lastWeek = today.minusDays(7)..today.minusDays(1)
        val avgProtein = meals.filter { it.date in lastWeek }.sumOf { it.proteinG } / 7
        val percent = (avgProtein / profile.proteinTargetG * 100).roundToInt()
        if (percent < 90) {
            tips += Recommendation(
                type = RecommendationType.PROTEIN_REMINDER,
                title = "Protein is running low",
                message = "Aim for about ${profile.proteinTargetG} g a day. " +
                    "A pot of Greek yogurt or a protein shake adds 20–30 g.",
                reason = "Your average over the last 7 days is ${avgProtein.roundToInt()} g, about $percent% of your target.",
                suggestedValue = profile.proteinTargetG.toDouble(),
                dedupeKey = "PROTEIN_REMINDER:$today",
                createdAt = createdAt,
            )
        }
        return tips
    }

    // ---------------------------------------------------------------- Helpers

    private fun at(date: LocalDate, hour: Int, minute: Int): Instant =
        date.atTime(hour, minute).atZone(zone).toInstant()

    private fun DayOfWeek.displayName(): String = name.lowercase().replaceFirstChar { it.uppercase() }

    companion object {
        const val HISTORY_DAYS = 35
        const val MEAL_DAYS = 21
        const val PLATEAU_DAYS = 14
        const val PHOTO_MEAL_DAYS_AGO = 3L
        const val START_WEIGHT_KG = 82.4
        const val WEEKLY_LOSS_KG = 0.42
        const val HEIGHT_CM = 178.0
        const val CALORIE_STEP = 150
        const val FEATURED_EXERCISE = "Barbell bench press"

        private fun round1(value: Double): Double = (value * 10).roundToInt() / 10.0

        private fun fmt(value: Double): String =
            if (value % 1.0 == 0.0) value.toInt().toString() else "%.1f".format(java.util.Locale.US, value)

        private fun roundToIncrement(value: Double, increment: Double): Double =
            if (increment <= 0.0) value else (value / increment).roundToInt() * increment
    }
}
