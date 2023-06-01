package com.example.roadtracking.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectDatePicker(val day: Int, val month: Int, val year: Int) : Parcelable