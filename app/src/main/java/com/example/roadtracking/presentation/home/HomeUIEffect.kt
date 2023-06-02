package com.example.roadtracking.presentation.home

import com.example.roadtracking.base.Effect
import com.example.roadtracking.data.model.RoadUI

sealed class HomeUIEffect : Effect {
    object ShowDatePicker : HomeUIEffect()
    data class ShowToast(val message: String) : HomeUIEffect()
    data class SendMonth(val month: List<RoadUI>) : HomeUIEffect()
    object ShowMonthPicker : HomeUIEffect()

}