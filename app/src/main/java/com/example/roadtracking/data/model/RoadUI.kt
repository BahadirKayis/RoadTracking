package com.example.roadtracking.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "RoadTracking")
data class RoadUI(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, val dateInMillis: Long, val company: String
)
