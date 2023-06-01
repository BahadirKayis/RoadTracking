package com.example.roadtracking.domain.source.local

import com.example.roadtracking.data.model.RoadUI
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun addRoad(road: RoadUI)
    fun getRoads(): Flow<List<RoadUI>>
    suspend fun searchCompany(company: String): List<String>
}