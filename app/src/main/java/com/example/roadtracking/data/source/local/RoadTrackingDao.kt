package com.example.roadtracking.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.roadtracking.data.model.Company
import com.example.roadtracking.data.model.RoadUI
import kotlinx.coroutines.flow.Flow

@Dao
interface RoadTrackingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRoad(road: RoadUI)

    @Delete
    suspend fun deleteRoadItem(roadUI: RoadUI)

    @Query("SELECT * FROM RoadTracking")
    fun getRoad(): Flow<List<RoadUI>>

    @Query("SELECT * FROM RoadTracking WHERE company LIKE '%' || :company || '%'")
    suspend fun searchCompany(company: String): List<RoadUI>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCompany(company: Company)

    @Query("SELECT * FROM Company WHERE company = :companyName")
    suspend fun getCompany(companyName: String): Company?

    @Query("SELECT * FROM Company WHERE company LIKE '%' || :companyName || '%'")
    suspend fun searchCompanyName(companyName: String): List<Company>

    @Query("SELECT * FROM RoadTracking WHERE dateInMillis >= :startOfMonth AND dateInMillis <= :endOfMonth")
    suspend fun getRecordsForCurrentMonth(startOfMonth: Long, endOfMonth: Long): List<RoadUI>
}
