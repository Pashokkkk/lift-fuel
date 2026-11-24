package com.example.liftfuel.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.liftfuel.data.local.entity.PhotoAnalysis
import com.example.liftfuel.data.local.entity.PhotoAnalysisItem
import com.example.liftfuel.data.local.entity.PhotoStatus
import com.example.liftfuel.data.local.model.PhotoAnalysisWithItems
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PhotoAnalysisDao {

    @Insert
    abstract suspend fun insert(analysis: PhotoAnalysis): Long

    @Update
    abstract suspend fun update(analysis: PhotoAnalysis)

    @Query("SELECT * FROM photo_analyses WHERE id = :id")
    abstract suspend fun getById(id: Long): PhotoAnalysis?

    /** Review meal screen. */
    @Transaction
    @Query("SELECT * FROM photo_analyses WHERE id = :id")
    abstract fun observeWithItems(id: Long): Flow<PhotoAnalysisWithItems?>

    /** Used by the upload worker, e.g. getByStatus(PENDING) and getByStatus(FAILED). */
    @Query("SELECT * FROM photo_analyses WHERE status = :status ORDER BY createdAt ASC")
    abstract suspend fun getByStatus(status: PhotoStatus): List<PhotoAnalysis>

    /** Photos that still need attention (waiting, failed or ready to review). */
    @Query("SELECT * FROM photo_analyses WHERE status != 'SAVED' ORDER BY createdAt DESC")
    abstract fun observeUnsaved(): Flow<List<PhotoAnalysis>>

    @Query("UPDATE photo_analyses SET status = :status WHERE id = :id")
    abstract suspend fun setStatus(id: Long, status: PhotoStatus)

    @Insert
    abstract suspend fun insertItems(items: List<PhotoAnalysisItem>): List<Long>

    @Update
    abstract suspend fun updateItem(item: PhotoAnalysisItem)

    @Delete
    abstract suspend fun deleteItem(item: PhotoAnalysisItem)

    @Query("DELETE FROM photo_analysis_items WHERE analysisId = :analysisId")
    abstract suspend fun deleteItemsFor(analysisId: Long)

    @Transaction
    open suspend fun insertWithItems(analysis: PhotoAnalysis, items: List<PhotoAnalysisItem>): Long {
        val analysisId = insert(analysis)
        insertItems(items.mapIndexed { index, item -> item.copy(analysisId = analysisId, position = index) })
        return analysisId
    }

    /** Stores a fresh AI result for an existing photo (e.g. after a retry). */
    @Transaction
    open suspend fun replaceItems(analysisId: Long, items: List<PhotoAnalysisItem>) {
        deleteItemsFor(analysisId)
        insertItems(items.mapIndexed { index, item -> item.copy(analysisId = analysisId, position = index) })
    }
}
