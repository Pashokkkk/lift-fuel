package com.example.liftfuel.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.liftfuel.data.local.entity.TemplateExercise
import com.example.liftfuel.data.local.entity.WorkoutTemplate
import com.example.liftfuel.data.local.model.TemplateWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WorkoutTemplateDao {

    @Transaction
    @Query("SELECT * FROM workout_templates ORDER BY sortOrder ASC, name COLLATE NOCASE ASC")
    abstract fun observeAllWithExercises(): Flow<List<TemplateWithExercises>>

    @Transaction
    @Query("SELECT * FROM workout_templates WHERE id = :id")
    abstract suspend fun getWithExercises(id: Long): TemplateWithExercises?

    @Query("SELECT * FROM workout_templates ORDER BY sortOrder ASC")
    abstract suspend fun getAll(): List<WorkoutTemplate>

    @Insert
    abstract suspend fun insertTemplate(template: WorkoutTemplate): Long

    @Insert
    abstract suspend fun insertTemplateExercises(items: List<TemplateExercise>)

    /** Saves a template with its exercises; templateId and position are filled in here. */
    @Transaction
    open suspend fun insertWithExercises(template: WorkoutTemplate, items: List<TemplateExercise>): Long {
        val templateId = insertTemplate(template)
        insertTemplateExercises(items.mapIndexed { index, item -> item.copy(templateId = templateId, position = index) })
        return templateId
    }

    @Update
    abstract suspend fun updateTemplate(template: WorkoutTemplate)

    @Delete
    abstract suspend fun deleteTemplate(template: WorkoutTemplate)
}
