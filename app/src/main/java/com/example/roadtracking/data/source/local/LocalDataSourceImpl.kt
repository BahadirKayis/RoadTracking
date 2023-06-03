package com.example.roadtracking.data.source.local

import android.util.Log
import com.example.roadtracking.data.model.Company
import com.example.roadtracking.data.model.RoadUI
import com.example.roadtracking.domain.source.local.LocalDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocalDataSourceImpl(private val roadDao: RoadTrackingDao) : LocalDataSource {
    override suspend fun addRoad(road: RoadUI) {
        roadDao.addRoad(road)
        addCompany(road.company)
    }

    override fun getRoads(): Flow<List<RoadUI>> = callbackFlow {
        roadDao.getRoad().collect {
            trySend(it.sortedBy { it.dateInMillis }.reversed())
        }
        awaitClose { channel.close() }
    }

    override suspend fun searchCompany(company: String): List<String> {
        val road = roadDao.searchCompany(company)
        return road.map { it.company }
    }

    override suspend fun addCompany(company: String) {
        getCompany(company)?.let {
            Log.e("LocalDataSourceImpl", "this company is registered")
        } ?: run {
            roadDao.addCompany(Company(company = company))
        }
    }

    override suspend fun getCompany(companyName: String): Company? {
        return roadDao.getCompany(companyName)
    }

    override suspend fun searchCompanyName(companyName: String): List<String> {
        val company = roadDao.searchCompanyName(companyName)
        return company.map { it.company }
    }

    override suspend fun sendMonth(startOfMonth:Long, endOfMonth:Long): List<RoadUI> {
        return roadDao.getRecordsForCurrentMonth(startOfMonth, endOfMonth)
            .sortedBy { it.dateInMillis }
    }

    override suspend fun deleteRoadItem(roadUI: RoadUI) {
        roadDao.deleteRoadItem(roadUI)
    }
}