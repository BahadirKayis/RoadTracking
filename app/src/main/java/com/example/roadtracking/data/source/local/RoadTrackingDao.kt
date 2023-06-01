package com.example.roadtracking.data.source.local

import androidx.lifecycle.viewmodel.R
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.roadtracking.data.model.RoadUI
import kotlinx.coroutines.flow.Flow

@Dao
interface RoadTrackingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRoad(road: RoadUI)

    @Query("SELECT * FROM RoadTracking")
    fun getRoad(): Flow<List<RoadUI>>

    @Query("SELECT * FROM RoadTracking WHERE company LIKE '%' || :company || '%'")
    suspend fun searchCompany(company: String): List<RoadUI>

}