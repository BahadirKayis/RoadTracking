package com.example.roadtracking.domain.repository

import com.example.roadtracking.data.model.RoadUI
import kotlinx.coroutines.flow.Flow


interface RoadRepository {
    suspend fun setRoad(road: RoadUI)
    fun getRoad(): Flow<List<RoadUI>>
    fun searchCompany(company: String): Flow<List<String>>

    fun sendMonth(month: Int): Flow<List<RoadUI>>
    suspend fun deleteRoadItem(roadUI: RoadUI)
}