package com.example.roadtracking.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Company")
data class Company(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, val company: String
)