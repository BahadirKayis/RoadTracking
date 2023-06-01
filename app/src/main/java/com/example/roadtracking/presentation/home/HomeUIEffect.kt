package com.example.roadtracking.presentation.home

import com.example.roadtracking.base.Effect

sealed class HomeUIEffect : Effect {
    object ShowDatePicker : HomeUIEffect()
    data class ShowToast(val message: String) : HomeUIEffect()

}