package com.example.roadtracking.data.repository

import android.util.Log
import com.example.roadtracking.data.model.RoadUI
import com.example.roadtracking.domain.repository.RoadRepository
import com.example.roadtracking.domain.source.local.LocalDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class RoadRepositoryImpl(private val localData: LocalDataSource) : RoadRepository {
    override suspend fun setRoad(road: RoadUI) {
        localData.addRoad(road)
    }

    override fun getRoad(): Flow<List<RoadUI>> = callbackFlow {
        localData.getRoads().collect {
            trySend(it)
        }
        awaitClose { channel.close() }
    }

    override fun searchCompany(company: String): Flow<List<String>> = callbackFlow {
        trySend(localData.searchCompanyName(company))
        awaitClose { channel.close() }
    }

    override fun sendMonth(month: Int): Flow<List<RoadUI>> {
        Log.e("RoadRepositoryImpl", "sendMonth: $month")
        return callbackFlow {
            trySend(localData.sendMonth(month))
            awaitClose { channel.close() }
        }
    }

    override suspend fun deleteRoadItem(roadUI: RoadUI) {
        localData.deleteRoadItem(roadUI)
    }

}


