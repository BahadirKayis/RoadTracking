package com.example.roadtracking.di

import com.example.roadtracking.data.source.local.LocalDataSourceImpl
import com.example.roadtracking.data.source.local.RoadTrackingDao
import com.example.roadtracking.domain.source.local.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideLocalDataSource(localService: RoadTrackingDao): LocalDataSource =
        LocalDataSourceImpl(localService)
}