package com.example.roadtracking.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.roadtracking.data.model.RoadUI


@Database(
    entities = [RoadUI::class],
    version = 1,
    exportSchema = false
)

abstract class RoadTrackingDatabase : RoomDatabase() {
    abstract fun roadDao(): RoadTrackingDao
}