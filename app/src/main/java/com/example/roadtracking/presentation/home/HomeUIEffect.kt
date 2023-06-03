package com.example.roadtracking.presentation.home

import com.example.roadtracking.base.Effect
import java.io.File

sealed class HomeUIEffect : Effect {
    object ShowDatePicker : HomeUIEffect()
    data class ShowToast(val message: String) : HomeUIEffect()
    data class SendMonth(val pdfFile: File) : HomeUIEffect()
    object ShowMonthPicker : HomeUIEffect()
}