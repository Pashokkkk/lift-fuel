package com.example.liftfuel.data.seed

import androidx.room.withTransaction
import com.example.liftfuel.data.local.LiftFuelDatabase
import com.example.liftfuel.data.local.entity.TemplateExercise
import com.example.liftfuel.data.local.entity.WorkoutTemplate
import java.time.Clock
import java.time.LocalDate

/**
 * Fills an empty database:
 * - the catalog (foods, exercises, templates, default reminders) on every install;
 * - the demo user's history only when [seedIfEmpty] is called with includeDemoData = true
 *   (debug builds, see BuildConfig.SEED_DEMO_DATA).
 */
class DatabaseSeeder(
    private val db: LiftFuelDatabase,
    private val clock: Clock = Clock.systemDefaultZone(),
) {

    suspend fun seedIfEmpty(includeDemoData: Boolean) {
        db.withTransaction {
            if (db.foodDao().count() == 0) seedCatalog()
            if (includeDemoData && db.userProfileDao().get() == null) seedDemoHistory()
        }
    }

    private suspend fun seedCatalog() {
        val now = clock.instant()
        db.foodDao().insertAll(FoodSeed.foods(createdAt = now))
        db.exerciseDao().insertAll(ExerciseSeed.exercises())

        val exerciseIds = db.exerciseDao().getAll().associate { it.name to it.id }
        TemplateSeed.templates.forEachIndexed { index, template ->
            db.workoutTemplateDao().insertWithExercises(
                template = WorkoutTemplate(
                    name = template.name,
                    description = template.description,
                    sortOrder = index,
                    isBuiltIn = true,
                    createdAt = now,
                ),
                items = template.items.map { item ->
                    TemplateExercise(
                        templateId = 0,
                        exerciseId = exerciseIds[item.exerciseName]
                            ?: error("Template \"${template.name}\" uses unknown exercise \"${item.exerciseName}\""),
                        position = 0,
                        targetSets = item.sets,
                        repRangeMin = item.repMin,
                        repRangeMax = item.repMax,
                        restSeconds = item.restSeconds,
                    )
                },
            )
        }
        db.reminderDao().insertAll(ReminderSeed.defaults())
    }

    private suspend fun seedDemoHistory() {
        val catalog = CatalogLookup(
            foods = db.foodDao().getAll(),
            exercises = db.exerciseDao().getAll(),
            templates = db.workoutTemplateDao().getAll(),
        )
        val history = DemoDataGenerator(
            today = LocalDate.now(clock),
            zone = clock.zone,
            catalog = catalog,
        ).generate()

        db.userProfileDao().upsert(history.profile)
        db.weightDao().insertAll(history.weights)
        db.mealEntryDao().insertAll(history.meals)

        history.photoMeals.forEach { photo ->
            val analysisId = db.photoAnalysisDao().insertWithItems(photo.analysis, photo.items)
            db.mealEntryDao().insertAll(photo.entries.map { it.copy(photoAnalysisId = analysisId) })
        }
        history.pendingPhotos.forEach { db.photoAnalysisDao().insert(it) }

        history.favorites.forEach { favorite ->
            db.favoriteMealDao().insertWithItems(favorite.meal, favorite.items)
        }
        history.workouts.forEach { workout ->
            db.workoutDao().insertSessionWithSets(workout.session, workout.sets)
        }
        db.recommendationDao().insertAllIfNew(history.recommendations)

        // "Recent foods" on Add food = foods the demo user actually logged.
        (history.meals + history.photoMeals.flatMap { it.entries })
            .filter { it.foodId != null }
            .groupBy { it.foodId!! }
            .forEach { (foodId, entries) -> db.foodDao().markUsed(foodId, entries.maxOf { it.loggedAt }) }

        // The demo user has reminders switched on.
        db.reminderDao().setAllEnabled(true)
    }
}
