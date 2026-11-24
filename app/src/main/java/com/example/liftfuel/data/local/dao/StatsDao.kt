package com.example.liftfuel.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.liftfuel.data.local.model.TableCounts
import kotlinx.coroutines.flow.Flow

@Dao
interface StatsDao {

    @Query(
        """
        SELECT (SELECT COUNT(*) FROM foods) AS foods,
               (SELECT COUNT(*) FROM exercises) AS exercises,
               (SELECT COUNT(*) FROM workout_templates) AS templates,
               (SELECT COUNT(*) FROM weight_entries) AS weightEntries,
               (SELECT COUNT(*) FROM meal_entries) AS mealEntries,
               (SELECT COUNT(*) FROM favorite_meals) AS favoriteMeals,
               (SELECT COUNT(*) FROM photo_analyses) AS photoAnalyses,
               (SELECT COUNT(*) FROM workout_sessions) AS workoutSessions,
               (SELECT COUNT(*) FROM workout_sets) AS workoutSets,
               (SELECT COUNT(*) FROM recommendations) AS recommendations,
               (SELECT COUNT(*) FROM reminders) AS reminders
        """,
    )
    fun observeCounts(): Flow<TableCounts>
}
