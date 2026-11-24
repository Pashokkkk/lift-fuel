package com.example.liftfuel.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.liftfuel.data.local.entity.WorkoutSession
import com.example.liftfuel.data.local.entity.WorkoutSet
import com.example.liftfuel.data.local.model.ExerciseProgressPoint
import com.example.liftfuel.data.local.model.PersonalRecord
import com.example.liftfuel.data.local.model.SessionWithSets
import com.example.liftfuel.data.local.model.WorkoutSessionSummary
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDate

/** Workout sessions and their sets, plus the progress / personal-record queries. */
@Dao
abstract class WorkoutDao {

    // ---- Sessions ----

    @Insert
    abstract suspend fun insertSession(session: WorkoutSession): Long

    @Update
    abstract suspend fun updateSession(session: WorkoutSession)

    @Query("UPDATE workout_sessions SET finishedAt = :finishedAt WHERE id = :sessionId")
    abstract suspend fun finishSession(sessionId: Long, finishedAt: Instant)

    @Delete
    abstract suspend fun deleteSession(session: WorkoutSession)

    /** The unfinished workout, if any (lets the app resume after being killed). */
    @Query("SELECT * FROM workout_sessions WHERE finishedAt IS NULL ORDER BY startedAt DESC LIMIT 1")
    abstract fun observeActiveSession(): Flow<WorkoutSession?>

    @Transaction
    @Query("SELECT * FROM workout_sessions WHERE id = :sessionId")
    abstract fun observeSessionWithSets(sessionId: Long): Flow<SessionWithSets?>

    @Query(
        """
        SELECT s.*,
               COUNT(ws.id) AS setCount,
               COUNT(DISTINCT ws.exerciseId) AS exerciseCount,
               COALESCE(SUM(ws.weightKg * ws.reps), 0) AS totalVolumeKg
        FROM workout_sessions s
        LEFT JOIN workout_sets ws ON ws.sessionId = s.id AND ws.isWarmup = 0
        WHERE s.finishedAt IS NOT NULL
        GROUP BY s.id
        ORDER BY s.startedAt DESC
        """,
    )
    abstract fun observeHistory(): Flow<List<WorkoutSessionSummary>>

    @Query("SELECT COUNT(*) FROM workout_sessions WHERE finishedAt IS NOT NULL AND date BETWEEN :from AND :to")
    abstract suspend fun countFinishedBetween(from: LocalDate, to: LocalDate): Int

    // ---- Sets ----

    @Insert
    abstract suspend fun insertSet(set: WorkoutSet): Long

    @Insert
    abstract suspend fun insertSets(sets: List<WorkoutSet>)

    @Update
    abstract suspend fun updateSet(set: WorkoutSet)

    @Delete
    abstract suspend fun deleteSet(set: WorkoutSet)

    /**
     * Working sets of [exerciseId] from the most recent finished session that included it,
     * ignoring [excludeSessionId] (the workout in progress; pass -1 to ignore nothing).
     * Shown as "last time" on the Active workout screen and used by the "add weight" rule.
     */
    @Query(
        """
        SELECT * FROM workout_sets
        WHERE exerciseId = :exerciseId AND isWarmup = 0 AND sessionId = (
            SELECT s.id FROM workout_sessions s
            INNER JOIN workout_sets ws ON ws.sessionId = s.id
            WHERE ws.exerciseId = :exerciseId
              AND s.finishedAt IS NOT NULL
              AND s.id != :excludeSessionId
            ORDER BY s.startedAt DESC
            LIMIT 1
        )
        ORDER BY setNumber ASC
        """,
    )
    abstract suspend fun getLastSessionSets(exerciseId: Long, excludeSessionId: Long): List<WorkoutSet>

    @Transaction
    open suspend fun insertSessionWithSets(session: WorkoutSession, sets: List<WorkoutSet>): Long {
        val sessionId = insertSession(session)
        insertSets(sets.map { it.copy(sessionId = sessionId) })
        return sessionId
    }

    // ---- Progress ----

    /** Strength chart: one point per finished session (Epley e1RM = weight × (1 + reps / 30)). */
    @Query(
        """
        SELECT s.id AS sessionId,
               s.date AS date,
               MAX(ws.weightKg) AS topWeightKg,
               MAX(ws.weightKg * (1 + ws.reps / 30.0)) AS bestEstimated1RmKg,
               SUM(ws.weightKg * ws.reps) AS totalVolumeKg,
               SUM(ws.reps) AS totalReps
        FROM workout_sets ws
        INNER JOIN workout_sessions s ON s.id = ws.sessionId
        WHERE ws.exerciseId = :exerciseId AND ws.isWarmup = 0 AND s.finishedAt IS NOT NULL
        GROUP BY s.id
        ORDER BY s.startedAt ASC
        """,
    )
    abstract fun observeExerciseProgress(exerciseId: Long): Flow<List<ExerciseProgressPoint>>

    @Query(
        """
        SELECT e.id AS exerciseId,
               e.name AS exerciseName,
               MAX(ws.weightKg) AS maxWeightKg,
               MAX(ws.weightKg * (1 + ws.reps / 30.0)) AS bestEstimated1RmKg,
               COUNT(DISTINCT ws.sessionId) AS sessionCount
        FROM workout_sets ws
        INNER JOIN exercises e ON e.id = ws.exerciseId
        INNER JOIN workout_sessions s ON s.id = ws.sessionId
        WHERE ws.isWarmup = 0 AND s.finishedAt IS NOT NULL
        GROUP BY e.id
        ORDER BY e.name COLLATE NOCASE ASC
        """,
    )
    abstract fun observePersonalRecords(): Flow<List<PersonalRecord>>
}
