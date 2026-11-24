package com.example.liftfuel.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.liftfuel.data.local.entity.Recommendation
import com.example.liftfuel.data.local.entity.RecommendationStatus
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface RecommendationDao {

    @Query("SELECT * FROM recommendations WHERE status = 'ACTIVE' ORDER BY createdAt DESC")
    fun observeActive(): Flow<List<Recommendation>>

    /** Returns -1 when a tip with the same dedupeKey already exists. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfNew(recommendation: Recommendation): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllIfNew(recommendations: List<Recommendation>): List<Long>

    @Query("UPDATE recommendations SET status = :status, resolvedAt = :resolvedAt WHERE id = :id")
    suspend fun setStatus(id: Long, status: RecommendationStatus, resolvedAt: Instant)
}
