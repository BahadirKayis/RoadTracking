package com.example.roadtracking.di

import com.example.roadtracking.data.repository.RoadRepositoryImpl
import com.example.roadtracking.domain.repository.RoadRepository
import com.example.roadtracking.domain.source.local.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRoadRepository(local: LocalDataSource): RoadRepository = RoadRepositoryImpl(local)
}