package com.example.liftfuel.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.liftfuel.data.local.entity.Exercise
import com.example.liftfuel.data.local.entity.FavoriteMeal
import com.example.liftfuel.data.local.entity.FavoriteMealItem
import com.example.liftfuel.data.local.entity.Food
import com.example.liftfuel.data.local.entity.PhotoAnalysis
import com.example.liftfuel.data.local.entity.PhotoAnalysisItem
import com.example.liftfuel.data.local.entity.TemplateExercise
import com.example.liftfuel.data.local.entity.WorkoutSession
import com.example.liftfuel.data.local.entity.WorkoutSet
import com.example.liftfuel.data.local.entity.WorkoutTemplate

// Room fills @Relation lists in no particular order, so use the sorted helpers below in the UI.

data class TemplateExerciseWithExercise(
    @Embedded val templateExercise: TemplateExercise,
    @Relation(parentColumn = "exerciseId", entityColumn = "id")
    val exercise: Exercise,
)

data class TemplateWithExercises(
    @Embedded val template: WorkoutTemplate,
    @Relation(entity = TemplateExercise::class, parentColumn = "id", entityColumn = "templateId")
    val exercises: List<TemplateExerciseWithExercise>,
)

data class WorkoutSetWithExercise(
    @Embedded val workoutSet: WorkoutSet,
    @Relation(parentColumn = "exerciseId", entityColumn = "id")
    val exercise: Exercise,
)

data class SessionWithSets(
    @Embedded val session: WorkoutSession,
    @Relation(entity = WorkoutSet::class, parentColumn = "id", entityColumn = "sessionId")
    val sets: List<WorkoutSetWithExercise>,
)

data class FavoriteMealItemWithFood(
    @Embedded val item: FavoriteMealItem,
    @Relation(parentColumn = "foodId", entityColumn = "id")
    val food: Food,
)

data class FavoriteMealWithItems(
    @Embedded val meal: FavoriteMeal,
    @Relation(entity = FavoriteMealItem::class, parentColumn = "id", entityColumn = "favoriteMealId")
    val items: List<FavoriteMealItemWithFood>,
)

data class PhotoAnalysisWithItems(
    @Embedded val analysis: PhotoAnalysis,
    @Relation(parentColumn = "id", entityColumn = "analysisId")
    val items: List<PhotoAnalysisItem>,
)

fun TemplateWithExercises.orderedExercises(): List<TemplateExerciseWithExercise> =
    exercises.sortedBy { it.templateExercise.position }

/** Sets grouped by exercise, in the order the exercises were first trained. */
fun SessionWithSets.setsByExercise(): Map<Exercise, List<WorkoutSet>> =
    sets.sortedBy { it.workoutSet.completedAt }
        .groupBy({ it.exercise }, { it.workoutSet })
        .mapValues { (_, sets) -> sets.sortedBy { it.setNumber } }

fun FavoriteMealWithItems.orderedItems(): List<FavoriteMealItemWithFood> =
    items.sortedBy { it.item.position }

fun PhotoAnalysisWithItems.orderedItems(): List<PhotoAnalysisItem> =
    items.sortedBy { it.position }
