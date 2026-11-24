package com.example.liftfuel.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.liftfuel.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface UserProfileDao {

    /** Emits null until onboarding has created the profile. */
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun observe(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun get(): UserProfile?

    @Upsert
    suspend fun upsert(profile: UserProfile)

    @Query("UPDATE user_profile SET calorieAdjustment = :adjustment, updatedAt = :updatedAt WHERE id = 1")
    suspend fun setCalorieAdjustment(adjustment: Int, updatedAt: Instant)
}
