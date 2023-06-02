package com.example.roadtracking.data.source.local

import android.util.Log
import com.example.roadtracking.data.model.Company
import com.example.roadtracking.data.model.RoadUI
import com.example.roadtracking.domain.source.local.LocalDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Calendar

class LocalDataSourceImpl(private val roadDao: RoadTrackingDao) : LocalDataSource {
    override suspend fun addRoad(road: RoadUI) {
        roadDao.addRoad(road)
        addCompany(road.company)
    }

    override fun getRoads(): Flow<List<RoadUI>> = callbackFlow {
        val (startOfMonth, endOfMonth) = getMonthRange()
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

    override suspend fun sendMonth(month: Int): List<RoadUI> {
        val (startOfMonth, endOfMonth) = getMonthRange(month)
        return roadDao.getRecordsForCurrentMonth(startOfMonth, endOfMonth)
            .sortedBy { it.dateInMillis }
    }

    override suspend fun deleteRoadItem(roadUI: RoadUI) {
        roadDao.deleteRoadItem(roadUI)
    }

    private fun getMonthRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH, 1) // Ayın ilk gününe ayarlı
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfMonth = calendar.timeInMillis

        calendar.set(
            Calendar.DAY_OF_MONTH,
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        ) // Ayın son gününe ayarlı
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfMonth = calendar.timeInMillis

        return Pair(startOfMonth, endOfMonth)
    }

    private fun getMonthRange(month: Int): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfMonth = calendar.timeInMillis

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfMonth = calendar.timeInMillis

        return Pair(startOfMonth, endOfMonth)
    }


}