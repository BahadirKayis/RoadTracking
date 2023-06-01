package com.example.roadtracking.di

import android.content.Context
import androidx.room.Room
import com.example.roadtracking.data.source.local.RoadTrackingDao
import com.example.roadtracking.data.source.local.RoadTrackingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoadRoomDB(@ApplicationContext appContext: Context): RoadTrackingDatabase =
        Room.databaseBuilder(
            appContext, RoadTrackingDatabase::class.java, "recipeDatabase.db"
        ).build()

    @Provides
    @Singleton
    fun provideRoadDAO(roadDao: RoadTrackingDatabase): RoadTrackingDao = roadDao.roadDao()
}