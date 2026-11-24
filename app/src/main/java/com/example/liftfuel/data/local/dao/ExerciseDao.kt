package com.example.liftfuel.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.liftfuel.data.local.entity.Exercise
import com.example.liftfuel.data.local.entity.MuscleGroup
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercises ORDER BY name COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE primaryMuscle = :muscle ORDER BY name COLLATE NOCASE ASC")
    fun observeByMuscle(muscle: MuscleGroup): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises ORDER BY name COLLATE NOCASE ASC")
    suspend fun getAll(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getById(id: Long): Exercise?

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun count(): Int

    @Insert
    suspend fun insert(exercise: Exercise): Long

    @Insert
    suspend fun insertAll(exercises: List<Exercise>): List<Long>

    @Update
    suspend fun update(exercise: Exercise)
}
