package com.example.roadtracking.domain.source.local

import com.example.roadtracking.data.model.Company
import com.example.roadtracking.data.model.RoadUI
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun addRoad(road: RoadUI)
    fun getRoads(): Flow<List<RoadUI>>
    suspend fun searchCompany(company: String): List<String>
    suspend fun addCompany(company: String)
    suspend fun getCompany(companyName: String): Company?
    suspend fun searchCompanyName(companyName: String): List<String>

    suspend fun sendMonth(month: Int): List<RoadUI>
    suspend fun deleteRoadItem(roadUI: RoadUI)
}