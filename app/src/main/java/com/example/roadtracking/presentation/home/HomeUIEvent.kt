package com.example.roadtracking.presentation.home

import com.example.roadtracking.base.Event
import com.example.roadtracking.data.model.RoadUI

sealed class HomeUIEvent : Event {
    data class SaveData(val date: String, val company: String) : HomeUIEvent()
    data class ResultData(val date: String) : HomeUIEvent()
    data class SearchCompany(val company: String) : HomeUIEvent()
    object ShowDatePicker : HomeUIEvent()
    object SelectMonth : HomeUIEvent()
    data class DeleteRoadItem(val roadUI: RoadUI) : HomeUIEvent()
    data class SelectedMonth(val mont: Int) : HomeUIEvent()
}