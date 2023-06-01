package com.example.roadtracking.data.source.local

import android.util.Log
import com.example.roadtracking.data.model.RoadUI
import com.example.roadtracking.domain.source.local.LocalDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocalDataSourceImpl(private val roadDao: RoadTrackingDao) : LocalDataSource {
    override suspend fun addRoad(road: RoadUI) = roadDao.addRoad(road)

    override fun getRoads(): Flow<List<RoadUI>> = callbackFlow {
        roadDao.getRoad().collect {
            trySend(it)
        }
        awaitClose { channel.close() }
    }

    override suspend fun searchCompany(company: String): List<String> {
        Log.e("RoadRepositoryImpl", "searchCompany: $company")
        val road = roadDao.searchCompany(company)

        Log.e("RoadRepositoryImpl", "searchCompany: $road")

        return road.map { it.company }
    }


}